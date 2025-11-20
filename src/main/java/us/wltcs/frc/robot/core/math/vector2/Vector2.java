package us.wltcs.frc.robot.core.math.vector2;

import us.wltcs.frc.robot.core.math.MathF;

public interface Vector2 {
  Vector2i Left = new Vector2i(-1, 0);
  Vector2i Right = new Vector2i(1, 0);
  Vector2i Up = new Vector2i(0, 1);
  Vector2i Down = new Vector2i(0, -1);
  Vector2i Zero = new Vector2i(0, 0);

  double getX();

  double getY();

  String toString();

  default boolean equals(Vector2 vector) {
    Vector2d equalVector = new Vector2d(getX(), getY());
    return equalVector.x == vector.getX() && equalVector.y == vector.getY();
  }

  default double length() {
    double x = getX() * getX() + getY() * getY();
    return x * MathF.invSqrt(x);
  }

  default Vector2d normalized() {
    Vector2d vector = new Vector2d(getX(), getY());
    return vector / length();
  }
}
