package com.samuelschwenn.labyrinth.game_app.visuals.game_visuals;

import com.samuelschwenn.labyrinth.game_logic.DesignLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class LoadDesignPopUp extends JFrame {
    public LoadDesignPopUp() {
        addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        setVisible(false);
                        GameScreen.pressed[1] = false;
                        dispose();
                    }
                }
        );
        //Grundlegende Initialisierung des Fensters, anschließende Darstellung des Fensters
        setBackground(Color.white);
        JTextField textField = new JTextField("", 8);
        JButton acceptText = new JButton("LoadDesignPopUp level");
        acceptText.addActionListener(
                _ -> {
                    DesignLoader.getInstance().setDesignToLoad(textField.getText());
                    setVisible(false);
                    GameScreen.pressed[1] = false;
                    dispose();
                }
        );
        add(textField);
        add(acceptText);
        setSize(200, 200);
        setLocation(0, 0);
        setLayout(new FlowLayout());
        setVisible(true);
    }
}
