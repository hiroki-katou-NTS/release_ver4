package nts.uk.ctx.basic.dom.company.useset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nts.arc.layer.dom.DomainObject;

/**
 * @author lanlt
 *
 */
@AllArgsConstructor
public class CompanyUseSet extends DomainObject {
	/** グループ会社権限 */
	@Getter
	private UseSet use_Gr_Set;

	/** 就業権限 */
	@Getter
	private UseSet use_Kt_Set;

	/** 給与権限 */
	@Getter
	private UseSet use_Qy_Set;
	/** 人事権限 */
	@Getter
	private UseSet use_Jj_Set;
	/** 会計権限 */
	@Getter
	private UseSet use_Ac_Set;
	/** グループウェア権限 */
	@Getter
	private UseSet use_Gw_Set;
	/** ヘルスケア権限 */
	@Getter
	private UseSet use_Hc_Set;
	/** 労務コスト権限 */
	@Getter
	private UseSet use_Lc_Set;
	/** BI権限 */
	@Getter
	private UseSet use_Bi_Set;
	/** 権限予備(Reserve） */
	@Getter
	private UseSet use_Rs01_Set;
	/** 権限予備(Reserve） */
	@Getter
	private UseSet use_Rs02_Set;
	/** 権限予備(Reserve） */
	@Getter
	private UseSet use_Rs03_Set;
	/** 権限予備(Reserve） */
	@Getter
	private UseSet use_Rs04_Set;
	/** 権限予備(Reserve） */
	@Getter
	private UseSet use_Rs05_Set;
	/** 権限予備(Reserve） */
	@Getter
	private UseSet use_Rs06_Set;
	/** 権限予備(Reserve） */
	@Getter
	private UseSet use_Rs07_Set;
	/** 権限予備(Reserve） */
	@Getter
	private UseSet use_Rs08_Set;
	/** 権限予備(Reserve） */
	@Getter
	private UseSet use_Rs09_Set;
	/** 権限予備(Reserve） */
	@Getter
	private UseSet use_Rs10_Set;

}
