import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    private static final double probabilityOfChosenCorrectAnswerByFriend = 0.7;

    public static void main(String[] args) throws FileNotFoundException {
        Quiz();

    }

    private static void Quiz() throws FileNotFoundException {
        File folder = new File("src/main/resources/quiz/");
        File[] categories = folder.listFiles();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Available categories");
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i+1) + ". " + categories[i].getName().replace(".txt", ""));
        }
        System.out.println('\n' + "Write category number: ...");
        int category = 0;
        do {
            category = scanner.nextInt();
            if (category < 1 || category > categories.length) System.out.println("Choose between 1 to " + categories.length);
        }while(category < 1 || category > categories.length);
        System.out.println("You have chosen category:    " + categories[category-1].toString().substring(24).replace(".txt","") + '\n');
        List<QuizClass> listOfQuestionsFromChosenCategory = uploadQuestions(categories[category-1]);
        System.out.println("You answered correctly on " + AnsweringQuestions(listOfQuestionsFromChosenCategory) + " answers on 10 possible");
    }

    private static int AnsweringQuestions(List<QuizClass> listOfQuestions){
        int sumOfCorrectAnswers = 0;
        Scanner scanner = new Scanner(System.in);
        String[] answers = {"a","b","c","d"};
        System.out.println("Answer a b c or d ; in case of lifeline write: 'fiftyfifty' 'phone' or 'public'" + '\n');
        String userAnswers = "";
        List<Integer> alreadyOccured = new ArrayList<>();
        boolean counterFiftyFifty = true;
        boolean counterPublic = true;
        boolean counterPhone = true;

        for (int i = 0; i < 10; i++) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, listOfQuestions.size()-1);
            while(alreadyOccured.contains(randomNum)) randomNum = ThreadLocalRandom.current().nextInt(0, listOfQuestions.size()-1);
            alreadyOccured.add(randomNum);

            final QuizClass question = listOfQuestions.get(randomNum);
            String[] possibleAnswers = question.getAnswers();

            System.out.println((i+1) + ". " + question.getQuestion() + " :");
            String correctAnswer = possibleAnswers[0];
            Collections.shuffle(Arrays.asList(possibleAnswers));

            for (int j = 0; j < possibleAnswers.length; j++) System.out.println(answers[j] + ") " + Arrays.asList(possibleAnswers[j]));

            userAnswers = scanner.nextLine();

            if (userAnswers.equals("public") && counterPublic){
                for (int j = 0; j < possibleAnswers.length; j++) if (possibleAnswers[j].equals(correctAnswer)) System.out.println(answers[j] + ") " + Arrays.asList(possibleAnswers[j]));
                System.out.println("Answer now...:" + '\n');
                userAnswers = scanner.nextLine();
                counterPublic = false;
            }

            if(userAnswers.equals("fiftyfifty") && counterFiftyFifty){
                possibleAnswers = fiftyFifty(correctAnswer,possibleAnswers,answers);
                System.out.println("Answer now...:" + '\n');
                userAnswers = scanner.nextLine();
                counterFiftyFifty = false;
            }

            if(userAnswers.equals("phone") && counterPhone){
                String friendAnswer = phone(correctAnswer,possibleAnswers);
                for (int j = 0; j < possibleAnswers.length; j++) if (possibleAnswers[j].equals(friendAnswer)) System.out.println(answers[j] + ") " + Arrays.asList(possibleAnswers[j]));
                System.out.println("Answer now...:" + '\n');
                userAnswers = scanner.nextLine();
                counterPhone = false;
            }

            int index = userAnswers.charAt(0)-97;

            while(index < 0 || index >= possibleAnswers.length){
                System.out.println("Answer a b c or d");
                userAnswers = scanner.nextLine();
                index = userAnswers.charAt(0)-97;
            }
            if (possibleAnswers[index].equals(correctAnswer)) {
                System.out.println("Correct!");
                sumOfCorrectAnswers++;
            }
            else System.out.println("correct answer is: " + correctAnswer);
            System.out.println('\n');
        }

        return sumOfCorrectAnswers;
    }

    private static String phone(String correctAnswer, String[] possibleAnswers){
        String badAnswer = possibleAnswers[0];
        if (badAnswer.equals(correctAnswer)) badAnswer = possibleAnswers[1];
        int answerFromFriend = ThreadLocalRandom.current().nextInt(0, 100);
        if (answerFromFriend < 100 * probabilityOfChosenCorrectAnswerByFriend) return correctAnswer;
        return badAnswer;
    }

    private static String[] fiftyFifty(String correctAnswer, String[] possibleAnswers, String[] answers){
        int newSize = possibleAnswers.length / 2;
        Collections.shuffle(Arrays.asList(possibleAnswers));
        String[] workArray = new String[newSize];
        boolean containCorrect = false;
        for (int i = 0; i < newSize; i++) {
            workArray[i] = possibleAnswers[i];
            if (workArray[i].equals(correctAnswer)) containCorrect = true;
        }
        if (!containCorrect) workArray[0] = correctAnswer;
//        System.out.println(Arrays.toString(tablica));

        Collections.shuffle(Arrays.asList(workArray));

        for (int i = 0; i < workArray.length; i++) System.out.println(answers[i] + ") " + workArray[i]);
        return workArray;
    }


    private static List<QuizClass> uploadQuestions(File file) throws FileNotFoundException {
        Scanner scanner = new Scanner(file);
        List<QuizClass> listOfQuestions = new ArrayList<>();
        while (scanner.hasNextLine()){
            String question = scanner.nextLine();
            int countAnswers = Integer.parseInt(scanner.nextLine()); // scanner.nextInt();
            String[] workAnswersArray = new String[countAnswers];
            for (int i = 0; i < countAnswers; i++) {
                workAnswersArray[i] = scanner.nextLine();
            }
            QuizClass tempQuestion = new QuizClass(question,workAnswersArray);
            listOfQuestions.add(tempQuestion);
        }
        return listOfQuestions;
    }
}
