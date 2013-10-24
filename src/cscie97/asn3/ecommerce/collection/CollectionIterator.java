package cscie97.asn3.ecommerce.collection;

import java.util.HashSet;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 10/23/13
 * Time: 12:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class CollectionIterator {


    public Set<String> visitedNodeIDs = new HashSet<String>();


    public Collectible next() {
        return null;
    }

    public boolean hasNext() {
        return false;
    }

}
