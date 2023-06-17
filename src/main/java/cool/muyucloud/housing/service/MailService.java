package cool.muyucloud.housing.service;

import org.springframework.stereotype.Service;

@Service
public interface MailService {
    void sendVerify(String target, long code);
}
