package us.wltcs.frc.util;

import lombok.experimental.UtilityClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Type;

@UtilityClass
public class Json {
  private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

  public <T> void write(T data, String filepath) {
    FileIO.writeTextFile(filepath, gson.toJson(data));
  }

  public <T> T read(T type, String filepath) {
    return gson.fromJson(FileIO.readTextFile(filepath), (Type)type);
  }
}
