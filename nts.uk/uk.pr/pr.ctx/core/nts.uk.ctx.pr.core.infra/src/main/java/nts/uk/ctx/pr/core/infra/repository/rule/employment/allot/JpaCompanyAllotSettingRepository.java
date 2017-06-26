package nts.uk.ctx.pr.core.infra.repository.rule.employment.allot;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.CompanyAllotSetting;
import nts.uk.ctx.pr.core.dom.rule.employment.layout.allot.CompanyAllotSettingRepository;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.allot.QstmtStmtAllotCp;
import nts.uk.ctx.pr.core.infra.entity.rule.employment.allot.QstmtStmtAllotCpPK;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class JpaCompanyAllotSettingRepository extends JpaRepository implements CompanyAllotSettingRepository {

	private final String SEL_1 = "SELECT c FROM QstmtStmtAllotCp c"
			+ " WHERE c.qstmtStmtAllotCpPK.companyCode = :companyCode "
			// + " c.startDate <= :BASEYM"
			// + " AND c.endDate >= :BASEYM"
			+ " AND ( c.paymentDetailCode != '00'" + " OR c.bonusDetailCode != '00')" + " ORDER BY c.startDate DESC";
	private final String SEL_9 = "SELECT c FROM QstmtStmtAllotCp c "
			+ " WHERE c.qstmtStmtAllotCpPK.companyCode = :companyCode "
			+ " AND c.endDate = (SELECT MAX(d.endDate) FROM QstmtStmtAllotCp d)";

	private final String SEL_MAX = "SELECT c FROM QstmtStmtAllotCp c"
			+ " WHERE c.qstmtStmtAllotCpPK.companyCode = :companyCode " 
			+ " AND c.endDate = :endDate";

//	private final String SEL_PREVIOUS_HIST = "SELECT c FROM QstmtStmtAllotCp c"
//			+ " WHERE c.qstmtStmtAllotCpPK.companyCode = :companyCode  " 
//			+ " AND c.endDate = :endDate";

	@Override
	public Optional<CompanyAllotSetting> find(String companyCode) {
		List<QstmtStmtAllotCp> psettings = this.queryProxy().query(SEL_1, QstmtStmtAllotCp.class)
				.setParameter("companyCode", companyCode).getList();
		if (psettings.isEmpty()) {
			return Optional.empty();
		}
		return Optional.of(toDomain(psettings.get(0)));
	}

	@Override
	public List<CompanyAllotSetting> findAll(String companyCode) {
		return this.queryProxy().query(SEL_1, QstmtStmtAllotCp.class)
				.setParameter("companyCode", companyCode)
				.getList(c -> toDomain(c));
	}

	// find max date
	@Override
	public Optional<CompanyAllotSetting> findMax(String companyCode, int endDate) {
		return this.queryProxy().query(SEL_MAX, QstmtStmtAllotCp.class)
				.setParameter("companyCode", companyCode)
				.setParameter("endDate", endDate)
				.getSingle(c -> toDomain(c));
	}

	// Get max start
	@Override
	public Optional<CompanyAllotSetting> maxStart(String companyCode) {
		return this.queryProxy().query(SEL_9, QstmtStmtAllotCp.class).setParameter("companyCode", companyCode)
				.getSingle(c -> toDomain(c));
	}

	private CompanyAllotSetting toDomain(QstmtStmtAllotCp entity) {
		val domain = CompanyAllotSetting.createFromJavaType(entity.qstmtStmtAllotCpPK.companyCode,
				entity.qstmtStmtAllotCpPK.histId, entity.startDate, entity.endDate, entity.paymentDetailCode,
				entity.bonusDetailCode);
		return domain;
	}

	private QstmtStmtAllotCp toEntity(CompanyAllotSetting companyAllotSetting) {
		// TODO Auto-generated method stub
		return new QstmtStmtAllotCp(
				new QstmtStmtAllotCpPK(AppContexts.user().companyCode(), companyAllotSetting.getHistoryId()),
				companyAllotSetting.getStartDate().v(), companyAllotSetting.getEndDate().v(),
				companyAllotSetting.getPaymentDetailCode().v(), companyAllotSetting.getBonusDetailCode().v());
	}

	@Override
	public void update(CompanyAllotSetting companyAllotSetting) {
		try {
			this.commandProxy().update(toEntity(companyAllotSetting));
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	public void insert(CompanyAllotSetting companyAllotSetting) {
		try {
			this.commandProxy().insert(toEntity(companyAllotSetting));
		} catch (Exception ex) {
			throw ex;
		}
	}

	@Override
	public void remove(String historyId) {
		val objectKey = new QstmtStmtAllotCpPK();
		objectKey.companyCode = AppContexts.user().companyCode();
		objectKey.histId = historyId;
		this.commandProxy().remove(QstmtStmtAllotCp.class, objectKey);
	}

	/**
	 * get previous History to Update process
	 */
	@Override
	public Optional<CompanyAllotSetting> getPreviousHistory(String companyCode, int endDate) {
		return this.queryProxy().query(SEL_MAX, QstmtStmtAllotCp.class)
				.setParameter("companyCode", companyCode)
				.setParameter("endDate", endDate)
				.getSingle(c -> toDomain(c));
	}

}
