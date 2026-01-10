package us.wltcs.frc.util;

import java.io.*;
import java.util.ArrayList;
import lombok.experimental.UtilityClass;
import us.wltcs.frc.core.logging.Context;

@UtilityClass
public class FileIO {
  public String readTextFile(String filepath) {
    ArrayList<String> content = new ArrayList<>();

    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath))) {
      String line;
      while ((line = bufferedReader.readLine()) != null)
        content.add(line);

    } catch (IOException exception) {
      Context.program.logError("Failed to read \"%s\", cause: %s", filepath, exception.getCause());
      return null;
    }

    return content.toString();
  }

  public void writeTextFile(String filepath, String content) {
    try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filepath))) {
      bufferedWriter.write(content);
    } catch(Exception exception) {
      Context.program.logError("Failed to write to \"%s\", cause: %s", filepath, exception.getCause());
    }
  }
}
