package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.catalog.CatalogFiltersResponse;
import br.com.george.commerce.dto.catalog.CatalogNotesResponse;
import br.com.george.commerce.dto.catalog.CatalogPageResponse;
import br.com.george.commerce.dto.catalog.CatalogProductResponse;
import br.com.george.commerce.dto.catalog.CatalogSearchRequest;
import br.com.george.commerce.dto.catalog.CatalogVariantResponse;
import br.com.george.commerce.entity.Product;
import br.com.george.commerce.entity.ProductNote;
import br.com.george.commerce.entity.ProductPromotion;
import br.com.george.commerce.entity.ProductVariant;
import br.com.george.commerce.entity.Promotion;
import br.com.george.commerce.enums.DiscountType;
import br.com.george.commerce.exception.CatalogProductNotFoundException;
import br.com.george.commerce.repository.ProductPromotionRepository;
import br.com.george.commerce.repository.ProductRepository;
import br.com.george.commerce.service.CatalogService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Collator;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Vitrine da loja. Filtro, busca, ordem e página são feitos AQUI (o front só mostra).
 *
 * <p>Dívida técnica (registrada): a filtragem roda em memória sobre os produtos publicados —
 * simples e certa para o catálogo do MVP (dezenas de produtos). Passando de ~1.000 produtos,
 * levar filtros e ordem para o SQL. O preço promocional vem da promoção ligada ao produto
 * (vale para todos os tamanhos); promoção por SKU, categoria, marca ou formato entra com a
 * tela de Promoções do backoffice.
 */
@Service
@RequiredArgsConstructor
public class CatalogServiceImpl implements CatalogService {

    static final String PUBLISHED = "publicado";
    static final int LAUNCH_COUNT = 10;
    static final int BEST_SELLER_COUNT = 10;
    static final int DEFAULT_PAGE_SIZE = 24;

    private static final List<String> OCCASIONS = List.of("dia", "noite");
    private static final List<String> GENDERS = List.of("Masculino", "Feminino", "Unissex");
    private static final Collator PT_BR = Collator.getInstance(Locale.of("pt", "BR"));

    private final ProductRepository productRepository;
    private final ProductPromotionRepository productPromotionRepository;

    @Override
    @Transactional(readOnly = true)
    public CatalogPageResponse search(CatalogSearchRequest request) {
        List<CatalogProductResponse> found = showcase().stream()
                .filter(matches(request))
                .sorted(order(request.sort()))
                .toList();
        int size = request.size() == null ? DEFAULT_PAGE_SIZE : request.size();
        int page = request.page() == null ? 0 : request.page();
        int from = Math.min(page * size, found.size());
        int to = Math.min(from + size, found.size());
        return CatalogPageResponse.builder()
                .items(found.subList(from, to))
                .page(page)
                .size(size)
                .totalItems(found.size())
                .totalPages((found.size() + size - 1) / size)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public CatalogProductResponse findBySlug(String slug) {
        // Lançamento e mais vendido dependem dos outros produtos: calcula sobre a vitrine inteira.
        return showcase().stream()
                .filter(product -> product.slug().equals(slug))
                .findFirst()
                .orElseThrow(CatalogProductNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public CatalogFiltersResponse filters() {
        List<CatalogProductResponse> showcase = showcase();
        List<BigDecimal> prices = showcase.stream()
                .flatMap(product -> product.variants().stream())
                .map(CatalogServiceImpl::effectivePrice)
                .toList();
        Set<String> occasions = showcase.stream().flatMap(p -> p.occasions().stream()).collect(Collectors.toSet());
        Set<String> genders = showcase.stream().map(CatalogProductResponse::gender).collect(Collectors.toSet());
        return CatalogFiltersResponse.builder()
                .categories(showcase.stream().map(CatalogProductResponse::category).filter(Objects::nonNull)
                        .distinct().toList())
                .brands(sortedText(showcase.stream().map(CatalogProductResponse::brandName)))
                .families(sortedText(showcase.stream().flatMap(p -> p.families().stream())))
                .occasions(OCCASIONS.stream().filter(occasions::contains).toList())
                .genders(GENDERS.stream().filter(genders::contains).toList())
                .badges(sortedText(showcase.stream().map(CatalogProductResponse::badge)))
                .minPrice(prices.stream().min(Comparator.naturalOrder()).orElse(null))
                .maxPrice(prices.stream().max(Comparator.naturalOrder()).orElse(null))
                .build();
    }

    /** Produtos publicados, com lançamento, mais vendido e preço promocional já calculados. */
    private List<CatalogProductResponse> showcase() {
        List<Product> published = productRepository.findByStatus(PUBLISHED);
        Map<Long, Promotion> promotions = activePromotions();
        Set<Long> launches = published.stream()
                .sorted(Comparator.comparing(Product::getCreatedAt, Comparator.nullsLast(Comparator.reverseOrder()))
                        .thenComparing(Product::getId, Comparator.reverseOrder()))
                .limit(LAUNCH_COUNT)
                .map(Product::getId)
                .collect(Collectors.toSet());
        Set<Long> bestSellers = published.stream()
                .filter(product -> soldCount(product) > 0)
                .sorted(Comparator.comparing(CatalogServiceImpl::soldCount).reversed())
                .limit(BEST_SELLER_COUNT)
                .map(Product::getId)
                .collect(Collectors.toSet());
        return published.stream()
                .map(product -> toResponse(product, promotions.get(product.getId()),
                        launches.contains(product.getId()), bestSellers.contains(product.getId())))
                .toList();
    }

    private Map<Long, Promotion> activePromotions() {
        LocalDateTime now = LocalDateTime.now();
        Map<Long, Promotion> byProduct = new HashMap<>();
        for (ProductPromotion link : productPromotionRepository.findAll()) {
            Promotion promotion = link.getPromotion();
            if (link.getProduct() != null && isActive(promotion, now)) {
                byProduct.putIfAbsent(link.getProduct().getId(), promotion);
            }
        }
        return byProduct;
    }

    private static boolean isActive(Promotion promotion, LocalDateTime now) {
        return promotion != null
                && Boolean.TRUE.equals(promotion.getActive())
                && (promotion.getStartAt() == null || !now.isBefore(promotion.getStartAt()))
                && (promotion.getEndAt() == null || !now.isAfter(promotion.getEndAt()));
    }

    private static CatalogProductResponse toResponse(Product product, Promotion promotion, boolean launch,
            boolean bestSeller) {
        return CatalogProductResponse.builder()
                .id(product.getId())
                .slug(product.getSlug())
                .line(product.getLine())
                .subtitle(product.getSubtitle())
                .name(product.displayName())
                .description(product.getDescription())
                .brandId(product.getBrand() == null ? null : product.getBrand().getId())
                .brandName(product.getBrand() == null ? null : product.getBrand().getName())
                .category(product.getCategory() == null ? null : product.getCategory().getSlug())
                .gender(product.getGender())
                .concentration(product.getConcentration())
                .families(List.copyOf(product.getFamilies()))
                .notes(notes(product.getNotes()))
                .kitItems(List.copyOf(product.getKitItems()))
                .occasions(List.copyOf(product.getOccasions()))
                .launch(launch)
                .bestSeller(bestSeller)
                .badge(product.getBadge())
                .soldCount(soldCount(product))
                .images(List.copyOf(product.getImages()))
                .variants(product.getVariants().stream().map(variant -> toResponse(variant, promotion)).toList())
                .build();
    }

    private static CatalogVariantResponse toResponse(ProductVariant variant, Promotion promotion) {
        return CatalogVariantResponse.builder()
                .id(variant.getSku())
                .kind(variant.getKind())
                .volumeMl(variant.getVolumeMl())
                .price(variant.getPrice())
                .promoPrice(promoPrice(variant.getPrice(), promotion))
                .stock(variant.getStock())
                .build();
    }

    /** Preço com a promoção; {@code null} quando não há promoção ou ela não baixa o preço. */
    private static BigDecimal promoPrice(BigDecimal price, Promotion promotion) {
        if (promotion == null || promotion.getDiscountValue() == null || price == null) {
            return null;
        }
        BigDecimal discount = promotion.getDiscountType() == DiscountType.PERCENTAGE
                ? price.multiply(promotion.getDiscountValue()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                : promotion.getDiscountValue();
        BigDecimal result = price.subtract(discount).max(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP);
        return result.compareTo(price) < 0 ? result : null;
    }

    /** Sem notas, a página não mostra a pirâmide: devolve {@code null}. */
    private static CatalogNotesResponse notes(List<ProductNote> notes) {
        if (notes == null || notes.isEmpty()) {
            return null;
        }
        return CatalogNotesResponse.builder()
                .top(layer(notes, "top"))
                .heart(layer(notes, "heart"))
                .base(layer(notes, "base"))
                .build();
    }

    private static List<String> layer(List<ProductNote> notes, String layer) {
        return notes.stream().filter(note -> layer.equals(note.getLayer())).map(ProductNote::getNote).toList();
    }

    private static int soldCount(Product product) {
        return product.getSoldCount() == null ? 0 : product.getSoldCount();
    }

    // ---- Filtros ----

    private static Predicate<CatalogProductResponse> matches(CatalogSearchRequest request) {
        String search = normalize(request.q());
        boolean anyHighlight = isTrue(request.deal()) || isTrue(request.launch()) || isTrue(request.bestSeller())
                || !isEmpty(request.badge());
        return product -> (isEmpty(request.category()) || request.category().contains(product.category()))
                && (isEmpty(request.family()) || product.families().stream().anyMatch(request.family()::contains))
                && (isEmpty(request.occasion()) || product.occasions().stream().anyMatch(request.occasion()::contains))
                && (request.gender() == null || request.gender().equalsIgnoreCase(product.gender()))
                && (request.brand() == null || request.brand().equalsIgnoreCase(product.brandName()))
                && (!isTrue(request.inStock()) || product.variants().stream().anyMatch(v -> v.stock() > 0))
                && (request.kind() == null || product.variants().stream().anyMatch(v -> request.kind().equals(v.kind())))
                && inPriceRange(product, request.minPrice(), request.maxPrice())
                && (!anyHighlight || highlighted(product, request))
                && (search.isEmpty() || searchText(product).contains(search));
    }

    /** O produto entra se ALGUM tamanho estiver na faixa (preço com promoção). */
    private static boolean inPriceRange(CatalogProductResponse product, BigDecimal min, BigDecimal max) {
        if (min == null && max == null) {
            return true;
        }
        return product.variants().stream()
                .map(CatalogServiceImpl::effectivePrice)
                .anyMatch(price -> (min == null || price.compareTo(min) >= 0)
                        && (max == null || price.compareTo(max) <= 0));
    }

    /** Destaques valem como "ou": Lançamentos + Ofertas mostra os dois. */
    private static boolean highlighted(CatalogProductResponse product, CatalogSearchRequest request) {
        return (isTrue(request.deal()) && product.variants().stream().anyMatch(v -> v.promoPrice() != null))
                || (isTrue(request.launch()) && isTrue(product.launch()))
                || (isTrue(request.bestSeller()) && isTrue(product.bestSeller()))
                || (!isEmpty(request.badge()) && product.badge() != null && request.badge().contains(product.badge()));
    }

    private static String searchText(CatalogProductResponse product) {
        return normalize(String.join(" ", Stream.concat(
                        Stream.of(product.name(), product.brandName()), product.families().stream())
                .filter(Objects::nonNull)
                .toList()));
    }

    /** Minúsculas e sem acento: "Cítrico" acha com "citrico". */
    private static String normalize(String text) {
        if (text == null) {
            return "";
        }
        return Normalizer.normalize(text.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }

    // ---- Ordem ----

    private static Comparator<CatalogProductResponse> order(String sort) {
        Comparator<CatalogProductResponse> newest = Comparator.comparing(CatalogProductResponse::id).reversed();
        Comparator<CatalogProductResponse> bestSellers =
                Comparator.comparing(CatalogProductResponse::soldCount).reversed().thenComparing(newest);
        Comparator<CatalogProductResponse> byPrice = Comparator.comparing(CatalogServiceImpl::lowestPrice);
        return switch (sort == null ? "relevance" : sort) {
            case "price_asc" -> byPrice.thenComparing(CatalogProductResponse::name, PT_BR);
            case "price_desc" -> byPrice.reversed().thenComparing(CatalogProductResponse::name, PT_BR);
            case "newest" -> newest;
            // relevance e best_sellers: mais vendidos primeiro, depois os mais novos.
            default -> bestSellers;
        };
    }

    private static BigDecimal lowestPrice(CatalogProductResponse product) {
        return product.variants().stream()
                .map(CatalogServiceImpl::effectivePrice)
                .min(Comparator.naturalOrder())
                .orElse(BigDecimal.ZERO);
    }

    private static BigDecimal effectivePrice(CatalogVariantResponse variant) {
        return variant.promoPrice() != null ? variant.promoPrice() : variant.price();
    }

    private static List<String> sortedText(Stream<String> values) {
        Set<String> distinct = values.filter(Objects::nonNull).collect(Collectors.toCollection(LinkedHashSet::new));
        return distinct.stream().sorted(PT_BR).toList();
    }

    private static boolean isTrue(Boolean value) {
        return Boolean.TRUE.equals(value);
    }

    private static boolean isEmpty(List<String> values) {
        return values == null || values.isEmpty();
    }
}
