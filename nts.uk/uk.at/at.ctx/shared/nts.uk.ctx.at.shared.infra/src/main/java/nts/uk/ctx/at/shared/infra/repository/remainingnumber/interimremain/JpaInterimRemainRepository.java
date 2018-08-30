package nts.uk.ctx.at.shared.infra.repository.remainingnumber.interimremain;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemain;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.CreateAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainAtr;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.RemainType;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.interimremain.KrcmtInterimRemainMng;
import nts.uk.shr.com.time.calendar.period.DatePeriod;

@Stateless
public class JpaInterimRemainRepository extends JpaRepository  implements InterimRemainRepository{
	
	private static final String QUERY_BY_SID_PRIOD = "SELECT c FROM KrcmtInterimRemainMng c"
			+ " WHERE c.sId = :employeeId"
			+ " AND c.ymd >= :startDate"
			+ " AND c.ymd <= :endDate"
			+ " AND c.remainType = :remainType";
	private static final String DELETE_BY_SID_PRIOD_TYPE = "DELETE FROM KrcmtInterimRemainMng c"
			+ " WHERE c.sId = :employeeId"
			+ " AND c.ymd >= :startDate"
			+ " AND c.ymd <= :endDate"
			+ " AND c.remainType = :remainType";
	private static final String DELETE_BY_SID_PRIOD = "DELETE FROM KrcmtInterimRemainMng c"
			+ " WHERE c.sId = :employeeId"
			+ " AND c.ymd >= :startDate"
			+ " AND c.ymd <= :endDate";
	private static final String DELETE_BY_ID = "DELETE FROM KrcmtInterimRemainMng c"
			+ " WHERE c.remainMngId = :remainMngId";
	
	private static final String QUERY_BY_SID_YMD = "SELECT c FROM KrcmtInterimRemainMng c"
			+ " WHERE c.sId = :sId"
			+ " AND c.ymd = :ymd";
	
	@Override
	public List<InterimRemain> getRemainBySidPriod(String employeeId, DatePeriod dateData, RemainType remainType) {
		return this.queryProxy().query(QUERY_BY_SID_PRIOD, KrcmtInterimRemainMng.class)
				.setParameter("employeeId", employeeId)
				.setParameter("startDate", dateData.start())
				.setParameter("endDate", dateData.end())
				.setParameter("remainType", remainType.value)
				.getList(c -> convertToDomainSet(c));
	}
	private InterimRemain convertToDomainSet(KrcmtInterimRemainMng c) {		
		return new InterimRemain(c.remainMngId, 
				c.sId, 
				c.ymd, 
				EnumAdaptor.valueOf(c.createrAtr, CreateAtr.class), 
				EnumAdaptor.valueOf(c.remainType, RemainType.class),
				EnumAdaptor.valueOf(c.remainAtr, RemainAtr.class));
	}
	
	@Override
	public Optional<InterimRemain> getById(String remainId) {
		return this.queryProxy().find(remainId, KrcmtInterimRemainMng.class)
				.map(x -> convertToDomainSet(x));
	}

	@Override
	public void persistAndUpdateInterimRemain(InterimRemain domain) {

		// キー
		val key = domain.getRemainManaID();
		
		// 登録・更新
		KrcmtInterimRemainMng entity = this.getEntityManager().find(KrcmtInterimRemainMng.class, key);
		if (entity == null){
			entity = new KrcmtInterimRemainMng();
			entity.remainMngId = domain.getRemainManaID();
			entity.sId = domain.getSID();
			entity.ymd = domain.getYmd();
			entity.createrAtr = domain.getCreatorAtr().value;
			entity.remainType = domain.getRemainType().value;
			entity.remainAtr = domain.getRemainAtr().value;
			this.getEntityManager().persist(entity);
		}
		else {
			entity.sId = domain.getSID();
			entity.ymd = domain.getYmd();
			entity.createrAtr = domain.getCreatorAtr().value;
			entity.remainType = domain.getRemainType().value;
			entity.remainAtr = domain.getRemainAtr().value;
			this.commandProxy().update(entity);
		}
		//this.getEntityManager().flush();
	}

	@Override
	public void deleteById(String mngId) {
		this.getEntityManager().createQuery(DELETE_BY_ID).setParameter("remainMngId", mngId);
	}

	@Override
	public void deleteBySidPeriodType(String employeeId, DatePeriod dateData, RemainType remainType) {
		this.getEntityManager().createQuery(DELETE_BY_SID_PRIOD_TYPE)
				.setParameter("employeeId", employeeId)
				.setParameter("startDate", dateData.start())
				.setParameter("endDate", dateData.end())
				.setParameter("remainType", remainType.value)
				.executeUpdate();
		
	}
	
	@Override
	public void deleteBySidPeriod(String employeeId, DatePeriod dateData) {
		this.getEntityManager().createQuery(DELETE_BY_SID_PRIOD)
			.setParameter("employeeId", employeeId)
			.setParameter("startDate", dateData.start())
			.setParameter("endDate", dateData.end())
			.executeUpdate();
	}
	@Override
	public List<InterimRemain> getDataBySidDate(String sid, GeneralDate baseDate) {
		return this.queryProxy().query(QUERY_BY_SID_YMD, KrcmtInterimRemainMng.class)
				.setParameter("sId", sid)
				.setParameter("ymd", baseDate)
				.getList(c -> convertToDomainSet(c));
	}
}
