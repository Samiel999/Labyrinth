package com.samuelschwenn.labyrinth.game_logic.database.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "monster_list")
public class MonsterListModel {
    @Id
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "monster_list_id")
    private List<MonsterModel> monsters;
}
