package com.samuelschwenn.labyrinth.game_logic.database.repository;

import com.samuelschwenn.labyrinth.game_logic.database.models.BuildingModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingRepository extends JpaRepository<BuildingModel, Integer> {
}
