package com.example.correo;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendSimpleEmail(String to, String subject, String text) {
        // Implementa aquí tu método de correo simple si quieres
    }

    public void sendMessageWithAttachment(
        String to,
        String subject,
        String text,
        org.springframework.web.multipart.MultipartFile file
    ) throws MessagingException, IOException {
        // Implementa aquí tu método para enviar correo con adjunto MultipartFile
    }

    // Método corregido para enviar correo con PDF interno (archivo estático)
    public void sendMessageWithInternalAttachment(
        String to,
        String subject,
        String text,
        String filePathInResources
    ) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        // Cargar el archivo completo en un arreglo de bytes para evitar el error
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePathInResources)) {
            if (inputStream == null) {
                throw new IOException("Archivo no encontrado: " + filePathInResources);
            }

            byte[] bytes = inputStream.readAllBytes();

            // Crear un recurso con los bytes que puede abrir nuevos streams
            ByteArrayResource resource = new ByteArrayResource(bytes);

            helper.addAttachment("documento.pdf", resource);
        }

        mailSender.send(message);
    }
}
