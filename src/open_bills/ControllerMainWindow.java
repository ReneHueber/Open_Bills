package open_bills;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

public class ControllerMainWindow {
    @FXML Label headingLb, file_nameLb, dateLb;
    @FXML Button payedBtn;
    @FXML MenuItem incomingBillsMI, outgoingBillsMI;
    @FXML MenuItem searchOpenBillsMI, searchSequelNumberMI;

    GuiMainWindow guiMainWindow;

    /** passes the gui elements to the class that handles the gui actions
     * constructor call in the initialize function because before the gui elements are Null**/
    public void initialize(){
        guiMainWindow = new GuiMainWindow(headingLb, file_nameLb, dateLb,
                                        payedBtn, incomingBillsMI, outgoingBillsMI,
                                        searchOpenBillsMI, searchSequelNumberMI);
    }

    public void incomingBillsClicked(){

    }

    public void outgoingBillsClicked(){

    }
}
