package ru.briks;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.slf4j.LoggerFactory.getLogger;

@SpringBootApplication
public class BriksApplication {
    public static final Logger log = getLogger(BriksApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(BriksApplication.class, args);
    }
}
