package com.backend.vofasbackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
        info = @Info(
                title = "VoFAS Backend API",
                version = "beta",
                description = "This backend services provides RESTful services for VoFAS frontend including: " +
                        "feedback management, analytics & kiosk contoller operations",
                contact = @Contact(
                        name = "OZGUR KAMALI",
                        email = "ozgurkamaliprofessional@gmail.com"
                )
        )
)
@SpringBootApplication
public class VoFasBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoFasBackendApplication.class, args);
    }

}
