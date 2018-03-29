/**
 * 5:09:42 PM Jul 24, 2017
 */
package nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.ErrorAlarmWorkRecord;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.ErrorAlarmCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.attendanceitem.ErAlAttendanceItemCondition;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktime.PlanActualWorkTime;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktime.SingleWorkTime;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktype.PlanActualWorkType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.condition.worktype.SingleWorkType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConditionAtr;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.ConditionType;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.enums.FilterByCompare;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.AttendanceItemId;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedAmountValue;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimeDuration;
import nts.uk.ctx.at.record.dom.workrecord.erroralarm.primitivevalue.CheckedTimesValue;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.KrcmtErAlCondition;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.KrcstErAlApplication;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.KrcstErAlApplicationPK;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.KrcstErAlBusinessType;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.KrcstErAlBusinessTypePK;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.KrcstErAlClass;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.KrcstErAlClassPK;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.KrcstErAlEmployment;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.KrcstErAlEmploymentPK;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.KrcstErAlJobTitle;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.KrcstErAlJobTitlePK;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.attendanceitem.KrcmtErAlAtdItemCon;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.attendanceitem.KrcmtErAlAtdItemConPK;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.attendanceitem.KrcstErAlAtdTarget;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.attendanceitem.KrcstErAlAtdTargetPK;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.attendanceitem.KrcstErAlCompareRange;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.attendanceitem.KrcstErAlCompareRangePK;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.attendanceitem.KrcstErAlCompareSingle;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.attendanceitem.KrcstErAlCompareSinglePK;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.attendanceitem.KrcstErAlConGroup;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.attendanceitem.KrcstErAlSingleAtd;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.attendanceitem.KrcstErAlSingleAtdPK;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.attendanceitem.KrcstErAlSingleFixed;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.attendanceitem.KrcstErAlSingleFixedPK;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.worktime.KrcstErAlWhActual;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.worktime.KrcstErAlWhPlan;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.worktime.KrcstErAlWhPlanActualPK;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.worktype.KrcstErAlWtActual;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.worktype.KrcstErAlWtPlan;
import nts.uk.ctx.at.record.infra.entity.workrecord.erroralarm.condition.worktype.KrcstErAlWtPlanActualPK;
import nts.uk.shr.com.context.AppContexts;
import nts.uk.shr.com.time.TimeWithDayAttr;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

/**
 * @author hungnm
 *
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KRCMT_ERAL_SET")
public class KwrmtErAlWorkRecord extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KwrmtErAlWorkRecordPK kwrmtErAlWorkRecordPK;

	@Column(name = "ERROR_ALARM_NAME")
	public String errorAlarmName;

	@Column(name = "FIXED_ATR")
	public int fixedAtr;

	@Column(name = "USE_ATR")
	public int useAtr;

	@Column(name = "ERAL_ATR")
	public int typeAtr;

	@Column(name = "BOLD_ATR")
	public int boldAtr;

	@Column(name = "MESSAGE_COLOR")
	public String messageColor;

	@Column(name = "CANCELABLE_ATR")
	public int cancelableAtr;

	@Column(name = "ERROR_DISPLAY_ITEM")
	public Integer errorDisplayItem;

	@Basic(optional = true)
	@Column(name = "ERAL_CHECK_ID")
	public String eralCheckId;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, optional = true)
	@JoinColumn(name = "ERAL_CHECK_ID", referencedColumnName = "ERAL_CHECK_ID", insertable = false, updatable = false)
	public KrcmtErAlCondition krcmtErAlCondition;

	@OneToMany(mappedBy = "kwrmtErAlWorkRecord", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
	public List<KrcstErAlApplication> krcstErAlApplication;

	@Column(name = "CANCEL_ROLE_ID")
	public String cancelRoleId;

	@Override
	protected Object getKey() {
		return this.kwrmtErAlWorkRecordPK;
	}

	@SuppressWarnings("unchecked")
	private static <V> ErAlAttendanceItemCondition<V> convertKrcmtErAlAtdItemConToDomain(KwrmtErAlWorkRecord entity,
			KrcmtErAlAtdItemCon atdItemCon) {
		ErAlAttendanceItemCondition<V> atdItemConDomain = new ErAlAttendanceItemCondition<V>(
				entity.kwrmtErAlWorkRecordPK.companyId, entity.kwrmtErAlWorkRecordPK.errorAlarmCode,
				atdItemCon.krcmtErAlAtdItemConPK.atdItemConNo, atdItemCon.conditionAtr, atdItemCon.useAtr == 1);
		// Set Target
		if (atdItemCon.conditionAtr == ConditionAtr.TIME_WITH_DAY.value) {
			atdItemConDomain.setUncountableTarget(Optional.ofNullable(atdItemCon.lstAtdItemTarget)
					.orElse(Collections.emptyList()).stream().filter(atdItemTarget -> atdItemTarget.targetAtr == 2)
					.findFirst().get().krcstErAlAtdTargetPK.attendanceItemId);
		} else {
			atdItemConDomain.setCountableTarget(
					Optional.ofNullable(atdItemCon.lstAtdItemTarget).orElse(Collections.emptyList()).stream()
							.filter(atdItemTarget -> atdItemTarget.targetAtr == 0)
							.map(addItem -> addItem.krcstErAlAtdTargetPK.attendanceItemId).collect(Collectors.toList()),
					Optional.ofNullable(atdItemCon.lstAtdItemTarget).orElse(Collections.emptyList()).stream()
							.filter(atdItemTarget -> atdItemTarget.targetAtr == 1)
							.map(addItem -> addItem.krcstErAlAtdTargetPK.attendanceItemId)
							.collect(Collectors.toList()));
		}
		// Set Compare
		if (atdItemCon.erAlCompareRange != null) {
			if (atdItemCon.conditionAtr == ConditionAtr.AMOUNT_VALUE.value) {
				atdItemConDomain.setCompareRange(atdItemCon.erAlCompareRange.compareAtr,
						(V) new CheckedAmountValue(atdItemCon.erAlCompareRange.startValue),
						(V) new CheckedAmountValue(atdItemCon.erAlCompareRange.endValue));
			} else if (atdItemCon.conditionAtr == ConditionAtr.TIME_DURATION.value) {
				atdItemConDomain.setCompareRange(atdItemCon.erAlCompareRange.compareAtr,
						(V) new CheckedTimeDuration(atdItemCon.erAlCompareRange.startValue),
						(V) new CheckedTimeDuration(atdItemCon.erAlCompareRange.endValue));
			} else if (atdItemCon.conditionAtr == ConditionAtr.TIME_WITH_DAY.value) {
				atdItemConDomain.setCompareRange(atdItemCon.erAlCompareRange.compareAtr,
						(V) new TimeWithDayAttr(atdItemCon.erAlCompareRange.startValue),
						(V) new TimeWithDayAttr(atdItemCon.erAlCompareRange.endValue));
			} else if (atdItemCon.conditionAtr == ConditionAtr.TIMES.value) {
				atdItemConDomain.setCompareRange(atdItemCon.erAlCompareRange.compareAtr,
						(V) new CheckedTimesValue(atdItemCon.erAlCompareRange.startValue),
						(V) new CheckedTimesValue(atdItemCon.erAlCompareRange.endValue));
			}
		} else if (atdItemCon.erAlCompareSingle != null) {
			if (atdItemCon.erAlCompareSingle.conditionType == ConditionType.FIXED_VALUE.value) {
				if (atdItemCon.conditionAtr == ConditionAtr.AMOUNT_VALUE.value) {
					atdItemConDomain.setCompareSingleValue(atdItemCon.erAlCompareSingle.compareAtr,
							atdItemCon.erAlCompareSingle.conditionType,
							(V) new CheckedAmountValue(atdItemCon.erAlSingleFixed.fixedValue));
				} else if (atdItemCon.conditionAtr == ConditionAtr.TIME_DURATION.value) {
					atdItemConDomain.setCompareSingleValue(atdItemCon.erAlCompareSingle.compareAtr,
							atdItemCon.erAlCompareSingle.conditionType,
							(V) new CheckedTimeDuration(atdItemCon.erAlSingleFixed.fixedValue));
				} else if (atdItemCon.conditionAtr == ConditionAtr.TIME_WITH_DAY.value) {
					atdItemConDomain.setCompareSingleValue(atdItemCon.erAlCompareSingle.compareAtr,
							atdItemCon.erAlCompareSingle.conditionType,
							(V) new TimeWithDayAttr(atdItemCon.erAlSingleFixed.fixedValue));
				} else if (atdItemCon.conditionAtr == ConditionAtr.TIMES.value) {
					atdItemConDomain.setCompareSingleValue(atdItemCon.erAlCompareSingle.compareAtr,
							atdItemCon.erAlCompareSingle.conditionType,
							(V) new CheckedTimesValue(atdItemCon.erAlSingleFixed.fixedValue));
				}
			} else {
				atdItemConDomain.setCompareSingleValue(atdItemCon.erAlCompareSingle.compareAtr,
						atdItemCon.erAlCompareSingle.conditionType, (V) new AttendanceItemId(
								atdItemCon.erAlSingleAtd.get(0).krcstEralSingleAtdPK.attendanceItemId));
			}
		}
		return atdItemConDomain;
	}

	private static KrcmtErAlAtdItemCon getKrcmtErAlAtdItemConFromDomain(String atdItemConditionGroup1,
			ErAlAttendanceItemCondition<?> erAlAtdItemCon) {
		KrcmtErAlAtdItemConPK krcmtErAlAtdItemConPK = new KrcmtErAlAtdItemConPK(atdItemConditionGroup1,
				(erAlAtdItemCon.getTargetNO()));
		List<KrcstErAlAtdTarget> lstAtdItemTarget = new ArrayList<>();
		if (erAlAtdItemCon.getConditionAtr() == ConditionAtr.TIME_WITH_DAY) {
			lstAtdItemTarget.add(new KrcstErAlAtdTarget(new KrcstErAlAtdTargetPK(atdItemConditionGroup1,
					(erAlAtdItemCon.getTargetNO()), (erAlAtdItemCon.getUncountableTarget().getAttendanceItem())), 2));
		} else {
			List<KrcstErAlAtdTarget> lstAtdItemTargetAdd = erAlAtdItemCon.getCountableTarget()
					.getAddSubAttendanceItems().getAdditionAttendanceItems().stream()
					.map(atdItemId -> new KrcstErAlAtdTarget(new KrcstErAlAtdTargetPK(atdItemConditionGroup1,
							(erAlAtdItemCon.getTargetNO()), (atdItemId)), 0))
					.collect(Collectors.toList());
			List<KrcstErAlAtdTarget> lstAtdItemTargetSub = erAlAtdItemCon.getCountableTarget()
					.getAddSubAttendanceItems().getSubstractionAttendanceItems().stream()
					.map(atdItemId -> new KrcstErAlAtdTarget(new KrcstErAlAtdTargetPK(atdItemConditionGroup1,
							(erAlAtdItemCon.getTargetNO()), (atdItemId)), 1))
					.collect(Collectors.toList());
			lstAtdItemTarget.addAll(lstAtdItemTargetAdd);
			lstAtdItemTarget.addAll(lstAtdItemTargetSub);
		}
		int compareAtr = 0;
		int conditionType = 0;
		int startValue = 0;
		int endValue = 0;
		KrcstErAlCompareSingle erAlCompareSingle = null;
		KrcstErAlCompareRange erAlCompareRange = null;
		KrcstErAlSingleFixed erAlSingleFixed = null;
		List<KrcstErAlSingleAtd> erAlSingleAtd = new ArrayList<>();
		if (erAlAtdItemCon.getCompareRange() != null) {
			compareAtr = erAlAtdItemCon.getCompareRange().getCompareOperator().value;
			if (erAlAtdItemCon.getConditionAtr() == ConditionAtr.AMOUNT_VALUE) {
				startValue = ((CheckedAmountValue) erAlAtdItemCon.getCompareRange().getStartValue()).v();
				endValue = ((CheckedAmountValue) erAlAtdItemCon.getCompareRange().getEndValue()).v();
			} else if (erAlAtdItemCon.getConditionAtr() == ConditionAtr.TIME_DURATION) {
				startValue = ((CheckedTimeDuration) erAlAtdItemCon.getCompareRange().getStartValue()).v();
				endValue = ((CheckedTimeDuration) erAlAtdItemCon.getCompareRange().getEndValue()).v();
			} else if (erAlAtdItemCon.getConditionAtr() == ConditionAtr.TIME_WITH_DAY) {
				startValue = ((TimeWithDayAttr) erAlAtdItemCon.getCompareRange().getStartValue()).v();
				endValue = ((TimeWithDayAttr) erAlAtdItemCon.getCompareRange().getEndValue()).v();
			} else if (erAlAtdItemCon.getConditionAtr() == ConditionAtr.TIMES) {
				startValue = ((CheckedTimesValue) erAlAtdItemCon.getCompareRange().getStartValue()).v();
				endValue = ((CheckedTimesValue) erAlAtdItemCon.getCompareRange().getEndValue()).v();
			}
			erAlCompareRange = new KrcstErAlCompareRange(
					new KrcstErAlCompareRangePK(atdItemConditionGroup1, (erAlAtdItemCon.getTargetNO())), compareAtr,
					startValue, endValue);
		} else if (erAlAtdItemCon.getCompareSingleValue() != null) {
			compareAtr = erAlAtdItemCon.getCompareSingleValue().getCompareOpertor().value;
			conditionType = erAlAtdItemCon.getCompareSingleValue().getConditionType().value;
			erAlCompareSingle = new KrcstErAlCompareSingle(
					new KrcstErAlCompareSinglePK(atdItemConditionGroup1, (erAlAtdItemCon.getTargetNO())), compareAtr,
					conditionType);
			if (erAlAtdItemCon.getCompareSingleValue().getConditionType() == ConditionType.FIXED_VALUE) {
				int fixedValue = 0;
				if (erAlAtdItemCon.getConditionAtr() == ConditionAtr.AMOUNT_VALUE) {
					fixedValue = ((CheckedAmountValue) erAlAtdItemCon.getCompareSingleValue().getValue()).v();
				} else if (erAlAtdItemCon.getConditionAtr() == ConditionAtr.TIME_DURATION) {
					fixedValue = ((CheckedTimeDuration) erAlAtdItemCon.getCompareSingleValue().getValue()).v();
				} else if (erAlAtdItemCon.getConditionAtr() == ConditionAtr.TIME_WITH_DAY) {
					fixedValue = ((TimeWithDayAttr) erAlAtdItemCon.getCompareSingleValue().getValue()).v();
				} else if (erAlAtdItemCon.getConditionAtr() == ConditionAtr.TIMES) {
					fixedValue = ((CheckedTimesValue) erAlAtdItemCon.getCompareSingleValue().getValue()).v();
				}
				erAlSingleFixed = new KrcstErAlSingleFixed(
						new KrcstErAlSingleFixedPK(atdItemConditionGroup1, erAlAtdItemCon.getTargetNO()), fixedValue);
			} else {
				erAlSingleAtd
						.add(new KrcstErAlSingleAtd(
								new KrcstErAlSingleAtdPK(atdItemConditionGroup1, erAlAtdItemCon.getTargetNO(),
										((AttendanceItemId) erAlAtdItemCon.getCompareSingleValue().getValue()).v()),
								2));
			}
		}
		return new KrcmtErAlAtdItemCon(krcmtErAlAtdItemConPK, erAlAtdItemCon.getConditionAtr().value,
				erAlAtdItemCon.isUse() ? 1 : 0, lstAtdItemTarget, erAlCompareSingle, erAlCompareRange,
				erAlSingleFixed, erAlSingleAtd);
	}

	public static KwrmtErAlWorkRecord fromDomain(ErrorAlarmWorkRecord domain, ErrorAlarmCondition conditionDomain) {
		// Set PK
		KwrmtErAlWorkRecordPK kwrmtErAlWorkRecordPK = new KwrmtErAlWorkRecordPK(AppContexts.user().companyId(),
				domain.getCode().v());
		// Set main data KwrmtErAlWorkRecord
		String errorAlarmName = domain.getName().v();
		int fixedAtr = domain.getFixedAtr() ? 1 : 0;
		int useAtr = domain.getUseAtr() ? 1 : 0;
		int typeAtr = domain.getTypeAtr().value;
		int boldAtr = domain.getMessage().getBoldAtr() ? 1 : 0;
		String messageColor = domain.getMessage().getMessageColor().v();
		int cancelableAtr = domain.getCancelableAtr() ? 1 : 0;
		Integer errorDisplayItem = domain.getErrorDisplayItem();
		String eralCheckId = domain.getErrorAlarmCheckID();
		List<KrcstErAlApplication> krcstErAlApplication = domain.getLstApplication().stream()
				.map(appTypeCd -> new KrcstErAlApplication(
						new KrcstErAlApplicationPK(AppContexts.user().companyId(), domain.getCode().v(), (appTypeCd))))
				.collect(Collectors.toList());
		String cancelRoleId = domain.getCancelRoleId();
		String messageDisplay = conditionDomain.getDisplayMessage().v();
		KrcmtErAlCondition krcmtErAlCondition = new KrcmtErAlCondition(eralCheckId, messageDisplay, (0),
				Collections.emptyList(), (0), Collections.emptyList(), (0), Collections.emptyList(), (0),
				Collections.emptyList(), (0), (0), null, null, null, Collections.emptyList(), Collections.emptyList(),
				(0), (0), null, null, null, Collections.emptyList(), Collections.emptyList(), (0), (0), "0", null, null,
				null, 0);
		if (!domain.getFixedAtr()) {
			// Set Check target condition
			int filterByBusinessType = conditionDomain.getCheckTargetCondtion().getFilterByBusinessType() ? (1) : (0);
			int filterByJobTitle = (conditionDomain.getCheckTargetCondtion().getFilterByJobTitle() ? 1 : 0);
			int filterByEmployment = (conditionDomain.getCheckTargetCondtion().getFilterByEmployment() ? 1 : 0);
			int filterByClassification = (conditionDomain.getCheckTargetCondtion().getFilterByClassification() ? 1 : 0);
			List<KrcstErAlBusinessType> lstBusinessType = conditionDomain.getCheckTargetCondtion()
					.getLstBusinessTypeCode().stream().map(businessTypeCd -> new KrcstErAlBusinessType(
							new KrcstErAlBusinessTypePK(eralCheckId, businessTypeCd.v())))
					.collect(Collectors.toList());
			List<KrcstErAlJobTitle> lstJobTitle = conditionDomain.getCheckTargetCondtion().getLstJobTitleId().stream()
					.map(jobTitleId -> new KrcstErAlJobTitle(new KrcstErAlJobTitlePK(eralCheckId, jobTitleId)))
					.collect(Collectors.toList());
			List<KrcstErAlEmployment> lstEmployment = conditionDomain.getCheckTargetCondtion().getLstEmploymentCode()
					.stream().map(emptCd -> new KrcstErAlEmployment(new KrcstErAlEmploymentPK(eralCheckId, emptCd.v())))
					.collect(Collectors.toList());
			List<KrcstErAlClass> lstClassification = conditionDomain.getCheckTargetCondtion().getLstClassificationCode()
					.stream().map(clssCd -> new KrcstErAlClass(new KrcstErAlClassPK(eralCheckId, clssCd.v())))
					.collect(Collectors.toList());
			// Set worktype condition
			int workTypeUseAtr = conditionDomain.getWorkTypeCondition().isUse() ? 1 : 0;
			int wtCompareAtr = conditionDomain.getWorkTypeCondition().getComparePlanAndActual().value;
			int wtPlanActualOperator = 0;
			int wtPlanFilterAtr = 0;
			int wtActualFilterAtr = 0;
			List<KrcstErAlWtPlan> lstWtPlan = new ArrayList<>();
			List<KrcstErAlWtActual> lstWtActual = new ArrayList<>();
			if (wtCompareAtr != FilterByCompare.EXTRACT_SAME.value) {
				PlanActualWorkType wtypeCondition = (PlanActualWorkType) conditionDomain.getWorkTypeCondition();
				wtPlanActualOperator = wtypeCondition.getOperatorBetweenPlanActual().value;
				wtPlanFilterAtr = wtypeCondition.getWorkTypePlan().isUse() ? 1 : 0;
				wtActualFilterAtr = wtypeCondition.getWorkTypeActual().isUse() ? 1 : 0;
				lstWtPlan = wtypeCondition.getWorkTypePlan().getLstWorkType().stream()
						.map(wtCode -> new KrcstErAlWtPlan(new KrcstErAlWtPlanActualPK(eralCheckId, wtCode.v())))
						.collect(Collectors.toList());
				lstWtActual = wtypeCondition.getWorkTypeActual().getLstWorkType().stream()
						.map(wtCode -> new KrcstErAlWtActual(new KrcstErAlWtPlanActualPK(eralCheckId, wtCode.v())))
						.collect(Collectors.toList());
			} else {
				SingleWorkType wtypeCondition = (SingleWorkType) conditionDomain.getWorkTypeCondition();
				wtPlanFilterAtr = wtypeCondition.getTargetWorkType().isUse() ? 1 : 0;
				lstWtPlan = wtypeCondition.getTargetWorkType().getLstWorkType().stream()
						.map(wtCode -> new KrcstErAlWtPlan(new KrcstErAlWtPlanActualPK(eralCheckId, wtCode.v())))
						.collect(Collectors.toList());
			}
			// Set worktime condition
			int workingHoursUseAtr = conditionDomain.getWorkTimeCondition().isUse() ? 1 : 0;
			int whCompareAtr = conditionDomain.getWorkTimeCondition().getComparePlanAndActual().value;
			int whPlanActualOperator = 0;
			int whPlanFilterAtr = 0;
			int whActualFilterAtr = 0;
			List<KrcstErAlWhPlan> lstWhPlan = new ArrayList<>();
			List<KrcstErAlWhActual> lstWhActual = new ArrayList<>();
			if (whCompareAtr != FilterByCompare.EXTRACT_SAME.value) {
				PlanActualWorkTime wtimeCondition = (PlanActualWorkTime) conditionDomain.getWorkTimeCondition();
				whPlanActualOperator = wtimeCondition.getOperatorBetweenPlanActual().value;
				whPlanFilterAtr = wtimeCondition.getWorkTimePlan().isUse() ? 1 : 0;
				whActualFilterAtr = wtimeCondition.getWorkTimeActual().isUse() ? 1 : 0;
				lstWhPlan = wtimeCondition.getWorkTimePlan().getLstWorkTime().stream()
						.map(wtCode -> new KrcstErAlWhPlan(new KrcstErAlWhPlanActualPK(eralCheckId, wtCode.v())))
						.collect(Collectors.toList());
				lstWhActual = wtimeCondition.getWorkTimeActual().getLstWorkTime().stream()
						.map(wtCode -> new KrcstErAlWhActual(new KrcstErAlWhPlanActualPK(eralCheckId, wtCode.v())))
						.collect(Collectors.toList());
			} else {
				SingleWorkTime wtimeCondition = (SingleWorkTime) conditionDomain.getWorkTimeCondition();
				whPlanFilterAtr = wtimeCondition.getTargetWorkTime().isUse() ? 1 : 0;
				lstWhPlan = wtimeCondition.getTargetWorkTime().getLstWorkTime().stream()
						.map(wtCode -> new KrcstErAlWhPlan(new KrcstErAlWhPlanActualPK(eralCheckId, wtCode.v())))
						.collect(Collectors.toList());
			}
			// Set attendance item condition
			int operatorBetweenGroups = conditionDomain.getAtdItemCondition().getOperatorBetweenGroups().value;
			int group2UseAtr = conditionDomain.getAtdItemCondition().isUseGroup2() ? 1 : 0;
			String atdItemConditionGroup1 = conditionDomain.getAtdItemCondition().getGroup1().getAtdItemConGroupId();
			String atdItemConditionGroup2 = conditionDomain.getAtdItemCondition().getGroup2().getAtdItemConGroupId();
			int conditionOperator1 = conditionDomain.getAtdItemCondition().getGroup1().getConditionOperator().value;
			List<KrcmtErAlAtdItemCon> lstAtdItemCon1 = conditionDomain.getAtdItemCondition().getGroup1()
					.getLstErAlAtdItemCon().stream()
					.map(erAlAtdItemCon -> getKrcmtErAlAtdItemConFromDomain(atdItemConditionGroup1, erAlAtdItemCon))
					.collect(Collectors.toList());
			KrcstErAlConGroup krcstErAlConGroup1 = new KrcstErAlConGroup(atdItemConditionGroup1, conditionOperator1,
					lstAtdItemCon1);
			int conditionOperator2 = conditionDomain.getAtdItemCondition().getGroup2().getConditionOperator().value;
			List<KrcmtErAlAtdItemCon> lstAtdItemCon2 = conditionDomain.getAtdItemCondition().getGroup2()
					.getLstErAlAtdItemCon().stream()
					.map(erAlAtdItemCon -> getKrcmtErAlAtdItemConFromDomain(atdItemConditionGroup2, erAlAtdItemCon))
					.collect(Collectors.toList());
			KrcstErAlConGroup krcstErAlConGroup2 = new KrcstErAlConGroup(atdItemConditionGroup2, conditionOperator2,
					lstAtdItemCon2);
			krcmtErAlCondition = new KrcmtErAlCondition(eralCheckId, messageDisplay, filterByBusinessType,
					lstBusinessType, filterByJobTitle, lstJobTitle, filterByEmployment, lstEmployment,
					filterByClassification, lstClassification, workTypeUseAtr, wtPlanActualOperator, wtPlanFilterAtr,
					wtActualFilterAtr, wtCompareAtr, lstWtActual, lstWtPlan, workingHoursUseAtr, whPlanActualOperator,
					whPlanFilterAtr, whActualFilterAtr, whCompareAtr, lstWhActual, lstWhPlan, operatorBetweenGroups,
					group2UseAtr, atdItemConditionGroup1, krcstErAlConGroup1, atdItemConditionGroup2,
					krcstErAlConGroup2,
					conditionDomain.getContinuousPeriod() != null ? conditionDomain.getContinuousPeriod().v() : null);
		}
		KwrmtErAlWorkRecord entity = new KwrmtErAlWorkRecord(kwrmtErAlWorkRecordPK, errorAlarmName, fixedAtr, useAtr,
				typeAtr, boldAtr, messageColor.equals("") ? null : messageColor, cancelableAtr, errorDisplayItem,
				eralCheckId, krcmtErAlCondition, krcstErAlApplication, cancelRoleId);
		return entity;
	}

	public static ErrorAlarmWorkRecord toDomain(KwrmtErAlWorkRecord entity) {
		ErrorAlarmWorkRecord domain = ErrorAlarmWorkRecord.createFromJavaType(AppContexts.user().companyId(),
				entity.kwrmtErAlWorkRecordPK.errorAlarmCode, entity.errorAlarmName, entity.fixedAtr == 1,
				entity.useAtr == 1, entity.typeAtr, entity.boldAtr == 1, entity.messageColor, entity.cancelableAtr == 1,
				entity.errorDisplayItem,
				Optional.ofNullable(entity.krcstErAlApplication).orElse(Collections.emptyList()).stream()
						.map(eralAppEntity -> eralAppEntity.krcstErAlApplicationPK.appTypeCd)
						.collect(Collectors.toList()),
				entity.eralCheckId);
		return domain;
	}

	public static ErrorAlarmCondition toConditionDomain(KwrmtErAlWorkRecord entity) {
		ErrorAlarmCondition condition = ErrorAlarmCondition.init();
		condition.setDisplayMessage(entity.krcmtErAlCondition.messageDisplay);
		condition.setContinuousPeriod(entity.krcmtErAlCondition.continuousPeriod);
		if (entity.fixedAtr != 1) {
			// Set AlCheckTargetCondition
			condition.createAlCheckTargetCondition(entity.krcmtErAlCondition.filterByBusinessType == 1,
					entity.krcmtErAlCondition.filterByJobTitle == 1, entity.krcmtErAlCondition.filterByEmployment == 1,
					entity.krcmtErAlCondition.filterByClassification == 1,
					Optional.ofNullable(entity.krcmtErAlCondition.lstBusinessType).orElse(Collections.emptyList())
							.stream().map(businessType -> businessType.krcstErAlBusinessTypePK.businessTypeCd)
							.collect(Collectors.toList()),
					Optional.ofNullable(entity.krcmtErAlCondition.lstJobTitle).orElse(Collections.emptyList()).stream()
							.map(jobTitle -> jobTitle.krcstErAlJobTitlePK.jobId).collect(Collectors.toList()),
					Optional.ofNullable(entity.krcmtErAlCondition.lstEmployment).orElse(Collections.emptyList())
							.stream().map(empt -> empt.krcstErAlEmploymentPK.emptcd).collect(Collectors.toList()),
					Optional.ofNullable(entity.krcmtErAlCondition.lstClassification).orElse(Collections.emptyList())
							.stream().map(clss -> clss.krcstErAlClassPK.clscd).collect(Collectors.toList()));
			// Set WorkTypeCondition
			condition.createWorkTypeCondition(entity.krcmtErAlCondition.workTypeUseAtr == 1,
					entity.krcmtErAlCondition.wtCompareAtr);
			if (entity.krcmtErAlCondition.wtCompareAtr != FilterByCompare.EXTRACT_SAME.value) {
				condition.setWorkTypePlan(entity.krcmtErAlCondition.wtPlanFilterAtr == 1,
						Optional.ofNullable(entity.krcmtErAlCondition.lstWtPlan).orElse(Collections.emptyList())
								.stream().map(wtype -> wtype.krcstErAlWtPlanPK.workTypeCode)
								.collect(Collectors.toList()));
				condition.setWorkTypeActual(entity.krcmtErAlCondition.wtActualFilterAtr == 1,
						Optional.ofNullable(entity.krcmtErAlCondition.lstWtActual).orElse(Collections.emptyList())
								.stream().map(wtype -> wtype.krcstErAlWtPlanActualPK.workTypeCode)
								.collect(Collectors.toList()));
				condition.chooseWorkTypeOperator(entity.krcmtErAlCondition.wtPlanActualOperator);
			} else {
				condition.setWorkTypeSingle(entity.krcmtErAlCondition.wtPlanFilterAtr == 1,
						Optional.ofNullable(entity.krcmtErAlCondition.lstWtPlan).orElse(Collections.emptyList())
								.stream().map(wtype -> wtype.krcstErAlWtPlanPK.workTypeCode)
								.collect(Collectors.toList()));
			}
			// Set WorkTimeCondtion
			condition.createWorkTimeCondition(entity.krcmtErAlCondition.workingHoursUseAtr == 1,
					entity.krcmtErAlCondition.whCompareAtr);
			if (entity.krcmtErAlCondition.whCompareAtr != FilterByCompare.EXTRACT_SAME.value) {
				condition.setWorkTimePlan(entity.krcmtErAlCondition.whPlanFilterAtr == 1,
						Optional.ofNullable(entity.krcmtErAlCondition.lstWhPlan).orElse(Collections.emptyList())
								.stream().map(wtime -> wtime.krcstErAlWhPlanActualPK.workTimeCode)
								.collect(Collectors.toList()));
				condition.setWorkTimeActual(entity.krcmtErAlCondition.whActualFilterAtr == 1,
						Optional.ofNullable(entity.krcmtErAlCondition.lstWhActual).orElse(Collections.emptyList())
								.stream().map(wtime -> wtime.krcstErAlWhPlanActualPK.workTimeCode)
								.collect(Collectors.toList()));
				condition.chooseWorkTimeOperator(entity.krcmtErAlCondition.whPlanActualOperator);
			} else {
				condition.setWorkTimeSingle(entity.krcmtErAlCondition.whPlanFilterAtr == 1,
						Optional.ofNullable(entity.krcmtErAlCondition.lstWhPlan).orElse(Collections.emptyList())
								.stream().map(wtime -> wtime.krcstErAlWhPlanActualPK.workTimeCode)
								.collect(Collectors.toList()));
			}
			// Set AttendanceItemCondition
			List<ErAlAttendanceItemCondition<?>> conditionsGroup1 = Optional
					.ofNullable(entity.krcmtErAlCondition.krcstErAlConGroup1)
					.orElse(new KrcstErAlConGroup("", 0, new ArrayList<>())).lstAtdItemCon.stream()
							.map(atdItemCon -> convertKrcmtErAlAtdItemConToDomain(entity, atdItemCon))
							.collect(Collectors.toList());
			List<ErAlAttendanceItemCondition<?>> conditionsGroup2 = Optional
					.ofNullable(entity.krcmtErAlCondition.krcstErAlConGroup2)
					.orElse(new KrcstErAlConGroup("", 0, new ArrayList<>())).lstAtdItemCon.stream()
							.map(atdItemCon -> convertKrcmtErAlAtdItemConToDomain(entity, atdItemCon))
							.collect(Collectors.toList());
			condition
					.createAttendanceItemCondition(entity.krcmtErAlCondition.operatorBetweenGroups,
							entity.krcmtErAlCondition.group2UseAtr == 1)
					.setAttendanceItemConditionGroup1(
							Optional.ofNullable(entity.krcmtErAlCondition.krcstErAlConGroup1)
									.orElse(new KrcstErAlConGroup("", 0, new ArrayList<>())).conditionOperator,
							conditionsGroup1)
					.setAttendanceItemConditionGroup2(
							Optional.ofNullable(entity.krcmtErAlCondition.krcstErAlConGroup2)
									.orElse(new KrcstErAlConGroup("", 0, new ArrayList<>())).conditionOperator,
							conditionsGroup2);
		}
		condition.setCheckId(entity.eralCheckId);
		return condition;
	}

}
