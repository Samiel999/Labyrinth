package com.samuelschwenn.game_logic.util;

import com.samuelschwenn.game_logic.LogicRepresentation;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.basis.Basis;
import com.samuelschwenn.game_logic.level.Level;
import com.samuelschwenn.game_app.visuals.game_visuals.GameScreen;
import com.samuelschwenn.game_app.visuals.game_visuals.StartGamePopUp;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;

import static com.samuelschwenn.Main.loop;

public class SetupMethods {
    public static void selectLevel(int chosenLevel) {
        loop.setCurrent_level(chosenLevel);
        Level level = getLevel(chosenLevel);
        loop.setMoney(level.getStartKapital());
        loop.resetLogicRepresentation(level);
        loop.setWall_count(level.getAnzahlMauern());
    }

    private static Level getLevel(int currentLevel) {
        loop.resetDrawables();
        loop.resetTickables();
        loop.resetBasis();
        try {
            Class<?> levelToLoad = Class.forName("com.samuelschwenn.game_logic.level.Level" + currentLevel);
            return (Level) levelToLoad.getDeclaredConstructor(Basis.class).newInstance(loop.getBasis());
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            System.out.println("Couldn't load class file for level " + currentLevel);
            throw new RuntimeException(e);
        }
    }

    public static JFrame[] setUpGameWindow() {
        JFrame game_frame = new JFrame();
        game_frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                game_frame.setVisible(false);
                try (FileOutputStream fileOutputStream = new FileOutputStream("Save.txt");
                     ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                    objectOutputStream.writeObject(LogicRepresentation.getInstance());
                    objectOutputStream.flush();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                game_frame.dispose();
                System.exit(0);
            }
        });
        loop.resetTickables();
        loop.resetDrawables();
        LogicRepresentation.getInstance().addToDrawablesAndTickables();
        loop.setBasis(LogicRepresentation.getInstance().getBasis());
        GameScreen gameScreen = new GameScreen();
        game_frame.setContentPane(gameScreen);
        game_frame.setSize(gameScreen.getSize());
        game_frame.setVisible(true);
        game_frame.setResizable(false);
        JFrame start_frame = new StartGamePopUp(gameScreen.getWidth());
        JFrame[] frames = new JFrame[2];
        frames[1] = start_frame;
        frames[0] = game_frame;
        return frames;
    }

    //Load-Design needs to be implemented
}
