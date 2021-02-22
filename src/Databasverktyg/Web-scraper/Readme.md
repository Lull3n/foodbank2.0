För att hämta ingredienser används coop.se/handla, samt webläsar-pluginet Web Scraper (finns att hämta för både chrome och firefox på www.webscraper.io).
För skrapning går till som följer:

(Instruktioner för firefox)
1. Installera pluginet.
2. Öppna valfri hemsida och klicka F12.
3. Välj fliken Web Scraper.
4. Klicka create new sitemap -> import sitemap.
5. Fyll i nedanstående i [import JSON] :

{"_id":"coop","startUrl":["https://www.coop.se/handla/"],
"selectors":[
{"id":"Category","type":"SelectorLink","parentSelectors":["_root"],"selector":".js-sectionHeader a","multiple":true,"delay":0},
{"id":"product","type":"SelectorLink","parentSelectors":["subcat_low"],"selector":"a.ItemTeaser-link","multiple":true,"delay":0},
{"id":"prod_name","type":"SelectorText","parentSelectors":["product"],"selector":"h1","multiple":false,"regex":"","delay":0},
{"id":"prod_price","type":"SelectorText","parentSelectors":["product"],"selector":"span.ItemInfo-priceValue","multiple":false,"regex":"","delay":0},
{"id":"prod_compPrice","type":"SelectorText","parentSelectors":["product"],"selector":".ItemInfo-description div","multiple":false,"regex":"","delay":0},
{"id":"subCat_link","type":"SelectorLink","parentSelectors":["Category"],"selector":".js-sectionSubLevel a","multiple":true,"delay":0},
{"id":"subcat_low","type":"SelectorLink","parentSelectors":["subCat_link"],"selector":".SidebarNav-subPanel a","multiple":true,"delay":0},
{"id":"prod_contents","type":"SelectorText","parentSelectors":["product"],"selector":"div.ItemInfo-description","multiple":false,"regex":"","delay":0}]}

5. Fyll i namn för din sitemap, och klicka import sitemap.
6. Välj fliken sitemaps, och klicka på länken för din sitemap.
7. Klicka på menyn för din sitemap, och klicka "scrape"
8. Ändra page load delay till 500, och klicka "start scraping"
9. Vänta.

När scrapingen är klar kan man exportera inhämtad data som en CSV fil.
Detta görs genom att klicka på menyn för din sitemap, och sedan "export data as CSV".
Om man sedan vill använda ingrediensInserter för att stoppa in ingredienserna i databasen krävs det dock att man först konverterar datan 
till JSON. Detta kan göras med diverse verktyg online, förslagsvis https://csvjson.com/csv2json
Sen ska det gå att köra IngrediensInserter med din JSON-fil som input, och databasen ska fyllas.