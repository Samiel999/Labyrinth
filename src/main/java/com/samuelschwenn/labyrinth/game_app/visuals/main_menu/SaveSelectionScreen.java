package com.samuelschwenn.labyrinth.game_app.visuals.main_menu;

import com.samuelschwenn.labyrinth.game_logic.GlobalUsageController;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.samuelschwenn.labyrinth.LabyrinthApplication.loop;

public class SaveSelectionScreen extends JFrame implements MouseListener {
    private final int saveWidth = 60, saveHeight = 60, margin = 10;
    private final int column1 = 10 + margin;
    private final int y = 10 + margin + 28;
    private final int column2 = column1 + saveWidth + margin;
    private final int column3 = column2 + saveWidth + margin;
    private final Rectangle save1Bounds = new Rectangle(column1, y, saveWidth, saveHeight);
    private final Rectangle save2Bounds = new Rectangle(column2, y, saveWidth, saveHeight);
    private final Rectangle save3Bounds = new Rectangle(column3, y, saveWidth, saveHeight);
    private BufferedImage bufferedImage = null;
    private final int width = 800, height = 533;
    private static boolean mainMenuEntered;

    public SaveSelectionScreen() {
        mainMenuEntered = false;
        addWindowListener(
                new WindowAdapter() {
                    public void windowClosing(WindowEvent e) {
                        new MainMenu();
                        setVisible(false);
                        dispose();
                    }
                }
        );
        try {
            bufferedImage = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/BackgroundLevelAuswahl.png")));
        } catch (IOException ignored) {
        }
        int systemWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        int systemHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        int leftCornerUpX = systemWidth / 2 - (width / 2);
        int leftCornerUpY = systemHeight / 2 - (height / 2);
        setBackground(Color.white);
        setSize(width, height);
        setLocation(
                leftCornerUpX, leftCornerUpY
        );
        setLayout(null);
        setVisible(true);
    }

    @Override
    public void paint(Graphics g2) {
        g2.drawImage(
                bufferedImage,
                0,
                0,
                width,
                height,
                null
        );

        Graphics2D g = (Graphics2D) g2;

        g.setStroke(new BasicStroke(3));
        g.drawRect(save1Bounds.x, save1Bounds.y, save1Bounds.width, save1Bounds.height);
        g.drawString("Level 1", save1Bounds.x + 8, save1Bounds.y + 33);

        g.drawRect(save2Bounds.x, save2Bounds.y, save2Bounds.width, save2Bounds.height);
        g.drawString("Level 2", save2Bounds.x + 8, save2Bounds.y + 33);

        g.drawRect(save3Bounds.x, save3Bounds.y, save3Bounds.width, save3Bounds.height);
        g.drawString("Level 3", save3Bounds.x + 8, save3Bounds.y + 33);

        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Map<Rectangle, Integer> bounds = new HashMap<>(Map.of(
                save1Bounds, 1,
                save2Bounds, 2,
                save3Bounds, 3
        ));

        if(!mainMenuEntered) {
            mainMenuEntered = true;
            for(Rectangle bound : bounds.keySet()) {
                if(bound.contains(e.getX(), e.getY())) {
                    dispose();
                    GlobalUsageController.getInstance().setCurrent_save(bounds.get(bound));
//                    loop.update()
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
