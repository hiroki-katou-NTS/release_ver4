package nts.uk.ctx.at.function.ac.specificdate;

import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.at.function.dom.adapter.SpecificDateAdapter;
import nts.uk.ctx.at.function.dom.adapter.SpecificDateImport;
import nts.uk.ctx.at.schedule.pub.shift.businesscalendar.specificdate.SpecificDateItemExport;
import nts.uk.ctx.at.schedule.pub.shift.businesscalendar.specificdate.WpSpecificDateSettingPub;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class SpecificDateFinder implements SpecificDateAdapter {

	@Inject
	private WpSpecificDateSettingPub wpSpecificDateSettingPub;

	@Override
	public List<SpecificDateImport> getSpecificDate(String companyId, List<Integer> specificDateItemNos) {

		List<SpecificDateItemExport> specificDateItemExports = this.wpSpecificDateSettingPub
				.getSpecifiDateByListCode(companyId, specificDateItemNos);
		List<SpecificDateImport> specificDateImports = specificDateItemExports.stream().map(item -> {
			return new SpecificDateImport(item.getCompanyId(), item.getUseAtr(), item.getSpecificDateItemNo(),
					item.getSpecificName());
		}).collect(Collectors.toList());
		return specificDateImports;
	}

}
