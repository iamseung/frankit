# 상품 및 옵션 관리 API 개발

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
  
## 📚 회고

유지보수성과 확장성을 고려한 코드 설계에 집중했습니다. 객체지향적인 설계 원칙과 확장성을 고려했으며 API 설계 시 기존 구조를 변경하지 않고 확장 가능하도록 설계하려고 노력했습니다.
하지만 주어진 시간 내에 기능을 구현해야 하는 상황에서 실제 운영 환경에서 복잡한 쿼리에 대한 튜닝과 실행 계획 분석이 부족했으며, 테스트 또한 부족했다고 생각합니다.

복잡한 쿼리 튜닝, 성능 최적화, API의 실제 데이터 흐름을 검증하는 테스트 강화를 목표로 개선해 나가야겠습니다.