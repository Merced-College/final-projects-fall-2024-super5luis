//Luis Cruz Pereda
// Ms. Kanemoto
// 12/5/2024


// Class representing an item in the game
public abstract class Item {
    
    // Item properties
    String name;   // Name of the item
    String rarity;  // Rarity of the item
    String stats;  // Stats related to the item
    String effects; // Any speaical effects the item might have
    int price; // Add a price field for the items


    // Constructor 
    public Item(String name, String rarity, String stats, String effects) {
        this.name = name; // set the name of the item
        this.rarity = rarity; // Set the rarity of the item
        this.stats = stats; // Set the stats of the item
        this.effects = effects; // set the effects of the item
        this.price = calculatePrice(rarity); // Set the price based on rarity
    }

    // Method to calculate price based on rarity
    private int calculatePrice(String rarity) {
        // Switch case to determine the price based on the rarity of the item
        switch (rarity.toLowerCase()) {
            case "common": // Common items have a base price of 30 gold
                return 30;
            case "uncommon": // Uncommon items have a base price of 60 gold
                return 60;
            case "rare": // Rare items have a base price of 90 gold
                return 90;
            default:
                return 30; // Default to common price
        }
    }


    // Override the toString method to prvide a string representation of the item
    @Override
    public String toString() {
        // Returns a string that inclues the item's name, rarity, and price in gold
        return name + " (" + rarity + ") - " + price + " gold";
    }
}
