package com.minjae.ecommerce.api.payment;

import com.minjae.ecommerce.api.payment.request.PaymentCancelRequest;
import com.minjae.ecommerce.api.payment.request.PaymentRequest;
import com.minjae.ecommerce.api.payment.response.PaymentResponse;
import com.minjae.ecommerce.domain.payment.service.PaymentService;
import com.minjae.ecommerce.global.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "결제", description = "결제 관리")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 요청")
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponse>> requestPayment(
            @AuthenticationPrincipal String publicId,
            @Valid @RequestBody PaymentRequest request
            ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.ok(paymentService.requestPayment(publicId, request)));
    }

    @Operation(summary = "결제 조회")
    @GetMapping("/orders/{orderId}")
    public ResponseEntity<ApiResponse<PaymentResponse>> getPayment(
            @PathVariable Long orderId) {
        return ResponseEntity.ok(ApiResponse.ok(paymentService.getPayment(orderId)));
    }

    @Operation(summary = "결제 취소")
    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<ApiResponse<PaymentResponse>> cancelPayment(
            @PathVariable Long paymentId,
            @Valid @RequestBody PaymentCancelRequest request) {
        return ResponseEntity.ok(ApiResponse.ok(paymentService.cancelPayment(paymentId, request)));
    }
}
