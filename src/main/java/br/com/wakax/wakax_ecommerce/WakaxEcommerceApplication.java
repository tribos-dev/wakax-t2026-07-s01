package br.com.wakax.wakax_ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@RequestMapping("/")
public class WakaxEcommerceApplication {

  public static void main(String[] args) {
    SpringApplication.run(WakaxEcommerceApplication.class, args);
  }
}
