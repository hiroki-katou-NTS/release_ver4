module nts.uk.com.view.cas001.a.viewmodel {
    import alert = nts.uk.ui.dialog.alert;
    import getText = nts.uk.resource.getText;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import block = nts.uk.ui.block;
    import dialog = nts.uk.ui.dialog.info;
    export class ScreenModel {

        personRoleList: KnockoutObservableArray<PersonRole> = ko.observableArray([]);
        currentRole: KnockoutObservable<PersonRole> = ko.observable(new PersonRole(null));
        currentRoleId: KnockoutObservable<string> = ko.observable('');
        roundingRules: KnockoutObservableArray<any> = ko.observableArray([
            { code: 1, name: getText('Enum_PersonInfoPermissionType_YES') },
            { code: 0, name: getText('Enum_PersonInfoPermissionType_NO') }
        ]);
        itemListCbb: KnockoutObservableArray<any> = ko.observableArray([
            { code: 1, name: getText('Enum_PersonInfoAuthTypes_HIDE') },
            { code: 2, name: getText('Enum_PersonInfoAuthTypes_REFERENCE') },
            { code: 3, name: getText('Enum_PersonInfoAuthTypes_UPDATE') }
        ]);
        anotherSelectedAll: KnockoutObservable<number> = ko.observable(1);
        seftSelectedAll: KnockoutObservable<number> = ko.observable(1);
        currentCategoryId: KnockoutObservable<string> = ko.observable('');
        allowPersonRef: KnockoutObservable<number> = ko.observable(1);
        allowOtherRef: KnockoutObservable<number> = ko.observable(1);
        RoleCategoryList: KnockoutObservableArray<PersonRoleCategory> = ko.observableArray([]);
        checkboxSelectedAll: KnockoutObservable<boolean> = ko.observable(false);

        constructor() {
            let self = this;
            self.currentRoleId.subscribe(function(newRoleId) {

                if (newRoleId == "") {
                    return;
                }

                let newPersonRole = _.find(self.personRoleList(), (role) => { return role.roleId === newRoleId });

                service.getPersonRoleAuth(newPersonRole.roleId).done((result: IPersonRole) => {

                    newPersonRole.loadRoleCategoriesList(newPersonRole.roleId).done(() => {

                        newPersonRole.setRoleAuth(result);

                        self.currentRole(newPersonRole);
                        self.RoleCategoryList.valueHasMutated();
                        if (self.RoleCategoryList().length > 0) {

                            self.currentCategoryId("");

                            self.currentCategoryId(self.RoleCategoryList()[0].categoryId);

                        }
                        else {
                            dialog({ messageId: "Msg_364" });
                        }

                    });
                });

            });

            self.currentCategoryId.subscribe((categoryId) => {

                if (categoryId == "") {
                    return;
                }

                let newCategory = _.find(self.RoleCategoryList(), (roleCategory) => {

                    return roleCategory.categoryId === categoryId;

                });

                service.getCategoryAuth(self.currentRoleId(), categoryId).done((result: IPersonRoleCategory) => {

                    newCategory.loadRoleItems(self.currentRoleId(), categoryId).done(() => {

                        newCategory.setCategoryAuth(result);

                        self.currentRole().currentCategory(newCategory);

                    });
                });
            });

         

            self.checkboxSelectedAll.subscribe((newValue) => {


                if (!self.currentRole().currentCategory()) {
                    return;
                }

                let currentList = self.currentRole().currentCategory().roleItemList();

                _.forEach(currentList, (item) => {
                    item.isChecked = newValue;
                });
                $("#item_role_table_body").igGrid("option", "dataSource", currentList);
            });

            self.allowOtherRef.subscribe((newValue) => {

                let grid = $("#item_role_table_body");
                let ds = grid.igGrid("option", "dataSource");
                if (ds == null) {
                    return;
                }

                grid.ntsGrid(newValue == 0 ? "disableNtsControls" : "enableNtsControls", "otherAuth", "SwitchButtons");

                grid.ntsGrid(self.isDisableAll() ? "disableNtsControls" : "enableNtsControls", "isChecked", "CheckBox");

            });

            self.allowPersonRef.subscribe((newValue) => {

                let grid = $("#item_role_table_body");
                let ds = grid.igGrid("option", "dataSource");
                if (ds == null) {
                    return;
                }

                grid.ntsGrid(newValue == 0 ? "disableNtsControls" : "enableNtsControls", "selfAuth", "SwitchButtons");

                grid.ntsGrid(self.isDisableAll() ? "disableNtsControls" : "enableNtsControls", "isChecked", "CheckBox");


            });
        }


        changeAll(parrentId) {
            let self = this,
                currentList = self.currentRole().currentCategory().roleItemList(),
                selectItemList = _.find(currentList, (i) => {
                    return i.isChecked;
                });

            if (!selectItemList) {
                dialog({ messageId: "Msg_664" });
                return;
            }

            _.forEach(currentList, (item) => {
                if (item.isChecked) {
                    parrentId == 'anotherSelectedAll_auth' ? item.otherAuth = self.anotherSelectedAll() : item.selfAuth = self.seftSelectedAll();
                }
            });

            $("#item_role_table_body").igGrid("option", "dataSource", currentList);
        }

        OpenDModal() {

            let self = this;

            setShared('personRole', self.currentRole());

            block.invisible();

            nts.uk.ui.windows.sub.modal('/view/cas/001/d/index.xhtml', { title: '' }).onClosed(function(): any {

                if (!getShared('isCanceled')) {
                    self.reload().done(function() {

                        block.clear();

                    });
                }
            });
        }

        OpenCModal() {

            let self = this;

            setShared('personRole', self.currentRole());

            block.invisible();

            nts.uk.ui.windows.sub.modal('/view/cas/001/c/index.xhtml', { title: '' }).onClosed(function(): any {

                if (!getShared('isCanceled')) {
                    self.reload().done(function() {

                        block.clear();

                    });
                }
            });
        }

        InitializationItemGrid() {
            let self = this,
                switchString = `<div id='{0}_auth' class='selected_all_auth'
                                    data-bind="ntsSwitchButton: {
                                        options: itemListCbb,
                                        optionsValue:'code',
                                        optionsText: 'name',
                                        value: {0},
                                        enable: {1} }"></div>
                                <span id='selected_all_caret' class='caret-bottom outline'></span>`,
                checkboxString = `<div id='selected_all_ckb' data-bind='ntsCheckBox: { checked: checkboxSelectedAll, enable: !isDisableAll() }'></div>`,

                selectedAllString = nts.uk.text.format(switchString, 'anotherSelectedAll', '!!allowOtherRef()'),

                seftSelectedAllString = nts.uk.text.format(switchString, 'seftSelectedAll', '!!allowPersonRef()');

            $("#item_role_table_body").ntsGrid({
                features: [{ name: 'Resizing' },
                    {
                        name: "RowSelectors",
                        enableCheckBoxes: false,
                        enableRowNumbering: false,
                        rowSelectorColumnWidth: 0
                    }
                ],
                ntsFeatures: [{ name: 'CopyPaste' }],

                showHeader: true,

                width: '830px',

                height: '315px',

                dataSource: self.currentRole().currentCategory() === null ? null : self.currentRole().currentCategory().roleItemList(),

                primaryKey: 'personItemDefId',

                virtualization: true,

                virtualizationMode: 'continuous',

                virtualrecordsrender: function(evt, ui) {
                    if ($("#item_role_table_body").data("igGrid") === undefined) {
                        return;
                    }
                    var ds = ui.owner.dataSource.data();
                    $(ds)
                        .each(
                        function(index, el: any) {
                            let CheckboxCell = $("#item_role_table_body").igGrid("cellAt", 1, index);
                            let IsConfigCell = $("#item_role_table_body").igGrid("cellAt", 2, index);
                            let NameCell = $("#item_role_table_body").igGrid("cellAt", 3, index);

                            if (el.requiredAtr == '1') {
                                $(CheckboxCell).addClass('requiredCell');
                                $(IsConfigCell).addClass('requiredCell');
                                $(NameCell).addClass('requiredCell');
                            }
                            $(CheckboxCell).addClass('checkbox_float_left');

                        });
                },
                columns: [
                    { headerText: checkboxString, key: 'isChecked', dataType: 'boolean', width: '48px', ntsControl: 'Checkbox' },
                    { headerText: getText('CAS001_69'), key: 'setting', dataType: 'string', width: '48px', formatter: makeIcon },
                    { headerText: '', key: 'requiredAtr', dataType: 'string', width: '34px', hidden: true },
                    { headerText: '', key: 'personItemDefId', dataType: 'string', width: '34px', hidden: true },
                    { headerText: getText('CAS001_47'), key: 'itemName', dataType: 'string', width: '255px' },
                    { headerText: getText('CAS001_48') + selectedAllString, key: 'otherAuth', dataType: 'string', width: '232px', ntsControl: 'SwitchButtons' },
                    { headerText: getText('CAS001_52') + seftSelectedAllString, key: 'selfAuth', dataType: 'string', width: '232px', ntsControl: 'SwitchButtons' },
                ],
                ntsControls: [
                    { name: 'Checkbox', options: { value: 1, text: '' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox', enable: true },
                    {
                        name: 'SwitchButtons',
                        options: [{ value: '1', text: getText('Enum_PersonInfoAuthTypes_HIDE') },
                            { value: '2', text: getText('Enum_PersonInfoAuthTypes_REFERENCE') },
                            { value: '3', text: getText('Enum_PersonInfoAuthTypes_UPDATE') }],
                        optionsValue: 'value',
                        optionsText: 'text',
                        controlType: 'SwitchButtons',
                        enable: true
                    }
                ],

            });

        }

        reload(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred(),
                personRole = self.currentRole(),
                selectedId = self.currentCategoryId(),
                grid = $("#item_role_table_body");;

            personRole.loadRoleCategoriesList(personRole.roleId).done(function() {

                if (self.RoleCategoryList().length > 0) {

                    self.currentRole().currentCategory().loadRoleItems(self.currentRoleId(), selectedId).done(function() {

                    });

                }
                else {
                    dialog({ messageId: "Msg_217" });
                }

                dfd.resolve();

            });

            return dfd.promise();
        }
        start(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred();

            self.InitializationItemGrid();

            self.loadPersonRoleList().done(function() {

                let selectedId = self.currentRoleId() !== '' ? self.currentRoleId() : self.personRoleList()[0].roleId;

                self.currentRoleId('');

                if (self.personRoleList().length > 0) {

                    self.currentRoleId(selectedId);

                }
                else {

                    dialog({ messageId: "Msg_217" });

                }

                dfd.resolve();

            });

            return dfd.promise();
        }

        loadPersonRoleList(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred();

            block.invisible();

            service.getPersonRoleList().done(function(result: Array<IPersonRole>) {

                self.personRoleList.removeAll();

                _.forEach(result, function(iPersonRole: IPersonRole) {

                    self.personRoleList().push(new PersonRole(iPersonRole));

                });

                block.clear();

                dfd.resolve();
            });

            return dfd.promise();
        }

        saveData() {
            let self = this,

                command = self.createSaveCommand();

            block.invisible();

            service.savePersonRole(command).done(function() {

                dialog({ messageId: "Msg_15" }).then(function() {

                    self.reload().done(function() {

                        block.clear();

                    });
                });

            }).fail(function(res) {

                alert(res);

            });
        }
        createSaveCommand() {

            let self = this;

            return new PersonRoleCommand(self.currentRole());

        }

        isHistoryNotCons() {
            let self = this,
                currentCtg = self.currentRole().currentCategory();
            return (currentCtg.categoryType !== 1 && currentCtg.personEmployeeType === 2);
        }

        genCategoryTypeText() {
            let self = this,
                currentCtgType = self.currentRole().currentCategory().categoryType;

            switch (currentCtgType) {
                case 1: return getText('Enum_CategoryType_SINGLEINFO');
                case 2: return getText('Enum_CategoryType_MULTIINFO');
                case 3: return getText('Enum_CategoryType_CONTINUOUSHISTORY');
                case 4: return getText('Enum_CategoryType_NODUPLICATEHISTORY');
                case 5: return getText('Enum_CategoryType_DUPLICATEHISTORY');
                case 6: return getText('Enum_CategoryType_CONTINUOUSHISTORY');
                default: return '';

            }
        }

        isDisableAll() {
            let self = this;
            return self.allowPersonRef() === 0 && self.allowOtherRef() === 0;
        }
    }
    export interface IPersonRole {
        roleId: string;
        roleCode: string;
        roleName: string;
        allowMapBrowse: number;
        allowMapUpload: number;
        allowDocUpload: number;
        allowDocRef: number;
        allowAvatarUpload: number;
        allowAvatarRef: number;
    }
    export interface IPersonRoleCategory {
        categoryId: string;
        categoryName: string;
        setting: boolean;
        categoryType: number;
        personEmployeeType: number;
        allowPersonRef: number;
        allowOtherRef: number;
        allowOtherCompanyRef: number;
        selfPastHisAuth: number;
        selfFutureHisAuth: number;
        selfAllowDelHis: number;
        selfAllowAddHis: number;
        otherPastHisAuth: number;
        otherFutureHisAuth: number;
        otherAllowDelHis: number;
        otherAllowAddHis: number;
        selfAllowDelMulti: number;
        selfAllowAddMulti: number;
        otherAllowDelMulti: number;
        otherAllowAddMulti: number;

    }
    export interface IPersonRoleItem {
        personItemDefId: string;
        setting: boolean;
        requiredAtr: string;
        itemName: string;
        otherAuth: number;
        selfAuth: number;
    }

    export class PersonRole {
        roleId: string;
        roleCode: string;
        roleName: string;
        allowMapBrowse: KnockoutObservable<number>;
        allowMapUpload: KnockoutObservable<number>;
        allowDocUpload: KnockoutObservable<number>;
        allowDocRef: KnockoutObservable<number>;
        allowAvatarUpload: KnockoutObservable<number>;
        allowAvatarRef: KnockoutObservable<number>;
        // RoleCategoryList: KnockoutObservableArray<PersonRoleCategory> = ko.observableArray([]);
        currentCategory: KnockoutObservable<PersonRoleCategory> = ko.observable(null);
        constructor(param: IPersonRole) {
            let self = this;
            self.roleId = param ? param.roleId : '';
            self.roleCode = param ? param.roleCode : '';
            self.roleName = param ? param.roleName : '';
            self.allowMapBrowse = ko.observable(param ? param.allowMapBrowse : 0);
            self.allowMapUpload = ko.observable(param ? param.allowMapUpload : 0);
            self.allowDocUpload = ko.observable(param ? param.allowDocUpload : 0);
            self.allowDocRef = ko.observable(param ? param.allowDocRef : 0);
            self.allowAvatarUpload = ko.observable(param ? param.allowAvatarUpload : 0);
            self.allowAvatarRef = ko.observable(param ? param.allowAvatarRef : 0);

        }

        setRoleAuth(param: IPersonRole) {
            let self = this;
            self.allowMapBrowse = ko.observable(param ? param.allowMapBrowse : 0);
            self.allowMapUpload = ko.observable(param ? param.allowMapUpload : 0);
            self.allowDocUpload = ko.observable(param ? param.allowDocUpload : 0);
            self.allowDocRef = ko.observable(param ? param.allowDocRef : 0);
            self.allowAvatarUpload = ko.observable(param ? param.allowAvatarUpload : 0);
            self.allowAvatarRef = ko.observable(param ? param.allowAvatarRef : 0);
        }


        loadRoleCategoriesList(RoleId): JQueryPromise<any> {
            var self = this,
                dfd = $.Deferred();
            let screenModel = __viewContext['screenModel'];
            block.invisible();

            service.getCategoryRoleList(RoleId).done(function(result: Array<IPersonRoleCategory>) {

                screenModel.RoleCategoryList.removeAll();

                _.forEach(result, function(iPersonRoleCategory: IPersonRoleCategory) {

                    screenModel.RoleCategoryList.push(new PersonRoleCategory(iPersonRoleCategory));

                });

                block.clear();

                dfd.resolve();
            });
            return dfd.promise();
        }
    }

    export class PersonRoleCategory {

        categoryId: string;
        categoryName: string;
        categoryType: number;
        setting: boolean;
        personEmployeeType: number;
        allowOtherCompanyRef: KnockoutObservable<number>;
        selfPastHisAuth: KnockoutObservable<number>;
        selfFutureHisAuth: KnockoutObservable<number>;
        selfAllowDelHis: KnockoutObservable<number>;
        selfAllowAddHis: KnockoutObservable<number>;
        otherPastHisAuth: KnockoutObservable<number>;
        otherFutureHisAuth: KnockoutObservable<number>;
        otherAllowDelHis: KnockoutObservable<number>;
        otherAllowAddHis: KnockoutObservable<number>;
        selfAllowDelMulti: KnockoutObservable<number>;
        selfAllowAddMulti: KnockoutObservable<number>;
        otherAllowDelMulti: KnockoutObservable<number>;
        otherAllowAddMulti: KnockoutObservable<number>;
        roleItemList: KnockoutObservableArray<PersonRoleItem> = ko.observableArray([]);

        constructor(param: IPersonRoleCategory) {
            let self = this;
            self.categoryId = param ? param.categoryId : '';
            self.categoryName = param ? param.categoryName : '';
            self.categoryType = param ? param.categoryType : 0;
            self.setting = param ? param.setting : false;
            self.personEmployeeType = param ? param.personEmployeeType : 0;
            self.allowOtherCompanyRef = ko.observable(param ? param.allowOtherCompanyRef : 0);
            self.selfPastHisAuth = ko.observable(param ? param.selfPastHisAuth : 1);
            self.selfFutureHisAuth = ko.observable(param ? param.selfFutureHisAuth : 1);
            self.selfAllowDelHis = ko.observable(param ? param.selfAllowDelHis : 0);
            self.selfAllowAddHis = ko.observable(param ? param.selfAllowAddHis : 0);
            self.otherPastHisAuth = ko.observable(param ? param.otherPastHisAuth : 1);
            self.otherFutureHisAuth = ko.observable(param ? param.otherFutureHisAuth : 1);
            self.otherAllowDelHis = ko.observable(param ? param.otherAllowDelHis : 0);
            self.otherAllowAddHis = ko.observable(param ? param.otherAllowAddHis : 0);
            self.selfAllowDelMulti = ko.observable(param ? param.selfAllowDelMulti : 0);
            self.selfAllowAddMulti = ko.observable(param ? param.selfAllowAddMulti : 0);
            self.otherAllowDelMulti = ko.observable(param ? param.otherAllowDelMulti : 0);
            self.otherAllowAddMulti = ko.observable(param ? param.otherAllowAddMulti : 0);
        }

        setCategoryAuth(param: IPersonRoleCategory) {

            let self = this,
                screenModel = __viewContext['screenModel'];
            screenModel.allowPersonRef(param ? param.allowPersonRef : 0);
            screenModel.allowOtherRef(param ? param.allowOtherRef : 0);
            self.allowOtherCompanyRef = ko.observable(param ? param.allowOtherCompanyRef : 0);
            self.selfPastHisAuth = ko.observable(param ? param.selfPastHisAuth : 1);
            self.selfFutureHisAuth = ko.observable(param ? param.selfFutureHisAuth : 1);
            self.selfAllowDelHis = ko.observable(param ? param.selfAllowDelHis : 0);
            self.selfAllowAddHis = ko.observable(param ? param.selfAllowAddHis : 0);
            self.otherPastHisAuth = ko.observable(param ? param.otherPastHisAuth : 1);
            self.otherFutureHisAuth = ko.observable(param ? param.otherFutureHisAuth : 1);
            self.otherAllowDelHis = ko.observable(param ? param.otherAllowDelHis : 0);
            self.otherAllowAddHis = ko.observable(param ? param.otherAllowAddHis : 0);
            self.selfAllowDelMulti = ko.observable(param ? param.selfAllowDelMulti : 0);
            self.selfAllowAddMulti = ko.observable(param ? param.selfAllowAddMulti : 0);
            self.otherAllowDelMulti = ko.observable(param ? param.otherAllowDelMulti : 0);
            self.otherAllowAddMulti = ko.observable(param ? param.otherAllowAddMulti : 0);
        }

        loadRoleItems(roleId, CategoryId): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred(),
                grid = $("#item_role_table_body"),
                screenModel = __viewContext['screenModel'];



            block.invisible();

            service.getPersonRoleItemList(roleId, CategoryId).done(function(result: Array<IPersonRoleItem>) {

                self.roleItemList.removeAll();
                _.forEach(result, function(iPersonRoleItem: IPersonRoleItem) {
                    self.roleItemList.push(new PersonRoleItem(iPersonRoleItem));
                });

                if (self.roleItemList().length < 1) {
                    dialog({ messageId: "Msg_217" });
                }
                grid.igGrid("option", "dataSource", self.roleItemList());

                grid.ntsGrid(screenModel.allowOtherRef() == 0 ? "disableNtsControls" : "enableNtsControls", "otherAuth", "SwitchButtons");
                grid.ntsGrid(screenModel.allowPersonRef() == 0 ? "disableNtsControls" : "enableNtsControls", "selfAuth", "SwitchButtons");
                grid.ntsGrid(screenModel.isDisableAll() ? "disableNtsControls" : "enableNtsControls", "isChecked", "CheckBox");

                screenModel.RoleCategoryList.valueHasMutated();

                block.clear();

                dfd.resolve();

            });
            return dfd.promise();
        }
    }

    export class PersonRoleItem {
        personItemDefId: string;
        isChecked: boolean = false;
        setting: boolean;
        requiredAtr: string;
        itemName: string;
        otherAuth: number;
        selfAuth: number;

        constructor(param: IPersonRoleItem) {
            let self = this;
            self.personItemDefId = param ? param.personItemDefId : '';
            self.setting = param ? param.setting : false;
            self.requiredAtr = param ? param.requiredAtr : 'false';
            self.itemName = param ? param.itemName : '';
            self.otherAuth = this.setting === true ? param ? param.otherAuth : 1 : 1;
            self.selfAuth = this.setting === true ? param ? param.selfAuth : 1 : 1;
        }
    }

    export class PersonRoleCommand {
        roleId: string;
        roleCode: string;
        roleName: string;
        allowMapBrowse: number;
        allowMapUpload: number;
        allowDocUpload: number;
        allowDocRef: number;
        allowAvatarUpload: number;
        allowAvatarRef: number;
        currentCategory: PersonRoleCategoryCommand = null;
        constructor(param: PersonRole) {
            this.roleId = param.roleId;
            this.roleCode = param.roleCode;
            this.roleName = param.roleName;
            this.allowMapBrowse = param.allowMapBrowse();
            this.allowMapUpload = param.allowMapUpload();
            this.allowDocUpload = param.allowDocUpload();
            this.allowDocRef = param.allowDocRef();
            this.allowAvatarUpload = param.allowAvatarUpload();
            this.allowAvatarRef = param.allowAvatarRef();
            this.currentCategory = new PersonRoleCategoryCommand(param.currentCategory());
        }
    }
    export class PersonRoleCategoryCommand {
        categoryId: string;
        categoryName: string;
        categoryType: number;
        allowPersonRef: number;
        allowOtherRef: number;
        allowOtherCompanyRef: number;
        selfPastHisAuth: number;
        selfFutureHisAuth: number;
        selfAllowDelHis: number;
        selfAllowAddHis: number;
        otherPastHisAuth: number;
        otherFutureHisAuth: number;
        otherAllowDelHis: number;
        otherAllowAddHis: number;
        selfAllowDelMulti: number;
        selfAllowAddMulti: number;
        otherAllowDelMulti: number;
        otherAllowAddMulti: number;
        roleItemList: Array<PersonRoleItemCommand> = [];
        constructor(param: PersonRoleCategory) {

            let screenModel = __viewContext['screenModel'];

            this.categoryId = param.categoryId;
            this.categoryName = param.categoryName;
            this.categoryType = param.categoryType;
            this.allowPersonRef = screenModel.allowPersonRef();
            this.allowOtherRef = screenModel.allowOtherRef();
            this.allowOtherCompanyRef = param.allowOtherCompanyRef();
            this.selfPastHisAuth = param.selfPastHisAuth();
            this.selfFutureHisAuth = param.selfFutureHisAuth();
            this.selfAllowDelHis = param.selfAllowDelHis();
            this.selfAllowAddHis = param.selfAllowAddHis();
            this.otherPastHisAuth = param.otherPastHisAuth();
            this.otherFutureHisAuth = param.otherFutureHisAuth();
            this.otherAllowDelHis = param.otherAllowDelHis();
            this.otherAllowAddHis = param.otherAllowAddHis();
            this.selfAllowDelMulti = param.selfAllowDelMulti();
            this.selfAllowAddMulti = param.selfAllowAddMulti();
            this.otherAllowDelMulti = param.otherAllowDelMulti();
            this.otherAllowAddMulti = param.otherAllowAddMulti();
            for (let i of param.roleItemList()) {
                this.roleItemList.push(new PersonRoleItemCommand(i))
            }

        }

    }
    export class PersonRoleItemCommand {
        personItemDefId: string;
        otherAuth: number;
        selfAuth: number;
        constructor(param: PersonRoleItem) {
            this.personItemDefId = param.personItemDefId;
            this.otherAuth = param.otherAuth;
            this.selfAuth = param.selfAuth;
        }

    }
}

function makeIcon(value, row) {
    if (value == "true")
        return "●";
    return '';
}

