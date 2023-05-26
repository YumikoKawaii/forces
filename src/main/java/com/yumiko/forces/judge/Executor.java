package com.yumiko.forces.judge;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

public class Executor {

    private static final Logger logger = Logger.getLogger(Executor.class.getName());
    private static final String path = "./code/";
    public static final String inputFile = "input.txt";
    public static final String outputFile = "output.txt";
    public static final String executable = "source";
    public static String sourceFile;
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
    public static void storeSourceFile(MultipartFile source) {

        Path destination = Paths.get(path, source.getOriginalFilename());

        sourceFile = source.getOriginalFilename();

        try {
            source.transferTo(destination);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void removeAll() {

        String command = "rm -rf " + path + "*";

        try {
            Process process = new ProcessBuilder("bash","-c",command).start();

            if (process.waitFor() != 0) {
                logger.warning("Error while cleaning storage!");
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    private static String compileCommand(String language) {

        return switch (language) {
            case "cpp" -> "g++ " + path + sourceFile + " -o " + path + executable;
            case "c" -> "gcc " + path + sourceFile + " -o " + path + executable;
            case "java_8" -> "javac " + path + sourceFile;
            case "go" -> "go build " + path + sourceFile;
            default -> "";
        };

    }
    public static void compile(String language) {

        try {

            String command = compileCommand(language);

            ProcessBuilder processBuilder = new ProcessBuilder(command.split("\\s+"));
            Process process = processBuilder.start();

            if (process.waitFor() != 0) {
                logger.warning("Error occur while compiling user's file!");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
    private static String executeCommand(String language) {

        return switch (language) {
            case "cpp", "c" -> path + executable;
            case "java" -> "java " + path + sourceFile;
            case "go" -> path + "./main";
            default -> "";
        };

    }
    public static void execute(String language) {

        String command = executeCommand(language);
        String input = path + inputFile;
        String output = path + outputFile;

        ProcessBuilder processBuilder = new ProcessBuilder(command);
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
