package testSamplesGenerator;

import domain.constrictions.Constriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.DayBannedConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.DifferentDayConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.SameDayConstriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.TimeDisplacementConstriction;
import domain.entities.Exam;
import geneticAlgorithm.output.ExcelWriter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;


public class SamplesGenerator {

    public static int idCounter = 0;
    public final static int optatives = 9;
    private final static int repetitions = 50;

    private final static ArrayList<Exam> result = new ArrayList<>();
    private final static Random generator = new Random();
    
    private final static HashMap<Duration, Double> examDurations = new HashMap<>();
    private final static HashMap<Duration, Double> examExtraTimes = new HashMap<>();
    
    
    private final static HashMap<String, List<Constriction>> constrictions = new HashMap<>();
    private static List<LocalDate> calendar;

    private final static HashMap<String, Integer> constrictionAmount = new HashMap<>();




    private final static List<ExamPair> sameDayPairs = new ArrayList<>();
    private final static List<ExamPair> differentDayPairs = new ArrayList<>();



    public static void main(String[] args) {
        generator();
    }

    public static void initialer() {
        initializeExamDurationProbabilities();
        initializeExamExtraTimesProbabilities();
    }

    private static void initializeCalendar() {
        long finalSize = repetitions * (optatives + 6 + 5);

        LocalDate currentDate = LocalDate.now();
        calendar = new ArrayList<>();
        calendar.add(currentDate);
        for (int i = 1; i < finalSize/4; i++) {
            currentDate = currentDate.plusDays(1);
            calendar.add(currentDate);
        }
    }

    private static void initializeConstrictionAmount() {
        constrictionAmount.put("DB", result.size());
        constrictionAmount.put("SD", result.size());
        constrictionAmount.put("DD", result.size() / 2);
        constrictionAmount.put("TD", result.size() / 2);
        constrictionAmount.put("OE", result.size() / 2);
    }

    private static void initializeExamExtraTimesProbabilities() {
        examExtraTimes.put(Duration.ZERO, 0.3);
        examExtraTimes.put(Duration.ofMinutes(10), 0.6);
        examExtraTimes.put(Duration.ofMinutes(15), 0.1);
    }

    private static void initializeExamDurationProbabilities() {
        double divisor = 60.0;
        examDurations.put(Duration.ofMinutes(30), 9/divisor);
        examDurations.put(Duration.ofMinutes(60), 12/divisor);
        examDurations.put(Duration.ofMinutes(90), 3/divisor);
        examDurations.put(Duration.ofMinutes(120), 8/divisor);
        examDurations.put(Duration.ofMinutes(150), 12/divisor);
        examDurations.put(Duration.ofMinutes(180), 14/divisor);
        examDurations.put(Duration.ofMinutes(210), 2/divisor);
    }

    public static void generator() {

        // Iterar la entrada
        initialer();
        for (int i = 0; i < repetitions; i++) {
            for (int j = 1; j <= 3; j++) {
                generateRandomExam(j, 1, "Teórico");
                generateRandomExam(j, 2, "Práctico");
            }
            for (int j = 0; j < optatives; j++) {
                generateRandomExam(0, 1, "Práctico");
            }
            generateRandomExam(4, 1, "Teórico");
            generateRandomExam(4, 1, "Teórico");
            generateRandomExam(4, 1, "Teórico");
            generateRandomExam(4, 2, "Teórico");
            generateRandomExam(4, 2, "Teórico");
        }

        initializeCalendar();
        initializeConstrictionAmount();
        generateConstrictions();

        ExcelWriter.parseExamListToExcel("files/testFilesGenerated/", "test.xslx", 1, result,
                constrictions, calendar);


    }

    private static void generateConstrictions() {

        generateDaysBanned();
        generateSameDays();
        generateDifferentDays();
        generateTimeDisplacements();
        //generateOrders();



    }

    private static void generateTimeDisplacements() {
        constrictions.put("TD", new ArrayList<>());
        for (int i = 0; i < constrictionAmount.get("TD"); i++) {
            Exam exam;
            Exam exam2;
            ExamPair exPair;
            do {
                exam = result.get(generator.nextInt(result.size()));
                exam2 = exam;
                while (exam.equals(exam2)) {
                    exam2 = result.get(generator.nextInt(result.size()));
                }
                exPair = new ExamPair(exam, exam2);
            }while (checkPairRegistered(exPair, sameDayPairs) || checkPairRegistered(exPair, differentDayPairs));


            TimeDisplacementConstriction tdC = new TimeDisplacementConstriction(exam, exam2,
                    generator.nextInt(5), calendar);
            differentDayPairs.add(exPair);
            constrictions.get("TD").add(tdC);

        }

    }

    private static void generateDifferentDays() {
        constrictions.put("DD", new ArrayList<>());
        for (int i = 0; i < constrictionAmount.get("DD"); i++) {
            Exam exam;
            Exam exam2;
            ExamPair exPair;
            do {
                exam = result.get(generator.nextInt(result.size()));
                exam2 = exam;
                while (exam.equals(exam2)) {
                    exam2 = result.get(generator.nextInt(result.size()));
                }
                exPair = new ExamPair(exam, exam2);
            }while (checkPairRegistered(exPair, sameDayPairs));

            DifferentDayConstriction ddC = new DifferentDayConstriction(exam, exam2);
            constrictions.get("DD").add(ddC);

        }
    }

    private static void generateSameDays() {
        constrictions.put("SD", new ArrayList<>());
        for (int i = 0; i < constrictionAmount.get("SD"); i++) {
            Exam exam;
            Exam exam2;
            ExamPair exPair;
            do {
                exam = result.get(generator.nextInt(result.size()));
                exam2 = exam;
                while (exam.equals(exam2)) {
                    exam2 = result.get(generator.nextInt(result.size()));
                }
                exPair = new ExamPair(exam, exam2);
            }while (checkPairRegistered(exPair, sameDayPairs));

            SameDayConstriction sdC = new SameDayConstriction(exam, exam2);
            constrictions.get("SD").add(sdC);
            sameDayPairs.add(exPair);


        }
    }

    private static boolean checkPairRegistered(ExamPair exams, List<ExamPair> sameDayPairs) {
        return sameDayPairs.contains(exams);
    }

    private static void generateDaysBanned() {
        constrictions.put("DB", new ArrayList<>());
        for (int i = 0; i < constrictionAmount.get("DB"); i++) {
            banRandomDayForRandomExam();
        }
    }

    private static void banRandomDayForRandomExam() {
        Exam exam = result.get(generator.nextInt(result.size()));
        LocalDate bannedDay = calendar.get(generator.nextInt(calendar.size()));
        constrictions.get("DB").add(new DayBannedConstriction(exam, bannedDay));
        
    }

    private static void generateRandomExam(int course, int semester, String content) {
        Exam exam = new Exam(course, semester, generateCode(),
                generateAcronym(), generateSubject(), 1,
                content, "presencial", generator.nextInt(90) + 40, generateRandomDuration().toMinutes(),
                null, null, generateRandomExtraTime(), generateRandomNumericalComplexity(), idCounter);
        idCounter++;
        result.add(exam);

    }

    private static int generateRandomNumericalComplexity() {
        return 0;
    }

    private static Duration generateRandomExtraTime() {
        return getDurationFromHashMap(examExtraTimes);

    }

    private static Duration getDurationFromHashMap(HashMap<Duration, Double> test) {
        double p = Math.random();
        double cumulativeProbability = 0.0;
        Duration defResult = null;
        for (Map.Entry<Duration, Double> item : test.entrySet()) {
            cumulativeProbability += item.getValue();
            defResult = item.getKey();
            if (p <= cumulativeProbability) {
                return item.getKey();
            }
        }
        return defResult;
    }

    private static Duration generateRandomDuration() {
        return getDurationFromHashMap(examDurations);
    }

    private static String generateSubject() {
        return "Subject:" + idCounter;
    }

    private static String generateAcronym() {
        return "Acro:" + idCounter;
    }

    private static String generateCode() {
        return "Code:" + idCounter;
    }
}
