package utils;

import book.Book;
import constant.Constant;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 书籍数据解析器/存储器
 *
 * 职责：
 * 1. 将 Book[] 数组序列化为字符串并写入文件
 * 2. 从文件读取字符串并反序列化为 Book[] 数组
 */
public class AnalyzingBook {

    // ==================== 写入方法 ====================

    /**
     * 将书籍数组写入文件
     *
     * @param books 书籍数组（可能包含 null 元素）
     * @param fileName 目标文件名
     */
    public void storeObject(Book[] books, String fileName) {
        // ① 统计有效书籍数量（排除 null）
        int validCount = 0;
        for (Book book : books) {
            if (book != null) {
                validCount++;
            }
        }

        // ② 构建 JSON 字符串
        StringBuilder jsonBuilder = new StringBuilder();
        for (int i = 0; i < validCount; i++) {
            Book book = books[i];
            if (book != null) {
                jsonBuilder.append(book.toJSON());
                // 非最后一本书时，添加换行符分隔
                if (i < validCount - 1) {
                    jsonBuilder.append("\n");
                }
            }
        }

        // ③ 调用 FileUtils 写入文件
        FileUtils.writeFile(jsonBuilder.toString(), fileName);
    }

    // ==================== 读取方法 ====================

    /**
     * 从文件读取并重建书籍数组
     *
     * @param fileName 源文件名
     * @return 重建的书籍数组，如果文件为空或不存在返回 null
     */
    public Book[] loadObject(String fileName) {
        // ① 读取文件内容
        String content = FileUtils.readFile(fileName);

        if (content == null || content.isEmpty()) {
            System.out.println("📭 文件为空或不存在：" + fileName);
            return null;
        }

        // ② 按换行符分割，得到每本书的 JSON 字符串
        String[] bookJsonStrings = content.split("\n");

        // ③ 解析每一行，重建 Book 对象
        Book[] books = new Book[bookJsonStrings.length];
        for (int i = 0; i < bookJsonStrings.length; i++) {
            books[i] = parseBookJson(bookJsonStrings[i]);
        }

        return books;
    }

    // ==================== 私有解析方法 ====================

    /**
     * 解析单行 JSON 字符串，重建 Book 对象
     *
     * 字段顺序（必须与 Book.toJSON() 保持一致）：
     * bookId, title, author, category, publishYear, isBorrowed, borrowCount, shelfDate
     */
    private Book parseBookJson(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }

        // 按逗号分割
        String[] fields = json.split(",");

        if (fields.length < 8) {
            System.out.println("⚠️ 数据格式错误，字段数量不足：" + json);
            return null;
        }

        // 按顺序解析每个字段
        int bookId = Integer.parseInt(fields[0]);
        String title = fields[1];
        String author = fields[2];
        String category = fields[3];
        int publishYear = Integer.parseInt(fields[4]);
        boolean isBorrowed = Boolean.parseBoolean(fields[5]);
        int borrowCount = Integer.parseInt(fields[6]);

        // 处理日期字段（可能为 "null"）
        LocalDate shelfDate;
        if ("null".equals(fields[7])) {
            shelfDate = null;
        } else {
            shelfDate = LocalDate.parse(fields[7], DateTimeFormatter.ISO_LOCAL_DATE);
        }

        // 构造 Book 对象（注意：构造方法只接收核心属性）
        Book book = new Book(title, author, category, publishYear, shelfDate);

        // 设置运行时属性
        book.setBookId(bookId);
        book.setBorrowed(isBorrowed);
        book.setBorrowCount(borrowCount);

        return book;
    }
}