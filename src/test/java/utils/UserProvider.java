package utils;

import com.vk.api.sdk.client.actors.UserActor;

import static utils.PropertyLoader.loadProperty;

public class UserProvider {

    private static UserActor publicDefaultUser = null;
    private static UserActor privateDefaultUser = null;

    private UserProvider() {}

    public static UserActor getPublicDefaultUser() {
        if (publicDefaultUser == null) {
            publicDefaultUser = new UserActor(Integer.valueOf(loadProperty("PublicUser.id")), loadProperty("PublicUser.access_token"));
        }
        return publicDefaultUser;
    }

    public static UserActor getPrivateDefaultUser() {
        if (privateDefaultUser == null) {
            return new UserActor(Integer.valueOf(loadProperty("PrivateUser.id")), loadProperty("PrivateUser.access_token"));
        }
        return privateDefaultUser;
    }
}
