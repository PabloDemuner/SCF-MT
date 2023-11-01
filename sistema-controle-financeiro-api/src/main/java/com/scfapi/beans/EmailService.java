package com.scfapi.beans;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.scfapi.model.Lancamento;
import com.scfapi.model.Usuario;
import com.scfapi.repository.LancamentoRepository;

@Component
public class EmailService {

	private static final String PATHTEMPLATE = "email/aviso-lancamentos-vencidos";
	private static final String EMAILEMPRESA = "pablod.teste1@hotmail.com";
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private TemplateEngine thymeleafTemplate;
	
	/*@Autowired
	private LancamentoRepository repo;
	
	@EventListener
	private void teste(ApplicationReadyEvent event) {
		
		String template = "email/aviso-lancamentos-vencidos";
		
		List<Lancamento> listaLancamentos = repo.findAll();
		
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("lancamentos", listaLancamentos);
		
		enviarEmail("pablod.teste1@hotmail.com", Arrays.asList("pablod.teste2@hotmail.com"), 
				"Testando", template, parametros);
		System.out.print("E-mail enviado com sucesso!");
	}
	
	@EventListener
	private void teste(ApplicationReadyEvent event) {
		enviarEmail("pablod.teste1@hotmail.com", Arrays.asList("pablod.teste2@hotmail.com"), 
				"Testando", "Olá <br/> Teste ok!");
		System.out.print("E-mail enviado com sucesso!");
	}*/
	
	public void avisoLancamentosVencidos(List<Lancamento> lancamentosVencidos, List<Usuario> destinatarios) {
		
		Map<String, Object> parametros = new HashMap<>();
		parametros.put("lancamentos", lancamentosVencidos);
		
		List<String> emails = destinatarios.stream().map(usuario -> usuario.getEmail())
				.collect(Collectors.toList());
		
		enviarEmail(EMAILEMPRESA, emails, "Lançamentos Vencidos", PATHTEMPLATE, parametros);
		System.out.print("E-mail enviado com sucesso!");
	}
	
	public void enviarEmail(String remetente, List<String> destinatarios, 
			String assunto, String template, Map<String, Object> parametros) {
		
		Context context = new Context(new Locale("pt", "BR"));
		
		parametros.entrySet().forEach(e -> context.setVariable(e.getKey(), e.getValue()));
		
		String mensagem = thymeleafTemplate.process(template, context);
		enviarEmail(remetente, destinatarios, assunto, mensagem);
	}
	
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
