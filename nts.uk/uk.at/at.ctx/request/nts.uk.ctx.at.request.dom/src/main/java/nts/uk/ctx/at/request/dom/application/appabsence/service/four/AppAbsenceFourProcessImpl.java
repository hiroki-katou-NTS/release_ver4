package nts.uk.ctx.at.request.dom.application.appabsence.service.four;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.HdAppSet;
import nts.uk.ctx.at.request.dom.setting.company.applicationapprovalsetting.vacationapplicationsetting.WorkUse;
import nts.uk.ctx.at.shared.dom.worktype.WorkType;
import nts.uk.ctx.at.shared.dom.worktype.WorkTypeRepository;

@Stateless
public class AppAbsenceFourProcessImpl implements AppAbsenceFourProcess{
	@Inject
	private WorkTypeRepository workTypeRepository;

	@Override
	public boolean getDisplayControlWorkingHours(String workTypeCd, Optional<HdAppSet> hdAppSet, String companyID) {
		boolean changeWorkHourFlg = false;
		if(workTypeCd != null && !workTypeCd.equals("-1")){
			if(hdAppSet.isPresent()){
				if(hdAppSet.get().getWrkHours().equals(WorkUse.NOT_USE)){
					return false;
				}else if(hdAppSet.get().getWrkHours().equals(WorkUse.USE)){
					return true;
				}else if(hdAppSet.get().getWrkHours().equals(WorkUse.USE_ONLY_HALF_HD)){
					// lấy những worktype dạng làm nửa ngày
					List<Integer> halfAtrs = new ArrayList<>();
					// 出勤
					halfAtrs.add(0);
					// 振出
					halfAtrs.add(7);
					List<String> workTypeCodes = new ArrayList<>();
					workTypeCodes.add(workTypeCd);
					List<WorkType> workTypes = this.workTypeRepository
							.findWorkTypeForHalfDay(companyID, halfAtrs, workTypeCodes).stream()
							.sorted(Comparator.comparing(WorkType::getWorkTypeCode)).collect(Collectors.toList());
					if(!CollectionUtil.isEmpty(workTypes)){
						return true;
					}
				}
			}
		}
		
		return changeWorkHourFlg;
	}

}
