package io.codef.api.constants;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CodefConstant {

	public static final String RESULT = "result";
	public static final String DATA = "data";
	public static final String PATH_PREFIX = "/v1";
	public static final String RSA = "RSA";
	public static final String APPLICATION_JSON = "application/json";

	public static class OAuth {
		public static final String ACCESS_TOKEN = "access_token";
		public static final String EXPIRES_IN = "expires_in";
	}

	public static class TwoWay {
		public static final String IS_2WAY = "is2Way";
		public static final String INFO_KEY = "twoWayInfo";

		public static final String JOB_INDEX = "jobIndex";
		public static final String THREAD_INDEX = "threadIndex";
		public static final String JTI = "jti";
		public static final String TIMESTAMP = "twoWayTimestamp";

		public static final Set<String> REQUIRED_KEYS;

		static {
			REQUIRED_KEYS = new HashSet<>(Arrays.asList(
				JOB_INDEX,
				THREAD_INDEX,
				JTI,
				TIMESTAMP
			));
		}
	}
}
