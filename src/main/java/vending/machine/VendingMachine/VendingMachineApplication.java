package vending.machine.VendingMachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import vending.machine.VendingMachine.config.security.ApplicationConfig;
import vending.machine.VendingMachine.config.security.SecurityConfig;

@SpringBootApplication
public class VendingMachineApplication {
	public static void main(String[] args) {
		SpringApplication.run(VendingMachineApplication.class, args);
	}

}
