package nts.uk.ctx.at.record.dom.dailyperformanceprocessing.finddata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.at.record.dom.adapter.company.StatusOfEmployeeExport;
import nts.uk.ctx.at.record.dom.adapter.company.SyCompanyRecordAdapter;
import nts.uk.ctx.at.record.dom.approvalmanagement.ApprovalProcessingUseSetting;
import nts.uk.ctx.at.record.dom.approvalmanagement.repository.ApprovalProcessingUseSettingRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentityProcessUseSetRepository;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class FindDataDCRecord implements IFindDataDCRecord{

	private static Map<String, Optional<IdentityProcessUseSet>> identityProcessUseSetMap = new HashMap<>();
	
	private static Map<String, Optional<ApprovalProcessingUseSetting>> approvalUseSettingMap = new HashMap<>();
	
	private static Map<String, List<StatusOfEmployeeExport>> lstStatusOfEmpMap = new HashMap<>();
	
	@Inject
	private ApprovalProcessingUseSettingRepository approvalProcessingUseSettingRepository;
	
	@Inject
	private IdentityProcessUseSetRepository identityProcessUseSetRepository;
	
	@Inject
	private SyCompanyRecordAdapter syCompanyRecordAdapter;
	
	@Override
	public Optional<IdentityProcessUseSet> findIdentityByKey(String companyId) {
		if(identityProcessUseSetMap.containsKey(companyId)){
			return identityProcessUseSetMap.get(companyId);
		}else{
			Optional<IdentityProcessUseSet> indentityOpt = identityProcessUseSetRepository.findByKey(companyId);
			identityProcessUseSetMap.put(companyId, indentityOpt);
			return indentityOpt;
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Optional<ApprovalProcessingUseSetting> findApprovalByCompanyId(String companyId) {
		if(approvalUseSettingMap.containsKey(companyId)){
			return approvalUseSettingMap.get(companyId);
		}else{
			Optional<ApprovalProcessingUseSetting> approvalOpt = approvalProcessingUseSettingRepository.findByCompanyId(companyId);
			approvalUseSettingMap.put(companyId, approvalOpt);
			return approvalOpt;
		}
	}

	@Override
	public List<StatusOfEmployeeExport> getListAffComHistByListSidAndPeriod(Optional<String> keyRandom,
			List<String> sid, DatePeriod datePeriod) {
		if(keyRandom.isPresent() && lstStatusOfEmpMap.containsKey(keyRandom.get())){
			return lstStatusOfEmpMap.get(keyRandom.get());
		}else{
			List<StatusOfEmployeeExport> lstResult = syCompanyRecordAdapter.getListAffComHistByListSidAndPeriod(sid, datePeriod);
			if(keyRandom.isPresent()) lstStatusOfEmpMap.put(keyRandom.get(), lstResult);
			return lstResult;
		}
	}

	@Override
	public void clearAllStateless() {
		identityProcessUseSetMap = new HashMap<>();
		approvalUseSettingMap = new HashMap<>();
		lstStatusOfEmpMap = new HashMap<>();
	}

}
