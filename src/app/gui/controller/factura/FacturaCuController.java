/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package app.gui.controller.factura;

import app.gui.controller.HomeController;
import app.logic.imp.ServicioManagerImp;
import app.logic.interfaces.ClienteManager;
import app.logic.interfaces.FacturaManager;
import app.logic.interfaces.ServicioManager;
import app.logic.pojo.ClienteBean;
import app.logic.pojo.FacturaBean;
import app.logic.pojo.ServicioBean;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author Carlos
 */
public class FacturaCuController implements Initializable {

    private static final Logger logger = Logger.getLogger(FacturaCuController.class.getName());
    private Stage stage;
    private Stage ownerStage;
    private FacturaBean factura;
    private FacturaManager facturaManager;
    private ServicioManager servicioManager;
    private ClienteManager clienteManager;
    private FacturaMainController facturaMainController;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private Double total = new Double(0);

    @FXML
    private ComboBox<ClienteBean> cbCliente;
    @FXML
    private DatePicker dpDate;
    @FXML
    private TableView<ServicioBean> tvServicios;
    @FXML
    private TableColumn<ServicioBean, Integer> tcId;
    @FXML
    private TableColumn<ServicioBean, String> tcName;
    @FXML
    private TableColumn<ServicioBean, String> tcDesc;
    @FXML
    private TableColumn<ServicioBean, Double> tcPrice;
    @FXML
    private Button btnDelete;
    @FXML
    private Button btnAdd;
    @FXML
    private ComboBox<ServicioBean> cbServicio;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Label lblTotal;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        servicioManager = HomeController.getServicioManager();
        clienteManager = HomeController.getClienteManager();
    }

    /**
     * Inicializa la stage
     *
     * @param root Elemento Parent del fxml
     */
    public void initStage(Parent root) {
        Scene scene = new Scene(root);
        stage.setScene(scene);

        if (factura != null) {
            stage.setTitle("ServiTec - Modificar factura");
        } else {
            stage.setTitle("ServiTec - Nueva factura");
        }

        //stage.getIcons().add(new Image(AppConstants.PATH_LOGO));
        stage.setResizable(false);

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(ownerStage);
        stage.setOnShowing(this::handleWindowShowing);
        stage.setMaxWidth(500);
        stage.setMinWidth(500);
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
        
        initTableAndCb();
        
        List<ClienteBean> clientes = (List) clienteManager.getClientes();

        if (factura != null) {
            cbCliente.getItems().clear();
            cbCliente.getItems().add(factura.getCliente());
            cbCliente.getSelectionModel().selectFirst();
            cbCliente.setDisable(true);

            String fdate = dateFormat.format(factura.getDate());
            dpDate.setValue(LocalDate.parse(fdate, dateFormatter));
            dpDate.setDisable(true);

            if (factura.getServicios() != null) {
                tvServicios.setItems(FXCollections.observableArrayList(factura.getServicios()));
            } else {
                tvServicios.setItems(FXCollections.observableArrayList(new ArrayList<ServicioBean>()));
            }
            
            tvServicios.getItems().forEach(s -> total+=s.getPrice());
            lblTotal.setText(String.valueOf(total));
        } else {
            lblTotal.setText(String.valueOf(total));
            factura = new FacturaBean();
            cbCliente.setItems(FXCollections.observableArrayList(clientes));
            cbCliente.setDisable(false);

            dpDate.setValue(LocalDate.now());
            dpDate.setDisable(false);

            tvServicios.setItems(FXCollections.observableArrayList(new ArrayList<ServicioBean>()));

        }

        cbServicio.setItems(FXCollections.observableArrayList(servicioManager.getServicios()));
        cbServicio.getSelectionModel().selectFirst();
        
        btnAdd.setOnAction(e -> addService(cbServicio.getValue()));
        btnDelete.setOnAction(e -> removeService(tvServicios.getSelectionModel().getSelectedItem()));
        btnSave.setOnAction(e -> save());
        btnCancel.setOnAction(e -> cancel());

    }

    public void initTableAndCb() {
        tcId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tcName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tcDesc.setCellValueFactory(new PropertyValueFactory<>("description"));
        tcPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
          
    }

    public void addService(ServicioBean servicio) {
        if (servicio != null && 
                !tvServicios.getItems().contains(servicio)) {
            tvServicios.getItems().add(servicio);
            
            total+=servicio.getPrice();
            lblTotal.setText(String.valueOf(total));
            
        }
    }

    public void removeService(ServicioBean servicio) {
        if (servicio != null) {
            tvServicios.getItems().remove(servicio);
            total-=servicio.getPrice();
            lblTotal.setText(String.valueOf(total));
        }
    }
    
    public void save(){
        factura.setDate(Date.from(dpDate.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        factura.setCliente(cbCliente.getSelectionModel().getSelectedItem());
        factura.setServicios(tvServicios.getItems());
        factura.setTotal(total);
        
        if(isValid(factura)){
            facturaMainController.actionCu(factura);
            stage.close();
            ownerStage.requestFocus();
        }
    }
    
    public void cancel(){
        stage.close();
        ownerStage.requestFocus();
    }
    
    public boolean isValid(FacturaBean factura){
        boolean res = true;
        
        if(factura.getServicios()==null || factura.getServicios().isEmpty()){
            res=false;
            logger.info("factura invalida, servicios."+factura.getServicios().toString());
        }
        if(factura.getCliente().getId()<1 || factura.getCliente()==null){
            res=false;
            logger.info("factura invalida, idcliente."+factura.getCliente());
            logger.info("cbcliente."+cbCliente.getValue().getId());
        }
        if(factura.getDate()==null){
            res=false;
            logger.info("factura invalida, date."+factura.getDate());
        }
        if(factura.getTotal()<0 || factura.getTotal()==null){
            res=false;
            logger.info("factura invalida, total."+factura.getTotal());
        }
        
        return res;
    }

    /**
     * @return the stage
     */
    public Stage getStage() {
        return stage;
    }

    /**
     * @param stage the stage to set
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * @return the ownerStage
     */
    public Stage getOwnerStage() {
        return ownerStage;
    }

    /**
     * @param ownerStage the ownerStage to set
     */
    public void setOwnerStage(Stage ownerStage) {
        this.ownerStage = ownerStage;
    }

    /**
     * @return the factura
     */
    public FacturaBean getFactura() {
        return factura;
    }

    /**
     * @param factura the factura to set
     */
    public void setFactura(FacturaBean factura) {
        this.factura = factura;
    }

    /**
     * @return the facturaManager
     */
    public FacturaManager getFacturaManager() {
        return facturaManager;
    }

    /**
     * @param facturaManager the facturaManager to set
     */
    public void setFacturaManager(FacturaManager facturaManager) {
        this.facturaManager = facturaManager;
    }

    /**
     * @return the facturaMainController
     */
    public FacturaMainController getFacturaMainController() {
        return facturaMainController;
    }

    /**
     * @param facturaMainController the facturaMainController to set
     */
    public void setFacturaMainController(FacturaMainController facturaMainController) {
        this.facturaMainController = facturaMainController;
    }

}
