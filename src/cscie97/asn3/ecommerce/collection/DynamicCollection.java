package cscie97.asn3.ecommerce.collection;

import cscie97.asn3.ecommerce.product.ContentSearch;
import cscie97.asn3.ecommerce.product.Content;
import cscie97.asn3.ecommerce.product.IProductAPI;
import cscie97.asn3.ecommerce.product.ProductAPI;

import java.util.List;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 10/23/13
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class DynamicCollection extends Collection {

    public ContentSearch searchCriteria;

//    public DynamicCollection  createCollection(String type){
//        return new DynamicCollection ();
//    }

    public DynamicCollection() { }

    public DynamicCollection(ContentSearch criteria) {
        this.searchCriteria = criteria;
    }

    public ContentSearch getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(ContentSearch searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public List<Content> foundContent = new ArrayList<Content>();

    public void executeSearch() {
        foundContent = ProductAPI.getInstance().searchContent(this.searchCriteria);



    }


}
