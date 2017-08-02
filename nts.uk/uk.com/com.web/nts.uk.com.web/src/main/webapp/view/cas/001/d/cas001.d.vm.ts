module nts.uk.com.view.cas001.d.viewmodel {
    import windows = nts.uk.ui.windows;
    import errors = nts.uk.ui.errors;
    import resource = nts.uk.resource;

    export class ScreenModel {
        roleList: KnockoutObservableArray<any>;
        columns: KnockoutObservableArray<NtsGridListColumn>;
        currentRoleCode: KnockoutObservable<string>;
        companyCode: KnockoutObservable<string>;
        itemSetting: KnockoutObservableArray<any>;
        selectItemCode: any;

        constructor() {
            var self = this;
            self.init();
            $("#grid").igGrid({
                columns: [
                    {
                        headerText: "<input id='selfAuth' type='checkbox'  tabindex='2' >他人</input>", key: 'selfAuth',
                         width: "80px", height: "40px",formatter: function(evt: any, row: any) {
                            let cb = $("<input  class = 'checkRow selfAuth' type='checkbox' tabindex='4'/>");
                            cb.attr("data-id", row.roleCode);
                            return cb[0].outerHTML;
                        }
                    },
                    {
                        headerText: "<input id='otherAuth' type='checkbox'  tabindex='3'>本人</input>", key: 'otherAuth',
                         width: "80px", height: "40px", formatter: function(evt: any, row: any) {
                            let cb = $("<input  class = 'checkRow otherAuth' type='checkbox' tabindex='5'/>");
                            cb.attr("data-id", row.roleCode);
                            return cb[0].outerHTML;
                        }
                    },
                    { headerText: "コード", key: "roleCode", dataType: "string", width: "100px", height: "40px", hidden: true },
                    { headerText: "カテゴリ名", key: "roleName", dataType: "string", width: "50px", height: "40px" },
                    { headerText: '説明', key: 'description', width: "35px", hidden: true, height: "40px" },

                ],
                primaryKey: 'roleCode',
                autoGenerateColumns: false,
                autoCommit: true,
                dataSource: self.roleList(),
                width: "300px",
                height: "310px",
                features: [
                    {
                        name: "Updating",
                        enableAddRow: false,
                        editMode: "row",
                        enableDeleteRow: false,
                        columnSettings: [
                            { columnKey: "selfAuth", readOnly: true },
                            { columnKey: "otherAuth", readOnly: true },
                            { columnKey: "roleCode", readOnly: true },
                            { columnKey: "roleName", readOnly: true },
                            { columnKey: "description", readOnly: true },
                        ]
                    }]
            });
            $(document).on("click", "#selfAuth", function(evt, ui) {
                $("#grid").igGridUpdating("endEdit");
                $("#grid").find("tr").each((index, element) => {
                    // change state of all checkbox delete
                    $(element).find(".selfAuth").prop("checked", $("#selfAuth").prop("checked")).trigger("change");
                });
            });
            $(document).on("click", "#otherAuth", function(evt, ui) {
                $("#grid").igGridUpdating("endEdit");
                $("#grid").find("tr").each((index, element) => {
                    // change state of all checkbox delete
                    $(element).find(".otherAuth").prop("checked", $("#otherAuth").prop("checked")).trigger("change");
                });
            });

        }
        init(): void {
            var self = this;
            self.roleList = ko.observableArray([new PersonRole({ roleCode: "1", roleName: 'A2', selfAuth: true, otherAuth: true }), new PersonRole({ roleCode: '2', roleName: 'B', selfAuth: true, otherAuth: false })]);
            self.currentRoleCode = ko.observable('');
            self.columns = ko.observableArray([
                { headerText: 'コード', key: 'roleCode', width: 100, hidden: true },
                { headerText: '他人', key: 'selfAuth', width: 50, template: "<input type='checkbox' checked='${selfAuth}'/>" },
                { headerText: '本人', key: 'otherAuth', width: 50, template: "<input type='checkbox' checked='${otherAuth}'/>" },
                { headerText: 'カテゴリ名', key: 'roleName', width: 180 },
                { headerText: '説明', key: 'description', width: 50, hidden: true }
            ]);
            self.companyCode = ko.observable('');
            self.itemSetting = ko.observableArray([
                { code: '1', name: '非表示' },
                { code: '2', name: '参照のみ' },
                { code: '3', name: '更新' }
            ]);
            self.selectItemCode = ko.observable(1);

        }
        creatCategory() {
            windows.close();
        }
        closeDialog() {
            windows.close();
        }
    }
    interface IPersonRole {
        roleCode: string;
        roleName: string;
        selfAuth: boolean;
        otherAuth: boolean;
    }
    export class PersonRole {
        roleCode: string;
        roleName: string;
        selfAuth: boolean;
        otherAuth: boolean;
        description: string;

        constructor(params: IPersonRole) {
            this.roleCode = params.roleCode;
            this.roleName = params.roleName;
            this.selfAuth = params.selfAuth;
            this.otherAuth = params.otherAuth;
            this.description = this.roleCode + this.roleName;
        }
    }
    interface ICategoryAuth {
        categoryCode: string;
        categoryName: string;
        isSetting: boolean;
    }
    class CategoryAuth {
        categoryCode: string;
        categoryName: string;
        isSetting: boolean;
        constructor(param: ICategoryAuth) {
            this.categoryCode = param.categoryCode;
            this.categoryName = param.categoryName;
            this.isSetting = param.isSetting || false;
        }
    }
}