import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.List;

public class ExpressionEditor extends Application {
	public static void main (String[] args) {
		launch(args);
	}

	/**
	 * Mouse event handler for the entire pane that constitutes the ExpressionEditor
	 */
	private static class MouseEventHandler implements EventHandler<MouseEvent> {
		Pane pane;
		Expression root, focus, focusCopy;
		Node focusN;
		boolean lastDragged;
		MouseEventHandler (Pane pane_, CompoundExpression rootExpression_) {
			this.pane = pane_;
			this.focus = this.root = rootExpression_;
			this.focusN = focus.getNode();
			this.focusCopy = null;
			this.lastDragged = false;
		}

		public void handle (MouseEvent event) {
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				System.out.println("PRESSED");

			} else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				//System.out.println("DRAGGED");
				if(focus != root) {
					if (focusCopy == null) {
						focusN.setOpacity(Expression.GHOST_OPACITY);
						focusCopy = focus.deepCopy(null);
						pane.getChildren().add(focusCopy.getNode());
					}
					focusCopy.getNode().setLayoutX(event.getX());
					focusCopy.getNode().setLayoutY(event.getY());

					List<Expression> ln = ((AbstractCompoundExpression) focus.getParent()).getSubexpressions();
					double closestVal = WINDOW_WIDTH;
					int index = 0;
					Node closestN = null;
					for (int i = 0, lnSize = ln.size(); i < lnSize; i++) {
						Expression e = ln.get(i);
						Node n = e.getNode();
						if(e instanceof ParentheticalExpression)
							continue;
						double xn = n.getBoundsInParent().getMinX();
						double x = event.getX();
						if (Math.abs(x - xn) < closestVal) {
							closestVal = x - xn;
							closestN = n;
							index = i;
						}
					}
					if(event.getX() > closestN.getBoundsInParent().getMinX() || event.getX() < closestN.getBoundsInParent().getMinX()){
						ObservableList<Node> nl = ((HBox) focus.getParent().getNode()).getChildren();
						List<Expression> el = ((AbstractCompoundExpression) focus.getParent()).getSubexpressions();
						ObservableList<Node> nlc = FXCollections.observableArrayList(nl);
						Collections.swap(nlc, index, nl.indexOf(focus.getNode()));
						nl.setAll(nlc);
					}
				}

			} else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
				System.out.println("RELEASED");
				if(focusCopy != null){
					pane.getChildren().remove(focusCopy.getNode());
					focusCopy = null;
					focusN.setOpacity(1);
				}
				if(focusN instanceof HBox) {
					ObservableList<Node> children = ((HBox) focus.getNode()).getChildren();
					for (int i = 0; i < children.size(); i++) {
						Node n = children.get(i);
						if (n.contains(n.sceneToLocal(event.getSceneX(), event.getSceneY()))) {
							if (n instanceof Label) {
								String val = ((Label) n).getText();
								if (val.equals("*") || val.equals("+") || val.equals("(") || val.equals(")"))
									continue;
							}
							((Region) focusN).setBorder(Expression.NO_BORDER);
							((Region) n).setBorder(Expression.RED_BORDER);
							focusN = n;
							//update focus
							if (focus instanceof AbstractCompoundExpression) {
								AbstractCompoundExpression ae = (AbstractCompoundExpression) focus;
								for (Expression e : ae.getSubexpressions()) {
									if (e.getNode().equals(n))
										focus = e;
								}
							}
							return;
						}
					}
				}
				this.focus = root;
				((Region) focusN).setBorder(Expression.NO_BORDER);
				this.focusN = root.getNode();
			}
		}
	}

	/**
	 * Size of the GUI
	 */
	private static final int WINDOW_WIDTH = 500, WINDOW_HEIGHT = 250;

	/**
	 * Initial expression shown in the textbox
	 */
	private static final String EXAMPLE_EXPRESSION = "2*x+3*y+4*z+(7+6*z)";

	public static final Font FONT = new Font("Menlo", 30);

	/**
	 * Parser used for parsing expressions.
	 */
	private final ExpressionParser expressionParser = new SimpleExpressionParser();

	@Override
	public void start (Stage primaryStage) {
		primaryStage.setTitle("Expression Editor");

		// Add the textbox and Parser button
		final Pane queryPane = new HBox();
		final TextField textField = new TextField(EXAMPLE_EXPRESSION);
		final Button button = new Button("Parse");
		queryPane.getChildren().add(textField);

		final Pane expressionPane = new Pane();

		// Add the callback to handle when the Parse button is pressed	
		button.setOnMouseClicked(e -> {
			// Try to parse the expression
			try {
				// Success! Add the expression's Node to the expressionPane
				final Expression expression = expressionParser.parse(textField.getText(), true);
				System.out.println(expression.convertToString(0));
				expressionPane.getChildren().clear();

				// If the parsed expression is a CompoundExpression, then register some callbacks
				if (expression instanceof CompoundExpression) {
					//((Label) expression.getNode()).setBorder(Expression.NO_BORDER);
					final MouseEventHandler eventHandler = new MouseEventHandler(expressionPane, (CompoundExpression) expression);
					expressionPane.setOnMousePressed(eventHandler);
					expressionPane.setOnMouseDragged(eventHandler);
					expressionPane.setOnMouseReleased(eventHandler);
					//TODO: ADD 2nd LEVEL EXPRESSIONS
					//placeNodes(expressionPane, expression, 0 , 0, false);
				}
				expressionPane.getChildren().add(expression.getNode());
				expression.getNode().setLayoutX((WINDOW_WIDTH >> 1) - getTextSize(expression.getVal(),false)/2);
				expression.getNode().setLayoutY((WINDOW_HEIGHT>>1) - getTextSize(expression.getVal(),true)/2);
				//TODO format text nicely

			} catch (ExpressionParseException epe) {
				// If we can't parse the expression, then mark it in red
				textField.setStyle("-fx-text-fill: red");
			}
		});
		queryPane.getChildren().add(button);

		// Reset the color to black whenever the user presses a key
		textField.setOnKeyPressed(e -> textField.setStyle("-fx-text-fill: black"));
		
		final BorderPane root = new BorderPane();
		root.setTop(queryPane);
		root.setCenter(expressionPane);

		primaryStage.setScene(new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT));
		primaryStage.show();
	}

	private double getTextSize(String s, boolean height){
		Text txt = new Text(s);
		txt.setFont(FONT);
		return height ? txt.getBoundsInLocal().getHeight() : txt.getBoundsInLocal().getWidth();
	}
}
