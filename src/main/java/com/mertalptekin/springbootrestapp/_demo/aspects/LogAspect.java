package com.mertalptekin.springbootrestapp._demo.aspects;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

    // Method çalıştırılmadan önce çalışır
    // @Before("execution(* com.mertalptekin.springbootrestapp.application.*.*(..))")
    @Before(value = "@annotation(com.mertalptekin.springbootrestapp._demo.aspects.Log)")
    public void logBeforeMethod() {
        System.out.println("A method is about to be executed.");
    }
    // Method çalıştırıldıktan sonra çalışır
    @After(value = "@annotation(com.mertalptekin.springbootrestapp._demo.aspects.Log)")
    public void logAfterMethod() {
        System.out.println("A method has been executed.");
    }
//    // Method başarılı bir şekilde tamamlandığında çalışır. Ve Methodun dönüş değerine erişebilir
//    @AfterReturning(value = "@annotation(com.mertalptekin.springbootrestapp._demo.aspects.Log)")
//    public void logAfterReturning() {
//        System.out.println("A method has successfully returned.");
//    }
    // Method çalıştırılmadan önce ve sonra çalışır. Genel olarak bütün method çağrılarını sarmak için kullanılır
//    @Around(value = "@annotation(com.mertalptekin.springbootrestapp._demo.aspects.Log)")
//    public void logAroundMethod(org.aspectj.lang.ProceedingJoinPoint joinPoint) throws Throwable {
//
//        // Not: Arroud advice ile method içerisindeki tüm sürece hakim olduğumuzdan sadece bunun üzerinden süreci yönetebiliriz.
//
//        System.out.println("Around Before Method");
//        try{
//            Object args =  joinPoint.getArgs();
//            System.out.println("Around Args" + args);
//
//            // method result değeri
//           Object result = joinPoint.proceed(); // Methodu çalıştır, takip edilen method çağırısı yapar.
//
//            System.out.println("Around  @AfterReturning after Method");
//            System.out.println("result : " + result);
//
//
//
//        } catch (Throwable e) {
//            System.out.println("Around execute @AfterThrowing after advice Method");
//        } finally {
//            System.out.println("Around execute @After advice after Method");
//        }
//
//    }

    // Methodda exception fırlatıldığında çalışır
    //@AfterThrowing(value = "@annotation(com.mertalptekin.springbootrestapp._demo.aspects.Log)")
    //public void logAfterThrowing() {
        //System.out.println("A method has thrown an exception.");
    //}
}
