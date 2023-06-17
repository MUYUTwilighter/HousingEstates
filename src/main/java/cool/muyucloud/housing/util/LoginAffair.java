package cool.muyucloud.housing.util;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class LoginAffair {
    @Autowired
    private final int uid;
    @Autowired
    private long expire;

    public LoginAffair(int uid, long duration) {
        this.uid = uid;
        this.extend(duration);
    }

    public void extend(long duration) {
        this.expire = System.currentTimeMillis() + duration;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > this.expire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginAffair that = (LoginAffair) o;
        return uid == that.uid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid);
    }

    public int uid() {
        return uid;
    }

    public long expire() {
        return expire;
    }
}
