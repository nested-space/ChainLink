/*------------------------------------------------------------------------------
 - Copyright (c) 08/12/2019, 19:45.2019. EmpowerLCSimConverter by Edward Eden-Rump is licensed under a Creative Commons Attribution 4.0 International License.
 -
 - Based on a work at https://github.com/nested-space/fxLCInfoConverter.To view a copy of this license, visit http://creativecommons.org/licenses/by/4.0/.
 -----------------------------------------------------------------------------*/

package com.edenrump.ui;

import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Class that represents the Ribbon display. Generally used at the top of a user interface.
 */
public class Ribbon extends VBox {

    /**
     * The map of module names to modules
     */
    private Map<String, RibbonModule> moduleMap = new LinkedHashMap<>();

    /**
     * The map of module positions (i.e. left or right) to modules
     *
     * Modules on the left are left-aligned
     * Modules on the right are right-aligned
     */
    private Map<RibbonModule, Boolean> positionMap = new LinkedHashMap<>();

    /**
     * The container that holds left-aligned modules
     */
    private HBox moduleHolder = new HBox();

    /**
     * The container that holds right-aligned modules
     */
    private HBox moduleEndHolder = new HBox();

    /**
     * Constructor, creates a new Ribbon instance
     */
    public Ribbon() {
        //set overall style
        setId("ribbonAndSearchBarVBox");
        getStylesheets().add("css/RibbonAndMenu.css");

        //set holder style
        moduleHolder.setId("ribbonModuleHolderHBox");
        moduleHolder.getStylesheets().add("css/RibbonAndMenu.css");
        moduleEndHolder.setId("ribbonModuleHolderHBox");
        moduleEndHolder.getStylesheets().add("css/RibbonAndMenu.css");
        HBox.setHgrow(moduleEndHolder, Priority.ALWAYS);
        moduleEndHolder.setAlignment(Pos.CENTER_RIGHT);
        HBox allModulesHolder = new HBox(moduleHolder, moduleEndHolder);
        this.getChildren().addAll(allModulesHolder);
    }

    /**
     * Method to disable the buttons in the stated module
     * @param moduleName the name of the module in which to disable the buttons
     */
    public void disableModuleButtons(String moduleName) {
        if (moduleMap.containsKey(moduleName)) {
            moduleMap.get(moduleName).disableControls();
        } else if (moduleName.toLowerCase().trim().equals("search")) {
            throw new IllegalArgumentException("Search is keyword. Cannot be module name");
        } else {
            throw new IllegalArgumentException("Module not found");
        }
    }

    /**
     * Method to enable the buttons in the stated module
     * @param moduleName the name of the module in which to enable the buttons
     */
    public void enableModuleButtons(String moduleName) {
        if (moduleMap.containsKey(moduleName)) {
            moduleMap.get(moduleName).enableControls();
        } else if (moduleName.toLowerCase().trim().equals("search")) {
            throw new IllegalArgumentException("Search is keyword. Cannot be module name");
        } else {
            throw new IllegalArgumentException("Module not found");
        }
    }

    /**
     * Method to add a module to the ribbon
     * @param moduleName the name of the module to add
     * @param floatingRight whether the module should float to the right of the ribbon
     */
    public void addModule(String moduleName, boolean floatingRight) {
        if (moduleMap.containsKey(moduleName)) {
            System.out.println("Warning: module already exists");
        } else if (moduleName.toLowerCase().trim().equals("search")) {
            throw new IllegalArgumentException("Search is keyword. Cannot be module name");
        } else {
            //if module doesn't exist, add it
            RibbonModule ribbonModule = new RibbonModule();
            ribbonModule.setName(moduleName);
            moduleMap.put(moduleName, ribbonModule);
            positionMap.put(ribbonModule, floatingRight);
        }
    }

    /**
     * Method to remove a module from the ribbon
     * @param moduleName the name of the module to remove from the ribbon
     */
    public void removeModule(String moduleName) {
        if (moduleName.toLowerCase().trim().equals("search")) {
            throw new IllegalArgumentException("Search is keyword. Cannot be module name");
        } else if (moduleMap.containsKey(moduleName)) {
            //remove module
            moduleHolder.getChildren().remove(moduleMap.get(moduleName));
            moduleEndHolder.getChildren().remove(moduleMap.get(moduleName));
            moduleMap.remove(moduleName);
        }
    }

    /**
     * Method to add a control to a stated module in the ribbon
     * @param moduleName the name of the module to which to add the control
     * @param control the control to be added
     */
    public void addControlToModule(String moduleName, Control control) {
        //if module doesn't exist, throw exception
        if (!moduleMap.containsKey(moduleName)) {
            throw new IllegalArgumentException("Module not found");
        } else {
            moduleMap.get(moduleName).add(control);
        }

        refresh();
    }

    /**
     * Method to clear the containers and reload all modules into module containers in the ribbon.
     */
    public void refresh() {
        //initially clear
        moduleHolder.getChildren().clear();
        moduleEndHolder.getChildren().clear();

        //rebuild children from Map
        for (String string : moduleMap.keySet()) {
            RibbonModule r = moduleMap.get(string);
            if(positionMap.get(r)){
                moduleEndHolder.getChildren().add(r);
            } else {
                moduleHolder.getChildren().add(r);
            }
        }
    }

    /**
     * Method to remove all buttons from Ribbon
     */
    public void clear() {
        moduleMap = new LinkedHashMap<>();
        positionMap = new LinkedHashMap<>();
        refresh();
    }
}
