// TreeGrid Node
module qpp011.f {
    export class ScreenModel {
        //gridlist
        F_LST_001_Data: any;
        columns_F_LST_001: any;
        F_LST_001_selectedValue: any;
        F_LST_001_columns: any;
        //Tree Data Variable
        TreeArray: any;
        ListLocation: any;
        ResidentialData: any = ko.observable([]);
        currentIndex: any = 0;
        //End Data Variable
        //currencyeditor
        F_LBL_002_Year_Month: any;
        F_Year_Month: any;
        currencyeditor: any;
        F_LBL_003_yearInJapanEmpire: any;
        //Switch
        roundingRules: KnockoutObservableArray<any>;
        selectedRuleCode: any;
        enable: KnockoutObservable<boolean>;
        constructor() {
            var self = this;
            //gridlist data
            self.F_LBL_002_Year_Month = ko.observable(null);
            self.F_Year_Month = ko.observable(null);
            self.F_LST_001_Data = ko.observable([]);
            self.F_LST_001_selectedValue = ko.observableArray([]);
            self.F_LST_001_columns = [
                { headerText: "position", key: "position", width: "30px", dataType: "string", hidden: true },
                { headerText: "code", key: "code", width: "30px", dataType: "string", hidden: true },
                { headerText: "コード/名称", key: "displayText", width: "230px", dataType: "string" },
            ];
            function registeredNameAndCode(val, row) {
                return row.resiTaxCode + " " + row.registeredName;
            }
            service.findAllResidential().done(function(data: Array<any>) {
                service.getlistLocation().done(function(listLocation: Array<any>) {
                    self.ResidentialData(data);
                    self.ListLocation = listLocation;
                    let ArrayData = CreateDataArray(listLocation, data);
                    self.F_LST_001_Data(ArrayData);
                    BindTreeGrid("#F_LST_001", self.F_LST_001_Data(), self.F_LST_001_selectedValue);
                }).fail(function(res) {
                    // Alert message
                    nts.uk.ui.dialog.alert("対象データがありません。");
                });

            }).fail(function(res) {
                // Alert message
                alert(res.message);
            });

            let $F_LST_001 = $("#F_LST_001");
            self.F_LST_001_selectedValue.subscribe(function(newValue) {
                let selectedRows = _.map($F_LST_001.igTreeGridSelection("selectedRows"), function(row) {
                    if (row != undefined)
                        return row.id;
                });
                if (!_.isEqual(selectedRows, newValue)) {
                    $F_LST_001.igTreeGridSelection("clearSelection");
                    newValue.forEach(function(id) {
                        $F_LST_001.igTreeGridSelection("selectRowById", id);
                    });
                }
            });
            function BindTreeGrid(gridID, Data, selectedValue) {
                $(gridID).igTreeGrid({
                    width: "450px",
                    height: "300px",
                    dataSource: Data,
                    autoGenerateColumns: false,
                    primaryKey: "position",
                    foreignKey: "child",
                    columns: self.F_LST_001_columns,
                    initialExpandDepth: 2,
                    features: [
                        {
                            name: "Selection",
                            multipleSelection: true,
                            rowSelectionChanged: function(evt: any, ui: any) {
                                var selectedRows: Array<any> = ui.selectedRows;
                                selectedValue(_.map(selectedRows, function(row) {
                                    return row.id;
                                }));
                            }
                        },
                        {
                            name: "RowSelectors",
                            enableCheckBoxes: true,
                            checkBoxMode: "biState",
                            enableSelectAllForPaging: true,
                            enableRowNumbering: false
                        }]
                });
                $(gridID).setupSearchScroll("igTreeGrid");
            }
            //Create Tree Data
            function CreateDataArray(TreeData, Data) {
                self.TreeArray = [];
                let parentPositionIndex = 0;
                for (let Object of Data) {
                    let positionIndex = CreateTreeBranchs(Object.prefectureCode);
                    if (positionIndex) {
                        self.TreeArray.push(new TreeItem(Object.resiTaxCode, Object.resiTaxName, positionIndex, Object.resiTaxCode, Object.resiTaxCode + " " + Object.registeredName, 'Item'));
                        // self.currentIndex++;
                    }
                }
                return self.TreeArray;
            }
            function CreateTreeBranchs(prefectureCode) {
                return CreateBranch(CreateTreeRoot(prefectureCode), prefectureCode);
            }
            function CreateTreeRoot(prefectureCode) {
                let prefectureCodeInt = parseInt(prefectureCode);
                let PositionIndex;
                switch (true) {
                    case (prefectureCodeInt == 1):
                        PositionIndex = AddTreeRootToArray(prefectureCode, "1");
                        break;
                    case (2 <= prefectureCodeInt && prefectureCodeInt <= 7):
                        PositionIndex = AddTreeRootToArray(prefectureCode, "2");
                        break;
                    case (8 <= prefectureCodeInt && prefectureCodeInt <= 14):
                        PositionIndex = AddTreeRootToArray(prefectureCode, "3");
                        break;
                    case (15 <= prefectureCodeInt && prefectureCodeInt <= 23):
                        PositionIndex = AddTreeRootToArray(prefectureCode, "4");
                        break;
                    case (24 <= prefectureCodeInt && prefectureCodeInt <= 30):
                        PositionIndex = AddTreeRootToArray(prefectureCode, "5");
                        break;
                    case (31 <= prefectureCodeInt && prefectureCodeInt <= 35):
                        PositionIndex = AddTreeRootToArray(prefectureCode, "6");
                        break;
                    case (26 <= prefectureCodeInt && prefectureCodeInt <= 39):
                        PositionIndex = AddTreeRootToArray(prefectureCode, "7");
                        break;
                    case (40 <= prefectureCodeInt && prefectureCodeInt <= 47):
                        PositionIndex = AddTreeRootToArray(prefectureCode, "8");
                        break;
                }
                return PositionIndex;
            }
            function AddTreeRootToArray(prefectureCode, regionCode) {
                let positionIndex = self.currentIndex;
                let returnValue;
                let root = _.find(self.TreeArray, { 'code': regionCode, 'typeBranch': 'Root' });
                if (!root) {
                    root = _.find(self.ListLocation, { 'regionCode': regionCode });
                    if (root) {
                        let NewRoot = new TreeItem(root["regionCode"], root["regionName"], -1, positionIndex + 1, root["regionName"], 'Root');
                        self.TreeArray.push(NewRoot);
                        self.currentIndex++;
                        returnValue = self.currentIndex;
                    }
                } else {
                    returnValue = root["position"];
                }
                return returnValue;
            }
            function CreateBranch(parrentIndex, prefectureCode) {
                if (parrentIndex) {
                    let firstBranch = _.find(self.TreeArray, { 'code': prefectureCode, 'typeBranch': 'firstBranch' });
                    let positionIndex = self.currentIndex;
                    let returnValue;
                    if (!firstBranch) {
                        for (let object of self.ListLocation) {
                            firstBranch = _.find(object.prefectures, { 'prefectureCode': prefectureCode });
                            if (firstBranch)
                                break;
                        }
                        if (firstBranch) {
                            let NewBranch = new TreeItem(firstBranch["prefectureCode"], firstBranch["prefectureName"], parrentIndex, positionIndex + 1, firstBranch["prefectureName"], 'firstBranch');
                            self.TreeArray.push(NewBranch);
                            self.currentIndex++;
                            returnValue = self.currentIndex;
                        }
                    } else {
                        returnValue = firstBranch["position"];
                    }
                    return returnValue;
                }
            }
            //End Create Tree Data
            //end set tree data
            //currencyeditor
            self.currencyeditor = {
                value: ko.observable(),
                constraint: '',
                option: ko.mapping.fromJS(new nts.uk.ui.option.CurrencyEditorOption({
                    grouplength: 3,
                    decimallength: 2,
                    currencyformat: "USD",
                    currencyposition: 'right'
                })),
                required: ko.observable(false),
                enable: ko.observable(true),
                readonly: ko.observable(false)
            };
            //Switch
            self.enable = ko.observable(true);
            self.roundingRules = ko.observableArray([
                { code: 1, name: '納付先別' },
                { code: 2, name: '個人明細別' }
            ]);
            self.selectedRuleCode = ko.observable(1);
            self.F_LBL_002_Year_Month(nts.uk.ui.windows.getShared('QPP011_F_TargetDate'));
            //self.F_LBL_002_Year_Month = nts.uk.sessionStorage.getItemAndRemove("TargetDate").value;
            self.F_Year_Month(nts.uk.ui.windows.getShared('QPP011_F_DesignatedMonth'));
            self.F_LBL_003_yearInJapanEmpire = ko.observable();
            self.F_LBL_003_yearInJapanEmpire("(" + nts.uk.time.yearmonthInJapanEmpire(self.F_LBL_002_Year_Month()).toString() + ")");

        }
        submitDialog() {
            nts.uk.ui.windows.close();
        }
        closeDialog() {
            nts.uk.ui.windows.close();
        }

        exportPdf(): void {
            var self = this;
            if (self.F_LST_001_selectedValue().length > 0) {
                var command = {
                    residentTaxCodeList: self.F_LST_001_selectedValue(),
                    yearMonth: self.F_LBL_002_Year_Month(),
                    //201703
                    processingYearMonth: self.F_Year_Month(),
                    processingDate: nts.uk.time.yearmonthInJapanEmpire(self.F_LBL_002_Year_Month()).toString(),
                };
                service.saveAsPdf(self.selectedRuleCode(),command).done(function(res) {
                  //
                  if (res.error.businessException) {
                    nts.uk.ui.dialog.alert(res.error.messageId);    
                  }
                }).fail(function(res) {
                    nts.uk.ui.dialog.alert(res.message);
                });

            } else {
                nts.uk.ui.dialog.alert("納付先が選択されていせん。");
            }
        }
    }
    export class GridItemModel_F_LST_001 {
        code: string;
        name: string;

        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }
    //Tree Data Class
    export class TreeItem {
        code: string;
        name: string;
        child: number;
        position: string;
        displayText: string;
        typeBranch: string;

        constructor(code: string, name: string, child: number, position: string, displayText: string, typeBranch: string) {
            this.code = code;
            this.name = name;
            this.child = child;
            this.position = position;
            this.displayText = displayText;
            this.typeBranch = typeBranch;
        }

    }
    // End Tree Data Class
};
