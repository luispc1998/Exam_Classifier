package geneticAlgorithm.output;

import domain.DataHandler;
import domain.constrictions.Constriction;
import domain.entities.Exam;
import domain.entities.ExamDatesComparator;
import domain.parsers.ConstrictionParser;
import domain.parsers.ExamParser;
import fitnessFunctions.greedyAlgorithm.ChromosomeDecoder;
import geneticAlgorithm.Individual;
import main.PrettyTimetable;
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

/**
 * This writes the final individuals to excel format.
 *
 * <p>
 * It provides the final scheduling as well as the user constrictions specifying if they were fulfilled or not
 * in the provided scheduling.
 */
public class ExcelWriter {

    /**
     * This writes the provided individuals to excel files. One per individual
     * @param outputIndividuals The {@code Individual} instances, that lead to the final scheduling to be outputted.
     * @param dataHandler The {@code DataHandler} instance over which the individuals will be decoded.
     * @param directory The output directory.
     * @param outputFileName The name prefix of the output file.
     * @throws IOException In case there is a problem when writing the excel.
     */
    public static void excelWrite(HashSet<Individual> outputIndividuals, DataHandler dataHandler,
                                  String directory, String outputFileName) throws IOException {

        ChromosomeDecoder decoder = new ChromosomeDecoder();
        PrettyTimetable prettyTimetable = new PrettyTimetable();



        int counter = 0;
        for (Individual idv: outputIndividuals) {
            decoder.decodeNew(idv, dataHandler);
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

            try (FileOutputStream outputStream = new FileOutputStream(directory + outputFileName + "_" + counter + ".xlsx")) {
                workbook.write(outputStream);
            }
        }


    }



}
