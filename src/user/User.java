package user;

import book.Book;
import book.Library;

import java.util.Scanner;

/**
 * 用户抽象类
 * 定义了所有用户的共同属性和强制要求的方法
 *
 * 为什么用抽象类而不是接口？
 * - 管理员和普通用户有共同的字段（name, userID, role），适合放在抽象类中
 * - display() 方法在不同角色中实现不同，必须由子类实现
 */
public abstract class User {

    // ==================== 共同属性 ====================

    protected String name;      // 用户名
    protected int userID;       // 用户唯一 ID
    protected String role;      // 角色（"管理员" 或 "普通用户"）
    protected Scanner scanner;  // 统一的 Scanner 实例

    // ==================== 构造方法 ====================

    /**
     * 构造方法
     * 注意：抽象类的构造方法不能被外部直接调用，但可以被子类通过 super() 调用
     */
    public User(String name, int userID, String role) {
        this.name = name;
        this.userID = userID;
        this.role = role;
        this.scanner = new Scanner(System.in);
    }

    // ==================== Getter 和 Setter ====================

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // ==================== 抽象方法 ====================

    /**
     * 显示用户菜单并获取用户的操作选择
     * 这是核心方法，不同角色的菜单完全不同
     *
     * @return 用户选择的操作编号
     */
    public abstract int display();

    // 添加 Library 引用（所有用户都需要访问图书馆）
    protected Library library = Library.getLibrary();

    /**
     * 查找图书（按书名）
     * 管理员和普通用户的查找逻辑相同，所以放在父类中
     */
    public void searchBook() {
        System.out.print("请输入要查找的书名：");
        String title = scanner.nextLine();

        Book book = library.searchBookByTitle(title);
        if (book != null) {
            System.out.println("✅ 找到书籍：");
            System.out.println(book);
        } else {
            System.out.println("❌ 没有找到名为《" + title + "》的书籍。");
        }
    }

    /**
     * 显示所有图书
     * 管理员和普通用户的显示逻辑相同，所以放在父类中
     */
    public void displayAllBooks() {
        library.displayAllBooks();
    }
}