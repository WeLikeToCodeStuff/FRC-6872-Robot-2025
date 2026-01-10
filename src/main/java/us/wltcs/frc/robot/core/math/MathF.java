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
}
