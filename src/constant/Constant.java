package constant;

/**
 * 常量类
 *
 * 集中管理项目中使用的常量，避免硬编码
 * 当常量值需要修改时，只需修改此处
 */
public class Constant {

    // ==================== 文件路径 ====================

    // 书籍数据文件名
    public static final String ALL_BOOK_FILE_NAME = "allbooks.txt";

    // 借阅记录文件名
    public static final String BORROWED_BOOK_FILE_NAME = "borrowedbook.txt";

    // ==================== 数组默认容量 ====================

    // 书籍数组默认容量
    public static final int DEFAULT_BOOK_CAPACITY = 5;

    // 借阅记录默认容量
    public static final int DEFAULT_BORROWED_RECORD_CAPACITY = 10;

    // ==================== 管理员菜单编号 ====================

    public static final int SEARCH_BOOK = 1;          // 查找图书
    public static final int DISPLAY_BOOK = 2;         // 打印所有图书
    public static final int EXIT = 3;                 // 退出系统
    public static final int ADD_BOOK = 4;             // 上架图书
    public static final int UPDATE_BOOK = 5;          // 修改图书
    public static final int REMOVE_BOOK = 6;          // 下架图书
    public static final int VIEW_BORROW_COUNT = 7;    // 查看借阅次数统计
    public static final int GENERATE_TOP_BOOKS = 8;   // 热门Top-K书籍
    public static final int CHECK_INVENTORY = 9;      // 查看库存状态
    public static final int CLEAN_OLD_BOOKS = 10;   // 清理超过一年的图书

    // ==================== 普通用户菜单编号 ====================

    public static final int BORROW_BOOK = 4;          // 借阅图书
    public static final int RETURN_BOOK = 5;          // 归还图书
    public static final int VIEW_BORROW_RECORD = 6;   // 查看借阅记录
}