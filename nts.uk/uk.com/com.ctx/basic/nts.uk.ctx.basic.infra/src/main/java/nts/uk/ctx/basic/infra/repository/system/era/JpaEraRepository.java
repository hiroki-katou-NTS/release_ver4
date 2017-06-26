package nts.uk.ctx.basic.infra.repository.system.era;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.basic.dom.system.era.Era;
import nts.uk.ctx.basic.dom.system.era.EraRepository;
import nts.uk.ctx.basic.infra.entity.system.era.CmnmtEra;
import nts.uk.ctx.basic.infra.entity.system.era.CmnmtEraPk;
import nts.uk.shr.com.time.japanese.JapaneseEraName;
import nts.uk.shr.com.time.japanese.JapaneseEras;
import nts.uk.shr.com.time.japanese.JapaneseErasAdapter;

@Stateless
public class JpaEraRepository extends JpaRepository implements EraRepository, JapaneseErasAdapter {
	private static final String SEL_1 = "SELECT e FROM CmnmtEra e";
	private static final String SEL_ALL_LIST_ERA = "SELECT e FROM CmnmtEra e ORDER BY e.startDate ";
	private static final String SEL_LIST_ERA = "SELECT e.era_Name, e.era_Mark, e.startDate, e.end_D FROM CmnmtEra e ORDER BY e.startDate ";
	private static final String SEL_LATEST_ERA = SEL_1 + " WHERE e.end_D = :endDate";
	private static final String SEL_STARTDATE_ERAMASTER = SEL_1 + " WHERE e.startDate > :startDate";
	private static final String SEL_UPD1 = SEL_1 + " WHERE e.cmnmtEraPk.hist_Id = :eraHist";
	private static final String SEL_MAX_STARTDATE = "SELECT e FROM CmnmtEra e ORDER BY e.startDate DESC";

	// private static final String SEL_3 = ""
	private final Era toDomain(CmnmtEra entity) {
		val domain = Era.createFromDataType(entity.era_Name, entity.cmnmtEraPk.hist_Id, entity.era_Mark,
				entity.startDate, entity.end_D, entity.fix_Atr);
		return domain;
	}

	private static CmnmtEra toEntity(Era domain) {
		CmnmtEra entity = new CmnmtEra();
		entity.era_Name = domain.getEraName().v();
		entity.era_Mark = domain.getEraMark().v();
		// entity.cmnmtEraPk = new CmnmtEra(domain.getStartDate());
		// GeneralDate.(entity.cmnmtEraPk.str_D);
		entity.cmnmtEraPk = new CmnmtEraPk(domain.getEraHist());
		entity.startDate = domain.getStartDate();
		entity.end_D = domain.getEndDate();
		entity.fix_Atr = domain.getFixAttribute().value;
		return entity;
	}

	@Override
	public List<Era> getEras() {
		return this.queryProxy().query(SEL_ALL_LIST_ERA, CmnmtEra.class).getList(c -> toDomain(c));
	}

	// @Override
	// public Optional<Era> getEraDetail(GeneralDate startDate) {
	// // TODO Auto-generated method stub
	// return this.queryProxy().find(new CmnmtEraPk(startDate.localDate()),
	// CmnmtEra.class).map(c -> toDomain(c));
	// }
	@Override
	public void add(Era era) {
		this.commandProxy().insert(toEntity(era));
	}

	@Override
	public void update(Era era) {
		this.commandProxy().update(toEntity(era));
	}

	@Override
	public void delete(String eraHist) {
		val objectKey = new CmnmtEraPk(eraHist);
		this.commandProxy().remove(CmnmtEra.class, objectKey);
	}

	@Override
	public Optional<Era> getEraDetail(String eraHist) {
		return this.queryProxy().find(new CmnmtEraPk(eraHist), CmnmtEra.class).map(c -> toDomain(c));
	}

	@Override
	public Optional<Era> getLatestEra() {
		return this.queryProxy().query(SEL_LATEST_ERA, CmnmtEra.class)
				.setParameter("endDate", GeneralDate.ymd(9999, 12, 31)).getSingle(s -> toDomain(s));
	}

	@Override
	public List<Era> getStartDateEraMaster(GeneralDate startDate) {
		return this.queryProxy().query(SEL_STARTDATE_ERAMASTER, CmnmtEra.class).setParameter("startDate", startDate)
				.getList(s -> toDomain(s));
	}

	@Override
	public Optional<Era> getHistIdUpdate(String eraHist) {

		return this.queryProxy().query(SEL_UPD1, CmnmtEra.class).setParameter("eraHist", eraHist)
				.getSingle(s -> toDomain(s));
	}

	@Override
	public Optional<Era> getEndDateBefore(GeneralDate endDate) {
		return this.queryProxy().query(SEL_LATEST_ERA, CmnmtEra.class).setParameter("endDate", endDate)
				.getSingle(c -> toDomain(c));
	}

	@Override
	public Optional<Era> getCurrentEndDate(GeneralDate endDate) {
		return this.queryProxy().query(SEL_LATEST_ERA, CmnmtEra.class).setParameter("endDate", endDate)
				.getSingle(s -> toDomain(s));
	}

	@Override
	public GeneralDate checkStartDate() {
		Era entity = this.queryProxy().query(SEL_MAX_STARTDATE, CmnmtEra.class).getList(m -> toDomain(m)).get(0);
		if (entity == null)
			return GeneralDate.min();
		else
			return entity.getStartDate();
	}

	@Override
	public JapaneseEras getAllEras() {
		return new JapaneseEras(this.queryProxy().query(SEL_LIST_ERA, Object[].class).getList(e -> {
			return new JapaneseEraName(e[0].toString(), e[1].toString(), (GeneralDate) e[2], (GeneralDate) e[3]);
		}));
	}

}
