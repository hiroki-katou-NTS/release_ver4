/// <reference path="../../reference.ts"/>

module nts.uk.ui.koExtentions {

    /**
     * Dialog binding handler
     */
    class NtsDialogBindingHandler implements KnockoutBindingHandler {

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {

        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data.
            var data = valueAccessor();
            var option: any = ko.unwrap(data.option);
            var title: string = ko.unwrap(data.title);
            var message: string = ko.unwrap(data.message);
            var modal: boolean = ko.unwrap(option.modal);
            var show: boolean = ko.unwrap(option.show);
            var buttons: any = ko.unwrap(option.buttons);

            var $dialog = $("<div id='ntsDialog'></div>");
            if (show == true) {
                $('body').append($dialog);
                // Create Buttons
                var dialogbuttons = [];
                for (let button of buttons) {
                    dialogbuttons.push({
                        text: ko.unwrap(button.text),
                        "class": ko.unwrap(button.class) + ko.unwrap(button.size) + " " + ko.unwrap(button.color),
                        click: function() { button.click(bindingContext.$data, $dialog) }
                    });
                }
                // Create dialog
                $dialog.dialog({
                    title: title,
                    modal: modal,
                    closeOnEscape: false,
                    buttons: dialogbuttons,
                    dialogClass: "no-close",
                    open: function() {
                        $(this).parent().find('.ui-dialog-buttonset > button.yes').focus();
                        $(this).parent().find('.ui-dialog-buttonset > button').removeClass('ui-button ui-corner-all ui-widget');
                        $('.ui-widget-overlay').last().css('z-index', 120000);
                    },
                    close: function(event) {
                        bindingContext.$data.option.show(false);
                    }
                }).text(message);
            }
            else {
                // Destroy dialog
                if ($('#ntsDialog').dialog("instance") != null)
                    $('#ntsDialog').dialog("destroy");
                $('#ntsDialog').remove();
            }
        }
    }

    /**
     * Error Dialog binding handler
     */
    class NtsErrorDialogBindingHandler implements KnockoutBindingHandler {

        /**
         * Init.
         */
        init(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data.
            var data = valueAccessor();
            var option: any = ko.unwrap(data.option);
            var title: string = ko.unwrap(data.title);
            var headers: Array<any> = ko.unwrap(option.headers);
            var modal: boolean = ko.unwrap(option.modal);
            var show: boolean = ko.unwrap(option.show);
            var buttons: any = ko.unwrap(option.buttons);

            var $dialog = $("<div id='ntsErrorDialog'></div>");

            $('body').append($dialog);
            // Create Buttons
            var dialogbuttons = [];
            for (let button of buttons) {
                dialogbuttons.push({
                    text: ko.unwrap(button.text),
                    "class": ko.unwrap(button.class) + ko.unwrap(button.size) + " " + ko.unwrap(button.color),
                    click: function() { button.click(bindingContext.$data, $dialog) }
                });
            }
            // Calculate width
            var dialogWidth: number = 40 + 35 + 17;
            headers.forEach(function(header, index) {
                if (ko.unwrap(header.visible)) {
                    if (typeof ko.unwrap(header.width) === "number") {
                        dialogWidth += ko.unwrap(header.width);
                    } else {
                        dialogWidth += 200;
                    }

                }
            });
            // Create dialog
            $dialog.dialog({
                title: title,
                modal: modal,

                closeOnEscape: false,
                width: dialogWidth,
                maxHeight: 500,
                buttons: dialogbuttons,
                dialogClass: "no-close",
                open: function() {
                    $(this).parent().find('.ui-dialog-buttonset > button.yes').focus();
                    $(this).parent().find('.ui-dialog-buttonset > button').removeClass('ui-button ui-corner-all ui-widget');
                    $('.ui-widget-overlay').last().css('z-index', 120000);

                    //                    let $headerContainer = $("<div'></div>").addClass("ui-dialog-titlebar-container");
                    //                    $headerContainer.append($("<img>").attr("src", "/nts.uk.com.js.web/lib/nittsu/ui/style/images/error.png").addClass("ui-dialog-titlebar-icon");
                    //                    $headerContainer.append($(this).parent().find(".ui-dialog-title"));
                    //                    $(this).parent().children(".ui-dialog-titlebar").append($headerContainer);
                },
                close: function(event) {
                    bindingContext.$data.option.show(false);
                }
            });
        }

        /**
         * Update
         */
        update(element: any, valueAccessor: () => any, allBindingsAccessor: () => any, viewModel: any, bindingContext: KnockoutBindingContext): void {
            // Get data.
            var data = valueAccessor();
            var option: any = ko.unwrap(data.option);
            var title: string = ko.unwrap(data.title);
            var errors: Array<any> = ko.unwrap(data.errors);
            var headers: Array<any> = ko.unwrap(option.headers);
            var displayrows: number = ko.unwrap(option.displayrows);
            //var maxrows: number = ko.unwrap(option.maxrows);
            var autoclose: boolean = ko.unwrap(option.autoclose);
            var show: boolean = ko.unwrap(option.show);

            var $dialog = $("#ntsErrorDialog");

            if (show == true) {
                
                // Create Error Table
                var $errorboard = $("<div id='error-board'></div>");
                var $errortable = $("<table></table>");
                // Header
                var $header = $("<thead></thead>");
                let $headerRow = $("<tr></tr>");
                $headerRow.append("<th style='display:none;'></th>");

                headers.forEach(function(header, index) {
                    if (ko.unwrap(header.visible)) {
                        let $headerElement = $("<th>" + ko.unwrap(header.text) + "</th>").width(ko.unwrap(header.width));
                        $headerRow.append($headerElement);
                    }
                });

                $header.append($headerRow);
                $errortable.append($header);

                // Body
                var $body = $("<tbody></tbody>");
                errors.forEach(function(error, index) {
                    // Row
                    let $row = $("<tr></tr>");
                    $row.click(function(){
                        error.$control[0].focus();    
                    });
                    $row.append("<td style='display:none;'>" + (index + 1) + "</td>");
                    headers.forEach(function(header) {
                        if (ko.unwrap(header.visible))
                            if (error.hasOwnProperty(ko.unwrap(header.name))) {
                                // TD
                                let $column = $("<td>" + error[ko.unwrap(header.name)] + "</td>");

                                $row.append($column);
                            }
                    });
                    $body.append($row);
                });
                $errortable.append($body);
                $errorboard.append($errortable);
                // Errors over maxrows message
                var $message = $("<div></div>");
                $dialog.html("");
                $dialog.append($errorboard).append($message);

//                $dialog.on("dialogresizestop dialogopen", function() {
                $dialog.on("dialogopen", function() {
                    var maxrowsHeight = 0;
                    var index = 0;
                    $(this).find("table tbody tr").each(function() {
                        if (index < displayrows) {
                            index++;
                            maxrowsHeight += $(this).height();
                        }
                    });
                    maxrowsHeight = maxrowsHeight + 33 + 20 + 20 + 55 +4 + $(this).find("table thead").height();
                    if (maxrowsHeight > $dialog.dialog("option", "maxHeight")) {
                        maxrowsHeight = $dialog.dialog("option", "maxHeight");
                    }
                    $dialog.dialog("option", "height", maxrowsHeight);
                });
                
                $dialog.dialog("open");
            }
            else {
                $dialog.dialog("close");

            }
        }
    }

    ko.bindingHandlers['ntsDialog'] = new NtsDialogBindingHandler();
    ko.bindingHandlers['ntsErrorDialog'] = new NtsErrorDialogBindingHandler();
}