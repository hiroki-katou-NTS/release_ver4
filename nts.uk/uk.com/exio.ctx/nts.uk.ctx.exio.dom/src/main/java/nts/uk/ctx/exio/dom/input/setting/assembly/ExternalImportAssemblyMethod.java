package nts.uk.ctx.exio.dom.input.setting.assembly;

import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.uk.ctx.exio.dom.input.ExecutionContext;
import nts.uk.ctx.exio.dom.input.csvimport.CsvRecord;
import nts.uk.ctx.exio.dom.input.csvimport.ExternalImportCsvFileInfo;
import nts.uk.ctx.exio.dom.input.setting.assembly.mapping.ImportingMapping;

/**
 * 受入データの組み立て方法
 */
@Getter
@AllArgsConstructor
public class ExternalImportAssemblyMethod {
	
	/** CSVファイル情報 */
	private ExternalImportCsvFileInfo csvFileInfo;
	
	/** マッピング */
	private ImportingMapping mapping;
	
	/**
	 * 組み立てる
	 * @param require
	 * @param context
	 * @param csvRecord
	 * @return
	 */
	public Optional<RevisedDataRecord> assemble(Require require, ExecutionContext context, CsvRecord csvRecord){
		return mapping.assemble(require, context, csvRecord);
	}
	
	public List<Integer> getAllItemNo() {
		return mapping.getAllItemNo();
	}
	
	public interface Require extends ImportingMapping.RequireAssemble {
	}
}
