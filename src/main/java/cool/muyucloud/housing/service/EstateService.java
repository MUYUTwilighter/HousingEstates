package cool.muyucloud.housing.service;

import cool.muyucloud.housing.entity.Estate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface EstateService {
    List<Estate> query(long aid);

    boolean updateOrAdd(long aid, int uid, double price, double square);

    void delete(long aid);

    void delete(int uid);
}
