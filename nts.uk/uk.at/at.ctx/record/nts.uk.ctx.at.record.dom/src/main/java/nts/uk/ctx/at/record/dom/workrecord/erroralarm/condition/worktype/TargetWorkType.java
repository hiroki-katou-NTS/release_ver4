/**
 * 4:08:38 PM Nov 3, 2017
 */
package nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktype;

import java.util.List;
import java.util.stream.Collectors;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeCode;

/**
 * @author hungnm
 *
 */
//対象の勤務種類
@Getter
public class TargetWorkType extends DomainObject {

	//しぼり込む
	private Boolean filterAtr;

	//対象の勤務種類一覧
	private List<WorkTypeCode> lstWorkType;

	private TargetWorkType(Boolean filterAtr, List<WorkTypeCode> lstWorkType) {
		super();
		this.filterAtr = filterAtr;
		this.lstWorkType = lstWorkType;
	}

	public static TargetWorkType createFromJavaType(boolean filterAtr, List<String> lstWorkType) {
		return new TargetWorkType(filterAtr, lstWorkType.stream().map((code) -> {
			return new WorkTypeCode(code);
		}).collect(Collectors.toList()));
	}

}
