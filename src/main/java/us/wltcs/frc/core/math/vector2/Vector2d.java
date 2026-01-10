package us.wltcs.frc.core.math.vector2;

public class Vector2d implements Vector2 {
  public double x, y;

  public Vector2d(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Vector2d plus(Vector2d vector) {
    return new Vector2d(this.x + vector.x, this.y + vector.y);
  }

  public Vector2d minus(Vector2d vector) {
    return new Vector2d(this.x - vector.x, this.y - vector.y);
  }

  public Vector2d times(Vector2d vector) {
    return new Vector2d(this.x * vector.x, this.y * vector.y);
  }

  public Vector2d times(double x) {
    return new Vector2d(this.x * x, this.y * x);
  }

  public Vector2d div(double x) {
    return new Vector2d(this.x / x, this.y / x);
  }

  @Override
  public String toString() {
    return String.format("(%.15f, %.15f)", x, y);
  }

  @Override
  public double getX() {
    return x;
  }

  @Override
  public double getY() {
    return y;
  }
}
