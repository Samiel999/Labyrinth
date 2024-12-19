package com.samuelschwenn.labyrinth.game_logic.database.repository.final_repositories;

import com.samuelschwenn.labyrinth.game_logic.database.models.GameFileEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameFileRepository extends CrudRepository<GameFileEntity, Long>, CustomizedGameFileRepository {
}
