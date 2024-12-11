package com.example.appproject05.models;

public enum OrderStatus {
    PENDING("Pendente"),
    CONFIRMED("Confirmado"),
    PREPARING("Em Preparo"),
    READY("Pronto"),
    OUT_FOR_DELIVERY("Em Entrega"),
    DELIVERED("Entregue"),
    CANCELLED("Cancelado");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        switch (this) {
            case PENDING:
                return "Pendente";
            case CONFIRMED:
                return "Confirmado";
            case PREPARING:
                return "Em Preparo";
            case READY:
                return "Pronto";
            case OUT_FOR_DELIVERY:
                return "Em Entrega";
            case DELIVERED:
                return "Entregue";
            case CANCELLED:
                return "Cancelado";
            default:
                return "Desconhecido";
        }
    }

    public static OrderStatus fromString(String status) {
        try {
            return valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PENDING;
        }
    }

    public OrderStatus next() {
        switch (this) {
            case PENDING:
                return CONFIRMED;
            case CONFIRMED:
                return PREPARING;
            case PREPARING:
                return READY;
            case READY:
                return OUT_FOR_DELIVERY;
            case OUT_FOR_DELIVERY:
                return DELIVERED;
            default:
                return this;
        }
    }

}