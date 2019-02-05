package online.madeofmagicandwires.restaurant;

import android.support.annotation.NonNull;
import android.webkit.URLUtil;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;


/**
 * Class representing all relevant information about a menu item.
 */
public class RestaurantMenuItem implements Serializable {

    private int id;
    private String name;
    private String desc;
    private String imgUrl;
    private double price;
    private String category;

    /**
     * Standard constructor
     * @param name the name of the menu item
     * @param desc a description of the menu item
     * @param imgUrl link to a picture of the menu item
     * @param price the price of the menu item
     * @param category the category to which this item belongs
     */
    public RestaurantMenuItem(int id, String name, String desc, String imgUrl, double price, String category) {
        this.id = id;
        this.name = name;
        this.desc = desc;
        this.imgUrl = imgUrl;
        this.price = price;
        this.category = category;
    }

    /**
     * Get this item's ID
     * @return the unique identifier under which this item is stored in the online database
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the name of the menu item
     * @return what this menu item is called
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the description of the menu item
     * @return a short description of the menu item
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Returns the image url of the menu item
     * @return a http:// link towards an image depicting the menu item
     */
    public String getImgUrl() {
        return imgUrl;
    }

    /**
     * Returns the price of the menu item
     * @return the total cost of the menu item
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the category of the menu item
     * @return the category to which this item belongs
     */
    public String getCategory() {
        return category;
    }

    /**
     * Sets this item's API id
     * @param id the new id which this item is identifiable
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the name of this item
     * @param name the name of this menu item
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets a description of this item
     * @param desc a short description of the menu item
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Sets the image url of this item
     * @param imgUrl a valid http(s):// url to an image depicting the menu item;
     *               must be a valid http(s) to be set
     * @return true if the item's url was set, false if not (in which case it was not a valid url).
     */
    public boolean setImgUrl(String imgUrl) {
        if(URLUtil.isValidUrl(imgUrl) && (URLUtil.isHttpUrl(imgUrl) || URLUtil.isHttpsUrl(imgUrl))) {
            this.imgUrl = imgUrl;
            return true;
        }
        return false;

    }

    /**
     * Sets the image url of this item
     * @param imgUrl a valid http(s):// url to an image depicting the menu item
     * @return true if the item's url was set, false if not (in which case it was not a valid url).
     */
    public boolean setUrl(URL imgUrl) {
        this.imgUrl = imgUrl.toString();
        return true;
    }

    /**
     * Sets the price of this item
     * @param price sets the cost of this menu item
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Set the category of this item
     * @param category the category this menu item is to belong to
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     *
     * @param obj the reference object with which to compare.
     * @return {@code true} if this object is the same as the obj
     * argument; {@code false} otherwise.
     * @see #hashCode()
     * @see "https://www.sitepoint.com/implement-javas-equals-method-correctly/"
     * @see HashMap
     */
    @Override
    public boolean equals(Object obj) {
        boolean equals = false;
        if(obj == this) {
            equals = true;
        } else if( obj != null && getClass() == obj.getClass()) {
            RestaurantMenuItem other = (RestaurantMenuItem) obj;
            if(this.id == other.id &&
               this.name.equals(other.getName()) &&
               this.desc.equals(other.getDesc()) &&
               this.imgUrl.equals(other.getImgUrl()) &&
               this.category.equals(other.getCategory()) &&
               this.price == other.getPrice()) {
                equals = true;
            }
        }
        return equals;
    }

    /**
     * Returns a hash code value for the object. This method is
     * supported for the benefit of hash tables such as those provided by
     * {@link HashMap}.
     *
     * @return a hash code value for this object.
     * @see Object#equals(Object)
     * @see System#identityHashCode
     * @see "https://www.sitepoint.com/how-to-implement-javas-hashcode-correctly/"
     */
    @Override
    public int hashCode() {
        int prime = 13;
        int result = 0;
        result =  (prime * result) + this.id;
        result =  (prime * result) + ((this.name != null)  ? this.name.hashCode() : 0);
        result =  (prime * result) + ((this.desc != null)  ? this.desc.hashCode() : 0);
        result =  (prime * result) + ((this.category != null)  ? this.category.hashCode() : 0);

        return result;
    }

    /**
     * Returns a human readable text representation of the menu item
     * @return
     */
    @NonNull
    @Override
    public String toString() {
        return "{ " +
                "id: " + this.id + ", " +
                "name: " + "'" +  name + "'" + ", " +
                "desc: " + "'" + desc + "'" + ", " +
                "category: " + "'" + category + "'" + ", " +
                "imageUrl: " +  "'" + imgUrl + "'" + " }";
    }
}
