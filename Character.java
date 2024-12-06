//Luis Cruz Pereda
// Ms. Kanemoto
// 12/5/2024

public class Character {
    // Character attributes
    String name; // Name of the character
    int health, maxHealth; // Current health and maximum health of the character
    int attackPower; // The amount of damage the character can deal
    int defense; // The amount of damage the character can block
    int gold; // The amount of gold the character has
    boolean canSneak; // Determines if the character can use Sneak attack
    boolean hasBossKey; // Tracks if the character has the boss key
    int potions; // The number of health potions the character has
    Weapon equippedWeapon; // The Weapon equpped by the Character
    Armor equippedArmor; // The armor equppied by the character

    // Constructor
    public Character(String name, int maxHealth, int attackPower, int defense, int gold, int potions) {
        this.name = name; // Set Character's name
        this.maxHealth = maxHealth; // Set the maximum health
        this.health = maxHealth; // Set the current health to max health
        this.attackPower = attackPower; // Set the attack power
        this.defense = defense; // Set the defense
        this.gold = gold; // Set the amouunt of gold
        this.potions = potions; // Set the number of potions
        this.canSneak = true; // Can perform sneak attack once
        this.hasBossKey = false; // Start without boss key
    }

    // Method to check if the character is still alive
    public boolean isAlive() {
        return health > 0; // Character is alive if health is greater than 0
    }

    // Method for the character to take damage
    public void takeDamage(int damage) {
        // Subtract defense from damage, ensuring it doesn't go below 0
        int damageTaken = Math.max(0, damage - defense);  // Subtract defense from damage
        health -= damageTaken; // Reduce health by the damage taken
        if (health < 0) {
            health = 0; // Ensure health does not go below 0
        }
    }

    // Method to use a health potion
    public void usePotion() {
        if (potions > 0) { // Check if the character has any potions
            health = Math.min(maxHealth, health + 50);  // Restores health up to maxHealth
            potions--; // Decrease the number of potions after use
        }
    }

    // Method to give the character the boss key
    public void giveBossKey() {
        hasBossKey = true; // set the flag that shows the character has the boss key
    }
}
