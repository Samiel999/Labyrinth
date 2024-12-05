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
        GameFile gameFile1 = createGameFile(1);
        GameFile gameFile2 = createGameFile(2);
        GameFile gameFile3 = createGameFile(3);
        gameFileRepository.save(gameFile1);
        gameFileRepository.save(gameFile2);
        gameFileRepository.save(gameFile3);
    }

    private GameFile createGameFile(long id) {
        LevelModel levelModel = new LevelModel();
        levelModel.setId(id);
        levelModel.setLevel_number(0);
        MonsterListModel monsterListModel = new MonsterListModel();
        monsterListModel.setId(id);
        MonstersToSpawnModel monstersToSpawnModel = new MonstersToSpawnModel();
        monstersToSpawnModel.setId(id);
        LogicRepresentationModel logicRepresentationModel = new LogicRepresentationModel();
        logicRepresentationModel.setId(id);
        logicRepresentationModel.setWidth(0);
        logicRepresentationModel.setHeight(0);
        logicRepresentationModel.setMonstersToSpawn(monstersToSpawnModel);
        logicRepresentationModel.setMonsterList(monsterListModel);
        logicRepresentationModel.setLevel(levelModel);
        GameFile gameFile = new GameFile();
        gameFile.setSaveId(id);
        gameFile.setLogicRepresentation(logicRepresentationModel);
        return gameFile;
    }
}
