package domain.entities;

import java.util.Comparator;

public class ExamDatesComparator implements Comparator<Exam> {

    @Override
    public int compare(Exam o1, Exam o2) {

        if (o1.getDate() == null && o2.getDate() != null)
            return 1;
        if (o1.getDate() != null && o2.getDate() == null)
            return -1;
        if (o1.getDate() == null && o2.getDate() == null)
            return 0;

        if (o1.getDate().isBefore(o2.getDate())){
            return -1;
        }

        if (o1.getDate().isAfter(o2.getDate())){
            return 1;
        }

        if (o1.getInitialHour().isBefore(o2.getInitialHour())){
            return -1;
        }

        if (o1.getInitialHour().isAfter(o2.getInitialHour())){
            return 1;
        }

        return 0;
    }
}
