import java.util.*;

public class GraphSearchEngineImpl implements GraphSearchEngine {

    /**
     * Finds shortest path between two nodes by storing visited paths of the same length until the target node is found
     * Returns path when node is found, null otherwise
     */
    @SuppressWarnings("unchecked")
    public List<Node> findShortestPath(Node s, Node t) {
        Queue<LinkedList<Node>> _pathsToVisit = new ArrayDeque<>(); // Stores paths that need to be searched (need to use add to maintain FIFO order)
        HashSet<Node> _visitedNodes = new HashSet<>(); // Stores list of visited nodes

        LinkedList<Node> startList = new LinkedList<>(); // Starting with Queue containing list containing first node
        startList.add(s);
        _pathsToVisit.add(startList); // Adds list containing first node to nodesToVisit queue


        while(_pathsToVisit.size() > 0){ // While the queue is not empty, loop
            LinkedList<Node> path = _pathsToVisit.remove(); // sets path LinkedList to first LinkedList in queue and removes it

            for(Node node : path.getLast().getNeighbors()){ // For all the neighbors of path
                if(node.equals(t)){ // if found end node, return path
                    path.add(node);
                    return path;
                }
                else{
                    if(!_visitedNodes.contains(node)) { // Check to see if we already visited this node, if so ignore it to avoid loops
                        _visitedNodes.add(node); // Adds neighbor to visited nodes
                        path.add(node);
                        _pathsToVisit.add((LinkedList) path.clone()); // add cloned list to paths so we can still modify path
                        path.removeLast(); //This might be wrong, will the path stored in _pathsToVisit be changed? yes, but we can clone it to prevent this TODO: change
                    }
                }

            }
        }
        return null; // Return null if no path is found
    }

}
