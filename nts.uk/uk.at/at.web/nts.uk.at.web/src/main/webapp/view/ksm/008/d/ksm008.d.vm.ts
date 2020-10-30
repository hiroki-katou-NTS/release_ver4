module nts.uk.at.ksm008.d {

    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;

    const PATH_API = {
        getStartupScreenD: "screen/at/ksm008/d/getStartupInfo",
        getDetailScreenD: "screen/at/ksm008/d/getDetails",
        registerScreenD: "work/method/company/register",
        updateScreenD: "work/method/company/update",
        deleteScreenD: "work/method/company/delete",

        getStartupScreenE: "screen/at/ksm008/e/getStartupInfo",
        getDetailScreenE: "screen/at/ksm008/e/getDetails",
        getLstRelshipScreenE: "screen/at/ksm008/e/getLstRelships",
        registerScreenE: "work/method/relationship/register",
        updateScreenE: "work/method/relationship/update",
        deleteScreenE: "work/method/relationship/delete",

        getAllWorkingHours: 'at/shared/worktimesetting/findAll',
    };

    @bean()
    export class KSM008DViewModel extends ko.ViewModel {
        backButton: string = "/view/ksm/008/a/index.xhtml";
        receiverCode: string;
        //isComSelected: KnockoutObservable<boolean> = ko.observable(false);
        //isOrgSelected: KnockoutObservable<boolean> = ko.observable(false);

        public isAttendance: KnockoutObservable<boolean> = ko.observable(true);

        workplace: Workplace = new Workplace(null, "", "", "", "");

        code: KnockoutObservable<string> = ko.observable("");
        name: KnockoutObservable<string> = ko.observable("");

        // D3_1 勤務予定のアラームチェック条件: コード + 条件名
        conditionCodeAndName: KnockoutObservable<string> = ko.observable("");
        // D5_2 勤務予定のアラームチェック条件.サブ条件リスト.説明
        conditionDescription: KnockoutObservable<string> = ko.observable("");

        // D6_3 就業時間帯の設定
        targetWorkMethods: KnockoutObservableArray<TargetWorkMethod> = ko.observableArray([]);
        dScreenCurrentCode: KnockoutObservable<string> = ko.observable("");
        eScreenCurrentCode: KnockoutObservable<string> = ko.observable("");

        // D7_2 対象の勤務方法の種類
        workMethodType: KnockoutObservable<string> = ko.observable("1");

        // D7_7 対象の就業時間コード
        targetWorkMethodCode: KnockoutObservable<string> = ko.observable("");
        // D7_8 対象の就業時間名称
        targetWorkMethodName: KnockoutObservable<string> = ko.observable("");

        // D8_3 関係性の指定方法
        nextDayWorkMethod: KnockoutObservable<string> = ko.observable("0");

        // D10 翌日の勤務方法の種類
        nextDayWorkMethodType: KnockoutObservable<string> = ko.observable("1");

        // D12 翌日の就業時間帯一覧
        nextDayWorkHourCodes: KnockoutObservableArray<string> = ko.observableArray([]);
        nextDayWorkHours: KnockoutObservableArray<ItemModel> = ko.observableArray([]);

        // 選択可能な就業時間帯コードリスト
        selectableWorkingHours: KnockoutObservableArray<ItemModel> = ko.observableArray([]);

        // E1_3 職場コード
        workplaceCode: KnockoutObservable<string> = ko.observable("");
        // E1_4 職場名
        workplaceName: KnockoutObservable<string> = ko.observable("");

        constructor(params: any) {
            super();
            const vm = this;

            if (params) {
                vm.receiverCode = params.code;
            }
            else {
                vm.$jump(vm.backButton);
            }

            vm.conditionCodeAndName = ko.computed(() => {
                return vm.code() + " " + vm.name();
            });

            if (vm.$user.role.isInCharge.attendance) {
                vm.isAttendance(true);
            } else {
                vm.isAttendance(false);
            }

            vm.$ajax(PATH_API.getAllWorkingHours).then(data => {
                if (data) {
                    vm.selectableWorkingHours(data.map(function (item: any) {
                        return new ItemModel(item.code, item.name);
                    }));
                }
            }).fail(res => {
                vm.$dialog.error({messageId: res.messageId});
            }).always(() => vm.$blockui("clear"));

            vm.loadScreenD(null);
        }

        created() {
            const vm = this;

            vm.dScreenCurrentCode.subscribe((newValue: any) => {
                vm.$errors("clear");
                vm.nextDayWorkHourCodes([]);
                vm.nextDayWorkHours([]);
                if (newValue) {
                    let item = _.find(vm.targetWorkMethods(), i => {
                        return i.key == newValue;
                    });
                    if (item) {
                        vm.targetWorkMethodCode(item.code);
                        vm.targetWorkMethodName(item.name);
                        let query = {
                            workTimeCode: item.code,
                            typeWorkMethod: item.workMethodType
                        };
                        vm.$blockui("invisible");
                        vm.$ajax(PATH_API.getDetailScreenD, query).done(data => {
                            if (data) {
                                vm.workMethodType(data.typeWorkMethod);
                                vm.nextDayWorkMethod(data.specifiedMethod);
                                vm.nextDayWorkMethodType(data.typeOfWorkMethods);

                                if (data.workTimeSettings) {
                                    vm.nextDayWorkHours(_.map(data.workTimeSettings, function (item: any) {
                                        return new ItemModel(item.code, item.name);
                                    }));
                                }
                            }
                        }).always(() => vm.$blockui("clear"));
                    }
                }
                else {
                    vm.reset();
                }
            });

            vm.eScreenCurrentCode.subscribe((newValue: any) => {
                vm.$errors("clear");
                vm.nextDayWorkHourCodes([]);
                vm.nextDayWorkHours([]);
                if (newValue) {
                    let item = _.find(vm.targetWorkMethods(), i => {
                        return i.key == newValue;
                    });

                    if (item) {
                        vm.targetWorkMethodCode(item.code);
                        vm.targetWorkMethodName(item.name);
                        let query = {
                            unit: vm.workplace.unit(),
                            workplaceId: vm.workplace.workplaceId(),
                            workplaceGroupId: vm.workplace.workplaceGroupId(),
                            workTimeCode: item.code,
                            typeWorkMethod: item.workMethodType
                        };
                        vm.$blockui("invisible");
                        vm.$ajax(PATH_API.getDetailScreenE, query).done(data => {
                            if (data) {
                                vm.workMethodType(data.typeWorkMethod);
                                vm.nextDayWorkMethod(data.specifiedMethod);
                                vm.nextDayWorkMethodType(data.typeOfWorkMethods);

                                if (data.workTimeSettings) {
                                    vm.nextDayWorkHours(_.map(data.workTimeSettings, function (item: any) {
                                        return new ItemModel(item.code, item.name);
                                    }));
                                }
                            }
                        }).always(() => vm.$blockui("clear"));
                    }
                }
                else {
                    vm.reset();
                }
            });
        }

        mounted() {
            const vm = this;
            setTimeout(() => {
                $("#pg-name").text("KSM008D " + vm.$i18n("KSM008_5"));
            }, 500);
        }

        loadScreenD(selectedCode: string) {
            const vm = this;
            $("#pg-name").text("KSM008D " + vm.$i18n("KSM008_5"));
            if (vm.receiverCode) {
                vm.$blockui("invisible");
                vm.$ajax(PATH_API.getStartupScreenD, {code: vm.receiverCode}).done(data => {
                    if (data) {
                        vm.code(data.conditionCode);
                        vm.name(data.conditionName);
                        vm.conditionDescription(data.subConditions);
                    }
                    if (data.workTimeSettings) {
                        let newData = data.workTimeSettings.map(function (item: any) {
                            return new TargetWorkMethod(item.code, item.name, item.typeWorkMethod);
                        });
                        newData = _.orderBy(newData, ['code', 'workMethodType'], ['asc', 'desc']);
                        vm.targetWorkMethods(newData);

                        if (selectedCode) {
                            vm.dScreenCurrentCode(selectedCode);
                        }
                        else if (vm.targetWorkMethods().length > 0) {
                            vm.dScreenCurrentCode(vm.targetWorkMethods()[0].key);
                        }
                        else {
                            vm.dScreenCurrentCode(null);
                        }
                    }
                    else {
                        vm.targetWorkMethods([]);
                    }
                }).fail(res => {
                    vm.$dialog.error({messageId: res.messageId});
                }).always(() => vm.$blockui("clear"));
            }
        }

        loadScreenE(selectedCode: string) {
            const vm = this;
            $("#pg-name").text("KSM008E " + vm.$i18n("KSM008_100"));
            vm.$blockui("invisible");

            vm.$ajax(PATH_API.getStartupScreenE).done(data => {
                if (data && data.orgInfoDto) {
                    vm.workplaceCode(data.orgInfoDto.code);
                    vm.workplaceName(data.orgInfoDto.displayName);
                    vm.workplace = new Workplace(data.orgInfoDto.unit, data.orgInfoDto.workplaceId, data.orgInfoDto.workplaceGroupId, data.orgInfoDto.code, data.orgInfoDto.displayName);
                }
                if (data && data.workTimeSettings) {
                    let newData = data.workTimeSettings.map(function (item: any) {
                        return new TargetWorkMethod(item.code, item.name, item.typeWorkMethod);
                    });
                    newData = _.orderBy(newData, ['code', 'workMethodType'], ['asc', 'desc']);
                    vm.targetWorkMethods(newData);

                    if (selectedCode) {
                        vm.eScreenCurrentCode(selectedCode);
                    }
                    else if (vm.targetWorkMethods().length > 0) {
                        vm.eScreenCurrentCode(vm.targetWorkMethods()[0].key);
                    }
                    else {
                        vm.eScreenCurrentCode(null);
                    }
                }
                else {
                    vm.targetWorkMethods([]);
                }
            }).fail(res => {
                vm.$dialog.error({messageId: res.messageId});
            }).always(() => vm.$blockui("clear"));
        }

        /**
         * on click tab panel company action event
         */
        onSelectCom() {
            const vm = this;
            vm.$errors("clear");
            vm.loadScreenD(null);
        }

        /**
         * on click tab panel Organization action event
         */
        onSelectOrg() {
            const vm = this;
            vm.$errors("clear");
            vm.loadScreenE(null);
        }

        /**
         * Call model KDL001 Single Select
         */
        openKdl001SingleSelect() {
            const vm = this;
            let selectableWorkingHoursCode = vm.selectableWorkingHours().map(function (item: any) {
                return item.code;
            });
            setShared("kml001multiSelectMode", false);
            setShared("kml001selectedCodeList", [vm.targetWorkMethodCode()]);
            setShared("kml001isSelection", false);
            setShared("kml001selectAbleCodeList", selectableWorkingHoursCode);
            modal('at', '/view/kdl/001/a/index.xhtml').onClosed(() => {
                let shareWorkCode: Array<string> = getShared('kml001selectedCodeList');
                if (shareWorkCode && shareWorkCode.length > 0) {
                    let selectedItem = _.filter(vm.selectableWorkingHours(), i => {
                        return shareWorkCode[0] == i.code;
                    });
                    if (selectedItem.length > 0) {
                        vm.targetWorkMethodCode(selectedItem[0].code);
                        vm.targetWorkMethodName(selectedItem[0].name);
                        vm.$errors("clear", "#D7_6");
                        vm.$errors("clear", "#E4_6");
                    }
                }
            });
        }

        /**
         * Call model KDL001 Multi Select
         */
        openKdl001MultiSelect() {
            const vm = this;
            let codes = vm.nextDayWorkHours().map(function (item: any) {
                return item.code;
            });
            let selectableWorkingHoursCode = vm.selectableWorkingHours().map(function (item: any) {
                return item.code;
            });
            setShared("kml001multiSelectMode", true);
            setShared("kml001selectedCodeList", codes);
            setShared("kml001isSelection", false);
            setShared("kml001selectAbleCodeList", selectableWorkingHoursCode);
            modal('at', '/view/kdl/001/a/index.xhtml').onClosed(() => {
                let shareWorkCode: Array<string> = getShared('kml001selectedCodeList');
                if (shareWorkCode) {
                    let selectedItem = _.filter(vm.selectableWorkingHours(), i => {
                        return shareWorkCode.indexOf(i.code) >= 0;
                    });
                    if (selectedItem.length > 0) {
                        vm.$errors("clear", "#D12");
                        vm.$errors("clear", "#E9");
                    }
                    vm.nextDayWorkHours(selectedItem);
                }
            });
        }

        /**
         * Call model KDL046
         */
        openModalKDL046() {
            const vm = this;
            let request: any = {
                unit: vm.workplace.unit()
            };
            if (request.unit === 1) {
                request.workplaceGroupId = vm.workplace.workplaceGroupId();
                request.workplaceGroupCode = vm.workplace.workplaceCode();
                request.workplaceGroupName = vm.workplace.workplaceName();
            } else {
                request.workplaceId = vm.workplace.workplaceId();
                request.enableDate = true;
                request.workplaceCode = vm.workplace.workplaceCode();
                request.workplaceName = vm.workplace.workplaceName();
            }
            const data = {
                dataShareDialog046: request
            };
            setShared('dataShareDialog046', request);
            vm.$window.modal('/view/kdl/046/a/index.xhtml')
                .then((result: any) => {
                    let selectedData = getShared('dataShareKDL046');
                    vm.workplace.unit(selectedData.unit);
                    if (selectedData.unit === 0) {
                        vm.workplace.workplaceName(selectedData.workplaceName);
                        vm.workplace.workplaceCode(selectedData.workplaceCode);
                        vm.workplace.workplaceId(selectedData.workplaceId);
                    } else {
                        vm.workplace.workplaceName(selectedData.workplaceGroupName);
                        vm.workplace.workplaceGroupId(selectedData.workplaceGroupID);
                        vm.workplace.workplaceCode(selectedData.workplaceGroupCode);
                    }
                    vm.workplaceCode(vm.workplace.workplaceCode());
                    vm.workplaceName(vm.workplace.workplaceName());
                });
        }

        /**
         * remove next day work hours
         */
        removeNextDayWorkHours() {
            const vm = this;

            vm.nextDayWorkHours(_.filter(vm.nextDayWorkHours(), function (item: any) {
                return vm.nextDayWorkHourCodes().indexOf(item.code) < 0;
            }));
        }

        validateScreenD() {
            const vm = this;
            vm.$errors("clear");
            let isValid = true;
            if (vm.workMethodType() == "0" && !vm.targetWorkMethodCode()) {
                vm.$errors({
                    "#D7_6": {messageId: "Msg_1780", messageParams: [vm.$i18n("KSM008_64")]}
                });
                isValid = false;
            }

            if (vm.nextDayWorkMethodType() == "0" && vm.nextDayWorkHours().length == 0) {
                vm.$errors({
                    "#D12": {messageId: "Msg_1780", messageParams: [vm.$i18n("KSM008_75")]}
                });
                isValid = false;
            }
            return isValid;
        }

        validateScreenE() {
            const vm = this;
            vm.$errors("clear");
            let isValid = true;
            if (vm.workMethodType() == "0" && !vm.targetWorkMethodCode()) {
                vm.$errors({
                    "#E4_6": {messageId: "Msg_1780", messageParams: [vm.$i18n("KSM008_86")]}
                });
                isValid = false;
            }

            if (vm.nextDayWorkMethodType() == "0" && vm.nextDayWorkHours().length == 0) {
                vm.$errors({
                    "#E9": {messageId: "Msg_1780", messageParams: [vm.$i18n("KSM008_97")]}
                });
                isValid = false;
            }
            return isValid;
        }

        reset() {
            const vm = this;
            vm.dScreenCurrentCode(null);
            vm.eScreenCurrentCode(null);
            vm.targetWorkMethodCode(null);
            vm.targetWorkMethodName(null);
            vm.workMethodType("1");
            vm.nextDayWorkMethod("0");
            vm.nextDayWorkMethodType("1");
            vm.nextDayWorkHourCodes([]);
            vm.nextDayWorkHours([]);
        }

        newScreenD() {
            const vm = this;
            vm.reset();
        }

        registerScreenD() {
            const vm = this;
            if (!vm.validateScreenD()) {
                return;
            }
            let workMethodCodes: Array<String> = [];
            if (vm.nextDayWorkMethodType() == "0") {
                workMethodCodes = vm.nextDayWorkHours().map(function (item: any) {
                    return item.code;
                });
            }
            let command = {
                typeWorkMethod: vm.workMethodType(),
                workTimeCode: vm.targetWorkMethodCode(),
                specifiedMethod: vm.nextDayWorkMethod(),
                typeOfWorkMethods: vm.nextDayWorkMethodType(),
                workMethods: workMethodCodes
            };

            let apiUrl = vm.dScreenCurrentCode() ? PATH_API.updateScreenD : PATH_API.registerScreenD;
            vm.$blockui("invisible");
            vm.$ajax(apiUrl, command).done((data) => {
                vm.$dialog.info({messageId: "Msg_15"}).then(() => {
                    let selectedCode = vm.workMethodType() == "1" ? "000-1" : vm.targetWorkMethodCode() + "-" + vm.workMethodType();
                    vm.loadScreenD(selectedCode);
                });
            }).fail(res => {
                vm.$dialog.error({messageId: res.messageId});
            }).always(() => {
                vm.$blockui("clear");
            });
        }

        deleteScreenD() {
            const vm = this;
            vm.$dialog.confirm({messageId: "Msg_18"}).then(res => {
                if (res == "yes") {
                    let nextCode: string = null;
                    if (vm.targetWorkMethods().length > 1) {
                        let currentIndex = _.findIndex(vm.targetWorkMethods(), (x) => {
                            return x.key == vm.dScreenCurrentCode();
                        });
                        let nextIndex = (currentIndex == vm.targetWorkMethods().length - 1) ? currentIndex - 1 : currentIndex + 1;
                        nextCode = vm.targetWorkMethods()[nextIndex].key;
                    }

                    let command = {
                        typeWorkMethod: vm.workMethodType(),
                        workTimeCode: vm.targetWorkMethodCode()
                    };
                    vm.$blockui("invisible");
                    vm.$ajax(PATH_API.deleteScreenD, command).done((data) => {
                        vm.$dialog.info({messageId: "Msg_16"}).then(() => {
                            vm.loadScreenD(nextCode);
                        });
                    }).fail(res => {
                        vm.$dialog.error({messageId: res.messageId});
                    }).always(() => {
                        vm.$blockui("clear");
                    });
                }
            });
        }

        newScreenE() {
            const vm = this;
            vm.reset();
        }

        registerScreenE() {
            const vm = this;
            if (!vm.validateScreenE()) {
                return;
            }

            let workMethodCodes: Array<String> = [];
            if (vm.nextDayWorkMethodType() == "0") {
                workMethodCodes = vm.nextDayWorkHours().map(function (item: any) {
                    return item.code;
                });
            }

            let command = {
                unit: vm.workplace.unit(),
                workplaceId: vm.workplace.workplaceId(),
                workplaceGroupId: vm.workplace.workplaceGroupId(),
                typeWorkMethod: vm.workMethodType(),
                workTimeCode: vm.targetWorkMethodCode(),
                specifiedMethod: vm.nextDayWorkMethod(),
                typeOfWorkMethods: vm.nextDayWorkMethodType(),
                workMethods: workMethodCodes
            };
            let apiUrl = vm.eScreenCurrentCode() ? PATH_API.updateScreenE : PATH_API.registerScreenE;
            vm.$blockui("invisible");
            vm.$ajax(apiUrl, command).done((data) => {
                vm.$dialog.info({messageId: "Msg_15"}).then(() => {
                    let selectedCode = vm.workMethodType() == "1" ? "000-1" : vm.targetWorkMethodCode() + "-" + vm.workMethodType();
                    vm.loadScreenE(selectedCode);
                });
            }).fail(res => {
                vm.$dialog.error({messageId: res.messageId});
            }).always(() => {
                vm.$blockui("clear");
            });
        }

        deleteScreenE() {
            const vm = this;
            vm.$dialog.confirm({messageId: "Msg_18"}).then(res => {
                if (res == "yes") {
                    let nextCode: string = null;
                    if (vm.targetWorkMethods().length > 1) {
                        let currentIndex = _.findIndex(vm.targetWorkMethods(), (x) => {
                            return x.key == vm.eScreenCurrentCode();
                        });
                        let nextIndex = (currentIndex == vm.targetWorkMethods().length - 1) ? currentIndex - 1 : currentIndex + 1;
                        nextCode = vm.targetWorkMethods()[nextIndex].key;
                    }

                    let command = {
                        unit: vm.workplace.unit(),
                        workplaceId: vm.workplace.workplaceId(),
                        workplaceGroupId: vm.workplace.workplaceGroupId(),
                        typeWorkMethod: vm.workMethodType(),
                        workTimeCode: vm.targetWorkMethodCode()
                    };

                    vm.$blockui("invisible");
                    vm.$ajax(PATH_API.deleteScreenE, command).done((data) => {
                        vm.$dialog.info({messageId: "Msg_16"}).then(() => {
                            vm.loadScreenE(nextCode);
                        });
                    }).fail(res => {
                        vm.$dialog.error({messageId: res.messageId});
                    }).always(() => {
                        vm.$blockui("clear");
                    });
                }
            });
        }
    }

    class ItemModel {
        code: string;
        name: string;
        display: string;

        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
            this.display = code + " " + name;
        }
    }

    class TargetWorkMethod {
        code: string;
        name: string;
        workMethodType: string;
        key: string;
        display: string;

        constructor(code: string, name: string, workMethodType: string) {
            this.code = code;
            this.name = name;
            this.workMethodType = workMethodType;
            this.key = code + "-" + workMethodType;
            this.display = workMethodType == "0" ? code + " " + name : nts.uk.resource.getText("KSM008_61");
        }
    }

    class Workplace {
        /**
         * 対象組織情報.単位
         */
        unit: KnockoutObservable<number>;

        /**
         * 対象組織情報.職場ID
         */
        workplaceId: KnockoutObservable<string>;

        /**
         * 対象組織情報.職場グループID
         */
        workplaceGroupId: KnockoutObservable<string>;

        /**
         * 組織の表示情報.コード
         */
        workplaceCode: KnockoutObservable<string>;

        /**
         * 組織の表示情報.表示名
         */
        workplaceName: KnockoutObservable<string>;

        constructor(unit: number, workplaceId: string, workplaceGroupId: string, workplaceCode: string, workplaceName: string) {
            this.unit = ko.observable(unit);
            this.workplaceId = ko.observable(workplaceId);
            this.workplaceGroupId = ko.observable(workplaceGroupId);
            this.workplaceCode = ko.observable(workplaceCode);
            this.workplaceName = ko.observable(workplaceName);
        }
    }
}