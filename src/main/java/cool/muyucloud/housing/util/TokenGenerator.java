package cool.muyucloud.housing.util;

import java.security.SecureRandom;

public class TokenGenerator {
    public static long genToken() {
        long token = 0;
        while (token == 0) {
            token = new SecureRandom().nextLong();
        }
        return token;
    }
}
