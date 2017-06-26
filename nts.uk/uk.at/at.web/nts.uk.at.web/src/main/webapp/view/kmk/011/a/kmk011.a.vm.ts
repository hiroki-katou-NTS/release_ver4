module kmk011.a.viewmodel {
    export class ScreenModel {
        //A_label_x
        columns: KnockoutObservableArray<any>;
        dataSource: KnockoutObservableArray<model.Item>;
        currentCode: KnockoutObservable<any>;
        useSet: KnockoutObservableArray<any>;
        selectUse: KnockoutObservable<any>;
        selectSel: KnockoutObservable<any>;
        selectInp: KnockoutObservable<any>;
        divTimeName: KnockoutObservable<string>;
        timeItemName: KnockoutObservable<string>;
        alarmTime: KnockoutObservable<string>;
        errTime: KnockoutObservable<string>;
        checkErrInput: KnockoutObservable<boolean>;
        checkErrSelect: KnockoutObservable<boolean>;
        enable: KnockoutObservable<boolean>;
        divTimeId: KnockoutObservable<number>;
        itemDivTime: KnockoutObservable<model.DivergenceTime>;
        listDivItem: KnockoutObservableArray<model.DivergenceItem>;
        lstItemSelected: KnockoutObservableArray<model.DivergenceItem>;
        list: KnockoutObservable<string>;
        use: KnockoutObservable<string>;
        notUse: KnockoutObservable<string>;
        enableUse: KnockoutObservable<boolean>;
        enableSelect: KnockoutObservable<boolean>;
        enableInput: KnockoutObservable<boolean>;
        constructor() {
            var self = this;
            self.list = ko.observable();
            self.enableUse = ko.observable();
            self.enableSelect = ko.observable();
            self.enableInput = ko.observable();
            self.currentCode = ko.observable(1);
            self.columns = ko.observableArray([
                { headerText: nts.uk.resource.getText('KMK011_4'), key: 'divTimeId', width: 100 },
                { headerText: nts.uk.resource.getText('KMK011_5'), key: 'divTimeName', width: 200 }
            ]);
            self.dataSource = ko.observableArray([]);
            self.useSet = ko.observableArray([
                { code: '1', name: nts.uk.resource.getText("Enum_UseAtr_Use") },
                { code: '0', name: nts.uk.resource.getText("Enum_UseAtr_NotUse") },
            ]);
            self.divTimeName = ko.observable('');
            self.timeItemName = ko.observable('');
            self.checkErrSelect = ko.observable(true);
            self.enable = ko.observable(true);
            self.divTimeId = ko.observable(1);
            self.itemDivTime = ko.observable(null);
            self.selectUse = ko.observable(0);
            self.alarmTime = ko.observable();
            self.errTime = ko.observable();
            self.selectSel = ko.observable();
            self.selectInp = ko.observable();
            self.checkErrInput = ko.observable(false);
            self.checkErrSelect = ko.observable(false);
            self.listDivItem = ko.observableArray([]);
            self.lstItemSelected = ko.observableArray([]);
            //subscribe currentCode
            self.currentCode.subscribe(function(codeChanged) {
                self.clearError();
                if (codeChanged == 0) { return; }
                self.selectUse(null);
                self.itemDivTime(self.findDivTime(codeChanged));
                self.alarmTime(self.convertTime(self.itemDivTime().alarmTime));
                self.errTime(self.convertTime(self.itemDivTime().errTime));
                self.selectUse(self.itemDivTime().divTimeUseSet);
                self.selectSel(self.itemDivTime().selectSet.selectUseSet);
                self.selectInp(self.itemDivTime().inputSet.selectUseSet);
                self.divTimeId(self.itemDivTime().divTimeId);
                self.divTimeName(self.itemDivTime().divTimeName);
                service.getItemSelected(self.divTimeId()).done(function(lstItem: Array<model.TimeItemSet>) {
                    var listItemId = [];
                    for (let j = 0; j < lstItem.length; j++) {
                        listItemId[j] = lstItem[j].attendanceId;
                    }
                    service.getNameItemSelected(listItemId).done(function(lstName: Array<model.DivergenceItem>){
                       self.lstItemSelected(lstName);
                        self.findTimeName(self.divTimeId()); 
                    });

                })
                if (self.itemDivTime().inputSet.cancelErrSelReason == 1) {
                    self.checkErrInput(true);
                } else {
                    self.checkErrInput(false);
                }
                if (self.itemDivTime().selectSet.cancelErrSelReason == 1) {
                    self.checkErrSelect(true);
                } else {
                    self.checkErrSelect(false);
                }
            });
            //subscribe selectUse
            self.selectUse.subscribe(function(codeChanged) {
                if (codeChanged == 1) {
                    self.enableUse(true);
                    if (self.selectSel() == 1) {
                        self.enableSelect(true);
                    } else {
                        self.enableSelect(false);
                    }
                    if (self.selectInp() == 1) {
                        self.enableInput(true);
                    } else {
                        self.enableInput(false);
                    }
                } else {
                    self.enableUse(false);
                    self.enableSelect(false);
                    self.enableInput(false);
                }
            });
            //subscribe selectSel
            self.selectSel.subscribe(function(codeChanged) {
                if (codeChanged == 1 && self.selectUse()==1) {
                    self.enableSelect(true);
                } else {
                    self.enableSelect(false);
                }
            });
            //subscribe selectInp
            self.selectInp.subscribe(function(codeChanged) {
                if (codeChanged == 1 && self.selectUse()==1) {
                    self.enableInput(true);
                } else {
                    self.enableInput(false);
                }
            });

        }
        /**
         * start page
         * get all divergence time
         * get all divergence name
         */
        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            service.getAllDivTime().done(function(lstDivTime: Array<model.DivergenceTime>) {
                if (lstDivTime === undefined || lstDivTime.length == 0) {
                    self.dataSource();
                } else {
                    self.currentCode(0);
                    self.dataSource(lstDivTime);
                    let rdivTimeFirst = _.first(lstDivTime);
                    self.currentCode(rdivTimeFirst.divTimeId);
                }
                dfd.resolve();
            })
            return dfd.promise();
        }
        /**
         * find Divergence Time is selected
         */
        findDivTime(value: number): any {
            let self = this;
            var itemModel = null;
            return _.find(self.dataSource(), function(obj: model.DivergenceTime) {
                return obj.divTimeId == value;
            })
        }
        openBDialog() {
            var self = this;
            nts.uk.ui.windows.setShared('KMK011_divTimeId', self.divTimeId(), true);
            nts.uk.ui.windows.sub.modal('/view/kmk/011/b/index.xhtml', { title: '選択肢の設定', })
        }
        openDialog021() {
            var self = this;
            service.getAllAttItem(1).done(function(lstAllItem: Array<model.AttendanceType>) {
                var listAllId = [];
                for (let j = 0; j < lstAllItem.length; j++) {
                    listAllId[j] = lstAllItem[j].attendanceItemId;
                }
                var listIdSelect = [];
                for (let i = 0; i < self.lstItemSelected().length; i++) {
                    listIdSelect[i] = self.lstItemSelected()[i].attendanceItemId;
                }
                nts.uk.ui.windows.setShared('AllAttendanceObj', listAllId, true);
                nts.uk.ui.windows.setShared('SelectedAttendanceId', listIdSelect, true);
                nts.uk.ui.windows.setShared('Multiple', true, true);
                nts.uk.ui.windows.sub.modal('../../../kdl/021/a/index.xhtml', { title: '乖離時間の登録＞対象項目', }).onClosed(function(): any {
                    var list = nts.uk.ui.windows.getShared('selectedChildAttendace');
                    if(list == null || list === undefined) return;
                    self.list(list);
                    var listUpdate = new Array<model.DivergenceTimeItem>();
                    for (let i = 0; i < list.length; i++) {
                        let itemUpdate = new model.DivergenceTimeItem(self.divTimeId(), list[i]);
                        listUpdate.push(itemUpdate);
                    }
                    service.getNameItemSelected(list).done(function(lstName: Array<model.DivergenceItem>) {
                        self.lstItemSelected(lstName);
                        self.findTimeName(self.divTimeId());
                    })
                })
            });
        }
        Registration() {
            var self = this;
            $('.nts-input').trigger("validate");
            _.defer(() => {
                if (nts.uk.ui.errors.hasError() === false) {
                    var dfd = $.Deferred();
                    var select = new model.SelectSet(self.selectSel(), self.convert(self.checkErrSelect()));
                    var input = new model.SelectSet(self.selectInp(), self.convert(self.checkErrInput()));
                    var divTime = new model.DivergenceTime(self.divTimeId(), self.divTimeName(), self.selectUse(), self.convertInt(self.alarmTime()), self.convertInt(self.errTime()), select, input);
                    var listAdd = new Array<model.TimeItemSet>();
                    if (self.list() != null) {
                        for (let k = 0; k < self.list().length; k++) {
                            let add = new model.TimeItemSet(self.divTimeId(), self.list()[k]);
                            listAdd.push(add);
                        }
                    }
                    var Object = new model.ObjectDivergence(divTime, listAdd);
                    service.updateDivTime(Object).done(function() {
                        self.getAllDivTimeNew();
                        nts.uk.ui.dialog.alert(nts.uk.resource.getMessage('Msg_15'));
                    }).fail(function(error) {
                        if (error.messageId == 'Msg_82') {
                            $('#inpAlarmTime').ntsError('set', error);
                        } else {
                            $('#inpDialog').ntsError('set', error);
                        }
                    })
                    dfd.resolve();
                    return dfd.promise();
                }
            })
        }
        convert(value: boolean): number {
            if (value == true) {
                return 1;
            } else
                if (value == false) {
                    return 0;
                }
        }
        convertTime(value: number): string {
            var hours = Math.floor(value / 60);
            var minutes = value % 60;
            return (hours < 10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes);
        }
        convertInt(value: string): number {
            if (value == '' || value == null || value === undefined) {
                return 0;
            } else {
                var hours = value.substring(0, 2);
                var minutes = value.substring(3, 5);
                return (parseFloat(hours) * 60 + parseFloat(minutes));
            }
        }
        clearError(): void {
            if ($('.nts-validate').ntsError("hasError")==true) {
                $('.nts-validate').ntsError('clear');
            }
            if($('.nts-editor').ntsError("hasError")==true){
                $('.nts-input').ntsError('clear');
            }
        }
        //get all divergence time new
        getAllDivTimeNew() {
            var self = this;
            var dfd = $.Deferred<any>();
            self.dataSource();
            service.getAllDivTime().done(function(lstDivTime: Array<model.DivergenceTime>) {
                self.currentCode('');
                self.dataSource(lstDivTime);
                self.currentCode(self.divTimeId());
                dfd.resolve();
            }).fail(function(error) {
                nts.uk.ui.dialog.alert(error.message);
            })
            dfd.resolve();
            return dfd.promise();
        }
        //ghep ten hien thi 
        findTimeName(divTimeId: number) {
            var self = this;
            self.timeItemName('');
            var strName = '';
            if (self.lstItemSelected().length < 1) {
                self.timeItemName('');
            } else {
                strName = self.lstItemSelected()[0].attendanceItemName;
                if (self.lstItemSelected().length > 1) {
                    for (let j = 1; j < self.lstItemSelected().length; j++) {
                        strName = strName + ' + ' + self.lstItemSelected()[j].attendanceItemName;
                    }
                }
                self.timeItemName(strName);
                strName = '';
            }
        }
    }
    export module model {
        export class DivergenceTime {
            divTimeId: number;
            divTimeUseSet: number;
            divTimeName: string;
            alarmTime: number;
            errTime: number;
            selectSet: SelectSet;
            inputSet: SelectSet;
            constructor(divTimeId: number, divTimeName: string,
                divTimeUseSet: number,
                alarmTime: number, errTime: number,
                selectSet: SelectSet,
                inputSet: SelectSet) {
                var self = this;
                self.divTimeId = divTimeId;
                self.divTimeName = divTimeName;
                self.divTimeUseSet = divTimeUseSet;
                self.alarmTime = alarmTime;
                self.errTime = errTime;
                self.selectSet = selectSet;
                self.inputSet = inputSet;
            }
        }
        export class SelectSet {
            selectUseSet: number;
            cancelErrSelReason: number;
            constructor(selectUseSet: number, cancelErrSelReason: number) {
                this.selectUseSet = selectUseSet;
                this.cancelErrSelReason = cancelErrSelReason;
            }
        }
        export class DivergenceTimeItem {
            divTimeId: number;
            attendanceId: number;
            constructor(divTimeId: number, attendanceId: number) {
                this.divTimeId = divTimeId;
                this.attendanceId = attendanceId;
            }
        }
        export class ItemSelected {
            id: number;
            name: string;
            constructor(id: number, name: string) {
                this.id = id;
                this.name = name;
            }
        }
        export class AttendanceType {
            attendanceItemId: number;
        }
        export class DivergenceItem {
            attendanceItemId: number;
            attendanceItemName: string;
            displayNumber: number;
            useAtr: number;
            attendanceAtr: number;
        }
        export class TimeItemSet {
            divTimeId: number;
            attendanceId: number;
            constructor(divTimeId: number, attendanceId: number) {
                this.divTimeId = divTimeId;
                this.attendanceId = attendanceId;
            }
        }
        export class ObjectDivergence {
            divTime: DivergenceTime;
            timeItem: List<TimeItemSet>;
            constructor(divTime: DivergenceTime, item: List<TimeItemSet>) {
                this.divTime = divTime;
                this.timeItem = item;
            }
        }
    }
}