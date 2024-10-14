package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.ObjectType;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.Objekt;
import com.samuelschwenn.game_logic.draw_and_tickables.Tickable;
import com.samuelschwenn.game_logic.LogicRepresentation;
import com.samuelschwenn.game_logic.util.CoordsDouble;
import com.samuelschwenn.game_logic.util.CoordsInt;
import com.samuelschwenn.game_logic.util.Direction;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.Serializable;
import java.util.List;

import static com.samuelschwenn.Main.loop;
import static com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.ObjectType.DefaultBasis;
import static com.samuelschwenn.game_logic.util.LoopType.game_over;
import static com.samuelschwenn.game_app.util.SoundUtils.playSFX;

@NoArgsConstructor
public abstract class Monster extends Objekt implements Tickable, Serializable {
    protected float movingSpeed;
    @SuppressWarnings("CanBeFinal")
    protected int attackSpeed;
    @Getter
    protected int stepsToGoal;
    protected double bounty;
    protected double monsterPathWeight;
    protected List<CoordsInt> monsterPathNodes;
    @Getter
    protected CoordsDouble drawnPosition;
    protected double attackCooldown = attackSpeed;
    protected boolean flying;

    public Monster(int strength, int health, CoordsInt position, float movingSpeed, int attackSpeed, double bounty, ObjectType type, boolean flying){
        super(strength, health, position, type);
        this.attackSpeed = attackSpeed;
        this.movingSpeed = movingSpeed;
        this.bounty = bounty;
        stepsToGoal = 250;
        this.drawnPosition = position.toCoordsDouble();
        this.flying = flying;
        loop.registerTickable(this);
    }

    public void updateFlyingMonsterPath(LogicRepresentation logicRepresentation) {
        SimpleWeightedGraph<CoordsInt, DefaultWeightedEdge> graph = logicRepresentation.getMapGraph();
        for (DefaultWeightedEdge edge : graph.edgeSet()){
            graph.setEdgeWeight(edge, 1);
        }
        CoordsInt positionBasis = logicRepresentation.getBasis().getPosition();
        for (DefaultWeightedEdge edge : graph.edgesOf(positionBasis)){
            graph.setEdgeWeight(edge, 10000);
        }
        DijkstraShortestPath<CoordsInt, DefaultWeightedEdge> pathfinder = new DijkstraShortestPath<>(graph);
        GraphPath<CoordsInt, DefaultWeightedEdge> monsterPath = pathfinder.getPath(position, logicRepresentation.getBasis().getPosition());
        monsterPathWeight = monsterPath.getWeight();
        monsterPathNodes = monsterPath.getVertexList();
    }

    public void updateWalkingMonsterPath(LogicRepresentation logicRepresentation) {
        DijkstraShortestPath<CoordsInt, DefaultWeightedEdge> pathfinder = new DijkstraShortestPath<>(logicRepresentation.getMapGraph());
        GraphPath<CoordsInt, DefaultWeightedEdge> monsterPath = pathfinder.getPath(position, logicRepresentation.getBasis().getPosition());
        monsterPathWeight = monsterPath.getWeight();
        monsterPathNodes = monsterPath.getVertexList();
    }

    public void tick(double timeDelta, LogicRepresentation logicRepresentation) {
        if(position.equals(new CoordsInt(-1, -1))) return;
        moveTowardsPosition(timeDelta, logicRepresentation);
    }

    public void moveTowardsPosition(double timeDelta, LogicRepresentation logicRepresentation) {
        Direction directionToMove = drawnPosition.getDirectionTo(position.toCoordsDouble());

        if (directionToMove == null) {
            makeMove(timeDelta, logicRepresentation);
            return;
        }

        CoordsDouble neededShiftage = position.toCoordsDouble().add(drawnPosition.scale(-1));
        CoordsDouble moved = CoordsDouble.getNormalized(directionToMove).scale((float) timeDelta * movingSpeed);
        CoordsDouble leftShiftage = neededShiftage.add(moved.scale(-1));
        if (CoordsDouble.getNormalized(directionToMove).minCoords(new CoordsDouble(0, 0)).equals(new CoordsDouble(0, 0))) {
            if (leftShiftage.maxCoords(new CoordsDouble(0, 0)).equals(new CoordsDouble(0, 0))) {
                this.drawnPosition = position.toCoordsDouble();
                return;
            }
        } else {
            if (leftShiftage.minCoords(new CoordsDouble(0, 0)).equals(new CoordsDouble(0, 0))) {
                this.drawnPosition = position.toCoordsDouble();
                return;
            }
        }

        this.drawnPosition = drawnPosition.add(moved);
    }

    public void makeMove(double timeDelta, LogicRepresentation logicRepresentation) {
        CoordsInt nextPosition = monsterPathNodes.get(1);
        if (!flying) {
            if (attackingWalking(logicRepresentation)) {
                attack(timeDelta, logicRepresentation.getBuildings().get(nextPosition));
            } else {
                stepsToGoal = monsterPathNodes.size() - 2;
                position = nextPosition;
            }
        } else {
            if (attackingFlying(logicRepresentation)) {
                attack(timeDelta, logicRepresentation.getBuildings().get(nextPosition));
            } else {
                stepsToGoal = monsterPathNodes.size() - 2;
                position = nextPosition;
            }
        }
    }

    public boolean attackingWalking(LogicRepresentation logicRepresentation) {
        return monsterPathWeight >= 10000 && logicRepresentation.getBuildings().containsKey(monsterPathNodes.get(1));
    }

    public boolean attackingFlying(LogicRepresentation logicRepresentation) {
        return monsterPathNodes.get(1).equals(logicRepresentation.getBasis().getPosition());
    }

    public abstract void updateMonsterPath(LogicRepresentation logicRepresentation);

    public void attack(double timeDelta, Objekt object) {
        attackCooldown -= timeDelta;
        if(attackCooldown <= 0) {
            object.setHealth(object.getHealth() - strength);
            if (object.getType().equals(DefaultBasis)) {
                playSFX(10);
            }
            attackCooldown = attackSpeed;
        }
    }

    public Direction getDirection(){
        CoordsDouble current_position = (monsterPathNodes.get(1).equals(position) ? position : monsterPathNodes.get(1)).toCoordsDouble();
        Direction direction = current_position.getDirectionTo(drawnPosition);
        if(direction == null){
            monsterPathNodes.remove(1);
            return getDirection();
        }
        return direction;
    }

    public float getOpacity() {
        return 1;
    }

//    public boolean hasArrived(){
//        return position.equals(drawnPosition);
//    }

    @Override
    public void die() {
        super.die();
        loop.unregisterTickable(this);
        loop.setMoney(loop.getMoney() + bounty);
        if (type.equals(ObjectType.Boss1)) {
            playSFX(9);
        } else playSFX(1);

        if (loop.getLogic_representation().getMonsterList().isEmpty() && loop.getLogic_representation().getLevel().getMonstersToSpawn().isEmpty()) {
            loop.update(game_over);
        }
    }

    @Override
    public void setPosition(CoordsInt position) {
        super.setPosition(position);
        this.drawnPosition = position.toCoordsDouble();
    }
}
