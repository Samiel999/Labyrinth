package com.samuelschwenn.labyrinth.game_logic;

import com.samuelschwenn.labyrinth.game_app.sounds.MusicController;
import com.samuelschwenn.labyrinth.game_app.visuals.end_screen.LosingScreen;
import com.samuelschwenn.labyrinth.game_app.visuals.end_screen.WinningScreen;
import com.samuelschwenn.labyrinth.game_app.visuals.main_menu.LevelSelectionScreen;
import com.samuelschwenn.labyrinth.game_app.visuals.main_menu.MainMenu;
import com.samuelschwenn.labyrinth.game_app.visuals.main_menu.Settings;
import com.samuelschwenn.labyrinth.game_logic.database.repository.final_repositories.CustomizedGameFileRepository;
import com.samuelschwenn.labyrinth.game_logic.database.repository.final_repositories.CustomizedGameFileRepositoryImpl;
import com.samuelschwenn.labyrinth.game_logic.util.LoopType;
import com.samuelschwenn.labyrinth.game_logic.util.SetupMethods;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.samuelschwenn.labyrinth.game_app.visuals.game_visuals.GameScreen.paused;
import static com.samuelschwenn.labyrinth.game_logic.util.LoopType.game_loop_pending;
import static com.samuelschwenn.labyrinth.game_logic.util.LoopType.main_menu;

@Component
public class UpdateLoop implements Runnable {

    private static final int MAIN_GRAPHIC = 0;

    CustomizedGameFileRepositoryImpl gameFileRepository;

    private LoopType current_loop;
    private List<JFrame> current_graphics = new ArrayList<>();
    private final GlobalUsageController usageController = GlobalUsageController.getInstance();
    private final MusicController musicController = MusicController.getInstance();

    private boolean game_loaded = false;

    public UpdateLoop(CustomizedGameFileRepositoryImpl gameFileRepository) {
        this.gameFileRepository = gameFileRepository;
    }

    public void run() {
        setCurrentLoopToMainMenu();
        tryToSelectLevelBasedOnSaveFile();
        main_menu_loop();
    }

    private void setCurrentLoopToMainMenu() {
        current_loop = main_menu;
    }

    private void tryToSelectLevelBasedOnSaveFile() {
        try {
            InputStream stream = MainMenu.class.getClassLoader().getResourceAsStream("GameFileEntity.txt");
            if (stream != null) {
                processFileData(stream);
            }
        } catch (IOException _) {
        }
    }

    private void processFileData(InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        char[] input = new char[6];
        reader.read(input);
        if (input[0] == '\u0000') throw new IOException("Invalid save file");
        String str = String.copyValueOf(input, 0, 5);
        if (str.equals("Level")) SetupMethods.selectLevel(input[5] - '0');
        reader.close();
    }

    private void main_menu_loop() {
        if(current_graphics.isEmpty()) current_graphics.add(new MainMenu());
        else current_graphics.set(MAIN_GRAPHIC, new MainMenu());
        musicController.playMainMenuMusic();
        tryCheckForUpdates();
    }

    private void game_loop_pending(boolean continue_game) {
        musicController.stopMusic();
        SetupMethods.selectLevel(usageController.getCurrent_level());
        if (continue_game) {
            LogicRepresentation lr;
            try (InputStream inputStream = UpdateLoop.class.getClassLoader().getResourceAsStream("GameFileEntity.txt");
                 ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
                lr = (LogicRepresentation) objectInputStream.readObject();
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            LogicRepresentation.setInstance(lr);
        } else {
            File save_file = new File("GameFileEntity.txt");
            try {
                if (save_file.createNewFile()) {
                    if (save_file.canWrite()) {
                        FileWriter fileWriter = new FileWriter(save_file, true);
                        fileWriter.write("");
                        fileWriter.flush();
                        fileWriter.close();
                    }
                } else {
                    if (save_file.canWrite()) {
                        FileWriter fileWriter = new FileWriter(save_file, false);
                        fileWriter.write("");
                        fileWriter.flush();
                        fileWriter.close();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try (PrintWriter printWriter = new PrintWriter("GameFileEntity.txt")) {
                printWriter.print("");
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        musicController.playGameNotStartedMusic();

        current_graphics = SetupMethods.setUpGameWindow();
        game_loaded = true;
        tryCheckForUpdates();
    }

    private void game_loop_started(){
        paused = false;
        tryCheckForUpdates();
    }

    private void game_over(){
        usageController.resetDrawables();
        usageController.resetTickables();
        current_graphics.getLast().setVisible(false);
        paused = true;

        int current_level = usageController.getCurrent_level();
        if (current_level == 4) {
            current_level = 1;
        } else if (LogicRepresentation.getInstance().playerWins()) current_level++;
        try (PrintWriter printWriter = new PrintWriter("GameFileEntity.txt")) {
            printWriter.print("Level" + current_level);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        usageController.setCurrent_level(current_level);
        int gameOverScreenX = current_graphics.getFirst().getX() + (current_graphics.getFirst().getWidth() / 2) - 100;
        int gameOverScreenY = current_graphics.getFirst().getY() + (current_graphics.getFirst().getHeight() / 2) - 50;
        musicController.stopMusic();

        current_graphics.clear();
        if (LogicRepresentation.getInstance().playerWins()) {
            current_graphics.add(new WinningScreen(gameOverScreenX, gameOverScreenY));
        } else {
            current_graphics.add(new LosingScreen(gameOverScreenX, gameOverScreenY));
        }
        game_loaded = false;
        tryCheckForUpdates();
    }

    private void level_selection(){
        current_graphics.set(MAIN_GRAPHIC, new LevelSelectionScreen());
        tryCheckForUpdates();
    }

    private void settings() {
        gameFileRepository.saveToFileById(1);
        new Settings(game_loaded, !paused);
        if (!paused) paused = true;
        tryCheckForUpdates();
    }

    private void tryCheckForUpdates(){
        try{
            check_for_updates();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized void check_for_updates() throws InterruptedException {
        wait();
        switch (current_loop) {
            case main_menu:
                main_menu_loop();
                break;
            case game_loop_pending:
                game_loop_pending(false);
                break;
            case game_loop_started:
                game_loop_started();
                break;
            case game_over:
                game_over();
                break;
            case level_selection:
                level_selection();
                break;
            case settings:
                settings();
                break;
            case forward:
                check_for_updates();
                break;
            case main_menu_during_game:
                for (JFrame frame : current_graphics) {
                    frame.setVisible(false);
                    frame.dispose();
                }
                musicController.stopMusic();
                main_menu_loop();
                break;
            case continue_game:
                game_loop_pending(true);
                break;
        }
    }

    public synchronized void update(LoopType update_info) {
        current_loop = update_info;
        notify();
    }

    public synchronized void update(int level) {
        current_loop = game_loop_pending;
        usageController.setCurrent_level(level);
        notify();
    }
}
