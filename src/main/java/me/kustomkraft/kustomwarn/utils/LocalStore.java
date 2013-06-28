package me.kustomkraft.kustomwarn.utils;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

import me.kustomkraft.kustomwarn.commands.KWarn;
import org.bukkit.Bukkit;

public class LocalStore{

    private File storageFile;
    private ArrayList<String> values;
    private Logger logger = Bukkit.getLogger();
    private KWarn warnCommand;

    public LocalStore(File file){

        this.storageFile = file;
        this.values = new ArrayList();

        if (!this.storageFile.exists())
            try {
                storageFile.createNewFile();

            } catch (IOException e) {
                logger.severe(e.getMessage());
            }
    }

    public void load(){
        try {
            DataInputStream input = new DataInputStream(new FileInputStream(this.storageFile));
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;
            while ((line = reader.readLine()) != null) {
                if (this.warnCommand.warningReason != null){
                    addReason(line, line, line, line);
                }else {
                    add(line, line, line);
                }
            }

            reader.close();
            input.close();
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    public void save() {
        try {
            FileWriter stream = new FileWriter(this.storageFile);
            BufferedWriter out = new BufferedWriter(stream);
            for (String value : values) {
                out.write(value + " ");
                out.newLine();
            }

            out.close();
            stream.close();
        } catch (IOException e) {
            logger.severe(e.getMessage());
        }
    }

    public int getWarnings(String offendingPlayer) {
        int warnings = 0;
        try {
            Scanner scanner = new Scanner(this.storageFile);
            int count = 0;
            while (scanner.hasNext()) {
                String nextString = scanner.next();
                if (nextString.equalsIgnoreCase(offendingPlayer)) {
                    count++;
                    warnings += count;
                }
                count = 0;
            }
        } catch (Exception e) {
            this.logger.severe(e.getMessage());
        }
        return warnings;
    }

    public boolean contains(String value) {
        return values.contains(value);
    }

    public void add(String offendingPlayer, String admin, String date) {
        values.add(offendingPlayer + " was warned by " + admin + " Date(HH:MM DD/MM)" + date);
    }

    public void addReason(String offendingPlayer, String admin, String reason, String date) {
        values.add(offendingPlayer + " was warned by " + admin + " for " + reason + " on " + date);
    }

    public void remove(String value) {
        values.remove(value);
    }

    public void removeReason(String value, String reason) {
        values.remove(value + reason);
    }

    public ArrayList<String> getValues() {
        return values;
    }
}