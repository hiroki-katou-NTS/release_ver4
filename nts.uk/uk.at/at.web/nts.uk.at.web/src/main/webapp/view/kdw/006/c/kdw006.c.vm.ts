module nts.uk.at.view.kdw006.c.viewmodel {
    let __viewContext: any = window["__viewContext"] || {};

    export class ScreenModelC {
        daiPerformanceFunDto: KnockoutObservable<DaiPerformanceFunDto>;
        approvalProcessDto: KnockoutObservable<ApprovalProcessDto>;
        identityProcessDto: KnockoutObservable<IdentityProcessDto>
        monPerformanceFunDto: KnockoutObservable<MonPerformanceFunDto>;
        appTypeDto: KnockoutObservable<AppTypeDto>;

        itemList: KnockoutObservableArray<any>;
        hiddenYourself: KnockoutObservable<boolean>;
        hiddenSuper: KnockoutObservable<boolean>;

        sideBar: KnockoutObservable<number>;
        appType: KnockoutObservable<string>;
        appTypeEnum : KnockoutObservable<any>;

        constructor() {
            let self = this;
            self.itemList = ko.observableArray([]);
            self.hiddenYourself = ko.observable(true);
            self.hiddenSuper = ko.observable(true);
            self.appTypeEnum = ko.observable(null);

            self.sideBar = ko.observable(0);
            let yourSelf = __viewContext.enums.YourselfConfirmError;
            _.forEach(yourSelf, (a) => {
                self.itemList.push(new ItemModel(a.value, a.name));
            });

            self.appTypeDto = ko.observable(new AppTypeDto({
                appTypes: [],
            }));

            self.daiPerformanceFunDto = ko.observable(new DaiPerformanceFunDto({
                cid: '',
                comment: null,
                monthChkMsgAtr: 0,
                disp36Atr: 0,
                clearManuAtr: 0,
                flexDispAtr: 0,
                breakCalcUpdAtr: 0,
                breakTimeAutoAtr: 0,
                breakClrTimeAtr: 0,
                autoSetTimeAtr: 0,
                ealyCalcUpdAtr: 0,
                overtimeCalcUpdAtr: 0,
                lawOverCalcUpdAtr: 0,
                manualFixAutoSetAtr: 0,
            }));
            self.approvalProcessDto = ko.observable(new ApprovalProcessDto({
                cid: '',
                jobTitleId: null,
                useDailyBossChk: 0,
                useMonthBossChk: 0,
                supervisorConfirmError: 0,
            }));
            self.identityProcessDto = ko.observable(new IdentityProcessDto({
                cid: '',
                useDailySelfCk: 0,
                useMonthSelfCK: 0,
                yourselfConfirmError: 0
            }));
            self.monPerformanceFunDto = ko.observable(new MonPerformanceFunDto({
                cid: '',
                comment: null,
                dailySelfChkDispAtr: 0
            }));
            
            service.getApplicationType().done(function(data) {
                let dfd = $.Deferred();
                self.appTypeEnum(data);
                dfd.resolve();
                return dfd.promise();
            });

            self.appType = ko.observable('');
            self.appTypeDto.subscribe((value) => {
                let temp = nts.uk.ui.windows.getShared("kdw006HResult");
                let result = "",valueSort = _.sortBy(value.appTypes()),listAppType = __viewContext.enums.ApplicationType;
                if(temp){
                    valueSort = value.appTypes();
                }
                _.forEach(valueSort, function(item) {
                    let itemModel = _.find(self.appTypeEnum(), function(obj) {
                        return obj.value == item;
                    });
                    result += itemModel.fieldName + ",";
                })
                let size = result.length - 1;
                self.appType(result.slice(0, size));
            });
        }


        start(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            nts.uk.ui.block.grayout();
            $.when(self.getIdentity(), self.getApproval(), self.getDaily(), self.getMonthly(), self.getAppType()).done(() => {
                dfd.resolve();
            }).always(() => {
                nts.uk.ui.errors.clearAll();
                nts.uk.ui.block.clear();
            });
            return dfd.promise();
        }

        jumpTo(sidebar): JQueryPromise<any> {
            let self = this;
            nts.uk.request.jump("/view/kdw/006/a/index.xhtml", { ShareObject: sidebar() });
        }


        getAppType(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();

            service.getAppType().done(function(data: IAppTypeDto) {
                if (data) {
                    self.appTypeDto(new AppTypeDto(data));
                    dfd.resolve();
                } else {
                    dfd.resolve();
                }
            });
            return dfd.promise();
        }

        //Get IdentityProcess 本人確認処理の利用設定
        getIdentity(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            service.getIdentity().done(function(data: IIdentityProcessDto) {
                if (data) {
                    self.identityProcessDto(new IdentityProcessDto(data));
                    dfd.resolve();
                } else {
                    dfd.resolve();
                }
            });
            return dfd.promise();
        }

        // get ApprovalProcess 承認処理の利用設定 
        getApproval(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            service.getApproval().done(function(data: IApprovalProcessDto) {
                if (data) {
                    self.approvalProcessDto(new ApprovalProcessDto(data));
                    dfd.resolve();
                } else {
                    dfd.resolve();
                }
            });
            return dfd.promise();
        }

        // get DaiPerformanceFun 日別実績の修正の機能 
        getDaily(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            service.getDaily().done(function(data: IDaiPerformanceFunDto) {
                if (data) {
                    self.daiPerformanceFunDto(new DaiPerformanceFunDto(data));
                    dfd.resolve();
                } else {
                    dfd.resolve();
                }
            });
            return dfd.promise();
        }

        // get DaiPerformanceFun 日別実績の修正の機能 
        getMonthly(): JQueryPromise<any> {
            let self = this;
            let dfd = $.Deferred();
            service.getMonthly().done(function(data: IMonPerformanceFunDto) {
                if (data) {
                    self.monPerformanceFunDto(new MonPerformanceFunDto(data));
                    dfd.resolve();
                } else {
                    dfd.resolve();
                }
            });
            return dfd.promise();
        }
        // when click register button 
        saveData() {
            let self = this;
            nts.uk.ui.block.grayout();
            if (nts.uk.ui.errors.hasError() === false) {
                service.updateIdentity(ko.toJS(self.identityProcessDto)).done(function() {
                    service.updateApproval(ko.toJS(self.approvalProcessDto)).done(function() {
                        service.updateDaily(ko.toJS(self.daiPerformanceFunDto)).done(function() {
                            service.updateMonthly(ko.toJS(self.monPerformanceFunDto)).done(function() {
                                service.updateAppType(ko.toJS(self.appTypeDto)).done(function() {
                                    nts.uk.ui.block.invisible();
                                    //self.start();
                                    nts.uk.ui.dialog.info({ messageId: "Msg_15" });
                                    nts.uk.ui.block.clear();
                                })
                            });
                        });
                    });
                }).always(() => {
                    nts.uk.ui.block.clear();
                });
            }

        }

        opentHDialog() {
            let self = this;
            let share = {
                appTypes: self.appTypeDto().appTypes(),
                multi: true,
            }
            nts.uk.ui.windows.setShared('kdw006CResult', share);
            nts.uk.ui.windows.sub.modal("../h/index.xhtml", { title: "計算式の設定" }).onClosed(() => {
                let temp = nts.uk.ui.windows.getShared("kdw006HResult");
                if (temp) {
                    let output = {
                        appTypes: temp,
                    }
                    self.appTypeDto(new AppTypeDto(output));
                }
            });
        }
    }
    class BoxModel {
        id: number;
        name: string;
        constructor(id, name) {
            var self = this;
            self.id = id;
            self.name = name;
        }
    }
    interface IDaiPerformanceFunDto {
        cid: string;
        comment: string;
        monthChkMsgAtr: number;
        disp36Atr: number;
        clearManuAtr: number;
        flexDispAtr: number;
        breakCalcUpdAtr: number;
        breakTimeAutoAtr: number;
        breakClrTimeAtr: number;
        autoSetTimeAtr: number;
        ealyCalcUpdAtr: number;
        overtimeCalcUpdAtr: number;
        lawOverCalcUpdAtr: number;
        manualFixAutoSetAtr: number;
        checkErrRefDisp : number;
    }

    class DaiPerformanceFunDto {
        cid: KnockoutObservable<string>;
        comment: KnockoutObservable<string>;
        monthChkMsgAtr: KnockoutObservable<boolean>;
        disp36Atr: KnockoutObservable<boolean>;
        clearManuAtr: KnockoutObservable<boolean>;
        flexDispAtr: KnockoutObservable<boolean>;
        breakCalcUpdAtr: KnockoutObservable<boolean>;
        breakTimeAutoAtr: KnockoutObservable<boolean>;
        breakClrTimeAtr: KnockoutObservable<boolean>;
        autoSetTimeAtr: KnockoutObservable<boolean>;
        ealyCalcUpdAtr: KnockoutObservable<boolean>;
        overtimeCalcUpdAtr: KnockoutObservable<boolean>;
        lawOverCalcUpdAtr: KnockoutObservable<boolean>;
        manualFixAutoSetAtr: KnockoutObservable<boolean>;
        checkErrRefDisp : KnockoutObservable<boolean>;

        constructor(param: IDaiPerformanceFunDto) {
            let self = this;
            self.cid = ko.observable(param.cid);
            self.comment = ko.observable(param.comment);
            self.monthChkMsgAtr = ko.observable(param.monthChkMsgAtr == 1 ? true : false);
            self.disp36Atr = ko.observable(param.disp36Atr == 1 ? true : false);
            self.clearManuAtr = ko.observable(param.clearManuAtr == 1 ? true : false);
            self.flexDispAtr = ko.observable(param.flexDispAtr == 1 ? true : false);
            self.breakCalcUpdAtr = ko.observable(param.breakCalcUpdAtr == 1 ? true : false);
            self.breakTimeAutoAtr = ko.observable(param.breakTimeAutoAtr == 1 ? true : false);
            self.breakClrTimeAtr = ko.observable(param.breakClrTimeAtr == 1 ? true : false);
            self.autoSetTimeAtr = ko.observable(param.autoSetTimeAtr == 1 ? true : false);
            self.ealyCalcUpdAtr = ko.observable(param.ealyCalcUpdAtr == 1 ? true : false);
            self.overtimeCalcUpdAtr = ko.observable(param.overtimeCalcUpdAtr == 1 ? true : false);
            self.lawOverCalcUpdAtr = ko.observable(param.lawOverCalcUpdAtr == 1 ? true : false);
            self.manualFixAutoSetAtr = ko.observable(param.manualFixAutoSetAtr == 1 ? true : false);
            self.checkErrRefDisp =  ko.observable(param.checkErrRefDisp == 1 ? true : false);
        }
    }

    interface IApprovalProcessDto {
        cid: string;
        jobTitleId: string;
        useDailyBossChk: number;
        useMonthBossChk: number;
        supervisorConfirmError: number;
    }
    class ApprovalProcessDto {
        cid: KnockoutObservable<string>;
        jobTitleId: KnockoutObservable<string>;
        useDailyBossChk: KnockoutObservable<boolean>;
        useMonthBossChk: KnockoutObservable<boolean>;
        supervisorConfirmError: KnockoutObservable<number>;

        constructor(param: IApprovalProcessDto) {
            let self = this;
            self.cid = ko.observable(param.cid);
            self.jobTitleId = ko.observable(param.jobTitleId);
            self.useDailyBossChk = ko.observable(param.useDailyBossChk == 1 ? true : false);
            self.useMonthBossChk = ko.observable(param.useMonthBossChk == 1 ? true : false);
            self.supervisorConfirmError = ko.observable(param.supervisorConfirmError);
        }
    }

    interface IAppTypeDto {
        appTypes: number[];
    }

    class AppTypeDto {
        appTypes: KnockoutObservableArray<number>;
        constructor(param: IAppTypeDto) {
            let self = this;
            self.appTypes = ko.observableArray(param.appTypes);
        }
    }

    interface IIdentityProcessDto {
        cid: string;
        useDailySelfCk: number;
        useMonthSelfCK: number;
        yourselfConfirmError: number;
    }
    class IdentityProcessDto {
        cid: KnockoutObservable<string>;
        useDailySelfCk: KnockoutObservable<boolean>;
        useMonthSelfCK: KnockoutObservable<boolean>;
        yourselfConfirmError: KnockoutObservable<number>;

        constructor(param: IIdentityProcessDto) {
            let self = this;
            self.cid = ko.observable(param.cid);
            self.useDailySelfCk = ko.observable(param.useDailySelfCk == 1 ? true : false);
            self.useMonthSelfCK = ko.observable(param.useMonthSelfCK == 1 ? true : false);
            self.yourselfConfirmError = ko.observable(param.yourselfConfirmError);
        }
    }
    interface IMonPerformanceFunDto {
        cid: string;
        comment: string;
        dailySelfChkDispAtr: number;
    }
    class MonPerformanceFunDto {
        cid: KnockoutObservable<string>;
        comment: KnockoutObservable<string>;
        dailySelfChkDispAtr: KnockoutObservable<boolean>;
        constructor(param: IMonPerformanceFunDto) {
            let self = this;
            self.cid = ko.observable(param.cid);
            self.comment = ko.observable(param.comment);
            self.dailySelfChkDispAtr = ko.observable(param.dailySelfChkDispAtr == 1 ? true : false);
        }
    }

    class ItemModel {
        code: number;
        name: string;

        constructor(code: number, name: string) {
            this.code = code;
            this.name = name;
        }
    }

    function getNames(): string {
        let arr = [1, 3];
        let numArr = [new ItemModel(1, 'name1'), new ItemModel(2, 'name2'), new ItemModel(3, 'name3')];
        let result = "";
        arr.forEach((code) => {
            let itemModel = _.find(numArr, (item) => { return item.code == code });
            result += itemModel.name;
        });
        return result;

    }
}
