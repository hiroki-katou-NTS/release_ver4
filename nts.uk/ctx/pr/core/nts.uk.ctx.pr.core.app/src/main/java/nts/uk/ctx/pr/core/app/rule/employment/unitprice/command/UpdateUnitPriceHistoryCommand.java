/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.pr.core.app.rule.employment.unitprice.command;

import nts.uk.ctx.core.dom.company.CompanyCode;
import nts.uk.ctx.core.dom.util.PrimitiveUtil;
import nts.uk.ctx.pr.core.dom.insurance.MonthRange;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.ApplySetting;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.Money;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.SettingType;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceCode;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceHistory;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceHistoryGetMemento;
import nts.uk.ctx.pr.core.dom.rule.employment.unitprice.UnitPriceName;
import nts.uk.shr.com.primitive.Memo;

public class UpdateUnitPriceHistoryCommand extends UnitPriceHistoryBaseCommand {

	/**
	 * To domain.
	 *
	 * @param companyCode
	 *            the company code
	 * @param historyId
	 *            the history id
	 * @param unitPriceCode
	 *            the unit price code
	 * @return the unit price history
	 */
	public UnitPriceHistory toDomain(CompanyCode companyCode, String historyId, UnitPriceCode unitPriceCode) {
		UpdateUnitPriceHistoryCommand command = this;

		// Transfer data
		UnitPriceHistory updatedHistory = new UnitPriceHistory(new UnitPriceHistoryGetMemento() {
			@Override
			public Long getVersion() {
				return command.getVersion();
			}

			@Override
			public UnitPriceName getUnitPriceName() {
				return new UnitPriceName(command.getUnitPriceName());
			}

			@Override
			public UnitPriceCode getUnitPriceCode() {
				return unitPriceCode;
			}

			@Override
			public Memo getMemo() {
				return new Memo(command.getMemo());
			}

			@Override
			public String getId() {
				return historyId;
			}

			@Override
			public SettingType getFixPaySettingType() {
				return SettingType.valueOf(command.getFixPaySettingType());
			}

			@Override
			public ApplySetting getFixPayAtrMonthly() {
				return ApplySetting.valueOf(command.getFixPayAtrMonthly());
			}

			@Override
			public ApplySetting getFixPayAtrHourly() {
				return ApplySetting.valueOf(command.getFixPayAtrHourly());
			}

			@Override
			public ApplySetting getFixPayAtrDayMonth() {
				return ApplySetting.valueOf(command.getFixPayAtrDayMonth());
			}

			@Override
			public ApplySetting getFixPayAtrDaily() {
				return ApplySetting.valueOf(command.getFixPayAtrDaily());
			}

			@Override
			public ApplySetting getFixPayAtr() {
				return ApplySetting.valueOf(command.getFixPayAtr());
			}

			@Override
			public CompanyCode getCompanyCode() {
				return companyCode;
			}

			@Override
			public Money getBudget() {
				return new Money(command.getBudget());
			}

			@Override
			public MonthRange getApplyRange() {
				return MonthRange.range(command.getStartMonth(), command.getEndMonth(),
						PrimitiveUtil.DEFAULT_YM_SEPARATOR_CHAR);
			}
		});

		return updatedHistory;
	}
}
