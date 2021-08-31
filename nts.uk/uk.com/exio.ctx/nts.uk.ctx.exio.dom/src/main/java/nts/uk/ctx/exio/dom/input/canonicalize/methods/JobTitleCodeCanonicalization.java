package nts.uk.ctx.exio.dom.input.canonicalize.methods;

import java.util.Arrays;
import java.util.Optional;

import lombok.Value;
import lombok.val;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.bs.employee.dom.jobtitle.info.JobTitleInfo;
import nts.uk.ctx.exio.dom.input.DataItem;
import nts.uk.ctx.exio.dom.input.canonicalize.CanonicalItem;
import nts.uk.ctx.exio.dom.input.canonicalize.CanonicalItemList;
import nts.uk.ctx.exio.dom.input.errors.RecordError;
import nts.uk.ctx.exio.dom.input.util.Either;
import nts.uk.ctx.exio.dom.input.workspace.domain.DomainWorkspace;
/**
 * 職位コードを職位IDに正準化
 */
@Value
public class JobTitleCodeCanonicalization {
	/** 開始日の項目No */
	private final int itemNoStartDate;
	/** 職位コードの項目No */
	private final int itemNoJobTitleCode;
	/** 職位IDの項目No */
	private final int itemNoJobTitleId;

	public JobTitleCodeCanonicalization(DomainWorkspace workspace) {
		itemNoStartDate = workspace.getItemByName("開始日").getItemNo();
		itemNoJobTitleCode = workspace.getItemByName("職位コード").getItemNo();
		itemNoJobTitleId = workspace.getItemByName("JOB_TITLE_ID").getItemNo();
	}
	/**
	 * 渡された編集済みデータを正準化する
	 * @param require
	 * @param revisedData
	 * @return
	 */
	public Either<RecordError, IntermediateResult> canonicalize(Require require, IntermediateResult revisedData, int csvRowNo) {
		String jobTitleCode = revisedData.getItemByNo(itemNoJobTitleCode).get().getString();
		GeneralDate startDate = revisedData.getItemByNo(itemNoStartDate).get().getDate();

		return getJobTitleId(require, jobTitleCode, startDate, csvRowNo)
				.map(workplaceId -> canonicalize(revisedData, workplaceId))
				.mapLeft(error -> error);
	}

	private static Either<RecordError, String> getJobTitleId(Require require, String jobTitleCode, GeneralDate startDate, int csvRowNo) {
		val jobTitle = require.getJobTitleByCode(jobTitleCode, startDate);
		
		return Either.rightOptional(
				jobTitle.map(e -> e.getJobTitleId()),
				() -> new RecordError(csvRowNo, "未登録の職位コードです。"));
	}

	private IntermediateResult canonicalize(IntermediateResult canonicalizingData, String jobTitleId) {
		return canonicalizingData.addCanonicalized(
				new CanonicalItemList(Arrays.asList(
						CanonicalItem.of(DataItem.of(itemNoJobTitleId, jobTitleId))
					)),
				itemNoJobTitleCode);
	}

	public static interface Require {
		Optional<JobTitleInfo> getJobTitleByCode(String jobTitleCode, GeneralDate startdate);
	}
}
