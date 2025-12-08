package io.codef.api.error;

/**
 * CODEF 라이브러리 전용 런타임 예외 클래스
 *
 */
public class CodefException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 기본 에러 메시지와 추가 메시지를 합성
	 *
	 * @param errorMessage 기본 에러 메시지
	 * @param message      추가 메시지 또는 cause 메시지
	 * @return 두 메시지를 개행 문자로 연결한 문자열
	 */
	private static String decoratedMessage(
		final String errorMessage, final String message) {
		return String.format("%s\n%s", errorMessage, message);
	}

	/**
	 * {@link CodefError} 기반 기본 예외 생성자
	 *
	 * @param codefError 에러 코드
	 */
	private CodefException(CodefError codefError) {
		super(codefError.getMessage());
	}

	/**
	 * {@link CodefError}와 원인 예외를 포함하는 예외 생성자
	 *
	 * @param codefError 에러 코드
	 * @param exception  원인 예외
	 */
	private CodefException(CodefError codefError, Exception exception) {
		super(decoratedMessage(codefError.getMessage(), exception.getMessage()), exception);
	}

	/**
	 * {@link CodefError}와 추가 메시지를 포함하는 예외 생성자
	 *
	 * @param codefError   에러 코드
	 * @param extraMessage 상세 설명 또는 추가 메시지
	 */
	private CodefException(CodefError codefError, String extraMessage) {
		super(decoratedMessage(codefError.getMessage(), extraMessage));
	}

	/**
	 * {@link CodefError}만을 기반으로 예외 생성
	 *
	 * @param codefError 에러 코드
	 * @return 생성된 {@link CodefException} 인스턴스
	 */
	public static CodefException from(CodefError codefError) {
		return new CodefException(codefError);
	}

	/**
	 * {@link CodefError}와 원인 예외를 포함하는 예외 생성
	 *
	 * @param codefError 에러 코드
	 * @param exception  원인 예외
	 * @return 생성된 {@link CodefException} 인스턴스
	 */
	public static CodefException of(CodefError codefError, Exception exception) {
		return new CodefException(codefError, exception);
	}

	/**
	 * {@link CodefError}와 추가 메시지를 포함하는 예외 생성
	 *
	 * @param codefError   에러 코드
	 * @param extraMessage 상세 설명 또는 추가 메시지
	 * @return 생성된 {@link CodefException} 인스턴스
	 */
	public static CodefException of(CodefError codefError, String extraMessage) {
		return new CodefException(codefError, extraMessage);
	}
}
