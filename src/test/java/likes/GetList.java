package likes;

import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.objects.enums.LikesFilter;
import com.vk.api.sdk.objects.likes.Type;
import org.testng.annotations.Test;


import static org.testng.Assert.assertEquals;
import static utils.UserProvider.getDefaultUser;
import static utils.VkClient.getVkApiClient;


public class GetList {

    private static VkApiClient vk = getVkApiClient();
    private static UserActor actor = getDefaultUser();


    @Test(groups = {"smoke", "positive"})
    public void returnAllUsersFromOwnPhoto() throws ClientException, ApiException {
        int quantityUsers = vk.likes()
                .getList(actor, Type.PHOTO)
                .itemId(88888)
                .execute()
                .getCount();

        assertEquals(quantityUsers, 50, String.format("Количество пользователей [%s] не соответствует ожидаемому [%s].", quantityUsers, 50));
    }

    @Test(groups = {"smoke", "negative"})
    public void returnAllUsersCopiesItemFromAnotherUser() throws ClientException, ApiException {
        int quantityUsers = vk.likes()
                .getList(actor, Type.PHOTO)
                .itemId(444)
                .ownerId(5656)
                .filter(LikesFilter.COPIES)
                .execute()
                .getCount();

        assertEquals(quantityUsers, 0, String.format("Количество пользователей [%s] не соответствует ожидаемому [%s].", quantityUsers, 0));
        //Или если тут должен быть exception вместо 0, то обработать его
    }
}
