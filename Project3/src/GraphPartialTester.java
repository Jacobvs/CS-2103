import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Code to test Project 3; you should definitely add more tests!
 */
public class GraphPartialTester {
	IMDBGraph imdbGraph, imdbGraph10k;
	GraphSearchEngine searchEngine;

	@Before
	/**
	 * Instantiates the graph
	 */
	public void setUp () throws IOException {
		long start = System.nanoTime();
		imdbGraph = new IMDBGraphImpl("/Users/kids/git/CS-2103/Project3/actors.list", "/Users/kids/git/CS-2103/Project3/actresses.list");
		imdbGraph10k = new IMDBGraphImpl("/Users/kids/git/CS-2103/Project3/actors10k.list", "/Users/kids/git/CS-2103/Project3/actresses10k.list");
		long end = System.nanoTime();
		System.out.println(TimeUnit.NANOSECONDS.toSeconds(end-start));
		searchEngine = new GraphSearchEngineImpl();
	}

//	@Test
//	/**
//	 * Just verifies that the graphs could be instantiated without crashing.
//	 */
//	public void finishedLoading () {
//		assertTrue(true);
//		// Yay! We didn't crash
//	}

	/**
	 * Verifies that there is no shortest path between a specific and actor and actress.
	 */
//	@Test(timeout=5000)
//	public void findShortestPath () {
//		final Node actor1 = imdbGraph.getActor("Actor1");
//		final Node actress2 = imdbGraph.getActor("Actress2");
//		final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actress2);
//		assertNull(shortestPath);  // there is no path between these people
//	}
//
//	@Test
//	/**
//	 * Verifies that a specific movie has been parsed.
//	 */
//	public void testSpecificMovie () {
//		testFindNode(imdbGraph.getMovies(), "Welcome to Slab City (2012)");
//	}
//
//	@Test
//	/**
//	 * Verifies that a specific actress has been parsed.
//	 */
//	public void testSpecificActress () {
//		testFindNode(imdbGraph.getActors(), "Abbot, Pamela");
//	}

	@Test(timeout=5000)
	public void testRightNum(){
		int numActors = imdbGraph.getActors().size();
		System.out.println(numActors);
		assertTrue(2728257 < numActors && numActors < 2897016);
	}

	@Test
	public void testGraphBuilding(){
		for (Node n : imdbGraph10k.getActors()){
			System.out.println(n.getName() + "\nMovies:");
			for (Node mn : n.getNeighbors()){
				System.out.println("neighbor: ");
				System.out.print(mn.getName());
			}
		}
	}


	/**
	 * Verifies that the specific graph contains a node with the specified name
	 * @param nodes the IMDBGraph to search for the node
	 * @param name the name of the Node
	 */
	private static void testFindNode (Collection<? extends Node> nodes, String name) {
		boolean found = false;
		for (Node node : nodes) {
			//System.out.println(node.getName());
			if (node.getName().trim().equals(name)) {
				found = true;
			}
		}
		assertTrue(found);
	}
}
