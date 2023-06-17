package cool.muyucloud.housing.service;

import cool.muyucloud.housing.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {

    /**
     * Login with id and password
     *
     * @param id  user ID
     * @param pwd password
     * @return true means success, false mean fail
     */
    boolean login(int id, String pwd);

    /**
     * Login with email and password
     */
    Integer login(String email, String pwd);

    /**
     * Register with name, password and email
     * The account will not be verified until UserService.verify(...) pass
     * Unverified accounts cannot do anything
     *
     * @param name  user's name, can be duplicated
     * @param pwd   password
     * @param email e-mail used for registration, must be unique
     * @return a code that used for verification
     */
    Long register(String name, String pwd, String email);

    /**
     * Verify an account-registration via code given by UserService.register(...)
     *
     * @param code verification code given by UserService.register(...)
     * */
    Integer verifyReg(long code);

    /**
     * Update account's password, origin password is required
     *
     * @param id target user's ID
     * @param pwd origin password
     * @param newPwd new password
     * */
    boolean updatePwd(int id, String pwd, String newPwd);

    /**
     * Request to reset password,
     * a verification code will be sent to verified email
     *
     * @param id target user's IU
     * @return true means request approved, verification code was sent by email
     */
    Long resetPwdReq(int id);

    /**
     * Reset password via verification code sent from UserService.resetPwdReq
     *
     * @param code verification code
     * @param newPwd new password
     * @return true means password was successfully reset
     * */
    boolean resetPwd(long code, String newPwd);

    /**
     * Find user by ID
     *
     * @param id target user's ID
     * @return hidden-copy of found user, null means not found
     * */
    User find(int id);

    /**
     * Find user by name, support SQL pattern
     *
     * @param name target user's ID
     * @return hidden-copy of found user, null means not found
     * */
    List<User> find(String name);

    /**
     * Completely remove a user of specific id,
     * NO UNDO!
     *
     * @param id target user's ID
     */
    Long deleteReq(int id);

    boolean delete(long code);

    boolean updateType(int id, byte type);

    boolean updateName(int id, String name);
}
