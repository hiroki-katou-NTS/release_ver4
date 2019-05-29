package nts.uk.ctx.at.record.dom.workrecord.identificationstatus.algorithm;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import org.apache.commons.lang3.tuple.Pair;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
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

//	@Inject
//	private EmployeeDailyPerErrorRepository employeeDailyPerErrorRepository;

	@Inject
	private ErrorAlarmWorkRecordRepository errorAlarmWorkRecordRepository;
	
	// code old 
//	@Inject
//	private OpOfDailyPerformance opOfDailyPerformance;

	public boolean registerIdentity(ParamIdentityConfirmDay param, List<EmployeeDailyPerError> employeeDailyPerErrors, Set<Pair<String, GeneralDate>> updated) {
		String companyId = AppContexts.user().companyId();
		Optional<IdentityProcessUseSet> identityProcessOpt = identityProcessUseSetRepository.findByKey(companyId);
		if (identityProcessOpt.isPresent()) {
			checkIdentityVerification.check(identityProcessOpt.get()).ifPresent(canRegist -> {
				registIdentification(param, companyId, canRegist, employeeDailyPerErrors);
			});
			
			if(identityProcessOpt.get().isUseConfirmByYourself()){
				param.getSelfConfirmDay().stream().forEach(cd -> {
					updated.add(Pair.of(param.getEmployeeId(), cd.getDate()));
//					workInfo.updated(param.getEmployeeId(), cd.getDate());
				});
				return true;
			}
		}
		
		return false;
	}

	private void registIdentification(ParamIdentityConfirmDay param, String companyId, SelfConfirmError canRegist, List<EmployeeDailyPerError> errors) {
		GeneralDate processingYmd = GeneralDate.today();
		String employeeId = param.getEmployeeId();
		param.getSelfConfirmDay().forEach(data -> {
			if (canRegist == SelfConfirmError.CAN_CONFIRM_WHEN_ERROR) {
				if (data.getValue()) {
					Optional<Identification> indenOpt = identificationRepository.findByCode(employeeId, data.getDate());
					if(!indenOpt.isPresent()) identificationRepository.insert(new Identification(companyId, employeeId, data.getDate(), processingYmd));
				} else {
					identificationRepository.remove(companyId, employeeId, data.getDate());
				}
			} else {
				List<EmployeeDailyPerError> employeeDailyPerErrors = errors.stream()
						.filter(x -> x.getEmployeeID().equals(employeeId) && 
									x.getDate().equals(data.getDate()) &&
									!x.getErrorAlarmWorkRecordCode().v().startsWith("D"))
						.collect(Collectors.toList());
				if (!employeeDailyPerErrors.isEmpty()) {
					List<ErrorAlarmWorkRecord> errorAlarmWorkRecords = errorAlarmWorkRecordRepository
							.getListErAlByListCodeError(companyId,
									employeeDailyPerErrors.stream()
											.map(x -> x.getErrorAlarmWorkRecordCode().v())
											.collect(Collectors.toList()));
					if (errorAlarmWorkRecords.isEmpty()) {
						if (data.getValue()) {
							Optional<Identification> indenOpt = identificationRepository.findByCode(employeeId, data.getDate());
							if(!indenOpt.isPresent()) identificationRepository.insert(new Identification(companyId, employeeId, data.getDate(), processingYmd));
						} else {
							identificationRepository.remove(companyId, employeeId, data.getDate());
						}
					}else {
						   identificationRepository.remove(companyId, employeeId, data.getDate());
					}
				}else{
					if (data.getValue()) {
						Optional<Identification> indenOpt = identificationRepository.findByCode(employeeId, data.getDate());
						if(!indenOpt.isPresent()) identificationRepository.insert(new Identification(companyId, employeeId, data.getDate(), processingYmd));
					} else {
						identificationRepository.remove(companyId, employeeId, data.getDate());
					}
				}
			}
		});
	}
}
