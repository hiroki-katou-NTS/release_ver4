package nts.uk.ctx.sys.log.app.find.reference;

import lombok.Data;
import nts.uk.ctx.sys.log.dom.reference.ItemNoEnum;
import nts.uk.ctx.sys.log.dom.reference.LogOutputItem;
import nts.uk.ctx.sys.log.dom.reference.RecordTypeEnum;

/*
 * author: hiep.th
 */

@Data
public class LogOutputItemDto {

	public LogOutputItemDto() {
		super();
	}

	/** The Item NO. */
	private int itemNo;

	/** The Item Name */
	private String itemName;

	/** The Record Type */
	private int recordType;

	/** Sort Order */
	private int sortOrder;

	public static LogOutputItemDto fromDomain(LogOutputItem domain) {

		LogOutputItemDto logOutputItemDto = new LogOutputItemDto(domain.getItemNo(), domain.getItemName().v(),
				domain.getRecordType().code);
		RecordTypeEnum recordTypeEnum = RecordTypeEnum.valueOf(logOutputItemDto.getRecordType());
		ItemNoEnum itemNoEnum = ItemNoEnum.valueOf(logOutputItemDto.getItemNo());
		switch (recordTypeEnum) {
		case LOGIN:
			switch (itemNoEnum) {
			case ITEM_NO_2:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_2.code);
				break;
			case ITEM_NO_3:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_1.code);
				break;
			case ITEM_NO_7:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_3.code);
				break;
			case ITEM_NO_19:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_5.code);
				break;
			case ITEM_NO_20:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_4.code);
				break;
			case ITEM_NO_22:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_6.code);
				break;
			default:
				break;
			}
			break;
		case START_UP:
			switch (itemNoEnum) {
			case ITEM_NO_2:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_2.code);
				break;
			case ITEM_NO_3:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_1.code);
				break;
			case ITEM_NO_7:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_3.code);
				break;
			case ITEM_NO_18:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_5.code);
				break;
			case ITEM_NO_19:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_4.code);
				break;
			default:
				break;
			}
			break;
		case UPDATE_PERSION_INFO:
			switch (itemNoEnum) {
			case ITEM_NO_2:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_5.code);
				break;
			case ITEM_NO_3:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_4.code);
				break;
			case ITEM_NO_7:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_6.code);
				break;
			case ITEM_NO_20:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_2.code);
				break;
			case ITEM_NO_21:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_1.code);
				break;
			case ITEM_NO_22:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_3.code);
				break;
			case ITEM_NO_23:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_1.code);
				break;
			case ITEM_NO_24:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_3.code);
				break;
			case ITEM_NO_29:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_2.code);
				break;
			case ITEM_NO_31:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_4.code);
				break;
			case ITEM_NO_33:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_5.code);
				break;
			case ITEM_NO_36:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_7.code);
				break;
			default:
				break;
			}
			break;
		case DATA_CORRECT:
			switch (itemNoEnum) {
			case ITEM_NO_2:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_4.code);
				break;
			case ITEM_NO_3:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_3.code);
				break;
			case ITEM_NO_7:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_5.code);
				break;
			case ITEM_NO_20:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_2.code);
				break;
			case ITEM_NO_21:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_1.code);
				break;
			case ITEM_NO_22:
			case ITEM_NO_23:
			case ITEM_NO_24:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_1.code);
				break;
			case ITEM_NO_26:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_5.code);
				break;
			case ITEM_NO_27:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_2.code);
				break;
			case ITEM_NO_30:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_3.code);
				break;
			case ITEM_NO_31:
				logOutputItemDto.setSortOrder(ItemNoEnum.ITEM_NO_4.code);
				break;
			default:
				break;
			}
			break;
		default:
			break;
		}
		return logOutputItemDto;
	}
	
	public static LogOutputItemDto fromDomainAll(LogOutputItem domain) {

		LogOutputItemDto logOutputItemDto = new LogOutputItemDto(domain.getItemNo(), domain.getItemName().v(),
				domain.getRecordType().code);
		return logOutputItemDto;
	}

	public LogOutputItemDto(int itemNo, String itemName, int recordType) {
		super();
		this.itemNo = itemNo;
		this.itemName = itemName;
		this.recordType = recordType;

	}

	public LogOutputItemDto(String itemName) {
		super();
		this.itemName = itemName;
	}
		

}
