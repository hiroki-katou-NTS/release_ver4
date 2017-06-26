module qmm020.c.viewmodel {
    export class ScreenModel {
        // listbox
        columns: KnockoutObservableArray<nts.uk.ui.NtsGridListColumn>;
        itemTotalList: KnockoutObservableArray<TotalModel>;
        itemHeaderList: KnockoutObservableArray<TotalModel>;
        currentItem: KnockoutObservable<TotalModel>;
        selectedCode: KnockoutObservable<string>;
        isEnable: KnockoutObservable<boolean>;
        maxItem: KnockoutObservable<TotalModel>;
        //selectedCode: KnockoutObservable<string>;
        dataSource: any;
        selectedList: any;

        constructor() {
            var self = this;
            let dfd = $.Deferred<any>();
            self.selectedCode = ko.observable();
            self.selectedList = ko.observableArray([]);
            self.isEnable = ko.observable(true);
            self.itemTotalList = ko.observableArray([]);
            self.itemHeaderList = ko.observableArray([]);
            self.maxItem = ko.observable(null);
            self.dataSource = ko.observableArray([]);
            self.currentItem = ko.observable(new TotalModel({
                historyId: '',
                employmentCode: '',
                paymentDetailCode: '',
                bonusDetailCode: '',
                startYm: '',
                endYm: ''
            }));


            // LST_001
            this.columns = ko.observableArray([
                { headerText: '', prop: 'startEnd', width: 200 }
            ]);

            self.selectedCode.subscribe(function(codeChange) {
                self.getHist();
                service.getEmployeeDetail(ko.toJS(codeChange)).done(function(data: Array<IModel>) {
                    let employeeItems: Array<IModel> = [];
                    if (data && data.length > 0) {
                        employeeItems = $("#C_LST_001").igGrid("option", "dataSource");
                        if (employeeItems.length == 0) {
                            employeeItems = data;
                        }
                        for (let i in employeeItems) {
                            let item = employeeItems[i];
                            let itemAllotSetting = _.find(data, function(allot) { return item.employmentCode == allot.employeeCode; });
                            if (itemAllotSetting) {
                                item.paymentDetailCode = itemAllotSetting.paymentDetailCode;
                                item.paymentDetailName = itemAllotSetting.paymentDetailName;
                                item.bonusDetailCode = itemAllotSetting.bonusDetailCode;
                                item.bonusDetailName = itemAllotSetting.bonusDetailName;
                            } else {
                                item.paymentDetailCode = undefined;
                                item.paymentDetailName = undefined;
                                item.bonusDetailCode = undefined;
                                item.bonusDetailName = undefined;
                            }
                        }

                        // Set data to grid
                        $("#C_LST_001").igGrid("option", "dataSource", employeeItems);

                        dfd.resolve();
                    }

                    //                    self.itemAllotSetting(employeeItems);
                    dfd.resolve();

                }).fail(function(res) {
                    //Alert message
                    alert(res);
                });
                dfd.promise();
                self.currentItem(self.getHist(codeChange));
            });
            // init columns and title for grid
            self.LoadData([]);
            // call first method
            self.start();
//            $("#C_BTN_001").click(function() {
//                alert("The paragraph was clicked.");
//            });
        }

        LoadData(itemList) {
            let self = this;
            $("#C_LST_001").igGrid({
                columns: [
                    { headerText: "繧ｳ繝ｼ繝�", key: "employmentCode", dataType: "string", width: "100px" },
                    { headerText: "蜷咲ｧｰ", key: "employmentName", dataType: "string", width: "200px" },
                    { headerText: "", key: "paymentDetailCode", dataType: "string", hidden: true },
                    { headerText: "", key: "paymentDetailName", dataType: "string", hidden: true },
                    { headerText: "", key: "bonusDetailCode", dataType: "string", hidden: true },
                    { headerText: "", key: "bonusDetailName", dataType: "string", hidden: true },
                    {
                        headerText: "邨ｦ荳取�守ｴｰ譖ｸ", key: "paymentDetailCode", dataType: "string", width: "250px", unbound: true,
                        template: '<button class="c-btn-001" onclick="__viewContext.viewModel.viewmodelC.openMDialog()">選択</button><span>${paymentDetailCode} ${paymentDetailName}</span>'
                    },
                    {
                        headerText: "雉樔ｸ取�守ｴｰ譖ｸ", key: "bonusDetailCode", dataType: "string", width: "20%", unbound: true,
                        template: '<button class="c-btn-002" onclick="__viewContext.viewModel.viewmodelC.openMDialog()">選択</button><span>${bonusDetailCode} ${bonusDetailName}</span>'
                    },
                ],
                features: [{
                    name: 'Selection',
                    mode: 'row',
                    multipleSelection: true,
                    activation: false,
                },
                ],
                virtualization: true,
                virtualizationMode: 'continuous',
                width: "800px",
                height: "240px",
                primaryKey: "employmentCode",
                dataSource: itemList

            });
        }

        //find histId to subscribe
        getHist(value: any) {
            let self = this;
            let rtHist = _.find(ko.toJS(self.itemHeaderList()), function(item: IModel) {
                return item.historyId == value;
            });
            return rtHist;
        }
        //Selected changed
//        selectionChanged(evt, ui) {
//            //console.log(evt.type);
//            var selectedRows = ui.selectedRows;
//
//            var arr = [];
//            for (var i = 0; i < selectedRows.length; i++) {
//                arr.push("" + selectedRows[i].id);
//            }
//            this.selectedList(arr);
//        };

        // start function
        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();
            //fill employ data to c_LST_001
            service.getEmployeeName().done(function(data: Array<IModel>) {
                let employeeItem: Array<TotalModel> = [];
                if (data && data.length > 0) {
                    _.forEach(data, function(item) {
                        employeeItem.push({ historyId: item.historyId, employmentCode: item.employmentCode, employmentName: item.employmentName });
                    });
                    dfd.resolve();
                }
                self.itemTotalList(employeeItem);
                // self.currentItem(employeeItem.employmentName);
                // Set datafor grid
                $("#C_LST_001").igGrid("option", "dataSource", employeeItem);
                dfd.resolve();

            }).fail(function(res) {
                // Alert message
                alert(res);
            });
            dfd.promise();
            //Get list startDate, endDate of History  
            let totalItem: Array<TotalModel> = [];
            service.getEmployeeAllotHeaderList().done(function(data: Array<IModel>) {
                if (data.length > 0) {
                    _.forEach(data, function(item) {
                        totalItem.push(new TotalModel({ historyId: item.historyId, startEnd: item.startYm + ' ~ ' + item.endYm, endYm: item.endYm, startYm: item.startYm }));
                    });
                    ko.mapping.toJS(self.itemHeaderList(totalItem));
                    debugger;
                    //let max = _.maxBy(self.itemList(), (itemMax) => { return itemMax.endYm });
                    dfd.resolve();
                } else {
                    dfd.resolve();
                }
            }).fail(function(res) {
                // Alert message
                alert(res);
            });

            service.getAllotEmployeeMaxDate().done(function(itemMax: number) {
                let maxDate: TotalModel = _.find(self.itemHeaderList(), function(obj) { return parseInt(obj.endYm()) == itemMax; });
                self.maxDate = (maxDate.startYm || "").toString();
                self.maxItem(maxDate);
                console.log(self.maxItem());
                console.log(self.maxDate);
                dfd.resolve();
            }).fail(function(res) {
                alert(res);
            });


            // Return.
            return dfd.promise();
        }
        //click register button
        /**
         * 
         */


        //        register() {
        //            var self = this;
        //            var current = _.find(self.itemTotalList(), function(item: IModel) { return item.historyId == self.currentItem().historyId(); });
        //            //            debugger;
        //            if (current) {
        //                //                service.insertEmAllot(current).done(function() {
        //                //                }).fail(function(res) {
        //                //                    alert(res);
        //                //                });
        //            }
        //        }
        //Open dialog Add History
        openJDialog() {
            var self = this;
            debugger;
            var historyScreenType = "1";
            let valueShareJDialog = historyScreenType + "~" + "201701";

            nts.uk.ui.windows.setShared('valJDialog', valueShareJDialog);

            nts.uk.ui.windows.sub.modal('../j/index.xhtml', { title: '譏守ｴｰ譖ｸ縺ｮ邏舌★縺托ｼ槫ｱ･豁ｴ霑ｽ蜉�' })
                .onClosed(function() {
                    let returnJDialog: string = nts.uk.ui.windows.getShared('returnJDialog');
                    if (!nts.uk.util.isNullOrUndefined(returnJDialog)) {

                        var modeRadio = returnJDialog.split("~")[0];
                        var returnValue = returnJDialog.split("~")[1];
                        if (returnValue != '') {
                            //                        let employeeAllotSettings = new Array<EmployeeAllotSettingDto>();
                            var items = self.itemTotalList();
                            var addItem;
                            var copItem;
                            if (modeRadio === "2") {
                                addItem = new TotalModel({
                                    historyId: '',
                                    employmentCode: '',
                                    paymentDetailCode: '',
                                    bonusDetailCode: '',
                                    startYm: returnValue,
                                    endYm: '999912',
                                    startEnd: (returnValue + ' ~ ' + '999912')
                                });
                                items.push(addItem);
                            } else {
                                copItem = new TotalModel({
                                    historyId: '',
                                    employmentCode: '',
                                    employmentName: '',
                                    paymentDetailCode: '',
                                    paymentDetailName: '',
                                    bonusDetailCode: '',
                                    bonusDetailName: '',
                                    startYm: returnValue,
                                    endYm: '999912',
                                    startEnd: (returnValue + ' ~ ' + '999912')
                                });
                                self.currentItem().historyId(copItem.historyId());
                                self.currentItem().startYm(returnValue);
                                self.currentItem().endYm('999912');
                                self.currentItem().employmentCode(self.maxItem().historyId());
                                //get employmentName, paymentDetailName, paymentDetailCode
                                let dfd = $.Deferred<any>();
                                service.getAllEmployeeAllotSetting(ko.toJS(self.maxItem().historyId())).done(function(data) {
                                    self.itemListDetail([]);
                                    if (data && data.length > 0) {
                                        _.map(data, function(item: IModel) {
                                            self.itemListDetail.push(new copItem(item.historyId, item.employmentCode, item.employmentName, item.paymentDetailCode
                                                , item.paymentDetailName, item.bonusDetailCode, item.bonusDetailName, item.startYm, item.endYm, item.startEnd));
                                        });
                                        self.currentItem().employmentCode(ko.toJS(self.itemListDetail()[0].employmentCode));
                                        self.currentItem().employmentName(ko.toJS(self.itemListDetail()[0].employmentName));
                                        self.currentItem().paymentDetailCode(ko.toJS(self.itemListDetail()[0].paymentDetailCode));
                                        self.currentItem().paymentDetailName(ko.toJS(self.itemListDetail()[0].paymentDetailName));
                                        self.currentItem().bonusDetailCode(ko.toJS(self.itemListDetail()[0].bonusDetailCode));
                                        self.currentItem().bonusDetailName(ko.toJS(self.itemListDetail()[0].bonusDetailName));
                                        dfd.resolve();
                                    }

                                    $("#C_LST_001").igGrid("option", "dataSource", ko.mapping.toJS(self.itemListDetail));
                                    dfd.resolve();
                                }).fail(function(res) {
                                    // Alert message
                                    alert(res);
                                });
                                dfd.promise();

                                items.push(copItem);

                            }

                            self.itemTotalList([]);
                            self.itemTotalList(items);
                        }
                    }
                });


        }
        //Open dialog Edit History
        openKDialog() {
            var self = this;
            //var singleSelectedCode = self.singleSelectedCode().split(';');
            //nts.uk.ui.windows.setShared('stmtCode', singleSelectedCode[0]);
            nts.uk.ui.windows.sub.modal('/view/qmm/020/k/index.xhtml', { title: '譏守ｴｰ譖ｸ縺ｮ邏舌★縺托ｼ槫ｱ･豁ｴ邱ｨ髮�' }).onClosed(function(): any {
                //self.start(self.singleSelectedCode());
            });
        }

        openMDialog() {
            var self = this;
            var valueShareMDialog = self.currentItem().startYm;
            //debugger;
            nts.uk.ui.windows.setShared('valMDialog', valueShareMDialog);
            nts.uk.ui.windows.sub.modal('/view/qmm/020/m/index.xhtml', { title: '隴丞ｮ茨ｽｴ�ｽｰ隴厄ｽｸ邵ｺ�ｽｮ鬩包ｽｸ隰夲ｿｽ' }).onClosed(function(): any {
                //get selected code from M dialog
                var stmtCodeSelected = nts.uk.ui.windows.getShared('stmtCodeSelected');
                ko.mapping.toJS(self.currentItem().paymentDetailCode(stmtCodeSelected));
                //get Name payment Name
                service.getAllotLayoutName(self.currentItem().payCode()).done(function(stmtName: string) {
                    self.currentItem().payName(stmtName);
                }).fail(function(res) {
                    alert(res);
                });

            });
        }

    }


    interface IModel {
        companyCode?: string;
        historyId: string;

        employmentCode?: string;
        employmentName?: string;
        paymentDetailCode?: string;
        paymentDetailName?: string;
        bonusDetailCode?: string;
        bonusDetailName?: string;
        startYm?: string;
        endYm?: string;
        startEnd?: string;
    }

    class TotalModel {
        id: string;
        companyCode: KnockoutObservable<string>;
        historyId: KnockoutObservable<string>;

        employmentCode: KnockoutObservable<string>;
        employmentName: KnockoutObservable<string>;
        paymentDetailCode: KnockoutObservable<string>;
        paymentDetailName: KnockoutObservable<string>;
        bonusDetailCode: KnockoutObservable<string>;
        bonusDetailName: KnockoutObservable<string>;
        startYm: KnockoutObservable<string>;
        endYm: KnockoutObservable<string>;
        startEnd: string;
        constructor(param: IModel) {
            this.companyCode = ko.observable(param.companyCode);
            this.id = param.historyId;
            this.historyId = ko.observable(param.historyId);
            this.startEnd = param.startEnd;
            this.employmentCode = ko.observable(param.employmentCode);
            this.employmentName = ko.observable(param.employmentName);
            this.paymentDetailCode = ko.observable(param.paymentDetailCode);
            this.paymentDetailName = ko.observable(param.paymentDetailName);
            this.bonusDetailCode = ko.observable(param.bonusDetailCode);
            this.bonusDetailName = ko.observable(param.bonusDetailName);
            this.startYm = ko.observable(param.startYm);
            this.endYm = ko.observable(param.endYm);
        }
    }

}

