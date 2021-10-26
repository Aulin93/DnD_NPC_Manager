import java.io.Serializable;
import java.util.Random;

public class Attack implements Serializable{

	private int toHit, damageDieType, damageDieNo, damageModifier, shortRange, longRange;
	private String attackType, damageType, attackName;
	private Random diceRoller = new Random();

	public Attack(int toHit, int damageDieType, int damageDieNo, int damageModifier, String attackName,
			String attackType, String damageType, int shortRange, int longRange) {
		this.toHit = toHit;
		this.damageDieType = damageDieType;
		this.damageDieNo = damageDieNo;
		this.damageModifier = damageModifier;
		this.attackName = attackName;
		this.attackType = attackType;
		this.damageType = damageType;
		this.shortRange = shortRange;
		this.longRange = longRange;
	}

	public Attack(int toHit, int damageDieType, int damageDieNo, int damageModifier, String attackName,
			String attackType, String damageType) {
		this(toHit, damageDieType, damageDieNo, damageModifier, attackName, attackType, damageType, 5, 5);
	}

	public Attack(int toHit, int damageDieType, int damageDieNo, int damageModifier, String attackName,
			String attackType, String damageType, int range) {
		this(toHit, damageDieType, damageDieNo, damageModifier, attackName, attackType, damageType, range, range);
	}

	public String getAttackName() {
		return this.attackName;
	}

	public int getToHit() {
		return this.toHit;
	}

	public String getDamageType() {
		return this.damageType;
	}

	public int attackRoll() {
		return diceRoller.nextInt(20) + 1 + toHit;
	}

	public int damageRoll() {
		int damageTotal = 0;
		for (int roll = 1; roll <= damageDieNo; roll++) {
			damageTotal += diceRoller.nextInt(damageDieType) + 1;
		}
		damageTotal += damageModifier;
		return damageTotal;
	}

	public int criticalDamageRoll() {
		return (damageRoll() - damageModifier) * 2 + damageModifier;
	}

	@Override
	public String toString() {
		if (shortRange == longRange) {
			return (attackName + ": " + attackType + ": +" + toHit + ", " + shortRange + " ft. " + damageDieNo + "d"
					+ damageDieType + " + " + damageModifier + " " + damageType + " damage");
		} else {
			return (attackName + ": " + attackType + ": +" + toHit + ", " + shortRange + "/" + longRange + " ft. "
					+ damageDieNo + "d" + damageDieType + " + " + damageModifier + " " + damageType + " damage");
		}
	}

}
