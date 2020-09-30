package nts.uk.ctx.at.shared.dom.scherec.monthlyattdcal.monthly.vacation.annualleave;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Optional;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.arc.time.GeneralDate;
import nts.arc.time.YearMonth;
import nts.uk.ctx.at.record.dom.monthly.vacation.ClosureStatus;
import nts.uk.ctx.at.shared.dom.workrule.closure.ClosureId;
import nts.uk.shr.com.time.calendar.date.ClosureDate;
import nts.arc.time.calendar.period.DatePeriod;
import nts.gul.serialize.binary.SerializableWithOptional;

/**
 * 年休月別残数データ
 * @author shuichu_ishida
 */
@Getter
public class AnnLeaRemNumEachMonth extends AggregateRoot implements SerializableWithOptional{

	/**
	 * Serializable
	 */
	private static final long serialVersionUID = 1L;
	
	/** 社員ID */
	private final String employeeId;
	/** 年月 */
	private final YearMonth yearMonth;
	/** 締めID */
	private final ClosureId closureId;
	/** 締め日付 */
	private final ClosureDate closureDate;

	/** 締め期間 */
	private DatePeriod closurePeriod;
	/** 締め処理状態 */
	private ClosureStatus closureStatus;
	/** 年休 */
	private AnnualLeave annualLeave;
	/** 実年休 */
	private AnnualLeave realAnnualLeave;
	/** 半日年休 */
	private Optional<HalfDayAnnualLeave> halfDayAnnualLeave;
	/** 実半日年休 */
	private Optional<HalfDayAnnualLeave> realHalfDayAnnualLeave;
	/** 年休付与情報 */
	private Optional<AnnualLeaveGrant> annualLeaveGrant;
	/** 上限残時間 */
	private Optional<AnnualLeaveMaxRemainingTime> maxRemainingTime;
	/** 実上限残時間 */
	private Optional<AnnualLeaveMaxRemainingTime> realMaxRemainingTime;
	/** 年休出勤率日数 */
	private AnnualLeaveAttdRateDays attendanceRateDays;
	/** 付与区分 */
	private boolean grantAtr;
	/** 未消化 */
	private AnnualLeaveUndigestedNumber undigestedNumber;
	
	
	/**
	 * コンストラクタ
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日
	 */
	public AnnLeaRemNumEachMonth(
			String employeeId,
			YearMonth yearMonth,
			ClosureId closureId,
			ClosureDate closureDate){
		
		super();
		this.employeeId = employeeId;
		this.yearMonth = yearMonth;
		this.closureId = closureId;
		this.closureDate = closureDate;
		
		this.closurePeriod = new DatePeriod(GeneralDate.today(), GeneralDate.today());
		this.closureStatus = ClosureStatus.UNTREATED;
		this.annualLeave = new AnnualLeave();
		this.realAnnualLeave = new AnnualLeave();
		this.halfDayAnnualLeave = Optional.empty();
		this.realHalfDayAnnualLeave = Optional.empty();
		this.annualLeaveGrant = Optional.empty();
		this.maxRemainingTime = Optional.empty();
		this.realMaxRemainingTime = Optional.empty();
		this.attendanceRateDays = new AnnualLeaveAttdRateDays();
		this.grantAtr = false;
		this.undigestedNumber = new AnnualLeaveUndigestedNumber();
	}
	
	/**
	 * ファクトリー
	 * @param employeeId 社員ID
	 * @param yearMonth 年月
	 * @param closureId 締めID
	 * @param closureDate 締め日
	 * @param closurePeriod 締め期間
	 * @param closureStatus 締め処理状態
	 * @param annualLeave 年休
	 * @param realAnnualLeave 実年休
	 * @param halfDayAnnualLeave 半日年休
	 * @param realHalfDayAnnualLeave 実半日年休
	 * @param annualLeaveGrant 年休付与情報
	 * @param maxRemainingTime 上限残時間
	 * @param realMaxRemainingTime 実上限残時間
	 * @param attendanceRateDays 年休出勤率日数
	 * @param grantAtr 付与区分
	 * @param undigestedNumber 未消化数
	 * @return 年休月別残数データ
	 */
	public static AnnLeaRemNumEachMonth of(
			String employeeId,
			YearMonth yearMonth,
			ClosureId closureId,
			ClosureDate closureDate,
			DatePeriod closurePeriod,
			ClosureStatus closureStatus,
			AnnualLeave annualLeave,
			AnnualLeave realAnnualLeave,
			Optional<HalfDayAnnualLeave> halfDayAnnualLeave,
			Optional<HalfDayAnnualLeave> realHalfDayAnnualLeave,
			Optional<AnnualLeaveGrant> annualLeaveGrant,
			Optional<AnnualLeaveMaxRemainingTime> maxRemainingTime,
			Optional<AnnualLeaveMaxRemainingTime> realMaxRemainingTime,
			AnnualLeaveAttdRateDays attendanceRateDays,
			boolean grantAtr,
			AnnualLeaveUndigestedNumber undigestedNumber){
		
		AnnLeaRemNumEachMonth domain = new AnnLeaRemNumEachMonth(
				employeeId, yearMonth, closureId, closureDate);
		domain.closurePeriod = closurePeriod;
		domain.closureStatus = closureStatus;
		domain.annualLeave = annualLeave;
		domain.realAnnualLeave = realAnnualLeave;
		domain.halfDayAnnualLeave = halfDayAnnualLeave;
		domain.realHalfDayAnnualLeave = realHalfDayAnnualLeave;
		domain.annualLeaveGrant = annualLeaveGrant;
		domain.maxRemainingTime = maxRemainingTime;
		domain.realMaxRemainingTime = realMaxRemainingTime;
		domain.attendanceRateDays = attendanceRateDays;
		domain.grantAtr = grantAtr;
		domain.undigestedNumber = undigestedNumber;
		return domain;
	}
	
	private void writeObject(ObjectOutputStream stream){	
		writeObjectWithOptional(stream);
	}	
	private void readObject(ObjectInputStream stream){	
		readObjectWithOptional(stream);
	}	

}

