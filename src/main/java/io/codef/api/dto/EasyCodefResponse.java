package io.codef.api.dto;

import com.alibaba.fastjson2.JSONObject;

public class EasyCodefResponse {

	private final Result result;
	private final Object data;
	private final Object extraInfo;

	private EasyCodefResponse(Result result, Object data, Object extraInfo) {
		this.result = result;
		this.data = data;
		this.extraInfo = extraInfo;
	}

	private EasyCodefResponse(Object data) {
		this.result = null;
		this.data = data;
		this.extraInfo = null;
	}

	public static EasyCodefResponse from(Object data) {
		return new EasyCodefResponse(data);
	}

	public static EasyCodefResponse of(Result result, Object data, Object extraInfo) {
		return new EasyCodefResponse(result, data, extraInfo);
	}

	public Result getResult() {
		return result;
	}

	public Object getData() {
		return data;
	}

	public Object getExtraInfo() {
		return extraInfo;
	}

	public <T> T getData(Class<T> clazz) {
		return JSONObject.from(data).to(clazz);
	}

	@Override
	public String toString() {
		JSONObject root = new JSONObject();

		root.put("result", result);
		root.put("data", data);

		if (extraInfo != null) {
			JSONObject extraJson = JSONObject.from(extraInfo);
			if (extraJson != null) {
				root.putAll(extraJson);
			}
		}

		return root.toJSONString();
	}

	public static class Result {
		private final String code;
		private final String extraMessage;
		private final String message;
		private final String transactionId;

		public Result(String code,
			String extraMessage,
			String message,
			String transactionId) {
			this.code = code;
			this.extraMessage = extraMessage;
			this.message = message;
			this.transactionId = transactionId;
		}

		public String getCode() {
			return code;
		}

		public String getExtraMessage() {
			return extraMessage;
		}

		public String getMessage() {
			return message;
		}

		public String getTransactionId() {
			return transactionId;
		}
	}
}
