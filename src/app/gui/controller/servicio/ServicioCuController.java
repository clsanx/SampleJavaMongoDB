/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.gui.controller.servicio;

import app.gui.controller.cliente.*;
import app.logic.pojo.ClienteBean;
import app.logic.pojo.ServicioBean;
import app.logic.interfaces.ClienteManager;
import app.logic.interfaces.ServicioManager;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Carlos
 */
public class ServicioCuController implements Initializable {

    private static final Logger logger = Logger.getLogger(ClienteMainController.class.getName());
    private Stage stage;
    private Stage ownerStage;
    private ServicioBean servicio;
    private ServicioManager servicioManager;
    private ServicioMainController servicioMainController;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @FXML
    private Button btnOk;
    @FXML
    private Button btnCancel;
    @FXML
    private Label lblTitle;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfDesc;
    @FXML
    private TextField tfPrice;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    /**
     * Inicializa la stage
     *
     * @param root Elemento Parent del fxml
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);

        //stage.getIcons().add(new Image(AppConstants.PATH_LOGO));
        stage.setResizable(false);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(ownerStage);
        stage.setOnShowing(this::handleWindowShowing);
        stage.setMaxWidth(300);
        stage.setMinWidth(300);
        stage.setMaxHeight(420);
        stage.setMinHeight(420);
        stage.show();

    }

    /**
     * Handle on window showing
     *
     * @param event
     */
    private void handleWindowShowing(WindowEvent event) {
        lblTitle.setText("");
        if (servicio != null) {
            logger.info("Abierta ventana modificar servicio.");
            
            // Set titulo de ventana y texto de boton crear/modificar.
            stage.setTitle("ServiTec - Modificar servicio");
            // Asignar valores de objeto seleccionado a los campos
            tfName.setText(servicio.getName());
            tfDesc.setText(servicio.getDescription());
            tfPrice.setText(String.valueOf(servicio.getPrice()));

        } else {
            logger.info("Abierta ventana crear servicio.");
            stage.setTitle("ServiTec - Crear servicio");
            lblTitle.setText("");
        }

    }

    /**
     * Establece stage
     *
     * @param stage stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOwnerStage(Stage ownerStage) {
        this.ownerStage = ownerStage;
    }

    public void setServicio(ServicioBean servicio) {
        this.servicio = servicio;
    }

    public void setServicioManager(ServicioManager servicioManager) {
        this.servicioManager = servicioManager;
    }

    public void setServicioMainController(ServicioMainController servicioMainController) {
        this.servicioMainController = servicioMainController;
    }

    

    @FXML
    public void actionOk() {

        if (servicio == null) {
            ServicioBean newServicio = new ServicioBean();
            
            newServicio.setId(0);
            newServicio.setName(tfName.getText());
            newServicio.setDescription(tfDesc.getText());
            newServicio.setPrice(Double.parseDouble(tfPrice.getText()));
            
            servicioMainController.actionCu(newServicio);
        }else{
            servicio.setName(tfName.getText());
            servicio.setDescription(tfDesc.getText());
            servicio.setPrice(Double.parseDouble(tfPrice.getText()));
            servicioMainController.actionCu(servicio);
        }

        stage.close();
        ownerStage.requestFocus();
    }

    @FXML
    public void actionCancel() {
        stage.close();
        ownerStage.requestFocus();
    }

}
