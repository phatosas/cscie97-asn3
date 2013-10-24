package cscie97.asn3.ecommerce.collection;

import cscie97.asn3.ecommerce.product.ContentSearch;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 10/23/13
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class DynamicCollection extends Collection {

    public ContentSearch searchCriteria;

    public DynamicCollection  createCollection(String type){
        return new DynamicCollection ();
    }
}
