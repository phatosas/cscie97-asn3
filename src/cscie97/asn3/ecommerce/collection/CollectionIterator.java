package cscie97.asn3.ecommerce.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Stack;

/**
 * Allows for the iteration of Collectible items.  {@link cscie97.asn3.ecommerce.collection.Collectible}s follow a
 * two-part Composite design pattern; at the lowest level,
 *
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 11/3/13
 * Time: 2:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class CollectionIterator implements Iterator {

    public Stack<Collectible> itemStack = new Stack<Collectible>();

    private Collectible current = null;

    public CollectionIterator(Collectible top) {
        this.itemStack.push(top);
        buildItemStack(top);
    }

    public Collectible getCurrent() {
        return current;
    }

    public Collectible next() {
        if (!hasNext()) {
            throw new NoSuchElementException("no more items!");
        }
        Collectible collectible = itemStack.pop();
        current = collectible;
        return current;
    }

    private void buildItemStack(Collectible item) {
        for (Collectible curChild : item.getChildren()) {
            itemStack.push(curChild);
            if (curChild instanceof StaticCollection || curChild instanceof DynamicCollection) {
                buildItemStack(curChild);
            }
        }
    }

    public boolean hasNext() {
        return !this.itemStack.empty();
    }

    public void remove() {
        throw new UnsupportedOperationException();
    }
}