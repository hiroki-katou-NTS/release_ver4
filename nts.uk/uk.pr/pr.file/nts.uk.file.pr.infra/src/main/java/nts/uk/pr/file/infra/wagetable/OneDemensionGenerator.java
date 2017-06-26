/******************************************************************
 * Copyright (c) 2015 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.pr.file.infra.wagetable;

import java.util.Map;
import java.util.stream.Collectors;

import com.aspose.cells.Cells;
import com.aspose.cells.Style;
import com.aspose.cells.Worksheet;

import nts.uk.ctx.pr.core.app.wagetable.command.WtUpdateCommand;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.ElementItemDto;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.ElementSettingDto;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.WtHistoryDto;
import nts.uk.ctx.pr.core.app.wagetable.command.dto.WtItemDto;
import nts.uk.shr.infra.file.report.aspose.cells.AsposeCellsReportContext;

/**
 * The Class OneDemensionGenerator.
 */
public class OneDemensionGenerator implements Generator {

	/*
	 * (non-Javadoc)
	 * @see
	 * nts.uk.pr.file.infra.wagetable.Generator#generate(nts.uk.shr.infra.file.
	 * report.aspose.cells.AsposeCellsReportContext,
	 * nts.uk.ctx.pr.core.app.wagetable.command.WtUpdateCommand)
	 */
	@Override
	public void generate(AsposeCellsReportContext context, WtUpdateCommand data) {
		WtHistoryDto history = data.getWtHistoryDto();

		// Set worksheet name.
		Worksheet ws = context.getWorkbook().getWorksheets().get(0);
		ws.setName(data.getName());

		// Set data.
		Cells cells = ws.getCells();
		ElementSettingDto elementSettingDto = history.getElements().get(0);
		cells.get(0, 0).setValue(elementSettingDto.getDemensionName());
		cells.get(0, 1).setValue("値");

		// Fill data.
		Map<String, Long> itemToAmountMap = history.getValueItems()
				.stream()
				.collect(Collectors.toMap(WtItemDto::getElement1Id, WtItemDto::getAmount));
		int index = 0;
		for (ElementItemDto item : elementSettingDto.getItemList()) {
			cells.get(index + 1, 0).setValue(item.getDisplayName());
			cells.get(index + 1, 1).setValue(itemToAmountMap.get(item.getUuid()));
			index ++;
		}

		// Border set.
		Style style = cells.get(1, 0).getStyle();
		cells.createRange(1, 0, index, 2).setStyle(style);
	}
}
