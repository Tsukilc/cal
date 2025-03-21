import java.math.BigInteger;
import java.util.Stack;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2025/3/21
 */
public class Cal {

    // 辅助方法：检查字符是否是数字
    private static boolean isDigit(char ch) {
        return ch >= '0' && ch <= '9';
    }

    // 解析和计算分数
    public static String evaluateExpression(String expr) {
        try {
            // 去掉所有空格
            expr = expr.replaceAll(" ", "");

            // 使用栈来处理表达式
            Stack<BigInteger> numerators = new Stack<>();  // 分子栈
            Stack<BigInteger> denominators = new Stack<>();  // 分母栈
            Stack<Character> operators = new Stack<>();  // 操作符栈

            int i = 0;
            while (i < expr.length()) {
                char c = expr.charAt(i);

                if (isDigit(c)) {
                    // 解析数字
                    StringBuilder numBuilder = new StringBuilder();
                    while (i < expr.length() && isDigit(expr.charAt(i))) {
                        numBuilder.append(expr.charAt(i++));
                    }
                    BigInteger numerator = new BigInteger(numBuilder.toString());
                    BigInteger denominator = BigInteger.ONE;

                    // 处理分数形式：如果有 / 处理分母
                    if (i < expr.length() && expr.charAt(i) == '/') {
                        i++;  // 跳过 '/'
                        numBuilder.setLength(0);  // 清空数字构建器
                        while (i < expr.length() && isDigit(expr.charAt(i))) {
                            numBuilder.append(expr.charAt(i++));
                        }
                        denominator = new BigInteger(numBuilder.toString());
                    }

                    // 将解析出的数字和分数压栈
                    numerators.push(numerator);
                    denominators.push(denominator);

                } else if (c == '(') {
                    // 处理左括号，直接压栈
                    operators.push(c);
                    i++;

                } else if (c == ')') {
                    // 处理右括号，进行运算直到遇到左括号
                    while (operators.peek() != '(') {
                        performOperation(numerators, denominators, operators);
                    }
                    operators.pop();  // 弹出 '('
                    i++;

                } else if (isOperator(c)) {
                    // 处理运算符
                    while (!operators.isEmpty() && precedence(c) <= precedence(operators.peek())) {
                        performOperation(numerators, denominators, operators);
                    }
                    operators.push(c);
                    i++;
                }
            }

            // 执行所有剩余的运算
            while (!operators.isEmpty()) {
                performOperation(numerators, denominators, operators);
            }

            // 最终的结果是栈顶的分子和分母
            BigInteger finalNumerator = numerators.pop();
            BigInteger finalDenominator = denominators.pop();

            // 简化分数
            BigInteger gcd = finalNumerator.gcd(finalDenominator);
            finalNumerator = finalNumerator.divide(gcd);
            finalDenominator = finalDenominator.divide(gcd);

            // 返回最简分数形式
            if (finalDenominator.equals(BigInteger.ONE)) {
                return finalNumerator.toString();  // 整数形式
            } else {
                return finalNumerator + "/" + finalDenominator;
            }

        } catch (Exception e) {
            return "Error";  // 捕获错误
        }
    }

    // 判断字符是否是操作符
    private static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    // 获取操作符优先级
    private static int precedence(char operator) {
        if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/') {
            return 2;
        }
        return 0;
    }

    // 执行运算
    private static void performOperation(Stack<BigInteger> numerators, Stack<BigInteger> denominators, Stack<Character> operators) {
        BigInteger numerator2 = numerators.pop();
        BigInteger denominator2 = denominators.pop();
        BigInteger numerator1 = numerators.pop();
        BigInteger denominator1 = denominators.pop();
        char operator = operators.pop();

        BigInteger resultNumerator = BigInteger.ZERO;
        BigInteger resultDenominator = BigInteger.ONE;

        switch (operator) {
            case '+':
                // a/b + c/d = (ad + bc) / bd
                resultNumerator = numerator1.multiply(denominator2).add(numerator2.multiply(denominator1));
                resultDenominator = denominator1.multiply(denominator2);
                break;
            case '-':
                // a/b - c/d = (ad - bc) / bd
                resultNumerator = numerator1.multiply(denominator2).subtract(numerator2.multiply(denominator1));
                resultDenominator = denominator1.multiply(denominator2);
                break;
            case '*':
                // a/b * c/d = ac / bd
                resultNumerator = numerator1.multiply(numerator2);
                resultDenominator = denominator1.multiply(denominator2);
                break;
            case '/':
                // a/b / c/d = ad / bc
                resultNumerator = numerator1.multiply(denominator2);
                resultDenominator = denominator1.multiply(numerator2);
                break;
        }

        // 压栈结果
        numerators.push(resultNumerator);
        denominators.push(resultDenominator);
    }
}
