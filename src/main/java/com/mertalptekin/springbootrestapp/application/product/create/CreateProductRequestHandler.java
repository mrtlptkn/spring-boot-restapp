package com.mertalptekin.springbootrestapp.application.product.create;


import com.mertalptekin.springbootrestapp.application.product.IProductRequestHandler;
import com.mertalptekin.springbootrestapp.domain.entity.Product;
import com.mertalptekin.springbootrestapp.domain.service.product.IProductService;
import com.mertalptekin.springbootrestapp.infra.service.IEmailSender;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

// ProductService -> Create, UPDATE, DELETE Class düzeyinde bunların ayrılması OCP dir.
// Her bir Operasyonun kendi koduna sahip olması sadece kendi sorumluluğunda olması SRP dir.

// Hexagonal Mimariye göre CreateProductRequestHandler ise IProductRequestHandler Portundan implemente olan bir Adapter'dir.
@Component
public class CreateProductRequestHandler implements IProductRequestHandler<CreateProductRequest,CreateProductResponse> {

    private final IProductService productService;
    private  final IEmailSender emailSender;


    public CreateProductRequestHandler(IProductService productService, ApplicationContext applicationContext,    @Value("${email.service}") String emailService) {
        this.productService = productService;
        // Service Locator Pattern
        this.emailSender = (IEmailSender) applicationContext.getBean(emailService);
    }

    // OCP açık kapalı prensibine uygundur. TDD ile yazılabilir. Her bir use case için ayrı handler yazıldığından dolayı Unit Test yazımı kolaydır. SRP tek sorumluluk prensibine uygundur. Sadece ürün oluşturma işlemi ile ilgilenir.
    // Application Katmanın görevi use case'leri yönetmektir. ve business logic içermez.
    // Business logic domain katmanında yer alır.
    // Bu handler bir use case'i temsil eder.
    // Ürün oluşturma işlemini yönetir.
    // request verisini alır, gerekli işlemleri yapar ve sonucu döner.
    // İşlem sonrasında email gönderme işlemi de yapılır.
    // Use Case -> Ürün kaydı girdiliğinde benzer kategorideki ürünleri takip eden müşterilere email gönder.
    @Override
    public CreateProductResponse handle(CreateProductRequest request) {

        // dto to entity
        Product entity = new Product();
        BeanUtils.copyProperties(request, entity);
        // Bean kopyalama işlemi yapıldıktan sonra servise gönderiyoruz.
        this.productService.addProduct(entity); // Domain
        // email gönderme işlemi
        // Infra

        emailSender.sendEmail("test@test.com","Yeni Ürün Eklendi","Yeni ürün eklendi: " + entity.getName());

        // entity to dto
        CreateProductResponse response = new CreateProductResponse();
        BeanUtils.copyProperties(entity, response);

        return response;

    }

}
