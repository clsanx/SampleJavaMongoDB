/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.gui.controller;

import app.App;
import app.gui.controller.cliente.ClienteMainController;
import app.gui.controller.factura.FacturaMainController;
import app.gui.controller.servicio.ServicioMainController;
import app.logic.imp.ClienteManagerImp;
import app.logic.imp.FacturaManagerImp;
import app.logic.imp.ServicioManagerImp;
import app.logic.interfaces.ClienteManager;
import app.logic.interfaces.FacturaManager;
import app.logic.interfaces.ServicioManager;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author Carlos
 */
public class HomeController implements Initializable {
    private static final Logger logger = Logger.getLogger(HomeController.class.getName());
    private static ServicioManager servicioManager = new ServicioManagerImp();
    private static FacturaManager facturaManager = new FacturaManagerImp();
    private static ClienteManager clienteManager = new ClienteManagerImp();
    private Stage stage;
    
    private Label label;
    @FXML
    private Button btnFacturas;
    @FXML
    private Button btnClientes;
    @FXML
    private Button btnServicios;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    /**
     * Conecta Stage a controlador
     *
     * @param stage stage actual
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Inicializa la stage
     *
     * @param root Elemento Parent del fxml
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.setTitle("ServiTec");
        stage.setResizable(false);
        //stage.getIcons().add(new Image(AppConstants.PATH_LOGO));

        stage.setOnShowing(this::handleWindowShowing);
        stage.setMaxWidth(400);
        stage.setMinWidth(400);
        stage.setMaxHeight(300);
        stage.setMinHeight(300);
        stage.show();
        logger.info("Home abierto.");
    }
    
    /**
     * Handle on window showing
     *
     * @param event
     */
    private void handleWindowShowing(WindowEvent event) {
//        FacturaManagerImp fdao = new FacturaManagerImp();
//        fdao.getFacturas();
        
        
    }
    
    /**
     * Iniciar y mostrar ventana de clientes
     */
    @FXML
    private void loadClientes() {
        try {
            
            FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/view/cliente/clienteMain.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            ClienteMainController ctr = ((ClienteMainController) loader.getController());

            //ctr.setClientesManager(clientesLogicController);

            ctr.setOwnerStage(stage);
            ctr.setStage(new Stage());
            ctr.initStage(root);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error al cargar modulo clientes.", ex);
        }
    }
    
    /**
     * Iniciar y mostrar ventana de servicios
     */
    @FXML
    private void loadServicios() {
        try {
            
            FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/view/servicio/servicioMain.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            ServicioMainController ctr = ((ServicioMainController) loader.getController());
            
            ctr.setOwnerStage(stage);
            ctr.setStage(new Stage());
            ctr.initStage(root);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error al cargar modulo servicios.", ex);
        }
    }
    
    /**
     * Iniciar y mostrar ventana de clientes
     */
    @FXML
    private void loadFacturas() {
        try {
            
            FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/view/factura/facturaMain.fxml"));
            AnchorPane root = (AnchorPane) loader.load();

            FacturaMainController ctr = ((FacturaMainController) loader.getController());

            //ctr.setClientesManager(clientesLogicController);

            ctr.setOwnerStage(stage);
            ctr.setStage(new Stage());
            ctr.initStage(root);
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Error al cargar modulo facturas.", ex);
        }
    }

    /**
     * @return the servicioManager
     */
    public static ServicioManager getServicioManager() {
        return servicioManager;
    }

    /**
     * @return the facturaManager
     */
    public static FacturaManager getFacturaManager() {
        return facturaManager;
    }

    /**
     * @return the clienteManager
     */
    public static ClienteManager getClienteManager() {
        return clienteManager;
    }
    
    
}
