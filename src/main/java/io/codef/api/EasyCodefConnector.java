package io.codef.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 * <pre>
 * io.codef.easycodef
 *   |_ EasyCodefConnector.java
 * </pre>
 * 
 * Desc : CODEF 엑세스 토큰 및 상품 조회를 위한 HTTP 요청 클래스
 * @Company : ©CODEF corp.
 * @Author  : notfound404@codef.io
 * @Date    : Jun 26, 2020 3:35:17 PM
 */
public class EasyCodefConnector {
	private static ObjectMapper mapper = new ObjectMapper();
	private static final int REPEAT_COUNT = 3;
	
	/**
	 * Desc : CODEF 상품 조회 요청
	 * @Company : ©CODEF corp.
	 * @Author  : notfound404@codef.io
	 * @Date    : Jun 26, 2020 3:35:26 PM
	 * @param urlPath
	 * @param serviceType
	 * @param bodyMap
	 * @return
	 * @throws InterruptedException
	 */
	@SuppressWarnings("unchecked")
	protected static EasyCodefResponse execute(String urlPath, int serviceType, String accessToken, HashMap<String, Object> bodyMap) {
		/**	#1.토큰 체크	*/
		String domain = (serviceType == 0) ? EasyCodefConstant.API_DOMAIN : EasyCodefConstant.DEMO_DOMAIN;

		/**	#2.요청 파라미터 인코딩	*/
		String bodyString;
		try {
			bodyString = mapper.writeValueAsString(bodyMap);
			bodyString = URLEncoder.encode(bodyString, "UTF-8");
		} catch (JsonProcessingException e) {
            return new EasyCodefResponse(EasyCodefMessageConstant.INVALID_JSON);
		} catch (UnsupportedEncodingException e) {
            return new EasyCodefResponse(EasyCodefMessageConstant.UNSUPPORTED_ENCODING);
		}
		
		/**	#3.상품 조회 요청	*/
		HashMap<String, Object> responseMap = requestProduct(domain + urlPath, accessToken, bodyString);
        if (EasyCodefConstant.ACCESS_DENIED.equals(responseMap.get("error")) ||
                "CF-00403".equals(((HashMap<String, Object>)responseMap.get(EasyCodefConstant.RESULT)).get(EasyCodefConstant.CODE))) {	// 접근 권한이 없는 경우 - 오류코드 반환
            return new EasyCodefResponse(EasyCodefMessageConstant.UNAUTHORIZED, EasyCodefConstant.ACCESS_DENIED);
		}
		
		/**	#4.상품 조회 결과 반환	*/
        return new EasyCodefResponse(responseMap);
	}

	/**
	 * Desc : CODEF HTTP POST 요청
	 * @Company : ©CODEF corp.
	 * @Author  : notfound404@codef.io
	 * @Date    : Jun 26, 2020 3:35:34 PM
	 * @param urlPath
	 * @param token
	 * @param bodyString
	 * @return
	 */
	private static HashMap<String, Object> requestProduct(String urlPath, String token, String bodyString) {
		BufferedReader br = null;
		try {
			// HTTP 요청을 위한 URL 오브젝트 생성
			URL url = new URL(urlPath);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("Accept", "application/json");

			if (token != null && !"".equals(token)) {
				con.setRequestProperty("Authorization", "Bearer " + token);		// 엑세스 토큰 헤더 설정
			}

			// 리퀘스트 바디 전송
			OutputStream os = con.getOutputStream();
			if (bodyString != null && !"".equals(bodyString)) {
				os.write(bodyString.getBytes());
			}
			os.flush();
			os.close();

			// 응답 코드 확인
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				br = new BufferedReader(new InputStreamReader(con.getInputStream())); 
			} else if (responseCode == HttpURLConnection.HTTP_BAD_REQUEST) {
				EasyCodefResponse response = new EasyCodefResponse(EasyCodefMessageConstant.BAD_REQUEST, urlPath); 
				return response;
			} else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED) {
				EasyCodefResponse response = new EasyCodefResponse(EasyCodefMessageConstant.UNAUTHORIZED, urlPath); 
				return response; 
			} else if (responseCode == HttpURLConnection.HTTP_FORBIDDEN) {
				EasyCodefResponse response = new EasyCodefResponse(EasyCodefMessageConstant.FORBIDDEN, urlPath); 
				return response; 
			} else if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
				EasyCodefResponse response = new EasyCodefResponse(EasyCodefMessageConstant.NOT_FOUND, urlPath); 
				return response; 
			} else {
				EasyCodefResponse response = new EasyCodefResponse(EasyCodefMessageConstant.SERVER_ERROR, urlPath); 
				return response;
			}
			
			// 응답 바디 read
			String inputLine;
			StringBuffer responseStr = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				responseStr.append(inputLine);
			}
			br.close();
			
			// 결과 반환
			return mapper.readValue(URLDecoder.decode(responseStr.toString(), "UTF-8"), new TypeReference<HashMap<String, Object>>(){});	
		} catch (Exception e) {
			EasyCodefResponse response = new EasyCodefResponse(EasyCodefMessageConstant.LIBRARY_SENDER_ERROR, e.getMessage()); 
			return response;
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {}
			}
		}
	}
	
	/**
	 * Desc : CODEF 엑세스 토큰 발급 요청
	 * @Company : ©CODEF corp.
	 * @Author  : notfound404@codef.io
	 * @Date    : Jun 26, 2020 3:36:01 PM
	 * @return
	 */
	protected static HashMap<String, Object> publishToken(String oauthToken) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpPost httpPost = new HttpPost(EasyCodefConstant.OAUTH_DOMAIN + EasyCodefConstant.GET_TOKEN);
            httpPost.addHeader("Authorization", oauthToken);
            httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded");

            String params = "grant_type=client_credentials&scope=read";
            httpPost.setEntity(new StringEntity(params));

            CloseableHttpResponse response = httpClient.execute(httpPost);

            int responseCode = response.getStatusLine().getStatusCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            }

            String responseBody = EntityUtils.toString(response.getEntity());

            return mapper.readValue(responseBody, new TypeReference<HashMap<String, Object>>() {});
        } catch (Exception e) {
            return null;
        }
	}
}
