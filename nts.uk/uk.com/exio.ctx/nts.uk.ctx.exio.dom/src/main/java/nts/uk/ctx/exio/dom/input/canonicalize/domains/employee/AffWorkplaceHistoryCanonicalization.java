package nts.uk.ctx.exio.dom.input.canonicalize.domains.employee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.val;
import nts.uk.ctx.exio.dom.input.ExecutionContext;
import nts.uk.ctx.exio.dom.input.canonicalize.domaindata.DomainDataColumn;
import nts.uk.ctx.exio.dom.input.canonicalize.domains.DomainCanonicalization;
import nts.uk.ctx.exio.dom.input.canonicalize.domains.ItemNoMap;
import nts.uk.ctx.exio.dom.input.canonicalize.domains.generic.EmployeeHistoryCanonicalization;
import nts.uk.ctx.exio.dom.input.canonicalize.history.HistoryType;
import nts.uk.ctx.exio.dom.input.canonicalize.methods.IntermediateResult;
import nts.uk.ctx.exio.dom.input.canonicalize.methods.WorkplaceCodeCanonicalization;
import nts.uk.ctx.exio.dom.input.errors.ExternalImportError;
import nts.uk.ctx.exio.dom.input.meta.ImportingDataMeta;
import nts.uk.ctx.exio.dom.input.workspace.datatype.DataType;

/**
 * 所属職場履歴の正準化
 */
public class AffWorkplaceHistoryCanonicalization extends EmployeeHistoryCanonicalization implements  DomainCanonicalization {

	private final WorkplaceCodeCanonicalization workplaceCodeCanonicalization;
		
	public AffWorkplaceHistoryCanonicalization() {
		super(HistoryType.PERSISTENERESIDENT);
		workplaceCodeCanonicalization = new WorkplaceCodeCanonicalization(this.getItemNoMap());
	}

	@Override
	protected ItemNoMap getItemNoMapExtends() {
		return new ItemNoMap()
				.put(Names.WKP_CD, 4)
				.put(Names.WKP_ID, 5);
	}
	
	private static class Names {
		static final String WKP_CD = "職場コード";
		static final String WKP_ID = "WORKPLACE_ID";
	}

	@Override
	protected List<Container> canonicalizeExtends(
			DomainCanonicalization.RequireCanonicalize require,
			ExecutionContext context,
			String employeeId,
			List<Container> targetContainers) {
		List<Container> results = new ArrayList<>();
		for (val container : targetContainers) {
			IntermediateResult interm = container.getInterm();
			
			workplaceCodeCanonicalization.canonicalize(require, interm, interm.getRowNo())
					.ifRight(canonicalized -> results.add(new Container(canonicalized, container.getAddingHistoryItem())))
					.ifLeft(error -> require.add(context, ExternalImportError.of(error)));
		}
		return results;
	}
	
	@Override
	protected String getParentTableName() {
		return "BSYMT_AFF_WKP_HIST";
	}
	@Override
	protected List<String> getChildTableNames() {
		return Arrays.asList("BSYMT_AFF_WKP_HIST_ITEM");
	}
	
	@Override
	protected List<DomainDataColumn> getDomainDataKeys() {
		// EmployeeHistoryCanonicalization.toDeleteのキーの順番と合わせる必要がある
		return Arrays.asList(
				new DomainDataColumn("HIST_ID", DataType.STRING),
				new DomainDataColumn("SID", DataType.STRING)
		);
	}
	
	@Override
	public ImportingDataMeta appendMeta(ImportingDataMeta source) {
		return super.appendMeta(source)
				.addItem("WORKPLACE_ID");
	}

	public interface RequireCanonicalize extends WorkplaceCodeCanonicalization.Require{
	}
	public interface RequireAdjust extends EmployeeHistoryCanonicalization.RequireAdjust{
	}


}
