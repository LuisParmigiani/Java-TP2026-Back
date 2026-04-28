package soda_roja.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void enviarMail(String destino, String asunto, String cuerpo) {
    	try{
    	//MimeMessage permite mandar un mail con fromato HTML   	
    	MimeMessage mensaje = mailSender.createMimeMessage(); 
    	//El helper es para no tener que tocar el MimeMessage directamente (en Jakarta), sino a través de este helper
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

        helper.setTo(destino);
        helper.setSubject(asunto);
        
        //true para enviar html
        // Footer with blue color and italics
        String footer = "<br><br><span style=\"color:blue; font-style:italic;\">"
            + "Soda Roja® - Calle Principal 123, Centro<br>"
            + "Contacto: sodaroja.java@gmail.com / +52 123 456 7890"
            + "</span>";
        helper.setText(cuerpo + footer, true);

        // Marcar como importante
        mensaje.setHeader("X-Priority", "1");
        // Otro formato de importante de algunos clientes de correo
        mensaje.setHeader("Importance", "High");

        mailSender.send(mensaje);
    } catch (MailException | MessagingException e) {
        throw new RuntimeException("Error al enviar el mail: " + e.getMessage());
    }
    }
}