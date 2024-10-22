package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster;

import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.GameObject;
import com.samuelschwenn.game_logic.draw_and_tickables.Tickable;
import com.samuelschwenn.game_logic.LogicRepresentation;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.basis.DefaultBasis;
import com.samuelschwenn.game_logic.util.CoordsDouble;
import com.samuelschwenn.game_logic.util.CoordsInt;
import com.samuelschwenn.game_logic.util.Direction;
import lombok.Getter;
import lombok.Setter;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.io.Serializable;
import java.util.List;

import static com.samuelschwenn.Main.loop;
import static com.samuelschwenn.game_logic.util.LoopType.game_over;
import static com.samuelschwenn.game_app.util.SoundUtils.playSFX;

public abstract class Monster extends GameObject implements Tickable, Serializable {
    @Setter
    protected float movingSpeed;
    @Setter
    protected int attackSpeed;
    @Getter
    protected int stepsToGoal;
    @Setter
    protected double bounty;
    protected double monsterPathWeight;
    protected List<CoordsInt> monsterPathNodes;
    @Getter
    protected CoordsDouble drawnPosition;
    protected double attackCooldown = attackSpeed;
    @Setter
    protected boolean flying;

    public Monster(){
        super();
        stepsToGoal = 250;
        loop.registerTickable(this);
    }

    public void updateFlyingMonsterPath() {
        SimpleWeightedGraph<CoordsInt, DefaultWeightedEdge> graph = LogicRepresentation.getInstance().getMapGraph();
        for (DefaultWeightedEdge edge : graph.edgeSet()){
            graph.setEdgeWeight(edge, 1);
        }
        CoordsInt positionBasis = LogicRepresentation.getInstance().getBasis().getPosition();
        for (DefaultWeightedEdge edge : graph.edgesOf(positionBasis)){
            graph.setEdgeWeight(edge, 10000);
        }
        DijkstraShortestPath<CoordsInt, DefaultWeightedEdge> pathfinder = new DijkstraShortestPath<>(graph);
        GraphPath<CoordsInt, DefaultWeightedEdge> monsterPath = pathfinder.getPath(position, LogicRepresentation.getInstance().getBasis().getPosition());
        monsterPathWeight = monsterPath.getWeight();
        monsterPathNodes = monsterPath.getVertexList();
    }

    public void updateWalkingMonsterPath() {
        DijkstraShortestPath<CoordsInt, DefaultWeightedEdge> pathfinder = new DijkstraShortestPath<>(LogicRepresentation.getInstance().getMapGraph());
        GraphPath<CoordsInt, DefaultWeightedEdge> monsterPath = pathfinder.getPath(position, LogicRepresentation.getInstance().getBasis().getPosition());
        monsterPathWeight = monsterPath.getWeight();
        monsterPathNodes = monsterPath.getVertexList();
    }

    public void tick(double timeDelta) {
        if(position.equals(new CoordsInt(-1, -1))) return;
        moveTowardsPosition(timeDelta);
    }

    public void moveTowardsPosition(double timeDelta) {
        Direction directionToMove = drawnPosition.getDirectionTo(position.toCoordsDouble());

        if (directionToMove == null) {
            makeMove(timeDelta);
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

    public void makeMove(double timeDelta) {
        CoordsInt nextPosition = monsterPathNodes.get(1);
        if (!flying) {
            if (attackingWalking()) {
                attack(timeDelta, LogicRepresentation.getInstance().getBuildings().get(nextPosition));
            } else {
                stepsToGoal = monsterPathNodes.size() - 2;
                position = nextPosition;
            }
        } else {
            if (attackingFlying()) {
                attack(timeDelta, LogicRepresentation.getInstance().getBuildings().get(nextPosition));
            } else {
                stepsToGoal = monsterPathNodes.size() - 2;
                position = nextPosition;
            }
        }
    }

    public boolean attackingWalking() {
        return monsterPathWeight >= 10000 && LogicRepresentation.getInstance().getBuildings().containsKey(monsterPathNodes.get(1));
    }

    public boolean attackingFlying() {
        return monsterPathNodes.get(1).equals(LogicRepresentation.getInstance().getBasis().getPosition());
    }

    public abstract void updateMonsterPath();

    public void attack(double timeDelta, GameObject object) {
        attackCooldown -= timeDelta;
        if(attackCooldown <= 0) {
            object.setHealth(object.getHealth() - strength);
            if (object.getClass().equals(DefaultBasis.class)) {
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

    @Override
    public void die() {
        super.die();
        loop.unregisterTickable(this);
        loop.setMoney(loop.getMoney() + bounty);
        if (this.getClass().equals(Boss1.class)) {
            playSFX(9);
        } else{
            playSFX(1);
        }

        if (LogicRepresentation.getInstance().getMonsterList().isEmpty() && LogicRepresentation.getInstance().getLevel().getMonstersToSpawn().isEmpty()) {
            loop.update(game_over);
        }
    }

    @Override
    public void setPosition(CoordsInt position) {
        super.setPosition(position);
        this.drawnPosition = position.toCoordsDouble();
    }
}
