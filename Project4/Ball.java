import java.util.ArrayList;

import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

/**
 * Class that implements a ball with a position and velocity.
 */
public class Ball {
	// Constants
	/**
	 * The radius of the ball.
	 */
	public static final int BALL_RADIUS = 8;
	/**
	 * The initial velocity of the ball in the x direction.
	 */
	public static final double INITIAL_VX = 1e-7;
	/**
	 * The initial velocity of the ball in the y direction.
	 */
	public static final double INITIAL_VY = 1e-7;

	public static final double SPEED_INCREASE = 1.13;

	// Instance variables
	// (x,y) is the position of the center of the ball.
	private double x, y;
	private double vx, vy;
	private Circle circle;
	private Boolean pColliding;


	/**
	 * @return the Circle object that represents the ball on the game board.
	 */
	public Circle getCircle () {
		return circle;
	}

	/**
	 * Constructs a new Ball object at the centroid of the game board
	 * with a default velocity that points down and right.
	 */
	public Ball () {
		x = GameImpl.WIDTH/2;
		y = GameImpl.HEIGHT/2;
		vx = INITIAL_VX;
		vy = INITIAL_VY;


		circle = new Circle(BALL_RADIUS, BALL_RADIUS, BALL_RADIUS);
		circle.setLayoutX(x - BALL_RADIUS);
		circle.setLayoutY(y - BALL_RADIUS);
		circle.setFill(Color.BLACK);
	}

	/**
	 * Updates the position of the ball, given its current position and velocity,
	 * based on the specified elapsed time since the last update.
	 * @param deltaNanoTime the number of nanoseconds that have transpired since the last update
	 */
	public void updatePosition (long deltaNanoTime) {
		double dx = vx * deltaNanoTime;
		double dy = vy * deltaNanoTime;
		x += dx;
		y += dy;

		System.out.println("x : " + x + " y: " + y);


		circle.setTranslateX(x - (circle.getLayoutX() + BALL_RADIUS));
		circle.setTranslateY(y - (circle.getLayoutY() + BALL_RADIUS));
	}

	public int checkCollisions(Pane pane, Paddle paddle){
		//TODO: fix repeated bounces off walls (don't inverse velocity again till off paddle/wall)
		Boolean left, right, top, bottom;

		// If at left border inverse x velocity
		right = (x >= (400 - BALL_RADIUS));
		left = (x <= BALL_RADIUS);
		// If at top border inverse y velocity
		top = (y <= BALL_RADIUS);
		// If at bottom border increase counter & inverse y velocity
		bottom = (y >= (600 - BALL_RADIUS));

		if(right && vx > 0)
			vx *= -1;
		else if(left && vx < 0)
			vx *= -1;
		else if(top && vy < 0)
			vy *= -1;
		else if(bottom && vy > 0)
			vy *= -1;

		if(bottom)
		    return 1;

		// Check if ball is colliding with paddle
		if(this.circle.getBoundsInParent().intersects(paddle.getRectangle().getBoundsInParent())) {
		    if(!pColliding)
                vy *= -1;
        }
		else
		    pColliding = false;

		return 0; // Did not touch bottom
	}

	public int checkTeleportation(Pane pane){
		// if animal teleported, set vx and vy to 1.25x speed
		for(Node n : pane.getChildren())
			if(n.getId() != null){
				if(this.circle.getBoundsInParent().intersects(n.getBoundsInParent())){
					vx *= SPEED_INCREASE;
					vy *= SPEED_INCREASE;
					if(this.x > n.getLayoutX() || this.x < n.getLayoutY()){
						vx *= -1;
						pane.getChildren().remove(n);
						return 1;
					}
					else{
						vy *= -1;
						pane.getChildren().remove(n);
						return 1;
					}
				}
			}
		return 0;
	}
}
