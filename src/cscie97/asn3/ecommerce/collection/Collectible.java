package cscie97.asn3.ecommerce.collection;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 10/23/13
 * Time: 12:36 AM
 * To change this template use File | Settings | File Templates.
 */
//public abstract class Collectible implements Iterator {
public abstract class Collectible {

    public String id;

    public String name;

    public String description;

    public List<Collectible> children = new ArrayList<Collectible>();

    public CollectionIterator iterator = null;

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

    /*
    public void remove(Collectible collectible) {
        children.remove(collectible);
    }
    public void remove() {
        //children.remove(collectible);
    }

    public Collectible next() {
        return null;
    }

    public boolean hasNext() {
        return false;
    }

    //public void remove() { }

    */


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

        private Collectible root = null;

        private Collectible currentItem = null;

        public CollectionIterator(Collectible top) {
            root = top;
            itemStack.push(top);
            buildItemStack(top);
        }

        public Collectible next() {
            if (!hasNext()) {
                throw new NoSuchElementException("no more items!");
            }
            Collectible collectible = itemStack.pop();
            currentItem = collectible;
            return currentItem;
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
