package likes;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.likes.Type;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static utils.UserProvider.getDefaultUser;
import static utils.VkClient.getVkApiClient;

public class Add {

    private static VkApiClient vk = getVkApiClient();
    private static UserActor actor = getDefaultUser();

    @Test(groups = {"smoke", "positive"})
    public void addLikeForOwnPhoto() throws ClientException, ApiException {
        int quantityBeforeAdd = vk.likes()
                .delete(actor, Type.PHOTO, 123)
                .execute()
                .getLikes();

        int quantityAfterAdd = vk.likes()
                .add(actor, Type.PHOTO, 123)
                .execute()
                .getLikes();

        assertEquals(quantityAfterAdd, quantityBeforeAdd + 1, String.format("Количество лайков [%s] не равно ожидаемому результату [%s]", quantityAfterAdd, quantityBeforeAdd + 1));
    }

    @Test(groups = {"smoke", "negative"})
    public void addLikeForPrivatePhoto() throws ClientException {
        int errorCode = 0;
        try { vk.likes()
                .add(actor, Type.PHOTO, 666)
                .ownerId(2)
                .execute();}
        catch(ApiException ex) {
            errorCode = ex.getCode();
        }
        assertEquals(errorCode, 30, String.format("Значение кода ошибки [%s] не равно ожидаемому [%s]", errorCode, 30));
    }
}
