package nts.uk.ctx.at.function.app.command.monthlycorrection.fixedformatmonthly;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly.MonPfmCorrectionFormat;
import nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly.MonPfmCorrectionFormatName;
import nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly.MonthlyActualResults;
import nts.uk.ctx.at.function.dom.monthlycorrection.fixedformatmonthly.MonthlyPerformanceFormatCode;

@Getter
@Setter
@NoArgsConstructor
public class MonPfmCorrectionFormatCmd {
	/**会社ID*/
	private String companyID;
	/**コード*/
	private String monthlyPfmFormatCode;
	/**名称*/
	private String monPfmCorrectionFormatName;
	/**表示項目*/
	private MonthlyActualResultsCmd displayItem;
	
	public MonPfmCorrectionFormatCmd(String companyID, String monthlyPfmFormatCode, String monPfmCorrectionFormatName, MonthlyActualResultsCmd displayItem) {
		super();
		this.companyID = companyID;
		this.monthlyPfmFormatCode = monthlyPfmFormatCode;
		this.monPfmCorrectionFormatName = monPfmCorrectionFormatName;
		this.displayItem = displayItem;
	}
	
	public static MonPfmCorrectionFormat fromCommand(MonPfmCorrectionFormatCmd command) {
		return new MonPfmCorrectionFormat(
				command.getCompanyID(),
				new MonthlyPerformanceFormatCode(command.getMonthlyPfmFormatCode()),
				new MonPfmCorrectionFormatName( command.getMonPfmCorrectionFormatName()),
				MonthlyActualResultsCmd.fromCommand(command.getDisplayItem())
				);
	}
	
	
}
