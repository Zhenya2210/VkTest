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

public class Delete {

    private static VkApiClient vk = getVkApiClient();
    private static UserActor actor = getDefaultUser();


    @Test(groups = {"smoke", "positive"})
    public void deleteLikeFromOwnPhoto() throws ClientException, ApiException {
        int quantityBeforeDelete = vk.likes()
                .add(actor, Type.PHOTO, 123456)
                .execute()
                .getLikes();

        int quantityAfterDelete = vk.likes()
                .delete(actor, Type.PHOTO, 123456)
                .execute()
                .getLikes();

        assertEquals(quantityAfterDelete, quantityBeforeDelete - 1, String.format("Количество лайков [%s] не равно ожидаемому результату [%s]", quantityAfterDelete, quantityBeforeDelete - 1));
    }


    @Test(groups = {"smoke", "negative"})
    public void doubleDeleteLikeFromOwnPhoto() throws ClientException, ApiException {
        vk.likes()
                .add(actor, Type.PHOTO, 654321)
                .execute();

        int quantityAfterFirstDelete = vk.likes()
                .delete(actor, Type.PHOTO, 654321)
                .execute()
                .getLikes();

        int quantityAfterSecondDelete = vk.likes()
                .delete(actor, Type.PHOTO, 654321)
                .execute()
                .getLikes();

        assertEquals(quantityAfterSecondDelete, quantityAfterFirstDelete, String.format("Количество лайков [%s] не равно ожидаемому результату [%s]", quantityAfterSecondDelete, quantityAfterFirstDelete));
    }
}
