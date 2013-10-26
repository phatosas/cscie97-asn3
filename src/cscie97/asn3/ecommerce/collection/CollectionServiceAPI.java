package cscie97.asn3.ecommerce.collection;

import cscie97.asn3.ecommerce.product.*;

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

    @Override
    public void addContentToCollection(String guid, String collectionId, Collectible collectible) {
        if (validateAccessToken(guid)) {

            // TODO: check for cycles before adding the content to the collection, and disallow creating cycles

            Collection foundCollection = this.getCollectionByID(collectionId);
            if (foundCollection != null) {
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
    public List<Collection> searchCollections(String searchCriteria) {
        List<Collection> matchingCollections = new ArrayList<Collection>();

        // TODO: use collection iterators to loop over all collections and find matching ones based on the passed search criteria

        return matchingCollections;
    }


    @Override
    public CollectionIterator getCollectionIterator(String collectionId) {
        Collection foundCollection = this.getCollectionByID(collectionId);
        if (foundCollection != null) {
            return foundCollection.getIterator();
        }
        else return null;
    }

}