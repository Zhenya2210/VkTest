package utils;

import com.vk.api.sdk.client.actors.UserActor;

import static utils.PropertyLoader.loadProperty;

public class UserProvider {

    private UserProvider() {}

    public static UserActor getDefaultUser() {
        return new UserActor(Integer.valueOf(loadProperty("User.id")), loadProperty("User.access_token"));
    }
}
