/******************************************************************
 * Copyright (c) 2016 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.pr.file.infra.wagetable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.inject.Inject;

import com.aspose.cells.Cells;
import com.aspose.cells.Style;
import com.aspose.cells.TextAlignmentType;
import com.aspose.cells.Worksheet;

import nts.uk.ctx.pr.core.app.wagetable.certification.find.CertifyGroupFinder;
import nts.uk.ctx.pr.core.app.wagetable.command.WtUpdateCommand;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.ElementItemDto;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.ElementSettingDto;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.WtHistoryDto;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.WtItemDto;
import nts.uk.ctx.pr.core.dom.wagetable.certification.Certification;
import nts.uk.ctx.pr.core.dom.wagetable.certification.CertifyGroup;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;

/**
 * The Class QualificationGenerator.
 */
@Stateless(name = "QualificationGenerator")
public class QualificationGenerator implements Generator {

	/** The finder. */
	@Inject
	private CertifyGroupFinder finder;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * nts.uk.pr.file.infra.wagetable.Generator#generate(nts.uk.shr.infra.file.
	 * report.aspose.cells.AsposeCellsReportContext,
	 * nts.uk.ctx.pr.core.app.wagetable.command.WtUpdateCommand)
	 */
	@Override
	public void generate(AsposeCellsReportContext context, WtUpdateCommand data) {
		WtHistoryDto history = data.getWtHistoryDto();

		// Set work sheet name.
		Worksheet ws = context.getWorkbook().getWorksheets().get(0);
		ws.setName(data.getName());

		// Set data.
		Cells cells = ws.getCells();
		ElementSettingDto elementSettingDto = history.getElements().get(0);
		cells.get(0, 0).setValue("グルップ");
		cells.get(0, 1).setValue("資格名称");
		cells.get(0, 2).setValue("値");

		// Fill data.
		Map<String, Long> itemToAmountMap = history.getValueItems().stream()
			.collect(Collectors.toMap(WtItemDto::getElement1Id, WtItemDto::getAmount));

		List<CertifyGroup> groups = this.finder.initAll();

		// Map data ui to name
		Map<String, String> codeToUuid = elementSettingDto.getItemList().stream()
			.collect(Collectors.toMap(ElementItemDto::getReferenceCode, ElementItemDto::getUuid));

		int index = 0;
		for (CertifyGroup group : groups) {
			int start = index;
			for (Certification certification : group.getCertifies()) {
				cells.get(index + 1, 1).setValue(certification.getName());
				cells.get(index + 1, 2)
					.setValue(itemToAmountMap.get(codeToUuid.get(certification.getCode())));
				index++;
			}
			cells.merge(start + 1, 0, index - start, 1, false);
			cells.get(start + 1, 0).setValue(group.getName());

		}
		// Border set.
		Style style = cells.get(1, 0).getStyle();
		style.setVerticalAlignment(TextAlignmentType.TOP);
		style.setHorizontalAlignment(TextAlignmentType.LEFT);
		cells.createRange(0, 0, index + 1, 3).setStyle(style);
	}
}
