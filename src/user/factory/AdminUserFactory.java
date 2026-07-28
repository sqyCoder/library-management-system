package user.factory;

import user.AdminUser;
import user.User;

/**
 * 管理员用户工厂
 * 负责创建 AdminUser 对象
 */
public class AdminUserFactory implements IUserFactory {

    /**
     * 创建管理员用户
     */
    @Override
    public User createUser(String name, int userID) {
        System.out.println("🏭 管理员工厂创建用户：" + name);
        return new AdminUser(name, userID);
    }
}