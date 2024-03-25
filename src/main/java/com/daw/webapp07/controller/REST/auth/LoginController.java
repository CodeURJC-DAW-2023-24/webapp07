package com.daw.webapp07.controller.REST.auth;

import com.daw.webapp07.DTO.GraphicsDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.daw.webapp07.security.jwt.AuthResponse;
import com.daw.webapp07.security.jwt.AuthResponse.Status;
import com.daw.webapp07.security.jwt.LoginRequest;
import com.daw.webapp07.security.jwt.UserLoginService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class LoginController {

	@Autowired
	private UserLoginService userService;

	@Operation(summary = "Login", description = "Authenticate user and generate access token.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Login successful",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = AuthResponse.class))),
			@ApiResponse(responseCode = "401", description = "Unauthorized - Invalid credentials", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
	})

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> login(
			@CookieValue(name = "accessToken", required = false) String accessToken,
			@CookieValue(name = "refreshToken", required = false) String refreshToken,
			@RequestBody LoginRequest loginRequest) {
		
		return userService.login(loginRequest, accessToken, refreshToken);
	}

	@Operation(summary = "Refresh Token", description = "Refreshes the access token using the refresh token.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Token refreshed successfully",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = AuthResponse.class))),
			@ApiResponse(responseCode = "400", description = "Bad Request - Missing or invalid refresh token", content = @Content),
			@ApiResponse(responseCode = "401", description = "Unauthorized - Invalid refresh token", content = @Content),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
	})

	@PostMapping("/refresh")
	public ResponseEntity<AuthResponse> refreshToken(
			@CookieValue(name = "refreshToken", required = false) String refreshToken) {

		return userService.refresh(refreshToken);
	}

	@Operation(summary = "Logout", description = "Logs out the current user.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Logout successful",
					content = @Content(mediaType = "application/json",
							schema = @Schema(implementation = AuthResponse.class))),
			@ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content)
	})

	@PostMapping("/logout")
	public ResponseEntity<AuthResponse> logOut(HttpServletRequest request, HttpServletResponse response) {

		return ResponseEntity.ok(new AuthResponse(Status.SUCCESS, userService.logout(request, response)));
	}
}
