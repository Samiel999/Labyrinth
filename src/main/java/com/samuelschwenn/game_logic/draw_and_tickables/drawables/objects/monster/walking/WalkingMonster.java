package com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.walking;

import com.samuelschwenn.game_logic.LogicRepresentation;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.buildings.Building;
import com.samuelschwenn.game_logic.draw_and_tickables.drawables.objects.monster.Monster;
import com.samuelschwenn.game_logic.util.CoordsInt;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.Map;

public abstract class WalkingMonster extends Monster {
    @Override
    public void updateMonsterPath() {
        SimpleWeightedGraph<CoordsInt, DefaultWeightedEdge> graph = LogicRepresentation.getInstance().getMapGraph();
        CoordsInt positionBasis = LogicRepresentation.getInstance().getBasis().getPosition();

        DijkstraShortestPath<CoordsInt, DefaultWeightedEdge> pathfinder = new DijkstraShortestPath<>(graph);
        GraphPath<CoordsInt, DefaultWeightedEdge> path = pathfinder.getPath(position, positionBasis);
        pathWeight = path.getWeight();
        pathNodes = path.getVertexList();
    }

    @Override
    public void makeMoveOrAttack() {
        Map<CoordsInt, Building> buildings = LogicRepresentation.getInstance().getBuildings();
        CoordsInt nextPosition = pathNodes.get(1);

        if (buildingIsInTheWay(buildings, nextPosition)) {
            attack(timeDelta, buildings.get(nextPosition));
        } else {
            stepsToGoal = pathNodes.size() - 2;
            position = nextPosition;
        }
    }

    private boolean buildingIsInTheWay(Map<CoordsInt, Building> buildings, CoordsInt nextPosition) {
        return pathWeight >= 10000 && buildings.containsKey(nextPosition);
    }
}
