package com.samuelschwenn.game_app.visuals.game_visuals;

// Import of other classes into the project

import com.samuelschwenn.Main;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.Building;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.basis.DefaultBasis;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.tower.DefaultTower;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.tower.Minigun;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.tower.Sniper;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.wall.DefaultWall;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.Monster;
import com.samuelschwenn.game_logic.draw_and_tickables.Drawable;
import com.samuelschwenn.game_logic.draw_and_tickables.Tickable;
import com.samuelschwenn.game_logic.level.*;
import com.samuelschwenn.game_logic.LogicRepresentation;
import com.samuelschwenn.game_logic.util.LoopType;
import com.samuelschwenn.game_logic.util.CoordsInt;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Objects;

import static com.samuelschwenn.Main.loadDesign;
import static com.samuelschwenn.Main.loop;
import static com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.GameObject.instantiate;
import static com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.Building.getStaticImageOf;

// Main window which runs the game
public class GameScreen extends JPanel {
    // space between lines, aka length of squares
    public final static int spaceBetweenLinesPixels = 32;
    public final static int titleBarSizePixels = 54;

    // height and width of the opened window in pixels
    private final int windowWidthPixels;
    private final int windowHeightPixels;

    // boolean that prevents opening the popup multiple times
    public static final boolean[] pressed = {false, false};
    private BufferedImage backgroundImageLevel3 = null;
    private BufferedImage backgroundImageLevel5 = null;
    private Building chosenBuilding = null;
    private long lastTick = System.currentTimeMillis();
    private double spawnCooldown;

    public static boolean paused = true;

    // constructor for class GameScreen
    public GameScreen() {
        windowWidthPixels = LogicRepresentation.getInstance().getWidth() * spaceBetweenLinesPixels;
        windowHeightPixels = LogicRepresentation.getInstance().getHeight() * spaceBetweenLinesPixels + titleBarSizePixels;

        try {
            backgroundImageLevel3 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/BackgroundLevel3.jpg")));
            backgroundImageLevel5 = ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/BackgroundLevel5.jpg")));
        } catch (IOException e) {
            System.out.println("Something went wrong");
        }

        {
            this.addMouseListener(
                    new MouseAdapter() {
                        public void mousePressed(MouseEvent e) {
                            if (e.getY() <= titleBarSizePixels) {
                                if (!pressed[1]) {
                                    if (e.getX() >= 300 && e.getX() <= 470) {
                                        new SaveDesignPopUp();
                                    } else if (e.getX() >= 490 && e.getX() <= 620) {
                                        new LoadDesignPopUp();
                                    } else if (e.getX() >= windowWidthPixels - 130) {
                                        Main.source = true;
                                        loop.update(LoopType.settings);
                                    } else if (e.getX() >= 630 && e.getX() <= 671) {
                                        choseBuilding(DefaultTower.class);
                                    } else if (e.getX() >= 689 && e.getX() <= 730) {
                                        choseBuilding(Minigun.class);
                                    } else if (e.getX() >= 748 && e.getX() <= 789) {
                                        choseBuilding(Sniper.class);
                                    } else if (e.getX() >= 807 && e.getX() <= 848) {
                                        choseBuilding(DefaultWall.class);
                                    }
                                }
                                return;
                            }

                            int x = e.getX() / spaceBetweenLinesPixels;
                            int y = (e.getY() - titleBarSizePixels) / spaceBetweenLinesPixels;

                            if (LogicRepresentation.getInstance().getBuildings().containsKey(new CoordsInt(x, y))) {
                                if (LogicRepresentation.getInstance().getBuildings().get(new CoordsInt(x, y)).getSpawnTime() + 2 <= System.currentTimeMillis()) {
                                    try {
                                        new RemoveBuildingPopUp(x, y, e.getX(), e.getY());
                                    } catch (IOException ignored) {
                                    }
                                } else {
                                    pressed[0] = false;
                                }
                            } else {
                                build(chosenBuilding.getClass(), new CoordsInt(x, y), chosenBuilding.getCost());
                                pressed[0] = false;
                            }
                        }
                    }
            );

        }
        {
            this.addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseMoved(MouseEvent e) {
                    if (chosenBuilding == null) return;
                    int x = e.getX() / spaceBetweenLinesPixels;
                    int y = (e.getY() - titleBarSizePixels) / spaceBetweenLinesPixels;
                    if (e.getY() > titleBarSizePixels && !LogicRepresentation.getInstance().getBuildings().containsKey(new CoordsInt(x, y))) {
                        chosenBuilding.setPosition(new CoordsInt(x, y));
                    } else {
                        chosenBuilding.setPosition(new CoordsInt(-1, -1));
                    }
                }
            });
        }
        spawnCooldown = 0;

        setSize(windowWidthPixels, windowHeightPixels);
        setLayout(null);
        setVisible(true);
    }

    // paint()-Method
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawBackground(g);
        drawDrawables(g);
        double timeDelta = (System.currentTimeMillis() - lastTick) / 1000.0;
        if (!paused) {
            tick(timeDelta);
        }

        paintingTitleBarAndEdges(g);

        try {
            loadDesign();
        } catch (IOException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
                 InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        lastTick = System.currentTimeMillis();
        repaint();
    }

    private void paintingTitleBarAndEdges(Graphics g) {
        paint_edges((Graphics2D) g);

        paintTextIntoTitleBar(0, (loop.getMoney() / 100 >= 1 ? 128 : 112), 27, "Geld: " + loop.getMoney(), "Arial", 20, g);
        paintTextIntoTitleBar(298, 172, 27, "Design speichern", "Helvetica", 20, g);
        paintTextIntoTitleBar(488, 132, 27, "Design laden", "Helvetica", 20, g);
        paintTextIntoTitleBar(windowWidthPixels - 132, 132, 27, "Settings", "Helvetica", 20, g);

        paintTextIntoTitleBar(628, 220, 54, "", "Helvetica", 13, g);

        paintBuildingsIntoTitleBar(630, DefaultTower.class, "Default", g);
        paintBuildingsIntoTitleBar(689, Minigun.class, "Mini-Gun", g);
        paintBuildingsIntoTitleBar(748, Sniper.class, "Sniper", g);
        paintBuildingsIntoTitleBar(807, DefaultWall.class, "Wall", g);
    }

    private void paintTextIntoTitleBar(int x, int width, int height, String attribute, String fontType, int fontSize, Graphics g) {
        g.setColor(Color.BLACK);
        g.fillRect(x, 0, width, height);
        g.setColor(Color.WHITE);
        g.setFont(new Font(fontType, Font.BOLD, fontSize));
        g.drawString(attribute, x + 2, titleBarSizePixels - 32);
    }

    private void paintBuildingsIntoTitleBar(int x, Class<? extends Building> buildingClass, String visibleType, Graphics g) {
        Image image = getStaticImageOf(buildingClass);


        if (image != null) {
            g.fillRect(x + 2, 2, 37, 37);
            g.drawImage(
                    image,
                    x,
                    0,
                    41,
                    41,
                    null
            );
            if (chosenBuilding != null && chosenBuilding.getClass().equals(buildingClass)) {
                g.setColor(Color.BLUE.brighter());
            }
            g.drawString(visibleType, x, titleBarSizePixels - 2);
            if (chosenBuilding != null && chosenBuilding.getClass().equals(buildingClass)) {
                g.setColor(Color.WHITE);
            }
        }
    }

    private void paint_edges(Graphics2D graphics2D) {
        graphics2D.setStroke(new BasicStroke(1));
        graphics2D.setColor(Color.black);
        graphics2D.drawLine(0, titleBarSizePixels, windowWidthPixels, titleBarSizePixels);
    }

    private void tick(double timeDelta) {
        if (LogicRepresentation.getInstance().gameOver()) return;
        spawnCooldown -= timeDelta;
        for (Tickable tickable : loop.getTickables()) {
            tickable.tick(timeDelta);
        }
        if (spawnCooldown <= 0) {
            if (!LogicRepresentation.getInstance().getLevel().getMonstersToSpawn().isEmpty()) {
                LogicRepresentation.getInstance().spawnMonster();
            }
            spawnCooldown = LogicRepresentation.getInstance().getLevel().getSpawnTime();
        }
        for (Monster monster : LogicRepresentation.getInstance().getMonsterList()) {
            if (monster.getHealth() <= 0) {
                LogicRepresentation.getInstance().getMonsterList().remove(monster);
                monster.die();
            }
        }
        for (GameObject building : LogicRepresentation.getInstance().getBuildings().values().stream().toList()) {
            if (building.getHealth() <= 0) {
                building.die();
                if (building.getClass().equals(DefaultBasis.class)) {
                    setVisible(false);
                }
            }
        }
    }

    private void drawDrawables(Graphics g) {
        List<Drawable> drawables = loop.getDrawables().reversed();
        for (Drawable drawable : drawables) {
            drawable.draw(g);
        }
    }

    private void drawBackground(Graphics g) {
        if (LogicRepresentation.getInstance().getLevel().getClass().equals(Level1.class) ||
                LogicRepresentation.getInstance().getLevel().getClass().equals(Level2.class) ||
                LogicRepresentation.getInstance().getLevel().getClass().equals(Level3.class) ||
                LogicRepresentation.getInstance().getLevel().getClass().equals(Level4.class)) {
            g.drawImage(
                    backgroundImageLevel3,
                    0,
                    titleBarSizePixels,
                    windowWidthPixels,
                    windowHeightPixels,
                    null
            );
        } else if (LogicRepresentation.getInstance().getLevel().getClass().equals(Level5.class)) {
            g.drawImage(
                    backgroundImageLevel5,
                    0,
                    titleBarSizePixels,
                    windowWidthPixels,
                    windowHeightPixels,
                    null
            );
        }
    }

    private void build(Class<? extends GameObject> toBuild, CoordsInt position, double price) {
        if (price <= loop.getMoney()) {
            if (LogicRepresentation.getInstance().getBuilding(position) == null) {
                Building building = (Building) instantiate(toBuild, position);
                LogicRepresentation.getInstance().addBuilding(position, building);
                loop.setMoney(loop.getMoney() - price);
                for (Monster monster : LogicRepresentation.getInstance().getMonsterList()) {
                    monster.updateMonsterPath();
                }
            }
        }
        pressed[0] = false;
    }

    private void choseBuilding(Class<? extends Building> chosen) {
        if (chosenBuilding == null) {
            createChosenBuilding(chosen);
            return;
        }
        chosenBuilding.die();
        if (chosenBuilding.getClass().equals(chosen)) {
            chosenBuilding = null;
        } else {
            createChosenBuilding(chosen);
        }
        pressed[1] = false;
    }

    private void createChosenBuilding(Class<? extends Building> chosen) {
        chosenBuilding = (Building) instantiate(chosen, new CoordsInt(-1, -1));
        chosenBuilding.setBlueprint(true);
    }
}
