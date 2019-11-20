import java.awt.*;
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

	// Instance variables
	// (x,y) is the position of the center of the ball.
	private double x, y;
	private double vx, vy;
	private Circle circle;
	private Boolean colliding;

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

		colliding = false;

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

		circle.setTranslateX(x - (circle.getLayoutX() + BALL_RADIUS));
		circle.setTranslateY(y - (circle.getLayoutY() + BALL_RADIUS));
	}

	public int checkCollisions(Pane pane, Paddle paddle){
		//TODO: fix repeated bounces off walls (add boolean to check if still on wall/paddle)
		final Bounds bounds = pane.getBoundsInLocal();

		// If at right or left border inverse x velocity
		if(x >= (bounds.getMaxX() - BALL_RADIUS) || x <= (bounds.getMinX() + BALL_RADIUS))
			vx *= -1;
		// If at top border inverse y velocity
		else if(y <= (bounds.getMinY() + BALL_RADIUS))
			vy*= -1;
		// If at bottom border increase counter & inverse y velocity
		else if(y >= (bounds.getMaxY() - BALL_RADIUS)){
			vy *= -1;
			return 1; // Touched bottom
		}

		// Check if ball is colliding with paddle
		if(this.circle.getBoundsInParent().intersects(paddle.getRectangle().getBoundsInParent()))
			vy *= -1;

		return 0; // Did not touch bottom
	}

	public int checkTeleportation(Pane pane, ArrayList<String> animalIds){
		// if animal teleported, set vx and vy to 1.25x speed
		for(Node n : pane.getChildren())
			if(animalIds.contains(n.getId())){
				if(this.circle.getBoundsInParent().intersects(n.getBoundsInParent())){
					System.out.println("Hit animal");
					vx *= 1.1;
					vy *= 1.1;
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
