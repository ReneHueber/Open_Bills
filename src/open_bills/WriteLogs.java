package open_bills;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteLogs {
    private static String logFilePath = "/home/ich/Desktop/Log_file.txt";

    /** writes a log to the log file**/
    protected static void writeLog(String text){
        File logFile = new File(logFilePath);
        try {
            FileWriter fr = new FileWriter(logFile, true);
            fr.write(String.format("%s:\t%s\n", getCurrentDate(), text));
            fr.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private static String getCurrentDate(){
        SimpleDateFormat formatter = new SimpleDateFormat(
                "dd/MM/yyyy, HH:mm:ss"
        );
        Date currentTime = new Date();
        return formatter.format(currentTime);
    }
}
