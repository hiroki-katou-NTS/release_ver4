module cmm044.a.viewmodel {
    import alert = nts.uk.ui.dialog.alert;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import DirtyChecker = nts.uk.ui.DirtyChecker;
    import modal = nts.uk.ui.windows.sub.modal;
    import formatym = nts.uk.time.parseYearMonthDate;
    import EmployeeSearchDto = nts.uk.com.view.ccg.share.ccg.service.model.EmployeeSearchDto;
    import Ccg001ReturnedData = nts.uk.com.view.ccg.share.ccg.service.model.Ccg001ReturnedData;
    import GroupOption = nts.uk.com.view.ccg.share.ccg.service.model.GroupOption;

    export class ScreenModel {
        // THU NGHIEM KCP_009 
        employeeInputList: KnockoutObservableArray<PersonModel>;
        systemReference: KnockoutObservable<number>;
        isDisplayOrganizationName: KnockoutObservable<boolean>;
        targetBtnText: string;

        listComponentOption: ComponentOption;
        selectedItem: KnockoutObservable<any>;
        tabindex: number;

        // Phong
        tabs: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel>;
        itemList: KnockoutObservableArray<any>;
        index_of_itemDelete: any;
        isEnableDelete: KnockoutObservable<boolean>;
        isEnableAdd: KnockoutObservable<boolean>;

        displayEmployeeInfo1: KnockoutObservable<boolean>;
        displayEmployeeInfo2: KnockoutObservable<boolean>;
        displayEmployeeInfo3: KnockoutObservable<boolean>;
        displayEmployeeInfo4: KnockoutObservable<boolean>;

        histItems: KnockoutObservableArray<model.AgentDto>;
        histSelectedItem: KnockoutObservable<any>;
        currentItem: KnockoutObservable<model.AgentAppDto>;

        agentAppType1: KnockoutObservable<number>;
        agentAppType2: KnockoutObservable<number>;
        agentAppType3: KnockoutObservable<number>;
        agentAppType4: KnockoutObservable<number>;

        agentSid1: KnockoutObservable<string>;
        agentSid2: KnockoutObservable<string>;
        agentSid3: KnockoutObservable<string>;
        agentSid4: KnockoutObservable<string>;


        employeeList: KnockoutObservable<any>;
        employeeNameScreen1: KnockoutObservable<any>;
        employeeNameScreen2: KnockoutObservable<any>;
        employeeNameScreen3: KnockoutObservable<any>;
        employeeNameScreen4: KnockoutObservable<any>;

        //Date Range Picker
        dateValue: KnockoutObservable<any>;

        selectedTab: KnockoutObservable<string>;

        ccgcomponent: GroupOption;
        selectedCode: KnockoutObservableArray<any>;
        showinfoSelectedEmployee: KnockoutObservable<boolean>;
        baseDate: KnockoutObservable<Date> = ko.observable(new Date());
        isShow: KnockoutObservable<boolean>;

        constructor() {
            let self = this;
            // THU NGHIEM  KCP_009 
            self.employeeInputList = ko.observableArray([]);

            self.systemReference = ko.observable(SystemType.EMPLOYMENT);
            self.isDisplayOrganizationName = ko.observable(true);
            self.targetBtnText = nts.uk.resource.getText("KCP009_3");
            self.selectedItem = ko.observable(null);
            self.tabindex = 1;
            // Phong
            self.index_of_itemDelete = ko.observable(-1);
            self.currentItem = ko.observable(null);
            self.tabs = ko.observableArray([
                { id: 'tab-1', title: nts.uk.resource.getText("CMM044_11"), content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true) },
                { id: 'tab-2', title: nts.uk.resource.getText("CMM044_12"), content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(false) },
                { id: 'tab-3', title: nts.uk.resource.getText("CMM044_37"), content: '.tab-content-3', enable: ko.observable(true), visible: ko.observable(false) },
                { id: 'tab-4', title: nts.uk.resource.getText("CMM044_13"), content: '.tab-content-4', enable: ko.observable(true), visible: ko.observable(false) }
            ]);

            self.dateValue = ko.observable({});

            self.displayEmployeeInfo1 = ko.observable(true);
            self.displayEmployeeInfo2 = ko.observable(true);
            self.displayEmployeeInfo3 = ko.observable(true);
            self.displayEmployeeInfo4 = ko.observable(true);

            self.isEnableDelete = ko.observable(true);
            self.isEnableAdd = ko.observable(true);

            self.agentAppType1 = ko.observable(0);
            self.agentAppType2 = ko.observable(2);
            self.agentAppType3 = ko.observable(2);
            self.agentAppType4 = ko.observable(2);

            self.agentSid1 = ko.observable('');
            self.agentSid2 = ko.observable('');
            self.agentSid3 = ko.observable('');
            self.agentSid4 = ko.observable('');

            self.agentAppType1.subscribe(function(newValue) {
                self.displayEmployeeInfo1(newValue == 0);
                self.currentItem().agentAppType1(newValue);
            });
            self.agentAppType2.subscribe(function(newValue) {
                self.displayEmployeeInfo2(newValue == 0);
                self.currentItem().agentAppType2(newValue);
            });
            self.agentAppType3.subscribe(function(newValue) {
                self.displayEmployeeInfo3(newValue == 0);
                self.currentItem().agentAppType3(newValue);
            });
            self.agentAppType4.subscribe(function(newValue) {
                self.displayEmployeeInfo4(newValue == 0);
                self.currentItem().agentAppType4(newValue);
            });

            self.histItems = ko.observableArray([]);
            self.selectedTab = ko.observable('tab-1');

            self.employeeInputList = ko.observableArray([]);
            self.selectedItem = ko.observable();
            self.selectedItem.subscribe(function(newValue) {
                if (newValue) {
                    $.when(self.getAllAgen(newValue)).done(function() {
                        if (self.histItems().length > 0) {
                            self.histSelectedItem(self.histItems()[0].requestId);
                        } else {
                            self.initAgent();
                        }
                    });
                }
            });

            self.histSelectedItem = ko.observable("");
            self.histSelectedItem.subscribe(function(requestId) {
                if (requestId) {
                    $.when(self.getAgen(self.selectedItem(), requestId)).done(function() {
                        nts.uk.ui.errors.clearAll();
                        self.isEnableDelete(true);
                        self.isEnableAdd(true);
                        if (!nts.uk.text.isNullOrEmpty(self.currentItem().agentSid1())) {
                            service.findEmployeeName(self.currentItem().agentSid1()).done(function(response) {
                                self.employeeNameScreen1(response);
                            });
                        }
                        if (!nts.uk.text.isNullOrEmpty(self.currentItem().agentSid2())) {
                            service.findEmployeeName(self.currentItem().agentSid2()).done(function(response) {
                                self.employeeNameScreen2(response);
                            });
                        }
                        if (!nts.uk.text.isNullOrEmpty(self.currentItem().agentSid3())) {
                            service.findEmployeeName(self.currentItem().agentSid3()).done(function(response) {
                                self.employeeNameScreen3(response);
                            });
                        }
                        if (!nts.uk.text.isNullOrEmpty(self.currentItem().agentSid4())) {
                            service.findEmployeeName(self.currentItem().agentSid4()).done(function(response) {
                                self.employeeNameScreen4(response);
                            });
                        }
                    });
                }
            });

            self.itemList = ko.observableArray([
                new BoxModel(0, nts.uk.resource.getText("CMM044_16")),
                new BoxModel(1, nts.uk.resource.getText("CMM044_17")),
                new BoxModel(2, nts.uk.resource.getText("CMM044_18"))
            ]);
            self.dateValue = ko.observable({ startDate: '', endDate: '' });
            self.employeeList = ko.observable('');
            self.employeeNameScreen1 = ko.observable('');
            self.employeeNameScreen2 = ko.observable('');
            self.employeeNameScreen3 = ko.observable('');
            self.employeeNameScreen4 = ko.observable('');

            self.ccgcomponent = {};
            self.selectedCode = ko.observableArray([]);
            self.showinfoSelectedEmployee = ko.observable(true);

            self.isShow = ko.observable(false);
        }

        start() {
            let self = this;
            var dfd = $.Deferred();
            self.currentItem(new model.AgentAppDto(null, "", "", "", "", null, "", null, "", null, "", null));
            self.initCCG001();

            if (self.employeeInputList().length == 0) {
                self.isEnableDelete(false);
                self.isEnableAdd(false);
                service.searchEmployeeByLogin(self.baseDate()).done(data => {
                    if (data.length > 0) {
                        self.searchEmployee(data);
                    }

                    //                    self.initKCP009();
                }).fail(function(error) {
                    nts.uk.ui.dialog.alertError(error);
                });
            }
            dfd.resolve();
            return dfd.promise();

        }

        findEmployeeName(sId): JQueryPromise<string> {
            var self = this;
            var dfd = $.Deferred();
            service.findEmployeeName(sId).done(function(response) {
                dfd.resolve(response);
            }).fail(function(error) {
                dfd.resolve("");
            });

            return dfd.promise();
        }

        /**
         * find agen by employee
         */
        getAllAgen(employeeId): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            self.histItems.removeAll();
            service.findAllAgent(employeeId).done(function(agent_arr: Array<model.AgentDto>) {
                if (agent_arr && agent_arr.length) {
                    self.histItems.removeAll();
                    for (var i = 0; i < agent_arr.length; i++) {
                        self.histItems.push(new model.AgentDto(
                            agent_arr[i].companyId,
                            agent_arr[i].employeeId,
                            agent_arr[i].requestId,
                            agent_arr[i].startDate,
                            agent_arr[i].endDate,
                            agent_arr[i].agentSid1,
                            agent_arr[i].agentAppType1,
                            agent_arr[i].agentSid2,
                            agent_arr[i].agentAppType2,
                            agent_arr[i].agentSid3,
                            agent_arr[i].agentAppType3,
                            agent_arr[i].agentSid4,
                            agent_arr[i].agentAppType4));
                    }
                }
                dfd.resolve();
            }).fail(function(error) {
                alert(error.message);
                dfd.reject(error);
            });

            return dfd.promise();
        }

        /**
         * find agent by requestId
         */
        getAgen(employeeId: string, requestId: string): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            if (!requestId) {
                return;
            }

            var param = {
                employeeId: employeeId,
                requestId: requestId
            };
            service.findAgent(param).done(function(agent: model.AgentDto) {
                self.currentItem(new model.AgentAppDto(employeeId, requestId, agent.startDate, agent.endDate,
                    agent.agentSid1, agent.agentAppType1,
                    agent.agentSid2, agent.agentAppType2,
                    agent.agentSid3, agent.agentAppType3,
                    agent.agentSid4, agent.agentAppType4));

                self.agentAppType1(agent.agentAppType1);
                self.agentAppType2(agent.agentAppType2);
                self.agentAppType3(agent.agentAppType3);
                self.agentAppType4(agent.agentAppType4);

                self.dateValue({ startDate: agent.startDate, endDate: agent.endDate });
                $("#daterangepicker").find(".ntsStartDatePicker").focus();
                dfd.resolve();
            }).fail(function(error) {
                alert(error.message);
                dfd.reject(error);
            });

            return dfd.promise();
        }

        /**
         * Find agent
         */
        findInHistItem(employeeId: string, requestId: string): model.AgentDto {
            var self = this;
            return _.find(self.histItems(), function(item: any) {
                if (item.employeeId == employeeId && item.requestId == requestId) {
                    return item;
                }
            });
        }

        /**
         * Add new agent and Update agent
         */
        addAgent() {
            var self = this;
            if (nts.uk.ui.errors.hasError()) {
                return;
            }

            nts.uk.ui.block.invisible();
            self.currentItem().agentAppType1(self.agentAppType1());
            self.currentItem().agentAppType2(self.agentAppType2());
            self.currentItem().agentAppType3(self.agentAppType3());
            self.currentItem().agentAppType4(self.agentAppType4());
            self.currentItem().startDate(new Date(self.dateValue().startDate));
            self.currentItem().endDate(new Date(self.dateValue().endDate));
            self.currentItem().agentSid1(self.agentSid1());
            self.currentItem().agentSid2(self.agentSid2());
            self.currentItem().agentSid3(self.agentSid3());
            self.currentItem().agentSid4(self.agentSid4());

            var agent = ko.toJSON(self.currentItem());
            agent["employeeId"] = self.selectedItem();
            var existsItem = self.findInHistItem(self.currentItem().employeeId(), self.histSelectedItem());

            if (existsItem) {
                service.updateAgent(agent).done(function() {
                    self.getAllAgen(self.selectedItem());
                    nts.uk.ui.dialog.info({messageId: "Msg_15"});
                    $("#daterangepicker").find(".ntsStartDatePicker").focus();
                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError({messageId: res.messageId});
                }).always(function() {
                    nts.uk.ui.block.clear();
                })
            } else {
                service.addAgent(agent).done(function(res) {
                    var resObj = ko.toJS(res);
                    if (self.histSelectedItem) {
                        self.getAllAgen(self.selectedItem());
                        self.histSelectedItem(res);
                        nts.uk.ui.dialog.info({messageId: "Msg_15"});
                        $("#daterangepicker").find(".ntsStartDatePicker").focus();

                    }

                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError({messageId: res.messageId});
                }).always(function() {
                    nts.uk.ui.block.clear();
                })
            }
        }

        deleteAgent() {
            let self = this;
            var index_of_itemDelete = _.findIndex(self.histItems(), ['requestId', self.histSelectedItem()]);
            nts.uk.ui.dialog.confirm("データを削除します。\r\nよろしいですか？").ifYes(function() {
                var agent = {
                    employeeId: self.selectedItem(),
                    requestId: self.currentItem().requestId()
                };
                service.deleteAgent(agent).done(function() {
                    $.when(self.getAllAgen(self.selectedItem())).done(function() {
                        var requestId = "";
                        if (self.histItems().length == 0) {
                            self.initAgent();
                        } else if (self.histItems().length == 1) {
                            requestId = self.histItems()[0].requestId;
                        } else if (index_of_itemDelete == self.histItems().length) {
                            requestId = self.histItems()[index_of_itemDelete - 1].requestId;
                        } else {
                            requestId = self.histItems()[index_of_itemDelete].requestId;
                        }
                        nts.uk.ui.dialog.info({messageId:"Msg_16"}).then(() => {
                            nts.uk.ui.errors.clearAll();
                            self.histSelectedItem(requestId);
                            $("#daterangepicker").find(".ntsStartDatePicker").focus();
                        });
                    });
                }).fail(function(res) {
                    nts.uk.ui.dialog.alertError(res.message);
                })
            }).ifNo(function() {
            });
        }

        initAgent(): void {
            let self = this;
            if (!nts.uk.util.isNullOrUndefined(self.selectedItem())) {
                self.dateValue = ko.observable({});
                self.displayEmployeeInfo1(true);
                self.displayEmployeeInfo2(true);
                self.displayEmployeeInfo3(true);
                self.displayEmployeeInfo4(true);
                self.isEnableDelete(false);
                self.isEnableAdd(true);
                nts.uk.ui.errors.clearAll();
                self.agentAppType1(0);
                self.agentAppType2(2);
                self.agentAppType3(2);
                self.agentAppType4(2);
                self.employeeNameScreen1("");
                self.employeeNameScreen2("");
                self.employeeNameScreen3("");
                self.employeeNameScreen4("");
				self.agentSid1("");
				self.agentSid2("");
				self.agentSid3("");
				self.agentSid4("");

                self.selectedTab('tab-1');
                self.histSelectedItem("");
                self.currentItem(new model.AgentAppDto(self.selectedItem(), "", "", "", "", self.agentAppType1(), "", self.agentAppType2(), "", self.agentAppType3(), "", self.agentAppType4()));
                _.defer(() => {
                    $("#daterangepicker").find(".ntsStartDatePicker").focus();
                });
            }
        }

        openDDialog() {
            let self = this;
            nts.uk.ui.block.invisible();
            nts.uk.ui.windows.setShared('CMM044_TABS', self.tabs());
            nts.uk.ui.windows.sub.modal('/view/cmm/044/d/index.xhtml').onClosed(function(): any {
                nts.uk.ui.block.clear();
            });

        }
        openCDL021(tab: number, empId: string) {
            let self = this;
            nts.uk.ui.block.invisible();
            var selectedId = [];
            //            if (!empId) {
            //                selectedId.push(empId);
            //            }
            if (tab == 1) {
                selectedId.push(self.currentItem().agentSid1());
            }
            // Set Param
            setShared('CDL009Params', {
                // isMultiSelect For Employee List Kcp005
                isMultiSelect: false,
                // For Workplace List Kcp004
                selectedIds: selectedId,
                // For Workplace List Kcp004
                baseDate: self.baseDate(),
                // Workplace or Department
                target: 1
            }, true);
            nts.uk.ui.windows.sub.modal('/view/cdl/009/a/index.xhtml').onClosed(function(): any {
                var isCancel = nts.uk.ui.windows.getShared('CDL009Cancel');
                var employeeId = nts.uk.ui.windows.getShared('CDL009Output');
                if (isCancel) {
                    return;
                }
                if (employeeId) {
                    var tabNumber = tab;
                    if (tabNumber == 1) {
                        self.agentSid1(employeeId);
                        service.findEmployeeName(self.agentSid1()).done(function(response) {
                            self.employeeNameScreen1(response);
                        });
                    } else if (tabNumber == 2) {
                        self.agentSid2(employeeId);
                        service.findEmployeeName(self.agentSid2()).done(function(response1) {
                            self.employeeNameScreen2(response1);
                        });
                    } else if (tabNumber == 3) {
                        self.agentSid3(employeeId);
                        service.findEmployeeName(self.agentSid3()).done(function(response2) {
                            self.employeeNameScreen3(response2);
                        });
                    } else {
                        self.agentSid4(employeeId);
                        service.findEmployeeName(self.agentSid4()).done(function(response3) {
                            self.employeeNameScreen4(response3);
                        });
                    }
                }
                nts.uk.ui.block.clear();
            });
        }

        openCCG001() {
            let self = this;
            $("#ccgcomponent, #ccgcomponent_contents").toggleClass("show");
            isOnlyMe: ko.observable(false);
        }

        initCCG001(): void {
            let self = this;
            // Component option
            self.ccgcomponent = {
                /** Common properties */
                systemType: 2, // システム区分
                showEmployeeSelection: false, // 検索タイプ
                showQuickSearchTab: true, // クイック検索
                showAdvancedSearchTab: true, // 詳細検索
                showBaseDate: true, // 基準日利用
                showClosure: false, // 就業締め日利用
                showAllClosure: false, // 全締め表示
                showPeriod: false, // 対象期間利用
                periodFormatYM: false, // 対象期間精度

                /** Required parameter */
                baseDate: self.baseDate().toISOString(), // 基準日
                inService: true, // 在職区分
                leaveOfAbsence: true, // 休職区分
                closed: true, // 休業区分
                retirement: true, // 退職区分

                /** Quick search tab options */
                showAllReferableEmployee: true, // 参照可能な社員すべて
                showOnlyMe: false, // 自分だけ
                showSameWorkplace: true, // 同じ職場の社員
                showSameWorkplaceAndChild: true, // 同じ職場とその配下の社員

                /** Advanced search properties */
                showEmployment: false, // 雇用条件
                showWorkplace: true, // 職場条件
                showClassification: false, // 分類条件
                showJobTitle: false, // 職位条件
                showWorktype: false, // 勤種条件
                isMutipleCheck: true, // 選択モード

                /** Return data */
                returnDataFromCcg001: function(data: Ccg001ReturnedData) {
                    self.searchEmployee(data.listEmployee);
                }
            }
            // Start component
            $('#ccgcomponent').ntsGroupComponent(self.ccgcomponent);
        }

        initKCP009() {
            var self = this;
            var listComponentOption: ComponentOption = {
                systemReference: self.systemReference(),
                isDisplayOrganizationName: false,
                employeeInputList: self.employeeInputList,
                targetBtnText: self.targetBtnText,
                selectedItem: self.selectedItem,
                tabIndex: self.tabindex
            };
            $('#emp-component').ntsLoadListComponent(listComponentOption);
        }

        searchEmployee(dataEmployee: EmployeeSearchDto[]) {
            var self = this;
            self.employeeInputList.removeAll();
            _.forEach(dataEmployee, function(item: EmployeeSearchDto) {
                self.employeeInputList.push(new PersonModel({
                    id: item.employeeId,
                    code: item.employeeCode,
                    businessName: item.employeeName,
                }));
            });

            self.initKCP009();
        }
    }

    interface IPersonModel {
        //        personId: string;
        //        code: string;
        //        name: string;
        //        baseDate?: number;
        id: string;
        code: string;
        businessName: string;
        workplaceName?: string;
        depName?: string;
        baseDate?: number;
    }

    class PersonModel {
        id: string;
        code: string;
        businessName: string;
        workplaceName: string;
        depName: string;
        baseDate: number;

        constructor(param: IPersonModel) {
            this.id = param.id;
            this.code = param.code;
            this.businessName = param.businessName;
            this.workplaceName = param.workplaceName || "";
            this.depName = param.depName || "";
            this.baseDate = param.baseDate || 20170104;
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

    // THU NGHIEM KCP_009
    export interface ComponentOption {
        systemReference: SystemType;
        isDisplayOrganizationName: boolean;
        employeeInputList: KnockoutObservableArray<IPersonModel>;
        targetBtnText: string;
        selectedItem: KnockoutObservable<string>;
        tabIndex: number;
    }
    export interface EmployeeModel {
        id: string;
        code: string;
        businessName: string;
        depName?: string;
        workplaceName?: string;
    }
    export class SystemType {
        static EMPLOYMENT = 1;
        static SALARY = 2;
        static PERSONNEL = 3;
        static ACCOUNTING = 4;
        static OH = 6;
    }

    //  Phong
    export module model {
        export class AgentDto {
            companyId: string;
            employeeId: string;
            requestId: string;
            startDate: string;
            endDate: string;
            agentSid1: string;
            agentAppType1: number;
            agentSid2: string;
            agentAppType2: number;
            agentSid3: string;
            agentAppType3: number;
            agentSid4: string;
            agentAppType4: number;
            text: string;
            constructor(companyId: string, employeeId: string, requestId: string, startDate: string, endDate: string, agentSid1: string, agentAppType1: number, agentSid2: string, agentAppType2: number, agentSid3: string, agentAppType3: number, agentSid4: string, agentAppType4: number) {
                this.companyId = companyId;
                this.employeeId = employeeId;
                this.requestId = requestId;
                this.startDate = startDate;
                this.endDate = endDate;
                this.agentSid1 = agentSid1;
                this.agentAppType1 = agentAppType1;
                this.agentSid2 = agentSid2;
                this.agentAppType2 = agentAppType2;
                this.agentSid3 = agentSid3;
                this.agentAppType3 = agentAppType3;
                this.agentSid4 = agentSid4;
                this.agentAppType4 = agentAppType4;
                this.text = startDate + ' ~ ' + endDate;
            }
        }

        export class AgentAppDto {
            employeeId: KnockoutObservable<string>;
            requestId: KnockoutObservable<string>;
            startDate: KnockoutObservable<string>;
            endDate: KnockoutObservable<string>;
            agentSid1: KnockoutObservable<string>;
            agentAppType1: KnockoutObservable<number>;
            agentSid2: KnockoutObservable<string>;
            agentAppType2: KnockoutObservable<number>;
            agentSid3: KnockoutObservable<string>;
            agentAppType3: KnockoutObservable<number>;
            agentSid4: KnockoutObservable<string>;
            agentAppType4: KnockoutObservable<number>;
            constructor(employeeId: string, requestId: string, startDate: string, endDate: string, agentSid1: string, agentAppType1: number, agentSid2: string, agentAppType2: number, agentSid3: string, agentAppType3: number, agentSid4: string, agentAppType4: number) {
                this.employeeId = ko.observable(employeeId);
                this.requestId = ko.observable(requestId);
                this.startDate = ko.observable(startDate);
                this.endDate = ko.observable(endDate);
                this.agentSid1 = ko.observable(agentSid1);
                this.agentAppType1 = ko.observable(agentAppType1);
                this.agentSid2 = ko.observable(agentSid2);
                this.agentAppType2 = ko.observable(agentAppType2);
                this.agentSid3 = ko.observable(agentSid3);
                this.agentAppType3 = ko.observable(agentAppType3);
                this.agentSid4 = ko.observable(agentSid4);
                this.agentAppType4 = ko.observable(agentAppType4);
            }
        }

        export class DeleteAgent {
            companyId: string;
            employeeId: string;
            requestId: string;
            constructor(companyId: string, employeeId: string, requestId: string) {
                this.companyId = employeeId;
                this.employeeId = employeeId;
                this.requestId = requestId;
            }
        }
    }
}