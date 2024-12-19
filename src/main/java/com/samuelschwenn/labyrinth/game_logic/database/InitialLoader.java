package com.samuelschwenn.labyrinth.game_logic.database;

import com.samuelschwenn.labyrinth.game_logic.database.models.*;
import com.samuelschwenn.labyrinth.game_logic.database.repository.final_repositories.GameFileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitialLoader implements CommandLineRunner {
    private final GameFileRepository gameFileRepository;

    public InitialLoader(GameFileRepository gameFileRepository) {
        this.gameFileRepository = gameFileRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        GameFileEntity gameFileEntity1 = createGameFile(1);
        GameFileEntity gameFileEntity2 = createGameFile(2);
        GameFileEntity gameFileEntity3 = createGameFile(3);
        gameFileRepository.save(gameFileEntity1);
        gameFileRepository.save(gameFileEntity2);
        gameFileRepository.save(gameFileEntity3);
    }

    private GameFileEntity createGameFile(long id) {
        LevelEntity levelEntity = new LevelEntity();
        levelEntity.setId(id);
        levelEntity.setLevel_number(0);
        MonsterListEntity monsterListEntity = new MonsterListEntity();
        monsterListEntity.setId(id);
        MonstersToSpawnEntity monstersToSpawnEntity = new MonstersToSpawnEntity();
        monstersToSpawnEntity.setId(id);
        LogicRepresentationEntity logicRepresentationEntity = new LogicRepresentationEntity();
        logicRepresentationEntity.setId(id);
        logicRepresentationEntity.setWidth(0);
        logicRepresentationEntity.setHeight(0);
        logicRepresentationEntity.setMonstersToSpawn(monstersToSpawnEntity);
        logicRepresentationEntity.setMonsterList(monsterListEntity);
        logicRepresentationEntity.setLevel(levelEntity);
        GameFileEntity gameFileEntity = new GameFileEntity();
        gameFileEntity.setSaveId(id);
        gameFileEntity.setLogicRepresentation(logicRepresentationEntity);
        return gameFileEntity;
    }
}
