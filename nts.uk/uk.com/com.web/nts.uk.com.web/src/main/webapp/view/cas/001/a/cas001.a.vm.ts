module nts.uk.com.view.cas001.a.viewmodel {
    import alert = nts.uk.ui.dialog.alert;
    import getText = nts.uk.resource.getText;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import dialog = nts.uk.ui.dialog.info;
    import ccg = nts.uk.com.view.ccg025.a;
    import model = nts.uk.com.view.ccg025.a.component.model;
    import color = nts.uk.ui.jqueryExtentions.ntsGrid.color;
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
        roleCategoryList: KnockoutObservableArray<PersonRoleCategory> = ko.observableArray([]);
        checkboxSelectedAll: KnockoutObservable<boolean> = ko.observable(false);
        component: ccg.component.viewmodel.ComponentModel = new ccg.component.viewmodel.ComponentModel({
            roleType: 8,
            multiple: false
        });
        listRole: KnockoutObservableArray<PersonRole> = ko.observableArray([]);

        constructor() {
            let self = this;

            self.component.columns([
                { headerText: getText("CCG025_3"), prop: 'roleId', width: 50, hidden: true },
                { headerText: getText("CCG025_3"), prop: 'roleCode', width: 50 },
                { headerText: getText("CCG025_4"), prop: 'name', width: 150 }
            ]);

            self.component.currentCode.subscribe(function(newRoleId) {
                if (self.personRoleList().length < 1) {
                    return;
                }
                self.currentRoleId(newRoleId);
            });

            self.currentRoleId.subscribe(function(newRoleId) {

                if (newRoleId == "" || self.personRoleList().length < 1) {
                    return;
                }


                let newPersonRole = _.find(self.personRoleList(), (role) => { return role.roleId === newRoleId });


                service.getPersonRoleAuth(newRoleId).done((result: IPersonRole) => {

                    newPersonRole.setRoleAuth(result);

                    self.currentRole(newPersonRole);
                });

                newPersonRole.loadRoleCategoriesList(newRoleId, false).done(() => {
                    if (!self.currentCategoryId()) {
                        newPersonRole.setCtgSelectedId(self.roleCategoryList());
                    }


                });

            });

            self.currentCategoryId.subscribe((categoryId) => {

                if (!categoryId) {
                    return;
                }

                let newCategory = _.find(self.roleCategoryList(), (roleCategory) => {

                    return roleCategory.categoryId === categoryId;

                });

                service.getCategoryAuth(self.currentRoleId(), categoryId).done((result: IPersonRoleCategory) => {

                    newCategory.loadRoleItems(self.currentRoleId(), categoryId).done(() => {

                        newCategory.setCategoryAuth(result);

                        self.currentRole().currentCategory(newCategory);

                    }).always(() => {

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
            });

            self.allowOtherRef.subscribe((newValue) => {

                let grid = $("#item_role_table_body");
                let ds = grid.igGrid("option", "dataSource");
                if (ds == null) {
                    return;
                }

                grid.ntsGrid(newValue == 0 ? "disableNtsControls" : "enableNtsControls", "otherAuth", "SwitchButtons");

                grid.ntsGrid(self.isDisableAll() ? "disableNtsControls" : "enableNtsControls", "isChecked", "CheckBox", true);

                if (newValue == 0) {
                    $("#anotherSelectedAll_auth > button.nts-switch-button").prop('disabled', true);
                } else {
                    $("#anotherSelectedAll_auth > button.nts-switch-button").prop('disabled', false);
                }
            });

            self.allowPersonRef.subscribe((newValue) => {

                let grid = $("#item_role_table_body");
                let ds = grid.igGrid("option", "dataSource");
                if (ds == null) {
                    return;
                }

                grid.ntsGrid(newValue == 0 ? "disableNtsControls" : "enableNtsControls", "selfAuth", "SwitchButtons");

                grid.ntsGrid(self.isDisableAll() ? "disableNtsControls" : "enableNtsControls", "isChecked", "CheckBox", true);

                if (newValue == 0) {
                    $("#seftSelectedAll_auth > button.nts-switch-button").prop('disabled', true);
                } else {
                    $("#seftSelectedAll_auth > button.nts-switch-button").prop('disabled', false);
                }
            });

        }


        changeAll(parrentId, changeValue) {
            let self = this,
                grid = $("#item_role_table_body"),
                currentList = $("#item_role_table_body").ntsGrid("updatedCells"),
                itemCheckLst = _.filter(currentList, { columnKey: "isChecked", value: true }),
                changeVal = changeValue < 1 ? 1 : changeValue > 3 ? 3 : changeValue,
                itemLst2E = self.currentRole().currentCategory().itemLst2E;

            if (itemCheckLst.length <= 0) {
                dialog({ messageId: "Msg_664" });
                return;
            }

            _.forEach(itemCheckLst, (item) => {
                if (item.value) {
                    grid.ntsGrid("updateRow", item.rowId, parrentId === 'anotherSelectedAll_auth' ? { otherAuth: String(changeVal) } : { selfAuth: String(changeVal) });
                }
            });

            _.forEach(itemLst2E, (item2E) => {
                _.forEach(itemCheckLst, (item) => {
                    if (item.value && item.rowId === item2E.personItemDefId) {
                        changeVal = (changeValue < 1) ? 1 : (changeValue > 2) ? 2 : changeValue;
                        setTimeout(function() {
                            grid.ntsGrid("updateRow", item2E.personItemDefId, parrentId === 'anotherSelectedAll_auth' ? { otherAuth: String(changeVal) } : { selfAuth: String(changeVal) });
                        }, 1);
                    }
                });
            });

        }

        openDModal() {

            let self = this;

            setShared('personRole', self.currentRole());



            nts.uk.ui.windows.sub.modal('/view/cas/001/d/index.xhtml', { title: '' }).onClosed(function(): any {

                if (!getShared('isCanceled')) {
                    self.reload().always(() => {
                    });
                }
            });
        }

        openCModal() {

            let self = this,
                currentRole = {
                    roleList: self.personRoleList(),
                    personRole: self.currentRole()
                };

            setShared('currentRole', currentRole);



            nts.uk.ui.windows.sub.modal('/view/cas/001/c/index.xhtml', { title: '' }).onClosed(function(): any {

                if (!getShared('isCanceled')) {
                    self.reload().always(() => {



                    });
                }
            });
        }

        reload(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred(),
                personRole = self.currentRole(),
                selectedId = self.currentCategoryId(),
                grid = $("#item_role_table_body");;

            personRole.loadRoleCategoriesList(personRole.roleId, true).done(function() {

                if (self.roleCategoryList().length > 0) {

                    self.currentRole().currentCategory().loadRoleItems(self.currentRoleId(), selectedId).done(function() {
                        self.checkboxSelectedAll(false);
                        let allowPerson = self.allowPersonRef(),
                            allowOther = self.allowOtherRef();
                        self.allowPersonRef(!allowPerson);
                        self.allowPersonRef(allowPerson);
                        self.allowOtherRef(!allowOther);
                        self.allowOtherRef(allowOther);
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

            self.loadPersonRoleList().done(function() {

                if (self.personRoleList().length > 0) {
                    let selectedId = self.currentRoleId() !== '' ? self.currentRoleId() : self.personRoleList()[0].roleId;

                    self.currentRoleId(selectedId);


                } else {

                    dialog({ messageId: "Msg_364" }).then(function() {
                        nts.uk.request.jump("/view/ccg/008/a/index.xhtml");
                    });

                }

                dfd.resolve();

            });

            return dfd.promise();
        }

        loadPersonRoleList(): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred();


            self.component.startPage().done(() => {
                self.personRoleList.removeAll();

                _.forEach(self.component.listRole(), function(iPersonRole: IPersonRole) {

                    self.personRoleList(_.map(self.component.listRole(), x => new PersonRole(x)));

                });



                dfd.resolve();

            });
            return dfd.promise();
        }

        saveData() {
            let self = this,

                command = self.createSaveCommand();

            service.savePersonRole(command).done(function() {

                dialog({ messageId: "Msg_15" }).then(function() {

                    self.reload().always(() => {

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
                case 1: return getText('Enum_CategoryType_SINGLE_INFO');
                case 2: return getText('Enum_CategoryType_MULTI_INFO');
                case 3: return getText('Enum_CategoryType_CONTINUOUS_HISTORY');
                case 4: return getText('Enum_CategoryType_NODUPLICATE_HISTORY');
                case 5: return getText('Enum_CategoryType_DUPLICATE_HISTORY');
                case 6: return getText('Enum_CategoryType_CONTINUOUS_HISTORY');
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
        name: string;
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
        parrentCd: string;
        otherAuth: number;
        selfAuth: number;
        dataType: number;
        isConvert: boolean;
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
        currentCategory: KnockoutObservable<PersonRoleCategory> = ko.observable(null);

        constructor(param: IPersonRole) {
            let self = this;
            self.roleId = param ? param.roleId : '';
            self.roleCode = param ? param.roleCode : '';
            self.roleName = param ? param.name : '';
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


        loadRoleCategoriesList(RoleId, isReload): JQueryPromise<any> {
            var self = this,
                dfd = $.Deferred();
            let screenModel = __viewContext['screenModel'];

            service.getCategoryRoleList(RoleId).done(function(result: Array<IPersonRoleCategory>) {

                if (result.length <= 0) {
                    screenModel.roleCategoryList(_.map(result, x => new PersonRoleCategory(x)));
                    dialog({ messageId: "Msg_217" });
                    dfd.resolve();
                }

                if (!isReload) {
                    if (screenModel.currentCategoryId()) {

                        self.setCtgSelectedId(result);
                    }
                }
                screenModel.roleCategoryList(_.map(result, x => new PersonRoleCategory(x)));




                dfd.resolve();
            });
            return dfd.promise();
        }

        setCtgSelectedId(result) {
            let screenModel = __viewContext['screenModel'];
            let oldValue = screenModel.currentCategoryId();

            screenModel.currentCategoryId(result[0].categoryId);

            if (screenModel.currentCategoryId() == oldValue) {

                screenModel.currentCategoryId.valueHasMutated();

            }
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
        roleItemDatas: KnockoutObservableArray<PersonRoleItem> = ko.observableArray([]);
        itemListCbb: KnockoutObservableArray<any> = ko.observableArray([
            { code: 1, name: getText('Enum_PersonInfoAuthTypes_HIDE') },
            { code: 2, name: getText('Enum_PersonInfoAuthTypes_REFERENCE') },
            { code: 3, name: getText('Enum_PersonInfoAuthTypes_UPDATE') }
        ]);

        anotherSelectedAll: KnockoutObservable<number> = ko.observable(1);
        seftSelectedAll: KnockoutObservable<number> = ko.observable(1);
        allowPersonRef: KnockoutObservable<number> = ko.observable(1);
        allowOtherRef: KnockoutObservable<number> = ko.observable(1);
        itemLst2E: Array<any> = [];

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


        // hÃ m khá»Ÿi táº¡o
        loadRoleItems(roleId, CategoryId): JQueryPromise<any> {
            let self = this,
                dfd = $.Deferred(),
                grid = $("#item_role_table_body"),
                screenModel = __viewContext['screenModel'],
                switchString = `<div id='{0}_auth' class='selected_all_auth'
                                    data-bind="ntsSwitchButton: {
                                        options: itemListCbb,
                                        optionsValue:'code',
                                        optionsText: 'name',
                                        value: {0},
                                        enable: {1} }"></div>
                                <span id='selected_all_caret' class='caret-bottom outline'></span>`,
                selectedAllString = nts.uk.text.format(switchString, 'anotherSelectedAll', '!!allowOtherRef'),
                selfSelectedAllString = nts.uk.text.format(switchString, 'seftSelectedAll', '!!allowPersonRef');

            let array2E = [{
                value: '1',
                text: getText('Enum_PersonInfoAuthTypes_HIDE')
            }, {
                    value: '2',
                    text: getText('Enum_PersonInfoAuthTypes_REFERENCE')
                }],
                array3E = [{
                    value: '1',
                    text: getText('Enum_PersonInfoAuthTypes_HIDE')
                }, {
                        value: '2',
                        text: getText('Enum_PersonInfoAuthTypes_REFERENCE')
                    }, {
                        value: '3',
                        text: getText('Enum_PersonInfoAuthTypes_UPDATE')
                    }];

            service.getPersonRoleItemList(roleId, CategoryId).done(function(result: any) {
                self.roleItemDatas(_.map(result.itemLst, x => new PersonRoleItem(x)));
                self.roleItemList(_.filter(_.map(result.itemLst, x => new PersonRoleItem(x)), ['parrentCd', null]));
                self.itemLst2E = result.itemReadLst;
                if (self.roleItemList().length < 1) {
                    dialog({ messageId: "Msg_217" });
                }
                let option2E: string = "{ ";

                for (var i = 0; i < result.itemReadLst.length; i++) {
                    if (i === (result.itemReadLst.length - 1)) {
                        option2E = option2E + '"' + result.itemReadLst[i].personItemDefId + '"' + ':' + '["1","2"]' + '}';
                    } else {
                        option2E = option2E + '"' + result.itemReadLst[i].personItemDefId + '"' + ':' + '["1","2"], ';
                    }
                }

                if ($("#item_role_table_body").data("igGrid")) {
                    $("#item_role_table_body").ntsGrid("destroy");
                }
                $("#item_role_table_body").ntsGrid({
                    width: '835px',
                    height: '315px',
                    dataSource: self.roleItemList(),
                    primaryKey: 'personItemDefId',
                    //                    hidePrimaryKey: true,
                    rowVirtualization: true,
                    virtualization: true,
                    virtualizationMode: 'continuous',
                    columns: [
                        { headerText: '', key: 'personItemDefId', dataType: 'string', width: '34px', hidden: true },
                        { headerText: '', key: 'isChecked', dataType: 'boolean', width: '48px', ntsControl: 'Checkbox', showHeaderCheckbox: true },
                        { headerText: getText('CAS001_69'), key: 'setting', dataType: 'string', width: '48px', formatter: makeIcon },
                        { headerText: getText('CAS001_47'), key: 'itemName', dataType: 'string', width: '255px' },
                        { headerText: getText('CAS001_48') + selectedAllString, key: 'otherAuth', dataType: 'string', width: '232px', ntsControl: 'SwitchButtons1' },
                        { headerText: getText('CAS001_52') + selfSelectedAllString, key: 'selfAuth', dataType: 'string', width: '232', ntsControl: 'SwitchButtons2' },
                    ],
                    ntsControls: [
                        { name: 'Checkbox', options: { value: 1, text: '' }, optionsValue: 'value', optionsText: 'text', controlType: 'CheckBox', enable: true },
                        {
                            name: 'SwitchButtons1',
                            options: array3E,
                            optionsValue: 'value',
                            optionsText: 'text',
                            controlType: 'SwitchButtons',
                            enable: true,
                            distinction: option2E == "{ " ? {} : JSON.parse(option2E)
                        },
                        {
                            name: 'SwitchButtons2',
                            options: array3E,
                            optionsValue: 'value',
                            optionsText: 'text',
                            controlType: 'SwitchButtons',
                            enable: true,
                            distinction: option2E == "{ " ? {} : JSON.parse(option2E)
                        }
                    ],
                    features: [

                        {
                            name: 'Selection',
                            mode: 'row',
                            multipleSelection: true
                        }
                    ],
                    ntsFeatures: [{
                        name: 'CellState',
                        rowId: 'rowId',
                        columnKey: 'columnKey',
                        state: 'state',
                        states: result.itemRequired
                    }],
                });

                let allowOther = __viewContext['screenModel'].allowOtherRef(),
                    allowPerson = __viewContext['screenModel'].allowPersonRef();
                __viewContext['screenModel'].allowOtherRef(!allowOther);
                __viewContext['screenModel'].allowOtherRef(allowOther);
                __viewContext['screenModel'].allowPersonRef(!allowPerson);
                __viewContext['screenModel'].allowPersonRef(allowPerson);

                // Ä‘oáº¡n bind láº¡i header
                ko.applyBindings(__viewContext['screenModel'], nts.uk.ui.ig.grid.header.getCell('item_role_table_body', 'otherAuth')[0]);
                ko.applyBindings(__viewContext['screenModel'], nts.uk.ui.ig.grid.header.getCell('item_role_table_body', 'selfAuth')[0]);


                dfd.resolve();

            }).always(() => {
                //register click change all event
                $(() => {
                    $('#anotherSelectedAll_auth, #seftSelectedAll_auth').on('click', '.nts-switch-button', function() {
                        let id = $(this).parent().attr('id');
                        screenModel.changeAll(id, id === 'anotherSelectedAll_auth' ? screenModel.anotherSelectedAll() : screenModel.seftSelectedAll());

                    });

                    $('.ui-iggrid-header').on('focus', function() {

                        if ($(this).find('.nts-switch-button').is(':enabled')) {
                            $(this).find('.selected_all_auth').focus();
                        }
                    });
                });

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
        parrentCd: string;
        otherAuth: string;
        selfAuth: string;
        itemCd: string;
        dataType: number;
        isConvert: boolean = false;

        constructor(param: IPersonRoleItem) {
            let self = this;
            self.personItemDefId = param ? param.personItemDefId : "";//_.replace(param.personItemDefId, new RegExp("-", "g"), "_") : '';
            self.setting = param ? param.setting : false;
            self.requiredAtr = param ? param.requiredAtr : 'false';
            self.itemName = param ? param.itemName : '';
            self.parrentCd = param ? param.parrentCd : '';
            self.itemCd = param ? param.itemCd : '';
            self.otherAuth = this.setting === true ? param ? param.otherAuth : 1 : 1;
            self.selfAuth = this.setting === true ? param ? param.selfAuth : 1 : 1;
            self.dataType = param ? param.dataType : '';
            self.isConvert = param ? (param.personItemDefId.search("CS") > -1 ? false : true) : false;
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
        items: Array<PersonRoleItemCommand> = [];
        constructor(param: PersonRoleCategory) {

            let sm: ScreenModel = __viewContext['screenModel'];

            this.categoryId = param.categoryId;
            this.categoryName = param.categoryName;
            this.categoryType = param.categoryType;
            this.allowPersonRef = sm.allowPersonRef();
            this.allowOtherRef = sm.allowOtherRef();
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
            //add parrent item

            let cellsUpdated = $("#item_role_table_body").ntsGrid("updatedCells"),
                dataSource = $("#item_role_table_body").igGrid("option", "dataSource"),
                itemGroup = _.groupBy(cellsUpdated, 'rowId'),
                itemLst: Array<any> = [];


            itemLst = _.map(dataSource, function(c: any) {
                _.each(itemGroup, function(i) {
                    if (i.length > 0) {
                        let personItemDefId: string = i[0].rowId;
                    }
                    if (c.personItemDefId === personItemDefId) {
                        _.each(i, function(x) {
                            if (x.columnKey == "otherAuth") {
                                c.otherAuth = x.value !== undefined ? x.value : c.otherAuth;
                            }
                            if (x.columnKey == "selfAuth") {
                                c.selfAuth = x.value !== undefined ? x.value : c.selfAuth;
                            }

                        });
                    }
                    return c;

                });
                return c;


            });

            this.items = _.map(itemLst, x => new PersonRoleItemCommand(x));
            console.log(this.items);
            //add child item
            this.addChildItem(this.items);
        }

        addChildItem(items: Array<PersonRoleItemCommand>) {
            let sm: ScreenModel = __viewContext['screenModel'],
                itemDatas = sm.currentRole().currentCategory().roleItemDatas();
            //create loop parent for get child item 
            _.forEach(items, function(parentItem: PersonRoleItemCommand) {
                let childItems = _.filter(itemDatas, { parrentCd: parentItem.itemCd });
                //set atr same parent item
                _.forEach(childItems, function(childItem: PersonRoleItem) {
                    let subItems = _.filter(itemDatas, { parrentCd: childItem.itemCd });
                    //set atr same child item
                    _.forEach(subItems, function(obj: PersonRoleItem) {
                        obj.selfAuth = parentItem.selfAuth;
                        obj.otherAuth = parentItem.otherAuth;
                        items.push(new PersonRoleItemCommand(obj));
                    })
                    childItem.selfAuth = parentItem.selfAuth;
                    childItem.otherAuth = parentItem.otherAuth;

                    items.push(new PersonRoleItemCommand(childItem));
                });


            });



        }

    }
    export class PersonRoleItemCommand {
        personItemDefId: string;
        itemCd: string;
        parrentCd: string;
        otherAuth: number;
        selfAuth: number;
        constructor(param: PersonRoleItem) {
            this.personItemDefId = param.personItemDefId;
            this.parrentCd = param.parrentCd;
            this.otherAuth = param.otherAuth;
            this.selfAuth = param.selfAuth;
            this.itemCd = param.itemCd;
        }

    }

}

function makeIcon(value, row) {
    if (value == "true")
        return "â—�";
    return '';
}



