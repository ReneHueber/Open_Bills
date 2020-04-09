package open_bills;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;

public class GuiMainWindow {
    Label headingLb, file_nameLb, dateLb;
    Button payedBtn;
    MenuItem incomingBillsMI, outgoingBillsMI;
    MenuItem searchOpenBillsMI, searchSequelNumberMI;

    public GuiMainWindow(Label headingLb, Label file_nameLb, Label dateLb,
                         Button payedBtn,
                         MenuItem incomingBillsMI, MenuItem outgoingBillsMI,
                         MenuItem searchOpenBillsMI, MenuItem searchSequelNumberMI){
        this.headingLb = headingLb;
        this.file_nameLb = file_nameLb;
        this.dateLb = dateLb;
        this.payedBtn = payedBtn;
        this.incomingBillsMI = incomingBillsMI;
        this.outgoingBillsMI = outgoingBillsMI;
        this.searchOpenBillsMI = searchOpenBillsMI;
        this.searchSequelNumberMI = searchSequelNumberMI;
    }


}
