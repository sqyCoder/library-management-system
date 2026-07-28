package book;

/**
 * 辅助类：记录借阅关系
 * 表示"某个用户(userId)借了某本书(bookId)"
 */
public class PairOfUidAndBookId {

    private int userId;  // 借阅用户的 ID
    private int bookId;  // 被借阅书籍的 ID

    // 无参构造方法（后续反序列化时需要）
    public PairOfUidAndBookId() {
    }

    // 有参构造方法
    public PairOfUidAndBookId(int userId, int bookId) {
        this.userId = userId;
        this.bookId = bookId;
    }

    // Getter 和 Setter
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    /**
     * 序列化方法：将借阅关系转为逗号分隔字符串
     * 格式: userId,bookId
     * 用于后续持久化到文件
     */
    public String toJson() {
        return userId + "," + bookId;
    }

    @Override
    public String toString() {
        return "借阅记录{用户ID=" + userId + ", 书籍ID=" + bookId + "}";
    }
}
