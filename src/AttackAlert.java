import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class AttackAlert extends Alert {

	private Label label = new Label("Click a button to make a roll");
	private Button attackRollBtn = new Button("Attack Roll");
	private Button damageRollBtn = new Button("Damage Roll");

	public AttackAlert(Attack attack) {
		super(AlertType.CONFIRMATION);

		attackRollBtn.setOnAction(event -> {
			label.setText("" + attack.attackRoll() + " to hit!");
		});
		damageRollBtn.setOnAction(
				event -> label.setText("" + attack.damageRoll() + " " + attack.getDamageType() + " damage!"));

		GridPane grid = new GridPane();
		grid.addRow(0, label);
		grid.addRow(1, attackRollBtn, damageRollBtn);
		getDialogPane().setContent(grid);
	}
}
