package nts.uk.ctx.at.record.dom.workrecord.monthcal.export;

import java.util.Optional;

import nts.arc.layer.app.cache.CacheCarrier;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrCompanySettings;
import nts.uk.ctx.at.record.dom.monthlyprocess.aggr.work.MonAggrEmployeeSettings;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.RegularWorkTimeAggrSet;
import nts.uk.ctx.at.record.dom.workrecord.monthcal.export.GetRegularAggrSetImpl.Require;

/**
 * 集計設定の取得（通常勤務）
 * @author shuichu_ishida
 */
public interface GetRegularAggrSet {

//	/**
//	 * 集計設定の取得（通常勤務）
//	 * @param companyId 会社ID
//	 * @param employmentCd 雇用コード
//	 * @param employeeId 社員ID
//	 * @param criteriaDate 基準日
//	 * @return 通常勤務の法定内集計設定
//	 */
//	Optional<RegularWorkTimeAggrSet> get(
//			String companyId, String employmentCd, String employeeId, GeneralDate criteriaDate);

	/**
	 * 集計設定の取得（通常勤務）
	 * @param companyId 会社ID
	 * @param employmentCd 雇用コード
	 * @param employeeId 社員ID
	 * @param criteriaDate 基準日
	 * @param companySets 月別集計で必要な会社別設定
	 * @param employeeSets 月別集計で必要な社員別設定
	 * @return 通常勤務の法定内集計設定
	 */
	Optional<RegularWorkTimeAggrSet> get(
			String companyId, String employmentCd, String employeeId, GeneralDate criteriaDate,
			MonAggrCompanySettings companySets, MonAggrEmployeeSettings employeeSets);
	
	Optional<RegularWorkTimeAggrSet> getRequire(
			Require require, CacheCarrier cacheCarrier,
			String companyId, String employmentCd, String employeeId, GeneralDate criteriaDate,
			MonAggrCompanySettings companySets, MonAggrEmployeeSettings employeeSets);
}
