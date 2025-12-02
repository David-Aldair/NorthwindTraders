package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {


        //did we pass in a username and password
        //if not, the application must die
        if(args.length != 2){
            //display a message to the user
            System.out.println("Application needs two args to run: A username and a password for the db");
            //exit the app due to failure because we dont have a username and password from the command line
            System.exit(1);
        }

        //get the username and password from args[]
        String username = args[0];
        String password = args[1];

        //scanner
        Scanner scanner = new Scanner(System.in);



            //create the connection (kinda like opening mySQL Workbench)
        try{
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", username, password);

            while(true){
                System.out.println("""
                        What do you want to do?
                            1)Display Products
                            0)Exit
                        """);

                switch(scanner.nextInt()){
                    case 1:
                        displayProducts(connection);
                        break;
                    case 0:
                        System.out.println("Goodbye!");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice");
                }
            }

        }catch (SQLException e){

            System.out.println("Something went wrong" + e);
            System.exit(1);
        }
    }

    public static void displayProducts(Connection connection) {


        try (
                PreparedStatement preparedStatement = connection.prepareStatement("""
                        SELECT
                            ProductID,
                            ProductName,
                            UnitPrice,
                            UnitsInStock
                        FROM
                            products
                       
                        """
                );

                ResultSet resultSet = preparedStatement.executeQuery();
        ) {

            printResults(resultSet);


        } catch (SQLException e) {
            System.out.println("Could not get all the products" + e);
            System.exit(1);
        }
    }

    //this method will be used in the displayMethods to actually print the results to the screen
    public static void printResults(ResultSet results) throws SQLException {
        System.out.printf("%-5s %-35s %-10s %-10s\n",
                "ID", "Name", "Price", "Stock");
        System.out.println("----- ----------------------------------- ---------- -------------");

        while (results.next()) {
            int id = results.getInt("ProductID");
            String name = results.getString("ProductName");
            double price = results.getDouble("UnitPrice");
            int stock = results.getInt("UnitsInStock");

            System.out.printf("%-5d %-35s %-10.2f %-10d\n",
                    id, name, price, stock);


        }
    }
}
