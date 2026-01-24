package us.wltcs.frc.core.devices.output;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import us.wltcs.frc.core.math.vector2.Vector2i;

public class Camera {
  private Vector2i resolution;
  CvSink camera;
  CvSource outputStream;

  public Camera(String name, int width, int height) {
    resolution = new Vector2i(width, height);
    CameraServer.startAutomaticCapture();
    camera = CameraServer.getVideo();
    outputStream = CameraServer.putVideo(name, width, height);
  }
}
