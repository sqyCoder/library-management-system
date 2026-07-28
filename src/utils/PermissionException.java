package utils;

/**
 * 权限异常类
 *
 * 继承 RuntimeException 成为非受检异常
 * 这样调用方不需要强制 try-catch，
 * 但可以选择捕获处理
 */
public class PermissionException extends RuntimeException {

    // 无参构造方法
    public PermissionException() {
        super();
    }

    // 带错误信息的构造方法
    public PermissionException(String message) {
        super(message);
    }
}