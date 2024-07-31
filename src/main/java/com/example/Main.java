package com.example;

import org.apache.catalina.startup.Tomcat;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        String webappDirLocation = "src/main/webapp/";
        Tomcat tomcat = new Tomcat();

        // Устанавливаем порт сервера
        String webPort = System.getenv("PORT");
        if (webPort == null || webPort.isEmpty()) {
            webPort = "8080";
        }
        tomcat.setPort(Integer.parseInt(webPort));

        // Добавляем webapp
        tomcat.addWebapp("", new File(webappDirLocation).getAbsolutePath());

        System.out.println("Configuring app with basedir: " + new File("./" + webappDirLocation).getAbsolutePath());

        // Запускаем сервер
        tomcat.start();
        tomcat.getServer().await();
    }
}
