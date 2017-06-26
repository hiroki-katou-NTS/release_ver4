package nts.uk.ctx.pr.formula.dom.formula;

import lombok.Getter;
import nts.arc.layer.dom.DomainObject;
import nts.uk.ctx.pr.formula.dom.primitive.FromColumnName;
import nts.uk.ctx.pr.formula.dom.primitive.FromTable;
import nts.uk.ctx.pr.formula.dom.primitive.VariableId;
import nts.uk.ctx.pr.formula.dom.primitive.VariableName;
import nts.uk.ctx.pr.formula.dom.primitive.WhereCondition;

@Getter
public class SystemVariable extends DomainObject {

	private VariableId variableId;

	private VariableName variableName;

	private FromTable fromTable;

	private FromColumnName fromColumnName;

	private WhereCondition whereCondition;

	/**
	 * @param variableId
	 * @param variableName
	 * @param fromTable
	 * @param fromColumnName
	 * @param whereCondition
	 */
	public SystemVariable(VariableId variableId, VariableName variableName, FromTable fromTable,
			FromColumnName fromColumnName, WhereCondition whereCondition) {
		super();
		this.variableId = variableId;
		this.variableName = variableName;
		this.fromTable = fromTable;
		this.fromColumnName = fromColumnName;
		this.whereCondition = whereCondition;
	}

	public static SystemVariable createFromJavaType(String variableId, String variableName, String fromTable,
			String fromColumnName, String whereCondition) {
		return new SystemVariable(new VariableId(variableId), new VariableName(variableName), new FromTable(fromTable),
				new FromColumnName(fromColumnName), new WhereCondition(whereCondition));

	}
}
