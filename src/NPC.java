import java.io.Serializable;
import java.util.ArrayList;

public class NPC implements Serializable{

	private String name;
	private int HP;
	private int AC;
	private int[] abilityScores;
	private ArrayList<Attack> attacks = new ArrayList<>();

	public NPC(String name, int HP, int AC, int[] abilityScores) {
		this.name = name;
		this.HP = HP;
		this.AC = AC;
		this.abilityScores = abilityScores;
	}

	public String getName() {
		return this.name;
	}

	public void takeDamage(int damage) {
		this.HP -= damage;
	}

	public void receiveHealing(int healing) {
		this.HP += healing;
	}

	public int getHP() {
		return this.HP;
	}

	public int getAbilityScore(int abilityIndex) {
		if (abilityIndex >= 0 || abilityIndex <= abilityScores.length) {
			return abilityScores[abilityIndex];
		} else {
			return -1;
		}
	}
	
	public int getAbilityScoreModifier(int abilityIndex) {
		if(abilityIndex >= 0||abilityIndex <= abilityScores.length) {
			double tempModifier = abilityScores[abilityIndex] - 10;
			tempModifier /= 2;
			Math.floor(tempModifier);
			int abilityScoreModifier = (int) tempModifier;
			return abilityScoreModifier;
		} else {
			return -9001;
		}
	}

	public NPC clone() {
		NPC clone = new NPC(name, HP, AC, abilityScores);
		if (!attacks.isEmpty()) {
			for (Attack myAttack : attacks) {
				clone.addAttack(myAttack);
			}
		}
		return clone;
	}

	public void addAttack(Attack attackToAdd) {
		attacks.add(attackToAdd);
	}

	public boolean removeAttack(Attack attackToRemove) {
		return attacks.remove(attackToRemove);
	}

	public ArrayList<Attack> getAttacks() {
		return attacks;
	}

	public void makeAttackRoll(String attackName) {
		if (!attacks.isEmpty()) {
			for (Attack myAttack : attacks) {
				if (myAttack.getAttackName().equalsIgnoreCase(attackName)) {
					int attackRoll = myAttack.attackRoll();
					if (attackRoll - myAttack.getToHit() == 20) {
						System.out.println(name + " rolled a critical hit!");
					} else if (!(attackRoll - myAttack.getToHit() == 1)) {
						System.out.println(name + " rolled " + attackRoll + " to hit!");
					} else {
						System.out.println(name + "rolled a nat 1!");
					}
				}
			}
		}
	}

	public void makeDamageRoll(String attackName) {
		if (!attacks.isEmpty()) {
			for (Attack myAttack : attacks) {
				if (myAttack.getAttackName().equalsIgnoreCase(attackName)) {
					System.out.println(
							name + " deals " + myAttack.damageRoll() + " " + myAttack.getDamageType() + " damage!");
				}
			}
		}
	}

	public void makeCriticalDamageRoll(String attackName) {
		if (!attacks.isEmpty()) {
			for (Attack myAttack : attacks) {
				if (myAttack.getAttackName().equalsIgnoreCase(attackName)) {
					System.out.println(name + " deals " + myAttack.criticalDamageRoll() + " " + myAttack.getDamageType()
							+ " damage!");
				}
			}
		}
	}

	@Override
	public String toString() {
		return (name + ", HP: " + HP + ", AC: " + AC);
	}
}
