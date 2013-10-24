package cscie97.asn3.ecommerce.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 10/23/13
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Collectible {

    public String id;

    public String name;

    public String description;

    public List<Collectible> children = new ArrayList<Collectible>();


    public void add(Collectible collectible) {

    }

    public void remove(Collectible collectible) {

    }
    public List<Collectible> getChildren() {
        return this.children;
    }






}
