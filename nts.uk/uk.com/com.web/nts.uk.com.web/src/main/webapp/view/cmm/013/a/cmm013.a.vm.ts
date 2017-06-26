module cmm013.a.viewmodel {
    export class ScreenModel {
        label_003: KnockoutObservable<Labels>;
        referenceSettings: KnockoutObservable<number>;
        roundingRules: KnockoutObservableArray<any>;
        inp_002_code: KnockoutObservable<string>;
        inp_002_enable: KnockoutObservable<boolean>;
        inp_003_name: KnockoutObservable<string>;
        inp_005_memo: KnockoutObservable<string>;
        listbox: KnockoutObservableArray<model.ListHistoryDto>;
        itemHist: KnockoutObservable<model.ListHistoryDto>;
        dataSource: KnockoutObservableArray<model.ListPositionDto>;
        columns: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
        currentItem: KnockoutObservable<model.ListPositionDto>;
        selectedCode: KnockoutObservable<string>;
        isEnable: KnockoutObservable<boolean>;
        index_selected: KnockoutObservable<string>;
        currentCode: KnockoutObservable<any>;
        index_of_itemDelete: any;
        itemList: KnockoutObservableArray<any>;
        selectedId: KnockoutObservable<number>;
        enable: KnockoutObservable<boolean>;
        historyIdUpdate: KnockoutObservable<string>;
        startDateLast: KnockoutObservable<string>;
        startDateAddNew: KnockoutObservable<string>;
        checkRegister: KnockoutObservable<string>;
        oldEndDate: KnockoutObservable<string>;
        oldStartDate: KnockoutObservable<string>;
        endDateUpdate: KnockoutObservable<string>;
        startDateUpdateNew: KnockoutObservable<string>;
        checkCoppyJtitle: KnockoutObservable<boolean>;
        checkAddJhist: KnockoutObservable<string>;
        checkAddJtitle: KnockoutObservable<string>;
        checkUpdate: KnockoutObservable<string>;
        checkDelete: KnockoutObservable<string>;
        srtDateLast: KnockoutObservable<string>;
        jTitleRef: KnockoutObservableArray<model.JobRef>;
        dataRef: KnockoutObservableArray<model.GetAuth>;
        enableListBoxAuth: KnockoutObservable<boolean>;
        isDeleteEnable: KnockoutObservable<boolean>;
        dataRefNew: KnockoutObservableArray<model.GetAuth>;
        createdMode: KnockoutObservable<boolean>;
        clickChange: KnockoutObservable<boolean>;

        notAlert: KnockoutObservable<boolean>;
        dirty: nts.uk.ui.DirtyChecker;
        constructor() {
            var self = this;
            self.jTitleRef = ko.observableArray([]);
            self.dataRef = ko.observableArray([]);
            self.dataRefNew = ko.observableArray([]);
            self.label_003 = ko.observable(new Labels());
            self.inp_002_code = ko.observable(null);
            self.inp_002_enable = ko.observable(null);
            self.inp_003_name = ko.observable(null);
            self.inp_005_memo = ko.observable(null);
            self.createdMode = ko.observable(true);
            self.listbox = ko.observableArray([]);
            self.selectedCode = ko.observable('');
            self.isEnable = ko.observable(true);
            self.itemHist = ko.observable(null);
            self.index_selected = ko.observable('');
            self.srtDateLast = ko.observable(null);
            self.startDateLast = ko.observable(null);
            self.startDateAddNew = ko.observable("");
            self.checkRegister = ko.observable('0');
            self.oldStartDate = ko.observable(null);
            self.endDateUpdate = ko.observable(null);
            self.startDateUpdateNew = ko.observable(null);
            self.oldEndDate = ko.observable(null);
            self.checkCoppyJtitle = ko.observable(true);
            self.historyIdUpdate = ko.observable(null);
            self.dataSource = ko.observableArray([]);
            self.currentItem = ko.observable(null);
            self.currentCode = ko.observable();
            self.clickChange = ko.observable(false);
            self.enableListBoxAuth = ko.observable(true);
            self.isDeleteEnable = ko.observable(true);
            self.columns = ko.observableArray([
                { headerText: 'コード', key: 'jobCode', width: 90 },
                { headerText: '名称', key: 'jobName', width: 100 }
            ]);
            self.roundingRules = ko.observableArray([
                { code: '0', name: '可能' },
                { code: '1', name: '不可' },
            ]);
            self.referenceSettings = ko.observable(0);
            self.itemList = ko.observableArray([
                new BoxModel(0, '全員参照可能'),
                new BoxModel(1, '全員参照不可'),
                new BoxModel(2, 'ロール毎に設定')
            ]);
            self.selectedId = ko.observable(0);
            self.selectedId.subscribe(function(codeChanged) {
                self.disableRadioBox(codeChanged);
            });
            self.disableRadioBox(0);
            self.enable = ko.observable(true);
            self.notAlert = ko.observable(true);
            self.dirty = new nts.uk.ui.DirtyChecker(self.dataSource);
            //change history
            $("#list-box").click(function(evt, ui) {
                self.clickChange(true);
            });
            self.selectedCode.subscribe(function(codeChanged) {
                self.isDeleteEnable(true);
                self.itemHist(self.findHist(codeChanged));
                self.oldStartDate(self.itemHist().startDate);
                self.oldEndDate(self.itemHist().endDate);
                self.srtDateLast(self.listbox()[0].startDate);

                var chkCopy = nts.uk.ui.windows.getShared('cmm013Copy');
                if (codeChanged === '1' && chkCopy) {
                    //set lai disable cho input code
                    self.inp_002_enable(true);
                    $("#inp_002").focus();
                    return;
                } else {
                    if (self.listbox().length > 0 && self.listbox()[0].historyId === "1" && codeChanged !== "1") {
                        if (self.listbox().length > 1) {
                            self.listbox()[1].endDate = '9999/12/31';
                        }
                        self.listbox.shift();
                    }
                    //find position by history                                        
                    service.findAllJobTitle(codeChanged).done(function(position_arr: Array<model.ListPositionDto>) {

                        self.dataSource(position_arr);
                        if (self.dataSource().length > 0) {
                            //set select position & history
                            let changedCode = self.clickChange() ? self.dataSource()[0].jobCode : self.inp_002_code() || self.dataSource()[0].jobCode;
                            if (changedCode === self.currentCode()) {
                                self.changedCode(changedCode);
                            } else {
                                self.currentCode(changedCode);
                            }
                        }
                        self.clickChange(false);
                    }).fail(function(res: any) {
                        nts.uk.ui.dialog.alert("対象データがありません。");
                    })
                }
            });
            //change position
            self.currentCode.subscribe(function(codeChanged) {
                if (codeChanged !== null && codeChanged !== undefined) {
                    self.changedCode(codeChanged);
                }
            });
        }

        disableRadioBox(value: number) {
            var self = this;
            $('#lst_003').removeClass('disableClass');
            if (value == 0 || value == 1) {
                $('#lst_003').show();
                $('#lst_003').addClass('disableClass');
                self.enableListBoxAuth(false);
            } else {
                $('#lst_003').show();
                self.enableListBoxAuth(true);
            }
        }

        changedCode(value) {
            var self = this;
            self.currentItem(self.findPosition(value));
            if (self.currentItem() != null) {
                self.inp_002_code(self.currentItem().jobCode);
                self.inp_003_name(self.currentItem().jobName);
                self.inp_005_memo(self.currentItem().memo);
                self.selectedId(self.currentItem().presenceCheckScopeSet);
                self.inp_002_enable(false);
                self.createdMode(false);
            }
            //find auth by historyId and job title code
            service.findByUseKt().done(function(res: any) {
                if (res.use_Kt_Set === 1) {
                    var historyId = (self.currentItem() && self.currentItem().historyId) ? self.currentItem().historyId : "NULL";
                    var jobCode = (self.currentItem() && self.currentItem().jobCode) ? self.currentItem().jobCode : "NULL";
                    service.getAllJobTitleAuth(historyId, jobCode).done(function(jTref) {
                        //show or hide list 003
                        if (jTref.length === 0) {
                            $('.trLst003').hide();
                        } else {
                            $('.trLst003').show();
                            self.dataRef([]);
                            _.map(jTref, function(item) {
                                var tmp = new model.GetAuth(item.jobCode, item.authCode, item.authName, item.referenceSettings);
                                self.dataRef.push(tmp);
                                return tmp;
                            });
                            self.dataRefNew(self.dataRef());
                            self.createdMode(true);
                        }
                    }).fail(function(res: any) {
                        nts.uk.ui.dialog.alert("対象データがありません。");
                    });
                }

            })

        }

        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            self.getHistory(dfd);
            self.dirty.reset();
            return dfd.promise();
        }

        //get all history
        getHistory(dfd: any, selectedHistory?: string): any {
            var self = this;
            service.getAllHistory().done(function(history_arr: Array<model.ListHistoryDto>) {
                var listHistory = _.map(history_arr, function(item) {
                    return new model.ListHistoryDto(item.companyCode, item.historyId, item.startDate, item.endDate);
                });
                self.listbox(listHistory);

                if (history_arr.length > 0) {
                    if (selectedHistory !== undefined && selectedHistory !== "1") {
                        let currentHist = self.findHist(selectedHistory);
                        self.selectedCode(currentHist.historyId);
                        self.startDateLast(currentHist.startDate);
                        self.endDateUpdate(currentHist.endDate);
                        self.historyIdUpdate(currentHist.historyId);
                    } else {
                        var histStart = _.first(history_arr);
                        self.selectedCode(histStart.historyId);
                        self.startDateLast(histStart.startDate);
                        self.endDateUpdate(histStart.endDate);
                        self.historyIdUpdate(histStart.historyId);
                    }
                    var hisEnd = _.last(history_arr);
                    self.oldStartDate();
                    dfd.resolve(history_arr);

                } else {
                    self.dataSource([]);
                    self.initPosition();
                    self.srtDateLast(null);
                    self.openCDialog();
                    dfd.resolve();
                }
            }).fail(function(res: any) {
                nts.uk.ui.dialog.alert("対象データがありません。");
            })

        }

        //Button 登録
        registerPosition(): any {
            var self = this;
            if (!self.checkPositionValue()) {
                return;
            } else {
                var chkInsert = nts.uk.ui.windows.getShared('cmm013Insert');
                var chkCopy = nts.uk.ui.windows.getShared('cmm013Copy');
                var jobInfor: model.jobTitle;
                var dfd = $.Deferred();
                var startDate = "";
                if (chkInsert === true) {
                    startDate = nts.uk.ui.windows.getShared('cmm013C_startDateNew');
                }
                if (!chkCopy || !chkInsert) {
                    //info of jobcode
                    jobInfor = new model.jobTitle(self.inp_003_name(),
                        self.inp_005_memo(),
                        '99',
                        self.selectedId(),
                        '');
                }
                var positionInfor = new model.registryCommand(null, null, false, null, false, null, []);
                //info of auth
                if (self.selectedId() === 2) {
                    var refInfor = [];
                    var dataRef = ko.toJS(self.dataRef());
                    _.each(dataRef, function(obj: model.JobRef) {
                        positionInfor.refCommand.push(new model.refJob(obj.authCode, obj.referenceSettings));
                    })
                }
                positionInfor.historyId = self.selectedCode();
                positionInfor.startDate = startDate;
                positionInfor.chkCopy = chkCopy;
                positionInfor.jobCode = self.inp_002_code();
                positionInfor.chkInsert = self.inp_002_enable();
                positionInfor.positionCommand = jobInfor;
                let selectedHistory = self.selectedCode();
                service.registry(positionInfor).done(function() {
                    //clear set shared
                    nts.uk.ui.windows.setShared('cmm013Insert', '', true);
                    nts.uk.ui.windows.setShared('cmm013Copy', '', true);
                    nts.uk.ui.windows.setShared('cmm013C_startDateNew', '', true);
                    self.selectedCode.valueHasMutated();
                    self.getHistory(dfd, selectedHistory);
                    self.referenceSettings = ko.observable(0);
                }).fail(function(error: any) {
                    if (error.messageId === "ER005") {
                        nts.uk.ui.dialog.alert("入力したコードは既に存在しています。\r\n職位コードを確認してください。");
                    }
                    if (error.messageId === "ER026") {
                       nts.uk.ui.dialog.alert("更新対象のデータが存在しません。");
                    }
                });
                return dfd.promise();


            }
        }

        checkPositionValue(): boolean {
            var self = this;
            if (self.inp_002_code() === "" || self.inp_002_code() === null) {
                nts.uk.ui.dialog.alert("コードが入力されていません");
                $('#inp_002').focus();
                return false;
            }
            if (self.inp_003_name() === "" || self.inp_003_name() === null) {
                nts.uk.ui.dialog.alert("名称が入力されていません");
                $('#inp_003').focus();
                return false;
            }
            return true;
        }
        findPosition(value: string): any {
            let self = this;
            var result = _.find(self.dataSource(), function(obj: model.ListPositionDto) {
                return obj.jobCode === value;
            });
            return (result) ? result : null;
        }
        findHist(value: string): any {
            let self = this;
            return _.find(self.listbox(), function(obj: model.ListHistoryDto) {
                return obj.historyId === value;
            })
        }


        clearInit() {
            var self = this;
            self.inp_002_enable(true);
            self.inp_002_code("");
            self.inp_003_name("");
            self.selectedId(0);
            self.inp_005_memo("");
            self.currentCode(null);
            _.forEach(self.dataRef(), function(item) {
                item.referenceSettings(0);
            });
            $("#inp_002").focus();
            self.isDeleteEnable(false);
        }
        initPosition() {
            var self = this;
            if (self.checkChangeData() == false || self.checkChangeData() === undefined) {
                self.clearInit()
            }
        }
        checkChangeData(): boolean {
            var self = this;
            var dfd = $.Deferred<any>();
            if (self.checkRegister() == '1') {
                var retVal = confirm("Changed ?");
                if (retVal == true && self.startDateAddNew() !== undefined && self.startDateAddNew() != '') {
                    self.registerPosition();
                    return true;
                } else {
                    let selectedHistory = self.selectedCode();
                    self.startDateAddNew('');
                    self.checkRegister('0');
                    self.getHistory(dfd, selectedHistory);
                    return false;
                }
            }
        }
        openCDialog() {

            var self = this;
            if (self.checkChangeData() == false || self.checkChangeData() === undefined) {
                var lstTmp = self.listbox();
                nts.uk.ui.windows.setShared('CMM013_historyId', self.index_selected(), true);
                nts.uk.ui.windows.setShared('CMM013_startDateLast', self.srtDateLast());
                nts.uk.ui.windows.sub.modal('/view/cmm/013/c/index.xhtml', { title: '履歴の追加', })
                    .onClosed(function(): any {
                        self.startDateUpdateNew('');
                        self.startDateAddNew(nts.uk.ui.windows.getShared('cmm013C_startDateNew'));
                        self.checkCoppyJtitle(nts.uk.ui.windows.getShared('cmm013Copy'));
                        if (self.checkCoppyJtitle() == false) {
                            if (self.startDateAddNew() != '' && self.startDateAddNew() !== undefined) {
                                let add = new model.ListHistoryDto('', '1', self.startDateAddNew(), '9999/12/31');
                                self.initPosition();
                                self.listbox.unshift(add);
                                self.selectedCode('1');
                                self.currentCode("");
                                self.dataSource([]);
                                $("#code").focus();
                                let startDate = new Date(self.startDateAddNew());
                                startDate.setDate(startDate.getDate() - 1);
                                let strStartDate = startDate.getFullYear() + '/' + (startDate.getMonth() + 1) + '/' + startDate.getDate();
                                let update = new model.ListHistoryDto('', self.historyIdUpdate(), self.startDateLast(), strStartDate);
                                if (self.listbox().length > 1) {
                                    self.listbox.splice(1, 1, update);
                                    self.listbox.valueHasMutated();
                                }
                            } else {
                                return;
                            }
                            //add history and coppy position    
                        } else {
                            if (self.startDateAddNew() != '' && self.startDateAddNew() !== undefined) {
                                let add = new model.ListHistoryDto('', '1', self.startDateAddNew(), '9999/12/31');
                                self.listbox.unshift(add);
                                self.selectedCode('1');
                                let startDate = new Date(self.startDateAddNew());
                                startDate.setDate(startDate.getDate() - 1);
                                let strStartDate = startDate.getFullYear() + '/' + (startDate.getMonth() + 1) + '/' + startDate.getDate();
                                let update = new model.ListHistoryDto('', self.historyIdUpdate(), self.startDateLast(), strStartDate);
                                if (self.listbox().length > 1) {
                                    self.listbox.splice(1, 1, update);
                                    service.findAllJobTitle(self.listbox()[1].historyId).done(function(position_arr: Array<model.ListPositionDto>) {
                                        self.dataSource(position_arr);
                                        if (self.dataSource().length > 0) {
                                            //set select position & history
                                            self.currentCode(self.dataSource()[0].jobCode);
                                        }
                                    }).fail(function(err: any) {
                                        nts.uk.ui.dialog.alert(err.message);
                                    })
                                    self.listbox.valueHasMutated();
                                    self.inp_002_enable(false);
                                }
                            } else {
                                return;
                            }
                        }

                    });
            }
        }
        openDDialog() {
            var self = this;
            var dfd = $.Deferred();
            if (self.checkChangeData() == false || self.checkChangeData() === undefined) {
                var lstTmp = [];
                self.startDateUpdateNew('');
                nts.uk.ui.windows.setShared('cmm013HistoryId', self.selectedCode(), true);
                nts.uk.ui.windows.setShared('cmm013StartDate', self.oldStartDate(), true);
                nts.uk.ui.windows.setShared('cmm013EndDate', self.endDateUpdate(), true);
                nts.uk.ui.windows.setShared('cmm013OldEndDate', self.oldEndDate(), true);
                nts.uk.ui.windows.setShared('cmm013JobCode', self.inp_002_code(), true);
                nts.uk.ui.windows.sub.modal('/view/cmm/013/d/index.xhtml', { title: '履歴の編集', })
                    .onClosed(function() {
                        if (!nts.uk.ui.windows.getShared('cancelDialog')) {
                            self.getHistory(dfd);
                            self.currentCode(self.dataSource()[0].jobCode);
                            //self.currentCode(self.selectedCode());
                        }
                        dfd.promise();
                    });
            }
        }
        deletePosition() {
            var self = this;
            if (self.checkRegister() == '1') {
                self.checkChangeData();
            } else {
                var dfd = $.Deferred<any>();
                var item = new model.DeleteJobTitle(self.selectedCode(), self.currentItem().jobCode);
                self.index_of_itemDelete = self.dataSource().indexOf(self.currentItem());
                if (self.dataSource().length == 1) {
                    nts.uk.ui.dialog.alert("選択している履歴の職位が1件のみのため、\r\n履歴の編集ボタンから履歴削除を行ってください。")
                } else {
                    nts.uk.ui.dialog.confirm("データを削除します。\r\nよろしいですか？").ifYes(function() {
                        service.deleteJobTitle(item).done(function(res) {
                            self.getPositionList_afterDelete();
                        }).fail(function(res) {
                            dfd.reject(res);
                        })
                    }).ifNo(function() {
                    });
                }
            }
        }
        getPositionList_afterDelete(): any {
            var self = this;
            var dfd = $.Deferred<any>();
            service.findAllJobTitle(self.selectedCode()).done(function(position_arr: Array<model.ListPositionDto>) {
                self.dataSource = ko.observableArray([]);
                self.dataSource(position_arr);
                if (self.dataSource().length > 0) {
                    if (self.index_of_itemDelete === self.dataSource().length) {
                        self.currentCode(self.dataSource()[self.index_of_itemDelete - 1].jobCode)
                        self.inp_002_code(self.dataSource()[self.index_of_itemDelete - 1].jobCode);
                        self.inp_003_name(self.dataSource()[self.index_of_itemDelete - 1].jobName);
                        self.inp_005_memo(self.dataSource()[self.index_of_itemDelete - 1].memo);
                    } else {
                        self.currentCode(self.dataSource()[self.index_of_itemDelete].jobCode)
                        self.inp_002_code(self.dataSource()[self.index_of_itemDelete].jobCode);
                        self.inp_003_name(self.dataSource()[self.index_of_itemDelete].jobName);
                        self.inp_005_memo(self.dataSource()[self.index_of_itemDelete].memo);
                    }
                } else {
                    self.initPosition();
                }
                dfd.resolve();
            }).fail(function(error) {
                alert(error.message);
            })
            dfd.resolve();
            return dfd.promise();
        }
    }
    export class Labels {
        constraint: string = 'LayoutCode';
        inline: KnockoutObservable<boolean>;
        required: KnockoutObservable<boolean>;
        enable: KnockoutObservable<boolean>;
        constructor() {
            var self = this;
            self.inline = ko.observable(true);
            self.required = ko.observable(true);
            self.enable = ko.observable(true);
        }
    }
    export class BoxModel {
        id: number;
        name: string;
        constructor(id, name) {
            var self = this;
            self.id = id;
            self.name = name;
        }
    }
    export module model {
        export class ListHistoryDto {
            companyCode: string;
            historyId: string;
            startDate: string;
            endDate: string;


            constructor(companyCode: string, historyId: string, startDate: string, endDate: string) {
                var self = this;
                self.companyCode = companyCode;
                self.historyId = historyId;
                self.startDate = moment.utc(startDate).format("YYYY/MM/DD");
                self.endDate = moment.utc(endDate).format("YYYY/MM/DD");

            }
        }
        export class ListPositionDto {
            historyId: string;
            jobCode: string;
            jobName: string;
            presenceCheckScopeSet: number;
            memo: string;
            constructor(jobCode: string, jobName: string, presenceCheckScopeSet: number, memo: string) {
                let self = this;
                self.jobCode = jobCode;
                self.jobName = jobName;
                self.presenceCheckScopeSet = presenceCheckScopeSet;
                self.memo = memo;
            }
        }
        export class JobRef {
            historyId: string;
            jobCode: string;
            authCode: string;
            referenceSettings: number;
            constructor(historyId: string, jobCode: string, authCode: string, referenceSettings: number) {
                this.historyId = historyId;
                this.jobCode = jobCode;
                this.authCode = authCode;
                this.referenceSettings = referenceSettings;
            }
        }
        export class GetAuth {
            jobCode: KnockoutObservable<string>;
            authCode: KnockoutObservable<string>;
            authName: KnockoutObservable<string>;
            referenceSettings: KnockoutObservable<number>;
            constructor(jobCode: string, authCode: string,
                authName: string, referenceSettings: number) {
                this.jobCode = ko.observable(jobCode);
                this.authCode = ko.observable(authCode);
                this.authName = ko.observable(authName);
                this.referenceSettings = ko.observable(referenceSettings);
            }
        }
        export class DeleteJobTitle {
            historyId: string;
            jobCode: string;
            constructor(historyId: string, jobCode: string) {
                this.historyId = historyId;
                this.jobCode = jobCode;
            }
        }
        export class DeleteobRefAuth {
            companyCode: string;
            historyId: string;
            jobCode: string;
            authCode: string;

            constructor(companyCode: string, historyId: string, jobCode: string, authCode: string) {
                this.companyCode = companyCode;
                this.historyId = historyId;
                this.jobCode = jobCode;
                this.authCode = authCode;
            }
        }
        export class registryCommand {
            historyId: string;
            startDate: string;
            chkCopy: boolean;
            jobCode: string;
            chkInsert: boolean;
            positionCommand: jobTitle;
            refCommand: Array<refJob>;
            constructor(historyId: string, startDate: string, chkCopy: boolean, jobCode: string, chkInsert: boolean, positionCommand: jobTitle, refCommand: Array<refJob>) {
                this.historyId = historyId;
                this.startDate = startDate;
                this.chkCopy = chkCopy;
                this.jobCode = jobCode;
                this.chkInsert = chkInsert;
                this.positionCommand = positionCommand;
                this.refCommand = refCommand;
            }

        }
        export class jobTitle {
            jobName: string;
            memo: string;
            hiterarchyOrderCode: string;
            presenceCheckScopeSet: number;
            jobOutCode: string;
            constructor(jobName: string, memo: string, hiterarchyOrderCode: string, presenceCheckScopeSet: number, jobOutCode: string) {
                this.jobName = jobName;
                this.memo = memo;
                this.hiterarchyOrderCode = hiterarchyOrderCode;
                this.presenceCheckScopeSet = presenceCheckScopeSet;
                this.jobOutCode = jobOutCode;
            }
        }
        export class refJob {
            authorizationCode: string;
            referenceSettings: number;
            constructor(authorizationCode: string, referenceSettings: number) {
                this.authorizationCode = authorizationCode;
                this.referenceSettings = referenceSettings;
            }
        }

        export class InputField {
            inp_002_enable: KnockoutObservable<boolean>;
            inp_003_name: KnockoutObservable<string>;
            inp_004_notes: KnockoutObservable<string>;
            inp_002_code: KnockoutObservable<string>;
            constructor(position: ListPositionDto, enable) {
                this.inp_002_code = ko.observable(position.jobCode);
                this.inp_003_name = ko.observable(position.jobName);
                this.inp_004_notes = ko.observable(position.memo);
                this.inp_002_enable = ko.observable(enable);
            }
        }
    }
}