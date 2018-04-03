package nts.uk.ctx.pereg.app.find.processor;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.enterprise.inject.spi.CDI;
import javax.enterprise.util.TypeLiteral;

import find.person.contact.PersonContactDto;
import find.person.info.PersonDto;
import nts.uk.ctx.at.record.app.find.dailyperformanceformat.businesstype.BusinessTypeDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.annualleave.AnnualLeaveDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave10informationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave11informationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave12informationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave13informationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave14informationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave15informationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave16informationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave17informationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave18informationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave19informationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave1InformationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave20informationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave2informationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave3informationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave4informationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave5informationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave6informationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave7informationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave8informationDto;
import nts.uk.ctx.at.record.app.find.remainingnumber.empinfo.basicinfo.Specialleave9informationDto;
import nts.uk.ctx.at.shared.app.find.shortworktime.ShortWorkTimeDto;
import nts.uk.ctx.at.shared.app.find.workingcondition.WorkingConditionDto;
import nts.uk.ctx.bs.employee.app.find.classification.affiliate.AffClassificationDto;
import nts.uk.ctx.bs.employee.app.find.department.affiliate.AffDeptHistDto;
import nts.uk.ctx.bs.employee.app.find.employee.contact.EmpInfoContactDto;
import nts.uk.ctx.bs.employee.app.find.employee.history.AffCompanyHistInfoDto;
import nts.uk.ctx.bs.employee.app.find.employee.mngdata.EmployeeDataMngInfoDto;
import nts.uk.ctx.bs.employee.app.find.jobtitle.affiliate.AffJobTitleDto;
import nts.uk.ctx.bs.employee.app.find.temporaryabsence.TempAbsHisItemDto;
import nts.uk.ctx.bs.employee.app.find.workplace.affiliate.AffWorlplaceHistItemDto;
import nts.uk.ctx.pereg.app.find.employment.history.EmploymentHistoryDto;
import nts.uk.shr.pereg.app.find.PeregFinder;
import nts.uk.ctx.at.record.app.find.remainingnumber.otherhdinfo.OtherHolidayInfoDto;

@Stateless
@SuppressWarnings("serial")
public class PeregLayoutingProcessorCollectorImpl implements PeregFinderProcessorCollector {

	/** ctg single finder */
	private static final List<TypeLiteral<?>> FINDER_CTG_SINGLE_HANDLER_CLASSES = Arrays.asList(
			// CS00001  社員データ管理
			new TypeLiteral<PeregFinder<EmployeeDataMngInfoDto>>(){},
			// CS00002 個人基本情報
			new TypeLiteral<PeregFinder<PersonDto>>(){},
			// CS00003 所属会社履歴
			new TypeLiteral<PeregFinder<AffCompanyHistInfoDto>>(){},
			// CS00004 分類１
			new TypeLiteral<PeregFinder<AffClassificationDto>>(){},
			// CS00014 雇用
			new TypeLiteral<PeregFinder<EmploymentHistoryDto>>(){},
			// CS00015 部門本務	
			new TypeLiteral<PeregFinder<AffDeptHistDto>>(){},
			// CS00016 職位本務
			new TypeLiteral<PeregFinder<AffJobTitleDto>>(){},
			// CS00017 職場
			new TypeLiteral<PeregFinder<AffWorlplaceHistItemDto>>(){},
			// CS00018 休職休業
			new TypeLiteral<PeregFinder<TempAbsHisItemDto>>(){},
			// CS00019 短時間勤務
			new TypeLiteral<PeregFinder<ShortWorkTimeDto>>(){},
			// CS00020 労働条件
			new TypeLiteral<PeregFinder<WorkingConditionDto>>(){},
			// CS00021 勤務種別
			new TypeLiteral<PeregFinder<BusinessTypeDto>>(){},
			// CS00022 個人連絡先
			new TypeLiteral<PeregFinder<PersonContactDto>>(){},
			// CS00023 社員連絡先
			new TypeLiteral<PeregFinder<EmpInfoContactDto>>(){},
			// CS00024 年休情報
			new TypeLiteral<PeregFinder<AnnualLeaveDto>>(){},
			// CS00025
			new TypeLiteral<PeregFinder<Specialleave1InformationDto>>(){},
			// CS00026
			new TypeLiteral<PeregFinder<Specialleave2informationDto>>(){},
			// CS00027
			new TypeLiteral<PeregFinder<Specialleave3informationDto>>(){},
			// CS00028
			new TypeLiteral<PeregFinder<Specialleave4informationDto>>(){},
			// CS00029
			new TypeLiteral<PeregFinder<Specialleave5informationDto>>(){},
			// CS00030
			new TypeLiteral<PeregFinder<Specialleave6informationDto>>(){},
			// CS00031
			new TypeLiteral<PeregFinder<Specialleave7informationDto>>(){},
			// CS00032
			new TypeLiteral<PeregFinder<Specialleave8informationDto>>(){},
			// CS00033
			new TypeLiteral<PeregFinder<Specialleave9informationDto>>(){},
			// CS00034
			new TypeLiteral<PeregFinder<Specialleave10informationDto>>(){},
			// CS00049
			new TypeLiteral<PeregFinder<Specialleave11informationDto>>(){},
			// CS00050
			new TypeLiteral<PeregFinder<Specialleave12informationDto>>(){},
			// CS00051
			new TypeLiteral<PeregFinder<Specialleave13informationDto>>(){},
			// CS00052
			new TypeLiteral<PeregFinder<Specialleave14informationDto>>(){},
			// CS00053
			new TypeLiteral<PeregFinder<Specialleave15informationDto>>(){},
			// CS00054
			new TypeLiteral<PeregFinder<Specialleave16informationDto>>(){},
			// CS00055
			new TypeLiteral<PeregFinder<Specialleave17informationDto>>(){},
			// CS00056
			new TypeLiteral<PeregFinder<Specialleave18informationDto>>(){},
			// CS00057
			new TypeLiteral<PeregFinder<Specialleave19informationDto>>(){},
			// CS00058
			new TypeLiteral<PeregFinder<Specialleave20informationDto>>(){},
			// CS00037
			new TypeLiteral<PeregFinder<OtherHolidayInfoDto>>(){}
			);

	@Override
	public Set<PeregFinder<?>> peregFinderCollect() {
		return FINDER_CTG_SINGLE_HANDLER_CLASSES.stream().map(type -> CDI.current().select(type).get())
				.map(obj -> (PeregFinder<?>) obj).collect(Collectors.toSet());
	}

}
