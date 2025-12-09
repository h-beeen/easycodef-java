<br>
<br>
<p align="center">
  <a title="코드에프" href="https://codef.io/">
    <picture>
      <source media="(prefers-color-scheme: dark)" srcset="https://github.com/user-attachments/assets/d83f0450-d84e-4594-8fc0-ed08a1d05390">
      <img alt="코드에프" src="https://github.com/user-attachments/assets/cd2e9709-742f-4e27-b695-4515bdbb7cc0" width="250">
    </picture>
  </a>
</p>

<p align="center">
  <span><code>easycodef-java</code><br><b>Open-Source Library</b><br>For the <b>CODEF API</b></span>
</p>

<p align="center">
  <a href="https://github.com/codef-io/easycodef-java"><img align="center"  src="https://img.shields.io/github/last-commit/codef-io/easycodef-java/master?style=for-the-badge&label=LAST%20BUILD&logo=Github&color=181717" alt="Last Commit"/></a>
  <a href="https://central.sonatype.com/artifact/io.codef.api/easycodef-java/1.0.6"><img align="center"  src="https://img.shields.io/maven-central/v/io.codef.api/easycodef-java.svg?style=for-the-badge&label=Maven%20Central&logo=apache-maven&color=C71A36" alt="Maven Central"/></a>

> [!IMPORTANT]
> - [홈페이지](https://codef.io/)
> - [개발가이드](https://developer.codef.io/)

> [!NOTE]
> - 기존 `1.0.0`~`1.0.6` 버전 코드에 대한 설명은 [Legacy EasyCodef](https://www.naver.com)을 참고해주세요
> - `2.0.0` 버전은 기존 `1.0.x` 버전에서 EasyCodef 객체를 활용한 호출 방법도 하위호환으로 지원합니다. `@Deprecated`

## Getting Started

- Gradle (Groovy DSL)
```gradle
  implementation 'io.codef.api:easycodef-java:2.0.0'
```

- Gradle (Kotlin DSL)
```gradle
  implementation ("io.codef.api:easycodef-java:2.0.0")
```

- Maven
```xml
<dependencies>
    <dependency>
        <groupId>io.codef.api</groupId>
        <artifactId>easycodef-java</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

> [!IMPORTANT]
> 지원 JDK : Java 1.8+
> JDK 1.8 미만 사용자는 TLS 버전 호환성 및 하위호환 언어 지원 불가로 사용이 어렵습니다.
> 본 README.md의 예제는 JDK 1.8, Spring Framework, Spring Boot 2.x.x 환경에서 연동 예제를 기술해요.

### 1. EasyCodefClient 객체 생성

> [!NOTE] 
> EasyCodefClient 객체는 모든 CODEF API 상품 요청을 위해 필요해요.  
> 토큰의 발급과 재사용을 자동으로 처리하며, 유효기간이 만료되는 경우 재발급 또한 자동으로 처리해요.  
> EasyCodefClient 객체는 EasyCodefBuilder 생성자를 통해 생성할 수 있어요.   
> EasyCodefClient 객체는 `@Bean`으로 스프링 빈으로 등록해 재사용할 것을 권장해요.

> [!IMPORTANT]
> 시크릿 키 값은 [Codef 홈페이지](https://codef.io/) 회원가입 후, [MY PAGE > 키 관리](https://codef.io/account/keys)에서 확인할 수 있어요.<br>
> 해당 키 값은 Codef API를 사용하기 위한 인증 정보를 포함해요. 외부에 노출되지 않도록 관리해주세요.<br><br>
> <img src="https://github.com/user-attachments/assets/42ef24ec-92d3-435c-b9ac-36befe81abc4" width=500px>

> [!NOTE]
> EasyCodefClient 객체
> - path: 실제 Codef API에 요청할 경로. `/v1/kr/***/` 형태의 String 값<br>
> - organization : 모든 Codef API의 필수 값. 각 상품 개발가이드에 적힌 고정 값 입력
> - 이 외 요청부를 개발가이드 `입력부(Input)` 에 맞게 입력해주세요.

```java
EasyCodefClient easyCodefClient = EasyCodefBuilder.builder()
	.serviceType(CodefServiceType.DEMO) // 클라이언트 정보가 데모버전일 경우 선택 [중복 선택 불가]
	.serviceType(CodefServiceType.API)  // 클라이언트 정보가 정식버전일 경우 선택 [중복 선택 불가] 

	.clientId("your-client-id")
	.clientSecret("your-client-secret")
	.publicKey("your-public-key")
	.build();
```

> [!WARNING]  
> EasyCodefClient는 빌더 패턴 생성자 검증 과정에서 아래의 예외 사항들을 같이 검증하고, 검증에 실패하면 예외를 반환합니다.
> - `clientId`, `clientSecret`, `publicKey`값이 `null`일 경우
> - CODEF OAuth API와 통신에 실패하는 경우 (https://oauth.codef.io)


### 2. 상품 요청 객체 생성

EasyCodef에서 CODEF API 상품을 요청하기 위해서는 EasyCodefRequest 객체가 필요해요.
해당 객체에는 호출하고자 하는 상품의 EndPoint(URL)와 요청에 필요한 파라미터 정보(Map 형태)를 포함해요.

> [!NOTE]  
> EasyCodefRequest 객체는 EasyCodefRequestBuilder 빌더 패턴을 통해 생성할 수 있어요.
> - EndPoint는 host를 제외한 `/v1/***` 형식으로 구성해요.
> - 파라미터 정보는 <"key", value> 형식으로 구성해요.
>
> 본 예제는 [건강보험공단 > 건강검진결과](https://developer.codef.io/products/public/each/pp/nhis-health-check),  
> [커넥티드 아이디 > 계정 등록](https://developer.codef.io/common-guide/connected-id/register) 기반으로 작성되었어요.
>
> `Connected Id`에 대한 자세한 내용은 [커넥티드 아이디 > 개요](https://developer.codef.io/common-guide/connected-id/cid)를 참고하세요.

- 일반 상품 [[건강보험공단 > 건강검진결과]](https://developer.codef.io/products/public/each/pp/nhis-health-check) 파라미터 구성 예시

```java
Map<String, Object> requestParams = new HashMap<>();
requestParams.put("organization",    "0002");
requestParams.put("loginType",       "5");
requestParams.put("loginTypeLevel",  "1");
requestParams.put("identity",        "19990101");
requestParams.put("userName",        "홍길동");
requestParams.put("telecom",         "1");
requestParams.put("phoneNo",         "01012345678");
requestParams.put("searchStartYear", "2023");
requestParams.put("searchEndYear",   "2023");
requestParams.put("inquiryType",     "4");
```

- EasyCodefRequest 객체 생성

```java
EasyCodefRequest request = EasyCodefRequestBuilder.builder()
	.productUrl("/v1/kr/public/pp/nhis-health-checkup/result")
	.parameterMap(requestParams)
	.build();
```

> [!WARNING]  
> EasyCodefRequestBuilder 생성자는 아래의 예외 사항들을 같이 검증하고, 검증에 실패하면 예외를 반환해요.
> - `productUrl`값이 `null`이거나 `/v1/***` 형식을 따르지 않는 경우
> - `parameterMap`값이 `null`일 경우

- 암호화가 필요한 상품 [[커넥티드 아이디 > 계정 등록]](https://developer.codef.io/common-guide/connected-id/register) 파라미터 구성 예시

```java
import io.codef.api.util.RsaUtil;

List<Map<String, Object>> accountList = new ArrayList<>();
Map<String, Object> accountMaps = new HashMap<>();
accountMap.put("countryCode",  "KR");
accountMap.put("businessType", "CD");
accountMap.put("clientType",   "P");
accountMap.put("organization", "0003");
accountMap.put("loginType",    "1");
accountMap.put("id",           "user_id");

accountMap.put("password", RsaUtil.encryptRsa("user_password", easyCodef.getPublicKey()));

accountList.add(accountMap);

Map<String, Object> parameterMap = new HashMap<>();
parameterMap.put("accountList",accountList);
```

> [!IMPORTANT]  
> 주민등록번호, 비밀번호 등 민감한 데이터는 반드시 RSA 암호화를 진행해야 합니다.
> ```java
> RsaUtil.encryptRsa("user_password", "your-public-key");
> ```

- EasyCodefRequest 객체 생성

```java
EasyCodefRequest requestCid = EasyCodefRequestBuilder
	.builder()
	.productUrl("/v1/account/create")
	.parameterMap(parameterMap)
	.build();
```

### 3. CODEF API 호출

> [!NOTE]  
> EasyCodef를 활용해 상품을 호출하기 위해서는 아래 과정이 선행되어야 합니다.
> 1. EasyCodef 객체 생성
> 2. 상품 요청 객체 생성

- CODEF API 상품 호출 메소드

```java
EasyCodefClient easyCodefClient;        // 1. 생성된 EasyCodefClient 객체
EasyCodefRequest request;         // 2. 상품 요청 객체 생성
EasyCodefResponse response = easyCodef.requestProduct(request);
```

> [!WARNING]  
> requestProduct 메소드는 아래의 예외 사항들을 같이 검증하고, 검증에 실패하면 예외를 반환합니다.
> - CODEF OAuth API와 통신에 실패하는 경우 (https://oauth.codef.io)
> - CODEF API와 통신에 실패하는 경우 (https://api.codef.io | https://development.codef.io)
> - 파라미터 정보에 "is2Way", "twoWayInfo"가 포함된 경우 -> `requestCertification` 메소드를 활용하세요.

> [!TIP]  
> EasyCodefClient는 API 응답을 `EasyCodefResponse`객체에 자동 바인딩합니다.  
> `response.getResult()`, `response.getData()`, `response.getExtraInfo()`형태로 편리하게 접근할 수 있습니다.

- 요청 성공 응답 예시

```json
{
  "result": {
    "code": "CF-00000",
    "extraMessage": "",
    "message": "성공",
    "transactionId": "673c243fec82470d0003129f"
  },
  "data": {
    "successList": [
    {
      "clientType": "",
      "code": "CF-00000",
      "loginType": "",
      "countryCode": "",
      "organization": "",
      "extraMessage": "",
      "businessType": "",
      "message":"성공", 
      "transactionId": "673c243fec82470d0003129f"
      }
    ],
    "errorList": [],
    "connectedId":""
  }
}
```

### 4. 간편인증 요청 (CF-03002)

추가인증 상품은 일반 API 상품과 달리 최초 API 요청 시 즉시 결과가 반환되지 않으며

기관 요구 인증(SMS 인증, 이메일 인증, 보안문자 입력 등)을 완료해야 최종 결과를 받을 수 있는 상품 유형입니다.

> [!IMPORTANT]  
> 최초 API 호출 응답의 `result.code`가 `CF-03002`라면, **추가인증 진행이 필요하다는 의미**이며  
> 이후 동일 API EndPoint(URL)로 추가인증 요청을 수행해야 합니다.

- CODEF API 간편인증 필요 상품 호출

```java
// 1. EasyCodefClient 객체 생성
EasyCodefClient easyCodefClient; 
// 2. 상품 요청 객체 생성
EasyCodefRequest request;
// 3. CODEF API 호출
EasyCodefResponse response = easyCodef.requestProduct(request);     
```

- 1차 요청 성공 응답 예시

```json
{
  "result": {
    "code": "CF-03002",
    "extraMessage": "API 요청 처리가 정상 진행 중입니다. 추가 정보를 입력하세요.",
    "message": "성공",
    "transactionId": "673d39ddec82017e742f9f1c"
  },
  "data": {
    "jobIndex": 0,
    "threadIndex": 0,
    "jti": "673d39ddec82017e742f9f1c",
    "twoWayTimestamp": 1732065758977,
    "continue2Way": true,
    "extraInfo": {
      "commSimpleAuth": ""
    },
    "method": "simpleAuth"
  }
}
```

- 추가인증 요청

```java
// 1. 1차 응답(response)에서 추가인증 진행에 필요한 Two-Way 인증 데이터 추출
JSONObject dataJson = response.getData(JSONObject.class);

// 2. Two-Way 인증 처리 필수 정보 구성
Map<String, Object> twoWayInfoMap = new HashMap<>();
twoWayInfoMap.put("jobIndex",dataJson.getLong("jobIndex"));
twoWayInfoMap.put("threadIndex",dataJson.getLong("threadIndex"));
twoWayInfoMap.put("jti",dataJson.getString("jti"));
twoWayInfoMap.put("twoWayTimestamp",dataJson.getLong("twoWayTimestamp"));

// 3. 최초 상품 요청 파라미터 정보(Map 형태)에 Two-Way 키워드 추가
requestParam.put("twoWayInfo",twoWayInfoMap);
requestParam.put("is2Way",true);
requestParam.put("simpleAuth","1");

// 4. requestCertification() 메소드 호출 → 추가 인증 요청 처리 수행
EasyCodefResponse easyCodefResponse = easyCodef.requestCertification(request);
```

> [!WARNING]  
> requestCertification 메소드는 아래의 예외 사항들을 같이 검증하고, 검증에 실패하면 예외를 반환합니다.
> - CODEF OAuth API와 통신에 실패하는 경우 (https://oauth.codef.io)
> - CODEF API와 통신에 실패하는 경우 (https://api.codef.io | https://development.codef.io)
> - 파라미터 정보에 "is2Way" `null`이거나 `false`로 전달된 경우
> - 파라미터 정보에 "twoWayInfo"에 Two-Way 인증 처리 필수 정보가 `null`일 경우  
    > (`jobIndex`, `threadIndex`, `jti`, `twoWayTimestamp`)

- 추가인증 성공 응답 예시

```json
{
  "result": {
    "code": "CF-00000",
    "extraMessage": "",
    "message": "성공",
    "transactionId": "673d39deec82017e742f9f1d"
  },
  "data": {
    "resPreviewList": [],
    "resReferenceList": [ { "resType": "단위", "resHeight": "Cm", ... } ],
    "resCheckupTarget": "코드에프",
    "resResultList": []
  }
}
```

### 5. 간편인증 다건요청

CODEF API는 1개 요청 1개 응답을 원칙으로 합니다.

하지만 추가인증이 필요한 상품인 경우, 같은 고객이 요청 A와 요청 B를 연속해서 호출하면 매번 추가인증을 수행해야 하는 번거로움이 있습니다.

**다건요청** 기능을 활용하면, 한 번의 간편인증으로 동일 고객에 대한 여러 상품을 순차적으로 조회할 수 있습니다.

다건요청 기능에 대한 자세한 내용은 [다건요청 개발가이드](https://developer.codef.io/common-guide/multiple-requests)를 참고하시기 바랍니다.

> [!IMPORTANT]  
> **다건요청은 비동기 방식**으로 호출해야 합니다.  
> 다건요청을 사용하는 모든 요청(`EasycodefRequest`)에는 파라미터 정보(Map 형태)에 **동일한 `id`값(요청 그룹 식별자)** 이 설정되어야 합니다.
>
> 동일한 `id`값은 한 번의 인증 세션을 공유하는 요청 묶음을 의미합니다.
> `id`값이 다르면 요청은 서로 독립적으로 처리되며, 간편인증을 다시 수행해야 합니다.

- 다건요청 상품 객체 생성

```java
Map<String, Object> requestParam = new HashMap<>();

requestParam.put("id", UUID.randomUUID().toString());   // request1, request2, ...에 동일한 id 값 설정

EasycodefRequest request1;
EasycodefRequest request2;
EasycodefRequest request3;
EasycodefRequest request4;
EasycodefRequest request5;

List<EasyCodefRequest> requests = Arrays.asList(request1, request2, request3, request4, request5);
```

- 다건요청 호출 예제

```java
for (EasyCodefRequest request : requests) {
	Thread.sleep(1000);     // 0.5초 ~ 1초 간격으로 송신
	
	new Thread(() -> {
	    EasyCodefResponse productResponse = easyCodef.requestProduct(request);
	    
	    String code = productResponse.getResult().getCode();
	    
	    if (code.equals("CF-03002")) {
	        System.out.println(" ============= 간편 인증을 완료해주세요. =============  ");

	        // 예제에서는 콘솔 입력으로 인증 완료 시점을 가정
	        Scanner sc = new Scanner(System.in);
	        sc.next();
	        System.out.println(" ============= 2차 요청이 진행됩니다. ============= ");

	        // 1. 1차 응답(response)에서 추가인증 진행에 필요한 Two-Way 인증 데이터 추출
	        JSONObject dataJson = productResponse.getData(JSONObject.class);

	        // 2. Two-Way 인증 처리 필수 정보 구성
	        Map<String, Object> twoWayInfoMap = new HashMap<>();
	        twoWayInfoMap.put("jobIndex", dataJson.getLong("jobIndex"));
	        twoWayInfoMap.put("threadIndex", dataJson.getLong("threadIndex"));
	        twoWayInfoMap.put("jti", dataJson.getString("jti"));
	        twoWayInfoMap.put("twoWayTimestamp", dataJson.getLong("twoWayTimestamp"));

	        // 3. 최초 상품 요청 파라미터 정보(Map 형태)에 Two-Way 키워드 추가
	        Map<String, Object> requestParam = request.getParameterMap();
	        requestParam.put("twoWayInfo", twoWayInfoMap);
	        requestParam.put("is2Way", Optional.of(true));
	        requestParam.put("simpleAuth", "1");
            
	        // 4. requestCertification() 메소드 호출 → 추가 인증 요청 처리 수행
	        EasyCodefResponse certificationResponse = codef.requestCertification(request);
			
	        System.out.println("result = " + certificationResponse);
	    } else if (code.equals("CF-00000")) {
	        System.out.println("result = " + productResponse);
	    } else {
	        System.out.println("비정상 result " + productResponse);
	    }
	}).start();
}
```

- 다건요청 성공 응답 예시

```json
{
  "result": {
    "code": "CF-00000",
    "extraMessage": "",
    "message": "성공",
    "transactionId": "673d39deec82017e742f9f1d"
  },
  "data": {
    "resPreviewList": [],
    "resReferenceList": [ { "resType": "단위", "resHeight": "Cm", ... } ],
    "resCheckupTarget": "코드에프",
    "resResultList": []
  }
},
{
  "result": {
    "code": "CF-00000",
    "extraMessage": "",
    "message": "성공",
    "transactionId": "673d39dfec82470d00031458"
  },
  "data": {
    "resPreviewList": [],
    "resReferenceList": [ { "resType": "단위", "resHeight": "Cm", ...} ],
    "resCheckupTarget": "코드에프",
    "resResultList": []
  }
}
```