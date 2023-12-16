package com.bergamo.leo.wuake;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author berga
 */
public class FileUtils {

    public static List loadTextFile(String sFilePath) {
        Scanner oScanner;
        List<String> oText = new ArrayList<>();
        try {
            oScanner = new Scanner(new File(sFilePath));
            while (oScanner.hasNextLine()) {
                String sLine = oScanner.nextLine();
                if(!sLine.isEmpty()) oText.add(sLine);
            }
            return oText;
        } catch (FileNotFoundException oExc) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, oExc);
            return null;
        }
    }

    public static boolean appendTextToFile(String sFilePath, String sText) {
        File oFile = new File(sFilePath);
        FileWriter oFileWriter = null;
        try {
            // Below constructor argument decides whether to append or override
            oFileWriter = new FileWriter(oFile, true);
            oFileWriter.write(sText + System.lineSeparator());
        } catch (IOException oExc) {
            Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, oExc);
            return false;
        } finally {
            try {
                oFileWriter.close();
                return true;
            } catch (IOException oExc) {
                Logger.getLogger(FileUtils.class.getName()).log(Level.SEVERE, null, oExc);
                return false;
            }
        }
    }
}
