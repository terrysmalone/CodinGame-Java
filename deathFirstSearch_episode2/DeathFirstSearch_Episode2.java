import java.util.*;
import java.util.stream.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    public static void main(String args[]) {
        Graph linkGraph = new Graph();

        Scanner in = new Scanner(System.in);
        int numberOfNodes = in.nextInt(); // the total number of nodes in the level, including the gateways
        int numberOfLinks = in.nextInt(); // the number of links
        int numberOfExitGateways = in.nextInt(); // the number of exit gateways

        for (int i = 0; i < numberOfLinks; i++) {
            int linkNode1 = in.nextInt(); // N1 and N2 defines a link between these nodes
            int linkNode2 = in.nextInt();

            linkGraph.addEdge(linkNode1, linkNode2);
        }

        List<Integer> exitGateways = new ArrayList<Integer>();
        for (int i = 0; i < numberOfExitGateways; i++) {
            exitGateways.add(in.nextInt());  // the index of a gateway node
        }

        // game loop
        while (true) {
            int agentPosition = in.nextInt(); // The index of the node on which the Bobnet agent is positioned this turn

            // Write an action using System.out.println()
            // To debug: System.err.println("Debug messages...");

            List<Link> allViableSevers = linkGraph.getAllViableSevers(exitGateways);

            Map<Link, Integer> priorities = new HashMap<Link, Integer>();

            for (Link sever: allViableSevers) {
                priorities.putIfAbsent(sever, calculatePriority(agentPosition, sever, allViableSevers, linkGraph, exitGateways));
            }
            
            // Order by priority
            List<Link> sortedPriorities = priorities.entrySet()
            .stream()
            .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

            String output = sortedPriorities.get(0).getNode1() + " " + sortedPriorities.get(0).getNode2();

            linkGraph.removeEdge(sortedPriorities.get(0).getNode1(), sortedPriorities.get(0).getNode2());
            // Example: 0 1 are the indices of the nodes you wish to sever the link between
            System.out.println(output);
        }
    }

    private static int calculatePriority(int agentPosition, Link sever, List<Link> allViableSevers, Graph graph, List<Integer> exitGateways) {
        // get shortest path to sever.getNode1()
        List<Integer> shortestPath = graph.findShortestPath(agentPosition, sever.getNode1(), exitGateways);

        if (shortestPath.size() == 1) {
            return Integer.MAX_VALUE;
        }

        int adjacentExits = 0;

        for (int i = 1; i < shortestPath.size(); i++) {
            int pathPoint = shortestPath.get(i);

            for (Link viableSever: allViableSevers) {
                if (viableSever.getNode1() == pathPoint) {
                    adjacentExits++;
                }
            }
        }

        int size = shortestPath.size() - 1;
        int priority = ((adjacentExits - size) * 50) - size;

        return priority;
    }
}

class Graph {
    private HashMap<Integer, List<Integer>> adjacencyList = new HashMap<Integer, List<Integer>>();

    public void addEdge(int node1, int node2) {
        adjacencyList.putIfAbsent(node1, new ArrayList<>());
        adjacencyList.get(node1).add(node2);

        adjacencyList.putIfAbsent(node2, new ArrayList<>());
        adjacencyList.get(node2).add(node1);
    }

    public void removeEdge(int node1, int node2) {
        if (adjacencyList.containsKey(node1)) {
            adjacencyList.get(node1).remove(Integer.valueOf(node2)); // This removes by object, not index
        }

        if (adjacencyList.containsKey(node2)) {
            adjacencyList.get(node2).remove(Integer.valueOf(node1)); // This removes by object, not index
        }
    }

    public List<Integer> getAdjVertices(int node) {
        return adjacencyList.get(node);
    }

    // Find the shortest path using a breadth first search
    public List<Integer> findShortestPath(int start, int end, List<Integer> exitGateways) {
        // Check that both nodes exist
        if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(end)) {
            return Collections.emptyList();
        }

        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> parentMap = new HashMap<>();
        Set<Integer> visited = new HashSet<>();

        // Add the vertices to visited. We can't walk over them
        for (int exitGateway: exitGateways) {
            visited.add(exitGateway);
        }

        queue.add(start);
        visited.add(start);
        parentMap.put(start, null);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            // We've reached the target. Build the path
            if (current == end) {
                return buildPath(parentMap, start, end);
            }

            for (int neighbour : adjacencyList.get(current)) {
                if (!visited.contains(neighbour)) {
                    queue.add(neighbour);
                    visited.add(neighbour);
                    parentMap.put(neighbour, current);
                }
            }
        }

        return Collections.emptyList();
    }

    private List<Integer> buildPath(Map<Integer, Integer> parentMap, int start, int end) {
        List<Integer> path = new ArrayList<>();
        Integer current = end;

        while (current != null) {
            path.add(current);
            current = parentMap.get(current);
        }

        Collections.reverse(path);
        return path;
    }

    public List<Link> getAllViableSevers(List<Integer> exitGateways) {
        List<Link> severs = new ArrayList<Link>();

        for (int exit: exitGateways) {
            List<Integer> neighbours = adjacencyList.get(exit);

            for (int neighbour: neighbours) {
                severs.add(new Link(neighbour, exit));
            }
        }

        return severs;
    }
}

class Link {
    private int node1;
    private int node2;

    public Link(int node1, int node2) {
        this.node1 = node1;
        this.node2 =node2;
    }

    public int getNode1() {
        return node1;
    }

    public int getNode2() {
        return node2;
    }
}