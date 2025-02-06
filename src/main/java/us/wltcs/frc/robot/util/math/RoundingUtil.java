package us.wltcs.frc.robot.util.math;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class RoundingUtil {
  public double roundNumber(float initialNumber, int decimalPlaces) {
    if (decimalPlaces < 0) throw new IllegalArgumentException("Decimal places must be non-negative");

    BigDecimal bd = BigDecimal.valueOf(initialNumber);
    bd = bd.setScale(decimalPlaces, RoundingMode.HALF_UP);
    return bd.doubleValue();
  }
}
