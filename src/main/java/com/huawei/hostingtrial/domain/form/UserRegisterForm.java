package com.huawei.hostingtrial.domain.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.ScriptAssert;

// Requires scripting engine (e.g. Rhino included automatically with Java 6)


@ScriptAssert(
	lang = "javascript",
	script = "_this.confirmPassword.equals(_this.password)",
	message = "account.password.mismatch.message")
public class UserRegisterForm {
	private String username, firstName, lastName, password, confirmPassword, email, imsUsername, imsPassword, description;
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	//used when updating user
	private String userId;

	public String getUserId() {
		return userId;
	}
	
	@NotNull
	@Pattern(regexp="^[A-Za-z0-9_-]$")
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@NotNull
	@Pattern(regexp="^[A-Za-z0-9_-]$")
	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@NotNull
	@Size(min = 1, max = 50)
	public String getImsUsername() {
		return imsUsername;
	}

	public void setImsUsername(String imsUsername) {
		this.imsUsername = imsUsername;
	}

	@NotNull
	@Size(min = 1, max = 50)
	public String getImsPassword() {
		return imsPassword;
	}

	public void setImsPassword(String imsPassword) {
		this.imsPassword = imsPassword;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@NotNull
	@Size(min = 1, max = 50)
	@Pattern(regexp="^[A-Za-z0-9_-]$")
	public String getUsername() { return username; }

	public void setUsername(String userName) { this.username = userName; }

	@NotNull
	@Size(min = 1, max = 50)
	@Pattern(regexp="^[A-Za-z0-9_-]$")
	public String getPassword() { return password; }

	public void setPassword(String password) { this.password = password; }

	public String getConfirmPassword() { return confirmPassword; }

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@NotNull
	@Size(min = 3, max = 50)
	@Email
	public String getEmail() { return email; }

	public void setEmail(String email) { this.email = email; }
	
	public String toString() {
		return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
			.append("username", username)
			.append("email", email)
			.toString();			
	}		
}
