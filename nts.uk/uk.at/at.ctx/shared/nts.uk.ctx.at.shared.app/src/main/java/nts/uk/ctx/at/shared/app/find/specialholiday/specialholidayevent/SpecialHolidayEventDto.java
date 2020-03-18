package nts.uk.ctx.at.shared.app.find.specialholiday.specialholidayevent;

import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.shared.dom.bonuspay.enums.UseAtr;
import nts.uk.ctx.at.shared.dom.specialholiday.grantcondition.AgeRange;
import nts.uk.ctx.at.shared.dom.specialholiday.grantcondition.GenderCls;
import nts.uk.ctx.at.shared.dom.specialholiday.specialholidayevent.AgeStandardType;
import nts.uk.ctx.at.shared.dom.specialholiday.specialholidayevent.FixedDayGrant;
import nts.uk.ctx.at.shared.dom.specialholiday.specialholidayevent.MaxNumberDayType;
import nts.uk.ctx.at.shared.dom.specialholiday.specialholidayevent.SpecialHolidayEvent;
import nts.uk.shr.com.primitive.Memo;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialHolidayEventDto {

	/* 会社ID */
	private String companyId;

	/* 特別休暇枠NO */
	private int specialHolidayEventNo;

	/* 上限日数の設定方法 */
	private int maxNumberDay;

	/* 固定上限日数 */
	private Integer fixedDayGrant;

	/* 忌引とする */
	private int makeInvitation;

	/* 休日を取得日に含める */
	private int includeHolidays;

	private int ageLimit;

	/* 性別条件 */
	private int genderRestrict;

	/* 雇用条件 */
	private int restrictEmployment;

	/* 分類条件 */
	private int restrictClassification;

	/* 性別 */
	private int gender;

	/* 年齢範囲 */
	private AgeRangeDto ageRange;

	/* 年齢基準 */
	private int ageStandard;

	/* 年齢基準 */
	private Integer ageStandardBaseDate;

	/* メモ */
	private String memo;

	/* 分類一覧 */
	List<ClassificationListDto> clsList;

	/* 雇用一覧 */
	List<EmploymentListDto> empList;

	public static SpecialHolidayEventDto fromDomain(SpecialHolidayEvent domain) {
		return new SpecialHolidayEventDto(domain.getCompanyId(), domain.getSpecialHolidayEventNo(),
				domain.getMaxNumberDay().value, domain.getFixedDayGrant().v(), domain.getMakeInvitation().value,
				domain.getIncludeHolidays().value, domain.getAgeLimit().value, domain.getGenderRestrict().value,
				domain.getRestrictEmployment().value, domain.getRestrictClassification().value,
				domain.getGender().value, AgeRangeDto.fromDomain(domain.getAgeRange()), domain.getAgeStandard().value,
				domain.getAgeStandardBaseDate(), domain.getMemo().v(),
				ClassificationListDto.fromClsList(domain.getClsList()),
				EmploymentListDto.fromEmpList(domain.getEmpList()));
	}
	
	public SpecialHolidayEvent toDomain() {
		return new SpecialHolidayEvent(
				companyId, 
				specialHolidayEventNo, 
				EnumAdaptor.valueOf(maxNumberDay, MaxNumberDayType.class), 
				new FixedDayGrant(fixedDayGrant), 
				EnumAdaptor.valueOf(makeInvitation, UseAtr.class), 
				EnumAdaptor.valueOf(includeHolidays, UseAtr.class), 
				EnumAdaptor.valueOf(ageLimit, UseAtr.class), 
				EnumAdaptor.valueOf(genderRestrict, UseAtr.class), 
				EnumAdaptor.valueOf(restrictEmployment, UseAtr.class), 
				EnumAdaptor.valueOf(restrictClassification, UseAtr.class), 
				EnumAdaptor.valueOf(gender, GenderCls.class), 
				AgeRange.createFromJavaType(ageRange.getAgeLowerLimit(), ageRange.getAgeHigherLimit()), 
				EnumAdaptor.valueOf(ageStandard, AgeStandardType.class), 
				ageStandardBaseDate, 
				new Memo(memo), 
				clsList.stream().map(x -> x.toDomain()).collect(Collectors.toList()), 
				empList.stream().map(x -> x.toDomain()).collect(Collectors.toList()));
	}

}
