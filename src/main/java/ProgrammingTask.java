import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.reverseOrder;
import static java.util.stream.Collectors.toMap;


public class ProgrammingTask {
    /**
     * Reads file, sorts IP addresses and Urls to print coding task answers
     * @param args
     */
    public static void main(String[] args) {
        LinkedHashMap<String, Integer> urls = new LinkedHashMap<>();
        LinkedHashMap<String, Integer> ipAddresses = new LinkedHashMap<>();
        readFileAndGetIpAddressesAndUrls(urls, ipAddresses);

        LinkedHashMap<String, Integer> sortedIpAddressesDescending = sortMapByValueDescending(ipAddresses);
        LinkedHashMap<String, Integer> sortedURLsDescending = sortMapByValueDescending(urls);

        System.out.println("No. of unique IP addresses = " + ipAddresses.size());
        System.out.println("Top 3 most active IP addresses = " + getTopNResults(sortedIpAddressesDescending, 3));
        System.out.println("Top 3 most active URLs = " + getTopNResults(sortedURLsDescending, 3));
    }

    /**
     * Reads log file and populates linked hash maps with urls and ip addresses found
     * @param urls
     * @param ipAddresses
     */
    public static void readFileAndGetIpAddressesAndUrls(LinkedHashMap<String, Integer> urls, LinkedHashMap<String, Integer> ipAddresses) {
        try {
            String fileName = "programming-task-example-data.log";
            InputStream inputStream = getFileFromResourcesAsStream(fileName);
            String strLine;
            BufferedReader reader = new BufferedReader(new InputStreamReader(Objects.requireNonNull(inputStream), StandardCharsets.UTF_8));
            while ((strLine = reader.readLine()) != null) {
                extractUrls(urls, strLine);
                extractIpAddresses(ipAddresses, strLine);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Gets file from resource folder as input stream
     * @return InputStream
     * @param fileName
     */
    private static InputStream getFileFromResourcesAsStream(String fileName) {
        InputStream inputStream = ProgrammingTask.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        }
        return inputStream;
    }

    /**
     * Extracts IP addresses based on regex pattern matched and counts how many instances each IP address occurs
     * @param ipAddresses
     * @param strLine
     * @return LinkedHashMap of IpAddresses from log file
     */
    public static LinkedHashMap<String, Integer> extractIpAddresses(LinkedHashMap<String, Integer> ipAddresses, String strLine) {
        String validIpAddressRegex = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
        Pattern pattern = Pattern.compile(validIpAddressRegex);
        Matcher matcher = pattern.matcher(strLine);
        if (matcher.find()) {
            String address = matcher.group();
            ipAddresses.put(address, ipAddresses.getOrDefault(address, 0) + 1);
        }
        return ipAddresses;
    }

    /**
     * Extracts URLs based on GET index and counts how many instances of each unique URL occurs
     * @param urls
     * @param strLine
     * @return LinkedHashMap of URLs from log file
     */
    public static LinkedHashMap<String, Integer> extractUrls(LinkedHashMap<String, Integer> urls, String strLine) {
        int indexOfGET = strLine.indexOf("GET");
        int indexOfSpace = strLine.indexOf(" ", indexOfGET + 4);
        if (strLine.contains("GET") && indexOfSpace >= 0) {
            String url = strLine.substring(indexOfGET + 4, indexOfSpace);
            urls.put(url, urls.getOrDefault(url, 0) + 1);
        }
        return urls;
    }

    /**
     * Sorts map by descending order based on value
     * @param map
     * @return LinkedHashMap by descending order based on value
     */
    public static LinkedHashMap<String, Integer> sortMapByValueDescending(Map<String, Integer> map) {
        return map.entrySet().stream()
                .sorted(reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    /**
     * Returns x amount of results from a LinkedHashMap where x is the specified number of results to return
     * @param sortedMapDescending
     * @param numOfResults number of results to return
     * @return List of Strings that pertain to key of the LinkedHashMap passed in
     */
    public static List<String> getTopNResults(LinkedHashMap<String, Integer> sortedMapDescending, int numOfResults) {
        List<String> keyList = new ArrayList<>();
        for (String key : sortedMapDescending.keySet()) {
            if (keyList.size() < numOfResults)
                keyList.add(key);
        }
        return keyList;
    }
}
