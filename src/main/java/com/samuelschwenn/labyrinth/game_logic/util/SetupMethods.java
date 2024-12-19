package com.samuelschwenn.labyrinth.game_logic.util;

import com.samuelschwenn.labyrinth.game_app.visuals.game_visuals.GameScreen;
import com.samuelschwenn.labyrinth.game_app.visuals.game_visuals.StartGamePopUp;
import com.samuelschwenn.labyrinth.game_logic.GlobalUsageController;
import com.samuelschwenn.labyrinth.game_logic.LogicRepresentation;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.basis.Basis;
import com.samuelschwenn.labyrinth.game_logic.level.Level;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class SetupMethods {
    private static final GlobalUsageController usageController = GlobalUsageController.getInstance();

    public static void selectLevel(int chosenLevel) {
        usageController.setCurrent_level(chosenLevel);
        Level level = getLevel(chosenLevel);
        usageController.setMoney(level.getStartKapital());
        usageController.resetLogicRepresentation(level);
        usageController.setWall_count(level.getAnzahlMauern());
    }

    private static Level getLevel(int currentLevel) {
        usageController.resetDrawables();
        usageController.resetTickables();
        usageController.resetBasis();
        try {
            Class<?> levelToLoad = Class.forName("com.samuelschwenn.labyrinth.game_logic.level.Level" + currentLevel);
            return (Level) levelToLoad.getDeclaredConstructor(Basis.class).newInstance(usageController.getBasis());
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            System.out.println("Couldn't load class file for level " + currentLevel);
            throw new RuntimeException(e);
        }
    }

    public static List<JFrame> setUpGameWindow() {
        JFrame game_frame = new JFrame();
        game_frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                game_frame.setVisible(false);
                try (FileOutputStream fileOutputStream = new FileOutputStream("GameFileEntity.txt");
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
        usageController.resetTickables();
        usageController.resetDrawables();
        LogicRepresentation.getInstance().addToDrawablesAndTickables();
        usageController.setBasis(LogicRepresentation.getInstance().getBasis());
        GameScreen gameScreen = new GameScreen();
        game_frame.setContentPane(gameScreen);
        game_frame.setSize(gameScreen.getSize());
        game_frame.setVisible(true);
        game_frame.setResizable(false);
        JFrame start_frame = new StartGamePopUp(gameScreen.getWidth());
        List<JFrame> frames = new ArrayList<>();
        frames.add(start_frame);
        frames.add(game_frame);
        return frames;
    }

    //Load-Design needs to be implemented
}
