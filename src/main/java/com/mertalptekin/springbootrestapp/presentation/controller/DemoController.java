package com.mertalptekin.springbootrestapp.presentation.controller;


import com.mertalptekin.springbootrestapp.domain.service.AspectService;
import com.mertalptekin.springbootrestapp._demo.springContext.circular.ServiceA;
import com.mertalptekin.springbootrestapp._demo.springContext.circular.ServiceB;
import com.mertalptekin.springbootrestapp._demo.springContext.custom.WebRequestBasedBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/demo")
public class DemoController {

    private final WebRequestBasedBean webRequestBasedBean;
    private final AspectService aspectService;
    private final ServiceA serviceA;
    private final ServiceB serviceB;

    public DemoController(WebRequestBasedBean webRequestBasedBean
    , AspectService aspectService, ServiceA serviceA, ServiceB serviceB) {
        this.aspectService = aspectService;
        this.webRequestBasedBean = webRequestBasedBean;
        this.serviceA = serviceA;
        this.serviceB = serviceB;
    }


    @GetMapping
    public String demo() {
        webRequestBasedBean.test();
        return "Demo Controller is working...";
    }

    @PostMapping
    public String demoPost() {
        aspectService.execute();
        return "Demo Controller Post is working...";
    }

    // @Lazy ile çözülen dairesel bağımlılık (ServiceA <-> ServiceB) örneği.
    @GetMapping("/circular")
    public String circular() {
        return serviceA.callB() + " | " + serviceB.callA();
    }

}
