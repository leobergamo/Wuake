package com.bergamo.leo.wuake;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

//Attribution: Marcel @ https://stackoverflow.com/a/23958880

public class CustomJButtonType1 extends JButton {
    private final Color oPressedColor = Color.GRAY;
    private final Color oRolloverColor = Color.GREEN;
    private final Color oNormalColor = Color.GRAY;

    public CustomJButtonType1(String text) {
        super(text);
        setBorderPainted(false);
        setFocusPainted(false);

        setContentAreaFilled(false);
        setOpaque(true);

        setBackground(oNormalColor);
        setForeground(Color.WHITE);
        setFont(new Font("Tahoma", Font.BOLD, 12));
        setFocusPainted(false);
        setBorderPainted(false);
        setText(text);

        addChangeListener(evt -> {
            if (getModel().isPressed()) {
                setBackground(oPressedColor);
            } else if (getModel().isRollover()) {
                setBackground(oRolloverColor);
                setForeground(Color.BLACK);
            } else {
                setBackground(oNormalColor);
                setForeground(Color.WHITE);
            }
        });
    }
}
