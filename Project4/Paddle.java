import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

class Paddle {
	// Constants
	/**
	 * The width of the paddle.
	 */
	private static final int PADDLE_WIDTH = 100;
	/**
	 * The height of the paddle.
	 */
	private static final int PADDLE_HEIGHT = 5;
	/**
	 * Half of the width of the paddle.
	 */
	private static final double HALF_WIDTH = PADDLE_WIDTH/2;
	/**
	 * Half of the width of the paddle.
	 */
	private static final double HALF_HEIGHT = PADDLE_HEIGHT/2;
	/**
	 * The initial position (specified as a fraction of the game height) of center of the paddle.
	 */
	private static final double INITIAL_Y_LOCATION_FRAC = 0.8;
	/**
	 * The minimum position (specified as a fraction of the game height) of center of the paddle.
	 */
	private static final double MIN_Y_LOCATION_FRAC = 0.7;
	/**
	 * The maximum position (specified as a fraction of the game height) of center of the paddle.
	 */
	private static final double MAX_Y_LOCATION_FRAC = 0.9;
	

	// Instance variables
	private Rectangle rectangle;

	/**
	 * @return the x coordinate of the center of the paddle.
	 */
	double getX() {
		return rectangle.getLayoutX() + rectangle.getTranslateX() + HALF_WIDTH;
	}

	/**
	 * @return the y coordinate of the center of the paddle.
	 */
	double getY() {
		return rectangle.getLayoutY() + rectangle.getTranslateY() + HALF_HEIGHT;
	}

	/**
	 * Constructs a new Paddle whose vertical center is at INITIAL_Y_LOCATION_FRAC * GameImpl.HEIGHT.
	 */
	Paddle() {
		rectangle = new Rectangle(0, 0, PADDLE_WIDTH, PADDLE_HEIGHT);
		rectangle.setLayoutX((GameImpl.WIDTH >> 1) - HALF_WIDTH);
		rectangle.setLayoutY((INITIAL_Y_LOCATION_FRAC * GameImpl.HEIGHT) - HALF_HEIGHT);
		rectangle.setStroke(Color.GREEN);
		rectangle.setFill(Color.GREEN);
	}

	/**
	 * @return the Rectangle object that represents the paddle on the game board.
	 */
	Rectangle getRectangle() {
		return rectangle;
	}

	/**
	 * Moves the paddle so that its center is at (newX, newY), subject to
	 * the horizontal constraint that the paddle must always be completely visible
	 * and the vertical constraint that its y coordiante must be between MIN_Y_LOCATION_FRAC
	 * and MAX_Y_LOCATION_FRAC times the game height.
	 * @param newX the newX position to move the center of the paddle.
	 * @param newY the newX position to move the center of the paddle.
	 */
	void moveTo(double newX, double newY) {
		if (newX < HALF_WIDTH) {
			newX = HALF_WIDTH;
		} else if (newX > GameImpl.WIDTH - HALF_WIDTH) {
			newX = GameImpl.WIDTH - HALF_WIDTH;
		}

		if (newY < MIN_Y_LOCATION_FRAC * GameImpl.HEIGHT) {
			newY = MIN_Y_LOCATION_FRAC * GameImpl.HEIGHT;
		} else if (newY > MAX_Y_LOCATION_FRAC * GameImpl.HEIGHT) {
			newY = MAX_Y_LOCATION_FRAC * GameImpl.HEIGHT;
		}

		rectangle.setTranslateX(newX - (rectangle.getLayoutX() + HALF_WIDTH));
		rectangle.setTranslateY(newY - (rectangle.getLayoutY() + HALF_HEIGHT));
	}
}
