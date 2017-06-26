package nts.uk.ctx.pr.core.dom.rule.employment.layout.allot;
import java.util.List;
import java.util.Optional;

public interface EmployeeAllotSettingHeaderRespository {
	Optional<EmployeeAllotSettingHeader> find(String companyCode);
	//find all, return list
	List<EmployeeAllotSettingHeader> findAll(String companyCode);
	Optional<Integer> findMaxEnd(String companyCode);
}
