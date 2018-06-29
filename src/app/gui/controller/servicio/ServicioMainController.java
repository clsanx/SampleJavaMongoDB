/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.gui.controller.servicio;

import app.App;
import app.logic.pojo.ServicioBean;
import app.logic.interfaces.ServicioManager;
import app.logic.imp.ServicioManagerImp;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
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
public class ServicioMainController implements Initializable {

    private static final Logger logger = Logger.getLogger(ServicioMainController.class.getName());
    private ServicioManager servicioManager = new ServicioManagerImp();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private Stage stage;
    private Stage ownerStage;
    private ObservableList<ServicioBean> serviciosData;
    
    @FXML
    private TableColumn<ServicioBean, Integer> tcId;
    @FXML
    private TableColumn<ServicioBean, String> tcName;
    @FXML
    private TableView<ServicioBean> tvServicios;
    @FXML
    private TableColumn<ServicioBean, String> tcDesc;
    @FXML
    private TableColumn<ServicioBean, String> tcPrice;
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

        stage.setTitle("ServiTec - Servicios");
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
        btnUpdate.setOnAction(e -> loadCrearMod(tvServicios.getSelectionModel().getSelectedItem()));
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
        // Obtener Collection de Servicios
        serviciosData = FXCollections.observableArrayList(servicioManager.getServicios());

        // Placeholder en caso de no tener datos
        tvServicios.setPlaceholder(new Label("Sin registros."));

        // TableCellFactory
        tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));


        // Añadir datos iniciales a la tabla
        tvServicios.setItems(serviciosData);
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
        tvServicios.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    cm.show(tvServicios, e.getScreenX(), e.getScreenY());
                }
            }
        });
        //ContextMenu Modificar
        cmItem2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                loadCrearMod(tvServicios.getSelectionModel().getSelectedItem());
            }
        });
        cm.getItems().add(cmItem2);
        tvServicios.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    cm.show(tvServicios, e.getScreenX(), e.getScreenY());
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
        alert.setContentText("¿Desea eliminar el servicio?");
        Optional<ButtonType> result = alert.showAndWait();
        ServicioBean servicio = tvServicios.getSelectionModel().getSelectedItem();

        if (result.get() == ButtonType.OK && servicio != null) {

            if(servicioManager.deleteServicio(servicio)){
                logger.info("Servicio ID:"+servicio.getId()+" eliminado.");
                tvServicios.getItems().remove(servicio);
                tvServicios.refresh();
            }else{
                logger.info("No se ha podido eliminar el servicio.");
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
    private void loadCrearMod(ServicioBean servicio) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/view/servicio/servicioCu.fxml"));

            AnchorPane root = (AnchorPane) loader.load();
            ServicioCuController ctr = ((ServicioCuController) loader.getController());
            ctr.setStage(new Stage());
            ctr.setServicioManager(servicioManager);
            ctr.setServicioMainController(this);

            // En caso de opción Modificar
            if (servicio != null) {
                ctr.setServicio(servicio);
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
        tvServicios.setRowFactory(tv -> {
            TableRow<ServicioBean> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    try {

                        loadCrearMod(tvServicios.getSelectionModel().getSelectedItem());
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
    public void actionCu(ServicioBean cli) {
        if(cli.getId()>0){ // update
            servicioManager.updateServicio(cli);
        }else{ // new
            servicioManager.insertServicio(cli);
        }
        reloadTable();
    }
    
    public void reloadTable(){
        serviciosData = FXCollections.observableArrayList(servicioManager.getServicios());
        tvServicios.getItems().setAll(serviciosData);
    }
}
