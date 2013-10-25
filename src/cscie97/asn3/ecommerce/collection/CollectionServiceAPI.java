package cscie97.asn3.ecommerce.collection;

import cscie97.asn3.ecommerce.product.Content;
import cscie97.asn3.ecommerce.product.Country;
import cscie97.asn3.ecommerce.product.Device;
import cscie97.asn3.ecommerce.product.IProductAPI;
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



    @Override
    public void addCollection(String guid, Collection collection) {
        // ensure that the collection doesn't already exist
        if (validateAccessToken(guid)) {
            if (collection != null && Collection.validateCollection(collection)) {
                this.topLevelCollections.add(collection);
            }
        }
    }

    @Override
    public void addContentToCollection(String guid, String collectionId, Collectible collectible) {

        ////To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List<Collection> searchCollections(String searchCriteria) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public CollectionIterator getCollectionIterator(String collectionId) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


}
