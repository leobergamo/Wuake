/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bergamo.leo.wuake;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

/**
 *
 * @author berga
 */
public class RegistryUtils {

    public static void setStringRegVal(String sName, String sValue) {
        // set registry string value...
        Advapi32Util.registrySetStringValue(
                WinReg.HKEY_CURRENT_USER,
                "Software\\LCB\\WuakeJava",
                sName,
                sValue
        );
    }

    public static String getStringRegVal(String sName) {
        // get registry string value...
        if (!Advapi32Util.registryGetStringValue(
                WinReg.HKEY_CURRENT_USER,
                "Software\\LCB\\WuakeJava",
                sName
        ).isEmpty()) {
            return Advapi32Util.registryGetStringValue(
                    WinReg.HKEY_CURRENT_USER,
                    "Software\\LCB\\WuakeJava",
                    sName
            );

        } else {
            return null;
        }
    }

    public static boolean isRegValSet(String sName) {
        // check if registry value exist and set...
        if (Advapi32Util.registryValueExists(
                WinReg.HKEY_CURRENT_USER,
                "Software\\LCB\\WuakeJava",
                sName
        )) {
            return !Advapi32Util.registryGetStringValue(
                    WinReg.HKEY_CURRENT_USER,
                    "Software\\LCB\\WuakeJava",
                    sName
            ).isEmpty();
        } else {
            return false;
        }
    }

    public static void setArbStringRegVal(String sName, WinReg.HKEY oRootKey, String sPath, String sValue) {
        // set registry string value...
        Advapi32Util.registrySetStringValue(
                oRootKey,
                sPath,
                sName,
                sValue
        );
    }

    public static String getArbStringRegVal(String sName, WinReg.HKEY oRootKey, String sPath) {
        // get registry string value...
        if (!Advapi32Util.registryGetStringValue(
                oRootKey,
                sPath,
                sName
        ).isEmpty()) {
            return Advapi32Util.registryGetStringValue(
                    oRootKey,
                    sPath,
                    sName
            );

        } else {
            return null;
        }
    }

    public static boolean isArbRegValSet(String sName, WinReg.HKEY oRootKey, String sPath) {
        // check if registry value exist and set...
        if (Advapi32Util.registryValueExists(
                oRootKey,
                sPath,
                sName
        )) {
            return !Advapi32Util.registryGetStringValue(
                    oRootKey,
                    sPath,
                    sName
            ).isEmpty();
        } else {
            return false;
        }
    }

}
