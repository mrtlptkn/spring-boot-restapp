package com.mertalptekin.springbootrestapp._demo.springContext;

import com.mertalptekin.springbootrestapp._demo.aspects.Log;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

// Streotype Bean tanımı

// Bean Lifecycle yönetimi
// Default singleton tanımılıdır
@Service
//@Log // Servisin her methodu için çalışır
//@Scope("singleton") // lifecycle yönetimi
public class DemoService {

    // initialize -> register olma anı
    @PostConstruct
    public void init() {
        System.out.println("DemoService Bean is created and initialized.");
    }

    // dispose, GB Collection
    @PreDestroy
    public void cleanup() {
        System.out.println("DemoService Bean is about to be destroyed.");
    }

    // Log Ascpecti yakalamak için method üzerinden çağırdık.
    @Log
    public  void  test(){
        System.out.println("DemoService test method called.");
    }

}
