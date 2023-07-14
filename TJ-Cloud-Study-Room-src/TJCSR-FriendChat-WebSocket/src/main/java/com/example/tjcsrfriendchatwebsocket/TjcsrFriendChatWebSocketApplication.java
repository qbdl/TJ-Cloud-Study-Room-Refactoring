package com.example.tjcsrfriendchatwebsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity   //开启security
public class TjcsrFriendChatWebSocketApplication {

	public static void main(String[] args) {
		SpringApplication.run(TjcsrFriendChatWebSocketApplication.class, args);
	}

}
