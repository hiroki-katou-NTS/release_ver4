module nts.uk.ui.menu {
    
    /** Showing item */
    let showingItem;
    
    /**
     * Menu item.
     */
    class MenuItem {
        name: string;
        path: string;
        constructor(name: string, path?: string) {
            this.name = name;
            this.path = path;
        }
    }
    
    /**
     * Create menu selection.
     */
    function createMenuSelect($menuNav: JQuery, menuSet: Array<any>) {
        let $cate = $("<li class='category'/>").addClass("menu-select").appendTo($menuNav);
        let $cateName = $("<div class='category-name'/>").html("&#9776;").appendTo($cate);
        let $menuItems = $("<ul class='menu-items'/>").appendTo($cate);
        $menuItems.append($("<li class='menu-item'/>").text("メニュー選択"));
        $menuItems.append($("<hr/>").css({ margin: "5px 0px" }));
        _.forEach(menuSet, function(item, i) {
            $menuItems.append($("<li class='menu-item'/>").data("code", item.webMenuCode)
                .text(item.webMenuName).on(constants.CLICK, function() {
                    uk.localStorage.setItem(constants.MENU, $(this).data("code"));
                    $menuNav.find(".category:eq(0)").off();
                    $menuNav.find(".category:gt(0)").remove();
                    generate($menuNav, item);
                    _.defer(function() {
                        showingItem = undefined;
                    });
                }));
        });
        $menuItems.append("<br/>");
    }
    
    /**
     * Request.
     */
    export function request() {
        $("#logo").on(constants.CLICK, function() {
            uk.request.jumpToTopPage();
        });
        
        displayUserInfo();
        nts.uk.request.ajax(constants.APP_ID, constants.MenuDataPath).done(function(menuSet) {
            let $menuNav = $("<ul/>").attr("id", "menu-nav").appendTo($("#nav-area"));
            if (!menuSet || menuSet.length === 0) return;
            createMenuSelect($menuNav, menuSet);
            let menuCode = uk.localStorage.getItem(constants.MENU);
            if (menuCode.isPresent()) {
                let selectedMenu = _.find(menuSet, function(m) {
                    return m.webMenuCode === menuCode.get();
                });
                
                !util.isNullOrUndefined(selectedMenu) ? generate($menuNav, selectedMenu)
                    : generate($menuNav, menuSet[0]);
            } else {
                generate($menuNav, menuSet[0]);
            }
        });
        getProgram();
    }
    
    /**
     * Generate.
     */
    function generate($menuNav: JQuery, menuSet: any) {
        _.forEach(menuSet.menuBar, function(category: any) {
            let $cate = $("<li class='category'/>").appendTo($menuNav);
            if (category.selectedAttr === 1) {
                $cate.addClass("direct").data("path", category.link).on(constants.CLICK, function() {
                    uk.request.jumpToMenu(category.link);
                });
            }
            
            let $cateName = $("<div class='category-name'/>")
                            .css({ background: category.backgroundColor, color: category.textColor || "#FFF" })
                            .text(category.menuBarName).appendTo($cate);
            let $menuItems = $("<ul class='menu-items'/>").appendTo($cate);
            
            if (category.items && category.items.length > 0) {
                _.forEach(category.items, function(item: any) {
                    $menuItems.append($("<li class='menu-item' path='" + item.path + "'/>").text(item.name));
                });
            } else if (category.titleMenu && category.titleMenu.length > 0) {
                titleMenu.createTitles($menuItems, category.titleMenu);
            }
        });
        init();
    }
    
    /**
     * Display user info.
     */
    export function displayUserInfo() {
        let $userInfo = $("#user-info");
        let $company = $userInfo.find("#company");
        let $user = $userInfo.find("#user");
        let $userName;
        
        let notThen = function($container: JQuery, target: any, op: any) {
            if (!$container.is(target) && $container.has(target).length === 0) {
                op();
            }
        };
        
        nts.uk.request.ajax(constants.APP_ID, constants.Companies).done(function(companies: any) {
            if (!companies || companies.length === 0) return;
            let $companyName = $("<span/>").attr("id", "company-name");
            nts.uk.request.ajax(constants.APP_ID, constants.Company).done(function(companyId: any) {
                let comp = _.find(companies, function(c) {
                    return c.companyId === companyId;
                });
                if (comp) $companyName.text(comp.companyName).appendTo($company);
            });
            let $companySelect = $("<div/>").addClass("company-select cf");
            $companySelect.appendTo($company);
            $("<div/>").addClass("ui-icon ui-icon-caret-1-s").appendTo($companySelect);
            let $companyList = $("<ul class='menu-items company-list'/>").appendTo($companySelect);
            _.forEach(companies, function(comp: any, i: number) {
                let $compItem = $("<li class='menu-item company-item'/>").text(comp.companyName).appendTo($companyList);
                $compItem.on(constants.CLICK, function() {
                    nts.uk.request.ajax(constants.APP_ID, constants.ChangeCompany, comp.companyId)
                    .done(function(personName) {
                        $companyName.text(comp.companyName);
                        $userName.text(personName);
                        $companyList.css("right", $user.outerWidth() + 30);
                    });
                });
            });
            
            $companySelect.on(constants.CLICK, function() {
                if ($companyList.css("display") === "none") {
                    $companyList.fadeIn(100);
                    return;
                }
                $companyList.fadeOut(100);
            });
            
            nts.uk.request.ajax(constants.APP_ID, constants.UserName).done(function(userName: any) {
                let $userImage = $("<div/>").attr("id", "user-image").addClass("ui-icon ui-icon-person").appendTo($user);
                $userImage.css("margin-right", "6px").on(constants.CLICK, function() {
                    // TODO: Jump to personal profile.
                });
                $userName = $("<span/>").attr("id", "user-name").text(userName).appendTo($user);
                let $userSettings = $("<div/>").addClass("user-settings cf").appendTo($user);
                $("<div class='ui-icon ui-icon-caret-1-s'/>").appendTo($userSettings);
                let userOptions = [ new MenuItem("個人情報の設定"), new MenuItem("ログアウト") ];
                let $userOptions = $("<ul class='menu-items user-options'/>").appendTo($userSettings);
                _.forEach(userOptions, function(option: any, i: number) {
                    let $li = $("<li class='menu-item'/>").text(option.name);
                    $userOptions.append($li);
                    if (i === 0) {
                        $li.on(constants.CLICK, function() {
                            // TODO: Jump to personal information settings.
                        });
                        return;
                    }
                    $li.on(constants.CLICK, function() {
                        // TODO: Jump to login screen and request logout to server
                        nts.uk.request.ajax(constants.APP_ID, constants.Logout).done(function() {
                            nts.uk.request.login.jumpToUsedLoginPage();
                        });
                    });
                });
                $companyList.css("right", $user.outerWidth() + 30);
                
                $userSettings.on(constants.CLICK, function() {
                    if ($userOptions.css("display") === "none") {
                        $userOptions.fadeIn(100);
                        return;
                    }
                    $userOptions.fadeOut(100);
                });
                
                $(document).on(constants.CLICK, function(evt: any) {
                    notThen($companySelect, evt.target, function() {
                        $companyList.fadeOut(100);
                    });
                    notThen($userSettings, evt.target, function() {
                        $userOptions.fadeOut(100);
                    });
                });
            });
        });
    }
    
    /**
     * Get program.
     */
    function getProgram() {
        nts.uk.request.ajax(constants.APP_ID, constants.PG).done(function(pg: any) {
            // show program name on title of browser
            ui.viewModelBuilt.add(() => {
                ui._viewModel.kiban.programName(pg);
            });
            
            let $pgArea = $("#pg-area");
            $("<div/>").attr("id", "pg-name").text(pg).appendTo($pgArea);
            let $manualArea = $("<div/>").attr("id", "manual").appendTo($pgArea);
            let $manualBtn = $("<button class='manual-button'/>").text("?").appendTo($manualArea);
            $manualBtn.on(constants.CLICK, function() {
                // TODO: Open manual
            });
            
            let $tglBtn = $("<div class='tgl cf'/>").appendTo($manualArea);
            $tglBtn.append($("<div class='ui-icon ui-icon-caret-1-s'/>"));
            $tglBtn.on(constants.CLICK, function() {
                // TODO
            });
        });
    }
    
    /**
     * Init.
     */
    function init() {
        let $navArea = $("#nav-area");
        let $menuItems = $("#menu-nav li.category:not(.direct)");
        
        /**
         * Close item.
         */
        function closeItem() {
            let $item = $("#menu-nav li.category:eq(" + showingItem + ")");
            $item.find(".category-name").removeClass("opening");
            $item.find("ul, div.title-menu").fadeOut(100);
        }
        
        /**
         * Open item.
         */
        function openItem($item: JQuery) {
            $item.find(".category-name").addClass("opening");
            $item.find("ul, div.title-menu").fadeIn(100);
        }
        
        $(document).on(constants.CLICK, function(evt) {
            if (!$navArea.is(evt.target) && $navArea.has(evt.target).length === 0
                && !util.isNullOrUndefined(showingItem)) {
                closeItem();
                showingItem = undefined;
            }
        });
        
        $menuItems.hover(function() {
            let $item = $(this);
            let ith = $item.index();
            if (util.isNullOrUndefined(showingItem) || showingItem === ith) return;
            closeItem();
            setTimeout(function() {
                openItem($item);
            }, 14);
            showingItem = ith;
        });
        
        $menuItems.on(constants.CLICK, function(event) {
            let $item = $(this);
            showingItem = $item.index();
            if ($item.find(".category-name").hasClass("opening") && showingItem === 0) {
                closeItem();
                return;
            }
            openItem($item);
        });
    
        $(".menu-item").on(constants.CLICK, function() {
            let path = $(this).data('path');
            if (path) nts.uk.request.jump(path);
        });   
    }
    
    module titleMenu {
        export let WIDTH: number = 192;
        export let FR: number = 20;
        
        /**
         * Create titles.
         */
        export function createTitles($category: JQuery, titles: any) {
            let $title = $("<div/>").addClass("title-menu").appendTo($category);
            let width = 0, height, maxHeight = 0;
            _.forEach(titles, function(t, i) {
                height = 60;
                let left = WIDTH * i + 3;
                if (i > 0) {
                    left += FR * i;
                }
                if (i === titles.length - 1) {
                    width = left + WIDTH + 7;
                }
                let $titleDiv = $("<div/>").addClass("title-div").css({ left: left }).appendTo($title);
                let $titleName = $("<div/>").addClass("title-name").text(t.titleMenuName)
                                .css({ background: t.backgroundColor, color: t.textColor }).appendTo($titleDiv);
                let $titleImage = $("<img/>").addClass("title-image").hide();
                $titleDiv.append($titleImage);
                
                if (!_.isNull(t.imageFile) && !_.isUndefined(t.imageFile) && !_.isEmpty(t.imageFile)) {
                    let fqpImage = nts.uk.request.file.pathToGet(t.imageFile);
                    // TODO: Show image
                    $titleImage.attr("src", fqpImage).show();
//                    $titleImage.attr("src", "../../catalog/images/valentine-bg.jpg").show();
                    height += 80;
                }
                
                if (t.treeMenu && t.treeMenu.length > 0) {
                    _.forEach(t.treeMenu, function(item, i) {
                        if (item.menuAttr === 1) {
                            $titleDiv.append($("<hr/>").css({ margin: "14px 0px" }));
                            height += 30;
                            return;
                        }
                        let $item = $("<li class='title-item'/>").data("path", item.url).text(item.displayName || item.defaultName);
                        $item.on(constants.CLICK, function() {
                            let path = $(this).data("path");
                            if (path && path.indexOf("http") !== 0) {
                                uk.request.jumpToMenu(path);   
                                return;
                            }
                            window.location.href = path;
                        });
                        $titleDiv.append($item);
                        height += (34 + (Math.ceil($item.text().length / 12) - 1) * 20);
                    });
                }
                maxHeight = Math.max(maxHeight, height); 
            });
            maxHeight += 20;
            $title.css({ height: maxHeight + "px", width: width + "px" });
        }
    }
    
    module constants {
        export let APP_ID = "com";
        export let MENU = "UK-Menu";
        export let CLICK = "click";
        export let MenuDataPath = "/sys/portal/webmenu/finddetails";
        export let Company = "/sys/portal/webmenu/currentCompany";
        export let Companies = "sys/portal/webmenu/companies";
        export let ChangeCompany = "sys/portal/webmenu/changeCompany";
        export let UserName = "sys/portal/webmenu/username";
        export let Logout = "sys/portal/webmenu/logout";
        export let PG = "sys/portal/webmenu/program";
    }
    
}