package nts.uk.ctx.pr.formula.dom.repository;

import java.util.Optional;

import nts.uk.ctx.pr.formula.dom.formula.BasicPayroll;

public interface BasicPayrollRepository {
	
	Optional<BasicPayroll> findAll(String companyCode);
}
