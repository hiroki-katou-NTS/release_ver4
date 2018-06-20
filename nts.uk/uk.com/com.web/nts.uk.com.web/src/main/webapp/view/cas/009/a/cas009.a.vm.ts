module cas009.a.viewmodel {
    import windows = nts.uk.ui.windows;
    import block = nts.uk.ui.block;
    import alertError = nts.uk.ui.dialog.alertError;

    import modal = windows.sub.modal;
    import text = nts.uk.resource.getText;
    import info = nts.uk.ui.dialog.info;
    import confirm = nts.uk.ui.dialog.confirm;
    import errors = nts.uk.ui.errors;

    import ccg026 = nts.uk.com.view.ccg026.component;

    import ComponentModelCCG025 = nts.uk.com.view.ccg025.a.component.viewmodel.ComponentModel;

    export class ScreenModel {
        selectedRole: Role = new Role();

        listRole: KnockoutObservableArray<IRole> = ko.observableArray([]);

        enumAuthen: KnockoutObservableArray<any> = ko.observableArray([
            { code: '0', name: text("CAS009_14") },
            { code: '1', name: text("CAS009_15") },
        ]);

        enumAllow: KnockoutObservableArray<any> = ko.observableArray([
            { code: true, name: text("CAS009_18") },
            { code: false, name: text("CAS009_19") },
        ]);

        enumRange: KnockoutObservableArray<EnumConstantDto> = ko.observableArray([]);

        component025: ComponentModelCCG025 = new ComponentModelCCG025({
            roleType: 8,
            multiple: false
        });

        enableDetail: KnockoutObservable<boolean> = ko.observable(true);

        constructor() {
            let self = this,
                role = self.selectedRole;

            _.extend(self, {
                listRole: self.component025.listRole
            });

            _.extend(role, {
                roleId: self.component025.currentCode
            });

            // subscribe and change data
            role.roleId.subscribe(rid => {
                let roles = ko.toJS(self.listRole),
                    exist: IRole = _.find(roles, (r: IRole) => _.isEqual(r.roleId, rid));

                if (exist) {
                    role.roleName(exist.name);
                    role.roleCode(exist.roleCode)

                    role.assignAtr(exist.assignAtr);
                    role.referFutureDate(exist.referFutureDate);
                    role.employeeReferenceRange(exist.employeeReferenceRange || 0);
                } else {
                    role.roleName('');
                    role.roleCode('');

                    role.assignAtr(0);
                    role.referFutureDate(false);
                    role.employeeReferenceRange(0);
                }

                // subscribe for focus and clear errors
                _.defer(() => {
                    if (_.isEmpty(rid)) {
                        role.roleCodeFocus(true);
                    } else {
                        role.roleNameFocus(true);
                    }

                    // clear all error
                    errors.clearAll();
                });
            });

            // call reload data
            self.start();
        }

        /** Start Page */
        start = () => {
            let self = this,
                role = self.selectedRole;

            block.invisible();

            // wait get options and permision
            $.when.apply($, [fetch.opt(), fetch.role.has()]).then(function() {
                let enumRange = arguments[0],
                    enableDetail = arguments[1];

                self.enumRange(enumRange || []);
                self.enableDetail(enableDetail);

                // get list role
                self.getListRole().done(() => {
                    let roles = ko.toJS(self.listRole);

                    if (!_.size(roles)) {
                        self.createNew();
                    } else {
                        role.roleId.valueHasMutated();
                    }

                }).always(() => {
                    block.clear();
                    errors.clearAll();
                });
            }, function() {
                let enumRange = arguments[0],
                    enableDetail = arguments[1];

                if (enumRange) {
                    alertError(enumRange);
                }

                if (enableDetail) {
                    alertError(enableDetail);
                }

                // clear all block
                block.clear();
                errors.clearAll();
            });
        }

        // Kinh dị:
        // Tạo 2 danh sách để lưu 1 giá trị.
        getListRole() {
            let self = this,
                dfd = $.Deferred();

            self.component025.startPage().done(() => {
                let roles: Array<IRole> = ko.toJS(self.listRole),
                    roleIds: Array<string> = _.map(roles, (x: IRole) => x.roleId);

                if (_.size(roleIds)) {
                    fetch.role.get(roleIds).done(resp => {
                        _.each(self.listRole(), (r: IRole) => {
                            let pinfo: IRole = _.find(resp, (o: IRole) => o.roleId == r.roleId);
                            if (pinfo) {
                                r.referFutureDate = pinfo.referFutureDate;
                            }
                        });
                        dfd.resolve();
                    });
                } else {
                    self.createNew();
                    dfd.resolve();
                }
            });

            return dfd.promise();
        }

        // create new mode
        createNew = () => {
            let self = this,
                role = self.selectedRole;

            role.roleId(undefined);
        }

        // open dialog
        openDialog = () => { modal("../b/index.xhtml").onClosed(() => { }); }

        // save change of role
        save = () => {
            let self = this,
                role: Role = self.selectedRole,
                command = ko.toJS(role);

            $(".nts-input").trigger("validate");
            if ($(".nts-input").ntsError("hasError")) {
                return;
            }

            block.invisible();

            // fix name
            _.extend(command, {
                name: command.roleName,
                createMode: _.isEmpty(command.roleId)
            });

            fetch.role.save(command).done((resp) => {
                fetch.permision.save({
                    roleId: resp,
                    functionAuthList: _.map(command.permisions, m => _.pick(m, ['functionNo', 'available']))
                }).done(() => {

                    info({ messageId: "Msg_15" });
                    self.getListRole().done(() => {
                        let exist: IRole = _.find(self.listRole(), o => o.roleCode == command.roleCode);

                        if (!exist) {
                            role.roleId(undefined);
                        } else {
                            role.roleId(exist.roleId);
                        }
                    }).always(() => {
                        block.clear();
                        errors.clearAll();
                    });
                });
            }).fail((error) => {
                alertError(error);
                block.clear();
                errors.clearAll();
            });
        }

        // remove selected role
        remove = () => {
            let self = this,
                roles: Array<IRole> = ko.toJS(self.listRole),
                role: IRole = ko.toJS(self.selectedRole),
                index: number = _.findIndex(roles, ["roleId", role.roleId]);

            index = _.min([_.size(roles) - 2, index]);

            if (!_.isNil(role.roleCode)) {
                confirm({ messageId: "Msg_18" }).ifYes(() => {
                    block.invisible();

                    fetch.role.remove(_.pick(role, ["roleId", "assignAtr"])).done(() => {
                        fetch.permision.remove(_.pick(role, ["roleId"])).done(() => {
                            info({ messageId: "Msg_16" });

                            self.getListRole().done(() => {
                                let roles: Array<IRole> = ko.toJS(self.listRole),
                                    selected: IRole = roles[index];

                                if (selected) {
                                    self.selectedRole.roleId(selected.roleId);
                                } else {
                                    self.selectedRole.roleId(roles[0].roleId);
                                }
                            }).always(() => {
                                block.clear();
                                errors.clearAll();
                            });
                        });

                    }).fail((error) => {
                        alertError(error);
                        block.clear();
                        nts.uk.ui.errors.clearAll();
                    });
                });
            }
        }
    }

    export interface EnumConstantDto {
        value: number;
        fieldName: string;
        localizedName: string;
    }

    export interface IRole {
        name: string;
        roleId: string;
        roleCode: string;
        assignAtr: number;
        createMode?: boolean;
        referFutureDate?: boolean;
        employeeReferenceRange: number;
        permisions: Array<IPermision>;
    }

    export class Role {
        roleId: KnockoutObservable<string> = ko.observable('');
        roleCode: KnockoutObservable<string> = ko.observable('');
        roleName: KnockoutObservable<string> = ko.observable('');

        assignAtr: KnockoutObservable<number> = ko.observable(1);
        referFutureDate: KnockoutObservable<boolean> = ko.observable(false);
        employeeReferenceRange: KnockoutObservable<number> = ko.observable(1);

        roleCodeFocus: KnockoutObservable<boolean> = ko.observable(true);
        roleNameFocus: KnockoutObservable<boolean> = ko.observable(false);

        permisions: KnockoutObservableArray<IPermision> = ko.observableArray([]);
    }

    export interface IPermision extends ccg026.IPermision { }
}