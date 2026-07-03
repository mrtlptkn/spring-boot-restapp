# Spring Boot REST App

Spring Framework & Spring Boot ile kurumsal uygulama geliştirme prensiplerini (Clean Architecture / N-Tier mimari, DI, AOP, JPA, Security, Validation vb.) uçtan uca örnekleyen bir REST API projesi.

## Teknoloji Yığını

- **Java 17**, **Spring Boot 3.5.7**
- Spring Web (REST), Spring Data JPA (Hibernate), Spring Security + JWT (jjwt)
- Spring AOP, Spring Validation, Spring Boot Actuator
- H2 (dosya tabanlı, iki ayrı veritabanı: primary + secondary)
- ModelMapper (Entity ↔ DTO dönüşümü)
- springdoc-openapi (Swagger UI)
- Lombok
- JUnit 5 + Mockito

## Mimari

Proje, katmanlar arası bağımlılığın her zaman dıştan içe doğru olduğu bir **Clean Architecture / N-Tier** yaklaşımıyla organize edilmiştir:

```
presentation/   -> Controller'lar, Security/OpenAPI/DB config sınıfları (HTTP katmanı)
application/    -> Use-case handler'lar, request/response DTO'ları (iş akışı orkestrasyonu)
domain/         -> Entity'ler ve domain servisleri (iş kuralları)
infra/          -> Repository implementasyonları, JWT servisi, email gönderim adaptörleri
secondarydb/    -> İkinci veritabanına (Course) ait entity/repository
_demo/          -> Spring çekirdek kavramlarını (DI, AOP, Bean lifecycle, Scope, Circular Dependency) izole şekilde gösteren öğretici sınıflar
```

Bağımlılık yönü: `Controller → Application Handler → Domain Service → Repository → Entity`.

## Öne Çıkan Özellikler

| Konu | Karşılığı |
|---|---|
| IoC / Dependency Injection | Constructor injection, `@Qualifier`, `@Primary`, `@Scope`, `@Lazy` ile circular dependency çözümü (`_demo/springContext/`) |
| AOP | `@Aspect` tabanlı loglama örneği (`_demo/aspects/LogAspect`) |
| RESTful API | Versiyonlu (`/api/v1/...`) controller'lar, DTO kullanımı, `ResponseEntity` |
| Spring Data JPA | Entity mapping, `JpaRepository`, query method'lar, JPQL, pagination & sorting |
| Transaction Yönetimi | `DiscountPriceHandler` üzerinde gerçek `@Transactional` kullanımı |
| Event-Driven Akış | `ApplicationEventPublisher` / `@EventListener` ile fiyat değişim geçmişi ve email bildirimi |
| Validation | Bean Validation (`@Valid`, `@NotBlank` vb.) + özel `@Constraint` validator (`NotReservedProductName`) |
| Global Exception Handling | `@RestControllerAdvice` (`ErrorConfig`) |
| Spring Security | JWT tabanlı stateless authentication, `@PreAuthorize` ile method-level yetkilendirme, CORS config |
| Observability | Spring Actuator (`/actuator/health`, `/info`, `/metrics`) |
| Profiles | `dev` / `prod` ortam bazlı konfigürasyon |
| Dokümantasyon | Swagger UI (springdoc-openapi) |

## Kurulum ve Çalıştırma

### Gereksinimler

- JDK 17+
- Maven (proje `mvnw` wrapper'ı ile birlikte gelir)

### Çalıştırma

```bash
./mvnw spring-boot:run
```

Uygulama varsayılan olarak `dev` profiliyle, `8080` portunda ayağa kalkar.

### Profiller

| Profil | Kullanım Amacı | Farklar |
|---|---|---|
| `dev` (varsayılan) | Yerel geliştirme | Detaylı SQL logu, `ddl-auto=update`, mock email sağlayıcı (`turkcell`) |
| `prod` | Canlı ortam | Minimal log, `ddl-auto=validate`, gerçek email sağlayıcı (`sendGrid`), actuator health detayları kapalı |

Profil değiştirmek için:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

### Veritabanı

İki ayrı H2 dosya veritabanı kullanılır (`PrimaryDatabaseConfig`, `SecondaryDatabaseConfig`):

- Primary: `jdbc:h2:file:C:/data/testdb`
- Secondary: `jdbc:h2:file:C:/data/secondarydb`

H2 konsoluna erişim: `http://localhost:8080/h2-console`

## API Uç Noktaları (özet)

Tüm iş endpoint'leri `/api/v1/...` altında versiyonlanmıştır.

| Controller | Base Path | Not |
|---|---|---|
| `AuthController` | `/api/v1/auth` | Kayıt (`POST /`), token üretme (`POST /token`) — herkese açık |
| `ProductController` | `/api/v1/products` | CRUD + `PATCH /{id}/discount` (indirim, `@Transactional`) |
| `CategoryController` | `/api/v1/categories` | Kategori sorgulama, pagination/sorting örneği |
| `CourseController` | `/api/v1/courses` | İkinci veritabanı (secondarydb) örneği |
| `AdminController` | `/api/v1/admins` | `ROLE_ADMIN` + `ROLE_MANAGER` gerektirir |
| `DemoController` | `/api/v1/demo` | AOP ve `@Lazy`/circular dependency demoları |

Swagger UI: `http://localhost:8080/swagger-ui.html`

## Kimlik Doğrulama Akışı

1. `POST /api/v1/auth` ile kullanıcı oluşturulur (`username`, `password`).
2. `POST /api/v1/auth/token` ile kullanıcı adı/şifre gönderilip JWT token alınır.
3. Sonraki isteklerde `Authorization: Bearer <token>` header'ı ile korumalı endpoint'lere erişilir.

## Test

```bash
./mvnw test
```

Testler: handler birim testleri (Mockito) ve `@WebMvcTest` ile Spring Security filtre zinciri (`SecurityConfigSadPathTest`) doğrulaması içerir.

## Kaynak

Bu proje, `src/main/resources/Spring_Boot_Mastery_Ön_Hazırlık.pdf` eğitim dökümanındaki teorik konuların pratik karşılıklarını içerecek şekilde geliştirilmiştir.
