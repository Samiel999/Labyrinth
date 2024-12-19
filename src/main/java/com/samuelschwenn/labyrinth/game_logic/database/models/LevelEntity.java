package com.samuelschwenn.labyrinth.game_logic.database.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "level")
public class LevelEntity {
    @Id
    private Long id;

    @Column(name = "level_number")
    private int level_number;
}
