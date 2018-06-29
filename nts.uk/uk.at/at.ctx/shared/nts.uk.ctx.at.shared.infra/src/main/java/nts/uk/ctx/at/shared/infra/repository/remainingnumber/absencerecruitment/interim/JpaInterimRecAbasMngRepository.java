package nts.uk.ctx.at.shared.infra.repository.remainingnumber.absencerecruitment.interim;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecAbasMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecAbsMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.interim.InterimRecMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.DataManagementAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.OccurrenceDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RequiredDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.SelectedAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.StatutoryAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnOffsetDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UnUsedDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UseDay;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.absencerecruitment.interim.KrcmtInterimAbsMng;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.absencerecruitment.interim.KrcmtInterimRecAbs;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.absencerecruitment.interim.KrcmtInterimRecAbsPK;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.absencerecruitment.interim.KrcmtInterimRecMng;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaInterimRecAbasMngRepository extends JpaRepository implements InterimRecAbasMngRepository{

	private String QUERY_REC_BY_ID = "SELECT c FROM KrcmtInterimRecAbs c"
			+ " WHERE c.recAbsPk.recruitmentMngId = :remainID"
			+ " AND c.recruitmentMngAtr = :mngAtr";
	private String QUERY_ABS_BY_ID = "SELECT c FROM KrcmtInterimRecAbs c"
			+ " WHERE c.recAbsPk.absenceMngID = :remainID"
			+ " AND c.recruitmentMngAtr = :mngAtr";
	private String QUERY_REC_BY_DATEPERIOD = "SELECT c FROM KrcmtInterimRecMng c"
			+ " WHERE c.recruitmentMngId in :mngIds"
			+ " AND c.unUsedDays > :unUsedDays"
			+ " AND c.expirationDate >= :startDate"
			+ " AND c.expirationDate <= :endDate";
	private String DELETE_RECMNG_BY_ID = "DELETE FROM KrcmtInterimRecMng c"
			+ " WHERE c.recruitmentMngId = :mngId";
	private String DELETE_ABSMNG_BY_ID = "DELETE FROM KrcmtInterimAbsMng c"
			+ " WHERE c.absenceMngId = :mngId";
	private String QUERY_ABS_BY_SID_MNGID = "SELECT c FROM KrcmtInterimRecAbs c"
			+ " WHERE c.recAbsPk.absenceMngID = :absenceMngID"
			+ " AND c.absenceMngAtr = :absenceMngAtr"
			+ " AND c.recruitmentMngAtr = :recruitmentMngAtr";
	private String DELETE_ABS_BY_MNGID = "DELETE FROM KrcmtInterimRecAbs c "
			+ " WHERE c.recAbsPk.absenceMngID = :mngId";
	private String DELETE_REC_BY_MNGID = "DELETE FROM KrcmtInterimRecAbs c "
			+ " WHERE c.recAbsPk.recruitmentMngId = :mngId";
	private String DELETE_BY_ID_ATR = "DELETE FROM KrcmtInterimRecAbs c"
			+ " WHERE c.recAbsPk.absenceMngID = :absId"
			+ " AND c.recAbsPk.recruitmentMngId = :recId"
			+ " AND c.absenceMngAtr = :absAtr"
			+ " AND c.recruitmentMngAtr = :recAtr";
	private String QUERY_REC_BY_SID_MNGID = "SELECT c FROM KrcmtInterimRecAbs c"
			+ " WHERE c.recAbsPk.recruitmentMngId = :recruitmentMngId"
			+ " AND c.absenceMngAtr = :absenceMngAtr"
			+ " AND c.recruitmentMngAtr = :recruitmentMngAtr";
	private String DELETE_REC_BY_ID = "DELETE FROM KrcmtInterimRecAbs c"
			+ " WHERE c.recAbsPk.recruitmentMngId = :remainID"
			+ " AND c.recruitmentMngAtr = :mngAtr";
	private String DELETE_ABS_BY_ID = "DELETE FROM KrcmtInterimRecAbs c"
			+ " WHERE c.recAbsPk.absenceMngID = :remainID"
			+ " AND c.recruitmentMngAtr = :mngAtr";
	private String QUERY_REC_BY_IDS_ATR = "SELECT c FROM KrcmtInterimRecAbs c "
			+ " WHERE c.recAbsPk.recruitmentMngId IN :recruitmentMngId"
			+ " AND c.recruitmentMngAtr = :recruitmentMngAtr";
	@Override
	public Optional<InterimRecMng> getReruitmentById(String recId) {
		return this.queryProxy().find(recId, KrcmtInterimRecMng.class)
				.map(x -> toDomainRecMng(x));
	}

	private InterimRecMng toDomainRecMng(KrcmtInterimRecMng x) {
		return new InterimRecMng(x.recruitmentMngId, 
				x.expirationDate, 
				new OccurrenceDay(x.occurrenceDays),
				EnumAdaptor.valueOf(x.statutoryAtr, StatutoryAtr.class),
				new UnUsedDay(x.unUsedDays));
	}

	@Override
	public Optional<InterimAbsMng> getAbsById(String absId) {
		return this.queryProxy().find(absId, KrcmtInterimAbsMng.class)
				.map(x -> toDomainAbsMng(x));
	}

	private InterimAbsMng toDomainAbsMng(KrcmtInterimAbsMng x) {		
		return new InterimAbsMng(x.absenceMngId, new RequiredDay(x.requiredDays), new UnOffsetDay(x.unOffsetDay));
	}

	@Override
	public List<InterimRecAbsMng> getRecOrAbsMng(String interimId, boolean isRec, DataManagementAtr mngAtr) {
		return this.queryProxy().query(isRec ? QUERY_REC_BY_ID : QUERY_ABS_BY_ID, KrcmtInterimRecAbs.class)
				.setParameter("remainID", interimId)
				.setParameter("mngAtr", mngAtr.values)
				.getList(x -> toDomainRecAbs(x));
	}

	private InterimRecAbsMng toDomainRecAbs(KrcmtInterimRecAbs x) {
		return new InterimRecAbsMng(x.recAbsPk.absenceMngID, 
				EnumAdaptor.valueOf(x.absenceMngAtr, DataManagementAtr.class),
				x.recAbsPk.recruitmentMngId,
				EnumAdaptor.valueOf(x.recruitmentMngAtr,DataManagementAtr.class),
				new UseDay(x.useDays),
				EnumAdaptor.valueOf(x.selectedAtr, SelectedAtr.class));
	}

	@Override
	public List<InterimRecMng> getRecByIdPeriod(List<String> recId, double unUseDays, DatePeriod dateData) {
		if(recId.isEmpty()) {
			return Collections.emptyList();
		}
		return this.queryProxy().query(QUERY_REC_BY_DATEPERIOD, KrcmtInterimRecMng.class)
				.setParameter("mngIds", recId)
				.setParameter("unUsedDays", unUseDays)
				.setParameter("startDate", dateData.start())
				.setParameter("endDate", dateData.end())
				.getList(c -> toDomainRecMng(c));
	}

	@Override
	public List<InterimRecAbsMng> getBySidMng(DataManagementAtr recAtr, DataManagementAtr absAtr,
			String absId) {
		return this.queryProxy().query(QUERY_ABS_BY_SID_MNGID, KrcmtInterimRecAbs.class)
				.setParameter("absenceMngID", absId)
				.setParameter("absenceMngAtr", absAtr.values)
				.setParameter("recruitmentMngAtr", recAtr.values)
				.getList(x -> toDomainRecAbs(x));
	}

	@Override
	public void persistAndUpdateInterimRecMng(InterimRecMng domain) {
		
		// キー
		val key = domain.getRecruitmentMngId();
		
		// 登録・更新
		KrcmtInterimRecMng entity = this.getEntityManager().find(KrcmtInterimRecMng.class, key);
		if (entity == null){
			entity = new KrcmtInterimRecMng();
			entity.recruitmentMngId = domain.getRecruitmentMngId();
			entity.expirationDate = domain.getExpirationDate();
			entity.occurrenceDays = domain.getOccurrenceDays().v();
			entity.statutoryAtr = domain.getStatutoryAtr().value;
			entity.unUsedDays = domain.getUnUsedDays().v();
			this.getEntityManager().persist(entity);
		}
		else {
			entity.expirationDate = domain.getExpirationDate();
			entity.occurrenceDays = domain.getOccurrenceDays().v();
			entity.statutoryAtr = domain.getStatutoryAtr().value;
			entity.unUsedDays = domain.getUnUsedDays().v();
		}
		//this.getEntityManager().flush();
	}
	
	@Override
	public void persistAndUpdateInterimAbsMng(InterimAbsMng domain) {
		
		// キー
		val key = domain.getAbsenceMngId();
		
		// 登録・更新
		KrcmtInterimAbsMng entity = this.getEntityManager().find(KrcmtInterimAbsMng.class, key);
		if (entity == null){
			entity = new KrcmtInterimAbsMng();
			entity.absenceMngId = domain.getAbsenceMngId();
			entity.requiredDays = domain.getRequeiredDays().v();
			entity.unOffsetDay = domain.getUnOffsetDays().v();
			this.getEntityManager().persist(entity);
		}
		else {
			entity.requiredDays = domain.getRequeiredDays().v();
			entity.unOffsetDay = domain.getUnOffsetDays().v();
		}
		//this.getEntityManager().flush();
	}
	
	@Override
	public void persistAndUpdateInterimRecAbsMng(InterimRecAbsMng domain) {
		
		// キー
		val key = new KrcmtInterimRecAbsPK(domain.getAbsenceMngId(), domain.getRecruitmentMngId());
		
		// 登録・更新
		KrcmtInterimRecAbs entity = this.getEntityManager().find(KrcmtInterimRecAbs.class, key);
		if (entity == null){
			entity = new KrcmtInterimRecAbs();
			entity.recAbsPk = new KrcmtInterimRecAbsPK();
			entity.recAbsPk.absenceMngID = domain.getAbsenceMngId();
			entity.recAbsPk.recruitmentMngId = domain.getRecruitmentMngId();
			entity.absenceMngAtr = domain.getAbsenceMngAtr().values;
			entity.recruitmentMngAtr = domain.getRecruitmentMngAtr().values;
			entity.useDays = domain.getUseDays().v();
			entity.selectedAtr = domain.getSelectedAtr().value;
			this.getEntityManager().persist(entity);
		}
		else {
			entity.absenceMngAtr = domain.getAbsenceMngAtr().values;
			entity.recruitmentMngAtr = domain.getRecruitmentMngAtr().values;
			entity.useDays = domain.getUseDays().v();
			entity.selectedAtr = domain.getSelectedAtr().value;
		}
		//this.getEntityManager().flush();
	}

	@Override
	public void deleteInterimRecMng(String recruitmentMngId) {
		this.getEntityManager().createQuery(DELETE_RECMNG_BY_ID).setParameter("mngId", recruitmentMngId).executeUpdate();		
	}

	@Override
	public void deleteInterimAbsMng(String absenceMngId) {
		this.getEntityManager().createQuery(DELETE_ABSMNG_BY_ID).setParameter("mngId", absenceMngId).executeUpdate();	
	}

	@Override
	public void deleteInterimRecAbsMng(String mndId, boolean isRec) {		
		this.getEntityManager().createQuery(isRec ? DELETE_REC_BY_MNGID : DELETE_ABS_BY_MNGID)
				.setParameter("mngId", mndId).executeUpdate();	
	}

	@Override
	public void deleteRecAbsMngByIdAndAtr(String recId, String absId, DataManagementAtr recAtr,
			DataManagementAtr absAtr) {
		this.getEntityManager().createQuery(DELETE_BY_ID_ATR, KrcmtInterimRecAbs.class)
				.setParameter("absId", absId)
				.setParameter("recId", recId)
				.setParameter("absAtr", absAtr.values)
				.setParameter("recAtr", recAtr.values)
				.executeUpdate();
	}

	@Override
	public void deleteRecAbsMngByIDAtr(String mngId, DataManagementAtr mngAtr, boolean isRec) {
		this.getEntityManager().createQuery(isRec ? DELETE_REC_BY_ID : DELETE_ABS_BY_ID, KrcmtInterimRecAbs.class)
			.setParameter("remainID", mngId)
			.setParameter("mngAtr", mngAtr.values)
			.executeUpdate();
			
	}

	@Override
	public List<InterimRecAbsMng> getRecBySidMngAtr(DataManagementAtr recAtr, DataManagementAtr absAtr, String recId) {
		return this.queryProxy().query(QUERY_REC_BY_SID_MNGID, KrcmtInterimRecAbs.class)
				.setParameter("recruitmentMngId", recId)
				.setParameter("absenceMngAtr", absAtr.values)
				.setParameter("recruitmentMngAtr", recAtr.values)
				.getList(x -> toDomainRecAbs(x));
	}

	@Override
	public List<InterimRecAbsMng> getRecByIdsMngAtr(List<String> recIds, DataManagementAtr recMngAtr) {
		return this.queryProxy().query(QUERY_REC_BY_IDS_ATR, KrcmtInterimRecAbs.class)
				.setParameter("recruitmentMngId", recIds)
				.setParameter("recruitmentMngAtr", recMngAtr.values)
				.getList(x -> toDomainRecAbs(x));
	}
}
