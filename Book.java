package shoppingCart;

/**
 A class to represent a Book
**/
public class Book
{
    private String title;
    private double price;
    public Book(String title, double price)
    {
        this.title = title;
        this.price = price;
    }
    public String getTitle()
    {
        return title;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public double getPrice()
    {
        return price;
    }
    public void setPrice(double price)
    {
        this.price = price;
    }
    @Override
    public String toString()
    {
        return title + " ($" + String.format("%,.2f", price) + ")";
    }

}
