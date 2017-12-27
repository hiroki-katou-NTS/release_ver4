package nts.uk.ctx.at.function.infra.repository.alarm.checkcondition;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.transaction.Transactional;

import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategoryRepository;
import nts.uk.ctx.at.function.infra.entity.alarm.checkcondition.KfnmtAlarmCheckConditionCategory;
import nts.uk.ctx.at.function.infra.entity.alarm.checkcondition.KfnmtAlarmCheckConditionCategoryPk;
import nts.uk.ctx.at.function.infra.entity.alarm.checkcondition.KfnmtAlarmCheckConditionCategoryRole;

/**
 * 
 * @author HungTT
 *
 */

@Stateless
@Transactional
public class JpaAlarmCheckConditionByCategoryRepository extends JpaRepository
		implements AlarmCheckConditionByCategoryRepository {

	private final String GET_All_BY_COMPANY = "SELECT c FROM KfnmtAlarmCheckConditionCategory c WHERE c.companyId = :companyId ";
	private final String GET_All_BY_COMPANY_CATEGORY = "SELECT c FROM KfnmtAlarmCheckConditionCategory c WHERE c.companyId = :companyId AND c.category = :category ";

	@Override
	public Optional<AlarmCheckConditionByCategory> find(String companyId, int category, String code) {
		Optional<KfnmtAlarmCheckConditionCategory> entity = this.queryProxy().find(
				new KfnmtAlarmCheckConditionCategoryPk(companyId, category, code),
				KfnmtAlarmCheckConditionCategory.class);
		if (entity.isPresent()) {
			return Optional.of(KfnmtAlarmCheckConditionCategory.toDomain(entity.get()));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public List<AlarmCheckConditionByCategory> findAll(String companyId) {
		return this.queryProxy().query(GET_All_BY_COMPANY, KfnmtAlarmCheckConditionCategory.class)
				.setParameter("companyId", companyId).getList(c -> KfnmtAlarmCheckConditionCategory.toDomain(c));
	}

	@Override
	public List<AlarmCheckConditionByCategory> findByCategory(String companyId, int category) {
		return this.queryProxy().query(GET_All_BY_COMPANY_CATEGORY, KfnmtAlarmCheckConditionCategory.class)
				.setParameter("companyId", companyId).setParameter("category", category)
				.getList(c -> KfnmtAlarmCheckConditionCategory.toDomain(c));
	}

	@Override
	public void add(AlarmCheckConditionByCategory domain) {
		this.commandProxy().insert(KfnmtAlarmCheckConditionCategory.fromDomain(domain));
	}

	@Override
	public void update(AlarmCheckConditionByCategory domain) {
		Optional<KfnmtAlarmCheckConditionCategory> entityOpt = this.queryProxy()
				.find(new KfnmtAlarmCheckConditionCategoryPk(domain.getCompanyId(), domain.getCategory().value,
						domain.getCode().v()), KfnmtAlarmCheckConditionCategory.class);
		if (entityOpt.isPresent()) {
			KfnmtAlarmCheckConditionCategory entity = entityOpt.get();
			entity.name = domain.getName().v();
			List<KfnmtAlarmCheckConditionCategoryRole> oldListRole = entity.listAvailableRole;
			List<KfnmtAlarmCheckConditionCategoryRole> newListRole = domain.getListRoleId().stream()
					.map(item -> new KfnmtAlarmCheckConditionCategoryRole(domain.getCompanyId(),
							domain.getCategory().value, domain.getCode().v(), item))
					.collect(Collectors.toList());
			for (KfnmtAlarmCheckConditionCategoryRole newRole : newListRole) {
				for (KfnmtAlarmCheckConditionCategoryRole oldRole : oldListRole) {
					if (oldRole.pk.equals(newRole.pk)) {
						newListRole.set(newListRole.indexOf(newRole), oldRole);
						break;
					}
				}
			}
			entity.listAvailableRole = newListRole;

			entity.targetCondition.filterByBusinessType = domain.getExtractTargetCondition().isFilterByBusinessType()
					? 1 : 0;
			entity.targetCondition.filterByClassification = domain.getExtractTargetCondition()
					.isFilterByClassification() ? 1 : 0;
			entity.targetCondition.filterByEmployment = domain.getExtractTargetCondition().isFilterByEmployment() ? 1
					: 0;
			entity.targetCondition.filterByJobTitle = domain.getExtractTargetCondition().isFilterByJobTitle() ? 1 : 0;

			this.commandProxy().update(entity);
		}

	}

	// When alarm check condition by category is deleted
	// Delete the "time item check of work record (勤務実績の勤怠項目チェック)"
	// and "error item condition of time item (勤怠項目のエラーアラーム条件)" (da co trong workrecord/erroralarm/condition/attendanceitem)
	// linked to error work alarm check ID of work record
	@Override
	public void delete(AlarmCheckConditionByCategory domain) {
		this.commandProxy().remove(KfnmtAlarmCheckConditionCategory.class, new KfnmtAlarmCheckConditionCategoryPk(
				domain.getCompanyId(), domain.getCategory().value, domain.getCode().v()));
	}

	@Override
	public boolean isCodeExist(String companyId, int category, String code) {
		return this.queryProxy().find(new KfnmtAlarmCheckConditionCategoryPk(companyId, category, code),
				KfnmtAlarmCheckConditionCategory.class).isPresent();
	}

}
