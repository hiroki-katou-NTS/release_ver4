package nts.uk.ctx.at.shared.infra.repository.remainingnumber.annualleave;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import lombok.SneakyThrows;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsResultSet.NtsResultRecord;
//import nts.arc.layer.infra.data.jdbc.NtsStatement;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualHolidayMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.interim.TmpAnnualHolidayMngRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UseDay;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.annlea.KrcdtInterimHdpaid;
import nts.arc.time.calendar.period.DatePeriod;

@Stateless
public class JpaTmpAnnualHolidayMngRepository extends JpaRepository implements TmpAnnualHolidayMngRepository{
	@Inject
	private InterimRemainRepository interRemain;
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Optional<TmpAnnualHolidayMng> getById(String mngId) {
		Optional<TmpAnnualHolidayMng> optTmpAnnualHolidayMng = this.queryProxy().find(mngId, KrcdtInterimHdpaid.class)
				.map(x -> toDomain(x));
		return optTmpAnnualHolidayMng;
	}

	private TmpAnnualHolidayMng toDomain(KrcdtInterimHdpaid x) {
		return new TmpAnnualHolidayMng(x.annualMngId, x.workTypeCode, new UseDay(x.useDays));
	}

	@Override
	public void deleteById(String mngId) {
		Optional<KrcdtInterimHdpaid> optTmpAnnualHolidayMng = this.queryProxy().find(mngId, KrcdtInterimHdpaid.class);
		optTmpAnnualHolidayMng.ifPresent(x -> {
			this.commandProxy().remove(x);
		});

	}

	@Override
	public void persistAndUpdate(TmpAnnualHolidayMng dataMng) {
		Optional<KrcdtInterimHdpaid> optTmpAnnualHolidayMng = this.queryProxy().find(dataMng.getAnnualId(), KrcdtInterimHdpaid.class);
		if(optTmpAnnualHolidayMng.isPresent()) {
			KrcdtInterimHdpaid entity = optTmpAnnualHolidayMng.get();
			entity.useDays = dataMng.getUseDays().v();
			entity.workTypeCode = dataMng.getWorkTypeCode();
			this.commandProxy().update(entity);
		} else {
			KrcdtInterimHdpaid entity = new KrcdtInterimHdpaid();
			entity.annualMngId = dataMng.getAnnualId();
			entity.useDays = dataMng.getUseDays().v();
			entity.workTypeCode = dataMng.getWorkTypeCode();
			this.getEntityManager().persist(entity);
		}
		this.getEntityManager().flush();
	}
	@SneakyThrows
	@Override
	public List<TmpAnnualHolidayMng> getBySidPeriod(String sid, DatePeriod period) {
		try(PreparedStatement sql = this.connection().prepareStatement("SELECT * FROM KRCMT_INTERIM_ANNUAL_MNG a1"
				+ " INNER JOIN KRCMT_INTERIM_REMAIN_MNG a2 ON a1.ANNUAL_MNG_ID = a2.REMAIN_MNG_ID"
				+ " WHERE a2.SID = ?"
				+ " AND  a2.REMAIN_TYPE = 0"
				+ " AND a2.YMD >= ? and a2.YMD <= ?"
				+ " ORDER BY a2.YMD");
		)
		{
			sql.setString(1, sid);
			sql.setDate(2, Date.valueOf(period.start().localDate()));
			sql.setDate(3, Date.valueOf(period.end().localDate()));
			List<TmpAnnualHolidayMng> lstOutput = new NtsResultSet(sql.executeQuery())
					.getList(x -> toDomain(x));
			return lstOutput;
		}
	}

	private TmpAnnualHolidayMng toDomain(NtsResultRecord x) {
		return new TmpAnnualHolidayMng(x.getString("ANNUAL_MNG_ID"),
				x.getString("WORKTYPE_CODE"),
				new UseDay(x.getBigDecimal("USE_DAYS") == null ? 0 : x.getBigDecimal("USE_DAYS").doubleValue()));
	}

	@Override
	public void deleteSidPeriod(String sid, DatePeriod period) {
		List<TmpAnnualHolidayMng> lstAnn = this.getBySidPeriod(sid, period);
		lstAnn.stream().forEach(x -> {
			//暫定残数管理データ
			interRemain.deleteById(x.getAnnualId());
			//暫定年休管理データ
			this.deleteById(x.getAnnualId());
		});
	}

}
