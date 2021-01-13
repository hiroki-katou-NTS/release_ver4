module nts.uk.ui.header {
    const MENU_KEY = 'UK-Menu';
    const MENU_SET = 'nts.uk.session.MENU_SET';

    @component({
        name: 'ui-header',
        template: `
        <div class="hamberger" data-bind="
                event: {
                    mouseover: $component.hambergerHover,
                    mouseout: $component.hambergerMouseOut
                },
                css: {
                    'hover': $component.menuSet.hover
                }">
            <svg viewBox="0 0 16 14" fill="none" xmlns="http://www.w3.org/2000/svg">
                <rect width="16" height="2" rx="1" fill="white"/>
                <rect y="6" width="16" height="2" rx="1" fill="white"/>
                <rect y="12" width="16" height="2" rx="1" fill="white"/>
            </svg>
            <div class="menu-dropdown menu-hamberger" data-bind="css: { hidden: !$component.menuSet.hover() }">
                <div class="menu-column">
                    <div class="menu-header" data-bind="i18n: nts.uk.ui.toBeResource.selectMenu"></div>
                    <div class="menu-item" data-bind="foreach: $component.menuSet.items">
                        <div class="item" data-bind="
                            i18n: $data.webMenuName,
                            click: function() { $component.selectSet($data) },                        
                            css: { 
                                selected: $component.menuSet.items() && $data.selected
                            }"></div>
                    </div>
                </div>
            </div>
        </div>
        <img class="favicon" src="/nts.uk.com.js.web/lib/nittsu/ui/style/images/kinjirou.png" />
        <div class="menu-groups" data-bind="foreach: { data: $component.menuBars, as: 'bar' }">
            <div class="item-group" data-bind="
                    event: {
                        mouseover: function() { $component.itemBarHover(bar) },
                        mouseout: function() { $component.itemBarMouseOut(bar) }
                    },
                    css: {
                        'hover': bar.hover() && $component.click()
                    },
                    attr: {
                        'data-column': (bar.titleMenu || []).length
                    }">
                <span class="bar-item-title" data-bind="text: bar.menuBarName, click: function() { $component.selectBar(bar) }"></span>
                <div class="menu-dropdown menu-item" data-bind="css: { hidden: !bar.hover() || !bar.titleMenu.length }, foreach: { data: bar.titleMenu, as: 'title' }">
                    <div class="menu-column">
                        <div class="menu-header" data-bind="
                            i18n: title.titleMenuName,
                            style: {
                                'color': title.textColor,
                                'background-color': title.backgroundColor
                            }"></div>
                        <div class="menu-items" data-bind="foreach: title.treeMenu">
                            <div class="item" data-bind="
                                i18n: $component.getName($data),
                                click: function() { $component.selectMenu($data, bar) },                        
                                css: { 
                                    selected: false,
                                    'divider': !$data.url || $data.url === '-'
                                }"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="user-info">
            <div class="menu-groups">
                <div class="item-group">
                    <span class="bar-item-title company" data-bind="text: $component.companyName"></span>
                </div>
                <span class="divider"></span>
                <div class="item-group" data-bind="
                        event: {
                            mouseover: $component.userHover,
                            mouseout: $component.userMouseOut
                        },
                        css: {
                            hover: $component.userNameHover
                        }">
                    <span class="bar-item-title user-name" data-bind="text: $component.userName"></span>
                    <div class="menu-dropdown menu-item">
                        <div class="menu-column">
                            <div class="menu-items">
                                <div class="item" data-bind="i18n: nts.uk.ui.toBeResource.manual, click: $component.manual"></div>
                                <div class="item divider"></div>
                                <div class="item" data-bind="i18n: nts.uk.ui.toBeResource.logout, click: $component.logout"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="avatar notification"></div>
        </div>
        `
    })
    export class HeaderViewModel extends ko.ViewModel {
        ctrl: KnockoutObservable<boolean> = ko.observable(false);
        click: KnockoutObservable<boolean> = ko.observable(false);

        menuSet: {
            hover: KnockoutObservable<boolean>;
            items: KnockoutObservableArray<MenuSet>;
        } = {
                hover: ko.observable(false),
                items: ko.observableArray([])
            }

        menuBars!: KnockoutComputed<MenuBar[]>;

        userName: KnockoutObservable<string> = ko.observable('');
        userNameHover: KnockoutObservable<boolean> = ko.observable(false);

        companies: KnockoutObservableArray<any> = ko.observableArray([]);

        companyName!: KnockoutComputed<string>;

        created() {
            const vm = this;

            vm.menuBars = ko.computed({
                read: () => {
                    const sets = ko.unwrap<MenuSet[]>(vm.menuSet.items);

                    const selected = _.find(sets, (set: MenuSet) => set.selected === true);

                    if (selected) {
                        return selected.menuBar || [];
                    }

                    const [first] = sets;

                    if (first) {
                        return first.menuBar || [];
                    }

                    return [];
                }
            })

            vm.companyName = ko.computed({
                read: () => {
                    const [first] = ko.unwrap<any>(vm.companies);

                    if (first) {
                        return first.companyName || '';
                    }

                    return '';
                }
            });

            nts.uk.sessionStorage
                .getItem(MENU_SET)
                .ifEmpty(() => {
                    vm
                        .$ajax('com', "/sys/portal/webmenu/finddetails")
                        .then((data: MenuSet[]) => nts.uk.sessionStorage.setItem(MENU_SET, JSON.stringify(data)))
                        .then(() => vm.loadData());
                });

            vm
                .$ajax('com', '/sys/portal/webmenu/username')
                .then((data: string) => vm.userName(data));

            vm
                .$ajax('com', '/sys/portal/webmenu/companies')
                .then((data) => vm.companies(data));
        }

        mounted() {
            const vm = this;

            vm.loadData();

            $(window)
                .on('keyup', (evt) => vm.ctrl(evt.ctrlKey))
                .on('keydown', (evt) => vm.ctrl(evt.ctrlKey));
        }

        loadData() {
            const vm = this;

            const menuSet: MenuSet[] = JSON.parse(nts.uk.sessionStorage.getItem(MENU_SET).orElse('[]'));

            _.each(menuSet, (set: MenuSet) => {
                _.each(set.menuBar, (bar: MenuBar) => {
                    bar.hover = ko.observable(false);
                });
            });

            vm.menuSet.items(menuSet);

            if (menuSet.length) {
                const selected = nts.uk.localStorage.getItem(MENU_KEY).orElse('');

                if (!selected) {
                    const [firstSet] = menuSet;

                    if (firstSet) {
                        vm.selectSet(firstSet);
                    }
                } else {
                    const selectedSet = _.find(menuSet, (m: MenuSet) => selected === `${m.companyId}:${m.webMenuCode}`);

                    if (selectedSet) {
                        vm.selectSet(selectedSet);
                    } else {
                        const [firstSet] = menuSet;

                        if (firstSet) {
                            vm.selectSet(firstSet);
                        }
                    }
                }
            }

            $(vm.$el).find('data-bind').removeAttr('data-bind');
        }

        getName(item: MenuTree) {
            const vm = this;

            return ko.computed(() => {
                if (!vm.ctrl()) {
                    return item.displayName;
                }

                return `${item.programId}${item.screenId}`;
            });
        }

        selectSet(item: MenuSet) {
            const vm = this;
            const sets = ko.unwrap<MenuSet[]>(vm.menuSet.items);

            vm.menuSet.hover(false);

            _.each(sets, (set: MenuSet) => {
                set.selected = false;
            });

            item.selected = true;

            vm.menuSet.items.valueHasMutated();

            const [menuBar] = item.menuBar;

            $(vm.$el).find('[data-bind]').removeAttr('data-bind');

            //  $(vm.$el).css({ 'background-color': menuBar.backgroundColor });

            // storage selected set for reload page
            nts.uk.localStorage.setItem(MENU_KEY, `${item.companyId}:${item.webMenuCode}`);
        }

        selectBar(item: MenuBar) {
            const vm = this;

            if (item.link && (!item.titleMenu || item.titleMenu.length === 0)) {
                window.location.href = item.link;
            } else {
                vm.click(!vm.click());
            }
        }

        selectMenu(item: MenuTree, bar: MenuBar) {
            if (item.url && item.url !== '-') {
                bar.hover(false);

                if (!item.queryString) {
                    window.location.href = item.url;
                } else {
                    window.location.href = `${item.url}?${item.queryString}`.replace(/\?{2,}/, '?');
                }
            }
        }

        hambergerHover() {
            const vm = this;

            vm.menuSet.hover(true);
        }

        hambergerMouseOut() {
            const vm = this;

            vm.menuSet.hover(false);
        }

        itemBarHover(item: MenuBar) {
            item.hover(true);
        }

        itemBarMouseOut(item: MenuBar) {
            item.hover(false);
        }

        userHover() {
            const vm = this;

            vm.userNameHover(true);
        }

        userMouseOut() {
            const vm = this;

            vm.userNameHover(false);
        }

        manual() {
            const { pathToManual } = __viewContext.env;

            // jump to index page of manual
            window.open(pathToManual.replace(/\{PGID\}/, "index"));
        }

        logout() {
            const vm = this;
            const { request, sessionStorage } = nts.uk;

            const COMPANY_KEY = "nts.uk.session.COMPANY";
            const PROGRAM_KEY = "nts.uk.session.PROGRAM";

            const MENU_SET_KEY = "nts.uk.session.MENU_SET";
            const EMPLOYEE_SETTING = 'nts.uk.session.EMPLOYEE_SETTING';

            // TODO: Jump to login screen and request logout to server
            vm.$ajax('com', '/sys/portal/webmenu/logout')
                .then(function () {
                    sessionStorage.removeItem(COMPANY_KEY);
                    sessionStorage.removeItem(PROGRAM_KEY);

                    sessionStorage.removeItem(MENU_SET_KEY);
                    sessionStorage.removeItem(EMPLOYEE_SETTING);

                    // cannot remove by flag: htmlOnly
                    // cookie.remove("nts.uk.sescon", { path: "/" });


                    request.login.jumpToUsedLoginPage();
                });
        }
    }

    interface MenuSet {
        selected: boolean;
        companyId: string;
        defaultMenu: 0 | 1;
        menuBar: MenuBar[];
        webMenuCode: string;
        webMenuName: string;
    }

    interface MenuBar {
        hover: KnockoutObservable<boolean>;
        backgroundColor: string;
        code: string;
        displayOrder: number;
        link: null | string;
        menuBarId: string;
        menuBarName: string;
        menuCls: number;
        selectedAttr: number;
        system: number;
        textColor: string;
        titleMenu: MenuTitle[];
    }

    interface MenuTitle {
        backgroundColor: string;
        displayOrder: number;
        imageFile: string;
        textColor: string;
        titleMenuAtr: number;
        titleMenuCode: string;
        titleMenuId: string;
        titleMenuName: string;
        treeMenu: MenuTree[];
    }

    interface MenuTree {
        afterLoginDisplay: number;
        classification: number;
        code: string;
        defaultName: string;
        displayName: string;
        displayOrder: number;
        menuAttr: number;
        programId: string;
        queryString: string;
        screenId: string;
        system: number;
        url: string;
    }
}