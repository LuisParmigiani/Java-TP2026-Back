package soda_roja.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import soda_roja.backend.dtoRequest.MailDTORequest;
import soda_roja.backend.service.MailService;

@RestController
@RequestMapping("/api/email")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    public ResponseEntity<String> enviar(@RequestBody MailDTORequest request) {
        try{
        	 mailService.enviarMail(request.getDestino(), request.getAsunto(), request.getCuerpo());
        	 return ResponseEntity.ok("Mail enviado");
        }
        catch(Exception e){
			return ResponseEntity.status(500).body("Error al enviar el mail: " + e.getMessage());
		}
        
    }
}
