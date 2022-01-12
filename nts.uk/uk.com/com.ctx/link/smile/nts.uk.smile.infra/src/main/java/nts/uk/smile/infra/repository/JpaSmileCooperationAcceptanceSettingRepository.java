package nts.uk.smile.infra.repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import nts.uk.ctx.at.function.dom.processexecution.ExternalAcceptanceConditionCode;
import nts.uk.shr.com.context.AppContexts;
import nts.arc.enums.EnumAdaptor;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.smile.dom.smilelinked.cooperationacceptance.SmileCooperationAcceptanceClassification;
import nts.uk.smile.dom.smilelinked.cooperationacceptance.SmileCooperationAcceptanceItem;
import nts.uk.smile.dom.smilelinked.cooperationacceptance.SmileCooperationAcceptanceSetting;
import nts.uk.smile.dom.smilelinked.cooperationacceptance.SmileCooperationAcceptanceSettingRepository;
import nts.uk.smile.infra.entity.smilelinked.LsmmtSmileCooperationAccepset;
import nts.uk.smile.infra.entity.smilelinked.LsmmtSmileCooperationAccepsetPK;

@Stateless
public class JpaSmileCooperationAcceptanceSettingRepository extends JpaRepository
		implements SmileCooperationAcceptanceSettingRepository {

	private static final String GET_BY_CONTRACT_AND_CID = String.join(" ",
			"SELECT m FROM MimmtSmileCooperationAccepset m WHERE m.pk.contractCd = :contractCd", "AND m.cid = :cid");

	private LsmmtSmileCooperationAccepset toEntity(SmileCooperationAcceptanceSetting domain) {
		LsmmtSmileCooperationAccepset data = new LsmmtSmileCooperationAccepset(
				new LsmmtSmileCooperationAccepsetPK(AppContexts.user().contractCode(), AppContexts.user().companyId(),
						domain.getCooperationAcceptance().value),
				domain.getCooperationAcceptanceClassification() == SmileCooperationAcceptanceClassification.DO,
				domain.getCooperationAcceptanceConditions().isPresent()
						? domain.getCooperationAcceptanceConditions().get().v()
						: null);
		return data;
	}

	private SmileCooperationAcceptanceSetting toDomain(LsmmtSmileCooperationAccepset entity) {
		ExternalAcceptanceConditionCode acceptanceConditionCode = null;
		if (entity.getConditionSetCd() != null) {
			acceptanceConditionCode = new ExternalAcceptanceConditionCode(entity.getConditionSetCd());
		}
		SmileCooperationAcceptanceSetting domain = new SmileCooperationAcceptanceSetting(
				EnumAdaptor.valueOf(entity.getPk().getSmileCooperAccept(), SmileCooperationAcceptanceItem.class),
				entity.getSmileCooperAcceptAtr() ? SmileCooperationAcceptanceClassification.DO
						: SmileCooperationAcceptanceClassification.DO_NOT,
				Optional.ofNullable(acceptanceConditionCode));
		return domain;
	}

	private List<LsmmtSmileCooperationAccepset> toEntities(List<SmileCooperationAcceptanceSetting> domainList) {
		return domainList.stream().map(domain -> {
			return new LsmmtSmileCooperationAccepset(
					new LsmmtSmileCooperationAccepsetPK(AppContexts.user().contractCode(),
							AppContexts.user().companyId(), domain.getCooperationAcceptance().value),
					domain.getCooperationAcceptanceClassification() == SmileCooperationAcceptanceClassification.DO,
					domain.getCooperationAcceptanceConditions().isPresent()
							? domain.getCooperationAcceptanceConditions().get().v()
							: null);
		}).collect(Collectors.toList());
	}

	@Override
	public void insert(SmileCooperationAcceptanceSetting domain) {
		this.commandProxy().insert(this.toEntity(domain));
	}

	@Override
	public void update(SmileCooperationAcceptanceSetting domain) {
		this.commandProxy().update(this.toEntity(domain));
	}

	@Override
	public void delete(String contractCode, String companyId) {
		List<LsmmtSmileCooperationAccepset> list = this.queryProxy()
				.query(GET_BY_CONTRACT_AND_CID, LsmmtSmileCooperationAccepset.class)
				.setParameter("contractCd", contractCode).setParameter("cid", companyId).getList();
		if (!list.isEmpty()) {
			this.commandProxy().removeAll(list);
		}
	}

	@Override
	public void updateAll(List<SmileCooperationAcceptanceSetting> smileCooperationAcceptanceSettings) {
		this.commandProxy().updateAll(this.toEntities(smileCooperationAcceptanceSettings));
	}

	@Override
	public void insertAll(List<SmileCooperationAcceptanceSetting> smileCooperationAcceptanceSettings) {
		this.commandProxy().insertAll(this.toEntities(smileCooperationAcceptanceSettings));
	}

	@Override
	public List<SmileCooperationAcceptanceSetting> get(String contractCode, String companyId) {
		return this.queryProxy().query(GET_BY_CONTRACT_AND_CID, LsmmtSmileCooperationAccepset.class)
				.setParameter("contractCd", contractCode).setParameter("cid", companyId).getList(this::toDomain);
	}
}
