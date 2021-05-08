package idv.tfp10101.tfp10101bowen2;

import java.io.Serializable;

/**
 * 配合Internal Storage做存取
 * 注意：有加入Serializable
 */
public class User implements Serializable {
    private String name;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
