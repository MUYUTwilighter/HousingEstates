package cool.muyucloud.housing.service;

import cool.muyucloud.housing.dao.UserDao;
import cool.muyucloud.housing.entity.User;
import cool.muyucloud.housing.util.UserAffair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    UserDao dao;
    private final HashMap<Long, UserAffair> affairs = new HashMap<>();

    @Override
    public boolean login(int id, String pwd) {
        User user = this.dao.queryById(id);
        if (user == null) {
            return false;
        } else {
            return this.matchPwd(user, pwd);
        }
    }

    @Override
    public Integer login(String email, String pwd) {
        User user = this.dao.queryByEmail(email);
        if (user == null) {
            return null;
        } else if (this.matchPwd(user, pwd)) {
            return user.getId();
        }
        return null;
    }

    @Override
    public Long register(String name, String pwd, String email) {
        if (dao.queryHiddenByEmail(email) != null) {
            return null;
        }
        User user = new User();
        user.setName(name);
        user.setPassword(pwd);
        user.setEmail(email);
        return this.addAffair(user, 43200000);
    }

    @Override
    public Integer verifyReg(long code) {
        User user = this.verifyAffair(code);
        if (user == null) {
            return 0;
        }
        dao.save(user);
        return user.getId();
    }

    @Override
    public boolean updatePwd(int id, String pwd, String newPwd) {
        User user = this.dao.queryById(id);
        if (user == null) {
            return false;
        }
        if (!this.matchPwd(user, pwd)) {
            return false;
        }
        user.setPassword(newPwd);
        this.dao.save(user);
        return true;
    }

    @Override
    public Long resetPwdReq(int id) {
        User user = this.dao.queryById(id);
        if (user == null) {
            return null;
        }
        return this.addAffair(user, 43200000);
    }

    @Override
    public boolean resetPwd(long code, String newPwd) {
        User user = this.verifyAffair(code);
        if (user == null) {
            return false;
        }
        user.setPassword(newPwd);
        this.dao.save(user);
        return true;
    }

    @Override
    public User find(int id) {
        return this.dao.queryHiddenById(id);
    }

    @Override
    public List<User> find(String name) {
        return this.dao.queryHiddenByName(name);
    }

    @Override
    public Long deleteReq(int uid) {
        User user = this.dao.queryById(uid);
        if (user == null) {
            return 0L;
        }
        return this.addAffair(user, 300000);
    }

    @Override
    public boolean delete(long code) {
        User user = this.verifyAffair(code);
        if (user == null) {
            return false;
        }
        this.dao.deleteById(user.getId());
        return true;
    }

    @Override
    public boolean updateType(int id, byte type) {
        User user = this.dao.queryById(id);
        if (user == null) {
            return false;
        }
        user.setType(type);
        this.dao.save(user);
        return true;
    }

    @Override
    public boolean updateName(int id, String name) {
        User user = this.dao.queryById(id);
        if (user == null) {
            return false;
        }
        user.setName(name);
        this.dao.save(user);
        return true;
    }

    private boolean matchPwd(User user, String pwd) {
        return Objects.equals(user.getPassword(), pwd);
    }

    private Long addAffair(User user, long duration) {
        long time = System.currentTimeMillis() + duration;
        long code = new SecureRandom().nextLong();
        UserAffair affair = new UserAffair(user, time);
        this.affairs.put(code, affair);
        return code;
    }

    private User verifyAffair(long code) {
        UserAffair affair = this.affairs.get(code);
        this.affairs.remove(code);
        long time = System.currentTimeMillis();
        if (affair == null || time > affair.expire()) {
            return null;
        }
        return affair.user();
    }
}
