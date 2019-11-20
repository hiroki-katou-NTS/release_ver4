package nts.uk.screen.at.app.dailyperformance.correction.monthflex;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.app.find.monthly.root.MonthlyRecordWorkDto;
import nts.uk.screen.at.app.dailyperformance.correction.agreement.AgreementInfomationDto;
import nts.uk.screen.at.app.dailyperformance.correction.flex.change.FlexShortageDto;
import nts.uk.screen.at.app.monthlyperformance.correction.dto.FormatDailyDto;
import nts.uk.screen.at.app.monthlyperformance.correction.query.MonthlyModifyResult;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DPMonthResult {
	private FlexShortageDto flexShortage;
	private List<MonthlyModifyResult> results;
	private boolean hasItem = false;
	private Integer month;
	private List<FormatDailyDto> formatDaily;
	private AgreementInfomationDto agreementInfo;
	private boolean needCallCalc = false;
	private Optional<MonthlyRecordWorkDto> domainMonthOpt = Optional.empty();

	public DPMonthResult(FlexShortageDto flexShortage, List<MonthlyModifyResult> results, boolean hasItem,
			Integer month, List<FormatDailyDto> formatDaily, AgreementInfomationDto agreementInfo, Optional<MonthlyRecordWorkDto> monthOpt) {
		this.flexShortage = flexShortage;
		this.results = results;
		this.hasItem = hasItem;
		this.month = month;
		this.formatDaily = formatDaily;
		this.agreementInfo = agreementInfo;
		this.needCallCalc = (flexShortage != null && flexShortage.isShowFlex()) || hasItem
				|| (agreementInfo != null && agreementInfo.isShowAgreement());
		this.domainMonthOpt = monthOpt;
	}
}
