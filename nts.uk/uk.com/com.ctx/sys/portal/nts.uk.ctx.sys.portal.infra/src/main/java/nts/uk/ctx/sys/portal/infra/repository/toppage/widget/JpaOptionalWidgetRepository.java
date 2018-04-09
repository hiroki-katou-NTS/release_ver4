package nts.uk.ctx.sys.portal.infra.repository.toppage.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;

import nts.arc.layer.infra.data.JpaRepository;
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

	private final String SELECT_ALL_TOPPAGEPART = "SELECT c FROM CcgmtTopPagePart AS c where c.ccgmtTopPagePartPK.companyID = :companyID ORDER BY c.code";
	private final String SELECT_WIDGET_DISPLAY = "SELECT s FROM SptstWidgetDisplay AS s where s.sptstWidgetDisplayPK.companyID = :companyID "
			+ "AND s.sptstWidgetDisplayPK.topPagePartID =:topPagePartID ";
	private final String FIND_BY_CODE = "SELECT c FROM CcgmtTopPagePart AS c where c.ccgmtTopPagePartPK.companyID = :companyID "
			+ "AND c.code =:code ";
	private final String GET_SELECTED_WIDGET = "SELECT c FROM CcgmtTopPagePart AS c where c.ccgmtTopPagePartPK.companyID = :companyID "
			+ "AND c.code =:code AND c.topPagePartType =:topPagePartType ";

	@Override
	public List<OptionalWidget> findByCompanyId(String companyID) {
		List<OptionalWidget> optionalWidgets = new ArrayList<OptionalWidget>();
		List<CcgmtTopPagePart> ccgmtTopPageParts = this.queryProxy()
				.query(SELECT_ALL_TOPPAGEPART, CcgmtTopPagePart.class).setParameter("companyID", companyID).getList();
		ccgmtTopPageParts.stream().forEach(c -> {
			List<WidgetDisplayItem> sptstOptionalWidgets = this.queryProxy()
					.query(SELECT_WIDGET_DISPLAY, SptstWidgetDisplay.class)
					.setParameter("companyID", c.ccgmtTopPagePartPK.companyID)
					.setParameter("topPagePartID", c.ccgmtTopPagePartPK.topPagePartID)
					.getList(s -> toDomainDisplayItem(s));
			optionalWidgets.add(new OptionalWidget(c.ccgmtTopPagePartPK.companyID, c.ccgmtTopPagePartPK.topPagePartID,
					new TopPagePartCode(c.code), new TopPagePartName(c.name),
					TopPagePartType.valueOf(c.topPagePartType), Size.createFromJavaType(c.width, c.height),
					sptstOptionalWidgets));
		});
		return optionalWidgets;
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
		CcgmtTopPagePart cmTopPagePart = new CcgmtTopPagePart();
		CcgmtTopPagePartPK cmPagePartPK = new CcgmtTopPagePartPK(widget.getCompanyID(), widget.getToppagePartID());
		cmTopPagePart.ccgmtTopPagePartPK = cmPagePartPK;
		cmTopPagePart.code = widget.getCode().v();
		cmTopPagePart.height = widget.getHeight().v();
		cmTopPagePart.name = widget.getName().v();
		cmTopPagePart.topPagePartType = widget.getType().value;
		cmTopPagePart.width = widget.getWidth().v();
		this.commandProxy().insert(cmTopPagePart);

		SptstOptionalWidget sptstOptionalWidget = new SptstOptionalWidget();
		SptstOptionalWidgetPK sptstOptionalWidgetPK = new SptstOptionalWidgetPK(widget.getCompanyID(),
				widget.getToppagePartID());
		sptstOptionalWidget.sptstOptionalWidgetPK = sptstOptionalWidgetPK;
		this.commandProxy().insert(sptstOptionalWidget);

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
	public boolean isExist(String companyId, String code) {
		Optional<CcgmtTopPagePart> optional = this.queryProxy().query(FIND_BY_CODE, CcgmtTopPagePart.class)
				.setParameter("companyID", companyId).setParameter("code", code).getSingle();
		return optional.isPresent();
	}

	@Override
	public List<OptionalWidget> getSelectedWidget(String companyId, String topPagePartCode, int topPagePartType) {
		List<OptionalWidget> optionalWidgets = new ArrayList<OptionalWidget>();
		
		List<CcgmtTopPagePart> ccgmtTopPageParts = this.queryProxy()
				.query(SELECT_ALL_TOPPAGEPART, CcgmtTopPagePart.class).setParameter("companyID", companyId).getList();
		
		ccgmtTopPageParts.stream().forEach(c -> {
			List<WidgetDisplayItem> sptstOptionalWidgets = this.queryProxy()
					.query(GET_SELECTED_WIDGET, SptstWidgetDisplay.class)
					.setParameter("companyID", companyId)
					.setParameter("code", topPagePartCode)
					.setParameter("topPagePartType", topPagePartType)
					.getList(s -> toDomainDisplayItem(s));
			optionalWidgets.add(new OptionalWidget(c.ccgmtTopPagePartPK.companyID, c.ccgmtTopPagePartPK.topPagePartID,
					new TopPagePartCode(c.code), new TopPagePartName(c.name),
					TopPagePartType.valueOf(c.topPagePartType), Size.createFromJavaType(c.width, c.height),
					sptstOptionalWidgets));
		});
		
		return optionalWidgets;
	}

}
