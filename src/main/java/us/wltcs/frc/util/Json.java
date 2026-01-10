package us.wltcs.frc.util;

import lombok.experimental.UtilityClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@UtilityClass
public class Json {
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public <T> void write(T data, String filepath) {
    FileIO.writeTextFile(filepath, gson.toJson(data));
  }

  public String read() {

    return "";
  }
}
