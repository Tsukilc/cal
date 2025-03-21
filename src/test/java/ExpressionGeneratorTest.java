import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Description:
 * @Author: zbj
 * @Date: 2025/3/21
 */

public class ExpressionGeneratorTest {

    @Test
    public void testGenerateExpression() {
        int numOperations = 3;
        int rangeLimit = 10;

        for (int i = 0; i < 100; i++) {
            String expression = ExpressionGenerator.generateExpression(numOperations, rangeLimit);
            assertNotNull(expression, "Expression should not be null");
            assertFalse(expression.isEmpty(), "Expression should not be empty");


            // Check that the expression contains the correct number of operators
            long operatorCount = expression.chars().filter(ch -> "+-*/".indexOf(ch) >= 0).count();
            assertEquals(numOperations, operatorCount, "Expression should contain the correct number of operators");
        }
    }
}