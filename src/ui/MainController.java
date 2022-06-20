package ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import model.Product;
import model.Shop;

public class MainController {
	
	
	//Main components
	
	@FXML
    private Button bottonMakeSale;

    @FXML
    private Button buttonAddNewProduct;

    @FXML
    private Button buttonAddOldProduct;

    @FXML
    private Button buttonInventory;

	@FXML
    private AnchorPane newScreen;

    @FXML
    private Font x3;

    @FXML
    private Color x4;
    
    
    //Inventory components
    
    @FXML
    private TableColumn<Product, String> columnCode;

    @FXML
    private TableColumn<Product, String> columnName;

    @FXML
    private TableColumn<Product, Integer> columnQuantity;

    @FXML
    private TableColumn<Product, Integer> columnSales;

    @FXML
    private TableView<Product> tableInventory;
    
    //AddNewProduct components
    
    @FXML
    private TextField txtName;

    @FXML
    private TextField txtPricePurchase;

    @FXML
    private TextField txtPriceSale;

    @FXML
    private TextField txtQuantity;

    @FXML
    private TextField txtReferences;
    
    private Shop shop;
    
    private Product product;
    
    private List<String> references;
    
    public MainController() {
    	shop = new Shop();
    	references = new ArrayList<>();
	}
    
    //Add numeric text formatting to text fields
    private void initNumberFormatTextField() {   
    	UnaryOperator<Change> filter = change -> {
    	    String text = change.getText();

    	    if (text.matches("[0-9]*")) {
    	        return change;
    	    }
    	    
    	    return null;
    	};

    	
    	TextFormatter<String> formatter1 = new TextFormatter<>(filter);
    	TextFormatter<String> formatter2 = new TextFormatter<>(filter);
    	TextFormatter<String> formatter3 = new TextFormatter<>(filter);
    	
    	txtPricePurchase.setTextFormatter(formatter1);
    	txtPriceSale.setTextFormatter(formatter2);
    	txtQuantity.setTextFormatter(formatter3);
    }
    
    @FXML
    public void makeSale(ActionEvent event) {
    	
    }

    @FXML
    public void openAddNewProduct(ActionEvent event) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddProductScreen.fxml"));
		
		fxmlLoader.setController(this);
		
		Parent root = fxmlLoader.load();
		
		newScreen.getChildren().clear();
		newScreen.getChildren().add(root);
		
		buttonInventory.setStyle("");
		buttonAddNewProduct.setStyle("-fx-background-color: #00BFFF");
		
		initNumberFormatTextField();
    }
    
    private boolean addNewProduct() {
    	
    		try {
    			
    			if(!txtName.getText().isEmpty() && !txtPricePurchase.getText().isEmpty() && !txtPriceSale.getText().isEmpty()
    					&& !txtQuantity.getText().isEmpty() && !txtQuantity.getText().isEmpty()) {
    				
    				int incrementalCode = shop.getProducts().size() + 1;

    				shop.addProduct(txtName.getText(), references,Double.parseDouble(txtPricePurchase.getText()), 
    						Double.parseDouble(txtPriceSale.getText()),incrementalCode,Integer.parseInt(txtQuantity.getText()));
    			}
    			
    			return true;
    		} catch (Exception e) {
    			
    			return false;
    		}
    }
    
    @FXML
    public void addNewProduct(ActionEvent event) {
    	
    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
       
    	
    	if(addNewProduct()) {
    		
    		txtName.clear();
    		txtPricePurchase.clear();
    		txtPriceSale.clear();
    		txtQuantity.clear();
    		txtReferences.clear();
    		
    		alert.setTitle("Info");
	        alert.setContentText("Se agreg� el producto.");
	        alert.showAndWait();
    	} else {
    		alert.setTitle("Info");
	        alert.setContentText("No se agreg� el producto.");
	        alert.showAndWait();
    	}
    }
    
    @FXML
    public void priceChangePurchase(KeyEvent event) {
    	product = new Product();
    	
    	if(!txtPricePurchase.getText().isEmpty() || event.getCode() == KeyCode.DELETE) {
    		txtPriceSale.setText(String.valueOf((int)product.generatePriceSale(Double.parseDouble(txtPricePurchase.getText()))));
    	}
    }

    @FXML
    public void putValuePurchase(KeyEvent event) {
    	
    }

    @FXML
    public void openAddOldProduct(ActionEvent event) {
    	
    }

    @FXML
    public void openInventory(ActionEvent event) throws IOException {
    	FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("InvetoryScreen.fxml"));
		
		fxmlLoader.setController(this);
		
		Parent root = fxmlLoader.load();
		
		newScreen.getChildren().clear();
		newScreen.getChildren().add(root);
		
		buttonAddNewProduct.setStyle("");
		buttonInventory.setStyle("-fx-background-color: #00BFFF");
		
		loadInventory();
    }
    
    private void loadInventory() {
    	
    	ObservableList<Product> data = FXCollections.observableArrayList(shop.getProducts());
    	 
    	columnCode.setCellValueFactory(new PropertyValueFactory<>("code"));
    	columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    	columnQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    	columnSales.setCellValueFactory(new PropertyValueFactory<>("sales"));
    	
        tableInventory.setItems(data);
    }
    
    @FXML
    public void addReference(ActionEvent event) {
    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
    	if(!txtReferences.getText().isEmpty()) {
    		references.add(txtReferences.getText());
    		txtReferences.clear();
    		alert.setTitle("Info");
	        alert.setContentText("Referencia agregada.");
	        alert.showAndWait();
    	} else {
    		alert.setTitle("Info");
	        alert.setContentText("Digita una referencia.");
	        alert.showAndWait();
    	}
    }

}
