package com.example.taskmanagementsystem.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "FeignClientLesson", url = "${FeignClientLesson.url}")
public interface MyFeignClient {
    @GetMapping("/{userId}")
    String getMessageByUserId(@PathVariable Long userId);
}

/*
@FeignClient(name = "customer-service", url = "${customer-service.url}")
public interface CustomerFeignClient {
    @GetMapping("/customers/{id}")
    ResponseEntity<CustomerDto> getCustomerById(@PathVariable("id") Long id);
}
 */