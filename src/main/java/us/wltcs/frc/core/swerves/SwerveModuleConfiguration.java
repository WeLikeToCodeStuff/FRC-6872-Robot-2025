package us.wltcs.frc.core.swerves;

import us.wltcs.frc.core.math.vector2.Vector2f;

public class SwerveModuleConfiguration {
  // Location of module in inches
  public final Vector2f moduleLocation;

  public SwerveModuleConfiguration(Vector2f moduleLocation) {
    this.moduleLocation = moduleLocation;
  }
}
