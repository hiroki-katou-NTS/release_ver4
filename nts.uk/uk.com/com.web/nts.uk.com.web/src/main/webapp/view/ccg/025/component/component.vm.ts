module nts.uk.com.view.ccg025.a.component {
    import getText = nts.uk.resource.getText;

    export interface Option {
        roleType?: number;
        multiple?: boolean;
        currentCode?: any;
        showEmptyItem?: boolean;
        tabindex?: number;
    }

    export module viewmodel {
        export class ComponentModel {
            listRole: KnockoutObservableArray<model.Role>;
            currentCode: any;
            private columns: KnockoutObservableArray<any>;
            private defaultOption: Option = {
                multiple: true,
                showEmptyItem: false
            }
            private setting: Option;
            private searchMode: string;

            constructor(option: Option) {
                let self = this;
                self.setting = $.extend({}, self.defaultOption, option);
                self.searchMode = (self.setting.multiple) ? "highlight" : "filter";
                self.listRole = ko.observableArray([]);
                if (self.setting.multiple) {
                    self.currentCode = ko.observableArray([]);
                    self.columns = ko.observableArray([
                        { headerText: getText("CCG025_3"), prop: 'roleId', width: 100, hidden: true },
                        { headerText: getText("CCG025_3"), prop: 'roleCode', width: 100 },
                        { headerText: getText("CCG025_4"), prop: 'name', width: 180 }
                    ]);
                } else {
                    self.currentCode = ko.observable("");
                    self.columns = ko.observableArray([
                        { headerText: getText("CCG025_3"), prop: 'roleId', width: 100, hidden: true },
                        { headerText: getText("CCG025_3"), prop: 'roleCode', width: 100 },
                        { headerText: getText("CCG025_4"), prop: 'name', width: 233 }
                    ]);
                }

            }

            /** functiton start page */
            startPage(): JQueryPromise<any> {
                let self = this;
                return self.getListRoleByRoleType(self.setting.roleType);
            }

            /** Get list Role by Type */
            private getListRoleByRoleType(roleType: number): JQueryPromise<Array<model.Role>> {
                let self = this;
                let dfd = $.Deferred();
                service.getListRoleByRoleType(roleType).done((data: Array<model.Role>) => {
                    data = _.orderBy(data, ['assignAtr', 'roleCode'], ['asc', 'asc']);
                    self.listRole(data);
                    self.addEmptyItem();

                    // Select item base on param code
                    if (nts.uk.util.isNullOrUndefined(self.setting.currentCode)) {
                        self.selectFirstItem();
                    } else {
                        self.currentCode(self.setting.currentCode);
                    }
                    dfd.resolve(data);
                }).fail(function(res: any) {
                    dfd.reject();
                    nts.uk.ui.dialog.alertError(res.message).then(() => {
                        nts.uk.ui.block.clear();
                    });
                });
                return dfd.promise();
            }

            private addEmptyItem(): void {
                var self = this;
                if (self.setting.showEmptyItem && !self.setting.multiple) {
                    self.listRole.unshift(new model.Role("", "", 0, 0, "選択なし", "", 0, ""));
                }
            }

            /** Select first item */
            private selectFirstItem(): void {
                var self = this;
                if (self.listRole().length > 0) {
                    self.currentCode(self.listRole()[0].roleId);
                }
            }

        }//end screenModel
    }//end viewmodel

    //module model
    export module model {

        export interface IRole {
            roleId: string;
            roleCode: string;
            roleType: number;
            employeeReferenceRange: number;
            name: string;
            contractCode: string;
            assignAtr: number;
            companyId: string;
        }
        
        /** class Role */
        export class Role {
            roleId: string;
            roleCode: string;
            roleType: number;
            employeeReferenceRange: number;
            name: string;
            contractCode: string;
            assignAtr: number;
            companyId: string;
            constructor(roleId: string, roleCode: string,
                roleType: number, employeeReferenceRange: number, name: string,
                contractCode: string, assignAtr: number, companyId: string) {
                this.roleId = roleId;
                this.roleCode = roleCode;
                this.roleType = roleType;
                this.employeeReferenceRange = employeeReferenceRange;
                this.name = name;
                this.contractCode = contractCode;
                this.assignAtr = assignAtr;
                this.companyId = companyId;
            }
        }//end class Role


    }//end module model

}//end module