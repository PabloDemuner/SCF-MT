package com.scfapi.util;

import java.util.Base64;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorSenha {

	public static void main(String[] args) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		System.out.println(passwordEncoder.encode("adminPablo"));
		
		/*byte[] decodificado = Base64.getDecoder().decode("$2a$10$X607ZPhQ4EgGNaYKt3n4SONjIv9zc.VMWdEuhCuba7oLAL5IvcL5.");
		
		String senhaDecodificada = new String(decodificado);
		System.out.println(senhaDecodificada);*/
	}
}
