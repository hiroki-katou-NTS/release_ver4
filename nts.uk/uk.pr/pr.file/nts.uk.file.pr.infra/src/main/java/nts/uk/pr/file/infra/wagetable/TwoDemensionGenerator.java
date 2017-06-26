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
 * The Class TwoDemensionGenerator.
 */
public class TwoDemensionGenerator implements Generator {

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

		ElementSettingDto _1El = history.getElements().get(0);
		ElementSettingDto _2El = history.getElements().get(1);

		// Group to row data.
		Map<String, Map<String, Long>> itemAmountMap = history.getValueItems()
				.stream()
				.collect(Collectors.groupingBy(
						WtItemDto::getElement1Id,
						Collectors.toMap(WtItemDto::getElement2Id, WtItemDto::getAmount)));

		Worksheet ws = context.getWorkbook().getWorksheets().get(0);
		Cells cells = ws.getCells();

		// Name.
		ws.setName(data.getName());

		// Fill header.
		cells.get(1, 0).setValue(_1El.getDemensionName());
		cells.get(0, 1).setValue(_2El.getDemensionName());

		int colIndex = 0;
		for (ElementItemDto _2Item : _2El.getItemList()) {
			cells.get(1, colIndex + 1).setValue(_2Item.getDisplayName());
			colIndex++;
		}

		// Fill row value base on 1 el item.
		int rowIndex = 0;
		for (ElementItemDto _1Item : _1El.getItemList()) {
			int rowColIndex = 0;
			Map<String, Long> amountMap = itemAmountMap.get(_1Item.getUuid());
			cells.get(rowIndex + 2, 0).setValue(_1Item.getDisplayName());
			for (ElementItemDto _2Item : _2El.getItemList()) {
				cells.get(rowIndex + 2, rowColIndex + 1).setValue(amountMap.get(_2Item.getUuid()));
				rowColIndex++;
			}
			rowIndex++;
		}

		// Border set.
		Style style = cells.get(1, 0).getStyle();
		cells.createRange(1, 0, _1El.getItemList().size() + 1, _2El.getItemList().size() + 1).setStyle(style);
	}

}
