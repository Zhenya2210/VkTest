package likes;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.likes.Type;
import com.vk.api.sdk.objects.likes.responses.IsLikedResponse;
import entities.items.PostMessage;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import service.PostMessageProvider;

import static org.testng.Assert.assertEquals;
import static utils.UserProvider.getPrivateDefaultUser;
import static utils.UserProvider.getPublicDefaultUser;
import static utils.VkClient.getVkApiClient;

public class IsLiked {

    private static VkApiClient vk = getVkApiClient();
    private static UserActor publicActor = getPublicDefaultUser();
    private static UserActor privateActor = getPrivateDefaultUser();

    @DataProvider
    public Object[][] dataCorrectItems() throws ClientException, ApiException {
        PostMessage newPostFirst = new PostMessageProvider().getItemByUser(publicActor);
        PostMessage newPostSecond = new PostMessageProvider().getItemByUser(publicActor);
        vk.likes()
                .add(publicActor, newPostFirst.getType(), newPostFirst.getPostId())
                .execute();

        return new Object[][] {
                new Object[] {publicActor, newPostFirst.getType(), newPostFirst.getPostId(), true},
                new Object[] {publicActor, newPostSecond.getType(), newPostSecond.getPostId(), false}
        };
    }

    @DataProvider
    public Object[][] dataCorrectPrivateItems() throws ClientException, ApiException {
        PostMessage newPost = new PostMessageProvider().getItemByUser(privateActor);

        return new Object[][] {
                new Object[] {publicActor, privateActor.getId(), newPost.getType(), newPost.getPostId()}
        };
    }

    @Test(dataProvider = "dataCorrectItems", groups = {"smoke", "positive"})
    public void correctItems(UserActor actor, Type type, int itemId, boolean expectedResult) throws ClientException, ApiException {
       IsLikedResponse isLikedResponse = vk.likes()
               .isLiked(actor, type, itemId)
               .execute();
       boolean actualResult = isLikedResponse.isLiked();
       assertEquals(actualResult, expectedResult, String.format("Значение флага isLiked [%s] не равно ожидаемому [%s]", actualResult, expectedResult));
    }


    @Test(dataProvider = "dataCorrectPrivateItems", groups = {"smoke", "negative"})
    public void privateItems(UserActor actor, int ownerId, Type type, int itemId) throws ClientException {
       int errorCode = 0;
       try {
           vk.likes()
                .isLiked(actor, type, itemId)
                .ownerId(ownerId)
                .execute();
       }
       catch(ApiException ex) {
           errorCode = ex.getCode();
       }
       assertEquals(errorCode, 30, String.format("Значение кода ошибки [%s] не равно ожидаемому [%s]", errorCode, 30));
    }
}
