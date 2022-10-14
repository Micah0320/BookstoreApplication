
package shoppingCart;
// Chapters 11 and 12 in your text contains several examples.
// Also see chapter 7 from details on ArrayList


/* Add your required multi-line comment here
 * Name: Micah Arndt
 * File: ShoppingCart.java
 * The purpose of this project is to use JavaFX in order to make a shopping cart app
 */

//imports
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
//Imports from my own personal tests (IO and Date formats)
import java.util.*;
import java.io.*;
import java.text.*;  
import java.util.Date;  

//for the date
import java.text.SimpleDateFormat;



public class ShoppingCart extends Application
{
   /* ArrayList to hold info from the BookProces.txt file, cart information
    * and other required variables
    * See chapter 7 from details on ArrayList
    */
	  ArrayList<Book> bookList, cartList;
	  ListView<Book> booksView, cartView;
	  Label labelSubTotal, labelTax, labelTotal;
	  double subTotal = 0, tax = 0, total = 0;
	  	
   //java always starts in the main method	
   public static void main(String[] args)
   {
      // the system prints will display in the console window.
	  // they are not required but could be helpfule wiht debuging.
	  System.out.println("Lauching"); 
      // Launch the application.
      launch(args);
      System.out.println("Exiting"); 
   }
   
   //Utility Functions
   
   //Creates a date string
   public static String Dateify()
   {
        String pattern = "MM/dd/yyyy HH:mm:ss";

       // Create an instance of SimpleDateFormat used for formatting 
       // the string representation of date according to the chosen pattern
       DateFormat df = new SimpleDateFormat(pattern);
       
       // Get the today date using Calendar object.
       Date today = Calendar.getInstance().getTime();        
       // Using DateFormat format method we can create a string 
       // representation of a date with the defined format.
       String todayAsString = df.format(today);

       return todayAsString + '\n';


   }

   //Get Tax Amount
   public static Double getTax(Double price)
   {
	   return price * .07;
   }
   //Formats a book object
   public static String formatBook(Book b)
   {
       int Padding = 36;
       String BookString = b.getTitle();
       for (int i = BookString.length(); i < Padding; ++i)
           {
               BookString = BookString + ' ';
           }
       BookString = BookString + '$' + String.format("%,.2f", b.getPrice());

       return BookString + '\n';

   }
   //Pads each line of a receipt string
   //Makes it look pretty
   public static String spacePad(String s)
   {
       for (int i = s.length(); i < 36; ++i)
           {
               s = s + ' ';
           }
       return s;
   }
   //Gets the book titles in one large string. For the receipt
   public static String getBookCart(ArrayList<Book> b)
   {
	   String BookCart = "";
       for (int i = 0; i < b.size(); ++i)
           {
               BookCart = BookCart + formatBook(b.get(i));
           }
       return BookCart;
   }
   
   public static Double get_total(ArrayList<Book> b)
   {
	   Double total = 0.0;
	   for (int i = 0; i < b.size(); ++i)
       {
           total += b.get(i).getPrice();
       }
	   return total;
   
	   
   }
   //Write Function for receipt writing
   public static void Write(File file, String BookString,
           Double total, Double tax)
   {
	   //Different Aspects that will be used in our reciept
	   String hyphens = "----------------------------------- ---------\n";
	   String header = "Title                                Price\n";
	   String subTotal = "Sub-Total: ";
	   //Pads the sub-total with spaces before printing the formatted
	   //sub-total
	   subTotal = spacePad(subTotal) + "$" + String.format("%,.2f",total)
	   + '\n';
	   //Creates the tax total and pads it for printing
	   String taxTotal = spacePad("Tax: ") +"$"
			   + String.format("%,.2f",tax) + '\n';
	   //Creates the lower sum. Aesthetic Purposes only
	   String lowerHyphen = spacePad("") + "---------\n";
	   //Creates the net total string for printing
	   String netTotal = spacePad("Total: ") + "$"
			   + String.format("%,.2f",(total + tax));


	   try
	   {
		   FileWriter myWriter = new FileWriter(file);
		   //Writes one long string to the receipt
		   myWriter.write("Receipt: " + Dateify() + '\n' + header
             + hyphens + BookString + '\n'
             + subTotal + taxTotal
             + lowerHyphen + netTotal);
		   myWriter.close();
		   //System.out.println("Successfully wrote to the file.");
	   }
	   catch (IOException e)
	   {
		   System.out.println("An error occurred.");
		   e.printStackTrace();
	   }
   }
   
   // Chapters 11 and 12 in your text contains several examples.
   // start method entry point of the application
   @Override
   public void start(Stage primaryStage) throws IOException
   {
	  // Launch start here
	  System.out.println("App Starting"); 
      // Build the inventory ArrayLists
	  bookList = new ArrayList<Book>();
	  cartList = new ArrayList<Book>();
	  readBookFile(); 
	   
	   
	  // work your way  
      // Convert the inventoryTtitles ArrayList to an ObservableList.
	   ObservableList<Book> bookObserveList =
			    FXCollections.observableArrayList(bookList);
	   ObservableList<Book> cartObserveList =
			    FXCollections.observableArrayList(cartList);
      // Build the Book ListView
	   booksView = new ListView<>(bookObserveList);
      // Build the Shopping Cart ListView
	   cartView = new ListView<>(cartObserveList);    
      // Create the output label for the cart subtotal.
	   Label subTotalLabel = new Label ("Sub-Total: $" + String.format("%,.2f", subTotal) + '\t');
      
      // Create the output label for the tax.
	   Label taxLabel = new Label ("Tax: $" + String.format("%,.2f", tax) + "     ");
      // Create the output label for the cart total.
	   Label TotalLabel = new Label ("Total: $" + String.format("%,.2f", total));
      // Add To Cart Button
      Button addToCartButton = new Button("Add To Cart");
      addToCartButton.setOnAction(e ->
      {
         // Get the selected index.
         int index = booksView.getSelectionModel().getSelectedIndex();// Get the selected index.

         // Add the item to the cart.
         if (index != -1)
         {
            // Update the cart ArrayLists
            cartList.add(bookList.get(index));
            // Update the cartListView
            cartView.getItems().add(bookList.get(index));
            
            // Update the subtotal
            subTotal += bookList.get(index).getPrice();
            subTotalLabel.setText("Sub-Total: $" + String.format("%,.2f", subTotal) + '\t');
            
            }
      });
      
      // Remove From Cart Button
      Button removeFromCartButton = new Button("Remove From Cart");
      removeFromCartButton.setOnAction(e ->
      {
         // Get the selected index.
         int index = cartView.getSelectionModel().getSelectedIndex();// Get the selected index.
         // Add the item to the cart.
         if (index != -1)
         {
        	 
            // Update the subtotal
        	 subTotal -= cartList.get(index).getPrice();
        	 subTotalLabel.setText("Sub-Total: $" + String.format("%,.2f", subTotal) + '\t');
            
        	 cartView.getItems().remove(cartList.get(index));
            // Remove the selected item from the cart ArrayLists
            cartList.remove(index);
            // Update the cartListView
            
         }
      });
      
      // Clear Cart Button
      Button clearCartButton = new Button("Clear Cart");
      clearCartButton.setOnAction(e ->
      {
         // Update the subtotal
    	  subTotal = 0;
          subTotalLabel.setText("Sub-Total: $" + String.format("%,.2f", subTotal) + '\t');
         
         // Clear the cart ArrayLists
    	  cartList.clear();
         
         // Update the cartListView
    	  cartView.getItems().clear();
      });
      
      // Checkout Button
      Button checkoutButton = new Button("Checkout");
      checkoutButton.setOnAction(e ->
      {
         final double TAX_RATE = 0.07;
         
         // Calculate the tax
       //Update Tax
         tax = subTotal * TAX_RATE;
         taxLabel.setText("Tax: $" + String.format("%,.2f", tax) + "     ");
         
         //Update the Total
         total = subTotal + tax;
         TotalLabel.setText("Total: $" + String.format("%,.2f", total));
      
         
         //setup dates
         String timeStamp = new SimpleDateFormat("MM-DD-YYYY HH:mm:ss").format(Calendar.getInstance().getTime());
         String fntimeStamp = new SimpleDateFormat("MM-DD-YYYY.HH.mm.ss").format(Calendar.getInstance().getTime());
         
         //create and open receipt file
         String filename = "Receipt-"+fntimeStamp+".txt"; 
        	   //open file
         File receiptFile = new File("src/shoppingCart/"+filename);
			   
			   //create the receipt
         String bookCart = "";
         total = get_total(cartList);
         tax = total * TAX_RATE;
         bookCart = getBookCart(cartList);
         Write(receiptFile, bookCart, total, tax);
         
      });
      
      // Build the VBox to hold the Add button
      VBox addButton = new VBox(addToCartButton);
      addButton.setAlignment(Pos.CENTER);
      // Build the VBox to hold the cart buttons
      VBox cartButtons = new VBox(removeFromCartButton, clearCartButton, checkoutButton);
      cartButtons.setAlignment(Pos.CENTER);
      // Build the top part of the GUI
      Label bookPrompt = new Label ("Pick a book");
      VBox books = new VBox(bookPrompt, booksView);
      
      Label cartPrompt = new Label ("Shopping Cart");
      VBox cartBox = new VBox(cartPrompt, cartView);
      
      HBox top = new HBox(books, addButton, cartBox, cartButtons);
      // Build the bottom part of the GUI
      HBox bottom = new HBox (subTotalLabel, taxLabel, TotalLabel);
      
      
      
      // Put everything into a VBox
      VBox main = new VBox(top, bottom);
      
      // Add the main VBox to a scene.
      Scene scene = new Scene(main);
      
      // Set the scene to the stage and display it.
      primaryStage.setScene(scene);
      
      primaryStage.show();
   }
   
   private void readBookFile() throws IOException
   {
      System.out.println("Reading Book File"); 
 
      String data;  // To hold a line from the file
    //Utility containers
      Vector<String> names = new Vector<String>();
      Vector<Double> price = new Vector<Double>();
      //Iterator
      int i = 0;
      // Read the file.
      
      // Open the file.
      File file = new File("src/shoppingCart/BookPrices.txt");
      //System.out.println("File Found");
      Scanner inFile = new Scanner(file);
    //Utility containers
      // Read the file.
      while (inFile.hasNextLine())
      {
         // Read a line.
    	  data = inFile.nextLine();
          names.add(data);
          
         // Tokenize the line.
          String[] arrOfStr = names.get(i).split(", ");
          names.set(i, arrOfStr[0]);
          price.add(Double.parseDouble(arrOfStr[1]));

          //Create a new Book Object:
          bookList.add(new Book(names.get(i), price.get(i)));
          i += 1;
         
         // Add the book info to the ArrayLists.
      }
      
      // Close the file.
      inFile.close();
   }
}
