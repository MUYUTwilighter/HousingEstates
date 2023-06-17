package cool.muyucloud.housing.dao;

import cool.muyucloud.housing.entity.Estate;
import cool.muyucloud.housing.entity.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavouriteDao extends JpaRepository<Favourite, Integer> {
    @Query(value = "select * from favourite where aid = ?1", nativeQuery = true)
    int query(long aid);

    @Query(value = "select e.* from favourite as f, estate as e where f.uid = ?1 and f.aid = e.aid", nativeQuery = true)
    List<Estate> query(int uid);

    @Query(value = "select * from favourite where aid = ?1 and uid = ?2", nativeQuery = true)
    Favourite query(int uid, long aid);
}
