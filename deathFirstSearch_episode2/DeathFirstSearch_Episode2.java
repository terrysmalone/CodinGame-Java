import java.util.*;
import java.io.*;
import java.math.*;

/**
 * Auto-generated code below aims at helping you parse
 * the standard input according to the problem statement.
 **/
class Player {

    private static int evaluations = 0;
    private static int totalEvaluations = 0;

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

            
            Link bestSever = getBestSever(agentPosition, exitGateways, linkGraph);

            // Get the shortest paths to all gateways 
            // List<List<Integer>> shortestPaths = new ArrayList<List<Integer>>();
            // for (int exitGateway: exitGateways) {
            //     List<Integer> shortestPath = linkGraph.findShortestPath(agentPosition, exitGateway);
            //     shortestPaths.add(shortestPath);

            //    displayPath(shortestPath);  
            // }

            // We don't always want the shortest one. Sometimes we may need to block another
            // since a node can connect to two exits
            // int chosenId = getPrioritisedSever(shortestPaths);
            
            // if (chosenId == -1) {
            //     chosenId = getShortestPathSever(shortestPaths);
            // }
            
            //List<Integer> chosenOne = shortestPaths.get(chosenId);
            // String output = chosenOne.get(chosenOne.size() - 2) + " " + chosenOne.get(chosenOne.size() - 1);

            // linkGraph.removeEdge(chosenOne.get(chosenOne.size() - 2), chosenOne.get(chosenOne.size() - 1));
            
            String output = bestSever.getNode1() + " " + bestSever.getNode2();
            linkGraph.removeEdge(bestSever.getNode1(), bestSever.getNode2());
            // Example: 0 1 are the indices of the nodes you wish to sever the link between
            System.out.println(output);
        }
    }

    private static int maxDepth = 6;
    private static Link getBestSever(int agentPosition, List<Integer> exitGateways, Graph graph) {
        // Get all possible severs
        List<Link> allViableSevers = graph.getAllViableSevers(exitGateways);

        int min = Integer.MAX_VALUE;
        Link bestSever = null;

        totalEvaluations = 0;
        
        for (Link sever: allViableSevers) {
            evaluations = 0;
            System.err.println("Checking Sever(" + sever.getNode1() + "," + sever.getNode2() + ")");

            // make sever
            graph.removeEdge(sever.getNode1(), sever.getNode2());

            int survivalTime = getSurvivalTime(false, agentPosition, exitGateways, graph, maxDepth);

            if (survivalTime < min) {
                min = survivalTime;
                bestSever = new Link(sever.getNode1(), sever.getNode2());
            }
            // unmake sever
            graph.addEdge(sever.getNode1(), sever.getNode2());

            System.err.println("Sever(" + sever.getNode1() + "," + sever.getNode2() + "):" + survivalTime + " - Evals: " + evaluations);
        
            if (survivalTime <= Integer.MIN_VALUE/2) {
                break;
            }
        } 

        System.err.println("Total Evals: " + totalEvaluations);
        
        return bestSever;
    }

    private static int getSurvivalTime(boolean ourMove, int agentPosition, List<Integer> exitGateways, Graph graph, int depth) {
        evaluations++;
        totalEvaluations++;

        // Evaluate
        List<Integer> allAgentMoves = graph.getAdjVertices(agentPosition);

        if (allAgentMoves.size() == 0) {
            return (Integer.MIN_VALUE/2) - depth;
        }

        //String space = "";
        //for (int i = 0; i < maxDepth-(depth-1); i++){
        //    space += " ";
        //}

        if (depth == 0) {
            //System.err.println(space + "Hit max depth");
            return 0;
        }
        
        if (ourMove) {
            int min = Integer.MAX_VALUE / 2;
            // Link bestSever = new Link(-1, -1);

            List<Link> allSevers = graph.getAllViableSevers(exitGateways);

            if (allSevers.size() == 1) {
                return (Integer.MIN_VALUE/2) - depth;
            }

            for (Link sever: allSevers) {
                // System.err.println(space + "Checking Sever(" + sever.getNode1() + "," + sever.getNode2() + ")");

                //   make sever
                graph.removeEdge(sever.getNode1(), sever.getNode2());

                int survivalTime = getSurvivalTime(!ourMove, agentPosition, exitGateways, graph, depth - 1);
                // System.err.println(space + "Survival time: " + survivalTime);

                if (survivalTime < min) {
                    min = survivalTime;
                    // bestSever = new Link(sever.getNode1(), sever.getNode2());
                }

                // unmake sever
                graph.addEdge(sever.getNode1(), sever.getNode2());

                if (min <= Integer.MIN_VALUE/2) {
                    // System.err.println(space + "Hit cut off for our move");
                    break;
                }
            }

            return min;

        } else {
            for (int move: allAgentMoves) {
                if (exitGateways.contains(move)) {
                    // System.err.println(space + "Agent can get to an exitGateway");
                    return Integer.MAX_VALUE/2 + depth;
                }
            }

            int max = Integer.MIN_VALUE / 2;
            // int bestMove = -1;

            int previousPosition = agentPosition;

            for (int agentMove: allAgentMoves) {
                // System.err.println(space + "Checking Agent move: " + previousPosition + " to " + agentMove);

                // make agent move
                agentPosition = agentMove;
                
                int survivalTime = getSurvivalTime(!ourMove, agentMove, exitGateways, graph, depth - 1);
            
                // System.err.println("Survival time: " + survivalTime);
                if (survivalTime > max) {
                    max = survivalTime;
                    // bestMove = current move
                }

                // unmake move
                agentPosition = previousPosition;

                if (max >= Integer.MAX_VALUE/2) {
                    // System.err.println("Hit cut off for our move");
                    break;
                }                
            }

            return max;
        }
    }

    private static void displayPath(List<Integer> path) {
        for (int i = 0; i < path.size(); i++) {
            System.err.print(path.get(i) + " ");
        }
        System.err.println();
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
    public List<Integer> findShortestPath(int start, int end) {
        // Check that both nodes exist
        if (!adjacencyList.containsKey(start) || !adjacencyList.containsKey(end)) {
            return Collections.emptyList();
        }

        Queue<Integer> queue = new LinkedList<>();
        Map<Integer, Integer> parentMap = new HashMap<>();
        Set<Integer> visited = new HashSet<>();

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