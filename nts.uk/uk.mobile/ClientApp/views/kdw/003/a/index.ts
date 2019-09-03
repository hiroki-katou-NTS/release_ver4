import { _, Vue } from '@app/provider';
import { component, Watch } from '@app/core/component';
import { FixTableComponent } from '@app/components';

@component({
    name: 'kdw003a',
    route: '/kdw/003/a',
    style: require('./style.scss'),
    template: require('./index.vue'),
    resource: require('./resources.json'),
    constraints: [],
    components: {
        'fix-table': FixTableComponent
    },
    validations: {
        yearMonth: {
            required: false
        },
    }
})
export class Kdw003AComponent extends Vue {
    public title: string = 'Kdw003A';
    public displayFormat: number = 0;
    public lstDataSourceLoad: Array<any> = [];
    public lstDataHeader: Array<any> = [];
    public optionalHeader: Array<any> = [];
    public cellStates: Array<any> = [];
    public fixHeaders: Array<any> = [];
    public showPrincipal: boolean = true;
    public showSupervisor: boolean = true;
    public employeeModeHeader: Array<any> = [];
    public dateModeHeader: Array<any> = [];
    public errorModeHeader: Array<any> = [];
    public hasLstHeader: boolean = true;
    public displayDataLst: Array<any> = [];
    public hasErrorBuss: boolean = false;
    public lstCellDisByLock: Array<any> = [];
    public lstEmployee: Array<any> = [];
    public yearMonth: number = 0;
    public actualTimeOptionDisp: Array<any> = [];
    public actualTimeSelectedCode: number = 0;

    @Watch('yearMonth')
    public changeYearMonth(value: any) {
        let self = this;
        self.$http.post('at', servicePath.genDate, {yearMonth: value}).then((result: { data: any }) => {
            let data = result.data;
            _.remove(self.actualTimeOptionDisp);
            if (data.lstRange && data.lstRange.length > 0) {
                for (let i = 0; i < data.lstRange.length; i++) {
                    let startDate = data.lstRange[i].startDate,
                        endDate =  data.lstRange[i].endDate;
                    self.actualTimeOptionDisp.push({ code: i, name: (i + 1) + ': ' + this.$dt(startDate, 'M/D') + '～' + this.$dt(endDate, 'M/D')});
                }
            }
        });
    }

    public created() {
        this.startPage();
        this.$nextTick();
    }

    public mounted() {
        this.$mask('show', { message: true });
    }

    public startPage() {
        let self = this;
        let param = {
            dateRange: null,
            displayFormat: 0,
            initScreen: 1,
            mode: 0,
            lstEmployee: [],
            formatCodes: [],
            objectShare: null,
            showError: true,
            closureId: 1,
            initFromScreenOther: false
        };

        self.$http.post('at', servicePath.initMOB, param).then((result: { data: any }) => {
            let dataInit = result.data;
            if (dataInit.lstEmployee == undefined || dataInit.lstEmployee.length == 0 || dataInit.errorInfomation != 0) {
                let messageId = 'Msg_1342';
                if (dataInit.errorInfomation == DCErrorInfomation.APPROVAL_NOT_EMP) {
                    messageId = 'Msg_916';
                    self.hasErrorBuss = true;
                } else if (dataInit.errorInfomation == DCErrorInfomation.ITEM_HIDE_ALL) {
                    messageId = 'Msg_1452';
                    self.hasErrorBuss = true;
                } else if (dataInit.errorInfomation == DCErrorInfomation.NOT_EMP_IN_HIST) {
                    messageId = 'Msg_1543';
                    self.hasErrorBuss = true;
                }
                this.$modal.error({ messageId });
            } else if (!_.isEmpty(dataInit.errors)) {
                let errors = [];
                _.forEach(dataInit.errors, (error) => {
                    errors.push({
                        message: error.message,
                        messageId: error.messageId,
                        supplements: {}
                    });
                });
                // pending
            } else {
                this.processMapData(result.data);
                this.$mask('hide');
            }
        }).catch((res: any) => {
            if (res.messageId == 'Msg_672') {
                this.$modal.info({ messageId: res.messageId });
            } else {
                if (res.messageId != undefined) {
                    this.$modal.error(res.messageId == 'Msg_1430' ? res.message : { messageId: res.messageId }).then(() => {
                        this.$goto('ccg008a');
                    });

                } else if ((res.messageId == undefined && res.errors.length > 0)) {
                    //nts.uk.ui.dialog.bundledErrors({ errors: res.errors }).then(function () {
                    //nts.uk.request.jumpToTopPage();
                    //});

                }
            }
        });


    }

    public processMapData(data: any) {
        let self = this;
        self.lstDataSourceLoad = self.formatDate(data.lstData);
        self.optionalHeader = data.lstControlDisplayItem.lstHeader;
        self.cellStates = data.lstCellState;

        self.fixHeaders = data.lstFixedHeader;
        self.showPrincipal = data.showPrincipal;
        self.showSupervisor = data.showSupervisor;
        if (data.lstControlDisplayItem.lstHeader.length == 0) {
            self.hasLstHeader = false;
        }
        if (self.showPrincipal || data.lstControlDisplayItem.lstHeader.length == 0) {
            self.employeeModeHeader = [self.fixHeaders[0], self.fixHeaders[1], self.fixHeaders[2], self.fixHeaders[3], self.fixHeaders[4]];
            self.dateModeHeader = [self.fixHeaders[0], self.fixHeaders[1], self.fixHeaders[2], self.fixHeaders[5], self.fixHeaders[6], self.fixHeaders[7], self.fixHeaders[4]];
            self.errorModeHeader = [self.fixHeaders[0], self.fixHeaders[1], self.fixHeaders[2], self.fixHeaders[5], self.fixHeaders[6], self.fixHeaders[3], self.fixHeaders[7], self.fixHeaders[4]];
        } else {
            self.employeeModeHeader = [self.fixHeaders[0], self.fixHeaders[1], self.fixHeaders[2], self.fixHeaders[3]];
            self.dateModeHeader = [self.fixHeaders[0], self.fixHeaders[1], self.fixHeaders[2], self.fixHeaders[5], self.fixHeaders[6], self.fixHeaders[7]];
            self.errorModeHeader = [self.fixHeaders[0], self.fixHeaders[1], self.fixHeaders[2], self.fixHeaders[5], self.fixHeaders[6], self.fixHeaders[3], self.fixHeaders[7]];
        }
        if (self.showSupervisor) {
            self.employeeModeHeader.push(self.fixHeaders[8]);
            self.dateModeHeader.push(self.fixHeaders[8]);
            self.errorModeHeader.push(self.fixHeaders[8]);
        }

        self.yearMonth = data.periodInfo.yearMonth;
        self.lstEmployee = _.orderBy(data.lstEmployee, ['code'], ['asc']);

        self.lstDataHeader = _.isNil(self.lstDataSourceLoad) ? null : _.keys(self.lstDataSourceLoad[0]);
        self.lstDataSourceLoad.forEach((rowDataSrc: any) => {
            let rowData = [];
            for (let i = 0; i < self.lstDataHeader.length; i++) {
                let headerKey = self.lstDataHeader[i];
                
                let header1 = (_.filter(self.optionalHeader, (o) => o.key == headerKey))[0];
                if (!_.isNil(header1)) {
                    rowData.push({key: headerKey, value: rowDataSrc[headerKey], headerText: header1.headerText, color: header1.color});
                }
                let header2 = (_.filter(self.optionalHeader, (o) => !_.isEmpty(o.group) && o.group[1].key == headerKey))[0];
                if (!_.isNil(header2)) {
                    rowData.push({key: headerKey, value: rowDataSrc[headerKey], headerText: header2.headerText, color: header2.color});
                }
            }
            self.displayDataLst.push({rowData, date: rowDataSrc.date});
        });
    }

    public formatDate(lstData: any) {
        let data = lstData.map((data) => {
            let object = {
                id: '_' + data.id,
                state: data.state,
                error: data.error,
                date: this.$dt(data.date, 'Do(dd)'),
                sign: data.sign,
                approval: data.approval,
                employeeId: data.employeeId,
                employeeCode: data.employeeCode,
                employeeName: data.employeeName,
                workplaceId: data.workplaceId,
                employmentCode: data.employmentCode,
                dateDetail: this.$dt(data.date, 'YYYY/MM/DD'),
                typeGroup: data.typeGroup
            };
            _.each(data.cellDatas, function(item) {
                object[item.columnKey] = item.value;
            });

            return object;
        });

        return data;
    }
}
const servicePath = {
    initMOB: 'screen/at/correctionofdailyperformance/initMOB',
    genDate: 'screen/at/correctionofdailyperformance/gendate'
};
enum DCErrorInfomation {
    NORMAL = 0,
    APPROVAL_NOT_EMP = 1,
    ITEM_HIDE_ALL = 2,
    NOT_EMP_IN_HIST = 3
}