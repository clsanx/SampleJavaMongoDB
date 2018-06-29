/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.gui.controller.cliente;

import app.App;
import app.logic.pojo.ClienteBean;
import app.logic.interfaces.ClienteManager;
import app.logic.imp.ClienteManagerImp;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import static java.time.LocalDate.now;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Carlos
 */
public class ClienteMainController implements Initializable {

    private static final Logger logger = Logger.getLogger(ClienteMainController.class.getName());
    private ClienteManager clienteManager = new ClienteManagerImp();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private Stage stage;
    private Stage ownerStage;
    private ObservableList<ClienteBean> clientesData;
    //private ClientesManager clientesLogicController;
    @FXML
    private TableView<ClienteBean> tvClientes;
    @FXML
    private TableColumn<ClienteBean, Integer> tcId;
    @FXML
    private TableColumn<ClienteBean, String> tcName;
    @FXML
    private TableColumn<ClienteBean, String> tcLastName;
    @FXML
    private TableColumn<ClienteBean, String> tcPhone;
    @FXML
    private TableColumn<ClienteBean, String> tcEmail;
    @FXML
    private TableColumn<ClienteBean, String> tcZipCode;
    @FXML
    private Button btnAdd;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnDelete;

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

        stage.setTitle("ServiTec - Clientes");
        //stage.getIcons().add(new Image(AppConstants.PATH_LOGO));
        stage.setResizable(false);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(ownerStage);
        stage.setOnShowing(this::handleWindowShowing);
        stage.setMaxWidth(700);
        stage.setMinWidth(700);
        stage.setMaxHeight(600);
        stage.setMinHeight(600);
        stage.show();

    }

    private void handleWindowShowing(WindowEvent event) {
        initTable();
        InitRowDoubleClickEvent();
        initContextMenu();
        
        // CRUD
        btnAdd.setOnAction(e -> loadCrearMod(null));
        btnUpdate.setOnAction(e -> loadCrearMod(tvClientes.getSelectionModel().getSelectedItem()));
    
    }

    /**
     * Establece owner stage
     *
     * @param ownerStage
     */
    public void setOwnerStage(Stage ownerStage) {
        this.ownerStage = ownerStage;
    }

    /**
     * Conecta Stage a controlador
     *
     * @param stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initTable() {
        // Obtener Collection de Clientes
        clientesData = FXCollections.observableArrayList(clienteManager.getClientes());

        // Placeholder en caso de no tener datos
        tvClientes.setPlaceholder(new Label("Sin registros."));

        // TableCellFactory
        tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcLastName.setCellValueFactory(new PropertyValueFactory<>("lastname"));
        tcPhone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tcEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tcZipCode.setCellValueFactory(new PropertyValueFactory<>("zipcode"));

        // Añadir datos iniciales a la tabla
        tvClientes.setItems(clientesData);
    }

    /**
     * Inicializa menu contextual
     */
    public void initContextMenu() {
        final ContextMenu cm = new ContextMenu();
        MenuItem cmItem1 = new MenuItem("Eliminar");
        MenuItem cmItem2 = new MenuItem("Modificar");

        //ContextMenu Eliminar
        cmItem1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                actionDelete();
            }
        });
        cm.getItems().add(cmItem1);
        tvClientes.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    cm.show(tvClientes, e.getScreenX(), e.getScreenY());
                }
            }
        });
        //ContextMenu Modificar
        cmItem2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                loadCrearMod(tvClientes.getSelectionModel().getSelectedItem());
            }
        });
        cm.getItems().add(cmItem2);
        tvClientes.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    cm.show(tvClientes, e.getScreenX(), e.getScreenY());
                }
            }
        });

    }

    /**
     * Acción borrar factura
     */
    @FXML
    private void actionDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("¿Desea eliminar el cliente?");
        Optional<ButtonType> result = alert.showAndWait();
        ClienteBean cliente = tvClientes.getSelectionModel().getSelectedItem();

        if (result.get() == ButtonType.OK && cliente != null) {

            if(clienteManager.deleteCliente(cliente)){
                logger.info("Cliente ID:"+cliente.getId()+" eliminado.");
                tvClientes.getItems().remove(cliente);
                tvClientes.refresh();
            }else{
                logger.info("No se ha podido eliminar el cliente.");
            }

        }
    }
    
    /**
     * Carga ventana Crear/Modificar factura. Si pasamos null se abre una
     * ventana para nueva factura. Si le pasamos la factura seleccionada se abre
     * una venatana para modificar.
     *
     * @param factura factura seleccionada en la tabla. Para nueva factura
     * utiizar null.
     */
    private void loadCrearMod(ClienteBean cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/view/cliente/clienteCu.fxml"));

            AnchorPane root = (AnchorPane) loader.load();
            ClienteCuController ctr = ((ClienteCuController) loader.getController());
            ctr.setStage(new Stage());
            ctr.setClienteManager(clienteManager);
            ctr.setClienteMainController(this);

            // En caso de opción Modificar
            if (cliente != null) {
                ctr.setCliente(cliente);
            }

            ctr.setOwnerStage(stage);
            ctr.initStage(root);
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "Error al cargar ventana nueva_factura.fxml.", ex);
        }
    }
    
    /**
     * Accion al hacer doble click sobre row de la tabla
     */
    public void InitRowDoubleClickEvent() {
        tvClientes.setRowFactory(tv -> {
            TableRow<ClienteBean> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    try {

                        loadCrearMod(tvClientes.getSelectionModel().getSelectedItem());
                    } catch (Exception ex) {
                        logger.info("Error al cargar ventana modificar factura. (Double click event)");
                    }

                }
            });
            return row;
        });
    }
    
    /**
     * Accion crear/modificar factura
     * @param factura If null entonces crear factura
     */
    public void actionCu(ClienteBean cli) {
        if(cli.getId()>0){ // update
            clienteManager.updateCliente(cli);
        }else{ // new
            clienteManager.insertCliente(cli);
        }
        reloadTable();
    }
    
    public void reloadTable(){
        clientesData = FXCollections.observableArrayList(clienteManager.getClientes());
        tvClientes.getItems().setAll(clientesData);
    }
    
}
