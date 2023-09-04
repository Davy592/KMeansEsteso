package com.example.springserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * <h2>La classe ServerApplication è la classe principale del server.</h2>
 */
@SpringBootApplication

public class ServerApplication {

	/**
	 * <h4>Metodo main, da cui viene avviato il server.</h4>
	 * @param args gli argomenti
	 */
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
