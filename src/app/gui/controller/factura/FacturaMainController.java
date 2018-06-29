/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.gui.controller.factura;

import app.App;
import app.gui.controller.cliente.ClienteCuController;
import app.logic.pojo.FacturaBean;
import app.logic.pojo.ServicioBean;
import app.logic.interfaces.FacturaManager;
import app.logic.imp.FacturaManagerImp;
import app.logic.pojo.ClienteBean;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.TableCell;
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
public class FacturaMainController implements Initializable {

    private static final Logger logger = Logger.getLogger(FacturaMainController.class.getName());
    private FacturaManager facturaManager = new FacturaManagerImp();
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private Stage stage;
    private Stage ownerStage;
    private ObservableList<FacturaBean> facturasData;

    @FXML
    private TableView<FacturaBean> tvFacturas;
    @FXML
    private TableColumn<FacturaBean, Integer> tcId;
    @FXML
    private TableColumn<FacturaBean, ClienteBean> tcIdCliente;
    @FXML
    private TableColumn<FacturaBean, List<ServicioBean>> tcServicios;
    @FXML
    private TableColumn<FacturaBean, String> tcDate;
    @FXML
    private TableColumn<FacturaBean, Double> tcTotal;
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
        
    }

    private void actionDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText(null);
        alert.setContentText("¿Desea eliminar la factura?");
        Optional<ButtonType> result = alert.showAndWait();
        FacturaBean factura = tvFacturas.getSelectionModel().getSelectedItem();

        if (result.get() == ButtonType.OK && factura != null) {

            if(facturaManager.deleteFactura(factura)){
                logger.info("Factura ID:"+factura.getId()+" eliminada.");
                tvFacturas.getItems().remove(factura);
                tvFacturas.refresh();
            }else{
                logger.info("No se ha podido eliminar la factura.");
            }

        }
    }

    /**
     * Inicializa la stage
     *
     * @param root Elemento Parent del fxml
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.setTitle("ServiTec - Facturas");
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
        
        btnAdd.setOnAction(e -> loadCrearMod(null));
        btnUpdate.setOnAction(e -> loadCrearMod(tvFacturas.getSelectionModel().getSelectedItem()));
        btnDelete.setOnAction(e -> actionDelete());
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
        facturasData = FXCollections.observableArrayList(facturaManager.getFacturas());

        // TableCellFactory
        tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcIdCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        tcIdCliente.setCellFactory(column -> {
            return new TableCell<FacturaBean, ClienteBean>() {
                @Override
                protected void updateItem(ClienteBean item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item.getName()+" "+item.getLastname());
                    }
                }
            };
        });
        
        tcServicios.setCellValueFactory(new PropertyValueFactory<>("servicios"));
        // Custom rendering of the table cell.
        tcServicios.setCellFactory(column -> {
            return new TableCell<FacturaBean, List<ServicioBean>>() {
                @Override
                protected void updateItem(List<ServicioBean> item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item.toString());
                    }
                }
            };
        });

        tcDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tcTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Placeholder en caso de no tener datos
        tvFacturas.setPlaceholder(new Label("Sin registros."));

        tvFacturas.setItems(facturasData);
    }

    /**
     * Accion al hacer doble click sobre row de la tabla
     */
    public void InitRowDoubleClickEvent() {
        tvFacturas.setRowFactory(tv -> {
            TableRow<FacturaBean> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    try {
                        loadCrearMod(tvFacturas.getSelectionModel().getSelectedItem());
                    } catch (Exception ex) {
                        logger.info("Error al cargar ventana modificar factura. (Double click event)");
                    }

                }
            });
            return row;
        });
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
        tvFacturas.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    cm.show(tvFacturas, e.getScreenX(), e.getScreenY());
                }
            }
        });
        //ContextMenu Modificar
        cmItem2.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                loadCrearMod(tvFacturas.getSelectionModel().getSelectedItem());
            }
        });
        cm.getItems().add(cmItem2);
        tvFacturas.addEventHandler(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                if (e.getButton() == MouseButton.SECONDARY) {
                    cm.show(tvFacturas, e.getScreenX(), e.getScreenY());
                }
            }
        });

    }
    
    /**
     * Carga ventana Crear/Modificar factura. Si pasamos null se abre una
     * ventana para nueva factura. Si le pasamos la factura seleccionada se abre
     * una venatana para modificar.
     *
     * @param factura factura seleccionada en la tabla. Para nueva factura
     * utiizar null.
     */
    private void loadCrearMod(FacturaBean factura) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("gui/view/factura/facturaCu.fxml"));

            AnchorPane root = (AnchorPane) loader.load();
            FacturaCuController ctr = ((FacturaCuController) loader.getController());
            ctr.setStage(new Stage());
            ctr.setFacturaManager(facturaManager);
            ctr.setFacturaMainController(this);

            // En caso de opción Modificar
            if (factura != null) {
                ctr.setFactura(factura);
            }

            ctr.setOwnerStage(stage);
            ctr.initStage(root);
        } catch (IOException ex) {
            ex.printStackTrace();
            logger.log(Level.SEVERE, "Error al cargar ventana facturaCu.fxml.", ex);
        }
    }
    
    /**
     * Accion crear/modificar factura
     * @param factura If null entonces crear factura
     */
    public void actionCu(FacturaBean factura) {
        
        System.out.println(factura.toString());
        
        if(factura.getId()>0){ // update
            facturaManager.updateFactura(factura);
        }else{ // new
            facturaManager.insertFactura(factura);
        }
        reloadTable();
    }
    
    public void reloadTable(){
        //facturasData.setAll(facturaManager.getFacturas());
        facturasData.setAll(FXCollections.observableArrayList(facturaManager.getFacturas()));
//        tvFacturas.getItems().clear();
//        tvFacturas.setItems(facturasData);
        tvFacturas.refresh();
    }
}
