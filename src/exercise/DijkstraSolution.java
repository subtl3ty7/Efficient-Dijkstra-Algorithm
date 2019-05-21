package exercise;

import org.jgrapht.GraphPath;
import util.Solution;
import java.util.HashMap;

public class DijkstraSolution<V extends DijkstraExercise.Vertex, E extends DijkstraExercise.Edge> extends Solution<GraphPath<V,E>> {
    @Override
	public boolean check(GraphPath<V,E> values, GraphPath<V,E> solution) {
        if (values == null && solution == null)
            return true;

        for (int i = 0; i < values.getVertexList().size(); ++i)
            if (values.getVertexList().get(i).getM_id().compareTo(solution.getVertexList().get(i).getM_id()) != 0)
                if (Math.abs((values.getWeight() - solution.getWeight())) > 0.01)
                    return false;

        return true;
	}

	@Override
	public String header() {
		String head = super.header();
		head += delim + "length" + delim + "path";
		return head;
	}
	
	@Override
	public String line(Integer i) {
		String l = super.line(i);
		l += delim;
		l += solutions.get(i).getLength();
		l += delim;
		l += pathToString(solutions.get(i));

		return l;
	}

    @Override
    public String solutionToString(int id) {
        String solution = "";

        for(int i = 0; i < solutions.get(id).getVertexList().size(); ++i) {
            V v = solutions.get(id).getVertexList().get(i);
            solution += v.toString() + ", ";
        }

        return solution;
    }

    private String getLatLonPair(HashMap<String, Float> c) {return "{\'lat\' : " + c.get("lat") + ",\'lng\' : " + c.get("lon") + "},";}

    private String pathToString(GraphPath<V,E> p) {
        String pString = "\"" + "{\'p\' : [";

        for(V v : p.getVertexList())
            pString += v.toString() + delim;

        pString = pString.substring(0, pString.length() - 1);

        return pString + "]}" + "\"";
    }
}
