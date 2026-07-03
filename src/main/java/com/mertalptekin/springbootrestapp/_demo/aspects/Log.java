package com.mertalptekin.springbootrestapp._demo.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD, ElementType.TYPE})
// Anatasyonun metod seviyesinde kullanılmasını sağlar.
@Retention(RetentionPolicy.RUNTIME)
// Anatasonun çalışma zamanında erişilebilir olmasını sağlar.
public @interface Log {
}

// Aspect Nedir ?
// Aspect, uygulamanın farklı bölümlerine yayılmış olan ortak işlevselliği (cross-cutting concerns) modüler bir şekilde yönetmeyi sağlayan bir programlama paradigmasıdır. Örneğin, logging, güvenlik, hata yönetimi gibi işlevler genellikle uygulamanın birçok farklı yerinde tekrar eder. Aspect'ler, bu tür işlevleri tek bir yerde tanımlayarak kodun tekrarını azaltır ve bakımını kolaylaştırır.

// Aspect ile @Log anatasyonunu birlikte kullanarak, belirli metodların çağrılması sırasında logging işlemlerini otomatik olarak gerçekleştirebilirsiniz. Bu sayede, logging kodunu her metoda ayrı ayrı eklemek yerine, @Log anatasyonunu kullanarak bu işlemi merkezi bir şekilde yönetebilirsiniz.
