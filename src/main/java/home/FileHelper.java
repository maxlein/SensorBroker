package home;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by max on 10.06.17.
 */
public class FileHelper {
    private static final org.apache.log4j.Logger log = org.apache.log4j.Logger.getLogger(ItemHandler.class);

    public static File[] getSubDirectories(String path){
        return new File(path).listFiles(File::isDirectory);
    }

    public static List<File> getSubDirectories(String path, String startingWith){
        File[] subDirs = FileHelper.getSubDirectories(path);
        return Arrays.stream(subDirs).filter(subdir -> subdir.getName().startsWith(startingWith)).collect(Collectors.toList());
    }

    public static File[] getFiles(String path){
        return new File(path).listFiles(File::isFile);
    }

    public static List<File> getFilesWithName(String path, String name){
        File[] files = FileHelper.getFiles(path);
        return Arrays.stream(files).filter(subdir -> subdir.getName().equals(name)).collect(Collectors.toList());
    }

    public static String parseFileForTemperature(File file){
        List<String> list = new ArrayList<>();

        try (Stream<String> stream = Files.lines(file.toPath())) {

            list = stream
                    .filter(line -> line.contains("t="))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            e.printStackTrace();
        }

        //list.forEach(System.out::println);
        String tempLine = list.get(0);
        return tempLine.substring(tempLine.lastIndexOf("t=") + 2);
    }

    public static String getTemperatureFromDS1820SensorReading(String path, String sensorFolderStartingWith, String sensorFile){
        List<File> filteredSubDirs = FileHelper.getSubDirectories(path, sensorFolderStartingWith);
        log.debug("size is " + filteredSubDirs.size() + ", path: " + filteredSubDirs.get(0).getPath() + ", abs path: " + filteredSubDirs.get(0).getAbsolutePath());
        List<File> files = FileHelper.getFilesWithName(filteredSubDirs.get(0).getPath(), sensorFile);
        log.debug("files: " + files.size() + ", file: " + files.get(0).getName());
        return FileHelper.parseFileForTemperature(files.get(0));
    }

    public static void main(String[] args) {
        String temperature = getTemperatureFromDS1820SensorReading("/Users/max", "Mov", "tmp");
        Integer temp = Integer.parseInt(temperature);
        Double temp_conv = temp / 1000.0;
        temperature = String.valueOf(temp_conv);
        log.info(temperature + ", int: " + temp + ", double: " + temp_conv);
    }
}
