package com.samuelschwenn.labyrinth.game_app.visuals.game_visuals;


import com.samuelschwenn.labyrinth.game_logic.GlobalUsageController;
import com.samuelschwenn.labyrinth.game_logic.LogicRepresentation;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.Building;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Objects;

import static com.samuelschwenn.labyrinth.game_app.visuals.game_visuals.GameScreen.pressed;
import static com.samuelschwenn.labyrinth.game_app.visuals.game_visuals.GameScreen.spaceBetweenLinesPixels;

public class RemoveBuildingPopUp extends JFrame implements ActionListener {
    private final int targetXCoordinate;
    private final int targetYCoordinate;

    // targetXCoordinate: x-coordinate of the building NOT in pixels
    // targetYCoordinate: y-coordinate of the building NOT in pixels
    // locationX: x-coordinate of the mouse-position in pixels
    // locationY: y-coordinate of the mouse-position in pixels
    public RemoveBuildingPopUp(int targetXCoordinate, int targetYCoordinate, int mouseLocationX, int mouseLocationY) throws IOException {
        System.out.println("PopupRemove started");

        this.targetXCoordinate = targetXCoordinate;
        this.targetYCoordinate = targetYCoordinate;

        Image trashcanImage = ImageIO.read(Objects.requireNonNull(RemoveBuildingPopUp.class.getClassLoader().getResourceAsStream("images/Trashcan.png"))).getScaledInstance(spaceBetweenLinesPixels - 2, spaceBetweenLinesPixels - 2, Image.SCALE_SMOOTH);

        addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        setVisible(false);
                        pressed[0] = false;
                        dispose();
                    }
                }
        );
        JButton trashButton = new JButton(new ImageIcon(trashcanImage));

        trashButton.setActionCommand("Delete");
        trashButton.addActionListener(this);

        JButton cancelButton = new JButton("Cancel");

        cancelButton.setActionCommand("");
        cancelButton.addActionListener(this);

        add(trashButton);
        add(cancelButton);

        setBackground(Color.white);
        setSize(150, 100);

        setLocation(mouseLocationX - 75, mouseLocationY - 50);
        setLayout(new FlowLayout());
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e1) {
        if (e1.getActionCommand().equals("Delete")) {
            Building building = LogicRepresentation.getInstance().getBuildings().get(new CoordsInt(targetXCoordinate, targetYCoordinate));
            if(building == null){
                System.out.println("Something went wrong");
            }
            assert building != null;
            building.die();
            GlobalUsageController.getInstance().setMoney(GlobalUsageController.getInstance().getMoney() + building.getCost());
        }
        setVisible(false);
        pressed[0] = false;
        dispose();
    }
}
