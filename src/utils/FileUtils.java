package utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 文件读写工具类
 *
 * 提供简单的文件读取和写入功能
 * 为上层的 AnalyzingBook 和 AnalyzingBorrowedBook 提供基础支持
 */
public class FileUtils {

    /**
     * 读取文件内容
     *
     * @param fileName 文件名（相对路径）
     * @return 文件内容，如果文件不存在或读取失败返回 null
     */
    public static String readFile(String fileName) {
        StringBuilder content = new StringBuilder();

        // try-with-resources 语法糖：自动关闭资源
        try (BufferedReader reader = new BufferedReader(
                new FileReader(fileName))) {

            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }

            // 移除最后一个多余的换行符
            if (content.length() > 0) {
                content.setLength(content.length() - 1);
            }

            return content.toString();

        } catch (IOException e) {
            System.out.println("⚠️ 读取文件失败：" + fileName + " - " + e.getMessage());
            return null;
        }
    }

    /**
     * 写入文件内容（覆盖写入）
     *
     * @param content 要写入的内容
     * @param fileName 文件名（相对路径）
     * @return 写入是否成功
     */
    public static boolean writeFile(String content, String fileName) {
        // try-with-resources 语法糖：自动关闭资源
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(fileName, false))) {  // false = 覆盖写入

            writer.write(content);
            return true;

        } catch (IOException e) {
            System.out.println("⚠️ 写入文件失败：" + fileName + " - " + e.getMessage());
            return false;
        }
    }
}