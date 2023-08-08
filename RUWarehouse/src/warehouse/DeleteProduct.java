package warehouse;

/*
 * Use this class to test the deleteProduct method.
 */ 
public class DeleteProduct {
    public static void main(String[] args) {
        StdIn.setFile("deleteproduct.in");
        StdOut.setFile("deleteproduct.out");

	// Use this file to test deleteProduct
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
        } else {
            int id = StdIn.readInt();
            w.deleteProduct(id);
        }
    }
    System.out.println(w);
    }
}
