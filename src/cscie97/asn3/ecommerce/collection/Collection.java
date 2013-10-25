package cscie97.asn3.ecommerce.collection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 10/23/13
 * Time: 12:19 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Collection extends Collectible {



    public String id;

    public String name;

    public String description;

    public List<Collection> childCollections = new ArrayList<Collection>();

    public CollectionIterator iterator;


    //public abstract Collection createCollection(String type);
    public static Collection createCollection(String type) {
        if (type != null && type.length() > 0) {
            if (type.equalsIgnoreCase("static")) {
                return new StaticCollection();
            } else if (type.equalsIgnoreCase("dynamic")) {
                return new DynamicCollection();
            }
        }
        return null;
    }


    public static boolean validateCollection(Collection collection) {
        if (collection != null && collection instanceof Collection) {
            return (
                    collection.getId() != null && collection.getId().length() > 0 &&
                    collection.getName() != null && collection.getName().length() > 0 &&
                    collection.getDescription() != null && collection.getDescription().length() > 0
            );
        }
        return false;
    }







    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Collection> getChildCollections() {
        return childCollections;
    }

    public void setChildCollections(List<Collection> childCollections) {
        this.childCollections = childCollections;
    }

    public CollectionIterator getIterator() {
        return iterator;
    }

    public void setIterator(CollectionIterator iterator) {
        this.iterator = iterator;
    }
}
