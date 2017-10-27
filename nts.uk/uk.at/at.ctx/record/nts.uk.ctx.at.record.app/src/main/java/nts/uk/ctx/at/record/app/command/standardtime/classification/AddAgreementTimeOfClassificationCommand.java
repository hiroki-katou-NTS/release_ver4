package nts.uk.ctx.at.record.app.command.standardtime.classification;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author nampt
 *
 */
@Data
@NoArgsConstructor
public class AddAgreementTimeOfClassificationCommand {
	
	private String classificationCode;
	
	private int laborSystemAtr;

	private int alarmWeek;

	private int errorWeek;

	private int limitWeek;

	private int alarmTwoWeeks;

	private int errorTwoWeeks;

	private int limitTwoWeeks;

	private int alarmFourWeeks;

	private int errorFourWeeks;

	private int limitFourWeeks;

	private int alarmOneMonth;

	private int errorOneMonth;

	private int limitOneMonth;

	private int alarmTwoMonths;

	private int errorTwoMonths;

	private int limitTwoMonths;

	private int alarmThreeMonths;

	private int errorThreeMonths;

	private int limitThreeMonths;

	private int alarmOneYear;

	private int errorOneYear;

	private int limitOneYear;
}
