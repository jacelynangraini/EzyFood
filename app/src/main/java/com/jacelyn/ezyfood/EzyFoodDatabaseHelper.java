package com.jacelyn.ezyfood;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EzyFoodDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "ezyfood";
    private static final int DB_VERSION = 2;

    public EzyFoodDatabaseHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDB(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDB(db, oldVersion, newVersion);
    }

    private static void insertRestaurant(SQLiteDatabase db, int id, String name, String lat, String lng){
        ContentValues restValues = new ContentValues();
        restValues.put("RESTAURANT_ID", id);
        restValues.put("NAME", name);
        restValues.put("LAT", lat);
        restValues.put("LNG", lng);
        db.insert("RESTAURANT", null, restValues);
    }

    private static void insertMenu(SQLiteDatabase db, String cat, int id, String name, int imgID, int price, int restId, int stock){
        ContentValues menuValues = new ContentValues();
        menuValues.put("CATEGORY", cat);
        menuValues.put("MENU_ID", id);
        menuValues.put("NAME", name);
        menuValues.put("IMG", imgID);
        menuValues.put("PRICE", price);
        menuValues.put("RESTAURANT_ID", restId);
        menuValues.put("STOCK", stock);

        db.insert("MENU", null, menuValues);
    }
    static void insertOrder(SQLiteDatabase db, int menuid, String name, int imgID, int price, int restId, int qty){
        ContentValues orderValues = new ContentValues();
        orderValues.put("MENU_ID", menuid);
        orderValues.put("NAME", name);
        orderValues.put("IMG", imgID);
        orderValues.put("PRICE", price);
        orderValues.put("RESTAURANT_ID", restId);
        orderValues.put("QTY", qty);
        orderValues.put("ORDER_PRICE", qty*price);

        db.insert("CART", null, orderValues);
    }

    static void insertHistory(SQLiteDatabase db, String trdate, String address, int menuid, String name, int imgID, int price, int restId, int qty){
        ContentValues orderValues = new ContentValues();
        orderValues.put("TRANSACTION_DATE", trdate);
        orderValues.put("ADDRESS", address);
        orderValues.put("MENU_ID", menuid);
        orderValues.put("NAME", name);
        orderValues.put("IMG", imgID);
        orderValues.put("PRICE", price);
        orderValues.put("RESTAURANT_ID", restId);
        orderValues.put("QTY", qty);
        orderValues.put("ORDER_PRICE", qty*price);
        db.insert("HISTORY", null, orderValues);
    }

    private void updateMyDB(SQLiteDatabase db, int oldVersion, int newVersion){
        if(oldVersion<=1){
            //table RESTAURANT
            db.execSQL("CREATE TABLE RESTAURANT ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "RESTAURANT_ID INTEGER, "
                    + "NAME TEXT, "
                    + "LAT TEXT, "
                    + "LNG TEXT);");
            insertRestaurant(db, 1, "Central Park", "-6.176873", "106.790807");
            insertRestaurant(db, 2, "PIK Avenue", "-6.1090330225994895", "106.74014991064062");
            insertRestaurant(db, 3, "Grand Indonesia", "-6.194924107127545", "106.82045192598413");

            //table MENU
            db.execSQL("CREATE TABLE MENU ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "CATEGORY TEXT, "
                    + "MENU_ID INTEGER, "
                    + "NAME TEXT, "
                    + "IMG INTEGER, "
                    + "PRICE INTEGER, "
                    + "RESTAURANT_ID INTEGER, "
                    + "STOCK INTEGER,"
                    + "FOREIGN KEY(RESTAURANT_ID) REFERENCES RESTAURANT(RESTAURANT_ID));");
            insertMenu(db, "Drink", 1, "Strawberry Smoothie", R.drawable.drinks__strawberry_smoothie, 15000, 1, 20);
            insertMenu(db, "Drink", 2, "Vanilla Smoothie", R.drawable.drinks__vanilla_smoothie, 12500, 1, 20);
            insertMenu(db, "Drink", 3, "Chocolate Smoothie", R.drawable.drinks_chocolate_smoothie, 15000, 1, 20);
            insertMenu(db, "Drink", 4, "Orange Juice", R.drawable.drinks__orange_juice, 12500, 1, 20);
            insertMenu(db, "Drink", 5, "Avocado Juice", R.drawable.drinks__avocado_juice, 18000, 1, 20);
            insertMenu(db, "Drink", 6, "Lemonade", R.drawable.drinks_lemon_juice, 12500, 1, 20);
            insertMenu(db, "Drink", 7, "Mango Juice", R.drawable.drinks_mango_juice, 15000, 1, 20);
            insertMenu(db, "Drink", 8, "Tea", R.drawable.drinks_tea, 5000, 1, 20);
            insertMenu(db, "Drink", 9, "Coffee", R.drawable.drinks__coffee, 7500, 1, 20);
            insertMenu(db, "Drink", 10, "Soda", R.drawable.drinks__soda, 5000, 1, 20);
            insertMenu(db, "Drink", 11, "Bubble Milk Tea", R.drawable.drinks__boba, 17500, 1, 20);
            insertMenu(db, "Drink", 12, "Mineral Water", R.drawable.drinks__water, 5000, 1, 20);

            insertMenu(db, "Drink", 13, "Strawberry Smoothie", R.drawable.drinks__strawberry_smoothie, 15000, 2, 30);
            insertMenu(db, "Drink", 14, "Vanilla Smoothie", R.drawable.drinks__vanilla_smoothie, 12500, 2, 30);
            insertMenu(db, "Drink", 15, "Chocolate Smoothie", R.drawable.drinks_chocolate_smoothie, 15000, 2, 30);
            insertMenu(db, "Drink", 16, "Orange Juice", R.drawable.drinks__orange_juice, 12500, 2, 30);
            insertMenu(db, "Drink", 17, "Avocado Juice", R.drawable.drinks__avocado_juice, 18000, 2, 30);
            insertMenu(db, "Drink", 18, "Lemonade", R.drawable.drinks_lemon_juice, 12500, 2, 30);
            insertMenu(db, "Drink", 19,  "Mango Juice", R.drawable.drinks_mango_juice, 15000, 2, 30);
            insertMenu(db, "Drink", 20,  "Tea", R.drawable.drinks_tea, 5000, 2, 30);
            insertMenu(db, "Drink", 21,  "Coffee", R.drawable.drinks__coffee, 7500, 2, 30);
            insertMenu(db, "Drink", 22,  "Soda", R.drawable.drinks__soda, 5000, 2, 30);
            insertMenu(db, "Drink", 23,  "Bubble Milk Tea", R.drawable.drinks__boba, 17500, 2, 30);
            insertMenu(db, "Drink", 24,  "Mineral Water", R.drawable.drinks__water, 5000, 2, 30);

            insertMenu(db, "Drink", 25,  "Strawberry Smoothie", R.drawable.drinks__strawberry_smoothie, 15000, 3, 40);
            insertMenu(db, "Drink", 26,  "Vanilla Smoothie", R.drawable.drinks__vanilla_smoothie, 12500, 3, 40);
            insertMenu(db, "Drink", 27,  "Chocolate Smoothie", R.drawable.drinks_chocolate_smoothie, 15000, 3, 40);
            insertMenu(db, "Drink", 28,  "Orange Juice", R.drawable.drinks__orange_juice, 12500, 3, 40);
            insertMenu(db, "Drink", 29,  "Avocado Juice", R.drawable.drinks__avocado_juice, 18000, 3, 40);
            insertMenu(db, "Drink", 30,  "Lemonade", R.drawable.drinks_lemon_juice, 12500, 3, 40);
            insertMenu(db, "Drink", 31,  "Mango Juice", R.drawable.drinks_mango_juice, 15000, 3, 40);
            insertMenu(db, "Drink", 32,  "Tea", R.drawable.drinks_tea, 5000, 3, 40);
            insertMenu(db, "Drink", 33,  "Coffee", R.drawable.drinks__coffee, 7500, 3, 40);
            insertMenu(db, "Drink", 34,  "Soda", R.drawable.drinks__soda, 5000, 3, 40);
            insertMenu(db, "Drink", 35,  "Bubble Milk Tea", R.drawable.drinks__boba, 17500, 3, 40);
            insertMenu(db, "Drink", 36,  "Mineral Water", R.drawable.drinks__water, 5000, 3, 40);

            insertMenu(db, "Food", 37,  "Pancake", R.drawable.food__pancakes, 15000, 1, 20);
            insertMenu(db, "Food", 38,  "Waffle", R.drawable.food__waffle, 17000, 1, 20);
            insertMenu(db, "Food", 39,  "Pizza (1 slice)", R.drawable.food__pizza, 15000, 1, 20);
            insertMenu(db, "Food", 40,  "Chicken Wings", R.drawable.food__chicken_wing, 20000, 1, 20);
            insertMenu(db, "Food", 41,  "Steak", R.drawable.food__steak, 30000, 1, 20);
            insertMenu(db, "Food",  42,  "Hamburger", R.drawable.food__burger, 25000, 1, 20);
            insertMenu(db, "Food", 43,  "Spaghetti Bolognese", R.drawable.food__spaghetti, 20000, 1, 20);
            insertMenu(db, "Food", 44,  "Fried Tofu", R.drawable.food__tofu, 18000, 1, 20);
            insertMenu(db, "Food", 45,  "Mac and Cheese", R.drawable.food__mac_and_cheese, 20000, 1, 20);
            insertMenu(db, "Food", 46,  "Sushi", R.drawable.food__sushi, 22500, 1, 20);
            insertMenu(db, "Food", 47,  "Ramen Noodles", R.drawable.food__ramen, 25000, 1, 20);
            insertMenu(db, "Food", 48,  "Lasagna", R.drawable.food__lasagna, 27500, 1, 20);
            insertMenu(db, "Food", 49,  "Chicken Curry Rice", R.drawable.food__chicken_curry_rice, 22500, 1, 20);

            insertMenu(db, "Food", 50,  "Pancake", R.drawable.food__pancakes, 15000, 2, 30);
            insertMenu(db, "Food", 51,  "Waffle", R.drawable.food__waffle, 17000, 2, 30);
            insertMenu(db, "Food", 52,  "Pizza (1 slice)", R.drawable.food__pizza, 15000, 2, 30);
            insertMenu(db, "Food", 53,  "Chicken Wings", R.drawable.food__chicken_wing, 20000, 2, 30);
            insertMenu(db, "Food", 54,  "Steak", R.drawable.food__steak, 30000, 2, 30);
            insertMenu(db, "Food", 55,  "Hamburger", R.drawable.food__burger, 25000, 2, 30);
            insertMenu(db, "Food", 56,  "Spaghetti Bolognese", R.drawable.food__spaghetti, 20000, 2, 30);
            insertMenu(db, "Food", 57,  "Fried Tofu", R.drawable.food__tofu, 18000, 1, 20);
            insertMenu(db, "Food", 58,  "Mac and Cheese", R.drawable.food__mac_and_cheese, 20000, 2, 30);
            insertMenu(db, "Food", 59,  "Sushi", R.drawable.food__sushi, 22500, 2, 30);
            insertMenu(db, "Food", 60,  "Ramen Noodles", R.drawable.food__ramen, 25000, 2, 30);
            insertMenu(db, "Food", 61,  "Lasagna", R.drawable.food__lasagna, 27500, 2, 30);
            insertMenu(db, "Food", 62,  "Chicken Curry Rice", R.drawable.food__chicken_curry_rice, 22500, 2, 30);

            insertMenu(db, "Food", 63,  "Pancake", R.drawable.food__pancakes, 15000, 3, 40);
            insertMenu(db, "Food", 64,  "Waffle", R.drawable.food__waffle, 17000, 3, 40);
            insertMenu(db, "Food", 65,  "Pizza (1 slice)", R.drawable.food__pizza, 15000, 3, 40);
            insertMenu(db, "Food", 66,  "Chicken Wings", R.drawable.food__chicken_wing, 20000, 3, 40);
            insertMenu(db, "Food", 67,  "Steak", R.drawable.food__steak, 30000, 3, 40);
            insertMenu(db, "Food", 68,  "Hamburger", R.drawable.food__burger, 25000, 3, 40);
            insertMenu(db, "Food", 69,  "Spaghetti Bolognese", R.drawable.food__spaghetti, 20000, 3, 40);
            insertMenu(db, "Food", 70,  "Fried Tofu", R.drawable.food__tofu, 18000, 3, 40);
            insertMenu(db, "Food", 71,  "Mac and Cheese", R.drawable.food__mac_and_cheese, 20000, 3, 40);
            insertMenu(db, "Food", 72,  "Sushi", R.drawable.food__sushi, 22500, 3, 40);
            insertMenu(db, "Food", 73,  "Ramen Noodles", R.drawable.food__ramen, 25000, 3, 40);
            insertMenu(db, "Food", 74,  "Lasagna", R.drawable.food__lasagna, 27500, 3, 40);
            insertMenu(db, "Food", 75,  "Chicken Curry Rice", R.drawable.food__chicken_curry_rice, 22500, 3, 40);

            insertMenu(db, "Snack", 76,  "French Fries", R.drawable.snack__fries, 15000, 1, 20);
            insertMenu(db, "Snack", 77,  "Popcorn", R.drawable.snacks_popcorn, 15000, 1, 20);
            insertMenu(db, "Snack", 78,  "Chicken Nuggets", R.drawable.snack__nugget, 12500, 1, 20);
            insertMenu(db, "Snack", 79,  "Potato Chips", R.drawable.snacks_chips, 12500, 1, 20);
            insertMenu(db, "Snack", 80, "Butter Croissant", R.drawable.snacks__bread, 12500, 1, 20);
            insertMenu(db, "Snack", 81, "Chocolate Cupcake", R.drawable.snacks___choco_cupcake, 12500, 1, 20);
            insertMenu(db, "Snack", 82,  "Red Velvet Cake", R.drawable.snacks__red_velvet_cake, 15000, 1, 20);
            insertMenu(db, "Snack", 83,  "Tiramisu", R.drawable.snack__tiramisu, 15000, 1, 20);
            insertMenu(db, "Snack", 84,  "Chocolate Ice Cream", R.drawable.snacks___choco_ice_cream, 15000, 1, 20);
            insertMenu(db, "Snack", 85,  "Strawberry Ice Cream", R.drawable.snacks_icecream, 12500, 1, 20);
            insertMenu(db, "Snack", 86,  "Popsicles", R.drawable.snacks__rasberry_popsicle, 7500, 1, 20);

            insertMenu(db, "Snack", 87,  "French Fries", R.drawable.snack__fries, 15000, 2, 30);
            insertMenu(db, "Snack", 88,  "Popcorn", R.drawable.snacks_popcorn, 15000, 2, 30);
            insertMenu(db, "Snack", 89,  "Chicken Nuggets", R.drawable.snack__nugget, 12500, 2, 30);
            insertMenu(db, "Snack", 90,  "Potato Chips", R.drawable.snacks_chips, 12500, 2, 30);
            insertMenu(db, "Snack", 91,  "Butter Croissant", R.drawable.snacks__bread, 12500, 2, 30);
            insertMenu(db, "Snack", 92,  "Chocolate Cupcake", R.drawable.snacks___choco_cupcake, 12500, 2, 30);
            insertMenu(db, "Snack", 93,  "Red Velvet Cake", R.drawable.snacks__red_velvet_cake, 15000, 2, 30);
            insertMenu(db, "Snack", 94, "Tiramisu", R.drawable.snack__tiramisu, 15000, 2, 30);
            insertMenu(db, "Snack", 95,  "Chocolate Ice Cream", R.drawable.snacks___choco_ice_cream, 15000, 2, 30);
            insertMenu(db, "Snack", 96,  "Strawberry Ice Cream", R.drawable.snacks_icecream, 12500, 2, 30);
            insertMenu(db, "Snack", 97,  "Popsicles", R.drawable.snacks__rasberry_popsicle, 7500, 2, 30);

            insertMenu(db, "Snack", 98,  "French Fries", R.drawable.snack__fries, 15000, 3, 40);
            insertMenu(db, "Snack", 99,  "Popcorn", R.drawable.snacks_popcorn, 15000, 3, 40);
            insertMenu(db, "Snack", 100,  "Chicken Nuggets", R.drawable.snack__nugget, 12500, 3, 40);
            insertMenu(db, "Snack", 101,  "Potato Chips", R.drawable.snacks_chips, 12500, 3, 40);
            insertMenu(db, "Snack", 102,  "Butter Croissant", R.drawable.snacks__bread, 12500, 3, 40);
            insertMenu(db, "Snack", 103,  "Chocolate Cupcake", R.drawable.snacks___choco_cupcake, 12500, 3, 40);
            insertMenu(db, "Snack", 104,  "Red Velvet Cake", R.drawable.snacks__red_velvet_cake, 15000, 3, 40);
            insertMenu(db, "Snack", 105,  "Tiramisu", R.drawable.snack__tiramisu, 15000, 3, 40);
            insertMenu(db, "Snack", 106,  "Chocolate Ice Cream", R.drawable.snacks___choco_ice_cream, 15000, 3, 40);
            insertMenu(db, "Snack", 107,  "Strawberry Ice Cream", R.drawable.snacks_icecream, 12500, 3, 40);
            insertMenu(db, "Snack", 108,  "Popsicles", R.drawable.snacks__rasberry_popsicle, 7500, 3, 40);

//            //table TRANSACTION
            db.execSQL("CREATE TABLE TRANSACTIONS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "MENU_ID INTEGER, "
                    + "LNG TEXT);");

            //table cart
            db.execSQL("CREATE TABLE CART ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "MENU_ID INTEGER, "
                    + "NAME TEXT, "
                    + "IMG INTEGER, "
                    + "PRICE INTEGER, "
                    + "RESTAURANT_ID INTEGER, "
                    + "QTY INTEGER, "
                    + "ORDER_PRICE INTEGER, "
                    + "FOREIGN KEY(RESTAURANT_ID) REFERENCES RESTAURANT(RESTAURANT_ID), "
                    + "FOREIGN KEY(MENU_ID)REFERENCES MENU(MENU_ID));");

            //table history
            db.execSQL("CREATE TABLE HISTORY ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "TRANSACTION_DATE TEXT, "
                    + "ADDRESS TEXT, "
                    + "MENU_ID INTEGER, "
                    + "NAME TEXT, "
                    + "IMG INTEGER, "
                    + "PRICE INTEGER, "
                    + "RESTAURANT_ID INTEGER, "
                    + "QTY INTEGER, "
                    + "ORDER_PRICE INTEGER, "
                    + "FOREIGN KEY(RESTAURANT_ID) REFERENCES RESTAURANT(RESTAURANT_ID), "
                    + "FOREIGN KEY(MENU_ID)REFERENCES MENU(MENU_ID));");
        }

    }

}
