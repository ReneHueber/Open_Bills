package open_bills;

public class BillItem {
    private String companyName, month, year, fileName, filePath, date;

    public BillItem(){
    }

    public BillItem(String companyName, String month, String year, String fileName, String filePath) {
        this.companyName = companyName;
        this.month = month;
        this.year = year;
        this.fileName = fileName;
        this.filePath = filePath;
        this.date = String.format("%s/%s", year, month);
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
}
