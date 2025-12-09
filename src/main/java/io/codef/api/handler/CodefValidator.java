package io.codef.api.handler;

import static io.codef.api.constant.CodefConstant.*;
import static io.codef.api.constant.TwoWayConstant.*;

import java.util.Map;

import io.codef.api.error.CodefError;
import io.codef.api.error.CodefException;
import io.codef.api.util.JsonUtil;

/**
 * CODEF 요청 값 검증을 위한 유틸리티 클래스
 *
 */
public class CodefValidator {

	private CodefValidator() {
	}

	/**
	 * 객체가 {@code null}이 아니고, 문자열인 경우 공백이 아닌지 검증
	 *
	 * @param object     검증할 객체
	 * @param codefError 검증 실패 시 사용할 오류 코드
	 * @param <T>        검증 대상 객체 타입
	 * @return 검증을 통과한 원본 객체
	 * @throws CodefException 객체가 {@code null}이거나 비어 있는 경우
	 */
	public static <T> T validateNotNullOrThrow(T object, CodefError codefError) {
		if (object == null || object.toString().trim().isEmpty()) {
			throw CodefException.from(codefError);
		}

		return object;
	}

	/**
	 * 상품 URL 경로 형식("/v1/***")을 검증
	 *
	 * @param productUrl 상품 URL 경로
	 * @return 검증을 통과한 상품 URL
	 * @throws CodefException 상품 URL이 {@code null}이거나 "/v1/***"으로 시작하지 않는 경우 {@link CodefError#INVALID_PATH_REQUESTED}
	 */
	public static String validatePathOrThrow(String productUrl) {
		if (productUrl == null || productUrl.toLowerCase().startsWith(HTTPS.getValue())) {
			throw CodefException.from(CodefError.INVALID_PATH_REQUESTED);
		}

		return productUrl;
	}

	/**
	 * 상품 요청에서 Two-Way 관련 키워드 사용 여부 검증
	 *
	 * @param parameterMap 요청 파라미터 정보
	 * @throws CodefException 일반 상품 요청에 Two-Way 관련 키워드가 포함된 경우 {@link CodefError#INVALID_2WAY_KEYWORD}
	 */
	public static void validateTwoWayKeywordsOrThrow(Map<String, Object> parameterMap) {
		if (parameterMap == null || parameterMap.isEmpty()) {
			return;
		}

		if (parameterMap.containsKey(IS_2WAY.getValue()) || parameterMap.containsKey(INFO_KEY.getValue())) {
			throw CodefException.from(CodefError.INVALID_2WAY_KEYWORD);
		}
	}

	/**
	 * Two-Way(간편인증/추가인증) 요청의 파라미터 유효성 검증
	 *
	 * @param parameterMap 요청 파라미터 맵
	 * @throws CodefException
	 *         <ul>
	 *             <li>파라미터 맵이 비어 있는 경우 {@link CodefError#EMPTY_PARAMETER}</li>
	 *             <li>Two-Way 관련 정보가 잘못되거나 포함되지 않은 경우
	 *             {@link CodefError#INVALID_2WAY_INFO}</li>
	 *         </ul>
	 */
	public static void validateTwoWayInfoOrThrow(Map<String, Object> parameterMap) {
		if (parameterMap == null || parameterMap.isEmpty()) {
			throw CodefException.from(CodefError.EMPTY_PARAMETER);
		}

		Object is2WayObj = parameterMap.get(IS_2WAY.getValue());
		if (is2WayObj == null || Boolean.FALSE.equals(is2WayObj)) {
			throw CodefException.from(CodefError.INVALID_2WAY_INFO);
		}

		Object twoWayInfoObj = parameterMap.get(INFO_KEY.getValue());
		Map<String, Object> twoWayInfoMap = JsonUtil.toMap(twoWayInfoObj);

		if (!twoWayInfoMap.keySet().containsAll(REQUIRED_KEYS)) {
			throw CodefException.from(CodefError.INVALID_2WAY_INFO);
		}
	}
}
