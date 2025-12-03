package io.codef.api.dto;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.annotation.JSONType;

@JSONType(orders = {"result", "data"})
public class EasyCodefResponse {

	private final Result result;
	private final Object data;

	private EasyCodefResponse(Result result, Object data) {
		this.result = result;
		this.data = data;
	}

	private EasyCodefResponse(Object data) {
		this.result = null;
		this.data = data;
	}

	public static EasyCodefResponse of(Result result, Object data) {
		return new EasyCodefResponse(result, data);
	}

	public static EasyCodefResponse ofToken(Object data) {
		return new EasyCodefResponse(data);
	}

	public Result getResult() {
		return result;
	}

	public Object getData() {
		return data;
	}

	public <T> T getData(Class<T> clazz) {
		return JSONObject.from(data).to(clazz);
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
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
