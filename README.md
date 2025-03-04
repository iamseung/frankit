# 상품 및 옵션 관리 API 개발 with JPA

## 📘 API 명세서
보다 자세한 사항과 테스트는 Swagger 를 통해서 확인하실 수 있습니다.

### **회원**
| HTTP Method | Endpoint            | 설명 | 인증 여부 |
|------------|--------------------|------------------------------|----------|
| `POST`     | `/api/users/signup` | 사용자 회원가입 | ❌ |
| `POST`     | `/api/users/login`  | 사용자 로그인 | ❌ |

### **상품**
| HTTP Method | Endpoint                      | 설명 | 인증 여부 |
|------------|--------------------------------|------------------------------|----------|
| `GET`      | `/api/products/{productId}`    | 특정 상품 조회 | ❌ |
| `PUT`      | `/api/products/{productId}`    | 특정 상품 수정 | 🔒 |
| `DELETE`   | `/api/products/{productId}`    | 특정 상품 삭제 | 🔒 |
| `GET`      | `/api/products`                | 전체 상품 목록 조회 | ❌ |
| `POST`     | `/api/products`                | 상품 등록 | 🔒 |

### **상품 옵션**
| HTTP Method | Endpoint                        | 설명 | 인증 여부 |
|------------|--------------------------------|------------------------------|----------|
| `GET`      | `/api/options/{optionId}`      | 특정 옵션 조회 | ❌ |
| `PUT`      | `/api/options/{optionId}`      | 특정 옵션 수정 | 🔒 |
| `DELETE`   | `/api/options/{optionId}`      | 특정 옵션 삭제 | 🔒 |
| `POST`     | `/api/options/{productId}`     | 특정 상품에 옵션 추가 | 🔒 |
| `GET`      | `/api/options/{productId}/all` | 특정 상품의 모든 옵션 조회 | ❌ |
| `DELETE`   | `/api/options/{productId}/all` | 특정 상품의 모든 옵션 삭제 | 🔒 |


🔒 아이콘이 표시된 API는 **로그인이 필요한 API**입니다.

---

## 코드 구조

```
.
├── FrankitApplication.java
├── FrankitControllerAdvice.java
├── config
│   ├── JpaConfig.java
│   ├── SecurityConfig.java
│   └── SwaggerConfig.java
├── controller
│   ├── ProductController.java
│   ├── ProductOptionController.java
│   └── UserController.java
├── dto
│   ├── request
│   │   ├── product
│   │   │   ├── ProductDto.java
│   │   │   ├── ProductOptionDto.java
│   │   │   ├── ProductOptionRequestDto.java
│   │   │   ├── ProductOptionsRequestDto.java
│   │   │   ├── ProductRequestDto.java
│   │   │   ├── SelectOptionRequestDto.java
│   │   │   └── SingleOptionRequestDto.java
│   │   └── user
│   │       ├── LoginRequestDto.java
│   │       ├── SignUpRequestDto.java
│   │       └── UserDto.java
│   ├── response
│   │   ├── ApiSuccessResponse.java
│   │   └── product
│   │       ├── ProductApiResponseDto.java
│   │       ├── ProductOptionResponseDto.java
│   │       ├── SelectOptionResponseDto.java
│   │       └── SingleOptionResponseDto.java
│   └── security
│       └── CustomUserDetails.java
├── entity
│   ├── product
│   │   ├── AuditingFields.java
│   │   ├── OptionType.java
│   │   ├── Product.java
│   │   ├── ProductOption.java
│   │   ├── SelectOption.java
│   │   └── SingleOption.java
│   └── user
│       └── User.java
├── exception
│   ├── ProductErrorCode.java
│   ├── ProductException.java
│   ├── UserErrorCode.java
│   └── UserException.java
├── jwt
│   ├── JwtAuthenticationFilter.java
│   └── JwtUtil.java
├── repository
│   ├── ProductOptionRepository.java
│   ├── ProductRepository.java
│   └── UserRepository.java
├── service
│   ├── CustomUserDetailsService.java
│   ├── ProductOptionService.java
│   ├── ProductService.java
│   └── UserService.java
└── util
└── OptionParser.java
```

### 주요 파일 및 디렉토리 설명

- config : 프로젝트 설정 관련 파일 (JPA, Security, Swagger 설정 등)
- controller : API 요청을 처리하는 컨트롤러 클래스
- dto : 데이터 전송 객체 (DTO), 요청/응답을 처리
  - request : 클라이언트에서 들어오는 요청을 위한 DTO
  - response : API 응답을 위한 DTO
- entity : 데이터베이스 테이블과 매핑되는 JPA 엔티티 클래스
- exception : 예외 및 에러 코드 정의
- jwt : JWT 인증 관련 클래스
- repository : JPA Repository 인터페이스 (DB 접근)
- service : 비즈니스 로직을 처리하는 서비스 계층
- util : 유틸리티 기능을 수행하는 클래스

---

## ✅ 고려 사항

### 1. 보안 및 인증
- 모든 `PUT`, `DELETE`, `POST` 요청에는 `JWT` 기반 인증이 필요합니다.
- **사용자 권한 검증**
   - 특정 상품/옵션을 수정 또는 삭제할 때 **본인이 등록한 항목인지 확인** 후 처리 (`validationUserAuth`)
   - `403 Forbidden` 응답을 통해 권한이 없는 사용자는 접근 불가
- 비밀번호는 `BCrypt`를 사용하여 암호화 (`PasswordEncoder` 적용)

### 2. 확장성을 고려한 코드 설계
- **옵션 타입 확장 (`ProductOption`)**

   - 현재 `SINGLE`, `SELECT` 옵션만 지원하지만, 새로운 옵션 타입을 추가하려면 `ProductOption`을 상속하여 새로운 클래스를 추가하면 되게 설계했습니다.
  ```java
  @DiscriminatorValue(value = "CUSTOM")
  public class CustomOption extends ProductOption {
      // 새로운 옵션 타입 추가 가능
  }
  ```
- **확장 가능한 옵션 Dto**
  - `ProductOptionRequestDto`는 `JsonTypeInfo`와 `JsonSubTypes`를 사용하여 다양한 옵션 유형(SINGLE, SELECT 등) 지원이 가능하도록 설계되었습니다.
  ```java
    @Getter
    @Setter
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "optionType", visible = true, defaultImpl = SingleOptionRequestDto.class)
    @JsonSubTypes({
        @JsonSubTypes.Type(value = SingleOptionRequestDto.class, name = "SINGLE"),
        @JsonSubTypes.Type(value = SelectOptionRequestDto.class, name = "SELECT")
    })
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    public abstract class ProductOptionRequestDto {
    private String productOptionName;
    private Long additionalPrice;
    private OptionType optionType;

    public abstract Object getValue();
    }
  ```
