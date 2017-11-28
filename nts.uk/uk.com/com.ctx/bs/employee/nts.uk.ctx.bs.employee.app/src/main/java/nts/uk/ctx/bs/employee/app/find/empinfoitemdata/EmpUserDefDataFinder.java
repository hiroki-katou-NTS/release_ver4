package nts.uk.ctx.bs.employee.app.find.empinfoitemdata;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.bs.employee.dom.regpersoninfo.personinfoadditemdata.item.EmpInfoItemDataRepository;
import nts.uk.shr.pereg.app.find.PeregEmpOptRepository;
import nts.uk.shr.pereg.app.find.dto.EmpOptionalDto;

@Stateless
public class EmpUserDefDataFinder implements PeregEmpOptRepository{

	@Inject
	private EmpInfoItemDataRepository empInfoItemDataRepository;
	
	@Override
	public List<EmpOptionalDto> getData(String recordId) {
		return empInfoItemDataRepository.getAllInfoItemByRecordId(recordId).stream()
				.map(x -> x.genToPeregDto()).collect(Collectors.toList());
	}

}
