package com.samuelschwenn.labyrinth.game_logic.database.repository;

import com.samuelschwenn.labyrinth.game_logic.database.models.LogicRepresentationModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogicRepresentationRepository extends JpaRepository<LogicRepresentationModel, Long> {
}
