package com.samuelschwenn.labyrinth.game_logic.database.repository.final_repositories;

import com.samuelschwenn.labyrinth.game_logic.LogicRepresentation;
import com.samuelschwenn.labyrinth.game_logic.database.models.*;
import com.samuelschwenn.labyrinth.game_logic.database.repository.*;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.Building;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.basis.Basis;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster.Monster;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.java.Log;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.samuelschwenn.labyrinth.game_logic.util.SetupMethods.selectLevel;

@Component
public class CustomizedGameFileRepositoryImpl implements CustomizedGameFileRepository {
    @PersistenceContext
    EntityManager entityManager;
    private final BuildingRepository buildingRepository;
    private final LevelRepository levelRepository;
    private final LogicRepresentationRepository logicRepresentationRepository;
    private final MonsterListRepository monsterListRepository;
    private final MonsterRepository monsterRepository;
    private final MonstersToSpawnRepository monstersToSpawnRepository;

    CustomizedGameFileRepositoryImpl(BuildingRepository buildingRepository, LevelRepository levelRepository, LogicRepresentationRepository logicRepresentationRepository, MonsterListRepository monsterListRepository, MonsterRepository monsterRepository, MonstersToSpawnRepository monstersToSpawnRepository) {

        this.buildingRepository = buildingRepository;
        this.levelRepository = levelRepository;
        this.logicRepresentationRepository = logicRepresentationRepository;
        this.monsterListRepository = monsterListRepository;
        this.monsterRepository = monsterRepository;
        this.monstersToSpawnRepository = monstersToSpawnRepository;
    }

    @Override
    @Transactional
    public <S extends GameFile> S saveToFileById(long id) {
        LogicRepresentation logicRepresentation = LogicRepresentation.getInstance();

        List<BuildingModel> buildingModelList = saveBuildingsAndGetModels(logicRepresentation);

        LevelModel levelModel = saveLevelAndGetModel(logicRepresentation, id);
        MonsterListModel monsterListModel = saveMonsterListAndGetModel(logicRepresentation, id);
        MonstersToSpawnModel monstersToSpawn = saveMonstersToSpawnAndGetModel(logicRepresentation, id);

        LogicRepresentationModel logicRepresentationModel = saveLogicRepresentationAndGetModel(buildingModelList, logicRepresentation, monsterListModel, monstersToSpawn, levelModel, id);

        GameFile gameFile = saveAndReturnGameFile(logicRepresentationModel, id);

        return (S) gameFile;
    }

    @Override
    public void loadGameFile(long id) {
        GameFile file = Optional.ofNullable(entityManager.find(GameFile.class, id)).orElseThrow();

        selectLevel(file.getLogicRepresentation().getLevel().getLevel_number());

        LogicRepresentation logicRepresentation = LogicRepresentation.getInstance();

        loadBuildingList(file, logicRepresentation);

        loadMonsterList(file, logicRepresentation);
        loadMonstersToSpawn(file, logicRepresentation);

        LogicRepresentation.setInstance(logicRepresentation);
    }

    private void loadBuildingList(GameFile file, LogicRepresentation logicRepresentation) {
        List<Building> buildingList = new ArrayList<>();

        for(BuildingModel buildingModel : file.getLogicRepresentation().getBuildings()){
            CoordsInt position = new CoordsInt(buildingModel.getPosition_x(), buildingModel.getPosition_y());
            try {
                Class<? extends Building> buildingClass = (Class<? extends Building>) Class.forName(buildingModel.getType());
                Building building = (Building) Building.instantiate(buildingClass, position);
                building.setHealth(buildingModel.getHealth());
                buildingList.add(building);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        logicRepresentation.getBuildings().clear();
        for(Building building : buildingList) {
            if(building.getClass().getSuperclass() == Basis.class) {
                logicRepresentation.setBasis((Basis) building);
            }
            logicRepresentation.addBuilding(building.getPosition(), building);
        }
    }

    private void loadMonstersToSpawn(GameFile file, LogicRepresentation logicRepresentation) {
        List<Monster> monstersToSpawn = new ArrayList<>();
        for(MonsterModel monsterModel : file.getLogicRepresentation().getMonstersToSpawn().getMonsters()) {
            monstersToSpawn.add(getMonsterFromModel(monsterModel));
        }
        logicRepresentation.getMonstersToSpawn().clear();
        logicRepresentation.getMonstersToSpawn().addAll(monstersToSpawn);
    }

    private void loadMonsterList(GameFile file, LogicRepresentation logicRepresentation) {
        List<Monster> monsterList = new ArrayList<>();
        for(MonsterModel monsterModel : file.getLogicRepresentation().getMonsterList().getMonsters()) {
            monsterList.add(getMonsterFromModel(monsterModel));
        }
        logicRepresentation.getMonsterList().clear();
        logicRepresentation.getMonsterList().addAll(monsterList);
    }

    private Monster getMonsterFromModel(MonsterModel monsterModel) {
        CoordsInt position = new CoordsInt(monsterModel.getPosition_x(), monsterModel.getPosition_y());
        try {
            Class<? extends Monster> monsterClass = (Class<? extends Monster>) Class.forName(monsterModel.getType());
            Monster monster = (Monster) Monster.instantiate(monsterClass, position);
            monster.setHealth(monsterModel.getHealth());
            monster.updateMonsterPath();
            return monster;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private GameFile saveAndReturnGameFile(LogicRepresentationModel logicRepresentationModel, long id) {
        GameFile gameFile = new GameFile();
        gameFile.setLogicRepresentation(logicRepresentationModel);
        gameFile.setSaveId(id);
        entityManager.merge(gameFile);
        return gameFile;
    }

    private LogicRepresentationModel saveLogicRepresentationAndGetModel(List<BuildingModel> buildingModelList, LogicRepresentation logicRepresentation, MonsterListModel monsterListModel, MonstersToSpawnModel monstersToSpawn, LevelModel levelModel, long id) {
        LogicRepresentationModel logicRepresentationModel = new LogicRepresentationModel();
        logicRepresentationModel.setBuildings(buildingModelList);
        logicRepresentationModel.setWidth(logicRepresentation.getWidth());
        logicRepresentationModel.setHeight(logicRepresentation.getHeight());
        logicRepresentationModel.setMonsterList(monsterListModel);
        logicRepresentationModel.setMonstersToSpawn(monstersToSpawn);
        logicRepresentationModel.setLevel(levelModel);
        logicRepresentationModel.setId(id);
        logicRepresentationRepository.saveAndFlush(logicRepresentationModel);
        return logicRepresentationModel;
    }

    private MonstersToSpawnModel saveMonstersToSpawnAndGetModel(LogicRepresentation logicRepresentation, long id) {
        MonstersToSpawnModel monstersToSpawn = new MonstersToSpawnModel();
        monstersToSpawn.setMonsters(getMonsterModels(logicRepresentation, false));
        monstersToSpawn.setId(id);
        monstersToSpawnRepository.saveAndFlush(monstersToSpawn);
        return monstersToSpawn;
    }

    private MonsterListModel saveMonsterListAndGetModel(LogicRepresentation logicRepresentation, long id) {
        MonsterListModel monsterListModel = new MonsterListModel();
        monsterListModel.setMonsters(getMonsterModels(logicRepresentation, true));
        monsterListModel.setId(id);
        monsterListRepository.saveAndFlush(monsterListModel);
        return monsterListModel;
    }

    private LevelModel saveLevelAndGetModel(LogicRepresentation logicRepresentation, long id) {
        LevelModel levelModel = new LevelModel();
        levelModel.setLevel_number(Integer.parseInt(String.valueOf(logicRepresentation.getLevel().getClass().getSimpleName().charAt(5))));
        levelModel.setId(id);
        levelRepository.saveAndFlush(levelModel);
        return levelModel;
    }

    private List<MonsterModel> getMonsterModels(LogicRepresentation logicRepresentation, boolean list) {
        List<MonsterModel> monsterModels = new ArrayList<>();
        List<Monster> monsterList;
        if(list){
            monsterList = logicRepresentation.getMonsterList();
        }else {
            monsterList = logicRepresentation.getLevel().getMonstersToSpawn();
        }

        for(Monster monster : monsterList){
            MonsterModel monsterModel = new MonsterModel();
            monsterModel.setHealth(monster.getHealth());
            monsterModel.setType(monster.getClass().getName());
            monsterModel.setPosition_x(monster.getPosition().x());
            monsterModel.setPosition_y(monster.getPosition().y());
            monsterModels.add(monsterModel);
            monsterRepository.saveAndFlush(monsterModel);
        }
        return monsterModels;
    }

    private List<BuildingModel> saveBuildingsAndGetModels(LogicRepresentation logicRepresentation) {
        List<BuildingModel> buildingModelList = new ArrayList<>();

        for(Building building : logicRepresentation.getBuildings().values()){
            BuildingModel buildingModel = new BuildingModel();
            buildingModel.setHealth(building.getHealth());
            buildingModel.setType(building.getClass().getName());
            buildingModel.setPosition_x(building.getPosition().x());
            buildingModel.setPosition_y(building.getPosition().y());
            buildingModelList.add(buildingModel);
            buildingRepository.saveAndFlush(buildingModel);
        }
        return buildingModelList;
    }
}
