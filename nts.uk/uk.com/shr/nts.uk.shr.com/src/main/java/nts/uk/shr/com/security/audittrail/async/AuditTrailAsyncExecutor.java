package nts.uk.shr.com.security.audittrail.async;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Optional;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import lombok.val;
import lombok.extern.slf4j.Slf4j;
import nts.arc.bean.SingletonBeansSoftCache;
import nts.arc.diagnose.PrintStackTrace;
import nts.arc.task.AsyncTask;
import nts.arc.task.AsyncTaskService;
import nts.arc.time.GeneralDateTime;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.security.audittrail.AuditTrailTransaction;
import nts.uk.shr.com.security.audittrail.UserInfoAdaptorForLog;
import nts.uk.shr.com.security.audittrail.basic.LogBasicInformation;
import nts.uk.shr.com.security.audittrail.basic.LoginInformation;
import nts.uk.shr.com.security.audittrail.correction.CorrectionLoggingAgent;
import nts.uk.shr.com.security.audittrail.correction.processor.CorrectionProcessorId;

@Slf4j
@ApplicationScoped
public class AuditTrailAsyncExecutor implements CorrectionLoggingAgent {

	/**
	 * AsyncTaskService
	 */
	@Inject
	private AsyncTaskService asyncTaskService;
	
	@Inject
	private UserInfoAdaptorForLog userInfoAdaptor;
	
	@Override
	public void requestProcess(String operationId, CorrectionProcessorId processorId, HashMap<String, Serializable> parameters) {
		
		log.debug("[AuditTrailAsyncExecutor ENTER] operation:" + operationId + ", processor:" + processorId);
		log.debug(parameters.toString());
		
		val userContext = AppContexts.user();
		
		val userInfo = userContext.isEmployee() 
				? this.userInfoAdaptor.findByEmployeeId(userContext.employeeId())
				: this.userInfoAdaptor.findByUserId(userContext.userId());
		
		log.debug("[AuditTrailAsyncExecutor] CID: " + userContext.companyId() + ", SCD: " + userContext.employeeCode());
		
		val basicInfo = new LogBasicInformation(
				operationId,
				userContext.companyId(),
				userInfo,
				LoginInformation.byAppContexts(),
				GeneralDateTime.now(),
				userContext.roles(),
				AppContexts.requestedWebApi().getScreenIdentifier(),
				Optional.empty());
		
		val task = AsyncTask.builder()
				.withContexts()
				.keepsTrack(false)
				.build(() -> {

					log.debug("[AuditTrailAsyncExecutor ASYNC START] " + operationId);
					
					try {
						val auditTrailTransaction = SingletonBeansSoftCache.get(AuditTrailTransaction.class);
						auditTrailTransaction.begin(basicInfo, processorId, parameters);
						log.debug("[AuditTrailAsyncExecutor ASYNC DONE] " + operationId);
					} catch (Exception ex) {
						log.debug("[AuditTrailAsyncExecutor ASYNC FAILED] " + operationId);
						PrintStackTrace.toLog(ex);
					}
				});
		
		this.asyncTaskService.execute(task);
		
		log.debug("[AuditTrailAsyncExecutor EXIT] " + operationId);
	}

}
