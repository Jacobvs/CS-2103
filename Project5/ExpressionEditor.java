import com.sun.javafx.tk.Toolkit;
import javafx.application.Application;
import javafx.scene.control.Label;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ExpressionEditor extends Application {
	public static void main (String[] args) {
		System.out.println(Toolkit.getToolkit().getFontLoader().getFontNames());
		launch(args);
	}

	/**
	 * Mouse event handler for the entire pane that constitutes the ExpressionEditor
	 */
	private class MouseEventHandler implements EventHandler<MouseEvent> {
		Pane pane;
		AbstractCompoundExpression root;
		Expression focus;
		MouseEventHandler (Pane pane_, CompoundExpression rootExpression_) {
			this.pane = pane_;
			this.root = (AbstractCompoundExpression) rootExpression_;
			this.focus = root;
		}

		public void handle (MouseEvent event) {
			if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
				System.out.println("PRESSED");
				if(focus instanceof AbstractCompoundExpression){
					if(!((AbstractCompoundExpression) focus).getSubexpressions().stream()
							.anyMatch(e -> {
								if(e.getNode().getBoundsInParent().intersects(event.getX(), event.getY(), 1, 1)){
									System.out.println(((Label) e.getNode()).getText());
									((Label) focus.getNode()).setBorder(Expression.NO_BORDER);
									focus = e;
									if(focus instanceof ParentheticalExpression) {
										System.out.println("PAREN");
										focus = ((ParentheticalExpression) focus).getSubexpressions().get(0);
										System.out.println(((Label) focus.getNode()).getText());
									}
									((Label) focus.getNode()).setBorder(Expression.RED_BORDER);
									return true;
								}
								return false;
							})){
						((Label) focus.getNode()).setBorder(Expression.NO_BORDER);
						focus = root;
					}
				}
				else{
					for(Expression e : root.getSubexpressions()){
						if(e.getNode().getBoundsInParent().intersects(event.getX(), event.getY(), 1, 1)){
							System.out.println(((Label) e.getNode()).getText());
							((Label) focus.getNode()).setBorder(Expression.NO_BORDER);
							((Label) e.getNode()).setBorder(Expression.RED_BORDER);
							focus = e;
							if(!(e instanceof LiteralExpression)) {
								((Label) focus.getNode()).setBorder(Expression.NO_BORDER);
								focus = root;
							}
							break;
						}
					}
				}
			} else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
				System.out.println("DRAGGED");
				((Label) focus.getNode()).setTextFill(Expression.GHOST_COLOR);
				focus = focus.deepCopy();
				focus.getNode().setLayoutX(event.getSceneX());
				focus.getNode().setLayoutY(event.getSceneY());
			} else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
				System.out.println("RELEASED");
				((Label) root.getNode()).setBorder(Expression.NO_BORDER);
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

	public static final String FONT = "Menlo";
	public static final int FONT_SIZE = 25;

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
					((Label) expression.getNode()).setBorder(Expression.NO_BORDER);
					final MouseEventHandler eventHandler = new MouseEventHandler(expressionPane, (CompoundExpression) expression);
					expressionPane.setOnMousePressed(eventHandler);
					expressionPane.setOnMouseDragged(eventHandler);
					expressionPane.setOnMouseReleased(eventHandler);
					//TODO: ADD 2nd LEVEL EXPRESSIONS
					placeNodes(expressionPane, expression, 0 , 0, false);
				}
				else{
					expressionPane.getChildren().add(expression.getNode());
					expression.getNode().setLayoutX(WINDOW_WIDTH/2);
					expression.getNode().setLayoutY(WINDOW_HEIGHT/2 - getTextSize(((Label) expression.getNode()), true));
					//TODO format text nicely
				}
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

	//TODO place all nodes invisibly at start recursively
	private void placeNodes(Pane pane, Expression root, double x, double y, boolean hidden){
		boolean firstNode = true;
		AbstractCompoundExpression ae = (AbstractCompoundExpression) root;
		Paint clear = Paint.valueOf(Color.color(0,0,0,0).toString());
		if (x == 0 && y == 0) {
			x = (WINDOW_WIDTH >> 1) - getTextSize(((Label) root.getNode()), false)/2;
			y = (WINDOW_HEIGHT >> 1) - getTextSize(((Label) root.getNode()), true);
		}
		if(root instanceof ParentheticalExpression){
			Label p = new Label("(");
			p.setFont(Font.font(FONT, FONT_SIZE));
			x += getTextSize(p, false);
		}
		for(Expression ce : ae.getSubexpressions()){
			if(firstNode){
				pane.getChildren().add(ce.getNode());
				ce.getNode().setLayoutY(y);
				ce.getNode().setLayoutX(x);
				if(hidden)
					((Label) ce.getNode()).setTextFill(clear);
				if(ce instanceof ParentheticalExpression)
					ce = ae.getSubexpressions().get(0);
				if(!(ce instanceof LiteralExpression))
					placeNodes(pane, ce, ce.getNode().getLayoutX(), ce.getNode().getLayoutY(), true);
				x += getTextSize((Label) ce.getNode(), false);
				firstNode = false;
			}
			else{
				Label o = new Label(ae.getOperator());
				o.setFont(Font.font(FONT, FONT_SIZE));
				pane.getChildren().add(o);
				o.setLayoutY(y);
				o.setLayoutX(x);
				if(hidden)
					o.setTextFill(clear);
				x += getTextSize(o, false);

				pane.getChildren().add(ce.getNode());
				ce.getNode().setLayoutY(y);
				ce.getNode().setLayoutX(x);
				if(hidden)
					((Label) ce.getNode()).setTextFill(clear);
				if(!(ce instanceof LiteralExpression))
					placeNodes(pane, ce, ce.getNode().getLayoutX(), ce.getNode().getLayoutY(), true);
				x += getTextSize((Label) ce.getNode(), false);
			}
		}
//		if(root instanceof ParentheticalExpression){
//			Label p = new Label(")");
//			p.setFont(Font.font(FONT, FONT_SIZE));
//			pane.getChildren().add(p);
//			p.setLayoutX(x);
//			p.setLayoutY(y);
//			if(hidden)
//				p.setTextFill(clear);
//		}
	}

	private double getTextSize(Label l, boolean height){
		Text txt = new Text(l.getText());
		txt.setFont(l.getFont());
		return height ? txt.getBoundsInLocal().getHeight() : txt.getBoundsInLocal().getWidth();
	}

	private void updateVisibleNodes(Pane pane, Expression focus){
		//((Label) focus.getNode()).setTextFill(Paint.valueOf(Color.color(0,0,0,0).toString()));
		//placeNodes(pane, focus, focus.getNode().getLayoutX(), focus.getNode().getLayoutY());
	}
}
