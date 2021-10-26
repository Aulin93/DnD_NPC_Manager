import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class NewNPCAlert extends Alert {

	private TextField nameField = new TextField();
	private TextField ACField = new TextField();
	private TextField HPField = new TextField();
	private TextField strengthField = new TextField();
	private TextField dexterityField = new TextField();
	private TextField constitutionField = new TextField();
	private TextField intelligenceField = new TextField();
	private TextField wisdomField = new TextField();
	private TextField charismaField = new TextField();

	public NewNPCAlert() {
		super(AlertType.CONFIRMATION);

		GridPane grid = new GridPane();
		grid.addRow(0, new Label("Name: "), nameField);
		grid.addRow(1, new Label("AC: "), ACField);
		grid.addRow(2, new Label("HP: "), HPField);
		grid.addRow(3, new Label("Strength: "), strengthField, new Label("Wisdom: "), wisdomField);
		grid.addRow(4, new Label("Dexterity: "), dexterityField, new Label("Intelligence: "), intelligenceField);
		grid.addRow(5, new Label("Constitution: "), constitutionField, new Label("Charisma: "), charismaField);
		getDialogPane().setContent(grid);
	}

	public String getName() {
		return nameField.getText();
	}

	public int getAC() {
		return Integer.parseInt(ACField.getText());
	}

	public int getHP() {
		return Integer.parseInt(HPField.getText());
	}

	public int[] getAbilityScores() {
		int[] abilityScores = new int[6];
		abilityScores[0] = Integer.parseInt(strengthField.getText());
		abilityScores[1] = Integer.parseInt(dexterityField.getText());
		abilityScores[2] = Integer.parseInt(constitutionField.getText());
		abilityScores[3] = Integer.parseInt(intelligenceField.getText());
		abilityScores[4] = Integer.parseInt(wisdomField.getText());
		abilityScores[5] = Integer.parseInt(charismaField.getText());
		return abilityScores;
	}
}
