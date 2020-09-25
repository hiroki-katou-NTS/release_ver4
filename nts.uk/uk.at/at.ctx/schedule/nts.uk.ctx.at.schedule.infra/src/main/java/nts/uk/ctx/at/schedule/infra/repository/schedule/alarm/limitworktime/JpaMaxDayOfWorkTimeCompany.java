package nts.uk.ctx.at.schedule.infra.repository.schedule.alarm.limitworktime;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.uk.ctx.at.schedule.dom.schedule.alarm.continuouswork.limitworktime.MaxDayOfWorkTimeCompany;
import nts.uk.ctx.at.schedule.dom.schedule.alarm.continuouswork.limitworktime.MaxDayOfWorkTimeCompanyRepo;
import nts.uk.ctx.at.schedule.dom.schedule.alarm.continuouswork.limitworktime.WorkTimeMaximumCode;
import nts.uk.ctx.at.schedule.infra.entity.schedule.alarm.limitworktime.KscmtAlchkMaxdaysWktmCmp;
import nts.uk.ctx.at.schedule.infra.entity.schedule.alarm.limitworktime.KscmtAlchkMaxdaysWktmCmpDtl;
import nts.uk.ctx.at.schedule.infra.entity.schedule.alarm.limitworktime.KscmtAlchkMaxdaysWktmCmpPk;

@Stateless
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
public class JpaMaxDayOfWorkTimeCompany extends JpaRepository implements MaxDayOfWorkTimeCompanyRepo {
	
	public static final String SELECT_MAX_DAYS_WKTM = "select * from KSCMT_ALCHK_MAXDAYS_WKTM_CMP"
			+ " where CID = @companyId"
			+ " and CD = @code";
	
	public static final String SELECT_MAX_DAYS_WKTM_DTL = "select * from KSCMT_ALCHK_MAXDAYS_WKTM_CMP_DTL"
			+ " where CID = @companyId"
			+ " and CD = @code";
	
	public static final String SELECT_ALL_MAX_DAYS_WKTM = "select * from KSCMT_ALCHK_MAXDAYS_WKTM_CMP"
			+ " where CID = @companyId";
	
	public static final String SELECT_ALL_MAX_DAYS_WKTM_DTL = "select * from KSCMT_ALCHK_MAXDAYS_WKTM_CMP_DTL"
			+ " where CID = @companyId";

	@Override
	public void insert(String companyId, MaxDayOfWorkTimeCompany domain) {
		KscmtAlchkMaxdaysWktmCmp maxDaysWktm = KscmtAlchkMaxdaysWktmCmp.fromDomain(companyId, domain);
		List<KscmtAlchkMaxdaysWktmCmpDtl> maxDaysWktmDtl = KscmtAlchkMaxdaysWktmCmpDtl.fromDomain(companyId, domain);
		
		this.commandProxy().insert(maxDaysWktm);
		this.commandProxy().insertAll(maxDaysWktmDtl);
	}

	@Override
	public void update(String companyId, MaxDayOfWorkTimeCompany domain) {
		KscmtAlchkMaxdaysWktmCmp maxDaysWktm = KscmtAlchkMaxdaysWktmCmp.fromDomain(companyId, domain);
		List<KscmtAlchkMaxdaysWktmCmpDtl> maxDaysWktmDtlList = KscmtAlchkMaxdaysWktmCmpDtl.fromDomain(companyId, domain);
		
		this.commandProxy().update(maxDaysWktm);
		this.commandProxy().updateAll(maxDaysWktmDtlList);
	}


	@Override
	public boolean exists(String companyId, WorkTimeMaximumCode code) {
		
		return this.queryProxy()
				.find(new KscmtAlchkMaxdaysWktmCmpPk(companyId, code.v()), KscmtAlchkMaxdaysWktmCmp.class)
				.isPresent();
	}

	@Override
	public void delete(String companyId, WorkTimeMaximumCode code) {
		
		List<KscmtAlchkMaxdaysWktmCmpDtl> maxDaysWktmDtlList = 
				new NtsStatement( SELECT_MAX_DAYS_WKTM_DTL, this.jdbcProxy())
				.paramString("companyId", companyId)
				.paramString("code", code.v())
				.getList( KscmtAlchkMaxdaysWktmCmpDtl.mapper );
		
		this.commandProxy().remove(KscmtAlchkMaxdaysWktmCmp.class, new KscmtAlchkMaxdaysWktmCmpPk(companyId, code.v()));
		this.commandProxy().removeAll(maxDaysWktmDtlList);
	}

	@Override
	public Optional<MaxDayOfWorkTimeCompany> get(String companyId, WorkTimeMaximumCode code) {
		
		Optional<KscmtAlchkMaxdaysWktmCmp> maxDaysWktm = 
				new NtsStatement( SELECT_MAX_DAYS_WKTM, this.jdbcProxy())
				.paramString("companyId", companyId)
				.paramString("code", code.v())
				.getSingle(KscmtAlchkMaxdaysWktmCmp.mapper );
		
		if (!maxDaysWktm.isPresent()) {
			return Optional.empty();
		}
		
		List<KscmtAlchkMaxdaysWktmCmpDtl> maxDaysWktmDtlList = 
				new NtsStatement( SELECT_MAX_DAYS_WKTM_DTL, this.jdbcProxy())
				.paramString("companyId", companyId)
				.paramString("code", code.v())
				.getList( KscmtAlchkMaxdaysWktmCmpDtl.mapper );
		
		return Optional.of(maxDaysWktm.get().toDomain(maxDaysWktmDtlList));
	}

	@Override
	public List<MaxDayOfWorkTimeCompany> getAll(String companyId) {
		
		List<KscmtAlchkMaxdaysWktmCmp> maxDaysWktmList = 
				new NtsStatement( SELECT_ALL_MAX_DAYS_WKTM, this.jdbcProxy())
				.paramString("companyId", companyId)
				.getList( KscmtAlchkMaxdaysWktmCmp.mapper );
		
		List<KscmtAlchkMaxdaysWktmCmpDtl> maxDaysWktmDtlList = 
				new NtsStatement( SELECT_ALL_MAX_DAYS_WKTM_DTL, this.jdbcProxy())
				.paramString("companyId", companyId)
				.getList( KscmtAlchkMaxdaysWktmCmpDtl.mapper );
		
		return maxDaysWktmList.stream().map( mdw -> {
			
			List<KscmtAlchkMaxdaysWktmCmpDtl> dtlList = maxDaysWktmDtlList.stream()
					.filter( dtl -> dtl.pk.code == mdw.pk.code)
					.collect(Collectors.toList());
			
			return mdw.toDomain(dtlList);
			
		}).collect(Collectors.toList());
		
	}

}
