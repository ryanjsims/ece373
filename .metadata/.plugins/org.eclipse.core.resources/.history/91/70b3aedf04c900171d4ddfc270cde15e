package store.software;
import java.util.HashMap;
import java.util.ArrayList;
import store.objects.Product;

public class ShoppingCart {
	private HashMap<Product, Integer> products;
	private double total;
	
	public ShoppingCart(){
		products = new HashMap<Product, Integer>();
		total = 0;
	}
	
	public void addProduct(Product toAdd){
		if(products.containsKey(toAdd))
			products.put(toAdd, products.get(toAdd) + 1);
		else
			products.put(toAdd, 1);
		total += toAdd.getPrice();
	}
	
	public void addProduct(Product toAdd, int quantity){
		if(products.containsKey(toAdd))
			products.put(toAdd, products.get(toAdd) + quantity);
		else
			products.put(toAdd, quantity);
		total += toAdd.getPrice() * quantity;
	}
	
	public void removeProduct(Product target){
		if(!products.containsKey(target))
			return;
		products.put(target, products.get(target) - 1);
		if(products.get(target) <= 0)
			products.remove(target);
		total -= target.getPrice();
	}
	
	public void removeProduct(Product target, int quantity){
		if(!products.containsKey(target))
			return;
		products.put(target, products.get(target) - quantity);
		if(products.get(target) <= 0)
			products.remove(target);
		total -= target.getPrice() * quantity;
	}
	
	public ArrayList<Product> getProducts(){
		ArrayList<Product> toReturn = new ArrayList<Product>();
		for(Product p : products.keySet()){
			toReturn.add(p);
		}
		return toReturn;
	}
	
	public int getQuantity(Product p){
		return products.get(p);
	}
	
	public double getTotal
}
