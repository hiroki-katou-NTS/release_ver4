package nts.uk.ctx.pr.core.infra.repository.allot;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.allot.PersonalAllotSetting;
import nts.uk.ctx.pr.core.dom.allot.PersonalAllotSettingRepository;
import nts.uk.ctx.pr.core.infra.entity.paymentdata.QstmtStmtAllotPs;

@Stateless
public class JpaPersonalAllotSettingRepository extends JpaRepository implements PersonalAllotSettingRepository {

	private final String SEL_1 = "SELECT c FROM QstmtStmtAllotPs c WHERE c.qstmtStmtAllotPsPK.companyCode = :ccd "
			+ "AND c.qstmtStmtAllotPsPK.personId = :pid and c.startDate <= :baseYM and c.endDate >= :baseYM";

	@Override
	public Optional<PersonalAllotSetting> find(String companyCode, String personId, int baseYM) {

		List<QstmtStmtAllotPs> psettings = this.queryProxy().query(SEL_1, QstmtStmtAllotPs.class)
				.setParameter("ccd", companyCode).setParameter("pid", personId).setParameter("baseYM", baseYM)
				.getList();
		if (psettings.isEmpty()) {
			return Optional.empty();
		}

		return Optional.of(toDomain(psettings.get(0)));
	}

	private static PersonalAllotSetting toDomain(QstmtStmtAllotPs entity) {
		val domain = PersonalAllotSetting.createFromJavaType(entity.qstmtStmtAllotPsPK.companyCode,
				entity.qstmtStmtAllotPsPK.personId, entity.startDate, entity.endDate,
				entity.bonusDetailCode, entity.paymentDetailCode);

		return domain;
	}

}