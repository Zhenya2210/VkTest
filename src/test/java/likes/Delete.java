package likes;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import entities.items.PostMessage;
import org.testng.annotations.Test;
import service.PostMessageProvider;

import static org.testng.Assert.assertEquals;
import static utils.UserProvider.getPrivateDefaultUser;
import static utils.UserProvider.getPublicDefaultUser;
import static utils.VkClient.getVkApiClient;

public class Delete {

    private static VkApiClient vk = getVkApiClient();
    private static UserActor publicActor = getPublicDefaultUser();
    private static UserActor privateActor = getPrivateDefaultUser();


    @Test(groups = {"smoke", "positive"})
    public void deleteLikeFromOwnPost() throws ClientException, ApiException {
        PostMessage newPost = new PostMessageProvider().getItemByActor(publicActor);

        vk.likes()
                .add(publicActor, newPost.getType(), newPost.getPostId())
                .execute()
                .getLikes();

        int quantityAfterDelete = vk.likes()
                .delete(publicActor, newPost.getType(), newPost.getPostId())
                .execute()
                .getLikes();

        assertEquals(quantityAfterDelete, 0, String.format("Количество лайков [%s] не равно ожидаемому результату [%s]", quantityAfterDelete, 0));
    }


    @Test(groups = {"smoke", "negative"})
    public void doubleDeleteLikeFromOwnPost() throws ClientException, ApiException {
        PostMessage newPost = new PostMessageProvider().getItemByActor(publicActor);

        vk.likes()
                .add(publicActor, newPost.getType(), newPost.getPostId())
                .execute();

        vk.likes()
                .add(privateActor, newPost.getType(), newPost.getPostId())
                .ownerId(publicActor.getId())
                .execute();

        int quantityAfterFirstDelete = vk.likes()
                .delete(publicActor, newPost.getType(), newPost.getPostId())
                .execute()
                .getLikes();

        int quantityAfterSecondDelete = vk.likes()
                .delete(publicActor, newPost.getType(), newPost.getPostId())
                .execute()
                .getLikes();

        assertEquals(quantityAfterSecondDelete, quantityAfterFirstDelete, String.format("Количество лайков [%s] не равно ожидаемому результату [%s]", quantityAfterSecondDelete, quantityAfterFirstDelete));
    }
}
