package exercise;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.*;
import org.jgrapht.io.*;
import util.Algorithm;
import util.Exercise;
import util.Solution;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class DijkstraExercise extends Exercise {
    public static class Vertex {
        Integer m_id;

        public Vertex(Integer id) {
            m_id = id;
        }

        public Integer getM_id() {
            return m_id;
        }

        public void setM_id(Integer m_id) {
            this.m_id = m_id;
        }

        public String toString() {
            return "" + getLabel();
        }

        public Integer getLabel() {
            return getM_id() + 1;
        }
    }

    public static class Edge {
        Vertex m_from;
        Vertex m_to;
        Double m_length;

        public Edge() {
            m_from = null;
            m_to = null;
            m_length = 0.0;
        }

        public Edge(Vertex from, Vertex to, Double length) {
            m_from = from;
            m_to = to;
            m_length = length;
        }

        public Double getM_length() {
            return m_length;
        }

        public void setM_length(Double m_length) {
            this.m_length = m_length;
        }


        public Vertex getM_from() {
            return m_from;
        }

        public void setM_from(Vertex m_from) {
            this.m_from = m_from;
        }

        public Vertex getM_to() {
            return m_to;
        }

        public void setM_to(Vertex m_to) {
            this.m_to = m_to;
        }

        public String toString() {
            return "(" + m_from.getLabel() + "," + m_to.getLabel() + "," + getM_length() + ")";
        }
    }

    public static class OSMVertex extends Vertex {
        Long m_osmId;
        Float m_long;
        Float m_lat;

        public OSMVertex(Integer id, Long osmId, Float x, Float y) {
            super(id);
            m_osmId = osmId;
            m_long = x;
            m_lat = y;
        }

        public Long getM_osmId() {
            return m_osmId;
        }

        public void setM_osmId(Long m_osmId) {
            this.m_osmId = m_osmId;
        }

        public Float getM_long() {
            return m_long;
        }

        public void setM_long(Float m_long) {
            this.m_long = m_long;
        }

        public Float getM_lat() {
            return m_lat;
        }

        public void setM_lat(Float m_lat) {
            this.m_lat = m_lat;
        }

        @Override
        public Integer getLabel() {
            return getM_id();
        }

        @Override
        public String toString() {
            return "{\'lat\' : " + getM_lat() + ",\'lng\' : " + getM_long() + "}";
        }
    }

    public static class OSMEdge extends Edge {
        Integer m_maxspeed;
        Boolean m_oneway;
        ArrayList<Float> m_lats = new ArrayList<>();
        ArrayList<Float> m_lons = new ArrayList<>();

        public OSMEdge(OSMVertex from, OSMVertex to, Double length, Integer maxspeed, boolean oneway) {
            super(from, to, length);
            m_maxspeed = maxspeed;
            m_oneway = m_oneway;
            m_lats.add(from.getM_lat());
            m_lats.add(to.getM_lat());
            m_lons.add(from.getM_long());
            m_lons.add(to.getM_long());
        }

        public Integer getM_maxspeed() {
            return m_maxspeed;
        }

        public void setM_maxspeed(Integer m_maxspeed) {
            this.m_maxspeed = m_maxspeed;
        }

        public Boolean isM_oneway() {
            return m_oneway;
        }

        public void setM_oneway(Boolean m_oneway) {
            this.m_oneway = m_oneway;
        }

        public void addPoint(Float lat, Float lon) {
            m_lats.add(lat);
            m_lons.add(lon);
        }

        public ArrayList<HashMap<String, Float>> coordinates() {
            ArrayList<HashMap<String, Float>> cs = new ArrayList<>();

            for(int i = 0; i < m_lons.size(); ++i) {
                HashMap<String, Float> latLonPair = new HashMap<>();
                latLonPair.put("lat", m_lats.get(i));
                latLonPair.put("lon", m_lons.get(i));
                cs.add(latLonPair);
            }

            return cs;
        }
    }

    private static GraphImporter<OSMVertex, OSMEdge> createOSMImporter() {
        VertexProvider<OSMVertex> vertexProvider = (id, attributes) -> {
            return new OSMVertex(
                    -1,
                    Long.parseLong(attributes.get("osmid").getValue()),
                    Float.parseFloat(attributes.get("x").getValue()),
                    Float.parseFloat(attributes.get("y").getValue())
            );
        };

        EdgeProvider<OSMVertex, OSMEdge> edgeProvider = (from, to, label, attributes) -> {
            Integer maxspeed = 0;
            if(!attributes.containsKey("maxspeed"))
                maxspeed = 30;
            else {
                String maxspeedS = attributes.get("maxspeed").getValue();
                try {
                    maxspeed = Integer.parseInt(maxspeedS);
                } catch (NumberFormatException e) {
                    Pattern pattern = Pattern.compile("[0-9]+");
                    Matcher matcher = pattern.matcher(maxspeedS);
                    if(matcher.find())
                        maxspeed = Integer.parseInt(matcher.group(0));
                    else
                        maxspeed = 7;
                }
            }

            OSMEdge e = new OSMEdge(from, to,
                    Double.parseDouble(attributes.get("length").getValue()),
                    maxspeed,
                    Boolean.parseBoolean(attributes.get("oneway").getValue())
            );

            if(attributes.containsKey("geometry")) {
                String csAttr = attributes.get("geometry").getValue();
                csAttr = csAttr.replace("LINESTRING (", "");
                csAttr = csAttr.replace(")", "");
                List<String> csPairs = Arrays.asList(csAttr.split(","));
                for(String cPair : csPairs) {
                    if(cPair.startsWith(" "))
                        cPair = cPair.substring(1, cPair.length() - 1);

                    String[] c = cPair.split(" ");
                    e.addPoint(Float.parseFloat(c[1]), Float.parseFloat(c[0]));
                }
            }

            return e;
        };

        return new GraphMLImporter<>(vertexProvider, edgeProvider);
    }

    private static GraphImporter<Vertex, Edge> createImporter() {
        VertexProvider<Vertex> vertexProvider = (id, attributes) -> {
            return new Vertex(Integer.parseInt(id));
        };

        EdgeProvider<Vertex, Edge> edgeProvider = (from, to, label, attributes) -> {
            return new Edge(from, to, Double.parseDouble(attributes.get("length").getValue()));
        };

        return new GraphMLImporter<>(vertexProvider, edgeProvider);
    }

    public DijkstraExercise(Path instancesBase_P, Path solutionsBase_P) {
        setInstancesBase(instancesBase_P);
        setSolutionsBase(solutionsBase_P);
    }

	@Override
	public void run() throws IOException {
		DijkstraSolution sol = new DijkstraSolution();
		this.<DijkstraSolution>promptInstances(sol, instancesBase, solutionsBase, ".csv");
	}

	@Override
	public String name() {
		return "Dijkstra";
	}

	@Override
	protected void runWithFile(BufferedReader reader, Solution sol) throws IOException, Algorithm.AlgorithmErrorException {
        String type = reader.readLine();
        if(type.compareTo("OSM") == 0)
            osmGraph(reader, sol);
        else if(type.compareTo("graph") == 0)
            normalGraph(reader, sol);
        else
            throw new IOException();
	}

    private void normalGraph(BufferedReader reader, Solution sol) throws IOException, Algorithm.AlgorithmErrorException {
        System.out.println("Reading Graph...");

        String graphFile = reader.readLine();
        BufferedReader graphReader = Files.newBufferedReader(Paths.get(instancesBase.toString(), graphFile));
        Graph<Vertex, Edge> G = getGraph(graphReader);

        System.out.println("Graph has been read successfully! There are " + G.vertexSet().size() + " Nodes and " + G.edgeSet().size() + " Edges.");

        ArrayList<Vertex> ss = new ArrayList<>();
        ArrayList<Vertex> ts = new ArrayList<>();
        String query = null;
        while((query = reader.readLine()) != null) {
            String[] st = query.split(" ");
            for(Vertex v : G.vertexSet()) {
                if(v.getM_id().compareTo(Integer.parseInt(st[0]) - 1) == 0)
                    ss.add(v);
                if(v.getM_id().compareTo(Integer.parseInt(st[1]) - 1) == 0)
                    ts.add(v);
            }
        }

        setDistanceMetric(G);
        runDijkstra(G, ss, ts, sol, 0, "distance");
    }

    private Graph<Vertex, Edge> getGraph(BufferedReader reader) {
        Graph<Vertex, Edge> G = new SimpleDirectedWeightedGraph<Vertex, Edge>(Edge.class);

        try {
            GraphImporter<Vertex, Edge> importer = createImporter();
            importer.importGraph(G, reader);
        } catch(ImportException e) {
            e.printStackTrace();
        }

        int id = 0;
        for(Vertex v : G.vertexSet())
            v.setM_id(id++);

        return G;
    }

	private void osmGraph(BufferedReader reader, Solution sol) throws IOException, Algorithm.AlgorithmErrorException {
        System.out.println("Reading OSM Graph...");

        String graphFile = reader.readLine();
        BufferedReader graphReader = Files.newBufferedReader(Paths.get(instancesBase.toString(), graphFile));
        Graph<OSMVertex, OSMEdge> G = getOSMGraph(graphReader);

        System.out.println("OSM Graph has been read successfully! There are " + G.vertexSet().size() + " Nodes and " + G.edgeSet().size() + " Edges.");

        ArrayList<OSMVertex> ss = new ArrayList<>();
        ArrayList<OSMVertex> ts = new ArrayList<>();
        String query = null;
        while((query = reader.readLine()) != null) {
            String[] st = query.split(" ");
            for(OSMVertex v : G.vertexSet()) {
                if(v.getM_osmId().compareTo(Long.parseLong(st[0])) == 0)
                    ss.add(v);
                if(v.getM_osmId().compareTo(Long.parseLong(st[1])) == 0)
                    ts.add(v);
            }
        }

        setDistanceMetric(G);
        runDijkstra(G, ss, ts, sol, 0, "distance");
        setTimeMetric(G);
        runDijkstra(G, ss, ts, sol, sol.numSolutions(), "time");
    }

	private Graph<OSMVertex, OSMEdge> getOSMGraph(BufferedReader reader) {
        Graph<OSMVertex, OSMEdge> G = new DirectedWeightedPseudograph<OSMVertex, OSMEdge>(OSMEdge.class);

        try {
            GraphImporter<OSMVertex, OSMEdge> importer = createOSMImporter();
            importer.importGraph(G, reader);
        } catch(ImportException e) {
            e.printStackTrace();
        }

        int id = 0;
        for(OSMVertex v : G.vertexSet())
            v.setM_id(id++);

        return G;
    }

	private<V extends Vertex,E extends Edge> void runDijkstra(Graph<V, E> G, ArrayList<V> ss, ArrayList<V> ts, Solution sol, int id_base, String metric) throws Algorithm.AlgorithmErrorException {
        int id = id_base;
        ArrayList<Class<? extends MyPriorityQueue>> qTypes = new ArrayList<Class<? extends MyPriorityQueue>>();
        qTypes.add(E2.MinHeap.class);
        qTypes.add(MyFibonacciHeap.class);
        qTypes.add(SimpleQueue.class);

        for(int i = 0; i < ss.size(); ++i) {
            for(Class<? extends MyPriorityQueue> qType : qTypes) {
                MyPriorityQueue Q = getPQ(qType);
                if(Q.isImplemented()) {
                    V s = ss.get(i);
                    V t = ts.get(i);
                    System.out.println(name() + " from Node " + s.getM_id() + " to Node " + t.getM_id() + " mit PQ " + Q.name() + " und Metric \'" + metric + "\'");
                    runDijkstraWithQ(id, G, s, t, sol, Q, metaData(i, metric, Q.name()));
                    ++id;
                }
            }
        }
    }

	private<V extends Vertex,E extends Edge> void runDijkstraWithQ(int id, Graph<V, E> G, V s, V t, Solution sol, MyPriorityQueue Q, String metadata) throws Algorithm.AlgorithmErrorException {
        DijkstraShortestPath<V, E> jDijkstra = new DijkstraShortestPath<>(G);
        Dijkstra sDijkstra = new Dijkstra();

        ArrayList<V> spA = new ArrayList<>();
        Dijkstra.Weight weight = new Dijkstra.Weight();
        long time = sDijkstra.call(G, s, t, Q, spA, weight);
        GraphPath<V, E> sp = new GraphWalk<>(G, spA, weight.getWeight());

        GraphPath<V, E> jp = jDijkstra.getPath(s, t);
        if(!sol.addSolution(id, name(), metadata, time, sp, jp))
            throw new Algorithm.AlgorithmErrorException(id, sol.solutionToString(id));
    }

    private MyPriorityQueue getPQ(Class<? extends MyPriorityQueue> QType) {
        MyPriorityQueue Q = null;
        try {
            Q = (MyPriorityQueue) QType.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return Q;
    }

    private<V extends Vertex, E extends Edge> void setDistanceMetric(Graph<V, E> G) {
        for(E e : G.edgeSet())
            G.setEdgeWeight(e, e.getM_length());
    }

    private void setTimeMetric(Graph<OSMVertex, OSMEdge> G) {
        for(OSMEdge e : G.edgeSet())
            G.setEdgeWeight(e, e.getM_length() / ((double) e.getM_maxspeed()));
    }

    private String metaData(int id, String metric, String name) {
        String metadata = "";

        metadata += "\"" + "{\'id\' : " + id + ", \'metric\' : \'" + metric + "\', \'name\' : \'" + name + "\'}\"";

        return metadata;
    }
}
