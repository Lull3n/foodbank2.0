# foodbank2.0
Foodbank är en java-applikation som hanterar en SQL-databas med ingredienser och recept. Applikationen består av fyra olika delar:
- IngredientsInserter: verktyg för att lägga till ingredienser i databasen.
  - Verktyget använder sig av en json-fil som hämtas från chrome/firefox-extensionen webscraper.io.
- ReceipInserter: verktyg för att lägga till recept i databasen.
  - Användare skapar recept via verktyget.
- RelationsInserter: verktyg för att skapa relationer mellan ett recept och dess ingående ingredienser
  - Användare använder detta verktyg för att koppla ingredienser i databasen till recept som de skapat. 
- FoodbankAPI

För att kunna köra applikationen:
- Skapa en databas i Microsoft SQL Server med namnet FoodBank.
- Skapa en SQL-användare med användarnamn "javaConnection" och lösenord "hejDatabasenFood".
- Gör en restore database på FoodBank med backup-filen FoodBank_Base.bak som ligger under Verktyg/database.

För att köra APIet:
- Eftersom backup-filen innehåller en del ingredienser, relationer och recept kan API-delen användas direkt.
- För att starta APIet öppna foodbank2.0/API/FoodBankAPI och kör klassen Server.java
  - När klassen är startad kan användaren kalla på endpointsen:
    - http://localhost:7000/foodbank/api/recipe (returnerar alla recept som finns i databasen)
    - http://localhost:7000/foodbank/api/recipe/:title (returnerar ett specifikt recept)
    - http://localhost:7000/foodbank/api/category/:category (returnerar alla recept med en specifik kategori)

För att köra IngredientInserter:
- Öppna paketet foodbank2.0/Verktyg/Databasverktyg/IngredientsInserter
- För att använda en existerande json-fil: 
  - öppna Controller.java och specificera vilken av filerna under files som du vill använda. 
  - Kör klassen Controller.java
- För att skapa en ny json-fil:
  - Gör en webscraping.
    - Instruktioner för hur detta görs finns under mappen Verktyg/Databasverktyg/Web-scraper.
  - Plasera filen under files och skriv in namnet på filen i Controller.java
  - Kör Controller.java

För att köra RecipeInserter:
- Öppna paketet foodbank2.0/Verktyg/Databasverktyg/RecipeInserter
- Kör klassen Controller.java

För att köra RelationsInserter
- För att detta verktyget ska fungera behöver databasen innehålla både recept och ingredienser
- Öppna paketet foodbank2.0/Verktyg/Databasverktyg/RelationsInserter
- Kör klassen Controller.java
- Använd verktyget för att skapa relationer mellan recept och ingredienser
- Öppna paketet foodbank2.0/Verktyg/Databasverktyg/RelationsAutoInserter
- Kör klassen Controller.java
- Öppna paketet foodbank2.0/Verktyg/Databasverktyg/RelationsPriceInserter
- Kör klassen Controller.java
