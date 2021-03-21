package utils;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.httpclient.HttpTransportClient;

public class VkClient {

    private static VkApiClient vk = null;

    private VkClient() {}

    public static VkApiClient getVkApiClient() {
        if (vk == null){
            TransportClient transportClient = HttpTransportClient.getInstance();
            vk = new VkApiClient(transportClient);
        }
        return vk;
    }
}
