package cscie97.asn3.ecommerce.collection;

import cscie97.asn3.ecommerce.product.Content;
import cscie97.asn3.ecommerce.product.IProductAPI;
import cscie97.asn3.ecommerce.product.ProductAPI;
import org.omg.PortableServer.POAPackage.InvalidPolicy;

/**
 * Created with IntelliJ IDEA.
 * User: dkilleffer
 * Date: 10/23/13
 * Time: 12:32 AM
 * To change this template use File | Settings | File Templates.
 */
public class ContentProxy extends Collectible {

//    public String contentID;
//
//    public String getContentID() {
//        return contentID;
//    }
//
//    public void setContentID(String contentID) {
//        this.contentID = contentID;
//    }

    public Content contentItem = null;

    public Content getContentItem() {
        //return ProductAPI.getInstance().getContentByID(this.contentID);
        //return ProductAPI.getInstance().getContentByID(this.id);
        return contentItem;
    };

    public ContentProxy(String contentId) {
        //this.contentID = contentId;
        //this.id = contentId;
        Content content = ProductAPI.getInstance().getContentByID(contentId);
        if (content != null) {
            this.contentItem = content;
            this.id = content.getID();
            this.name = content.getName();
            this.description = content.getDescription();
        }
    }

}
