package nts.uk.ctx.pr.core.dom.base.service.internal;

import java.math.BigDecimal;

import javax.ejb.Stateless;

import nts.uk.ctx.pr.core.dom.base.service.RoundingNumber;
import nts.uk.ctx.pr.core.dom.insurance.RoundingMethod;

@Stateless
public class RoundingNumberImpl extends RoundingNumber {

	@Override
	public BigDecimal healthRounding(RoundingMethod roudingMethod, BigDecimal roundValue, double roundType) {
		double getLevel = Math.pow(10, roundType);
		final double backupValue = roundValue.doubleValue() * (getLevel / 10);
		double returnValue = BigDecimal.ZERO.doubleValue();
		switch (roudingMethod) {
		case RoundUp:
			returnValue = Math.ceil(backupValue) / (getLevel / 10);
			break;
		case Truncation:
			returnValue = Math.floor(backupValue) / (getLevel / 10);
			break;
		case RoundDown:
			if ((backupValue * getLevel) % 10 > 5) {
				returnValue = (Math.ceil(backupValue)) / (getLevel / 10);
			} else {
				returnValue = Math.floor(backupValue) / (getLevel / 10);
			}
			break;
		case Down4_Up5:
			returnValue = this.roudingDownUp(backupValue, 4) / (getLevel / 10);
			break;
		case Down5_Up6:
			returnValue = this.roudingDownUp(backupValue, 5) / (getLevel / 10);
			break;
		default:
			returnValue = Math.ceil(backupValue) / (getLevel / 10);
		}
		return BigDecimal.valueOf(returnValue);
	}

	@Override
	public BigDecimal pensionRounding(RoundingMethod roudingMethod, BigDecimal roundValue) {
		final double backupValue = roundValue.doubleValue();
		double returnValue = BigDecimal.ZERO.doubleValue();
		switch (roudingMethod) {
		case RoundUp:
			returnValue = Math.ceil(backupValue);
			break;
		case Truncation:
			returnValue = Math.floor(backupValue);
			break;
		case RoundDown:
			if ((backupValue * 10) % 10 > 5)
				returnValue = Math.ceil(backupValue);
			else
				returnValue = Math.floor(backupValue);
			break;
		case Down4_Up5:
			returnValue = this.roudingDownUp(backupValue, 4);
			break;
		case Down5_Up6:
			returnValue = this.roudingDownUp(backupValue, 5);
			break;
		default:
			returnValue = Math.ceil(backupValue);
		}
		return BigDecimal.valueOf(returnValue);
	}

	/**
	 * Rouding down up.
	 */
	private double roudingDownUp(double value, double down) {
		double newVal = Math.round(value * 10) / 10;
		if ((newVal * 10) % 10 > down)
			return Math.ceil(value);
		else
			return Math.floor(value);
	}
}
