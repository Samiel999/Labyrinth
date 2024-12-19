package com.samuelschwenn.labyrinth.game_logic.database.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "monster")
public class MonsterEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "health")
    private int health;

    @Column(name = "position_x")
    private int position_x;

    @Column(name = "position_y")
    private int position_y;

    @Column(name = "type")
    private String type;
}
