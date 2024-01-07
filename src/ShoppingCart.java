import java.util.HashMap;
import java.util.Map;

public class ShoppingCart {
    private static Map<String, Integer> shoppingCart = new HashMap<>();

    public static void addToCart(String product) {
        shoppingCart.put(product, shoppingCart.getOrDefault(product, 0) + 1);
        System.out.println("Product added to the shopping cart: " + product);
    }

    public static void viewShoppingCart() {
        if (shoppingCart.isEmpty()) {
            System.out.println("Shopping cart is empty.");
        } else {
            System.out.println("Shopping Cart:");
            for (Map.Entry<String, Integer> entry : shoppingCart.entrySet()) {
                System.out.println(entry.getKey() + " - Quantity: " + entry.getValue());
            }
        }
    }
}
