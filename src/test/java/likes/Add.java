package likes;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import entities.items.PostMessage;
import org.testng.annotations.Test;
import service.PostMessageProvider;

import static org.testng.Assert.assertEquals;
import static utils.UserProvider.getPublicDefaultUser;
import static utils.UserProvider.getPrivateDefaultUser;
import static utils.VkClient.getVkApiClient;

public class Add {

    private static VkApiClient vk = getVkApiClient();
    private static UserActor publicActor = getPublicDefaultUser();
    private static UserActor privateActor = getPrivateDefaultUser();

    @Test(groups = {"smoke", "positive"})
    public void addLikeForOwnPost() throws ClientException, ApiException {
        PostMessage newPost = new PostMessageProvider().getItemByUser(publicActor);

        int quantityAfterAdd = vk.likes()
                .add(publicActor, newPost.getType(), newPost.getPostId())
                .execute()
                .getLikes();

        assertEquals(quantityAfterAdd, 1, String.format("Количество лайков [%s] не равно ожидаемому результату [%s]", quantityAfterAdd, 1));
    }

    @Test(groups = {"smoke", "negative"})
    public void addLikeForPrivatePost() throws ClientException, ApiException {
        PostMessage newPost = new PostMessageProvider().getItemByUser(privateActor);

        int errorCode = 0;
        try { vk.likes()
                .add(publicActor, newPost.getType(), newPost.getPostId())
                .ownerId(privateActor.getId())
                .execute();}
        catch(ApiException ex) {
            errorCode = ex.getCode();
        }
        assertEquals(errorCode, 30, String.format("Значение кода ошибки [%s] не равно ожидаемому [%s]", errorCode, 30));
    }
}
