package cool.muyucloud.housing.service;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AddressIdService {
    String query(long aid);

    List<String> querySub(long aid);

    void updateOrAdd(long aid, String division);

    void delete(long aid);
}
