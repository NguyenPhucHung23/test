package Practice.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class EmailService {

    private static final Logger logger = Logger.getLogger(EmailService.class.getName());

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendOtpEmail(String toEmail, String otpCode) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("hungnguyen23072004@gmail.com");
            message.setTo(toEmail);
            message.setSubject("Mã xác thực đặt lại mật khẩu");
            message.setText(
                    "Xin chào,\n\n" +
                            "Bạn đã yêu cầu đặt lại mật khẩu.\n\n" +
                            "Mã OTP của bạn là: " + otpCode + "\n\n" +
                            "Mã này có hiệu lực trong 5 phút.\n\n" +
                            "Nếu bạn không yêu cầu đặt lại mật khẩu, vui lòng bỏ qua email này.\n\n" +
                            "Trân trọng,\n" +
                            "Practice Team"
            );

            mailSender.send(message);
            logger.info("OTP email sent successfully to: " + toEmail);

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to send OTP email to: " + toEmail, e);
            throw new RuntimeException("Không thể gửi email. Vui lòng thử lại sau.");
        }
    }
}
