package com.deep.ecommerce.customer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService service;

    @PostMapping
    public ResponseEntity<String> createCustomer(@RequestBody @Valid CustomerRequest request) {
        return ResponseEntity.ok(service.createCustomer(request));
    }

    @PutMapping
    public ResponseEntity<Void> updateCustomer(@RequestBody @Valid CustomerRequest request) {
        service.updateCustomer(request);
        return ResponseEntity.accepted().build();
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> findAll() {
        return ResponseEntity.ok(service.findAllCustomers());
    }

    @GetMapping("/exists/{customerId}")
    public ResponseEntity<Boolean> existsById(@PathVariable String customerId) {
        return ResponseEntity.ok(service.existsById(customerId));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponse> findById(@PathVariable String customerId) {
        return ResponseEntity.ok(service.findById(customerId));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> delete(@PathVariable String customerId) {
        service.deleteCustomer(customerId);
        return ResponseEntity.accepted().build();
    }
}
