package cscie97.asn3.ecommerce.collection;

import java.util.List;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.Iterator;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 10/23/13
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Collectible {

    private String id;

    private String name;

    private String description;

    private List<Collectible> children = new ArrayList<Collectible>();

    private CollectionIterator iterator = null;

    public CollectionIterator getIterator() {
        if (iterator == null) {
            CollectionIterator ci = new CollectionIterator(this);
            this.iterator = ci;
        }
        return this.iterator;
    }

    public List<Collectible> getChildren() {
        return this.children;
    }

    public void add(Collectible collectible) {
        this.iterator = null;  // since we're modifying the collection, ensure that the next time the iterator is referenced it is re-created
        children.add(collectible);
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

    @Override
    public String toString() {
        return this.id;
    }




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

}
