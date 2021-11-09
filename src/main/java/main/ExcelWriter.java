package main;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.entities.Exam;
import domain.entities.ExamDatesComparator;
import domain.parsers.ConstrictionParser;
import domain.parsers.ExamParser;
import fitnessFunctions.greedyAlgorithm.CromosomeDecoder;
import geneticAlgorithm.Individual;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class ExcelWriter {

    public static void excelWrite(HashSet<Individual> outputIndividuals, DataHandler dataHandler,
                                  String outputFileName) throws IOException {

        CromosomeDecoder decoder = new CromosomeDecoder();
        PrettyTimetable prettyTimetable = new PrettyTimetable();

        String directory = createOuputDirectory(dataHandler.getConfigurer().getFilePaths("outputBaseDirectory"));

        int counter = 0;
        for (Individual idv: outputIndividuals) {
            decoder.decode(idv, dataHandler);
            prettyTimetable.orderScheduling(dataHandler);
            List<Exam> finalResult = dataHandler.getClonedSchedule();
            HashMap<String, List<Constriction>> verifiedConstrictions = dataHandler.verifyConstrictions();
            dataHandler.resetScheduling();
            Comparator<Exam> examComparator = new ExamDatesComparator();
            finalResult.sort(examComparator);



            XSSFWorkbook workbook = new XSSFWorkbook();
            ExamParser.parseToExcel(finalResult, workbook);
            ConstrictionParser.parseToExcel(verifiedConstrictions, workbook);
            counter++;

            try (FileOutputStream outputStream = new FileOutputStream(directory + "/" + outputFileName + "_" + counter + ".xlsx")) {
                workbook.write(outputStream);
            }
        }


    }

    public static String createOuputDirectory(String outputBaseDirectory) {

        LocalDate ld = LocalDate.now();
        LocalTime lt = LocalTime.now();

        StringBuilder directoryBuilder = new StringBuilder();
        directoryBuilder.append(outputBaseDirectory);
        directoryBuilder.append(ld.getYear());
        directoryBuilder.append(ld.getMonthValue());
        directoryBuilder.append(ld.getDayOfMonth());

        directoryBuilder.append("_");
        directoryBuilder.append(lt.getHour());
        directoryBuilder.append(lt.getMinute());

        File theDir = new File(directoryBuilder.toString());

        if (!theDir.exists()){
            theDir.mkdirs();
        }

        return directoryBuilder.toString();
    }

}
