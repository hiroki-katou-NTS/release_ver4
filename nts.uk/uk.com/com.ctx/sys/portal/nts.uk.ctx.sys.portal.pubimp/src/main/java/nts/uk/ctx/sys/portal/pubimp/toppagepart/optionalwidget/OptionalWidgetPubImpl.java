package nts.uk.ctx.sys.portal.pubimp.toppagepart.optionalwidget;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;

import nts.uk.ctx.sys.portal.dom.toppagepart.optionalwidget.OptionalWidget;
import nts.uk.ctx.sys.portal.dom.toppagepart.optionalwidget.service.OptionalWidgetService;
import nts.uk.ctx.sys.portal.pub.toppagepart.optionalwidget.OptionalWidgetExport;
import nts.uk.ctx.sys.portal.pub.toppagepart.optionalwidget.OptionalWidgetPub;
import nts.uk.ctx.sys.portal.pub.toppagepart.optionalwidget.WidgetDisplayItemExport;

@Stateless
public class OptionalWidgetPubImpl implements OptionalWidgetPub {

	@Inject
	private OptionalWidgetService optionalWidgetService;

	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	@Override
	public Optional<OptionalWidgetExport> getSelectedWidget(String companyId, String topPagePartCode) {

		Optional<OptionalWidget> OptionalWidget = optionalWidgetService.getSelectedWidget(companyId, topPagePartCode);

		if (!OptionalWidget.isPresent())
			return Optional.empty();

		List<WidgetDisplayItemExport> listDisplayItem = OptionalWidget.get().getWDisplayItems().stream()
				.map(t -> new WidgetDisplayItemExport(t.getDisplayItemType().value, t.getNotUseAtr().value))
				.collect(Collectors.toList());

		return Optional.ofNullable(
				new OptionalWidgetExport(OptionalWidget.get().getToppagePartID(), OptionalWidget.get().getCode().v(),
						OptionalWidget.get().getName().v(), OptionalWidget.get().getSize().getWidth().v(),
						OptionalWidget.get().getSize().getHeight().v(), listDisplayItem));
	}

}
