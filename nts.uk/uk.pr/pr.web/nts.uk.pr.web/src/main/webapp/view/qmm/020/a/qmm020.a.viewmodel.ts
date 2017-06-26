module qmm020.a.viewmodel {
    export class ScreenModel {
        itemGrid: KnockoutObservable<IgGrid>;
        itemList: KnockoutObservableArray<ItemModel>;
        //test
        itemList1: KnockoutObservableArray<ItemModel>;
        itemName: KnockoutObservable<string>;
        textSearch: string = "";
        currentCode: KnockoutObservable<number>
        selectedCodes: KnockoutObservableArray<string>;
        //Display of Tab Panel

        //ADD Value of items in list history
        selectedCode: KnockoutObservable<string>;
        selectedName: KnockoutObservable<string>;
        //````````````````````
        singleSelectedCode: KnockoutObservable<string>;
        //layouts: KnockoutObservableArray<service.model.LayoutMasterDto>;
        allowClick: KnockoutObservable<boolean> = ko.observable(true);
        columns: KnockoutObservableArray<any>;

        isEnable: KnockoutObservable<boolean>;

        //Screen F
        departmentItems: any;
        returnJDialog: KnockoutObservable<string>;
        returnLDialog: KnockoutObservable<string>;

        companyallots: KnockoutObservableArray<service.model.CompanyAllotSettingDto>;

        constructor() {
            var self = this;
            //data of list history
            self.itemList = ko.observableArray([]);


            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            //~~~~~~　　Screen D ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            //self.selectedCode= ko.observableArray([]);
            self.singleSelectedCode = ko.observable(null);
            //self.index = 0;
            self.columns = ko.observableArray([{ headerText: "Item Code", width: "150px", key: 'code', dataType: "string", hidden: false },
                { headerText: "Item Text", key: 'nodeText', width: "200px", dataType: "string" }]);
            //SCREEN B 
            //            self.paycode = ko.observable("");
            //            self.payname = ko.observable("");
            //            self.bonuscode = ko.observable("");
            // elf.bonusname = ko.observable("");
            
            self.itemName = ko.observable('');
            //self.selectedCode = ko.observable(null);
            self.isEnable = ko.observable(true);
            //self.selectedCode = ko.observableArray(null);
            self.selectedName = ko.observable(null);
            //          self.singleSelectedCode = ko.observa            
            //subcribe list box's change
            //            self.selectedCode.subscribe(function(codeChange) {
            //                if (codeChange.length > 0) {
            //                    //get ValueName when click to item in HistoryList
            //                    let row = _.find(self.itemList(), function(item) {
            //                        let code = self.selectedCode();
            //                        if (code === item.code) {
            //                            self.selectedName(item.name);
            //                        }
            //                    });
            //
            //                    self.employment([]);
            //                    self.employment(self.employment2());
            //                    self.buildGrid("#grid", "C_BTN_001", "C_BTN_002");
            //                }
            //            });


            $('#A_LST_001').on('selectionChanging', function(event) {
                console.log('Selecting value:' + (<any>event.originalEvent).detail);
            })
            $('#A_LST_001').on('selectionChanged', function(event: any) {
                console.log('Selected value:' + (<any>event.originalEvent).detail)
            })
            //            // Array Data 1 
            //            self.employment1 = ko.mapping.fromJS([
            //                { "NO": 1, "ID": "000000001", "Name": "正社員", "PaymentDocID": "K001", "PaymentDocName": "給与明細書001", "BonusDocID": "S001", "BonusDocName": "賞与明細書001" },
            //                { "NO": 2, "ID": "000000002", "Name": "DucPham社員", "PaymentDocID": "K002", "PaymentDocName": "給与明細書002", "BonusDocID": "S001", "BonusDocName": "賞与明細書002" },
            //                { "NO": 3, "ID": "000000003", "Name": "HoangMai社員", "PaymentDocID": "K003", "PaymentDocName": "給与明細書003", "BonusDocID": "S001", "BonusDocName": "賞与明細書003" }
            //            ]);
            //            // Array Data 2 
            //            self.employment2 = ko.mapping.fromJS([
            //                { "NO": 1, "ID": "000000004", "Name": "ABC社員", "PaymentDocID": "K001", "PaymentDocName": "給与明細書001", "BonusDocID": "S001", "BonusDocName": "賞与明細書001" },
            //                { "NO": 2, "ID": "000000005", "Name": "DEF社員", "PaymentDocID": "K002", "PaymentDocName": "給与明細書002", "BonusDocID": "S001", "BonusDocName": "賞与明細書002" },
            //                { "NO": 3, "ID": "000000006", "Name": "GHK社員", "PaymentDocID": "K003", "PaymentDocName": "給与明細書003", "BonusDocID": "S001", "BonusDocName": "賞与明細書003" }
            //            ]);

            //            self.employment = ko.observableArray([]);
            //            self.employment(self.employment1());
            //            self.buildGrid("#grid", "C_BTN_001", "C_BTN_002");
            //            self.buildGrid("#E_LST_001", "E_BTN_001", "E_BTN_002");
            // sd(,  "
            //            self.buildGrid("#G_LST_001", "G_BTN_001", "G_BTN_002");
            //
            //            $("#grid").igGridSelection("selectRow", 0);
            
            
            
            
            
            self.start();
        };
        // start function
        start(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred<any>();
            //Get list startDate, endDate of History 
            service.getAllotCompanyList().done(function(companyAllots: Array<service.model.CompanyAllotSettingDto>) {
                //console.log(companyAllots);
                if (companyAllots.length > 0) {
                    //                    let _items : Array<ItemModel> = [];
                    //                    //push data to listItem of hist List
                    //                   _.forEach(companyAllots, function(historyCompany, i) {
                    //                        _items.push(new ItemModel(i + 1, historyCompany.startDate+'~'+historyCompany.endDate));
                    //                       if(i == companyAllots.length - 1){
                    //                           self.itemList(_items);
                    //                       }
                    //                    });
                    //                    //Get Code, Name of PayDoc, BonusDoc
                    //                    self.paycode(companyAllots[0].paymentDetailCode);
                    //                    self.bonuscode(companyAllots[1].bonusDetailCode);
                    dfd.resolve();
                } else {
                    self.allowClick(false);
                    dfd.resolve();
                }
            }).fail(function(res) {
                // Alert message
                alert(res);
            });
            // Return.
            return dfd.promise();
        }


        //Rebuild igGrid Screen C
        //        buildGrid(id: string, buttonKID: string, buttonSID: string) {
        //            var self = this;
        //            if ($(id).data("igGrid") !== undefined) {
        //                $(id).igGrid({
        //                    dataSource: [],
        //                    features: []
        //                });
        //                $(id).igGrid("destroy");
        //            }
        //            $(id).igGrid({
        //                columns: [
        //                    { headerText: "", key: "NO", dataType: "string", width: "20px" },
        //                    { headerText: "コード", key: "ID", dataType: "string", width: "100px" },
        //                    { headerText: "名称", key: "Name", dataType: "string", width: "200px" },
        //                    { headerText: "", key: "PaymentDocID", dataType: "string", hidden: true },
        //                    { headerText: "", key: "PaymentDocName", dataType: "string", hidden: true },
        //                    { headerText: "", key: "BonusDocID", dataType: "string", hidden: true },
        //                    { headerText: "", key: "BonusDocName", dataType: "string", hidden: true },
        //                    {
        //                        headerText: "給与明細書", key: "PaymentDocID", dataType: "string", width: "250px", unbound: true,
        //                        template: "<input type='button' id='" + buttonKID + "' value='選択'/><label style='margin-left:5px;'>${PaymentDocID}</label><label style='margin-left:15px;'>${PaymentDocName}</label>"
        //                    },
        //                    {
        //                        headerText: "賞与明細書", key: "BonusDoc", dataType: "string", width: "20%", unbound: true,
        //                        template: "<input type='button' id='" + buttonSID + "' value='選択'/><label style='margin-left:5px;'>${BonusDocID}</label><label style='margin-left:15px;'>${BonusDocName}</label>"
        //                    },
        //                ],
        //                primaryKey: "ID",
        //                width: "800px",
        //                autofitLastColumn: true,
        //                avgColumnWidth: "100px",
        //                dataSource: ko.mapping.toJS(self.employment()),
        //                value: self.currentCodeL      //                cellClick: self.openPaymentDialog,
        //                features: [{
        //                    name: "Selection",
        //                    mode: "row",
        //                    activation: true
        //                }]
        //            });
        //        }
        
        //Search Employment
        //        searchEmployment(idInputSearch: string, idList: string) {
        //            var self = this;
        //            let textSearch: string = $("#C_INP_001").val().trim();
        //            if (textSearch.length === 0) {
        //                nts.uk.ui.dialog.alert("コード/名称が入力されていません。");
        //            } else {
        //                if (self.textSearch !== textSearch) {
        //                    let searchResult = _.filter(self.products, function(item) {
        //                        return _.includes(item.ID, textSearch) || _.includes(item.Name, textSearch);
        //                    });
        //                    self.queueSearchResult = [];
        //                    for (let item of searchResult) {
        //                        self.queueSearchResult.push(item);
        //                    }
        //                    self.textSearch = textSearch;
        //                }
        //                if (self.queueSearchResult.length === 0) {
        //                    nts.uk.ui.dialog.alert("対象データがありません。");
        //                } else {
        //                    let firstResult: ItemModel = _.first(self.queueSearchResult);
        //                    //self.listBox().selectedCode(firstResult.id);
        //                    $("#grid").igGridSelection("selectRowById", firstResult.ID);
        //                    self.queueSearchResult.shift();
        //                    self.queueSearchResult.push(firstResult);
        //                }
        //            }
        //        }
        //        //Open dialog Add History
        //        openJDialog() {
        //            var self = this;
        //            //console.log(self);
        //            //alert($($("#sidebar-area .navigator a.active")[0]).attr("href"));
        //            var valueShareJDialog = $($("#sidebar-area .navigator a.active")[0]).attr("href");
        //            //Get value TabCode + value of selected Name in History List
        //            valueShareJDialog = valueShareJDialog + "~" + self.selectedName();
        //            nts.uk.ui.windows.setShared('valJDialog', valueShareJDialog);
        //            nts.uk.ui.windows.sub.modal('/view/qmm/020/f/index.xhtml', { title: '明細書の紐ずけ＞履歴追加' }).onClosed(function(): any {
        //                self.returnJDialog = ko.observable(nts.uk.ui.windows.getShared('returnJDialog'));
        //                //self.itemList.removeAll();
        //                self.itemList.push(new ItemModel('4', self.returnJDialog() + '~9999/12'));
        //            });
        //        }
        //        //Open dialog Edit History
        //        openKDialog() {
        //            var self = this;
        //            //var singleSelectedCode = self.singleSelectedCode().split(';');
        //            //nts.uk.ui.windows.setShared('stmtCode', singleSelectedCode[0]);
        //            nts.uk.ui.windows.sub.modal('/view/qmm/020/k/index.xhtml', { title: '明細書の紐ずけ＞履歴編集' }).onClosed(function(): any {
        //                //self.start(self.singleSelectedCode());
        //            });
        //        }
        //Open dialog TabPanel SETTING
        openMainLDialog() {
            var self = this;
            debugger;
            nts.uk.ui.windows.sub.modal('/view/qmm/020/l/index.xhtml', { title: '利用単位の設定' }).onClosed(function(): any {
                self.returnLDialog = ko.observable(nts.uk.ui.windows.getShared('arrSettingVal'));
                //alert(self.returnLDialog().split('~')[0]+self.returnLDialog().split('~')[1]+self.returnLDialog().split('~')[2]);
                if (self.returnLDialog().split('~')[0] === '2') {
                    $("#sidebar").ntsSideBar("hide", 1);
                    $("#sidebar").ntsSideBar("hide", 2);
                    $("#sidebar").ntsSideBar("hide", 3);
                    $("#sidebar").ntsSideBar("hide", 4);
                    $("#sidebar").ntsSideBar("hide", 5);
                } else {
                    if (self.returnLDialog().split('~')[1] === '1') {
                        $("#sidebar").ntsSideBar("show", 1);
                        $("#sidebar").ntsSideBar("hide", 2);
                        $("#sidebar").ntsSideBar("hide", 3);
                        $("#sidebar").ntsSideBar("hide", 4);
                        $("#sidebar").ntsSideBar("hide", 5);
                    };
                    if (self.returnLDialog().split('~')[1] === '2') {
                        $("#sidebar").ntsSideBar("hide", 1);
                        $("#sidebar").ntsSideBar("show", 2);
                        $("#sidebar").ntsSideBar("hide", 3);
                        $("#sidebar").ntsSideBar("hide", 4);
                        $("#sidebar").ntsSideBar("hide", 5);
                    };
                    if (self.returnLDialog().split('~')[1] === '3') {
                        $("#sidebar").ntsSideBar("hide", 1);
                        $("#sidebar").ntsSideBar("hide", 2);
                        $("#sidebar").ntsSideBar("show", 3);
                        $("#sidebar").ntsSideBar("hide", 4);
                        $("#sidebar").ntsSideBar("hide", 5);
                    };
                    if (self.returnLDialog().split('~')[1] === '4') {
                        $("#sidebar").ntsSideBar("hide", 1);
                        $("#sidebar").ntsSideBar("hide", 2);
                        $("#sidebar").ntsSideBar("hide", 3);
                        $("#sidebar").ntsSideBar("show", 4);
                        $("#sidebar").ntsSideBar("hide", 5);
                    };
                    if (self.returnLDialog().split('~')[1] === '5') {
                        $("#sidebar").ntsSideBar("hide", 1);
                        $("#sidebar").ntsSideBar("hide", 2);
                        $("#sidebar").ntsSideBar("hide", 3);
                        $("#sidebar").ntsSideBar("hide", 4);
                        $("#sidebar").ntsSideBar("show", 5);
                    };
                };
                if (self.returnLDialog().split('~')[2] === '1') {
                    $("#sidebar").ntsSideBar("show", 6);
                }
                if (self.returnLDialog().split('~')[2] === '2') {
                    $("#sidebar").ntsSideBar("hide", 6);
                }
            });
        }
    }
    //Item History List
    class ItemModel {
        code: string;
        name: string;
        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }

    //item tree view 
    class Node {
        code: string;
        name: string;
        nodeText: string;
        custom: string;
        childs: any;
        constructor(code: string, name: string, childs: Array<Node>) {
            var self = this;
            self.code = code;
            self.name = name;
            self.nodeText = self.code + ' ' + self.name;
            self.childs = childs;
            self.custom = 'Random' + new Date().getTime();
        }
    }
}