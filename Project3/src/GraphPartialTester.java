import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Code to test Project 3; you should definitely add more tests!
 */
public class GraphPartialTester {
	IMDBGraph imdbGraph, imdbGraph10k;
	GraphSearchEngine searchEngine;

	/**
	 * Verifies that there is no shortest path between a specific and actor and actress.
	 */
	@Test(timeout=5000)
	public void findShortestPath () throws IOException {
		imdbGraph = new IMDBGraphImpl("/Users/Jacob/OneDrive - Worcester Polytechnic Institute (wpi.edu)/CS 2103/Project3/actors_test.list", "/Users/Jacob/OneDrive - Worcester Polytechnic Institute (wpi.edu)/CS 2103/Project3/actresses_test.list");
		final Node actor1 = imdbGraph.getActor("Actor1");
		final Node actress2 = imdbGraph.getActor("Actress2");
		final List<Node> shortestPath = searchEngine.findShortestPath(actor1, actress2);
		assertNull(shortestPath);  // there is no path between these people
	}

	@Before
	/**
	 * Instantiates the graph
	 */
	public void setUp () throws IOException {
		imdbGraph = new IMDBGraphImpl("/Users/Jacob/OneDrive - Worcester Polytechnic Institute (wpi.edu)/CS 2103/Project3/actors_test.list", "/Users/Jacob/OneDrive - Worcester Polytechnic Institute (wpi.edu)/CS 2103/Project3/actresses_test.list");
		searchEngine = new GraphSearchEngineImpl();
	}

	@Test
	/**
	 * Just verifies that the graphs could be instantiated without crashing.
	 */
	public void finishedLoading () {
		assertTrue(true);
		// Yay! We didn't crash
	}

	@Test
	/**
	 * Verifies that a specific movie has been parsed.
	 */
	public void testSpecificMovie () {
		testFindNode(imdbGraph.getMovies(), "Movie1 (2001)");
	}

	@Test
	/**
	 * Verifies that a specific actress has been parsed.
	 */
	public void testSpecificActress () {
		testFindNode(imdbGraph.getActors(), "Actress2");
	}

	@Test(timeout=5000)
	public void testRightNum() throws IOException{
		imdbGraph10k = new IMDBGraphImpl("/Users/Jacob/OneDrive - Worcester Polytechnic Institute (wpi.edu)/CS 2103/Project3/actors10k.list", "/Users/Jacob/OneDrive - Worcester Polytechnic Institute (wpi.edu)/CS 2103/Project3/actresses10k.list");
		int numActors = imdbGraph10k.getActors().size();
		System.out.println(numActors);
		assertTrue(2100 < numActors && numActors < 2300);
	}

	@Test
	public void testNestedLLMutation(){
		Queue<LinkedList<String>> test = new ArrayDeque<>();
		LinkedList<String> startList = new LinkedList<String>(); // Starting with Queue containing list containing first node
		startList.add("a");
		startList.add("b");
		test.add((LinkedList) startList.clone());
		test.add(startList);
		System.out.println("test");
		System.out.println("Original List: " + ((ArrayDeque<LinkedList<String>>) test).peekFirst().toString());
        System.out.println("Removing element");
		startList.remove();
		System.out.println("Cloned List: " + test.remove().toString());
        System.out.println("OG List: " + test.remove().toString());

	}

	/**
	 * Verifies that the specific graph contains a node with the specified name
	 * @param nodes the IMDBGraph to search for the node
	 * @param name the name of the Node
	 */
	private static void testFindNode (Collection<? extends Node> nodes, String name) {
		boolean found = false;
		for (Node node : nodes) {
			System.out.println(node.getName());
			if (node.getName().trim().equals(name)) {
				found = true;
			}
		}
		assertTrue(found);
	}
}
