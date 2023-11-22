import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.util.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ProgrammingTaskTest {
    @Test
    @DisplayName("test that no exception is thrown from readFileAndGetIpAddressesAndUrls")
    public void testReadFileAndGetIpAddressesAndUrlsDoesNotThrowException(){
        LinkedHashMap<String, Integer> urls = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> ipAddresses = new LinkedHashMap<>();

        ProgrammingTask.readFileAndGetIpAddressesAndUrls(urls, ipAddresses);

        assertDoesNotThrow(() -> ProgrammingTask.readFileAndGetIpAddressesAndUrls(urls, ipAddresses));
    }

    @Test
    @DisplayName("Test that readFileAndGetIpAddressesAndUrls returns updates hashmaps")
    public void testReadFileAndGetIpAddressesAndUrlsExceutes(){
        LinkedHashMap<String, Integer> urls = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> ipAddresses = new LinkedHashMap<>();

        ProgrammingTask.readFileAndGetIpAddressesAndUrls(urls, ipAddresses);

        assertThat(urls.size()).isEqualTo(22);
        assertThat(ipAddresses.size()).isEqualTo(11);
    }

    @ParameterizedTest
    @CsvSource({"168.41.191.9, 1",
            "168.41.191.9 - - [72.44.32.10], 1",
            "nullValues={\"null\"}, 0",
            "'', 0"})
    @DisplayName("Test that IP addresses are identified based on pattern and added to map")
    public void testExtractIpAddressesReturnsCorrectAddresses(String strLine, int noOfIpAddresses) {
        LinkedHashMap<String, Integer> ipAddresses = new LinkedHashMap<>();

        LinkedHashMap<String, Integer> ipAddressMap = ProgrammingTask.extractIpAddresses(ipAddresses, strLine);

        assertThat(ipAddressMap.size()).isEqualTo(noOfIpAddresses);
    }

    @ParameterizedTest
    @CsvSource({"GET /madison GET /hello, 1",
            "168.41.191.9 - - [72.44.32.10], 0",
            "GET test, 0",
            "nullValues={\"null\"}, 0",
            "'', 0"})
    @DisplayName("Test that correct valid urls are added to map")
    public void testExtractUrlsReturnsCorrectUrls(String strLine, int noOfUrls) {
        LinkedHashMap<String, Integer> urls = new LinkedHashMap<>();

        LinkedHashMap<String, Integer> urlMap = ProgrammingTask.extractUrls(urls, strLine);

        assertThat(urlMap.size()).isEqualTo(noOfUrls);
    }

    @Test
    @DisplayName("Test map is sorted by descending value")
    public void testSortMapByValueDescendingReturnsSortedDescendingOrderMap() {
        Map<String, Integer> unsortedMap = new HashMap<>();
        unsortedMap.put("one", 1);
        unsortedMap.put("three", 3);
        unsortedMap.put("five", 5);
        unsortedMap.put("two", 2);
        unsortedMap.put("four", 4);

        Map<String, Integer> sortedMap = ProgrammingTask.sortMapByValueDescending(unsortedMap);

        final Iterator<Integer> iterator = sortedMap.values().iterator();

        assertAll (
                () -> assertThat(iterator.next()).isEqualTo(5),
                () -> assertThat(iterator.next()).isEqualTo(4),
                () -> assertThat(iterator.next()).isEqualTo(3),
                () -> assertThat(iterator.next()).isEqualTo(2),
                () -> assertThat(iterator.next()).isEqualTo(1),

                () -> assertThat(sortedMap.size()).isEqualTo(5)
        );
    }

    @Test
    @DisplayName("Test returned list holds correct elements and is of the correct size")
    public void testGetTopNResultsReturnsCorrectOrderAndNumberOfResults() {
        LinkedHashMap<String, Integer> sortedMapDescending = new LinkedHashMap<>();
        sortedMapDescending.put("five", 5);
        sortedMapDescending.put("four", 4);
        sortedMapDescending.put("three", 3);
        sortedMapDescending.put("threeAdditional", 4);
        sortedMapDescending.put("two", 2);

        List<String> topNResults = ProgrammingTask.getTopNResults(sortedMapDescending, 3);

        assertAll (
                () -> assertThat(topNResults.get(0)).isEqualTo("five"),
                () -> assertThat(topNResults.get(1)).isEqualTo("four"),
                () -> assertThat(topNResults.get(2)).isEqualTo("three"),

                () -> assertThat(topNResults.size()).isEqualTo(3)
        );
    }
}