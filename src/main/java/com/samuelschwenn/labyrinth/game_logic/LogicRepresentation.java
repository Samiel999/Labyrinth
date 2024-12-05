package com.samuelschwenn.labyrinth.game_logic;

import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.Tickable;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.Building;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.basis.Basis;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.tower.DefaultTower;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.tower.Minigun;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.tower.Sniper;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster.Monster;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster.walking.Boss1;
import com.samuelschwenn.labyrinth.game_logic.level.Level;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;
import lombok.Getter;
import lombok.Setter;
import org.javatuples.Pair;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.samuelschwenn.labyrinth.game_app.util.SoundUtils.playMusic;
import static com.samuelschwenn.labyrinth.game_app.util.SoundUtils.stopMusic;

public class LogicRepresentation implements Serializable {
    private static LogicRepresentation _instance;

    private final GlobalUsageController globalUsageController = GlobalUsageController.getInstance();

    @Getter
    private SimpleWeightedGraph<CoordsInt, DefaultWeightedEdge> mapGraph;
    @Getter
    private final Map<CoordsInt, Building> buildings;
    @Getter
    private int width;
    @Getter
    private int height;
    @Getter
    @Setter
    private Basis basis;
    @Getter
    private final List<Monster> monsterList;
    @Getter
    private List<Monster> monstersToSpawn;
    @Getter
    private Level level;

    private LogicRepresentation() {
        buildings = new HashMap<>();
        monsterList = new CopyOnWriteArrayList<>();
    }

    public static LogicRepresentation getInstance() {
        if (_instance == null) {
            _instance = new LogicRepresentation();
        }
        return _instance;
    }

    public static void resetInstance(){
        _instance = null;
    }

    public static void setInstance(LogicRepresentation logicRepresentation) {
        _instance = logicRepresentation;
    }

    public void initializeWithLevel(Level level) {
        this.level = level;
        width = level.getWidth();
        height = level.getHeight();
        mapGraph = createMap(height, width);

        basis = level.getBasis();
        basis.setPosition(level.getBasisPosition());
        addBuilding(basis.getPosition(), basis);
        monstersToSpawn = level.getMonstersToSpawn();
    }

    public void addBuilding(CoordsInt coordsInt, Building building) {
        modifyBuildings(coordsInt, building, true);
    }

    public void removeBuilding(CoordsInt coordsInt) {
        modifyBuildings(coordsInt, null, false);
    }

    private void modifyBuildings(CoordsInt coordsInt, Building building, boolean shouldAdd) {
        if (shouldAdd == buildings.containsKey(coordsInt)) {
            return;
        }
        if (shouldAdd) {
            buildings.put(coordsInt, building);
        } else {
            buildings.remove(coordsInt);
        }
        updateMap();
    }

    public void updateMap() {
        for (DefaultWeightedEdge edge : mapGraph.edgeSet()) {
            mapGraph.setEdgeWeight(edge, 1);
        }
        for (CoordsInt building : buildings.keySet()) {
            for (DefaultWeightedEdge edge : mapGraph.edgesOf(building)) {
                mapGraph.setEdgeWeight(edge, 10000);
            }
        }
    }

    // Baut den Graphen einer leeren height*width großen LogicRepresentation
    private SimpleWeightedGraph<CoordsInt, DefaultWeightedEdge> createMap(int height, int width){
        //Neuer leerer Graph
        SimpleWeightedGraph<CoordsInt, DefaultWeightedEdge> graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);

        //Erstellung der Nodes
        for(int i = 0; i < height; i++){
            for(int j = 0; j < width; j++){
                CoordsInt newCoordsInt = new CoordsInt(j, i);
                graph.addVertex(newCoordsInt);
            }
        }

        //Erstellung der Edges
        for(int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if(i < height - 1){
                    DefaultWeightedEdge edge = graph.addEdge(new CoordsInt(j, i), new CoordsInt(j, i + 1));
                    graph.setEdgeWeight(edge, 1);
                }
                if(j < width - 1){
                    DefaultWeightedEdge edge = graph.addEdge(new CoordsInt(j, i), new CoordsInt(j + 1, i));
                    graph.setEdgeWeight(edge, 1);
                }
            }
        }

        //Rückgabe des Graphen
        return graph;
    }

    // Der Spieler hat gewonnen?
    public boolean playerWins() {
        return level.getMonstersToSpawn().isEmpty() && monsterList.isEmpty();
    }

    // Das Spiel ist vorbei?
    public boolean gameOver() {
        return playerWins() || basis.getHealth() <= 0;
    }

    // Es wird ein monster gespawnt
    public void spawnMonster() {
        Monster monster = createAndSetupMonster();
        if(monster.getClass().equals(Boss1.class)){
            stopMusic();
            playMusic(5);
        }
        monster.updateMonsterPath();
    }

    private Monster createAndSetupMonster() {
        Monster monster = monstersToSpawn.removeFirst();
        if(level.spawnAtPoint()) {
            monster.setPosition(level.getSpawnPoint());
        }else{
            Pair<CoordsInt, CoordsInt> specificSpawnArea = level.getSpawnArea().get(new Random().nextInt(level.getSpawnArea().size()));
            int x;
            int y;
            try {
                x = new Random().nextInt(specificSpawnArea.getValue0().x(), specificSpawnArea.getValue1().x());
            }catch(IllegalArgumentException e){
                x = specificSpawnArea.getValue1().x();
            }
            try {
                y = new Random().nextInt(specificSpawnArea.getValue0().y(), specificSpawnArea.getValue1().y());
            } catch(IllegalArgumentException e){
                y = specificSpawnArea.getValue0().y();
            }
            monster.setPosition(new CoordsInt(x, y));
        }
        monsterList.add(monster);
        return monster;
    }

    public void addToDrawablesAndTickables(){
        for(Building building : buildings.values()){
            if(building.getClass().equals(DefaultTower.class)||building.getClass().equals(Sniper.class) || building.getClass().equals(Minigun.class)){
                globalUsageController.registerTickable((Tickable) building);
            }
            globalUsageController.registerDrawable(building);
        }
        for(Monster monster : monsterList){
            globalUsageController.registerDrawable(monster);
            globalUsageController.registerTickable(monster);
        }
        for(Monster monster : monstersToSpawn){
            globalUsageController.registerDrawable(monster);
            globalUsageController.registerTickable(monster);
        }
    }

    public Building getBuilding(CoordsInt position) {
        return buildings.getOrDefault(position, null);
    }
}
