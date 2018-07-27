package nts.uk.ctx.at.record.infra.repository.dailyperformanceformat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.BusinessTypeFormatMonthly;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.repository.BusinessTypeFormatMonthlyRepository;
import nts.uk.ctx.at.record.infra.entity.dailyperformanceformat.KrcmtBusinessTypeMonthly;
import nts.uk.ctx.at.record.infra.entity.dailyperformanceformat.KrcmtBusinessTypeMonthlyPK;

@Stateless
public class JpaBusinessTypeFormatMonthlyRepository extends JpaRepository
		implements BusinessTypeFormatMonthlyRepository {

	private static final String FIND;
	
	private static final String FIND_BY_LIST_CODE;

	private static final String UPDATE_BY_KEY;

	private static final String REMOVE_EXIST_DATA;
	
	private static final String IS_EXIST_DATA;
	
	private static final String FIND_BY_COMPANYID;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcmtBusinessTypeMonthly a ");
		builderString.append("WHERE a.krcmtBusinessTypeMonthlyPK.companyId = :companyId ");
		builderString.append("AND a.krcmtBusinessTypeMonthlyPK.businessTypeCode = :businessTypeCode ");
		FIND = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcmtBusinessTypeMonthly a ");
		builderString.append("WHERE a.krcmtBusinessTypeMonthlyPK.companyId = :companyId ");
		builderString.append("AND a.krcmtBusinessTypeMonthlyPK.businessTypeCode IN :listBusinessTypeCode ");
		builderString.append("ORDER BY a.order ASC ");
		FIND_BY_LIST_CODE = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("UPDATE KrcmtBusinessTypeMonthly a ");
		builderString.append("SET a.order = :order , a.columnWidth = :columnWidth ");
		builderString.append("WHERE a.krcmtBusinessTypeMonthlyPK.companyId = :companyId ");
		builderString.append("AND a.krcmtBusinessTypeMonthlyPK.businessTypeCode = :businessTypeCode ");
		builderString.append("AND a.krcmtBusinessTypeMonthlyPK.attendanceItemId = :attendanceItemId ");
		UPDATE_BY_KEY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcmtBusinessTypeMonthly a ");
		builderString.append("WHERE a.krcmtBusinessTypeMonthlyPK.companyId = :companyId ");
		builderString.append("AND a.krcmtBusinessTypeMonthlyPK.businessTypeCode = :businessTypeCode ");
		builderString.append("AND a.krcmtBusinessTypeMonthlyPK.attendanceItemId IN :attendanceItemIds ");
		REMOVE_EXIST_DATA = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(a) ");
		builderString.append("FROM KrcmtBusinessTypeMonthly a ");
		builderString.append("WHERE a.krcmtBusinessTypeMonthlyPK.companyId = :companyId ");
		builderString.append("AND a.krcmtBusinessTypeMonthlyPK.businessTypeCode = :businessTypeCode ");
		IS_EXIST_DATA = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcmtBusinessTypeMonthly a ");
		builderString.append("WHERE a.krcmtBusinessTypeMonthlyPK.companyId = :companyId ");
		FIND_BY_COMPANYID = builderString.toString();
	}

	@Override
	public List<BusinessTypeFormatMonthly> getMonthlyDetail(String companyId, String businessTypeCode) {
		return this.queryProxy().query(FIND, KrcmtBusinessTypeMonthly.class).setParameter("companyId", companyId)
				.setParameter("businessTypeCode", businessTypeCode).getList(f -> toDomain(f));
	}

	@Override
	public void update(BusinessTypeFormatMonthly businessTypeFormatMonthly) {
		this.getEntityManager().createQuery(UPDATE_BY_KEY)
				.setParameter("companyId", businessTypeFormatMonthly.getCompanyId())
				.setParameter("businessTypeCode", businessTypeFormatMonthly.getBusinessTypeCode().v())
				.setParameter("attendanceItemId", businessTypeFormatMonthly.getAttendanceItemId())
				.setParameter("columnWidth", businessTypeFormatMonthly.getColumnWidth())
				.setParameter("order", businessTypeFormatMonthly.getOrder()).executeUpdate();
	}

	/*
	 * Remove attendanceItemId not exist in list that need update
	 */
	@Override
	public void deleteExistData(String companyId, String businessTypeCode, List<Integer> attendanceItemIds) {
		this.getEntityManager().createQuery(REMOVE_EXIST_DATA)
		.setParameter("companyId", companyId)
		.setParameter("businessTypeCode", businessTypeCode)
		.setParameter("attendanceItemIds", attendanceItemIds)
				.executeUpdate();
	}

	@Override
	public void add(List<BusinessTypeFormatMonthly> businessTypeFormatMonthlyAdds) {
		businessTypeFormatMonthlyAdds.forEach(f -> this.commandProxy().insert(toEntity(f)));
	}

	@Override
	public boolean checkExistData(String companyId, String businessTypeCode) {
		return this.queryProxy().query(IS_EXIST_DATA, long.class).setParameter("companyId", companyId)
				.setParameter("businessTypeCode", businessTypeCode).getSingle().get() > 0;
	}

	private static BusinessTypeFormatMonthly toDomain(KrcmtBusinessTypeMonthly krcmtBusinessTypeMonthly) {
		BusinessTypeFormatMonthly workTypeFormatMonthly = BusinessTypeFormatMonthly.createFromJavaType(
				krcmtBusinessTypeMonthly.krcmtBusinessTypeMonthlyPK.companyId,
				krcmtBusinessTypeMonthly.krcmtBusinessTypeMonthlyPK.businessTypeCode,
				krcmtBusinessTypeMonthly.krcmtBusinessTypeMonthlyPK.attendanceItemId, krcmtBusinessTypeMonthly.order,
				krcmtBusinessTypeMonthly.columnWidth);
		return workTypeFormatMonthly;
	}
	
	private KrcmtBusinessTypeMonthly toEntity(BusinessTypeFormatMonthly businessTypeFormatMonthly){
		val entity = new KrcmtBusinessTypeMonthly();
		
		entity.krcmtBusinessTypeMonthlyPK = new KrcmtBusinessTypeMonthlyPK();
		entity.krcmtBusinessTypeMonthlyPK.companyId = businessTypeFormatMonthly.getCompanyId();
		entity.krcmtBusinessTypeMonthlyPK.attendanceItemId = businessTypeFormatMonthly.getAttendanceItemId();
		entity.krcmtBusinessTypeMonthlyPK.businessTypeCode = businessTypeFormatMonthly.getBusinessTypeCode().v();
		entity.columnWidth = businessTypeFormatMonthly.getColumnWidth();
		entity.order = businessTypeFormatMonthly.getOrder();
		
		return entity;
	}

	@Override
	public List<BusinessTypeFormatMonthly> getMonthlyDetailByCompanyId(String companyId) {
		return this.queryProxy().query(FIND_BY_COMPANYID, KrcmtBusinessTypeMonthly.class).setParameter("companyId", companyId).getList(f -> toDomain(f));
	}

	@Override
	public List<BusinessTypeFormatMonthly> getListBusinessTypeFormat(String companyId, List<String> listBusinessTypeCode) {
		return this.queryProxy().query(FIND_BY_LIST_CODE, KrcmtBusinessTypeMonthly.class)
				.setParameter("companyId", companyId)
				.setParameter("listBusinessTypeCode", listBusinessTypeCode).getList(f -> toDomain(f));
	}

	@Override
	public void updateColumnsWidth(String companyId, Map<Integer, Integer> lstHeader, List<String> formatCodes) {
		List<Integer> itemIds = new ArrayList<>();
		itemIds.addAll(lstHeader.keySet());
		List<KrcmtBusinessTypeMonthly> items = this.getListBusinessTypeFormat(companyId, formatCodes).stream()
				.map(x -> toEntity(x)).collect(Collectors.toList());
		List<KrcmtBusinessTypeMonthly> entities = items.stream()
				.map(x -> new KrcmtBusinessTypeMonthly(x.krcmtBusinessTypeMonthlyPK, x.order,
						new BigDecimal(lstHeader.get(x.krcmtBusinessTypeMonthlyPK.attendanceItemId))))
				.collect(Collectors.toList());
		this.commandProxy().updateAll(entities);
	}

}
