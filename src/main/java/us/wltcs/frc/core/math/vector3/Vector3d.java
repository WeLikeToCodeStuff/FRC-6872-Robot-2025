package us.wltcs.frc.core.math.vector3;

public class Vector3d implements Vector3 {
  public double x, y, z;

  public Vector3d(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vector3d plus(Vector3d vector) {
    return new Vector3d(this.x + vector.x, this.y + vector.y, this.z + vector.z);
  }

  public Vector3d minus(Vector3d vector) {
    return new Vector3d(this.x - vector.x, this.y - vector.y, this.z - vector.z);
  }

  public Vector3d times(Vector3d vector) {
    return new Vector3d(this.x * vector.x, this.y * vector.y, this.z * vector.z);
  }

  public Vector3d times(double x) {
    return new Vector3d(this.x * x, this.y * x, this.z * x);
  }

  public Vector3d div(double x) {
    return new Vector3d(this.x / x, this.y / x, this.z / x);
  }

  @Override
  public String toString() {
    return String.format("(%.15f, %.15f, %.15f)", x, y, z);
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
