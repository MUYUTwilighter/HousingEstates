package cool.muyucloud.housing.service;

import cool.muyucloud.housing.dao.EstateDao;
import cool.muyucloud.housing.entity.Estate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstateServiceImpl implements EstateService {
    @Autowired
    EstateDao dao;

    @Override
    public List<Estate> query(long aid) {
        return this.dao.query(aid);
    }

    public List<Estate> query(int uid) {
        return this.dao.query(uid);
    }

    @Override
    public boolean updateOrAdd(long aid, int uid, double price, double square) {
        Estate estate = new Estate(aid, uid, price, square);
        this.dao.save(estate);
        return true;
    }

    @Override
    public void delete(long aid) {
        this.dao.delete(aid);
    }

    @Override
    public void delete(int uid) {
        this.dao.delete(uid);
    }
}
