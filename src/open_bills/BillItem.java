package open_bills;

public class BillItem {
    private String companyName, month, year, fileName, filePath, date, description, dayOfIssue;
    private boolean outgoingBill;

    public BillItem(){
    }

    public BillItem(String companyName, String month, String year, String fileName, String filePath, String description, String dayOfIssue) {
        this.companyName = companyName;
        this.month = month;
        this.year = year;
        this.fileName = fileName;
        this.filePath = filePath;
        this.description = description;
        this.dayOfIssue = dayOfIssue;
        this.date = createDate(this.year, this.month, this.dayOfIssue);
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(String year) {
        this.year = year;
    }

    /** getter for the Table View **/
    public String getCompanyName() {
        return companyName;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getDate(){
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getDayOfIssue() {
        return dayOfIssue;
    }

    public boolean getOutgoingBill(){
        return outgoingBill;
    }

    private String createDate(String year, String month, String dayOfIssue){
        String date = "";
        int year_number = Integer.parseInt(year);

        if (year_number == 0){
            date = String.format("%s/%s/%s", dayOfIssue.substring(0,4), dayOfIssue.substring(4, 6),
                                            dayOfIssue.substring(6, 8));
            outgoingBill = false;
        }
        else{
            date = String.format("%s/%s", year, month);
            outgoingBill = true;
        }

        return date;
    }
}
