package nts.uk.ctx.at.request.app.find.setting.company.request.applicationsetting.apptypesetting;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AfterhandRestrictionDto {
	
	/**
	 * 未来日許可しない
	 */
	public boolean allowFutureDay;
	
//	public static AfterhandRestrictionDto fromDomain(AfterhandRestriction afterhandRestriction) {
//		return new AfterhandRestrictionDto(afterhandRestriction.getAllowFutureDay());
//	}
	
//	public AfterhandRestriction toDomain() {
//		return new AfterhandRestriction(allowFutureDay);
//	}
}
