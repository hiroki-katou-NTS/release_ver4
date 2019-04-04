package nts.uk.ctx.at.record.infra.entity.daily.divergencetime;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.val;
import nts.arc.time.GeneralDate;
import nts.gul.reflection.FieldReflection;
import nts.gul.text.StringUtil;
import nts.uk.ctx.at.record.dom.divergencetime.DiverdenceReasonCode;
import nts.uk.ctx.at.record.dom.divergencetime.DivergenceReasonContent;
import nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTime;
import nts.uk.ctx.at.record.dom.divergencetimeofdaily.DivergenceTimeOfDaily;
import nts.uk.ctx.at.record.infra.entity.daily.actualworktime.KrcdtDayAttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTime;
import nts.uk.ctx.at.shared.dom.common.time.AttendanceTimeOfExistMinus;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 日別実績の乖離時間
 * @author keisuke_hoshina
 *
 */
@Entity
@Table(name = "KRCDT_DAY_DIVERGENCE_TIME")
public class KrcdtDayDivergenceTime extends UkJpaEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/*主キー*/
	@EmbeddedId
	public KrcdtDayDivergenceTimePK krcdtDayDivergenceTimePK;
	
	/*乖離時間１*/
	@Column(name = "DIVERGENCE_TIME1")
	public int divergenceTime1;
	/*乖離時間2*/
	@Column(name = "DIVERGENCE_TIME2")
	public int divergenceTime2;
	/*乖離時間3*/
	@Column(name = "DIVERGENCE_TIME3")
	public int divergenceTime3;
	/*乖離時間4*/
	@Column(name = "DIVERGENCE_TIME4")
	public int divergenceTime4;
	/*乖離時間5*/
	@Column(name = "DIVERGENCE_TIME5")
	public int divergenceTime5;
	/*乖離時間6*/
	@Column(name = "DIVERGENCE_TIME6")
	public int divergenceTime6;
	/*乖離時間7*/
	@Column(name = "DIVERGENCE_TIME7")
	public int divergenceTime7;
	/*乖離時間8*/
	@Column(name = "DIVERGENCE_TIME8")
	public int divergenceTime8;
	/*乖離時間9*/
	@Column(name = "DIVERGENCE_TIME9")
	public int divergenceTime9;
	/*乖離時間１0*/
	@Column(name = "DIVERGENCE_TIME10")
	public int divergenceTime10;
	
	/*控除時間１*/
	@Column(name = "DEDUCTION_TIME1")
	public int deductionTime1;
	/*控除時間2*/
	@Column(name = "DEDUCTION_TIME2")
	public int deductionTime2;
	/*控除時間3*/
	@Column(name = "DEDUCTION_TIME3")
	public int deductionTime3;
	/*控除時間4*/
	@Column(name = "DEDUCTION_TIME4")
	public int deductionTime4;
	/*控除時間5*/
	@Column(name = "DEDUCTION_TIME5")
	public int deductionTime5;
	/*控除時間6*/
	@Column(name = "DEDUCTION_TIME6")
	public int deductionTime6;
	/*控除時間7*/
	@Column(name = "DEDUCTION_TIME7")
	public int deductionTime7;
	/*控除時間8*/
	@Column(name = "DEDUCTION_TIME8")
	public int deductionTime8;
	/*控除時間9*/
	@Column(name = "DEDUCTION_TIME9")
	public int deductionTime9;
	/*控除時間１0*/
	@Column(name = "DEDUCTION_TIME10")
	public int deductionTime10;
	
	/*控除後乖離時間１*/
	@Column(name = "AFTER_DEDUCTION_TIME1")
	public int afterDeductionTime1;
	/*控除後乖離時間2*/
	@Column(name = "AFTER_DEDUCTION_TIME2")
	public int afterDeductionTime2;
	/*控除後乖離時間3*/
	@Column(name = "AFTER_DEDUCTION_TIME3")
	public int afterDeductionTime3;
	/*控除後乖離時間4*/
	@Column(name = "AFTER_DEDUCTION_TIME4")
	public int afterDeductionTime4;
	/*控除後乖離時間5*/
	@Column(name = "AFTER_DEDUCTION_TIME5")
	public int afterDeductionTime5;
	/*控除後乖離時間6*/
	@Column(name = "AFTER_DEDUCTION_TIME6")
	public int afterDeductionTime6;
	/*控除後乖離時間7*/
	@Column(name = "AFTER_DEDUCTION_TIME7")
	public int afterDeductionTime7;
	/*控除後乖離時間8*/
	@Column(name = "AFTER_DEDUCTION_TIME8")
	public int afterDeductionTime8;
	/*控除後乖離時間9*/
	@Column(name = "AFTER_DEDUCTION_TIME9")
	public int afterDeductionTime9;
	/*控除後乖離時間１0*/
	@Column(name = "AFTER_DEDUCTION_TIME10")
	public int afterDeductionTime10;
	
	/*乖離理由コード1*/
	@Column(name = "REASON_CODE1")
	public String reasonCode1;
	/*乖離理由コード2*/
	@Column(name = "REASON_CODE2")
	public String reasonCode2;
	/*乖離理由コード3*/
	@Column(name = "REASON_CODE3")
	public String reasonCode3;
	/*乖離理由コード4*/
	@Column(name = "REASON_CODE4")
	public String reasonCode4;
	/*乖離理由コード5*/
	@Column(name = "REASON_CODE5")
	public String reasonCode5;
	/*乖離理由コード6*/
	@Column(name = "REASON_CODE6")
	public String reasonCode6;
	/*乖離理由コード7*/
	@Column(name = "REASON_CODE7")
	public String reasonCode7;
	/*乖離理由コード8*/
	@Column(name = "REASON_CODE8")
	public String reasonCode8;
	/*乖離理由コード9*/
	@Column(name = "REASON_CODE9")
	public String reasonCode9;
	/*乖離理由コード10*/
	@Column(name = "REASON_CODE10")
	public String reasonCode10;

	/*乖離理由1*/
	@Column(name = "REASON1")
	public String reason1;
	/*乖離理由2*/
	@Column(name = "REASON2")
	public String reason2;
	/*乖離理由3*/
	@Column(name = "REASON3")
	public String reason3;
	/*乖離理由4*/
	@Column(name = "REASON4")
	public String reason4;
	/*乖離理由5*/
	@Column(name = "REASON5")
	public String reason5;
	/*乖離理由6*/
	@Column(name = "REASON6")
	public String reason6;
	/*乖離理由7*/
	@Column(name = "REASON7")
	public String reason7;
	/*乖離理由8*/
	@Column(name = "REASON8")
	public String reason8;
	/*乖離理由9*/
	@Column(name = "REASON9")
	public String reason9;
	/*乖離理由10*/
	@Column(name = "REASON10")
	public String reason10;
	
	@OneToOne(mappedBy="krcdtDayDivergenceTime")
	public KrcdtDayAttendanceTime krcdtDayAttendanceTime;
	
	@Override
	protected Object getKey() {
		return this.krcdtDayDivergenceTimePK;
	}
	
	public static KrcdtDayDivergenceTime toEntity(String employeeId,GeneralDate targetDate, DivergenceTimeOfDaily domain) {
		val entity = new KrcdtDayDivergenceTime();
		/*主キーセット*/
		entity.krcdtDayDivergenceTimePK = new KrcdtDayDivergenceTimePK(employeeId, targetDate);
		/*データセット*/
		entity.setData(domain);
		return entity;
	}
	
	
	public void setData(DivergenceTimeOfDaily domain) {
		if(domain == null || domain.getDivergenceTime() == null || domain.getDivergenceTime().isEmpty() )
			return;
		//値のセットループ
		for(int loopNumber = 1 ; loopNumber <= 10 ; loopNumber++) {
			val divergenceTime = getDivergenceTime(domain , loopNumber);
			setValue(divergenceTime, loopNumber);
		}
	}
	
	
	private void setValue(Optional<DivergenceTime> frame,int number) {
		if(frame.isPresent()) {
			setPerDivergenceTimeData("divergenceTime",number,frame.get().getDivTime() == null ? 0 : frame.get().getDivTime().valueAsMinutes());
			setPerDivergenceTimeData("deductionTime",number,frame.get().getDeductionTime() == null ? 0 : frame.get().getDeductionTime().valueAsMinutes());
			setPerDivergenceTimeData("afterDeductionTime",number,frame.get().getDivTimeAfterDeduction() == null ? 0 : frame.get().getDivTimeAfterDeduction().valueAsMinutes());
			setPerDivergenceTimeData("reasonCode",number,frame.get().getDivResonCode().isPresent() ? null : frame.get().getDivResonCode().get().v());
			setPerDivergenceTimeData("reason",number,frame.get().getDivReason().isPresent() ? null : frame.get().getDivReason().get().v());
		}
		else {
			setPerDivergenceTimeData("divergenceTime",number,0);
			setPerDivergenceTimeData("deductionTime",number,0);
			setPerDivergenceTimeData("afterDeductionTime",number,0);
			setPerDivergenceTimeData("reasonCode",number, null);
			setPerDivergenceTimeData("reason",number, null);
		}
	}
	
	
	private void setPerDivergenceTimeData(String fieldName ,int number ,Object value) {
		//自分自身の値セット先(フィールド)取得
		Field field = FieldReflection.getField(this.getClass(), fieldName + number);
		//値セット
		FieldReflection.setField(field, this, value);
	}
	
	private Optional<DivergenceTime> getDivergenceTime(DivergenceTimeOfDaily domain,int number){
		return domain.getDivergenceTime().stream().filter(tc -> tc.getDivTimeId() == number).findFirst();
	}

	
	
	
	public DivergenceTimeOfDaily toDomain() {
		List<DivergenceTime> divergenceTimeList = new ArrayList<>();
		divergenceTimeList.add(new DivergenceTime(new AttendanceTimeOfExistMinus(this.afterDeductionTime1),
				  new AttendanceTimeOfExistMinus(this.deductionTime1),
				  new AttendanceTimeOfExistMinus(this.divergenceTime1),
				  1,
				  StringUtil.isNullOrEmpty(this.reason1, true) ? null : new DivergenceReasonContent(this.reason1),
				  StringUtil.isNullOrEmpty(this.reasonCode1, true) ? null : new DiverdenceReasonCode(this.reasonCode1)));

		divergenceTimeList.add(new DivergenceTime(new AttendanceTimeOfExistMinus(this.afterDeductionTime2),
						  new AttendanceTimeOfExistMinus(this.deductionTime2),
						  new AttendanceTimeOfExistMinus(this.divergenceTime2),
						  2,
						StringUtil.isNullOrEmpty(this.reason2, true) ? null : new DivergenceReasonContent(this.reason2),
						StringUtil.isNullOrEmpty(this.reasonCode2, true) ? null : new DiverdenceReasonCode(this.reasonCode2)));
		divergenceTimeList.add(new DivergenceTime(new AttendanceTimeOfExistMinus(this.afterDeductionTime3),
						  new AttendanceTimeOfExistMinus(this.deductionTime3),
						  new AttendanceTimeOfExistMinus(this.divergenceTime3),
						  3,
						StringUtil.isNullOrEmpty(this.reason3, true) ? null : new DivergenceReasonContent(this.reason3),
						StringUtil.isNullOrEmpty(this.reasonCode3, true) ? null : new DiverdenceReasonCode(this.reasonCode3)));
		divergenceTimeList.add(new DivergenceTime(new AttendanceTimeOfExistMinus(this.afterDeductionTime4),
						  new AttendanceTimeOfExistMinus(this.deductionTime4),
						  new AttendanceTimeOfExistMinus(this.divergenceTime4),
						  4,
						  StringUtil.isNullOrEmpty(this.reason4, true) ? null : new DivergenceReasonContent(this.reason4),
						  StringUtil.isNullOrEmpty(this.reasonCode4, true) ? null : new DiverdenceReasonCode(this.reasonCode4)));
		divergenceTimeList.add(new DivergenceTime(new AttendanceTimeOfExistMinus(this.afterDeductionTime5),
						  new AttendanceTimeOfExistMinus(this.deductionTime5),
						  new AttendanceTimeOfExistMinus(this.divergenceTime5),
						  5,
						StringUtil.isNullOrEmpty(this.reason5, true) ? null : new DivergenceReasonContent(this.reason5),
						StringUtil.isNullOrEmpty(this.reasonCode5, true) ? null : new DiverdenceReasonCode(this.reasonCode5)));
		divergenceTimeList.add(new DivergenceTime(new AttendanceTimeOfExistMinus(this.afterDeductionTime6),
						  new AttendanceTimeOfExistMinus(this.deductionTime6),
						  new AttendanceTimeOfExistMinus(this.divergenceTime6),
						  6,
						StringUtil.isNullOrEmpty(this.reason6, true) ? null : new DivergenceReasonContent(this.reason6),
						StringUtil.isNullOrEmpty(this.reasonCode6, true) ? null : new DiverdenceReasonCode(this.reasonCode6)));
		divergenceTimeList.add(new DivergenceTime(new AttendanceTimeOfExistMinus(this.afterDeductionTime7),
						  new AttendanceTimeOfExistMinus(this.deductionTime7),
						  new AttendanceTimeOfExistMinus(this.divergenceTime7),
						  7,
						StringUtil.isNullOrEmpty(this.reason7, true) ? null : new DivergenceReasonContent(this.reason7),
						StringUtil.isNullOrEmpty(this.reasonCode7, true) ? null : new DiverdenceReasonCode(this.reasonCode7)));
		divergenceTimeList.add(new DivergenceTime(new AttendanceTimeOfExistMinus(this.afterDeductionTime8),
						  new AttendanceTimeOfExistMinus(this.deductionTime8),
						  new AttendanceTimeOfExistMinus(this.divergenceTime8),
						  8,
						StringUtil.isNullOrEmpty(this.reason8, true) ? null : new DivergenceReasonContent(this.reason8),
						StringUtil.isNullOrEmpty(this.reasonCode8, true) ? null : new DiverdenceReasonCode(this.reasonCode8)));
		divergenceTimeList.add(new DivergenceTime(new AttendanceTimeOfExistMinus(this.afterDeductionTime9),
						  new AttendanceTimeOfExistMinus(this.deductionTime9),
						  new AttendanceTimeOfExistMinus(this.divergenceTime9),
						  9,
						StringUtil.isNullOrEmpty(this.reason9, true) ? null : new DivergenceReasonContent(this.reason9),
						StringUtil.isNullOrEmpty(this.reasonCode9, true) ? null : new DiverdenceReasonCode(this.reasonCode9)));
		divergenceTimeList.add(new DivergenceTime(new AttendanceTimeOfExistMinus(this.afterDeductionTime10),
						  new AttendanceTimeOfExistMinus(this.deductionTime10),
						  new AttendanceTimeOfExistMinus(this.divergenceTime10),
						  10,
						StringUtil.isNullOrEmpty(this.reason10, true) ? null : new DivergenceReasonContent(this.reason10),
						StringUtil.isNullOrEmpty(this.reasonCode10, true) ? null : new DiverdenceReasonCode(this.reasonCode10)));
		
		return new DivergenceTimeOfDaily(divergenceTimeList);
	}
}
