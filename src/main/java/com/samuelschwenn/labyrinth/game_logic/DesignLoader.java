package com.samuelschwenn.labyrinth.game_logic;

import com.samuelschwenn.labyrinth.LabyrinthApplication;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.Building;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster.Monster;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;
import lombok.Setter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;

import static com.samuelschwenn.labyrinth.game_app.util.SoundUtils.playSFX;


@Setter
public class DesignLoader {
    private static DesignLoader instance;

    private DesignLoader(){}

    public static DesignLoader getInstance() {
        if(instance == null){
            instance = new DesignLoader();
        }
        return instance;
    }

    private String designToLoad = "";

    public void loadDesign() throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        if (!designToLoad.isEmpty()) {
            InputStream stream = LabyrinthApplication.class.getClassLoader().getResourceAsStream(designToLoad + ".txt");
            if (stream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String[] arguments = getArguments(reader);
                for (int i = 0; i < arguments.length - 2; i += 3) {
                    CoordsInt position = new CoordsInt(Integer.parseInt(String.valueOf(arguments[i])), Integer.parseInt(String.valueOf(arguments[i + 1])));
                    String build = arguments[i + 2].split(" ")[1];
                    Class<?> buildingToBuild = Class.forName(build);
                    Building building = (Building) buildingToBuild.getDeclaredConstructor(CoordsInt.class).newInstance(position);

                    if (GlobalUsageController.getInstance().getMoney() - building.getCost() >= 0) {
                        LogicRepresentation.getInstance().addBuilding(position, building);
                        for (Monster monster : LogicRepresentation.getInstance().getMonsterList()) {
                            monster.updateMonsterPath();
                        }
                        playSFX(8);
                        GlobalUsageController.getInstance().setMoney(GlobalUsageController.getInstance().getMoney() - building.getCost());
                    }
                }
            }else{
                System.out.println("Ungültige File");
            }
            designToLoad = "";
        }
    }

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
