package br.com.george.commerce.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** Uma nota da pirâmide olfativa: camada (top | heart | base) e o nome da nota. */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductNote {

    private String layer;

    private String note;
}
