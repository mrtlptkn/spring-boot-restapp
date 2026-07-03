package com.mertalptekin.springbootrestapp.application.product.create;

import com.mertalptekin.springbootrestapp.domain.entity.Product;
import com.mertalptekin.springbootrestapp.domain.service.product.IProductService;
import com.mertalptekin.springbootrestapp.infra.service.IEmailSender;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;

import java.math.BigDecimal;

// Unit Test for CreateProductRequestHandler
@ExtendWith(MockitoExtension.class)
class CreateProductRequestHandlerTest {

    @Mock
    private IProductService productService;

    @Mock
    private IEmailSender emailSender;

    @Mock
    private ApplicationContext applicationContext;

    private CreateProductRequestHandler handler;

    // Setup method to initialize the handler before each test
    @BeforeEach
    void setUp() {
        // Mocking ApplicationContext to return the mocked emailSender
        Mockito.when(applicationContext.getBean("emailService")).thenReturn(emailSender);
        handler = new CreateProductRequestHandler(productService, applicationContext, "emailService");
    }

    @Test
    void handleCreatesProductAndSendsEmail() {
        CreateProductRequest request = new CreateProductRequest("Test Product", new BigDecimal("99.99"), 5);

        // Mocking the behavior of productService and emailSender
        Mockito.doNothing().when(productService).addProduct(Mockito.any(Product.class));
        Mockito.doNothing().when(emailSender).sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        // Execute the handler
        CreateProductResponse response = handler.handle(request);

        // Verify interactions and response
        Mockito.verify(productService).addProduct(Mockito.any(Product.class));
        Mockito.verify(emailSender).sendEmail(
            "test@test.com",
            "Yeni Ürün Eklendi",
            "Yeni ürün eklendi: Test Product"
        );

        // Verify response content

        Assertions.assertEquals("Test Product", response.getName());
        Assertions.assertEquals(new BigDecimal("99.99"), response.getPrice());
        Assertions.assertEquals(5, response.getStock());
    }

    @Test
    @DisplayName("handle throws exception when email sender fails")
    void handleThrowsExceptionWhenEmailSenderFails() {
        CreateProductRequest request = new CreateProductRequest("Test Product", new BigDecimal("99.99"), 5);

        Mockito.doNothing().when(productService).addProduct(Mockito.any(Product.class));
        Mockito.doThrow(new RuntimeException("Email sending failed"))
            .when(emailSender).sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        Assertions.assertThrows(RuntimeException.class, () -> handler.handle(request));

        Mockito.verify(productService).addProduct(Mockito.any(Product.class));
        Mockito.verify(emailSender).sendEmail(
            "test@test.com",
            "Yeni Ürün Eklendi",
            "Yeni ürün eklendi: Test Product"
        );
    }

    @Test
    @DisplayName("handle throws exception when product service fails")
    void handleThrowsExceptionWhenProductServiceFails() {
        CreateProductRequest request = new CreateProductRequest("Test Product", new BigDecimal("99.99"), 5);

        Mockito.doThrow(new RuntimeException("Product service failure"))
            .when(productService).addProduct(Mockito.any(Product.class));

        Assertions.assertThrows(RuntimeException.class, () -> handler.handle(request));

        Mockito.verify(productService).addProduct(Mockito.any(Product.class));
        Mockito.verifyNoInteractions(emailSender);
    }

    @Test
    @DisplayName("handle processes request with minimum values")
    void handleProcessesRequestWithMinimumValues() {
        CreateProductRequest request = new CreateProductRequest("P", new BigDecimal("1"), 1);

        Mockito.doNothing().when(productService).addProduct(Mockito.any(Product.class));
        Mockito.doNothing().when(emailSender).sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        CreateProductResponse response = handler.handle(request);

        Mockito.verify(productService).addProduct(Mockito.any(Product.class));
        Mockito.verify(emailSender).sendEmail(
            "test@test.com",
            "Yeni Ürün Eklendi",
            "Yeni ürün eklendi: P"
        );

        Assertions.assertEquals("P", response.getName());
        Assertions.assertEquals(new BigDecimal("1"), response.getPrice());
        Assertions.assertEquals(1, response.getStock());
    }

    @Test
    @DisplayName("handle processes request with maximum values")
    void handleProcessesRequestWithMaximumValues() {
        CreateProductRequest request = new CreateProductRequest("12345678901234567890", new BigDecimal("999999.99"), 10);

        Mockito.doNothing().when(productService).addProduct(Mockito.any(Product.class));
        Mockito.doNothing().when(emailSender).sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        CreateProductResponse response = handler.handle(request);

        Mockito.verify(productService).addProduct(Mockito.any(Product.class));
        Mockito.verify(emailSender).sendEmail(
            "test@test.com",
            "Yeni Ürün Eklendi",
            "Yeni ürün eklendi: 12345678901234567890"
        );

        Assertions.assertEquals("12345678901234567890", response.getName());
        Assertions.assertEquals(new BigDecimal("999999.99"), response.getPrice());
        Assertions.assertEquals(10, response.getStock());
    }

    @Test
    @DisplayName("handle processes request with decimal price")
    void handleProcessesRequestWithDecimalPrice() {
        CreateProductRequest request = new CreateProductRequest("Test Product", new BigDecimal("49.95"), 3);

        // Mocking the behavior of productService and emailSender
        Mockito.doNothing().when(productService).addProduct(Mockito.any(Product.class));
        Mockito.doNothing().when(emailSender).sendEmail(Mockito.anyString(), Mockito.anyString(), Mockito.anyString());

        CreateProductResponse response = handler.handle(request);

        // Verify interactions and response
        Mockito.verify(productService).addProduct(Mockito.any(Product.class));
        Mockito.verify(emailSender).sendEmail(
            "test@test.com",
            "Yeni Ürün Eklendi",
            "Yeni ürün eklendi: Test Product"
        );

        // Verify response content
        Assertions.assertEquals("Test Product", response.getName());
        Assertions.assertEquals(new BigDecimal("49.95"), response.getPrice());
        Assertions.assertEquals(3, response.getStock());
    }
}
