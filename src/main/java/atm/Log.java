package atm;

import banking.Message;
import banking.Money;
import banking.Status;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

public class Log {
    private static final String LOG_FILE_PATH = "logfile.txt";

    public Log() {
    }

    public void logSend(Message message) {
        String logMessage = "[LOG] Sending: " + message;
        System.out.println(logMessage);
        writeToFile(logMessage);
    }
    
    public void writeLog(String string) {
    	writeToFile(string);
    }
    

    public void logResponse(Status response) {
        String logMessage = "[LOG] Response: " + response;
        System.out.println(logMessage);
        writeToFile(logMessage);
    }

    public void logCashDispensed(Money amount) {
        String logMessage = "[LOG] Cash dispensed: " + amount;
        System.out.println(logMessage);
        writeToFile(logMessage);
    }

    public String getLastLog(int index) {
        LinkedList<String> logs = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                logs.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "[ERROR] Unable to read log file.";
        }

        if (index <= 0 || index > logs.size()) {
            return "[ERROR] Invalid log index.";
        }

        return logs.get(logs.size() - index);
    }
    
    public void logEnvelopeAccepted() {
        String logMessage = "[LOG] Envelope accepted.";
        System.out.println(logMessage);
        writeToFile(logMessage);
    }

    private void writeToFile(String message) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.write(message);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
