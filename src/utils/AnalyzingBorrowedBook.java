package utils;

import book.PairOfUidAndBookId;
import constant.Constant;

/**
 * 借阅记录解析器/存储器
 *
 * 职责：
 * 1. 将 PairOfUidAndBookId[] 数组序列化为字符串并写入文件
 * 2. 从文件读取字符串并反序列化为 PairOfUidAndBookId[] 数组
 */
public class AnalyzingBorrowedBook {

    // ==================== 写入方法 ====================

    /**
     * 将借阅记录数组写入文件
     *
     * @param records 借阅记录数组
     * @param fileName 目标文件名
     */
    public void storeObject(PairOfUidAndBookId[] records, String fileName) {
        // ① 统计有效记录数量
        int validCount = 0;
        for (PairOfUidAndBookId record : records) {
            if (record != null) {
                validCount++;
            }
        }

        // ② 构建字符串
        StringBuilder jsonBuilder = new StringBuilder();
        for (int i = 0; i < validCount; i++) {
            PairOfUidAndBookId record = records[i];
            if (record != null) {
                jsonBuilder.append(record.toJson());
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
     * 从文件读取并重建借阅记录数组
     *
     * @param fileName 源文件名
     * @return 重建的借阅记录数组
     */
    public PairOfUidAndBookId[] loadObject(String fileName) {
        // ① 读取文件内容
        String content = FileUtils.readFile(fileName);

        if (content == null || content.isEmpty()) {
            System.out.println("📭 借阅记录文件为空或不存在：" + fileName);
            return null;
        }

        // ② 按换行符分割
        String[] recordStrings = content.split("\n");

        // ③ 解析每一行
        PairOfUidAndBookId[] records = new PairOfUidAndBookId[recordStrings.length];
        for (int i = 0; i < recordStrings.length; i++) {
            records[i] = parseRecordJson(recordStrings[i]);
        }

        return records;
    }

    // ==================== 私有解析方法 ====================

    /**
     * 解析单行 JSON 字符串，重建 PairOfUidAndBookId 对象
     *
     * 字段顺序（必须与 PairOfUidAndBookId.toJson() 保持一致）：
     * userId, bookId
     */
    private PairOfUidAndBookId parseRecordJson(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }

        // 按逗号分割
        String[] fields = json.split(",");

        if (fields.length < 2) {
            System.out.println("⚠️ 借阅记录格式错误：" + json);
            return null;
        }

        // 解析字段
        int userId = Integer.parseInt(fields[0]);
        int bookId = Integer.parseInt(fields[1]);

        // 构造对象
        return new PairOfUidAndBookId(userId, bookId);
    }
}