package home;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

/**
 * Created by max on 09.06.17.
 */
@Component
public class RestHandler {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ItemHandler.class);

    public static final String ITEMS_URL = "http://openhabian.local:8080/rest/items/";

    RestTemplate restTemplate = new RestTemplate();

    public Item callGetItemRequest(String itemName){
        try {
            ResponseEntity<Item> responseEntity = restTemplate.getForEntity(ITEMS_URL + itemName, Item.class);
            Object objects = responseEntity.getBody();
            MediaType contentType = responseEntity.getHeaders().getContentType();
            HttpStatus statusCode = responseEntity.getStatusCode();

            if (statusCode.equals(HttpStatus.OK)) {
                log.info("Loaded item " + ITEMS_URL + itemName);
                return responseEntity.getBody();
            }
        }
        catch (HttpClientErrorException e){

            log.error(e.getMessage());
        }

        return null;
    }

    public boolean itemExists(String itemName){
        if (callGetItemRequest(itemName) == null){
            return false;
        }
        return true;
    }

    public boolean callPutItemRequest(Item item){
        try {
            HttpHeaders headers = new HttpHeaders();

            HttpEntity<Item> requestEntity = new HttpEntity<Item>(item, headers);
            ResponseEntity<Item> response = restTemplate.exchange(item.getLink(), HttpMethod.PUT, requestEntity, Item.class);

            MediaType contentType = response.getHeaders().getContentType();
            HttpStatus statusCode = response.getStatusCode();

            if (statusCode.equals(HttpStatus.OK) || statusCode.equals(201)) {
                log.info("Put item " + item.getName() + " with code " + statusCode);
                return true;
            }
            log.warn("Put item " + item.getName() + " with code " + statusCode);
        }
        catch (HttpClientErrorException e){
            log.error("Failed to put item with error : " + e.getMessage());
        }
        return false;
    }

    public boolean updateItemState(Item item){
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<String> requestEntity = new HttpEntity<String>(item.getState(), headers);
            ResponseEntity<String> response = restTemplate.exchange(item.getLink() + "/state", HttpMethod.PUT, requestEntity, String.class);

            MediaType contentType = response.getHeaders().getContentType();
            HttpStatus statusCode = response.getStatusCode();

            if (statusCode.equals(HttpStatus.OK) || statusCode.equals(HttpStatus.ACCEPTED)) {
                log.info("Update item " + item.getName() + " state with code " + statusCode);
                return true;
            }
        }
        catch (HttpClientErrorException e){
            log.error("Update failed with item " + item.getName() + ", status code: " + e.getStatusCode() + ", error: " + e.getMessage() );
            if (callPutItemRequest(item)){
                log.info("Succesfully created new item " + item.getLink());
                return true;
            }
        }

        return false;
    }

    public Item[] loadItems() {
        ResponseEntity<Item[]> responseEntity = restTemplate.getForEntity(ITEMS_URL, Item[].class);
        Object[] objects = responseEntity.getBody();
        MediaType contentType = responseEntity.getHeaders().getContentType();
        HttpStatus statusCode = responseEntity.getStatusCode();

        Item[] items = null;
        if (statusCode.equals(HttpStatus.OK)) {
            items = responseEntity.getBody();
            log.info("Loaded " + items.length + " temperatureSensors");
        }

        return items;
    }
}
