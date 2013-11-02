package cscie97.asn3.ecommerce.collection;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 10/23/13
 * Time: 12:19 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Collection extends Collectible {

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

}
