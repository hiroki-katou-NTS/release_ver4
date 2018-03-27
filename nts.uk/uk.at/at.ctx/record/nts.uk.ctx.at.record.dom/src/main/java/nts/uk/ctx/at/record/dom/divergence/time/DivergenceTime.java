package nts.uk.ctx.at.record.dom.divergence.time;

import java.util.List;

import lombok.Getter;
import nts.arc.layer.dom.AggregateRoot;
import nts.uk.ctx.at.record.dom.divergence.time.DivergenceTimeName;
import nts.uk.shr.com.context.AppContexts;

/**
 * The Class DivergenceTime.
 */
// 乖離時間
@Getter
public class DivergenceTime extends AggregateRoot {

	/** The divergence time no. */
	// 乖離時間NO
	private int divergenceTimeNo;

	/** The c id. */
	// 会社ID
	private String companyId;

	/** The Use classification. */
	// 使用区分
	private DivergenceTimeUseSet divTimeUseSet;

	/** The divergence time name. */
	// 乖離時間名称
	private DivergenceTimeName divTimeName;

	/** The divergence type. */
	// 乖離の種類
	private DivergenceType divType;

	/** The divergence time error cancel method. */
	// 乖離時間のエラーの解除方法
	private DivergenceTimeErrorCancelMethod errorCancelMedthod;

	/** The target item list. */
	// 対象項目一覧
	private List<Double> targetItems;

	/**
	 * Instantiates a new divergence time.
	 *
	 * @param memento
	 *            the memento
	 */
	public DivergenceTime(DivergenceTimeGetMemento memento) {
		this.divergenceTimeNo = memento.getDivergenceTimeNo();
		this.companyId = AppContexts.user().companyId();
		this.divTimeUseSet = memento.getDivTimeUseSet();
		this.divTimeName = memento.getDivTimeName();
		this.divType = memento.getDivType();
		this.errorCancelMedthod = memento.getErrorCancelMedthod();
		this.targetItems = memento.getTargetItems();
	}

	/**
	 * Save to memento.
	 *
	 * @param memento
	 *            the memento
	 */
	public void saveToMemento(DivergenceTimeSetMemento memento) {

		memento.setDivergenceTimeNo(this.divergenceTimeNo);
		memento.setCompanyId(AppContexts.user().companyId());
		memento.setDivTimeName(this.divTimeName);
		memento.setDivTimeUseSet(this.divTimeUseSet);
		memento.setDivType(this.divType);
		memento.setErrorCancelMedthod(this.errorCancelMedthod);
		memento.setTarsetItems(this.targetItems);

	}


	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((companyId == null) ? 0 : companyId.hashCode());
		result = prime * result + divergenceTimeNo;
		return result;
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DivergenceTime other = (DivergenceTime) obj;
		if (companyId == null) {
			if (other.companyId != null)
				return false;
		} else if (!companyId.equals(other.companyId))
			return false;
		if (divergenceTimeNo != other.divergenceTimeNo)
			return false;
		return true;
	}

}
