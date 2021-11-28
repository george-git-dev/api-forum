package br.com.alura.forum.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

//Service para gerar token

@Service
public class TokenService {

	@Value("${forum.jwt.expiration}")
	private String expiration;

	@Value("${forum.jwt.secret}")
	private String secret;

	public String gerarToken(Authentication authenticate) {
		// Pegando o usuario
		Usuario user = (Usuario) authenticate.getPrincipal();
		// Criando data de criacao e expiracao
		Date hoje = new Date();

		Date dataExpired = new Date(hoje.getTime() + Long.parseLong(expiration));

		// Metodo de geracao de token

		return Jwts.builder().setIssuer("Api Forum").setSubject(user.getId().toString()).setIssuedAt(hoje)
				.setExpiration(dataExpired).signWith(SignatureAlgorithm.HS256, secret).compact();

	}

	public boolean isTokenValido(String token) {
		try {
			Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token);
			return true;

		} catch (Exception e) {
			return false;
		}

	}

}