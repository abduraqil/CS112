package warehouse;

/*
 * Use this class to put it all together.
 */ 
public class Everything {
    public static void main(String[] args) {
        StdIn.setFile("everything.in");
        StdOut.setFile("everything.out");

	// Use this file to test all methods
    int size = StdIn.readInt();
    Warehouse w = new Warehouse();
    for (int i = 0; i < size; i++) {
        String check = StdIn.readString();
        if(check.equals("add")) {
        int day = StdIn.readInt();
        int id = StdIn.readInt();
        String name = StdIn.readString();
        int stock = StdIn.readInt();
        int demand = StdIn.readInt();
        w.addProduct(id, name, stock, day, demand);
        } else if (check.equals("delete")) {
                int id = StdIn.readInt();
                w.deleteProduct(id);
            } else if ((check.equals("purchase"))) {
                int day = StdIn.readInt();
                int id = StdIn.readInt();
                int amount = StdIn.readInt();
                w.purchaseProduct(id, day, amount);;
            } else {
                int id = StdIn.readInt();
                int amount = StdIn.readInt();
                w.restockProduct(id, amount);
            }
        }
        System.out.print(w);
    }
}
