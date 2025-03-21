import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    static String exerciseFile = "Exercises.txt";
    static String answerFile = "";

    public static void generateQuestionsAndAnswers(int numQuestions, int rangeLimit) {
        List<String> questions = new ArrayList<>();
        List<String> answers = new ArrayList<>();
        Set<String> uniqueQuestions = new HashSet<>();  // 用集合存储，确保题目唯一

        while (questions.size() < numQuestions) {
            String expr = ExpressionGenerator.generateExpression(3, rangeLimit);
            String answer = Cal.evaluateExpression(expr);

            // 确保题目不重复
            if (!uniqueQuestions.contains(expr)) {
                questions.add(expr + " =");
                answers.add(answer);
                uniqueQuestions.add(expr); // 标记题目为已生成
            }
        }

        writeToFiles(questions, answers);
    }


    // 将题目和答案写入文件
    public static void writeToFiles(List<String> questions, List<String> answers) {
        try (BufferedWriter fQuestions = new BufferedWriter(new FileWriter("Exercises.txt"));
             BufferedWriter fAnswers = new BufferedWriter(new FileWriter("Answers.txt"))) {

            for (String question : questions) {
                fQuestions.write(question);
                fQuestions.newLine();
            }
            for (String answer : answers) {
                fAnswers.write(answer);
                fAnswers.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 评分功能
    public static void gradeAnswers(String exerciseFile, String answerFile) {
        try (BufferedReader fQuestions = new BufferedReader(new FileReader(exerciseFile));
             BufferedReader fAnswers = new BufferedReader(new FileReader(answerFile))) {

            List<String> questions = new ArrayList<>();
            List<String> answers = new ArrayList<>();
            String line;

            // 读取题目文件
            while ((line = fQuestions.readLine()) != null) {
                questions.add(line.trim().replace(" =", "").replaceAll(" ", ""));  // 去掉空格和 "="
            }

            // 读取答案文件
            while ((line = fAnswers.readLine()) != null) {
                answers.add(line.trim().replaceAll(" ", ""));  // 去掉空格
            }

            // 存储正确和错误的题目索引
            List<Integer> correct = new ArrayList<>();
            List<Integer> wrong = new ArrayList<>();

            // 遍历每一道题目，计算结果并与答案文件中的答案进行对比
            for (int i = 0; i < questions.size(); i++) {
                // 计算每一道题目的正确答案
                String correctAnswer = Cal.evaluateExpression(questions.get(i));

                // 如果计算结果与答案文件中的答案一致，则是正确的
                if (correctAnswer.equals(answers.get(i))) {
                    correct.add(i + 1);
                } else {
                    wrong.add(i + 1);
                }
            }

            // 输出评分结果到文件
            try (BufferedWriter fGrade = new BufferedWriter(new FileWriter("Grade.txt"))) {
                fGrade.write("Correct: " + correct.size() + " (" + String.join(", ", correct.stream().map(String::valueOf).toArray(String[]::new)) + ")");
                fGrade.newLine();
                fGrade.write("Wrong: " + wrong.size() + " (" + String.join(", ", wrong.stream().map(String::valueOf).toArray(String[]::new)) + ")");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        int numQuestions = 10000;
        int rangeLimit = 10;

        // 检查参数数量是否正确
        if (args.length < 3) {
            System.out.println("错误：缺少必要的命令行参数，讲启用默认值生成");
            System.out.println("用法示例: java -jar ArithmeticGenerator.jar -n 10 -r 100 -e Exercises.txt -a Answers.txt");
            System.out.println("参数说明:");
            System.out.println("  -n [生成题目的个数]  // 生成的题目数量");
            System.out.println("  -r [题目中数值的范围]  // 题目中数值的最大值");
            System.out.println("  -e [题目文件名]       // 存储生成题目的文件名");
            System.out.println("  -a [答案文件名]       // 存储答案的文件名");
        }

        // 解析命令行参数
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-n":
                    try {
                        numQuestions = Integer.parseInt(args[i + 1]);
                    } catch (NumberFormatException e) {
                        System.out.println("错误：'-n' 后应跟一个整数值。");
                        return;
                    }
                case "-r":
                    try {
                        rangeLimit = Integer.parseInt(args[i + 1]);
                    } catch (NumberFormatException e) {
                        System.out.println("错误：'-r' 后应跟一个整数值。");
                        return;
                    }
                case "-e":
                    exerciseFile = args[i + 1];
                case "-a":
                    answerFile = args[i + 1];
            }
        }

        // 生成题目和答案，并写入文件
        if(answerFile.isEmpty()) {
            generateQuestionsAndAnswers(numQuestions, rangeLimit);
        }else{
            gradeAnswers(exerciseFile, answerFile);
        }
    }
}
