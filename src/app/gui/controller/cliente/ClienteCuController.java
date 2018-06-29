/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.gui.controller.cliente;

import app.logic.pojo.ClienteBean;
import app.logic.interfaces.ClienteManager;
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
public class ClienteCuController implements Initializable {

    private static final Logger logger = Logger.getLogger(ClienteMainController.class.getName());
    private Stage stage;
    private Stage ownerStage;
    private ClienteBean cliente;
    private ClienteManager clienteManager;
    private ClienteMainController clienteMainController;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfLastName;
    @FXML
    private TextField tfPhone;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfZipCode;
    @FXML
    private Button btnOk;
    @FXML
    private Button btnCancel;
    @FXML
    private Label lblTitle;

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

        lblTitle.setText("");


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
        if (cliente != null) {
            logger.info("Abierta ventana modificar cliente.");

            // Set titulo de ventana y texto de boton crear/modificar.
            stage.setTitle("ServiTec - Modificar cliente");

            // Asignar valores de objeto seleccionado a los campos
            tfId.setText(String.valueOf(cliente.getId()));
            tfId.setEditable(false);
            tfName.setText(cliente.getName());
            tfLastName.setText(cliente.getLastname());
            tfPhone.setText(cliente.getPhone());
            tfEmail.setText(cliente.getEmail());
            tfZipCode.setText(cliente.getZipcode());

        } else {
            logger.info("Abierta ventana crear factura.");
            stage.setTitle("ServiTec - Crear cliente");
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

    public void setCliente(ClienteBean cliente) {
        this.cliente = cliente;
    }

    public void setClienteManager(ClienteManager clienteManager) {
        this.clienteManager = clienteManager;
    }

    public void setClienteMainController(ClienteMainController clienteMainController) {
        this.clienteMainController = clienteMainController;
    }

    public void actionOk() {

        if (cliente == null) {
            ClienteBean newCliente = new ClienteBean();
            newCliente.setId(0);
            newCliente.setName(tfName.getText());
            newCliente.setLastname(tfLastName.getText());
            newCliente.setPhone(tfPhone.getText());
            newCliente.setEmail(tfEmail.getText());
            newCliente.setZipcode(tfZipCode.getText());
            clienteMainController.actionCu(newCliente);
        }else{
            cliente.setName(tfName.getText());
            cliente.setLastname(tfLastName.getText());
            cliente.setPhone(tfPhone.getText());
            cliente.setEmail(tfEmail.getText());
            cliente.setZipcode(tfZipCode.getText());
            clienteMainController.actionCu(cliente);
        }

        
        stage.close();
        ownerStage.requestFocus();
    }

    public void actionCancel() {
        stage.close();
        ownerStage.requestFocus();
    }

}
