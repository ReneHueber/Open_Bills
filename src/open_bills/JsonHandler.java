package open_bills;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class JsonHandler {
    private String jsonFilePath;
    private FileWriter file;

    public JsonHandler(String file_path){
        this.jsonFilePath = file_path;
    }

    /** get's the information out of the json file
     * returns an dictionary with all the json values for the table view**/
    protected Map<String, ObservableList<BillItem>> readJsonFile(){
        Map<String, ObservableList<BillItem>> openBillsDict = new HashMap<>();

        JSONParser parser = new JSONParser();
        try{
            Object object = parser.parse(new FileReader(jsonFilePath));
            JSONObject jsonReadObject = (JSONObject) object;
            // get's the keys of the object
            Set<String> keys = jsonReadObject.keySet();

            // goes throw the keys
            for (String key : keys){
                // the json file contains a dictionary with a list of dictionary's
                JSONArray openBillsJsonArray = (JSONArray) jsonReadObject.get(key);
                // list for the elements that are saved in the dictionary
                ObservableList<BillItem> billItemList = FXCollections.observableArrayList();

                // goes throw the list of open bill "dictionary's" in the json file and saves the values
                // in the bill item list
                for (Object openBill : openBillsJsonArray){
                    JSONObject billValue = (JSONObject) openBill;

                    // get's all the values
                    String companyName = (String) billValue.get("company_name");
                    String month = (String) billValue.get("month");
                    String year = (String) billValue.get("year");
                    String fileName = (String) billValue.get("file_name");
                    String filePath = (String) billValue.get("file_path");

                    // add's the object to the list
                    billItemList.add(new BillItem(companyName, month, year, fileName, filePath));
                }
                // put's the list of item's in the dictionary
                openBillsDict.put(key, billItemList);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return openBillsDict;
    }

    /** get's an dictionary an writes all the values in the json file **/
    protected void writeJsonFile(Map<String, ObservableList<BillItem>> openBillsDict){
        JSONObject writeObjectJson = new JSONObject();

        // goes throw all the key in the dictionary
        for (String key : openBillsDict.keySet()){
            // array for the objects that are converted into a dictionary in the json file
            JSONArray billObjectsJsonArray = new JSONArray();
            // list for all the open Bills elements in the passed dictionary
            ObservableList<BillItem> openBills = openBillsDict.get(key);

            // goes throw the bill objects in the list of open bill objects
            for (BillItem openBill : openBills){
                // writes all the values in an json object
                JSONObject billJsonObject = new JSONObject();
                billJsonObject.put("company_name", openBill.getCompanyName());
                billJsonObject.put("month", openBill.getMonth());
                billJsonObject.put("year", openBill.getYear());
                billJsonObject.put("file_name", openBill.getFileName());
                billJsonObject.put("file_path", openBill.getFilePath());

                // add's the json object to the json list
                billObjectsJsonArray.add(billJsonObject);
            }
            // add's the list of json objects to the json write object
            writeObjectJson.put(key, billObjectsJsonArray);
        }
        try{
            file = new FileWriter("/home/ich/Documents/Projekte_Andere/Rechnungen/open_bills.txt");
            file.write(writeObjectJson.toJSONString());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                file.flush();
                file.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
