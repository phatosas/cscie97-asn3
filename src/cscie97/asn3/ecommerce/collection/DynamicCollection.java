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
        //IProductAPI productAPI = ProductAPI.getInstance();

        executeSearch();
//        List<Content> foundContent = ProductAPI.getInstance().searchContent(this.searchCriteria);
//        for (Content content : foundContent) {
//            ContentProxy cp = new ContentProxy(content);
//            this.children.add(cp);
//        }

        //this.children = ProductAPI.getInstance().searchContent(this.searchCriteria);
    }

    public ContentSearch getSearchCriteria() {
        return searchCriteria;
    }

    public void setSearchCriteria(ContentSearch searchCriteria) {
        this.searchCriteria = searchCriteria;
        // in case the collection does not yet have any children, try to execute the search and find some content
        executeSearch();
    }

    public void executeSearch() {
        //foundContent = ProductAPI.getInstance().searchContent(this.searchCriteria);
        if (this.searchCriteria != null) {
            for (Content content : ProductAPI.getInstance().searchContent(this.searchCriteria)) {
                ContentProxy cp = new ContentProxy(content);
                this.children.add(cp);
            }
        }
    }


}
