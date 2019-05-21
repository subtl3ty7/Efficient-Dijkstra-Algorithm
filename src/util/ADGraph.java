package util;

import exercise.DijkstraExercise;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class ADGraph<V extends DijkstraExercise.Vertex, E extends DijkstraExercise.Edge> {
    public Graph<V, E> m_g;
    HashMap<Integer, V> idToVertex;

    public ADGraph(Graph<V, E> G) {
        m_g = G;
        idToVertex = new HashMap<>();
        for(V v : m_g.vertexSet())
            idToVertex.put(v.getM_id(), v);
    }

    public ArrayList<Integer> outNeighbors(int v) {
        ArrayList<Integer> outNeighbors = new ArrayList<>();
        V vertex = idToVertex.get(v);
        for(E e : m_g.outgoingEdgesOf(vertex))
            outNeighbors.add(Graphs.getOppositeVertex(m_g, e, vertex).getM_id());

        return outNeighbors;
    }

    public Double weight(int u, int v) {
        Set<E> multiEdges = m_g.getAllEdges(idToVertex.get(u), idToVertex.get(v));
        Double min = Double.MAX_VALUE;
        for(E e : multiEdges)
            if(Double.compare(m_g.getEdgeWeight(e), min) < 0)
                min = m_g.getEdgeWeight(e);

        return min;
    }

    public Integer numVertices() {
        return m_g.vertexSet().size();
    }

    public V getVertex(int id) {return idToVertex.get(id);}
}
