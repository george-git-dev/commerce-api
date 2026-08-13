package br.com.george.commerce.enums;


public enum OrderStatus {

    PENDING("Pedido criado"),
    PAID("Pagamento aprovado"),
    SHIPPED("Enviado"),
    DELIVERED("Entregue"),
    CANCELED("Cancelado");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
