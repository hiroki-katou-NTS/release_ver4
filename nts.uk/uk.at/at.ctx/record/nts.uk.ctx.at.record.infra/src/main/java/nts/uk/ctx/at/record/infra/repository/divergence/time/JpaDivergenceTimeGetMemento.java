package nts.uk.ctx.at.record.infra.repository.divergence.time;

import java.util.List;
import java.util.stream.Collectors;

import nts.gul.collection.CollectionUtil;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcAttendance;
import nts.uk.ctx.at.record.infra.entity.divergence.time.KrcstDvgcTime;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.worktime.DivergenceTimeErrorCancelMethod;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.worktime.DivergenceTimeGetMemento;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.worktime.DivergenceTimeName;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.worktime.DivergenceTimeUseSet;
import nts.uk.ctx.at.shared.dom.dailyattdcal.dailyattendance.worktime.DivergenceType;

/**
 * The Class JpaDivergenceTimeRepositoryGetMemento.
 */
public class JpaDivergenceTimeGetMemento implements DivergenceTimeGetMemento {

	/** The entityDvgcTime. */
	private KrcstDvgcTime entityDvgcTime;

	/** The entity dvgc attendance. */
	private List<KrcstDvgcAttendance> entityDvgcAttendance;

	/**
	 * Instantiates a new jpa divergence time repository get memento.
	 *
	 * @param entityDvgcTime
	 *            the entityDvgcTime
	 * @param entityDvgcAttendance
	 *            the entity dvgc attendance
	 */
	public JpaDivergenceTimeGetMemento(KrcstDvgcTime entityDvgcTime, List<KrcstDvgcAttendance> entityDvgcAttendance) {

		this.entityDvgcTime = entityDvgcTime;
		this.entityDvgcAttendance = entityDvgcAttendance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeGetMemento#
	 * getDivergenceTimeNo()
	 */
	@Override
	public Integer getDivergenceTimeNo() {
		return this.entityDvgcTime.getId().getNo();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeGetMemento#
	 * getCompanyId()
	 */
	@Override
	public String getCompanyId() {
		return this.entityDvgcTime.getId().getCid();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeGetMemento#
	 * getDivTimeUseSet()
	 */
	@Override
	public DivergenceTimeUseSet getDivTimeUseSet() {
		return DivergenceTimeUseSet.valueOf(this.entityDvgcTime.getDvgcTimeUseSet().intValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeGetMemento#
	 * getDivTimeName()
	 */
	@Override
	public DivergenceTimeName getDivTimeName() {
		return new DivergenceTimeName(this.entityDvgcTime.getDvgcTimeName());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeGetMemento#
	 * getDivType()
	 */
	@Override
	public DivergenceType getDivType() {
		return DivergenceType.valueOf(this.entityDvgcTime.getDvgcType().intValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeGetMemento#
	 * getErrorCancelMedthod()
	 */
	@Override
	public DivergenceTimeErrorCancelMethod getErrorCancelMedthod() {
		return new DivergenceTimeErrorCancelMethod(this.entityDvgcTime.getReasonInputCanceled().intValue(),
				this.entityDvgcTime.getReasonSelectCanceled().intValue());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeGetMemento#
	 * getTargetItems()
	 */
	@Override
	public List<Integer> getTargetItems() {
		if (!CollectionUtil.isEmpty(this.entityDvgcAttendance)) {
			return this.entityDvgcAttendance.stream().filter(item -> item != null)
					.map(item -> item.getId().getAttendanceId()).collect(Collectors.toList());
		}

		return this.entityDvgcTime.getKrcstDvgcAttendances().stream()
				.map(item -> item.getId().getAttendanceId()).collect(Collectors.toList());
	}

}
