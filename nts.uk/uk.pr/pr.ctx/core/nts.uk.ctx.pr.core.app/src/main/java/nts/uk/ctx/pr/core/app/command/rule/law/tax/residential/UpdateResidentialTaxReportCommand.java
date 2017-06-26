package nts.uk.ctx.pr.core.app.command.rule.law.tax.residential;

import java.util.List;

import lombok.Getter;

/**
 * UpdateResidentialTaxReportCommand
 * 
 * @author lanlt
 *
 */
@Getter
public class UpdateResidentialTaxReportCommand {
	private List<String> resiTaxCodes;
	private String resiTaxReportCode;
	private int yearKey;
}
