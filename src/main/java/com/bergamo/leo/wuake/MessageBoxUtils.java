package com.bergamo.leo.wuake;

import javax.swing.JOptionPane;

/**
 *
 * @author berga
 */
public class MessageBoxUtils {

    public static void showInfoBox(String sMessage, String sTitle)
    {
        JOptionPane.showMessageDialog(null, sMessage, sTitle, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showErrorBox(String sMessage, String sTitle)
    {
        JOptionPane.showMessageDialog(null, sMessage, sTitle, JOptionPane.ERROR_MESSAGE);
    }

    public static Boolean showConfirmationBox(String sMessage, String sTitle)
    {
        int iDialogType = JOptionPane.YES_NO_OPTION;
        int iDialogResult = JOptionPane.showConfirmDialog(null,sMessage,sTitle, iDialogType);

        if(iDialogResult == 0) {
            return true;
        } else {
            return false;
        }
    }
    
}
