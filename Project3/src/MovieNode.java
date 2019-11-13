import java.util.ArrayList;
import java.util.Collection;

public class MovieNode implements Node {

    private String _name;
    private ArrayList<ActorNode> _neighbors;

    public MovieNode(String name, ArrayList<ActorNode> neighbors) {
        this._name = name;
        this._neighbors = neighbors;
    }

    public void addNeighbor(ActorNode n){
        this._neighbors.add(n);
    }

    @Override
    public String getName() {
        return this._name;
    }

    @Override
    public Collection<? extends Node> getNeighbors() {
        return this._neighbors;
    }

}
