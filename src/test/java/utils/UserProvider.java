package utils;

import com.vk.api.sdk.client.actors.UserActor;

import static utils.PropertyLoader.loadProperty;

public class UserProvider {

    private UserProvider() {}

    public static UserActor getPublicDefaultUser() {
        return new UserActor(Integer.valueOf(loadProperty("PublicUser.id")), loadProperty("PublicUser.access_token"));
    }

    public static UserActor getPrivateDefaultUser() {
        return new UserActor(Integer.valueOf(loadProperty("PrivateUser.id")), loadProperty("PrivateUser.access_token"));
    }
}
