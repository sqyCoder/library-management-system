package book;
import constant.Constant;
import utils.AnalyzingBook;
import utils.AnalyzingBorrowedBook;

import java.time.LocalDate;
import java.util.Arrays;

public class Library {

// ==================== 持久化相关 ====================

    // 书籍数据解析器
    private AnalyzingBook analyzingBook = new AnalyzingBook();

    // 借阅记录解析器
    private AnalyzingBorrowedBook analyzingBorrowed = new AnalyzingBorrowedBook();


    // ==================== 第一部分：单例模式核心 ====================

    // 1. 私有静态变量：持有唯一的 Library 实例
    // 用 static 修饰，使其属于类，而非对象
    private static Library library;

    // 2. 私有构造方法：防止外部 new 出新实例
    // 注意：构造方法中可以做初始化工作，比如从文件加载数据
    // 目前暂时留空，我们稍后再添加加载逻辑
    private Library() {
        // 启动时从文件加载书籍数据
        loadAllBook();
        // 启动时从文件加载借阅记录
        loadBorrowedRecords();
        System.out.println("📚 图书馆系统初始化完成，已加载 " + bookCount + " 本书籍和 " + borrowedCount + " 条借阅记录。");
    }

    /**
     * 从文件加载所有书籍数据
     */
    private void loadAllBook() {
        Book[] loaded = analyzingBook.loadObject(Constant.ALL_BOOK_FILE_NAME);

        if (loaded == null) {
            // 文件不存在或为空，初始化空数组
            books = new Book[Constant.DEFAULT_BOOK_CAPACITY];
            bookCount = 0;
        } else {
            // 检查是否需要扩容
            if (loaded.length > Constant.DEFAULT_BOOK_CAPACITY) {
                books = new Book[loaded.length];
            } else {
                books = new Book[Constant.DEFAULT_BOOK_CAPACITY];
            }

            // 复制数据
            System.arraycopy(loaded, 0, books, 0, loaded.length);
            bookCount = loaded.length;
        }
    }

    /**
     * 将书籍数据保存到文件
     * 在每次修改书籍数据后调用
     */
    private void storeBook() {
        analyzingBook.storeObject(books, Constant.ALL_BOOK_FILE_NAME);
    }

    /**
     * 从文件加载借阅记录
     */
    private void loadBorrowedRecords() {
        PairOfUidAndBookId[] loaded = analyzingBorrowed.loadObject(Constant.BORROWED_BOOK_FILE_NAME);

        if (loaded == null) {
            borrowedRecords = new PairOfUidAndBookId[Constant.DEFAULT_BORROWED_RECORD_CAPACITY];
            borrowedCount = 0;
        } else {
            if (loaded.length > Constant.DEFAULT_BORROWED_RECORD_CAPACITY) {
                borrowedRecords = new PairOfUidAndBookId[loaded.length];
            } else {
                borrowedRecords = new PairOfUidAndBookId[Constant.DEFAULT_BORROWED_RECORD_CAPACITY];
            }

            System.arraycopy(loaded, 0, borrowedRecords, 0, loaded.length);
            borrowedCount = loaded.length;
        }
    }

    /**
     * 将借阅记录保存到文件
     */
    private void storeBorrowedRecords() {
        analyzingBorrowed.storeObject(borrowedRecords, Constant.BORROWED_BOOK_FILE_NAME);
    }

    // 3. 公有静态方法：全局唯一的访问入口
    // 外部只能通过这个方法获取 Library 实例
    public static Library getLibrary() {
        if (library == null) {
            library = new Library(); // 第一次调用时才创建实例
        }
        return library;
    }

    // ==================== 第二部分：书籍管理的字段 ====================

    // 书籍数组：存储所有 Book 对象
    // 目前用字面量 5 作为默认容量，后续我们会抽取到 Constant 常量类
    private Book[] books = new Book[5];

    // 实际存储的书籍数量（不是数组长度，数组可能有空位）
    private int bookCount = 0;

    /**
     * 上架一本书
     *
     * @param book 要上架的书籍对象（由管理员创建好后传入）
     * @return 是否上架成功
     */
    public boolean addBook(Book book) {
        // 输入校验
        if (book == null) {
            System.out.println("❌ 上架失败：书籍对象不能为空。");
            return false;
        }

        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            System.out.println("❌ 上架失败：书名不能为空。");
            return false;
        }

        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()) {
            System.out.println("❌ 上架失败：作者不能为空。");
            return false;
        }

        if (book.getCategory() == null || book.getCategory().trim().isEmpty()) {
            System.out.println("❌ 上架失败：分类不能为空。");
            return false;
        }

        int currentYear = LocalDate.now().getYear();
        if (book.getPublishYear() < 1900 || book.getPublishYear() > currentYear) {
            System.out.println("❌ 上架失败：出版年份必须在 1900 到 " + currentYear + " 之间。");
            return false;
        }

        if (book.getShelfDate() == null) {
            System.out.println("❌ 上架失败：上架日期不能为空。");
            return false;
        }

        // 1. 检查书架是否已满，如果满了则自动扩容
        if (bookCount >= books.length) {
            System.out.println("📦 书架已满，正在扩容...");
            expandBooks(); // 自动扩容
        }

        // 2. 为新书分配唯一的 bookId
        // 如果是书架上的第一本书，ID 设为 1
        if (bookCount == 0) {
            book.setBookId(1);
        } else {
            // 否则，取最后一本书的 ID + 1
            Book lastBook = books[bookCount - 1];
            book.setBookId(lastBook.getBookId() + 1);
        }

        // 3. 将书放入数组
        books[bookCount] = book;

        // 4. 更新计数
        bookCount++;
        storeBook();
        System.out.println("✅ 图书上架成功！书名：" + book.getTitle() + "，编号：" + book.getBookId());
        return true;
    }

    /**
     * 根据书籍 ID 查找书籍
     *
     * @param bookId 书籍的唯一编号
     * @return 找到的 Book 对象，如果没找到返回 null
     */
    public Book searchBookById(int bookId) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getBookId() == bookId) {
                return books[i]; // 找到立即返回
            }
        }
        return null; // 遍历完没找到
    }

    /**
     * 显示书架上所有书籍的信息
     */
    public void displayAllBooks() {
        if (bookCount == 0) {
            System.out.println("📚 书架上目前没有任何书籍。");
            return;
        }

        System.out.println("========= 书架上所有书籍 =========");
        for (int i = 0; i < bookCount; i++) {
            System.out.println(books[i]); // 自动调用 Book.toString()
        }
        System.out.println("===================================");
    }

    /**
     * 根据书名精确查找书籍
     *
     * @param title 书名（需要完全匹配）
     * @return 找到的 Book 对象，如果没找到返回 null
     */
    public Book searchBookByTitle(String title) {
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getTitle().equals(title)) {
                return books[i];
            }
        }
        return null;
    }

    /**
     * 内部方法：当数组容量不足时，动态扩容
     * 原理：创建新数组 -> 复制旧数据 -> 替换引用
     */
    private void expandBooks() {
        // 1. 创建一个容量为原来 2 倍的新数组
        Book[] newBooks = new Book[books.length * 2];

        // 2. 将旧数组中的所有元素复制到新数组
        for (int i = 0; i < bookCount; i++) {
            newBooks[i] = books[i];
        }

        // 3. 将 books 引用指向新数组
        // 旧数组会被 Java 垃圾回收器自动回收
        books = newBooks;

        System.out.println("🔧 书架已自动扩容，当前容量：" + books.length);
    }

    // ==================== 借阅记录相关字段 ====================

    // 借阅记录数组：记录所有用户的借阅关系
    private PairOfUidAndBookId[] borrowedRecords = new PairOfUidAndBookId[10];

    // 当前借阅记录数量
    private int borrowedCount = 0;


    /**
     * 获取指定用户的所有借阅记录
     *
     * @param userId 用户 ID
     * @return 该用户的借阅记录数组
     */
    public PairOfUidAndBookId[] getUserBorrowedRecords(int userId) {
        // 统计该用户的借阅记录数量
        int count = 0;
        for (int i = 0; i < borrowedCount; i++) {
            if (borrowedRecords[i].getUserId() == userId) {
                count++;
            }
        }

        // 创建结果数组
        PairOfUidAndBookId[] userRecords = new PairOfUidAndBookId[count];
        int index = 0;
        for (int i = 0; i < borrowedCount; i++) {
            if (borrowedRecords[i].getUserId() == userId) {
                userRecords[index] = borrowedRecords[i];
                index++;
            }
        }

        return userRecords;
    }

    /**
     * 借阅书籍
     *
     * 业务校验链：
     * 1. 书存在吗？
     * 2. 书已被借出了吗？
     * 3. 用户是否已经借过这本书？
     *
     * @param userId 借阅用户的 ID
     * @param bookId 要借阅的书籍 ID
     * @return 借阅是否成功
     */
    public boolean borrowBook(int userId, int bookId) {
        // ① 查找书籍
        Book book = searchBookById(bookId);
        if (book == null) {
            System.out.println("❌ 借阅失败：编号为 " + bookId + " 的书籍不存在。");
            return false;
        }

        // ② 检查书籍是否已被借出
        if (book.isBorrowed()) {
            // 查找是谁借走了这本书
            for (int i = 0; i < borrowedCount; i++) {
                if (borrowedRecords[i].getBookId() == bookId) {
                    int otherUserId = borrowedRecords[i].getUserId();
                    System.out.println("❌ 借阅失败：《" + book.getTitle() + "》已被用户(ID:" + otherUserId + ")借走。");
                    return false;
                }
            }
            System.out.println("❌ 借阅失败：《" + book.getTitle() + "》已被借出。");
            return false;
        }

        // ③ 检查用户是否已借过这本书
        for (int i = 0; i < borrowedCount; i++) {
            PairOfUidAndBookId record = borrowedRecords[i];
            if (record.getUserId() == userId && record.getBookId() == bookId) {
                System.out.println("❌ 借阅失败：你已经借过《" + book.getTitle() + "》了。");
                return false;
            }
        }

        // ④ 全部校验通过，执行借阅
        // 更新书籍状态
        book.setBorrowed(true);
        book.incrementBorrowCount();

        // 检查借阅记录数组是否需要扩容
        if (borrowedCount >= borrowedRecords.length) {
            System.out.println("📦 借阅记录已满，正在扩容...");
            // 这里可以复用 expandBooks 的逻辑，暂时简化处理
            PairOfUidAndBookId[] newRecords = new PairOfUidAndBookId[borrowedRecords.length * 2];
            for (int i = 0; i < borrowedCount; i++) {
                newRecords[i] = borrowedRecords[i];
            }
            borrowedRecords = newRecords;
            System.out.println("🔧 借阅记录已扩容，当前容量：" + borrowedRecords.length);
        }

        // 记录借阅关系
        borrowedRecords[borrowedCount] = new PairOfUidAndBookId(userId, bookId);
        borrowedCount++;
        storeBook();
        storeBorrowedRecords();
        System.out.println("✅ 借阅成功！用户(ID:" + userId + ") 成功借阅《" + book.getTitle() + "》");
        return true;
    }

    /**
     * 归还书籍（当前版本：内存操作，暂不持久化）
     */
    public boolean returnBook(int userId, int bookId) {
        // ① 查找书籍
        Book book = searchBookById(bookId);
        if (book == null) {
            System.out.println("❌ 归还失败：编号为 " + bookId + " 的书籍不存在。");
            return false;
        }

        // ② 查找借阅记录
        int recordIndex = -1;
        for (int i = 0; i < borrowedCount; i++) {
            if (borrowedRecords[i].getUserId() == userId && borrowedRecords[i].getBookId() == bookId) {
                recordIndex = i;
                break;
            }
        }

        if (recordIndex == -1) {
            System.out.println("❌ 归还失败：你没有借阅过这本书。");
            return false;
        }

        // ③ 执行归还
        book.setBorrowed(false);
        borrowedRecords[recordIndex] = borrowedRecords[borrowedCount - 1];
        borrowedRecords[borrowedCount - 1] = null;
        borrowedCount--;
        storeBook();
        storeBorrowedRecords();
        System.out.println("✅ 归还成功！");
        return true;
    }

    /**
     * 根据 bookId 下架书籍
     *
     * 数组删除元素的原理：
     * 1. 找到要删除的位置
     * 2. 后面的元素依次前移
     * 3. 最后一个位置置 null
     * 4. bookCount--
     */
    public boolean removeBook(int bookId) {
        // ① 找到要删除的书籍
        Book book = searchBookById(bookId);
        if (book == null) {
            System.out.println("❌ 下架失败：找不到编号为 " + bookId + " 的书籍。");
            return false;
        }

        // ② 检查书籍是否正在被借阅
        if (book.isBorrowed()) {
            System.out.println("❌ 下架失败：《" + book.getTitle() + "》正在被借阅，无法下架。");
            return false;
        }

        // ③ 找到要删除的书籍位置
        int index = -1;
        for (int i = 0; i < bookCount; i++) {
            if (books[i].getBookId() == bookId) {
                index = i;
                break;
            }
        }

        if (index == -1) {
            System.out.println("❌ 下架失败：找不到编号为 " + bookId + " 的书籍。");
            return false;
        }

        // ④ 将后面的元素前移
        // 从 index 位置开始，每个元素用它后面的元素覆盖
        for (int i = index; i < bookCount - 1; i++) {
            books[i] = books[i + 1];
        }

        // ⑤ 把最后一个位置置 null（防止内存泄漏）
        books[bookCount - 1] = null;

        // ⑥ 更新计数
        bookCount--;
        storeBook();
        System.out.println("✅ 下架成功！");
        return true;
    }

    /**
     * 更新书籍信息
     *
     * 当前版本：只做内存中的状态更新，后续会添加持久化
     *
     * @param bookId 要更新的书籍 ID
     * @param newTitle 新书名
     * @param newAuthor 新作者
     * @param newCategory 新分类
     * @return 更新是否成功
     */
    public boolean updateBook(int bookId, String newTitle, String newAuthor, String newCategory) {
        // ① 查找书籍
        Book book = searchBookById(bookId);
        if (book == null) {
            System.out.println("❌ 更新失败：找不到编号为 " + bookId + " 的书籍。");
            return false;
        }

        // ② 保存旧信息，用于提示
        String oldTitle = book.getTitle();

        // ③ 更新字段
        book.setTitle(newTitle);
        book.setAuthor(newAuthor);
        book.setCategory(newCategory);
        storeBook();
        System.out.println("✅ 更新成功！《" + oldTitle + "》 → 《" + newTitle + "》");
        System.out.println("   新作者：" + newAuthor + "，新分类：" + newCategory);
        return true;
    }

    /**
     * 查看所有书籍的借阅次数统计
     */
    public void viewBorrowCount() {
        if (bookCount == 0) {
            System.out.println("📚 书架上目前没有任何书籍。");
            return;
        }

        System.out.println("\n========= 借阅次数统计 =========");
        for (int i = 0; i < bookCount; i++) {
            Book book = books[i];
            System.out.println("ID: " + book.getBookId() +
                    " | 书名: " + book.getTitle() +
                    " | 借阅次数: " + book.getBorrowCount());
        }
        System.out.println("===================================");
    }

    /**
     * 生成最受欢迎的前 K 本书
     *
     * 原理：
     * 1. 复制书籍到临时数组
     * 2. 使用 Arrays.sort() 排序（Book 已实现 Comparable，按借阅次数降序）
     * 3. 取前 K 本输出
     */
    public void generateTopBooks(int k) {
        if (bookCount == 0) {
            System.out.println("📚 书架上目前没有任何书籍。");
            return;
        }

        if (k <= 0 || k > bookCount) {
            System.out.println("❌ 无效的 K 值：" + k + "（有效范围：1 ~ " + bookCount + "）");
            return;
        }

        // ① 复制书籍到临时数组
        Book[] temp = new Book[bookCount];
        System.arraycopy(books, 0, temp, 0, bookCount);

        // ② 排序（Book.compareTo 已实现降序排序）
        Arrays.sort(temp);

        // ③ 输出前 K 本
        System.out.println("\n========= 最受欢迎的前 " + k + " 本书 =========");
        for (int i = 0; i < k; i++) {
            Book book = temp[i];
            System.out.println("第" + (i + 1) + "名：《" + book.getTitle() +
                    "》 | 作者：" + book.getAuthor() +
                    " | 借阅次数：" + book.getBorrowCount());
        }
        System.out.println("===========================================");
    }

    /**
     * 查看库存状态（每本书的在馆/借出情况）
     */
    public void checkInventory() {
        if (bookCount == 0) {
            System.out.println("📚 书架上目前没有任何书籍。");
            return;
        }

        System.out.println("\n========= 库存状态 =========");
        int borrowed = 0;
        for (int i = 0; i < bookCount; i++) {
            Book book = books[i];
            String status = book.isBorrowed() ? "🔴 已借出" : "🟢 在馆";
            System.out.println("ID: " + book.getBookId() +
                    " | 书名: " + book.getTitle() +
                    " | 状态: " + status);
            if (book.isBorrowed()) {
                borrowed++;
            }
        }
        System.out.println("---------------------------");
        System.out.println("总计：" + bookCount + "本 | 在馆：" + (bookCount - borrowed) +
                "本 | 借出：" + borrowed + "本");
        System.out.println("===========================");
    }

    /**
     * 清理上架超过一年的书籍
     * 仅清理未被借出的书籍
     *
     * @return 被清理的书籍数量
     */
    public int cleanOldBooks() {
        if (bookCount == 0) {
            System.out.println("📚 书架上目前没有任何书籍。");
            return 0;
        }

        LocalDate oneYearAgo = LocalDate.now().minusYears(1);
        int cleanedCount = 0;

        System.out.println("\n========= 清理超过一年的图书 =========");

        // 从后向前遍历，避免索引问题
        for (int i = bookCount - 1; i >= 0; i--) {
            Book book = books[i];

            // 检查是否有上架日期
            if (book.getShelfDate() == null) {
                continue;
            }

            // 检查上架时间是否超过一年
            if (book.getShelfDate().isBefore(oneYearAgo)) {
                // 检查是否正在被借阅
                if (book.isBorrowed()) {
                    System.out.println("⚠️ 跳过《" + book.getTitle() + "》(ID:" + book.getBookId() + ")：已借出，无法清理");
                    continue;
                }

                // 清理该书
                System.out.println("🗑️ 清理：《" + book.getTitle() + "》(ID:" + book.getBookId() +
                        ")，上架日期：" + book.getShelfDate());

                // 执行下架操作
                removeBook(book.getBookId());
                cleanedCount++;
            }
        }

        if (cleanedCount == 0) {
            System.out.println("✅ 没有需要清理的过期书籍。");
        } else {
            System.out.println("✅ 清理完成，共清理 " + cleanedCount + " 本过期书籍。");
        }
        System.out.println("========================================");

        return cleanedCount;
    }
}
