package user.factory;

import user.NormalUser;
import user.User;

/**
 * 普通用户工厂
 * 负责创建 NormalUser 对象
 */
public class NormalUserFactory implements IUserFactory {

    /**
     * 创建普通用户
     */
    @Override
    public User createUser(String name, int userID) {
        System.out.println("🏭 普通用户工厂创建用户：" + name);
        return new NormalUser(name, userID);
    }
}