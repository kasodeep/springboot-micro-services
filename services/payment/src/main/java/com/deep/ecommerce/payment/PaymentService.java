package com.deep.ecommerce.payment;

import com.deep.ecommerce.notification.NotificationProducer;
import com.deep.ecommerce.notification.PaymentNotificationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repository;

    private final PaymentMapper mapper;

    private final NotificationProducer notificationProducer;

    public Integer createPayment(PaymentRequest paymentRequest) {
        Payment payment = repository.save(mapper.toPayment(paymentRequest));

        // Send notification to service.
        notificationProducer.sendNotification(
                new PaymentNotificationRequest(
                        paymentRequest.orderReference(),
                        paymentRequest.amount(),
                        paymentRequest.paymentMethod(),
                        paymentRequest.customer().firstname(),
                        paymentRequest.customer().lastname(),
                        paymentRequest.customer().email()
                )
        );

        return payment.getId();
    }
}
