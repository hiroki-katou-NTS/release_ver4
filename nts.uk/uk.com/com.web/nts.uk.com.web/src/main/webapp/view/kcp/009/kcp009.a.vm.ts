module kcp009.a.viewmodel {
    import ComponentOption = kcp009.viewmodel.ComponentOption;
    import EmployeeModel = kcp009.viewmodel.EmployeeModel;
    import SystemType = kcp009.viewmodel.SystemType;
    
    export class ScreenModel {
        empList: KnockoutObservableArray<EmployeeModel>;
        systemType: KnockoutObservable<number>;
        isDisplayOrganizationName: KnockoutObservable<boolean>;
        targetBtnText: string;

        systemReferenceList: KnockoutObservableArray<any>;
        selectedSystem: KnockoutObservable<number>;
        
        listComponentOption: ComponentOption;
        selectedItem: KnockoutObservable<string>;
        tabindex: number;
        constructor() {
            let self = this;
//            self.empList = ko.observableArray([]);
            self.empList = ko.observableArray([
            {id: '000426a2-181b-4c7f-abc8-6fff9f4f983a', code: '1234567890BA', businessName: 'A', workplaceName: 'Webメニューの設定', depName: '部門2'},
            {id: '90000000-0000-0000-0000-000000000001', code: '1234567890AE', businessName: 'B', workplaceName: 'テスト情報', depName: '部門3'},
            {id: '90000000-0000-0000-0000-000000000002', code: '000000000005', businessName: 'C', workplaceName: 'カレンダーの登録', depName: '部門4'},
            {id: '90000000-0000-0000-0000-000000000003', code: '000000000006', businessName: 'D', workplaceName: 'カレンダーの登録', depName: '部門5'},
            {id: '90000000-0000-0000-0000-000000000004', code: '000000000007', businessName: 'E', workplaceName: 'ベトナムレビュー', depName: '部門6'},
            {id: '90000000-0000-0000-0000-000000000005', code: '000000000008', businessName: 'F', workplaceName: '雇用名称', depName: '部門7'},
            {id: '90000000-0000-0000-0000-000000000009', code: '000000000009', businessName: 'G', workplaceName: '外部予算実績受入職場ID', depName: '部門8'},
            {id: '90000000-0000-0000-0000-000000000007', code: '000000000010', businessName: 'H', workplaceName: '算実績受入年月日', depName: '部門9'},
            {id: '90000000-0000-0000-0000-000000000009', code: '000000000011', businessName: 'I', workplaceName: '個人情報レイアウトコード', depName: '部門10'},
            {id: '90000000-0000-0000-0000-000000000010', code: '1234567890AD', businessName: 'K', workplaceName: '個人情報レイアウト名称', depName: '部門11'},
            {id: '90000000-0000-0000-0000-000000000011', code: '1234567890AC', businessName: 'J', workplaceName: '個人情報', depName: '部門12' },
            {id: '90000000-0000-0000-0000-000000000012', code: '000000000002', businessName: 'D', workplaceName: '名称', depName: '部門13'},
            {id: '90000000-0000-0000-0000-000000000013', code: '000000000013', businessName: 'C', workplaceName: '個人情報', depName: '部門14'}]);
            self.systemType = ko.observable(SystemType.EMPLOYMENT);
            self.isDisplayOrganizationName = ko.observable(true);
            self.isDisplayOrganizationName.subscribe(function(value: boolean) {
                self.reloadComponent();
            });
            self.targetBtnText = nts.uk.resource.getText("KCP009_3");
            self.selectedItem = ko.observable(null);
            self.systemReferenceList = ko.observableArray([
                { code: 1, name: 'Employment System' },
                { code: 2, name: 'Other Systems' },
            ]);
            self.selectedSystem = ko.observable(1);
            self.selectedSystem.subscribe(function(value: number) {
                if (value == 1) {
                    // System Type = Employment
                    self.systemType(SystemType.EMPLOYMENT);
                } else {
                    // Other System Type
                    self.systemType(SystemType.SALARY);
                }
                // Reload Component
                self.reloadComponent();
            });
            self.tabindex = 1;
            
            // Initial listComponentOption
            self.listComponentOption = {
                systemReference: self.systemType(),
                isDisplayOrganizationName: self.isDisplayOrganizationName(),
                employeeInputList: self.empList,
                targetBtnText: self.targetBtnText,
                selectedItem: self.selectedItem,
                tabIndex: self.tabindex
            };
            
        }
        
        // Reload component
        private reloadComponent() {
            let self = this;
            self.listComponentOption.systemReference = self.systemType();
            self.listComponentOption.isDisplayOrganizationName = self.isDisplayOrganizationName();
            self.listComponentOption.targetBtnText = self.targetBtnText;
            self.listComponentOption.employeeInputList(self.empList());
            // Load listComponent
            $('#emp-component').ntsLoadListComponent(self.listComponentOption);
        }
        
    }

}
