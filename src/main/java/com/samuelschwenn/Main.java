package com.samuelschwenn;

// Import necessary packages and classes
//import org.jetbrains.annotations.NotNull;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.Building;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.Monster;
import com.samuelschwenn.game_logic.LogicRepresentation;
import com.samuelschwenn.game_logic.UpdateLoop;
import com.samuelschwenn.game_app.sounds.SFX;
import com.samuelschwenn.game_app.sounds.Sound;
import com.samuelschwenn.game_logic.util.CoordsInt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

import static com.samuelschwenn.game_app.util.SoundUtils.playSFX;

// Main class
public class Main {
    public static UpdateLoop loop;
    public static String loadDesign = "";
    public static final Sound sound = new Sound();
    public static final SFX sfx = new SFX();
    public static Boolean source = false;


    public static void main(String[] args) {
        loop = new UpdateLoop();
        Thread loop_thread = new Thread(loop);
        loop_thread.start();
    }

    public static void loadDesign() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (!loadDesign.isEmpty()) {
            InputStream stream = Main.class.getClassLoader().getResourceAsStream(loadDesign + ".txt");
            if (stream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String[] arguments = getArguments(reader);
                for (int i = 0; i < arguments.length - 2; i += 3) {
                    CoordsInt position = new CoordsInt(Integer.parseInt(String.valueOf(arguments[i])), Integer.parseInt(String.valueOf(arguments[i + 1])));
                    String build = arguments[i + 2].split(" ")[1];
                    Class<?> buildingToBuild = Class.forName(build);
                    Building building = (Building) buildingToBuild.getDeclaredConstructor(CoordsInt.class).newInstance(position);

                    if (loop.getMoney() - building.getCost() >= 0) {
                        LogicRepresentation.getInstance().addBuilding(position, building);
                        for (Monster monster : LogicRepresentation.getInstance().getMonsterList()) {
                            monster.updateMonsterPath();
                        }
                        playSFX(8);
                        loop.setMoney(loop.getMoney() - building.getCost());
                    }
                }
            }else{
                System.out.println("Ungültige File");
            }
            loadDesign = "";
        }
    }

    //    @NotNull
    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static String[] getArguments(BufferedReader reader) throws IOException {
        char[] input = new char[15000];
        reader.read(input);
        String inputString = String.copyValueOf(input);
        for (int i = 0; i < input.length; i++) {
            if (input[i] == '\u0000') {
                inputString = inputString.substring(0, i);
                break;
            }
        }
        return inputString.split("_");
    }
}
