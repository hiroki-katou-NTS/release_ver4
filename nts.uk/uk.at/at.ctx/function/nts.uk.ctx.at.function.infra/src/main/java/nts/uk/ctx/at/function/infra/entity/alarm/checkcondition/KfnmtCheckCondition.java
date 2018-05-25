package nts.uk.ctx.at.function.infra.entity.alarm.checkcondition;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.NoArgsConstructor;
import nts.arc.enums.EnumAdaptor;
import nts.uk.ctx.at.function.dom.alarm.AlarmCategory;
import nts.uk.ctx.at.function.dom.alarm.checkcondition.CheckCondition;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.ExtractionRangeBase;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.daily.ExtractionPeriodDaily;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.month.ExtractionPeriodMonth;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.periodunit.ExtractionPeriodUnit;
import nts.uk.ctx.at.function.dom.alarm.extractionrange.year.AYear;
import nts.uk.ctx.at.function.infra.entity.alarm.KfnmtAlarmPatternSet;
import nts.uk.ctx.at.function.infra.entity.alarm.extractionrange.daily.KfnmtExtractionPeriodDaily;
import nts.uk.ctx.at.function.infra.entity.alarm.extractionrange.monthly.KfnmtExtractPeriodMonth;
import nts.uk.ctx.at.function.infra.entity.alarm.extractionrange.periodunit.KfnmtExtractionPerUnit;
import nts.uk.ctx.at.function.infra.entity.alarm.extractionrange.yearly.KfnmtExtractRangeYear;
import nts.uk.shr.infra.data.entity.UkJpaEntity;

@NoArgsConstructor
@Entity
@Table(name = "KFNMT_CHECK_CONDITION")
public class KfnmtCheckCondition extends UkJpaEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@EmbeddedId
	public KfnmtCheckConditionPK pk;

	@Column(name = "EXTRACTION_ID")
	public String extractionId;

	@Column(name = "EXTRACTION_RANGE")
	public int extractionRange;

	@ManyToOne
	@JoinColumns(
		  { @JoinColumn(name = "CID", referencedColumnName = "CID", insertable = false, updatable = false),
			@JoinColumn(name = "ALARM_PATTERN_CD", referencedColumnName = "ALARM_PATTERN_CD", insertable = false, updatable = false) })
	public KfnmtAlarmPatternSet alarmPatternSet;

	@OneToMany(mappedBy = "checkCondition", cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "KFNMT_CHECK_CON_ITEM")
	public List<KfnmtCheckConItem> checkConItems;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumns({
		@JoinColumn(name="EXTRACTION_ID", referencedColumnName="EXTRACTION_ID", insertable=false, updatable=false),
		@JoinColumn(name="EXTRACTION_RANGE", referencedColumnName="EXTRACTION_RANGE", insertable=false, updatable=false)
	})
	public KfnmtExtractionPeriodDaily extractionPeriodDaily;

	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumns({
		@JoinColumn(name="EXTRACTION_ID", referencedColumnName="EXTRACTION_ID", insertable=false, updatable=false),
		@JoinColumn(name="EXTRACTION_RANGE", referencedColumnName="EXTRACTION_RANGE", insertable=false, updatable=false)
	})
	public KfnmtExtractionPerUnit extractionPerUnit;
	
	
	@OneToMany(mappedBy = "checkCondition", cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinTable(name = "KFNMT_EXTRACT_PER_MONTH")
	public List<KfnmtExtractPeriodMonth> listExtractPerMonth;
	
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumns({
		@JoinColumn(name="EXTRACTION_ID", referencedColumnName="EXTRACTION_ID", insertable=false, updatable=false),
		@JoinColumn(name="EXTRACTION_RANGE", referencedColumnName="EXTRACTION_RANGE", insertable=false, updatable=false)
	})
	public KfnmtExtractRangeYear extractRangeYear;
	
	
	public KfnmtCheckCondition(KfnmtCheckConditionPK pk, String extractionId, int extractionRange,
			List<KfnmtCheckConItem> checkConItems, KfnmtExtractionPeriodDaily extractionPeriodDaily) {
		super();
		this.pk = pk;
		this.extractionId = extractionId;
		this.extractionRange = extractionRange;
		this.checkConItems = checkConItems;
		this.extractionPeriodDaily = extractionPeriodDaily;
	}
	
	public KfnmtCheckCondition(KfnmtCheckConditionPK pk, String extractionId, int extractionRange, List<KfnmtCheckConItem> checkConItems,
			KfnmtExtractionPerUnit extractionPerUnit) {
		super();
		this.pk = pk;
		this.extractionId = extractionId;
		this.extractionRange = extractionRange;
		this.checkConItems = checkConItems;
		this.extractionPerUnit = extractionPerUnit;
	}

	public KfnmtCheckCondition(KfnmtCheckConditionPK pk, String extractionId, int extractionRange,
			List<KfnmtCheckConItem> checkConItems, List<KfnmtExtractPeriodMonth> listExtractPerMonth) {
		super();
		this.pk = pk;
		this.extractionId = extractionId;
		this.extractionRange = extractionRange;
		this.checkConItems = checkConItems;
		this.listExtractPerMonth = listExtractPerMonth;
	}


	public KfnmtCheckCondition(KfnmtCheckConditionPK pk, String extractionId, int extractionRange,
			List<KfnmtCheckConItem> checkConItems, KfnmtExtractionPeriodDaily extractionPeriodDaily,
			List<KfnmtExtractPeriodMonth> listExtractPerMonth, KfnmtExtractRangeYear extractRangeYear) {
		super();
		this.pk = pk;
		this.extractionId = extractionId;
		this.extractionRange = extractionRange;
		this.checkConItems = checkConItems;
		this.extractionPeriodDaily = extractionPeriodDaily;
		this.listExtractPerMonth = listExtractPerMonth;
		this.extractRangeYear = extractRangeYear;
	}
	
	
	public KfnmtCheckCondition(KfnmtCheckConditionPK pk, String extractionId, int extractionRange, List<KfnmtCheckConItem> checkConItems) {
		super();
		this.pk = pk;
		this.extractionId = extractionId;
		this.extractionRange = extractionRange;
		this.checkConItems = checkConItems;
	}

	@Override
	protected Object getKey() {
		return this.pk;
	}

	public CheckCondition toDomain() {

		List<ExtractionRangeBase> extractPeriodList = new ArrayList<>();
		if (this.pk.alarmCategory == AlarmCategory.DAILY.value
				|| this.pk.alarmCategory == AlarmCategory.MAN_HOUR_CHECK.value) {
			extractPeriodList.add(extractionPeriodDaily.toDomain());

		} else if (this.pk.alarmCategory == AlarmCategory.MONTHLY.value) {
			listExtractPerMonth.forEach(e -> {
				if (e.pk.unit == 3)
					extractPeriodList.add(e.toDomain(extractionId, extractionRange));
			});
			
		} else if (this.pk.alarmCategory == AlarmCategory.SCHEDULE_4WEEK.value) {
			extractPeriodList.add(extractionPerUnit.toDomain());
			
		} else if(this.pk.alarmCategory == AlarmCategory.AGREEMENT.value) {
			extractPeriodList.add(extractionPeriodDaily.toDomain());
			
			listExtractPerMonth.forEach(e -> {				
					extractPeriodList.add(e.toDomain(extractionId, extractionRange));
			});
			
			extractPeriodList.add(extractRangeYear.toDomain());
		}

		List<String> checkConList = this.checkConItems.stream().map(c -> c.pk.checkConditionCD)
				.collect(Collectors.toList());
		CheckCondition domain = new CheckCondition(EnumAdaptor.valueOf(this.pk.alarmCategory, AlarmCategory.class),
				checkConList, extractPeriodList);
		return domain;
	}
	
	public static KfnmtCheckCondition toEntity(CheckCondition domain, String companyId, String alarmPatternCode) {
		
		if (domain.isDaily() || domain.isManHourCheck()) {	
			
			ExtractionRangeBase extractBase = domain.getExtractPeriodList().get(0);
			ExtractionPeriodDaily extractionPeriodDaily = (ExtractionPeriodDaily) extractBase;
			KfnmtCheckCondition entity = new KfnmtCheckCondition(
					new KfnmtCheckConditionPK(companyId, alarmPatternCode, domain.getAlarmCategory().value),
					extractBase.getExtractionId(), extractBase.getExtractionRange().value,
					domain.getCheckConditionList().stream().map(
							x -> new KfnmtCheckConItem(buildCheckConItemPK(domain, x, companyId, alarmPatternCode)))
							.collect(Collectors.toList()),
					KfnmtExtractionPeriodDaily.toEntity(extractionPeriodDaily));
			return entity;
			
		} else if (domain.isMonthly()) {		
			ExtractionRangeBase extractBase = domain.getExtractPeriodList().get(0);
			ExtractionPeriodMonth extractionPeriodMonth = (ExtractionPeriodMonth) extractBase;

			KfnmtCheckCondition entity = new KfnmtCheckCondition(
					new KfnmtCheckConditionPK(companyId, alarmPatternCode, domain.getAlarmCategory().value),
					extractBase.getExtractionId(), extractBase.getExtractionRange().value,
					domain.getCheckConditionList().stream().map(
							x -> new KfnmtCheckConItem(buildCheckConItemPK(domain, x, companyId, alarmPatternCode)))
							.collect(Collectors.toList()),
					Arrays.asList(KfnmtExtractPeriodMonth.toEntity(companyId, alarmPatternCode,
							domain.getAlarmCategory().value, extractionPeriodMonth)));
			return entity;
			
		} else if (domain.is4W4D()) {

			ExtractionRangeBase extractBase = domain.getExtractPeriodList().get(0);

			ExtractionPeriodUnit extractionPerUnit = (ExtractionPeriodUnit) extractBase;
			KfnmtCheckCondition entity = new KfnmtCheckCondition(
					new KfnmtCheckConditionPK(companyId, alarmPatternCode, domain.getAlarmCategory().value),
					extractBase.getExtractionId(), extractBase.getExtractionRange().value,
					domain.getCheckConditionList().stream().map(
							x -> new KfnmtCheckConItem(buildCheckConItemPK(domain, x, companyId, alarmPatternCode)))
							.collect(Collectors.toList()),
					KfnmtExtractionPerUnit.toEntity(extractionPerUnit));
			return entity;			
			
		} else if (domain.isAgrrement()) {
			List<ExtractionPeriodMonth> listMonth = new ArrayList<ExtractionPeriodMonth>();
			ExtractionPeriodDaily extractionPeriodDaily = null;
			AYear extractYear = null ;
			
			for(ExtractionRangeBase extractBase : domain.getExtractPeriodList()) {
				
				if(extractBase instanceof ExtractionPeriodDaily) {
					extractionPeriodDaily = (ExtractionPeriodDaily) extractBase;
					
				}else if(extractBase  instanceof ExtractionPeriodMonth) {
					listMonth.add((ExtractionPeriodMonth) extractBase);					
				}else {
					extractYear = (AYear) extractBase;
				}
				
				
			}
			return new KfnmtCheckCondition(
					new KfnmtCheckConditionPK(companyId, alarmPatternCode, domain.getAlarmCategory().value),
					extractYear.getExtractionId(), extractYear.getExtractionRange().value,
					domain.getCheckConditionList().stream().map( x -> new KfnmtCheckConItem(buildCheckConItemPK(domain, x, companyId, alarmPatternCode))).collect(Collectors.toList()), 
					KfnmtExtractionPeriodDaily.toEntity(extractionPeriodDaily), 
					listMonth.stream().map( e-> KfnmtExtractPeriodMonth.toEntity(companyId, alarmPatternCode,
					domain.getAlarmCategory().value, e) ).collect(Collectors.toList()),
					KfnmtExtractRangeYear.toEntity(extractYear));
			
		} else {
			
			return new KfnmtCheckCondition(
					new KfnmtCheckConditionPK(companyId, alarmPatternCode, domain.getAlarmCategory().value), "", 0,
					domain.getCheckConditionList().stream().map(
							x -> new KfnmtCheckConItem(buildCheckConItemPK(domain, x, companyId, alarmPatternCode)))
							.collect(Collectors.toList()));
		}


	}
	
	public void fromEntity(KfnmtCheckCondition entity) {

		if (entity.pk.alarmCategory == AlarmCategory.DAILY.value || entity.pk.alarmCategory == AlarmCategory.MAN_HOUR_CHECK.value) {
			
			this.extractionPeriodDaily.fromEntity(entity.extractionPeriodDaily);
			
		} else if (entity.pk.alarmCategory == AlarmCategory.MONTHLY.value) {
			
			this.listExtractPerMonth= new ArrayList<KfnmtExtractPeriodMonth>();
			entity.listExtractPerMonth.forEach(item -> {
				this.listExtractPerMonth.add(item);
			});
			
		} else if (entity.pk.alarmCategory == AlarmCategory.SCHEDULE_4WEEK.value) {
			
			this.extractionPerUnit.fromEntity(entity.extractionPerUnit);
			
		} else if(entity.pk.alarmCategory ==AlarmCategory.AGREEMENT.value) {
			
			this.extractionPeriodDaily.fromEntity(entity.extractionPeriodDaily);
			
			this.listExtractPerMonth= new ArrayList<KfnmtExtractPeriodMonth>();
			entity.listExtractPerMonth.forEach(item -> {
				this.listExtractPerMonth.add(item);
			});
			
			this.extractRangeYear.fromEntity(entity.extractRangeYear);
		}
		
		
		this.extractionRange = entity.extractionRange;
		this.checkConItems.removeIf(item -> !entity.checkConItems.contains(item));
		entity.checkConItems.forEach( item ->{
			if(!this.checkConItems.contains(item)) this.checkConItems.add(item);
		});
	}
	
	private static KfnmtCheckConItemPK buildCheckConItemPK(CheckCondition domain, String checkConditionCD, String companyId, String alarmPatternCode) {
		return new KfnmtCheckConItemPK(companyId, alarmPatternCode, domain.getAlarmCategory().value, checkConditionCD);
	}

	
}
