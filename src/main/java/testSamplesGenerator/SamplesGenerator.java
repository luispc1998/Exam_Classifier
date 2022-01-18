package testSamplesGenerator;

import domain.constrictions.Constriction;
import domain.constrictions.types.weakConstriction.hardifiableConstrictions.*;
import domain.entities.Exam;
import domain.entities.Interval;
import domain.parsers.ConstrictionParser;
import domain.parsers.ExamParser;
import geneticAlgorithm.output.ExcelWriter;
import random.RandomGenerator;
import utils.Utils;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

/**
 * This generates instance of the problem according to the configurations specified by means of constants.
 */
public class SamplesGenerator {

    // Configurations
    /**
     * Number of optatives that the instance will have
     */
    public final static int optatives = 9;

    /**
     * Number of times that the situation at the school will be repeated.
     *
     * If the school has a mean of 62 exams, and we set 2 as repetitions, then the instance will have 124 exams.
     * The concrete formula is 74 * repetitions.
     */
    private final static int repetitions = 1;

    /**
     * States if there will be hard constraints among the generated user constraints.
     */
    private final static boolean hardsEnabled = true;

    /**
     * States if the extra time feature is enabled for the instance generation.
     */
    private final static boolean extraTimeEnabled = false;

    /**
     * States if the numerical complexity feature is enabled for the instance generation.
     */
    private final static boolean cnEnabled = false;

    /**
     * States the number of rounds per repetition.
     */
    private final static int rounds_per_rep = 2;

    /**
     * States the maximum round size.
     */
    private final static int max_round_size = 3;

    /**
     * Maximun number of exams that can be scheduled.
     */
    private static int max_exams_scheduled = 6;

    // End of configurations

    /**
     * Counter of the exams that will be assigned as a new id for a new exam.
     */
    public static int idCounter = 0;

    /**
     * The list containing the final exams of the instance.
     */
    private static ArrayList<Exam> result = new ArrayList<>();

    /**
     * A list which contains the exams that have no constraints associated to them.
     */
    private static ArrayList<Exam> freeExams;

    /**
     * A random object,
     */
    private final static Random generator = RandomGenerator.getGenerator();

    /**
     * A HashMap containing as keys possible durations of the exams, and as values the probability of such a duration.
     */
    private static HashMap<Duration, Double> examDurations = new HashMap<>();

    /**
     * A HashMap containing as keys possible extra times of the exams, and as values the probability of such a duration.
     */
    private final static HashMap<Duration, Double> examExtraTimes = new HashMap<>();

    /**
     * A HashMap containing as keys possible numerical complexities of the exams, and as values the probability of such a duration.
     */
    private static HashMap<Integer, Double> numericalComplexities = new HashMap<>();

    /**
     * A HashMap containing the rounds of the final instance, and as values the size of such round.
     */
    private static HashMap<String, Integer> rounds = new HashMap<>();

    /**
     * The final list of constrictions of the instance
     */
    private static HashMap<String, List<Constriction>> constrictions = new HashMap<>();

    /**
     * The final calendar of days of the instance.
     */
    private static List<LocalDate> calendar;

    /**
     * A HashMap containing the constriction ids as keys, and as value of the number of such constriction type in the final
     * instance.
     */
    private static HashMap<String, Integer> constrictionAmount = new HashMap<>();


    /**
     * Exam pairs that must take place on the same day.
     */
    private static List<ExamPair> sameDayPairs = new ArrayList<>();

    /**
     * Exam pair that must be on a certain order.
     */
    private static List<ExamPair> orderedPairs = new ArrayList<>();

    /**
     * Base directory in which the intances will be generated.
     */
    private final static String outputBaseDirectory = "files/testFilesGenerated";

    /**
     * Instances base name without suffix.
     */
    private final static String fileBaseName = "default_scenario";

    /**
     * Number of instances to be generated simultaneously.
     */
    private final static int instancesNumberToGenerate = 10;

    /**
     * Just generates an instance according to the provided data.
     * @param args No args needed.
     */
    public static void main(String[] args) {
        String subfolder = Utils.createDirectoryStringBasedOnHour();
        String path = outputBaseDirectory + "/" + subfolder;
        Utils.createDirectory(path);

        for (int i = 0; i < instancesNumberToGenerate; i++) {
            generator(path + "/", fileBaseName, i);
        }

    }

    /**
     * This method initializes some data structures.
     */
    public static void initialize() {
        initializeExamDurationProbabilities();
        initializeExamExtraTimesProbabilities();
        initializeExamNumericalComplexity();
    }

    /**
     * Generates the rounds ids and sizes and afterwards assigns them to the exam.
     */
    private static void initializeRounds() {
        Random generator = new Random();
        rounds = new HashMap<>();
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

    /**
     * Pairs a list of exams on the same day. Generates each possible combination pair.
     * @param toBePaired The list of exams to be paired.
     */
    private static void pairExamsOnDay(List<Exam> toBePaired) {
        for (int i = 0; i < toBePaired.size()-1; i++) {
            for (int j = i; j < toBePaired.size(); j++) {
                ExamPair exPair = new ExamPair(toBePaired.get(i), toBePaired.get(j));
                sameDayPairs.add(exPair);
            }
        }
    }

    /**
     * Initializes the calendar of days that the instance will have.
     *
     * <p>
     * Taking into account real life problem instances, the average calendar size is 17, so the final calendar size
     * will be 17 * {@code repetitions}.
     */
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

    /**
     * Initializes how many constrictions per type will be.
     */
    private static void initializeConstrictionAmount() {
        constrictionAmount = new HashMap<>();
        constrictionAmount.put("DB", randomConstrictionNumber(result.size() / 6, result.size() / 3)/2);
        constrictionAmount.put("SD", randomConstrictionNumber(result.size() / 6, result.size() / 3)/2);
        constrictionAmount.put("DD", randomConstrictionNumber(result.size() / 6, result.size() / 3)/2);
        constrictionAmount.put("TD", randomConstrictionNumber(result.size() / 6, result.size() / 3)/2);
        constrictionAmount.put("OE", randomConstrictionNumber(result.size() / 6, result.size() / 3)/2);
    }

    /**
     * Generates a random number considering the minimum and the maximum numbers provided.
     * @param minimun The minimum number of instances.
     * @param maximum The maximum number of instances.
     * @return A number between {@code minimum} and {@code maximum}.
     */
    private static int randomConstrictionNumber(int minimun, int maximum) {
        Random generator = new Random();
        return minimun + generator.nextInt(maximum-minimun);

    }

    /**
     * Initializes the possibilities and their probabilities of the extra times.
     */
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

    /**
     * Initializes the possibilities and their probabilities of the numerical complexities.
     */
    private static void initializeExamNumericalComplexity() {
        numericalComplexities = new HashMap<>();
        if (cnEnabled) {
            numericalComplexities.put(0, 0.7);
            numericalComplexities.put(5, 0.15);
            numericalComplexities.put(7, 0.1);
            numericalComplexities.put(10, 0.05);
        }
        else{
            numericalComplexities.put(0, 1.0);
        }
    }

    /**
     * Initializes the possibilities and their probabilities of the exam durations.
     */
    private static void initializeExamDurationProbabilities() {
        double divisor = 60.0;
        examDurations = new HashMap<>();
        examDurations.put(Duration.ofMinutes(30), 9/divisor);
        examDurations.put(Duration.ofMinutes(60), 12/divisor);
        examDurations.put(Duration.ofMinutes(90), 3/divisor);
        examDurations.put(Duration.ofMinutes(120), 8/divisor);
        examDurations.put(Duration.ofMinutes(150), 12/divisor);
        examDurations.put(Duration.ofMinutes(180), 14/divisor);
        examDurations.put(Duration.ofMinutes(210), 2/divisor);
    }

    /**
     * Generates the instance.
     */
    public static void generator(String directory, String filename, int counter) {

        // Iterar la entrada
        reset();
        initialize();

        for (int i = 0; i < repetitions; i++) {

            for (int k = 1; k <= 3; k++) { // Cursos
                for (int j = 1; j <= 10; j++) { // Asignaturas por curso a ojo
                    generateRandomExam(k, 1, "Teórico");
                    generateRandomExam(k, 2, "Práctico");
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

        scheduleSomeExams();


        ExcelWriter excelWriter = new ExcelWriter(new ExamParser(), new ConstrictionParser());
        excelWriter.parseExamListToExcel(directory, filename, counter, result,
                constrictions, getDefaultCalendarTimeInterval(calendar));


    }

    /**
     * Resets all the data structures.
     */
    private static void reset() {
        sameDayPairs = new ArrayList<>();
        orderedPairs = new ArrayList<>();
        result = new ArrayList<>();
        constrictions = new HashMap<>();
    }

    /**
     * Gets some free exams and schedules them.
     */
    private static void scheduleSomeExams() {
        List<Exam> scheduled = new ArrayList<>();
        Random generator = new Random();
        for (int i = 0; i < Math.min(max_exams_scheduled, freeExams.size()); i++) {
            Exam exam = freeExams.get(i);
            while(! exam.isScheduled() || collisionDetected(scheduled, exam)){
                LocalDate day = calendar.get(generator.nextInt(calendar.size()));
                double prob = generator.nextDouble();
                int initialHour;
                if (prob < 0.5) {
                    initialHour = 9 + generator.nextInt(3);
                }
                else{
                    initialHour = 15 + generator.nextInt(3);
                }
                exam.setDate(day);
                exam.setInitialHour(LocalTime.of(initialHour, 0));
            }
            scheduled.add(exam);
        }
    }

    /**
     * Checks if a given exam collides with any of the exam in a given list.
     * @param exams A list of exams
     * @param exam The exam that we want to check if colliding with any of the list.
     * @return True in case the exam collides with any of the ones in the list, False otherwise.
     */
    private static boolean collisionDetected(List<Exam> exams, Exam exam) {
        for (Exam ex : exams) {
            if (exam.willCollideWith(ex)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the hour interval of the days in the final instance
     * @param dates The list of days of the final instance.
     * @return The final calendar containing also the information about the time interval per each day.
     */
    private static HashMap<LocalDate, Interval> getDefaultCalendarTimeInterval(List<LocalDate> dates) {
        HashMap<LocalDate, Interval> realCalendar = new HashMap<>();
        Interval defaultInterval = new Interval(LocalTime.of(9,0), LocalTime.of(21,0));
        for (LocalDate date: dates) {
            realCalendar.put(date, defaultInterval);
        }
        return realCalendar;
    }

    /**
     * Generates the user constrictions.
     */
    private static void generateConstrictions() {

        generateDaysBanned();
        generateSameDays();
        generateDifferentDays();
        generateTimeDisplacements();
        generateOrders();

        constrictionAmount.put("DI", Math.min(randomConstrictionNumber(result.size() / 6, result.size() / 3)/2,
                freeExams.size() / 2));
        generateDayIntervals();
    }

    /**
     * Generates the {@code DayIntervalConstriction} instances.
     */
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

    /**
     * Generates the {@code OrderExamsConstriction} instances.
     */
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

    /**
     * Generates the {@code TimeDisplacementConstriction} instances.
     */
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

    /**
     * Generates the {@code DifferentDayConstriction} instances.
     */
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

    /**
     * Generates the {@code SameDayConstriction} instances.
     */
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

    /**
     * Checks if a {@code ExamPair} was registered on a list.
     */
    private static boolean checkPairRegistered(ExamPair exams, List<ExamPair> examPairList) {
        return examPairList.contains(exams);
    }

    /**
     * Generates the {@code DayBannedConstriction} instances.
     */
    private static void generateDaysBanned() {
        constrictions.put("DB", new ArrayList<>());
        for (int i = 0; i < constrictionAmount.get("DB"); i++) {
            banRandomDayForRandomExam();
        }
    }

    /**
     * Bans a random calendar day for an exam.
     */
    private static void banRandomDayForRandomExam() {
        Exam exam = result.get(generator.nextInt(result.size()));
        LocalDate bannedDay = calendar.get(generator.nextInt(calendar.size()));
        UserConstriction uc = new DayBannedConstriction(exam, bannedDay);
        constrictions.get("DB").add(uc);
        handleHards(uc);
        freeExams.remove(exam);
        
    }

    /**
     * Randomly makes a constriction hard.
     * @param uc The constriction that may be marked as hard.
     */
    private static void handleHards(UserConstriction uc) {
        if (hardsEnabled) {
            Random generator = new Random();
            if (generator.nextDouble() < 0.3) {
                uc.hardify();
            }
        }
    }

    /**
     * Generates an exam full of random data but the specified as parameter.
     * @param course The course of the exam.
     * @param semester The semester of the exam.
     * @param content The content of the exam.
     */
    private static void generateRandomExam(int course, int semester, String content) {
        Exam exam = new Exam(course, semester, generateCode(),
                generateAcronym(), generateSubject(), 1,
                content, generateRandomModality(), generator.nextInt(90) + 40, generateRandomDuration().toMinutes(),
                null, null, generateRandomExtraTime(), generateRandomNumericalComplexity(), idCounter, null);
        idCounter++;
        result.add(exam);

    }

    private static String generateRandomModality() {
        Random generator = RandomGenerator.getGenerator();
        if (generator.nextDouble()< 0.15){
            return "Entrega";
        }
        else{
            return "Presencial";
        }
    }


    /**
     * Generates a random numerical complexity.
     * @return A random numerical complexity.
     */
    private static int generateRandomNumericalComplexity() {
        return getIntegerFromHashMap(numericalComplexities);
    }

    /**
     * Gets an integer key from as hashmap having on the values the probability of the key to be selected.
     * @param test A hashmap with integers as keys and their probability to be selected as values.
     * @return The integer key selected.
     */
    private static Integer getIntegerFromHashMap(HashMap<Integer, Double> test) {
        double p = Math.random();
        double cumulativeProbability = 0.0;
        Integer defResult = null;
        for (Map.Entry<Integer, Double> item : test.entrySet()) {
            cumulativeProbability += item.getValue();
            defResult = item.getKey();
            if (p <= cumulativeProbability) {
                return item.getKey();
            }
        }
        return defResult;
    }

    /**
     * Generates a random extra time.
     * @return A random extra time.
     */
    private static Duration generateRandomExtraTime() {
        return getDurationFromHashMap(examExtraTimes);
    }


    /**
     * Gets a duration key from as hashmap having on the values the probability of the key to be selected.
     * @param test A hashmap with durations as keys and their probability to be selected as values.
     * @return The duration key selected.
     */
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

    /**
     * Generates a random exam duration.
     * @return A random exam duration.
     */
    private static Duration generateRandomDuration() {
        return getDurationFromHashMap(examDurations);
    }

    /**
     * Generates a random unique string for the subject field.
     * @return A random unique string for the subject field.
     */
    private static String generateSubject() {
        return "Subject:" + idCounter;
    }

    /**
     * Generates a random unique string for the acronym field.
     * @return A random unique string for the acronym field.
     */
    private static String generateAcronym() {
        return "Acro:" + idCounter;
    }

    /**
     * Generates a random unique string for the code field.
     * @return A random unique string for the code field.
     */
    private static String generateCode() {
        return "Code:" + idCounter;
    }
}
