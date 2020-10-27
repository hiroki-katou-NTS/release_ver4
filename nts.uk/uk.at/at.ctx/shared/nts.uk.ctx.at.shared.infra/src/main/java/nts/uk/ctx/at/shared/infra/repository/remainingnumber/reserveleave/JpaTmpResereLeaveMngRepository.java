package nts.uk.ctx.at.shared.infra.repository.remainingnumber.reserveleave;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import lombok.SneakyThrows;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.layer.infra.data.jdbc.NtsResultSet;
import nts.arc.layer.infra.data.jdbc.NtsResultSet.NtsResultRecord;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.InterimRemainRepository;
import nts.uk.ctx.at.shared.dom.remainingnumber.interimremain.primitive.UseDay;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.interim.TmpResereLeaveMng;
import nts.uk.ctx.at.shared.dom.remainingnumber.reserveleave.interim.TmpResereLeaveMngRepository;
import nts.uk.ctx.at.shared.infra.entity.remainingnumber.reserveleave.KrcdtHdstkTemp;
import nts.arc.time.calendar.period.DatePeriod;
@Stateless
public class JpaTmpResereLeaveMngRepository extends JpaRepository implements TmpResereLeaveMngRepository{
	@Inject
	private InterimRemainRepository interRemain;
	@Override
	public Optional<TmpResereLeaveMng> getById(String resereMngId) {
		Optional<TmpResereLeaveMng> optKrcdtHdstkTemp = this.queryProxy().find(resereMngId, KrcdtHdstkTemp.class)
				.map(x -> toDomain(x));
		return optKrcdtHdstkTemp;
	}

	private TmpResereLeaveMng toDomain(KrcdtHdstkTemp x) {
		return new TmpResereLeaveMng(x.reserveMngId, new UseDay(x.useDays));
	}

	@Override
	public void deleteById(String resereMngId) {
		Optional<KrcdtHdstkTemp> optKrcdtHdstkTemp = this.queryProxy().find(resereMngId, KrcdtHdstkTemp.class);
		optKrcdtHdstkTemp.ifPresent(x -> {
			this.commandProxy().remove(x);
		});
	}

	@Override
	public void persistAndUpdate(TmpResereLeaveMng dataMng) {
		Optional<KrcdtHdstkTemp> optKrcdtHdstkTemp = this.queryProxy().find(dataMng.getResereId(), KrcdtHdstkTemp.class);
		if(optKrcdtHdstkTemp.isPresent()) {
			KrcdtHdstkTemp entity = optKrcdtHdstkTemp.get();
			entity.useDays = dataMng.getUseDays().v();
			this.commandProxy().update(entity);
		} else {
			KrcdtHdstkTemp entity = new KrcdtHdstkTemp();
			entity.reserveMngId = dataMng.getResereId();
			entity.useDays = dataMng.getUseDays().v();
			this.getEntityManager().persist(entity);
		}
		this.getEntityManager().flush();
	}
	@SneakyThrows
	@Override
	public List<TmpResereLeaveMng> findBySidPriod(String sid, DatePeriod period) {
		try(PreparedStatement sql = this.connection().prepareStatement("SELECT * FROM KRCDT_HDSTK_TEMP a1"
				+ " INNER JOIN KRCDT_INTERIM_REMAIN_MNG a2 ON a1.RESERVE_MNG_ID = a2.REMAIN_MNG_ID"
				+ " WHERE a2.SID = ?"
				+ " AND  a2.REMAIN_TYPE = 1"
				+ " AND a2.YMD >= ? and a2.YMD <= ?"
				+ " ORDER BY a2.YMD");
		)
		{
			sql.setString(1, sid);
			sql.setDate(2, Date.valueOf(period.start().localDate()));
			sql.setDate(3, Date.valueOf(period.end().localDate()));
			List<TmpResereLeaveMng> lstOutput = new NtsResultSet(sql.executeQuery())
					.getList(x -> toDomainNts(x));
			return lstOutput;
		}
	}
	private TmpResereLeaveMng toDomainNts(NtsResultRecord x) {		
		return new TmpResereLeaveMng(x.getString("RESERVE_MNG_ID"),
				new UseDay(x.getBigDecimal("USE_DAYS") == null ? 0 : x.getBigDecimal("USE_DAYS").doubleValue()));
	}

	@Override
	public void deleteSidPriod(String sid, DatePeriod period) {
		List<TmpResereLeaveMng> listRese = this.findBySidPriod(sid, period);
		listRese.stream().forEach(x -> {
			this.deleteById(x.getResereId());
			interRemain.deleteById(x.getResereId());
		});
	}

}
