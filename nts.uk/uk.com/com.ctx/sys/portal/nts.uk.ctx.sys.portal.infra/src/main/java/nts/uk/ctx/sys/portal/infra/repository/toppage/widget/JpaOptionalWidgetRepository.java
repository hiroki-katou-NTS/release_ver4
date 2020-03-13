package nts.uk.ctx.sys.portal.infra.repository.toppage.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import nts.arc.layer.infra.data.DbConsts;
import nts.arc.layer.infra.data.JpaRepository;
import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.sys.portal.dom.enums.TopPagePartType;
import nts.uk.ctx.sys.portal.dom.toppagepart.TopPagePartCode;
import nts.uk.ctx.sys.portal.dom.toppagepart.TopPagePartName;
import nts.uk.ctx.sys.portal.dom.toppagepart.optionalwidget.OptionalWidget;
import nts.uk.ctx.sys.portal.dom.toppagepart.optionalwidget.OptionalWidgetRepository;
import nts.uk.ctx.sys.portal.dom.toppagepart.optionalwidget.WidgetDisplayItem;
import nts.uk.ctx.sys.portal.dom.toppagepart.size.Size;
import nts.uk.ctx.sys.portal.infra.entity.toppage.widget.SptstOptionalWidget;
import nts.uk.ctx.sys.portal.infra.entity.toppage.widget.SptstOptionalWidgetPK;
import nts.uk.ctx.sys.portal.infra.entity.toppage.widget.SptstWidgetDisplay;
import nts.uk.ctx.sys.portal.infra.entity.toppage.widget.SptstWidgetDisplayPK;
import nts.uk.ctx.sys.portal.infra.entity.toppagepart.CcgmtTopPagePart;
import nts.uk.ctx.sys.portal.infra.entity.toppagepart.CcgmtTopPagePartPK;

@Stateless
public class JpaOptionalWidgetRepository extends JpaRepository implements OptionalWidgetRepository {

	private static final String SELECT_WIDGET_DISPLAY = "SELECT s FROM SptstWidgetDisplay AS s where s.sptstWidgetDisplayPK.companyID = :companyID "
			+ "AND s.sptstWidgetDisplayPK.topPagePartID =:topPagePartID ";
	private static final String GET_SELECTED_WIDGET = "SELECT c FROM CcgmtTopPagePart AS c where c.ccgmtTopPagePartPK.companyID = :companyID "
			+ "AND c.code =:code AND c.topPagePartType =:topPagePartType ";

	private static final String SELECT_BASE = "SELECT o, t FROM SptstOptionalWidget o "
			+ "INNER JOIN CcgmtTopPagePart t ON o.sptstOptionalWidgetPK.topPagePartID = t.ccgmtTopPagePartPK.topPagePartID "
			+ "AND o.sptstOptionalWidgetPK.companyID = t.ccgmtTopPagePartPK.companyID ";
	
	private static final String SELECT_IN = SELECT_BASE + " WHERE o.sptstOptionalWidgetPK.topPagePartID IN :topPagePartID AND o.sptstOptionalWidgetPK.companyID =:companyID";
	private static final String SELECT_LIST_DISPLAY_ITEMS = "SELECT d FROM SptstWidgetDisplay d WHERE d.sptstWidgetDisplayPK.topPagePartID = :topPagePartID AND d.sptstWidgetDisplayPK.companyID =:companyID";
	
	private static final String SELECT_BY_COMPANY = SELECT_BASE + " WHERE o.sptstOptionalWidgetPK.companyID = :companyID";
			
	@Override
	public List<OptionalWidget> findByCompanyId(String companyID) {
		List<Object[]> Objects = this.queryProxy().query(SELECT_BY_COMPANY, Object[].class)
				.setParameter("companyID", companyID)
			    .getList();
		return this.joinListObjectToDomain(Objects, companyID);
	}

	@Override
	public Optional<OptionalWidget> findByCode(String companyID, String topPagePartID) {
		Optional<CcgmtTopPagePart> optional = this.queryProxy().find(new CcgmtTopPagePartPK(companyID, topPagePartID),
				CcgmtTopPagePart.class);
		if (!optional.isPresent()) {
			return Optional.empty();
		}
		CcgmtTopPagePart cPagePart = optional.get();
		List<WidgetDisplayItem> sptstOptionalWidgets = this.queryProxy()
				.query(SELECT_WIDGET_DISPLAY, SptstWidgetDisplay.class)
				.setParameter("companyID", cPagePart.ccgmtTopPagePartPK.companyID)
				.setParameter("topPagePartID", cPagePart.ccgmtTopPagePartPK.topPagePartID)
				.getList(s -> toDomainDisplayItem(s));
		OptionalWidget optionalWidget = new OptionalWidget(cPagePart.ccgmtTopPagePartPK.companyID,
				cPagePart.ccgmtTopPagePartPK.topPagePartID, new TopPagePartCode(cPagePart.code),
				new TopPagePartName(cPagePart.name), TopPagePartType.valueOf(cPagePart.topPagePartType),
				Size.createFromJavaType(cPagePart.width, cPagePart.height), sptstOptionalWidgets);
		return Optional.of(optionalWidget);
	}

	private WidgetDisplayItem toDomainDisplayItem(SptstWidgetDisplay entity) {
		WidgetDisplayItem widget = WidgetDisplayItem.createFromJavaType(entity.sptstWidgetDisplayPK.widgetType,
				entity.useAtr);
		return widget;
	}

	
	@Override
	public void add(OptionalWidget widget) {
		//insert into Top page part
		CcgmtTopPagePart cmTopPagePart = new CcgmtTopPagePart();
		CcgmtTopPagePartPK cmPagePartPK = new CcgmtTopPagePartPK(widget.getCompanyID(), widget.getToppagePartID());
		cmTopPagePart.ccgmtTopPagePartPK = cmPagePartPK;
		cmTopPagePart.code = widget.getCode().v();
		cmTopPagePart.height = widget.getHeight().v();
		cmTopPagePart.name = widget.getName().v();
		cmTopPagePart.topPagePartType = widget.getType().value;
		cmTopPagePart.width = widget.getWidth().v();
		this.commandProxy().insert(cmTopPagePart);
		//insert into Optional Widget
		SptstOptionalWidget sptstOptionalWidget = new SptstOptionalWidget();
		SptstOptionalWidgetPK sptstOptionalWidgetPK = new SptstOptionalWidgetPK(widget.getCompanyID(),
				widget.getToppagePartID());
		sptstOptionalWidget.sptstOptionalWidgetPK = sptstOptionalWidgetPK;
		this.commandProxy().insert(sptstOptionalWidget);
		//insert into Widget display items
		widget.getWDisplayItems().stream().forEach(x -> {
			SptstWidgetDisplay sptstWidgetDisplay = new SptstWidgetDisplay();
			SptstWidgetDisplayPK sptstWidgetDisplayPK = new SptstWidgetDisplayPK(widget.getCompanyID(),
					widget.getToppagePartID(), x.getDisplayItemType().value);
			sptstWidgetDisplay.sptstWidgetDisplayPK = sptstWidgetDisplayPK;
			sptstWidgetDisplay.useAtr = x.getNotUseAtr().value;
			this.commandProxy().insert(sptstWidgetDisplay);
		});
	}

	@Override
	public void update(OptionalWidget widget) {
		CcgmtTopPagePart cmTopPagePart = new CcgmtTopPagePart();
		CcgmtTopPagePartPK cmPagePartPK = new CcgmtTopPagePartPK(widget.getCompanyID(), widget.getToppagePartID());
		cmTopPagePart.ccgmtTopPagePartPK = cmPagePartPK;
		cmTopPagePart.code = widget.getCode().v();
		cmTopPagePart.height = widget.getHeight().v();
		cmTopPagePart.name = widget.getName().v();
		cmTopPagePart.topPagePartType = widget.getType().value;
		cmTopPagePart.width = widget.getWidth().v();
		this.commandProxy().update(cmTopPagePart);

		SptstOptionalWidget sptstOptionalWidget = new SptstOptionalWidget();
		SptstOptionalWidgetPK sptstOptionalWidgetPK = new SptstOptionalWidgetPK(widget.getCompanyID(),
				widget.getToppagePartID());
		sptstOptionalWidget.sptstOptionalWidgetPK = sptstOptionalWidgetPK;
		this.commandProxy().update(sptstOptionalWidget);

		widget.getWDisplayItems().stream().forEach(x -> {
			SptstWidgetDisplay sptstWidgetDisplay = new SptstWidgetDisplay();
			SptstWidgetDisplayPK sptstWidgetDisplayPK = new SptstWidgetDisplayPK(widget.getCompanyID(),
					widget.getToppagePartID(), x.getDisplayItemType().value);
			sptstWidgetDisplay.sptstWidgetDisplayPK = sptstWidgetDisplayPK;
			sptstWidgetDisplay.useAtr = x.getNotUseAtr().value;
			this.commandProxy().update(sptstWidgetDisplay);
		});

	}

	@Override
	public void remove(String companyID, String topPagePartID, List<Integer> displayItemTypes) {
		List<SptstWidgetDisplayPK> sWidgetDisplayPKs = new ArrayList<>();
		displayItemTypes.stream().forEach(x -> {
			sWidgetDisplayPKs.add(new SptstWidgetDisplayPK(companyID, topPagePartID, x.intValue()));
		});
		this.commandProxy().removeAll(SptstWidgetDisplay.class, sWidgetDisplayPKs);
		this.getEntityManager().flush();
		this.commandProxy().remove(SptstOptionalWidget.class, new SptstOptionalWidgetPK(companyID, topPagePartID));
		this.commandProxy().remove(CcgmtTopPagePart.class, new CcgmtTopPagePartPK(companyID, topPagePartID));
	}

	@Override
	public boolean isExist(String companyId, String code, int type) {
		List<CcgmtTopPagePart> list = this.queryProxy().query(GET_SELECTED_WIDGET, CcgmtTopPagePart.class)
				.setParameter("companyID", companyId)
				.setParameter("code", code)
				.setParameter("topPagePartType", type).getList();
		return list.size() > 0;
	}
	
	private static final String SELECT_BY_TOP_PAGE_PART_CODE = SELECT_BASE + " WHERE o.sptstOptionalWidgetPK.companyID = :companyID AND t.code =:topPagePartCode AND t.topPagePartType =:topPagePartType";
	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public Optional<OptionalWidget> getSelectedWidget(String companyId, String topPagePartCode) {
		
		Optional<OptionalWidget> ccgmtTopPageParts = this.queryProxy()
				.query(SELECT_BY_TOP_PAGE_PART_CODE, Object[].class)
				.setParameter("companyID", companyId)
				.setParameter("topPagePartCode", topPagePartCode)
				.setParameter("topPagePartType", TopPagePartType.OptionalWidget.value)
				.getSingle(c ->joinObjectToDomain(c));
		return ccgmtTopPageParts;
	}
	
	@Override
	public List<OptionalWidget> findByCode(String companyId, List<String> listOptionalWidgetID) {
		if(listOptionalWidgetID.size()==0) {
			return Collections.emptyList();
		}
		List<OptionalWidget> results = new ArrayList<>();
		CollectionUtil.split(listOptionalWidgetID, DbConsts.MAX_CONDITIONS_OF_IN_STATEMENT, subList -> {
			results.addAll(this.queryProxy().query(SELECT_IN, Object[].class)
				.setParameter("topPagePartID", subList)
				.setParameter("companyID", companyId)
				.getList(c -> joinObjectToDomain(c)));
		});
		return results;
	}

	private OptionalWidget joinObjectToDomain(Object[] entity) {
		SptstOptionalWidget OptionalWidget = (SptstOptionalWidget) entity[0];
		CcgmtTopPagePart topPagePart = (CcgmtTopPagePart) entity[1];
		List<WidgetDisplayItem> wDisplayItems = widgetDisplayItems(OptionalWidget.sptstOptionalWidgetPK.topPagePartID, OptionalWidget.sptstOptionalWidgetPK.companyID);
		return new OptionalWidget(OptionalWidget.sptstOptionalWidgetPK.companyID, OptionalWidget.sptstOptionalWidgetPK.topPagePartID, 
				new TopPagePartCode(topPagePart.code), new TopPagePartName(topPagePart.name), TopPagePartType.valueOf(topPagePart.topPagePartType),
				Size.createFromJavaType(topPagePart.width, topPagePart.height), wDisplayItems);
	}
	
	private List<WidgetDisplayItem> widgetDisplayItems(String topPagePartId, String companyID){
		return this.queryProxy().query(SELECT_LIST_DISPLAY_ITEMS, SptstWidgetDisplay.class)
				.setParameter("topPagePartID", topPagePartId)
				.setParameter("companyID", companyID)
				.getList(c ->c.toDomain());
	}

	private List<OptionalWidget> joinListObjectToDomain(List<Object[]> entitys, String companyID) {
		List<OptionalWidget> result = new ArrayList<>();
		List<SptstWidgetDisplay> displayItems = this.findItems(companyID);
		for (Object[] entity : entitys) {
			SptstOptionalWidget OptionalWidget = (SptstOptionalWidget) entity[0];
			CcgmtTopPagePart topPagePart = (CcgmtTopPagePart) entity[1];
			List<WidgetDisplayItem> wDisplayItems = displayItems.stream()
					.filter(c -> c.sptstWidgetDisplayPK.topPagePartID
							.equals(OptionalWidget.sptstOptionalWidgetPK.topPagePartID))
					.map(c -> c.toDomain()).collect(Collectors.toList());
			result.add(new OptionalWidget(OptionalWidget.sptstOptionalWidgetPK.companyID,
					OptionalWidget.sptstOptionalWidgetPK.topPagePartID, new TopPagePartCode(topPagePart.code),
					new TopPagePartName(topPagePart.name), TopPagePartType.valueOf(topPagePart.topPagePartType),
					Size.createFromJavaType(topPagePart.width, topPagePart.height), wDisplayItems));
		}

		return result;
	}
	
	private static final String SELECT_LIST_DISPLAY_ITEMS_BY_COMPANY_ID = "SELECT i FROM SptstOptionalWidget o " 
			+ "INNER JOIN SptstWidgetDisplay i ON o.sptstOptionalWidgetPK.topPagePartID = i.sptstWidgetDisplayPK.topPagePartID "
			+ "AND o.sptstOptionalWidgetPK.companyID = i.sptstWidgetDisplayPK.companyID "
			+ "WHERE o.sptstOptionalWidgetPK.companyID = :companyID ";
	
	private List<SptstWidgetDisplay> findItems(String companyID){
		return this.queryProxy().query(SELECT_LIST_DISPLAY_ITEMS_BY_COMPANY_ID, SptstWidgetDisplay.class)
				.setParameter("companyID", companyID)
				.getList();
	}
}
