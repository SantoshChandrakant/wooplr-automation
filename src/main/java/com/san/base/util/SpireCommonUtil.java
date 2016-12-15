package com.san.base.util;

import org.testng.Assert;

import com.wooplr.base.controller.Assertion;
import com.wooplr.base.controller.Logging;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Vinay Kumar on 25-05-2015.
 * <p>
 * SpireCommon Util is a class has the common methods used across the project.
 */
public class SpireCommonUtil {

    /***
     * Parses line with the provided delimiter
     *
     * @param line  input of the line to be parsed
     * @param delim delimiter to be parsed
     * @return string array of line after parsed
     */
    public static String[] parseLine(String line, String delim) {
        if (line == null || line.trim().length() == 0) {
            return null;
        }
        List<String> tokenList = new ArrayList<String>();
        String[] result = null;
        String[] tokens = line.split(delim);
        int count = 0;
        while (count < tokens.length) {
            if (tokens[count] == null || tokens[count].length() == 0) {
                tokenList.add("");
                count++;
                continue;
            }

            if (tokens[count].startsWith(SpireUtilConstants.DOUBLE_QUOTE)) {
                StringBuffer sbToken = new StringBuffer(tokens[count].substring(1));
                while (count < tokens.length && !tokens[count].endsWith(SpireUtilConstants.DOUBLE_QUOTE)) {
                    count++;
                    sbToken.append(SpireUtilConstants.DELIM_CHAR).append(tokens[count]);
                }
                sbToken.deleteCharAt(sbToken.length() - 1);
                tokenList.add(sbToken.toString());
            } else {
                tokenList.add(tokens[count]);
            }
            count++;
        }

        if (tokenList.size() > 0) {
            result = new String[tokenList.size()];
            tokenList.toArray(result);
        }
        return result;

    }


    /**
     * Parses an input stream and returns a String[][] object
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String[][] read(InputStream is, String delim) throws IOException {

        String[][] result = null;
        List<String[]> list = new ArrayList<String[]>();
        String inputLine; // String that holds current file line

        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8")); // KEEPME
        while ((inputLine = reader.readLine()) != null) {
            try {
                String[] item = null;
                if (delim == null)
                    item = parseLine(inputLine, SpireUtilConstants.DELIM_CHAR);
                else
                    item = parseLine(inputLine, delim);
                if (item != null)
                    list.add(item);
            } catch (Exception e) {
                //todo.. logging of exception or warning
            }
        }
        reader.close();

        if (list.size() > 0) {
            result = new String[list.size()][];
            list.toArray(result);
        }
        return result;
    }


    /***
     * Converts map object to an temp array and returns its size .
     *
     * @param map Map object
     * @param key key of the map object
     * @return int of array size .
     */
    public static int getArraySize(Map<String, Object> map, String key) {
        if (key == null)
            return map.size();
        int count = 0;
        boolean valueFound = false;
        key = key.toLowerCase();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String key2 = entry.getKey();
            String value2 = (String) entry.getValue();
            if (value2 == null || value2.length() == 0)
                continue;

            key2 = key2.toLowerCase();
            if (key2.startsWith(key + ".")) {
                valueFound = true;
                String subst = key2.substring((key + ".").length());
                String[] ss = subst.split("\\.");
                try {
                    int value = Integer.parseInt(ss[0]);
                    count = (value > count ? value : count);
                } catch (Exception e) {
                    //todo.. logging of exception or warning
                }
            }
        }
        return (valueFound ? count + 1 : count);
    }


    public static void uploadFile(String filePath) {
        String[] cmd = {"./src/test/resources/executables/sample.exe", filePath};
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public static void assertDownloadedFile(String fileName) {

        //  String f = fileName.replace(".html", ".doc");

        String path = SpireUtilConstants.DOWNLOAD_PATH_PREF;

        File directory = new File(path);
        if (directory.exists()) {
            Logging.log("Folder already exists");
        } else {
            boolean mkdirs = directory.mkdirs();
            Logging.log("Folder created " + mkdirs);
        }
        FileInputStream fis;
        try {
            fis = new FileInputStream(path + fileName);
            boolean file = fis.read() != -1;
            if (file) {
                Logging.log("File downloaded");
            } else {
                Assert.fail("No file downloaded");
            }
        } catch (FileNotFoundException ex) {
            Assert.fail("File does not exist");
            //System.out.println("File not present");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public static void assertFileExists(String path) throws Exception {
        File directory = new File(path);
        if (directory.exists()) {
            Logging.log("Folder already exists");
        } else {
            boolean mkdirs = directory.mkdirs();
            Logging.log("Folder created " + mkdirs);
        }

        File[] listOfFiles = directory.listFiles();
        if (listOfFiles == null || listOfFiles.length == 0) {
            Assertion.assertTrue(Boolean.FALSE, "No files downloaded..");
        } else {
            for (File listOfFile : listOfFiles) {
                if (listOfFile.getName().contains(".csv") || listOfFile.getName().contains("_Report")
                        || listOfFile.getName().contains("_Temp") || listOfFile.getName().contains(".csv")) {
                    Logging.log("Downloaded Files Names " + listOfFile.getName());
                    System.out.println("Downloaded Files Names " + listOfFile.getName());
                }
            }
        }
    }


    public static void main(String args[]) {

        // uploadFile("F:\\Resumes.zip");
        String a = "115752815_Vishveshwar_M_78396.zip";
        String[] b = a.split("[^-?0-9]+");

        System.out.print("" + b[1]);


    }


    //C:\Users\Vinay Kumar\Desktop/
}
