package nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import nts.uk.ctx.at.shared.dom.remainingnumber.absencerecruitment.export.query.OccurrenceDigClass;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.AccumulationAbsenceDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.LeaveOccurrDetail;
import nts.uk.ctx.at.shared.dom.remainingnumber.breakdayoffmng.export.query.numberremainrange.param.SeqVacationAssociationInfo;
import nts.uk.ctx.at.shared.dom.vacation.algorithm.TimeLapseVacationSetting;

/**
 * @author ThanhNX
 *
 *         時系列順で相殺する
 */
public class OffsetChronologicalOrder {

	private OffsetChronologicalOrder() {
	};

	// 時系列順で相殺する
	public static List<SeqVacationAssociationInfo> process(Require require, String employeeId,
			List<TimeLapseVacationSetting> lstTimeLap, List<AccumulationAbsenceDetail> lstAccAbse,
			TypeOffsetJudgment typeJudgment) {

		List<SeqVacationAssociationInfo> lstSeqVacation = new ArrayList<>();
		// INPUT．「逐次発生の休暇明細」．発生消化区分により、発生と消化で別ける

		List<AccumulationAbsenceDetail> lstAccdigest = lstAccAbse.stream()
				.filter(x -> x.getOccurrentClass() == OccurrenceDigClass.DIGESTION).collect(Collectors.toList());

		List<AccumulationAbsenceDetail> lstAccOccur = lstAccAbse.stream()
				.filter(x -> x.getOccurrentClass() == OccurrenceDigClass.OCCURRENCE).collect(Collectors.toList());

		// 「逐次発生の休暇明細」(消化)でループする
		for (AccumulationAbsenceDetail accAbsence : lstAccdigest) {

			if (!accAbsence.getDateOccur().getDayoffDate().isPresent())
				continue;

			// 逐次発生の休暇明細（消化）.年月日が期間に含まれる逐次発生の休暇設定を取得
			TimeLapseVacationSetting timeLapSet = lstTimeLap.stream()
					.filter(x -> accAbsence.getDateOccur().getDayoffDate().get().afterOrEquals(x.getPeriod().start())
							&& accAbsence.getDateOccur().getDayoffDate().get().beforeOrEquals(x.getPeriod().end()))
					.findFirst().orElse(null);
			if (timeLapSet == null)
				continue;
			// ループ中の「逐次発生の休暇明細」（消化）．未相殺数をチェックする
			if (checkUnbalNum(timeLapSet, accAbsence))
				continue;

			// 逐次発生の休暇明細（消化）.年月日が期間に含まれる逐次発生の休暇設定を取得
			// 「逐次発生の休暇明細」(発生)でループする
			for (AccumulationAbsenceDetail occur : lstAccOccur) {
				Pair<OffsetJudgment, Optional<SeqVacationAssociationInfo>> offsetJudgment = offsetJudgment(timeLapSet,
						accAbsence, occur, typeJudgment);
				if (offsetJudgment.getRight().isPresent())
					lstSeqVacation.add(offsetJudgment.getRight().get());
				if (offsetJudgment.getLeft() == OffsetJudgment.ERROR)
					break;
				else {
					// 「逐次発生の休暇明細」（消化）.未相殺数 > 0
					if (checkUnbalNum(timeLapSet, accAbsence)) {
						break;
					} else {
						continue;
					}
				}
			}

			// 相殺できる「発生数」があるかチェックする
			if (!lstAccOccur.stream().filter(x -> !x.getUnbalanceNumber().allFieldZero()).findAny().isPresent()) {
				break;
			}

		}
		return lstSeqVacation;

	}

	// 相殺判定
	private static Pair<OffsetJudgment, Optional<SeqVacationAssociationInfo>> offsetJudgment(
			TimeLapseVacationSetting timeLapVacationSetting, AccumulationAbsenceDetail accdigest,
			AccumulationAbsenceDetail occur, TypeOffsetJudgment typeJudgment) {

		// 期限切れかをチェックする
		Optional<? extends LeaveOccurrDetail> occurrDetail = occurrDetail(occur, typeJudgment);
		if (!occurrDetail.isPresent() || !accdigest.getDateOccur().getDayoffDate().isPresent()
				|| occurrDetail.get().getDeadline().before(accdigest.getDateOccur().getDayoffDate().get())) {
			return Pair.of(OffsetJudgment.SUCCESS, Optional.empty());
		}

		// 逐次発生の休暇明細（発生）.休暇数をチェックする
		if (checkUnbalNum(timeLapVacationSetting, occur)) {
			return Pair.of(OffsetJudgment.SUCCESS, Optional.empty());

		}

		if (!occur.getDateOccur().getDayoffDate().isPresent()) {
			return Pair.of(OffsetJudgment.SUCCESS, Optional.empty());
		}
		// 先取りをできるか
		if (!timeLapVacationSetting.isReceivAdvance()
				&& accdigest.getDateOccur().getDayoffDate().get().before(occur.getDateOccur().getDayoffDate().get())) {
			// 先取り制限エラーを代休集計結果.エラーメッセージに追加
			return Pair.of(OffsetJudgment.ERROR, Optional.empty());
		}

		// 紐づけ登録処理
		Optional<SeqVacationAssociationInfo> seqVacation = TypeRegistrationProcess.process(timeLapVacationSetting,
				occur.getDateOccur().getDayoffDate().get(), accdigest.getDateOccur().getDayoffDate().get(),
				accdigest.getUnbalanceNumber().getDay(), typeJudgment);

		// 未相殺数を更新 in process 振休
		UpdateUnbalancedNumber.updateUnbalanced(timeLapVacationSetting, accdigest, occur, typeJudgment);

		return Pair.of(OffsetJudgment.SUCCESS, seqVacation);

	}

	// 期限切れかをチェックする
	private static Optional<? extends LeaveOccurrDetail> occurrDetail(AccumulationAbsenceDetail occur,
			TypeOffsetJudgment typeJudgment) {
		if (typeJudgment == TypeOffsetJudgment.ABSENCE) {
			return occur.getUnbalanceCompensation();
		} else {
			return occur.getUnbalanceVacation();
		}

	}

	private static boolean checkUnbalNum(TimeLapseVacationSetting timeLapSet, AccumulationAbsenceDetail accAbsence) {

		// 逐次発生休暇設定.時間管理区分 = true
		// 逐次発生の休暇明細（消化）.未相殺数.時間 ＞０
		// 逐次発生休暇設定.時間管理区分 = false
		// 逐次発生の休暇明細（消化）.未相殺数.日数 ＞０
		if ((!timeLapSet.getManagerTimeCate().isPresent() || !timeLapSet.getManagerTimeCate().get())
				&& accAbsence.getUnbalanceNumber().getDay().v() <= 0) {
			return true;
		} else if (timeLapSet.getManagerTimeCate().isPresent() && timeLapSet.getManagerTimeCate().get()
				&& (!accAbsence.getUnbalanceNumber().getTime().isPresent()
						|| accAbsence.getUnbalanceNumber().getTime().get().v() <= 0)) {
			return true;
		}
		return false;

	}

	public static interface Require {

	}
}
