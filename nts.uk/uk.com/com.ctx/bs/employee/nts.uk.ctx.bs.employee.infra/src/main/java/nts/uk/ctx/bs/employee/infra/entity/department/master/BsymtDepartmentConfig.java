//package nts.uk.ctx.bs.employee.infra.entity.department.master;
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
//import nts.uk.ctx.bs.employee.dom.department.master.DepartmentConfiguration;
//import nts.uk.shr.com.history.DateHistoryItem;
//import nts.uk.shr.com.time.calendar.period.DatePeriod;
//import nts.uk.shr.infra.data.entity.UkJpaEntity;
//
///**
// * 
// * @author HungTT - 部門構成
// *
// */
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Table(name = "BSYMT_DEP_CONFIG")
//public class BsymtDepartmentConfig extends UkJpaEntity {
//
//	@EmbeddedId
//	public BsymtDepartmentConfigPk pk;
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
//	public static DepartmentConfiguration toDomain(List<BsymtDepartmentConfig> listEntities) {
//		if (listEntities.isEmpty())
//			return null;
//		listEntities.sort((e1, e2) -> e2.startDate.compareTo(e1.startDate));
//		List<DateHistoryItem> listDepartmentHistories = listEntities.stream()
//				.map(e -> new DateHistoryItem(e.pk.departmentHistoryId, new DatePeriod(e.startDate, e.endDate)))
//				.collect(Collectors.toList());
//		return new DepartmentConfiguration(listEntities.get(0).pk.companyId, listDepartmentHistories);
//	}
//
//	public static List<BsymtDepartmentConfig> fromDomain(DepartmentConfiguration domain) {
//		if (domain.items().isEmpty())
//			return Collections.emptyList();
//		return domain.items().stream()
//				.map(h -> new BsymtDepartmentConfig(new BsymtDepartmentConfigPk(domain.getCompanyId(), h.identifier()),
//						h.start(), h.end()))
//				.collect(Collectors.toList());
//	}
//
//}
