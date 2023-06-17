package cool.muyucloud.housing.service;

import cool.muyucloud.housing.dao.FavouriteDao;
import cool.muyucloud.housing.entity.Estate;
import cool.muyucloud.housing.entity.Favourite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavouriteServiceImpl implements FavouriteService {
    @Autowired
    private FavouriteDao dao;

    @Override
    public int query(long aid) {
        return this.dao.query(aid);
    }

    @Override
    public List<Estate> query(int uid) {
        return this.dao.query(uid);
    }

    @Override
    public void add(int uid, long aid) {
        Favourite f = new Favourite(uid, aid);
        this.dao.save(f);
    }

    @Override
    public void remove(int uid, long aid) {
        Favourite f = new Favourite(uid, aid);
        this.dao.delete(f);
    }
}
