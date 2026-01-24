package us.wltcs.frc.core.math.vector3;

public interface Vector3 {
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

    if (x < 0)
      return 0;

    return Math.sqrt(x);
  }

  default Vector3d normalized() {
    Vector3d vector = new Vector3d(getX(), getY(), getZ());
    return vector.div(length());
  }
}
