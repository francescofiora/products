package it.francescofiora.product.api.service.dto.enumeration;

/**
 * The OrderStatus enumeration.
 */
public enum OrderStatus {
    /**
     * COMPLETED: order send to the customer.
     */
    COMPLETED,

    /**
     * PENDING: order not send to the customer yet.
     */
    PENDING,

    /**
     * CANCELLED: order cancelled.
     */
    CANCELLED
}
