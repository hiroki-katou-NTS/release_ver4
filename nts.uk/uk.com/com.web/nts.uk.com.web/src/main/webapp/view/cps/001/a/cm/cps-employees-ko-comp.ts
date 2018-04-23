module nts.custom.component {
    import ajax = nts.uk.request.ajax;
    import text = nts.uk.resource.getText;
    import modal = nts.uk.ui.windows.sub.modal;
    import setShared = nts.uk.ui.windows.setShared;
    import getShared = nts.uk.ui.windows.getShared;
    import liveView = nts.uk.request.liveView;

    let __viewContext: any = window["__viewContext"] || {};

    const fetch = {
        perm: () => ajax('ctx/pereg/roles/auth/get-self-auth'),
        employee: (id: string) => ajax(`bs/employee/person/get-header/${id}`),
        get_list: (ids) => ajax('com', 'bs/employee/person/get-list-emps', ids),
        avartar: (id: string) => ajax(`basic/organization/empfilemanagement/find/getAvaOrMap/${id}/0`)
    },
        DEF_AVATAR = 'images/avatar.png',
        style = `<style type="text/css" rel="stylesheet" id="header_style">
            .header-info .avatar-group,
            .header-info .person-group,
            .header-info .action-group {
                float: left;
                padding: 0 1px;
            }
            
            .header-info .avatar-group {
                width: 110px;
                height: 147px;
                cursor: pointer;
            }
            
            .header-info .person-group {
                width: 510px;
                height: 147px;
            }
            
            .header-info .action-group {
                width: 234px;
                height: 147px;
            }
            
            .header-info .action-group .active-panel:first-child {
                height: 85px;
            }
            
            .header-info .action-group .active-panel:last-child {
                height: 60px;
            }
            
            .header-info .action-group .column {
                width: 112px;
                margin-left: 1px;
                padding: 3px 0 3px 5px;
                box-sizing: border-box;
                float: left;
            }
            
            .header-info .action-group button.btn {
                width: 100%;
                margin-bottom: 5px;
                padding-left: 30px;
                text-align: left;
            }
            
            .header-info .action-group .btn-print {
                height: 65px;
                background-repeat: no-repeat;
                background-position-x: 2px;
                background-position-y: center;
                background-image: url("images/print.png");
            }
            
            .header-info .action-group .btn-location {
                background-repeat: no-repeat;
                background-position-x: 2px;
                background-position-y: center;
                background-image: url("images/location.png");
                background-position-y: center;
            }
            
            .header-info .action-group .btn-details {
                background-repeat: no-repeat;
                background-position-x: 5px;
                background-position-y: center;
                background-image: url("images/details.png");
            }
            
            .header-info .action-group .btn-goout {
                cursor: pointer;
                display: block;
                min-height: 51px;
                line-height: 50px;
                padding-left: 60px;
                background-repeat: no-repeat;
                background-position-x: 5px;
                background-position-y: center;
                background-image: url("images/goout.png");
                display: block;
            }
            
            .header-info .active-panel {
                width: 100%;
                min-height: 52px;
                height: 100%;
                box-sizing: border-box;
                border: 1px solid #999999;
                margin-bottom: 2px;
            }
            
            .header-info .active-panel:before {
                content: '';
                display: block;
                background-color: #00B050;
                display: block;
                background-color: #00B050;
                height: 7px;
            }
            
            .header-info .avatar-group img.avatar {
                padding: 5px 2px 2px 5px;
                width: 100px;
                height: 130px;
            }
            
            .header-info .person-info {
                margin: 5px;
                width: calc(100% - 10px);
                box-sizing: border-box;
            }
            
            .header-info .person-info td {
                line-height: 30px;
                border: 1px solid #999;
            }
            
            .header-info .person-info td:not (:first-child ) {
                position: relative;
            }
            
            .header-info .person-info td:first-child {
                width: 120px;
                padding-left: 15px;
            }
            
            .header-info .person-info td:nth-child(3) {
                width: 100px;
                padding-left: 15px;
                box-sizing: border-box;
            }
            
            .header-info .person-info td>div.info {
                float: left;
                display: block;
                padding-left: 5px;
                padding-right: 5px;
                min-height: 30px;
                max-height: 30px;
                overflow: hidden;
            }
            
            .header-info .person-info td>div.info>* {
                display: inline-block;
                max-width: 215px;
                text-overflow: ellipsis;
                height: 30px;
                overflow: hidden;
                white-space: nowrap;
            }
            
            .header-info .person-info td>div.info>*:first-child {
                min-width: 110px;
                max-width: 110px;
            }
            
            .header-info .person-info td>div.info.first {
                min-width: 95px;
                max-width: 95px;
            }
            
            .header-info .person-info td>div.bg-calendar-ym-set {
                display: inline-block;
                padding-left: 10px;
                padding-right: 10px;
                border-left: 1px solid #999;
                border-right: 1px solid #999;
            }
        </style>`;

    // add support virtual element to let control
    ko.virtualElements.allowedBindings.let = true;

    window["ko"].components.register('employee-list', {
        template: `<!-- ko let: {
                        text: nts.uk.resource.getText
                } -->
            <div class="left-area">
                <div class="bg-green caret-right caret-background">
                    <table data-bind="attr: { 
                                NameID: text('CPS001_09'), 
                                id: nts.uk.util.randomId().replace(/-/g, '')
                            }, 
                            ntsGridList: {                              
                                rows: 10,
                                multiple: false,
                                columns: [
                                    { headerText: 'コード', key: 'employeeId', width: 100, hidden: true },
                                    { headerText: text('CPS001_10'), key: 'employeeCode', width: 115, hidden: false },
                                    { headerText: '', key: 'employeeName', width: 125, hidden: false }
                                ],
                                primaryKey: 'employeeId',
                                value: employeeId,
                                dataSource: employees
                            }"></table>
                </div>
            </div>
            <div class="right-area">
                <div class='header-info'>
                    <div class="avatar-group">
                        <div class="active-panel" data-bind="click: action.avatar">
                            <img data-bind="attr: { src: person.avatar }" class="avatar" tabindex="13" />
                        </div>
                    </div>
                    <div class="person-group">
                        <div class="active-panel">
                            <table class="person-info">
                                <tbody>
                                    <tr>
                                        <td class="bg-calendar-ym-set" data-bind="text: text('CPS001_11')"></td>
                                        <td>
                                            <div class="info">
                                                <span data-bind="text: employee.code, attr: { title: employee.code }"></span>
                                                <span data-bind="text: employee.name, attr: { title: employee.name }"></span>
                                                <span data-bind="text: person.gender, attr: { title: person.gender }"></span>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="bg-calendar-ym-set" data-bind="text: text('CPS001_12')"></td>
                                        <td>
                                            <div class="info first" data-bind="text: person.age"></div>
                                            <div class="info bg-calendar-ym-set" data-bind="text: text('CPS001_12')"></div>
                                            <div class="info" data-bind="text: employee.entire"></div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="bg-calendar-ym-set" data-bind="text: text('CPS001_14')"></td>
                                        <td>
                                            <span data-bind="text: department.code"></span>
                                            <span data-bind="text: department.name"></span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td class="bg-calendar-ym-set" data-bind="text: text('CPS001_15')"></td>
                                        <td>
                                            <div class="info first" data-bind="text: constract.position"></div>
                                            <div class="info bg-calendar-ym-set" data-bind="text: text('CPS001_16')"></div>
                                            <div class="info" data-bind="text: constract.contractType"></div>
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="action-group">
                        <div class="active-panel cf">
                            <div class="column">
                                <button class="btn btn-location" type="button" 
                                        data-bind="
                                            click: action.location,
                                            text: text('CPS001_20'),
                                            style: { 
                                                visibility: auth.allowMapBrowse() ? 'visible' : 'hidden' 
                                            }" 
                                        tabindex="16"></button>
                                <button class="btn btn-details" type="button" 
                                        data-bind="
                                            click: action.ebook,
                                            text: text('CPS001_19'),   
                                            style: { 
                                                visibility: auth.allowDocRef() ? 'visible' : 'hidden' 
                                            }" 
                                        tabindex="15"></button>
                            </div>
                            <div class="column">
                                <button class="btn btn-print hidden" type="button" tabindex="14" data-bind="text: text('CPS001_17')"></button>
                            </div>
                        </div>
                        <div class="active-panel">
                            <a class="btn-goout hidden">出向に来ています</a>
                        </div>
                    </div>
                </div>
            </div>
        <!-- /ko -->`,
        viewModel: function(params: any) {
            // add style to <head> on first run
            if (!$('#header_style').length) {
                $('head').append(style);
            }

            ko.utils.extend(params, {
                person: {
                    id: params.personId || ko.observable(''),
                    age: ko.observable(''),
                    gender: ko.observable(''),
                    avatar: ko.observable(DEF_AVATAR)
                },
                employee: {
                    id: params.employeeId || ko.observable(''),
                    name: ko.observable(''),
                    code: ko.observable(''),
                    entire: ko.observable('')
                },
                department: {
                    code: ko.observable(''),
                    name: ko.observable('')
                },
                constract: {
                    position: ko.observable(''),
                    contractType: ko.observable('')
                },
                action: {
                    ebook: () => {
                        let auth = params.auth,
                            id = ko.toJS(params.person.id);

                        setShared("CPS001F_PARAMS", {
                            pid: id
                        });

                        modal('../f/index.xhtml').onClosed(() => { });
                    },
                    avatar: () => {
                        let auth = params.auth,
                            person = params.person,
                            id = ko.toJS(params.employee.id);

                        if (auth.allowAvatarRef) {
                            setShared("CPS001D_PARAMS", {
                                employeeId: id
                            });

                            modal('../d/index.xhtml').onClosed(() => {
                                let data = getShared("CPS001D_VALUES");

                                if (data) {
                                    person.avatar(data.fileId ? liveView(data.fileId) : DEF_AVATAR);
                                }
                            });
                        }
                    },
                    location: () => {
                        let auth = params.auth,
                            id = ko.toJS(params.employee.id);

                        if (auth.allowMapBrowse) {
                            setShared("CPS001E_PARAMS", {
                                employeeId: id
                            });

                            modal('../e/index.xhtml').onClosed(() => { });
                        }
                    }
                },
                auth: {
                    allowDocRef: ko.observable(false),
                    allowMapBrowse: ko.observable(false),
                    allowAvatarRef: ko.observable(false)
                }
            });

            fetch.perm().done((data: IPersonAuth) => {
                if (data) {
                    params.auth.allowDocRef(!!data.allowDocRef);
                    params.auth.allowAvatarRef(!!data.allowAvatarRef);
                    params.auth.allowMapBrowse(!!data.allowMapBrowse);
                } else {
                    params.auth.allowDocRef(false);
                    params.auth.allowAvatarRef(false);
                    params.auth.allowMapBrowse(false);
                }
            });

            params.employeeId.subscribe(id => {
                let person = params.person,
                    employee = params.employee,
                    department = params.department,
                    constract = params.constract;

                fetch.employee(id).done((emp: IData) => {
                    if (emp) {
                        fetch.avartar(id).done(avatar => {
                            person.avatar(avatar.fileId ? liveView(avatar.fileId) : DEF_AVATAR);
                        }).fail(msg => person.avatar(DEF_AVATAR));

                        person.id(emp.pid);

                        if (!emp.gender) {
                            person.gender('');
                        } else {
                            person.gender(`(${emp.gender})`);
                        }

                        if (!emp.birthday) {
                            person.age('');
                        } else {
                            let now = moment.utc(),
                                birthDay = moment.utc(emp.birthday, "YYYY/MM/DD").toDate(),
                                duration = moment.duration(now.diff(birthDay));

                            if (!birthDay) {
                                person.age('');
                            } else {
                                person.age((duration.years() + text('CPS001_66')));
                            }
                        }

                        if (emp.numberOfWork > -1 && emp.numberOfTempHist > -1) {
                            let days = emp.numberOfWork - emp.numberOfTempHist,
                                duration = moment.duration(days, "days");

                            employee.entire(`${duration.years()}${text('CPS001_67')}${duration.months()}${text('CPS001_88')}`);
                        } else {
                            employee.entire('');
                        }

                        employee.code(emp.employeeCode);
                        employee.name(emp.employeeName);

                        department.code(emp.departmentCode);
                        department.name(emp.departmentName);

                        constract.position(emp.position);
                        constract.contractType(emp.contractCodeType);
                    } else {
                        person.id('');
                        person.age('');
                        person.gender('');


                        employee.code('');
                        employee.name('');
                        employee.entire('');

                        department.code('');
                        department.name('');

                        constract.position('');
                        constract.contractType('');
                    }
                });
            });

            params.employeeIds.subscribe(ids => {
                if (!ids || !ids.length) {
                    params.employeeIds([__viewContext.user.employeeId]);
                    return;
                }

                fetch.get_list(ids).done((datas: Array<any>) => {
                    if (!datas.length) {
                        params.employeeIds([__viewContext.user.employeeId]);
                        return;
                    }

                    datas = _(datas).map(d => ({ i: ids.indexOf(d.employeeId), v: d }))
                        .orderBy(o => o.i)
                        .map(m => m.v)
                        .value();

                    params.employees(datas || []);
                    
                    let _ids = _.map(datas, m => m.employeeId);
                    if (_ids.length) {
                        if (_ids.indexOf(params.employeeId()) > -1) {
                            params.employeeId.valueHasMutated();
                        } else {
                            let oidx = _.indexOf(ids, params.employeeId());
                            if (oidx > -1) {
                                if (oidx < ids.length - 1) {
                                    params.employeeId(ids[oidx + 1]);
                                } else {
                                    params.employeeId(ids[oidx - 1]);
                                }
                            } else {
                                params.employeeId(_ids[0]);
                            }
                        }
                    } else {
                        params.employeeId(undefined);
                    }
                });
            });

            return params;
        }
    });

    interface IPersonAuth {
        allowMapUpload: number;
        allowMapBrowse: number;
        allowDocRef: number;
        allowDocUpload: number;
        allowAvatarUpload: number;
        allowAvatarRef: number;
    }

    interface IData {
        pid?: string;
        employeeId: string;
        gender?: string;
        birthday?: string;

        employeeCode?: string;
        employeeName?: string;

        numberOfWork?: number;
        numberOfTempHist?: number;

        departmentCode?: string;
        departmentName?: string;

        avatar?: string;

        position?: string;
        contractCodeType?: string;
    }
}