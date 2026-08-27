package br.com.george.commerce.service.impl;

import br.com.george.commerce.dto.affiliate.*;
import br.com.george.commerce.entity.Affiliate;
import br.com.george.commerce.entity.AffiliateSale;
import br.com.george.commerce.entity.User;
import br.com.george.commerce.enums.DiscountType;
import br.com.george.commerce.exception.AffiliateAlreadyExistsException;
import br.com.george.commerce.exception.AffiliateNotFoundException;
import br.com.george.commerce.exception.UserNotFoundException;
import br.com.george.commerce.mapper.AffiliateMapper;
import br.com.george.commerce.mapper.AffiliateSaleMapper;
import br.com.george.commerce.repository.AffiliateRepository;
import br.com.george.commerce.repository.AffiliateSaleRepository;
import br.com.george.commerce.repository.UserRepository;
import br.com.george.commerce.service.AffiliateService;
import br.com.george.commerce.service.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AffiliateServiceImpl implements AffiliateService {

    private final AffiliateRepository affiliateRepository;
    private final AffiliateSaleRepository affiliateSaleRepository;
    private final AffiliateMapper affiliateMapper;
    private final AffiliateSaleMapper affiliateSaleMapper;
    private final UserRepository userRepository;
    private final JwtService jwtService;


    @Override
    public MyAffiliateResponse myAffiliate() {

        String email = jwtService.getCurrentUserEmail();

        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        Affiliate affiliate = affiliateRepository.findByUserId(user.getId()).orElseThrow(AffiliateNotFoundException::new);

        return affiliateMapper.toMyAffiliateResponse(affiliate);
    }

    @Override
    public List<AffiliateSaleResponse> mySales() {

        String email = jwtService.getCurrentUserEmail();

        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        Affiliate affiliate = affiliateRepository.findByUserId(user.getId()).orElseThrow(AffiliateNotFoundException::new);

        return affiliateSaleRepository
                .findByAffiliateId(affiliate.getId())
                .stream()
                .map(affiliateSaleMapper::toResponse)
                .toList();
    }

    @Override
    public AffiliateResponse becomeAffiliate() {

        String email = jwtService.getCurrentUserEmail();

        User user = userRepository.findByEmail(email).orElseThrow(UserNotFoundException::new);

        if (affiliateRepository.findByUserId(user.getId()).isPresent()) {
            throw new AffiliateAlreadyExistsException();
        }

        Affiliate affiliate = Affiliate.builder()
                .user(user)
                .couponCode(user.getName().toUpperCase().replace(" ", "") + "10")
                .active(true)
                .discountType(DiscountType.PERCENTAGE)
                .discountValue(BigDecimal.valueOf(10))
                .commissionPercentage(BigDecimal.valueOf(5))
                .expiresAt(LocalDateTime.now().plusYears(1))
                .build();

        affiliate = affiliateRepository.save(affiliate);

        return affiliateMapper.toResponse(affiliate);
    }

    @Override
    public AffiliateSummaryResponse mySummary(AffiliateReportRequest request) {

        String email = jwtService.getCurrentUserEmail();

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException(email));

        Affiliate affiliate = affiliateRepository.findByUserId(user.getId()).orElseThrow(AffiliateNotFoundException::new);

        List<AffiliateSale> sales = affiliateSaleRepository.findByAffiliateId(affiliate.getId());

        if (request.startDate() != null) {
            sales = sales.stream().filter(sale -> !sale.getCreatedAt().toLocalDate().isBefore(request.startDate())).toList();
        }

        if (request.endDate() != null) {
            sales = sales.stream().filter(sale -> !sale.getCreatedAt().toLocalDate().isAfter(request.endDate())).toList();
        }

        BigDecimal totalSales = sales.stream()
                .map(AffiliateSale::getSaleAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCommission = sales.stream()
                .map(AffiliateSale::getCommissionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal pendingCommission = sales.stream()
                .filter(sale -> !sale.getPaid())
                .map(AffiliateSale::getCommissionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal paidCommission = sales.stream()
                .filter(AffiliateSale::getPaid)
                .map(AffiliateSale::getCommissionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new AffiliateSummaryResponse(totalSales, totalCommission, pendingCommission, paidCommission);
    }


}
