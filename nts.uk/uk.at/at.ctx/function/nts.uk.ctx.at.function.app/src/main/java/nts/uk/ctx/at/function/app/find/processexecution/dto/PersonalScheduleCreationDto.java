package nts.uk.ctx.at.function.app.find.processexecution.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.function.dom.processexecution.personalschedule.PersonalScheduleCreation;
import nts.uk.shr.com.enumcommon.NotUseAtr;

/**
 * The class Personal schedule creation dto.<br>
 * Dto 個人スケジュール作成
 *
 * @author nws-minhnb
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PersonalScheduleCreationDto {

	/**
	 * The Personal schedule creation period.<br>
	 * 作成期間
	 */
	private PersonalScheduleCreationPeriodDto perSchedulePeriod;

	/**
	 * The Personal schedule creation classification.<br>
	 * 個人スケジュール作成区分
	 */
	private boolean perScheduleCls;

//	/**
//	 * 対象社員
//	 */
//	private PersonalScheduleCreationTarget target;

	/**
	 * The Create new employee schedule.<br>
	 * 新入社員を作成
	 */
	private boolean createNewEmpSched;
	
	/**
	 * Create from domain.
	 *
	 * @param domain the domain
	 * @return the Personal schedule creation dto
	 */
	public static PersonalScheduleCreationDto createFromDomain(PersonalScheduleCreation domain) {
		if (domain == null) {
			return null;
		}
		PersonalScheduleCreationDto dto = new PersonalScheduleCreationDto();
		dto.perSchedulePeriod = PersonalScheduleCreationPeriodDto.createFromDomain(domain.getPerSchedulePeriod());
		dto.perScheduleCls = domain.getPerScheduleCls().equals(NotUseAtr.USE);
//		dto.target = PersonalScheduleCreationTargetDto.createFromDomain(domain.getTarget());
		return dto;
	}

	public PersonalScheduleCreation toDomain() {
		return PersonalScheduleCreation.builder()
				.createNewEmpSched(createNewEmpSched ? NotUseAtr.USE : NotUseAtr.NOT_USE)
				.perScheduleCls(perScheduleCls ? NotUseAtr.USE : NotUseAtr.NOT_USE)
				.perSchedulePeriod(perSchedulePeriod.toDomain())
				.build();
	}
	
}
