import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;

public class DamageAlert extends Alert {

	private TextField damageField = new TextField();

	public DamageAlert() {
		super(AlertType.CONFIRMATION);

		FlowPane flow = new FlowPane();
		flow.getChildren().addAll(new Label("Damage: "), damageField);
		getDialogPane().setContent(flow);
	}

	public int getDamage() {
		return Integer.parseInt(damageField.getText());
	}
}
