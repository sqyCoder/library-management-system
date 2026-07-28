package book;
import java.time.LocalDate; // 用于存储上架日期
import java.time.format.DateTimeFormatter;
public class Book implements Comparable<Book> {
    // 1. 核心属性（构造时必传）
    private String title;
    private String author;
    private String category;
    private int publishYear;
    private LocalDate shelfDate;

    // 2. 系统状态（有默认值）
    private boolean isBorrowed = false;
    private int borrowCount = 0;

    // 3. 唯一标识（由 Library 分配）
    private int bookId;

    // 构造方法：创建一本新书时调用
    public Book(String title, String author, String category, int publishYear, LocalDate shelfDate) {
        this.title = title;
        this.author = author;
        this.category = category;
        this.publishYear = publishYear;
        this.shelfDate = shelfDate;
        // 注意：这里没有初始化 bookId, isBorrowed, borrowCount，因为它们有各自的默认值或稍后会被设置
    }

    // --- Getter 和 Setter 方法 ---

    // 1. bookId 的 Getter 和 Setter
    public int getBookId() {
        return bookId;
    }
    public void setBookId(int bookId) {
        this.bookId = bookId; // this 关键字代表当前对象本身，区分了成员变量和参数
    }

    // 2. title 的 Getter 和 Setter
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    // 3. author 的 Getter 和 Setter
    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    // 4. category 的 Getter 和 Setter
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    // 5. publishYear 的 Getter 和 Setter
    public int getPublishYear() {
        return publishYear;
    }
    public void setPublishYear(int publishYear) {
        this.publishYear = publishYear;
    }

    // 6. shelfDate 的 Getter 和 Setter
    public LocalDate getShelfDate() {
        return shelfDate;
    }
    public void setShelfDate(LocalDate shelfDate) {
        this.shelfDate = shelfDate;
    }

    // 7. isBorrowed 的 Getter 和 Setter (注意 boolean 类型的 getter 命名习惯)
    public boolean isBorrowed() { // 通常用 is 代替 get
        return isBorrowed;
    }
    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    // 8. borrowCount 的 Getter 和 Setter
    public int getBorrowCount() {
        return borrowCount;
    }
    public void setBorrowCount(int borrowCount) {
        this.borrowCount = borrowCount;
    }

    // --- 业务逻辑方法 ---

    /**
     * 增加借阅次数
     * 当图书被成功借出时调用
     */
    public void incrementBorrowCount() {
        this.borrowCount++; // 这里封装了操作，未来可以在此添加更多逻辑，比如记录日志、检查上限等
    }

    /**
     * 减少借阅次数 (通常在归还时调用，如果你想保留历史，也可以不调用)
     */
    public void decreaseBorrowCount() {
        if (this.borrowCount > 0) { // 可以加入防护逻辑，防止出现负数
            this.borrowCount--;
        }
    }

    /**
     * 将 Book 对象序列化为逗号分隔的字符串
     * 用于持久化到文件中
     *
     * 字段顺序（必须与 parseBookJson 保持一致）:
     * bookId, title, author, category, publishYear, isBorrowed, borrowCount, shelfDate
     */
    public String toJSON() {
        StringBuilder json = new StringBuilder();

        // 按固定顺序拼接所有字段，用逗号分隔
        json.append(bookId).append(",");
        json.append(title).append(",");
        json.append(author).append(",");
        json.append(category).append(",");
        json.append(publishYear).append(",");
        json.append(isBorrowed).append(",");
        json.append(borrowCount).append(",");

        // shelfDate 需要特殊处理：格式化 + null 检查
        if (shelfDate != null) {
            json.append(shelfDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        } else {
            json.append("null");
        }

        return json.toString();
    }

    /**
     * 实现 Comparable 接口
     * 按借阅次数降序排序（借阅次数多的排在前面）
     *
     * 返回值规则：
     * 负数 -> this 排在 o 前面
     * 正数 -> this 排在 o 后面
     * 0 -> 相等
     */
    @Override
    public int compareTo(Book o) {
        // 降序排序：o.borrowCount - this.borrowCount
        // 如果 this 借阅次数多，o.borrowCount - this.borrowCount 为负，this 排前面
        return o.borrowCount - this.borrowCount;
    }

    /**
     * 重写 toString()，提供友好的文本展示
     * 用于 System.out.println(book) 时的输出
     */
    @Override
    public String toString() {
        return "图书信息{" +
                "ID=" + bookId +
                ", 书名='" + title + '\'' +
                ", 作者='" + author + '\'' +
                ", 分类='" + category + '\'' +
                ", 出版年份=" + publishYear +
                ", 状态=" + (isBorrowed ? "已借出" : "在馆") +
                ", 借阅次数=" + borrowCount +
                ", 上架日期=" + shelfDate +
                '}';
    }



}
