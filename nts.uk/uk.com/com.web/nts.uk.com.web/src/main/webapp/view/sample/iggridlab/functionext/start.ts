module nts.uk.ui.gridlist {
    
    __viewContext.ready(function () {
    
        class ScreenModel {
            
            items = (function () {
                var list = [];
                for (var i = 0; i < 400; i++) {
                    list.push(new GridItem(i));
                }
                return list;
            })();
        }
        
        class GridItem {
            id: number;
            flag: boolean;
            ruleCode: number;
            time: string;
            addressCode1: string;
            addressCode2: string;
            address1: string;
            address2: string;
            combo: string;
            header0: string;
            header01: string;
            header1: string;
            header2: string;
            header3: number;
            header4: string;
            header5: string;
            header6: string;
            alert: string;
            constructor(index: number) {
                this.id = index;
                this.flag = index % 2 == 0;
                this.ruleCode = index % 3 + 1;
                this.time = "13:36";
                this.addressCode1 = "001";
                this.addressCode2 = "002";
                this.address1 = "HN";
                this.address2 = "愛知県日本";
                this.combo = String(index % 3 + 1);
                this.header0 = "Out";
                this.header01 = String(index % 3 + 4);
                this.header02 = String(index % 3 + 1);
                this.header1 = "001";
                this.header2 = "内容１２";
                this.header3 = index % 9;
                this.header4 = "内容４";
                this.header5 = "002"; 
                this.header6 = "内容５６";
                this.alert = "Act";
            }
        }
        
        var model = new ScreenModel();
        
        class ItemModel {
            code: string;
            name: string;
    
            constructor(code: string, name: string) {
                this.code = code;
                this.name = name;
            }
        }
        
        var comboItems = [ new ItemModel('1', '基本給'),
                            new ItemModel('2', '役職手当'),
                            new ItemModel('3', '基本給2') ];
        var comboColumns = [{ prop: 'code', length: 4 },
                            { prop: 'name', length: 8 }];
        var comboItems2 = [ new ItemModel('4', '基本給'),
                            new ItemModel('5', '役職手当'),
                            new ItemModel('6', '基本給2') ];
        var comboItems3 = [ new ItemModel('1', 'Text1'),
                            new ItemModel('2', 'Text2'),
                            new ItemModel('3', 'Text3')];
        
        $("#grid2").ntsGrid({ 
                            width: '1500px',
                            height: '800px',
                            dataSource: model.items,
                            primaryKey: 'id',
                            rowVirtualization: true,
                            virtualization: true,
                            virtualizationMode: 'continuous',
                            enter: 'right',
                            preventEditInError: false,
//                            avgRowHeight: 36,
//                            autoAdjustHeight: false,
//                            adjustVirtualHeights: false,
                            columns: [
                                { headerText: 'ID', key: 'id', dataType: 'number', width: '50px', ntsControl: 'Label' },
//                                 headerText: 'Common1',
//                                    group: [
                                        { headerText: 'FLAG', key: 'flag', dataType: 'boolean', width: '190px', ntsControl: 'Checkbox' },
                                            { headerText: 'RULECODE', key: 'ruleCode', dataType: 'number', width: '100px',
                                                            constraint: { 
                                                                primitiveValue: 'ProcessingNo',
                                                                required: true
                                                            }
                                            },
//                                    ],
                                { headerText: 'Inbound time', key: 'time', dataType: 'string', width: '140px', 
                                                constraint: { 
                                                                primitiveValue: 'SampleTimeClock',
                                                                required: true
                                                            }
                                },
                                { headerText: 'Address',
                                    group: [
                                            { headerText: 'Item<br/>Code', key: 'addressCode1', dataType: 'string', width: '150px' },
                                            { headerText: 'Address1', key: 'address1', dataType: 'string', width: '150px'}
                                           ]},
                                { headerText: 'Combobox', key: 'combo', dataType: 'string', width: '230px', ntsControl: 'Combobox' },
                                { headerText: 'Header0', key: 'header0', dataType: 'string', width: '150px', ntsControl: 'Label' },
                                { headerText: 'Header01', key: 'header01', dataType: 'string', width: '500px', ntsControl: 'Combobox2' },
                                { headerText: 'Header02', key: 'header02', dataType: 'string', width: '500px', ntsControl: 'Combobox3' },
                                { headerText: '住所',
                                    group: [
                                            { headerText: 'Item<br/>Code', key: 'addressCode2', dataType: 'string', width: '150px' },
                                            { headerText: 'Address2', key: 'address2', dataType: 'string', width: '150px'}
                                           ]},
                                { headerText: 'Header12',
                                    group: [
                                            { headerText: 'Code<br/>Item', key: 'header1', dataType: 'string', width: '150px', ntsType: 'code', onChange: search },
                                            { headerText: 'Header2', key: 'header2', dataType: 'string', width: '150px', ntsControl: 'Link1' }
                                           ]},
                                { headerText: 'Header3', key: 'header3', dataType: 'number', width: '150px'},
                                { headerText: 'Header4', key: 'header4', dataType: 'string', width: '150px'},
                                { headerText: 'Header56',
                                    group: [
                                            { headerText: 'Code<br/>Item', key: 'header5', dataType: 'string', width: '150px', ntsType: 'code', onChange: search },
                                            { headerText: 'Header6', key: 'header6', dataType: 'string', width: '150px', ntsControl: 'Link2' }
                                           ]},
//                                { headerText: 'Delete', key: 'delete', dataType: 'string', width: '90px', unbound: true, ntsControl: 'DeleteButton' }
                                { headerText: 'Button', key: 'alert', dataType: 'string', width: '90px', ntsControl: 'Button' }
                            ], 
                            features: [
//                                { name: 'Sorting', type: 'local' },
                                        { name: 'Resizing' },
                                        { name: 'MultiColumnHeaders'},
//                                        { name: "Responsive",
//                                            enableVerticalRendering: true
//                                        },
                                        { name: 'ColumnFixing', fixingDirection: 'left',
//                                            syncRowHeights: true,
                                            showFixButtons: false,
                                            columnSettings: [{ columnKey: 'id', isFixed: true },
                                                             { columnKey: 'flag', isFixed: true },
                                                             { columnKey: 'ruleCode', isFixed: true } ]},
                                        { name: 'Summaries', 
                                          showSummariesButton: false,
                                          showDropDownButton: false,
                                          columnSettings: [
                                            { columnKey: 'id', allowSummaries: true, 
                                                summaryOperands: [{ type: "custom", order: 0, summaryCalculator: function() { return "合計"; } }] },
                                            { columnKey: 'flag', allowSummaries: false },
                                            { columnKey: 'addressCode1', allowSummaries: false },
                                            { columnKey: 'addressCode2', allowSummaries: false },
                                            { columnKey: 'address1', allowSummaries: false },
                                            { columnKey: 'address2', allowSummaries: false },
                                            { columnKey: 'time', allowSummaries: true, 
                                                summaryOperands: [{ 
                                                    rowDisplayLabel: "",
                                                    type: 'custom',
                                                    summaryCalculator: $.proxy(totalTime, this),
                                                    order: 0 
                                                }]},
                                            { columnKey: 'ruleCode', allowSummaries: true,
                                                summaryOperands: [{
                                                    rowDisplayLabel: "合計",
                                                    type: "custom",
                                                    summaryCalculator: $.proxy(totalNumber, this),
                                                    order: 0  
                                                }]},
                                            { columnKey: 'combo', allowSummaries: false },
                                            { columnKey: 'header0', allowSummaries: false },
                                            { columnKey: 'header01', allowSummaries: false },
                                            { columnKey: 'header02', allowSummaries: false },
                                            { columnKey: 'header1', allowSummaries: false },
                                            { columnKey: 'header2', allowSummaries: false },
                                            { columnKey: 'header3', allowSummaries: true,
                                                summaryOperands: [{
                                                    rowDisplayLabel: '合計',
                                                    type: "custom",
                                                    summaryCalculator: $.proxy(totalNumber, this),
                                                    order: 0  
                                                }]},
                                            { columnKey: 'header4', allowSummaries: false },
                                            { columnKey: 'header5', allowSummaries: false },
                                            { columnKey: 'header6', allowSummaries: false },
                                            { columnKey: 'alert', allowSummaries: false }
                                          ], 
                                          resultTemplate: '{1}'
                                        }
                                      ],
                            ntsFeatures: [{ name: 'CopyPaste' },
                                            { name: 'CellEdit' },
                                            { name: 'CellColor', columns: [ 
                                                  { 
                                                    key: 'ruleCode', 
                                                    parse: function(value) {
                                                        return value;
                                                    }, 
                                                    map: function(result) {
                                                        if (result <= 1) return "#00b050";
                                                        else if (result === 2) return "pink";
                                                        else return "#0ff";
                                                    } 
                                                  } 
                                                ]
                                            },
                                            { name: "Sheet", 
                                              initialDisplay: "sheet1",
                                              sheets: [ 
                                                        { name: "sheet1", text: "Sheet 1", columns: ["time", "addressCode1", "addressCode2", "address1", "address2", "combo", "header0", "header01", "header02"] }, 
                                                        { name: "sheet2", text: "Sheet 2", columns: ["header1", "header2", "header3", "header4", "header5", "header6", "alert"] }
                                                      ]
                                            }
                                         ],
                            ntsControls: [{ name: 'Checkbox', options: { value: 1, text: 'Custom Check' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox', enable: true },
                                            { name: 'Combobox', options: comboItems, optionsValue: 'code', optionsText: 'name', columns: comboColumns, editable: false, displayMode: 'codeName', controlType: 'ComboBox', enable: true },
                                            { name: 'DeleteButton', text: 'Delete', controlType: 'DeleteButton', enable: true },
                                            { name: 'Button', controlType: 'Button', text: 'Warn me', enable: true, click: function() { alert("Oops!!"); } },
                                            { name: 'Combobox2', options: comboItems2, optionsValue: 'code', optionsText: 'name', columns: comboColumns, editable: false, displayMode: 'name', controlType: 'ComboBox', enable: true },
                                            { name: 'Combobox3', options: comboItems3, optionsValue: 'code', optionsText: 'name', columns: comboColumns, editable: false, displayMode: 'name', controlType: 'ComboBox', enable: true },
                                            { name: 'Link1', click: function() { alert('Do something.'); }, controlType: 'LinkLabel' },
                                            { name: 'Link2', click: function() { alert('Do something.'); }, controlType: 'LinkLabel' } ]
                            });
        $("#run").on("click", function() {
            var source = $("#grid2").igGrid("option", "dataSource");
            alert(source[1].flag);
        });
        $("#update-row").on("click", function() {
            $("#grid2").ntsGrid("updateRow", 0, { flag: false, ruleCode: '2', combo: '3' });
        });
        $("#enable-ctrl").on("click", function() {
            $("#grid2").ntsGrid("enableNtsControlAt", 1, "combo", "ComboBox");
        });
        $("#disable-ctrl").on("click", function() {
            $("#grid2").ntsGrid("disableNtsControlAt", 1, "combo", "ComboBox");
        });
        
        function totalNumber(data) {
            let total = 0;
            _.forEach(data, function(d) {
                total += parseInt(d);
            });
            return total;
        }
        function totalTime(data) {
            let total = moment.duration("0");
            _.forEach(data, function(d) {
                total.add(moment.duration(d));
            });
            let time = total.asHours();
            let hour = Math.floor(time);
            let minute = (time - hour) * 60;
            let roundMin = Math.round(minute);
            let minuteStr = roundMin < 10 ? ("0" + roundMin) : String(roundMin);
            return hour + ":" + minuteStr;
        }
        function search(val) {
            let dfd = $.Deferred();
            let i = 0;
            let result = "Not found";
            while(i < 500) {
                i++
            }
            if (val === "001") {
                result = "結果01";
            } else if (val === "002") {
                result = "結果02"
            }
            dfd.resolve(result);
            return dfd.promise();
        }
        this.bind(model);
    });
}