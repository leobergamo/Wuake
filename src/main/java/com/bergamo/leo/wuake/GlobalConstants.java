package com.bergamo.leo.wuake;

import java.awt.*;
import java.io.File;
import static com.bergamo.leo.wuake.SundryUtils.*;

/**
 *
 * @author berga
 */
public class GlobalConstants {

    public final static String WORKING_DIRECTORY
            = new File("").getAbsolutePath();
    
    public final static Integer SCREEN_WIDTH_UNIT = getPerc(
            (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth(),
            10
    );
    
    public final static Integer SCREEN_HEIGHT_UNIT = getPerc(
            (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight(),
            10
    );
    
    public final static Integer SCREEN_WIDTH
            = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    
    public final static Integer SCREEN_HEIGHT
            = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
    
    public final static Integer STD_CTRL_PADD = 5;

}
