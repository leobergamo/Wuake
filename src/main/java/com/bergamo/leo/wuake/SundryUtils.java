package com.bergamo.leo.wuake;

import static com.bergamo.leo.wuake.GlobalConstants.SCREEN_HEIGHT_UNIT;
import static com.bergamo.leo.wuake.GlobalConstants.SCREEN_WIDTH;
import com.sun.jna.*;
import com.sun.jna.platform.win32.*;

import javax.swing.*;

import static com.bergamo.leo.wuake.RegistryUtils.getStringRegVal;
import static com.bergamo.leo.wuake.RegistryUtils.isRegValSet;
import static com.bergamo.leo.wuake.RegistryUtils.setStringRegVal;
import java.awt.*;

public class SundryUtils {

    private static final Toolkit moToolkit = Toolkit.getDefaultToolkit();

    public static void log(Object oObject) {
        System.out.println(oObject);
    }

    public static int getPerc(int iValue, float fPerc) {
        // return percentage of int as int...

        //attribution:
        // a: https://stackoverflow.com/a/18034962
        // u: https://stackoverflow.com/users/1909094/lake

        return (int) (iValue * (fPerc / 100.0f));
    }

    public static WinDef.HWND string2hwnd(String sHwnd) {
        // convert string version of HWND to HWND...
        WinDef.HWND oHwnd = new WinDef.HWND();
        oHwnd.setPointer(
                new Pointer(Long.decode(sHwnd.substring(7)))
        );
        return oHwnd;
    }

    public static boolean embedCommandPrompt(JFrame oParentFrame) {
        // embed Command Prompt...
        final User32 oUser32 = User32.INSTANCE;
        final Shell32 oShell32 = Shell32.INSTANCE;

        try {

            if (isRegValSet("CommandPromptFileSpec")) { //registry string value is set...

                // set required fields...
                WinDef.HWND oCmdPmtWindowHandle;
                int iTick = 0;
                GlobalFields.oRectangle oCoordinates = new GlobalFields.oRectangle(
                        +5,
                        -45,
                        SCREEN_WIDTH - 10,
                        (SCREEN_HEIGHT_UNIT * 7)
                );
                ShellAPI.SHELLEXECUTEINFO oSei = new ShellAPI.SHELLEXECUTEINFO();
                //oSei.fMask = SEE_MASK_NOCLOSEPROCESS;
                //oSei.lpVerb = "runas";
                //oSei.lpParameters = "/redirect redirect.log "+command;
                //oSei.lpDirectory = pwd.getAbsolutePath();
                oSei.lpFile = getStringRegVal("CommandPromptFileSpec");
                oSei.nShow = WinUser.SW_SHOW;
                WinDef.HWND oMainFrameHandle = string2hwnd(getStringRegVal("MainFrameHandle"));

                disableComponents(oParentFrame);
                // launch Command Prompt...
                if (oShell32.ShellExecuteEx(oSei)) { // Command Prompt launched...

                    // monitor for Command Prompt window, embed it into 
                    // the main window, set its position, change it's style and 
                    // store it's handle in registry...
                    log("Waiting on Command Prompt window to appear...");

                    while (true) {
                        if (iTick < 10 /* 5 seconds */) {
                            oCmdPmtWindowHandle = oUser32.FindWindow(
                                    getStringRegVal("CommandPromptWindowClassname"),
                                    getStringRegVal("CommandPromptWindowTitle")
                            );

                            if (oCmdPmtWindowHandle != null) {
                                log("\tembedding Command Prompt window...");

                                setStringRegVal(
                                        "CommandPromptWindowHandle",
                                        oCmdPmtWindowHandle.toString()
                                );

                                oUser32.SetParent(
                                        oCmdPmtWindowHandle,
                                        oMainFrameHandle
                                );

                                log("\tresizing Command Prompt window...");
                                oUser32.SetWindowPos(
                                        oCmdPmtWindowHandle,
                                        null,
                                        oCoordinates.getLeft(),
                                        oCoordinates.getTop(),
                                        oCoordinates.getRight(),
                                        oCoordinates.getBottom(),
                                        WinUser.SWP_NOOWNERZORDER
                                );

                                log("\tre-styling Command Prompt window...");
                                int iStyle = oUser32.GetWindowLong(
                                        oCmdPmtWindowHandle,
                                        WinUser.GWL_STYLE
                                );

                                //re-style command prompt window...
                                iStyle &= ~(WinUser.WS_CAPTION | WinUser.WS_THICKFRAME | WinUser.WS_MINIMIZEBOX | WinUser.WS_MAXIMIZEBOX | WinUser.WS_SYSMENU);
                                oUser32.SetWindowLong(oCmdPmtWindowHandle, WinUser.GWL_STYLE, iStyle);

                                log("Done...");
                                setStringRegVal("CommandPromptWindowHandle", oCmdPmtWindowHandle.toString());
                                enableComponents(oParentFrame);
                                return true;
                            } else {
                                log("\twaiting...");
                                iTick++;
                                Thread.sleep(500);
                            }
                        } else { // Command Prompt failed to appear before timeout...
                            log("Done...");
                            log("Error: Command Prompt has not appeared within 5 seconds; timeout!");
                            setStringRegVal("CommandPromptWindowHandle", "");
                            enableComponents(oParentFrame);
                            return false;
                        }
                    }
                } else { // Command Prompt failed to launch...
                    log("Error: Command Prompt failed to launch!");
                    enableComponents(oParentFrame);
                    return false;
                }
            } else { // registry value not set...
                log("Error: registry value error!");
                enableComponents(oParentFrame);
                return false;
            }
        } catch (Exception objExc) {
            log("Error: exception has occurred!");
            log(objExc.getMessage());
            enableComponents(oParentFrame);
            return false;
        }

    }

    public static void disableComponents(JFrame oFrame) {
        for (Component component : oFrame.getContentPane().getComponents()) {
            component.setEnabled(false);
        }
    }

    public static void enableComponents(JFrame oFrame) {
        for (Component component : oFrame.getContentPane().getComponents()) {
            component.setEnabled(true);
        }
    }

}
