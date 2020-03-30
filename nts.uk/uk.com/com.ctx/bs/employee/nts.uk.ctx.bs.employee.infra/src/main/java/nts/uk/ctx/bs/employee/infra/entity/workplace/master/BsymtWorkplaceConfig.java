//package nts.uk.ctx.bs.employee.infra.entity.workplace.master;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import javax.persistence.Column;
//import javax.persistence.EmbeddedId;
//import javax.persistence.Entity;
//import javax.persistence.Table;
//
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import nts.arc.time.GeneralDate;
//import nts.uk.ctx.bs.employee.dom.workplace.master.WorkplaceConfiguration;
//import nts.uk.shr.com.history.DateHistoryItem;
//import nts.uk.shr.com.time.calendar.period.DatePeriod;
//import nts.uk.shr.infra.data.entity.UkJpaEntity;
//
///**
// * 
// * @author HungTT - 職場構成
// *
// */
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "BSYMT_WKP_CONFIG_2")
//public class BsymtWorkplaceConfig extends UkJpaEntity {
//
//	@EmbeddedId
//	public BsymtWorkplaceConfigPk pk;
//
//	@Column(name = "START_DATE")
//	public GeneralDate startDate;
//
//	@Column(name = "END_DATE")
//	public GeneralDate endDate;
//
//	@Override
//	protected Object getKey() {
//		return this.pk;
//	}
//
//	public static WorkplaceConfiguration toDomain(List<BsymtWorkplaceConfig> listEntities) {
//		if (listEntities.isEmpty())
//			return null;
//		listEntities.sort((e1, e2) -> e2.startDate.compareTo(e1.startDate));
//		List<DateHistoryItem> listWorkplaceHistories = listEntities.stream()
//				.map(e -> new DateHistoryItem(e.pk.workplaceHistoryId, new DatePeriod(e.startDate, e.endDate)))
//				.collect(Collectors.toList());
//		return new WorkplaceConfiguration(listEntities.get(0).pk.companyId, listWorkplaceHistories);
//	}
//
//	public static List<BsymtWorkplaceConfig> fromDomain(WorkplaceConfiguration domain) {
//		if (domain.items().isEmpty())
//			return Collections.emptyList();
//		return domain.items().stream()
//				.map(h -> new BsymtWorkplaceConfig(new BsymtWorkplaceConfigPk(domain.getCompanyId(), h.identifier()),
//						h.start(), h.end()))
//				.collect(Collectors.toList());
//	}
//
//}
