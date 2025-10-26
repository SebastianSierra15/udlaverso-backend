package com.udlaverso.udlaversobackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.imageio.ImageIO;

@SpringBootApplication
public class UdlaversoBackendApplication {

    public static void main(String[] args) {
        // 🔧 Forzar registro de plugins de TwelveMonkeys
        ImageIO.scanForPlugins();

        System.out.println("=== FORMATS DISPONIBLES ===");
        for (String format : ImageIO.getWriterMIMETypes()) {
            System.out.println(format);
        }
        System.out.println("============================");

        SpringApplication.run(UdlaversoBackendApplication.class, args);
    }
}
