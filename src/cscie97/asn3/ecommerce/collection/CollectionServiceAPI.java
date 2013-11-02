package cscie97.asn3.ecommerce.collection;

import cscie97.asn3.ecommerce.collection.Collectible.CollectionIterator;
import cscie97.asn3.ecommerce.product.ContentSearch;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 10/23/13
 * Time: 12:49 AM
 * To change this template use File | Settings | File Templates.
 */
public class CollectionServiceAPI implements ICollectionServiceAPI {

    /**
     * The unique top-level collections contained in the Collection catalog; each collection may only be declared at
     * the top-level once, but may be nested arbitrarily deeply in other collections.
     */
    private Set<Collection> topLevelCollections = new HashSet<Collection>();

    /**
     * Singleton instance of the CollectionServiceAPI
     */
    private static ICollectionServiceAPI instance = null;

    /**
     * Class constructor.  Initially sets all collections to be empty HashSets.
     */
    private CollectionServiceAPI() {
        this.topLevelCollections = new HashSet<Collection>() { };
    }

    /**
     * Returns a reference to the single static instance of the CollectionServiceAPI.
     *
     * @return  singleton instance of CollectionServiceAPI
     */
    public static synchronized ICollectionServiceAPI getInstance() {
        if (instance == null) {
            instance = new CollectionServiceAPI();
        }
        return instance;
    }

    /**
     * Verifies that the <b>guid</b> access token passed is authenticated and authorized for carrying out
     * restricted actions on the CollectionServiceAPI (such as adding new Collections, adding Content to Collections,
     * etc.).
     * <b>Note that for this version of the CollectionServiceAPI, this method is mocked and will return true for
     * any string passed.</b>
     *
     * @param  guid    the string access token to check for authentication and authorization for carrying
     *                 out restricted actions on the CollectionServiceAPI
     * @return         true if guid is authenticated and authorized to execute restricted actions on
     *                 CollectionServiceAPI, false otherwise
     */
    public boolean validateAccessToken(String guid) {
        if (guid != null && guid.length() > 0) {
            return true;
        }
        return false;
    }


    public Collection createCollection(String type, String id) {

        // TODO: ensure that there is no other collection that has the same ID (use collection iterator)

        if (type != null && type.length() > 0) {
            if (type.equalsIgnoreCase("static")) {
                return new StaticCollection();
            } else if (type.equalsIgnoreCase("dynamic")) {
                return new DynamicCollection();
            }
        }
        return null;
    }



    /**
     * Given a collection ID, search for any {@link cscie97.asn3.ecommerce.collection.Collection} that matches that
     * code  in the collection catalog.
     *
     * @param collectionID  a unique collection ID
     * @return              the found {@link cscie97.asn3.ecommerce.collection.Collection} with the matching ID
     */
     public Collection getCollectionByID(String collectionID) {

         // TODO: use collection iterators to iterate over ALL collections and return the one that has the matching ID

         for (Collection collection : this.topLevelCollections) {
             if (collection.getId().equalsIgnoreCase(collectionID)) {
                 return collection;
             }
         }
         return null;
     }


    @Override
    public void addCollection(String guid, Collection collection) {
        if (validateAccessToken(guid)) {
            // ensure the collection is valid and that it doesn't already exist at the top level
            if (collection != null && Collection.validateCollection(collection) && !this.topLevelCollections.contains(collection)) {
                this.topLevelCollections.add(collection);
            }
        }
    }

    /**
     * Adds a Collectible to the collection specified by the <c>collectionId</c>.  Because Collections are trees, will not
     *
     * @param guid
     * @param collectionId
     * @param collectible
     */
    @Override
    public void addContentToCollection(String guid, String collectionId, Collectible collectible) {
        if (validateAccessToken(guid)) {
            Collection foundCollection = this.getCollectionByID(collectionId);
            if (foundCollection != null) {
                List<String> preexistingIDs = new ArrayList<String>();
                CollectionIterator iterator = foundCollection.getIterator();
                while (iterator.hasNext()) {
                    Collectible currentCollectible  = iterator.next();
                    preexistingIDs.add(currentCollectible.getId());
                }


                foundCollection.add(collectible);
            }
        }
    }

    @Override
    public void setDynamicCollectionSearchCriteria(String guid, String collectionId, ContentSearch searchCriteria) {
        if (validateAccessToken(guid)) {
            Collection foundCollection = this.getCollectionByID(collectionId);
            if (foundCollection != null && foundCollection instanceof DynamicCollection) {
                ((DynamicCollection)foundCollection).setSearchCriteria(searchCriteria);
            }
        }
    }

    @Override
    public Set<Collection> searchCollections(String searchCriteria) {
        // construct a virtual "root" collection that has all the "topLevelCollections" as children; then use the
        // iterator for this virtual top-level collection to iterate over all collections and find ones that match
        // name/description to the searchCirteria
        Collection virtual = Collection.createCollection("static");

        virtual.setId("virtualRoot");
        virtual.setName("virtualRoot");
        virtual.setDescription("Virtual root level collection containing all child collections.");

        for (Collectible c : this.topLevelCollections) {
            virtual.add(c);
        }
        CollectionIterator iterator = virtual.getIterator();

        Set<Collection> matchingCollections = new HashSet<Collection>();

        while (iterator.hasNext()) {
            Collectible currentCollectible = iterator.next();

            if (!(currentCollectible instanceof StaticCollection) && !(currentCollectible instanceof DynamicCollection)) {
                continue;
            }

            if ((searchCriteria != null && searchCriteria.length() > 0) && currentCollectible.getName().toLowerCase().contains(searchCriteria.trim().toLowerCase())) {
                matchingCollections.add((Collection)currentCollectible);
            }
            else if (searchCriteria == null || searchCriteria.length() == 0) {
                matchingCollections.add((Collection)currentCollectible);
            }
        }
        return matchingCollections;
    }


    @Override
    public Collectible.CollectionIterator getCollectionIterator(String collectionId) {
        Collection foundCollection = this.getCollectionByID(collectionId);
        if (foundCollection != null) {
            return foundCollection.getIterator();
        }
        else return null;
    }

}