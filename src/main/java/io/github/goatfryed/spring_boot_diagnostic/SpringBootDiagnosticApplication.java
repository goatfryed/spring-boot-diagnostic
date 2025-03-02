package io.github.goatfryed.spring_boot_diagnostic;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class SpringBootDiagnosticApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootDiagnosticApplication.class, args);
    }

}
