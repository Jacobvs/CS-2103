import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.animation.AnimationTimer;
import javafx.scene.media.AudioClip;
import javafx.scene.text.TextAlignment;

import java.util.Random;

public class GameImpl extends Pane implements Game {
	/**
	 * Defines different states of the game.
	 */
	public enum GameState {
		WON, LOST, ACTIVE, NEW
	}

	// Constants
	/**
	 * The width of the game board.
	 */
	static final int WIDTH = 400;
	/**
	 * The height of the game board.
	 */
	static final int HEIGHT = 600;
	/**
	 * The border where no animals will be placed
	 */
	private static final int BORDER = WIDTH/10;
	/**
	 * The grid size for x and y where each cell has an animal
	 */
	private static final int GRID_SIZE_X = (WIDTH - BORDER)/4;
	private static final int GRID_SIZE_Y = (HEIGHT - BORDER)/8;
	/**
	 * The number of hits on the bottom wall before the game ends
	 */
	private static final int BOTTOM_HITS = 5;

	// Instance variables
	private Ball ball;
	private Paddle paddle;
	private int numBottom, numTeleported;
	private final AudioClip winSound;

	/**
	 * Constructs a new GameImpl.
	 */
	GameImpl() {
		setStyle("-fx-background-color: white;");

		restartGame(GameState.NEW);
		winSound = new AudioClip(getClass().getClassLoader().getResource("chaching.wav").toString());
	}

	public String getName () {
		return "Zutopia";
	}

	public Pane getPane () {
		return this;
	}

	private void restartGame (GameState state) {
		getChildren().clear();  // remove all components from the game

		// Add start message
		final String message;
		if (state == GameState.LOST)
			message = "Game Over with " + (16 - numTeleported) + " animals remaining. \n";
		else if (state == GameState.WON) {
			winSound.play();
			message = "You won! \n";
		} else
			message = "";

		// Create and add animals ...
		String[] fileNames = new String[]{"goat.jpg", "horse.jpg", "duck.jpg"}; // Holds the possibe image filenames to be placed

		for (int i = 0; i < 4; i++) { // Create 4 lines of cells for the x axis
			for (int j = 0; j < 4; j++) { // 4 lines for y axis
				final Image image = new Image(getClass().getResourceAsStream(fileNames[new Random().nextInt(fileNames.length)])); // Retrieve random image from arr of filenames
				Label imageLabel = new Label("", new ImageView(image));
				imageLabel.setLayoutX((GRID_SIZE_X * i) + BORDER); // Place image by x and y cell
				imageLabel.setLayoutY((GRID_SIZE_Y * j) + BORDER);
				imageLabel.setId("animal"); // Add a tag that this Node is an animal
				getChildren().add(imageLabel);
			}
		}

		// Create and add ball
		ball = new Ball();
		getChildren().add(ball.getCircle());  // Add the ball to the game board


		// Create and add paddle
		paddle = new Paddle();
		getChildren().add(paddle.getRectangle());  // Add the paddle to the game board

		// Reset variables
		numBottom = 0;
		numTeleported = 0;

		// Add and center text (whitespace to ensure label stays centered)
		final Label startLabel = new Label(message + "                    Click mouse to start                    ");
		startLabel.setTextAlignment(TextAlignment.CENTER);
		startLabel.setLayoutX((WIDTH / 2) - 135);
		startLabel.setLayoutY(HEIGHT / 2 + 100);
		getChildren().add(startLabel);

		// Add event handler to start the game
		setOnMouseClicked(e -> {
			GameImpl.this.setOnMouseClicked(null);

			// As soon as the mouse is clicked, remove the startLabel from the game board
			getChildren().remove(startLabel);
			run();
		});

		// Add event handler to steer paddle
		setOnMouseMoved(e -> paddle.moveTo(e.getX(), e.getY()));
	}

	/**
	 * Begins the game-play by creating and starting an AnimationTimer.
	 */
	private void run() {
		// Instantiate and start an AnimationTimer to update the component of the game.
		new AnimationTimer () {
			private long lastNanoTime = -1;
			public void handle (long currentNanoTime) {
				if (lastNanoTime >= 0) {  // Necessary for first clock-tick.
					GameState state;
					if ((state = runOneTimestep(currentNanoTime - lastNanoTime)) != GameState.ACTIVE) {
						// Once the game is no longer ACTIVE, stop the AnimationTimer.
						stop();
						// Restart the game, with a message that depends on whether
						// the user won or lost the game.
						restartGame(state);
					}
				}
				// Keep track of how much time actually transpired since the last clock-tick.
				lastNanoTime = currentNanoTime;
			}
		}.start();
	}

	/**
	 * Updates the state of the game at each time-step. In particular, this method should
	 * move the ball, check if the ball collided with any of the animals, walls, or the paddle, etc.
	 * @param deltaNanoTime how much time (in nanoseconds) has transpired since the last update
	 * @return the current game state
	 */
	private GameState runOneTimestep(long deltaNanoTime) {
		numBottom += ball.checkWallCollision(); // Check wall collision and log bottom hits
		numTeleported += ball.checkTeleportation(getPane()); // Check animal collision and log animal teleportations
		ball.checkPaddleCollision(paddle); // Check paddle collision

		if(numBottom >= BOTTOM_HITS) // If the bottom has been hit too many times, lose game
			return GameState.LOST;
		if(numTeleported >= 16) // If all 16 animals have been teleported, win game
			return GameState.WON;

		ball.updatePosition(deltaNanoTime); // Update ball position
		return GameState.ACTIVE;
	}
}
