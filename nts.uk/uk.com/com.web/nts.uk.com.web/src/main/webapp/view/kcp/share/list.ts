module kcp.share.list {
    export interface UnitModel {
        id?: string;
        code: string;
        name?: string;
        workplaceName?: string;
        isAlreadySetting?: boolean;
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
         * is Multi select.
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
    
    /**
     * Screen Model.
     */
    export class ListComponentScreenModel {
        itemList: KnockoutObservableArray<UnitModel>;
        selectedCodes: KnockoutObservable<any>;
        listComponentColumn: Array<any>;
        isMultiple: boolean;
        isDialog: boolean;
        hasBaseDate: boolean;
        baseDate: KnockoutObservable<Date>;
        isHasButtonSelectAll: boolean;
        gridStyle: GridStyle;
        listType: ListType;
        componentGridId: string;
        alreadySettingList: KnockoutObservableArray<UnitAlreadySettingModel>;
        searchOption: any;
        targetKey: string;
        maxRows: number;
        tabIndex: TabIndex;
        
        constructor() {
            this.itemList = ko.observableArray([]);
            this.listComponentColumn = [];
            this.isMultiple = false;
            this.componentGridId = (Date.now()).toString();
            this.alreadySettingList = ko.observableArray([]);
        }
        /**
         * Init component.
         */
        public init($input: JQuery, data: ComponentOption) :JQueryPromise<void> {
            var dfd = $.Deferred<void>();
            var self = this;
            $(document).undelegate('#' + self.componentGridId, 'iggriddatarendered');
            ko.cleanNode($input[0]);
            
            // Init self data.
            self.isMultiple = data.isMultiSelect;
            self.targetKey = data.listType == ListType.JOB_TITLE ? 'id': 'code';
            self.maxRows = data.maxRows ? data.maxRows : 12;
            self.selectedCodes = data.selectedCode;
            self.isDialog = data.isDialog;
            self.hasBaseDate = data.listType == ListType.JOB_TITLE && !data.isDialog && !data.isMultiSelect;
            self.isHasButtonSelectAll = data.listType == ListType.EMPLOYEE
                 && data.isMultiSelect && data.isShowSelectAllButton;
            self.initGridStyle(data);
            if (data.maxWidth && data.maxWidth <= 350) {
                data.maxWidth = 350;
            }
            self.listType = data.listType;
            self.tabIndex = this.getTabIndexByListType(data);
            if (data.baseDate) {
                self.baseDate = data.baseDate;
            } else {
                self.baseDate = ko.observable(new Date());
            }
            if (self.listType == ListType.JOB_TITLE) {
                this.listComponentColumn.push({headerText: '', hidden: true, prop: 'id'});
            }
            
            // Setup list column.
            this.listComponentColumn.push({headerText: nts.uk.resource.getText('KCP001_2'), prop: 'code', width: self.gridStyle.codeColumnSize,
                        formatter: function(code) {
                            return code;
                        },});
            this.listComponentColumn.push({headerText: nts.uk.resource.getText('KCP001_3'), prop: 'name', width: self.gridStyle.nameColumnSize,
                        template: "<td class='list-component-name-col'>${name}</td>",});
            // With Employee list, add column company name.
            if (data.listType == ListType.EMPLOYEE && data.isShowWorkPlaceName) {
                self.listComponentColumn.push({headerText: nts.uk.resource.getText('KCP005_4'), prop: 'workplaceName', width: self.gridStyle.workplaceColumnSize,
                        template: "<td class='list-component-name-col'>${workplaceName}</td>"});
            }
            
            // If show Already setting.
            if (data.isShowAlreadySet) {
                self.alreadySettingList = data.alreadySettingList;
                // Add row already setting.
                self.listComponentColumn.push({
                    headerText: nts.uk.resource.getText('KCP001_4'), prop: 'isAlreadySetting', width: self.gridStyle.alreadySetColumnSize,
                    formatter: function(isAlreadySet: string) {
                        if (isAlreadySet == 'true') {
                            return '<div style="text-align: center;max-height: 18px;"><i class="icon icon-78"></i></div>';
                        }
                        return '';
                    }
                });
            }
            
            // With list type is employee list, use employee input.
            if (self.listType == ListType.EMPLOYEE) {
                self.initComponent(data, data.employeeInputList(), $input).done(function() {
                    dfd.resolve();
                });
                data.employeeInputList.subscribe(dataList => {
                    self.addAreadySettingAttr(dataList, self.alreadySettingList());
                    self.itemList(dataList);
                    self.createGlobalVarDataList(dataList, $input);
                })
                return dfd.promise();
            }
            
            // Find data list.
            this.findDataList(data.listType).done(function(dataList: Array<UnitModel>) {
                self.initComponent(data, dataList, $input).done(function() {
                    dfd.resolve();
                });
            });
            return dfd.promise();
        }
        
        /**
         * Init component.
         */
        private initComponent(data: ComponentOption, dataList: Array<UnitModel>, $input: JQuery) :JQueryPromise<void>{
            var dfd = $.Deferred<void>();
            var self = this;

            // Map already setting attr to data list.
            if (data.isShowAlreadySet) {
                self.addAreadySettingAttr(dataList, self.alreadySettingList());

                // subscribe when alreadySettingList update => reload component.
                self.alreadySettingList.subscribe((newSettings: Array<UnitModel>) => {
                    var currentDataList = self.itemList();
                    self.addAreadySettingAttr(currentDataList, newSettings);
                    self.itemList(currentDataList);
                })
            }
            self.itemList(dataList);
            
            // Remove No select row.
            self.itemList.remove(self.itemList().filter(item => item.code === '')[0]);
            
            // Check is show no select row.
            if (data.isShowNoSelectRow && self.itemList().map(item => item.code).indexOf('') == -1 && !self.isMultiple) {
                self.itemList.unshift({code: '', name: nts.uk.resource.getText('KCP001_5'), isAlreadySetting: false});
            }
            
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
                
                // Set default value when init component.
                self.initSelectedValue(data, self.itemList());
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
                if (self.isMultiple) {
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
            $('#script-for-' + $input.attr('id')).remove();
            var s = document.createElement("script");
            s.type = "text/javascript";
            s.innerHTML = 'var dataList' + $input.attr('id').replace(/-/gi, '') + ' = ' + JSON.stringify(dataList);
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
        private initSelectedValue(data: ComponentOption, dataList: Array<UnitModel>) {
            var self = this;
            switch(data.selectType) {
                case SelectType.SELECT_BY_SELECTED_CODE:
                    //self.selectedCodes(data.selectedCode());
                    return;
                case SelectType.SELECT_ALL:
                    if (!self.isMultiple){
                        return;
                    }
                    self.selectedCodes(dataList.map(item => self.listType == ListType.JOB_TITLE ? item.id : item.code));
                    return;
                case SelectType.SELECT_FIRST_ITEM:
                    self.selectedCodes(dataList.length > 0 ? self.selectData(data, dataList[0]) : null);
                    return;
                case SelectType.NO_SELECT:
                    self.selectedCodes(data.isMultiSelect ? [] : null);
                    return;
                default:
                    self.selectedCodes(data.isMultiSelect ? [] : null);
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
        private selectData(option: ComponentOption, data: UnitModel) :any {
            if (this.isMultiple) {
                return [data.code];
            }
            if (option.listType = ListType.JOB_TITLE) {
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
            var totalHeight: number = this.hasBaseDate ? 101 : 55;
            codeColumnSize = data.maxWidth ? '25%': codeColumnSize;
            var nameColumnSize = data.maxWidth ? '35%' : 170;
            var workplaceColumnSize = data.maxWidth ? '25%' : 150;
            var alreadySetColumnSize = data.maxWidth ? '15%' : 70;
            this.gridStyle = {
                codeColumnSize: codeColumnSize,
                totalColumnSize: Math.max(minTotalSize, totalColumnSize),
                totalComponentSize: Math.max(minTotalSize, totalColumnSize) + 2,
                totalHeight: totalHeight + totalRowsHeight,
                rowHeight: totalRowsHeight,
                nameColumnSize: nameColumnSize,
                workplaceColumnSize: workplaceColumnSize,
                alreadySetColumnSize: alreadySetColumnSize
            };
        }
        
        /**
         * Find data list.
         */
        private findDataList(listType: ListType):JQueryPromise<Array<UnitModel>> {
            switch(listType) {
                case ListType.EMPLOYMENT:
                    return service.findEmployments();
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
            if (self.itemList().length == 0 || !self.isMultiple) {
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
            if (self.hasBaseDate && (!self.baseDate() || self.baseDate().toString() == '')) {
                return;
            }
            self.findDataList(self.listType).done((data: UnitModel[]) => {
                if (self.alreadySettingList) {
                    self.addAreadySettingAttr(data, self.alreadySettingList());
                }
                self.itemList(data);
            });
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
    
    /**
     * Service,
     */
    export module service {
        
        // Service paths.
        var servicePath = {
            findEmployments: "bs/employee/employment/findAll/",
            findJobTitles: 'bs/employee/jobtitle/findAll',
            findClassifications: 'bs/employee/classification/findAll',
        }
        
        /**
         * Find Employment list.
         */
        export function findEmployments(): JQueryPromise<Array<UnitModel>> {
            return nts.uk.request.ajax('com', servicePath.findEmployments);
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