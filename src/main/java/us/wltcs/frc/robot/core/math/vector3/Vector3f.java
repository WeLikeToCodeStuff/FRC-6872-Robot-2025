package us.wltcs.frc.robot.core.math.vector3;

public class Vector3f implements Vector3 {
  public float x, y, z;

  public Vector3f(float x, float y, float z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vector3f plus(Vector3f vector) {
    return new Vector3f(this.x + vector.x, this.y + vector.y, this.z + vector.z);
  }

  public Vector3f minus(Vector3f vector) {
    return new Vector3f(this.x - vector.x, this.y - vector.y, this.z - vector.z);
  }

  public Vector3f times(Vector3f vector) {
    return new Vector3f(this.x * vector.x, this.y * vector.y, this.z * vector.z);
  }

  public Vector3f times(float x) {
    return new Vector3f(this.x * x, this.y * x, this.z * x);
  }

  public Vector3f div(float x) {
    return new Vector3f(this.x / x, this.y / x, this.z / x);
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

  @Override
  public double getZ() {
    return z;
  }
}
