package nts.uk.shr.com.validate.constraint.implement;

import java.math.BigDecimal;
import java.util.Optional;

import lombok.Getter;
import nts.gul.util.value.ValueWithType;
import nts.uk.shr.com.validate.constraint.DataConstraint;
import nts.uk.shr.com.validate.constraint.ValidatorType;
import nts.uk.shr.com.validate.validator.ErrorIdFactory;

@Getter
public class NumericConstraint extends DataConstraint {

	private boolean minusAvailable;

	private boolean money;

	private BigDecimal min;

	private BigDecimal max;

	private int integerPart;

	private int decimalPart;

	public NumericConstraint(int column, boolean minusAvailable, boolean money, BigDecimal min, BigDecimal max,
			int integerPart, int decimalPart) {
		super(column, ValidatorType.NUMERIC);
		this.minusAvailable = minusAvailable;
		this.money = money;
		this.min = min;
		this.max = max;
		this.integerPart = integerPart;
		this.decimalPart = decimalPart;
	}

	public NumericConstraint(int column, boolean minusAvailable, BigDecimal min, BigDecimal max, int integerPart,
			int decimalPart) {
		super(column, ValidatorType.NUMERIC);
		this.minusAvailable = minusAvailable;
		this.min = min;
		this.max = max;
		this.integerPart = integerPart;
		this.decimalPart = decimalPart;
	}
	
	public Optional<String> validate(ValueWithType value) {
		switch (value.getType()) {
		case NUMBER:
			return validateNumber(new BigDecimal(value.getValue().toString()));
		case TEXT:
			return validateString(value.getText());
		default:
			return	Optional.of(ErrorIdFactory.NumericType); 
		}
	}

	public Optional<String> validateString(String stringValue) {

		if (!isNumeric(stringValue)) {
			return Optional.of(ErrorIdFactory.NumericType);
		}

		BigDecimal value = new BigDecimal(stringValue);

		return validateNumber(value);
	}
	
	private Optional<String> validateNumber(BigDecimal value) {

		if (!this.minusAvailable && !validateMinus(value)) {
			return Optional.of(ErrorIdFactory.MinusErrorId);
		}

		if (!validateMin(this.min, value)) {
			return Optional.of(ErrorIdFactory.NumericMinErrorId);
		}

		if (!validateMax(this.max, value)) {
			return Optional.of(ErrorIdFactory.NumericMaxErrorId);
		}

		if (!validateIntegerPart(this.integerPart, value)) {
			return Optional.of(ErrorIdFactory.IntegerPartErrorId);
		}

		if (!validateDecimalPart(this.decimalPart, value)) {
			return Optional.of(ErrorIdFactory.DecimalPartErrorId);
		}

		return Optional.empty();
	}

	private boolean validateMinus(BigDecimal value) {
		return value.compareTo(BigDecimal.ZERO) >= 0;
	}

	private boolean validateMin(BigDecimal max, BigDecimal value) {
		return value.compareTo(max) >= 0;
	}

	private boolean validateMax(BigDecimal max, BigDecimal value) {
		return value.compareTo(max) <= 0;
	}

	private boolean validateIntegerPart(int integerPart, BigDecimal value) {
		value = value.stripTrailingZeros();
		int integerPartOfValue = value.precision() - value.scale();
		return integerPartOfValue <= integerPart;
	}

	private boolean validateDecimalPart(int decimalPart, BigDecimal value) {
		value = value.stripTrailingZeros();
		return value.scale() <= decimalPart;
	}

	public boolean isNumeric(String strNum) {
		return strNum.matches("-?\\d+(\\.\\d+)?");
	}

}
