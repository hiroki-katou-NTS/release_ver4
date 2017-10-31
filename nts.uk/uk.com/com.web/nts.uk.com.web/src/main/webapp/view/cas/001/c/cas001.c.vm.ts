module nts.uk.com.view.cas001.c.viewmodel {
    import error = nts.uk.ui.errors;
    import text = nts.uk.resource.getText;
    import close = nts.uk.ui.windows.close;
    import dialog = nts.uk.ui.dialog;
    import getShared = nts.uk.ui.windows.getShared;
    import setShared = nts.uk.ui.windows.setShared;

    export class ScreenModel {
        roleList: KnockoutObservableArray<any> = ko.observableArray([]);
        roleCodeArray = [];
        roleCopy: KnockoutObservable<PersonRole> = ko.observable(getShared('personRole'));
        isCanceled : boolean ;

        constructor() {
            var self = this;

            self.roleList.subscribe(data => {
                if (data) {
                    $("#roles").igGrid("option", "dataSource", data);
                }else{
                    $("#roles").igGrid("option", "dataSource", []);
                }
            });

            self.start();
        }

        start() {
            let self = this;
            self.roleList.removeAll();

            service.getAllPersonRole().done(function(data: Array<any>) {
                if (data.length > 0) {
                    _.each(data, function(c) {
                        self.roleList.push(new PersonRole(c.roleId, c.roleCode, c.roleName));
                    });
                }else{
                   dialog.alert(text('CAS001_7')); 
                }
            });
        }

        createCategory() {
            let data = (__viewContext["viewModel"].roleList());
            let self = this;
            self.roleCodeArray = [];
            _.find(data, function(role:PersonRole) {
                if (role.selected === true) {
                    self.roleCodeArray.push(role.roleId);
                }
            });
            if (self.roleCodeArray.length > 0) {
               dialog.confirm({messageId: "Msg_64"}).ifYes(() => {
                    let roleObj = { roleIdDestination: self.roleCopy().roleId, roleIds: self.roleCodeArray };
                    service.update(roleObj).done(function(obj) {
                        dialog.info({ messageId: "Msg_20" }).then(function() {
                            close();
                        });
                    }).fail(function(res: any) {
                        dialog.alert(res.message);
                    })

                }).ifCancel(function() {
                })
            }else{
                dialog.alert({messageId: "Msg_365"});
            }
            self.isCanceled = false;
            setShared('isCanceled', self.isCanceled);

        }
        
        closeDialog() {
            let self = this;
            self.isCanceled = true;
            setShared('isCanceled', self.isCanceled);
            close();
        }
    }

    export class PersonRole {
        check: boolean = false;
        roleId: string;
        roleCode: string;
        roleName: string;
        constructor(roleId: string, roleCode: string, roleName: string) {
            this.roleId = roleId;
            this.roleCode = roleCode;
            this.roleName = roleName;
        }
    }
}