package com.samuelschwenn.labyrinth.game_logic.database.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "building")
public class BuildingModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "position_x")
    private int position_x;

    @Column(name = "position_y")
    private int position_y;

    @Column(name = "health")
    private int health;
}
