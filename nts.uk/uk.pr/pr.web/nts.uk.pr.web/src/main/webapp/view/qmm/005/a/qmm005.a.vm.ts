module nts.uk.pr.view.qmm005.a.viewmodel {
    import close = nts.uk.ui.windows.close;
    import getText = nts.uk.resource.getText;
    import model = nts.uk.pr.view.qmm005.share.model;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import block = nts.uk.ui.block;
    import CurrentProcessDate = nts.uk.pr.view.qmm005.share.model.CurrentProcessDate;
    import modal = nts.uk.ui.windows.sub.modal;
    import SetDaySupport = nts.uk.pr.view.qmm005.share.model.SetDaySupport;
    import current = nts.uk.request.location.current;
    const MAX_NUMBER_SETTING = 5;
    export class ScreenModel {
        //A2_2
        itemTable: ItemTable;
        //A3_4 対象雇用
        targetEmployment: KnockoutObservableArray<number>;
        processCategoryNO: number;
        itemBinding: KnockoutObservableArray<ItemBinding>;

        test: Array<model.ItemModel>;


        constructor() {
            var self = this;
            $("#A2_2").ntsFixedTable({height: 247, width: 1000});
            $("#A3_1").ntsFixedTable({height: 247, width: 400});
            //A3_4 対象雇用
            self.targetEmployment = ko.observable([]);
            self.itemBinding = ko.observableArray([]);
        }

        showDialogD(processInfomation, mode): void {
            let self = this;
            let param = {
                mode: mode,
                processInfomation: processInfomation
            }
            setShared("QMM005_output_D", param);
            modal('/view/qmm/005/d/index.xhtml', {title: '',}).onClosed(function (): any {
                let param = getShared("QMM005_output_A");
                let action: number = param.action;
                let processInformationUpdate = param.processInfomationUpdate;
                if (action == 0) {
                    let procescateno = processInformationUpdate.processCateNo;
                    self.itemBinding()[procescateno - 1].processInfomation.processDivisionName(processInformationUpdate.processDivisionName);
                }
                if (action == 1) {
                    self.itemBinding.removeAll();
                    self.startPage();
                }
            })
        }

        showDialogB(param): void {
            setShared("QMM005_output_B", param);
            modal('/view/qmm/005/b/index.xhtml', {title: '',}).onClosed(function (): any {
            })
        }

        showDialogF(processCateNo, employeeList): void {
            let self = this;
            let existList = [];
            for (let i = 0; i < self.itemBinding().length; i++) {
                existList = existList.concat(self.itemBinding()[i].employeeList());
            }
            let employeeArr = self.itemTable.empCdNameImports.filter(self.comparer(existList));
            let paramEmployment = {
                processCateNo: processCateNo,
                employeeList: employeeArr,
                employeeSelectedList: employeeList()
            }
            setShared("QMM005_output_F", paramEmployment);
            modal('/view/qmm/005/f/index.xhtml', {title: '',}).onClosed(function (): any {
                let employeeString = '';
                let params = getShared("QMM005F_outParams");
                if (!_.isUndefined(params)) {
                    for (let i = 0; i < params.returnList.length; i++) {
                        employeeString == '' ? employeeString += params.returnList[i].name : employeeString += (', ' + params.returnList[i].name);
                    }
                    for (let i = 0; i < MAX_NUMBER_SETTING; i++) {
                        if (self.itemBinding()[i].processInfomation.processCateNo == params.processCateNo) {
                            self.itemBinding()[i].employeeString(employeeString);
                            self.itemBinding()[i].employeeList(params.returnList);
                        }
                    }
                }
            });
        }

        comparer(otherArray) {
            return (current => {
                return otherArray.filter(other => {
                        return other.code == current.code && other.name == current.name
                    }).length == 0
            })
        }

        getYear(setDaySupportList: Array<SetDaySupport>, i: number): Array<ItemComboBox> {
            let self = this;
            let ArrSetDaySuport: Array<SetDaySupport> = _.sortBy(_.filter(self.itemTable.setDaySupports, function (o) {
                    return o.processCateNo == i + 1;
                }),
                function (o) {
                    return o.processDate;
                });
            let Araybinding: Array<ItemComboBox> = new Array<ItemComboBox>();
            for (let i = 0; i < ArrSetDaySuport.length; i++) {
                Araybinding.push(new ItemComboBox(
                    ArrSetDaySuport[i].processCateNo,
                    parseInt(ArrSetDaySuport[i].processDate / 100),
                    parseInt(ArrSetDaySuport[i].processDate / 100).toString()
                    )
                )
            }
            return _.sortBy(_.uniqBy(Araybinding, function (o) {
                return o.code;
            }), function (o) {
                return o.code;
            })
        }

        getListMonth(setDaySupportList: Array<SetDaySupport>, i: number): Array<ItemComboBox> {
            let self = this;
            let ArrSetDaySuport: Array<SetDaySupport> = _.sortBy(_.filter(self.itemTable.setDaySupports, function (o) {
                    return o.processCateNo == i + 1;
                }),
                function (o) {
                    return o.processDate;
                });
            let Araybinding: Array<ItemComboBox> = new Array<ItemComboBox>();
            for (let i = 0; i < ArrSetDaySuport.length; i++) {
                Araybinding.push(new ItemComboBox(
                    ArrSetDaySuport[i].processCateNo,
                    ArrSetDaySuport[i].processDate,
                        nts.uk.time.applyFormat("Short_YMDW", ArrSetDaySuport[i].paymentDate) + ' | ' + ArrSetDaySuport[i].empExtraRefeDate
                    )
                )
            }
            return Araybinding;
        }

        resetEmployee(processCateNo) {
            let self = this;
            self.itemBinding()[processCateNo - 1].employeeString('');
            self.itemBinding()[processCateNo - 1].employeeList([]);
        }

        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            service.findDisplayRegister().done(data => {
                self.itemTable = new ItemTable(data);
                if(data){
                    self.itemTable =new ItemTable(data);
                }
                for (let i: number = self.itemTable.processInfomations.length; i < MAX_NUMBER_SETTING; i++) {
                    self.itemTable.processInfomations.push(new model.ProcessInfomation({
                            processCateNo: i + 1,
                            processDivisionName: '',
                            deprecatCate: 0
                        }
                    ));
                }
                for (let i: number = 0; i < MAX_NUMBER_SETTING; i++) {
                    let employeeString = '';
                    let employeeList = [];
                    let employeeSetting = _.find(self.itemTable.empTiedProYear, x => {
                        if (x != null) {
                            return x.processCateNo == i + 1;
                        }
                    })
                    if (employeeSetting) {
                        for (let j = 0; j < employeeSetting.getEmploymentCodes.length; j++) {
                            let obj = _.find(self.itemTable.empCdNameImports, x => {
                                return x.code == employeeSetting.getEmploymentCodes[j];
                            })
                            employeeList.push(obj);
                            employeeString == '' ? employeeString += obj.name : employeeString += (', ' + obj.name);
                        }
                    }
                    self.itemBinding.push(new ItemBinding(
                        self.itemTable.processInfomations[i],
                        _.sortBy(_.filter(self.itemTable.setDaySupports, function (o) {
                                return o.processCateNo == i + 1;
                            }),
                            function (o) {
                                return o.processDate;
                            }),
                        employeeString, employeeList,

                        self.getYear(self.itemTable.setDaySupports, i)
                        ,
                        self.getListMonth(self.itemTable.setDaySupports, i)
                    ));

                }
            });
            dfd.resolve();
            return dfd.promise();
        }

        registerProcessing() {
            let self = this;
            let commandData = {currProcessDateCommand: [], empTiedProYearCommand: []};
            for (let i = 0; i < MAX_NUMBER_SETTING; i++) {
                if (self.itemBinding()[i].processInfomation.processDivisionName != '') {
                    commandData.currProcessDateCommand.push({giveCurrTreatYear: self.itemBinding()[i].monthsSelectd()});
                    let codeList = _.map(self.itemBinding()[i].employeeList(), "code");
                    if (codeList.length > 0) {
                        commandData.empTiedProYearCommand.push({employmentCodes: codeList});
                    } else {
                        nts.uk.ui.dialog.error({messageId: "MsgQ_217"});
                        return;
                    }
                }
            }
            service.registerProcessing(commandData).done(function (data) {
                nts.uk.ui.dialog.info({messageId: "Msg_15"});
            }).fail(function (error) {
            });
        }
    }

    export class ItemBinding {
        processInfomation: model.ProcessInfomation;
        setDaySupports: KnockoutObservableArray<model.SetDaySupport>;
        setDaySupportsSelectedCode: KnockoutObservable<number>;
        employeeString: KnockoutObservable<string>;
        employeeList: KnockoutObservableArray<any>;
        years: KnockoutObservableArray<ItemComboBox>;
        yaersSelected: KnockoutObservable<number>;
        months: KnockoutObservableArray<ItemComboBox>;
        monthsSubcriceYear: KnockoutObservableArray<ItemComboBox> = ko.observableArray([]);
        monthsSelectd: KnockoutObservable<number>;

        constructor(processInfomation: model.ProcessInfomation, setDaySupports: Array<model.SetDaySupport>, employeeString: string, employeeList: Array, years: Array<ItemComboBox>, months: Array<ItemComboBox>) {
            var self = this;
            self.processInfomation = processInfomation;
            self.setDaySupports = ko.observableArray(setDaySupports);
            self.setDaySupportsSelectedCode = ko.observable(0);
            self.employeeString = ko.observable(employeeString);
            self.employeeList = ko.observableArray(employeeList);
            self.years = ko.observableArray(years);
            self.yaersSelected = ko.observable(0);
            self.months = ko.observableArray(months);
            self.monthsSelectd = ko.observable(0);


            self.yaersSelected.subscribe(function (data) {
                self.monthsSubcriceYear.removeAll();
                self.monthsSubcriceYear(
                    _.filter(self.months(), function (o) {
                        return parseInt(o.code / 100) == data;
                    })
                )
            })

        }
    }

    export interface IitemTable {
        informationDto: Array<model.ProcessInfomation>,
        setDaySupportDto: Array<model.SetDaySupport>,
        currProcessDateDto: Array<model.CurrentProcessDate>,
        empTiedProYearDto: Array<model.EmpTiedProYear>,
        empCdNameImports: Array<model.EmpCdNameImport>
    }

    export class ItemTable {
        processInfomations: Array<model.ProcessInfomation> = [];
        setDaySupports: Array<model.SetDaySupport> = [];
        currentProcessDates: Array<model.CurrentProcessDate> = [];
        empCdNameImports: Array<model.EmpCdNameImport> = [];
        empTiedProYear: Array<model.EmpTiedProYear> = [];

        constructor(param: IitemTable) {
            if (param) {
                this.processInfomations = param.informationDto;
                this.currentProcessDates = param.currProcessDateDto;
                this.setDaySupports = param.setDaySupportDto;
                this.empCdNameImports = param.empCdNameImports;
                this.empTiedProYear = param.empTiedProYearDto;
            }
        }
    }

    export class ItemComboBox {
        processNO: number;
        code: number;
        name: string;

        constructor(processNO: number, code: number, name: string) {
            this.processNO = processNO;
            this.code = code;
            this.name = name;
        }
    }
}

