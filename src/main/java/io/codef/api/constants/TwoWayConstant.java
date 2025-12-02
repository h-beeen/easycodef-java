package io.codef.api.constants;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public enum TwoWayConstant {
	IS_2WAY("is2Way"),
	INFO_KEY("twoWayInfo"),
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

	public static final Set<String> REQUIRED_KEYS;

	static {
		Set<String> keys = new HashSet<>();
		keys.add(JOB_INDEX.getValue());
		keys.add(THREAD_INDEX.getValue());
		keys.add(JTI.getValue());
		keys.add(TIMESTAMP.getValue());

		REQUIRED_KEYS = Collections.unmodifiableSet(keys);
	}
}
