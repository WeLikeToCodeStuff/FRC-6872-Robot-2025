package us.wltcs.frc.core.math;

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

  public double inchesToMeters(double meters) {
    return meters * 39.3700787402;
  }

  public double metersToInches(double inches) {
    return inches * 0.0254;
  }
}
