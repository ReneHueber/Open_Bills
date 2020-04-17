package open_bills;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ControllerMainWindow {
    // Gui Elements
    @FXML Label headingLb;
    @FXML MenuItem incomingBillsMI, outgoingBillsMI;
    @FXML MenuItem searchOpenBillsMI, searchSequelNumberMI;
    @FXML DatePicker payDateDp;

    @FXML TableView<BillItem> openBillsTable;
    @FXML TableColumn<BillItem, String> companyTC, dateTC, fileNameTC;

    // Global Variables
    /** changes this variables if the path has been changed **/
    private String jsonFilePath = "/home/ich/Dokumente/Projekte_Andere/Rechnungen/open_bills.txt";
    private String movePath = "/home/ich/Schreibtisch/Move_Folder";
    private String payedFolder = "Bezahlt";
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
        payDateDp.setValue(LocalDate.now());
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
        // only updates the list and renames the file if it exists
        if (selectedBill != null){
            if (selectedBill.getYear().equals("0000")){
                LocalDate payDate = payDateDp.getValue();
                selectedBill.setMonth(Integer.toString(payDate.getMonthValue()));
                selectedBill.setYear(Integer.toString(payDate.getYear()));
            }
            if (Files.exists(Paths.get(selectedBill.getFilePath(), selectedBill.getFileName()))){
                deleteOpenBillEntrance();
                renameFile(selectedBill);
            }
            else {
                WriteLogs.writeLog(String.format("The Bill \"%s\" doesn't exist any more!", selectedBill.getFileName()));
                WriteLogs.writeLog(String.format("It has been here \"%s\"\n", Paths.get(selectedBill.getFilePath())));
            }
        }
    }

    /** deletes an entrance that has been removed manually **/
    public void deleteBtnClicked(){
        deleteOpenBillEntrance();
    }

    /** reads the json file again, is something changed while the program is open**/
    public void reloadValuesBtnClicked(){
        openBills = jsonHandler.readJsonFile();
        if (selectedIncomingBillsScene)
            setTableValues(openBills.get("bills_incoming"));
        else
            setTableValues(openBills.get("bills_outgoing"));
    }


    // Gui functions

    /** changes the heading and the table view item **/
    protected void setScene(String headingText, boolean navigateIncomingBillScene){
        headingLb.setText(headingText);
        // values are only updated if the selected and the navigation scene are different
        // because otherwise the selected values are lost if the same scene is selected
        if (navigateIncomingBillScene && !selectedIncomingBillsScene){
            setTableValues(openBills.get("bills_incoming"));
            payDateDp.setDisable(false);
        }
        else if (!navigateIncomingBillScene && selectedIncomingBillsScene){
            setTableValues(openBills.get("bills_outgoing"));
            payDateDp.setDisable(true);
        }

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

    private void deleteOpenBillEntrance(){
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
    }


    /** renames and moves the file
     *  deletes the open bill folder if it is empty **/
    private void renameFile(BillItem selectedBill){
        String newFileName = "";
        Path destinationFolderPath = Paths.get("");
        Path locationFolderPath = Paths.get("");


        if (Files.exists(Paths.get(selectedBill.getFilePath()))){
            // gets the file name
            String oldFileName = selectedBill.getFileName();
            // outgoing bill
            if (selectedBill.getOutgoingBill()){
                newFileName = selectedBill.getFileName().replace("_o", "");
                // the path for the open bill folder
                locationFolderPath = Paths.get(selectedBill.getFilePath());
                String parentFolderPath = locationFolderPath.getParent().toString();
                destinationFolderPath = Paths.get(parentFolderPath, payedFolder);

                // checks if the payed folder is not existing and creates it
                if (!Files.exists(destinationFolderPath)){
                    File createDir = new File(destinationFolderPath.toString());
                    createDir.mkdir();
                }
            }
            // incoming bill
            else{
                String fileType = selectedBill.getFileName().split("\\.")[1];
                newFileName = String.format("~%s-%s-%s~%s-%s.%s", selectedBill.getMonth(), selectedBill.getYear(), selectedBill.getCompanyName(),
                        selectedBill.getDescription(), selectedBill.getDayOfIssue(), fileType);
                locationFolderPath = Paths.get(selectedBill.getFilePath());
                destinationFolderPath = Paths.get(movePath);
            }


            // creates the paths to rename the file
            Path newFilePath = Paths.get(destinationFolderPath.toString(), newFileName);
            Path oldFilePath = Paths.get(locationFolderPath.toString(), oldFileName);
            try {
                Files.move(oldFilePath, newFilePath);
            } catch (IOException e){
                e.printStackTrace();
            }

            // checks if the open bills folder is empty and in this case deletes the folder
            File openBillFolder = new File(locationFolderPath.toString());
            File[] filesList = openBillFolder.listFiles();
            if (filesList != null && filesList.length == 0) {
                try {
                    Files.delete(locationFolderPath);
                    WriteLogs.writeLog(String.format("\tDeleted \"%s\" because it was empty", locationFolderPath.toString().split("Rechnungen/")[1]));
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
            WriteLogs.writeLog(String.format("\tBill \"%s\" was payed and renamed to \"%s\"!", oldFileName, newFileName));
            // TODO write log entrance
            // WriteLogs.writeLog(String.format("Moved \"%s\" to \"%s\"\n", newFileName, newFilePath.toString().split("Rechnungen/")[1]));
        }
    }
}

