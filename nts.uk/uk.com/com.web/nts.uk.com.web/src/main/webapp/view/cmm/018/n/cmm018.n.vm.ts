module nts.uk.com.view.cmm018.n {
   import vmbase = cmm018.shr.vmbase;
export module viewmodel {
    export class ScreenModel {
        //Right table's properties.
        applicationType: KnockoutObservableArray<ItemModel>;
        columns: KnockoutObservableArray<NtsGridListColumn>;
        currentAppType: KnockoutObservableArray<number>;

        //Left filter area
        ccgcomponent: vmbase.GroupOption;

        // Options
        baseDate: KnockoutObservable<Date>;
        selectedEmployee: KnockoutObservableArray<vmbase.EmployeeSearchDto>;

        constructor() {
            var self = this;

            //Init right table.
            self.applicationType = ko.observableArray([]);

            self.columns = ko.observableArray([
                { headerText: '社員CD', key: 'code', width: 30, hidden: true},
                { headerText: '申請一覧', key: 'name', width: 200 }
            ]);

            self.currentAppType = ko.observableArray([]);

            //Init selectedEmployee
            self.selectedEmployee = ko.observableArray([]);
            self.baseDate = ko.observable(moment(new Date()).toDate());
        }

        loadGrid() {
            var self = this;
            self.ccgcomponent = {
                showEmployeeSelection: false, // 検索タイプ
                systemType: 2, // システム区分
                showQuickSearchTab: true, // クイック検索
                showAdvancedSearchTab: true, // 詳細検索
                showBaseDate: true, // 基準日利用
                showClosure: false, // 就業締め日利用
                showAllClosure: false, // 全締め表示
                showPeriod: false, // 対象期間利用
                periodFormatYM: false, // 対象期間精度

                /** Required parameter */
                baseDate: moment.utc().toISOString(), // 基準日
                periodStartDate: moment.utc("1900/01/01", "YYYY/MM/DD").toISOString(), // 対象期間開始日
                periodEndDate: moment.utc("9999/12/31", "YYYY/MM/DD").toISOString(), // 対象期間終了日
                inService: false, // 在職区分
                leaveOfAbsence: false, // 休職区分
                closed: false, // 休業区分
                retirement: false, // 退職区分

                /** Quick search tab options */
                showAllReferableEmployee: true, // 参照可能な社員すべて
                showOnlyMe: false, // 自分だけ
                showSameWorkplace: true, // 同じ職場の社員
                showSameWorkplaceAndChild: true, // 同じ職場とその配下の社員

                /** Advanced search properties */
                showEmployment: false, // 雇用条件
                showWorkplace: true, // 職場条件
                showClassification: false, // 分類条件
                showJobTitle: false, // 職位条件
                showWorktype: false, // 勤種条件
                isMutipleCheck: true,
                isInDialog:true,
                   /**  
                   * @param dataList: list employee returned from component.
                   * Define how to use this list employee by yourself in the function's body.
                   */
                 returnDataFromCcg001: function(data: vmbase.Ccg001ReturnedData){
                     self.selectedEmployee(data.listEmployee);            
                 }

            }

            $('#ccgcomponent').ntsGroupComponent(self.ccgcomponent);
        }

        getRightList() {
            let self = this;
            var dfd = $.Deferred();
            self.applicationType.removeAll();
            service.getRightList().done(function(data: any) {
                let items : ItemModel[] = [];
                items.push( new ItemModel(99,  "共通ルート"));
                _.forEach(data, function(value: any){
                    if(value.value !== 14){
                        items.push(new ItemModel(value.value, value.localizedName));
                    }
                    
                })
                
                self.applicationType(items);

                dfd.resolve();
            });
            return dfd.promise();
        }

        start() {
            let self = this;
            let dfd = $.Deferred();
            $.when(self.getRightList()).done(function() {
                dfd.resolve();
            });
            return dfd.promise();
        }
        //閉じるボタン
        closeDialog(){
            nts.uk.ui.windows.close();    
        }
        //Exceｌ出力
        printExcel(){
            var self = this;
            
            
            //対象社員を選択したかをチェックする(kiểm tra đã chọn nhân viên chưa?)
            //対象者未選択(chưa chọn nhân viên)
            if(self.selectedEmployee().length <= 0){
                nts.uk.ui.dialog.alertError({ messageId: "Msg_184"});
                return;
            }
            //出力対象申請を選択したかチェックする(check đã chọn đơn xin để xuất ra chưa?)
            //出力対象未選択(chưa chọn đối tượng output)
            if(self.currentAppType().length <= 0){
                nts.uk.ui.dialog.alertError({ messageId: "Msg_199"});
                return;    
            }
            //xuat file
            let data = new service.model.appInfor();
            data.baseDate = self.baseDate();
            
            //fix tam du lieu
            //data.lstEmpIds = self.selectedEmployee();
            var lstEmpIds : string[] = [];
            data.lstEmpIds = self.selectedEmployee();
            data.lstApps = self.currentAppType();
            var isCommon = self.findCommon();
            if(!nts.uk.util.isNullOrUndefined(isCommon)){
                data.rootAtr = 0;
            }else{
                data.rootAtr = 1;
            }
            nts.uk.ui.block.grayout();
            service.saveAsExcel(data).done(()=>{
                 nts.uk.ui.block.clear();   
            }).fail(function(res: any){
                nts.uk.ui.dialog.alertError(res.messageId);
                 nts.uk.ui.block.clear();
            });
        }
        findCommon(){
            let self = this;
            return _.find(self.currentAppType(), function(value){
                return value  == 99;    
            });    
        }
    }

        export class ItemModel {
            code: number;
            name: string;
            constructor(code: number, name: string) {
                this.code = code;
                this.name = name;
            }
        }
        
        export interface IItemModel {
            value: number;
            localizedName: string;
        }
    }
}
