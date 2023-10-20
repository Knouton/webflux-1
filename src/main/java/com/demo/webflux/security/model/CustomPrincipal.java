package com.demo.webflux.security.model;

import java.security.Principal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomPrincipal implements Principal {
	private String id;
	private String name;
}
