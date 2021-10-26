import java.util.ArrayList;
import java.util.Optional;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class NPCWindow extends Stage {

	private BorderPane root = new BorderPane();
	private NPC selectedNPC;
	private TextArea display = new TextArea();
	private ArrayList<NPC> parentList = new ArrayList<>();
	private NPCManagementApp parent;

	public NPCWindow(NPC npc, NPCManagementApp app) {
		super();
		selectedNPC = npc;
		parent = app;
		this.setTitle(npc.getName());
		this.parentList = app.getNPCList();
	}

	public void openNewNPCWindow() {
		display.setEditable(false);
		refreshDisplay();
		root.setCenter(display);

		FlowPane bottom = new FlowPane();
		bottom.setHgap(8);
		Button newAttackBtn = new Button("New Attack");
		MenuButton selectAttack = new MenuButton("Select Attack");
		if (!selectedNPC.getAttacks().isEmpty()) {
			for (Attack x : selectedNPC.getAttacks()) {
				MenuItem newAttackOption = new MenuItem(x.getAttackName());
				newAttackOption.setOnAction(ave -> {
					AttackAlert myAttackWindow = new AttackAlert(x);
					myAttackWindow.showAndWait();
				});
				selectAttack.getItems().add(newAttackOption);
			}
		}
		newAttackBtn.setOnAction(event -> {
			try {
				NewAttackAlert dialogue = new NewAttackAlert();
				Optional<ButtonType> result = dialogue.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					String name = dialogue.getName();
					if (name.trim().isEmpty()) {
						showErrorMessage("Name can't be empty!");
					} else {
						String attackType = dialogue.getAttackType();
						String damageType = dialogue.getDamageType();
						int toHit = dialogue.getToHit();
						int damageDieType = dialogue.getDamageDieType();
						int damageDieNo = dialogue.getDamageDieNo();
						int damageModifier = dialogue.getDamageModifier();
						int shortRange = dialogue.getShortRange();
						int longRange = dialogue.getLongRange();
						Attack newAttack = new Attack(toHit, damageDieType, damageDieNo, damageModifier, name,
								attackType, damageType, shortRange, longRange);
						selectedNPC.addAttack(newAttack);
						MenuItem newAttackOption = new MenuItem(name);
						newAttackOption.setOnAction(ave -> {
							AttackAlert myAttackWindow = new AttackAlert(newAttack);
							myAttackWindow.showAndWait();
						});
						selectAttack.getItems().add(newAttackOption);
						refreshDisplay();
					}
				}
			} catch (NumberFormatException e) {
				showErrorMessage("Invalid input!");
			}
		});
		Button damageBtn = new Button("Damage");
		damageBtn.setOnAction(event -> {
			try {
				DamageAlert dialogue = new DamageAlert();
				Optional<ButtonType> result = dialogue.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					selectedNPC.takeDamage(dialogue.getDamage());
					refreshDisplay();
				}
			} catch (NumberFormatException e) {
				showErrorMessage("Invalid input!");
			}
		});
		Button cloneBtn = new Button("Clone");
		cloneBtn.setOnAction(event -> {
			NPC clone = selectedNPC.clone();
			parentList.add(clone);
			MenuItem cloneItem = new MenuItem(clone.getName());
			parent.addCloneToMenu(clone, cloneItem);
			display.appendText(selectedNPC.getName() + " has been cloned.");
		});
		bottom.getChildren().addAll(newAttackBtn, selectAttack, damageBtn, cloneBtn);
		root.setBottom(bottom);

		GridPane right = new GridPane();
		Button strengthSaveBtn = new Button("Strength Save");
		strengthSaveBtn.setOnAction(new SaveThrowHandler(selectedNPC.getAbilityScoreModifier(0)));
		Button dexteritySaveBtn = new Button("Dexterity Save");
		dexteritySaveBtn.setOnAction(new SaveThrowHandler(selectedNPC.getAbilityScoreModifier(1)));
		Button constitutionSaveBtn = new Button("Constitution Save");
		constitutionSaveBtn.setOnAction(new SaveThrowHandler(selectedNPC.getAbilityScoreModifier(2)));
		Button intelligenceSaveBtn = new Button("Intelligence Save");
		intelligenceSaveBtn.setOnAction(new SaveThrowHandler(selectedNPC.getAbilityScoreModifier(3)));
		Button wisdomSaveBtn = new Button("Wisdom Save");
		wisdomSaveBtn.setOnAction(new SaveThrowHandler(selectedNPC.getAbilityScoreModifier(4)));
		Button charismaSaveBtn = new Button("Charisma Save");
		charismaSaveBtn.setOnAction(new SaveThrowHandler(selectedNPC.getAbilityScoreModifier(5)));
		right.addRow(0, strengthSaveBtn, wisdomSaveBtn);
		right.addRow(1, dexteritySaveBtn, intelligenceSaveBtn);
		right.addRow(2, constitutionSaveBtn, charismaSaveBtn);
		root.setRight(right);

		GridPane top = new GridPane();
		top.addRow(0, new Label("Str: " + selectedNPC.getAbilityScore(0) + " "),
				new Label("Dex: " + selectedNPC.getAbilityScore(1) + " "),
				new Label("Con: " + selectedNPC.getAbilityScore(2) + " "),
				new Label("Int: " + selectedNPC.getAbilityScore(3) + " "),
				new Label("Wis: " + selectedNPC.getAbilityScore(4) + " "),
				new Label("Cha: " + selectedNPC.getAbilityScore(5) + " "));
		top.addRow(1, new Label(" +" + selectedNPC.getAbilityScoreModifier(0)),
				new Label(" +" + selectedNPC.getAbilityScoreModifier(1)),
				new Label(" +" + selectedNPC.getAbilityScoreModifier(2)),
				new Label(" +" + selectedNPC.getAbilityScoreModifier(3)),
				new Label(" +" + selectedNPC.getAbilityScoreModifier(4)),
				new Label(" +" + selectedNPC.getAbilityScoreModifier(5)));
		top.setAlignment(Pos.CENTER);
		root.setTop(top);

		Scene scene = new Scene(root);
		setScene(scene);
		show();
	}

	public void refreshDisplay() {
		display.clear();
		display.appendText(selectedNPC.toString() + "\n");
		if (!selectedNPC.getAttacks().isEmpty()) {
			for (Attack x : selectedNPC.getAttacks()) {
				display.appendText(x.toString() + "\n");
			}
		}
	}
	
	private void showErrorMessage(String message) {
		Alert msg = new Alert(AlertType.ERROR, message);
		msg.showAndWait();
	}

	private class SaveThrowHandler implements EventHandler<ActionEvent> {

		private int abilityScoreModifier;
		private Random diceRoller = new Random();

		public SaveThrowHandler(int abilityScoreModifier) {
			this.abilityScoreModifier = abilityScoreModifier;
		}

		@Override
		public void handle(ActionEvent event) {
			int roll = diceRoller.nextInt(20) + 1 + abilityScoreModifier;
			display.appendText(selectedNPC.getName() + " rolled a " + roll + "! \n");
		}

	}
}
