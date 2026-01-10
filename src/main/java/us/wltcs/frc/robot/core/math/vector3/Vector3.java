package us.wltcs.frc.robot.core.math.vector3;

public interface Vector3 {
  Vector3i Left = new Vector3i(-1, 0, 0);
  Vector3i Right = new Vector3i(1, 0, 0);

  Vector3i Up = new Vector3i(0, 1, 0);
  Vector3i Down = new Vector3i(0, -1, 0);

  Vector3i Forward = new Vector3i(0, 0, 1);
  Vector3i Backward = new Vector3i(0, 0, -1);

  Vector3i Zero = new Vector3i(0, 0, 0);

  double getX();

  double getY();

  double getZ();

  String toString();

  default boolean equals(Vector3 vector) {
    Vector3d equalVector = new Vector3d(getX(), getY(), getZ());
    return equalVector.x == vector.getX() && equalVector.y == vector.getY() && equalVector.z == vector.getZ();
  }

  default double length() {
    double x = getX() * getX() + getY() * getY() + getZ() * getZ();
    return Math.sqrt(x);
  }

  default Vector3d normalized() {
    Vector3d vector = new Vector3d(getX(), getY(), getZ());
    return vector / length();
  }
}
