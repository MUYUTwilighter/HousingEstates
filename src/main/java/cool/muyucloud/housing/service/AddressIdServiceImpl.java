package cool.muyucloud.housing.service;

import cool.muyucloud.housing.dao.AddressIdDao;
import cool.muyucloud.housing.entity.AddressId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class AddressIdServiceImpl implements AddressIdService {
    @Autowired
    private AddressIdDao dao;

    @Override
    public String query(long aid) {
        AddressId map = this.dao.query(aid);
        if (map == null) {
            return null;
        }
        return map.getDivision();
    }

    @Override
    public List<String> querySub(long aid) {
        LinkedList<String> list = new LinkedList<>();
        long slice = 0, op, b;
        short bit = 56;
        while (true) {
            op = 0xFFL << bit;
            b = op | aid;
            if (b == 0L) {
                break;
            }
            slice |= b;
            bit -= 8;
        }
        for (short i = 1; i < 256; ++i) {
            op = (long) i << bit;
            slice |= op;
            AddressId map = dao.query(slice);
            if (map == null) {
                return list;
            }
            list.offer(map.getDivision());
        }
        return list;
    }

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
