const express = require("express");
const mysql = require("mysql");
const sqlite = require ("sqlite3").verbose();

const path = require("path");

const bodyParser = require("body-parser");
const db = new sqlite.Database('./../database/sqliteDb.db', (err) => {
    if (err) {
      console.error(err.message);
    }
    console.log('Connected to the database.');
  });

/*
const db = mysql.createConnection({
    host: "195.178.232.16",
    user: "aj1757",
    password: "foodbank123",
    database: "aj1757"
});
*/
const app = express();

app.use(bodyParser.urlencoded({ extended: true }));

app.set("port", process.env.PORT || 3000);
app.use(express.static(__dirname + "/public"));
app.set("views", __dirname + "/views");
app.engine("html", require("ejs").renderFile);
app.set("view engine", "html");

app.use(express.static(path.join(__dirname, "public")));

const queryRelationsWhereRecipeId = `SELECT * FROM relations WHERE recipe_id=`;
const queryRecipesWhereRecipeId = `SELECT * FROM recipes WHERE recipe_id=`;
const queryRecipesWhereCategory = `SELECT * FROM recipes WHERE category=`;
const queryIngredientsWhereId = `SELECT * FROM ingredients2 WHERE id =`;

/*

När användaren läser in recept/id används id för att hämta receptet från databasen som har id som recipe_id.
Efteråt beräknas sammanlagt pris ut av getRecipesPrices() och renderar HTML-sidan Receptsida.

*/
app.get("/recept/:id", (req, res) => {
    var result1;

    db.query(queryRelationsWhereRecipeId + req.params.id, (err, result) => {
        if (err) throw err;
        result1 = result;
    });
    db.query(queryRecipesWhereRecipeId + req.params.id, (err, result) => {
        if (err) throw err;

        getRecipesPrices(result1, res, result);
    });
});

/*

När användaren läser in kategorier/id används id för att hämta recepten från databasen som har id  kategori-id.
De tre första recepten från databasen placeras i vars ett kort och HTML-sidan Kategorisida renderas.

*/

app.get("/kategorier/:id", (req, res) => {
    getCategoryPrices(req.params.id, res, req);
});

/*

När användaren läser in hem renderas HTML-sidan Hemsida.

*/

app.get("/", (req, res) => {
    res.render("Hemsida");
});

/*

När användaren läser in kontakt renderas HTML-sidan Kontaktsida.

*/

app.get("/kontakt/", (req, res) => {
    res.render("Kontaktsida");
});

/*

Funktion som hämtar priset på varje ingrediens som tillhör det recept som skickas med som parameter array2 och adderar 
för att få fram ett pris

*/

async function getRecipesPrices(array, res, array2) {
    let totalPrice = 0;
    let listBuildString = ``;
    let instructionsBuildString = ``;
    let priceArray = [];

    await Promise.all(
        array.map(async item => {
            return new Promise((resolve, reject) => {
                db.all(
                    queryIngredientsWhereId + item.ingredients_id,
                    (err, result) => {
                        if (err) reject();
                        if(result[0] != null) {
                            let price = result[0].price;
                            for(let pos in array) {
                                if(array[pos].ingredients_id === result[0].id) {
                                    priceArray[pos] = Math.round((result[0].price / 1000) * item.units);
                                }
                            }
                            totalPrice += (price / 1000) * item.units;
                            resolve();
                        } else {
                            resolve();
                        }
                    }
                );
            });
        })
    );

    totalPrice = Math.round(totalPrice);

    splitIngredients();

    async function splitIngredients() {

    let listString = array2[0].ingredients + '';

    let listSplit = listString.split('\\');

    await Promise.all(
        listSplit.map(async item => {
            return new Promise((resolve, reject) => {
                let price = priceArray[listSplit.indexOf(item)];
                if(price == null) {
                    price = "SAKNAS";
                } else if (price == 0) {
                    price = "<1:-";
                } else {
                    price = price + ":-";
                }
                listBuildString += `<tr><td>`+ item +`</td><td><span style="color:green">` + price + `</span></td></tr>`;
                resolve();
            })
                
        })
        
    );

        splitInstructions();
    }

    async function splitInstructions() {


        let instructionsString = array2[0].instructions + '';

        let instructionsSplit = instructionsString.split('\\');
    
        await Promise.all(
            instructionsSplit.map(async item => {
                return new Promise((resolve, reject) => {
                        instructionsBuildString += `<li class="list-group-item">`+ item +`</li>`;
                        resolve();   
                        })
                    
                })
            
        );

    renderRecipe();

    }

    async function renderRecipe() {

    let recipeRender = "";

    recipeRender += String.raw`<div class="container">
                                <div class="card" style="width: 100%;">
                                        <div class="row">
                                                
                                                <img class="bilden" src="` + array2[0].image +`"  style="width:40%; object-fit: cover; transform: translate(15px, 0px);" alt="...">
                                                <div class="col">
                                                    <div class="container mt-4">
                                                        <h1 class="recipe-title">`+ array2[0].title +`</h1>
                                                        <hr>
                                                        <p class="recipe-description">
                                                                `+ array2[0].descr + `
                                                        </p>
                                                        <p class="recipe-totalPrice">
                                                        Pris: `+ totalPrice + `:-
                                                        </p>
                                                        <p class="recipe-portions">
                                                        Portioner: `+ array2[0].portions + `st
                                                        </p>
                                                        <p class="recipe-portionPrice">
                                                        Pris per portion ≈ `+ Math.round( (totalPrice/array2[0].portions) ) + `:-
                                                        </p>
                                                    </div>
                                                </div>
                                                <div class="w-100"></div>
                                                <div class="col">
                                                    <div class="container mt-4">
                                                        <h3>Ingredienser</h3>
                                                        <table class="table mt-4" style="width:80%;">
                                                            <thead>
                                                                <tr>
                                                                    <th scope="col">Ingrediens</th>
                                                                    <th scope="col">Pris</th>
                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                `
                                                                    + listBuildString +
                                                                  `
                                                            </tbody>
                                                        </table>
                                                    </div>
                                                </div>
                                                <div class="col" style="transform: translate(-100px, 0px);">
                                                        <div class="container mt-4">
                                                                <h3>Gör så här</h3>
                                                                    <ol class="list-group list-group-flush mt-4" style="list-style: decimal inside;" style="width:80%;">`
                                                                          + instructionsBuildString +  
                                                                          `</ol>
                                                        </div>
                                                </div>
                                         </div>
                                 </div>
                             </div>`;

        res.render("Receptsida", {
            result: recipeRender
        });
    }
}

async function getCategoryPrices(category, res, req) {
    let totalPrice;
    let categoryS = `btn-`+ category;

    let array;

    db.all(queryRecipesWhereCategory + category, async (err, result) => {
        totalPrice = new Array(result.length);
        totalPrice.fill(0);
        array = result;

        // Nestade Promise.all-funktioner löser problemet med att synkronisera flera databashämtningar och beräkningar
        await Promise.all(
            result.map(async item => {
                return new Promise((resolve, reject) => {
                    db.all(
                        queryRelationsWhereRecipeId + item.recipe_id,
                        async (err, result2) => {
                            await Promise.all(
                                result2.map(async item2 => {
                                    return new Promise((resolve2, reject) => {
                                        totalPrice[
                                            result
                                                .map(x => x.recipe_id)
                                                .indexOf(item2.recipe_id)
                                        ] += item2.price;
                                        console.log(totalPrice);
                                        resolve2();
                                    });
                                })
                            );
                            resolve();
                        }
                    );
                });
            })
        );

        run();
        console.log("TotalPrice finished");
    });

    function calcPrice(units, price) {
        return (price / 1000) * units;
    }

    async function run() {
        await Promise.all(
            totalPrice.map(async item => {
                return new Promise((resolve, reject) => {
                    totalPrice[totalPrice.indexOf(item)] = Math.round(
                        totalPrice[totalPrice.indexOf(item)]
                    );
                    // Blockar ut all pris-utskrift till terminalen.
                    // console.log(totalPrice);
                    resolve();
                });
            })
        );

        renderCards(array, totalPrice, res, req, categoryS);
    }
}

async function renderCards(recipes, totalPrice, res, req, categoryS) {
    let resultCard = "";

    await Promise.all(
        recipes.map(async item => {
            return new Promise((resolve, reject) => {
                resultCard += String.raw`
                    <div class="card">
                        <div class="card__content">
                            <div class="card__front">
                                <img class="card-img-top" src="` + recipes[recipes.indexOf(item)].image + `" alt="Card image cap">
                                    <h3 class="card__title">` + recipes[recipes.indexOf(item)].title + `</h3>
                                    <p class="card__subtitle" style="color:green; font-size: 20px;">` + totalPrice[recipes.indexOf(item)] + `:- </p>
                                </div> 
                                <li class="nav-card">
                                <a href="/recept/` + recipes[recipes.indexOf(item)].recipe_id + `" class="nav-cards">
                                
                                <div class="card__back">
                                    <p class="card__body">` + recipes[recipes.indexOf(item)].descr + `
                                    <br>
                                    <br> <p class="card__body" style="color:green;">klicka här för mer info..</p>
                                    <br>
                                    <p class="card__subtitle" style="color:green; font-size: 20px;">` + totalPrice[recipes.indexOf(item)] + `:- </p>
                                </div>
                                </a>
                             </div>
                        </div>
                    `;
                resolve();
            });
        })
    );

    res.render("Kategorisida", {
        result: resultCard,
        categoryStyle: categoryS
    });
    req.next = function() {};
}

app.listen(app.get("port"), () => {
    console.log("Server started on port " + app.get("port"));
});

