package com.samuelschwenn.labyrinth.game_app.visuals.game_visuals;

import com.samuelschwenn.labyrinth.game_logic.LogicRepresentation;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.Building;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.basis.Basis;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class SaveDesignPopUp extends JFrame {
    public SaveDesignPopUp() {
        addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        setVisible(false);
                        GameScreen.pressed[1] = false;
                        dispose();
                    }
                }
        );
        setBackground(Color.white);
        JTextField textField = new JTextField("", 8);
        JButton acceptText = new JButton("SaveDesignPopUp Design");
        acceptText.addActionListener(
                _ -> {
                    Map<CoordsInt, Building> listToSave = LogicRepresentation.getInstance().getBuildings();
                    File saveFile = new File(textField.getText()+".txt");
                    try {
                        if(saveFile.createNewFile()){
                            if(saveFile.canWrite()){
                                FileWriter writer = new FileWriter(saveFile, true);
                                toFile(listToSave, writer);
                            }
                        }else{
                            if(saveFile.canWrite()){
                                FileWriter writer = new FileWriter(saveFile, false);
                                toFile(listToSave, writer);
                            }
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
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
    private void toFile(Map<CoordsInt, Building> listToSave, FileWriter writer) throws IOException {
        for (CoordsInt key : listToSave.keySet()){
            Building building = listToSave.get(key);
            if (!(building instanceof Basis)) {
                writer.write("");
                writer.flush();
                writer
                        .append(key.toString())
                        .append("_")
                        .append(building.getClass().toString())
                        .append("_")
                ;
            }
        }
        writer.close();
    }
}
