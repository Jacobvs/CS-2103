import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Class that implements a ball with a position and velocity.
 */
public class Ball {
	// Constants
	/**
	 * The radius of the ball.
	 */
	private static final int BALL_RADIUS = 8;
	/**
	 * The initial velocity of the ball in the x direction.
	 */
	private static final double INITIAL_VX = 15e-8;
	/**
	 * The initial velocity of the ball in the y direction.
	 */
	private static final double INITIAL_VY = 15e-8;
	/**
	 * The increase in velocity per animal teleported.
	 */
	private static final double SPEED_INCREASE = 1.07;

	// Instance variables
	// (x,y) is the position of the center of the ball.
	private double x, y;
	private double vx, vy;
	private Circle circle;
	private final AudioClip paddleSound, teleportSound;

	/**
	 * @return the Circle object that represents the ball on the game board.
	 */
	Circle getCircle() {
		return circle;
	}

	/**
	 * Constructs a new Ball object at the centroid of the game board
	 * with a default velocity that points down and right.
	 */
	Ball() {
		x = GameImpl.WIDTH/2;
		y = GameImpl.HEIGHT/2;
		vx = INITIAL_VX;
		vy = INITIAL_VY;


		circle = new Circle(BALL_RADIUS, BALL_RADIUS, BALL_RADIUS);
		circle.setLayoutX(x - BALL_RADIUS);
		circle.setLayoutY(y - BALL_RADIUS);
		circle.setFill(Color.BLACK);
		paddleSound = new AudioClip(getClass().getClassLoader().getResource("paddle.wav").toString());
		teleportSound = new AudioClip(getClass().getClassLoader().getResource("teleport.wav").toString());
	}

	/**
	 * Updates the position of the ball, given its current position and velocity,
	 * based on the specified elapsed time since the last update.
	 * @param deltaNanoTime the number of nanoseconds that have transpired since the last update
	 */
	void updatePosition(long deltaNanoTime) {
		double dx = vx * deltaNanoTime;
		double dy = vy * deltaNanoTime;
		x += dx;
		y += dy;

		circle.setTranslateX(x - (circle.getLayoutX() + BALL_RADIUS));
		circle.setTranslateY(y - (circle.getLayoutY() + BALL_RADIUS));
	}

	/**
	 * Checks if ball has hit a wall, if so inverse velocity appropriately.
	 * @return 1 if ball has hit the bottom wall, 0 if not.
	 */
	int checkWallCollision(){
		// For each case, check that the edge of the ball (x/y + radius) has crossed the edge
		// If at left or right border, inverse x velocity
		if(((x >= (GameImpl.WIDTH - BALL_RADIUS)) && vx > 0) || ((x <= BALL_RADIUS) && vx < 0))
			vx *= -1;
		// If at top border, inverse y velocity
		else if((y <= BALL_RADIUS) && vy < 0)
			vy *= -1;
		// If at bottom border increase counter & inverse y velocity
		else if((y >= (GameImpl.HEIGHT - BALL_RADIUS)) && vy > 0) {
			vy *= -1;
			return 1;
		}
		return 0; // Did not touch bottom
	}

	/**
	 * Checks if ball has hit the paddle, if so inverse velocity appropriately.
	 * @param paddle Paddle object.
	 */
	void checkPaddleCollision(Paddle paddle){
		// Check if ball is colliding with paddle
		if(this.circle.getBoundsInParent().intersects(paddle.getRectangle().getBoundsInParent())) {
			paddleSound.play();
			if(y < paddle.getY() && vy > 0)
				vy *= -1;
			else if(y > paddle.getY() && vy < 0)
				vy *= -1;
		}
	}

	/**
	 * Checks if ball has hit an animal, if so teleport it.
	 * @param pane Pane object.
	 * @return 1 if animal has been teleported, 0 if not.
	 */
	int checkTeleportation(Pane pane){
		// if animal teleported, increase velocity
		for(Node n : pane.getChildren())
			if(n.getId() != null){
				Bounds bound = n.getBoundsInParent();
				if(this.circle.getBoundsInParent().intersects(bound)){
					vx *= SPEED_INCREASE;
					vy *= SPEED_INCREASE;

					teleportSound.play();
					pane.getChildren().remove(n);

					if(((x >= (bound.getMinX() - BALL_RADIUS)) && vx > 0) || ((x <= (bound.getMaxX() + BALL_RADIUS)) && vx < 0))
						vx *= -1;
					else
						vy *= -1;
					return 1;
				}
			}
		return 0;
	}
}
