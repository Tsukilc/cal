import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2025/3/21
 */
public class CalTest {

    // 基础运算测试
    @Test
    void testBasicOperations() {
        assertEquals("3", Cal.evaluateExpression("1 + 2"));
        assertEquals("1/2", Cal.evaluateExpression("3/4 - 1/4"));
        assertEquals("3/4", Cal.evaluateExpression("1/2 * 3/2"));
        assertEquals("2", Cal.evaluateExpression("4/3 / 2/3"));
    }

    // 分数约简测试
    @Test
    void testSimplification() {
        assertEquals("1/2", Cal.evaluateExpression("2/4"));
        assertEquals("3", Cal.evaluateExpression("6/2"));
        assertEquals("5/7", Cal.evaluateExpression("10/14"));
    }

    // 错误处理测试
    @Test
    void testErrorCases() {
        // 括号不匹配
        assertEquals("Error", Cal.evaluateExpression("(1 + 2"));
    }


    // 嵌套运算测试
    @Test
    void testNestedOperations() {
        assertEquals("9/16", Cal.evaluateExpression("(1/2 + (3/4 - 1/8)) * 1/2"));
        assertEquals("1", Cal.evaluateExpression("(1)"));
    }
}