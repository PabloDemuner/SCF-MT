package com.scfapi.beans;

import java.util.Arrays;
import java.util.List;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	/*@EventListener
	private void teste(ApplicationReadyEvent event) {
		enviarEmail("pablod.teste1@hotmail.com", Arrays.asList("pablod.teste2@hotmail.com"), 
				"Testando", "Olá <br/> Teste ok!");
		System.out.print("E-mail enviado com sucesso!");
	}*/
	
	public void enviarEmail(String remetente, List<String> destinatarios, 
			String assunto, String mensagem) {
		
		try {
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			
			helper.setFrom(remetente);
			helper.setTo(destinatarios.toArray(new String[destinatarios.size()]));
			helper.setSubject(assunto);
			helper.setText(mensagem, true);
			
			mailSender.send(mimeMessage);
		} catch (Exception e) {
			throw new RuntimeException("Erro ao tentar enviar o e-mail!", e);
		}
	}
}
