package com.samuelschwenn.labyrinth.game_logic.database.repository;

import com.samuelschwenn.labyrinth.game_logic.database.models.MonsterModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonsterRepository extends JpaRepository<MonsterModel, Integer> {
}
