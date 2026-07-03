package com.mertalptekin.springbootrestapp;

import com.mertalptekin.springbootrestapp._demo.springContext.commit.CommitService;
import com.mertalptekin.springbootrestapp._demo.springContext.commit.ICommit;
import com.mertalptekin.springbootrestapp._demo.springContext.custom.MyCustomBean;
import com.mertalptekin.springbootrestapp._demo.springContext.DemoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.Scanner;

@SpringBootApplication
@EnableAspectJAutoProxy
public class SpringBootRestappApplication {

    public static void main(String[] args) {
       ApplicationContext context = SpringApplication.run(SpringBootRestappApplication.class, args);
       runBeanSample(context);
    }

    // Manuel , Config dosyasından Manuel Bean tanımı
    @Bean
    public String getAppName() { // Bean isimleri method ismi ile ayni olur
        return "Spring Boot Rest App";
    }

    public static   void runBeanSample(ApplicationContext context) {
        String name =  context.getBean("getAppName",String.class); // Retrieve the bean to demonstrate it's working
        System.out.println("Bean " + name);

        DemoService demoService = context.getBean(DemoService.class);
        demoService.test();



//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Write commit type dbCommit or cacheCommit : ");
//        String CommitType = scanner.nextLine();
//        String beanName = "";
//
//        if(CommitType.equalsIgnoreCase("db")){
//            beanName = "dbCommit";
//        } else if (CommitType.equalsIgnoreCase("cache")){
//            beanName = "cacheCommit";
//        } else if (CommitType.equalsIgnoreCase("text")) {
//            beanName = "textCommit";
//        }
//
//
//        ICommit commit = context.getBean(beanName,ICommit.class);
//        // beanden aldığımı commit serviste çalıştıracağımızı söyledik.
//
//        CommitService commitService = new CommitService(commit);
//        commitService.save();


        // Custom Registration Bean Kullanımı
        MyCustomBean myCustomBean = context.getBean(MyCustomBean.class);
        System.out.println("customBean Def:" + myCustomBean.getName());



    }

}
