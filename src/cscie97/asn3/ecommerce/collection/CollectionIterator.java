package cscie97.asn3.ecommerce.collection;

import java.util.HashSet;
import java.util.Set;
import java.util.Iterator;
import java.util.Stack;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 10/23/13
 * Time: 12:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class CollectionIterator implements Iterator {

    public Set<String> visitedNodeIDs = new HashSet<String>();

    public Stack<Collectible> items = new Stack<Collectible>();

    private Collectible currentItem = null;


    public CollectionIterator() {

    }

    public Collectible next() {
        return null;
    }

    public boolean hasNext() {
        return false;
    }

    public void remove() {

    }

}
