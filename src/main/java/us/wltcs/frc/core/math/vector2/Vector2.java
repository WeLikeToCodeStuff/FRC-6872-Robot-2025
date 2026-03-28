package us.wltcs.frc.core.math.vector2;

public interface Vector2 {
  double getX();

  double getY();

  String toString();

  default boolean equals(Vector2 vector) {
    Vector2d equalVector = new Vector2d(getX(), getY());
    return equalVector.x == vector.getX() && equalVector.y == vector.getY();
  }

  default double length() {
    double x = getX() * getX() + getY() * getY();

    if (x < 0)
      return 0;

    return Math.sqrt(x);
  }

  default Vector2d normalized() {
    Vector2d vector = new Vector2d(getX(), getY());

    if (vector.length() == 0)
      return new Vector2d(0, 0);

    return vector.div(length());
  }
}
