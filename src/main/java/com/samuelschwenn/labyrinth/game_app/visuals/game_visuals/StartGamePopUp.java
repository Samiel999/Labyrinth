package com.samuelschwenn.labyrinth.game_app.visuals.game_visuals;

import com.samuelschwenn.labyrinth.game_logic.LogicRepresentation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static com.samuelschwenn.labyrinth.LabyrinthApplication.loop;
import static com.samuelschwenn.labyrinth.game_app.util.SoundUtils.playMusic;
import static com.samuelschwenn.labyrinth.game_app.util.SoundUtils.stopMusic;
import static com.samuelschwenn.labyrinth.game_logic.util.LoopType.game_loop_started;


public class StartGamePopUp extends JFrame {
    public StartGamePopUp(int x) {
        addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        setVisible(false);
                        try (FileOutputStream fileOutputStream = new FileOutputStream("GameFileEntity.txt");
                             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                            objectOutputStream.writeObject(LogicRepresentation.getInstance());
                            objectOutputStream.flush();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        dispose();
                        System.exit(0);
                    }
                }
        );
        //Grundlegende Initialisierung des Fensters, anschließende Darstellung des Fensters
        setBackground(Color.white);
        JButton button = new JButton("Start");
        button.addActionListener(_ -> {
            setVisible(false);
            loop.update(game_loop_started);
            stopMusic();
            playMusic(0);
            dispose();
        });
        add(button);
        setSize(100, 100);
        setLocation(x + 30, 0);
        setLayout(new FlowLayout());
        setVisible(true);
    }
}
