package nts.uk.ctx.bs.employee.pubimp.employee.workplace.export;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.workplace.export.WorkplaceExport;
import nts.uk.ctx.bs.employee.pub.employee.workplace.export.WorkplaceExportPub;
import nts.uk.ctx.bs.employee.pub.employee.workplace.export.WorkplaceExportPubDto;

/**
 * 
 * @author sonnh1
 *
 */
@Stateless
public class WorkplaceExportPubImpl implements WorkplaceExportPub {

	@Inject
	private WorkplaceExport workplaceExport;

	@Override
	public List<WorkplaceExportPubDto> getAllWkpConfig(String companyId, List<String> listWkpId, GeneralDate baseDate) {
		String hierarchyCd = null;
		return this.workplaceExport.getAllWkpConfig(companyId, listWkpId, baseDate).stream()
				.map(x -> new WorkplaceExportPubDto(x.getWorkplaceId(), x.getWorkplaceCode().v(),
						x.getWorkplaceName().v(), x.getWkpGenericName().v(), x.getWkpDisplayName().v(),
						x.getOutsideWkpCode().v(), hierarchyCd))
				.collect(Collectors.toList());
	}

	@Override
	public List<WorkplaceExportPubDto> getPastWkpInfo(String companyId, List<String> listWkpId, String histId) {
		String hierarchyCd = null;
		return this.workplaceExport.getPastWkpInfo(companyId, listWkpId, histId).stream()
				.map(x -> new WorkplaceExportPubDto(x.getWorkplaceId(), x.getWorkplaceCode().v(),
						x.getWorkplaceName().v(), x.getWkpGenericName().v(), x.getWkpDisplayName().v(),
						x.getOutsideWkpCode().v(), hierarchyCd))
				.collect(Collectors.toList());
	}

}
