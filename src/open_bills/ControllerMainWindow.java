package open_bills;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControllerMainWindow {
    @FXML Label headingLb, file_nameLb, dateLb;
    @FXML Button payedBtn;
    @FXML MenuItem incomingBillsMI, outgoingBillsMI;
    @FXML MenuItem searchOpenBillsMI, searchSequelNumberMI;

    @FXML TableView<BillItem> openBillsTable;
    @FXML TableColumn<BillItem, String> companyTC, dateTC, fileNameTC;

    private String jsonFilePath = "/home/ich/Documents/Projekte_Andere/Rechnungen/open_bills.txt";

    GuiMainWindow guiMainWindow;
    JsonHandler jsonHandler;

    /** passes the gui elements to the class that handles the gui actions
     * constructor call in the initialize function because before the gui elements are Null**/
    public void initialize(){
        guiMainWindow = new GuiMainWindow(headingLb, file_nameLb, dateLb,
                                        payedBtn, incomingBillsMI, outgoingBillsMI,
                                        searchOpenBillsMI, searchSequelNumberMI);
        jsonHandler = new JsonHandler(jsonFilePath);

        setupTableView();
    }
    // Button functions

    /** handles incomingBills Menu Item clicked
     * changes the heading and the table view items **/
    public void incomingBillsClicked(){
        setScene("Offene Rechnungen an Mich", false);
        setTableValues(jsonHandler.readJsonFile("bills_incoming"));
        resetSelectBillLabels();
    }

    /** handles incomingBills Menu Item clicked
     * changes the heading and the table view items **/
    public void outgoingBillsClicked(){
        setScene("Offene Rechnungen von Mir", true);
        setTableValues(jsonHandler.readJsonFile("bills_outgoing"));
        resetSelectBillLabels();
    }

    /** get's the clicked table view item **/
    public void tableViewItemClicked(){
        BillItem currentBill = openBillsTable.getSelectionModel().getSelectedItem();
        file_nameLb.setText(currentBill.getFileName());
        dateLb.setText(currentBill.getDate());
    }


    // Gui handle functions

    /** changes the heading and the table view item **/
    protected void setScene(String headingText, boolean outgoing){
        headingLb.setText(headingText);
        // TODO change Tableview list
    }

    /** set's the values for the columns, to display the values correctly **/
    private void setColumnsOpenBillsTable(){
        companyTC.setCellValueFactory(new PropertyValueFactory<BillItem, String>("companyName"));
        dateTC.setCellValueFactory(new PropertyValueFactory<BillItem, String>("date"));
        fileNameTC.setCellValueFactory(new PropertyValueFactory<BillItem, String>("fileName"));
    }

    /** set's the values and sorts by date ascending
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
        setTableValues(jsonHandler.readJsonFile("bills_incoming"));
    }

    /** resets the selected labels if table content is changed **/
    private void resetSelectBillLabels(){
        file_nameLb.setText("");
        dateLb.setText("");
    }
}

