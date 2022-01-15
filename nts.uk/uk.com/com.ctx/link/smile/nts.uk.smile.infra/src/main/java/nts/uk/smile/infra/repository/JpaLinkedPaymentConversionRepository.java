package nts.uk.smile.infra.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.smile.dom.smilelinked.cooperationoutput.EmploymentAndLinkedMonthSetting;
import nts.uk.smile.dom.smilelinked.cooperationoutput.LinkedMonthSettingClassification;
import nts.uk.smile.dom.smilelinked.cooperationoutput.LinkedPaymentConversion;
import nts.uk.smile.dom.smilelinked.cooperationoutput.LinkedPaymentConversionRepository;
import nts.uk.smile.dom.smilelinked.cooperationoutput.PaymentCategory;
import nts.uk.smile.infra.entity.smilelinked.LsmmtEmplinkMonthSet;
import nts.uk.smile.infra.entity.smilelinked.LsmmtEmplinkMonthSetPK;

@Stateless
public class JpaLinkedPaymentConversionRepository extends JpaRepository implements LinkedPaymentConversionRepository {

	private static final String GET_BY_CONTRACT_AND_CID = String.join(" ",
			"SELECT m FROM LsmmtEmplinkMonthSet m WHERE m.pk.contractCd = :contractCd", "AND m.cid = :cid");

	private static final String GET_BY_CONTRACT_CD_AND_CID_AND_PAYMENT_CD = String.join(" ",
			"SELECT m FROM LsmmtEmplinkMonthSet m WHERE m.pk.contractCd = :contractCd", "AND m.pk.cid = :cid",
			"AND m.pk.paymentCd = :paymentCode");

	private List<LsmmtEmplinkMonthSet> toEntities(LinkedPaymentConversion domain) {
		Integer paymentCode = domain.getPaymentCode().value;
		return domain.getSelectiveEmploymentCodes().stream()
				.map(data -> new LsmmtEmplinkMonthSet(new LsmmtEmplinkMonthSetPK(AppContexts.user().contractCode(),
						AppContexts.user().companyId(), paymentCode, data.getScd()),
						data.getInterlockingMonthAdjustment().value))
				.collect(Collectors.toList());
	}

	private List<LinkedPaymentConversion> toDomain(List<LsmmtEmplinkMonthSet> entity) {
		List<LinkedPaymentConversion> linkedPaymentConversions = new ArrayList<LinkedPaymentConversion>();
		Map<Integer, List<LsmmtEmplinkMonthSet>> map = entity.stream()
				.collect(Collectors.groupingBy(e -> e.getPk().getPaymentCd()));
		if (entity.isEmpty()) {
			return null;
		}
		map.keySet().forEach(k -> {
			List<EmploymentAndLinkedMonthSetting> monthSettings = new ArrayList<EmploymentAndLinkedMonthSetting>();
			map.get(k).forEach(m -> {
				EmploymentAndLinkedMonthSetting employmentAndLinkedMonthSetting = new EmploymentAndLinkedMonthSetting(
						LinkedMonthSettingClassification.valueOf(m.getMiomtEmplinkMonthSet()), m.getPk().getEmpCd());
				monthSettings.add(employmentAndLinkedMonthSetting);
			});
			PaymentCategory paymentCategory = PaymentCategory.valueOf(entity.get(0).getPk().getPaymentCd());
			LinkedPaymentConversion linkedPaymentConversion = new LinkedPaymentConversion(paymentCategory,
					monthSettings);
			linkedPaymentConversions.add(linkedPaymentConversion);
		});
		return linkedPaymentConversions;
	}

	@Override
	public void insert(LinkedPaymentConversion domain) {
		this.commandProxy().insertAll(this.toEntities(domain));
	}

	@Override
	public void update(LinkedPaymentConversion domain) {
		List<LsmmtEmplinkMonthSet> list = this.queryProxy().query(GET_BY_CONTRACT_AND_CID, LsmmtEmplinkMonthSet.class)
				.setParameter("contractCd", AppContexts.user().contractCode())
				.setParameter("cid", AppContexts.user().companyId()).setParameter("paymentCd", domain.getPaymentCode())
				.getList();
		if (list.isEmpty()) {
			return;
		}
		// Before update => delete all
		this.commandProxy().removeAll(list);
		// Insert new entity
		this.commandProxy().insertAll(this.toEntities(domain));
	}

	@Override
	public void delete(String contractCode, String companyId) {
		List<LsmmtEmplinkMonthSet> list = this.queryProxy().query(GET_BY_CONTRACT_AND_CID, LsmmtEmplinkMonthSet.class)
				.setParameter("contractCd", contractCode).setParameter("cid", companyId).getList();
		if (list.isEmpty()) {
			return;
		}
		this.commandProxy().removeAll(list);
	}

	@Override
	public void delete(String contractCode, String companyId, PaymentCategory paymentCode) {
		List<LsmmtEmplinkMonthSet> list = this.queryProxy()
				.query(GET_BY_CONTRACT_CD_AND_CID_AND_PAYMENT_CD, LsmmtEmplinkMonthSet.class)
				.setParameter("contractCd", contractCode).setParameter("cid", companyId)
				.setParameter("paymentCd", paymentCode).getList();
		if (list.isEmpty()) {
			return;
		}
		this.commandProxy().removeAll(list);
	}

	@Override
	public List<EmploymentAndLinkedMonthSetting> getByPaymentCode(String contractCode, String companyId,
			PaymentCategory paymentCode) {
		return this.queryProxy().query(GET_BY_CONTRACT_CD_AND_CID_AND_PAYMENT_CD, LsmmtEmplinkMonthSet.class)
				.setParameter("contractCd", contractCode).setParameter("cid", companyId)
				.setParameter("paymentCd", paymentCode).getList(e -> new EmploymentAndLinkedMonthSetting(
						LinkedMonthSettingClassification.valueOf(e.getMiomtEmplinkMonthSet()), e.getPk().getEmpCd()));
	}

	@Override
	public List<EmploymentAndLinkedMonthSetting> get(String contractCode, String companyId) {
		return this.queryProxy().query(GET_BY_CONTRACT_AND_CID, LsmmtEmplinkMonthSet.class)
				.setParameter("contractCd", contractCode).setParameter("cid", companyId)
				.getList(e -> new EmploymentAndLinkedMonthSetting(
						LinkedMonthSettingClassification.valueOf(e.getMiomtEmplinkMonthSet()), e.getPk().getEmpCd()));
	}

}
