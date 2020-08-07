module nts.uk.at.view.kaf002_ref.a.viewmodel {
    
    import AppType = nts.uk.at.view.kaf000_ref.shr.viewmodel.model.AppType;
    import Kaf000AViewModel = nts.uk.at.view.kaf000_ref.a.viewmodel.Kaf000AViewModel;
    @bean()
    class Kaf002AViewModel extends Kaf000AViewModel {
        
        tabs: KnockoutObservableArray<nts.uk.ui.NtsTabPanelModel>;
        selectedTab: KnockoutObservable<string>;
        texteditor: any;
        enable1: KnockoutObservable<boolean>;
        enable2: KnockoutObservable<boolean>;
        enable3: KnockoutObservable<boolean>;
        enable4: KnockoutObservable<boolean>;
        enable5: KnockoutObservable<boolean>;
        readonly: KnockoutObservable<boolean>;
        time: KnockoutObservable<number>;
        
        
    
        isFillColor1: KnockoutObservable<boolean> = ko.observable(true);

        
        
        items1 = (function() {
            let list = []; 
            for (let i = 1; i < 3; i++) {
                let dataObject = new TimePlaceOutput(i);
                list.push(new GridItem(dataObject, STAMPTYPE.ATTENDENCE)); 
            }
            
            return list;
        })();
        
        items2 = (function() {
            let list = [];
            for (let i = 3; i < 6; i++) {
                let dataObject = new TimePlaceOutput(i);
                list.push(new GridItem(dataObject, STAMPTYPE.EXTRAORDINARY));
                
            }
            
            return list;
        })();
        
        items3 = (function() {
            let list = [];
            for (let i = 1; i < 11; i++) {
                let dataObject = new TimePlaceOutput(i);
                list.push(new GridItem(dataObject, STAMPTYPE.GOOUT_RETURNING));
            }
            
            return list;
        })();
        
        items4 = (function() {
            let list = [];
            for (let i = 1; i < 11; i++) {
                let dataObject = new TimePlaceOutput(i);
                list.push(new GridItem(dataObject, STAMPTYPE.BREAK));
            }
            
            return list;
        })();
        items5 = (function() {
            let list = [];
            for (let i = 1; i < 3; i++) {
                let dataObject = new TimePlaceOutput(i);
                list.push(new GridItem(dataObject, STAMPTYPE.PARENT));
            }
            
            return list;
        })();
        
        items6 = (function() {
            let list = [];
            for (let i = 1; i < 3; i++) {
                let dataObject = new TimePlaceOutput(i);
                list.push(new GridItem(dataObject, STAMPTYPE.NURSE));
            }
            
            return list;
        })();
        
       
        
        created() {
            const self = this;
            self.enable1 = ko.observable(false);
            self.enable2 = ko.observable(false);
            self.enable3 = ko.observable(false);
            self.enable4 = ko.observable(false);
            self.enable5 = ko.observable(false);
            self.readonly = ko.observable(false);
            self.time = ko.observable(1400);
            
//            self.$blockui("show");
//            self.loadData([], [], AppType.STAMP_APPLICATION)
//            .then((loadDataFlag: any) => {
//                if(loadDataFlag) {
//                    let ApplicantEmployeeID: null,
//                        ApplicantList: null,
//                        appDispInfoStartupOutput = ko.toJS(self.appDispInfoStartupOutput),
//                        command = { ApplicantEmployeeID, ApplicantList, appDispInfoStartupOutput };
//                }
//            })
            self.texteditor = {
                    value: ko.observable(''),
                    constraint: 'ResidenceCode',
                    option: ko.mapping.fromJS(new nts.uk.ui.option.TextEditorOption({
                        textmode: "text",
                        placeholder: "Placeholder for text editor",
                        width: "100px",
                        textalign: "left"
                    })),
                    required: ko.observable(true),
                    enable: ko.observable(true),
                    readonly: ko.observable(false)
                };
            self.tabs = ko.observableArray([
                {id: 'tab-1', title: self.$i18n('KAF002_29'), content: '.tab-content-1', enable: ko.observable(true), visible: ko.observable(true)},
                {id: 'tab-2', title: self.$i18n('KAF002_31'), content: '.tab-content-2', enable: ko.observable(true), visible: ko.observable(true)},
                {id: 'tab-3', title: self.$i18n('KAF002_76'), content: '.tab-content-3', enable: ko.observable(true), visible: ko.observable(true)},
                {id: 'tab-4', title: self.$i18n('KAF002_32'), content: '.tab-content-4', enable: ko.observable(true), visible: ko.observable(true)},
                {id: 'tab-5', title: self.$i18n('KAF002_33'), content: '.tab-content-5', enable: ko.observable(true), visible: ko.observable(true)},
                {id: 'tab-6', title: self.$i18n('KAF002_34'), content: '.tab-content-6', enable: ko.observable(true), visible: ko.observable(true)}
            ]);
            self.selectedTab = ko.observable('tab-1');
            
            let comboColumns = [{ prop: 'code', length: 4 },
                                { prop: 'name', length: 8 }];
            let comboItems = [ new ItemModel('1', '基本給'),
                               new ItemModel('2', '役職手当'),
                               new ItemModel('3', '基本給2') ];
            

            
            let items1_2 = this.items1.concat(this.items2);
            
            self.enable1.subscribe((value) => {
                _.forEach(items1_2, item => {
                   if (ko.toJS(item.flagEnable)) {
                       item.flagObservable(value);                       
                   }
                });
            });
            self.enable2.subscribe((value) => {
                _.forEach(self.items3, item => {
                   if (ko.toJS(item.flagEnable)) {
                       item.flagObservable(value);                       
                   }
                });
            });
            self.enable3.subscribe((value) => {
                _.forEach(self.items4, item => {
                   if (ko.toJS(item.flagEnable)) {
                       item.flagObservable(value);                       
                   }
                });
            });
            self.enable4.subscribe((value) => {
                _.forEach(self.items5, item => {
                   if (ko.toJS(item.flagEnable)) {
                       item.flagObservable(value);                       
                   }
                });
            });
            self.enable5.subscribe((value) => {
                _.forEach(self.items6, item => {
                   if (ko.toJS(item.flagEnable)) {
                       item.flagObservable(value);                       
                   }
                });
            });
            
            let statesTable1 = [];
            let statesTable2 = [];
            let statesTable3 = [];
            let statesTable4 = [];
            let statesTable5 = [];
            let isShowHeaderCheckbox = true;
            let numberDisble1 = 0;
            let numberDisble2 = 0;
            let numberDisble3 = 0;
            let numberDisble4 = 0;
            let numberDisble5 = 0;
            for (let i = 1; i < items1_2.length + 1; i++) {
                statesTable1.push(new CellState(i, "text1", ['titleColor']));
                
                if (!ko.toJS(items1_2[i-1].flagEnable)) {
                    numberDisble1++;
                }
                             
            }
            
            for (let i = 1; i < self.items3.length + 1; i++) {
                statesTable2.push(new CellState(i, "text1", ['titleColor']));
                
                if (!ko.toJS(self.items3[i-1].flagEnable)) {
                    numberDisble2++;
                }
                             
            }
            
            for (let i = 1; i < self.items4.length + 1; i++) {
                statesTable3.push(new CellState(i, "text1", ['titleColor']));
                
                if (!ko.toJS(self.items4[i-1].flagEnable)) {
                    numberDisble3++;
                }
                             
            }
            
            for (let i = 1; i < self.items5.length + 1; i++) {
                statesTable4.push(new CellState(i, "text1", ['titleColor']));
                
                if (!ko.toJS(self.items5[i-1].flagEnable)) {
                    numberDisble4++;
                }
                             
            }
            
            for (let i = 1; i < self.items6.length + 1; i++) {
                statesTable5.push(new CellState(i, "text1", ['titleColor']));
                
                if (!ko.toJS(self.items6[i-1].flagEnable)) {
                    numberDisble5++;
                }
                             
            }
            let headerFlagContent1 = numberDisble1 != items1_2.length ? '<div style="display: block" align="center" data-bind="ntsCheckBox: { checked: enable1}">' + self.$i18n('KAF002_72')+ '</div>' : '<div style="display: block" align="center">' + self.$i18n('KAF002_72')+ '</div>';
            let headerFlagContent2 = numberDisble2 != self.items3.length ? '<div style="display: block" align="center" data-bind="ntsCheckBox: { checked: enable2}">' + self.$i18n('KAF002_72')+ '</div>' : '<div style="display: block" align="center">' + self.$i18n('KAF002_72')+ '</div>';
            let headerFlagContent3 = numberDisble3 != self.items4.length ? '<div style="display: block" align="center" data-bind="ntsCheckBox: { checked: enable3}">' + self.$i18n('KAF002_72')+ '</div>' : '<div style="display: block" align="center">' + self.$i18n('KAF002_72')+ '</div>';
            let headerFlagContent4 = numberDisble4 != self.items5.length ? '<div style="display: block" align="center" data-bind="ntsCheckBox: { checked: enable4}">' + self.$i18n('KAF002_72')+ '</div>' : '<div style="display: block" align="center">' + self.$i18n('KAF002_72')+ '</div>';
            let headerFlagContent5 = numberDisble5 != self.items6.length ? '<div style="display: block" align="center" data-bind="ntsCheckBox: { checked: enable5}">' + self.$i18n('KAF002_72')+ '</div>' : '<div style="display: block" align="center">' + self.$i18n('KAF002_72')+ '</div>';

            
//            出勤／退勤
            $("#grid1").ntsGrid({ 
                width: '970px',
                height: '400px',
                dataSource: items1_2,
                primaryKey: 'id',
                virtualization: true,
                virtualizationMode: 'continuous',
                hidePrimaryKey: true,
                columns: [
                    { headerText: 'ID', key: 'id', dataType: 'number', width: '50px', ntsControl: 'Label' },
                    { headerText: '', key: 'text1', dataType: 'string', width: '120px' }, 
                    { headerText: self.$i18n('KAF002_22'), key: 'startTime', dataType: 'string', width: '290px' },
                    { headerText: self.$i18n('KAF002_23'), key: 'endTime', dataType: 'string', width: '230px', 
//                        ntsControl: 'Combobox', tabIndex: 0 
                        },
                    { headerText: headerFlagContent1, key: 'flag', dataType: 'string', width: '200px' }
//                        , showHeaderCheckbox: isShowHeaderCheckbox, ntsControl: 'Checkbox'
                    
                ], 
                features: [{ name: 'Resizing',
                                columnSettings: [{
                                    columnKey: 'id', allowResizing: true, minimumWidth: 30
                                }, {
                                    columnKey: 'flag', allowResizing: true, minimumWidth: 30
                                }] 
                            },
                            { 
                                name: 'Selection',
                                mode: 'row',
                                multipleSelection: true
                            }
                ],
                ntsFeatures: [
//                    { name: 'CopyPaste' },
                    { name: 'CellState',
                        rowId: 'rowId',
                        columnKey: 'columnKey',
                        state: 'state',
                        states: statesTable1
                    }
                    ],
                ntsControls: [{ name: 'Checkbox', options: { value: 1, text: '' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox', enable: true },
//                                { name: 'SwitchButtons', options: [{ value: '1', text: 'Option 1' }, { value: '2', text: 'Option 2' }, { value: '3', text: 'Option 3' }], 
//                                    optionsValue: 'value', optionsText: 'text', controlType: 'SwitchButtons', enable: true,
//                                    distinction: { "503": ['1', '2'], "506": ["2", "3"], "600": ["1", "2"] }},
//                                { name: 'Combobox', options: comboItems, optionsValue: 'code', optionsText: 'name', columns: comboColumns, controlType: 'ComboBox', enable: true },
                                { name: 'Button', text: 'Open', click: function() { alert("Button!!"); }, controlType: 'Button' },
                                { name: 'DeleteButton', text: 'Delete', controlType: 'DeleteButton', enable: true }]
                });
            
            
            
//            外出
            
            
            $("#grid2").ntsGrid({ 
                width: '970px',
                height: '400px',
                dataSource: this.items3,
                primaryKey: 'id',
                virtualization: true,
                virtualizationMode: 'continuous',
                hidePrimaryKey: true,
//                enter: 'right',
                columns: [
                    { headerText: 'ID', key: 'id', dataType: 'number', width: '50px', ntsControl: 'Label' },
                    { headerText: '', key: 'text1', dataType: 'string', width: '120px' }, 
                    { headerText: self.$i18n('KAF002_22'), key: 'combobox', dataType: 'string', width: '120px', ntsControl: 'Combobox' }, 
                    { headerText: self.$i18n('KAF002_22'), key: 'startTime', dataType: 'string', width: '290px' },
                    { headerText: self.$i18n('KAF002_23'), key: 'endTime', dataType: 'string', width: '230px', 
//                        ntsControl: 'Combobox', tabIndex: 0 
                        },
                    { headerText: headerFlagContent2, key: 'flag', dataType: 'string', width: '200px'}
                    
                ], 
                features: [{ name: 'Resizing',
                                columnSettings: [{
                                    columnKey: 'id', allowResizing: true, minimumWidth: 30
                                }, {
                                    columnKey: 'flag', allowResizing: false 
                                }] 
                            },
                            { 
                                name: 'Selection',
                                mode: 'row',
                                multipleSelection: true
                            }
                ],
                ntsFeatures: [
                    { name: 'CellState',
                        rowId: 'rowId',
                        columnKey: 'columnKey',
                        state: 'state',
                        states: statesTable2
                    }
                    ],
                ntsControls: [{ name: 'Checkbox', options: { value: 1, text: '' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox', enable: true },
//                                { name: 'SwitchButtons', options: [{ value: '1', text: 'Option 1' }, { value: '2', text: 'Option 2' }, { value: '3', text: 'Option 3' }], 
//                                    optionsValue: 'value', optionsText: 'text', controlType: 'SwitchButtons', enable: true,
//                                    distinction: { "503": ['1', '2'], "506": ["2", "3"], "600": ["1", "2"] }},
                                { name: 'Combobox', options: comboItems, optionsValue: 'code', optionsText: 'name', columns: comboColumns, controlType: 'ComboBox', enable: true },
                                { name: 'Button', text: 'Open', click: function() { alert("Button!!"); }, controlType: 'Button' },
                                { name: 'DeleteButton', text: 'Delete', controlType: 'DeleteButton', enable: true }]
                });
            
            
//            休憩
            $("#grid3").ntsGrid({ 
                width: '970px',
                height: '400px',
                dataSource: this.items4,
                primaryKey: 'id',
                virtualization: true,
                virtualizationMode: 'continuous',
                hidePrimaryKey: true,
//                enter: 'right',
                columns: [
                    { headerText: 'ID', key: 'id', dataType: 'number', width: '50px', ntsControl: 'Label' },
                    { headerText: '', key: 'text1', dataType: 'string', width: '120px' }, 
                    { headerText: self.$i18n('KAF002_22'), key: 'startTime', dataType: 'string', width: '290px' },
                    { headerText: self.$i18n('KAF002_23'), key: 'endTime', dataType: 'string', width: '230px', 
//                        ntsControl: 'Combobox', tabIndex: 0 
                        },
                    { headerText: headerFlagContent3, key: 'flag', dataType: 'string', width: '200px'}
                    
                ], 
                features: [{ name: 'Resizing',
                                columnSettings: [{
                                    columnKey: 'id', allowResizing: true, minimumWidth: 30
                                }, {
                                    columnKey: 'flag', allowResizing: false 
                                }] 
                            },
                            { 
                                name: 'Selection',
                                mode: 'row',
                                multipleSelection: true
                            }
                ],
                ntsFeatures: [
//                    { name: 'CopyPaste' },
                    { name: 'CellState',
                        rowId: 'rowId',
                        columnKey: 'columnKey',
                        state: 'state',
                        states: statesTable3
                    }
                    ],
                ntsControls: [{ name: 'Checkbox', options: { value: 1, text: '' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox', enable: true },
//                                { name: 'SwitchButtons', options: [{ value: '1', text: 'Option 1' }, { value: '2', text: 'Option 2' }, { value: '3', text: 'Option 3' }], 
//                                    optionsValue: 'value', optionsText: 'text', controlType: 'SwitchButtons', enable: true,
//                                    distinction: { "503": ['1', '2'], "506": ["2", "3"], "600": ["1", "2"] }},
//                                { name: 'Combobox', options: comboItems, optionsValue: 'code', optionsText: 'name', columns: comboColumns, controlType: 'ComboBox', enable: true },
                                { name: 'Button', text: 'Open', click: function() { alert("Button!!"); }, controlType: 'Button' },
                                { name: 'DeleteButton', text: 'Delete', controlType: 'DeleteButton', enable: true }]
                });
            
            
            
            
            
//            育児
            $("#grid4").ntsGrid({ 
                width: '970px',
                height: '400px',
                dataSource: this.items5,
                primaryKey: 'id',
                virtualization: true,
                virtualizationMode: 'continuous',
                hidePrimaryKey: true,
//                enter: 'right',
                columns: [
                    { headerText: 'ID', key: 'id', dataType: 'number', width: '50px', ntsControl: 'Label' },
                    { headerText: '', key: 'text1', dataType: 'string', width: '120px' }, 
                    { headerText: self.$i18n('KAF002_22'), key: 'startTime', dataType: 'string', width: '290px' },
                    { headerText: self.$i18n('KAF002_23'), key: 'endTime', dataType: 'string', width: '230px', 
//                        ntsControl: 'Combobox', tabIndex: 0 
                        },
                    { headerText: headerFlagContent4, key: 'flag', dataType: 'string', width: '200px'}
                    
                ], 
                features: [{ name: 'Resizing',
                                columnSettings: [{
                                    columnKey: 'id', allowResizing: true, minimumWidth: 30
                                }, {
                                    columnKey: 'flag', allowResizing: false 
                                }] 
                            },
                            { 
                                name: 'Selection',
                                mode: 'row',
                                multipleSelection: true
                            }
                ],
                ntsFeatures: [
//                    { name: 'CopyPaste' },
                    { name: 'CellState',
                        rowId: 'rowId',
                        columnKey: 'columnKey',
                        state: 'state',
                        states: statesTable4
                    }
                    ],
                ntsControls: [{ name: 'Checkbox', options: { value: 1, text: '' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox', enable: true },
//                                { name: 'SwitchButtons', options: [{ value: '1', text: 'Option 1' }, { value: '2', text: 'Option 2' }, { value: '3', text: 'Option 3' }], 
//                                    optionsValue: 'value', optionsText: 'text', controlType: 'SwitchButtons', enable: true,
//                                    distinction: { "503": ['1', '2'], "506": ["2", "3"], "600": ["1", "2"] }},
//                                { name: 'Combobox', options: comboItems, optionsValue: 'code', optionsText: 'name', columns: comboColumns, controlType: 'ComboBox', enable: true },
                                { name: 'Button', text: 'Open', click: function() { alert("Button!!"); }, controlType: 'Button' },
                                { name: 'DeleteButton', text: 'Delete', controlType: 'DeleteButton', enable: true }]
                });
            
//            介護
            $("#grid5").ntsGrid({ 
                width: '970px',
                height: '400px',
                dataSource: this.items6,
                primaryKey: 'id',
                virtualization: true,
                virtualizationMode: 'continuous',
                hidePrimaryKey: true,
//                enter: 'right',
                columns: [
                    { headerText: 'ID', key: 'id', dataType: 'number', width: '50px', ntsControl: 'Label' },
                    { headerText: '', key: 'text1', dataType: 'string', width: '120px' }, 
                    { headerText: self.$i18n('KAF002_22'), key: 'startTime', dataType: 'string', width: '290px' },
                    { headerText: self.$i18n('KAF002_23'), key: 'endTime', dataType: 'string', width: '230px', 
//                        ntsControl: 'Combobox', tabIndex: 0 
                        },
                    { headerText: headerFlagContent5, key: 'flag', dataType: 'string', width: '200px'}
                    
                ], 
                features: [{ name: 'Resizing',
                                columnSettings: [{
                                    columnKey: 'id', allowResizing: true, minimumWidth: 30
                                }, {
                                    columnKey: 'flag', allowResizing: false 
                                }] 
                            },
                            { 
                                name: 'Selection',
                                mode: 'row',
                                multipleSelection: true
                            }
                ],
                ntsFeatures: [
//                    { name: 'CopyPaste' },
                    { name: 'CellState',
                        rowId: 'rowId',
                        columnKey: 'columnKey',
                        state: 'state',
                        states: statesTable5
                    }
                    ],
                ntsControls: [{ name: 'Checkbox', options: { value: 1, text: '' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox', enable: true },
//                                { name: 'SwitchButtons', options: [{ value: '1', text: 'Option 1' }, { value: '2', text: 'Option 2' }, { value: '3', text: 'Option 3' }], 
//                                    optionsValue: 'value', optionsText: 'text', controlType: 'SwitchButtons', enable: true,
//                                    distinction: { "503": ['1', '2'], "506": ["2", "3"], "600": ["1", "2"] }},
//                                { name: 'Combobox', options: comboItems, optionsValue: 'code', optionsText: 'name', columns: comboColumns, controlType: 'ComboBox', enable: true },
                                { name: 'Button', text: 'Open', click: function() { alert("Button!!"); }, controlType: 'Button' },
                                { name: 'DeleteButton', text: 'Delete', controlType: 'DeleteButton', enable: true }]
                });
            
            
            
            
//            応援 pending
            
            
        }
        
        mounted() {
            
        }
        
    }
    const API = {
            startStampApp: 'at/request/application/stamp/startStampApp',
            checkBeforeRegister: 'at/request/application/stamp/checkBeforeRegister',
            register: 'at/request/application/stamp/register',
            changeDate: 'at/request/application/stamp/changeDate'
    }
    class GridItem {
        id: number;
        flag: string;
        startTimeRequest: KnockoutObservable<number> = ko.observable(null);
        endTimeRequest: KnockoutObservable<number> = ko.observable(null);
        startTimeActual: number;
        endTimeActual: number
        typeReason: KnockoutObservable<number>;
        startTime: string;
        endTime: string;
        text1: string;
        flagObservable: KnockoutObservable<boolean> = ko.observable(false);
        flagEnable: KnockoutObservable<boolean> = ko.observable(true);
        constructor(dataObject: TimePlaceOutput, typeStamp : STAMPTYPE) {
            const self = this;
            self.id = dataObject.frameNo;
//            self.flag = false;
            self.startTimeActual = dataObject.opStartTime;
            self.endTimeActual = dataObject.opEndTime;
            if (_.isNull(dataObject.opStartTime) && _.isNull(dataObject.opEndTime)) {
                self.flagEnable(false);
            }
            let start = _.isNull(dataObject.opStartTime) ? '--:--' : '10:00';
            let end = _.isNull(dataObject.opEndTime) ? '--:--' : '17:30';
            let idGetList = typeStamp == STAMPTYPE.EXTRAORDINARY ? self.id - 3 : self.id - 1;
            let param = 'items';
            if (typeStamp == STAMPTYPE.ATTENDENCE) {
                this.text1 = nts.uk.resource.getText('KAF002_65', [dataObject.frameNo]); 
                param = param + 1;
            } else if (typeStamp == STAMPTYPE.GOOUT_RETURNING) {
                this.text1 = nts.uk.resource.getText('KAF002_67', [dataObject.frameNo]);
                param = param + 3;
            } else if (typeStamp == STAMPTYPE.BREAK) {
                this.text1 = nts.uk.resource.getText('KAF002_75', [dataObject.frameNo]);
                param = param + 4;
            } else if (typeStamp == STAMPTYPE.PARENT) {
                this.text1 = nts.uk.resource.getText('KAF002_68', [dataObject.frameNo]);
                param = param + 5;
            } else if (typeStamp == STAMPTYPE.NURSE) {
                this.text1 = nts.uk.resource.getText('KAF002_69', [dataObject.frameNo]);
                param = param + 6;
            } else if (typeStamp == STAMPTYPE.EXTRAORDINARY) {
                this.text1 = nts.uk.resource.getText('KAF002_66', [dataObject.frameNo -2]); 
                param = param + 2;
            }
            this.startTime = '<div style="display: block; margin: 0px 5px 5px 5px">'
                                   +'<span align="center" style="display: block">'+ start +'</span>'
                                   +'<div align="center">'
                                           +'<input style="width: 50px; text-align: center" data-name="Time Editor" data-bind="'
                                           +'style:{\'background-color\': $data.'+ param +'['+ idGetList +'].flagEnable() ? ($data.'+param+'['+ idGetList + '].startTimeActual ? ($data.' + param + '['+ idGetList +'].flagObservable() ? \'#b1b1b1\' : \'\') : \'#ffc0cb\') : \'\'},' 
                                           +'ntsTimeEditor: {value: $data.'+ param +'['+ idGetList +'].startTimeRequest, enable: !$data.' + param +'[' + idGetList +'].flagObservable() , constraint: \'SampleTimeDuration\', inputFormat: \'time\', mode: \'time\', readonly: readonly, required: false}" />'
                                   +'</div>'
                              +'</div>';
            this.endTime = '<div style="display: block; margin: 0px 5px 5px 5px">'
                                +'<span align="center" style="display: block">'+ end +'</span>'
                                +'<div align="center">'
                                        +'<input style="width: 50px; text-align: center" data-name="Time Editor" data-bind="'
                                        +'style:{\'background-color\': $data.'+ param +'['+ idGetList +'].flagEnable() ? ($data.'+param+'['+ idGetList + '].endTimeActual ? ($data.' + param + '['+ idGetList +'].flagObservable() ? \'#b1b1b1\' : \'\') : \'#ffc0cb\') : \'\'},' 
                                        +'ntsTimeEditor: {value: $data.'+ param +'['+ idGetList +'].endTimeRequest, enable: !$data.' + param +'[' + idGetList +'].flagObservable() , constraint: \'SampleTimeDuration\', inputFormat: \'time\', mode: \'time\', readonly: readonly, required: false}" />'
                                +'</div>'
                           +'</div>';
//            style: { \'background-color\': isFillColor1() ? \'#ffc0cb\' : \'gray\'} , 
            
            this.flag = '<div  style="display: block" align="center" data-bind="css: !$data.' + param + '[' + idGetList + '].flagEnable() ? \'disableFlag\' : \'enableFlag\' , ntsCheckBox: {enable: $data.' + param + '[' + idGetList + '].flagEnable, checked: $data.' + param + '[' + idGetList + '].flagObservable}"></div>';
           
        }
    }
    
    
    class TimePlaceOutput {
        
        opWorkLocationCD: string;
    
        opGoOutReasonAtr: number;
    
        frameNo: number;
    
        opEndTime: number;
    
        opStartTime: number;
    
        constructor(index: number) {
            this.opWorkLocationCD = null;
            this.opGoOutReasonAtr = null;
            this.frameNo = index;
            if (index == 1) {
                this.opStartTime = 650;
                this.opEndTime = null;
            }else if (index == 3){
                this.opStartTime = 500;
                this.opEndTime = 1500;
                
            } else {
                this.opStartTime = index % 2 == 0 ? 1000 : null;
                this.opEndTime = index % 2 == 0 ? 1500 : null;
            }
        }
        
    }
    
    
    class ItemModel {
        code: string;
        name: string;

        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }
    
    class CellState {
        rowId: number;
        columnKey: string;
        state: Array<any>;
        constructor(rowId: string, columnKey: string, state: Array<any>) {
            this.rowId = rowId;
            this.columnKey = columnKey;
            this.state = state;
        }
    }
    
    export enum STAMPTYPE {
        ATTENDENCE = 0,
        EXTRAORDINARY = 1,
        GOOUT_RETURNING = 2,
        CHEERING = 3,
        PARENT = 4,
        NURSE = 5,
        BREAK = 6
        
    }
    
    
}