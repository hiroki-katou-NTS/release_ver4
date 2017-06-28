package nts.uk.ctx.at.record.dom.standardtime;

import java.math.BigDecimal;

import lombok.Getter;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.AlarmFourWeeks;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.AlarmOneMonth;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.AlarmOneYear;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.AlarmThreeMonths;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.AlarmTwoMonths;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.AlarmTwoWeeks;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.AlarmWeek;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.ErrorFourWeeks;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.ErrorOneMonth;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.ErrorOneYear;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.ErrorThreeMonths;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.ErrorTwoMonths;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.ErrorTwoWeeks;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.ErrorWeek;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitFourWeeks;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitOneMonth;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitOneYear;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitThreeMonths;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitTwoMonths;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitTwoWeeks;
import nts.uk.ctx.at.record.dom.standardtime.primitivevalue.LimitWeek;

/**
 * 
 * @author nampt
 *
 */
@Getter
public class BasicAgreementSetting extends AggregateRoot {

	private String basicSettingId;

	private AlarmWeek alarmWeek;

	private ErrorWeek errorWeek;

	private LimitWeek limitWeek;

	private AlarmTwoWeeks alarmTwoWeeks;

	private ErrorTwoWeeks errorTwoWeeks;

	private LimitTwoWeeks limitTwoWeeks;

	private AlarmFourWeeks alarmFourWeeks;

	private ErrorFourWeeks errorFourWeeks;

	private LimitFourWeeks limitFourWeeks;

	private AlarmOneMonth alarmOneMonth;

	private ErrorOneMonth errorOneMonth;

	private LimitOneMonth limitOneMonth;

	private AlarmTwoMonths alarmTwoMonths;

	private ErrorTwoMonths errorTwoMonths;

	private LimitTwoMonths limitTwoMonths;

	private AlarmThreeMonths alarmThreeMonths;

	private ErrorThreeMonths errorThreeMonths;

	private LimitThreeMonths limitThreeMonths;

	private AlarmOneYear alarmOneYear;

	private ErrorOneYear errorOneYear;

	private LimitOneYear limitOneYear;

	public BasicAgreementSetting(String basicSettingId, AlarmWeek alarmWeek, ErrorWeek errorWeek, LimitWeek limitWeek,
			AlarmTwoWeeks alarmTwoWeeks, ErrorTwoWeeks errorTwoWeeks, LimitTwoWeeks limitTwoWeeks,
			AlarmFourWeeks alarmFourWeeks, ErrorFourWeeks errorFourWeeks, LimitFourWeeks limitFourWeeks,
			AlarmOneMonth alarmOneMonth, ErrorOneMonth errorOneMonth, LimitOneMonth limitOneMonth,
			AlarmTwoMonths alarmTwoMonths, ErrorTwoMonths errorTwoMonths, LimitTwoMonths limitTwoMonths,
			AlarmThreeMonths alarmThreeMonths, ErrorThreeMonths errorThreeMonths, LimitThreeMonths limitThreeMonths,
			AlarmOneYear alarmOneYear, ErrorOneYear errorOneYear, LimitOneYear limitOneYear) {
		super();
		this.basicSettingId = basicSettingId;
		this.alarmWeek = alarmWeek;
		this.errorWeek = errorWeek;
		this.limitWeek = limitWeek;
		this.alarmTwoWeeks = alarmTwoWeeks;
		this.errorTwoWeeks = errorTwoWeeks;
		this.limitTwoWeeks = limitTwoWeeks;
		this.alarmFourWeeks = alarmFourWeeks;
		this.errorFourWeeks = errorFourWeeks;
		this.limitFourWeeks = limitFourWeeks;
		this.alarmOneMonth = alarmOneMonth;
		this.errorOneMonth = errorOneMonth;
		this.limitOneMonth = limitOneMonth;
		this.alarmTwoMonths = alarmTwoMonths;
		this.errorTwoMonths = errorTwoMonths;
		this.limitTwoMonths = limitTwoMonths;
		this.alarmThreeMonths = alarmThreeMonths;
		this.errorThreeMonths = errorThreeMonths;
		this.limitThreeMonths = limitThreeMonths;
		this.alarmOneYear = alarmOneYear;
		this.errorOneYear = errorOneYear;
		this.limitOneYear = limitOneYear;
	}

	public static BasicAgreementSetting createFromJavaType(String basicSettingId, BigDecimal alarmWeek,
			BigDecimal errorWeek, BigDecimal limitWeek, BigDecimal alarmTwoWeeks, BigDecimal errorTwoWeeks,
			BigDecimal limitTwoWeeks, BigDecimal alarmFourWeeks, BigDecimal errorFourWeeks, BigDecimal limitFourWeeks,
			BigDecimal alarmOneMonth, BigDecimal errorOneMonth, BigDecimal limitOneMonth, BigDecimal alarmTwoMonths,
			BigDecimal errorTwoMonths, BigDecimal limitTwoMonths, BigDecimal alarmThreeMonths,
			BigDecimal errorThreeMonths, BigDecimal limitThreeMonths, BigDecimal alarmOneYear, BigDecimal errorOneYear,
			BigDecimal limitOneYear) {
		return new BasicAgreementSetting(basicSettingId, new AlarmWeek(alarmWeek), new ErrorWeek(errorWeek), new LimitWeek(limitWeek),
				new AlarmTwoWeeks(alarmTwoWeeks), new ErrorTwoWeeks(errorTwoWeeks), new LimitTwoWeeks(limitTwoWeeks),
				new AlarmFourWeeks(alarmFourWeeks),new ErrorFourWeeks(errorFourWeeks),new LimitFourWeeks(limitFourWeeks),
				new AlarmOneMonth(alarmOneMonth), new ErrorOneMonth(errorOneMonth), new LimitOneMonth(limitOneMonth),
				new AlarmTwoMonths(alarmTwoMonths),new ErrorTwoMonths(errorTwoMonths),new LimitTwoMonths(limitTwoMonths),
				new AlarmThreeMonths(alarmThreeMonths),new ErrorThreeMonths(errorThreeMonths), new LimitThreeMonths(limitThreeMonths),
				new AlarmOneYear(alarmOneYear),new ErrorOneYear(errorOneYear), new LimitOneYear(limitOneYear));
	}

}
