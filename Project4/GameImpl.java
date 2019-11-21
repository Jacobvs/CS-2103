import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.control.Label;
import javafx.animation.AnimationTimer;

import java.util.ArrayList;
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
	public static final int WIDTH = 400;
	/**
	 * The height of the game board.
	 */
	public static final int HEIGHT = 600;

	public static final int BORDER = WIDTH/9;

	public static final int fourthX = (WIDTH - BORDER)/4;
	public static final int fourthY = (HEIGHT - BORDER)/8;

	// Instance variables
	private Ball ball;
	private Paddle paddle;
	private int numBottom, numTeleported;

	/**
	 * Constructs a new GameImpl.
	 */
	public GameImpl () {
		setStyle("-fx-background-color: white;");

		restartGame(GameState.NEW);
	}

	public String getName () {
		return "Zutopia";
	}

	public Pane getPane () {
		return this;
	}

	private void restartGame (GameState state) {
		getChildren().clear();  // remove all components from the game

		// Create and add ball
		ball = new Ball();
		getChildren().add(ball.getCircle());  // Add the ball to the game board

		// Create and add animals ...
		String[] fileNames = new String[]{"goat.jpg", "horse.jpg", "duck.jpg"};
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				final Image image = new Image(getClass().getResourceAsStream(fileNames[new Random().nextInt(3)]));
				Label imageLabel = new Label("", new ImageView(image));
				imageLabel.setLayoutX((fourthX*i)+BORDER);
				imageLabel.setLayoutY((fourthY*j)+BORDER);
				imageLabel.setId("animal");
				getChildren().add(imageLabel);
			}
		}

		// Create and add paddle
		paddle = new Paddle();
		getChildren().add(paddle.getRectangle());  // Add the paddle to the game board

		numBottom = 0;
		numTeleported = 0;

		// Add start message
		final String message;
		if (state == GameState.LOST) {
			message = "Game Over with " + (16 - numTeleported) + " animals remaining. \n";
		} else if (state == GameState.WON) {
			message = "You won!\n";
		} else {
			message = "";
		}
		final Label startLabel = new Label(message + "Click mouse to start");
		startLabel.setLayoutX(WIDTH / 2 - 50);
		startLabel.setLayoutY(HEIGHT / 2 + 100);
		getChildren().add(startLabel);

		// Add event handler to start the game
		setOnMouseClicked(e -> {
			GameImpl.this.setOnMouseClicked(null);

			// As soon as the mouse is clicked, remove the startLabel from the game board
			getChildren().remove(startLabel);
			run();
		});

		// Add another event handler to steer paddle...
		setOnMouseMoved(e -> paddle.moveTo(e.getX(), e.getY()));
	}

	/**
	 * Begins the game-play by creating and starting an AnimationTimer.
	 */
	public void run () {
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
						// TODO: add message
						restartGame(state);
					}
				}
				// Keep track of how much time actually transpired since the last clock-tick.
				lastNanoTime = currentNanoTime;
			}
		}.start();
	}

	/**
	 * Updates the state of the game at each timestep. In particular, this method should
	 * move the ball, check if the ball collided with any of the animals, walls, or the paddle, etc.
	 * @param deltaNanoTime how much time (in nanoseconds) has transpired since the last update
	 * @return the current game state
	 */
	public GameState runOneTimestep (long deltaNanoTime) {
		numBottom += ball.checkCollisions(getPane(), paddle);
		numTeleported += ball.checkTeleportation(getPane());

		if(numBottom >= 1)
			return GameState.LOST;
		if(numTeleported >= 16)
			return GameState.WON;

		ball.updatePosition(deltaNanoTime);
		return GameState.ACTIVE;
	}
}
