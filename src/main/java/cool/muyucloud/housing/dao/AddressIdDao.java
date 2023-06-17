package cool.muyucloud.housing.dao;

import cool.muyucloud.housing.entity.AddressId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressIdDao extends JpaRepository<AddressId, Long> {
    @Query(value = "select * from address_id where id = ?1", nativeQuery = true)
    AddressId query(long aid);
}
