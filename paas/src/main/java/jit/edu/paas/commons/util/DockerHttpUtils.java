package jit.edu.paas.commons.util;

import com.spotify.docker.client.messages.Image;
import com.spotify.docker.client.messages.ImageSearchResult;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Docker Remote API
 * v 1.32
 * https://docs.docker.com/engine/api/v1.32/#\
 */
public class DockerHttpUtils {
//    public static String serverUrl = "http://192.168.91.129:2375";

    private static String serverUrl;
    @Value("${docker.server.url}")
    public void setServerUrl(String url) {
        serverUrl = url;
    }

    /*  Image  Start */
    public static  List<ImageSearchResult> searchImages(String term, Integer limit, Boolean isAutomated, Boolean isOfficial, Integer leastStars) {
        // 准备Map
        Map<String,Object> map = new HashMap<>();
        if(isAutomated != null) {
            map.put("is-automated", isAutomated);
        }
        if(isOfficial != null) {
            map.put("is-official", isOfficial);
        }
        if(leastStars != null) {
            map.put("stars", leastStars);
        }
        String jsonStr = JsonUtils.mapToJson(map);

        String url = serverUrl + "/images/search";
        String params = "term=" + term + "&limit=" + limit + "&filters=" +  jsonStr;

        String res = HttpClientUtils.sendGetRequest(url, params);
        List<ImageSearchResult> list = JsonUtils.jsonToList(res,ImageSearchResult.class);

        return list;
    }


    public static List<Image> showImagesList(Boolean showall, String before, Boolean dangling, String lablel, String reference, String since, Boolean showdigests){
        Map<String,Object> map = new HashMap<>();
        if (dangling != null){
            map.put("danglang",dangling);
        }
        String jsonStr = JsonUtils.mapToJson(map);

        String url = serverUrl + "/images/json";
        String params = "all=" + showall + "&filters=" + jsonStr + "&digests=" + showdigests;

        String res = HttpClientUtils.sendGetRequest(url,params);
        List<Image> list = JsonUtils.jsonToList(res,Image.class);
        return  list;
    }

    public static void downloadImage(String imageId){
        String url = serverUrl + "/images/" + imageId + "/get";
        String res = HttpClientUtils.sendGetRequest(url,null);
    }

    /*  Image  Start */
}