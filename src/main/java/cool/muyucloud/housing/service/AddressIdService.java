package cool.muyucloud.housing.service;

import org.springframework.stereotype.Service;

@Service
public interface AddressIdService {
    void updateOrAdd(long aid, String division);

    void delete(long aid);
}
