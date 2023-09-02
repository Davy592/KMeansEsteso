package com.example.springserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <h2>La classe ServerApplication è la classe principale del server.</h2>
 */
@SpringBootApplication
public class ServerApplication {

	/**
	 * <h4>Il metodo main.</h4>
	 * @param args gli argomenti
	 */
	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
