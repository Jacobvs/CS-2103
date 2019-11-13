import java.util.ArrayList;
import java.util.Collection;

public class ActorNode implements Node {

    private String _name;
    private ArrayList<MovieNode> _neighbors;

    public ActorNode(String name, ArrayList<MovieNode> neighbors){
        this._name = name;
        this._neighbors = neighbors;
    }

    @Override
    public String getName() {
        return _name;
    }

    @Override
    public Collection<? extends Node> getNeighbors() {
        return _neighbors;
    }

}
