package cool.muyucloud.housing.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    JavaMailSender sender;

    @Override
    public void sendVerify(String target, long code) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("MUYU Housing Estates");
        msg.setTo(target);
        msg.setSubject("Verification Code");
        msg.setText(
            "This is a register-verification email, if the registration is not submitted by you, please ignore.\n"
            + "Your verification code: %d".formatted(code));
        sender.send(msg);
    }
}
