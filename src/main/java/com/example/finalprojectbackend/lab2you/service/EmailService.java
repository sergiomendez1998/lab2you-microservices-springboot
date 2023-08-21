package com.example.finalprojectbackend.lab2you.service;


import com.example.finalprojectbackend.lab2you.db.model.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

   private final JavaMailSender javaMailSender;

    @Autowired
    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendRegistrationEmail(UserDTO user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Bienvenido a lab2you!");
        message.setText("Hello " + user.getName() + " " + user.getLastName()
                + ",\n\n" + "Tu cuenta ha sido creada exitosamente.\n\n"
                + "Tu usuario es: "
                + user.getEmail() + "\n" + "Tu Contraseña: "
                + user.getPassword() + "\n\n" + "Gracias por elegir lab2you donde te hacemos la vida facil en tus laboratorios!\n\n"
                + "Salduos Coordiales,\n"
                + "Equipo lab2you");
        javaMailSender.send(message);
    }
}
