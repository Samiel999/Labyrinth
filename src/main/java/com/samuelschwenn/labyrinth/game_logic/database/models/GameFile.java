package com.samuelschwenn.labyrinth.game_logic.database.models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "save")
public class GameFile {
    @Id
    private Long saveId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "logic_representation_id", referencedColumnName = "id")
    private LogicRepresentationModel logicRepresentation;

}
