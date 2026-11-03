package br.com.george.commerce.dto.catalog;

import java.util.List;
import lombok.Builder;

/** Pirâmide olfativa: topo, coração e fundo. */
@Builder
public record CatalogNotesResponse(List<String> top, List<String> heart, List<String> base) {
}
