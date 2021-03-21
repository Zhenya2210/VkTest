package likes;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.likes.Type;
import com.vk.api.sdk.objects.likes.responses.IsLikedResponse;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static utils.UserProvider.getDefaultUser;
import static utils.VkClient.getVkApiClient;

public class IsLiked {

    private static VkApiClient vk = getVkApiClient();

    @DataProvider
    public Object[][] dataCorrectItems() {
        return new Object[][] {
                new Object[] {getDefaultUser(), Type.PHOTO, 1, true},
                new Object[] {getDefaultUser(), Type.PHOTO, 2, false}
        };
    }

    @DataProvider
    public Object[][] dataCorrectPrivateItems() {
        return new Object[][] {
                new Object[] {getDefaultUser(), 2, Type.PHOTO, 88},
                new Object[] {getDefaultUser(), 2, Type.PHOTO, 99}
        };
    }

    @Test(dataProvider = "dataCorrectItems", groups = {"smoke", "positive"})
    public void correctItems(UserActor actor, Type type, int itemId, boolean expectedResult) throws ClientException, ApiException {
       IsLikedResponse isLikedResponse = vk.likes()
               .isLiked(actor, type, itemId)
               .execute();
       boolean actualResult = isLikedResponse.isLiked();
       assertEquals(actualResult, expectedResult);
    }


    @Test(dataProvider = "dataCorrectPrivateItems", groups = {"smoke", "negative"})
    public void testPrivateItems(UserActor actor, int ownerId, Type type, int itemId) throws ClientException {
       int errorCode = 0;
       try { vk.likes()
                .isLiked(actor, type, itemId)
                .ownerId(ownerId)
                .execute();}
       catch(ApiException ex) {
           errorCode = ex.getCode();
       }
       assertEquals(errorCode, 30, String.format("Значение кода ошибки [%s] не равно ожидаемому [%s]", errorCode, 30));
    }
}
