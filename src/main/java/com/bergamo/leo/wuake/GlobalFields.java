package com.bergamo.leo.wuake;

import java.util.HashMap;
import java.util.Map;

public class GlobalFields {
    
    public static Map<String,String> oPrivateMacros;

    static{
        oPrivateMacros = Map.ofEntries(
                Map.entry("install Gsudo by Geradog", "winget install gerardog.gsudo")
        );
    }

    public static Map<String, Integer> oVirtKeys;

    static {
           oVirtKeys = Map.ofEntries(
                Map.entry("VK_OEM_SEMI-COLON_OR_COLON", 0xBA),                              //the ; or : keys
                Map.entry("VK_OEM_PLUS", 0xBB),                                             //the + key
                Map.entry("VK_OEM_COMMA", 0xBC),                                            //the , key
                Map.entry("VK_OEM_MINUS", 0xBD),                                            //the - key
                Map.entry("VK_OEM_PERIOD", 0xBE),                                           //the . key
                Map.entry("VK_OEM_FORWARD_SLASH_OR_QUESTION_MARK", 0xBF),                   //the / or ? keys
                Map.entry("VK_OEM_APOSTROPHE_OR_TILDE", 0xC0),                              //the ` or ~ key
                Map.entry("VK_OEM_FORWARD_SQUARE_BRACE_OR_FORWARD_CURLY_BRACE", 0xDB),      //the [ or { keys
                Map.entry("VK_OEM_BACKSLASH_OR_PIPE", 0xDC),                                //the \ or | keys
                Map.entry("VK_OEM_BACKWARD_SQUARE_BRACE_OR_BACKWARD_CURLY_BRACE", 0xDD),    //the ] or } keys
                Map.entry("VK_OEM_SINGLE_QUOTE_OR_DOUBLE-QUOTE", 0xDE),                     //the ' or " key
                Map.entry("VK_0", 0x30),                                                    //The 0 key
                Map.entry("VK_1", 0x31),                                                    //The 1 key
                Map.entry("VK_2", 0x32),                                                    //The 2 key
                Map.entry("VK_3", 0x33),                                                    //The 3 key
                Map.entry("VK_4", 0x34),                                                    //The 4 key
                Map.entry("VK_5", 0x35),                                                    //The 5 key
                Map.entry("VK_6", 0x36),                                                    //The 6 key
                Map.entry("VK_7", 0x37),                                                    //The 7 key
                Map.entry("VK_8", 0x38),                                                    //The 8 key
                Map.entry("VK_9", 0x39),                                                    //The 9 key
                Map.entry("VK_SHIFT", 0xA0),                                                //The left SHIFT key
                Map.entry("VK_ENTER", 0x0D)                                                 //The ENTER key
           );
    }

    public static class oRectangle {

        private final int miLeft;
        private final int miTop;
        private final int miRight;
        private final int miBottom;

        public oRectangle(int iLeft, int iTop, int iRight, int iBottom) {
            this.miLeft = iLeft;
            this.miTop = iTop;
            this.miRight = iRight;
            this.miBottom = iBottom;
        }

        public int getLeft() {
            return miLeft;
        }

        public int getTop() {
            return miTop;
        }

        public int getRight() {
            return miRight;
        }

        public int getBottom() {
            return miBottom;
        }
    }
}
