package com.edenrump.controllers;

import com.edenrump.comms.Mailer;
import com.edenrump.models.Link;
import com.edenrump.ui.Ribbon;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Class represents the controller of the main application window
 */
public class MainWindowController {

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
    public void addLinks(List<Link> linkList) {
        this.links = FXCollections.observableArrayList(linkList);
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        locationColumn.setCellValueFactory(cellData -> cellData.getValue().urlProperty());
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<Link> filteredData = new FilteredList<>(links, p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
                    filteredData.setPredicate(dataPredicate -> {
                        return (newValue.isEmpty() ||
                                dataPredicate.getDescription().toLowerCase().contains(newValue.toLowerCase()) ||
                                dataPredicate.getName().toLowerCase().contains(newValue.toLowerCase()) ||
                                dataPredicate.getUrl().toLowerCase().contains(newValue.toLowerCase()));
                    });
                });

        linkTableView.setItems(links);
    }

    private void establishRibbon() {
        String module_name = "Bob";
        mRibbon.addModule(module_name, false);
        Button copyButton = new Button("", new ImageView("/img/analytics.png"));
        copyButton.setOnAction(actionEvent -> {
        });
        mRibbon.addControlToModule(module_name, copyButton);

        mRibbon.addModule(module_name, false);
        Button openButton = new Button("", new ImageView("/img/browser.png"));
        openButton.setOnAction(actionEvent -> {
        });
        mRibbon.addControlToModule(module_name, openButton);

        mRibbon.addModule(module_name, false);
        Button emailButton = new Button("", new ImageView("/img/browser.png"));
        emailButton.setOnAction(actionEvent -> {
            if(linkTableView.getSelectionModel().isEmpty()) return;
            try {
                Mailer.mailto(null, "Here's The Link!", linkTableView.getSelectionModel().getSelectedItem().getUrl());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });
        mRibbon.addControlToModule(module_name, emailButton);
    }
}
