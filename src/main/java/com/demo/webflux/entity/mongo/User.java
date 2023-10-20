package com.demo.webflux.entity.mongo;

import com.demo.webflux.entity.UserRole;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
	@Id
	private String id;
	private String username;
	private String password;
	private UserRole role;
	private String firstName;
	private String lastName;
	private boolean enabled;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	@ToString.Include(name = "password")
	private String maskPassword() {
		return "******";
	}
}
