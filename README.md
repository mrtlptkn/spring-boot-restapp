# Spring Boot REST App

Spring Framework & Spring Boot ile kurumsal uygulama geliştirme prensiplerini (Clean Architecture / N-Tier mimari, DI, AOP, JPA, Security, Validation vb.) uçtan uca örnekleyen bir REST API projesi.

## Teknoloji Yığını

- **Java 17**, **Spring Boot 3.5.7**
- Spring Web (REST), Spring Data JPA (Hibernate), Spring Security + JWT (jjwt)
- Spring AOP, Spring Validation, Spring Boot Actuator
- H2 (dosya tabanlı, iki ayrı veritabanı: primary + secondary) veya `postgres` profiliyle PostgreSQL (Docker)
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
| `dev` (varsayılan) | Yerel geliştirme | Detaylı SQL logu, `ddl-auto=update`, mock email sağlayıcı (`turkcell`), H2 |
| `prod` | Canlı ortam | Minimal log, `ddl-auto=validate`, gerçek email sağlayıcı (`sendGrid`), actuator health detayları kapalı, H2 |
| `postgres` | PostgreSQL ile yerel/entegrasyon testi | Primary + secondary veri kaynağı Docker'daki PostgreSQL'e bağlanır (bkz. [PostgreSQL ile Çalıştırma](#postgresql-ile-çalıştırma)) |

Profil değiştirmek için:

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

### Veritabanı

Varsayılan (`dev`/`prod`) profillerde iki ayrı H2 dosya veritabanı kullanılır (`PrimaryDatabaseConfig`, `SecondaryDatabaseConfig`):

- Primary: `jdbc:h2:file:C:/data/testdb`
- Secondary: `jdbc:h2:file:C:/data/secondarydb`

H2 konsoluna erişim: `http://localhost:8080/h2-console`

### PostgreSQL ile Çalıştırma

`postgres` profili, hem primary hem secondary veri kaynağını H2 yerine Docker üzerinde çalışan bir PostgreSQL örneğine bağlar (`application-postgres.properties`).

**1) PostgreSQL container'ını başlat** (proje kökündeki `docker-compose.yml`):

```bash
docker compose up -d
```

```yaml
# docker-compose.yml
services:
  postgres:
    image: postgres:15
    container_name: my_postgres
    environment:
      POSTGRES_USER: root
      POSTGRES_PASSWORD: admin
      POSTGRES_DB: testdb
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
```

**2) Uygulamayı `postgres` profiliyle başlat** — aşağıdaki yöntemlerden biriyle:

> ⚠️ **Windows PowerShell'de en sık yapılan hata:** `-Dspring-boot.run.profiles=postgres` parametresini **tırnaksız** yazmak. PowerShell bu argümanı `-D`'den sonraki `.` karakterinde yanlış böler ve Maven'e iki ayrı parametre gibi iletir; sonuç olarak `[ERROR] Unknown lifecycle phase ".run.profiles=postgres"` hatasıyla derleme bile başlamadan başarısız olur. Bu yüzden PowerShell'de `-D...` parametresini **mutlaka çift tırnak içinde** verin (aşağıdaki (a) yöntemi), ya da tamamen bu sorunu ortadan kaldıran (b) ortam değişkeni yöntemini kullanın.

**a) Maven Wrapper — Windows PowerShell (tırnak zorunlu):**

```powershell
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=postgres"
```

**b) Maven Wrapper — Windows PowerShell, ortam değişkeniyle (önerilen, tırnak sorunu yaşatmaz):**

```powershell
$env:SPRING_PROFILES_ACTIVE = "postgres"
.\mvnw.cmd spring-boot:run
```

**c) Maven Wrapper — CMD:**

```cmd
mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=postgres
```

**d) Maven Wrapper — Git Bash / Linux / macOS:**

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

**e) IntelliJ IDEA (Run/Debug Configuration üzerinden):**

1. `Run > Edit Configurations...` açın, `SpringBootRestappApplication` konfigürasyonunu seçin.
2. `Modify options > Add VM options` ile VM options alanına ekleyin: `-Dspring.profiles.active=postgres`
   — veya `Environment variables` alanına `SPRING_PROFILES_ACTIVE=postgres` ekleyin.
3. `Apply` > `Run` (▶) ile başlatın.

**f) Derlenmiş jar ile:**

```powershell
.\mvnw.cmd clean package -DskipTests
java -jar target\spring-boot-restapp-0.0.1-SNAPSHOT.jar --spring.profiles.active=postgres
```

Başarılı bir başlangıçta log'un sonunda şu satırları görmelisiniz (Postgres'e bağlandığının kanıtı):

```
HikariPool-1 - Added connection org.postgresql.jdbc.PgConnection@...
Database version: 15.18
Tomcat started on port 8080 (http) with context path '/'
Started SpringBootRestappApplication in ... seconds
```

> Aynı anda `dev`/`prod` profiliyle çalışan başka bir örnek (ör. IDE'de) varsa, port çakışmasını önlemek için `--server.port=8081` gibi bir parametre de ekleyin.

`spring.jpa.hibernate.ddl-auto=update` sayesinde tablolar (`users`, `roles`, `user_roles`, `categories`, `products`, `product_prices`, `courses`) ilk açılışta Hibernate tarafından otomatik oluşturulur; ek bir migration betiğine gerek yoktur.

**3) Doğrulama** — bu profil gerçek bir Postgres container'ına karşı test edilmiştir: kayıt (`POST /api/v1/auth`), token alma (`POST /api/v1/auth/token`), ürün oluşturma (`POST /api/v1/products`) ve kurs oluşturma (`POST /api/v1/courses`) uçları çalıştırılmış; veriler `docker exec my_postgres psql -U root -d testdb -c "SELECT * FROM products;"` ile veritabanında doğrulanmıştır.

> Not: `application.properties`'teki H2 dosyaları (`C:/data/testdb`, `C:/data/secondarydb`) aynı anda yalnızca tek bir JVM tarafından açılabilir. `dev`/`prod` profiliyle çalışan bir örnek zaten açıkken (ör. IDE'den başlatılmış), aynı makinede ikinci bir örneği yalnızca farklı bir `--server.port` ile ve tercihen `postgres` profiliyle (H2 dosya kilidi olmadığı için) çalıştırabilirsiniz.

## API Uç Noktaları (özet)

Tüm iş endpoint'leri `/api/v1/...` altında versiyonlanmıştır.

| Controller | Base Path | Not |
|---|---|---|
| `AuthController` | `/api/v1/auth` | Kayıt (`POST /`), token üretme (`POST /token`) — herkese açık |
| `ProductController` | `/api/v1/products` | CRUD + `PATCH /{id}/discount` (indirim, `@Transactional`) |
| `CategoryController` | `/api/v1/categories` | Listeleme (`GET /`), oluşturma (`POST /`), silme, pagination/sorting örneği |
| `CourseController` | `/api/v1/courses` | İkinci veritabanı (secondarydb) örneği |
| `AdminController` | `/api/v1/admins` | `ROLE_ADMIN` + `ROLE_MANAGER` gerektirir |
| `DemoController` | `/api/v1/demo` | AOP ve `@Lazy`/circular dependency demoları |

Swagger UI: `http://localhost:8080/swagger-ui.html`

## Kimlik Doğrulama Akışı ve Giriş Yapılacak Hesap

Projede **önceden tanımlı/seed edilmiş bir kullanıcı yoktur** (`data.sql` veya `CommandLineRunner` ile kullanıcı oluşturma yapılmaz). Giriş yapabilmek için önce kendi hesabınızı kayıt etmeniz gerekir:

1. **Kayıt ol:** `POST /api/v1/auth` (herkese açık)
2. **Token al:** `POST /api/v1/auth/token` (herkese açık) — kullanıcı adı/şifre doğrulanır, JWT döner
3. **Korumalı endpoint'lere eriş:** `Authorization: Bearer <token>` header'ı ile istek at

```bash
# 1) Kayıt ol
curl -X POST http://localhost:8080/api/v1/auth \
  -H "Content-Type: application/json" \
  -d '{"username":"mert","password":"P@ssword1"}'
# 200 OK (body boş döner)

# 2) Token al
curl -X POST http://localhost:8080/api/v1/auth/token \
  -H "Content-Type: application/json" \
  -d '{"username":"mert","password":"P@ssword1"}'
# 200 OK -> {"accessToken":"eyJhbGciOiJIUzI1NiJ9..."}

# 3) Token'ı kullan
curl http://localhost:8080/api/v1/products/1 \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

Bu şekilde oluşturulan bir kullanıcının **hiçbir rolü yoktur**; yalnızca `.anyRequest().authenticated()` kuralına giren endpoint'lere (ör. `/api/v1/products/**`) erişebilir.

### Rol gerektiren endpoint'lere erişim (Admin / Demo)

`AdminController` (`hasRole('ADMIN') and hasAuthority('ROLE_MANAGER')`) ve `DemoController`'ın `/api/v1/demo/**` altındaki uçları (`hasAuthority('ROLE_MANAGER')`) rol ister. Rol atamak için self-servis bir endpoint bulunmadığından, rolleri H2 konsolundan (`http://localhost:8080/h2-console`, JDBC URL: `jdbc:h2:file:C:/data/testdb`, kullanıcı: `sa`, şifre: `password`) manuel eklemeniz gerekir:

```sql
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');
INSERT INTO roles (name) VALUES ('ROLE_MANAGER');

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r
WHERE u.username = 'mert' AND r.name IN ('ROLE_ADMIN', 'ROLE_MANAGER');
```

> Roller mevcut bir JWT'ye sonradan eklenmez; SQL'i çalıştırdıktan sonra **`/api/v1/auth/token` ile tekrar giriş yapıp yeni bir token almanız gerekir** — yeni token, o an veritabanındaki güncel rolleri içerir.

## Endpoint Kullanım Örnekleri

Aşağıdaki tüm örneklerde `TOKEN` değişkeni, yukarıdaki token alma adımından dönen `accessToken` değeridir. `/api/v1/auth/**`, `/api/v1/categories/**` ve `/api/v1/courses/**` `permitAll` olduğundan token gerektirmez; diğer tüm endpoint'ler en az geçerli bir JWT ister.

### Auth (`/api/v1/auth`) — herkese açık

```bash
# Auth controller ayakta mı?
curl http://localhost:8080/api/v1/auth
# -> "Auth Controller is working..."
```

### Product (`/api/v1/products`) — kimlik doğrulama gerektirir

**Ürün oluştur** — `productName` en fazla 20 karakter, `test/deneme/asdf/sample/örnek` gibi placeholder değerler yasaktır (özel `@NotReservedProductName` validator); `unitsInStock` 1-10 arası olmalıdır:

```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"productName":"Kalem","unitPrice":19.90,"unitsInStock":5}'
# 201 Created, Location: /api/v1/products/1
# -> {"productId":1,"productName":"Kalem","unitPrice":19.90,"unitsInStock":5}
```

Validasyon hatası örneği (`productName` yasaklı liste veya diğer kurallara aykırıysa) `ErrorConfig` tarafından 400 ile şu formatta döner:

```json
{
  "productName": ["Product name must not be a reserved/placeholder value (e.g. 'test', 'deneme')"]
}
```

**Id ile ürün getir:**

```bash
curl http://localhost:8080/api/v1/products/1 -H "Authorization: Bearer $TOKEN"
# -> {"productId":1,"productName":"Kalem","unitPrice":19.90,"unitsInStock":5}
```

**Ürün güncelle** (path'teki `id` ile body'deki `productId` eşleşmelidir):

```bash
curl -X PUT http://localhost:8080/api/v1/products/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"productId":1,"productName":"Kalem Kutulu","unitPrice":24.90,"unitsInStock":8}'
# -> {"message":"Product updated successfully"}
```

**Ürün sil:**

```bash
curl -X DELETE http://localhost:8080/api/v1/products/1 -H "Authorization: Bearer $TOKEN"
# -> {"message":"Kalem Product deleted successfully"}
```

**Fiyata indirim uygula** (path'teki `id` ile body'deki `productId` eşleşmelidir; işlem `@Transactional`'dır, fiyat güncelleme + `ProductPrice` geçmiş kaydı tek transaction'da yürütülür):

```bash
curl -X PATCH http://localhost:8080/api/v1/products/1/discount \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"productId":1,"newPrice":14.90}'
# -> {"productId":1,"oldPrice":19.90,"newPrice":14.90}
```

### Category (`/api/v1/categories`) — herkese açık

**Tüm kategorileri getir** (parametresiz):

```bash
curl http://localhost:8080/api/v1/categories
# -> [{"categoryId":1,"categoryName":"Kırtasiye","products":[]},{"categoryId":2,"categoryName":"Elektronik","products":[]}]
```

**Kategori oluştur:**

```bash
curl -X POST http://localhost:8080/api/v1/categories \
  -H "Content-Type: application/json" \
  -d '{"categoryName":"Kırtasiye"}'
# 201 Created, Location: /api/v1/categories/1
# -> {"categoryId":1,"categoryName":"Kırtasiye","products":null}
```

```bash
# Kategori adını getir (kategorinin ilk ürünü üzerinden)
curl "http://localhost:8080/api/v1/categories?id=1"
# -> "Category Name: Kalem"

# Kategoriyi ürünleriyle birlikte getir (Entity)
curl "http://localhost:8080/api/v1/categories/withProducts?id=1"

# Kategoriyi ürünleriyle birlikte getir (DTO/ModelMapper versiyonu)
curl "http://localhost:8080/api/v1/categories/withProductsDtoVersion?id=1"
# -> {"categoryId":1,"categoryName":"Kırtasiye","products":[{"productId":1,"productName":"Kalem","unitPrice":19.90,"unitsInStock":5}]}

# Kategori sil
curl -X DELETE http://localhost:8080/api/v1/categories/1

# Fiyat aralığına göre ürün ara
curl "http://localhost:8080/api/v1/categories/findProductBetweenPrices?min=10&max=50"

# Aynısı DTO versiyonu ile
curl "http://localhost:8080/api/v1/categories/findProductBetweenPricesDtoVersion?min=10&max=50"

# Sayfalama ve sıralama (fiyata göre artan, isme göre azalan)
curl "http://localhost:8080/api/v1/categories/paginationAndSorting?page=0&size=10"
```

### Course (`/api/v1/courses`) — herkese açık

```bash
curl -X POST http://localhost:8080/api/v1/courses
# Sabit "Java Spring Boot" adlı bir kurs oluşturur (secondarydb'ye kaydedilir)
# -> "Course created"
```

### Admin (`/api/v1/admins`) — `ROLE_ADMIN` + `ROLE_MANAGER` gerektirir

```bash
curl http://localhost:8080/api/v1/admins -H "Authorization: Bearer $TOKEN"
# Roller atanmadıysa 403 Forbidden; atandıysa -> "Admin and user data accessed."
```

### Demo (`/api/v1/demo`) — DI/AOP/Circular Dependency örnekleri

```bash
# /** için ROLE_MANAGER gerekir (SecurityConfig)
curl http://localhost:8080/api/v1/demo -H "Authorization: Bearer $TOKEN"
curl -X POST http://localhost:8080/api/v1/demo -H "Authorization: Bearer $TOKEN"

# @Lazy ile çözülen circular dependency (ServiceA <-> ServiceB) örneği
curl http://localhost:8080/api/v1/demo/circular -H "Authorization: Bearer $TOKEN"
```

### Actuator — kısmen herkese açık

```bash
curl http://localhost:8080/actuator/health   # permitAll
curl http://localhost:8080/actuator/info     # permitAll
curl http://localhost:8080/actuator/metrics -H "Authorization: Bearer $TOKEN"  # authenticated gerektirir
```

## Test

```bash
./mvnw test
```

Testler: handler birim testleri (Mockito) ve `@WebMvcTest` ile Spring Security filtre zinciri (`SecurityConfigSadPathTest`) doğrulaması içerir.

## Kaynak

Bu proje, `src/main/resources/Spring_Boot_Mastery_Ön_Hazırlık.pdf` eğitim dökümanındaki teorik konuların pratik karşılıklarını içerecek şekilde geliştirilmiştir.
