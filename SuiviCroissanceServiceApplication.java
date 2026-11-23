package tn.pedialink.suivicroissance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class SuiviCroissanceServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SuiviCroissanceServiceApplication.class, args);
    }
}
