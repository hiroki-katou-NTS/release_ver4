package nts.uk.ctx.at.record.dom.workrecord.identificationstatus.algorithm;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerError;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.EmployeeDailyPerErrorRepository;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecordRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.Identification;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.IdentityProcessUseSet;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.enums.SelfConfirmError;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentificationRepository;
import nts.uk.ctx.at.record.dom.workrecord.identificationstatus.repository.IdentityProcessUseSetRepository;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.ConfirmOfManagerOrYouself;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.OpOfDailyPerformance;
import nts.uk.ctx.at.record.dom.workrecord.operationsetting.OperationOfDailyPerformance;
import nts.uk.ctx.at.shared.dom.common.CompanyId;
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
	
	// code old 
	@Inject
	private OpOfDailyPerformance opOfDailyPerformance;

	public void registerIdentity(ParamIdentityConfirmDay param) {
		String companyId = AppContexts.user().companyId();
		GeneralDate processingYmd = GeneralDate.today();
		Optional<IdentityProcessUseSet> identityProcessOpt = identityProcessUseSetRepository.findByKey(companyId);
		if (identityProcessOpt.isPresent()) {
			Optional<SelfConfirmError> canRegist = checkIdentityVerification.check(identityProcessOpt.get());
			param.getSelfConfirmDay().forEach(data -> {
				String employeeId = AppContexts.user().employeeId();
				if (canRegist.isPresent()) {
					if (canRegist.get() == SelfConfirmError.CAN_CONFIRM_WHEN_ERROR) {
						if (data.getValue()) {
							identificationRepository
									.insert(new Identification(companyId, employeeId, data.getDate(), processingYmd));
						} else {
							identificationRepository.remove(companyId, employeeId, data.getDate());
						}
					} else {
						List<EmployeeDailyPerError> employeeDailyPerErrors = employeeDailyPerErrorRepository
								.find(employeeId, data.getDate());
						if (!employeeDailyPerErrors.isEmpty()) {
							List<ErrorAlarmWorkRecord> errorAlarmWorkRecords = errorAlarmWorkRecordRepository
									.getListErAlByListCodeError(companyId,
											employeeDailyPerErrors.stream()
													.map(x -> x.getErrorAlarmWorkRecordCode().v())
													.collect(Collectors.toList()));
							if (errorAlarmWorkRecords.isEmpty()) {
								if (data.getValue()) {
									identificationRepository.insert(
											new Identification(companyId, employeeId, data.getDate(), processingYmd));
								} else {
									identificationRepository.remove(companyId, employeeId, data.getDate());
								}
							}
						}
					}
				}
			});
		}
	}
	
	public void registerIdentityOld(ParamIdentityConfirmDay param) {
		String companyId = AppContexts.user().companyId();
		GeneralDate processingYmd = GeneralDate.today();
		//Optional<IdentityProcessUseSet> identityProcessOpt = identityProcessUseSetRepository.findByKey(companyId);
		OperationOfDailyPerformance operation = opOfDailyPerformance.find(new CompanyId(companyId));
		if (operation != null && operation.getFunctionalRestriction() != null) {
			 Optional<ConfirmOfManagerOrYouself> canRegist = checkIdentityVerification.checkOld(operation.getFunctionalRestriction());
			param.getSelfConfirmDay().forEach(data -> {
				String employeeId = AppContexts.user().employeeId();
				if (canRegist.isPresent()) {
					if (canRegist.get() == ConfirmOfManagerOrYouself.CAN_CHECK_WHEN_ERROR) {
						if (data.getValue()) {
							identificationRepository
									.insert(new Identification(companyId, employeeId, data.getDate(), processingYmd));
						} else {
							identificationRepository.remove(companyId, employeeId, data.getDate());
						}
					} else {
						List<EmployeeDailyPerError> employeeDailyPerErrors = employeeDailyPerErrorRepository
								.find(employeeId, data.getDate());
						if (!employeeDailyPerErrors.isEmpty()) {
							List<ErrorAlarmWorkRecord> errorAlarmWorkRecords = errorAlarmWorkRecordRepository
									.getListErAlByListCodeError(companyId,
											employeeDailyPerErrors.stream()
													.map(x -> x.getErrorAlarmWorkRecordCode().v())
													.collect(Collectors.toList()));
							if (errorAlarmWorkRecords.isEmpty()) {
								if (data.getValue()) {
									identificationRepository.insert(
											new Identification(companyId, employeeId, data.getDate(), processingYmd));
								} else {
									identificationRepository.remove(companyId, employeeId, data.getDate());
								}
							}
						}else{
							if (data.getValue()) {
								identificationRepository.insert(
										new Identification(companyId, employeeId, data.getDate(), processingYmd));
							} else {
								identificationRepository.remove(companyId, employeeId, data.getDate());
							}
						}
					}
				}
			});
		}
	}
}
