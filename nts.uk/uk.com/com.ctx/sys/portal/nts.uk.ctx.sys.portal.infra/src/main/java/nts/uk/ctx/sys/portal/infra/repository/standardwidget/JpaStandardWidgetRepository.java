package nts.uk.ctx.sys.portal.infra.repository.standardwidget;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.portal.dom.toppagepart.standardwidget.StandardWidget;
import nts.uk.ctx.sys.portal.dom.toppagepart.standardwidget.StandardWidgetRepository;
import nts.uk.ctx.sys.portal.infra.entity.standardwidget.SptmtStandardWidget;
import nts.uk.ctx.sys.portal.infra.entity.toppagepart.CcgmtTopPagePart;
@Stateless
public class JpaStandardWidgetRepository extends JpaRepository implements StandardWidgetRepository {

	
	private static final String  SELECT_ALL = " SELECT s, t FROM SptmtStandardWidget s, CcgmtTopPagePart t " 
										+ " WHERE s.sptmtStandardWidgetPK.toppagePartID = t.ccgmtTopPagePartPK.topPagePartID";
	
	private static final String  SELECT_IN_BY_TOP_PAGE_PART_ID = " SELECT s, t FROM SptmtStandardWidget s, CcgmtTopPagePart t " 
			+ " WHERE s.sptmtStandardWidgetPK.toppagePartID = t.ccgmtTopPagePartPK.topPagePartID "
			+ "AND t.ccgmtTopPagePartPK.topPagePartID IN :toppagePartIDs "
			+ "AND t.ccgmtTopPagePartPK.companyID =:cID";

	private static final String  SELECT_BY_ID = SELECT_ALL + "AND s.sptmtStandardWidgetPK.toppagePartID =:toppagePartID AND s.sptmtStandardWidgetPK.companyID =:companyID";
	
	@Override
	public List<StandardWidget> getAll(){
		return this.queryProxy().query(SELECT_ALL , Object[].class)
				.getList(c -> joinObjectToDomain(c));
	}

	@Override
	public Optional<StandardWidget> getByID(String ToppagePartID, String companyID) {
		return this.queryProxy().query(SELECT_BY_ID , Object[].class)
				.setParameter("toppagePartID", ToppagePartID)
				.setParameter("companyID", companyID)
				.getSingle(c -> joinObjectToDomain(c));
	}

/*	private final String  SELECT_ALL = " SELECT d FROM CcgmtStandardWidget " 
			+ " WHERE d.ccgmtStandardWidgetPK.toppagePartID = :toppagePartID ";
	@Override
	public List<StandardWidget> getByListID(String listToppagePartID) {
		// TODO Auto-generated method stub
		return this.queryProxy().query(SELECT_ALL , Object[].class)
				.setParameter("toppagePartID", listToppagePartID)
				.getList( c -> joinObjectToDomain(c));
	}
	*/
	private StandardWidget joinObjectToDomain(Object[] entity) {
		SptmtStandardWidget standardWidget = (SptmtStandardWidget) entity[0];
		CcgmtTopPagePart toppagePart = (CcgmtTopPagePart) entity[1];
		return  StandardWidget.createFromJavaType(
				toppagePart.ccgmtTopPagePartPK.companyID,
				standardWidget.sptmtStandardWidgetPK.toppagePartID,
				toppagePart.code,
				toppagePart.name,
				toppagePart.topPagePartType,
				toppagePart.width,
				toppagePart.height);
	}

	@Override
	public List<StandardWidget> findByTopPagePartId(List<String> toppagePartIDs, String cID) {
		List<StandardWidget> resultList = new ArrayList<>();
		CollectionUtil.split(toppagePartIDs, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			resultList.addAll(this.queryProxy().query(SELECT_IN_BY_TOP_PAGE_PART_ID , Object[].class)
				.setParameter("toppagePartIDs", subList)
				.setParameter("cID", cID)
				.getList(c -> joinObjectToDomain(c)));
		});
		return resultList;
	}

}
