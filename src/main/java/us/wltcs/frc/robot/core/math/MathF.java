package us.wltcs.frc.robot.core.math;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class MathF {
  public double round(float initialNumber, int decimalPlaces) {
    if (decimalPlaces < 0) throw new IllegalArgumentException("Decimal places must be non-negative");

    BigDecimal bd = BigDecimal.valueOf(initialNumber);
    bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }

  public float invSqrt(float x) {
    float xHalf = 0.5f * x;
    int i = Float.floatToIntBits(x);
    i = 0x5f3759df - (i >> 1);
    x = Float.intBitsToFloat(i);
    x *= (1.5f - xHalf * x * x);
    return x;
  }

  public double invSqrt(double x) {
    double xHalf = 0.5f * x;
    long i = Double.doubleToLongBits(x);
    i = 0x5fe6ec85e7de30daL - (i >> 1);
    x = Double.longBitsToDouble(i);

    for(int y = 0; y < 4; y++)
      x *= (1.5d - xHalf * x * x);

    return x;
  }
}
