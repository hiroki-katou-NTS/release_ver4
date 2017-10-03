module nts.uk.com.view.cmm018.n {
export module viewmodel {
    export class ScreenModel {
        //Right table's properties.
        applicationType: KnockoutObservableArray<ItemModel>;
        columns: KnockoutObservableArray<NtsGridListColumn>;
        currentAppType: KnockoutObservableArray<any>;

        //Left filter area
        ccgcomponent: GroupOption;

        // Options
        baseDate: KnockoutObservable<Date>;
        selectedEmployee: KnockoutObservableArray<EmployeeSearchDto>;

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
            self.baseDate = ko.observable(new Date());
        }

        loadGrid() {
            var self = this;
            self.ccgcomponent = {
                baseDate: self.baseDate,
                //Show/hide options
                isQuickSearchTab: true,
                isAdvancedSearchTab: true,
                isAllReferableEmployee: true,
                isOnlyMe: true,
                isEmployeeOfWorkplace: true,
                isEmployeeWorkplaceFollow: true,
                isMutipleCheck: true,
                isSelectAllEmployee: false,
                /**
                * @param dataList: list employee returned from component.
                * Define how to use this list employee by yourself in the function's body.
                */
                onSearchAllClicked: function(dataList: EmployeeSearchDto[]) {
                    self.selectedEmployee(dataList);
                },
                onSearchOnlyClicked: function(data: EmployeeSearchDto) {
                    var dataEmployee: EmployeeSearchDto[] = [];
                    dataEmployee.push(data);
                    self.selectedEmployee(dataEmployee);
                },
                onSearchOfWorkplaceClicked: function(dataList: EmployeeSearchDto[]) {
                    self.selectedEmployee(dataList);
                },
                onSearchWorkplaceChildClicked: function(dataList: EmployeeSearchDto[]) {
                    self.selectedEmployee(dataList);
                },
                onApplyEmployee: function(dataEmployee: EmployeeSearchDto[]) {
                    self.selectedEmployee(dataEmployee);
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
                items.push( new ItemModel("",  "共通ルート"));
                _.forEach(data, function(value: any){
                    items.push(new ItemModel(value.value, value.localizedName));
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
            //tam thoi comment de test
//            if(self.selectedEmployee().length <= 0){
//                nts.uk.ui.dialog.alertError({ messageId: "Msg_184"});
//                return;
//            }
            //出力対象申請を選択したかチェックする(check đã chọn đơn xin để xuất ra chưa?)
            //出力対象未選択(chưa chọn đối tượng output)
            if(self.currentAppType().length <= 0){
                nts.uk.ui.dialog.alertError({ messageId: "Msg_199"});
                return;    
            }
            //xuat file
            var data = new service.model.appInfor();
            data.baseDate = self.baseDate();
            
            
            //fix tam du lieu
            //data.lstEmpIds = self.selectedEmployee();
            var lstEmpIds : string[] = [];
            
            lstEmpIds.push("000426a2-181b-4c7f-abc8-6fff9f4f983a");
            lstEmpIds.push("90000000-0000-0000-0000-000000000001");
            lstEmpIds.push("90000000-0000-0000-0000-000000000002");
            lstEmpIds.push("90000000-0000-0000-0000-000000000003");
            lstEmpIds.push("90000000-0000-0000-0000-000000000004");
            lstEmpIds.push("90000000-0000-0000-0000-000000000005");
            lstEmpIds.push("90000000-0000-0000-0000-000000000006");
            lstEmpIds.push("90000000-0000-0000-0000-000000000007");
            lstEmpIds.push("90000000-0000-0000-0000-000000000008");
            lstEmpIds.push("90000000-0000-0000-0000-000000000009");
            lstEmpIds.push("90000000-0000-0000-0000-000000000010");
            lstEmpIds.push("90000000-0000-0000-0000-000000000011");
            lstEmpIds.push("90000000-0000-0000-0000-000000000012");
            lstEmpIds.push("90000000-0000-0000-0000-000000000013");
            lstEmpIds.push("90000000-0000-0000-0000-000000000014");
            lstEmpIds.push("90000000-0000-0000-0000-000000000015");
            lstEmpIds.push("90000000-0000-0000-0000-000000000016");
            data.lstEmpIds = lstEmpIds;
            var isCommon = _.find(self.currentAppType(), function(value){
                return value  === "";    
            })
            if(isCommon !== undefined || isCommon !== null){
                data.rootAtr = 0;
            }else{
                data.rootAtr = 1;
            }
            data.lstApps = self.currentAppType();
            service.getInforRoot(data).done(function(){
                
            }).fail(function(res: any){
                nts.uk.ui.dialog.alertError(res.messageId);
            })
        }
    }

    export class ItemModel {
        code: string;
        name: string;
        constructor(code: string, name: string) {
            this.code = code;
            this.name = name;
        }
    }
    
//    export interface IItemModel {
//        value: string;
//        localizedName: string;
//    }
    
    export interface EmployeeSearchDto {
        employeeId: string;

        employeeCode: string;

        employeeName: string;

        workplaceCode: string;

        workplaceId: string;

        workplaceName: string;
    }

    export interface GroupOption {
        baseDate?: KnockoutObservable<Date>;
        // クイック検索タブ
        isQuickSearchTab: boolean;
        // 参照可能な社員すべて
        isAllReferableEmployee: boolean;
        //自分だけ
        isOnlyMe: boolean;
        //おなじ部門の社員
        isEmployeeOfWorkplace: boolean;
        //おなじ＋配下部門の社員
        isEmployeeWorkplaceFollow: boolean;


        // 詳細検索タブ
        isAdvancedSearchTab: boolean;
        //複数選択 
        isMutipleCheck: boolean;

        //社員指定タイプ or 全社員タイプ
        isSelectAllEmployee: boolean;

        onSearchAllClicked: (data: EmployeeSearchDto[]) => void;

        onSearchOnlyClicked: (data: EmployeeSearchDto) => void;

        onSearchOfWorkplaceClicked: (data: EmployeeSearchDto[]) => void;

        onSearchWorkplaceChildClicked: (data: EmployeeSearchDto[]) => void;

        onApplyEmployee: (data: EmployeeSearchDto[]) => void;
    }
}
    }
