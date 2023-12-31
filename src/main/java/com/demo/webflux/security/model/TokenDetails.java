package com.demo.webflux.security.model;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TokenDetails {
	private String userId;
	private String token;
	private Date issuedAt;
	private Date expiresAt;
}
