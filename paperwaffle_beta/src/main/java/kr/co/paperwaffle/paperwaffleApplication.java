package kr.co.paperwaffle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class paperwaffleApplication {

	public static void main(String[] args) {
		SpringApplication.run(paperwaffleApplication.class, args);
	}
	
	@GetMapping(value = {"", "index"})
	public String index() {
		return "/index";
	}


}
