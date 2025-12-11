package io.codef.api.constant;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * CODEF Two-Way 필드 키 enum 클래스
 *
 */
public enum TwoWayConstant {
	JOB_INDEX("jobIndex"),
	THREAD_INDEX("threadIndex"),
	JTI("jti"),
	TIMESTAMP("twoWayTimestamp");

	private final String value;

	TwoWayConstant(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public static final Set<String> REQUIRED_KEYS = Collections.unmodifiableSet(
		Stream.of(values())
			.map(TwoWayConstant::getValue)
			.collect(Collectors.toSet()));
}
