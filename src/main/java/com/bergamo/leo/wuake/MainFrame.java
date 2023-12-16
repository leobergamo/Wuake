package com.bergamo.leo.wuake;

import com.sun.jna.platform.win32.*;

import static com.bergamo.leo.wuake.MessageBoxUtils.*;
import static com.bergamo.leo.wuake.SundryUtils.*;
import static com.bergamo.leo.wuake.GlobalConstants.*;
import static com.bergamo.leo.wuake.GlobalFields.*;
import static com.bergamo.leo.wuake.RegistryUtils.*;
import static com.bergamo.leo.wuake.KeyboardUtils.*;
import static com.bergamo.leo.wuake.FileUtils.*;

import com.tulskiy.keymaster.common.HotKey;
import com.tulskiy.keymaster.common.HotKeyListener;
import com.tulskiy.keymaster.common.Provider;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.File;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainFrame extends JFrame {

    private final Toolkit moToolkit = Toolkit.getDefaultToolkit();
    private final JLabel moMacroControlsLabel = new JLabel("Macro:");
    private final JLabel moOptionControlsLabel = new JLabel("Option:");
    private final CustomJButtonType1 moCloseButton = new CustomJButtonType1("Close");
    private final CustomJButtonType1 moMacroExecButton = new CustomJButtonType1("Execute");
    private final CustomJButtonType1 moMacroAddButton = new CustomJButtonType1("Add");
    private final CustomJButtonType1 moMacroEditButton = new CustomJButtonType1("Edit");
    private final CustomJButtonType1 moOptionExecButton = new CustomJButtonType1("Execute");
    private final Provider moJkeyMasterProvider = Provider.getCurrentProvider(true);
    private Timer moSysMonTimer;
    private Timer moUiMonTimer;
    private final JComboBox<MacroSelectItem> moMacroSelect = new JComboBox();
    private final JComboBox<OptionSelectItem> moOptionSelect = new JComboBox();

    private List<String> moMacros = new ArrayList<>();
    private boolean mbWindowIsVisible;

    public MainFrame() {

        configureUI();
        configureListeners();
        loadMacros();
        loadPrivateMacros();

        if (isRegValSet("Hotkey")) {
            moJkeyMasterProvider.register(KeyStroke.getKeyStroke(getStringRegVal("Hotkey")), new HotKeyListener() {
                @Override
                public void onHotKey(HotKey hotKey) {
                    log("Hotkey pressed...");
                    if(isVisible()){
                        setVisible(false);
                    } else {
                        setVisible(true);
                    }
                }

            });
            showInfoBox("Wuake is active!  Press '" + getStringRegVal("Hotkey") + "' to toggle window!","Hey!");
        } else {
            showInfoBox("Wuake is active!","Hey!");
            showErrorBox("There was an error registering hotkey!","Ohhh! Snap!");
        }
    }

    private void configureListeners() {

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                configureUI();
            }
        });
        addWindowFocusListener(new WindowAdapter() {

            //To check window gained focus
            @Override
            public void windowGainedFocus(WindowEvent e) {
                //set flag
                mbWindowIsVisible = true;
                log("Window is visible: " + mbWindowIsVisible);
            }

            //To check window lost focus
            @Override
            public void windowLostFocus(WindowEvent e) {
                //set flag
                mbWindowIsVisible = false;
                log("Window is visible: " + mbWindowIsVisible);
            }
        });

        moCloseButton.addActionListener((ActionEvent e) -> {
            if (isRegValSet("CommandPromptWindowHandle")) {
                try {
                    moJkeyMasterProvider.reset();
                    moJkeyMasterProvider.stop();
                    sendKeys("exit", string2hwnd(getStringRegVal("CommandPromptWindowHandle")));
                    Thread.sleep(250);
                    keyDownUp(oVirtKeys.get("VK_ENTER"), string2hwnd(getStringRegVal("CommandPromptWindowHandle")));
                    Thread.sleep(2000);
                    System.exit(0);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                log("Error: registry value error!");
            }
        });

        moMacroExecButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRegValSet("CommandPromptWindowHandle")) {
                    try {
                        MacroSelectItem oMacroSelectItem = (MacroSelectItem) moMacroSelect.getItemAt(moMacroSelect.getSelectedIndex());
                        sendKeys(oMacroSelectItem.getCommand(), string2hwnd(getStringRegVal("CommandPromptWindowHandle")));
                        keyDownUp(oVirtKeys.get("VK_ENTER"), string2hwnd(getStringRegVal("CommandPromptWindowHandle")));
                        Thread.sleep(2000);
                    } catch (InterruptedException oExc) {
                        log("Error: exception occurred!\n");
                        oExc.printStackTrace();
                    }
                }
            }

        });

        moMacroAddButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                setVisible(false);
                String sMacroName = JOptionPane.showInputDialog("Please enter macro name:");
                if (!sMacroName.isEmpty()) {
                    String sMacroCommandLine = JOptionPane.showInputDialog("Please enter macro command line:");
                    if (!sMacroCommandLine.isEmpty()) {
                        appendTextToFile(WORKING_DIRECTORY + "\\macros.csv", sMacroName + "," + sMacroCommandLine);
                        moMacroSelect.removeAllItems();
                        loadMacros();
                    }
                }
            }
        }
        );

        moMacroEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e
            ) {
                final Shell32 oShell32 = Shell32.INSTANCE;
                ShellAPI.SHELLEXECUTEINFO oSei = new ShellAPI.SHELLEXECUTEINFO();
                //oSei.fMask = SEE_MASK_NOCLOSEPROCESS;
                //oSei.lpVerb = "runas";
                oSei.lpParameters = WORKING_DIRECTORY + "\\macros.csv";
                //oSei.lpDirectory = pwd.getAbsolutePath();
                oSei.lpFile = "notepad.exe";
                oSei.nShow = WinUser.SW_SHOW;

                setVisible(false);
                // launch notepad...
                if (!oShell32.ShellExecuteEx(oSei)) {
                    showErrorBox("Notepad could not be launched!", "Error");
                }
            }
        });

        moOptionExecButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isRegValSet("CommandPromptWindowHandle")) {
                    try {
                        OptionSelectItem oOptionSelectItem = (OptionSelectItem) moOptionSelect.getItemAt(moOptionSelect.getSelectedIndex());
                        setVisible(false);
                        if(showConfirmationBox("Are you sure you wish to " + oOptionSelectItem.getTitle() + " ?", "Alert")) {
                            sendKeys(oOptionSelectItem.getVerb(), string2hwnd(getStringRegVal("CommandPromptWindowHandle")));
                            keyDownUp(oVirtKeys.get("VK_ENTER"), string2hwnd(getStringRegVal("CommandPromptWindowHandle")));
                        }
                    } catch (Exception oExc) {
                        log("Error: exception occurred!\n");
                        oExc.printStackTrace();
                    }
                }
            }
        });

        moSysMonTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            }
        });

        //moSysMonTimer.start();

        moUiMonTimer = new Timer(500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                configureUI();
            }
        });

        moUiMonTimer.start();

    }

    private void configureUI() {
        /*
        configure window UI...
         */
        
        int iHeightDivisor = 4;
        
        setLayout(null);
        setLocation(0, 0);
        setUndecorated(true);
        setSize(SCREEN_WIDTH, (SCREEN_HEIGHT_UNIT * 8));
        setAlwaysOnTop(true);
        getContentPane().setBackground(Color.BLACK);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        moMacroControlsLabel.setLocation(
                /* x */STD_CTRL_PADD,
                /* y */ ((getHeight() - SCREEN_HEIGHT_UNIT) - STD_CTRL_PADD)
        );
        moMacroControlsLabel.setSize((SCREEN_WIDTH_UNIT * 2), (SCREEN_HEIGHT_UNIT / iHeightDivisor));
        moMacroControlsLabel.setVisible(true);
        moMacroControlsLabel.setForeground(Color.white);

        moMacroSelect.setLocation(
                /* x */STD_CTRL_PADD,
                /* y */ ((getHeight() - SCREEN_HEIGHT_UNIT / 2) - STD_CTRL_PADD)
        );
        moMacroSelect.setSize((SCREEN_WIDTH_UNIT * 2), (SCREEN_HEIGHT_UNIT / iHeightDivisor));
        moMacroSelect.setVisible(true);

        moMacroExecButton.setLocation(
                /* x */((moMacroSelect.getLocation().x + ((SCREEN_WIDTH_UNIT * 2))) + STD_CTRL_PADD),
                /* y */ ((getHeight() - SCREEN_HEIGHT_UNIT / 2) - STD_CTRL_PADD)
        );
        moMacroExecButton.setSize((SCREEN_WIDTH_UNIT / 2), (SCREEN_HEIGHT_UNIT / iHeightDivisor));
        moMacroExecButton.setVisible(true);

        moMacroAddButton.setLocation(
                /* x */((moMacroExecButton.getLocation().x + (SCREEN_WIDTH_UNIT / 2)) + STD_CTRL_PADD),
                /* y */ ((getHeight() - SCREEN_HEIGHT_UNIT / 2) - STD_CTRL_PADD)
        );
        moMacroAddButton.setSize((SCREEN_WIDTH_UNIT / 2), (SCREEN_HEIGHT_UNIT / iHeightDivisor));
        moMacroAddButton.setVisible(true);

        moMacroEditButton.setLocation(
                /* x */((moMacroAddButton.getLocation().x + (SCREEN_WIDTH_UNIT / 2)) + STD_CTRL_PADD),
                /* y */ ((getHeight() - SCREEN_HEIGHT_UNIT / 2) - STD_CTRL_PADD)
        );
        moMacroEditButton.setSize((SCREEN_WIDTH_UNIT / 2), (SCREEN_HEIGHT_UNIT / iHeightDivisor));
        moMacroEditButton.setVisible(true);


        moCloseButton.setSize((int) ((SCREEN_WIDTH_UNIT * 3.75) - STD_CTRL_PADD * 4), (SCREEN_HEIGHT_UNIT / iHeightDivisor));
        moCloseButton.setLocation(
                /* x */ ((moMacroEditButton.getLocation().x + (SCREEN_WIDTH_UNIT / 2)) + STD_CTRL_PADD * 4),
                /* y */ ((getHeight() - SCREEN_HEIGHT_UNIT / 2) - STD_CTRL_PADD)
        );
        moCloseButton.setVisible(true);





        moOptionControlsLabel.setLocation(
                /* x */(int) ((SCREEN_WIDTH - (SCREEN_WIDTH_UNIT * 2.5)) - (STD_CTRL_PADD * 2)),
                /* y */ ((getHeight() - SCREEN_HEIGHT_UNIT) - STD_CTRL_PADD)
        );
        moOptionControlsLabel.setSize((SCREEN_WIDTH_UNIT * 2), (SCREEN_HEIGHT_UNIT / iHeightDivisor));
        moOptionControlsLabel.setVisible(true);
        moOptionControlsLabel.setForeground(Color.white);

        moOptionSelect.setSize((SCREEN_WIDTH_UNIT * 2), (SCREEN_HEIGHT_UNIT / iHeightDivisor));
        moOptionSelect.setLocation(
                /* x */(int) ((SCREEN_WIDTH - (SCREEN_WIDTH_UNIT * 2.5)) - (STD_CTRL_PADD * 2)),
                /* y */ ((getHeight() - SCREEN_HEIGHT_UNIT / 2) - STD_CTRL_PADD)
        );
        moOptionSelect.setVisible(true);

        moOptionExecButton.setSize((SCREEN_WIDTH_UNIT / 2), (SCREEN_HEIGHT_UNIT / iHeightDivisor));
        moOptionExecButton.setLocation(
                /* x */ (SCREEN_WIDTH - (SCREEN_WIDTH_UNIT / 2) - STD_CTRL_PADD),
                /* y */ ((getHeight() - SCREEN_HEIGHT_UNIT / 2) - STD_CTRL_PADD)
        );
        moOptionExecButton.setVisible(true);

        add(moMacroControlsLabel);
        add(moCloseButton);
        add(moMacroExecButton);
        add(moMacroAddButton);
        add(moMacroEditButton);
        add(moMacroSelect);
        add(moOptionControlsLabel);
        add(moOptionSelect);
        add(moOptionExecButton);
    }

    private boolean loadMacros() {
        try {
            File oFile = new File(WORKING_DIRECTORY + "\\macros.csv");
            if (oFile.exists() && !oFile.isDirectory()) {
                moMacros = loadTextFile("macros.csv");
                if (!moMacros.isEmpty()) {
                    log("Macros loaded!");
                    for (String sMacroEntry : moMacros) {
                        if (sMacroEntry.contains(",") && !sMacroEntry.contains("#")) {
                            String[] saPieces = sMacroEntry.split(",");
                            moMacroSelect.addItem(new MacroSelectItem(saPieces[0], saPieces[1]));
                        }
                    }
                    return true;
                } else {
                    log("Macros file empty! Macros not loaded!");
                    showErrorBox("Macros file empty! Macros not loaded!", "Error");
                    return false;
                }
            } else {
                log("Macro file not found! Macros not loaded!");
                showErrorBox("Macro file not found! Macros not loaded!", "Error");
                return false;
            }
        } catch (Exception oExc) {
            log("Error: exception occurred!\n");
            oExc.printStackTrace();
            showErrorBox("Macro file could not be loaded due to exeception!", "Error");
            return false;
        }
    }

    private boolean loadPrivateMacros() {
            if(!oPrivateMacros.isEmpty()) {
                log("Loading private macros...");
                for (Map.Entry<String, String> entry : oPrivateMacros.entrySet()) {
                    moOptionSelect.addItem(new OptionSelectItem(entry.getKey(), entry.getValue()));
                }
                return true;
            }else{
                log("Error: no private macros to load!");
                return false;
            }
    }

}
