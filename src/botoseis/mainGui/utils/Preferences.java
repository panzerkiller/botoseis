/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package botoseis.mainGui.utils;

import java.io.*;
import static java.lang.System.getenv;
import java.util.HashMap;

/**
 *
 * @author gabriel
 */
public class Preferences {

    private String format;

    public static Preferences getPreferences() {
        Preferences p = new Preferences();
        p.format = "XDR";
        Object botov = getenv("BOTOSEIS_ROOT");
        try {
            File file = new File(botov.toString() + "/.cfg");
            if (file.exists()) {
                try (BufferedReader buff = new BufferedReader(new FileReader(file))) {
                    HashMap<String, String> map = new HashMap();
                    while (buff.ready()) {
                        String linha = buff.readLine();
                        if (linha.contains("=")) {
                            map.put(linha.split("=")[0], linha.split("=")[1]);
                        }
                    }
                    p.setFormat(map.get("format"));
                }
            }
        } catch (Exception e) {
        }
        return p;
    }

    public void writePreferences() {
        try {
            Object botov = getenv("BOTOSEIS_ROOT");
            File file = new File(botov.toString() + "/.cfg");
            try (BufferedWriter buff = new BufferedWriter(new FileWriter(file))) {
                buff.write("format=" + getFormat());
                buff.newLine();
            }
        } catch (Exception e) {
        }
    }

    /**
     * @return the format
     */
    public String getFormat() {
        return format == null ? "" : format;
    }

    /**
     * @param format the format to set
     */
    public void setFormat(String format) {
        this.format = format;
    }
}
