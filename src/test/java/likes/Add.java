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

    private final VkApiClient vk = getVkApiClient();
    private final UserActor publicActor = getPublicDefaultUser();
    private final UserActor privateActor = getPrivateDefaultUser();

    @Test(groups = {"smoke", "positive"}, description = "Добавление лайка на свой пост.")
    public void addLikeForOwnPost() throws ClientException, ApiException {
        PostMessage newPost = new PostMessageProvider().getItemByUser(publicActor);

        int quantityAfterAdd = vk.likes()
                .add(publicActor, newPost.getType(), newPost.getPostId())
                .execute()
                .getLikes();

        assertEquals(quantityAfterAdd, 1, String.format("Количество лайков [%s] не равно ожидаемому результату [%s]", quantityAfterAdd, 1));
    }

    @Test(groups = {"smoke", "negative"}, description = "Добавление лайка на пост приватной страницы.")
    public void addLikeForPrivatePost() throws ClientException, ApiException {
        PostMessage newPost = new PostMessageProvider().getItemByUser(privateActor);
        int errorCode = 0;
        try {
            vk.likes()
                .add(publicActor, newPost.getType(), newPost.getPostId())
                .ownerId(privateActor.getId())
                .execute();
        }
        catch(ApiException ex) {
            errorCode = ex.getCode();
        }
        assertEquals(errorCode, 30, String.format("Значение кода ошибки [%s] не равно ожидаемому [%s]", errorCode, 30));
    }
}
