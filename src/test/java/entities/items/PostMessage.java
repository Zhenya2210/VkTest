package entities.items;

import com.vk.api.sdk.objects.likes.Type;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostMessage implements Likeable {
    public Integer postId;
    public Integer ownerId;

    @Override
    public Type getType() {
        return Type.POST;
    }
}
