import user.AdminUser;
import user.NormalUser;
import user.ProxyUser;
import user.User;
import user.factory.AdminUserFactory;
import user.factory.IUserFactory;
import user.factory.NormalUserFactory;

import java.util.Scanner;

/**
 * 图书管理系统 - 主程序入口
 *
 * 核心流程：
 * 1. 使用工厂模式创建用户
 * 2. 使用代理模式包裹用户
 * 3. 角色选择 + 菜单循环
 */
public class LibrarySystem {

    public static void main(String[] args) {
        // ==================== 第一步：使用工厂创建真实用户 ====================

        IUserFactory adminFactory = new AdminUserFactory();
        IUserFactory normalFactory = new NormalUserFactory();

        User realAdmin = adminFactory.createUser("刘备", 1);
        User realNormal1 = normalFactory.createUser("关羽", 2);
        User realNormal2 = normalFactory.createUser("张飞", 3);

        // ==================== 第二步：创建代理用户 ====================

        ProxyUser proxyAdmin = new ProxyUser(realAdmin);
        ProxyUser proxyNormal1 = new ProxyUser(realNormal1);
        ProxyUser proxyNormal2 = new ProxyUser(realNormal2);

        // ==================== 第三步：角色选择 ====================

        Scanner scanner = new Scanner(System.in);
        ProxyUser currentProxy = selectRole(scanner, proxyAdmin, proxyNormal1, proxyNormal2);

        if (currentProxy == null) {
            System.out.println("感谢使用，再见！");
            return;
        }

        // ==================== 第四步：主操作循环 ====================

        System.out.println("\n👋 " + currentProxy.getRealUser().getName() + " 已登录！");
        System.out.println("你的角色是：" + currentProxy.getRealUser().getRole() + "\n");

        while (true) {
            try {
                // 显示菜单并获取选择
                int choice = currentProxy.display();

                // 通过代理执行操作（自动进行权限校验）
                currentProxy.handleOperation(choice);

            } catch (Exception e) {
                // 兜底异常处理
                System.out.println("❌ 系统异常：" + e.getMessage());
            }
        }
    }

    /**
     * 角色选择菜单
     */
    private static ProxyUser selectRole(Scanner scanner,
                                        ProxyUser proxyAdmin,
                                        ProxyUser proxyNormal1,
                                        ProxyUser proxyNormal2) {
        System.out.println("\n=================================");
        System.out.println("  欢迎使用图书管理系统");
        System.out.println("=================================");
        System.out.println("请选择登录角色：");
        System.out.println("1. 管理员 - 刘备");
        System.out.println("2. 普通用户 - 关羽");
        System.out.println("3. 普通用户 - 张飞");
        System.out.println("4. 退出系统");
        System.out.println("=================================");
        System.out.print("请输入你的选择：");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                return proxyAdmin;
            case 2:
                return proxyNormal1;
            case 3:
                return proxyNormal2;
            case 4:
                System.out.println("👋 系统已退出，再见！");
                System.exit(0);
                return null;
            default:
                System.out.println("⚠️ 无效的选择，请重新启动程序。");
                System.exit(1);
                return null;
        }
    }

    /**
     * 根据用户选择执行对应操作
     *
     * @param user 当前登录的用户
     * @param choice 用户选择的操作编号
     * @param scanner 扫描器
     */
    private static void handleOperation(User user, int choice, Scanner scanner) {
        // 根据用户类型分发处理
        if (user instanceof AdminUser) {
            // 管理员的操作
            AdminUser admin = (AdminUser) user;
            switch (choice) {
                case 1: // 查找图书
                    admin.searchBook();
                    break;
                case 2: // 打印所有图书
                    admin.displayAllBooks();
                    break;
                case 3: // 退出系统
                    System.out.println("系统已退出...");
                    System.exit(0);
                    break;
                case 4: // 上架图书
                    admin.addBook();
                    break;
                case 5: // 修改图书
                    admin.updateBook();
                    break;
                case 6: // 下架图书
                    admin.removeBook();
                    break;
                case 7: // 查看借阅次数统计
                    admin.viewBorrowCount();
                    break;
                case 8: // 热门 Top-K 书籍
                    admin.generateTopBooks();
                    break;
                case 9: // 查看库存状态
                    admin.checkInventory();
                    break;
                default:
                    System.out.println("❌ 无效的操作选择。");
            }
        } else if (user instanceof NormalUser) {
            // 普通用户的操作
            NormalUser normalUser = (NormalUser) user;
            switch (choice) {
                case 1: // 查找图书
                    normalUser.searchBook();
                    break;
                case 2: // 打印所有图书
                    normalUser.displayAllBooks();
                    break;
                case 3: // 退出系统
                    System.out.println("系统已退出...");
                    System.exit(0);
                    break;
                case 4: // 借阅图书
                    normalUser.borrowBook();
                    break;
                case 5: // 归还图书
                    normalUser.returnBook();
                    break;
                case 6: // 查看借阅记录
                    normalUser.viewMyBorrowedBooks();
                    break;
                default:
                    System.out.println("❌ 无效的操作选择。");
            }
        }
    }
}