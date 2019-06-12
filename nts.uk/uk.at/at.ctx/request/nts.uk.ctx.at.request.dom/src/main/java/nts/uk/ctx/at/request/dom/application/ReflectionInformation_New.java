package nts.uk.ctx.at.request.dom.application;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.dom.DomainObject;
import nts.arc.time.GeneralDateTime;

/**
 * 反映情報
 * @author Doan Duy Hung
 *
 */
@Getter
@AllArgsConstructor
@Builder
public class ReflectionInformation_New extends DomainObject {
	
	// 予定反映状態
	@Setter
	private ReflectedState_New stateReflection;
	
	// 実績反映状態
	@Setter
	private ReflectedState_New stateReflectionReal;
	
	// 予定強制反映
	private DisabledSegment_New forcedReflection;
	
	// 実績強制反映
	private DisabledSegment_New forcedReflectionReal;
	
	// 予定反映不可理由
	@Setter
	private Optional<ReasonNotReflect_New> notReason;
	
	// 実績反映不可理由
	@Setter
	private Optional<ReasonNotReflectDaily_New> notReasonReal;
	
	// 予定反映日時
	@Setter
	private Optional<GeneralDateTime> dateTimeReflection;
	
	// 実績反映日時
	@Setter
	private Optional<GeneralDateTime> dateTimeReflectionReal;
	
	public static ReflectionInformation_New firstCreate(){
		return ReflectionInformation_New.builder()
				.stateReflection(ReflectedState_New.NOTREFLECTED)
				.stateReflectionReal(ReflectedState_New.NOTREFLECTED)
				.forcedReflection(DisabledSegment_New.NOTTODO)
				.forcedReflectionReal(DisabledSegment_New.NOTTODO)
				.notReason(Optional.empty())
				.notReasonReal(Optional.empty())
				.dateTimeReflection(Optional.empty())
				.dateTimeReflectionReal(Optional.empty())
				.build();
	}
}
