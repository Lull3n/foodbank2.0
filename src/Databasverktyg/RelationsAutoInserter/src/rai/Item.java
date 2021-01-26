package rai;

import java.text.MessageFormat;

public class Item {
	private PriceItem price = null;
	private Float units;
	private String title;
	private String unitType;
	
	public Item(String title, float tempUnit, PriceItem price, String unitType) {
		this.title = title;
		this.units = tempUnit;
		this.price = price;
		this.unitType = unitType;
	}
	
	public void setPrice(PriceItem price) {
		this.price = price;
	}
	
	public String toString() {
		if(price != null)
			return MessageFormat.format("{0}: {1} - {2} {3}", title, units, price.getPrice(), unitType);
		return title;
	}
	
	public float getUnits() {
		return units;
	}
	
	public PriceItem getPriceItem() {
		return price;
	}
}
