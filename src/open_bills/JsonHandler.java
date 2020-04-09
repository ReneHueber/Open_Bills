package open_bills;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class JsonHandler {
    private String jsonFilePath;

    public JsonHandler(String file_path){
        this.jsonFilePath = file_path;
    }

    /** get's the information out of the json file
     * returns an Observable Array List of Bill Items for the table view**/
    protected ObservableList<BillItem> readJsonFile(String keyValue){
        ObservableList<BillItem> openBillObjects = FXCollections.observableArrayList();

        JSONParser parser = new JSONParser();
        try{
            Object object = parser.parse(new FileReader(jsonFilePath));
            JSONObject jsonObject = (JSONObject) object;

            // get's all the outgoing or incoming open bills
            JSONArray allOpenBills = (JSONArray) jsonObject.get(keyValue);

            // goes throw the open bills
            for (Object openBill : allOpenBills){
                JSONObject billValue = (JSONObject) openBill;

                // get's all the values
                String companyName = (String) billValue.get("company_name");
                String month = (String) billValue.get("month");
                String year = (String) billValue.get("year");
                String fileName = (String) billValue.get("file_name");
                String filePath = (String) billValue.get("file_path");

                // add's the object to the list
                openBillObjects.add(new BillItem(companyName, month, year, fileName, filePath));
            }

        } catch (Exception e){
            e.printStackTrace();
        }

        return openBillObjects;
    }

    protected void writeJsonFile(){

    }
}
