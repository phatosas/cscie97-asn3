package cscie97.asn3.ecommerce.collection;

import cscie97.asn3.ecommerce.product.Content;
import cscie97.asn3.ecommerce.product.ProductAPI;

/**
 * Wrapper class for {@link cscie97.asn3.ecommerce.product.Content} objects that may be contained in Collections.
 * Since all items that may be part of a {@link cscie97.asn3.ecommerce.collection.Collection} must be able to be
 * easily iterated over, {@link cscie97.asn3.ecommerce.product.Content} items must be wrapped so that they can
 * inherit the same properties and be treated similarly by the
 * {@link cscie97.asn3.ecommerce.collection.CollectionIterator}.
 *
 * @author David Killeffer <rayden7@gmail.com>
 * @version 1.0
 * @see cscie97.asn3.ecommerce.product.Content
 * @see cscie97.asn3.ecommerce.collection.Collection
 * @see cscie97.asn3.ecommerce.collection.Collectible
 * @see cscie97.asn3.ecommerce.collection.DynamicCollection
 * @see cscie97.asn3.ecommerce.collection.StaticCollection
 */
public class ContentProxy extends Collectible {

    private Content contentItem = null;

    public Content getContentItem() {
        return contentItem;
    }

    public void setContentItem(Content item) {
        this.contentItem = item;
    }

    public ContentProxy(String contentId) {
        Content content = ProductAPI.getInstance().getContentByID(contentId);
        if (content != null) {
            this.setContentItem(content);
            this.setId(content.getID());
            this.setName(content.getName());
            this.setDescription(content.getDescription());
        }
    }

    public ContentProxy(Content contentItem) {
        if (contentItem != null) {
            this.setContentItem(contentItem);
            this.setId(contentItem.getID());
            this.setName(contentItem.getName());
            this.setDescription(contentItem.getDescription());
        }
    }

}