package nts.uk.ctx.at.shared.app.find.remainingnumber.empinfo.basicinfo;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.grantremainingdata.SpecialLeaveCode;
import nts.uk.shr.pereg.app.ComboBoxObject;
import nts.uk.shr.pereg.app.find.PeregFinder;
import nts.uk.shr.pereg.app.find.PeregQuery;
import nts.uk.shr.pereg.app.find.PeregQueryByListEmp;
import nts.uk.shr.pereg.app.find.dto.DataClassification;
import nts.uk.shr.pereg.app.find.dto.GridPeregDto;
import nts.uk.shr.pereg.app.find.dto.PeregDomainDto;

@Stateless
public class Specialleave17InformationFinder implements PeregFinder<Specialleave17informationDto>{

	@Inject 
	private SpecialleaveInformationFinder specialleaveInformationFinder;
	
	@Override
	public String targetCategoryCode() {
		return "CS00055";
	}

	@Override
	public Class<Specialleave17informationDto> dtoClass() {
		return Specialleave17informationDto.class;
	}

	@Override
	public DataClassification dataType() {
		return DataClassification.EMPLOYEE;
	}

	@Override
	public PeregDomainDto getSingleData(PeregQuery query) {
		// 社員ID　＝　社員ID　 and 特別休暇コード＝17
		return specialleaveInformationFinder.getSingleData(query, SpecialLeaveCode.CS00055.value);
	}
	
	@Override
	public List<PeregDomainDto> getListData(PeregQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ComboBoxObject> getListFirstItems(PeregQuery query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<GridPeregDto> getAllData(PeregQueryByListEmp query) {
		// TODO Auto-generated method stub
		return null;
	}
}
