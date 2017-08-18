package nts.uk.ctx.at.record.infra.repository.dailyperformanceformat;

import java.math.BigDecimal;
import java.util.Optional;

import javax.ejb.Stateless;

import lombok.val;
import nts.arc.layer.infra.data.JpaRepository;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.BusinessFormatSheet;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.primitivevalue.BusinessTypeCode;
import nts.uk.ctx.at.record.dom.dailyperformanceformat.repository.BusinessFormatSheetRepository;
import nts.uk.ctx.at.record.infra.entity.dailyperformanceformat.KrcmtBusinessFormatSheet;
import nts.uk.ctx.at.record.infra.entity.dailyperformanceformat.KrcmtBusinessFormatSheetPK;

@Stateless
public class JpaBusinessFormatSheetRepository extends JpaRepository implements BusinessFormatSheetRepository {

	private static final String FIND;

	private static final String UPDATE_BY_KEY;

	static {
		StringBuilder builderString = new StringBuilder();
		builderString.append("SELECT a ");
		builderString.append("FROM KrcmtBusinessFormatSheet a ");
		builderString.append("WHERE a.krcmtBusinessFormatSheetPK.companyId = :companyId ");
		builderString.append("AND a.krcmtBusinessFormatSheetPK.businessTypeCode = :businessTypeCode ");
		builderString.append("AND a.krcmtBusinessFormatSheetPK.sheetNo = :sheetNo ");
		FIND = builderString.toString();
		
		builderString = new StringBuilder();
		builderString.append("UPDATE KrcmtBusinessFormatSheet a ");
		builderString.append("SET a.sheetName = :sheetName ");
		builderString.append("WHERE a.krcmtBusinessFormatSheetPK.companyId = :companyId ");
		builderString.append("AND a.krcmtBusinessFormatSheetPK.businessTypeCode = :businessTypeCode ");
		builderString.append("AND a.krcmtBusinessFormatSheetPK.sheetNo = :sheetNo ");
		UPDATE_BY_KEY = builderString.toString();
	}

	@Override
	public Optional<BusinessFormatSheet> getSheetInformation(String companyId, BusinessTypeCode businessTypeCode,
			BigDecimal sheetNo) {
		return this.queryProxy().query(FIND, KrcmtBusinessFormatSheet.class).setParameter("companyId", companyId)
				.setParameter("businessTypeCode", businessTypeCode.v()).setParameter("sheetNo", sheetNo)
				.getSingle(f -> toDomain(f));
	}

	@Override
	public void update(BusinessFormatSheet businessFormatSheet) {
		this.getEntityManager().createQuery(UPDATE_BY_KEY)
				.setParameter("companyId", businessFormatSheet.getCompanyId())
				.setParameter("businessTypeCode", businessFormatSheet.getBusinessTypeCode().v())
				.setParameter("sheetNo", businessFormatSheet.getSheetNo())
				.setParameter("sheetName", businessFormatSheet.getSheetName()).executeUpdate();
	}

	@Override
	public void add(BusinessFormatSheet businessFormatSheet) {
		this.commandProxy().insert(toEntity(businessFormatSheet));
	}

	private static BusinessFormatSheet toDomain(KrcmtBusinessFormatSheet krcmtBusinessFormatSheet) {
		BusinessFormatSheet businessFormatSheet = BusinessFormatSheet.createJavaTye(
				krcmtBusinessFormatSheet.krcmtBusinessFormatSheetPK.companyId,
				krcmtBusinessFormatSheet.krcmtBusinessFormatSheetPK.businessTypeCode,
				krcmtBusinessFormatSheet.krcmtBusinessFormatSheetPK.sheetNo, krcmtBusinessFormatSheet.sheetName);
		return businessFormatSheet;
	}

	private KrcmtBusinessFormatSheet toEntity(BusinessFormatSheet businessFormatSheet){
		val entity = new KrcmtBusinessFormatSheet();
		
		entity.krcmtBusinessFormatSheetPK = new KrcmtBusinessFormatSheetPK();
		entity.krcmtBusinessFormatSheetPK.companyId = businessFormatSheet.getCompanyId();
		entity.krcmtBusinessFormatSheetPK.businessTypeCode = businessFormatSheet.getBusinessTypeCode().v();
		entity.krcmtBusinessFormatSheetPK.sheetNo = businessFormatSheet.getSheetNo();
		entity.sheetName = businessFormatSheet.getSheetName();
		
		return entity;
	}

}
