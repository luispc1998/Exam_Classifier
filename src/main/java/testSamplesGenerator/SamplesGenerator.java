package testSamplesGenerator;

import domain.constrictions.Constriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.*;
import domain.entities.Exam;
import domain.entities.Interval;
import geneticAlgorithm.output.ExcelWriter;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;


public class SamplesGenerator {

    // Configurations
    public static int idCounter = 0;
    public final static int optatives = 9;
    private final static int repetitions = 1;
    private final static boolean hardsEnabled = true;
    private final static boolean extraTimeEnabled = false;
    private final static int rounds_per_rep = 2;
    private final static int max_round_size = 3;
    // End of configurations



    private final static ArrayList<Exam> result = new ArrayList<>();
    private static ArrayList<Exam> freeExams;
    private final static Random generator = new Random();
    
    private final static HashMap<Duration, Double> examDurations = new HashMap<>();
    private final static HashMap<Duration, Double> examExtraTimes = new HashMap<>();
    private final static HashMap<String, Integer> rounds = new HashMap<>();
    
    
    private final static HashMap<String, List<Constriction>> constrictions = new HashMap<>();
    private static List<LocalDate> calendar;

    private final static HashMap<String, Integer> constrictionAmount = new HashMap<>();




    private final static List<ExamPair> sameDayPairs = new ArrayList<>();
    private final static List<ExamPair> orderedPairs = new ArrayList<>();



    public static void main(String[] args) {
        generator();
    }

    public static void initialer() {
        initializeExamDurationProbabilities();
        initializeExamExtraTimesProbabilities();
    }

    private static void initializeRounds() {
        Random generator = new Random();
        for (int i = 0; i < repetitions * rounds_per_rep; i++) {
            rounds.put(String.valueOf(i), 2 + generator.nextInt(max_round_size-2));
        }

        for (Map.Entry<String, Integer> entry :rounds.entrySet()) {
            List<Exam> toBePaired = new ArrayList<>();
            for (int i = 0; i < entry.getValue(); i++) {

                Exam exam = result.get(generator.nextInt(result.size()));

                while (exam.getRoundId() != null){
                    exam = result.get(generator.nextInt(result.size()));
                }

                exam.setRoundId(entry.getKey());
                toBePaired.add(exam);
            }
            pairExamsOnDay(toBePaired);
        }
    }

    private static void pairExamsOnDay(List<Exam> toBePaired) {
        for (int i = 0; i < toBePaired.size()-1; i++) {
            for (int j = i; j < toBePaired.size(); j++) {
                ExamPair exPair = new ExamPair(toBePaired.get(i), toBePaired.get(j));
                sameDayPairs.add(exPair);
            }
        }
    }

    private static void initializeCalendar() {
        long finalSize = repetitions * 17;

        LocalDate currentDate = LocalDate.now();
        calendar = new ArrayList<>();
        calendar.add(currentDate);
        for (int i = 1; i < finalSize; i++) {
            currentDate = currentDate.plusDays(1);
            calendar.add(currentDate);
        }
    }

    private static void initializeConstrictionAmount() {

        constrictionAmount.put("DB", randomConstrictionNumber(result.size() / 6, result.size() / 3));
        constrictionAmount.put("SD", randomConstrictionNumber(result.size() / 6, result.size() / 3));
        constrictionAmount.put("DD", randomConstrictionNumber(result.size() / 6, result.size() / 3));
        constrictionAmount.put("TD", randomConstrictionNumber(result.size() / 6, result.size() / 3));
        constrictionAmount.put("OE", randomConstrictionNumber(result.size() / 6, result.size() / 3));

    }

    private static int randomConstrictionNumber(int minimun, int maximum) {
        Random generator = new Random();
        return minimun + generator.nextInt(maximum-minimun);

    }

    private static void initializeExamExtraTimesProbabilities() {
        if (extraTimeEnabled) {
            examExtraTimes.put(Duration.ZERO, 0.3);
            examExtraTimes.put(Duration.ofMinutes(10), 0.6);
            examExtraTimes.put(Duration.ofMinutes(15), 0.1);
        }
        else{
            examExtraTimes.put(Duration.ZERO, 1.0);
        }
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

            for (int k = 0; k < 3; k++) { // Cursos
                for (int j = 1; j <= 10; j++) { // Asignaturas por curso a ojo
                    generateRandomExam(j, 1, "Teórico");
                    generateRandomExam(j, 2, "Práctico");
                }
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

        freeExams = new ArrayList<>(result);

        initializeRounds();
        initializeCalendar();
        initializeConstrictionAmount();
        generateConstrictions();


        ExcelWriter.parseExamListToExcel("files/testFilesGenerated/", "test.xslx", 1, result,
                constrictions, getDefaultCalendarTimeInterval(calendar));


    }

    private static HashMap<LocalDate, Interval> getDefaultCalendarTimeInterval(List<LocalDate> dates) {
        HashMap<LocalDate, Interval> realCalendar = new HashMap<>();
        Interval defaultInterval = new Interval(LocalTime.of(9,0), LocalTime.of(21,0));
        for (LocalDate date: dates) {
            realCalendar.put(date, defaultInterval);
        }
        return realCalendar;
    }

    private static void generateConstrictions() {

        generateDaysBanned();
        generateSameDays();
        generateDifferentDays();
        generateTimeDisplacements();
        generateOrders();

        constrictionAmount.put("DI", freeExams.size() / 2);
        generateDayIntervals();


    }

    private static void generateDayIntervals() {
        constrictions.put("DI", new ArrayList<>());

        for (int i = 0; i < constrictionAmount.get("DI"); i++) {
            Exam exam;
            exam = freeExams.get(generator.nextInt(freeExams.size()));

            int index = generator.nextInt(calendar.size()-1);
            LocalDate start = calendar.get(index);
            int additionIndex = generator.nextInt(calendar.size()-index);
            LocalDate end = calendar.get(index + additionIndex);
            DayIntervalConstriction diC = new DayIntervalConstriction(exam,start, end, calendar);
            freeExams.remove(exam);

            constrictions.get("DI").add(diC);
            handleHards(diC);
        }
    }

    private static void generateOrders() {
        constrictions.put("OE", new ArrayList<>());

        for (int i = 0; i < constrictionAmount.get("OE"); i++) {
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

            OrderExamsConstriction oeC;
            if (checkPairRegistered(exPair, orderedPairs)) {
                int registeredPairIndex = orderedPairs.indexOf(exPair);
                ExamPair registeredPair = orderedPairs.get(registeredPairIndex);
                oeC = new OrderExamsConstriction(registeredPair.getExam1(),
                        registeredPair.getExam2());
            }
            else{
                oeC = new OrderExamsConstriction(exam, exam2);
            }

            freeExams.remove(exam);
            freeExams.remove(exam2);
            orderedPairs.add(exPair);
            constrictions.get("OE").add(oeC);
            handleHards(oeC);
        }
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
            }while (checkPairRegistered(exPair, sameDayPairs) || checkPairRegistered(exPair, orderedPairs));


            TimeDisplacementConstriction tdC = new TimeDisplacementConstriction(exam, exam2,
                    generator.nextInt(5), calendar);
            freeExams.remove(exam);
            freeExams.remove(exam2);
            orderedPairs.add(exPair);
            constrictions.get("TD").add(tdC);
            handleHards(tdC);
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
            //freeExams.remove(exam);
            //freeExams.remove(exam2);
            handleHards(ddC);
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
            freeExams.remove(exam);
            freeExams.remove(exam2);
            constrictions.get("SD").add(sdC);
            handleHards(sdC);
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
        UserConstriction uc = new DayBannedConstriction(exam, bannedDay);
        constrictions.get("DB").add(uc);
        handleHards(uc);
        freeExams.remove(exam);
        
    }

    private static void handleHards(UserConstriction uc) {
        if (hardsEnabled) {
            Random generator = new Random();
            if (generator.nextDouble() < 0.3) {
                uc.hardify();
            }
        }
    }

    private static void generateRandomExam(int course, int semester, String content) {
        Exam exam = new Exam(course, semester, generateCode(),
                generateAcronym(), generateSubject(), 1,
                content, "presencial", generator.nextInt(90) + 40, generateRandomDuration().toMinutes(),
                null, null, generateRandomExtraTime(), generateRandomNumericalComplexity(), idCounter, null);
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
