package nts.uk.ctx.hr.develop.app.databeforereflecting.retirementinformation.find;

import java.util.List;
import java.util.Optional;

import javax.ejb.Stateless;
import javax.inject.Inject;

import nts.arc.error.BusinessException;
import nts.arc.time.GeneralDate;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.MandatoryRetirementRegulation;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.RetirePlanCource;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.algorithm.mandatoryRetirementRegulation.MandatoryRetirementRegulationService;
import nts.uk.ctx.hr.develop.dom.announcement.mandatoryretirement.algorithm.retirePlanCource.RetirePlanCourceService;
import nts.uk.ctx.hr.develop.dom.empregulationhistory.algorithm.EmploymentRegulationHistoryInterface;
import nts.uk.ctx.hr.shared.dom.employment.EmploymentInfoImport;
import nts.uk.ctx.hr.shared.dom.employment.SyEmploymentAdaptor;

@Stateless
public class RetirementInformationFinder {

	@Inject
	private EmploymentRegulationHistoryInterface empHis;

	@Inject
	private MandatoryRetirementRegulationService medaRepo;

	@Inject
	private RetirePlanCourceService retiRepo;

	@Inject
	private SyEmploymentAdaptor sysEmp;

	public void startPage(String cId, GeneralDate baseDate) {
		// アルゴリズム[定年退職設定の取得]を実行する(thực hiện thuật toán [lấy setting nghỉ hưu])

		getRetirementAgeSetting(cId, baseDate);
	}

	private void getRetirementAgeSetting(String cId, GeneralDate baseDate) {
		// アルゴリズム[基準日で選択されている定年退職コースの取得]を実行する(Thực hiện thuật toán [lấy
		// RetirementCourse đang chọn ở BaseDate])
		getRetirementCourse(cId, baseDate);
	}

	private void getRetirementCourse(String cId, GeneralDate baseDate) {
		// アルゴリズム [基準日から就業規則の履歴IDの取得] を実行する((Thực hiện thuật toán[Lấy
		// EmploymentRegulationHistory ID từ baseDate])
		Optional<String> hisIdOpt = this.empHis.getHistoryIdByDate(cId, baseDate);

		if (!hisIdOpt.isPresent()) {
			throw new BusinessException("MsgJ_JMM018_2");
		}

		// ドメインモデル [定年退職の就業規則] を取得する (Lấy domain
		// [MandatoryRetirementRegulation])
		MandatoryRetirementRegulation mada = this.medaRepo.getMandatoryRetirementRegulation(cId, hisIdOpt.get());
		if (mada == null) {
			throw new BusinessException("MsgJ_JMM018_2");
		}

		// アルゴリズム [全ての定年退職コースの取得] を実行する (Thực hiện thuật toán "Lấy tất cả
		// retirePlanCourse")

		List<RetirePlanCource> retis = this.retiRepo.getRetirePlanCourse(cId);

		if (retis.isEmpty()) {
			throw new BusinessException("MsgJ_JMM018_6");
		}

		List<EmploymentInfoImport> empInfos = this.sysEmp.getEmploymentInfo(cId, Optional.of(true), Optional.of(false),
				Optional.of(false), Optional.of(false), Optional.of(true));

		if (empInfos.isEmpty()) {
			throw new BusinessException(" MsgJ_JMM018_10");
		}

	}
}
