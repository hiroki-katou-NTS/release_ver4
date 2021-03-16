/******************************************************************
 * Copyright (c) 2017 Nittsu System to present.                   *
 * All right reserved.                                            *
 *****************************************************************/
package nts.uk.ctx.at.shared.infra.repository.vacation.setting.nursingleave;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nts.uk.ctx.at.shared.dom.vacation.setting.ManageDistinct;
import nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.ChildCareNurseUpperLimit;
import nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.MaxPersonSetting;
import nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.NumberOfCaregivers;
import nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.NursingCategory;
import nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.NursingLeaveSettingGetMemento;
import nts.uk.ctx.at.shared.infra.entity.vacation.setting.nursingleave.KnlmtNursingLeaveSet;
import nts.uk.shr.com.time.calendar.MonthDay;

/**
 * The Class JpaNursingVacationSettingGetMemento.
 */
public class JpaNursingLeaveSettingGetMemento implements NursingLeaveSettingGetMemento {

    /** The entity nursing. */
    private KnlmtNursingLeaveSet entityNursing;

    /**
     * Instantiates a new jpa nursing vacation setting get memento.
     *
     * @param entityNursing the entity nursing
     * @param entityWorkTypes the entity work types
     */
    public JpaNursingLeaveSettingGetMemento(KnlmtNursingLeaveSet entityNursing) {
        this.entityNursing = entityNursing;
    }

    /*
     * (non-Javadoc)
     *
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.
     * NursingVacationSettingGetMemento#getCompanyId()
     */
    @Override
    public String getCompanyId() {
        return this.entityNursing.getKnlmtNursingLeaveSetPK().getCid();
    }

    /*
     * (non-Javadoc)
     *
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.
     * NursingVacationSettingGetMemento#getManageType()
     */
    @Override
    public ManageDistinct getManageType() {
        return ManageDistinct.valueOf(this.entityNursing.getManageType());
    }

    /*
     * (non-Javadoc)
     *
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.
     * NursingVacationSettingGetMemento#getNursingCategory()
     */
    @Override
    public NursingCategory getNursingCategory() {
        return NursingCategory.valueOf(this.entityNursing.getKnlmtNursingLeaveSetPK().getNursingCtr());
    }

    /*
     * (non-Javadoc)
     *
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.
     * NursingVacationSettingGetMemento#getStartMonthDay()
     */
    @Override
    public MonthDay getStartMonthDay() {
       //return this.entityNursing.getStartMonthDay();

    	int month = this.entityNursing.getStartMonthDay() / 100;
    	int day = this.entityNursing.getStartMonthDay() % 100;
    	return new MonthDay(month, day);
    }

    /*
     * (non-Javadoc)
     *
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.
     * NursingVacationSettingGetMemento#getMaxPersonSetting()
     */
    @Override
    public List<MaxPersonSetting> getMaxPersonSetting() {
        //return new MaxPersonSetting(new JpaMaxPersonSettingGetMemento(this.entityNursing));

    	// 介護看護休暇上限人数設定
    	List<MaxPersonSetting> maxPersonSetting = new ArrayList<>();

    	maxPersonSetting.add(MaxPersonSetting.of(new ChildCareNurseUpperLimit(this.entityNursing.getNursingNumLeaveDay()), new NumberOfCaregivers(1)));
    	maxPersonSetting.add(MaxPersonSetting.of(new ChildCareNurseUpperLimit(this.entityNursing.getNursingNumLeaveDay2()), new NumberOfCaregivers(2))); //一時対応　要確認

    	return maxPersonSetting;
    }

	@Override
	public Optional<Integer> getHdspFrameNo() {
		return Optional.ofNullable(this.entityNursing.getHdspFrameNo());
	}

	@Override
	public Optional<Integer> getAbsenceFrameNo() {
		return Optional.ofNullable(this.entityNursing.getAbsenceFrameNo());
	}

    /*
     * (non-Javadoc)
     *
     * @see nts.uk.ctx.at.shared.dom.vacation.setting.nursingleave.
     * NursingVacationSettingGetMemento#getWorkTypeCodes()
     */
//    @Override
//    public List<String> getWorkTypeCodes() {
//        return this.entityNursing.getListWorkType().stream()
//                .filter(entity -> entity.getKnlmtNursingWorkTypePK().getNursingCtr() == this.entityNursing
//                        .getKnlmtNursingLeaveSetPK().getNursingCtr())
//                .map(entity -> entity.getWorkTypeCode())
//                .collect(Collectors.toList());
//    }

}
