package com.example.appproject05.models;

/**
 * Enum que representa os possíveis estados de um pedido
 * Inclui descrições amigáveis e códigos de status
 */
public enum OrderStatus {
    PENDING("Pendente", "Aguardando confirmação", 1),
    CONFIRMED("Confirmado", "Pedido confirmado", 2),
    PREPARING("Em Preparo", "Pedido está sendo preparado", 3),
    READY("Pronto", "Pronto para entrega", 4),
    OUT_FOR_DELIVERY("Em Entrega", "Saiu para entrega", 5),
    DELIVERED("Entregue", "Pedido entregue com sucesso", 6),
    CANCELLED("Cancelado", "Pedido foi cancelado", 7);

    private final String label;
    private final String description;
    private final int code;

    OrderStatus(String label, String description, int code) {
        this.label = label;
        this.description = description;
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public int getCode() {
        return code;
    }

    // Método útil para converter código em status
    public static OrderStatus fromCode(int code) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return PENDING; // Status padrão
    }
}