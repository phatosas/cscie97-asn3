package cscie97.asn3.ecommerce.collection;

import cscie97.asn3.ecommerce.product.Content;
import cscie97.asn3.ecommerce.product.ContentSearch;
import cscie97.asn3.ecommerce.product.ProductAPI;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 10/23/13
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
public class DynamicCollection extends Collection {

    private ContentSearch searchCriteria;

    public DynamicCollection() { }

    public DynamicCollection(ContentSearch criteria) {
        this.searchCriteria = criteria;
        executeSearch();
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
        if (this.searchCriteria != null) {
            for (Content content : ProductAPI.getInstance().searchContent(this.searchCriteria)) {
                ContentProxy cp = new ContentProxy(content);
                this.add(cp);
            }
        }
    }

}
