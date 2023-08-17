package com.demo.webflux.security.token;

import com.demo.webflux.exception.AuthException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PBFDK2Encoder implements PasswordEncoder {

	@Value("${jwt.password.encoder.secret}")
	private String secret;
	@Value("${jwt.password.encoder.iteration}")
	private Integer iteration;
	@Value("${jwt.password.encoder.keylength}")
	private Integer keyLength;

	private static final String SECRET_KEY_INSTANCE = "PBKDF2WithHmacSHA512";

	@Override
	public String encode(final CharSequence rawPassword) {
		try {
			byte[] result= SecretKeyFactory.getInstance(SECRET_KEY_INSTANCE)
					.generateSecret(new PBEKeySpec(rawPassword.toString().toCharArray(),
					                secret.getBytes(), iteration, keyLength))
					.getEncoded();
			return Base64.getEncoder().encodeToString(result);
		} catch (InvalidKeySpecException e) {
			log.error("IN encode InvalidKeySpecException", e);
			throw new AuthException(e.getMessage(),"INVALID_KEY_SPEC");
		} catch (NoSuchAlgorithmException e) {
			log.error("IN encode NoSuchAlgorithmException", e);
			throw new AuthException(e.getMessage(), "NO_SUCH_ALGORITHM");
		}
	}

	@Override
	public boolean matches(final CharSequence rawPassword, final String encodedPassword) {
		return encode(rawPassword).equals(encodedPassword);
	}
}
