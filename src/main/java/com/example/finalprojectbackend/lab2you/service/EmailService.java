package com.example.finalprojectbackend.lab2you.service;


import com.example.finalprojectbackend.lab2you.db.model.dto.CustomerDTO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

   private final JavaMailSender javaMailSender;


    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendRegistrationEmail(CustomerDTO customerDTO) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(customerDTO.getUser().getEmail());
        message.setSubject("Bienvenido a lab2you!");
        message.setText("Hello " + customerDTO.getFirstName() + " " + customerDTO.getLastName()
                + ",\n\n" + "Tu cuenta ha sido creada exitosamente.\n\n"
                + "Tu usuario es: "
                + customerDTO.getUser().getEmail() + "\n" + "Tu Contraseña: "
                + customerDTO.getUser().getPassword() + "\n\n" + "Gracias por elegir lab2you donde te hacemos la vida facil en tus laboratorios!\n\n"
                + "Salduos Coordiales,\n"
                + "Equipo lab2you");
        javaMailSender.send(message);
    }
}
