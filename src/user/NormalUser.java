package user;

import book.Book;
import book.Library;
import book.PairOfUidAndBookId;

import java.util.Scanner;

/**
 * 普通用户类
 * 继承自 User 抽象类，实现普通用户的菜单和操作
 */
public class NormalUser extends User {

    // ==================== 字段 ====================

    private PairOfUidAndBookId[] myBorrowedBooks;  // 当前用户的借阅记录
    private int myBorrowedCount;       // 当前用户的借阅数量

    // ==================== 构造方法 ====================

    /**
     * 构造方法
     * @param name 用户名
     * @param userID 用户唯一 ID
     */
    public NormalUser(String name, int userID) {
        super(name, userID, "普通用户"); // 调用父类 User 的构造方法

        // 从 Library 加载持久化的借阅记录
        PairOfUidAndBookId[] savedRecords = library.getUserBorrowedRecords(userID);
        if (savedRecords != null && savedRecords.length > 0) {
            // 扩容数组以容纳现有记录
            int capacity = Math.max(savedRecords.length, 5);
            this.myBorrowedBooks = new PairOfUidAndBookId[capacity];
            System.arraycopy(savedRecords, 0, this.myBorrowedBooks, 0, savedRecords.length);
            this.myBorrowedCount = savedRecords.length;
        } else {
            this.myBorrowedBooks = new PairOfUidAndBookId[5]; // 默认最多借 5 本
            this.myBorrowedCount = 0;
        }
    }


    /**
     * 实现父类的抽象方法
     * 显示普通用户的操作菜单，获取用户选择
     *
     * @return 用户选择的操作编号
     */
    @Override
    public int display() {
        System.out.println("================================");
        System.out.println("  普通用户 " + name + " 的操作菜单");
        System.out.println("================================");
        System.out.println("1. 查找图书");
        System.out.println("2. 打印所有图书");
        System.out.println("3. 退出系统");
        System.out.println("4. 借阅图书");
        System.out.println("5. 归还图书");
        System.out.println("6. 查看我的借阅记录");
        System.out.println("=================================");
        System.out.print("请选择你的操作：");
        return scanner.nextInt();
    }


    /**
     * 借阅书籍
     * 流程：显示书籍 → 读取 bookId → 调用 Library.borrowBook() → 更新个人记录
     */
    public void borrowBook() {
        System.out.println("\n========= 借阅书籍 =========");

        // 1. 显示所有书籍供用户选择
        library.displayAllBooks();

        // 2. 读取用户想借阅的书籍 ID
        System.out.print("请输入要借阅的书籍编号：");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // 吞掉换行符，防止影响下次读取

        // 3. 调用 Library 的借阅方法
        boolean success = library.borrowBook(userID, bookId);

        // 4. 如果借阅成功，更新个人借阅记录
        if (success) {
            // 检查个人借阅记录数组是否需要扩容
            if (myBorrowedCount >= myBorrowedBooks.length) {
                PairOfUidAndBookId[] newArr = new PairOfUidAndBookId[myBorrowedBooks.length * 2];
                System.arraycopy(myBorrowedBooks, 0, newArr, 0, myBorrowedCount);
                myBorrowedBooks = newArr;
                System.out.println("🔧 个人借阅记录已扩容");
            }

            // 添加到个人记录
            myBorrowedBooks[myBorrowedCount] = new PairOfUidAndBookId(userID, bookId);
            myBorrowedCount++;
        }
    }

    /**
     * 归还书籍
     * 流程：显示个人借阅记录 → 读取 bookId → 调用 Library.returnBook() → 更新个人记录
     */
    public void returnBook() {
        System.out.println("\n========= 归还书籍 =========");

        // 1. 先显示当前用户的借阅记录
        viewMyBorrowedBooks();

        if (myBorrowedCount == 0) {
            System.out.println("你目前没有借阅任何书籍。");
            return;
        }

        // 2. 读取用户想归还的书籍 ID
        System.out.print("请输入要归还的书籍编号：");
        int bookId = scanner.nextInt();
        scanner.nextLine();

        // 3. 调用 Library 的归还方法
        boolean success = library.returnBook(userID, bookId);

        // 4. 如果归还成功，从个人记录中移除
        if (success) {
            for (int i = 0; i < myBorrowedCount; i++) {
                if (myBorrowedBooks[i].getBookId() == bookId) {
                    // 用最后一条替换当前位置
                    myBorrowedBooks[i] = myBorrowedBooks[myBorrowedCount - 1];
                    myBorrowedBooks[myBorrowedCount - 1] = null;
                    myBorrowedCount--;
                    break;
                }
            }
        }
    }

    /**
     * 查看当前用户的借阅记录
     * 遍历个人借阅记录数组，打印每本书的详细信息
     */
    public void viewMyBorrowedBooks() {
        System.out.println("\n========= 我的借阅记录 =========");

        if (myBorrowedCount == 0) {
            System.out.println("你目前没有借阅任何书籍。");
            return;
        }

        for (int i = 0; i < myBorrowedCount; i++) {
            PairOfUidAndBookId record = myBorrowedBooks[i];
            // 通过 Library 查找书籍详情
            Book book = library.searchBookById(record.getBookId());
            if (book != null) {
                System.out.println(book);
            }
        }
        System.out.println("===================================");
    }
}