import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import java.util.ArrayList;

public class JsonCleaner {
    private static ArrayList<String> validCategories = new ArrayList<>();

    public static void init(){
        validCategories.add("Mejeri & Ägg");
        validCategories.add("Ost");
        validCategories.add("Frukt");
        validCategories.add("Frukt & Grönsaker");
        validCategories.add("Skafferi");
        validCategories.add("Kött, Fågel & Chark");
        validCategories.add("Vegetariskt");
        validCategories.add("Fisk & Skaldjur");
        //validCategories.add("Dryck");
        validCategories.add("Kryddor & Smaksättare");
        validCategories.add("Frys");
        //validCategories.add("Godis, Glass & Snacks");

    }




    /**
     * The method takes a JsonArray, and attempts to clean the containing data.
     * 1. Removes objects of unwanted category.
     * 2. Changes the field-name  "Category" to "prod_category" for consistency.
     * 3. Cleans out the prod_cost value.
     * 4. Cleans out, and adds fields prod_compPrice and prod_compUnit.
     * compPrice is the comparable price. For example kr/kg. and compUnit is the unit. Usually kg or lit.
     * @param array array to be cleaned.
     *
     *     Example raw data:
     *    {
     *     "web-scraper-order": "1612049449-8578",
     *     "web-scraper-start-url": "https://www.coop.se/handla/",
     *     "Category": "Skafferi\n                    \n                        \n        \n   Pil",
     *     "Category-href": "https://www.coop.se/handla/varor/skafferi",
     *     "product": "",
     *     "product-href": "https://www.coop.se/handla/varor/skafferi/flingor-musli/gryn/polenta-7317731525003",
     *     "prod_name": "Polenta",
     *     "prod_price": "29:95 /st",
     *     "prod_compPrice": "Jfr-pris 59.90/kr/kg.",
     *     "subCat_link": "Flingor & Müsli\n  \n   \n      \n        Pil",
     *     "subCat_link-href": "https://www.coop.se/handla/varor/skafferi/flingor-musli",
     *     "subcat_low": "Gryn",
     *     "subcat_low-href": "https://www.coop.se/handla/varor/skafferi/flingor-musli/gryn",
     *     "prod_contents": "Saltå Kvarn.\n500 gr.\n\n                                        Jfr-pris 59.90/kr/kg."
     *   }
     */
    public static void cleanArray (JsonArray array){
        for(int i = 0; i < array.size(); i++) {
            JsonObject cur = (JsonObject) array.get(i);
            if (cur != null) {
                String cat = cur.remove("Category").getAsString();
                cat = cat.substring(0, cat.indexOf("\n"));

                if (validCategories.contains(cat)) {
                    //System.out.println("CAT " +cat);
                    cur.addProperty("prod_cat", cat);
                    JsonElement elementPrice = cur.remove("prod_price");
                    String price = "";
                    if (!elementPrice.isJsonNull()) {
                        price = elementPrice.getAsString();
                       // System.out.println(cur.get("prod_name"));
                        int slashIndex = price.indexOf('/') - 1;
                        if(slashIndex < 0) slashIndex = price.length();
                        price = price.substring(0, slashIndex);
                        price = price.replace(":", ".");
                        try{
                            Float.parseFloat(price);
                        } catch (NumberFormatException e){
                            price = price.substring(0,2);
                        }
                        cur.addProperty("prod_price", price);
                    } else {
                        array.remove(i--);
                        continue;
                    }
                    String compPrice = "";
                    JsonElement elementComp = cur.remove("prod_compPrice");


                    if(!elementComp.isJsonNull()){
                        String compUnit;
                        compPrice = elementComp.getAsString();
                        String[] strings = compPrice.split("/");
                        compPrice = strings[0].substring(9);
                        compUnit = strings[strings.length -1];
                        compUnit = compUnit.substring(0, compUnit.length() -1);

                        if(compUnit.equals("spad")){
                            array.remove(i--); //removes products which have for example: Jfr-pris 127.80/utan sås/spad. As comparable price.
                            continue;
                        } else if(compUnit.length() > 5 ){
                            if(compUnit.contains("drickfärdig")) compUnit = "lit";
                            else if(compUnit.contains("ätbar") || compUnit.contains("ätklar")) compUnit = "kg";
                        }
                        cur.addProperty("prod_compPrice", compPrice);
                        cur.addProperty("prod_compUnit", compUnit);

                    } else {
                        array.remove(i--);

                    }
                } else {
                    ;
                    array.remove(i--);
                }
            }
        }
    }
}
