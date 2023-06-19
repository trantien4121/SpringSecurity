package com.trantien.demo.controller;

import com.trantien.demo.model.User;
import com.trantien.demo.payload.response.MessageResponse;
import com.trantien.demo.repository.UserRepository;
import com.trantien.demo.service.email.IEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.util.List;

@RestController
@RequestMapping(path = "api/email")
public class EmailController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    IEmailService iEmailService;

    @PostMapping("/testSendEmail")
    public ResponseEntity<?> testSendEmail() throws MessagingException {
        List<User> lstUser = userRepository.findAll();
        for (User user : lstUser) {
            String emailAddress = user.getEmail();
            iEmailService.sendEmail(emailAddress, "Notification Email", "This is a notification email send by system!");
        }
        return ResponseEntity.ok(new MessageResponse("Send Email successfully!"));
    }
}
