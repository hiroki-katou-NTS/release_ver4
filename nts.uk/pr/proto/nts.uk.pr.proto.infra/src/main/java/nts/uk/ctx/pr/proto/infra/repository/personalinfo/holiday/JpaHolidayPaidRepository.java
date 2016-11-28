package nts.uk.ctx.pr.proto.infra.repository.personalinfo.holiday;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;

import lombok.val;
import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.ListUtil;
import nts.uk.ctx.pr.proto.dom.personalinfo.employmentcontract.PersonalEmploymentContract;
import nts.uk.ctx.pr.proto.dom.personalinfo.holiday.HolidayPaid;
import nts.uk.ctx.pr.proto.dom.personalinfo.holiday.HolidayPaidRepository;
import nts.uk.ctx.pr.proto.infra.entity.personalinfo.employmentcontract.PclmtPersonEmpContract;
import nts.uk.ctx.pr.proto.infra.entity.personalinfo.holiday.PhldtHolidayPaid;
import nts.uk.ctx.pr.proto.infra.entity.personalinfo.holiday.PhldtHolidayPaidPK;

@RequestScoped
public class JpaHolidayPaidRepository extends JpaRepository implements HolidayPaidRepository {

	private final String SELECT_BY_CCD_AND_PID = "SELECT c FROM PhldtHolidayPaid c WHERE c.phldtHolidayPaidPK.ccd = :ccd and c.phldtHolidayPaidPK.pId IN :pIds";
	private final String SEL_1 = "SELECT c FROM PhldtHolidayPaid c WHERE c.phldtHolidayPaidPK.ccd = :ccd and c.phldtHolidayPaidPK.pId = :pId";

	@Override
	public List<HolidayPaid> findAll(String companyCode, List<String> personIds) {
		List<HolidayPaid> results = new ArrayList<>();
		ListUtil.split(personIds, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, personIdList -> {
			this.queryProxy().query(SELECT_BY_CCD_AND_PID, PhldtHolidayPaid.class)
			.setParameter("ccd", companyCode).setParameter("pIds", personIdList)
			.getList().stream().forEach(e -> results.add(toDomain(e)));
		});
		return results;
	}

	private static HolidayPaid toDomain(PhldtHolidayPaid entity) {
		val domain = HolidayPaid.createFromJavaType(entity.remainDays, entity.remainTime,
				entity.phldtHolidayPaidPK.pId.toString());
		//entity.toDomain(domain);
		return domain;
	}

	@Override
	public Optional<HolidayPaid> find(String companyCode, String personId, LocalDate startDate) {
		Optional<HolidayPaid> result = this.queryProxy()
				.find(new PhldtHolidayPaidPK(companyCode, personId, startDate), PhldtHolidayPaid.class)
				.map(c -> toDomain(c));
		
		return result;
	}

	@Override
	public Optional<HolidayPaid> find(String companyCode, String personId) {
		List<HolidayPaid> holidayList = this.queryProxy().query(SEL_1, PhldtHolidayPaid.class)
				.setParameter("ccd", companyCode)
				.setParameter("pId", personId)
				.getList(e -> toDomain(e));
		
		if (holidayList.isEmpty()) {
			return Optional.empty();
		}
		
		return Optional.of(holidayList.get(0));
	}

}
