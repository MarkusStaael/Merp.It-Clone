package org.example.firstyearproject.Algorithms;

import org.example.firstyearproject.DataStructures.EdgeWeightedGraph;
import org.example.firstyearproject.MapObjects.Edge;
import org.example.firstyearproject.MapObjects.Node;

import java.io.Serializable;
import java.util.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Dijkstra implements Serializable {
    public double[] distTo;          // distTo[v] = distance  of shortest s->v path
    public Edge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private minPQ<Double> pq;    // priority queue of

    //initialises the Dijkstra SP, with an EdgeWeightedGraph G, and source vertex, s
    public Dijkstra(EdgeWeightedGraph G, int source) {
        distTo = new double[G.V()];
        edgeTo = new Edge[G.V()];

        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[source] = 0.0;

        // relax vertices in order of distance from s
        pq = new minPQ<>(G.V()+1);
        pq.insert(source, distTo[source]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            if (G.adj(v) != null) {
                for (Edge e : G.adj(v))
                    relax(e);

            }
        }
    }

    // relax edge e and update pq if changed
    public void relax(Edge e) {
        int v = e.from(), w = e.to();
        if (distTo[w] > distTo[v] + e.weight()) {
            distTo[w] = distTo[v] + e.weight();
            edgeTo[w] = e;
            if (pq.contains(w)) pq.decreaseKey(w, distTo[w]);
            else                pq.insert(w, distTo[w]);
        }

        //System.out.println("dist to w " + distTo[w]);
        //System.out.println("dist to v + weight " + distTo[v] + e.weight());
    }

    //returns the distance to the node v
    public double distTo(int v) {
        return distTo[v];
    }

    //returns true if there's a path to node v
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    //returns the edge/path that leads to node v
    public ArrayList<Node> pathTo(Node node) {
        ArrayList<Node> pathNode = new ArrayList<>();
        pathNode.add(node);
        for (Edge e = edgeTo[node.counter()]; e != null; e = edgeTo[e.from()]) {
            if (!pathNode.contains(e.getNode0())) { pathNode.add(e.getNode0());}
            //if (!pathNode.contains(e.getNode1())) { pathNode.add(e.getNode1());}
        }
        return pathNode;
    }

    private static class minPQ<Key extends Comparable<Key>> implements Iterable<Integer> {
        int maxN;        // maximum number of elements on PQ
        int n;           // number of elements on PQ
        int[] pq;        // binary heap using 1-based indexing
        int[] qp;        // inverse of pq - qp[pq[i]] = pq[qp[i]] = i
        Key[] keys;      // keys[i] = priority of i

        public minPQ(int maxN) {
            if (maxN < 0) throw new IllegalArgumentException();
            this.maxN = maxN;
            n = 0;
            keys = (Key[]) new Comparable[maxN + 1];    // make this of length maxN??
            pq   = new int[maxN + 1];
            qp   = new int[maxN + 1];                   // make this of length maxN??
            for (int i = 0; i <= maxN; i++)
                qp[i] = -1;
        }


        public boolean isEmpty() {
            return n == 0;
        }

        public boolean contains(int i) {
            try {
                validateIndex(i);
            } catch (IllegalArgumentException e) { System.out.println("Invalid index"); }
            return qp[i] != -1;
        }

        public int size() {
            return n;
        }

        public void insert(int i, Key key) {
            try { validateIndex(i);
            } catch (IllegalArgumentException e) { System.out.println("Invalid index"); }
            if (!contains(i)) {
                n++;
                qp[i] = n;
                pq[n] = i;
                keys[i] = key;
                swim(n);
            }
        }

        public int minIndex() {
            if (n == 0) throw new NoSuchElementException("Priority queue underflow");
            return pq[1];
        }

        public Key minKey() {
            if (n == 0) throw new NoSuchElementException("Priority queue underflow");
            return keys[pq[1]];
        }

        public int delMin() {
            if (n == 0) throw new NoSuchElementException("Priority queue underflow");
            int min = pq[1];
            exch(1, n--);
            sink(1);
            assert min == pq[n+1];
            qp[min] = -1;        // delete
            keys[min] = null;    // to help with garbage collection
            pq[n+1] = -1;        // not needed
            return min;
        }

        public Key keyOf(int i) {
            validateIndex(i);
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            else return keys[i];
        }


        public void changeKey(int i, Key key) {
            validateIndex(i);
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            keys[i] = key;
            swim(qp[i]);
            sink(qp[i]);
        }

        @Deprecated
        public void change(int i, Key key) {
            changeKey(i, key);
        }

        public void decreaseKey(int i, Key key) {
            validateIndex(i);
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            if (keys[i].compareTo(key) == 0)
                throw new IllegalArgumentException("Calling decreaseKey() with a key equal to the key in the priority queue");
            if (keys[i].compareTo(key) < 0)
                throw new IllegalArgumentException("Calling decreaseKey() with a key strictly greater than the key in the priority queue");
            keys[i] = key;
            swim(qp[i]);
        }

        public void increaseKey(int i, Key key) {
            validateIndex(i);
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            if (keys[i].compareTo(key) == 0)
                throw new IllegalArgumentException("Calling increaseKey() with a key equal to the key in the priority queue");
            if (keys[i].compareTo(key) > 0)
                throw new IllegalArgumentException("Calling increaseKey() with a key strictly less than the key in the priority queue");
            keys[i] = key;
            sink(qp[i]);
        }

        public void delete(int i) {
            validateIndex(i);
            if (!contains(i)) throw new NoSuchElementException("index is not in the priority queue");
            int index = qp[i];
            exch(index, n--);
            swim(index);
            sink(index);
            keys[i] = null;
            qp[i] = -1;
        }

        // throw an IllegalArgumentException if i is an invalid index
        private void validateIndex(int i) {
            if (i < 0) throw new IllegalArgumentException("index is negative: " + i);
            if (i >= maxN) throw new IllegalArgumentException("index >= capacity: " + i);
        }

        private boolean greater(int i, int j) {
            return keys[pq[i]].compareTo(keys[pq[j]]) > 0;
        }

        private void exch(int i, int j) {
            int swap = pq[i];
            pq[i] = pq[j];
            pq[j] = swap;
            qp[pq[i]] = i;
            qp[pq[j]] = j;
        }

        private void swim(int k) {
            while (k > 1 && greater(k/2, k)) {
                exch(k, k/2);
                k = k/2;
            }
        }

        private void sink(int k) {
            while (2*k <= n) {
                int j = 2*k;
                if (j < n && greater(j, j+1)) j++;
                if (!greater(k, j)) break;
                exch(k, j);
                k = j;
            }
        }

        public Iterator<Integer> iterator() { return new HeapIterator(); }

        private class HeapIterator implements Iterator<Integer> {
            // create a new pq
            private minPQ<Key> copy;

            // add all elements to copy of heap
            // takes linear time since already in heap order so no keys move
            public HeapIterator() {
                copy = new minPQ<Key>(pq.length - 1);
                for (int i = 1; i <= n; i++)
                    copy.insert(pq[i], keys[pq[i]]);
            }

            public boolean hasNext()  { return !copy.isEmpty();                     }
            public void remove()      { throw new UnsupportedOperationException();  }

            public Integer next() {
                if (!hasNext()) throw new NoSuchElementException();
                return copy.delMin();
            }
        }
    }
}