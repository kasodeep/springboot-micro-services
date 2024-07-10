package com.deep.ecommerce.order;

import com.deep.ecommerce.customer.CustomerClient;
import com.deep.ecommerce.exception.BusinessException;
import com.deep.ecommerce.kafka.OrderConfirmation;
import com.deep.ecommerce.kafka.OrderProducer;
import com.deep.ecommerce.orderline.OrderLineRequest;
import com.deep.ecommerce.orderline.OrderLineService;
import com.deep.ecommerce.payment.PaymentClient;
import com.deep.ecommerce.payment.PaymentRequest;
import com.deep.ecommerce.product.ProductClient;
import com.deep.ecommerce.product.PurchaseRequest;
import com.deep.ecommerce.product.PurchaseResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderMapper mapper;

    private final CustomerClient customerClient;

    private final ProductClient productClient;

    private final OrderLineService orderLineService;

    private final OrderProducer orderProducer;

    private final PaymentClient paymentClient;

    public Integer createOrder(OrderRequest request) {
        // Check the customer -> OpenFeign
        var customer = customerClient.findCustomerById(request.customerId())
                .orElseThrow(() -> new BusinessException("Cannot create order:: No Customer exists with the provided ID " + request.customerId()));

        // Purchase the products  (RestTemplate)
        List<PurchaseResponse> purchaseProducts = productClient.purchaseProducts(request.products());

        // Persist order
        var order = orderRepository.save(mapper.toOrder(request));
        for (PurchaseRequest purchaseRequest : request.products()) {
            orderLineService.saveOrderLine(new OrderLineRequest(
                    null,
                    order.getId(),
                    purchaseRequest.productId(),
                    purchaseRequest.quantity()));
        }

        // Start payment process.
        paymentClient.requestOrderPayment(
                new PaymentRequest(
                        request.amount(),
                        request.paymentMethod(),
                        order.getId(),
                        order.getReference(),
                        customer
                )
        );

        // Send the order confirmation -> notification-ms (kafka)
        orderProducer.sendOrderConfirmation(
                new OrderConfirmation(
                        request.reference(),
                        request.amount(),
                        request.paymentMethod(),
                        customer,
                        purchaseProducts
                )
        );

        return order.getId();
    }

    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(mapper::fromOrder)
                .toList();
    }

    public OrderResponse findById(Integer orderId) {
        return orderRepository.findById(orderId)
                .map(mapper::fromOrder)
                .orElseThrow(() -> new EntityNotFoundException(String.format("No order found with the provided ID: %d", orderId)));
    }
}
