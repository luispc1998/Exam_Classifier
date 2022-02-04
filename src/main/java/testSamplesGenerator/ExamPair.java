package testSamplesGenerator;

import domain.constraints.types.softConstrictions.userConstraints.DifferentDayConstraint;
import domain.entities.Exam;

/**
 * This represents a pair of exams.
 *
 * <p>
 * Used to check wether possible to create other new constrictions among exams. For instance if exam A and exam B are
 * said to be on the same day, then when creating instance of {@link DifferentDayConstraint}
 * those two won't be put together.
 */
public class ExamPair {

    /**
     * The first exam.
     */
    private final Exam exam1;

    /**
     * The second exam.
     */
    private final Exam exam2;

    /**
     * Default constructor of the class.
     * @param exam1 The first exam.
     * @param exam2 The second exam.
     */
    public ExamPair(Exam exam1, Exam exam2) {
        this.exam1 = exam1;
        this.exam2 = exam2;
    }

    /**
     * Returns the first exam.
     * @return The first exam.
     */
    public Exam getExam1() {
        return exam1;
    }

    /**
     * Returns the second exam.
     * @return The second exam.
     */
    public Exam getExam2() {
        return exam2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExamPair examPair = (ExamPair) o;
        return (exam1.equals(examPair.getExam1()) && exam2.equals(examPair.getExam2()))
                || (exam1.equals(examPair.getExam2()) && exam2.equals(examPair.getExam1()));
    }

}
