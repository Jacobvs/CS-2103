import java.util.Collection;
import java.util.List;

public class MovieNode implements Node {

    private String _name;
    private List<ActorNode> _neighbors;

    /**
     * Constructor that initializes name and neighbors fields for each MovieNode
     * @param name String represented the name of the current Movie
     * @param neighbors List of ActorNodes representing the MovieNode's neighboring actors
     */
    public MovieNode(String name, List<ActorNode> neighbors) {
        this._name = name;
        this._neighbors = neighbors;
    }

    /**
     * Adds ActorNode to list of current MovieNode's actors
     * @param n  ActorNode representing Actor to be added to Movie's neighbors
     */
    public void addNeighbor(ActorNode n){
        this._neighbors.add(n);
    }

    @Override
    /**
     * Accessor method used to return the name of the current Movie
     */
    public String getName() {
        return this._name;
    }

    @Override
    /**
     * Accessor method used to return the neighbors of the current MovieNode
     */
    public Collection<? extends Node> getNeighbors() {
        return this._neighbors;
    }

}
