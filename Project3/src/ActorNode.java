import java.util.Collection;
import java.util.List;

public class ActorNode implements Node {

    private String _name;
    private List<MovieNode> _neighbors;

    /**
     * Constructor that initializes an ActorNode with a name and neigbors
     * @param name String representing actor's name
     * @param neighbors List representing the neigboring movie nodes.
     */
    public ActorNode(String name, List<MovieNode> neighbors){
        this._name = name;
        this._neighbors = neighbors;
    }

    /**
     * Adds MovieNode to current node's list of movies
     * @param n
     */
    public void addNeighbor(MovieNode n){
        this._neighbors.add(n);
    }

    @Override
    /**
     * Returns the name of the ActorNode
     */
    public String getName() {
        return _name;
    }

    @Override
    /**
     * Returns a list containing the neighbors of the current ActorNode
     */
    public Collection<? extends Node> getNeighbors() {
        return _neighbors;
    }

}
