package nts.uk.ctx.sys.shared.infra.repository;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDateTime;
import nts.uk.ctx.sys.shared.dom.toppagealarm.TopPageAlarm;
import nts.uk.ctx.sys.shared.dom.toppagealarm.TopPageAlarmDetail;
import nts.uk.ctx.sys.shared.dom.toppagealarm.TopPageAlarmRepository;
import nts.uk.ctx.sys.shared.infra.entity.KrcstToppageAlarm;
import nts.uk.ctx.sys.shared.infra.entity.KrcstToppageAlarmDetail;
import nts.uk.ctx.sys.shared.infra.entity.KrcstToppageAlarmDetailPK;
import nts.uk.shr.com.context.AppContexts;
@Stateless
public class JpaTopPageAlarmRepository extends JpaRepository implements TopPageAlarmRepository{
	// toppage alarm table
	private static final String SELECT_BYCOM = "SELECT c FROM KrcstToppageAlarm c WHERE c.companyId = :companyId ";
	private static final String SELECT_BYEMP = SELECT_BYCOM + "AND c.managerId = :managerId ";
	private static final String SELECT_ROGER = SELECT_BYEMP + "AND c.rogerFlag = :rogerFlag ";
	private static final String SELECT_BYDATE = SELECT_ROGER + "AND c.finishDateTime BETWEEN :dateStart AND :dateEnd ";
	private static final String SELECT_EMP_DATE = SELECT_BYEMP + "AND c.finishDateTime BETWEEN :dateStart AND :dateEnd ";
	private static final String SELECT_EXECUTIONCONTENT = SELECT_BYCOM + "AND c.executionContent = :executionContent ";
	
	// toppage alarm detail table 
	private static final String SELECT_BYLOGID = "SELECT c FROM KrcstToppageAlarmDetail c WHERE c.krcstToppageAlarmDetailPK.executionLogId = :executionLogId ";
	private static final String SELECT_SORT = SELECT_BYLOGID + "ORDER BY c.krcstToppageAlarmDetailPK.serialNo, c.targerEmployee ASC";
	
	// convert from entity to toppage alarm domain
	private TopPageAlarm toDomain(KrcstToppageAlarm entity){
		return TopPageAlarm.createFromJavaType(entity.companyId, entity.executionLogId, 
												entity.managerId, entity.finishDateTime, 
												entity.executionContent, entity.existenceError, 
												entity.rogerFlag, entity.isCancelled);
	}
	
	// convert from domain to entity, this function created for request list No.477
	private KrcstToppageAlarm toEntity(String executionLogId, String managerId, int executionContent, int isCancelled, int existenceError){
		val entity = new KrcstToppageAlarm();
		entity.executionLogId = executionLogId;
		entity.companyId = AppContexts.user().companyId();
		entity.managerId = managerId;
		entity.executionContent = executionContent;
		entity.existenceError = existenceError;
		entity.finishDateTime = GeneralDateTime.now();
		entity.isCancelled = isCancelled;
		entity.rogerFlag = 0;
		return entity;
	}
	
	// convert from entity to toppage alarm detail domain
	private TopPageAlarmDetail toDomainDetail(KrcstToppageAlarmDetail entity){
		return TopPageAlarmDetail.createFromJavaType(entity.krcstToppageAlarmDetailPK.executionLogId, 
														entity.krcstToppageAlarmDetailPK.serialNo,
														entity.errorMessage, entity.targerEmployee);
	}
	
	// convert from domain to entity, this function created for request list No.477
	private KrcstToppageAlarmDetail toEntityDetail(TopPageAlarmDetail domainDetail){
		KrcstToppageAlarmDetail entityDetail = new KrcstToppageAlarmDetail(
				new KrcstToppageAlarmDetailPK(
						domainDetail.getExecutionLogId(),
						domainDetail.getSerialNo().v()
						),
				domainDetail.getErrorMessage().v(),
				domainDetail.getTargerEmployee()
				);
//		entityDetail.errorMessage = domainDetail.getErrorMessage().v();
//		entityDetail.targerEmployee = domainDetail.getTargerEmployee();
//		entityDetail.krcstToppageAlarmDetailPK.executionLogId = domainDetail.getExecutionLogId();
//		entityDetail.krcstToppageAlarmDetailPK.serialNo = domainDetail.getSerialNo().v();
		return entityDetail;
	}
	
	// find toppage alarm by companyId, employeeid and rogerFlag = 0 (have not read)
	@Override
	public List<TopPageAlarm> findToppage(String companyId, String managerId, int rogerFlag, int month) {
		GeneralDateTime dateEnd = GeneralDateTime.now();
		GeneralDateTime dateStart = dateEnd.addDays(-month);
		return this.queryProxy().query(SELECT_BYDATE, KrcstToppageAlarm.class)
								.setParameter("companyId", companyId)
								.setParameter("managerId", managerId)
								.setParameter("rogerFlag", rogerFlag)
								.setParameter("dateStart", dateStart)
								.setParameter("dateEnd", dateEnd)
								.getList(c -> toDomain(c));
	}
	
	// find toppage alarm detail by executionLogId and processingName
	@Override
	public List<TopPageAlarmDetail> findDetail(String executionLogId) {
		return this.queryProxy().query(SELECT_SORT, KrcstToppageAlarmDetail.class)
								.setParameter("executionLogId", executionLogId)
								.getList(x -> toDomainDetail(x));
	}
	
	// update roger Flag
	@Override
	public void updateRoger(String executionLogId, int rogerFlag) {
		Optional<KrcstToppageAlarm> find = this.queryProxy().find(executionLogId, KrcstToppageAlarm.class);
		if(find != null){
			find.get().setRogerFlag(rogerFlag);
			this.commandProxy().update(find.get());
		}
	}

	@Override
	public List<TopPageAlarm> findAllToppage(String companyId, String managerId, int month) {
		GeneralDateTime dateEnd = GeneralDateTime.now();
		GeneralDateTime dateStart = dateEnd.addDays(-month);
		return this.queryProxy().query(SELECT_EMP_DATE, KrcstToppageAlarm.class)
								.setParameter("companyId", companyId)
								.setParameter("managerId", managerId)
								.setParameter("dateStart", dateStart)
								.setParameter("dateEnd", dateEnd)
								.getList(c -> toDomain(c));
	}
	
	// find toppage alarm by companyId and executionContent for request list 477
	@Override
	public List<TopPageAlarm> findByExecutionContent(String companyId, int executionContent) {
		return this.queryProxy().query(SELECT_EXECUTIONCONTENT, KrcstToppageAlarm.class)
								.setParameter("companyId", companyId)
								.setParameter("executionContent", executionContent)
								.getList(c -> toDomain(c));
	}

	// function insert for request list 477
	@Override
	public void insertTopPage(String executionLogId, String managerId, int executionContent, int isCancelled, int existenceError) {
		KrcstToppageAlarm entity = toEntity(executionLogId, managerId, executionContent, isCancelled, existenceError);
		this.commandProxy().insert(entity);
	}

	@Override
	public void insertDetail(TopPageAlarmDetail domain) {
		KrcstToppageAlarmDetail entity = toEntityDetail(domain);
		this.commandProxy().insert(entity);
	}

}
