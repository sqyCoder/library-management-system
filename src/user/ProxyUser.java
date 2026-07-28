package user;

import book.Library;
import constant.Constant;
import utils.PermissionException;

/**
 * 用户代理类
 *
 * 代理模式的实现：
 * 1. 持有真实用户的引用
 * 2. 在调用真实对象方法前进行权限检查
 * 3. 权限检查失败时抛出 PermissionException
 */
public class ProxyUser {

    // ==================== 字段 ====================

    // 被代理的真实用户（可能是 AdminUser 或 NormalUser）
    private User realUser;

    // Library 引用（用于查找图书等公共操作）
    private Library library = Library.getLibrary();

    // ==================== 构造方法 ====================

    /**
     * 构造方法
     * @param user 要代理的真实用户
     */
    public ProxyUser(User user) {
        this.realUser = user;
    }

    // ==================== 权限检查方法 ====================

    /**
     * 检查真实用户是否是管理员
     * 如果不是，抛出权限异常
     */
    private void checkAdminPermission(String operationName) {
        if (!(realUser instanceof AdminUser)) {
            throw new PermissionException(
                    "❌ 权限不足：普通用户无权执行【" + operationName + "】操作。"
            );
        }
    }

    /**
     * 检查真实用户是否是普通用户
     * 如果不是，抛出权限异常
     */
    private void checkNormalUserPermission(String operationName) {
        if (!(realUser instanceof NormalUser)) {
            throw new PermissionException(
                    "❌ 权限不足：管理员无权执行【" + operationName + "】操作。"
            );
        }
    }

    // ==================== 管理员专属代理方法 ====================

    /**
     * 上架图书（管理员权限）
     */
    public void addBook() {
        System.out.println("🔐 正在执行：上架图书");
        checkAdminPermission("上架图书");
        ((AdminUser) realUser).addBook();
    }

    /**
     * 修改图书（管理员权限）
     */
    public void updateBook() {
        System.out.println("🔐 正在执行：修改图书");
        checkAdminPermission("修改图书");
        ((AdminUser) realUser).updateBook();
    }

    /**
     * 下架图书（管理员权限）
     */
    public void removeBook() {
        System.out.println("🔐 正在执行：下架图书");
        checkAdminPermission("下架图书");
        ((AdminUser) realUser).removeBook();
    }

    /**
     * 查看借阅次数统计（管理员权限）
     */
    public void viewBorrowCount() {
        System.out.println("🔐 正在执行：查看借阅次数统计");
        checkAdminPermission("查看借阅次数统计");
        ((AdminUser) realUser).viewBorrowCount();
    }

    /**
     * 热门 Top-K 书籍（管理员权限）
     */
    public void generateTopBooks() {
        System.out.println("🔐 正在执行：热门Top-K书籍");
        checkAdminPermission("热门Top-K书籍");
        ((AdminUser) realUser).generateTopBooks();
    }

    /**
     * 查看库存状态（管理员权限）
     */
    public void checkInventory() {
        System.out.println("🔐 正在执行：查看库存状态");
        checkAdminPermission("查看库存状态");
        ((AdminUser) realUser).checkInventory();
    }

    /**
     * 清理超过一年的图书（管理员权限）
     */
    public void cleanOldBooks() {
        System.out.println("🔐 正在执行：清理过期图书");
        checkAdminPermission("清理过期图书");
        ((AdminUser) realUser).cleanOldBooks();
    }

    // ==================== 普通用户专属代理方法 ====================

    /**
     * 借阅图书（普通用户权限）
     */
    public void borrowBook() {
        System.out.println("🔐 正在执行：借阅图书");
        checkNormalUserPermission("借阅图书");
        ((NormalUser) realUser).borrowBook();
    }

    /**
     * 归还图书（普通用户权限）
     */
    public void returnBook() {
        System.out.println("🔐 正在执行：归还图书");
        checkNormalUserPermission("归还图书");
        ((NormalUser) realUser).returnBook();
    }

    /**
     * 查看借阅记录（普通用户权限）
     */
    public void viewMyBorrowedBooks() {
        System.out.println("🔐 正在执行：查看借阅记录");
        checkNormalUserPermission("查看借阅记录");
        ((NormalUser) realUser).viewMyBorrowedBooks();
    }

    // ==================== 公共方法（不需要权限检查）====================

    /**
     * 显示菜单（直接转发给真实对象）
     */
    public int display() {
        return realUser.display();
    }

    /**
     * 查找图书（直接转发给真实对象）
     */
    public void searchBook() {
        realUser.searchBook();
    }

    /**
     * 显示所有图书（直接转发给真实对象）
     */
    public void displayAllBooks() {
        realUser.displayAllBooks();
    }

    /**
     * 获取真实用户对象（供外部访问）
     */
    public User getRealUser() {
        return realUser;
    }

    // ==================== 操作调度方法 ====================

    /**
     * 根据操作编号执行对应操作
     * 所有操作都通过代理方法调用，自动进行权限校验
     */
    public void handleOperation(int choice) {
        try {
            // 公共操作
            switch (choice) {
                case Constant.SEARCH_BOOK:      // 1
                    searchBook();
                    break;
                case Constant.DISPLAY_BOOK:     // 2
                    displayAllBooks();
                    break;
                case Constant.EXIT:             // 3
                    System.out.println("👋 系统已退出...");
                    System.exit(0);
                    break; // 虽然 System.exit(0) 会终止程序，但保留 break 是良好的编码习惯
            }

            // 管理员专属操作
            if (realUser instanceof AdminUser) {
                switch (choice) {
                    case Constant.ADD_BOOK:              // 4
                        addBook();
                        break;
                    case Constant.UPDATE_BOOK:           // 5
                        updateBook();
                        break;
                    case Constant.REMOVE_BOOK:           // 6
                        removeBook();
                        break;
                    case Constant.VIEW_BORROW_COUNT:      // 7
                        viewBorrowCount();
                        break;
                    case Constant.GENERATE_TOP_BOOKS:    // 8
                        generateTopBooks();
                        break;
                    case Constant.CHECK_INVENTORY:       // 9
                        checkInventory();
                        break;
                    case Constant.CLEAN_OLD_BOOKS:    // 10
                        cleanOldBooks();
                        break;
                }
            }

            // 普通用户专属操作
            if (realUser instanceof NormalUser) {
                switch (choice) {
                    case Constant.BORROW_BOOK:            // 4
                        borrowBook();
                        break;
                    case Constant.RETURN_BOOK:            // 5
                        returnBook();
                        break;
                    case Constant.VIEW_BORROW_RECORD:     // 6
                        viewMyBorrowedBooks();
                        break;
                }
            }

        } catch (utils.PermissionException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ 操作异常：" + e.getMessage());
        }
    }
}