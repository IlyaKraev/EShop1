/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbutils;

import cmdutils.Command;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Customer;
import models.Product;
import models.ProductIdQuantity;

/**
 *
 * @author George.Pasparakis
 */
public class Database {
    public String server;
    public String username;
    public String password;
    public String database;
    
    Connection con;
    Statement statement;
    PreparedStatement prStatement;
    ResultSet rs;
    
    public Database() {
        
        username = "root";
        password = "CB12FT_Java!";
        database = "eshop1";
        server = "jdbc:mysql://localhost/" + database + "?useSSL=false&serverTimezone=Europe/Athens";
        try {
            con = DriverManager.getConnection(server, username, password);
            System.out.println("Connected!");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Connection is not established");
        }
    }
    
    // INSERT, UPDATE, DELETE <--- int result ----> executeUpdate
    // SELECT                 <--- ResultSet  ----> executeQuery
    
    // insert into customers
    public int insertCustomer(Customer c, String tableName) {
        // INSERT INTO `customers`(first_name, last_name, tel, email) 
        // VALUES("John", "Johnakos", "2111111", "j@j.jjj")
        int result = 0;
        StringBuilder sb = new StringBuilder();
        
        sb.append("INSERT INTO ");
        sb.append("`"); sb.append(tableName); sb.append("`");
        sb.append("(`first_name`, `last_name`, `tel`, `email`)");
        sb.append(" VALUES(");
        sb.append("\""); sb.append(c.getFirstName()); sb.append("\""); sb.append(",");
        sb.append("\""); sb.append(c.getLastName()); sb.append("\""); sb.append(",");
        sb.append("\""); sb.append(c.getTel()); sb.append("\""); sb.append(",");
        sb.append("\""); sb.append(c.getEmail()); sb.append("\"");
        sb.append(")");
        try {
            //        System.out.println(sb.toString());
            statement = con.createStatement();
            result = statement.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return(result);
    }
    
    public int insertProduct(Product p, String tableName) {
        // INSERT INTO `products`(`name`, `price`, `quantity`) 
        // VALUES("Fixit Kit", "187,65", "1")
        int result = 0;
        StringBuilder sb = new StringBuilder();
        
        sb.append("INSERT INTO ");
        sb.append("`"); sb.append(tableName); sb.append("`");
        sb.append("(`name`, `price`, `quantity`)");
        sb.append(" VALUES(");
        sb.append("\""); sb.append(p.getName()); sb.append("\""); sb.append(",");
        sb.append("\""); sb.append(p.getPrice()); sb.append("\""); sb.append(",");
        sb.append("\""); sb.append(p.getQuantity()); sb.append("\"");
        sb.append(")");
        try {
            //        System.out.println(sb.toString());
            statement = con.createStatement();
            result = statement.executeUpdate(sb.toString());
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return(result);
    }
    
    public int insertOrder(Scanner sc) {
        int result = 0;
        
        /*  
            Step    1 - Select customer 
                    1.1 SELECT * FROM customers
                    1.2 PRINT customers
                    1.3 From cmd choose customer
            Step    2 - Select products
                    2.1 SELECT * FROM products
                    2.2 PRINT products
                    2.3 From cmd choose products
            Step    3 - Sum products
            Step    4 - insertOrder()
            Step    5 - insertProducts()
        */
//        int customerId = selectCustomer(sc);
        List<ProductIdQuantity> productsIdsQuantities = selectProducts(sc);
        System.out.println(productsIdsQuantities);
        
        
        return(result);
    }
    
    public int selectCustomer(Scanner sc) {
        int customerId = -1;
        Command cmd = new Command();
        
        ResultSet rs;
        try {
            statement = con.createStatement();
            rs = statement.executeQuery("SELECT * FROM customers");
            while(rs.next()) {
                System.out.println(rs.getString("id") + ". " +
                                   rs.getString("first_name") + " " +
                                   rs.getString("last_name"));
            }
            // we should check that the returned Id is valid
            customerId = cmd.getIntField(sc, "Please select the customer");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return(customerId);
    }
    
    public List<ProductIdQuantity> selectProducts(Scanner sc) {
        List<ProductIdQuantity> productIdsQuantities = new ArrayList<>();
        Command cmd = new Command();
        
        ResultSet rs;
        try {
            statement = con.createStatement();
            rs = statement.executeQuery("SELECT * FROM products");
            while(rs.next()) {
                System.out.println(rs.getString("id") + ". " +
                                   rs.getString("name"));
            }
            // we should check that the returned Ids are valid
            int choice = 1;
            while(choice == 1) {
                // product id
                int prId = cmd.getIntField(sc, "Please select a product to add");
                
                
                // ask for quantity for the previous product
                int quant = cmd.getIntField(sc, "Please type the quantity of the product with id: " + prId);
                productIdsQuantities.add(new ProductIdQuantity(prId, quant));
                
                // ask if he would like to add one more product
                choice = cmd.getIntField(sc, "Would you like to add 1 more product, "
                                           + "if yes press 1 else press any other number");
            }
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return(productIdsQuantities);
    }
    
}
