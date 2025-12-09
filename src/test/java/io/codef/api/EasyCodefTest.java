package io.codef.api;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

class EasyCodefTest {

    @Test
    void tdd() {
        EasyCodef easyCodef = new EasyCodef();
        easyCodef.setClientInfo("ef27cfaa-10c1-4470-adac-60ba476273f9", "83160c33-9045-4915-86d8-809473cdf5c3");
        easyCodef.setPublicKey("publicKey");
        String s = easyCodef.requestProduct("/", EasyCodefServiceType.API, new HashMap<>());
    }
}
