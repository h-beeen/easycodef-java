package io.codef.api.core;

import io.codef.api.dto.EasyCodefRequest;
import io.codef.api.dto.EasyCodefResponse;

public interface Executor {
	EasyCodefResponse execute(EasyCodefRequest request);
}
