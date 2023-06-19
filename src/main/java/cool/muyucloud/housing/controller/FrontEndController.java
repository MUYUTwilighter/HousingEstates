package cool.muyucloud.housing.controller;

import cool.muyucloud.housing.entity.User;
import cool.muyucloud.housing.service.AddressIdService;
import cool.muyucloud.housing.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Controller
public class FrontEndController {
    @Autowired
    private AddressIdService aidService;
    @Autowired
    private UserService userService;
    private final HashMap<Long, Integer> online = new HashMap<>();


    @RequestMapping(
        path = {"/address/login"},
        params = {"token"},
        method = RequestMethod.PUT
    )
    @ResponseBody
    public Long login(int uid, String pwd) {
        User user = this.userService.find(uid);
        if (user == null || user.getType() < User.TYPE_DATABASE_ADMIN) {
            return null;
        }
        if (!this.userService.login(uid, pwd)) {
            return null;
        }
        return this.online(uid);
    }

    @RequestMapping(
        path = {"/address/logout"},
        params = {"token"},
        method = RequestMethod.PUT
    )
    @ResponseBody
    public boolean logout(long token) {
        Integer uid = this.online.get(token);
        if (uid == null) {
            return false;
        }
        this.online.remove(token);
        return true;
    }

    @RequestMapping(
        path = {"/address/update"},
        params = {"token", "aid", "div"},
        method = RequestMethod.PUT
    )
    @ResponseBody
    public boolean update(long token, int aid, String div) {
        if (!this.online.containsKey(token)) {
            return false;
        }
        this.aidService.updateOrAdd(aid, div);
        return true;
    }

    @RequestMapping(
        path = {"/address/resolve"},
        params = {"aid"},
        method = RequestMethod.POST
    )
    @ResponseBody
    public List<String> resolve(long aid) {
        LinkedList<String> list = new LinkedList<>();
        long op = 0;
        short bit = 56;
        while (bit >= 0) {
            op |= 0xFFL << bit;
            long slice = aid | op;
            String d = this.aidService.query(slice);
            list.offer(d);
            bit -= 8;
        }
        return list;
    }


    @RequestMapping(
        path = {"/address/sub"},
        params = {"aid"},
        method = RequestMethod.POST
    )
    @ResponseBody
    public List<String> findSub(long aid) {
        return this.aidService.querySub(aid);
    }

    private Long online(int uid) {
        if (online.containsValue(uid)) {
            return null;
        }
        long token = new SecureRandom().nextLong();
        this.online.put(token, uid);
        return token;
    }
}
