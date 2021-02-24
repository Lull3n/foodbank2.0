package rai;

import java.text.MessageFormat;

public class PriceItem implements Comparable {
	private int id;
	private String title;
	private float price;
	private String pricetype;
	
	public PriceItem(int id, String title, float price, String pricetype) {
		this.id = id;
		this.title = title;
		this.price = price;
		this.pricetype = pricetype;
	}
	
	public String toString() {
		String res = MessageFormat.format("#{0} {1} - {2} {3}", id, title, price, pricetype);
		return res;
	}
	
	public float getPrice() {
		return price;
	}
	
	public String getPriceType() {
		return pricetype;
	}
	
	public int getId() {
		return id;
	}

	public int compareTo(Object o) {
		if(o instanceof PriceItem) {
			PriceItem item = (PriceItem) o;
			if(item.getPrice() > price)
				return -1;
			if(item.getPrice() < price)
				return 1;
			return 0;
		} 
		return 0;
	}
}
