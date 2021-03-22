package service;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import entities.items.Likeable;

public interface ItemProvider {

    Likeable getItemByActor(UserActor userActor) throws ClientException, ApiException;
}
