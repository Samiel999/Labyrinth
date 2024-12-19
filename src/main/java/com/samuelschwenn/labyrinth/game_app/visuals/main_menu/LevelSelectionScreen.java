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
import static com.samuelschwenn.labyrinth.game_logic.util.LoopType.main_menu;


public class LevelSelectionScreen extends JFrame implements MouseListener {
    private final int levelWidth = 60, levelHeight = 60;
    private final int margin = 10;
    private final int column1 = 10 + margin;
    private final int row1 = 10 + margin + 28;
    private final int column2 = column1 + levelWidth + margin;
    private final int column3 = column2 + levelWidth + margin;
    private final int column4 = column3 + levelWidth + margin;
    private final int column5 = column4 + levelWidth + margin;
    private final Rectangle level1Bounds = new Rectangle(column1, row1, levelWidth, levelHeight);
    private final Rectangle level2Bounds = new Rectangle(column2, row1, levelWidth, levelHeight);
    private final Rectangle level3Bounds = new Rectangle(column3, row1, levelWidth, levelHeight);
    private final Rectangle level4Bounds = new Rectangle(column4, row1, levelWidth, levelHeight);
    private final Rectangle level5Bounds = new Rectangle(column5, row1, levelWidth, levelHeight);
    private BufferedImage bufferedImage = null;
    private final int width = 800;
    private final int height = 533;
    private static boolean mainMenuEntered;

    public LevelSelectionScreen() {
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

        g.drawRect(level1Bounds.x, level1Bounds.y, level1Bounds.width, level1Bounds.height);
        g.drawString("Level 1", level1Bounds.x + 8, level1Bounds.y + 33);

        g.drawRect(level2Bounds.x, level2Bounds.y, level2Bounds.width, level2Bounds.height);
        g.drawString("Level 2", level2Bounds.x + 8, level2Bounds.y + 33);

        g.drawRect(level3Bounds.x, level3Bounds.y, level3Bounds.width, level3Bounds.height);
        g.drawString("Level 3", level3Bounds.x + 8, level3Bounds.y + 33);

        g.drawRect(level4Bounds.x, level4Bounds.y, level4Bounds.width, level4Bounds.height);
        g.drawString("Level 4", level4Bounds.x + 8, level4Bounds.y + 33);

        g.drawRect(level5Bounds.x, level5Bounds.y, level5Bounds.width, level5Bounds.height);
        g.drawString("Level 5", level5Bounds.x + 8, level5Bounds.y + 33);

        addMouseListener(this);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        Map<Rectangle, Integer> bounds = new HashMap<>(Map.of(
                level1Bounds, 1,
                level2Bounds, 2,
                level3Bounds, 3,
                level4Bounds, 4,
                level5Bounds, 5
        ));

        if (!mainMenuEntered) {
            mainMenuEntered = true;

            for (Rectangle bound : bounds.keySet()) {
                if (bound.contains(e.getX(), e.getY())) {
                    dispose();
                    GlobalUsageController.getInstance().setCurrent_level(bounds.get(bound));
                    loop.update(main_menu);
                    setVisible(false);
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
