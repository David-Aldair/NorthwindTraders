package com.pluralsight;

import java.sql.*;
import java.util.Scanner;

public class App {

    //scanner
    public static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {


        //did we pass in a username and password
        //if not, the application must die
        if (args.length != 2) {
            //display a message to the user
            System.out.println("Application needs two args to run: A username and a password for the db");
            //exit the app due to failure because we dont have a username and password from the command line
            System.exit(1);
        }

        //get the username and password from args[]
        String username = args[0];
        String password = args[1];

        //create the connection (kinda like opening mySQL Workbench)
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/northwind", username, password);

            while (true) {
                System.out.println("""
                        What do you want to do?
                            1)Display All Products
                            2)Display All Customers
                            3)Display All Categories
                            0)Exit
                        """);

                switch (scanner.nextInt()) {
                    case 1:
                        displayAllProducts(connection);
                        break;
                    case 2:
                        displayAllCustomers(connection);
                        break;
                    case 3:
                        displayAllCategories(connection);
                        break;
                    case 0:
                        System.out.println("Goodbye!");
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice");
                }
            }

        } catch (SQLException e) {

            System.out.println("Something went wrong" + e);
            System.exit(1);
        }
    }

    public static void displayAllProducts(Connection connection) {


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

            printAllProducts(resultSet);


        } catch (SQLException e) {
            System.out.println("Could not get all the products" + e);
            System.exit(1);
        }
    }

    public static void displayAllCustomers(Connection connection) {

        try (
                PreparedStatement preparedStatement = connection.prepareStatement("""
                        SELECT
                            ContactName,
                            CompanyName,
                            City,
                            Country,
                            Phone
                        FROM
                            Customers
                        """
                );

                ResultSet resultSet = preparedStatement.executeQuery();
        ) {

            printAllCustomers(resultSet);


        } catch (SQLException e) {
            System.out.println("Could not get all the customers" + e);
            System.exit(1);
        }
    }

    public static void displayAllCategories(Connection connection) {

        try (
                PreparedStatement preparedStatement = connection.prepareStatement("""
                        SELECT
                            CategoryID,
                            CategoryName
                        FROM
                            Categories
                        ORDER BY
                            CategoryID;
                        """
                )) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                printAllCategories(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Could not get all the categories" + e);
            System.exit(1);
        }

        System.out.println("What category would you like?");
        int categoryNum = scanner.nextInt();
        scanner.nextLine();

        try (
                PreparedStatement preparedStatement = connection.prepareStatement("""
                        SELECT
                            ProductID,
                            ProductName,
                            UnitPrice,
                            UnitsInStock
                        FROM
                            products
                        WHERE
                            CategoryID = ?
                        
                        """
                )) {

            preparedStatement.setInt(1, categoryNum);

            ResultSet resultSet = preparedStatement.executeQuery();

            printAllProducts(resultSet);

        } catch (SQLException e) {
            System.out.println("Could not get all the products" + e);
            System.exit(1);
        }
    }

    //this method will be used in the displayMethods to actually print the results to the screen
    public static void printAllProducts(ResultSet results) throws SQLException {
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

    public static void printAllCustomers(ResultSet results) throws SQLException {
        System.out.printf("%-30s %-35s %-20s %-20s %-10s\n",
                "Contact Name", "Company Name", "City", "Country", "Phone");
        System.out.println("---------------------------------------------------------------------------------------------------------------------");

        while (results.next()) {
            String conName = results.getString("ContactName");
            String comName = results.getString("CompanyName");
            String City = results.getString("City");
            String Country = results.getString("Country");
            String phone = results.getString("Phone");

            System.out.printf("%-30s %-35s %-20s %-20s %-10s\n",
                    conName, comName, City, Country, phone);
        }
    }
    public static void printAllCategories(ResultSet results) throws SQLException {

        // Print header row
        System.out.printf("%-15s %-30s\n", "Category ID", "Category Name");
        System.out.println("--------------------------------------------------------");

        // Print each row returned from the query
        while (results.next()) {
            int categoryId = results.getInt("CategoryID");
            String categoryName = results.getString("CategoryName");

            System.out.printf("%-15d %-30s\n", categoryId, categoryName);
        }
    }

}
