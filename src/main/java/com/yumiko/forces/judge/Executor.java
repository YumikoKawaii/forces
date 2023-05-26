package com.yumiko.forces.judge;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Executor {

    private static final Logger logger = Logger.getLogger(Executor.class.getName());
    private static final String path = "./code/";
    public static final String inputFile = "input.txt";
    public static final String outputFile = "output.txt";
    public static final String keyFile = "key.txt";
    public static final String executable = "source";

    public static String sourceFile;

    private static String calculateMD5(String filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (DigestInputStream dis = new DigestInputStream(new FileInputStream(filePath), md)) {
            while (dis.read() != -1) ;
            byte[] hashBytes = md.digest();
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(Integer.toHexString((b & 0xFF) | 0x100).substring(1, 3));
            }
            return hashString.toString();
        }
    }
    public static void createFile(String input, String filename) {

        String location = path + filename;
        File file = new File(location);
        try {
            file.createNewFile();
            BufferedWriter writer = new BufferedWriter(new FileWriter(location));
            writer.write(input);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public static String getContentFromFile(String filename) {
        String location = path + filename;

        try (BufferedReader reader = new BufferedReader(new FileReader(location))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
                result.append("\n");
            }
            result.deleteCharAt(result.length() - 1);
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    public static void deleteFile(String filename) {
        String filePath = path + filename;
        try {
          Path location = Paths.get(filePath);
          Files.delete(location);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void compileCpp(MultipartFile source) {

        Path destination = Paths.get(path, source.getOriginalFilename());

        sourceFile = source.getOriginalFilename();

        try {
            source.transferTo(destination);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            String compile = "g++ " + path + source.getOriginalFilename() + " -o " + path + executable;

            ProcessBuilder processBuilder = new ProcessBuilder(compile.split("\\s+"));
            Process process = processBuilder.start();

            if (process.waitFor() != 0) {
                logger.warning("Error occur while compiling user's file!");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
    public static void executeFile() {

        String execute = path + executable;
        String input = path + inputFile;
        String output = path + outputFile;

        ProcessBuilder processBuilder = new ProcessBuilder(execute);
        processBuilder.redirectInput(new File(input));
        processBuilder.redirectOutput(new File(output));

        try {
            Process process = processBuilder.start();

            if (process.waitFor() != 0) {
                logger.warning("Error occur while running user's file!");
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

}
