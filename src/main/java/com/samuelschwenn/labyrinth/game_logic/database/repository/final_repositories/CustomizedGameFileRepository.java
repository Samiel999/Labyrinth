package com.samuelschwenn.labyrinth.game_logic.database.repository.final_repositories;

import com.samuelschwenn.labyrinth.game_logic.database.models.GameFileEntity;

public interface CustomizedGameFileRepository {
    <S extends GameFileEntity> S saveToFileById(long id);
    void loadGameFile(long id);
}
