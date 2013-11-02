package cscie97.asn3.ecommerce.collection;

import cscie97.asn3.ecommerce.product.Content;
import cscie97.asn3.ecommerce.product.ProductAPI;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 10/23/13
 * Time: 12:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class ContentProxy extends Collectible {

    private Content contentItem = null;

    public Content getContentItem() {
        return contentItem;
    };

    public ContentProxy(String contentId) {
        Content content = ProductAPI.getInstance().getContentByID(contentId);
        if (content != null) {
            this.contentItem = content;
            this.setId(content.getID());
            this.setName(content.getName());
            this.setDescription(content.getDescription());
        }
    }

    public ContentProxy(Content contentItem) {
        if (contentItem != null) {
            this.contentItem = contentItem;
            this.setId(contentItem.getID());
            this.setName(contentItem.getName());
            this.setDescription(contentItem.getDescription());
        }
    }

}