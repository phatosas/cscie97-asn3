package cscie97.asn3.ecommerce.csv;

import cscie97.asn3.ecommerce.exception.CollectionNotFoundException;
import cscie97.asn3.ecommerce.exception.ContentNotFoundException;
import cscie97.asn3.ecommerce.exception.ImportException;
import cscie97.asn3.ecommerce.exception.ParseException;
import cscie97.asn3.ecommerce.product.*;
import cscie97.asn3.ecommerce.collection.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Set;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.ArrayList;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.StringUtils;

/**
 * Provides public static methods for supplying CSV files to load {@link cscie97.asn3.ecommerce.product.Country},
 * {@link cscie97.asn3.ecommerce.product.Device}, and
 * {@link cscie97.asn3.ecommerce.product.Content} items (which may be one of
 * {@link cscie97.asn3.ecommerce.product.Application}, {@link cscie97.asn3.ecommerce.product.Ringtone}, or
 * {@link cscie97.asn3.ecommerce.product.Wallpaper} depending on the content type) into the Product catalog.  The
 * input CSV files passed may contain a header line or comment lines that begin with a "#" symbol; those lines will
 * be skipped for analysis.
 *
 * For importing {@link cscie97.asn3.ecommerce.product.Country} items, the input file must be in the following
 * column format:
 * <ol>
 *     <li>2-character country code</li>
 *     <li>country name</li>
 *     <li>country export status (open or closed)</li>
 * </ol>
 * For example, here is a sample of what a valid country CSV input file might look like (country names that have
 * commas in them may be escaped by putting a backslash in front of the comma):
 * <pre>
 * #country_id, country_name, country_export_status
 * AF,AFGHANISTAN,open
 * AX,ALAND ISLANDS,open
 * AL,ALBANIA,open
 * BO,BOLIVIA\, PLURINATIONAL STATE OF,open
 * BQ,BONAIRE\, SINT EUSTATIUS AND SABA,open
 *  ...
 * </pre>
 *
 * For importing {@link cscie97.asn3.ecommerce.product.Device} items, the input file must be in the following
 * column format:
 * <ol>
 *     <li>unique device ID</li>
 *     <li>device name</li>
 *     <li>device manufacturer name</li>
 * </ol>
 * For example, here is a sample of what a valid device CSV input file might look like (device names that have
 * commas in them may be escaped by putting a backslash in front of the comma):
 * <pre>
 * # device_id, device_name, manufacturer
 * iphone5, IPhone 5, Apple
 * iphone6, IPhone 6, Apple
 * lumina800, Lumina 800, Nokia
 * lumina900, Lumina 900, Nokia
 * ...
 * </pre>
 *
 * For importing {@link cscie97.asn3.ecommerce.product.Content} items (regardless of type), the input file must be
 * in the following column format:
 * <ol>
 *     <li>content type (can be one of "application", "ringtone", or "wallpaper" currently)</li>
 *     <li>ID</li>
 *     <li>content name</li>
 *     <li>description</li>
 *     <li>author name</li>
 *     <li>content rating (0 to 5, where 5 is best)</li>
 *     <li>content categories (pipe-separated)</li>
 *     <li>list of 2-character country codes where the content may be legally downloaded to (pipe-separated)</li>
 *     <li>list of supported device IDs (pipe-separated)</li>
 *     <li>price (as a float, in BitCoins)</li>
 *     <li>list of supported language codes (pipe-separated)</li>
 *     <li>image URL to screenshot, box art, etc.</li>
 *     <li>application filesize in bytes [OPTIONAL, only for {@link cscie97.asn3.ecommerce.product.Application} type content)</li>
 *     <li>ringtone duration in seconds (as a float) [OPTIONAL, only for {@link cscie97.asn3.ecommerce.product.Ringtone} type content)</li>
 *     <li>wallpaper pixel width (integer) [OPTIONAL, only for {@link cscie97.asn3.ecommerce.product.Wallpaper} type content)</li>
 *     <li>wallpaper pixel height (integer) [OPTIONAL, only for {@link cscie97.asn3.ecommerce.product.Wallpaper} type content)</li>
 * </ol>
 * For example, here is a sample of what a valid content CSV input file might look like (device names that have
 * commas in them may be escaped by putting a backslash in front of the comma):
 * <pre>
 * #content_type, content_id, content_name, content_description, author, rating, categories, export_countries,supported_devices,price, supported_languages, image_url, application_size
 * application, a1, Angry Birds Seasons, Angry Birds Seasons, Rovio, 5, game|kids,US|CA|MX,iphone_5|iphone_6|lumina_800,1.5,en_us|en_ca|en_gb, http://web-assets.angrybirds.com/abcom/img/games/223/Icon_download_seasons_223x223.png,564
 * ringtone, rt1, Ferrari 575M Maranello - 2002,This ringtone is a unique and exclusive recording of the 2002 Ferrari 575M Maranello being driven to its limits!,Nutbags,4,car|racing,US|CA|MX|AF|AL,lumina_800,0.0,en_us|en_ca|en_gb,http://p.d.ovi.com/p/g/store/1492679/_02_Ferrari_575M_Maranello-192x192.png
 * ...
 * </pre>
 *
 * @author David Killeffer <rayden7@gmail.com>
 * @version 1.0
 * @see cscie97.asn3.ecommerce.collection.ICollectionServiceAPI
 * @see cscie97.asn3.ecommerce.collection.CollectionServiceAPI
 * @see cscie97.asn3.ecommerce.collection.Collectible
 * @see cscie97.asn3.ecommerce.collection.Collection
 * @see cscie97.asn3.ecommerce.collection.CollectionIterator
 * @see cscie97.asn3.ecommerce.collection.ContentProxy
 * @see cscie97.asn3.ecommerce.collection.StaticCollection
 * @see cscie97.asn3.ecommerce.collection.DynamicCollection
 */
public class CollectionImporter extends Importer {

    /*
    # define_collection, <collection_type>, <collection_id>, <collection_name>, <collection_description>
    define_collection, static, sports_collection, sports, cool sports apps
    define_collection, dynamic, news_apps, News Apps, All about News
    */
    private static void defineCollection(String guid, String[] collectionData) throws ParseException {

        // ensure that we have at least 5 elements passed and that the first element is "define_collection"
        if (collectionData == null || collectionData.length != 5 || !collectionData[0].trim().equalsIgnoreCase("define_collection")) {
            throw new ParseException("Import Collections line contains invalid data when calling definedCollections(): "+StringUtils.join(collectionData,","),
                    null,
                    0,
                    null,
                    null);
        }

        Collection collection = Collection.createCollection(collectionData[1].trim());
        collection.setId(collectionData[2].trim());
        collection.setName(collectionData[3].trim());
        collection.setDescription(collectionData[4].trim());

        ICollectionServiceAPI collectionAPI = CollectionServiceAPI.getInstance();
        collectionAPI.addCollection(guid, collection);
    }


    /*
    # add_collection_content, <collection_id>, <content_type>, <content_id>
     add_collection_content, sports_collection, collection, cricket_collection
     add_collection_content, sports_collection, product, Yahoo!_Sports
     add_collection_content, sports_collection, product, Score_Center
     add_collection_content, sports_collection, collection, cricket_collection
    */
    private static void addContentToCollection(String guid, String[] collectionData)
            throws ParseException, CollectionNotFoundException, ContentNotFoundException
    {
        // ensure that we have at least 4 elements passed and that the first element is "add_collection_content"
        if (collectionData == null || collectionData.length != 4 || !collectionData[0].trim().equalsIgnoreCase("add_collection_content")) {
            throw new ParseException("Import Collections line contains invalid data when calling addContentToCollection(): "+StringUtils.join(collectionData,","),
                    null,
                    0,
                    null,
                    null);
        }

        // first, we need to find the current collection based on the collectionID in the collection catalog
        ICollectionServiceAPI collectionAPI = CollectionServiceAPI.getInstance();
        Collection foundCollection = collectionAPI.getCollectionByID(collectionData[1].trim());
        if (foundCollection == null) {
            throw new CollectionNotFoundException("Add Content to Collection could not find the collection to add content to with ID:"+collectionData[1].trim()+" - arguments passed to addContentToCollection(): "+StringUtils.join(collectionData,","), 0, null, null);
        }

        // ensure that the type we're adding to the collection is either "collection" or "product"
        if (collectionData[2] == null ||
            collectionData[2].trim().length() == 0 ||
            (!collectionData[2].trim().equalsIgnoreCase("collection") && !collectionData[2].trim().equalsIgnoreCase("product"))
        ) {
            throw new ContentNotFoundException("Add Content to Collection encountered an illegal value for type to add:"+collectionData[2].trim()+" - "+StringUtils.join(collectionData,","), 0, null, null);
        }
        // and also ensure that the content ID is not null and at least 1 character in length
        if ( collectionData[3] == null || !(collectionData[3].trim().length() >= 1) ) {
            throw new ContentNotFoundException("Add Content to Collection could not find the content item to add with ID: ["+collectionData[3].trim()+"] - "+StringUtils.join(collectionData,","), 0, null, null);
        }


        Collectible thingToAdd = null;

        // add a child collection to a parent collection
        if (collectionData[2].trim().equalsIgnoreCase("collection")) {
            // find the collection to add
            thingToAdd = collectionAPI.getCollectionByID(collectionData[3].trim());
            if (thingToAdd == null) {
                throw new CollectionNotFoundException("Add Content to Collection could not find the collection attempting to be added [ID: "+collectionData[3].trim()+"] to existing collection [ID:"+collectionData[1].trim()+"] - arguments passed to addContentToCollection(): "+StringUtils.join(collectionData,","), 0, null, null);
            }
        }
        // add a child Content item to a parent collection
        else if (collectionData[2].trim().equalsIgnoreCase("product")) {
            // find the Content item to add, and if found, create a new ContentProxy object for the actual adding to the Collection
            Content foundContent = ProductAPI.getInstance().getContentByID(collectionData[3].trim());
            if (foundContent == null) {
                throw new ContentNotFoundException("Add Content to Collection could not find the content item attempting to be added [ID: "+collectionData[3].trim()+"] to existing collection [ID:"+collectionData[1].trim()+"] - arguments passed to addContentToCollection(): "+StringUtils.join(collectionData,","), 0, null, null);
            }
            thingToAdd = new ContentProxy(foundContent.getID());
        }

        collectionAPI.addContentToCollection(guid, foundCollection.getId(), thingToAdd);
    }


    /*
    # set_dynamic_criteria, <collection_id>, <category list>, <text search>, <minimum rating>, <max price>, <language list>, <country code>, <device id>, <content type list>
    set_dynamic_criteria, cricket_collection, , cricket, , , , , ,
    set_dynamic_criteria, news_apps, news, , ,,, , ,
    */
    private static void setDynamicCriteria(String guid, String[] collectionData) throws ParseException, CollectionNotFoundException {

        // ensure that we have at least 10 elements passed and that the first element is "set_dynamic_criteria"
        if (collectionData == null || collectionData.length != 10 || !collectionData[0].trim().equalsIgnoreCase("set_dynamic_criteria")) {
            throw new ParseException("Import Collections line contains invalid data when calling setDynamicCriteria(): "+StringUtils.join(collectionData,","),
                    null,
                    0,
                    null,
                    null);
        }

        // first, we need to find the current collection based on the collectionID in the collection catalog
        ICollectionServiceAPI collectionAPI = CollectionServiceAPI.getInstance();
        Collection foundCollection = collectionAPI.getCollectionByID(collectionData[1].trim());
        if (foundCollection == null) {
            throw new CollectionNotFoundException("Set Dynamic Criteria for Collection could not find the collection to set the dynamic content search criteria for with ID:"+collectionData[1].trim()+" - arguments passed to addContentToCollection(): "+StringUtils.join(collectionData,","), 0, null, null);
        }

        // ensure that we can construct our ContentSearch object based on the remaining parameters
        ContentSearch searchCriteria = null;

        // construct a string that contains ONLY the search criteria portion of the passed collectionData, and make
        // sure to separate each element with a comma and a space character for correct parsing by SearchEngine
        String csvSearchCriteria = StringUtils.join(Arrays.copyOfRange(collectionData, 2, collectionData.length), ", ");

        searchCriteria = SearchEngine.getContentSearchForCSV(csvSearchCriteria);

        CollectionServiceAPI.getInstance().setDynamicCollectionSearchCriteria(guid, foundCollection.getId(), searchCriteria);
    }


    /*
    # search collection
    search_collection, neWs
    # search for all collections with empty search string
    search_collection,
    */
    private static void searchCollections(String searchText) throws ParseException {

        System.out.println("SEARCH COLLECTIONS: " + searchText );
    }



    public static void importCollectionsFile(String guid, String filename) throws ImportException, ParseException, CollectionNotFoundException {
        int lineNumber = 0;  // keep track of what lineNumber we're reading in from the input file for exception handling
        String line = null;  // store the text on each line as it's processed

        // reference to CollectionServiceAPI for adding collections, adding content items, etc.
        ICollectionServiceAPI collectionsAPI = CollectionServiceAPI.getInstance();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            List<Content> contentItemsToAdd = new ArrayList<Content>();

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // FIRST check if we encountered an empty line, and just skip to the next one if so
                if (line.length() == 0) { continue; }

                // SECOND check if the line contains column headers, since some lines may contain comments
                // (preceeded by hash character); if first character is a hash, skip to next line
                if (line.substring(0,1).matches("#")) { continue; }

                //String[] cleanedColumns = Importer.parseCSVLine(line, ",");
                String[] cleanedColumns = CollectionImporter.parseCSVLine(line, ",");

                // depending on both the size of cleanedColumns as well as the first item in the array,
                // call the appropriate method to handle the command


                // set dynamic collection search criteria
                if (cleanedColumns != null ) {
                    try {
                        // define collections
                        if (cleanedColumns.length == 5 && cleanedColumns[0].equalsIgnoreCase("define_collection")) {
                            CollectionImporter.defineCollection(guid, cleanedColumns);
                        }

                        // add content to collections
                        if (cleanedColumns.length == 4 && cleanedColumns[0].equalsIgnoreCase("add_collection_content")) {
                            CollectionImporter.addContentToCollection(guid, cleanedColumns);
                        }

                        // set dynamic collection content search crtieria
                        if (cleanedColumns.length == 10 && cleanedColumns[0].equalsIgnoreCase("set_dynamic_criteria")) {
                            CollectionImporter.setDynamicCriteria(guid, cleanedColumns);
                        }

                        // search collections
                        if (cleanedColumns.length == 2 && cleanedColumns[0].equalsIgnoreCase("search_collection")) {
                            CollectionImporter.searchCollections(cleanedColumns[1].toLowerCase());
                        }
                    }
                    catch (ParseException pe) {
                        throw new ParseException(pe.getMessage(), line, lineNumber, filename, pe);
                    }
                }
                else {
                    throw new ParseException("Import Collections line contains invalid data for the collection data row.",
                                                line,
                                                lineNumber,
                                                filename,
                                                null);
                }


                /*
                // depending on what info was supplied, the cleaned columns can be 12 to 16 columns in size,
                // depending on content attribute supplied
                if (cleanedColumns != null && cleanedColumns.length >= 12 && cleanedColumns.length <= 16) {
                    // set up empty values for the content that will be parsed out from the line
                    String contentID = "";
                    String contentName = "";
                    String contentDescription = "";
                    String contentAuthorName = "";
                    String contentImageURL = "";
                    int contentRating = 0;
                    int contentFilesizeBytes = 0;
                    int contentPixelWidth = 0;
                    int contentPixelHeight = 0;
                    float contentPrice = 0;
                    float contentDurationInSeconds = 0;
                    Set<String> contentCategories = new HashSet<String>(){};
                    Set<Device> contentDevices = new HashSet<Device>(){};
                    Set<Country> contentCountries = new HashSet<Country>(){};
                    Set<String> contentSupportedLanguages = new HashSet<String>(){};
                    ContentType contentType = null;

                    List<ContentType> allContentTypes = Arrays.asList(ContentType.values());
                    String upperCaseContentType = cleanedColumns[0].toUpperCase();

                    // get the content type
                    if (cleanedColumns[0] != null && cleanedColumns[0].length() > 0 && allContentTypes.contains(ContentType.valueOf(upperCaseContentType)) ) {
                        contentType = ContentType.valueOf(cleanedColumns[0].trim().toUpperCase());
                    }
                    // get the content ID
                    if (cleanedColumns[1] != null && cleanedColumns[1].length() > 0) {
                        contentID = cleanedColumns[1].trim();
                    }
                    // get the content name
                    if (cleanedColumns[2] != null && cleanedColumns[2].length() > 0) {
                        contentName = cleanedColumns[2].trim();
                    }
                    // get the content description
                    if (cleanedColumns[3] != null && cleanedColumns[3].length() > 0) {
                        contentDescription = cleanedColumns[3].trim();
                    }
                    // get the content author name
                    if (cleanedColumns[4] != null && cleanedColumns[4].length() > 0) {
                        contentAuthorName = cleanedColumns[4].trim();
                    }
                    // get the content rating
                    if (cleanedColumns[5] != null && cleanedColumns[5].length() == 1) {
                        try {
                            contentRating = Integer.parseInt(cleanedColumns[5].trim());
                        }
                        catch (NumberFormatException nfe) {
                            throw new ParseException("Import Content line contains invalid data for the content rating ["+cleanedColumns[5].toString()+"].",
                                                        line,
                                                        lineNumber,
                                                        filename,
                                                        nfe);
                        }
                    }
                    // get the content categories
                    if (cleanedColumns[6] != null && cleanedColumns[6].length() > 0) {
                        // need to parse out the categories by splitting on the pipe character
                        String[] parsedCategories = Importer.parseCSVLine(cleanedColumns[6], "\\|");
                        // remove any leading or trailing whitespace from category names
                        for (int i=0; i<parsedCategories.length; i++) { parsedCategories[i] = parsedCategories[i].trim(); }
                        if (parsedCategories != null && parsedCategories.length > 0) {
                            contentCategories.addAll(Arrays.asList(parsedCategories));
                        }
                    }
                    // get the content countries
                    if (cleanedColumns[7] != null && cleanedColumns[7].length() > 0) {
                        // need to parse out the countries by splitting on the pipe character
                        String[] parsedCountries = Importer.parseCSVLine(cleanedColumns[7], "\\|");
                        if (parsedCountries != null && parsedCountries.length > 0) {
                            for (String countryCode : parsedCountries) {
                                Country foundCountry = productAPI.getCountryByCode(countryCode.trim());
                                if (foundCountry != null) {
                                    contentCountries.add(foundCountry);
                                }
                            }
                        }
                    }
                    // get the content supported devices
                    if (cleanedColumns[8] != null && cleanedColumns[8].length() > 0) {
                        // need to parse out the devices by splitting on the pipe character
                        String[] parsedDevices = Importer.parseCSVLine(cleanedColumns[8], "\\|");
                        if (parsedDevices != null && parsedDevices.length > 0) {
                            for (String deviceID : parsedDevices) {
                                Device foundDevice = productAPI.getDeviceByID(deviceID.trim());
                                if (foundDevice != null) {
                                    contentDevices.add(foundDevice);
                                }
                            }
                        }
                    }
                    // get the content price (in BitCoins)
                    if (cleanedColumns[9] != null && cleanedColumns[9].length() > 0) {
                        try {
                            contentPrice = Float.parseFloat(cleanedColumns[9].trim());
                        }
                        catch (NumberFormatException nfe) {
                            throw new ParseException("Import Content line contains invalid data for the content price ["+cleanedColumns[9].toString()+"].",
                                                        line,
                                                        lineNumber,
                                                        filename,
                                                        nfe);
                        }
                    }
                    // get the content supported languages
                    if (cleanedColumns[10] != null && cleanedColumns[10].length() > 0) {
                        // need to parse out the supported languages by splitting on the pipe character
                        String[] parsedLanguages = Importer.parseCSVLine(cleanedColumns[10], "\\|");
                        // remove any leading or trailing whitespace from supported language names
                        for (int i=0; i<parsedLanguages.length; i++) { parsedLanguages[i] = parsedLanguages[i].trim(); }
                        if (parsedLanguages != null && parsedLanguages.length > 0) {
                            contentSupportedLanguages.addAll(Arrays.asList(parsedLanguages));
                        }
                    }
                    // get the content image URL
                    if (cleanedColumns[11] != null && cleanedColumns[11].length() > 0) {
                        contentImageURL = cleanedColumns[11].trim();
                    }
                    // OPTIONAL: if there is a 13th item in the array, it is the application file size
                    if (cleanedColumns.length >= 13 && cleanedColumns[12] != null && cleanedColumns[12].trim().length() > 0) {
                        try {
                            contentFilesizeBytes = Integer.parseInt(cleanedColumns[12].trim());
                        }
                        catch (NumberFormatException nfe) {
                            throw new ParseException("Import Content line contains invalid data for the content application filesize ["+cleanedColumns[12].toString()+"].",
                                                        line,
                                                        lineNumber,
                                                        filename,
                                                        nfe);
                        }
                    }
                    // OPTIONAL: if there is a 14th item in the array, it is the ringtone duration in seconds
                    if (cleanedColumns.length >= 14 && cleanedColumns[13] != null && cleanedColumns[13].trim().length() > 0) {
                        try {
                            contentDurationInSeconds = Float.parseFloat(cleanedColumns[13].trim());
                        }
                        catch (NumberFormatException nfe) {
                            throw new ParseException("Import Content line contains invalid data for the content ringtone duration in seconds ["+cleanedColumns[13].toString()+"].",
                                                        line,
                                                        lineNumber,
                                                        filename,
                                                        nfe);
                        }
                    }
                    // OPTIONAL: if there are 15th and 16th columns in the array, it is the wallpaper pixel width and pixel height
                    if (cleanedColumns.length >= 16 &&
                            cleanedColumns[14] != null &&
                            cleanedColumns[15] != null &&
                            cleanedColumns[14].trim().length() > 0 &&
                            cleanedColumns[15].trim().length() > 0
                    ) {
                        try {
                            contentPixelWidth = Integer.parseInt(cleanedColumns[14].trim());
                            contentPixelHeight = Integer.parseInt(cleanedColumns[15].trim());
                        }
                        catch (NumberFormatException nfe) {
                            throw new ParseException("Import Content line contains invalid data for the content wallpaper pixel width and height ["+cleanedColumns[14].toString()+","+cleanedColumns[15].toString()+"].",
                                                        line,
                                                        lineNumber,
                                                        filename,
                                                        nfe);
                        }
                    }

                    // try to create the content
                    if (contentType == null) {
                        throw new ParseException("Import Content line contains invalid data for the content type ["+cleanedColumns[0].toString()+"].",
                                                    line,
                                                    lineNumber,
                                                    filename,
                                                    null);
                    }

                    // call the appropriate content type class constructor based on the parsed values from the CSV line
                    switch (contentType) {
                        case APPLICATION :
                            Application application = new Application(contentID, contentName, contentDescription,
                                                                  contentAuthorName, contentRating, contentCategories,
                                                                  contentDevices, contentPrice, contentCountries,
                                                                  contentSupportedLanguages, contentImageURL,
                                                                  contentType, contentFilesizeBytes);
                            if (Application.validateContent(application)) {
                                contentItemsToAdd.add(application);
                            } else {
                                throw new ParseException("Import Content line contains invalid data for some of the application content attributes.",
                                                            line,
                                                            lineNumber,
                                                            filename,
                                                            null);
                            }
                            break;
                        case RINGTONE :
                            Ringtone ringtone = new Ringtone(contentID, contentName, contentDescription, contentAuthorName,
                                                            contentRating, contentCategories, contentDevices,
                                                            contentPrice, contentCountries, contentSupportedLanguages,
                                                            contentImageURL, contentType, contentDurationInSeconds);
                            if (Ringtone.validateContent(ringtone)) {
                                contentItemsToAdd.add(ringtone);
                            } else {
                                throw new ParseException("Import Content line contains invalid data for some of the ringtone content attributes.",
                                                            line,
                                                            lineNumber,
                                                            filename,
                                                            null);
                            }
                            //contentItemsToAdd.add(ringtone);
                            break;
                        case WALLPAPER :
                            Wallpaper wallpaper = new Wallpaper(contentID, contentName, contentDescription, contentAuthorName,
                                                              contentRating, contentCategories, contentDevices,
                                                              contentPrice, contentCountries, contentSupportedLanguages,
                                                              contentImageURL, contentType, contentPixelWidth, contentPixelHeight);
                            if (Wallpaper.validateContent(wallpaper)) {
                                contentItemsToAdd.add(wallpaper);
                            } else {
                                throw new ParseException("Import Content line contains invalid data for some of the wallpaper content attributes.",
                                                            line,
                                                            lineNumber,
                                                            filename,
                                                            null);
                            }
                            //contentItemsToAdd.add(wallpaper);
                            break;
                    }
                } else {
                    throw new ParseException("Import Content line contains invalid data for some of the content attributes.",
                                                line,
                                                lineNumber,
                                                filename,
                                                null);
                }
                */


            }

            // add the content items to the Product catalog
            //if (contentItemsToAdd.size() > 0) {
            //    //collectionsAPI.importContent(guid, contentItemsToAdd);
            //}

        }
        catch (FileNotFoundException fnfe) {
            throw new ImportException("Could not find file ["+filename+"] to open for reading", lineNumber, filename, fnfe);
        }
        catch (IOException ioe) {
            throw new ImportException("Encountered an IOException when trying to open ["+filename+"] for reading", lineNumber, filename, ioe);
        }
        catch (Exception e) {
            throw new ImportException("Caught a generic Exception when attempting to read file ["+filename+"]", lineNumber, filename, e);
        }
    }

}