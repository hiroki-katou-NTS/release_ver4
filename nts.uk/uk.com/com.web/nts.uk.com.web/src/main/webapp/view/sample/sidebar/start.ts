__viewContext.ready(function() {
    class ScreenModel {
        show: KnockoutObservable<boolean>;
        enable: KnockoutObservable<boolean>;

        constructor() {
            this.show = ko.observable(true);
            this.show.subscribe(function(newVal) {
                if (newVal)
                    $("#sidebar").ntsSideBar("show", 1);
                else
                    $("#sidebar").ntsSideBar("hide", 1);
            });

            this.enable = ko.observable(true);
            this.enable.subscribe(function(newVal) {
                if (newVal) {
                    $("#sidebar").ntsSideBar("enable", 1);
                    $("#sidebar").ntsSideBar("enable", 2);
                }
                else {
                    $("#sidebar").ntsSideBar("disable", 1);
                    $("#sidebar").ntsSideBar("disable", 2);
                }
            });
        }

        testSideMenu() {
            alert("clicked");
        }

        openSubWindow() {
            nts.uk.ui.windows.sub.modeless("/view/sample/sidebar/sidebar-sub.xhtml");
        }
        
        openNewTab() {
            window.open("/nts.uk.com.web/view/sample/sidebar/sidebar-sub.xhtml", "_blank").focus();
        }

    }

    this.bind(new ScreenModel());

});