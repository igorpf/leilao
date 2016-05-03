package com.leilao;

import com.leilao.GUI.MainLayout;
import javafx.application.Preloader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Lazy;

@Lazy
@SpringBootApplication
public class LeilaoApplication extends AbstractJavaFxApplicationSupport {

    @Value("${app.ui.title:Leilao}")//
    private String windowTitle;

    @Autowired//
    private MainLayout mainLayout;

    public static void main(String[] args) {
        launchApp(LeilaoApplication.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        notifyPreloader(new Preloader.StateChangeNotification(Preloader.StateChangeNotification.Type.BEFORE_START));
        stage.setTitle(windowTitle);
        stage.setScene(new Scene(mainLayout));
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }
}
