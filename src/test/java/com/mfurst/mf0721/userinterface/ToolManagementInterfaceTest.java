package com.mfurst.mf0721.userinterface;

import com.mfurst.mf0721.config.AppConfig;
import com.mfurst.mf0721.model.dto.Tool;
import com.mfurst.mf0721.service.RentalService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * The tests for this class are used for integration tests
 */
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(
        classes = AppConfig.class
)
class ToolManagementInterfaceTest {

    private static final String INTERFACE_INPUT_MESSAGES = "Enter tool code to search by: Enter date of rental (mm/dd/yy): Enter amount of days customer will be renting for: Enter discount percentage [0-100]: ";

    @Autowired
    private ToolManagementInterface toolManagementInterface;

    /**
     * If there is an invalid discount rate, then we should expect the error stream to equal the error
     * message for an invalid discount rate. In this case, an error message stating that the discount rate
     * cannot be greater than one-hundred percent
     */
    @Test
    void runTestForRidgidJackhammerWithInvalidDiscount() {
        String data = "JAKR\r\n09/03/15\r\n5\r\n101\r\n";
        InputStream testInput = new ByteArrayInputStream(data.getBytes());
        ByteArrayOutputStream outputByteStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorByteStream = new ByteArrayOutputStream();
        PrintStream outputStream = new PrintStream(outputByteStream);
        PrintStream errorStream = new PrintStream(errorByteStream);

        toolManagementInterface.run(outputStream, testInput, errorStream);
        assertEquals(INTERFACE_INPUT_MESSAGES, outputByteStream.toString());
        assertEquals("Discount rate cannot be greater than one-hundred percent.\n", errorByteStream.toString());
    }

    /**
     * Test for valid rental of a Werner ladder
     *
     * We will compare output stream to validate correct values are being generated
     * And we will check the error stream is empty to validate no errors are thrown
     */
    @Test
    void runTestForJulyRentalOfWernerLadder() {
        String data = "LADW\r\n07/02/20\r\n3\r\n10\r\n";
        InputStream testInput = new ByteArrayInputStream(data.getBytes());
        ByteArrayOutputStream outputByteStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorByteStream = new ByteArrayOutputStream();
        PrintStream outputStream = new PrintStream(outputByteStream);
        PrintStream errorStream = new PrintStream(errorByteStream);

        toolManagementInterface.run(outputStream, testInput, errorStream);
        assertEquals(INTERFACE_INPUT_MESSAGES + String.format(ToolManagementInterface.RECEIPT_MESSAGE,
                "LADW", "Ladder", "Werner", 3, "07/02/20", "07/05/20", "$1.99", 2, "$3.98", 10, "$0.40", "$3.58"
                ) + "\n", outputByteStream.toString());
        assertEquals("", errorByteStream.toString());
    }

    /**
     * Test for valid rental of a Stihl chainsaw
     *
     * We will compare output stream to validate correct values are being generated
     * And we will check the error stream is empty to validate no errors are thrown
     */
    @Test
    void runTestForJulyRentalOfStihlChainsaw() {
        String data = "CHNS\r\n07/02/15\r\n5\r\n25\r\n";
        InputStream testInput = new ByteArrayInputStream(data.getBytes());
        ByteArrayOutputStream outputByteStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorByteStream = new ByteArrayOutputStream();
        PrintStream outputStream = new PrintStream(outputByteStream);
        PrintStream errorStream = new PrintStream(errorByteStream);

        toolManagementInterface.run(outputStream, testInput, errorStream);
        assertEquals(INTERFACE_INPUT_MESSAGES + String.format(ToolManagementInterface.RECEIPT_MESSAGE,
                "CHNS", "Chainsaw", "Stihl", 5, "07/02/15", "07/07/15", "$1.49", 3, "$4.47", 25, "$1.12", "$3.35"
                ) + "\n", outputByteStream.toString());
        assertEquals("", errorByteStream.toString());
    }

    /**
     * Test for valid rental of a DeWalt jackhammer
     *
     * We will compare output stream to validate correct values are being generated
     * And we will check the error stream is empty to validate no errors are thrown
     */
    @Test
    void runTestForSeptemberRentalOfDeWaltJackhammer() {
        String data = "JAKD\r\n09/03/15\r\n6\r\n0\r\n";
        InputStream testInput = new ByteArrayInputStream(data.getBytes());
        ByteArrayOutputStream outputByteStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorByteStream = new ByteArrayOutputStream();
        PrintStream outputStream = new PrintStream(outputByteStream);
        PrintStream errorStream = new PrintStream(errorByteStream);

        toolManagementInterface.run(outputStream, testInput, errorStream);
        assertEquals(INTERFACE_INPUT_MESSAGES + String.format(ToolManagementInterface.RECEIPT_MESSAGE,
                "JAKD", "Jackhammer", "DeWalt", 6, "09/03/15", "09/09/15", "$2.99", 3, "$8.97", 0, "$0.00", "$8.97"
                ) + "\n", outputByteStream.toString());
        assertEquals("", errorByteStream.toString());
    }

    /**
     * Test for valid rental of a Ridgid jackhammer
     *
     * We will compare output stream to validate correct values are being generated
     * And we will check the error stream is empty to validate no errors are thrown
     */
    @Test
    void runTestForJulyRentalOfRidgidJackhammer() {
        String data = "JAKR\r\n07/02/15\r\n9\r\n0\r\n";
        InputStream testInput = new ByteArrayInputStream(data.getBytes());
        ByteArrayOutputStream outputByteStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorByteStream = new ByteArrayOutputStream();
        PrintStream outputStream = new PrintStream(outputByteStream);
        PrintStream errorStream = new PrintStream(errorByteStream);

        toolManagementInterface.run(outputStream, testInput, errorStream);
        assertEquals(INTERFACE_INPUT_MESSAGES + String.format(ToolManagementInterface.RECEIPT_MESSAGE,
                "JAKR", "Jackhammer", "Ridgid", 9, "07/02/15", "07/11/15", "$2.99", 5, "$14.95", 0, "$0.00", "$14.95"
                ) + "\n", outputByteStream.toString());
        assertEquals("", errorByteStream.toString());
    }

    /**
     * Test for valid rental of a Ridgid jackhammer
     *
     * We will compare output stream to validate correct values are being generated
     * And we will check the error stream is empty to validate no errors are thrown
     */
    @Test
    void runTestForJulyRentalOfRidgidJackhammerAtFiftyPercentOff() {
        String data = "JAKR\r\n07/02/20\r\n4\r\n50\r\n";
        InputStream testInput = new ByteArrayInputStream(data.getBytes());
        ByteArrayOutputStream outputByteStream = new ByteArrayOutputStream();
        ByteArrayOutputStream errorByteStream = new ByteArrayOutputStream();
        PrintStream outputStream = new PrintStream(outputByteStream);
        PrintStream errorStream = new PrintStream(errorByteStream);

        toolManagementInterface.run(outputStream, testInput, errorStream);
        assertEquals(INTERFACE_INPUT_MESSAGES + String.format(ToolManagementInterface.RECEIPT_MESSAGE,
                "JAKR", "Jackhammer", "Ridgid", 4, "07/02/20", "07/06/20", "$2.99", 1, "$2.99", 50, "$1.50", "$1.49"
                ) + "\n", outputByteStream.toString());
        assertEquals("", errorByteStream.toString());
    }
}