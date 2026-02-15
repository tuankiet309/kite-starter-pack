# 🪁 Kite Starter Pack

> **A production-ready Spring Boot library** designed to bootstrap microservices with best-practice infrastructure.

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen)]()
[![Java 21](https://img.shields.io/badge/Java-21-orange)]()
[![Spring Boot 3.2.0](https://img.shields.io/badge/Spring%20Boot-3.2.0-green)]()

---

## 📖 Philosophy: Library, Not Framework

**Kite is a library.** It respects your code.

1.  **Opt-in**: You only import the modules you need.
2.  **Overridable**: Every bean is `@ConditionalOnMissingBean`. Define your own, and ours backs off.
3.  **Unintrusive**: No magic scanning of your packages. It just provides helpful beans in the context.

---

## � Installation

Add individual modules to your `build.gradle` dependencies:

```gradle
dependencies {
    // 1. Core Utilities (JSON, Date, String)
    implementation project(':kite-starter-core')

    // 2. Web API Standards (ApiResponse, GlobalExceptionHandler)
    implementation project(':kite-starter-web')

    // 3. Security (JWT, Secrets)
    implementation project(':kite-starter-security')

    // 4. Data Persistence (JPA Auditing, Soft Delete)
    implementation project(':kite-starter-data')

    // 5. Redis (Cache, Distributed Lock)
    implementation project(':kite-starter-redis')

    // 6. External Storage (Local Disk / S3)
    implementation project(':kite-starter-storage')

    // 7. HTTP Client (Resilience, Logging)
    implementation project(':kite-starter-http')
}
```

---

## 📚 Module Reference

### 1. kite-starter-web
> **Standardize your API layer.**

**Features:**
*   **Unified Response**: All endpoints return `{ code, message, data, timestamp }`.
*   **Global Exception Handler**: Automatically converts exceptions to JSON errors.
*   **API Versioning**: Configurable prefix support.

**Configuration:**
```yaml
kite:
  web:
    api:
      enabled: true
      prefix: /api   # Base path
      version: v1    # Version segment
```

**Usage:**

*   **Standard Success Response:**
    ```java
    @GetMapping("/{id}")
    public ApiResponse<UserDto> getUser(@PathVariable Long id) {
        return ApiResponse.success(userService.findById(id));
    }
    ```

*   **Throwing Errors:**
    ```java
    if (user == null) {
        throw new BusinessException(ErrorCode.USER_NOT_FOUND, "User ID not found");
    }
    ```
    *Result:* `HTTP 404 { "code": 404, "message": "User ID not found", ... }`

---

### 2. kite-starter-security
> **Stateless JWT Security out of the box.**

**Features:**
*   **JWT Filter**: Validates `Authorization: Bearer <token>`.
*   **Token Utils**: Generate Access/Refresh tokens.
*   **CORS**: Pre-configured for modern web apps.

**Configuration:**
```yaml
kite:
  security:
    enabled: true
    jwt:
      secret-key: "YOUR_VERY_LONG_SECRET_KEY_MUST_BE_At_LEAST_32_CHARS"
      access-token-validity: 1h
      refresh-token-validity: 7d
    public-endpoints:
      - /auth/**
      - /public/**
```

**Usage:**

*   **Generating Tokens (Login):**
    ```java
    @Autowired
    private JwtUtil jwtUtil;

    public String login(String username) {
        return jwtUtil.generateAccessToken(username, Map.of("role", "ADMIN"));
    }
    ```

*   **Securing Endpoints:**
    Standard Spring Security annotations work as expected.
    ```java
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) { ... }
    ```

---

### 3. kite-starter-data
> **JPA Supercharged.**

**Features:**
*   **Auditing**: `created_by`, `updated_at` automatically filled.
*   **Soft Delete**: Records are marked deleted instead of removed.
*   **UUID Support**: Pre-configured UUID entities.

**Usage:**

*   **Option A: UUID + Soft Delete (Recommended)**
    ```java
    @Entity
    public class User extends BaseUuidSoftDeleteEntity {
        // Automatically has: String id (UUID), createdAt, updatedAt, deleted
    }
    ```

*   **Option B: Custom ID + Soft Delete**
    ```java
    @Entity
    public class Product extends BaseSoftDeleteEntity {
        @Id
        private Long id; // Use Long ID
    }
    ```

---

### 4. kite-starter-redis
> **Caching & Distributed Locks.**

**Features:**
*   **RedisUtil**: Typed helper methods for common operations.
*   **@DistributedLock**: Annotation to prevent concurrent execution across nodes.

**Configuration:**
```yaml
kite:
  redis:
    host: localhost
    port: 6379
    password: secret
    pool:
      max-active: 16
```

**Usage:**

*   **Caching:**
    ```java
    @Autowired private RedisUtil redisUtil;

    redisUtil.set("user:1", userDto, Duration.ofMinutes(10));
    UserDto cached = (UserDto) redisUtil.get("user:1");
    ```

*   **Distributed Locking:**
    ```java
    @DistributedLock(key = "'report_' + #date", timeout = 5000)
    public void generateDailyReport(String date) {
        // Only ONE server will execute this at a time
    }
    ```

---

### 5. kite-starter-storage
> **File Uploads Abstraction.**

**Features:**
*   **Switchable Backend**: Use `LOCAL` for dev, `MINIO` (S3) for prod.
*   **Extensions**: Validation for allowed file types.

**Configuration:**
```yaml
kite:
  resources:
    type: MINIO # or LOCAL
    allowed-extensions: jpg,png,pdf
    max-file-size: 10485760 # 10MB
    minio:
      url: http://minio:9000
      bucket: my-bucket
      access-key: minio
      secret-key: minio123
```

**Usage:**
```java
@Autowired
private FileUploadUtil fileUploadUtil;

public String uploadAvatar(MultipartFile file) {
    // Returns URL (MinIO) or File Path (Local)
    return fileUploadUtil.uploadFile(file, "avatars");
}
```

---

### 6. kite-starter-http
> **Robust External API Calls.**

**Features:**
*   **Resilience**: Circuit Breaker & Retry patterns built-in.
*   **Logging**: Logs request/response for debugging.

**Configuration:**
```yaml
kite:
  http-client:
    connect-timeout: 5s
    read-timeout: 10s
    logging: true
```

**Usage:**
```java
@Autowired
private KiteHttpClient client;

public Weather getWeather(String city) {
    return client.get("https://api.weather.com/" + city, Weather.class);
}
```

---

### 7. kite-starter-core
> **Essential Utilities.**

**Key Classes:**
*   **`JsonUtils`**: Jackson wrapper. `toJson(obj)`, `fromJson(json, Class)`. No checked exceptions.
*   **`BeanUtils`**: `copyProperties(source, Target.class)`.
*   **`StringUtils`**: `toSlug("Hello World")` -> `"hello-world"`.

---

## 🎨 Extension Guide

**Requirement:** You need custom logic that differs from the library default.

**Solution:** Define your own Bean.

Since all Library beans are conditional, **your bean wins**.

*Example: Custom File Validator*
```java
@Configuration
public class MyConfig {

    // Naming the method 'fileValidator' replaces the library's default validator
    @Bean
    public FileValidator fileValidator(KiteResourceProperties props) {
        return new MyComplexFileValidator(props);
    }
}
```

---

## 🏗️ Build & Test

```bash
# Build entire project
./gradlew clean build

# Publish to local maven (for use in other local projects)
./gradlew publishToMavenLocal
```

---
**Happy Coding!** 🚀

## 📄 License
MIT
