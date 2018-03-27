module nts.uk.at.view.kmk011.b {
    import blockUI = nts.uk.ui.block;
    import setShared = nts.uk.ui.windows.setShared;
    import modal = nts.uk.ui.windows.sub.modal;
    export module viewmodel {

        export class ScreenModel {
            columns: KnockoutObservableArray<any>;
            dataSource: KnockoutObservableArray<viewmodel.model.DivergenceTime>;
            currentCode: KnockoutObservable<number>;

            useSet: KnockoutObservableArray<any>;//list value swith button
            useSetBool:KnockoutObservableArray<any>;//list bool value swith button

            //B3_2
            selectUse: KnockoutObservable<any>;//value of B3_2
            enableUse: KnockoutObservable<boolean>; // for define enable for elements behind

            divergenceTypeName: KnockoutObservable<string>; // B4_1 show

            divTimeName: KnockoutObservable<string>;//divergence name B3_4
            timeItemName: KnockoutObservable<string>;//list name KDL021 - B3_7

            //B3_13:swith button, B3_15
            selectSelect: KnockoutObservable<boolean>;//value B3_13
            enableSelect: KnockoutObservable<boolean>; // for define enable for B3_15 checkbox
            checkErrorSelect: KnockoutObservable<boolean>; //value B3_15

            //B3_17:siwth button, B3_18
            selectInput: KnockoutObservable<boolean>; //value B3_17
            enableInput: KnockoutObservable<boolean>;
            checkErrorInput: KnockoutObservable<boolean>;

            existedDivergenceReason: KnockoutObservable<boolean>; //define view for B3_19, B3_20

            enableState: KnockoutObservable<boolean>;
            divergenceTimeId: KnockoutObservable<number>;
            itemDivergenceTime: KnockoutObservable<viewmodel.model.DivergenceTime>;
            listDivergenceItem: KnockoutObservableArray<viewmodel.model.DivergenceItem>;
            listItemSelected: KnockoutObservableArray<viewmodel.model.DivergenceItem>;
            listItem: KnockoutObservableArray<number>;
            demoDivTime: model.DivergenceTime;


            use: KnockoutObservable<string>;
            notUse: KnockoutObservable<string>;
            check: boolean;



            constructor() {
                var self = this;

                self.check = false;
                self.enableState = ko.observable(true);
                self.listItem = ko.observableArray([]);
                self.enableUse = ko.observable(false);
                self.enableSelect = ko.observable(false);
                self.enableInput = ko.observable(false);
                self.currentCode = ko.observable(1);
                self.columns = ko.observableArray([
                    { headerText: nts.uk.resource.getText('KMK011_4'), key: 'divergenceTimeNo', width: 100 },
                    { headerText: nts.uk.resource.getText('KMK011_5'), key: 'divergenceTimeName', formatter: _.escape, width: 200 }
                ]);                
                self.dataSource = ko.observableArray([]);
                self.useSet = ko.observableArray([
                    { code: '1', name: nts.uk.resource.getText("Enum_UseAtr_Use") },
                    { code: '0', name: nts.uk.resource.getText("Enum_UseAtr_NotUse") },
                ]);

                self.useSetBool = ko.observableArray([
                    { isUse: true, name: nts.uk.resource.getText("Enum_UseAtr_Use") },
                    { isUse: false, name: nts.uk.resource.getText("Enum_UseAtr_NotUse") },
                ]);

                self.selectUse = ko.observable(0);
                self.divergenceTypeName = ko.observable('');
                self.divTimeName = ko.observable('');
                self.timeItemName = ko.observable('');
                self.checkErrorSelect = ko.observable(true);
                self.divergenceTimeId = ko.observable(null);
                self.itemDivergenceTime = ko.observable(null);

                self.selectSelect = ko.observable(false);
                self.selectInput = ko.observable(false);

                self.checkErrorInput = ko.observable(false);
                self.checkErrorSelect = ko.observable(false);
                self.listDivergenceItem = ko.observableArray([]);
                self.listItemSelected = ko.observableArray([]);
                self.existedDivergenceReason = ko.observable(false);
                //subscribe currentCode
                self.currentCode.subscribe(function(codeChanged) {
                    self.clearError();
                    if (codeChanged == 0) {
                        self.enableState(false);
                        self.refreshData();
                        return;
                    }
                    self.enableState(true);
                    self.selectUse(null);
                    self.findDivergenceTime(self.currentCode()).done((itemDivTime) => {
                        self.itemDivergenceTime(itemDivTime);
                        self.setValueDivergenceTimeDisplay();
                    });

                    //
                    //                    self.check = false;
                    //                    $("#itemname").focus();
                });
                //subscribe selectUse
                self.selectUse.subscribe(function(codeChanged) {
                    if (codeChanged == 1) {
                        self.enableUse(true);
                        if (self.selectSelect()) {
                            self.enableSelect(true);
                        } else {
                            self.enableSelect(false);
                        }
                        if (self.selectInput()) {
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
                self.selectSelect.subscribe(function(codeChanged) {
                    if (codeChanged  && self.selectUse() == 1) {
                        self.enableSelect(true);
                    } else {
                        self.enableSelect(false);
                        if ($('#inpDialog').ntsError("hasError") == true) {
                            $('#inpDialog').ntsError('clear');
                        }
                    }
                });
                //subscribe selectInp
                self.selectInput.subscribe(function(codeChanged) {
                    if (codeChanged && self.selectUse() == 1) {
                        self.enableInput(true);
                    } else {
                        self.enableInput(false);
                    }
                });
            }

            public start_page(): JQueryPromise<any> {

                var self = this;
                blockUI.invisible();
                var dfd = $.Deferred();

                service.getAllDivergenceTime().done(function(lstDivTime: Array<model.DivergenceTime>) {
                    blockUI.clear();
                    if (lstDivTime === undefined || lstDivTime.length == 0) {
                        self.dataSource();
                    } else {
                        self.dataSource(lstDivTime);
                        let rdivTimeFirst = _.first(lstDivTime);
                        self.currentCode(rdivTimeFirst.divergenceTimeNo);
                        self.findDivergenceTime(self.currentCode()).done((itemDivTime) => {
                            self.itemDivergenceTime(itemDivTime);
                            //get divergenceTypeName method this
                            self.setValueDivergenceTimeDisplay();
                            $("#itemname").focus();
                        });
                    }
                    dfd.resolve();
                })
                return dfd.promise();
            }
            /**
             * find Divergence Time is selected
             */
            private findDivergenceTime(divergenceTimeNo: number): JQueryPromise<any> {
                var self = this;
                var dfd = $.Deferred();
                service.findDivergenceTime(divergenceTimeNo).done(function(itemDivergenceTime: model.DivergenceTime) {
                    dfd.resolve(itemDivergenceTime);
                });
                $("#itemname").focus();
                return dfd.promise();
            }
            /**
             * refreshData 
             */
            refreshData() {
                var self = this;
                self.currentCode(0);
                //B3_2
                self.selectUse(0)
                self.enableUse(false);
                self.divergenceTypeName('');

                self.divTimeName('');
                self.timeItemName('');

                //B3_13:swith button, B3_15
                self.selectSelect(false);
                self.enableSelect(false);
                self.checkErrorSelect(false);

                //B3_17:siwth button, B3_18
                self.selectInput(false);
                self.enableInput(false);
                self.checkErrorInput(false);

                self.existedDivergenceReason(false);

            }

            /**
             * set display value divergence item
             */
            private setValueDivergenceTimeDisplay() {
                var self = this;
                self.selectUse(self.itemDivergenceTime().divergenceTimeUseSet);
                self.selectSelect(self.itemDivergenceTime().divergenceReasonSelected);
                self.selectInput(self.itemDivergenceTime().divergenceReasonInputed);
                self.divergenceTimeId(self.itemDivergenceTime().divergenceTimeNo);
                self.divTimeName(self.itemDivergenceTime().divergenceTimeName);
                self.divergenceTypeName(self.itemDivergenceTime().divergenceType);
                service.getAllDivReason(self.itemDivergenceTime().divergenceTimeNo).done((data) => {
                    if (data.length != 0) {
                        self.existedDivergenceReason(true);
                    } else {
                        self.existedDivergenceReason(false);
                    }
                });

                self.timeItemName('');

                //set name KDL selected for divergenceTime
                self.listItemSelected();
                var listItemAttendanceId = self.itemDivergenceTime().targetItems; //list attendanceId
                if (listItemAttendanceId == null || listItemAttendanceId === undefined || listItemAttendanceId.length == 0) {
                    self.timeItemName();
                    self.listItemSelected([]);
                } else {
                    service.getNameItemSelected(listItemAttendanceId).done(function(lstName: Array<model.DivergenceItem>) {
                        self.listItemSelected(lstName);
                        self.findTimeName(self.divergenceTimeId());
                    });
                }

                if (self.itemDivergenceTime().reasonInput) {
                    self.checkErrorInput(true);
                } else {
                    self.checkErrorInput(false);
                }
                if (self.itemDivergenceTime().reasonSelect) {
                    self.checkErrorSelect(true);
                } else {
                    self.checkErrorSelect(false);
                }
            }
            //create divergence
            public Registration() {
                var self = this;
                blockUI.invisible();
                $('.nts-input').trigger("validate");
                _.defer(() => {
                    if (nts.uk.ui.errors.hasError() === false) {
                        var dfd = $.Deferred();
                        if (self.divergenceTimeId() == null) {
                            return;
                        }
                        var listIdSelect : number[] = [];
                        for (let i = 0; i < self.listItemSelected().length; i++) {
                            listIdSelect[i] = self.listItemSelected()[i].attendanceItemId;
                        }                        
                        
                        
                        var divergenceTime = new model.DivergenceTime(self.divergenceTimeId(), self.selectUse(), self.divTimeName(),
                            self.divergenceTypeName(), self.checkErrorInput(), self.checkErrorSelect(),
                            self.selectInput(),self.selectSelect(), listIdSelect);


                        var listAdd = new Array<model.TimeItemSet>();
                        if (self.listItem() != null) {
                            for (let k = 0; k < self.listItem().length; k++) {
                                let add = new model.TimeItemSet(self.divergenceTimeId(), self.listItem()[k]);
                                listAdd.push(add);
                            }
                        }

                        //                        var Object = new model.ObjectDivergence(divTime, listAdd, self.timeItemName());
                        service.updateDivTime(divergenceTime).done(function() {
                            self.getAllDivTimeNew();
                            nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                            self.listItem([]);
                            self.check = false;
                            $("#itemname").focus();
                            nts.uk.ui.block.clear();

                        }).fail(function(error) {
                            nts.uk.ui.block.clear();
                        })
                        dfd.resolve();
                        return dfd.promise();
                    }
                })
                if (self.listItemSelected().length == 0 && self.selectUse()==1) {
                    nts.uk.ui.dialog.info({ messageId: "Msg_1008" });
                }
                nts.uk.ui.block.clear();
            }
            private clearError(): void {
                if ($('.nts-validate').ntsError("hasError") == true) {
                    $('.nts-validate').ntsError('clear');
                }
                if ($('.nts-editor').ntsError("hasError") == true) {
                    $('.nts-input').ntsError('clear');
                }
            }

            //get all divergence time new
            private getAllDivTimeNew() {
                var self = this;
                var dfd = $.Deferred<any>();
                self.dataSource();
                service.getAllDivergenceTime().done(function(lstDivTime: Array<model.DivergenceTime>) {
                    self.dataSource(lstDivTime);
                    self.currentCode(self.divergenceTimeId());
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
                if (self.listItemSelected().length < 1) {
                    self.timeItemName('');
                } else {
                    strName = self.listItemSelected()[0].attendanceItemName;
                    if (self.listItemSelected().length > 1) {
                        for (let j = 1; j < self.listItemSelected().length; j++) {
                            strName = strName + ' + ' + self.listItemSelected()[j].attendanceItemName;
                        }
                    }
                    self.timeItemName(strName);
                    strName = '';
                }
            }
            //open Diaglog 021
            public openDialog021() {
                var self = this;
                nts.uk.ui.block.grayout();
                service.getAllAttendanceItem(1).done(function(lstAllItem: Array<model.AttendanceType>) {
                    var listAllId : number[]= [];
                    for (let j = 0; j < lstAllItem.length; j++) {
                        listAllId[j] = lstAllItem[j].attendanceItemId;
                    }
                    var listIdSelect : number[] = [];
                    for (let i = 0; i < self.listItemSelected().length; i++) {
                        listIdSelect[i] = self.listItemSelected()[i].attendanceItemId;
                    }
                    setShared('AllAttendanceObj', listAllId, true);
                    setShared('SelectedAttendanceId', listIdSelect, true);
                    setShared('Multiple', true, true);
                    modal('../../../kdl/021/a/index.xhtml', { title: '乖離時間の登録＞対象項目', }).onClosed(function(): any {
                        blockUI.clear();
                        $("#itemname").focus();
                        var list = nts.uk.ui.windows.getShared('selectedChildAttendace');
                        if (list == null || list === undefined) return;
                        self.listItem(list);
                        var listUpdate = new Array<model.DivergenceTimeItem>();
                        for (let i = 0; i < list.length; i++) {
                            let itemUpdate = new model.DivergenceTimeItem(self.divergenceTimeId(), list[i]);
                            listUpdate.push(itemUpdate);
                        }
                        service.getNameItemSelected(list).done(function(lstName: Array<model.DivergenceItem>) {
                            self.listItemSelected(lstName);
                            self.findTimeName(self.divergenceTimeId());
                            if (self.timeItemName() != '') {
                                if ($('#inpName').ntsError("hasError") == true) {
                                    $('#inpName').ntsError('clear');
                                }
                            }
                        })
                    })
                });
            }

            //open C dialog
            public openCDialog() {
                var self = this;
                blockUI.grayout();
                setShared('KMK011_divTimeId', self.divergenceTimeId(), true);
                var test :boolean;
                if (self.selectInput()) {
                    test =true;    
                }
                else test =false
                    nts.uk.ui.windows.setShared('selectInput', test);
                
                nts.uk.ui.windows.sub.modal('/view/kmk/011/c/index.xhtml', { title: '選択肢の設定', }).onClosed(function(): any {
                    blockUI.clear();
                    if ($('#inpDialog').ntsError("hasError") == true) {
                        $('#inpDialog').ntsError('clear');
                    }
                    service.getAllDivReason(self.itemDivergenceTime().divergenceTimeNo).done((data) => {
                        if (data.length != 0) {
                            self.existedDivergenceReason(true);
                        } else {
                            self.existedDivergenceReason(false);
                        }
                    });
                    $("#itemname").focus();
                });
            }
        }

        export module model {
            export class Demo {
                attendanceId: number;
            }
            export class DivergenceTime {
                divergenceTimeNo: number;
                divergenceTimeUseSet: number;
                divergenceTimeName: string;
                divergenceType: string;
                reasonInput: boolean;
                reasonSelect: boolean;
                divergenceReasonInputed: boolean;
                divergenceReasonSelected: boolean;
                targetItems: number[];

                constructor(divergenceTimeNo: number, divergenceTimeUseSet: number, divergenceTimeName: string,
                    divergenceType: string, reasonInput: boolean, reasonSelect: boolean,
                    divergenceReasonInputed: boolean, divergenceReasonSelected: boolean, targetItems: number[]
                ) {
                    this.divergenceTimeNo = divergenceTimeNo;
                    this.divergenceTimeUseSet = divergenceTimeUseSet;
                    this.divergenceTimeName = divergenceTimeName;
                    this.divergenceType = divergenceType;
                    this.reasonInput = reasonInput;
                    this.reasonSelect = reasonSelect;
                    this.divergenceReasonInputed = divergenceReasonInputed;
                    this.divergenceReasonSelected = divergenceReasonSelected;
                    this.targetItems = targetItems;
                }
                public resetData() {
                    this.divergenceTimeNo = 0;
                    this.divergenceTimeUseSet = 0;
                    this.divergenceTimeName = '';
                    this.divergenceType = '';
                    this.reasonInput = false;
                    this.reasonSelect = false;
                    this.divergenceReasonInputed = false;
                    this.divergenceReasonSelected = false;
                    this.targetItems = [];

                }

            }
            export class AttendanceType {
                attendanceItemId: number;
            }
            export class DivergenceTimeItem {
                divTimeId: number;
                attendanceId: number;
                constructor(divTimeId: number, attendanceId: number) {
                    this.divTimeId = divTimeId;
                    this.attendanceId = attendanceId;
                }
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
            export class DivergenceReason {
                divergenceTimeNo: number;
                divergenceReasonCode: string;
                reason: string;
                reasonRequired: number;
                constructor(divTimeId: number, divReasonCode: string, divReasonContent: string, requiredAtr: number) {
                    this.divergenceTimeNo = divTimeId;
                    this.divergenceReasonCode = divReasonCode;
                    this.reason = divReasonContent;
                    this.reasonRequired = requiredAtr;
                }
            }

        }
    }
}