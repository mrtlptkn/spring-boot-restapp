package com.mertalptekin.springbootrestapp._demo.springContext.circular;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ServiceB {

    // Çözüm: @Lazy ile ServiceA için gerçek bir proxy enjekte edilir,
    // asıl bean ilk kullanıldığı anda (call() çağrılana kadar) oluşturulmaz.
    // Böylece container, ServiceA <-> ServiceB döngüsünü kurarken kilitlenmez.
    // Not: Bu geçici bir çözümdür; asıl öneri (PDF 4.7) sınıflardan birini
    // parçalamak veya ortak bir üçüncü servise çıkarmaktır.
    private final ServiceA serviceA;

    public ServiceB(@Lazy ServiceA serviceA) {
        this.serviceA = serviceA;
    }

    public String ping() {
        return "ServiceB.ping()";
    }

    public String callA() {
        return "ServiceB -> " + serviceA.ping();
    }
}
