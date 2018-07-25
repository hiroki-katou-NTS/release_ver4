/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.query.infra.entity.employee;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import nts.arc.layer.infra.data.entity.type.GeneralDateTimeToDBConverter;
import nts.arc.layer.infra.data.entity.type.GeneralDateToDBConverter;
import nts.arc.time.GeneralDate;
import nts.arc.time.GeneralDateTime;
import nts.uk.query.infra.repository.employee.JpaRegulationInfoEmployeeRepository;
import nts.uk.query.model.employee.EmployeeSearchQuery;

/**
 * The Class EmployeeDataView.
 */
@Entity
@Table(name="EMPLOYEE_DATA_VIEW")
@Getter
@Setter
public class EmployeeDataView implements Serializable {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The id. */
	@Id
	@Column(name="ID")
	private String id;

	/** The job seq disp. */
	@Column(name="JOB_SEQ_DISP")
	private String jobSeqDisp;

	/** The abs end date. */
	@Column(name="ABS_END_DATE")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	private GeneralDateTime absEndDate;

	/** The abs str date. */
	@Column(name="ABS_STR_DATE")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	private GeneralDateTime absStrDate;

	/** The business english name. */
	@Column(name="BUSINESS_NAME_KANA")
	private String businessNameKana;

	/** The business english name. */
	@Column(name="BUSINESS_ENGLISH_NAME")
	private String businessEnglishName;

	/** The business name. */
	@Column(name="BUSINESS_NAME")
	private String businessName;

	/** The business other name. */
	@Column(name="BUSINESS_OTHER_NAME")
	private String businessOtherName;

	/** The cid. */
	@Column(name="CID")
	private String cid;

	/** The class end date. */
	@Column(name="CLASS_END_DATE")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	private GeneralDateTime classEndDate;

	/** The class str date. */
	@Column(name="CLASS_STR_DATE")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	private GeneralDateTime classStrDate;

	/** The classification code. */
	@Column(name="CLASSIFICATION_CODE")
	private String classificationCode;

	/** The com end date. */
	@Column(name="COM_END_DATE")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	private GeneralDateTime comEndDate;

	/** The com str date. */
	@Column(name="COM_STR_DATE")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	private GeneralDateTime comStrDate;

	/** The del status atr. */
	@Column(name="DEL_STATUS_ATR")
	private Integer delStatusAtr;

	/** The emp cd. */
	@Column(name="EMP_CD")
	private String empCd;

	/** The employment end date. */
	@Column(name="EMPLOYMENT_END_DATE")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	private GeneralDateTime employmentEndDate;

	/** The employment str date. */
	@Column(name="EMPLOYMENT_STR_DATE")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	private GeneralDateTime employmentStrDate;

	/** The job cd. */
	@Column(name="JOB_CD")
	private String jobCd;

	/** The job end date. */
	@Column(name="JOB_END_DATE")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	private GeneralDateTime jobEndDate;

	/** The job info end date. */
	@Column(name="JOB_INFO_END_DATE")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	private GeneralDateTime jobInfoEndDate;

	/** The job info str date. */
	@Column(name="JOB_INFO_STR_DATE")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	private GeneralDateTime jobInfoStrDate;

	/** The job str date. */
	@Column(name="JOB_STR_DATE")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	private GeneralDateTime jobStrDate;

	/** The job title id. */
	@Column(name="JOB_TITLE_ID")
	private String jobTitleId;

	/** The person name kana. */
	@Column(name="PERSON_NAME_KANA")
	private String personNameKana;

	/** The scd. */
	@Column(name="SCD")
	private String scd;

	/** The sid. */
	@Column(name="SID")
	private String sid;

	/** The temp abs frame no. */
	@Column(name="TEMP_ABS_FRAME_NO")
	private Integer tempAbsFrameNo;

	/** The work type cd. */
	@Column(name="WORK_TYPE_CD")
	private String workTypeCd;

	/** The work type end date. */
	@Column(name="WORK_TYPE_END_DATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate workTypeEndDate;

	/** The work type str date. */
	@Column(name="WORK_TYPE_STR_DATE")
	@Convert(converter = GeneralDateToDBConverter.class)
	private GeneralDate workTypeStrDate;

	/** The workplace id. */
	@Column(name="WORKPLACE_ID")
	private String workplaceId;

	/** The wpl cd. */
	@Column(name="WPL_CD")
	private String wplCd;

	/** The wpl end date. */
	@Column(name="WPL_END_DATE")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	private GeneralDateTime wplEndDate;

	/** The wpl info end date. */
	@Column(name="WPL_INFO_END_DATE")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	private GeneralDateTime wplInfoEndDate;

	/** The wpl info str date. */
	@Column(name="WPL_INFO_STR_DATE")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	private GeneralDateTime wplInfoStrDate;

	/** The wkp conf end date. */
	@Column(name="WKP_CONF_END_DT")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	private GeneralDateTime wkpConfEndDate;
	
	/** The wkp conf str date. */
	@Column(name="WKP_CONF_STR_DT")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	private GeneralDateTime wkpConfStrDate;

	/** The wpl hierarchy code. */
	@Column(name="WKP_HIERARCHY_CD")
	private String wplHierarchyCode;

	/** The wpl name. */
	@Column(name="WPL_NAME")
	private String wplName;

	/** The wpl str date. */
	@Column(name="WPL_STR_DATE")
	@Convert(converter = GeneralDateTimeToDBConverter.class)
	private GeneralDateTime wplStrDate;

	/** The closure id. */
	@Column(name="CLOSURE_ID")
	private Integer closureId;

	/**
	 * Instantiates a new employee data view.
	 */
	public EmployeeDataView() {
		super();
	}

	public boolean isIncluded(EmployeeSearchQuery paramQuery) {
		GeneralDateTime retireStart = paramQuery.getRetireStart() == null ? paramQuery.getPeriodStart()
				: paramQuery.getRetireStart();
		GeneralDateTime retireEnd = paramQuery.getRetireEnd() == null ? paramQuery.getPeriodEnd()
				: paramQuery.getRetireEnd();
		GeneralDateTime start = paramQuery.getPeriodStart();
		GeneralDateTime end = paramQuery.getPeriodEnd();
		
		// check employee in company or not.
		if (this.isNotInCompany(paramQuery.getPeriodStart(), paramQuery.getPeriodEnd())) {
			return false;
		}
		
		boolean isIncludeIncumbents = paramQuery.getIncludeIncumbents() && this.isWorking(start, end);
		boolean isIncludeWorkersOnLeave = paramQuery.getIncludeWorkersOnLeave() && this.isWorkersOnLeave(start, end);
		boolean isIncludeOccupancy = paramQuery.getIncludeOccupancy() && this.isOccupancy(start, end);
		boolean isIncludeRetirees = paramQuery.getIncludeRetirees() && this.isRetire(retireStart, retireEnd);
		
		return isIncludeIncumbents || isIncludeWorkersOnLeave || isIncludeOccupancy || isIncludeRetirees;
	}
	
	public boolean isNotInCompany(GeneralDateTime start, GeneralDateTime end) {
		return this.comStrDate.after(end) || this.comEndDate.before(start);
	}

	private boolean isWorking(GeneralDateTime start, GeneralDateTime end) {
		boolean isWorking = this.tempAbsFrameNo == null && this.absStrDate == null;
		return isWorking || this.absStrDate.after(end) || this.absEndDate.before(start);
	}

	public boolean isWorkersOnLeave(GeneralDateTime start, GeneralDateTime end) {
		return !this.isWorking(start, end)
				&& this.tempAbsFrameNo == JpaRegulationInfoEmployeeRepository.LEAVE_ABSENCE_QUOTA_NO;
	}

	public boolean isOccupancy(GeneralDateTime start, GeneralDateTime end) {
		return !this.isWorking(start, end)
				&& this.tempAbsFrameNo != JpaRegulationInfoEmployeeRepository.LEAVE_ABSENCE_QUOTA_NO;
	}

	public boolean isRetire(GeneralDateTime start, GeneralDateTime end) {
		return this.comEndDate.afterOrEquals(start) && this.comEndDate.beforeOrEquals(end);
	}
	
}
