package nts.uk.ctx.pr.formula.app.find.formula;

import lombok.Data;
import nts.uk.ctx.pr.formula.dom.formula.SystemVariable;

/**
 * @author nampt
 *
 */
@Data
public class SystemVariableDto {

	private String systemVariableName;

	private String systemVariableCode;

	private String result;

	public static SystemVariableDto fromDomain(SystemVariable domain) {
		return new SystemVariableDto(domain.getVariableName().v(), domain.getVariableId().v());
	}

	/**
	 * @param systemVariableName
	 * @param systemVariableCode
	 */
	public SystemVariableDto(String systemVariableName, String systemVariableCode) {
		super();
		this.systemVariableName = systemVariableName;
		this.systemVariableCode = systemVariableCode;
	}

}
