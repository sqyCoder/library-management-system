package user.factory;

import user.User;

/**
 * 用户工厂接口
 *
 * 工厂模式的核心：
 * 1. 定义创建用户的标准接口
 * 2. 调用方只依赖此接口，不依赖具体实现
 */
public interface IUserFactory {

    /**
     * 根据用户名和用户 ID 创建用户
     *
     * @param name 用户名
     * @param userID 用户唯一 ID
     * @return 创建的 User 对象
     */
    User createUser(String name, int userID);
}