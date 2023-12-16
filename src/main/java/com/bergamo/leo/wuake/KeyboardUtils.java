package com.bergamo.leo.wuake;


import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinUser;

import static com.bergamo.leo.wuake.GlobalFields.oVirtKeys;

/**
 *
 * @author berga
 */
public class KeyboardUtils {
        public static void sendKeys(String sKeys, WinDef.HWND oTargetWindowHandle) {

        int iCharCode;

        // loop through keys one by one...
        for (int iI = 0; iI < sKeys.length(); iI++) { 
            
            // hold SHIFT if needed and set character code field according 
            // to character...
            switch (sKeys.charAt(iI)) {
                case '!' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_1");
                }
                case '@' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_2");
                }
                case '#' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_3");
                }
                case '$' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_4");
                }
                case '%' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_5");
                }
                case '^' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_6");
                }
                case '&' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_7");
                }
                case '*' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_8");
                }
                case '(' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_9");
                }
                case ')' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_0");
                }
                case ';' ->
                    iCharCode = oVirtKeys.get("VK_OEM_SEMI-COLON_OR_COLON");
                case ':' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_OEM_SEMI-COLON_OR_COLON");
                }
                case '+' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_OEM_PLUS");
                }
                case ',' ->
                    iCharCode = oVirtKeys.get("VK_OEM_COMMA");
                case '-' ->
                    iCharCode = oVirtKeys.get("VK_OEM_MINUS");
                case '.' ->
                    iCharCode = oVirtKeys.get("VK_OEM_PERIOD");
                case '/' ->
                    iCharCode = oVirtKeys.get("VK_OEM_FORWARD_SLASH_OR_QUESTION_MARK");
                case '?' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_OEM_FORWARD_SLASH_OR_QUESTION_MARK");
                }
                case '`' ->
                    iCharCode = oVirtKeys.get("VK_OEM_APOSTROPHE_OR_TILDE");
                case '~' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_OEM_APOSTROPHE_OR_TILDE");
                }
                case '[' ->
                    iCharCode = oVirtKeys.get("VK_OEM_FORWARD_SQUARE_BRACE_OR_FORWARD_CURLY_BRACE");
                case '{' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_OEM_FORWARD_SQUARE_BRACE_OR_FORWARD_CURLY_BRACE");
                }
                case '\\' ->
                    iCharCode = oVirtKeys.get("VK_OEM_BACKSLASH_OR_PIPE");
                case '|' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_OEM_BACKSLASH_OR_PIPE");
                }
                case ']' ->
                    iCharCode = oVirtKeys.get("VK_OEM_BACKWARD_SQUARE_BRACE_OR_BACKWARD_CURLY_BRACE");
                case '}' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_OEM_BACKWARD_SQUARE_BRACE_OR_BACKWARD_CURLY_BRACE");
                }
                case '\'' ->
                    iCharCode = oVirtKeys.get("VK_OEM_SINGLE_QUOTE_OR_DOUBLE-QUOTE");
                case '"' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_OEM_SINGLE_QUOTE_OR_DOUBLE-QUOTE");
                }
                case '<' ->
                    iCharCode = oVirtKeys.get("VK_OEM_LESS_THAN_OR_GREATER_THAN");
                case '>' -> {
                    keyDown(0xA0, oTargetWindowHandle);
                    iCharCode = oVirtKeys.get("VK_OEM_LESS_THAN_OR_GREATER_THAN");
                }
                default ->
                    iCharCode = (int) sKeys.toUpperCase().charAt(iI);
            }

            // hold SHIFT if character is upper case...
            if (Character.isUpperCase(sKeys.charAt(iI))) {
                keyDown(oVirtKeys.get("VK_SHIFT"), oTargetWindowHandle);
            }

            // send key to target window...
            keyDownUp(iCharCode, oTargetWindowHandle);
            
            // release SHIFT...
            keyUp(oVirtKeys.get("VK_SHIFT"), oTargetWindowHandle);

        }
    }

    public static void keyUp(Integer iKey, WinDef.HWND oTargetWindowHandle) {
        // release key...
        // set required fields...
        final User32 oUser32 = User32.INSTANCE;
        
        // bring the target window to the front...
        oUser32.SetForegroundWindow(oTargetWindowHandle);

        // prepare input reference...
        WinUser.INPUT oInput = new WinUser.INPUT();
        
        oInput.type = new WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD);
        oInput.input.setType("ki");
        oInput.input.ki.wScan = new WinDef.WORD(0);
        oInput.input.ki.time = new WinDef.DWORD(0);
        oInput.input.ki.dwExtraInfo = new BaseTSD.ULONG_PTR(0);

        // release key...
        oInput.input.ki.wVk = new WinDef.WORD(iKey);
        oInput.input.ki.dwFlags = new WinDef.DWORD(2);
        oUser32.SendInput(
                new WinDef.DWORD(1), 
                (WinUser.INPUT[]) oInput.toArray(1), 
                oInput.size()
        );
    }

    public static void keyDown(int iKey, WinDef.HWND oTargetWindowHandle) {
        // press key...
        // set required fields...
        final User32 oUser32 = User32.INSTANCE;

        // bring the target window to the front...
        oUser32.SetForegroundWindow(oTargetWindowHandle);

        // prepare input reference...
        WinUser.INPUT oInput = new WinUser.INPUT();

        oInput.type = new WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD);
        oInput.input.setType("ki");
        oInput.input.ki.wScan = new WinDef.WORD(0);
        oInput.input.ki.time = new WinDef.DWORD(0);
        oInput.input.ki.dwExtraInfo = new BaseTSD.ULONG_PTR(0);

        // press key...
        oInput.input.ki.wVk = new WinDef.WORD(iKey);
        oInput.input.ki.dwFlags = new WinDef.DWORD(0);
        oUser32.SendInput(
                new WinDef.DWORD(1), 
                (WinUser.INPUT[]) oInput.toArray(1), 
                oInput.size()
        );
    }

    public static void keyDownUp(int iKey, WinDef.HWND oTargetWindowHandle) {
        // press and release key...
        // set required fields...
        final User32 oUser32 = User32.INSTANCE;

        // bring the target window to the front...
        oUser32.SetForegroundWindow(oTargetWindowHandle);

        // prepare input reference...
        WinUser.INPUT oInput = new WinUser.INPUT();
        oInput.type = new WinDef.DWORD(WinUser.INPUT.INPUT_KEYBOARD);
        oInput.input.setType("ki");
        oInput.input.ki.wScan = new WinDef.WORD(0);
        oInput.input.ki.time = new WinDef.DWORD(0);
        oInput.input.ki.dwExtraInfo = new BaseTSD.ULONG_PTR(0);

        // Press key...
        oInput.input.ki.wVk = new WinDef.WORD(iKey);
        oInput.input.ki.dwFlags = new WinDef.DWORD(0);
        oUser32.SendInput(
                new WinDef.DWORD(1), 
                (WinUser.INPUT[]) oInput.toArray(1), 
                oInput.size()
        );

        // Release key...
        oInput.input.ki.wVk = new WinDef.WORD(iKey);
        oInput.input.ki.dwFlags = new WinDef.DWORD(2);
        oUser32.SendInput(
                new WinDef.DWORD(1), 
                (WinUser.INPUT[]) oInput.toArray(1), 
                oInput.size()
        );
    }
    

}
