package nts.uk.ctx.pr.formula.infra.repository.formula;

import java.util.List;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.formula.dom.formula.SystemVariable;
import nts.uk.ctx.pr.formula.dom.repository.SystemVariableRepository;
import nts.uk.ctx.pr.formula.infra.entity.formula.QsvstSystemVariable;

@Stateless
public class JpaSystemVariableRepository extends JpaRepository implements SystemVariableRepository {

	private static final String FIND_ALL;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM QsvstSystemVariable a ");
		FIND_ALL = builderString.toString();
	}

	@Override
	public List<SystemVariable> findAll() {
		return this.queryProxy().query(FIND_ALL, QsvstSystemVariable.class).getList(f -> toDomain(f));
	}

	private SystemVariable toDomain(QsvstSystemVariable qsvstSystemVariable) {
		SystemVariable systemVariable = SystemVariable.createFromJavaType(
				qsvstSystemVariable.qsvstSystemVariablePK.variableId, qsvstSystemVariable.variableName,
				qsvstSystemVariable.fromTable, qsvstSystemVariable.fromColumnName, qsvstSystemVariable.whereCondition);
		return systemVariable;
	}

}
