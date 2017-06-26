package nts.uk.ctx.pr.formula.dom.repository;

import java.util.List;

import nts.uk.ctx.pr.formula.dom.formula.SystemVariable;

public interface SystemVariableRepository {

	List<SystemVariable> findAll();
	
}
