package nts.uk.ctx.exio.dom.input;

import lombok.Value;
import nts.uk.ctx.exio.dom.input.canonicalize.ImportingMode;
import nts.uk.ctx.exio.dom.input.importableitem.group.ImportingGroupId;
import nts.uk.ctx.exio.dom.input.setting.ExternalImportCode;
import nts.uk.ctx.exio.dom.input.setting.ExternalImportSetting;

/**
 * 外部受入の実行コンテキスト
 */
@Value
public class ExecutionContext {

	/** 会社ID */
	String companyId;
	
	/** 受入設定コード */
	String settingCode;
	
	/** 受入グループID */
	ImportingGroupId groupId;
	
	/** 受入モード */
	ImportingMode mode;
	
	public static ExecutionContext create(ExternalImportSetting source) {
		return new ExecutionContext(
				source.getCompanyId(),
				source.getCode().v(),
				ImportingGroupId.valueOf(source.getExternalImportGroupId()),
				source.getImportingMode());
	}
	
	public ExternalImportCode getExternalImportCode() {
		return new ExternalImportCode(settingCode);
	}
	
}