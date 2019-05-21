package exercise;

import org.jgrapht.Graph;
import util.Algorithm;
import util.ADGraph;
import java.util.ArrayList;

public class Dijkstra extends Algorithm {
    public static class Weight {
        double weight = 0.0f;

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }
    }

	public<V extends DijkstraExercise.Vertex, E extends DijkstraExercise.Edge> long call(Graph G, V s, V t, MyPriorityQueue Q, ArrayList<V> p, Weight weight) {
        ADGraph adG = new ADGraph(G);
        ArrayList<Integer> path = new ArrayList<>();

        long startTime = System.nanoTime();

        weight.setWeight(E2.shortestPaths(adG, s.getM_id(), t.getM_id(), path, Q));

        long elapsedTime = System.nanoTime() - startTime;

        for(Integer v : path) {
            V vertex = (V) adG.getVertex(v);
            p.add(vertex);
        }

        return elapsedTime;
	}
	
	@Override
	protected String name() {
		return "Dijkstra";
	}
}
