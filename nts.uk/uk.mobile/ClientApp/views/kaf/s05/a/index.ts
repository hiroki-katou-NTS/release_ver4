import { component, Prop, Watch } from '@app/core/component';
import { _, Vue } from '@app/provider';
import { KafS05aStep1Component } from '../components/step1/index';
import { KafS05aStep2Component } from '../components/step2/index';
import { KafS05aStep3Component } from '../components/step3/index';
import { KafS05aStep4Component } from '../components/step4/index';
import { StepwizardComponent } from '@app/components';
import { Kafs05Model } from '../components/common/CommonClass';

@component({
    name: 'kafS05a',
    route: '/kaf/s05/a',
    style: require('../style.scss'),
    template: require('./index.html'),
    resource: require('../resources.json'),
    constraints: [],
    components: {
        'kaf005_1': KafS05aStep1Component,
        'kaf005_2': KafS05aStep2Component,
        'kaf005_3': KafS05aStep3Component,
        'kaf005_4': KafS05aStep4Component,
        'step-wizard': StepwizardComponent
    }
})

export class KafS05aComponent extends Vue {
    @Prop({ default: () => ({ appID: null }) })
    public params: {appID: string};

    public kafs05Model: Kafs05Model = null;

    private step: string = 'step1';

    public created() {
        this.step = 'step1';
        this.kafs05Model = {
            isCreate: true, step1Start: true, resetTimeRange: 0, checkBoxValue: false, enableSendMail: false, displayBreakTimeFlg: false, employeeName: '', enteredPersonName: '', prePostSelected: 2, workState: true,
            typeSiftVisible: true, appDate: null, workTypeCd: '', workTypeName: '', siftCD: '', siftName: '', workTypecodes: [], workTimecodes: [], selectedWorkTime: '',
            reasonCombo: [], selectedReason: '', requiredReason: false, multilContent: '', reasonCombo2: [], selectedReason2: '', requiredReason2: false, multilContent2: '',
            approvalSource: [], employeeID: null, employeeIDs: [], employeeList: [], selectedEmplCodes: '', employeeFlag: false, totalEmployee: '', heightOvertimeHours: null,
            overtimeAtr: null, restTime: [], overtimeHours: [], breakTimes: [], bonusTimes: [], prePostEnable: true, displayCaculationTime: false, displayBonusTime: false, displayPrePostFlg: false,
            restTimeDisFlg: false, typicalReasonDisplayFlg: false, displayAppReasonContentFlg: false, displayDivergenceReasonForm: false, displayDivergenceReasonInput: false,
            workTypeChangeFlg: false, overtimeWork: [], indicationOvertimeFlg: true, calculateFlag: 0, uiType: 0, preWorkContent: null, targetDate: null, editable: true,
            enableOvertimeInput: false, isSpr: false, resultCaculationTimeFlg: false, workTimeInput: { start: null, end: null }, appID: this.params.appID, version: 0, reflectPerState: 0, user: 0
        };
    }

    public toStep2(kafs05Model: Kafs05Model) {
        this.step = 'step2';
        this.kafs05Model = _.cloneWith(kafs05Model);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    public toStep3(kafs05Model: Kafs05Model) {
        this.step = 'step3';
        this.kafs05Model = _.cloneWith(kafs05Model);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    public toStep4(kafs05Model: Kafs05Model) {
        this.step = 'step4';
        this.kafs05Model = _.cloneWith(kafs05Model);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    public backToStep1(kafs05Model: Kafs05Model) {
        this.step = 'step1';
        this.kafs05Model = _.cloneWith(kafs05Model);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    public backToStep2(kafs05Model: Kafs05Model) {
        this.step = 'step2';
        this.kafs05Model = _.cloneWith(kafs05Model);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }

    public backToStep3(kafs05Model: Kafs05Model) {
        this.step = 'step3';
        this.kafs05Model = _.cloneWith(kafs05Model);
        window.scrollTo({ top: 0, behavior: 'smooth' });
    }
}
