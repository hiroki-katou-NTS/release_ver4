package nts.uk.ctx.exio.dom.input.canonicalize.domains.employee.holiday.year;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import lombok.val;
import nts.uk.ctx.at.shared.dom.remainingnumber.annualleave.empinfo.basicinfo.AnnualLeaveEmpBasicInfo;
import nts.uk.ctx.exio.dom.input.ExecutionContext;
import nts.uk.ctx.exio.dom.input.canonicalize.CanonicalItem;
import nts.uk.ctx.exio.dom.input.canonicalize.CanonicalItemList;
import nts.uk.ctx.exio.dom.input.canonicalize.CanonicalizeUtil;
import nts.uk.ctx.exio.dom.input.canonicalize.domaindata.DomainDataColumn;
import nts.uk.ctx.exio.dom.input.canonicalize.domaindata.KeyValues;
import nts.uk.ctx.exio.dom.input.canonicalize.domains.DomainCanonicalization;
import nts.uk.ctx.exio.dom.input.canonicalize.domains.generic.IndependentCanonicalization;
import nts.uk.ctx.exio.dom.input.canonicalize.methods.EmployeeCodeCanonicalization;
import nts.uk.ctx.exio.dom.input.canonicalize.methods.IntermediateResult;
import nts.uk.ctx.exio.dom.input.errors.ExternalImportError;
import nts.uk.ctx.exio.dom.input.meta.ImportingDataMeta;
import nts.uk.ctx.exio.dom.input.workspace.domain.DomainWorkspace;

/**
 * 社員の年休付与設定
 */
public class EmployeeYearHolidaySettingCanonicalization extends IndependentCanonicalization {

	@Override
	protected String getParentTableName() {
		return "KRCMT_HDPAID_BASIC";
	}

	@Override
	protected List<String> getChildTableNames() {
		return Collections.emptyList();
	}

	@Override
	protected List<DomainDataColumn> getDomainDataKeys() {
		return Arrays.asList(DomainDataColumn.SID);
	}
	
	
	private final EmployeeCodeCanonicalization employeeCodeCanonicalization;
	
	public EmployeeYearHolidaySettingCanonicalization(DomainWorkspace workspace) {
		super(workspace);
		this.employeeCodeCanonicalization = new EmployeeCodeCanonicalization(workspace);
	}
	
	/**
	 * 正準化
	 */
	@Override
	public void canonicalize(DomainCanonicalization.RequireCanonicalize require, ExecutionContext context) {

		// 受入データ内の重複チェック
		Set<List<Object>> importingKeys = new HashSet<>();
		
		CanonicalizeUtil.forEachEmployee(require, context, employeeCodeCanonicalization, interms -> {

			for (val interm : interms) {
				val addedFixedItem = interm.addCanonicalized(addFixedItem(interm)) ;
				val key = getPrimaryKeys(addedFixedItem, workspace);
				if (importingKeys.contains(key)) {
					require.add(context, ExternalImportError.record(interm.getRowNo(), "キーが重複しています。：" + key));
					continue;
				}
				importingKeys.add(key);
				super.canonicalize(require, context, addedFixedItem, new KeyValues(key));
			}
		});
	}
	
	public static interface RequireCanonicalize{
		Optional<AnnualLeaveEmpBasicInfo> getExistingEmployeeGrantHoliday(String employeeId);
	}
	
	/**
	 *  受入時に固定の値を入れる物たち
	 */
	private CanonicalItemList addFixedItem(IntermediateResult interm) {
	    List<CanonicalItem> items = new ArrayList<>();
	    items.add(new CanonicalItem(100,null));
	    
	    if(!interm.getItemByNo(2).isPresent())
	        items.add(CanonicalItem.of(2, 0));
	    
	    return new CanonicalItemList(items);
	}
	
	private static List<Object> getPrimaryKeys(IntermediateResult record, DomainWorkspace workspace) {
		return Arrays.asList(record.getItemByNo(workspace.getItemByName("SID").getItemNo()).get().getString());
	}
	
	@Override
	public ImportingDataMeta appendMeta(ImportingDataMeta source) {
		return employeeCodeCanonicalization.appendMeta(source);
	}
}
