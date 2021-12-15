package testSamplesGenerator;

import domain.entities.Exam;

public class ExamPair {

    private final Exam exam1;
    private final Exam exam2;

    public ExamPair(Exam exam1, Exam exam2) {
        this.exam1 = exam1;
        this.exam2 = exam2;
    }

    public Exam getExam1() {
        return exam1;
    }

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
