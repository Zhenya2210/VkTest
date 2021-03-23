package service;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.wall.responses.PostResponse;
import entities.items.PostMessage;

import static utils.VkClient.getVkApiClient;

public class PostMessageProvider implements ItemProvider {

    private final VkApiClient vk = getVkApiClient();

    @Override
    public PostMessage getItemByUser(UserActor creator) throws ClientException, ApiException {
        PostResponse postResponse = vk.wall()
                .post(creator)
                .message("Hello world")
                .execute();
        return new PostMessage(creator.getId(), postResponse.getPostId());
    }
}
