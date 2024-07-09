package hutech.com.demo.Controller;

import hutech.com.demo.Constants.UserNotFoundException;
import hutech.com.demo.Utility;
import hutech.com.demo.model.User;
import hutech.com.demo.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/password")
public class ForgotPasswordController {
    @Autowired
    private JavaMailSender mailSender;
     
    @Autowired
    private UserService customerService;
     
    @GetMapping("/forgotPassword")
    public String showForgotPasswordForm(Model model) {
        model.addAttribute("Title", "Forgot Password");
        return "forgotPasswordForm";
    }
 
    @PostMapping("/forgotPassword")
    public String processForgotPassword(HttpServletRequest request,Model model) throws UserNotFoundException {
        String email = request.getParameter("email");
        String token = RandomString.make(45);
        try{
        customerService.updateResetPasswordToken(token,email);
        String resetPasswordLink = Utility.getSiteURL(request) + "/password/resetPassword?token=" + token;
        sendEmail(email, resetPasswordLink);
        }catch(Exception e){model.addAttribute("error", e.toString());}
        return "resetPasswordForm";
    }
     
    public void sendEmail(String email,String resetLink) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("abc@gmail.com","Contact me");
        helper.setTo(email);
        helper.setSubject("Click here to reset your password!");
        String content = "<p>Hello</p>"
                + "<p>Click the link to change your password!</p>"
                + "<p><b><a href=\"" + resetLink + "\">Change my Password</a><b></p>"
                + "<p>Ignore if you did not make the request!</p>";
        helper.setText(content, true);
        mailSender.send(message);
    }  
     
     
    @GetMapping("/resetPassword")
    public String showResetPasswordForm(@Param(value ="token")String token,Model model) {
        User user = customerService.getByResetPasswordToken(token);
        if(user == null) {
            model.addAttribute("Title", "Reset your Password");
            model.addAttribute("error", "Invalid Token");
            return "/users/login";
        }

        model.addAttribute("token", token);
        model.addAttribute("Page Title", "Reset Your Password");
        return "resetPasswordForm";
    }
     
    @PostMapping("/resetPassword")
    public String processResetPassword(HttpServletRequest request,Model model) throws UserNotFoundException {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
        User user = customerService.getByResetPasswordToken(token);
        if(user != null) {
            customerService.updatePassword(user,password);
            model.addAttribute("message", "Success!");
        }else{
            model.addAttribute("Title", "Reset your Password");
            model.addAttribute("error", "Invalid Token");
            return "/users/login";
        }

        return "/users/login";
    }
}