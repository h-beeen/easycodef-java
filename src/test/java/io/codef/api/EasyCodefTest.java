package io.codef.api;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

class EasyCodefTest {

    @Test
    void tdd() {
        EasyCodef easyCodef = new EasyCodef();
        easyCodef.setClientInfo("abc", "efg");
        easyCodef.setPublicKey("publicKey");
        String s = easyCodef.requestProduct("/v1/api", EasyCodefServiceType.API, new HashMap<>());
    }
}
