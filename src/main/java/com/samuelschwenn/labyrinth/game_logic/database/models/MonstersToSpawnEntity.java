package com.samuelschwenn.labyrinth.game_logic.database.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "monsters_to_spawn")
public class MonstersToSpawnEntity {
    @Id
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "monsters_to_spawn_id")
    private List<MonsterEntity> monsters;
}


