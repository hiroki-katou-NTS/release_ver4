module kcp.share.tree {
    export interface UnitModel {
        workplaceId: string;
        code: string;
        name: string;
        nodeText?: string;
        level: number;
        heirarchyCode: string;
        isAlreadySetting?: boolean;
        childs: Array<UnitModel>;
    }
    
    export interface UnitAlreadySettingModel {
        /**
         * workplaceId
         */
        workplaceId: string;
        
        /**
         * State setting:
         *  true: saved setting.
         *  false: parent setting, child does not setting.
         *  undefined || null: both parent and child do not setting.
         */
        isAlreadySetting: boolean;
    }
    
    export interface RowSelection {
        workplaceId: string;
        workplaceCode: string;
    }
    
    export interface TreeComponentOption {
        /**
         * is Show Already setting.
         */
        isShowAlreadySet: boolean;
        
        /**
         * is Multi select.
         */
        isMultiSelect: boolean;
        
        /**
         * Tree type, if not set, default is work place.
         */
        treeType?: TreeType;
        
        /**
         * selected value.
         * May be string or Array<string>
         */
        selectedWorkplaceId: KnockoutObservable<any>;
        
        /**
         * Base date.
         */
        baseDate: KnockoutObservable<Date>;
        
        /**
         * Select mode
         */
        selectType: SelectionType; 
        
        /**
         * isShowSelectButton
         * Show/hide button select all and selected sub parent
         */
        isShowSelectButton: boolean;
        
        /**
         * is dialog, if is main screen, set false,
         */
        isDialog: boolean;
        
        /**
         * Already setting list code. structure: {workplaceId: string, isAlreadySetting: boolean}
         * ignore when isShowAlreadySet = false.
         */
        alreadySettingList?: KnockoutObservableArray<UnitAlreadySettingModel>;
    }
    
    export class TreeType {
        static WORK_PLACE = 1;
    }
    
    export class SelectionType {
        static SELECT_BY_SELECTED_CODE = 1;
        static SELECT_ALL = 2;
        static SELECT_FIRST_ITEM = 3;
        static NO_SELECT = 4;
    }
    
    export class TreeComponentScreenModel {
        itemList: KnockoutObservableArray<UnitModel>;
        backupItemList: KnockoutObservableArray<UnitModel>;
        selectedWorkplaceIds: KnockoutObservable<any>;
        isShowSelectButton: boolean;
        treeComponentColumn: Array<any>;
        isMultiple: boolean;
        isDialog: boolean;
        hasBaseDate: KnockoutObservable<boolean>;
        baseDate: KnockoutObservable<Date>;
        levelList: Array<any>;
        levelSelected: KnockoutObservable<number>;
        listWorkplaceId: Array<string>;
        alreadySettingList: KnockoutObservableArray<UnitAlreadySettingModel>;
        
        constructor() {
            let self = this;
            self.itemList = ko.observableArray([]);
            self.backupItemList = ko.observableArray([]);
            self.listWorkplaceId = [];
            self.hasBaseDate = ko.observable(false);
            self.alreadySettingList = ko.observableArray([]);
            self.treeComponentColumn = [
                { headerText: "", key: 'workplaceId', dataType: "string", hidden: true },
                { headerText: nts.uk.resource.getText("KCP004_5"), key: 'nodeText', width: "90%", dataType: "string" }
            ];
            self.levelList = [
                {level: 1, name: '1'},
                {level: 2, name: '2'},
                {level: 3, name: '3'},
                {level: 4, name: '4'},
                {level: 5, name: '5'},
                {level: 6, name: '6'},
                {level: 7, name: '7'},
                {level: 8, name: '8'},
                {level: 9, name: '9'},
                {level: 10, name: '10'}
            ];
            self.levelSelected = ko.observable(10);
        }
        
        public init($input: JQuery, data: TreeComponentOption) :JQueryPromise<void> {
            let self = this;
            let dfd = $.Deferred<void>();
            self.isMultiple = data.isMultiSelect;
            self.hasBaseDate(!self.isMultiple);
            self.selectedWorkplaceIds = data.selectedWorkplaceId;
            self.isShowSelectButton = data.isShowSelectButton;
            self.isDialog = data.isDialog;
            self.baseDate = data.baseDate;
            if (data.alreadySettingList) {
                self.alreadySettingList = data.alreadySettingList;
            }
            
            // If show Already setting.
            if (data.isShowAlreadySet) {
                // Add row already setting.
                self.treeComponentColumn.push({
                    headerText: nts.uk.resource.getText('KCP004_6'), key: 'isAlreadySetting', width: "15%", dataType: 'string'
                    formatter: function(isAlreadySetting: string) {
                        if (isAlreadySetting == 'true') {
                            return '<div style="text-align: center;"><i class="icon icon icon-78"></i></div>';
                        }
                        if (isAlreadySetting == 'false') {
                            return '<div style="text-align: center;"><i class="icon icon icon-84"></i></div>';
                        }
                        return '';
                    }
                });
            }
            // subscribe change selected level
            self.levelSelected.subscribe(function(level) {
                self.filterData();
            });
            
            // subscribe change item list origin
            self.backupItemList.subscribe((newData) => {
                // data is empty, set selected work place id empty
                if (!newData || newData.length <= 0) {
                    self.selectedWorkplaceIds(self.isMultiple ? [] : '');
                }
            });
            
            // Find data.
            service.findWorkplaceTree(self.baseDate()).done(function(res: Array<UnitModel>) {
                if (res != null) {
                    // Map already setting attr to data list.
                    self.addAlreadySettingAttr(res, self.alreadySettingList());
                    
                    // Set default value when initial component.
                    self.initSelectedValue(data, res);
                    
                    if (data.isShowAlreadySet) { 
                        // subscribe when alreadySettingList update => reload component.
                        self.alreadySettingList.subscribe((newAlreadySettings: any) => {
                            self.addAlreadySettingAttr(res, newAlreadySettings);
                            self.itemList(res);
                            self.backupItemList(res);
                            
                            self.filterData();
                        });
                    }
                    
                    // Init component.
                    self.itemList(res);
                    self.backupItemList(res);
                }
                var webserviceLocator = nts.uk.request.location.siteRoot
                    .mergeRelativePath(nts.uk.request.WEB_APP_NAME["com"] + '/')
                    .mergeRelativePath('/view/kcp/share/tree.xhtml').serialize();
                $input.load(webserviceLocator, function() {
                    ko.cleanNode($input[0]);
                    ko.applyBindings(self, $input[0]);
                    
                    // defined function get data list.
                    $('#script-for-' + $input.attr('id')).remove();
                    var s = document.createElement("script");
                    s.type = "text/javascript";
                    s.innerHTML = 'var dataList' + $input.attr('id').replace(/-/gi, '') + ' = '
                        + JSON.stringify(self.backupItemList());
                    s.id = 'script-for-' + $input.attr('id');
                    $("head").append(s);
                    $.fn.getDataList = function(): Array<kcp.share.list.UnitModel> {
                        return window['dataList' + this.attr('id').replace(/-/gi, '')];
                    }
                    dfd.resolve();
                });
                
                $(document).delegate('#' + self.getComIdSearchBox(), "igtreegridrowsrendered", function(evt, ui) {
                   self.addIconToAlreadyCol();
                });
                // defined function focus
                $.fn.focusTreeGridComponent = function() {
                    if (self.hasBaseDate()) {
                        $('.base-date-editor').first().focus();
                    } else {
                        $(".ntsSearchBox").focus();
                    }
                }
            });
            
            // defined function get data list.
            $.fn.getDataList = function(): Array<kcp.share.tree.UnitModel> {
                return self.backupItemList();
            }
            
            // define function get row selected
            $.fn.getRowSelected = function(): Array<any> {
                let listModel = self.findUnitModelByListWorkplaceId(self.backupItemList());
                let listRowSelected: Array<RowSelection> = [];
                self.findSelectionRowData(listModel, listRowSelected);
                return listRowSelected;
            }
            
            return dfd.promise();
        }
        
        /**
         * Initial select mode
         */
        private initSelectedValue(data: TreeComponentOption, dataList: Array<UnitModel>) {
            let self = this;
            switch(data.selectType) {
                case SelectionType.SELECT_BY_SELECTED_CODE:
                    if (self.isMultiple) {
                        self.selectedWorkplaceIds = data.selectedWorkplaceId;
                    }
                    break;
                case SelectionType.SELECT_ALL:
                    if (self.isMultiple) { 
                        self.selectAll();
                    }
                    break;
                case SelectionType.SELECT_FIRST_ITEM:
                    self.selectedWorkplaceIds(dataList.length > 0 ? self.selectData(data, dataList[0]) : null);
                    break;
                case SelectionType.NO_SELECT:
                    self.selectedWorkplaceIds(data.isMultiSelect ? [] : '');
                    break;
                default:
                    self.selectedWorkplaceIds(data.isMultiSelect ? [] : '');
                    break
            }
        }
        
        /**
         * add icon by already setting
         */
        private addIconToAlreadyCol() {
            var icon84Link = nts.uk.request.location.siteRoot
                .mergeRelativePath(nts.uk.request.WEB_APP_NAME["com"] + '/')
                .mergeRelativePath('/view/kcp/share/icon/icon84.png').serialize();
            $('.icon-84').attr('style', "background: url('"+ icon84Link
                +"');width: 20px;height: 20px;background-size: 20px 20px;")
            
            var icon78Link = nts.uk.request.location.siteRoot
                .mergeRelativePath(nts.uk.request.WEB_APP_NAME["com"] + '/')
                .mergeRelativePath('/view/kcp/share/icon/icon78.png').serialize();
            $('.icon-78').attr('style', "background: url('"+ icon78Link
                +"');width: 20px;height: 20px;background-size: 20px 20px;")
        }
        
        /**
         * Add Already Setting Attr into data list.
         */
        private addAlreadySettingAttr(dataList: Array<UnitModel>, alreadySettingList: Array<UnitAlreadySettingModel>) {
            let mapAlreadySetting = _.reduce(alreadySettingList, function(hash, value) {
                let key = value['workplaceId'];
                hash[key] = value['isAlreadySetting'] == false ? null : value['isAlreadySetting'];
                return hash;
            }, {});
            this.updateTreeData(dataList, mapAlreadySetting);
        }
        
        /**
         * Update setting type for dataList
         */
        private updateTreeData(dataList: Array<UnitModel>, mapAlreadySetting: any, isAlreadySettingParent?: boolean,
            heirarchyCodeParent?: string) {
            let self = this;
            for (let unitModel of dataList) {
                
                // add workplaceId work place
                self.listWorkplaceId.push(unitModel.workplaceId);
                
                // set level
                unitModel.level = unitModel.heirarchyCode.length / 3;
                
                // set node text
                unitModel.nodeText = unitModel.code + ' ' + unitModel.name; 
                
                // set already setting 
                let isAlreadySetting = mapAlreadySetting[unitModel.workplaceId];
                unitModel.isAlreadySetting = isAlreadySetting;
                
                let heirarchyCode: string = null;
                // if it is saved already setting, will be save heirarchyCode that it is parent heirarchyCode.
                if (isAlreadySetting == true) {
                    heirarchyCode = unitModel.heirarchyCode;
                }
                
                // check work place child
                if (heirarchyCodeParent && unitModel.heirarchyCode.startsWith(heirarchyCodeParent)) {
                    
                    // if is work place child and it has not setting, it will set icon flag.
                    if (isAlreadySettingParent == true && typeof unitModel.isAlreadySetting != "boolean") {
                         unitModel.isAlreadySetting = false;
                    }
                    // if is not work place child and it has not setting, it will not set icon.
                    if (typeof isAlreadySettingParent != "boolean" && unitModel.isAlreadySetting == false) {
                        unitModel.isAlreadySetting = isAlreadySettingParent;
                    }
                }
                if (unitModel.childs.length > 0) {
                    this.updateTreeData(unitModel.childs, mapAlreadySetting,
                        isAlreadySetting ? isAlreadySetting : isAlreadySettingParent,
                        heirarchyCode ? heirarchyCode : heirarchyCodeParent);
                }
            }
        }
        
        /**
         * Filter data by level
         */
        private filterData() {
            let self = this;
            if (self.backupItemList().length > 0) {
                let subItemList = self.filterByLevel(self.backupItemList(), self.levelSelected(), new Array<UnitModel>());
                if (subItemList.length > 0) {
                    self.itemList(subItemList);
//                    self.selectedWorkplaceIds(self.isMultiple ? [subItemList[0].workplaceId] : subItemList[0]
//                        .workplaceId);
                }
            }
        }
        
        /**
         * Find list work place by base date
         */
        private reload() {
            let self = this;
            if (!self.baseDate()) {
                return;
            }
            service.findWorkplaceTree(self.baseDate()).done(function(res: Array<UnitModel>) {
                if (self.alreadySettingList) {
                    self.addAlreadySettingAttr(res, self.alreadySettingList());
                }
                self.itemList(res);
                self.backupItemList(res);
                
                // Filter data
                self.filterData();
            });
        }
        
        /**
         * Select all
         */
        private selectAll() {
            this.selectedWorkplaceIds(this.listWorkplaceId);
        }
        
        /**
         * Select all children
         */
        private selectSubParent() {
            let self = this;
            let listSubWorkplaceId: Array<string> = [];
            
            let listModel = self.findUnitModelByListWorkplaceId();
            self.findListSubWorkplaceId(listModel, listSubWorkplaceId);
            if (listSubWorkplaceId.length > 0) {
                self.selectedWorkplaceIds(listSubWorkplaceId);
            }
        }
        /**
         * Find UnitModel By ListWorkplaceId
         */
        private findUnitModelByListWorkplaceId(dataList?: Array<UnitModel>): Array<UnitModel>  {
            let self = this;
            let listModel: Array<UnitModel> = [];
            
            // get selected work place
            let listWorkplaceId = self.getSelectedWorkplace();
            
            for (let workplaceId of listWorkplaceId) {
                listModel = self.findUnitModelByWorkplaceId(dataList ? self.backupItemList() : self.itemList(),
                    workplaceId, listModel);
            }
            return listModel;
        }
        
        /**
         * Find list sub workplaceId of parent
         */
        private findListSubWorkplaceId(dataList: Array<UnitModel>, listSubWorkplaceId: Array<string>) {
            let self = this;
            for (let alreadySetting of dataList) {
                listSubWorkplaceId.push(alreadySetting.workplaceId);
                if (alreadySetting.childs && alreadySetting.childs.length > 0) {
                    this.findListSubWorkplaceId(alreadySetting.childs, listSubWorkplaceId);
                }
            }
        }
        
        /**
         * Select data for multiple or not
         */
        private selectData(option: TreeComponentOption, data: UnitModel) :any {
            if (this.isMultiple) {
                return [data.workplaceId];
            }
            return data.workplaceId;
        }
        
        /**
         * Get selected work place id
         */
        private getSelectedWorkplace() :any {
            if (this.isMultiple) {
                return this.selectedWorkplaceIds() ? this.selectedWorkplaceIds() : [];
            }
            return [this.selectedWorkplaceIds()];
        }
        
        /**
         * Find UnitModel by workplaceId
         */
        private findUnitModelByWorkplaceId(dataList: Array<UnitModel>, workplaceId: string,
            listModel: Array<UnitModel>) :Array<UnitModel> {
            let self = this;
            for (let item of dataList) {
                if (item.workplaceId == workplaceId) {
                    let modelString = JSON.stringify(listModel);
                    // Check item existed
                    if (modelString.indexOf(item.workplaceId) < 0) {
                        listModel.push(item); 
                    }
                }
                if (item.childs.length > 0) {
                    this.findUnitModelByWorkplaceId(item.childs, workplaceId, listModel);
                }
            }
            return listModel;
        }
        
        /**
         * Find selected row data
         */
        private findSelectionRowData(dataList: Array<UnitModel>, listRowData: Array<RowSelection>) {
            let self = this;
            for (let unitModel of dataList) {
                if (self.getSelectedWorkplace().contains(unitModel.workplaceId)) {
                    listRowData.push({
                        workplaceId: unitModel.workplaceId,
                        workplaceCode: unitModel.code
                    });
                }
                if (unitModel.childs.length > 0) {
                    this.findSelectionRowData(unitModel.childs, listRowData);
                }
            }
        }
        
        /**
         * Get ComId Search Box by multiple choice
         */
        private getComIdSearchBox(): string {
            if (this.isMultiple) {
                return 'multiple-tree-grid';
            }
            return 'single-tree-grid';
        }
        
        /**
         * Filter list work place follow selected level
         */
        private filterByLevel(dataList: Array<UnitModel>, level: number, listModel: Array<UnitModel>): Array<UnitModel> {
            let self = this;
            for (let item of dataList) {
                let newItem: any = {};
                if (item.level <= level) {
                    newItem = JSON.parse(JSON.stringify(item));
                    listModel.push(newItem);
                    if (level == 1) {
                        newItem.childs = [];
                    } else if (item.childs && item.childs.length > 0) {
                        let tmpModels = this.filterByLevel(newItem.childs, level, new Array<UnitModel>());
                        newItem.childs = tmpModels;
                    }
                }
            }
            return listModel;
        }
        
    }
     export module service {
        
        // Service paths.
        var servicePath = {
            findWorkplaceTree: "basic/company/organization/workplace/find",
        }
        
        /**
         * Find workplace list.
         */
        export function findWorkplaceTree(baseDate: Date): JQueryPromise<Array<UnitModel>> {
            return nts.uk.request.ajax('com', servicePath.findWorkplaceTree, { baseDate: baseDate });
        }
    }
}

/**
 * Defined Jquery interface.
 */
interface JQuery {

    /**
     * Nts tree component.
     */
    ntsTreeComponent(option: kcp.share.tree.TreeComponentOption): JQueryPromise<void>;
    
    /**
     * Get Data List
     */
    getDataList(): Array<kcp.share.tree.UnitModel>;
    
    /**
     * Get row selected 
     */
    getRowSelected(): Array<any>;
    
    /**
     * Focus component.
     */
    focusTreeGridComponent(): void;
}

(function($: any) {
    $.fn.ntsTreeComponent = function(option: kcp.share.tree.TreeComponentOption): JQueryPromise<void> {

        // Return.
        return new kcp.share.tree.TreeComponentScreenModel().init(this, option);;
    }
} (jQuery));