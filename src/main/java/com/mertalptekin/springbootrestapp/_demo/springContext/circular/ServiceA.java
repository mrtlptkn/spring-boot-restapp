package com.mertalptekin.springbootrestapp._demo.springContext.circular;

import org.springframework.stereotype.Service;

@Service
public class ServiceA {

    // ServiceA, ServiceB'ye constructor üzerinden bağımlı.
    // ServiceB de aynı anda ServiceA'ya bağımlı olduğu için normalde
    // BeanCurrentlyInCreationException fırlatılır (dairesel bağımlılık).
    private final ServiceB serviceB;

    public ServiceA(ServiceB serviceB) {
        this.serviceB = serviceB;
    }

    public String callB() {
        return "ServiceA -> " + serviceB.ping();
    }

    public String ping() {
        return "ServiceA.ping()";
    }
}
