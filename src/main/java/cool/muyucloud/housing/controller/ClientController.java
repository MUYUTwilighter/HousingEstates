package cool.muyucloud.housing.controller;

import cool.muyucloud.housing.entity.Estate;
import cool.muyucloud.housing.entity.User;
import cool.muyucloud.housing.service.EstateService;
import cool.muyucloud.housing.service.FavouriteService;
import cool.muyucloud.housing.service.MailService;
import cool.muyucloud.housing.service.UserService;
import cool.muyucloud.housing.util.LoginAffair;
import cool.muyucloud.housing.util.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Controller
public class ClientController {
    private static final long KEEP_ALIVE = 300000;

    private final HashMap<Long, LoginAffair> affairs = new HashMap<>();
    @Autowired
    UserService userService;
    @Autowired
    MailService mailService;
    @Autowired
    EstateService estateService;
    @Autowired
    FavouriteService favouriteService;

    /* Login & Register */
    @RequestMapping(
        path = {"/login/uid"},
        params = {"uid", "pwd"},
        method = RequestMethod.GET
    )
    @ResponseBody
    public long login(int uid, String pwd) {
        if (this.userService.login(uid, pwd)) {
            return 0L;
        } else {
            return this.addAffair(uid);
        }
    }

    @RequestMapping(
        path = {"/login/email"},
        params = {"email", "pwd"},
        method = RequestMethod.GET
    )
    @ResponseBody
    public long login(String email, String pwd) {
        if (!isValidEmail(email)) {
            return 0;
        }
        Integer uid = this.userService.login(email, pwd);
        if (uid == null) {
            return 0;
        }
        return this.addAffair(uid);
    }

    @RequestMapping(
        path = {"/register/req"},
        params = {"name", "email", "pwd"},
        method = RequestMethod.GET
    )
    @ResponseBody
    public boolean register(String name, String email, String pwd) {
        if (!isValidEmail(email)) {
            return false;
        }
        Long code = this.userService.register(name, email, pwd);
        if (code == null) {
            return false;
        } else {
            mailService.sendVerify(email, code);
            return true;
        }
    }

    @RequestMapping(
        path = {"/register/verify"},
        params = {"code"},
        method = RequestMethod.PUT
    )
    @ResponseBody
    public Integer verifyRegister(long code) {
        return this.userService.verifyReg(code);
    }

    /* Account Service */
    @RequestMapping(
        path = {"/account/resetPwd/req"},
        params = {"uid"},
        method = RequestMethod.GET
    )
    @ResponseBody
    public boolean resetPwdReq(int uid) {
        Long code = this.userService.resetPwdReq(uid);
        if (code == null) {
            return false;
        }
        User user = this.userService.find(uid);
        this.mailService.sendVerify(user.getEmail(), code);
        return true;
    }

    @RequestMapping(
        path = {"/account/resetPwd/exec"},
        params = {"code", "newPwd"},
        method = RequestMethod.PUT
    )
    @ResponseBody
    public boolean resetPwd(long code, String newPwd) {
        return this.userService.resetPwd(code, newPwd);
    }

    @RequestMapping(
        path = {"/account/delete/req"},
        params = {"token", "uid"},
        method = RequestMethod.GET
    )
    @ResponseBody
    public boolean deleteUserReq(long token) {
        Integer uid = this.accessAffair(token);
        if (uid == null) {
            return false;
        }
        Long code = this.userService.deleteReq(uid);
        if (code == null) {
            return false;
        }
        String email = this.userService.find(uid).getEmail();
        this.mailService.sendVerify(email, code);
        return true;
    }

    @RequestMapping(
        path = {"/account/delete/exec"},
        params = {"token", "code"},
        method = RequestMethod.DELETE
    )
    @ResponseBody
    public boolean deleteUser(long token, long code) {
        Integer uid = this.accessAffair(token);
        if (uid == null) {
            return false;
        }
        if (!this.userService.delete(code)) {
            return false;
        }
        this.estateService.delete(uid);
        this.affairs.remove(token);
        return true;
    }

    @RequestMapping(
        path = {"/account/updatePwd"},
        params = {"token", "uid", "pwd", "newPwd"},
        method = RequestMethod.PUT
    )
    @ResponseBody
    public boolean updatePwd(long token, String pwd, String newPwd) {
        Integer uid = this.accessAffair(token);
        if (uid == null) {
            return false;
        }
        return this.userService.updatePwd(uid, pwd, newPwd);
    }

    @RequestMapping(
        path = {"/account/update/type"},
        params = {"token", "uid", "type"},
        method = RequestMethod.PUT
    )
    @ResponseBody
    public boolean updateType(long token, int uid, byte type) {
        Integer opId = this.accessAffair(token);
        // Can not find operator by ID
        if (opId == null) {
            return false;
        }
        User op = this.userService.find(opId);
        // Operator has limited auth
        if (op.getType() != User.TYPE_USER_ADMIN) {
            return false;
        }
        User target = this.userService.find(uid);
        // Can not find target by ID or target is prior
        if (target == null || target.getType() == User.TYPE_USER_ADMIN) {
            return false;
        }
        target.setType(type);
        this.updateType(token, uid, type);
        return true;
    }

    @RequestMapping(
        path = {"/account/update/name"},
        params = {"token", "name"},
        method = RequestMethod.PUT
    )
    @ResponseBody
    public boolean updateName(long token, String name) {
        Integer uid = this.accessAffair(token);
        if (uid == null) {
            return false;
        }
        return this.userService.updateName(uid, name);
    }

    @RequestMapping(
        path = {"/account/logout"},
        params = {"token", "uid"},
        method = RequestMethod.PUT
    )
    @ResponseBody
    public boolean logout(long token) {
        Integer access = this.accessAffair(token);
        if (access == null) {
            return false;
        }
        this.affairs.remove(token);
        return true;
    }

    @RequestMapping(
        path = {"/account/keep_alive"},
        params = {"token"},
        method = RequestMethod.HEAD
    )
    @ResponseBody
    public boolean keepAlive(long token) {
        return this.accessAffair(token) == null;
    }

    /* Search Service */
    @RequestMapping(
        path = {"/search/user/name"},
        params = {"name"},
        method = RequestMethod.POST
    )
    @ResponseBody
    public List<User> searchUserByName(String name) {
        return this.userService.find(name);
    }

    @RequestMapping(
        path = {"/search/user/id"},
        params = {"id"},
        method = RequestMethod.POST
    )
    @ResponseBody
    public User searchUserById(int id) {
        return this.userService.find(id);
    }

    @RequestMapping(
        path = {"/search/estate/aid"},
        params = {"aid"},
        method = RequestMethod.POST
    )
    @ResponseBody
    public List<Estate> searchEstate(long aid) {
        return estateService.query(aid);
    }

    @RequestMapping(
        path = {"/search/estate/uid"},
        params = {"uid"},
        method = RequestMethod.POST
    )
    @ResponseBody
    public List<Estate> searchEstate(int uid) {
        return estateService.query(uid);
    }

    /* Estate Service */
    @RequestMapping(
        path = {"/estate/add"},
        params = {"token", "aid", "square", "price"},
        method = RequestMethod.PUT
    )
    @ResponseBody
    public boolean addEstate(long token, long aid, double square, double price) {
        Integer uid = this.accessAffair(token);
        // Token is bad or address ID invalid
        if (uid == null || !isUniqueAid(aid)) {
            return false;
        }
        // Estate already exists
        if (!estateService.query(aid).isEmpty()) {
            return false;
        }
        estateService.updateOrAdd(aid, uid, square, price);
        return true;
    }

    @RequestMapping(
        path = {"/estate/update"},
        params = {"token", "aid", "price", "square"},
        method = RequestMethod.PUT
    )
    @ResponseBody
    public boolean updateEstate(long token, long aid, double price, double square) {
        Integer uid = this.accessAffair(token);
        if (uid == null || !isUniqueAid(aid)) {
            return false;
        }
        List<Estate> estates = estateService.query(aid);
        if (estates.isEmpty()) {
            return false;
        }
        Estate estate = estates.get(0);
        User user = userService.find(uid);
        if (!Objects.equals(estate.getOwner(), uid) || user.getType() < User.TYPE_ESTATE_ADMIN) {
            return false;
        }
        estateService.updateOrAdd(aid, uid, price, square);
        return true;
    }

    @RequestMapping(
        path = {"/estate/remove"},
        params = {"token", "aid"},
        method = RequestMethod.PUT
    )
    @ResponseBody
    public boolean removeEstate(long token, long aid) {
        Integer uid = this.accessAffair(token);
        if (uid == null || !isUniqueAid(aid)) {
            return false;
        }
        List<Estate> estates = estateService.query(aid);
        if (estates.isEmpty()) {
            return false;
        }
        Estate estate = estates.get(0);
        User user = userService.find(uid);
        if (!Objects.equals(estate.getOwner(), uid) || user.getType() < User.TYPE_ESTATE_ADMIN) {
            return false;
        }
        estateService.delete(aid);
        return true;
    }

    /* Favourite Service */
    @RequestMapping(
        path = {"/favourite/add"},
        params = {"token", "aid"},
        method = RequestMethod.PUT
    )
    @ResponseBody
    public boolean addFavourite(long token, long aid) {
        Integer uid = this.accessAffair(token);
        if (uid == null || !isUniqueAid(aid)) {
            return false;
        }
        List<Estate> estates = estateService.query(aid);
        if (estates.isEmpty()) {
            return false;
        }
        this.favouriteService.add(uid, aid);
        return true;
    }

    @RequestMapping(
        path = {"/favourite/remove"},
        params = {"token", "aid"},
        method = RequestMethod.DELETE
    )
    @ResponseBody
    public boolean removeFavourite(long token, long aid) {
        Integer uid = this.accessAffair(token);
        if (uid == null || !isUniqueAid(aid)) {
            return false;
        }
        List<Estate> estates = estateService.query(aid);
        if (estates.isEmpty()) {
            return false;
        }
        this.favouriteService.remove(uid, aid);
        return true;
    }

    @RequestMapping(
        path = {"/favourite/user"},
        params = {"token"},
        method = RequestMethod.DELETE
    )
    @ResponseBody
    public List<Estate> userFavourite(long token) {
        Integer uid = this.accessAffair(token);
        if (uid == null) {
            return null;
        }
        return this.favouriteService.query(uid);
    }

    @RequestMapping(
        path = {"/favourite/estate"},
        params = {"aid"},
        method = RequestMethod.DELETE
    )
    @ResponseBody
    public int estateFavourite(long aid) {
        return this.favouriteService.query(aid);
    }

    /* Private Utils */
    private static boolean isUniqueAid(long aid) {
        while (aid != 0) {
            byte b = (byte) (aid | 0xFF);
            if (b == 0) {
                return false;
            }
            aid >>>= 8;
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        return Pattern.matches("[a-zA-Z0-9]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+", email);
    }

    private Long addAffair(int uid) {
        LoginAffair affair = new LoginAffair(uid, KEEP_ALIVE * 4);
        if (this.affairs.containsValue(affair)) {
            return 0L;
        }
        long code = TokenGenerator.genToken();
        this.affairs.put(code, affair);
        return code;
    }

    private Integer accessAffair(long token) {
        LoginAffair affair = this.affairs.get(token);
        if (affair == null || affair.isExpired()) {
            this.affairs.remove(token);
            return null;
        }
        affair.extend(KEEP_ALIVE);
        return affair.uid();
    }
}
