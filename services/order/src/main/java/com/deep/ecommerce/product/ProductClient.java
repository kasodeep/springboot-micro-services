package com.deep.ecommerce.product;

import com.deep.ecommerce.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductClient {

    private final RestTemplate restTemplate;

    @Value("${application.config.product-url}")
    private String productUrl;

    public List<PurchaseResponse> purchaseProducts(List<PurchaseRequest> requestBody) {
        // Specify the headers.
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // Create the request entity.
        HttpEntity<List<PurchaseRequest>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Specify the response type.
        ParameterizedTypeReference<List<PurchaseResponse>> responseType =
                new ParameterizedTypeReference<>() {
                };

        // Get the data.
        ResponseEntity<List<PurchaseResponse>> responseEntity = restTemplate.exchange(
                productUrl + "/purchase",
                HttpMethod.POST,
                requestEntity,
                responseType
        );

        if (responseEntity.getStatusCode().isError()) {
            throw new BusinessException("An error occurred while processing the products purchase: " + responseEntity.getStatusCode());
        }
        return responseEntity.getBody();
    }
}
