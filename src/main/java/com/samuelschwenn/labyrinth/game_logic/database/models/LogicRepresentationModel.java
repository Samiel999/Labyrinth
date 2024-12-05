package com.samuelschwenn.labyrinth.game_logic.database.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "logic_representation")
public class LogicRepresentationModel {
    @Id
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "logic_representation_id")
    private List<BuildingModel> buildings;

    @Column(name = "width")
    private int width;

    @Column(name = "height")
    private int height;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "monster_list_id", referencedColumnName = "id")
    private MonsterListModel monsterList;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "monsters_to_spawn_id", referencedColumnName = "id")
    private MonstersToSpawnModel monstersToSpawn;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "level_id", referencedColumnName = "id")
    private LevelModel level;
}
