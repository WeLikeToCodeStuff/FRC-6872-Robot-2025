package us.wltcs.frc.robot.core.math.vector2;

public class Vector2f implements Vector2 {
  public float x, y;

  public Vector2f(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public Vector2f plus(Vector2f vector) {
    return new Vector2f(this.x + vector.x, this.y + vector.y);
  }

  public Vector2f minus(Vector2f vector) {
    return new Vector2f(this.x - vector.x, this.y - vector.y);
  }

  public Vector2f times(Vector2f vector) {
    return new Vector2f(this.x * vector.x, this.y * vector.y);
  }

  public Vector2f times(float x) {
    return new Vector2f(this.x * x, this.y * x);
  }

  public Vector2f div(float x) {
    return new Vector2f(this.x / x, this.y / x);
  }

  @Override
  public String toString() {
    return String.format("(%.6f, %.6f)", x, y);
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
