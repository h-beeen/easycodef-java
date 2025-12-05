<br>
<br>
<p style="text-align: center;">
  <a title="코드에프" href="https://codef.io/">
    <picture>
      <source media="(prefers-color-scheme: dark)" srcset="https://github.com/user-attachments/assets/d83f0450-d84e-4594-8fc0-ed08a1d05390">
      <img alt="코드에프" src="https://github.com/user-attachments/assets/cd2e9709-742f-4e27-b695-4515bdbb7cc0" width="250">
    </picture>
  </a>
</p>

<p style="text-align: center;">
  <span><code>easycodef-java</code><br><b>Open-Source Library</b><br>For the <b>CODEF API</b></span>
</p>

<p style="text-align: center;">
<img alt="헥토데이터" src="https://github.com/user-attachments/assets/ac6b7a7d-33f1-4b1e-9fbb-8231d56e7f33" height="20"><br>
<span>MIT © | <a href="https://github.com/codef-io/easycodef-java/blob/master/LICENSE" target="_blank">LICENSE</a></span>
</p>

<p style="text-align: center;">
  <a href="https://search.maven.org/search?q=g:%22io.codef.api%22%20AND%20a:%22easycodef-java%22">
    <img src="https://img.shields.io/maven-central/v/io.codef.api/easycodef-java.svg?label=Maven%20Central" />
  </a>
  <a href="https://javadoc.io/doc/io.codef.api/easycodef-java">
    <img src="https://javadoc.io/badge2/io.codef.api/easycodef-java/javadoc.svg" />
  </a>
  <a href="https://github.com/codef-io/easycodef-java/blob/master/LICENSE">
    <img src="https://img.shields.io/badge/License-MIT-yellow.svg" />
  </a>
</p>


> [!IMPORTANT]
> - [홈페이지](https://codef.io/)
> - [개발가이드](https://developer.codef.io/)
> - [블로그](https://blog.hectodata.co.kr/)

easycodef-java는 CODEF API 연동 개발을 돕는 Java 라이브러리입니다.

사용을 위해서는 [홈페이지](https://codef.io/) 가입 후 데모/정식 서비스 신청을 통해 자격 증명을 위한 클라이언트 정보 등을 발급받아야 하며

사용 가능한 모든 API(은행, 카드, 보험, 증권, 공공, 기타)와 요청/응답 항목은 모두 [개발가이드](https://developer.codef.io/)를 통해 확인할 수 있습니다.

## Getting Started

라이브러리는 [Maven 중앙 저장소](https://search.maven.org/artifact/io.codef.api/easycodef-java)에서 확인 가능한 io.codef.api 패키지에 속해 있으며
의존관계 설정을 통해 사용 가능합니다.

- Maven (pom.xml)

```xml

<dependencies>
    <dependency>
        <groupId>io.codef.api</groupId>
        <artifactId>easycodef-java</artifactId>
        <version>1.0.6</version>
    </dependency>
</dependencies>
```

- Gradle

```gradle
dependencies {
  implementation 'io.codef.api:easycodef-java:1.0.6'
 }
```

[Maven 중앙 저장소](https://search.maven.org/artifact/io.codef.api/easycodef-java)에서 모든 버전의 easycodef-java jar 파일을 다운로드할 수
있습니다.

### 1. EasyCodef 객체 생성

CODEF API 서비스를 이용하기 위해서는 자격 증명을 위한 클라이언트 정보를 통해 토큰을 발급받아야 합니다.

한 번 발급 받은 토큰은 일주일간 재사용이 가능합니다.

> [!NOTE]  
> EasyCodef 객체는 모든 CODEF API 상품 요청을 위해 필요하며, Singleton 객체로 재사용하도록 설계되었습니다.  
> 토큰의 발급과 재사용을 자동으로 처리하며, 유효기간이 만료되는 경우 재발급 또한 자동으로 처리합니다.  
> EasyCodef 객체는 EasyCodefBuilder 생성자를 통해 생성할 수 있습니다.

사용자는 자격증명을 위한 **클라이언트 정보** 설정만 진행하면 됩니다.

클라이언트 정보는 [MY > 키 관리](https://codef.io/account/keys)에서 확인할 수 있습니다.

```java
EasyCodef easycodef = EasyCodefBuilder.builder()
	.serviceType(CodefServiceType.DEMO) // 클라이언트 정보가 데모버전일 경우 선택 [중복 선택 불가]
	.serviceType(CodefServiceType.API)  // 클라이언트 정보가 정식버전일 경우 선택 [중복 선택 불가] 

	.clientId("your-client-id")
	.clientSecret("your-client-secret")
	.publicKey("your-public-key")
	.build();
```

> [!WARNING]  
> EasyCodef 생성자는 아래의 예외 사항들을 같이 검증하고, 검증에 실패하면 예외를 반환합니다.
> - `clientId`, `clientSecret`, `publicKey`값이 `null`일 경우
> - CODEF OAuth API와 통신에 실패하는 경우 (https://oauth.codef.io)

### 2. 상품 요청 객체 생성

EasyCodef에서 CODEF API 상품을 요청하기 위해서는 EasyCodefRequest 객체가 필요합니다.

해당 객체에는 호출하고자 하는 상품의 EndPoint(URL)와 요청에 필요한 파라미터 정보(Map 형태)가 포함됩니다.

> [!NOTE]  
> EasyCodefRequest 객체는 EasyCodefRequestBuilder 생성자를 통해 생성할 수 있습니다.
> - EndPoint는 host를 제외한 `/v1/***` 형식으로 구성합니다.
> - 파라미터 정보는 < 'key', 'value [ Object ]' > 형식으로 구성합니다.
>
> 본 예제는 [건강보험공단 > 건강검진결과](https://developer.codef.io/products/public/each/pp/nhis-health-check),  
> [커넥티드 아이디 > 계정 등록](https://developer.codef.io/common-guide/connected-id/register) 기반으로 작성되었습니다.
>
> `Connected Id`에 대한 자세한 내용은 [커넥티드 아이디 > 개요](https://developer.codef.io/common-guide/connected-id/cid)를 참고하시기 바랍니다.

- 일반 상품 [[건강보험공단 > 건강검진결과]](https://developer.codef.io/products/public/each/pp/nhis-health-check) 파라미터 구성 예시

```java
Map<String, Object> requestParam = new HashMap<>();
requestParam.put("organization",    "0002");
requestParam.put("loginType",       "5");
requestParam.put("loginTypeLevel",  "1");
requestParam.put("identity",        "19990101");
requestParam.put("userName",        "홍길동");
requestParam.put("telecom",         "1");
requestParam.put("phoneNo",         "01012345678");
requestParam.put("searchStartYear", "2023");
requestParam.put("searchEndYear",   "2023");
requestParam.put("inquiryType",     "4");
```

- EasyCodefRequest 객체 생성

```java
EasyCodefRequest request = EasyCodefRequestBuilder.builder()
	.productUrl("/v1/kr/public/pp/nhis-health-checkup/result")
	.parameterMap(requestParam)
	.build();
```

> [!WARNING]  
> EasyCodefRequestBuilder 생성자는 아래의 예외 사항들을 같이 검증하고, 검증에 실패하면 예외를 반환합니다.
> - `productUrl`값이 `null`이거나 `/v1/***` 형식을 따르지 않는 경우
> - `parameterMap`값이 `null`일 경우

- 암호화가 필요한 상품 [[커넥티드 아이디 > 계정 등록]](https://developer.codef.io/common-guide/connected-id/register) 파라미터 구성 예시

```java
List<HashMap<String, Object>> accountList = new ArrayList<HashMap<String, Object>>();
HashMap<String, Object> accountMap = new HashMap<String, Object>();
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
EasyCodef easyCodef;        // 1. EasyCodef 객체 생성
EasyCodefRequest request;   // 2. 상품 요청 객체 생성

EasyCodefResponse response = easyCodef.requestProduct(request);
```

> [!WARNING]  
> requestProduct 메소드는 아래의 예외 사항들을 같이 검증하고, 검증에 실패하면 예외를 반환합니다.
> - CODEF OAuth API와 통신에 실패하는 경우 (https://oauth.codef.io)
> - CODEF API와 통신에 실패하는 경우 (https://api.codef.io | https://development.codef.io)
> - 파라미터 정보에 "is2Way", "twoWayInfo"가 포함된 경우

> [!TIP]  
> EasyCodef는 API 응답을 `EasyCodefResponse`객체에 자동 바인딩합니다.  
> `response.getResult()`, `response.getData()`형태로 편리하게 접근할 수 있습니다.

- 요청 성공 응답 예시

```java
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
EasyCodef easyCodef;        // 1. EasyCodef 객체 생성
EasyCodefRequest request    // 2. 상품 요청 객체 생성

EasyCodefResponse response = easyCodef.requestProduct(request);     // 3. CODEF API 호출
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
EasyCodefResponse easyCodefResponse = codef.requestCertification(request);
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

### 4. 간편인증 다건요청

CODEF API는 1개 요청 1개 응답을 원칙으로 합니다.

하지만 추가인증이 필요한 상품인 경우, 같은 고객이 요청 A와 요청 B를 연속해서 호출하면 매번 추가인증을 수행해야 하는 번거로움이 있습니다.

**다건요청** 기능을 활용하면, 한 번의 간편인증으로 동일 고객에 대한 여러 상품을 순차적으로 조회할 수 있습니다.

다건요청 기능에 대한 자세한 내용은 [다건요청 개발가이드](https://developer.codef.io/common-guide/multiple-requests)를 참고하시기 바랍니다.

> [!IMPORTANT]  
> **다건요청은 비동기 방식**으로 호출해야 합니다.  
> 다건요청을 사용하는 모든 요청(`EasycodefRequest`)에는 파라미터 정보(Map 형태)에 **동일한 `"id"`값(요청 그룹 식별자, SSO 그룹 Key)** 이 설정되어야 합니다.
>
> 동일한 `"id"`값은 한 번의 인증 세션을 공유하는 요청 묶음을 의미합니다.
> `"id"`값이 다르면 요청은 서로 독립적으로 처리되며, 간편인증을 다시 수행해야 합니다.

- 다건요청 상품 객체 생성

```java
Map<String, Object> requestParam = new HashMap<>();
...
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
for (EasyCodefRequest request : easyCodefRequests) {
	Thread.sleep(1000);     // 0.5초 ~ 1초 간격으로 송신
	
	new Thread(() -> {
	    EasyCodefResponse response = codef.requestProduct(request);
	    
	    String code = response.getResult().getCode();
	    
	    if (code.equals("CF-03002")) {
	        System.out.println(" ============= 간편 인증을 완료해주세요. =============  ");
			
			// 예제에서는 콘솔 입력으로 인증 완료 시점을 가정
			Scanner sc = new Scanner(System.in);
			sc.next();
			System.out.println(" ============= 2차 요청이 진행됩니다. ============= ");
			
			// 1. 1차 응답(response)에서 추가인증 진행에 필요한 Two-Way 인증 데이터 추출
            
			// 2. Two-Way 인증 처리 필수 정보 구성
                
			// 3. 최초 상품 요청 파라미터 정보(Map 형태)에 Two-Way 키워드 추가
            
			// 4. requestCertification() 메소드 호출 → 추가 인증 요청 처리 수행
			EasyCodefResponse resultCertification = codef.requestCertification(request);
			
			System.out.println("result = " + resultCertification);
		} else if (code.equals("CF-00000")) {
		    System.out.println("result = " + response);
		} else {
		    System.out.println("비정상 result " + response);
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

### 5. 커스텀 설정 (Optional)

> [!NOTE]  
> EasyCodef는 앞선 기본 설정으로도 바로 사용할 수 있지만, 전략에 따라 다음과 같은 설정을 적용할 수 있습니다.
> - Apache HttpClient 커넥션 풀(Custom Connection Pool) 지정
> - 요청별 http Timeout 지정

- Apache HttpClient 커넥션 풀 설정 예제

```java
PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
poolingHttpClientConnectionManager.setMaxTotal(200); // 최대 커넥션 수

CloseableHttpClient httpClient = HttpClients.custom()
	.setConnectionManager(poolingHttpClientConnectionManager)
	.build();

EasyCodef easycodef = EasyCodefBuilder.builder()
    ...
	.httpClient(httpClient) // CloseableHttpClient 타입만 지원
	.build();
```

> [!TIP]  
> `setMaxTotal()` 및 `setDefaultMaxPerRoute()` 값은 API 호출량 및 서버 환경에 맞게 조정하는 것이 좋습니다.

- 요청별 Timeout 설정 예제

```java
EasyCodefRequest request = EasyCodefRequestBuilder.builder()
    ...
	.customTimeout(500) // 단위: 초(s)
	.build();
```

> [!WARNING]  
> `customTimeout은` 해당 요청에만 적용되며, 전역 Timeout 설정을 변경하지 않습니다.

# Ask us

easycodef-java 라이브러리 사용에 대한 문의사항과 개발 과정에서의 오류 등에 대한 문의를 [홈페이지 문의게시판](https://codef.io/#/cs/inquiry)에 올려주시면 운영팀이 답변을
드립니다.

문의게시판의 작성 양식에 맞춰 문의 글을 남겨주세요. 가능한 빠르게 응답을 드리겠습니다. 감사합니다.
