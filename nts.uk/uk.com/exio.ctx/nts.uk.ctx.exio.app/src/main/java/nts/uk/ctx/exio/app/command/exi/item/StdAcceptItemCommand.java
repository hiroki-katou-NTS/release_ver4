package nts.uk.ctx.exio.app.command.exi.item;

import lombok.Value;
import nts.uk.ctx.exio.app.command.exi.condset.AcScreenCondSetCommand;
import nts.uk.ctx.exio.app.command.exi.dataformat.ChrDataFormatSetCommand;
import nts.uk.ctx.exio.app.command.exi.dataformat.DateDataFormSetCommand;
import nts.uk.ctx.exio.app.command.exi.dataformat.InsTimeDatFmSetCommand;
import nts.uk.ctx.exio.app.command.exi.dataformat.NumDataFormatSetCommand;
import nts.uk.ctx.exio.app.command.exi.dataformat.TimeDatFmSetCommand;
import nts.uk.ctx.exio.dom.exi.dataformat.DataFormatSetting;
import nts.uk.ctx.exio.dom.exi.dataformat.ItemType;
import nts.uk.ctx.exio.dom.exi.item.StdAcceptItem;

@Value
public class StdAcceptItemCommand {

	private int systemType;
	/**
	 * 条件設定コード
	 */
	private String conditionSettingCode;

	/**
	 * 受入項目番号
	 */
	private int acceptItemNumber;

	/**
	 * CSV項目番号
	 */
	private int csvItemNumber;

	/**
	 * CSV項目名
	 */
	private String csvItemName;

	/**
	 * 項目型
	 */
	private int itemType;

	private int categoryItemNo;

	private NumDataFormatSetCommand numberFormatSetting;

	private ChrDataFormatSetCommand charFormatSetting;

	private DateDataFormSetCommand dateFormatSetting;

	private InsTimeDatFmSetCommand instTimeFormatSetting;

	private TimeDatFmSetCommand timeFormatSetting;

	private AcScreenCondSetCommand screenConditionSetting;

	public StdAcceptItem toDomain(String companyId) {
		DataFormatSetting dataFormatSet = null;
		ItemType itemType = ItemType.values()[this.itemType];
		switch (itemType) {
		case NUMERIC:
			dataFormatSet = this.numberFormatSetting == null ? null : this.numberFormatSetting.toDomain();
			break;
		case CHARACTER:
			dataFormatSet = this.charFormatSetting == null ? null : this.charFormatSetting.toDomain();
			break;
		case DATE:
			dataFormatSet = this.dateFormatSetting == null ? null : this.dateFormatSetting.toDomain();
			break;
		case INS_TIME:
			dataFormatSet = this.instTimeFormatSetting == null ? null : this.instTimeFormatSetting.toDomain();
			break;
		case TIME:
			dataFormatSet = this.timeFormatSetting == null ? null : this.timeFormatSetting.toDomain();
			break;
		}
		return new StdAcceptItem(companyId, this.systemType, this.getConditionSettingCode(), this.acceptItemNumber,
				this.categoryItemNo, this.csvItemNumber, this.csvItemName, this.itemType,
				this.screenConditionSetting == null ? null : this.screenConditionSetting.toDomain(), dataFormatSet);
	}

}
