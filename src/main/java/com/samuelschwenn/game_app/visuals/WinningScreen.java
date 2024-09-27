package com.samuelschwenn.game_app.visuals;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import static com.samuelschwenn.Main.loop;
import static com.samuelschwenn.game_logic.util.LoopType.main_menu;
import static com.samuelschwenn.game_app.util.SoundUtils.playSFX;
import static com.samuelschwenn.game_app.util.SoundUtils.stopMusic;

public class WinningScreen extends JFrame {
    public WinningScreen(int xCoordinateUpperLeftCorner, int yCoordinateUpperLeftCorner) {
        playSFX(7);

        addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        setVisible(false);
                        dispose();
                        System.exit(0);
                    }
                }
        );
        JLabel text = new JLabel("Du hast gewonnen");
        text.setFont(new Font("font", Font.BOLD, 20));
        add(text);

        JButton mainMenu = new JButton("Hauptmenü");
        mainMenu.addActionListener(
                _ -> {
                    loop.update(main_menu);
                    stopMusic();
                    setVisible(false);
                    dispose();
                }
        );
        add(mainMenu);

        // Initialisierung des Frames und Sichtbarkeit
        setBackground(Color.white);
        setSize(200, 100);
        setLocation(xCoordinateUpperLeftCorner, yCoordinateUpperLeftCorner);
        setLayout(new FlowLayout());
        setVisible(true);
    }
}
