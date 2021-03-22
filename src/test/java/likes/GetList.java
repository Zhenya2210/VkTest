package likes;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.enums.LikesFilter;
import entities.items.PostMessage;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import service.PostMessageProvider;


import static org.testng.Assert.assertEquals;
import static utils.UserProvider.getPrivateDefaultUser;
import static utils.UserProvider.getPublicDefaultUser;
import static utils.VkClient.getVkApiClient;


public class GetList {

    private static VkApiClient vk = getVkApiClient();
    private static UserActor publicActor = getPublicDefaultUser();
    private static UserActor privateActor = getPrivateDefaultUser();
    private static PostMessage newPost;

    @BeforeClass
    private void createNewPost() throws ClientException, ApiException {
        newPost = new PostMessageProvider().getItemByUser(publicActor);

        vk.likes()
                .add(publicActor, newPost.getType(), newPost.getPostId())
                .execute();

        vk.likes()
                .add(privateActor, newPost.getType(), newPost.getPostId())
                .ownerId(publicActor.getId())
                .execute();
    }

    @Test(groups = {"smoke", "positive"})
    public void returnAllUsersFromOwnPost() throws ClientException, ApiException {
        int quantityUsers = vk.likes()
                .getList(publicActor, newPost.getType())
                .itemId(newPost.postId)
                .execute()
                .getCount();

        assertEquals(quantityUsers, 2, String.format("Количество пользователей [%s] не соответствует ожидаемому [%s].", quantityUsers, 2));
    }

    @Test(groups = {"smoke", "negative"})
    public void returnAllUsersCopiesItemFromAnotherUser() throws ClientException, ApiException {
        int errorCode = 0;
        try {
            vk.likes()
                .getList(privateActor, newPost.getType())
                .itemId(newPost.getPostId())
                .ownerId(publicActor.getId())
                .filter(LikesFilter.COPIES)
                .execute();
        }
        catch (ApiException ex) {
            errorCode = ex.getCode();
        }

        assertEquals(errorCode, 15, String.format("Значение кода ошибки [%s] не равно ожидаемому [%s]", errorCode, 15));
    }
}
