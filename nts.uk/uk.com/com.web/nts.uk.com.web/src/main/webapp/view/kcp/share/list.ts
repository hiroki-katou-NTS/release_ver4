module kcp.share.list {
    export interface UnitModel {
        code: string;
        name?: string;
        companyName?: string;
        isAlreadySetting?: boolean;
    }
    
    export interface ComponentOption {
        isShowAlreadySet: boolean;
        isMultiSelect: boolean;
        listType: ListType;
        selectedCode: KnockoutObservable<any>;
        alreadySettingList?: KnockoutObservableArray<UnitModel>;
    }
    
    export class ListType {
        static EMPLOYMENT = 1;
        static JOB_TITLE = 3;
        static EMPLOYEE = 4;
    }
    
    export class ListComponentScreenModel {
        itemList: KnockoutObservableArray<UnitModel>;
        selectedCodes: KnockoutObservable<any>;
        listComponentColumn: Array<any>;
        isMultiple: boolean;
        
        constructor() {
            this.itemList = ko.observableArray([]);
            this.listComponentColumn = [];
            this.isMultiple = false;
            this.selectedCodes = ko.observable(null);
            
            // Setup list column.
            this.listComponentColumn.push({headerText: 'コード', prop: 'code', width: 50});
            this.listComponentColumn.push({headerText: '名称', prop: 'name', width: 170});
        }
        /**
         * Init component.
         */
        public init($input: JQuery, data: ComponentOption) {
            var self = this;
            self.isMultiple = data.isMultiSelect;
            self.selectedCodes(data.selectedCode());
            self.selectedCodes.subscribe((newVal: any) => {
                data.selectedCode(newVal);
            })
            
            // With Employee list, add column company name.
            if (data.listType == ListType.EMPLOYEE) {
                self.listComponentColumn.push({headerText: '所属', prop: 'companyName', width: 50});
            }
            
            // If show Already setting.
            if (data.isShowAlreadySet) {
                // Add row already setting.
                self.listComponentColumn.push({
                    headerText: '設定済', prop: 'isAlreadySetting', width: 30,
                    formatter: function(isAlreadySet: string) {
                        if (isAlreadySet == 'true') {
                            return '<div style="text-align: center;"><i class="icon icon-dot"></i></div>';
                        }
                        return '';
                    }
                });
            }
            
            // Find data list.
            this.findDataList(data.listType).done(function(dataList: Array<UnitModel>) {
                
                // Map already setting attr to data list.
                if (data.isShowAlreadySet) {
                    var alreadyListCode = data.alreadySettingList().filter(item => item.isAlreadySetting).map(item => item.code);
                    dataList.forEach((item => {
                        item.isAlreadySetting = alreadyListCode.indexOf(item.code) > -1;
                    }))
                }
                
                // Init component.
                self.itemList(dataList);
                $input.appendTo($('body'))
                    .load(nts.uk.request.location.appRoot.rawUrl + '/view/kcp/share/list.xhtml', function() {
                        ko.cleanNode($input[0]);
                        ko.applyBindings(self, $input[0]);
                    })
            });
        }
        
        public findDataList(listType: ListType):JQueryPromise<Array<UnitModel>> {
            switch(listType) {
                case ListType.EMPLOYMENT:
                    return service.findEmployments();
                case ListType.JOB_TITLE:
                    return service.findJobTitles();
                case ListType.EMPLOYEE:
                    return service.findEmployees();
            }
        }
        
    }
    
    /**
     * Service,
     */
    export module service {
        
        // Service paths.
        var servicePath = {
            findEmployments: "basic/company/organization/employment/findAll/",
            findJobTitles: '???',
            findEmployees: '???'
        }
        
        /**
         * Find Employment list.
         */
        export function findEmployments(): JQueryPromise<Array<UnitModel>> {
            var dfd = $.Deferred<any>();
            return nts.uk.request.ajax(servicePath.findEmployments);
        }
        
        export function findJobTitles(): JQueryPromise<Array<UnitModel>> {
            var dfd = $.Deferred<any>();
            dfd.resolve();
            return dfd.promise();
        }
        
        export function findEmployees(): JQueryPromise<Array<UnitModel>> {
            var dfd = $.Deferred<any>();
            dfd.resolve();
            return dfd.promise();
        }
        
    }
}

/**
 * Defined Jquery interface.
 */
interface JQuery {

    /**
     * Nts list component.
     */
    ntsListComponent(option: kcp.share.list.ComponentOption);
}

(function($: any) {
    $.fn.ntsListComponent = function(option: kcp.share.list.ComponentOption): any {

        // Init nts file upload. 
        var widget = new kcp.share.list.ListComponentScreenModel().init(this, option);

        // Return.
        return widget;
    }
} (jQuery));