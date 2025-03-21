import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2025/3/21
 */
public class ExpressionGenerator {

    private final static Random random = new Random();
    private static final int MAX_OPERATORS = 3; // 最大运算符数量
    private static final double BRACKET_PROBABILITY = 0.7; // 括号出现的概率


    // 生成表达式的方法
    public static String generateExpression(int numOperations, int rangeLimit) {
        if (numOperations > MAX_OPERATORS) {
            throw new IllegalArgumentException("运算符数量不能超过 " + MAX_OPERATORS);
        }

        StringBuilder expression = new StringBuilder();
        ArrayList<String> operators = new ArrayList<>(Arrays.asList("+", "-", "*", "/"));

        // 生成第一个操作数
        int operand = random.nextInt(rangeLimit);
        expression.append(operand);

        // 生成后续的操作数和运算符
        for (int i = 0; i < numOperations; i++) {
            // 随机选择运算符
            String operator = operators.get(random.nextInt(operators.size()));
            // 随机选择下一个操作数
            operand = random.nextInt(rangeLimit);

            // 确保减法不会导致负数结果
            if (operator.equals("-") && operand > 0) {
                operand = random.nextInt(rangeLimit - 1);
            }

            // 确保除法结果为真分数
            if (operator.equals("/")) {
                int denominator = random.nextInt(rangeLimit - 1) + 1; // 避免除以零
                expression.append("/").append(denominator);
            } else {
                // 添加运算符和操作数到表达式
                expression.append(operator).append(operand);
            }

            // 随机决定是否在当前子表达式周围添加括号
            if (random.nextDouble() < BRACKET_PROBABILITY) {
                int insertPos = random.nextInt(expression.length());

                if (insertPos + 1 >= expression.length()){
                    continue;
                }

                char nextChar = expression.charAt(insertPos + 1);
                if ( nextChar == '*' || nextChar == '/' || isDigit(nextChar))
                    continue;
                expression.insert(insertPos, "(");
                expression.append(")");
            }
        }

        // 确保括号正确匹配
        String result = expression.toString().replaceAll("\\(\\s*\\*", "\\(\\*").replaceAll("\\(\\s*/", "\\(/");

        // 给result表达式加上空格
        result = result.replaceAll("\\+", " + ").replaceAll("-", " - ").replaceAll("\\*", " * ").replaceAll("/", " / ");


        return result;
    }

    private static boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

}
