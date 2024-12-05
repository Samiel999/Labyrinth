package com.samuelschwenn.labyrinth.game_logic.database.repository;

import com.samuelschwenn.labyrinth.game_logic.database.models.MonsterListModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MonsterListRepository extends JpaRepository<MonsterListModel, Long> {
}
