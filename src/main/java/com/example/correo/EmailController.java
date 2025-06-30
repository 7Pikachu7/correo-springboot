package com.example.correo;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/email")
@CrossOrigin(origins = "*")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @GetMapping("/send")
    public String sendEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String body
    ) {
        emailService.sendSimpleEmail(to, subject, body);
        return "Correo enviado exitosamente a " + to;
    }

    @PostMapping("/send-with-attachment")
    public String sendEmailWithAttachment(
        @RequestParam String to,
        @RequestParam String subject,
        @RequestParam String body,
        @RequestParam("file") MultipartFile file
    ) throws MessagingException, IOException {
        emailService.sendMessageWithAttachment(to, subject, body, file);
        return "Correo con adjunto enviado.";
    }

    //@GetMapping("/send-with-static-pdf")
    @PostMapping("/send-with-static-pdf")

    public ResponseEntity<String> sendEmailWithStaticPdf(
        @RequestParam String to,
        @RequestParam String subject,
        @RequestParam String body
    ) {
        try {
            emailService.sendMessageWithInternalAttachment(to, subject, body, "static/documento.pdf");
            return ResponseEntity.ok("Correo con PDF interno enviado.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error: " + e.getMessage());
        }
    }
}
