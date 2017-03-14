module qmm025.a.viewmodel {
    export class ScreenModel {
        items: KnockoutObservableArray<any>;
        isEnable: KnockoutObservable<boolean>;
        yearKey: KnockoutObservable<number>;
        yearInJapanEmpire: KnockoutObservable<string>;
        residenceTax10: any;

        constructor() {
            var self = this;
            self.items = ko.observableArray([]);
            self.isEnable = ko.observable(false);
            self.yearKey = ko.observable(0);
            self.yearInJapanEmpire = ko.computed(function() {
                return nts.uk.time.yearInJapanEmpire(self.yearKey()).toString();
            });
            // checkbox square
            $.ig.checkboxMarkupClasses = "ui-state-default ui-corner-all ui-igcheckbox-small";

        }

        startPage(): JQueryPromise<any> {
            var self = this;
            var dfd = $.Deferred();
            $.when(self.getYearKey()).done(function() {
                self.findAll().done(function() {
                    dfd.resolve();
                }).fail(function(res) {
                    dfd.reject(res);
                });
            }).fail(function(res) {
                dfd.reject(res);
            });
            return dfd.promise();
        }

        findAll() {
            var self = this;
            var dfd = $.Deferred();
            qmm025.a.service.findAll(self.yearKey())
                .done(function(data) {
                    var perResiTaxData = [];
                    if (data[0] === undefined) {
                        qmm025.a.service.findAll(self.yearKey() - 1)
                            .done(function(data) {
                                if (data[0] === undefined) {
                                    alert("ERROR!");
                                } else {
                                    self.getData(perResiTaxData, data);
                                }
                                dfd.resolve();
                            })
                            .fail(function(res) {
                                dfd.reject(res);
                            });
                    } else {
                        self.getData(perResiTaxData, data);
                        dfd.resolve();
                    }
                })
                .fail(function(res) {
                    dfd.reject(res);
                });
            return dfd.promise();
        }
        //lay du lieu tu DB de hien thi ra man hinh
        getData(perResiTaxData = [], data) {
            var self = this;
            perResiTaxData.push(new ResidenceTax('NSVC', data[0].personId, 'name', 'Vietnam', false,
                data[0].residenceTax[0].value, data[0].residenceTax[1].value, data[0].residenceTax[2].value,
                data[0].residenceTax[3].value, data[0].residenceTax[4].value, data[0].residenceTax[5].value,
                data[0].residenceTax[6].value, data[0].residenceTax[7].value, data[0].residenceTax[8].value,
                data[0].residenceTax[9].value, data[0].residenceTax[10].value, data[0].residenceTax[11].value));
            perResiTaxData.push(new ResidenceTax('NSVC', '00001', 'name1', 'Japan', false,
                25000, 25000, 25000, 25000, 25000, 15000, 25000, 25000, 25000, 25000, 25000, 25000));
            self.items(perResiTaxData);
            self.isEnable(true);
            self.bindGrid(self.items());
        }

        getYearKey() {
            var self = this;
            var dfd = $.Deferred();
            qmm025.a.service.getYearKey()
                .done(function(res) {
                    self.yearKey(res.currentProcessingYm);
                    dfd.resolve();
                })
                .fail(function(res) {
                    dfd.reject(res);
                });
            return dfd.promise();
        }

        saveData() {
            var self = this;
            var obj = {
                //                residenceTaxBn:
                //                residenceTaxAve:
                //                residenceTax: [
                //                    { "month": 1, "value":}, { "month": 2, "value":}, { "month": 3, "value":}, { "month": 4, "value":}, { "month": 5, "value":}, { "month": 6, "value":},
                //                    { "month": 7, "value":}, { "month": 8, "value":}, { "month": 9, "value":}, { "month": 10, "value":}, { "month": 11, "value":}, { "month": 12, "value":},
                //                ]
            };
            qmm025.a.service.update(obj)
                .done(function() {
                    self.findAll();
                })
                .fail(function() {
                });
        }

        remove() {
            var self = this;
            var obj = {
                yearKey: self.yearKey()
            }
            qmm025.a.service.remove(obj)
                .done(function() {
                    self.findAll();
                })
                .fail(function() {
                });
        }

        bindGrid(items) {
            var self = this;
            //tinh lai tong khi row bi thay doi- updating when row edited
            $("#grid").on("iggridupdatingeditrowended", function(event, ui) {
                var grid = ui.owner.grid;
                var residenceTax06 = ui.values["residenceTax06"];
                var residenceTax07 = ui.values["residenceTax07"];
                var totalValue = 0;
                if (ui.values["checkAllMonth"]) {
                    totalValue = residenceTax06 + residenceTax07 * 11 || ui.values["residenceTaxPerYear"];
                    $("#grid").igGridUpdating("setCellValue", ui.rowID, "residenceTax08", residenceTax07);
                    //set background-color khi thay doi trang thai cua totalValue
                    for (var i = 3; i < 13; i++) {
                        $(grid.cellAt(i, 0)).css('background-color', 'red');
                    }
                } else {
                    totalValue = residenceTax06 + residenceTax07 || ui.values["residenceTaxPerYear"];
                    //set background-color khi thay doi trang thai cua totalValue
                    for (var i = 3; i < 13; i++) {
                        $(grid.cellAt(i, 0)).css('background-color', 'grey');
                    }
                }
                $("#grid").igGridUpdating("setCellValue", ui.rowID, "residenceTaxPerYear", totalValue);
            });

            $("#grid").igGrid({
                columns: [
                    { headerText: "部門", key: "department", dataType: "string", width: "100px", formatter: _.escape },
                    { headerText: "コード", key: "code", dataType: "string", width: "100px", formatter: _.escape },
                    { headerText: "名称", key: "name", dataType: "string", width: "120px", formatter: _.escape },
                    { headerText: "住民税納付先", key: "add", dataType: "string", width: "100px", formatter: _.escape },
                    { headerText: "年税額", key: "residenceTaxPerYear", dataType: "number", width: "100px", columnCssClass: "align_right" },
                    { headerText: "全月", key: "checkAllMonth", dataType: "bool", width: "35px" },
                    { headerText: "6月", key: "residenceTax06", dataType: "number", width: "100px", template: "<a style='float: right'>${residenceTax06} 円</a>" },
                    { headerText: "7月", key: "residenceTax07", dataType: "number", width: "100px", template: "<a style='float: right'>${residenceTax07} 円</a>" },
                    { headerText: "8月", key: "residenceTax08", dataType: "number", width: "100px", columnCssClass: "columnCss", template: "<a style='float: right'>${residenceTax08} 円</a>" },
                    { headerText: "9月", key: "residenceTax09", dataType: "number", width: "100px", columnCssClass: "columnCss", template: "<a style='float: right'>${residenceTax09} 円</a>" },
                    { headerText: "10月", key: "residenceTax10", dataType: "number", width: "100px", columnCssClass: "columnCss", template: "<a style='float: right'>${residenceTax10} 円</a>" },
                    { headerText: "11月", key: "residenceTax11", dataType: "number", width: "100px", columnCssClass: "columnCss", template: "<a style='float: right'>${residenceTax11} 円</a>" },
                    { headerText: "12月", key: "residenceTax12", dataType: "number", width: "100px", columnCssClass: "columnCss", template: "<a style='float: right'>${residenceTax12} 円</a>" },
                    { headerText: "1月", key: "residenceTax01", dataType: "number", width: "100px", columnCssClass: "columnCss", template: "<a style='float: right'>${residenceTax01} 円</a>" },
                    { headerText: "2月", key: "residenceTax02", dataType: "number", width: "100px", columnCssClass: "columnCss", template: "<a style='float: right'>${residenceTax02} 円</a>" },
                    { headerText: "3月", key: "residenceTax03", dataType: "number", width: "100px", columnCssClass: "columnCss", template: "<a style='float: right'>${residenceTax03} 円</a>" },
                    { headerText: "4月", key: "residenceTax04", dataType: "number", width: "100px", columnCssClass: "columnCss", template: "<a style='float: right'>${residenceTax04} 円</a>" },
                    { headerText: "5月", key: "residenceTax05", dataType: "number", width: "100px", columnCssClass: "columnCss", template: "<a style='float: right'>${residenceTax05} 円</a>" }
                ],
                renderCheckboxes: true,
                primaryKey: 'code',
                autoGenerateColumns: false,
                dataSource: items,
                width: "1200px",
                height: "550px",
                features: [

                    {
                        name: "ColumnFixing",
                        showFixButtons: false,
                        fixingDirection: "left",
                        columnSettings: [
                            {
                                columnKey: "department",
                                isFixed: true,
                                allowFixing: false,
                                readOnly: true
                            },
                            {
                                columnKey: "code",
                                isFixed: true,
                                allowFixing: false
                            },
                            {
                                columnKey: "name",
                                isFixed: true,
                                allowFixing: false
                            },
                            {
                                columnKey: "add",
                                isFixed: true,
                                allowFixing: false
                            },
                            {
                                columnKey: "residenceTaxPerYear",
                                isFixed: true,
                                allowFixing: false,
                                unbound: true
                            }]
                    },
                    {
                        name: 'Paging',
                        type: "local",
                        pageSize: 20,
                        pageSizeDropDownTrailingLabel: "件",
                        pagerRecordsLabelTemplate: "${recordCount} 件のデータ",
                        nextPageLabelText: "",
                        prevPageLabelText: "",
                    },
                    {
                        name: 'Selection',
                        multipleSelection: true,
                        //allow use arrow keys for the selection of cells/rows
                        activation: true,
                        // click vao row, sau khi chuyen sang trang khac roi quay lai van click tai row do
                        persist: true,
                    },
                    {
                        name: "Updating",
                        editMode: "row",
                        enableAddRow: false,
                        enableDeleteRow: false,
                        editCellStarting: function(evt, ui) {
                            if (ui.columnKey === "checkAllMonth") {
                                ui.value = !ui.value;
                            }
                        },
                        columnSettings: [
                            { columnKey: "department", readOnly: true },
                            { columnKey: "code", readOnly: true },
                            { columnKey: "name", readOnly: true },
                            { columnKey: "add", readOnly: true },
                            { columnKey: "residenceTaxPerYear", readOnly: true },
                            { columnKey: "residenceTax06", required: true },
                            { columnKey: "residenceTax07", required: true },
                            { columnKey: "residenceTax08", readOnly: true },
                            { columnKey: "residenceTax09", readOnly: true },
                            { columnKey: "residenceTax10", readOnly: true },
                            { columnKey: "residenceTax11", readOnly: true },
                            { columnKey: "residenceTax12", readOnly: true },
                            { columnKey: "residenceTax01", readOnly: true },
                            { columnKey: "residenceTax02", readOnly: true },
                            { columnKey: "residenceTax03", readOnly: true },
                            { columnKey: "residenceTax04", readOnly: true },
                            { columnKey: "residenceTax05", readOnly: true }
                        ]
                    },
                    {
                        name: 'RowSelectors',
                        enableCheckBoxes: true,
                        //enableSelectAllForPaging: false -> khong hien dong chu selectAllRow
                        enableSelectAllForPaging: false,
                    }
                ]
            });
        }
    }

    class ResidenceTax {
        residenceTaxPerYear: number;
        constructor(public department: string, public code: string, public name: string, public add: string, public checkAllMonth: boolean,
            public residenceTax01: number, public residenceTax02: number, public residenceTax03: number, public residenceTax04: number,
            public residenceTax05: number, public residenceTax06: number, public residenceTax07: number, public residenceTax08: number,
            public residenceTax09: number, public residenceTax10: number, public residenceTax11: number, public residenceTax12: number) {
            this.residenceTaxPerYear = this.residenceTax06 + this.residenceTax07;
        }
    }
}