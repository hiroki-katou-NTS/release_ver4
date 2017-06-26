module qpp011.d {

    export class ScreenModel {
        //gridlist
        D_LST_001_Data: any = ko.observable([]);
        columns_D_LST_001: any;
        D_LST_001_selectedValue: any = ko.observableArray([]);;
        D_LST_001_selectedResiTaxCode: KnockoutObservable<any> = ko.observable();
        D_LST_001_selectedRegisteredName: KnockoutObservable<any> = ko.observable();
        currentObject: any = ko.observable();
        currentTax: KnockoutObservable<service.model.residentialTax>;
        //Tree Data Variable
        TreeArray: any;
        ListLocation: any;
        ResidentialData: any = ko.observable([]);
        currentIndex: any = 0;
        //End Data Variable
        //value 
        taxPayRollMoney: any = ko.observable();
        taxBonusMoney: any = ko.observable();
        taxOverDueMoney: any = ko.observable();
        taxDemandChargeMoyney: any = ko.observable();
        address: any = ko.observable();
        headcount: any = ko.observable();
        retirementBonusAmout: any = ko.observable();
        cityTaxMoney: any = ko.observable();
        prefectureTaxMoney: any = ko.observable();
        //Switch
        roundingRules: KnockoutObservableArray<any>;
        selectedRuleCode: any;
        enable: KnockoutObservable<boolean>;
        //search box 
        yearInJapanEmpire_LBL_012: any;
        D_LBL_018_Year_Month: any;
        D_LBL_019_yearInJapanEmpire: any;
        //datepicker
        date_D_INP_007: KnockoutObservable<Date> = ko.observable(new Date());;
        //dirty check
        dirty: nts.uk.ui.DirtyChecker;
        notLoopAlert: KnockoutObservable<boolean>;
        initScreen: boolean;
        constructor() {
            var self = this;
            self.currentTax = ko.observable(new service.model.residentialTax(null, null, null, null, null, null, null, null, null, null, null, null));
            self.initScreen = true;
            self.dirty = new nts.uk.ui.DirtyChecker(self.currentObject);
            self.notLoopAlert = ko.observable(true);
            self.yearInJapanEmpire_LBL_012 = ko.computed(function() {
                if (self.date_D_INP_007()) {
                    return "(" + nts.uk.time.yearmonthInJapanEmpire(moment(self.date_D_INP_007()).format("YYYY/MM")).toString() + ")";
                }
                
                return "";
            });
            //gridlist data                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
            self.columns_D_LST_001 = [
                { headerText: "position", key: "position", width: "30px", dataType: "string", hidden: true },
                { headerText: "code", key: "code", width: "30px", dataType: "string", hidden: true },
                { headerText: "コード/名称", key: "displayText", width: "230px", dataType: "string" },

            ];
            function registeredNameAndCode(val, row) {
                return row.resiTaxCode + " " + row.registeredName;
            }

            //Set Tree Data 
            service.findAllResidential().done(function(data: Array<any>) {
                service.getlistLocation().done(function(listLocation: Array<any>) {
                    
                    self.ResidentialData(data);
                    self.ListLocation = listLocation;
                    let ArrayData = CreateDataArray(listLocation, data);
                    self.D_LST_001_Data(ArrayData);
                    BindTreeGrid("#D_LST_001", self.D_LST_001_Data(), self.D_LST_001_selectedValue);
                    if (data.length > 0) {
                        self.dirty.reset();
                        self.D_LST_001_selectedValue([data[0].resiTaxCode]);
                    }
                    if(data.length ===0){
                        nts.uk.ui.dialog.alert("対象データがありません。");
                    }
                }).fail(function(res) {
                    // Alert message
                    nts.uk.ui.dialog.alert("対象データがありません。");
                });

            }).fail(function(res) {
                // Alert message
                alert(res.message);
            });
            //Create Tree Data
            function CreateDataArray(TreeData, Data) {
                self.TreeArray = [];
                let parentPositionIndex = 0;
                for (let Object of Data) {
                    let positionIndex = CreateTreeBranchs(Object.prefectureCode);
                    if (positionIndex) {
                        self.TreeArray.push(new TreeItem(Object.resiTaxCode, Object.resiTaxName, positionIndex, Object.resiTaxCode, Object.resiTaxCode + " " + Object.resiTaxAutonomy, 'Item'));
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
            let D_LST_001 = $("#D_LST_001");
            self.D_LST_001_selectedValue.subscribe(function(newValue) {
                // clear error for all input
                self.clearError();
                // clean all input
                self.cleanInput();
                
                let selectedRows = _.map(D_LST_001.igTreeGridSelection("selectedRows"), function(row) {
                    if (row != undefined)
                        return row.id;
                });
                if (!_.isEqual(selectedRows, newValue)) {
                    D_LST_001.igTreeGridSelection("clearSelection");
                    newValue.forEach(function(id) {
                        D_LST_001.igTreeGridSelection("selectRowById", id);
                    });
                }
                var item: TreeItem = _.find(self.D_LST_001_Data(), function(obj: TreeItem) {
                    return (obj.position == self.D_LST_001_selectedValue() && obj.typeBranch == "Item");
                });
                if (item != undefined) {
                    self.D_LST_001_selectedResiTaxCode(item.position);
                    self.D_LST_001_selectedRegisteredName(item.displayText);
                    GetDataFormSelectedRows(item.position);
                } else {
                    self.D_LST_001_selectedResiTaxCode("");
                    self.D_LST_001_selectedRegisteredName("");
                    GetDataFormSelectedRows(null);
                }
            });
            self.currentObject.subscribe(function(newValue) {
                self.initScreen = false;
                self.taxPayRollMoney(newValue ? newValue.taxPayRollMoney : null);
                self.taxBonusMoney(newValue ? newValue.taxBonusMoney : null)
                self.taxOverDueMoney(newValue ? newValue.taxOverDueMoney : null);
                self.taxDemandChargeMoyney(newValue ? newValue.taxDemandChargeMoyney : null);
                self.address(newValue ? newValue.address : null);
                self.headcount(newValue ? newValue.headcount : null);
                self.retirementBonusAmout(newValue ? newValue.retirementBonusAmout : null);
                self.cityTaxMoney(newValue ? newValue.cityTaxMoney : null);
                self.prefectureTaxMoney(newValue ? newValue.prefectureTaxMoney : null);
                self.date_D_INP_007(newValue ? new Date(newValue.dueDate) : new Date());
            });
            //Switch

            self.D_LBL_018_Year_Month = nts.uk.sessionStorage.getItemAndRemove("QPP011_D_TargetDate").value;
            self.D_LBL_019_yearInJapanEmpire = ko.observable();
            self.D_LBL_019_yearInJapanEmpire("(" + nts.uk.time.yearmonthInJapanEmpire(self.D_LBL_018_Year_Month).toString() + ")");

            self.enable = ko.observable(true);
            self.roundingRules = ko.observableArray([
                { code: '1', name: 'Item1' },
                { code: '2', name: 'Item2' }
            ]);
            self.selectedRuleCode = ko.observable(1);



            function BindTreeGrid(gridID, Data, selectedValue) {
                $(gridID).igTreeGrid({
                    width: "280px",
                    height: "350px",
                    dataSource: Data,
                    autoGenerateColumns: false,
                    primaryKey: "position",
                    foreignKey: "child",
                    columns: self.columns_D_LST_001,
                    initialExpandDepth: 2,
                    features: [
                        {
                            name: "Selection",
                            mode: "row",
                            rowSelectionChanged: function(evt: any, ui: any) {
                                var selectedRows: any = ui.row;
                                selectedValue([selectedRows.id]);
                            }
                        }]
                });
                $(gridID).setupSearchScroll("igTreeGrid");
            }

            function GetDataFormSelectedRows(selectedResiTaxCode) {
                let TargetDate = self.D_LBL_018_Year_Month = self.D_LBL_018_Year_Month.replace("/", "");
                service.findresidentialTax(selectedResiTaxCode, TargetDate).done(function(data: any) {
                    self.dirty.reset();
                    self.currentObject(data)
                }).fail(function(res) {
                    // Alert message
                    alert("対象データがありません。");
                })

            }

        }
        UpdateResidentialTax(selectedResiTaxCode) {
            let self = this;
            let TargetDate = self.D_LBL_018_Year_Month = self.D_LBL_018_Year_Month.replace("/", "");
            let residentialTax = {
                resimentTaxCode: selectedResiTaxCode,
                taxPayRollMoney: self.taxPayRollMoney(),
                yearMonth: TargetDate,
                taxBonusMoney: self.taxBonusMoney(),
                taxOverDueMoney: self.taxOverDueMoney(),
                taxDemandChargeMoyney: self.taxDemandChargeMoyney(),
                address: self.address(),
                dueDate: new Date(self.date_D_INP_007().toDateString()),
                headcount: self.headcount(),
                retirementBonusAmout: self.retirementBonusAmout(),
                cityTaxMoney: self.cityTaxMoney(),
                prefectureTaxMoney: self.prefectureTaxMoney()
            };
            service.update(residentialTax).done(function(data: any) {
                self.currentObject(residentialTax);
            }).fail(function(res) {
                alert(res.message);
            })
        }
        CreateResidentialTax(selectedResiTaxCode) {
            let self = this;
            let TargetDate = self.D_LBL_018_Year_Month = self.D_LBL_018_Year_Month.replace("/", "");
            let residentialTax = {
                resimentTaxCode: selectedResiTaxCode,
                taxPayRollMoney: self.taxPayRollMoney(),
                yearMonth: TargetDate,
                taxBonusMoney: self.taxBonusMoney(),
                taxOverDueMoney:self.taxOverDueMoney(),
                 taxDemandChargeMoyney: self.taxDemandChargeMoyney(),
                address: self.address(),
                dueDate: new Date(self.date_D_INP_007().toDateString()),
                headcount: self.headcount(),
                retirementBonusAmout: self.retirementBonusAmout(),
                cityTaxMoney: self.cityTaxMoney(),
                prefectureTaxMoney: self.prefectureTaxMoney()
                };
            if (residentialTax.cityTaxMoney && residentialTax.dueDate && residentialTax.prefectureTaxMoney && residentialTax.resimentTaxCode && residentialTax.retirementBonusAmout && residentialTax.taxBonusMoney && residentialTax.taxDemandChargeMoyney && residentialTax.taxOverDueMoney && residentialTax.taxPayRollMoney && residentialTax.yearMonth) {
                service.add(residentialTax).done(function(data: any) {
                    self.currentObject(residentialTax);
                }).fail(function(res) {
                    alert(res.message);
                })
            }
        }
        
        /**
         * Clean all input
         */
        cleanInput(): void {
            var self = this;
            self.taxPayRollMoney(null);
            self.taxBonusMoney(null)
            self.taxOverDueMoney(null);
            self.taxDemandChargeMoyney(null);
            self.address(null);
            self.headcount(null);
            self.retirementBonusAmout(null);
            self.cityTaxMoney(null);
            self.prefectureTaxMoney(null);
            self.date_D_INP_007(new Date());
        }
        
        clearError(): void {
            $('#salary_portion').ntsError('clear');
            $('#retirement_income').ntsError('clear');
            $('#tax_over_due_money').ntsError('clear');
            $('#tax_demand_charge_moyney').ntsError('clear');
            $('#D_INP_006').ntsError('clear');
            $('#head_count').ntsError('clear');
            $('#retirement_bonus_amout').ntsError('clear');
            $('#city_tax_money').ntsError('clear');
            $('#prefecture_tax_money').ntsError('clear');
        };
        
        /**
         * Event submit form D_BTN_001
         */
        submitDialog(): void {
            let self = this;
            
            let TargetDate = self.D_LBL_018_Year_Month = self.D_LBL_018_Year_Month.replace("/", "");
            if (self.D_LST_001_selectedResiTaxCode()) {
                service.findresidentialTax(self.D_LST_001_selectedResiTaxCode(), TargetDate).done(function(data: any) {
                    if (data) {
                        self.UpdateResidentialTax(self.D_LST_001_selectedResiTaxCode());
                    } else {
                        self.CreateResidentialTax(self.D_LST_001_selectedResiTaxCode());
                    }
                }).fail(function(res) {
                    nts.uk.ui.dialog.alert("対象データがありません。");
                })
            }
        }
        
        /**
         * Event close dialog D_BTN_002
         */
        closeDialog(): void {
            nts.uk.ui.windows.close();
        }
    }
    export class D_LST_001_ItemModel {
        resiTaxCode: string;
        registeredName: string;

        constructor(resiTaxCode: string, registeredName: string) {
            this.resiTaxCode = resiTaxCode;
            this.registeredName = registeredName;
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
