package nts.uk.ctx.at.record.infra.repository.dailyperformanceformat;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.BusinessTypeFormatDaily;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.repository.BusinessTypeFormatDailyRepository;
import nts.uk.ctx.at.record.infra.entity.dailyperformanceformat.KrcmtBusinessTypeDaily;
import nts.uk.ctx.at.record.infra.entity.dailyperformanceformat.KrcmtBusinessTypeDailyPK;
import nts.uk.shr.com.context.AppContexts;

@Stateless
public class JpaBusinessTypeFormatDailyRepository extends JpaRepository implements BusinessTypeFormatDailyRepository {

	private static final String FIND;

	private static final String FIND_DETAIl;

	private static final String UPDATE_BY_KEY;

	private static final String REMOVE_EXIST_DATA;
	
	private static final String IS_EXIST_DATA;

	private final static String SEL_FORMAT_BY_ATD_ITEM = "SELECT f FROM KrcmtBusinessTypeDaily f WHERE f.krcmtBusinessTypeDailyPK.companyId = :companyId AND f.krcmtBusinessTypeDailyPK.attendanceItemId IN :lstItem";
	
	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcmtBusinessTypeDaily a ");
		builderString.append("WHERE a.krcmtBusinessTypeDailyPK.companyId = :companyId ");
		builderString.append("AND a.krcmtBusinessTypeDailyPK.businessTypeCode = :businessTypeCode ");
		FIND = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcmtBusinessTypeDaily a ");
		builderString.append("WHERE a.krcmtBusinessTypeDailyPK.companyId = :companyId ");
		builderString.append("AND a.krcmtBusinessTypeDailyPK.businessTypeCode = :businessTypeCode ");
		builderString.append("AND a.krcmtBusinessTypeDailyPK.sheetNo = :sheetNo ");
		FIND_DETAIl = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("UPDATE KrcmtBusinessTypeDaily a ");
		builderString.append("SET a.order = :order , a.columnWidth = :columnWidth ");
		builderString.append("WHERE a.krcmtBusinessTypeDailyPK.companyId = :companyId ");
		builderString.append("AND a.krcmtBusinessTypeDailyPK.businessTypeCode = :businessTypeCode ");
		builderString.append("AND a.krcmtBusinessTypeDailyPK.attendanceItemId = :attendanceItemId ");
		builderString.append("AND a.krcmtBusinessTypeDailyPK.sheetNo = :sheetNo ");
		UPDATE_BY_KEY = builderString.toString();

		builderString = new StringBuilder();
		builderString.append("DELETE ");
		builderString.append("FROM KrcmtBusinessTypeDaily a ");
		builderString.append("WHERE a.krcmtBusinessTypeDailyPK.attendanceItemId IN :attendanceItemIds ");
		REMOVE_EXIST_DATA = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("SELECT COUNT(a) ");
		builderString.append("FROM KrcmtBusinessTypeDaily a ");
		builderString
				.append("WHERE a.krcmtBusinessTypeDailyPK.companyId = :companyId ");
		builderString.append("AND a.krcmtBusinessTypeDailyPK.businessTypeCode = :businessTypeCode ");
		builderString.append("AND a.krcmtBusinessTypeDailyPK.sheetNo = :sheetNo ");
		IS_EXIST_DATA = builderString.toString();
	}

	@Override
	public List<BusinessTypeFormatDaily> getBusinessTypeFormat(String companyId, String businessTypeCode) {
		return this.queryProxy().query(FIND, KrcmtBusinessTypeDaily.class).setParameter("companyId", companyId)
				.setParameter("businessTypeCode", businessTypeCode).getList(f -> toDomain(f));
	}

	@Override
	public List<BusinessTypeFormatDaily> getBusinessTypeFormatDailyDetail(String companyId, String businessTypeCode,
			BigDecimal sheetNo) {

		return this.queryProxy().query(FIND_DETAIl, KrcmtBusinessTypeDaily.class).setParameter("companyId", companyId)
				.setParameter("businessTypeCode", businessTypeCode).setParameter("sheetNo", sheetNo)
				.getList(f -> toDomain(f));
	}

	@Override
	public void deleteExistData(List<Integer> attendanceItemIds) {
		this.getEntityManager().createQuery(REMOVE_EXIST_DATA).setParameter("attendanceItemIds", attendanceItemIds)
				.executeUpdate();
	}

	@Override
	public void add(List<BusinessTypeFormatDaily> businessTypeFormatDailies) {
		businessTypeFormatDailies.forEach(f -> this.commandProxy().insert(toEntity(f)));
	}

	@Override
	public void update(BusinessTypeFormatDaily businessTypeFormatDaily) {
		this.getEntityManager().createQuery(UPDATE_BY_KEY)
				.setParameter("companyId", businessTypeFormatDaily.getCompanyId())
				.setParameter("businessTypeCode", businessTypeFormatDaily.getBusinessTypeCode().v())
				.setParameter("attendanceItemId", businessTypeFormatDaily.getAttendanceItemId())
				.setParameter("sheetNo", businessTypeFormatDaily.getSheetNo())
				.setParameter("columnWidth", businessTypeFormatDaily.getColumnWidth())
				.setParameter("order", businessTypeFormatDaily.getOrder()).executeUpdate();
	}

	@Override
	public boolean checkExistData(String companyId, String businessTypeCode, BigDecimal sheetNo) {
		return this.queryProxy().query(IS_EXIST_DATA, long.class).setParameter("companyId", companyId)
				.setParameter("businessTypeCode", businessTypeCode)
				.setParameter("sheetNo", sheetNo).getSingle().get() > 0;
	}

	private static BusinessTypeFormatDaily toDomain(KrcmtBusinessTypeDaily krcmtBusinessTypeDaily) {
		BusinessTypeFormatDaily workTypeFormatDaily = BusinessTypeFormatDaily.createFromJavaType(
				krcmtBusinessTypeDaily.krcmtBusinessTypeDailyPK.companyId,
				krcmtBusinessTypeDaily.krcmtBusinessTypeDailyPK.businessTypeCode,
				krcmtBusinessTypeDaily.krcmtBusinessTypeDailyPK.attendanceItemId,
				krcmtBusinessTypeDaily.krcmtBusinessTypeDailyPK.sheetNo, krcmtBusinessTypeDaily.order,
				krcmtBusinessTypeDaily.columnWidth);
		return workTypeFormatDaily;
	}
	
	private KrcmtBusinessTypeDaily toEntity(BusinessTypeFormatDaily businessTypeFormatDaily){
		val entity = new KrcmtBusinessTypeDaily();
		
		entity.krcmtBusinessTypeDailyPK = new KrcmtBusinessTypeDailyPK();
		entity.krcmtBusinessTypeDailyPK.attendanceItemId = businessTypeFormatDaily.getAttendanceItemId();
		entity.krcmtBusinessTypeDailyPK.businessTypeCode = businessTypeFormatDaily.getBusinessTypeCode().v();
		entity.krcmtBusinessTypeDailyPK.companyId = businessTypeFormatDaily.getCompanyId();
		entity.krcmtBusinessTypeDailyPK.sheetNo = businessTypeFormatDaily.getSheetNo();
		entity.columnWidth = businessTypeFormatDaily.getColumnWidth();
		entity.order = businessTypeFormatDaily.getOrder();
		
		return entity;
	}
	
	@Override
	public void updateColumnsWidth(Map<Integer, Integer> lstHeader) {
		List<KrcmtBusinessTypeDaily> lstBusDailyItem = this.queryProxy()
				.query(SEL_FORMAT_BY_ATD_ITEM, KrcmtBusinessTypeDaily.class)
				.setParameter("companyId", AppContexts.user().companyId()).setParameter("lstItem", lstHeader.keySet())
				.getList();
		for(KrcmtBusinessTypeDaily busItem: lstBusDailyItem){
			busItem.columnWidth =  new BigDecimal(lstHeader.get(busItem.krcmtBusinessTypeDailyPK.attendanceItemId));
		}
		this.commandProxy().updateAll(lstBusDailyItem);
	}
	
}
