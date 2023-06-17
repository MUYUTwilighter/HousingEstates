package cool.muyucloud.housing.service;

import cool.muyucloud.housing.entity.Estate;

import java.util.List;

public interface FavouriteService {
    int query(long aid);

    List<Estate> query(int uid);

    void add(int uid, long aid);

    void remove(int uid, long aid);
}
