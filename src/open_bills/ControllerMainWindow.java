package open_bills;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ControllerMainWindow {
    // Gui Elements
    @FXML Label headingLb;
    @FXML Button payedBtn;
    @FXML MenuItem incomingBillsMI, outgoingBillsMI;
    @FXML MenuItem searchOpenBillsMI, searchSequelNumberMI;

    @FXML TableView<BillItem> openBillsTable;
    @FXML TableColumn<BillItem, String> companyTC, dateTC, fileNameTC;

    // Global Variables
    /** changes this variables if the path has been changed **/
    private String jsonFilePath = "/home/ich/Documents/Projekte_Andere/Rechnungen/open_bills.txt";
    private String movePath = "/home/ich/Desktop/Move_Folder";
    private Map<String, ObservableList<BillItem>> openBills = new HashMap<>();
    private boolean selectedIncomingBillsScene = true;

    // objects
    JsonHandler jsonHandler;

    /** passes the gui elements to the class that handles the gui actions
     * constructor call in the initialize function because before the gui elements are Null**/
    public void initialize(){
        jsonHandler = new JsonHandler(jsonFilePath);

        fillDictionary();
        setupTableView();
    }
    // Button functions

    /** handles incomingBills Menu Item clicked
     * changes the heading and the table view items **/
    public void incomingBillsClicked(){
        setScene("Offene Rechnungen an Mich", true);
        selectedIncomingBillsScene = true;
    }

    /** handles incomingBills Menu Item clicked
     * changes the heading and the table view items **/
    public void outgoingBillsClicked(){
        setScene("Offene Rechnungen von Mir", false);
        selectedIncomingBillsScene = false;
    }

    /** deletes the selected item, updates the list, rewrites the json file,
     * moves and renames the bill pdf file**/
    public void payedBtnClicked(){
        BillItem selectedBill = openBillsTable.getSelectionModel().getSelectedItem();
        // clears the selected item
        openBillsTable.getSelectionModel().clearSelection();
        // deletes the selected item for the open bills list
        if (selectedIncomingBillsScene){
            ObservableList<BillItem> incomingBills = openBills.get("bills_incoming");
            incomingBills.remove(selectedBill);
        }
        else{
            ObservableList<BillItem> incomingBills = openBills.get("bills_outgoing");
            incomingBills.remove(selectedBill);
        }
        jsonHandler.writeJsonFile(openBills);
        renameFile(selectedBill);
    }


    // Gui functions

    /** changes the heading and the table view item **/
    protected void setScene(String headingText, boolean navigateIncomingBillScene){
        headingLb.setText(headingText);
        // values are only updated if the selected and the navigation scene are different
        // because otherwise the selected values are lost if the same scene is selected
        if (navigateIncomingBillScene && !selectedIncomingBillsScene)
            setTableValues(openBills.get("bills_incoming"));
        else if (!navigateIncomingBillScene && selectedIncomingBillsScene)
            setTableValues(openBills.get("bills_outgoing"));
    }

    /** set's the values for the columns, to display the values correctly **/
    private void setColumnsOpenBillsTable(){
        companyTC.setCellValueFactory(new PropertyValueFactory<BillItem, String>("companyName"));
        dateTC.setCellValueFactory(new PropertyValueFactory<BillItem, String>("date"));
        fileNameTC.setCellValueFactory(new PropertyValueFactory<BillItem, String>("fileName"));
    }

    /** set's the values, sorts by date ascending and resets the selected labels
     * so the oldest bill is on top **/
    private void setTableValues(ObservableList<BillItem> list){
        openBillsTable.setItems(list);
        // sorting
        dateTC.setSortType(TableColumn.SortType.ASCENDING);
        openBillsTable.getSortOrder().setAll(dateTC);
    }

    /** set's the columns and the values at the start **/
    private void setupTableView(){
        setColumnsOpenBillsTable();
        setTableValues(openBills.get("bills_incoming"));
    }

    // program functions

    /** fills the dictionary with the current values **/
    private void fillDictionary(){
        openBills = jsonHandler.readJsonFile();
    }

    private void renameFile(BillItem selectedBill){
        String newFileName = selectedBill.getFileName().replace("_o", "");
        Path oldPath = Paths.get(selectedBill.getFilePath());
        Path newPath = Paths.get(movePath, newFileName);

        try {
            Files.move(oldPath, newPath);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}

