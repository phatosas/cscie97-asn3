package cscie97.asn3.ecommerce.collection;

import cscie97.asn3.ecommerce.product.*;
import cscie97.asn3.ecommerce.exception.*;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 10/23/13
 * Time: 12:18 AM
 * To change this template use File | Settings | File Templates.
 */
public interface ICollectionServiceAPI {

    public void addCollection(String guid, Collection collection);

    public void addContentToCollection(String guid, String collectionId, Collectible collectible);
    //public void addContentToCollection(String guid, String collectionId, ContentProxy contentProxy);
    //public void addContentToCollection(String guid, String collectionId, Collection childCollection);


    public void setDynamicCollectionSearchCriteria(String guid, String collectionId, ContentSearch searchCriteria);

    public List<Collection> searchCollections(String searchCriteria);

    public CollectionIterator getCollectionIterator(String collectionId);

    public boolean validateAccessToken(String guid);

    /**
     * Given a collection ID, search for any {@link cscie97.asn3.ecommerce.collection.Collection} that matches that
     * code  in the collection catalog.
     *
     * @param collectionID  a unique collection ID
     * @return              the found {@link cscie97.asn3.ecommerce.collection.Collection} with the matching ID
     */
     public Collection getCollectionByID(String collectionID);

}
