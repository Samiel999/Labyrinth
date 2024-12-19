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
    public <S extends GameFileEntity> S saveToFileById(long id) {
        LogicRepresentation logicRepresentation = LogicRepresentation.getInstance();

        List<BuildingEntity> buildingEntityList = saveBuildingsAndGetModels(logicRepresentation);

        LevelEntity levelEntity = saveLevelAndGetModel(logicRepresentation, id);
        MonsterListEntity monsterListEntity = saveMonsterListAndGetModel(logicRepresentation, id);
        MonstersToSpawnEntity monstersToSpawn = saveMonstersToSpawnAndGetModel(logicRepresentation, id);

        LogicRepresentationEntity logicRepresentationEntity = saveLogicRepresentationAndGetModel(buildingEntityList, logicRepresentation, monsterListEntity, monstersToSpawn, levelEntity, id);

        GameFileEntity gameFileEntity = saveAndReturnGameFile(logicRepresentationEntity, id);

        return (S) gameFileEntity;
    }

    @Override
    public void loadGameFile(long id) {
        GameFileEntity file = Optional.ofNullable(entityManager.find(GameFileEntity.class, id)).orElseThrow();

        selectLevel(file.getLogicRepresentation().getLevel().getLevel_number());

        LogicRepresentation logicRepresentation = LogicRepresentation.getInstance();

        loadBuildingList(file, logicRepresentation);

        loadMonsterList(file, logicRepresentation);
        loadMonstersToSpawn(file, logicRepresentation);

        LogicRepresentation.setInstance(logicRepresentation);
    }

    private void loadBuildingList(GameFileEntity file, LogicRepresentation logicRepresentation) {
        List<Building> buildingList = new ArrayList<>();

        for(BuildingEntity buildingEntity : file.getLogicRepresentation().getBuildings()){
            CoordsInt position = new CoordsInt(buildingEntity.getPosition_x(), buildingEntity.getPosition_y());
            try {
                Class<? extends Building> buildingClass = (Class<? extends Building>) Class.forName(buildingEntity.getType());
                Building building = (Building) Building.instantiate(buildingClass, position);
                building.setHealth(buildingEntity.getHealth());
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

    private void loadMonstersToSpawn(GameFileEntity file, LogicRepresentation logicRepresentation) {
        List<Monster> monstersToSpawn = new ArrayList<>();
        for(MonsterEntity monsterEntity : file.getLogicRepresentation().getMonstersToSpawn().getMonsters()) {
            monstersToSpawn.add(getMonsterFromModel(monsterEntity));
        }
        logicRepresentation.getMonstersToSpawn().clear();
        logicRepresentation.getMonstersToSpawn().addAll(monstersToSpawn);
    }

    private void loadMonsterList(GameFileEntity file, LogicRepresentation logicRepresentation) {
        List<Monster> monsterList = new ArrayList<>();
        for(MonsterEntity monsterEntity : file.getLogicRepresentation().getMonsterList().getMonsters()) {
            monsterList.add(getMonsterFromModel(monsterEntity));
        }
        logicRepresentation.getMonsterList().clear();
        logicRepresentation.getMonsterList().addAll(monsterList);
    }

    private Monster getMonsterFromModel(MonsterEntity monsterEntity) {
        CoordsInt position = new CoordsInt(monsterEntity.getPosition_x(), monsterEntity.getPosition_y());
        try {
            Class<? extends Monster> monsterClass = (Class<? extends Monster>) Class.forName(monsterEntity.getType());
            Monster monster = (Monster) Monster.instantiate(monsterClass, position);
            monster.setHealth(monsterEntity.getHealth());
            monster.updateMonsterPath();
            return monster;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private GameFileEntity saveAndReturnGameFile(LogicRepresentationEntity logicRepresentationEntity, long id) {
        GameFileEntity gameFileEntity = new GameFileEntity();
        gameFileEntity.setLogicRepresentation(logicRepresentationEntity);
        gameFileEntity.setSaveId(id);
        entityManager.merge(gameFileEntity);
        return gameFileEntity;
    }

    private LogicRepresentationEntity saveLogicRepresentationAndGetModel(List<BuildingEntity> buildingEntityList, LogicRepresentation logicRepresentation, MonsterListEntity monsterListEntity, MonstersToSpawnEntity monstersToSpawn, LevelEntity levelEntity, long id) {
        LogicRepresentationEntity logicRepresentationEntity = new LogicRepresentationEntity();
        logicRepresentationEntity.setBuildings(buildingEntityList);
        logicRepresentationEntity.setWidth(logicRepresentation.getWidth());
        logicRepresentationEntity.setHeight(logicRepresentation.getHeight());
        logicRepresentationEntity.setMonsterList(monsterListEntity);
        logicRepresentationEntity.setMonstersToSpawn(monstersToSpawn);
        logicRepresentationEntity.setLevel(levelEntity);
        logicRepresentationEntity.setId(id);
        logicRepresentationRepository.saveAndFlush(logicRepresentationEntity);
        return logicRepresentationEntity;
    }

    private MonstersToSpawnEntity saveMonstersToSpawnAndGetModel(LogicRepresentation logicRepresentation, long id) {
        MonstersToSpawnEntity monstersToSpawn = new MonstersToSpawnEntity();
        monstersToSpawn.setMonsters(getMonsterModels(logicRepresentation, false));
        monstersToSpawn.setId(id);
        monstersToSpawnRepository.saveAndFlush(monstersToSpawn);
        return monstersToSpawn;
    }

    private MonsterListEntity saveMonsterListAndGetModel(LogicRepresentation logicRepresentation, long id) {
        MonsterListEntity monsterListEntity = new MonsterListEntity();
        monsterListEntity.setMonsters(getMonsterModels(logicRepresentation, true));
        monsterListEntity.setId(id);
        monsterListRepository.saveAndFlush(monsterListEntity);
        return monsterListEntity;
    }

    private LevelEntity saveLevelAndGetModel(LogicRepresentation logicRepresentation, long id) {
        LevelEntity levelEntity = new LevelEntity();
        levelEntity.setLevel_number(Integer.parseInt(String.valueOf(logicRepresentation.getLevel().getClass().getSimpleName().charAt(5))));
        levelEntity.setId(id);
        levelRepository.saveAndFlush(levelEntity);
        return levelEntity;
    }

    private List<MonsterEntity> getMonsterModels(LogicRepresentation logicRepresentation, boolean list) {
        List<MonsterEntity> monsterEntities = new ArrayList<>();
        List<Monster> monsterList;
        if(list){
            monsterList = logicRepresentation.getMonsterList();
        }else {
            monsterList = logicRepresentation.getLevel().getMonstersToSpawn();
        }

        for(Monster monster : monsterList){
            MonsterEntity monsterEntity = new MonsterEntity();
            monsterEntity.setHealth(monster.getHealth());
            monsterEntity.setType(monster.getClass().getName());
            monsterEntity.setPosition_x(monster.getPosition().x());
            monsterEntity.setPosition_y(monster.getPosition().y());
            monsterEntities.add(monsterEntity);
            monsterRepository.saveAndFlush(monsterEntity);
        }
        return monsterEntities;
    }

    private List<BuildingEntity> saveBuildingsAndGetModels(LogicRepresentation logicRepresentation) {
        List<BuildingEntity> buildingEntityList = new ArrayList<>();

        for(Building building : logicRepresentation.getBuildings().values()){
            BuildingEntity buildingEntity = new BuildingEntity();
            buildingEntity.setHealth(building.getHealth());
            buildingEntity.setType(building.getClass().getName());
            buildingEntity.setPosition_x(building.getPosition().x());
            buildingEntity.setPosition_y(building.getPosition().y());
            buildingEntityList.add(buildingEntity);
            buildingRepository.saveAndFlush(buildingEntity);
        }
        return buildingEntityList;
    }
}
