package warehouse;

/*
 *
 * This class implements a warehouse on a Hash Table like structure, 
 * where each entry of the table stores a priority queue. 
 * Due to your limited space, you are unable to simply rehash to get more space. 
 * However, you can use your priority queue structure to delete less popular items 
 * and keep the space constant.
 * 
 * @author Ishaan Ivaturi
 */ 
public class Warehouse {
    private Sector[] sectors;
    
    // Initializes every sector to an empty sector
    public Warehouse() {
        sectors = new Sector[10];

        for (int i = 0; i < 10; i++) {
            sectors[i] = new Sector();
        }
    }
    
    /**
     * Provided method, code the parts to add their behavior
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void addProduct(int id, String name, int stock, int day, int demand) {
        evictIfNeeded(id);
        addToEnd(id, name, stock, day, demand);
        fixHeap(id);
    }

    /**
     * Add a new product to the end of the correct sector
     * Requires proper use of the .add() method in the Sector class
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    private void addToEnd(int id, String name, int stock, int day, int demand) {
        // IMPLEMENT THIS METHOD
        
        Product p = new Product(id, name);
        p.setStock(stock);
        p.setDemand(demand);
        p.setLastPurchaseDay(day);
        sectors[id % 10].add(p);


    }

    /**
     * Fix the heap structure of the sector, assuming the item was already added
     * Requires proper use of the .swim() and .getSize() methods in the Sector class
     * @param id The id of the item which was added
     */
    private void fixHeap(int id) {
        // IMPLEMENT THIS METHOD

        int sectorID = id%10;
        Sector s = sectors[sectorID];
        s.swim(s.getSize());

    }

    /**
     * Delete the least popular item in the correct sector, only if its size is 5 while maintaining heap
     * Requires proper use of the .swap(), .deleteLast(), and .sink() methods in the Sector class
     * @param id The id of the item which is about to be added
     */
    private void evictIfNeeded(int id) {
       // IMPLEMENT THIS METHOD

       int sectorID = id % 10;
       Sector s = sectors[sectorID];

       if (s.getSize() < 5) {
        return;
       }
       int minIndex = 1;
       int i = 1;
       while (i <= s.getSize()) {
        if (s.get(minIndex).getPopularity() > s.get(i).getPopularity()) {
            minIndex = i;
            }
        i++;
        }
        s.swap(minIndex, s.getSize());
        s.deleteLast();
        s.sink(1);
    }

    /**
     * Update the stock of some item by some amount
     * Requires proper use of the .getSize() and .get() methods in the Sector class
     * Requires proper use of the .updateStock() method in the Product class
     * @param id The id of the item to restock
     * @param amount The amount by which to update the stock
     */
    public void restockProduct(int id, int amount) {
        // IMPLEMENT THIS METHOD

        int sectorID = id %10;
        Sector s = sectors[sectorID];
        int index = -1;
        for (int i = 1; i <= s.getSize(); i++) {
            if(s.get(i).getId() == id){
                index = i;
                break;
            }
        }

        if (index == -1) return;

        s.get(index).updateStock(amount); 

    }
    
    /**
     * Delete some arbitrary product while maintaining the heap structure in O(logn)
     * Requires proper use of the .getSize(), .get(), .swap(), .deleteLast(), .sink() and/or .swim() methods
     * Requires proper use of the .getId() method from the Product class
     * @param id The id of the product to delete
     */
    public void deleteProduct(int id) {
        // IMPLEMENT THIS METHOD

        int sectorID = id %10;
        Sector s = sectors[sectorID];
        int index = -1;
        
        for (int i = 1; i <= s.getSize(); i++) {
            if (s.get(i).getId() == id) {
                index = i;
                break;
            }
        }

        if (index == -1) return;

        s.swap(index, s.getSize());
        s.deleteLast();
        s.sink(index);

    }
    
    /**
     * Simulate a purchase order for some product
     * Requires proper use of the getSize(), sink(), get() methods in the Sector class
     * Requires proper use of the getId(), getStock(), setLastPurchaseDay(), updateStock(), updateDemand() methods
     * @param id The id of the purchased product
     * @param day The current day
     * @param amount The amount purchased
     */
    public void purchaseProduct(int id, int day, int amount) {
        // IMPLEMENT THIS METHOD

        int sectorID = id %10;
        Sector s = sectors[sectorID];
        int index = -1;

        int i = 1;
        while (i <= s.getSize() && index == -1) {
            if (s.get(i).getId() == id) {
                index = i;
            }
            i++;
        }

        if (index == -1 || s.get(index).getStock() < amount) {
            return;
        }
        s.get(index).setLastPurchaseDay(day);
        s.get(index).updateStock(-1*amount);
        s.get(index).updateDemand(amount);
        s.sink(index);

    }
    
    /**
     * Construct a better scheme to add a product, where empty spaces are always filled
     * @param id The id of the item to add
     * @param name The name of the item to add
     * @param stock The stock of the item to add
     * @param day The day of the item to add
     * @param demand Initial demand of the item to add
     */
    public void betterAddProduct(int id, String name, int stock, int day, int demand) {
        // IMPLEMENT THIS METHOD

        int sectorID = id %10;
        Sector s = sectors[sectorID];
        if(s.getSize() < 5){
            addProduct(id, name, stock, day, demand);
        }
        int currentID = sectorID +1;
        while(currentID != sectorID){
            if(currentID >= sectors.length){
                currentID = 0;
                if(sectorID == 0){
                    break;
                }
            }
            if(sectors[currentID].getSize() < 5){
                break;
            }
            currentID++;
        }
        if(currentID == sectorID){
            addProduct(id, name, stock, day, demand);
        }
        else{
            int newID = ((id /10) *10) + currentID;
            addProduct(newID, name, stock, day, demand);
        }

    }


    /*
     * Returns the string representation of the warehouse
     */
    public String toString() {
        String warehouseString = "[\n";

        for (int i = 0; i < 10; i++) {
            warehouseString += "\t" + sectors[i].toString() + "\n";
        }
        
        return warehouseString + "]";
    }

    /*
     * Do not remove this method, it is used by Autolab
     */ 
    public Sector[] getSectors () {
        return sectors;
    }
}
