/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.dom.worktype;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import nts.arc.enums.EnumAdaptor;
import nts.arc.error.BusinessException;
import nts.arc.layer.dom.AggregateRoot;

/**
 * The Class WorkType.
 */
// 勤務種類
@Getter
public class WorkType extends AggregateRoot {

	/** The company id. */
	/* 会社ID */
	private String companyId;

	/** The work type code. */
	/* 勤務種類コード */
	private WorkTypeCode workTypeCode;

	/** The symbolic name. */
	/* 勤務種類記号名 */
	private WorkTypeSymbolicName symbolicName;

	/** The name. */
	/* 勤務種類名称 */
	private WorkTypeName name;

	/** The abbreviation name. */
	/* 勤務種類略名 */
	private WorkTypeAbbreviationName abbreviationName;

	/** The memo. */
	/* 勤務種類備考 */
	private WorkTypeMemo memo;

	/** The daily work. */
	// 1日の勤務
	private DailyWork dailyWork;

	/** The deprecate. */
	// 廃止区分
	private DeprecateClassification deprecate;

	// 出勤率の計算
	private CalculateMethod calculateMethod;

	private List<WorkTypeSet> workTypeSetList;

	private Integer dispOrder;

	@Override
	public void validate() {
		super.validate();
		if ("000".equals(this.workTypeCode.v())) {
			throw new BusinessException("Msg_385");
		}

		WorkTypeClassification moringType = this.dailyWork.getMorning();
		WorkTypeClassification afternoonType = this.dailyWork.getAfternoon();
		if (this.dailyWork.getWorkTypeUnit() == WorkTypeUnit.MonringAndAfternoon) {
			if ((moringType == afternoonType && (moringType != WorkTypeClassification.Absence || moringType == WorkTypeClassification.SpecialHoliday))
					|| (moringType == WorkTypeClassification.Pause && afternoonType == WorkTypeClassification.Shooting)
					|| (moringType == WorkTypeClassification.Shooting
							&& afternoonType == WorkTypeClassification.Pause)) {
				throw new BusinessException("Msg_395");
			}
		}
	}
	
	/** 取得したドメインモデル「勤務種類．一日の勤務．一日」をチェックする */
	public boolean isWokingDay() {
		if(dailyWork == null) { return false; }
		if (dailyWork.getWorkTypeUnit() == WorkTypeUnit.OneDay) {
			return isWorkingType(dailyWork.getOneDay());
		}
		return isWorkingType(dailyWork.getMorning()) || isWorkingType(dailyWork.getAfternoon());
	}

	/** 出勤系かチェックする　*/
	private boolean isWorkingType(WorkTypeClassification wt) {
		return wt == WorkTypeClassification.Attendance || wt == WorkTypeClassification.Shooting;
	}

	/**
	 * 
	 * @param companyId
	 * @param workTypeCode
	 * @param sortOrder
	 * @param symbolicName
	 * @param name
	 * @param abbreviationName
	 * @param memo
	 * @param displayAtr
	 * @param dailyWork
	 * @param deprecate
	 * @param calculateMethod
	 */
	public WorkType(String companyId, WorkTypeCode workTypeCode, WorkTypeSymbolicName symbolicName, WorkTypeName name,
			WorkTypeAbbreviationName abbreviationName, WorkTypeMemo memo, DailyWork dailyWork,
			DeprecateClassification deprecate, CalculateMethod calculateMethod) {
		super();
		this.companyId = companyId;
		this.workTypeCode = workTypeCode;
		this.symbolicName = symbolicName;
		this.name = name;
		this.abbreviationName = abbreviationName;
		this.memo = memo;
		this.dailyWork = dailyWork;
		this.deprecate = deprecate;
		this.calculateMethod = calculateMethod;
	}

	/**
	 * 
	 * @param companyId
	 * @param workTypeCode
	 * @param name
	 * @param abbreviationName
	 */
	public WorkType(String companyId, WorkTypeCode workTypeCode, WorkTypeName name,
			WorkTypeAbbreviationName abbreviationName) {
		super();
		this.companyId = companyId;
		this.workTypeCode = workTypeCode;
		this.name = name;
		this.abbreviationName = abbreviationName;
	}

	/**
	 * Creates the simple from java type.
	 *
	 * @param companyId
	 *            the company id
	 * @param workTypeCode
	 *            the work type code
	 * @param sortOrder
	 *            the sort order
	 * @param symbolicName
	 *            the symbolic name
	 * @param name
	 *            the name
	 * @param abbreviationName
	 *            the abbreviation name
	 * @param memo
	 *            the memo
	 * @param displayAtr
	 *            the display atr
	 * @return the work type
	 */
	public static WorkType createSimpleFromJavaType(String companyId, String workTypeCode, String symbolicName,
			String name, String abbreviationName, String memo, int workTypeUnit, int oneDay, int morning, int afternoon,
			int deprecate, int calculateMethod) {
		DailyWork dailyWork = new DailyWork();
		dailyWork.setWorkTypeUnit(EnumAdaptor.valueOf(workTypeUnit, WorkTypeUnit.class));
		dailyWork.setOneDay(EnumAdaptor.valueOf(oneDay, WorkTypeClassification.class));
		dailyWork.setMorning(EnumAdaptor.valueOf(morning, WorkTypeClassification.class));
		dailyWork.setAfternoon(EnumAdaptor.valueOf(afternoon, WorkTypeClassification.class));
		return new WorkType(companyId, new WorkTypeCode(workTypeCode), new WorkTypeSymbolicName(symbolicName),
				new WorkTypeName(name), new WorkTypeAbbreviationName(abbreviationName), new WorkTypeMemo(memo),
				dailyWork, EnumAdaptor.valueOf(deprecate, DeprecateClassification.class),
				EnumAdaptor.valueOf(calculateMethod, CalculateMethod.class));
	}

	/**
	 * Set work type set
	 * 
	 * @param workTypeList
	 */
	public void setWorkTypeSet(List<WorkTypeSet> workTypeList) {
		this.workTypeSetList = workTypeList;
	}
	
	
	public AttendanceHolidayAttr getAttendanceHolidayAttr() {
		return this.dailyWork.getAttendanceHolidayAttr();
	}
	
	/**
	 * 出勤系か判定する
	 * @return　出勤系である
	 */
	public boolean isWeekDayAttendance() {
		return this.dailyWork.isWeekDayAttendance();
	}

	

	/**
	 * Set display order
	 * 
	 * @param order
	 */
	public void setDisplayOrder(int dispOrder) {
		this.dispOrder = dispOrder;
	}

	/**
	 * Check OneDay Morning Afternoon
	 * 
	 * @return
	 */
	public boolean isOneDay() {
		return this.dailyWork.getWorkTypeUnit() == WorkTypeUnit.OneDay;
	}

	/**
	 * Checks if is attendance or shooting.
	 *
	 * @param worktypeCls the worktype cls
	 * @return true, if is attendance or shooting
	 */
	public boolean isAttendanceOrShooting(WorkTypeClassification worktypeCls) {
		return worktypeCls == WorkTypeClassification.Attendance || worktypeCls == WorkTypeClassification.Shooting;
	}

	/**
	 * Checks if is attendance or shooting or holiday work.
	 *
	 * @param worktypeCls the worktype cls
	 * @return true, if is attendance or shooting or holiday work
	 */
	public boolean isAttendanceOrShootingOrHolidayWork(WorkTypeClassification worktypeCls) {
		return worktypeCls == WorkTypeClassification.Attendance || worktypeCls == WorkTypeClassification.Shooting
				|| worktypeCls == WorkTypeClassification.HolidayWork;
	}

	/**
	 * Gets the work type set by atr.
	 *
	 * @param atr the atr
	 * @return the work type set by atr
	 */
	public WorkTypeSet getWorkTypeSetByAtr(WorkAtr atr) {
		return this.getWorkTypeSetList().stream().filter(item -> item.getWorkAtr() == atr).findFirst().get();
	}

	/**
	 * Gets the work type set.
	 *
	 * @return the work type set
	 */
	public WorkTypeSet getWorkTypeSet() {
		// 1日
		if (this.isOneDay()) {
			if (this.isAttendanceOrShootingOrHolidayWork(this.dailyWork.getOneDay())) {
				return this.getWorkTypeSetByAtr(WorkAtr.OneDay);
			} else {
				return null;
			}
		}
		// 午前と午後
		else {
			if (this.isAttendanceOrShooting(this.dailyWork.getMorning())) {
				WorkTypeSet am = this.getWorkTypeSetByAtr(WorkAtr.Monring);
				if (this.isAttendanceOrShooting(this.dailyWork.getAfternoon())) {
					WorkTypeSet pm = this.getWorkTypeSetByAtr(WorkAtr.Afternoon);
					return new WorkTypeSet(pm.getTimeLeaveWork(), am.getAttendanceTime());
				}
				return am;
			} else {
				if (this.isAttendanceOrShooting(this.dailyWork.getAfternoon())) {
					return this.getWorkTypeSetByAtr(WorkAtr.Afternoon);
				} else {
					return null;
				}
			}
		}
	}
	
	/**
	 * 廃止区分.廃止する
	 * @return
	 */
	public boolean isDeprecated() {
		return DeprecateClassification.Deprecated == this.deprecate;
	}
	
	/**
	 * 勤務種類設定の編集(一時的なSetter)
	 * @param workTypeSet
	 */
	public void addWorkTypeSet(WorkTypeSet workTypeSet) {
		if(this.workTypeSetList == null || this.workTypeSetList.isEmpty()) {
			List<WorkTypeSet> addItems = new ArrayList<>();
			addItems.add(workTypeSet);
			this.workTypeSetList = addItems;
		}
		else {
			this.workTypeSetList.add(workTypeSet);
		}
		
							
	}
}
