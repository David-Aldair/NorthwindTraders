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
                            ProductName
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
        //get the meta data so we have access to the field names
        ResultSetMetaData metaData = results.getMetaData();
        //get the number of rows returned
        int columnCount = metaData.getColumnCount();

        //this is looping over all the results from the DB
        while (results.next()) {

            //loop over each column in the rown and display the data
            for (int i = 1; i <= columnCount; i++) {
                //gets the current colum name
                String columnName = metaData.getColumnName(i);
                //get the current column value
                String value = results.getString(i);
                //print out the column name and column value
                System.out.println(columnName + ": " + value + " ");
            }

            //print an empty line to make the results prettier
            System.out.println();

        }
    }
}
