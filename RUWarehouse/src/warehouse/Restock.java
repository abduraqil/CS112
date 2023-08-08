package warehouse;

public class Restock {
    public static void main(String[] args) {
        StdIn.setFile("restock.in");
        StdOut.setFile("restock.out");

	// Uset his file to test restock
    int size = StdIn.readInt();
    Warehouse w = new Warehouse();

    for(int i = 0; i < size; i++) {
        String check = StdIn.readString();
        if(check.equals("add")) {
        int day = StdIn.readInt();
        int id = StdIn.readInt();
        String name = StdIn.readString();
        int stock = StdIn.readInt();
        int demand = StdIn.readInt();
        w.addProduct(id, name, stock, day, demand);
        }
        else {
            int id = StdIn.readInt();
            int amount = StdIn.readInt();
            w.restockProduct(id, amount);
        }
    }
    System.out.print(w);
    }
}
