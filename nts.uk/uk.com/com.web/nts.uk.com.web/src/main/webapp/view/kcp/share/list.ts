module kcp.share.list {
    export interface UnitModel {
        id?: string;
        code: string;
        name?: string;
        workplaceName?: string;
        isAlreadySetting?: boolean;
        optionalColumn?: any;
    }
    
    export interface OptionalColumnDataSource {
        empId: string;
        content: any;
    }

    export interface UnitAlreadySettingModel {
        code: string;
        isAlreadySetting: boolean;
    }
    
    /**
     * Component option.
     */
    export interface ComponentOption {
        /**
         * is Show Already setting.
         */
        isShowAlreadySet: boolean;
        
        /**
         * is Multi use (複数使用区分). Setting use multiple components?
         */
        isMultipleUse: boolean;
        
        /**
         * is Multi select (選択モード). Setting multiple selection in grid.
         */
        isMultiSelect: boolean;
        
        /**
         * list type.
         * 1. Employment list.
         * 2. Classification.
         * 3. Job title list.
         * 4. Employee list.
         */
        listType: ListType;
        
        /**
         * selected value.
         * May be string or Array<string>.
         * Note: With job title list (KCP003), this is selected job title id.
         */
        selectedCode: KnockoutObservable<any>;
        
        /**
         * baseDate. Available for job title list only.
         */
        baseDate?: KnockoutObservable<Date>;
        
        /**
         * is dialog, if is main screen, set false,
         */
        isDialog: boolean;
        
        /**
         * Select Type.
         * 1 - Select by selected codes.
         * 2 - Select All (Cannot select all while single select).
         * 3 - Select First item.
         * 4 - No select.
         */
        selectType: SelectType;
        
        /**
         * Check is show no select row in grid list.
         */
        isShowNoSelectRow: boolean;
        
        /**
         * check is show select all button or not. Available for employee list only.
         */
        isShowSelectAllButton?: boolean;
        
        /**
         * check is show work place column. Available for employee list only.
         */
        isShowWorkPlaceName?: boolean;
        
        /**
         * Already setting list code. structure: {code: string, isAlreadySetting: boolean}
         * ignore when isShowAlreadySet = false.
         * Note: With job title list (KCP003), structure: {id: string, isAlreadySetting: boolean}.
         */
        alreadySettingList?: KnockoutObservableArray<UnitAlreadySettingModel>;
        
        /**
         * Employee input list. Available for employee list only.
         * structure: {code: string, name: string, workplaceName: string}.
         */
        employeeInputList?: KnockoutObservableArray<UnitModel>;
        
        /**
         * Max rows to visible in list component.
         */
        maxRows: number;
        
        /**
         * Set max width for component.Min is 350px;
         */
        maxWidth?: number;
        
        /**
         * Set tabindex attr for controls in component.
         * If not set, tabindex will same as spec of KCPs.
         */
        tabindex?: number;
        
        /**
         * Set to display Closure Selection of not. Available for employment list only.
         */
        isDisplayClosureSelection?: boolean;
        
        /**
         * Set to display FullClosure Option in Closure Selection of not. Available for employment list only.
         */
        isDisplayFullClosureOption?: boolean;
        
        /**
         * Closure select type. Available for employment list only.
         * 1. Select FullClosure option.
         * 2. Select by selected closure code.
         * 3. No select.
         */
        closureSelectionType?: ClosureSelectionType; 
        
        /**
         * Selected closure code. Available for employment list only.
         */
        selectedClosureId?: KnockoutObservable<any>;

        /**
         * Show optional column property. Default = false
         */
        showOptionalColumn?: boolean;

        /**
         * Optional column name.
         */
        optionalColumnName?: string;

        /**
         * Optional column datasource
         */
        optionalColumnDatasource?: KnockoutObservableArray<OptionalColumnDataSource>;

        subscriptions?: Array<KnockoutSubscription>;
    }
    
    export class ClosureSelectionType {
        static SELECT_FULL_OPTION = 1;
        static SELECT_BY_SELECTED_CODE = 2;
        static NO_SELECT = 3;
    }
    
    export class SelectType {
        static SELECT_BY_SELECTED_CODE = 1;
        static SELECT_ALL = 2;
        static SELECT_FIRST_ITEM = 3;
        static NO_SELECT = 4;
    }
    
    /**
     * List Type
     */
    export class ListType {
        static EMPLOYMENT = 1;
        static Classification = 2;
        static JOB_TITLE = 3;
        static EMPLOYEE = 4;
    }
    
    /**
     * Grid style
     */
    export interface GridStyle {
        codeColumnSize: any;
        totalColumnSize: number;
        totalComponentSize: number;
        totalHeight: number;
        rowHeight: number;
        nameColumnSize: any;
        workplaceColumnSize: any;
        alreadySetColumnSize: any;
        optionalColumnSize: any;
    }
    
    /**
     * Tab index.
     */
    export interface TabIndex {
        searchBox: number;
        table: number;
        baseDateInput?: number;
        decideButton?: number;
        selectAllButton?: number;
    }
    
    export interface ClosureItem {
        id: number;
        name: string;
    }
    
    /**
     * Screen Model.
     */
    export class ListComponentScreenModel {
        itemList: KnockoutObservableArray<UnitModel>;
        selectedCodes: KnockoutObservable<any>;
        listComponentColumn: Array<any>;
        isMultipleUse: boolean;
        isMultipleSelect: boolean;
        isDialog: boolean;
        hasBaseDate: boolean;
        baseDate: KnockoutObservable<Date>;
        isHasButtonSelectAll: boolean;
        gridStyle: GridStyle;
        listType: ListType;
        selectType: SelectType;
        componentGridId: string;
        alreadySettingList: KnockoutObservableArray<UnitAlreadySettingModel>;
        searchOption: any;
        targetKey: string;
        maxRows: number;
        tabIndex: TabIndex;
        isDisplayClosureSelection: boolean;
        closureSelectionType: ClosureSelectionType;
        selectedClosureId: KnockoutObservable<any>;
        isDisplayFullClosureOption: boolean;
        closureList: KnockoutObservableArray<ClosureItem>;
        isShowNoSelectRow: boolean;
        isShowAlreadySet: boolean;
        isShowWorkPlaceName: boolean;
        showOptionalColumn: boolean;
        optionalColumnName: string;
        optionalColumnDatasource: KnockoutObservableArray<OptionalColumnDataSource>;
        hasUpdatedOptionalContent: KnockoutObservable<boolean>;
        componentWrapperSelector: string;
        componentWrapperId: string;
        searchBoxId: string;
        
        constructor() {
            this.itemList = ko.observableArray([]);
            this.listComponentColumn = [];
            this.isMultipleUse = false;
            this.isMultipleSelect = false;
            this.componentGridId = (Date.now()).toString();
            this.alreadySettingList = ko.observableArray([]);
            this.isDisplayClosureSelection = false;
            this.isDisplayFullClosureOption = true;
            this.closureSelectionType = ClosureSelectionType.NO_SELECT;
            this.closureList = ko.observableArray([]);
            this.hasUpdatedOptionalContent = ko.observable(false);
            // set random id to prevent bug caused by calling multiple component on the same page
            this.componentWrapperId = nts.uk.util.randomId();
            this.searchBoxId = nts.uk.util.randomId();
            this.componentWrapperSelector = '#' + this.componentWrapperId;
        }

        /**
         * Init component.
         */
        public init($input: JQuery, data: ComponentOption) :JQueryPromise<any> {
            var dfd = $.Deferred<any>();
            var self = this;
            $(document).undelegate('#' + self.componentGridId, 'iggriddatarendered');
            ko.cleanNode($input[0]);

            // clear subscriptions
            if (data.subscriptions) {
                _.each(data.subscriptions, sub => {
                    sub.dispose();
                });
            }
            
            // Init self data.
            if (!nts.uk.util.isNullOrUndefined(data) && !nts.uk.util.isNullOrUndefined(data.isMultipleUse)) { 
                self.isMultipleUse = data.isMultipleUse; 
            }
            self.isMultipleSelect = data.isMultiSelect;
            self.targetKey = data.listType == ListType.JOB_TITLE ? 'id': 'code';
            self.maxRows = data.maxRows ? data.maxRows : 12;
            self.selectType = data.selectType;
            self.selectedCodes = data.selectedCode;
            self.isDialog = data.isDialog;
            self.hasBaseDate = data.listType == ListType.JOB_TITLE && !data.isDialog && !data.isMultipleUse;
            self.isHasButtonSelectAll = data.listType == ListType.EMPLOYEE
                 && data.isMultiSelect && data.isShowSelectAllButton;
            self.isShowNoSelectRow = data.isShowNoSelectRow;
            self.isShowAlreadySet = data.isShowAlreadySet;
            self.isShowWorkPlaceName = data.isShowWorkPlaceName;
            self.showOptionalColumn = data.showOptionalColumn ? data.showOptionalColumn : false;
            self.optionalColumnName = data.optionalColumnName;
            self.optionalColumnDatasource = data.optionalColumnDatasource;
            
            // Init data for employment list component.
            if (data.listType == ListType.EMPLOYMENT) {
                self.selectedClosureId = data.selectedClosureId ? data.selectedClosureId : ko.observable(null);
                self.initClosureSubscription(data.subscriptions);
                self.isDisplayClosureSelection = data.isDisplayClosureSelection ? true : false;
                self.isDisplayFullClosureOption = data.isDisplayFullClosureOption ? true : false;
                self.closureSelectionType = data.closureSelectionType ? data.closureSelectionType : ClosureSelectionType.NO_SELECT;
                if (data.isDisplayClosureSelection) {
                    self.initSelectClosureOption(data);
                }
            }
            self.initGridStyle(data);
            self.listType = data.listType;
            self.tabIndex = this.getTabIndexByListType(data);
            if (data.baseDate) {
                self.baseDate = data.baseDate;
            } else {
                self.baseDate = ko.observable(new Date());
            }

            // Setup list column.
            self.setupListColumns();

            // When itemList change -> refesh data list.
            self.itemList.subscribe(newList => {
                if(self.showOptionalColumn && !self.hasUpdatedOptionalContent()) {
                    self.addOptionalContentToItemList();
                }
                self.hasUpdatedOptionalContent(false);
                self.reloadNtsGridList();
                self.createGlobalVarDataList(newList, $input);
            });
            
            // With list type is employee list, use employee input.
            if (self.listType == ListType.EMPLOYEE) {
                self.initEmployeeSubscription(data);
                self.initComponent(data, data.employeeInputList(), $input).done(function() {
                    // Set default value when init component.
                    _.defer(() => self.loadNtsGridList());
                    dfd.resolve(self);
                });
                return dfd.promise();
            }
            
            // Find data list.
            this.findDataList(data.listType).done(function(dataList: Array<UnitModel>) {
                self.initComponent(data, dataList, $input).done(function() {
                    // Set default value when init component.
                    _.defer(() => self.loadNtsGridList());
                    dfd.resolve(self);
                });
            });
            return dfd.promise();
        }

        /**
         * Reload nts grid list
         */
        private reloadNtsGridList(): void {
            let self = this;
            $('#' + self.componentGridId).ntsGridList("setDataSource", self.itemList());
        }

        private loadNtsGridList(): void {
            let self = this;
            _.defer(() => {
                // Set default value when init component.
                self.initSelectedValue();
                const options = {
                    width: self.gridStyle.totalColumnSize,
                    dataSource: self.itemList(),
                    primaryKey: self.targetKey,
                    columns: self.listComponentColumn,
                    multiple: self.isMultipleSelect,
                    value: self.selectedCodes(),
                    name: self.getItemNameForList(),
                    rows: self.maxRows
                };
                const searchBoxOptions = {
                    searchMode: 'filter',
                    targetKey: self.targetKey,
                    comId: self.componentGridId,
                    items: self.itemList(),
                    selected: self.selectedCodes(),
                    selectedKey: self.targetKey,
                    fields: ['name', 'code'],
                    mode: 'igGrid'
                };

                // load ntsGrid & searchbox component
                $('#' + self.searchBoxId).ntsSearchBox(searchBoxOptions);
                $('#' + self.componentGridId).ntsGridList(options);

                // setup event
                self.initEvent();
            });
        }

        // set up on selected code changed event
        private initEvent(): void {
            let self = this;
            self.selectedCodes.subscribe(value => {
                $('#' + self.componentGridId).ntsGridList("setSelectedValue", value);
            });
            $(self.componentWrapperSelector).on('click', () => {
                const selecteds = $('#' + self.componentGridId).ntsGridList("getSelectedValue");
                if (self.isMultipleSelect) {
                    self.selectedCodes(_.map(selecteds, o => o.id));
                } else {
                    self.selectedCodes(selecteds.id);
                }
            });
            $(self.componentWrapperSelector + ' span').on('click', () => {
                const selecteds = $('#' + self.componentGridId).ntsGridList("getSelectedValue");
                if (self.isMultipleSelect) {
                    self.selectedCodes(_.map(selecteds, o => o.id));
                } else {
                    self.selectedCodes(selecteds.id);
                }
            });
            $(self.componentWrapperSelector).on('keyup', e => {
                if (e.which != KeyCode.ARROW_UP ||
                    e.which != KeyCode.ARROW_DOWN ||
                    e.which != KeyCode.ARROW_LEFT ||
                    e.which != KeyCode.ARROW_RIGHT ||
                    e.which != KeyCode.ENTER) {
                    return;
                }
                const selecteds = $('#' + self.componentGridId).ntsGridList("getSelectedValue");
                if (self.isMultipleSelect) {
                    self.selectedCodes(_.map(selecteds, o => o.id));
                } else {
                    self.selectedCodes(selecteds.id);
                }
            });
        }

        private initClosureSubscription(subscriptions: Array<KnockoutSubscription>): void {
            let self = this;
            if (subscriptions) {
                subscriptions.push(self.selectedClosureId.subscribe(id => {
                    self.reloadEmployment(id);
                }));
            } else {
                self.selectedClosureId.subscribe(id => {
                    self.reloadEmployment(id);
                });
            }
        }

        private initEmployeeSubscription(data: ComponentOption): void {
            let self = this;
            if (data.subscriptions) {
                data.subscriptions.push(data.employeeInputList.subscribe(dataList => {
                    self.addAreadySettingAttr(dataList, self.alreadySettingList());
                    self.itemList(dataList);
                }));
            } else {
                data.employeeInputList.subscribe(dataList => {
                    self.addAreadySettingAttr(dataList, self.alreadySettingList());
                    self.itemList(dataList);
                });
            }
        }

        /**
         * Setup list columns
         */
        private setupListColumns(): void {
            let self = this;

            // id column
            if (self.listType == ListType.JOB_TITLE) {
                self.listComponentColumn.push({ headerText: '', hidden: true, prop: 'id' });
            }

            // code column
            self.listComponentColumn.push({
                headerText: nts.uk.resource.getText('KCP001_2'), prop: 'code', width: self.gridStyle.codeColumnSize,
                formatter: function(code) {
                    return code;
                }
            });

            // name column
            self.listComponentColumn.push({
                headerText: nts.uk.resource.getText('KCP001_3'), prop: 'name', width: self.gridStyle.nameColumnSize,
                template: "<td class='list-component-name-col'>${name}</td>",
            });

            // workplace name column
            if (self.listType == ListType.EMPLOYEE && self.isShowWorkPlaceName) {
                self.listComponentColumn.push({
                    headerText: nts.uk.resource.getText('KCP005_4'), prop: 'workplaceName', width: self.gridStyle.workplaceColumnSize,
                    template: "<td class='list-component-name-col'>${workplaceName}</td>"
                });
            }

            // optional column
            if (self.showOptionalColumn) {
                self.addOptionalContentToItemList();
                self.listComponentColumn.push({
                    headerText: self.optionalColumnName, prop: 'optionalColumn', width: self.gridStyle.optionalColumnSize,
                    template: "<td class='list-component-name-col'>${optionalColumn}</td>"
                });
            }

            // Already setting column
            if (self.isShowAlreadySet) {
                self.listComponentColumn.push({
                    headerText: nts.uk.resource.getText('KCP001_4'), prop: 'isAlreadySetting', width: self.gridStyle.alreadySetColumnSize,
                    formatter: function(isAlreadySet) {
                        if (isAlreadySet == true || isAlreadySet == 'true') {
                            return '<div style="text-align: center;max-height: 18px;"><i class="icon icon-78"></i></div>';
                        }
                        return '';
                    }
                });
            }

        }

        /**
         * Add optional content to item list
         */
        private addOptionalContentToItemList(): void {
            let self = this;
            let mappedList = _.map(self.itemList(), item => {
                const found = _.find(self.optionalColumnDatasource(), vl => vl.empId == item.code);
                item.optionalColumn = found ? found.content : '';
                return item;
            });
            self.hasUpdatedOptionalContent(true);
            self.itemList(mappedList);
        }
        
        /**
         * Inint Closure Selection Type.
         */
        private initSelectClosureOption(data: ComponentOption) {
            var self = this;
            switch(data.closureSelectionType) {
                case ClosureSelectionType.SELECT_FULL_OPTION:
                    if (data.isDisplayFullClosureOption) {
                        self.selectedClosureId(0);
                    }
                    break;
                case ClosureSelectionType.SELECT_BY_SELECTED_CODE:
                    break;
                case ClosureSelectionType.NO_SELECT:
                    self.selectedClosureId(data.isDisplayFullClosureOption ? 0 : 1);
                    break;
                default:
                    break;
            }
        }
        
        /**
         * Init component.
         */
        private initComponent(data: ComponentOption, dataList: Array<UnitModel>, $input: JQuery) :JQueryPromise<void>{
            var dfd = $.Deferred<void>();
            var self = this;

            if (self.showOptionalColumn) {
                self.optionalColumnDatasource.subscribe(vl => {
                    self.addOptionalContentToItemList();
                });
            }

            // Map already setting attr to data list.
            if (!_.isNil(data.alreadySettingList)) {
                self.alreadySettingList = data.alreadySettingList;
                self.addAreadySettingAttr(dataList, self.alreadySettingList());

                // subscribe when alreadySettingList update => reload component.
                self.alreadySettingList.subscribe((newSettings: Array<UnitModel>) => {
                    var currentDataList = self.itemList();
                    self.addAreadySettingAttr(currentDataList, newSettings);
                    self.itemList(currentDataList);
                })
            }
            self.itemList(dataList);
            self.initNoSelectRow(data.isShowNoSelectRow);
             
            
            // Init component.
            var fields: Array<string> = ['name', 'code'];
            if (data.isShowWorkPlaceName) {
                fields.push('workplaceName');
            }
            self.searchOption = {
                searchMode: 'filter',
                targetKey: self.targetKey,
                comId: self.componentGridId,
                items: self.itemList,
                selected: self.selectedCodes,
                selectedKey: self.targetKey,
                fields: fields,
                mode: 'igGrid'
            }
            var webserviceLocator = nts.uk.request.location.siteRoot
                .mergeRelativePath(nts.uk.request.WEB_APP_NAME["com"] + '/')
                .mergeRelativePath('/view/kcp/share/list.xhtml').serialize();
            $input.load(webserviceLocator, function() {
                $input.find('table').attr('id', self.componentGridId);
                ko.applyBindings(self, $input[0]);
                $input.find('.base-date-editor').find('.nts-input').width(133);
                dfd.resolve();
            });
            
            $(document).delegate('#' + self.componentGridId, "iggridrowsrendered", function(evt) {
                self.addIconToAlreadyCol();
            });
            
            // defined function get data list.
            self.createGlobalVarDataList(dataList, $input);
            $.fn.getDataList = function(): Array<kcp.share.list.UnitModel> {
                return window['dataList' + this.attr('id').replace(/-/gi, '')];
            }
            
            // defined function focus
            $.fn.focusComponent = function() {
                if (self.hasBaseDate) {
                    $input.find('.base-date-editor').first().focus();
                } else {
                    $input.find(".ntsSearchBox").focus();
                }
            }
            $.fn.reloadJobtitleDataList = self.reload;
            $.fn.isNoSelectRowSelected = function() {
                if (self.isMultipleSelect) {
                    return false;
                }
                var selectedRow: any = $('#' + self.componentGridId).igGridSelection("selectedRow");
                if (selectedRow && selectedRow.id === '' && selectedRow.index > -1) {
                    return true;
                }
                return false;
            }
            return dfd.promise();
        }
        
        /**
         * Add No select row to list
         */
        private initNoSelectRow(isShowNoSelectRow: boolean) {
            var self = this;
            // Remove No select row.
            self.itemList.remove(self.itemList().filter(item => item.code === '')[0]);
            
            // Check is show no select row.
            if (isShowNoSelectRow && self.itemList().map(item => item.code).indexOf('') == -1 && !self.isMultipleSelect) {
                self.itemList.unshift({code: '', id: '', name: nts.uk.resource.getText('KCP001_5'), isAlreadySetting: false});
            }
        }
        
        /**
         * Get tab index by list type.
         */
        private getTabIndexByListType(data: ComponentOption): TabIndex {
            if (data.tabindex) {
                return {
                    searchBox: data.tabindex,
                    table: data.tabindex,
                    baseDateInput: data.tabindex,
                    decideButton: data.tabindex
                }
            }
            switch(data.listType) {
                case ListType.EMPLOYMENT, ListType.Classification:
                    return {
                        searchBox: 1,
                        table: 2
                    }
                case ListType.JOB_TITLE:
                    return {
                        searchBox: 3,
                        table: 4,
                        baseDateInput: 1,
                        decideButton: 2
                    }
                case ListType.EMPLOYEE:
                    return {
                        searchBox: 1,
                        table: 2,
                        selectAllButton: 3
                    }
            }
            return {
                searchBox: 1,
                table: 2
            }
        }
        
        /**
         * create Global Data List.
         */
        private createGlobalVarDataList(dataList: Array<UnitModel>, $input: JQuery) {
            var dataListCloned : Array<UnitModel> = _.cloneDeep(dataList);
            _.remove(dataListCloned, item => !item.id && !item.code)
            $('#script-for-' + $input.attr('id')).remove();
            var s = document.createElement("script");
            s.type = "text/javascript";
            s.innerHTML = 'var dataList' + $input.attr('id').replace(/-/gi, '') + ' = ' + JSON.stringify(dataListCloned);
            s.id = 'script-for-' + $input.attr('id');
            $("head").append(s);
        }
        
        /**
         * Add Icon to already column setting
         */
        private addIconToAlreadyCol() {
            // Add icon to column already setting.
            var iconLink = nts.uk.request.location.siteRoot
                .mergeRelativePath(nts.uk.request.WEB_APP_NAME["com"] + '/')
                .mergeRelativePath('/view/kcp/share/icon/icon78.png').serialize();
            $('.icon-78').attr('style', "background: url('" + iconLink + "');width: 20px;height: 20px;background-size: 20px 20px;")
        }
        
        /**
         * Init default selected value.
         */
        private initSelectedValue() {
            var self = this;
            switch(self.selectType) {
                case SelectType.SELECT_BY_SELECTED_CODE:
                    if(self.isShowNoSelectRow && _.isEmpty(self.selectedCodes())) {
                        self.selectedCodes("");
                    }
                    return;
                case SelectType.SELECT_ALL:
                    if (!self.isMultipleSelect){
                        return;
                    }
                    self.selectedCodes(self.itemList().map(item => self.listType == ListType.JOB_TITLE ? item.id : item.code));
                    return;
                case SelectType.SELECT_FIRST_ITEM:
                    self.selectedCodes(_.isEmpty(self.itemList()) ? null : self.selectData(self.itemList()[0]));
                    return;
                case SelectType.NO_SELECT:
                    self.selectedCodes(self.isMultipleSelect ? [] : null);
                    return;
                default:
                    self.selectedCodes(self.isMultipleSelect ? [] : null);
            }
        }
        
        /**
         * Add Aready Setting Attr into data list.
         */
        private addAreadySettingAttr(dataList: Array<UnitModel>, alreadySettingList: Array<UnitModel>) {
            if (this.listType == ListType.JOB_TITLE) {
                // Use id to set already  setting list.
                var alreadyListCode = alreadySettingList.filter(item => item.isAlreadySetting).map(item => item.id);
                dataList.map((item => {
                    item.isAlreadySetting = alreadyListCode.indexOf(item.id) > -1;
                }))
                return;
            }
            var alreadyListCode = alreadySettingList.filter(item => item.isAlreadySetting).map(item => item.code);
            dataList.map((item => {
                item.isAlreadySetting = alreadyListCode.indexOf(item.code) > -1;
            }))
        }
        
        /**
         * Select data for multiple or not
         */
        private selectData(data: UnitModel): any {
            let self = this;
            if (self.isMultipleSelect) {
                return self.listType == ListType.JOB_TITLE ? [data.id] : [data.code];
            }
            if (self.listType == ListType.JOB_TITLE) {
                return data.id;
            }
            return data.code;
        }
        
        /**
         * Init Grid Style.
         */
        private initGridStyle(data: ComponentOption) {
            var codeColumnSize: any = 50;
            var companyColumnSize: number = 0;
            var heightOfRow : number = data.isMultiSelect ? 24 : 23;
            switch(data.listType) {
                case ListType.EMPLOYMENT:
                    break;
                case ListType.JOB_TITLE:
                    codeColumnSize = 70;
                    break;
                case ListType.Classification:
                    codeColumnSize = 150;
                    break;
                case ListType.EMPLOYEE:
                    codeColumnSize = 150;
                    companyColumnSize = data.isShowWorkPlaceName ? 150 : 0;
                    break;
                default:
                break;
            }
            var alreadySettingColSize = data.isShowAlreadySet ? 70 : 0;
            var multiSelectColSize = data.isMultiSelect ? 55 : 0;
            var selectAllButtonSize = this.isHasButtonSelectAll ? 60 : 0;
            var totalColumnSize: number = data.maxWidth ? data.maxWidth : codeColumnSize + 170 + companyColumnSize
                + alreadySettingColSize + multiSelectColSize;
            var minTotalSize = this.isHasButtonSelectAll ? 415 : 350;
            var totalRowsHeight = heightOfRow * this.maxRows + 24;
            var totalHeight: number = this.hasBaseDate || this.isDisplayClosureSelection ? 101 : 55;
            var optionalColumnSize = 0;

            if (this.showOptionalColumn) {
                codeColumnSize = data.maxWidth ? '15%': codeColumnSize;
                var nameColumnSize = data.maxWidth ? '30%' : 170;
                var workplaceColumnSize = data.maxWidth ? '20%' : 150;
                var alreadySetColumnSize = data.maxWidth ? '15%' : 70;
                optionalColumnSize = data.maxWidth ? '20%' : 150;
            } else {
                codeColumnSize = data.maxWidth ? '25%' : codeColumnSize;
                var nameColumnSize = data.maxWidth ? '30%' : 170;
                var workplaceColumnSize = data.maxWidth ? '30%' : 150;
                var alreadySetColumnSize = data.maxWidth ? '15%' : 70;
            }
            this.gridStyle = {
                codeColumnSize: codeColumnSize,
                totalColumnSize: Math.max(minTotalSize, totalColumnSize),
                totalComponentSize: Math.max(minTotalSize, totalColumnSize) + 2,
                totalHeight: totalHeight + totalRowsHeight,
                rowHeight: totalRowsHeight,
                nameColumnSize: nameColumnSize,
                workplaceColumnSize: workplaceColumnSize,
                alreadySetColumnSize: alreadySetColumnSize,
                optionalColumnSize: optionalColumnSize
            };
            if (data.maxWidth && data.maxWidth <= 350) {
                data.maxWidth = 350;
            }
        }
        
        /**
         * Find data list.
         */
        private findDataList(listType: ListType):JQueryPromise<Array<UnitModel>> {
            var self = this;
            var dfd = $.Deferred<any>();
            switch(listType) {
                case ListType.EMPLOYMENT:
                    if (self.isDisplayClosureSelection) {
                        // Find all closure in current month.
                        service.findAllClosure().done((items: ClosureItem[]) => {
                            items = _.sortBy(items, item => item.id);
                            self.closureList(items);
                            // if show FullClosureOption -> add option.
                            if (self.isDisplayFullClosureOption) {
                                self.closureList.unshift({id: 0, name: nts.uk.resource.getText('CCG001_64')})
                            }
                            var selectedClosure = 0;
                            if (!self.selectedClosureId()) {
                                selectedClosure = self.closureList().length > 1 ? self.closureList()[0].id : 0;
                            } else {
                                selectedClosure = self.selectedClosureId();
                            }
                            service.findEmployments(selectedClosure).done(data => {
                                dfd.resolve(data);
                            });
                        })
                        return dfd.promise();
                    } else {
                        if (self.selectedClosureId()){
                            return service.findEmployments(self.selectedClosureId());
                        }
                        return service.findAllEmployments();
                    }
                case ListType.JOB_TITLE:
                    return service.findJobTitles(this.baseDate());
                case ListType.Classification:
                    return service.findClassifications();
                default:
                    return;
            }
        }
        
        /**
         * Select all.
         */
        public selectAll() {
            var self = this;
            if (self.itemList().length == 0 || !self.isMultipleSelect) {
                return;
            }
            self.selectedCodes(self.itemList().map(item => item.code));
        }
        
        /**
         * Reload screen data.
         */
        public reload() {
            var self = this;
            // Check if is has base date.
            if ((self.hasBaseDate && (!self.baseDate() || self.baseDate().toString() == ''))||nts.uk.ui.errors.hasError()) {
                return;
            }
            self.findDataList(self.listType).done((data: UnitModel[]) => {
                if (self.alreadySettingList) {
                    self.addAreadySettingAttr(data, self.alreadySettingList());
                }
                _.defer(() => {
                    self.itemList(data);
                    self.initNoSelectRow(self.isShowNoSelectRow);
                    self.reloadNtsGridList();
                });
            });
        }
        
        /**
         * Reload employment.
         */
        private reloadEmployment(closureId: number) {
            var self = this;
            service.findEmployments(closureId).done(function(data: UnitModel[]) {
                if (!_.isEmpty(self.alreadySettingList())) {
                    self.addAreadySettingAttr(data, self.alreadySettingList());
                }
                self.itemList(data);
                self.initNoSelectRow(self.isShowNoSelectRow);

                // set selected codes
                const selectedCodes = _.filter(self.selectedCodes(), code => _.find(data, item => code == item.code));
                if (nts.uk.util.isNullOrEmpty(selectedCodes)) {
                    self.selectedCodes(self.isMultipleSelect ? [] : null);
                } else {
                    self.selectedCodes(selectedCodes);
                }
                self.reloadNtsGridList();
            })
        }
        
        /**
         * Get item name for each component type.
         */
        public getItemNameForList(): string {
            switch(this.listType) {
                case ListType.EMPLOYMENT:
                    return '#[KCP001_1]';
                case ListType.JOB_TITLE:
                    return '#[KCP003_1]';
                case ListType.Classification:
                    return '#[KCP002_1]';
                case ListType.EMPLOYEE:
                    return '#[KCP005_1]';
                default:
                    return '';
            }
        }
        
        public getItemNameForBaseDate(): string {
            if (this.hasBaseDate) {
                return '#[KCP003_2]'
            }
            return '';
        }
    }

    export class KeyCode {
        static ARROW_UP = 37;
        static ARROW_DOWN = 38;
        static ARROW_LEFT = 39;
        static ARROW_RIGHT = 40;
        static ENTER = 13;
    }

    
    /**
     * Service,
     */
    export module service {
        
        // Service paths.
        var servicePath = {
            findEmployments: "bs/employee/employment/findAll/",
            findJobTitles: 'bs/employee/jobtitle/findAll',
            findClassifications: 'bs/employee/classification/findAll',
            findAllClosureItems: 'ctx/at/shared/workrule/closure/findClosureListByCurrentMonth',
            findEmploymentByClosureId: 'ctx/at/shared/workrule/closure/findEmpByClosureId/',
            findEmploymentByCodes: 'bs/employee/employment/findByCodes'
        }
        
        /**
         * Find Employment list.
         */
        export function findEmployments(closureId?: number): JQueryPromise<Array<UnitModel>> {
            
            // Find Employment Closure.
            var dfd = $.Deferred<Array<UnitModel>>();
            nts.uk.request.ajax('at', servicePath.findEmploymentByClosureId + closureId).done(function(empList: Array<any>) {
                if (empList && empList.length > 0) {
                    // Find by employment codes.
                    nts.uk.request.ajax('com', servicePath.findEmploymentByCodes, empList).done(data => {
                        dfd.resolve(data);
                    })
                    return dfd.promise();
                }
                dfd.resolve([])
            })
            return dfd.promise();
        }
        
        export function findAllEmployments(): JQueryPromise<Array<UnitModel>>{
            return nts.uk.request.ajax('com', servicePath.findEmployments);
        }
        
        /**
         * Find Closure list.
         */
        export function findAllClosure(): JQueryPromise<Array<ClosureItem>> {
            return nts.uk.request.ajax('at', servicePath.findAllClosureItems);
        }
        
        /**
         * Find Job title.
         */
        export function findJobTitles(baseDate: Date): JQueryPromise<Array<UnitModel>> {
            return nts.uk.request.ajax('com', servicePath.findJobTitles, {baseDate: baseDate});
        }
        
        /**
         * Find Classification list.
         */
        export function findClassifications(): JQueryPromise<Array<UnitModel>> {
            return nts.uk.request.ajax('com', servicePath.findClassifications);
        }
        
    }
}

/**
 * Defined Jquery interface.
 */
interface JQuery {

    /**
     * Nts list component.
     * This Function used after apply binding only.
     */
    ntsListComponent(option: kcp.share.list.ComponentOption): JQueryPromise<void>;
    
    /**
     * Get data list in component.
     */
    getDataList(): Array<kcp.share.list.UnitModel>;
    
    /**
     * Focus component.
     */
    focusComponent(): void;
    
    /**
     * Function reload job title data list. Support job title list only.
     */
    reloadJobtitleDataList(): void;
    
    /**
     * Check isNoSelectRowSelected.
     */
    isNoSelectRowSelected(): boolean;
}

(function($: any) {
    $.fn.ntsListComponent = function(option: kcp.share.list.ComponentOption): JQueryPromise<void> {

        // Return.
        return new kcp.share.list.ListComponentScreenModel().init(this, option);
    }
    
} (jQuery));