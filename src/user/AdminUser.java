package user;

import book.Book;
import book.Library;

import java.time.LocalDate;

/**
 * 管理员用户类
 * 继承自 User 抽象类，实现管理员的菜单和图书管理操作
 */
public class AdminUser extends User {

    // ==================== 构造方法 ====================

    /**
     * 构造方法
     * @param name 用户名
     * @param userID 用户唯一 ID
     */
    public AdminUser(String name, int userID) {
        super(name, userID, "管理员"); // 调用父类构造方法，角色设为"管理员"
    }

    /**
     * 实现父类的抽象方法
     * 显示管理员的操作菜单，获取用户选择
     */
    @Override
    public int display() {
        System.out.println("=================================");
        System.out.println("  管理员 " + name + " 的操作菜单");
        System.out.println("=================================");
        System.out.println("1. 查找图书");
        System.out.println("2. 打印所有图书");
        System.out.println("3. 退出系统");
        System.out.println("4. 上架图书");
        System.out.println("5. 修改图书");
        System.out.println("6. 下架图书");
        System.out.println("7. 查看借阅次数统计");
        System.out.println("8. 最受欢迎的前K本书");
        System.out.println("9. 查看库存状态");
        System.out.println("10. 清理超过一年的图书");
        System.out.println("=================================");
        System.out.print("请选择你的操作：");
        return scanner.nextInt();
    }

    /**
     * 上架图书
     * 流程：读取管理员输入 → 创建 Book 对象 → 调用 Library.addBook()
     */
    public void addBook() {
        System.out.println("\n========= 上架图书 =========");
        scanner.nextLine();
        // 1. 读取图书信息
        System.out.print("请输入书名：");
        String title = scanner.nextLine();
        System.out.print("请输入作者：");
        String author = scanner.nextLine();

        System.out.print("请输入分类：");
        String category = scanner.nextLine();

        System.out.print("请输入出版年份：");
        int year = scanner.nextInt();
        scanner.nextLine(); // 吞掉换行符

        // 2. 自动获取当前日期作为上架日期
        LocalDate shelfDate = LocalDate.now();

        // 3. 创建 Book 对象
        Book newBook = new Book(title, author, category, year, shelfDate);

        // 4. 调用 Library 的 addBook 方法完成上架
        boolean success = library.addBook(newBook);

        if (success) {
            System.out.println("✅ 《" + title + "》已成功上架！");
        }
    }

    /**
     * 下架图书
     * 流程：显示所有书籍 → 读取 bookId → 调用 Library.removeBook()
     */
    public void removeBook() {
        System.out.println("\n========= 下架图书 =========");

        // 1. 先显示所有书籍，方便管理员查看
        library.displayAllBooks();

        // 2. 读取要下架的书籍 ID
        System.out.print("请输入要下架的书籍编号：");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        // 3. 调用 Library 的 removeBook 方法
        boolean success = library.removeBook(bookId);

        if (success) {
            System.out.println("📚 书籍已下架。");
        }
    }

    /**
     * 修改图书信息
     * 流程：显示书籍列表 → 读取 bookId → 读取新信息 → 调用 Library.updateBook()
     */
    public void updateBook() {
        System.out.println("\n========= 修改图书 =========");

        // 1. 显示所有书籍
        library.displayAllBooks();

        // 2. 读取要修改的书籍 ID
        System.out.print("请输入要修改的书籍编号：");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        // 3. 查找该书
        Book book = library.searchBookById(bookId);
        if (book == null) {
            System.out.println("❌ 找不到编号为 " + bookId + " 的书籍。");
            return;
        }

        // 4. 显示当前信息
        System.out.println("\n当前书籍信息：");
        System.out.println(book);

        // 5. 读取新信息（留空表示不修改）
        System.out.print("请输入新书名（直接回车保持不变）：");
        String newTitle = scanner.nextLine();
        // 如果用户直接回车，使用原值
        if (newTitle.isEmpty()) {
            newTitle = book.getTitle();
        }

        System.out.print("请输入新作者（直接回车保持不变）：");
        String newAuthor = scanner.nextLine();
        if (newAuthor.isEmpty()) {
            newAuthor = book.getAuthor();
        }

        System.out.print("请输入新分类（直接回车保持不变）：");
        String newCategory = scanner.nextLine();
        if (newCategory.isEmpty()) {
            newCategory = book.getCategory();
        }

        // 6. 调用 Library 的 updateBook 方法
        library.updateBook(bookId, newTitle, newAuthor, newCategory);
    }

    /**
     * 查看借阅次数统计
     */
    public void viewBorrowCount() {
        System.out.println("\n========= 借阅次数统计 =========");
        library.viewBorrowCount();
    }

    /**
     * 生成最受欢迎的前 K 本书
     */
    public void generateTopBooks() {
        System.out.println("\n========= 热门书籍 =========");
        System.out.print("请输入要查看的 Top-K 值：");
        int k = scanner.nextInt();
        scanner.nextLine();
        library.generateTopBooks(k);
    }

    /**
     * 查看库存状态
     */
    public void checkInventory() {
        System.out.println("\n========= 库存状态 =========");
        library.checkInventory();
    }

    /**
     * 清理上架超过一年的图书
     */
    public void cleanOldBooks() {
        System.out.println("\n========= 清理过期图书 =========");
        library.cleanOldBooks();
    }
}