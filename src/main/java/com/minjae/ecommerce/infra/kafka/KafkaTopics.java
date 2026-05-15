package com.minjae.ecommerce.infra.kafka;

public final class KafkaTopics {
    public static final String PAYMENT_COMPLETED = "payment.completed";
    public static final String PAYMENT_CANCELLED = "payment.cancelled";
    public static final String ORDER_CREATED = "order.created";
    public static final String ORDER_CANCELLED = "order.cancelled";

    private KafkaTopics() {}
}
