/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app;

import app.gui.controller.HomeController;
import app.logic.interfaces.ClienteManager;
import app.logic.imp.ClienteManagerImp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Carlos
 */
public class App extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("gui/view/home.fxml"));
        Parent root = (Parent) loader.load();
        
        HomeController ctr = ((HomeController) loader.getController());
        
        ctr.setStage(new Stage());
        ctr.initStage(root);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        launch(args);
    }
    
}
