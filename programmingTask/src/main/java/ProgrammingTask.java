import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.reverseOrder;
import static java.util.stream.Collectors.toMap;


public class ProgrammingTask {
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

    public static void readFileAndGetIpAddressesAndUrls(LinkedHashMap<String, Integer> urls, LinkedHashMap<String, Integer> ipAddresses) {
        try {
            InputStream logFileStream = ProgrammingTask.class.getResourceAsStream("/programming-task-example-data.log");
            String strLine;
            BufferedReader reader = new BufferedReader(new InputStreamReader(logFileStream, StandardCharsets.UTF_8));
            while ((strLine = reader.readLine()) != null) {
                extractUrls(urls, strLine);
                extractIpAddresses(ipAddresses, strLine);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

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

    public static LinkedHashMap<String, Integer> extractUrls(LinkedHashMap<String, Integer> urls, String strLine) {
        int indexOfGET = strLine.indexOf("GET");
        int indexOfSpace = strLine.indexOf(" ", indexOfGET + 4);
        if (strLine.contains("GET") && indexOfSpace >= 0) {
            String url = strLine.substring(indexOfGET + 4, indexOfSpace);
            urls.put(url, urls.getOrDefault(url, 0) + 1);
        }
        return urls;
    }

    public static LinkedHashMap<String, Integer> sortMapByValueDescending(Map<String, Integer> map) {
        return map.entrySet().stream()
                .sorted(reverseOrder(Map.Entry.comparingByValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static List<String> getTopNResults(LinkedHashMap<String, Integer> sortedMapDescending, int numOfResults) {
        List<String> keyList = new ArrayList<>();
        for (String key : sortedMapDescending.keySet()) {
            if (keyList.size() < numOfResults)
                keyList.add(key);
        }
        return keyList;
    }
}
