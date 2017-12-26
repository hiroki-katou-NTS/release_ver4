package nts.uk.ctx.at.function.infra.entity.alarm.checkcondition;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckConditionByCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.AlarmCheckTargetCondition;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * 
 * @author HungTT
 *
 */

@NoArgsConstructor
@Entity
@Table(name = "KFNMT_AL_CHECK_COND_CATE")
public class KfnmtAlarmCheckConditionCategory extends UkJpaEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KfnmtAlarmCheckConditionCategoryPk pk;

	@Basic
	@Column(name = "NAME")
	public String name;

	@Basic
	@Column(name = "EXTRACT_TARGET_COND_ID")
	public String targetConditionId;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "EXTRACT_TARGET_COND_ID", insertable=false, updatable=false)
	public KfnmtAlarmCheckTargetCondition targetCondition;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "condition", orphanRemoval = true)
	public List<KfnmtAlarmCheckConditionCategoryRole> listAvailableRole;

	@Override
	protected Object getKey() {
		return this.pk;
	}

	public KfnmtAlarmCheckConditionCategory(String companyId, int category, String code, String name,
			KfnmtAlarmCheckTargetCondition targetCondition,
			List<KfnmtAlarmCheckConditionCategoryRole> listAvailableRole) {
		super();
		this.pk = new KfnmtAlarmCheckConditionCategoryPk(companyId, category, code);
		this.name = name;
		this.targetConditionId = targetCondition.id;
		this.targetCondition = targetCondition;
		this.listAvailableRole = listAvailableRole;
	}

	public static AlarmCheckConditionByCategory toDomain(KfnmtAlarmCheckConditionCategory entity) {
		return new AlarmCheckConditionByCategory(entity.pk.companyId, entity.pk.category, entity.pk.code, entity.name,
				new AlarmCheckTargetCondition(entity.targetConditionId,
						entity.targetCondition.filterByBusinessType == 1 ? true : false,
						entity.targetCondition.filterByJobTitle == 1 ? true : false,
						entity.targetCondition.filterByEmployment == 1 ? true : false,
						entity.targetCondition.filterByClassification == 1 ? true : false,
						entity.targetCondition.listBusinessType.stream().map(item -> item.pk.businessTypeCode)
								.collect(Collectors.toList()),
						entity.targetCondition.listJobTitle.stream().map(item -> item.pk.jobTitleId)
								.collect(Collectors.toList()),
						entity.targetCondition.listEmployment.stream().map(item -> item.pk.employmentCode)
								.collect(Collectors.toList()),
						entity.targetCondition.listClassification.stream().map(item -> item.pk.classificationCode)
								.collect(Collectors.toList())),
				entity.listAvailableRole.stream().map(item -> item.pk.roleId).collect(Collectors.toList()), null);
	}

	public static KfnmtAlarmCheckConditionCategory fromDomain(AlarmCheckConditionByCategory domain) {
		return new KfnmtAlarmCheckConditionCategory(domain.getCompanyId(), domain.getCategory().value,
				domain.getCode().v(), domain.getName().v(),
				new KfnmtAlarmCheckTargetCondition(domain.getExtractTargetCondition().getId(),
						domain.getExtractTargetCondition().isFilterByEmployment() ? 1 : 0,
						domain.getExtractTargetCondition().isFilterByClassification() ? 1 : 0,
						domain.getExtractTargetCondition().isFilterByJobTitle() ? 1 : 0,
						domain.getExtractTargetCondition().isFilterByBusinessType() ? 1 : 0,
						domain.getExtractTargetCondition().getLstEmploymentCode().stream()
								.map(item -> new KfnmtAlarmCheckTargetEmployment(
										domain.getExtractTargetCondition().getId(), item))
								.collect(Collectors.toList()),
						domain.getExtractTargetCondition().getLstClassificationCode().stream()
								.map(item -> new KfnmtAlarmCheckTargetClassification(
										domain.getExtractTargetCondition().getId(), item))
								.collect(Collectors.toList()),
						domain.getExtractTargetCondition().getLstJobTitleId().stream()
								.map(item -> new KfnmtAlarmCheckTargetJobTitle(
										domain.getExtractTargetCondition().getId(), item))
								.collect(Collectors.toList()),
						domain.getExtractTargetCondition().getLstBusinessTypeCode().stream()
								.map(item -> new KfnmtAlarmCheckTargetBusinessType(
										domain.getExtractTargetCondition().getId(), item))
								.collect(Collectors.toList())),
				domain.getListRoleId().stream()
						.map(item -> new KfnmtAlarmCheckConditionCategoryRole(domain.getCompanyId(),
								domain.getCategory().value, domain.getCode().v(), item))
						.collect(Collectors.toList()));
	}

}
