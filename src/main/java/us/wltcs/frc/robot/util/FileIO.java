package us.wltcs.frc.robot.util;

import java.io.*;
import java.util.ArrayList;

import lombok.experimental.UtilityClass;
import us.wltcs.frc.robot.core.logging.Context;
import us.wltcs.frc.robot.core.logging.Levels;

@UtilityClass
public class FileIO {
  public String readTextFile(String filepath) {
    ArrayList<String> content = new ArrayList<>();

    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath))) {
      String line;
      while ((line = bufferedReader.readLine()) != null)
        content.add(line);

    } catch (IOException exception) {
      Context.program.log(Levels.ERROR, String.format("Failed to read %s, cause: %s", filepath, exception.getCause()));
      return null;
    }

    return content.toString();
  }

  public void writeTextFile(String filepath, String content) {
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath))) {
      bufferedWriter.write(content);
    } catch(Exception exception) {
      Context.program.log(Levels.ERROR, String.format("Failed to write to %s\n%s", filepath, exception.getCause()));
    }
  }
}
