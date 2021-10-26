import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class NewAttackAlert extends Alert {

	private TextField nameField = new TextField();
	private TextField attackTypeField = new TextField();
	private TextField damageTypeField = new TextField();
	private TextField toHitField = new TextField();
	private TextField damageDieTypeField = new TextField();
	private TextField damageDieNoField = new TextField();
	private TextField shortRangeField = new TextField();
	private TextField longRangeField = new TextField();
	private TextField damageModifierField = new TextField();

	public NewAttackAlert() {
		super(AlertType.CONFIRMATION);

		GridPane grid = new GridPane();
		grid.addRow(0, new Label("Attack Name: "), nameField, new Label("Attack Type: "), attackTypeField);
		grid.addRow(1, new Label("To Hit Bonus: "), toHitField, new Label("Damage: "), damageDieNoField, new Label("d"),
				damageDieTypeField, new Label(" + "), damageModifierField);
		grid.addRow(2, new Label("Damage Type: "), damageTypeField, new Label("Short Range/Long RangeF: "),
				shortRangeField, new Label("/"), longRangeField);
		getDialogPane().setContent(grid);
	}

	public String getName() {
		return nameField.getText();
	}

	public String getAttackType() {
		return attackTypeField.getText();
	}

	public String getDamageType() {
		return damageTypeField.getText();
	}

	public int getToHit() {
		return Integer.parseInt(toHitField.getText());
	}

	public int getDamageDieType() {
		return Integer.parseInt(damageDieTypeField.getText());
	}

	public int getDamageDieNo() {
		return Integer.parseInt(damageDieNoField.getText());
	}

	public int getDamageModifier() {
		return Integer.parseInt(damageModifierField.getText());
	}

	public int getShortRange() {
		return Integer.parseInt(shortRangeField.getText());
	}

	public int getLongRange() {
		return Integer.parseInt(longRangeField.getText());
	}
}
