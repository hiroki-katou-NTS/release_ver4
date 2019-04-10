package nts.uk.ctx.at.record.dom.workrecord.identificationstatus.algorithm;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workinformation.repository.WorkInformationRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecordRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.enums.SelfConfirmError;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentityProcessUseSetRepository;
import nts.uk.shr.com.context.AppContexts;

/**
 * @author thanhnx 日の本人確認を登録する
 */
@Stateless
public class RegisterIdentityConfirmDay {

	@Inject
	private IdentityProcessUseSetRepository identityProcessUseSetRepository;

	@Inject
	private CheckIdentityVerification checkIdentityVerification;

	@Inject
	private IdentificationRepository identificationRepository;

	@Inject
	private EmployeeDailyPerErrorRepository employeeDailyPerErrorRepository;

	@Inject
	private ErrorAlarmWorkRecordRepository errorAlarmWorkRecordRepository;
	
	@Inject
	private WorkInformationRepository workInfo;
	
	// code old 
//	@Inject
//	private OpOfDailyPerformance opOfDailyPerformance;

	public boolean registerIdentity(ParamIdentityConfirmDay param) {
		String companyId = AppContexts.user().companyId();
		Optional<IdentityProcessUseSet> identityProcessOpt = identityProcessUseSetRepository.findByKey(companyId);
		if (identityProcessOpt.isPresent()) {
			checkIdentityVerification.check(identityProcessOpt.get()).ifPresent(canRegist -> {
				registIdentification(param, companyId, canRegist);
			});
			
			if(identityProcessOpt.get().isUseConfirmByYourself()){
				param.getSelfConfirmDay().stream().forEach(cd -> {
					workInfo.verShouldUp(param.getEmployeeId(), cd.getDate());
				});
				return true;
			}
		}
		
		return false;
	}

	private void registIdentification(ParamIdentityConfirmDay param, String companyId, SelfConfirmError canRegist) {
		GeneralDate processingYmd = GeneralDate.today();
		String employeeId = AppContexts.user().employeeId();
		param.getSelfConfirmDay().forEach(data -> {
			if (canRegist == SelfConfirmError.CAN_CONFIRM_WHEN_ERROR) {
				if (data.getValue()) {
					identificationRepository.insert(new Identification(companyId, employeeId, data.getDate(), processingYmd));
				} else {
					identificationRepository.remove(companyId, employeeId, data.getDate());
				}
			} else {
				List<EmployeeDailyPerError> employeeDailyPerErrors = employeeDailyPerErrorRepository
						.find(employeeId, data.getDate()).stream().filter(x -> !x.getErrorAlarmWorkRecordCode().v().startsWith("D")).collect(Collectors.toList());
				if (!employeeDailyPerErrors.isEmpty()) {
					List<ErrorAlarmWorkRecord> errorAlarmWorkRecords = errorAlarmWorkRecordRepository
							.getListErAlByListCodeError(companyId,
									employeeDailyPerErrors.stream()
											.map(x -> x.getErrorAlarmWorkRecordCode().v())
											.collect(Collectors.toList()));
					if (errorAlarmWorkRecords.isEmpty()) {
						if (data.getValue()) {
							identificationRepository.insert(new Identification(companyId, employeeId, data.getDate(), processingYmd));
						} else {
							identificationRepository.remove(companyId, employeeId, data.getDate());
						}
					}else {
						   identificationRepository.remove(companyId, employeeId, data.getDate());
					}
				}else{
					if (data.getValue()) {
						identificationRepository.insert(new Identification(companyId, employeeId, data.getDate(), processingYmd));
					} else {
						identificationRepository.remove(companyId, employeeId, data.getDate());
					}
				}
			}
		});
	}
}
