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








    public class CollectionIterator implements Iterator {

       public List<String> visitedNodeIDs = new ArrayList<String>();

       public Stack<Collectible> itemStack = new Stack<Collectible>();

       private Collectible currentItem = null;

//       public CollectionIterator(List<Collectible> items) {
//           for ( Collectible collectible : items ) {
//               if (collectible instanceof StaticCollection || collectible instanceof DynamicCollection) {
//               }
//           }
//       }

        /*
        // sort all the children items at this level: put the StaticCollection and DynamicCollection objects first
        public List<Collectible> sortCollectiblesPutCollectionsFirst(Collectible c) {
            List<Collectible> sortedChildren = new ArrayList<Collectible>();
            for (Collectible child : c.getChildren()) {
                if (child instanceof StaticCollection || child instanceof DynamicCollection) {
                    sortedChildren.add()
                }
            }
            return
        }
        */

        public Collectible getitem(Collectible c) {
            if (c instanceof StaticCollection || c instanceof DynamicCollection) { }
            return null;
        }

        public CollectionIterator(Collectible top) {
            //for (Collectible collectible : children) {
            //    // first, find the items that are either StaticCollection or DynamicCollection objects, and skip the ContentProxy items for now
            //    if (collectible instanceof StaticCollection || collectible instanceof DynamicCollection) { }
            //}
            digDeeper(top);
        }

        public Collectible next() {
            if (!hasNext()) {
                throw new NoSuchElementException("no more items!");
            }
            Collectible collectible = itemStack.pop();
            currentItem = collectible;
            visitedNodeIDs.add(collectible.getId());

            //for (Collectible child : collectible.getChildren()) {
            //    if (child instanceof StaticCollection || child instanceof DynamicCollection) { }
            //}
            //return null;
            return currentItem;
        }

        private void digDeeper(Collectible item) {
            boolean leafLevel = false;
            for (Collectible child : item.getChildren()) {
                itemStack.push(child);
                if (child instanceof StaticCollection || child instanceof DynamicCollection) {
                    //return
                    digDeeper(child);
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
