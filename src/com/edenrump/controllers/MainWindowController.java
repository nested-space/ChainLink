package com.edenrump.controllers;

import com.edenrump.ChainLink;
import com.edenrump.comms.Clipper;
import com.edenrump.comms.Launcher;
import com.edenrump.comms.Mailer;
import com.edenrump.config.Defaults;
import com.edenrump.models.Link;
import com.edenrump.ui.Ribbon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.DataFormat;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.edenrump.config.Defaults.*;

/**
 * Class represents the controller of the main application window
 */
public class MainWindowController {

    public BorderPane contentBase;
    private ObservableList<Link> links;
    public TableView<Link> linkTableView;
    public TableColumn<Link, String> descriptionColumn;
    public TableColumn<Link, String> nameColumn;
    public TableColumn<Link, String> locationColumn;
    public TextField filterField = new TextField();
    Ribbon mRibbon = new Ribbon();

    /**
     * Method to add links to the list.
     * @param linkList a list of links to display
     */
    public void setUp(List<Link> linkList) {
        addLinks(linkList);
        establishRibbon();
    }

    private void addLinks(List<Link> linkList){
        this.links = FXCollections.observableArrayList(linkList);
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().urlProperty());
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Link> filteredData = new FilteredList<>(links, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(dataPredicate -> (newValue.isEmpty() ||
                    dataPredicate.getDescription().toLowerCase().contains(newValue.toLowerCase()) ||
                    dataPredicate.getName().toLowerCase().contains(newValue.toLowerCase()) ||
                    dataPredicate.getUrl().toLowerCase().contains(newValue.toLowerCase())));
        });

        linkTableView.setItems(filteredData);
    }

    private void establishRibbon() {
        contentBase.setTop(mRibbon);

        mRibbon.addModule(USE_MODULE_NAME, false);
        Button copyButton = new Button("", createImageView("/img/clipboard.png"));
        copyButton.setOnAction(actionEvent -> {
            if(linkTableView.getSelectionModel().isEmpty()) return;
            Clipper.pushToClipboard(DataFormat.PLAIN_TEXT, linkTableView.getSelectionModel().getSelectedItem().getUrl());
        });
        mRibbon.addControlToModule(USE_MODULE_NAME, copyButton);

        Button openButton = new Button("", createImageView("/img/browser.png"));
        openButton.setOnAction(actionEvent -> {
            if(linkTableView.getSelectionModel().isEmpty()) return;
            Launcher.handleOpenHyperlink(linkTableView.getSelectionModel().getSelectedItem().getUrl());
        });
        mRibbon.addControlToModule(USE_MODULE_NAME, openButton);

        Button emailButton = new Button("", createImageView("/img/email.png"));
        emailButton.setOnAction(actionEvent -> {
            if(linkTableView.getSelectionModel().isEmpty()) return;
            try {
                Mailer.mailto(null, "Here's the link to " + linkTableView.getSelectionModel().getSelectedItem().getName() + "!", linkTableView.getSelectionModel().getSelectedItem().getUrl());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
        mRibbon.addControlToModule(USE_MODULE_NAME, emailButton);

        mRibbon.addModule(ABOUT_MODULE_NAME, true);
        Button aboutButton = new Button("", createImageView("/img/link.png"));
        aboutButton.setOnAction(actionEvent -> launchWindow(
                APP_NAME + " - About",
                "/fxml/About.fxml",
                "/img/link.png",
                aboutButton
        ));
        mRibbon.addControlToModule(ABOUT_MODULE_NAME, aboutButton);

        mRibbon.addModule(SEARCH_MODULE_NAME, false);
        filterField.setPrefWidth(150);
        mRibbon.addControlToModule(SEARCH_MODULE_NAME, filterField);

    }

    private ImageView createImageView(String imageloc){
        return new ImageView(new Image(getClass().getResourceAsStream(imageloc), 24, 24, true, true));
    }

    /**
     * Utility method provides clean way to launch a window where the response of user interaction doesn't need to be tracked
     * The window will be launched as a child of the current window
     *
     * @param title         the title of the window
     * @param fxmlDoc       the location of the fxml document to be loaded as a resource
     * @param windowIconLoc the location of the icon image
     * @param source        the control launching the window
     */
    private void launchWindow(String title, String fxmlDoc, String windowIconLoc, Control source) {
        FXMLLoader fxmlLoader;
        Parent root;
        fxmlLoader = new FXMLLoader(getClass().getResource(fxmlDoc));
        try {
            root = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.initOwner(source.getScene().getWindow());
            stage.getIcons().add(new Image(ChainLink.class.getResourceAsStream(windowIconLoc)));
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
