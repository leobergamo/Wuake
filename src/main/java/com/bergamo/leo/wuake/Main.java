package com.bergamo.leo.wuake;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import static com.bergamo.leo.wuake.SundryUtils.*;
import static com.bergamo.leo.wuake.RegistryUtils.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    private static final ActionListener moMainWindowMonitor = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent evt) {

            final User32 oUser32 = User32.INSTANCE;
            WinDef.HWND oMainFrameHandle = new WinDef.HWND(Native.getComponentPointer(moMainFrame));
            if(oUser32.IsWindow(oMainFrameHandle)){

                setStringRegVal(
                        "MainFrameHandle",
                        oMainFrameHandle.toString()
                );

                embedCommandPrompt(moMainFrame);

                moTimer.stop();
            }

        }
    };

    public static JFrame moMainFrame;

    private static final Timer moTimer = new Timer(500, moMainWindowMonitor);

    public static void main(String[] args) {
        // Press Alt+Enter with your caret at the highlighted text to see how
        // IntelliJ IDEA suggests fixing it.
        log("Initializing, please wait...");
        log("Args: " + Arrays.toString(args));

            try {
                //if parent key doesn't exist in registry, create it...
                if (!Advapi32Util.registryKeyExists(
                        WinReg.HKEY_CURRENT_USER,
                        "Software\\LCB\\WuakeJava"
                ))
                    Advapi32Util.registryCreateKey(
                            WinReg.HKEY_CURRENT_USER,
                            "Software\\LCB\\WuakeJava"
                    );

                if (!isRegValSet("Hotkey"))
                    setStringRegVal(
                            "Hotkey",
                            "shift PLUS"
                    );

                if (!isRegValSet("CommandPromptWindowTitle"))
                    setStringRegVal(
                            "CommandPromptWindowTitle",
                            "C:\\Windows\\System32\\cmd.exe"
                    );

                if (!isRegValSet("CommandPromptWindowClassName"))
                    setStringRegVal(
                            "CommandPromptWindowClassName",
                            "CASCADIA_HOSTING_WINDOW_CLASS"
                    );


                if (!isRegValSet("CommandPromptFileSpec"))
                    setStringRegVal(
                            "CommandPromptFileSpec",
                            "C:\\Windows\\System32\\cmd.exe"
                    );

                moMainFrame = new MainFrame();
                moMainFrame.setVisible(true);

                moTimer.start();
            } catch (Exception objExc) {
                log("Error: exception has occurred!");
                log(objExc.getMessage());
                log(Arrays.toString(objExc.getStackTrace()));
                System.exit(1);
            }
    }
}