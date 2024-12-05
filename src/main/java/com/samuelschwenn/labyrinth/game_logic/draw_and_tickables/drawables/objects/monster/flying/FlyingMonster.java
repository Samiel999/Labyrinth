package com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster.flying;

import com.samuelschwenn.labyrinth.game_logic.LogicRepresentation;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.buildings.basis.Basis;
import com.samuelschwenn.labyrinth.game_logic.draw_and_tickables.drawables.objects.monster.Monster;
import com.samuelschwenn.labyrinth.game_logic.util.CoordsInt;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

public abstract class FlyingMonster extends Monster {
    @Override
    public void updateMonsterPath() {
        SimpleWeightedGraph<CoordsInt, DefaultWeightedEdge> graph = getGraphWithoutBuildings();

        DijkstraShortestPath<CoordsInt, DefaultWeightedEdge> pathfinder = new DijkstraShortestPath<>(graph);
        GraphPath<CoordsInt, DefaultWeightedEdge> monsterPath = pathfinder.getPath(position, LogicRepresentation.getInstance().getBasis().getPosition());
        pathWeight = monsterPath.getWeight();
        pathNodes = monsterPath.getVertexList();
    }

    private SimpleWeightedGraph<CoordsInt, DefaultWeightedEdge> getGraphWithoutBuildings() {
        SimpleWeightedGraph<CoordsInt, DefaultWeightedEdge> graph = LogicRepresentation.getInstance().getMapGraph();
        for (DefaultWeightedEdge edge : graph.edgeSet()) {
            graph.setEdgeWeight(edge, 1);
        }
        return graph;
    }

    @Override
    public void makeMoveOrAttack() {
        Basis basis = LogicRepresentation.getInstance().getBasis();
        if (pathNodes.get(1).equals(basis.getPosition())) {
            attack(timeDelta, basis);
        } else {
            stepsToGoal = pathNodes.size() - 2;
            position = pathNodes.get(1);
        }
    }
}
