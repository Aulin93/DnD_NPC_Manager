import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import java.io.*;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class NPCManagementApp extends Application {

	private ArrayList<NPC> NPCList = new ArrayList<>();
	private ArrayList<Button> selectedNPCDependentButtons = new ArrayList<>();
	private Map<NPC, ArrayList<MenuItem>> selectedNPCAttacks = new HashMap<>();
	private MenuButton selectNPC = new MenuButton("Select NPC");
	private MenuButton selectAttack = new MenuButton("Select Attack");
	private NPC selectedNPC;
	private TextArea display = new TextArea();

	public void start(Stage primaryStage) {

		BorderPane root = new BorderPane();
		display.setEditable(false);
		root.setCenter(display);

		GridPane right = new GridPane();
		right.setAlignment(Pos.CENTER);
		right.setPadding(new Insets(5));
		right.setVgap(5);
		Button strengthSaveBtn = new Button("Strength Save");
		strengthSaveBtn.setOnAction(new SaveThrowHandler(0));
		selectedNPCDependentButtons.add(strengthSaveBtn);
		Button dexteritySaveBtn = new Button("Dexterity Save");
		dexteritySaveBtn.setOnAction(new SaveThrowHandler(1));
		selectedNPCDependentButtons.add(dexteritySaveBtn);
		Button constitutionSaveBtn = new Button("Constitution Save");
		constitutionSaveBtn.setOnAction(new SaveThrowHandler(2));
		selectedNPCDependentButtons.add(constitutionSaveBtn);
		Button intelligenceSaveBtn = new Button("Intelligence Save");
		intelligenceSaveBtn.setOnAction(new SaveThrowHandler(3));
		selectedNPCDependentButtons.add(intelligenceSaveBtn);
		Button wisdomSaveBtn = new Button("Wisdom Save");
		wisdomSaveBtn.setOnAction(new SaveThrowHandler(4));
		selectedNPCDependentButtons.add(wisdomSaveBtn);
		Button charismaSaveBtn = new Button("Charisma Save");
		charismaSaveBtn.setOnAction(new SaveThrowHandler(5));
		selectedNPCDependentButtons.add(charismaSaveBtn);
		Button damageBtn = new Button("Damage");
		selectedNPCDependentButtons.add(damageBtn);
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
		right.addRow(0, strengthSaveBtn, wisdomSaveBtn);
		right.addRow(1, dexteritySaveBtn, intelligenceSaveBtn);
		right.addRow(2, constitutionSaveBtn, charismaSaveBtn);
		right.add(damageBtn, 0, 3);
		root.setRight(right);

		ListView<NPC> NPCListView = new ListView<>();
		NPCListView.getSelectionModel().selectedItemProperty().addListener((obs, old, nev) -> {
			selectedNPC = nev;
			if (selectedNPC == null) {
				toggleDependantButtons(true);
				selectAttack.setDisable(true);
			} else {
				toggleDependantButtons(false);
				refreshDisplay();
				selectAttack.setDisable(false);
				selectAttack.getItems().clear();
				selectAttack.getItems().addAll(selectedNPCAttacks.get(selectedNPC));
			}

		});
		root.setLeft(NPCListView);

		FlowPane bottom = new FlowPane();
		root.setBottom(bottom);
		bottom.setAlignment(Pos.CENTER);
		bottom.setHgap(8);
		Button newNPCBtn = new Button("New NPC");
		newNPCBtn.setOnAction(event -> {
			try {
				NewNPCAlert dialogue = new NewNPCAlert();
				Optional<ButtonType> result = dialogue.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					String name = dialogue.getName();
					if (name.trim().isEmpty()) {
						showErrorMessage("Name can't be empty");
					} else {
						int AC = dialogue.getAC();
						if (AC < 0) {
							showErrorMessage("Invalid AC!");
						} else {
							int HP = dialogue.getHP();
							if (HP <= 0) {
								showErrorMessage("Invalid HP!");
							} else {
								int[] abilityScores = dialogue.getAbilityScores();
								boolean abilityScoreIsValid = true;
								for (int i = 0; i < abilityScores.length; i++) {
									if (abilityScores[i] <= 0) {
										abilityScoreIsValid = false;
									}
								}
								if (!abilityScoreIsValid) {
									showErrorMessage("Invalid Ability Scores");
								} else {
									NPC newNPC = new NPC(name, HP, AC, abilityScores);
									NPCList.add(newNPC);
									NPCListView.getItems().add(newNPC);
									MenuItem npcItem = new MenuItem(name);
									npcItem.setOnAction(ave -> {
										NPCWindow myWindow = new NPCWindow(newNPC, this);
										myWindow.openNewNPCWindow();
									});
									selectNPC.getItems().add(npcItem);
									selectedNPCAttacks.put(newNPC, new ArrayList<>());
								}
							}
						}
					}
				}
			} catch (NumberFormatException e) {
				showErrorMessage("Invalid input!");
			}
		});
		Button showBtn = new Button("Show");
		showBtn.setOnAction(event -> {
			display.clear();
			for (NPC x : NPCList) {
				display.appendText(x.toString() + "\n");
			}
		});
		Button newAttackBtn = new Button("New Attack");
		selectedNPCDependentButtons.add(newAttackBtn);
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
						if (attackType.trim().isEmpty()) {
							showErrorMessage("Please type an attack type");
						} else {
							String damageType = dialogue.getDamageType();
							if (damageType.trim().isEmpty()) {
								showErrorMessage("Please type a damage type");
							} else {
								int toHit = dialogue.getToHit();
								int damageDieType = dialogue.getDamageDieType();
								if (damageDieType < 1) {
									showErrorMessage("Invalid die type");
								} else {
									int damageDieNo = dialogue.getDamageDieNo();
									if (damageDieNo < 0) {
										showErrorMessage("Invalid number of dice");
									} else {
										int damageModifier = dialogue.getDamageModifier();
										int shortRange = dialogue.getShortRange();
										int longRange = dialogue.getLongRange();
										if (shortRange < 0 || longRange < 0 || longRange < shortRange) {
											showErrorMessage("Invalid ranges");
										} else {
											Attack newAttack = new Attack(toHit, damageDieType, damageDieNo,
													damageModifier, name, attackType, damageType, shortRange,
													longRange);
											selectedNPC.addAttack(newAttack);
											MenuItem newAttackOption = new MenuItem(name);
											newAttackOption.setOnAction(ave -> {
												AttackAlert myAttackWindow = new AttackAlert(newAttack);
												myAttackWindow.showAndWait();
											});
											selectAttack.getItems().add(newAttackOption);
											selectedNPCAttacks.get(selectedNPC).add(newAttackOption);
											refreshDisplay();
										}
									}
								}
							}
						}
					}
				}
			} catch (NumberFormatException e) {
				showErrorMessage("Invalid input!");
			}
		});
		bottom.getChildren().addAll(newNPCBtn, selectNPC, showBtn, newAttackBtn, selectAttack);

		FlowPane top = new FlowPane();
		root.setTop(top);
		top.setPadding(new Insets(5));
		top.setHgap(8);
		top.setAlignment(Pos.CENTER);
		Button loadBtn = new Button("Load");
		loadBtn.setOnAction(ave -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Data Files:", "*.dat"));
			File selectedFile = fileChooser.showOpenDialog(primaryStage);
			if (selectedFile != null) {
				try {
					FileInputStream fis = new FileInputStream(selectedFile);
					ObjectInputStream ois = new ObjectInputStream(fis);
					NPCList.addAll((ArrayList) ois.readObject());
					for (NPC x : NPCList) {
						ArrayList<MenuItem> attackItems = new ArrayList<>();
						for (Attack y : x.getAttacks()) {
							MenuItem newAttackOption = new MenuItem(y.getAttackName());
							newAttackOption.setOnAction(event -> {
								AttackAlert myAttackWindow = new AttackAlert(y);
								myAttackWindow.showAndWait();
							});
							attackItems.add(newAttackOption);
						}
						selectedNPCAttacks.put(x, attackItems);
					}
					ois.close();
					fis.close();
					NPCListView.getItems().addAll(NPCList);
				} catch (FileNotFoundException e) {
					showErrorMessage("Read Error: " + e.getMessage());
				} catch (ClassNotFoundException e) {
					showErrorMessage("Read Error: " + e.getMessage());
				} catch (IOException e) {
					showErrorMessage("Read Error:" + e.getMessage());
				}
			}
		});
		Button saveBtn = new Button("Save");
		saveBtn.setOnAction(ave -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.getExtensionFilters().add(new ExtensionFilter("Data Files:", "*.dat"));
			File selectedFile = fileChooser.showSaveDialog(primaryStage);
			if (selectedFile != null) {
				try {
					FileOutputStream fos = new FileOutputStream(selectedFile);
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(NPCList);
					oos.close();
					fos.close();
				} catch (FileNotFoundException e) {
					showErrorMessage("Read Error: " + e.getMessage());
				} catch (IOException e) {
					showErrorMessage("Read Error:" + e.getMessage());
				}
			}
		});
		top.getChildren().addAll(loadBtn, saveBtn);

		toggleDependantButtons(true);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("NPC Manager");
		primaryStage.show();

	}

	public ArrayList<NPC> getNPCList() {
		return NPCList;
	}

	public void addCloneToMenu(NPC clone, MenuItem cloneItem) {
		cloneItem.setOnAction(event -> {
			NPCWindow myWindow = new NPCWindow(clone, this);
			myWindow.openNewNPCWindow();
		});
		selectNPC.getItems().add(cloneItem);
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

	private void toggleDependantButtons(boolean disableButtons) {
		for (Button x : selectedNPCDependentButtons) {
			x.setDisable(disableButtons);
		}
	}

	private void showErrorMessage(String message) {
		Alert msg = new Alert(AlertType.ERROR, message);
		msg.showAndWait();
	}

	private class SaveThrowHandler implements EventHandler<ActionEvent> {

		private int abilityScoreModifierIndex;
		private Random diceRoller = new Random();

		public SaveThrowHandler(int abilityScoreModifierIndex) {
			this.abilityScoreModifierIndex = abilityScoreModifierIndex;
		}

		@Override
		public void handle(ActionEvent event) {
			int roll = diceRoller.nextInt(20) + 1 + selectedNPC.getAbilityScoreModifier(abilityScoreModifierIndex);
			display.appendText(selectedNPC.getName() + " rolled a " + roll + "! \n");
		}

	}

	public static void main(String[] args) {
		launch(args);
	}
}
