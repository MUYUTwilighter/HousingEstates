package cool.muyucloud.housing.dao;

import cool.muyucloud.housing.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserDao extends JpaRepository<User, Integer> {
    @Query(value = "select id, name, email, type from user where name like ?1", nativeQuery = true)
    List<User> queryHiddenByName(String name);

    @Query(value = "select id, name, email, type from user where email = ?1", nativeQuery = true)
    User queryHiddenByEmail(String email);

    @Query(value = "select id, name, email, type from user where id = ?1", nativeQuery = true)
    User queryHiddenById(Integer id);

    @Query(value = "select * from user where email = ?1", nativeQuery = true)
    User queryByEmail(String email);

    @Query(value = "select * from user where id = ?1", nativeQuery = true)
    User queryById(Integer id);
}
