package us.wltcs.frc.robot.core.math.vector3;

public class Vector3i implements Vector3 {
  public int x, y, z;

  public Vector3i(int x, int y, int z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public Vector3i plus(Vector3i vector) {
    return new Vector3i(this.x + vector.x, this.y + vector.y, this.z + vector.z);
  }

  public Vector3i minus(Vector3i vector) {
    return new Vector3i(this.x - vector.x, this.y - vector.y, this.z - vector.z);
  }

  public Vector3i times(Vector3i vector) {
    return new Vector3i(this.x * vector.x, this.y * vector.y, this.z * vector.z);
  }

  public Vector3i times(int x) {
    return new Vector3i(this.x * x, this.y * x, this.z * x);
  }

  public Vector3i div(int x) {
    return new Vector3i(this.x / x, this.y / x, this.z / x);
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

  @Override
  public double getZ() {
    return z;
  }
}
