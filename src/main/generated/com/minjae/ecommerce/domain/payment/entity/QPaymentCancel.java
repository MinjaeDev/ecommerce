package com.minjae.ecommerce.domain.payment.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPaymentCancel is a Querydsl query type for PaymentCancel
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPaymentCancel extends EntityPathBase<PaymentCancel> {

    private static final long serialVersionUID = -875127116L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPaymentCancel paymentCancel = new QPaymentCancel("paymentCancel");

    public final NumberPath<java.math.BigDecimal> cancelAmount = createNumber("cancelAmount", java.math.BigDecimal.class);

    public final NumberPath<Long> cancelId = createNumber("cancelId", Long.class);

    public final StringPath cancelKey = createString("cancelKey");

    public final DateTimePath<java.time.LocalDateTime> cancelledAt = createDateTime("cancelledAt", java.time.LocalDateTime.class);

    public final StringPath cancelReason = createString("cancelReason");

    public final QPayment payment;

    public QPaymentCancel(String variable) {
        this(PaymentCancel.class, forVariable(variable), INITS);
    }

    public QPaymentCancel(Path<? extends PaymentCancel> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPaymentCancel(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPaymentCancel(PathMetadata metadata, PathInits inits) {
        this(PaymentCancel.class, metadata, inits);
    }

    public QPaymentCancel(Class<? extends PaymentCancel> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.payment = inits.isInitialized("payment") ? new QPayment(forProperty("payment"), inits.get("payment")) : null;
    }

}

