import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.Scanner;

public class IMDBGraphImpl implements IMDBGraph {

    public IMDBGraphImpl(String actorsFilename, String actressesFilename) throws FileNotFoundException {
        final Scanner actorScanner = new Scanner(new File(actorsFilename), "ISO-8859-1");
        final Scanner actressScanner = new Scanner(new File(actorsFilename), "ISO-8859-1");

        while(actorScanner.hasNext()){
            String line = actorScanner.nextLine();
            if(line.equals("----\t\t\t------"))
                break;

        }
        while(actressScanner.hasNext()){
            String line = actorScanner.nextLine();
            if(line.equals("----\t\t\t------"))
                break;
        }



    }

    @Override
    public Collection<? extends Node> getActors() {
        return null;
    }

    @Override
    public Collection<? extends Node> getMovies() {
        return null;
    }

    @Override
    public Node getActor(String name) {
        return null;
    }

    @Override
    public Node getMovie(String name) {
        return null;
    }
}
