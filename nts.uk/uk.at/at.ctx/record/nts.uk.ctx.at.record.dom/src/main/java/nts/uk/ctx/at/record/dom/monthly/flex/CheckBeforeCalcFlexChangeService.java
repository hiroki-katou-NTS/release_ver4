package nts.uk.ctx.at.record.dom.monthly.flex;

public interface CheckBeforeCalcFlexChangeService {
	// 社員のフレックス繰越上限時間を求める
	ConditionCalcResult getConditionCalcFlex(String companyId, CalcFlexChangeDto calc);
}
