package atm;

import banking.Card;
import banking.Message;
import banking.Money;
import banking.Status;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

public class Log {
    private static final String LOG_FILE_PATH = "src/main/logfile.txt";
    private ATM atm;

    public Log(ATM atm) {
        this.atm = atm;
    }

    public void logSend(Message message) {
        String logMessage = "[LOG] Sending: " + message;
        atm.getDisplay().showMessage(logMessage);
        writeToFile(logMessage);
    }
    
    public void writeLog(String string) {
    	writeToFile(string);
    }
    

    public void logResponse(Status response) {
        String logMessage = "[LOG] Response: " + response;
        atm.getDisplay().showMessage(logMessage);
        writeToFile(logMessage);
    }

    public void logCashDispensed(Money amount) {
        String logMessage = "[LOG] Cash dispensed: " + amount;
        atm.getDisplay().showMessage(logMessage);
        writeToFile(logMessage);
    }
    public void logCashAdded(Money amount) {
        String logMessage = "[LOG] Cash added: " + amount;
        atm.getDisplay().showMessage(logMessage);
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
        atm.getDisplay().showMessage(logMessage);
        writeToFile(logMessage);
    }
    
    public void logCardInfo(Card card) {
        if (card == null) {
            writeToFile("[LOG] Attempted to log a null card.");
            return;
        }
        
        String logMessage = String.format("[LOG] Card Info - Serial Number: %s, Bank Code: %s, Expiry Date: %s",
                card.getNumber(), card.getBankNumber(), card.getExpiryDate());
        
        atm.getDisplay().showMessage(logMessage);
        writeToFile(logMessage);
    }

    private void writeToFile(String message) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String timestamp = LocalDateTime.now().format(formatter);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE_PATH, true))) {
            writer.write(  message +"[" + timestamp + "] ");
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
