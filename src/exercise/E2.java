package exercise;

import util.ADGraph;

import java.util.*;

public class E2 {
    public static class MinHeap extends PartialMinHeap {
        /*
         * Setzen Sie IS_IMPLEMENTED auf true, wenn ihr MinHeap bei der
         * Ausfuerhung von shortestPaths verwendet werden soll!
         */
        private static boolean IS_IMPLEMENTED = true;

        @Override
        /*
         * Implementieren Sie die heapifyUp Methode wie in der Vorlesung besprochen.
         * Der Integer i ist dabei die Position des Heap Elements. Mit keyAt(i) haben sie Zugriff
         * auf den Schluessel.
         */
        protected void heapifyUp(int i) {
            int j;
            if(i>0){
                j = getParent(i); //Get the Parent Node
                if(keyAt(i)< keyAt(j)){  // if parent is bigger than the children
                    swap(i,j);          //swap them
                    heapifyUp(j);       //recursive call
                }

            }

        }

        @Override
        /*
         * Implementieren Sie die heapifyDown Methode wie in der Vorlesung besprochen.
         * Der Integer i ist dabei die Position des Heap Elements. Mit keyAt(i) haben sie Zugriff
         * auf den Schluessel.
         */
        protected void heapifyDown(int i) {
            int n = elements.size()-1;
            int left = getLeft(i);
            int right = getRight(i);
            int j;
            if(left > n){
                return;
            }
            else if(left < n){
                    if(keyAt(left) > keyAt(right)){
                        j = right; //index of the smallest key value for childrens
                    } else { j = left;}
            } else {j = left;}

            if(keyAt(j)< keyAt(i)){
                swap(i,j);
                heapifyDown(j);

            }

        }

        @Override
        /*
         * Fuer ein Element an Position i gibt getParent(i) dessen Elternelement, wie
         * in der Vorlesung besprochen, zurueck. Beachten Sie, dass wir in dieser Implementierung
         * ab 0 zaehlen, nicht ab 1 wie in der Vorlesung!
         */
        protected int getParent(int i) {
            return Math.abs((i-1)/2);
        }

        @Override
        /*
         * Fuer ein Element an Position i gibt getLeft(i) dessen linken Nachfolger, wie
         * in der Vorlesung besprochen, zurueck. Beachten Sie, dass wir in dieser Implementierung
         * ab 0 zaehlen, nicht ab 1 wie in der Vorlesung!
         */
        protected int getLeft(int i) {return 2*i +1; }

        @Override
        /*
         * Fuer ein Element an Position i gibt getRight(i) dessen rechten Nachfolger, wie
         * in der Vorlesung besprochen, zurueck. Beachten Sie, dass wir in dieser Implementierung
         * ab 0 zaehlen, nicht ab 1 wie in der Vorlesung!
         */
        protected int getRight(int i) {
            return 2*i + 2;
        }

        //TO IGNORE
        @Override
        public boolean isImplemented() {
            return IS_IMPLEMENTED;
        }
    }

    /*
     * Implementieren Sie hier ihre Version von Dijkstras Algorithmus. G ist der Graph, s und t die IDs
     * der Knoten, zwischen denen Sie den kuerzesten s->t Pfad finden sollen. Q ist eine bereits korrekt
     * initialisiert, leere Priority Queue. Sie muessen NICHT den MinHeap von oben an diese Methode
     * weitergeben, dass erledigt das Framework fuer sie!
     *
     * Als Rueckgabe erwartet das Framework das Gewicht eines kuerzesten Pfades von s nach t. In path
     * speichern Sie bitte die IDs der besuchten Knoten von s nach t. Dass heisst am Ende sollte path
     * an Position Null s enthalten, gefolgt von den Knoten auf dem Weg von s nach t und auf dem letzten
     * Arrayplatz den Knoten t.
     */

    private static void Get_Path(int s_vert,int vert, int[] pre, ArrayList<Integer>path){ //recursive method to get the predecessors and put them in path in respect to start and target vertices
        if(s_vert != vert){
            Get_Path(s_vert,pre[vert],pre,path); //recursion will be ran only when start vertex is not equal to target vertex
            // in every recursion step the start vertex will stay the same and starting from the target vector, the predecessors of each vert variable
            // will be traced in predecessor array back to the start vertex saving in recursive stack
        }
        path.add(vert);                          // every vert which are traced will be added to path one by one after recursive period ends.


    }

    public static double shortestPaths(ADGraph G, Integer s, Integer t, ArrayList<Integer> path, MyPriorityQueue Q) {
        int number_Of_Vertices = G.numVertices();
        double[] d = new double[number_Of_Vertices];     // Array for Distances
        boolean[] Discovered = new boolean[number_Of_Vertices];  //Array for discovered Vertices
        int[] pre = new int[number_Of_Vertices];        // Array of predecessors
        for(int i = 0; i < number_Of_Vertices; i++) {   // Declaring the distances of vertices as infinity
            d[i] = Double.POSITIVE_INFINITY;
        }
        d[s] = 0;                                       // Declaring the distance of start vertex as 0
        for(int i = 0; i < number_Of_Vertices; i++) {       //Adding the vertices and their distance values to queue
            Q.add(i, d[i]);
        }




        while(Q.isEmpty() == false){                //loop till queue is empty
            int u = Q.removeMin();                  // u has the vertex with the minimum distance value
            Discovered[u] = true;                   // u has been marked as discovered
            if(u == t) {                            //if u is equal to target vertex, quit the loop
                break;
            }

            ArrayList<Integer> neighbours = G.outNeighbors(u);  //copying the neighbours of u to a new list
            for (int v:neighbours) {                    //foreach v neighbour of u
                if(!Discovered[v]) {                    // check if the neighbour is discovered
                    if(d[v]> d[u]+ G.weight(u,v)) { //  if distance of neighbour is bigger than the distance of u + the edge weight inbetween,
                        d[v] = d[u] + G.weight(u,v);  // new distance of the neighbour is distance of u + edge weight inbetween
                        Q.decreaseKey(v, d[v]);       // reassign the key of the neighbour vertex with the new distance of neighbour
                        pre[v] = u;                   // declare the neighbours predecessor as u
                    }
                }
            }



        }

        Get_Path(s,t,pre,path);
        return d[t];
    }



}
