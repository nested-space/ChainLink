package com.edenrump;

import com.edenrump.config.Defaults;
import com.edenrump.controllers.MainWindowController;
import com.edenrump.models.Link;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.*;

/**
 * This class represents the main class of the JavaFX Application.
 */
public class ChainLink extends Application {

    /**
     * Launch method of the application
     * @param stage the primary stage of the application
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception{
        FXMLLoader loader;
        Parent root;
        loader = new FXMLLoader(getClass().getResource("/fxml/MainWindow.fxml"));
        root = loader.load();
        MainWindowController mainWindowController = loader.getController();
        mainWindowController.setUp(getSeedLinks());
        stage.setTitle(Defaults.APP_NAME);
        stage.getIcons().setAll(new Image(getClass().getResourceAsStream("/img/link.png")));
        stage.setScene(new Scene(root, 600, 300));
        stage.show();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/QuickNoteMainWindow.fxml"));

    }

    /**
     * Utility method to seed the applicaiton. To be replaced by a loader.
     * @return a map of links to seed
     */
    private List<Link> getSeedLinks() {
        List<Link> linkListMap = new ArrayList<>();
        Link link = new Link("Google" , "Google Home Page","www.google.com");
        Link link2 = new Link("GitHub", "GitHub home page", "www.github.com");
        linkListMap.add(link);
        linkListMap.add(link2);
        return linkListMap;
    }


    public static void main(String[] args) {
        launch(args);
    }
}
