/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controllers;

import Beans.Currency;
import DataAdapters.tblDivisaAdapter;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Miguel
 */
public class SceneCatalogeController implements Initializable {

    tblDivisaAdapter tbldivisa = new tblDivisaAdapter();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Currency> tbDivisas;

    @FXML
    private TableColumn<Currency, Number> colID;

    @FXML
    private TableColumn<Currency, String> colName;

    @FXML
    private TableColumn<Currency, Number> colValue;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtValue;

    private Stage dialogStage;

    /**
     * The data as an observable list of Persons.
     */
    private ObservableList<Currency> currencyData = FXCollections.observableArrayList();

    @FXML
    void initialize() {
        assert tbDivisas != null : "fx:id=\"tbDivisas\" was not injected: check your FXML file 'sceneCataloge.fxml'.";
        assert colID != null : "fx:id=\"colID\" was not injected: check your FXML file 'sceneCataloge.fxml'.";
        assert colName != null : "fx:id=\"colName\" was not injected: check your FXML file 'sceneCataloge.fxml'.";
        assert colValue != null : "fx:id=\"colValue\" was not injected: check your FXML file 'sceneCataloge.fxml'.";
        assert txtName != null : "fx:id=\"txtName\" was not injected: check your FXML file 'sceneCataloge.fxml'.";
        assert txtValue != null : "fx:id=\"txtValue\" was not injected: check your FXML file 'sceneCataloge.fxml'.";

    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        colID.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        colValue.setCellValueFactory(cellData -> cellData.getValue().valueProperty());

        loadTableCurrency();
        //txtName.setText("N/A");
        //txtValue.setText("00.0");

        //clear currency details
        showCurrencyDetails(null);

        //Listen for selection changes and show the currency details when changed.
        tbDivisas.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showCurrencyDetails(newValue));

    }

    /**
     * Returns the data as an observable list of Persons.
     *
     * @return
     */
    public ObservableList<Currency> getCurrencyData() {
        return currencyData;
    }

    /**
     * Sets the stage of this dialog.
     *
     * @param dialogStage
     */
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    private void loadTableCurrency() {

        tbDivisas.getItems().clear();
        tbDivisas.getColumns().clear();
        currencyData.clear();

        currencyData = FXCollections.observableArrayList(tbldivisa.Select());
        tbDivisas.getColumns().addAll(colID);
        tbDivisas.getColumns().addAll(colName);
        tbDivisas.getColumns().addAll(colValue);
        tbDivisas.setItems(currencyData);

    }

    private void showCurrencyDetails(Currency curr) {
        if (curr != null) {
            txtName.setText(curr.getName());
            txtValue.setText(Double.toString(curr.getValue()));
        } else {
            txtName.setText("");
            txtValue.setText("");
        }
    }

    @FXML
    private void handleDeleteCurrency() {
        int selectedIndex = tbDivisas.getSelectionModel().getSelectedIndex();
        int selectedID = 0;

        if (selectedIndex >= 0) {

            if (confirmationDialog("Delete Selected")) {
                selectedID = tbDivisas.getSelectionModel().getSelectedItem().getID();
                tbDivisas.getItems().remove(selectedIndex);
                tbldivisa.Delete(selectedID);
            }

        } else {
            //Nothing selected
            Alert alert = new Alert(AlertType.WARNING);
            alert.initOwner(dialogStage);
            alert.setTitle("No Selection");
            alert.setHeaderText("No Value Selected");
            alert.setContentText("Please select a Currency in the table.");

            alert.showAndWait();
        }
    }

    @FXML
    private void handleSave() {

        if (tbDivisas.getSelectionModel().getSelectedItem() == null) {

            String errorMessage = "Please Create a New Currency";

            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("No Currency to Edit");
            alert.setHeaderText("Please create a new Item");
            alert.setContentText(errorMessage);

            alert.showAndWait();

        } else {
            if (isInputValid()) {

                if (confirmationDialog("Edit Selected")) {
                    int id = tbDivisas.getSelectionModel().getSelectedItem().getID();
                    String name = txtName.getText();
                    Double value = Double.parseDouble(txtValue.getText());

                    if (tbldivisa.Update(id, name, value) == 1) {
                        tbDivisas.getSelectionModel().getSelectedItem().setName(name);
                        tbDivisas.getSelectionModel().getSelectedItem().setValue(value);
                    }
                }
            }
        }

    }

    @FXML
    private void handleNewCurrency() {

        if (isInputValid()) {
            //String name = txtName.getText();
            //Double value = Double.parseDouble(txtValue.getText());
            if (confirmationDialog("Create New")) {
                if (tbldivisa.Insert(txtName.getText(), Double.parseDouble(txtValue.getText())) == 1) {
                    loadTableCurrency();
                }
            }
        }
    }

    /**
     * Validates the user input in the text fields.
     *
     * @return true if the input is valid
     */
    private boolean isInputValid() {
        String errorMessage = "";

        if (txtName.getText() == null || txtName.getText().length() == 0) {
            errorMessage += "No valid currency Name!\n";
        }
        if (txtValue.getText() == null || txtValue.getText().length() == 0) {
            errorMessage += "No valid value!\n";
        } else {
            // try to parse the postal code into an int.
            try {
                Double.parseDouble(txtValue.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid value  (must be a Number)!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
            Alert alert = new Alert(AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }

    public boolean confirmationDialog(String type) {

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(type + " Item");
        alert.setContentText("Are you ok with this?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    //TODO: Fix the function to close the window
    @FXML
    private void handleClose() {
        //window.close();
    }

}
