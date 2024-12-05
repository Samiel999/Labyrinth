package com.samuelschwenn.labyrinth.game_logic.database.repository.final_repositories;

import com.samuelschwenn.labyrinth.game_logic.database.models.GameFile;

import java.util.Optional;

public interface CustomizedGameFileRepository {
    <S extends GameFile> S saveToFileById(long id);
    void loadGameFile(long id);
}
