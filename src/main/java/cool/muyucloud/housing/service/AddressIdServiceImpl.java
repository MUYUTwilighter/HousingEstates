package cool.muyucloud.housing.service;

import cool.muyucloud.housing.dao.AddressIdDao;
import cool.muyucloud.housing.entity.AddressId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressIdServiceImpl implements AddressIdService {
    @Autowired
    private AddressIdDao dao;

    @Override
    public void updateOrAdd(long aid, String division) {
        AddressId entity = new AddressId(aid, division);
        this.dao.save(entity);
    }

    @Override
    public void delete(long aid) {
        this.dao.deleteById(aid);
    }
}
