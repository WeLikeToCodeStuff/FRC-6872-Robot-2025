package us.wltcs.frc.core.math.vector2;

public class Vector2i implements Vector2 {
  public int x, y;

  public Vector2i(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Vector2i plus(Vector2i vector) {
    return new Vector2i(this.x + vector.x, this.y + vector.y);
  }

  public Vector2i minus(Vector2i vector) {
    return new Vector2i(this.x - vector.x, this.y - vector.y);
  }

  public Vector2i times(Vector2i vector) {
    return new Vector2i(this.x * vector.x, this.y * vector.y);
  }

  public Vector2i times(int x) {
    return new Vector2i(this.x * x, this.y * x);
  }

  public Vector2i div(int x) {
    return new Vector2i(this.x / x, this.y / x);
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", x, y);
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
