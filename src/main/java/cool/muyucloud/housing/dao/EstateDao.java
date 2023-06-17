package cool.muyucloud.housing.dao;

import cool.muyucloud.housing.entity.Estate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EstateDao extends JpaRepository<Estate, Long> {
    @Query(value = "select * from estate " +
        "   where (aid & 0xFF00000000000000 = 0 or aid & 0xFF00000000000000 = ?1 & 0xFF00000000000000)" +
        "       and (aid & 0x00FF000000000000 = 0 or aid & 0x00FF000000000000 = ?1 & 0x00FF000000000000)" +
        "       and (aid & 0x0000FF0000000000 = 0 or aid & 0x0000FF0000000000 = ?1 & 0x0000FF0000000000)" +
        "       and (aid & 0x000000FF00000000 = 0 or aid & 0x000000FF00000000 = ?1 & 0x000000FF00000000)" +
        "       and (aid & 0x00000000FF000000 = 0 or aid & 0x00000000FF000000 = ?1 & 0x00000000FF000000)" +
        "       and (aid & 0x0000000000FF0000 = 0 or aid & 0x0000000000FF0000 = ?1 & 0x0000000000FF0000)" +
        "       and (aid & 0x000000000000FF00 = 0 or aid & 0x000000000000FF00 = ?1 & 0x000000000000FF00)" +
        "       and (aid & 0x00000000000000FF = 0 or aid & 0x00000000000000FF = ?1 & 0x00000000000000FF)",
    nativeQuery = true)
    List<Estate> query(long aid);

    @Query(value = "select * from estate where uid = ?1", nativeQuery = true)
    List<Estate> query(int uid);

    @Query(value = "delete from estate where aid = ?1", nativeQuery = true)
    void delete(long uid);

    @Query(value = "delete from estate where owner = ?1", nativeQuery = true)
    void delete(int uid);
}
