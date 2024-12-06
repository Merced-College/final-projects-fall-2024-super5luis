//Luis Cruz Pereda
// Ms. Kanemoto
// 12/5/2024
//to complie on terminal javac DungeonExplorer.java
//to run on terminal java DungeonExplorer

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DungeonExplorer {

    private static final Random random = new Random();// Random number generator for randomness in the game
    private static final Scanner scanner = new Scanner(System.in);// Scanner for user input
    private static List<Item> chestItems = new ArrayList<>();// List to store items found in chests

    // Riddles for the Golem encounter. Each riddle has a question and the correct answer.
    private static final String[][] riddles = {
        {"I speak without a mouth and hear without ears. I have no body, but I come alive with the wind. What am I?", "echo"},
        {"I have keys but open no locks. I have space but no room. You can enter, but you can't go outside. What am I?", "keyboard"},
        {"I can fly without wings. I can cry without eyes. Whenever I go, darkness flies. What am I?", "cloud"},
        {"The more you take, the more you leave behind. What am I?", "footsteps"},
        {"I am not alive, but I can grow. I don’t have lungs, but I need air. What am I?", "fire"}
    };

    public static void main(String[] args) {
        Character player = new Character("Hero", 100, 20, 5, 50, 3); // Player setup with name, health, attack power, defense, gold, and potions
        System.out.println("Welcome to the Dungeon Explorer!");
        loadItemsFromCSV(); // Load items from CSV file
        
        int enemyDefeats = 0; // Track number of enemies defeated

        // Game Loop, keeps running as long as the player is alive
        while (player.isAlive()) {
            System.out.println("\nYou stand at the entrance of a dark dungeon.");
            System.out.println("Do you want to go left, right, or back? (Press 'I' for inventory)");
            if (player.hasBossKey) {
                System.out.println("You have the Boss Key. You can also go forward to face the Dark Lord.");
            }
            System.out.println("Or press 'Q'to quit the game.");

            String direction = scanner.nextLine().trim().toLowerCase();// Gets direction input from user

            if(direction.equals("q")){
                System.out.println("Thank you for playing! Exiting the game...");
                System.exit(0); //Quit the game
            }

            if (direction.equals("i")) {
                showInventory(player, enemyDefeats);  // Show the player's inventory along with health and enemies defeated
                continue;
            }

            switch (direction) {
                case "left":
                case "right":
                    enemyDefeats = enterRandomRoom(player, enemyDefeats);  // Pass enemyDefeats to update it
                    break;
                case "back":
                    if (enemyDefeats >= 3) {
                        System.out.println("You can now access the Golem room. Solve its riddle to proceed.");
                        enemyDefeats = golemEncounter(player, enemyDefeats);  // Golem encounter after defeating 3 enemies
                    } else {
                        System.out.println("You haven't defeated enough enemies to unlock the Golem room.");
                    }
                    break;
                case "forward":
                    if (player.hasBossKey) {
                        System.out.println("You use the Boss Key and enter the final room to face the Dark Lord!");
                        Character boss = new Character("Dark Lord", 150, 30, 0, 10, 0); // Dark Lord boss setup
                        combat(player, boss);

                        if (player.isAlive()) {
                            System.out.println("Congratulations! " + player.name + " has defeated the Dark Lord!");
                            System.out.println("You have completed the dungeon!");
                            System.exit(0); // Ends the game after defeating the boss
                        }
                    } else {
                        System.out.println("You need the Boss Key to enter the Dark Lord's room.");
                    }
                    break;
                default:
                    System.out.println("Invalid direction! Choose 'left', 'right', 'back', or 'forward'.");
                    continue;
            }

            if (!player.isAlive()) {
                System.out.println(player.name + " has been defeated. Game Over!");
                break;
            }
        }
    }

    // show inventory and character stats
    private static void showInventory(Character player, int enemyDefeats) {
        System.out.println("\n----- Inventory -----");
        System.out.println("Health: " + player.health + "/" + player.maxHealth);
        System.out.println("Gold: " + player.gold);
        System.out.println("Potions: " + player.potions);
        System.out.println("Enemies Defeated: " + enemyDefeats);
        System.out.println("---------------------");

        // Option to use a potion
        if (player.potions > 0) {
            System.out.println("Would you like to use a potion? (Y/N)");
            String choice = scanner.nextLine().trim().toLowerCase();
            if (choice.equals("y")) {
                usePotion(player);// Use a potion to restore health
            }
        }

        // Display equipped items (weapon and armor)
        if (player.equippedWeapon != null) {
            System.out.println("Weapon: " + player.equippedWeapon);
        } else {
            System.out.println("No weapon equipped.");
        }

        if (player.equippedArmor != null) {
            System.out.println("Armor: " + player.equippedArmor);
        } else {
            System.out.println("No armor equipped.");
        }
    }

    // Use a potion to restore health
    private static void usePotion(Character player) {
        if (player.potions > 0) {
            System.out.println("You use a potion. Health is now: " + player.health);
            player.usePotion();// Restore health and consume a potion
        } else {
            System.out.println("You have no potions left!");
        }
    }

    // Enter a random room which could be an enemy encounter, treasure room, healing room, or store room
    private static int enterRandomRoom(Character player, int enemyDefeats) {
        int roomType = random.nextInt(4); // Random number between 0 and 3

        switch (roomType) {
            case 0:
                enemyDefeats = enemyEncounter(player, enemyDefeats);// Handle an enemy encounter
                break;
            case 1:
                treasureRoom(player);// handles treasure room
                break;
            case 2:
                healingRoom(player);// handles room for healing
                break;
            case 3:
                storeRoom(player);// handles store room
                break;
        }
        return enemyDefeats;// Returns updated enemy defeated count
    }

    // Random enemy encounter, player flight against a random enemy
    private static int enemyEncounter(Character player, int enemyDefeats) {
        String[] enemies = {"Goblin", "Lizard Person", "Man-Bat"};// List of possible enemies
        int enemyIndex = random.nextInt(enemies.length);
        String enemyName = enemies[enemyIndex];
        int enemyLevel = random.nextInt(3) + 1; // Random enemy between Level 1 to 3
        int enemyHealth = 15 + (enemyLevel * 5); // Health scales with level

        System.out.println("You enter a dark room and a wild " + enemyName + " appears!");
        Character enemy = new Character(enemyName, enemyHealth, enemyLevel, 5, 0, 0); // Enemy setup

        combat(player, enemy); //start combat between player and enemy

        if (player.isAlive()) {
            enemyDefeats++;// Increment the number of enemies defeated if the player wins
            System.out.println("Enemies defeated: " + enemyDefeats);
        }
        return enemyDefeats;
    }

    // Golem encounter where the player must solve a riddle
    private static int golemEncounter(Character player, int enemyDefeats) {
        System.out.println("A Golem stands before you, blocking your path.");
        System.out.println("To pass, you must solve its riddle.");

        int riddleIndex = random.nextInt(riddles.length);// Randomly pick a riddle
        String riddle = riddles[riddleIndex][0];
        String answer = riddles[riddleIndex][1];

        int attempts = 0;
        boolean solved = false;

        while (attempts < 3 && !solved) {
            System.out.println("Riddle: " + riddle);
            System.out.print("Your answer: ");
            String playerAnswer = scanner.nextLine().trim().toLowerCase();

            if (playerAnswer.equals(answer)) {
                System.out.println("Correct! The Golem steps aside.");
                player.giveBossKey();// player is giving a Boss key if player solves riddle
                solved = true;
            } else {
                attempts++;
                player.takeDamage(10);// player takes damage on incorrect answer
                System.out.println("Incorrect! The Golem attacks. You lose 5 health.");
                System.out.println("You have " + player.health + " health remaining.");
            }
        }

        if (!solved) {
            System.out.println("You failed to solve the riddle after 3 attempts.");
            enemyDefeats = 0;// If player fails, enemyDefeated counter resets
        }

        return enemyDefeats;
    }

    // handle the treasure room encounter
    private static void treasureRoom(Character player) {
        System.out.println("You find a treasure chest in this room!");

        //Give the player 30 gold from the chest
        int goldFound = 30;
        player.gold += goldFound;
        System.out.println("You found " + goldFound + " gold!");
        
        // Randomly choose an item from the chestItems list
        Item itemFound = getRandomItem();
        System.out.println("You found a " + itemFound.name + " (" + itemFound.rarity + ")");

        //prompt the player to decide wheather to equip the item
        System.out.println("Would you like to equip the " + itemFound.name + "? (Y/N)");
        String choice = scanner.nextLine().trim().toLowerCase();
        
        if(choice.equals("y")){
        if (itemFound instanceof Weapon) {
            // Equip the item if it's a weapon
            player.equippedWeapon = (Weapon) itemFound;
            System.out.println("You equip the " + itemFound.name + " as your weapon.");
        } else if (itemFound instanceof Armor) {
            //equip the item if it's an armor
            player.equippedArmor = (Armor) itemFound;
            System.out.println("You equip the " + itemFound.name + " as your armor.");
            }
        }else{
            //if the player does not want to equip the item
            System.out.println("You choose not to equip the " + itemFound.name + ".");
        }
    }

    // This method handles the healing room encounter.
    private static void healingRoom(Character player) {
        // Reastore the player's health to the maximum
        System.out.println("You find a healing pool. You can restore some health.");
        player.health = player.maxHealth;
        System.out.println("Your health is fully restored to " + player.health);
    }

    // Handles the store room where players buy items
    private static void storeRoom(Character player) {
        System.out.println("You find a merchant in this room. Would you like to buy items?");
        System.out.println("1. Health Potion - 10 Gold");
        System.out.println("2. Weapon - 50 Gold");
        System.out.println("3. Armor - 50 Gold");
        System.out.println("4. Exit");

        // Lists for random items
        List<Item> possibleWeapons = new ArrayList<>();
        possibleWeapons.add(new Weapon("Ancient Wooden Sword", "Common", "Attack: 10", "None"));
        possibleWeapons.add(new Weapon("Shattered Dagger of the Forgotten", "Common", "Attack: 8", "None"));
        possibleWeapons.add(new Weapon("Frost-Bitten War Hammer", "Common", "Attack: 12", "None"));

        List<Item> possibleArmors = new ArrayList<>();
        possibleArmors.add(new Armor("Soldier's Helm", "Common", "Defense: +2", "None"));
        possibleArmors.add(new Armor("Traveler's Vest", "Common", "Defense: +2", "None"));
        possibleArmors.add(new Armor("Traveler's Footwear", "Common", "Defense: +2", "None"));

        boolean continueShopping = true;

        //Loop for the player to continue shopping until they choose to exit
        while (continueShopping) {
            System.out.print("Enter your choice: ");
            int choice = Integer.parseInt(scanner.nextLine().trim());

            // handles different shopping choices based on the players input
            switch (choice) {
                case 1:
                    // If the player has enough gold, they can buy a health potion
                    if (player.gold >= 10) {
                        player.gold -= 10;
                        player.potions++;
                        System.out.println("You bought a health potion. You now have " + player.potions + " potions.");
                    } else {
                        System.out.println("You don't have enough gold!");
                    }
                    break;
                case 2:
                    // If player has enough gold to buy a weapon
                    if (player.gold >= 50) {
                        player.gold -= 50;
                        Item weapon = possibleWeapons.get(random.nextInt(possibleWeapons.size()));
                        player.equippedWeapon = (Weapon) weapon;
                        System.out.println("You bought a " + weapon.name);
                    } else {
                        System.out.println("You don't have enough gold!");
                    }
                    break;
                case 3:
                    // If player has enough gold to buy an armor
                    if (player.gold >= 50) {
                        player.gold -= 50;
                        Item armor = possibleArmors.get(random.nextInt(possibleArmors.size()));
                        player.equippedArmor = (Armor) armor;
                        System.out.println("You bought " + armor.name);
                    } else {
                        System.out.println("You don't have enough gold!");
                    }
                    break;
                case 4:
                    // Exit the store room
                    continueShopping = false;
                    break;
                default:
                    // If the player input an invalid choice, prompt them again
                    System.out.println("Invalid choice. Try again.");
                    break;
            }
        }
    }

    // Handles the combat between the player and an enemy
    private static void combat(Character player, Character enemy) {
        System.out.println(player.name + " faces " + enemy.name);
        System.out.println(enemy.name + " has " + enemy.health + " health.");
        boolean playerTurn = true;

        // Combat loop, alternating turns between the player and the enemy
        while (player.isAlive() && enemy.isAlive()) {
            if (playerTurn) {
                // Player's turn to act
                System.out.println("Choose your action: Attack, Run, Sneak, Use Potion");
                System.out.println("Player Health: " + player.health + "/" + player.maxHealth);
                System.out.println("Enemy Health: " + enemy.health);

                String action = scanner.nextLine().trim().toLowerCase();
                switch (action) {
                    case "attack":
                        // Calaulate damage for an attack and deal iit ti the enemy
                        int damage = random.nextInt(player.attackPower + 1);
                        System.out.println(player.name + " attacks for " + damage + " damage.");
                        enemy.takeDamage(damage);
                        break;
                    case "run":
                        // Attempt to run away from the battle
                        System.out.println(player.name + " tries to run away!");
                        if (random.nextBoolean()) {
                            System.out.println(player.name + " successfully ran away.");
                            return;// Ends comabt if player escapes
                        } else {
                            System.out.println(player.name + " fails to escape and takes 10 damage.");
                            player.takeDamage(10);// Takes damage if runnung fails
                        }
                        break;
                    case "sneak":
                        // Sneak attack, dealing double damage if possible
                        if (!player.canSneak) {
                            System.out.println("You have already used your sneak attack.");
                            break;
                        }
                        int sneakDamage = random.nextInt(player.attackPower + 1) * 2;  // Sneak attack deals double damage
                        System.out.println(player.name + " uses Sneak Attack for " + sneakDamage + " damage.");
                        enemy.takeDamage(sneakDamage);
                        player.canSneak = false;  // Disable further sneak attacks
                        break;
                    case "use potion":
                        // Use a potion to heal the player
                        usePotion(player);
                        break;
                    default:
                        // Invaild action, prompt the player to choose again
                        System.out.println("Invalid action! Choose: Attack, Run, Sneak, or Use Potion.");
                        continue;
                }
            } else {
                // Enemy's turn to attack
                int enemyDamage = random.nextInt(11)+ 5;// Random enemy damage
                System.out.println(enemy.name + " attacks for " + enemyDamage + " damage.");
                player.takeDamage(enemyDamage);
            }

            playerTurn = !playerTurn;  // Switch turn
        }

        // If the player is alive after the battle, they defeated the enemy
        if (player.isAlive()) {
            System.out.println(player.name + " has defeated " + enemy.name + "!");
        } else {
            // If the player is defeated, the enemy wins
            System.out.println(enemy.name + " has defeated " + player.name + "!");
        }
    }

    // Helper method to load items from a CSV
    private static void loadItemsFromCSV() {
        try (BufferedReader br = new BufferedReader(new FileReader("Chest.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Item Name")) {
                    continue; // Skip header
                }
                String[] data = line.split(",");// Split each line by cooma to get item details
                if (data.length >= 5) {
                    String name = data[0].trim();
                    String type = data[1].trim();
                    String rarity = data[2].trim();
                    String stats = data[3].trim();
                    String effects = data[4].trim();

                    // Based on the item type, add it to the chestItems list
                    if (type.equalsIgnoreCase("Weapon")) {
                        chestItems.add(new Weapon(name, rarity, stats, effects));// add weapon
                    } else if (type.equalsIgnoreCase("Armor")) {
                        chestItems.add(new Armor(name, rarity, stats, effects));// add armor
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error loading items from CSV: " + e.getMessage());// Handles any file read errors
        }
    }

    // Helper method to return a random item from the chestItems list
    private static Item getRandomItem() {
        return chestItems.get(random.nextInt(chestItems.size()));// Return a random item
    }
}
