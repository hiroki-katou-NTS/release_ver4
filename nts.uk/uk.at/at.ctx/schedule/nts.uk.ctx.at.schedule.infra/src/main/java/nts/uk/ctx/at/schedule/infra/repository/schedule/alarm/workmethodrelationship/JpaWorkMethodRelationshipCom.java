package nts.uk.ctx.at.schedule.infra.repository.schedule.alarm.workmethodrelationship;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import lombok.Value;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.uk.ctx.at.schedule.dom.schedule.alarm.workmethodrelationship.WorkMethod;
import nts.uk.ctx.at.schedule.dom.schedule.alarm.workmethodrelationship.WorkMethodAttendance;
import nts.uk.ctx.at.schedule.dom.schedule.alarm.workmethodrelationship.WorkMethodRelationshipCom;
import nts.uk.ctx.at.schedule.dom.schedule.alarm.workmethodrelationship.WorkMethodRelationshipComRepo;
import nts.uk.ctx.at.schedule.infra.entity.schedule.alarm.workmethodrelationship.KscmtAlchkWorkContextCmp;
import nts.uk.ctx.at.schedule.infra.entity.schedule.alarm.workmethodrelationship.KscmtAlchkWorkContextCmpDtl;
import nts.uk.ctx.at.schedule.infra.entity.schedule.alarm.workmethodrelationship.KscmtAlchkWorkContextCmpPk;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class JpaWorkMethodRelationshipCom extends JpaRepository implements WorkMethodRelationshipComRepo{
	
	private static final String SELECT_ONE_WORK_CONTEXT = "select * from KSCMT_ALCHK_WORK_CONTEXT_CMP"
			+ " where CID = @companyId"
			+ " and PREVIOUS_WORK_ATR = @prevWorkMethod"
			+ " and PREVIOUS_WKTM_CD = @prevWorkTimeCode";
	
	private static final String SELECT_ONE_WORK_CONTEXT_DTL = "select * from KSCMT_ALCHK_WORK_CONTEXT_CMP_DTL"
			+ " where CID = @companyId"
			+ " and PREVIOUS_WORK_ATR = @prevWorkMethod"
			+ " and PREVIOUS_WKTM_CD = @prevWorkTimeCode"
			+ " order by TGT_WKTM_CD ASC";
	
	private static final String SELECT_LIST_WORK_CONTEXT = "select * from KSCMT_ALCHK_WORK_CONTEXT_CMP"
			+ " where CID = @companyId"
			+ " and PREVIOUS_WKTM_CD in @prevWorkTimeCodeList"
			+ " order by PREVIOUS_WKTM_CD ASC";
	
	private static final String SELECT_LIST_WORK_CONTEXT_DTL = "select * from KSCMT_ALCHK_WORK_CONTEXT_CMP_DTL"
			+ " where CID = @companyId"
			+ " and PREVIOUS_WKTM_CD = @prevWorkTimeCodeList"
			+ " order by PREVIOUS_WKTM_CD, TGT_WKTM_CD ASC";
	
	private static final String SELECT_ALL_WORK_CONTEXT = "select * from KSCMT_ALCHK_WORK_CONTEXT_CMP"
			+ " where CID = @companyId"
			+ " order by PREVIOUS_WKTM_CD ASC";
	
	private static final String SELECT_ALL_WORK_CONTEXT_DTL = "select * from KSCMT_ALCHK_WORK_CONTEXT_CMP_DTL"
			+ " where CID = @companyId"
			+ " order by PREVIOUS_WKTM_CD, TGT_WKTM_CD ASC";

	@Override
	public void insert(WorkMethodRelationshipCom domain) {
		KscmtAlchkWorkContextCmp workContext = KscmtAlchkWorkContextCmp.fromDomain(domain);
		List<KscmtAlchkWorkContextCmpDtl> workContextDtlList = KscmtAlchkWorkContextCmpDtl.fromDomain(domain);
		
		this.commandProxy().insert(workContext);
		this.commandProxy().insertAll(workContextDtlList);
	}

	@Override
	public void update(WorkMethodRelationshipCom domain) {
		KscmtAlchkWorkContextCmp workContext = KscmtAlchkWorkContextCmp.fromDomain(domain);
		List<KscmtAlchkWorkContextCmpDtl> workContextDtlList = KscmtAlchkWorkContextCmpDtl.fromDomain(domain);
		
		this.commandProxy().update(workContext);
		this.commandProxy().updateAll(workContextDtlList);
	}

	@Override
	public void deleteAll(String companyId) {
		Entities entities = getAllEntities(companyId);
		
		this.commandProxy().removeAll(entities.workContextList);
		this.commandProxy().removeAll(entities.workContextDtlList);
		
	}

	@Override
	public void deleteWithWorkMethod(String companyId, WorkMethod prevWorkMethod) {
		
		String prevWorkTimeCode = prevWorkMethod.getWorkMethodClassification().isAttendance() ?
				((WorkMethodAttendance) prevWorkMethod).getWorkTimeCode().v() : 
					KscmtAlchkWorkContextCmp.HOLIDAY_WORK_TIME_CODE;
		
		Optional<KscmtAlchkWorkContextCmp> workContext = 
				new NtsStatement(SELECT_ONE_WORK_CONTEXT, this.jdbcProxy())
				.paramString("companyId", companyId)
				.paramInt("prevWorkMethod", prevWorkMethod.getWorkMethodClassification().value)
				.paramString("prevWorkTimeCode", prevWorkTimeCode)
				.getSingle(KscmtAlchkWorkContextCmp.mapper);
		
		if (!workContext.isPresent()) {
			return;
		}
		
		List<KscmtAlchkWorkContextCmpDtl> workContextDtlList = 
				new NtsStatement(SELECT_ONE_WORK_CONTEXT_DTL, this.jdbcProxy())
				.paramString("companyId", companyId)
				.paramInt("prevWorkMethod", prevWorkMethod.getWorkMethodClassification().value)
				.paramString("prevWorkTimeCode", prevWorkTimeCode)
				.getList( KscmtAlchkWorkContextCmpDtl.mapper);
		
		this.commandProxy().remove(workContext);
		this.commandProxy().removeAll(workContextDtlList);
		
	}

	@Override
	public List<WorkMethodRelationshipCom> getAll(String companyId) {
		
		Entities entities = getAllEntities(companyId);
		
		return toDomain(entities.workContextList, entities.workContextDtlList);
	}

	@Override
	public Optional<WorkMethodRelationshipCom> getWithWorkMethod(String companyId, WorkMethod prevWorkMethod) {
		
		String prevWorkTimeCode = prevWorkMethod.getWorkMethodClassification().isAttendance() ?
				((WorkMethodAttendance) prevWorkMethod).getWorkTimeCode().v() : 
					KscmtAlchkWorkContextCmp.HOLIDAY_WORK_TIME_CODE;
		
		Optional<KscmtAlchkWorkContextCmp> workContext = 
				new NtsStatement(SELECT_ONE_WORK_CONTEXT, this.jdbcProxy())
				.paramString("companyId", companyId)
				.paramInt("prevWorkMethod", prevWorkMethod.getWorkMethodClassification().value)
				.paramString("prevWorkTimeCode", prevWorkTimeCode)
				.getSingle(KscmtAlchkWorkContextCmp.mapper);
		
		if (!workContext.isPresent()) {
			return Optional.empty();
		}
		
		List<KscmtAlchkWorkContextCmpDtl> workContextDtlList = 
				new NtsStatement(SELECT_ONE_WORK_CONTEXT_DTL, this.jdbcProxy())
				.paramString("companyId", companyId)
				.paramInt("prevWorkMethod", prevWorkMethod.getWorkMethodClassification().value)
				.paramString("prevWorkTimeCode", prevWorkTimeCode)
				.getList( KscmtAlchkWorkContextCmpDtl.mapper);
		
		 WorkMethodRelationshipCom domain = toDomain(Arrays.asList(workContext.get()), workContextDtlList).get(0);
		return Optional.of(domain);
		
	}

	@Override
	public List<WorkMethodRelationshipCom> getWithWorkMethodList(String companyId, List<WorkMethod> prevWorkMethodList) {
		
		List<String> prevWorkTimeCodeList = prevWorkMethodList.stream().map( p -> 
			p.getWorkMethodClassification().isAttendance() ? 
					((WorkMethodAttendance) p).getWorkTimeCode().v() : 
					KscmtAlchkWorkContextCmp.HOLIDAY_WORK_TIME_CODE
		).collect(Collectors.toList());
		
		List<KscmtAlchkWorkContextCmp> workContextList = 
				new NtsStatement(SELECT_LIST_WORK_CONTEXT, this.jdbcProxy())
				.paramString("companyId", companyId)
				.paramString("prevWorkTimeCodeList", prevWorkTimeCodeList)
				.getList( KscmtAlchkWorkContextCmp.mapper);
		
		List<KscmtAlchkWorkContextCmpDtl> workContextDtlList = 
				new NtsStatement(SELECT_LIST_WORK_CONTEXT_DTL, this.jdbcProxy())
				.paramString("prevWorkTimeCodeList", prevWorkTimeCodeList)
				.paramString("companyId", companyId)
				.getList( KscmtAlchkWorkContextCmpDtl.mapper);
		
		return toDomain(workContextList, workContextDtlList);
	}

	@Override
	public boolean exists(String companyId, WorkMethod prevWorkMethod) {
		
		String prevWorkTimeCode = prevWorkMethod.getWorkMethodClassification().isAttendance() ?
				((WorkMethodAttendance) prevWorkMethod).getWorkTimeCode().v() : 
					KscmtAlchkWorkContextCmp.HOLIDAY_WORK_TIME_CODE;
		
		return this.queryProxy().find(
				new KscmtAlchkWorkContextCmpPk(
					companyId, 
					prevWorkMethod.getWorkMethodClassification().value, 
					prevWorkTimeCode), 
				KscmtAlchkWorkContextCmp.class)
			.isPresent();
		
	}
	
	private List<WorkMethodRelationshipCom> toDomain(List<KscmtAlchkWorkContextCmp> workContextList, 
			List<KscmtAlchkWorkContextCmpDtl> workContextDtlList) {
		
		return workContextList.stream().map( wContext -> {
			
			List<KscmtAlchkWorkContextCmpDtl> dtlList = 
					workContextDtlList.stream()
					.filter( dtl -> dtl.pk.prevWorkTimeCode == wContext.pk.prevWorkTimeCode)
					.collect(Collectors.toList());
			
			return wContext.toDomain(dtlList);
		
		}).collect(Collectors.toList());
		
	}
	
	private Entities getAllEntities(String companyId) {
		
		List<KscmtAlchkWorkContextCmp> workContextList = 
				new NtsStatement(SELECT_ALL_WORK_CONTEXT, this.jdbcProxy())
				.paramString("companyId", companyId)
				.getList( KscmtAlchkWorkContextCmp.mapper);
		
		List<KscmtAlchkWorkContextCmpDtl> workContextDtlList = 
				new NtsStatement(SELECT_ALL_WORK_CONTEXT_DTL, this.jdbcProxy())
				.paramString("companyId", companyId)
				.getList( KscmtAlchkWorkContextCmpDtl.mapper);
		
		return new Entities(workContextList, workContextDtlList); 
	}
	
	@Value
	class Entities {
		
		List<KscmtAlchkWorkContextCmp> workContextList;
		
		List<KscmtAlchkWorkContextCmpDtl> workContextDtlList; 
	} 


}
