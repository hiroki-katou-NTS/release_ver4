﻿/*!@license
 * Infragistics.Web.ClientUI ColorPickerSplitButton 16.1.20161.2145
 *
 * Copyright (c) 2011-2016 Infragistics Inc.
 * <Licensing info>
 *
 * http://www.infragistics.com/
 *
 * Depends on:
 *   jquery-1.9.1.js
 *   jquery.ui.core.js
 *   jquery.ui.widget.js
 *   infragistics.util.js
 *   infragistics.ui.shared.js
 *   infragistics.ui.popover.js
 *   infragistics.ui.toolbarbutton.js
 *   infragistics.ui.splitbutton.js
 *   infragistics.ui.colorpicker.js
 */

/*global jQuery */
if (typeof jQuery !== "function") {
    throw new Error("jQuery is undefined");
}

(function ($) {
    /*
       The igColorPickerSplitButton is a jQuery based widget.
   */
    $.widget("ui.igColorPickerSplitButton", $.ui.igSplitButton, {
        options: {
            /* type="array" Button items. */
            items: [],
            /* type="string" Default color. */
            defaultColor: "#000",
            /* type="boolean" With or withour icon. */
            hasDefaultIcon: true
        },
        _create: function () {
            $.ui.igSplitButton.prototype._create.call(this);
            this._setButtonColorIndicator(this.options.defaultColor);
        },
        _createItemsList: function () {
            var popoverCont,
                popoverOptions;
            this._options.popover = $('<div id="' + this._id("_popover") + '"></div>')
                .appendTo(this.element);

            // D.G. 14 December 2015 Added check for touch Mozilla Firefox Bug 211118
            if ($.ig.util.isTouchDevice() || $("html").hasClass("touch")) {
                popoverOptions = {
				    direction: "bottom",
				    closeOnBlur: false,
				    width: 480,
                    height: 360,
                    maxWidth: 480,
				    maxHeight: 360,
				    defaultMaxWidth: null
				};
            } else {
                 popoverOptions = {
				    direction: "bottom",
				    closeOnBlur: false,
				    width: 220,
				    defaultMaxWidth: null
				};
            }
            this._options.popover.igPopover(popoverOptions);
            popoverCont = this._options.popover.igPopover("container");
            this._options.itemsList = popoverCont.igColorPicker();
        },
        _attachEvents: function () {
            $.ui.igSplitButton.prototype._attachEvents.call(this);
            this._options.itemsList.on("igcolorpickercolorselected",
                $.proxy(this._onColorSelect, this));
            this._options.defaultButton.on("igtoolbarbuttonclick",
                $.proxy(this._onDefaultButtonClick, this));
            this._options.itemsList.parent().parent().on("mousedown",
                $.proxy(this._preventCollapsing, this));
        },
        collapse: function (e) {
            /* Collapse the widget. */
            var _opt = this._options;

            _opt.popover.igPopover("hide");
            this.element.removeClass(this.css.active);
            _opt.expandButton.removeClass(this.css.focus);
            _opt.isExpanded = false;

            if (e) {
                this._triggerCollapsed(e);
            }
        },
        expand: function (e) {
            /* Expands the widget. */
            var _opt = this._options;

            _opt.popover.igPopover("show", this.element);
            this.element.addClass(this.css.active);
            _opt.isExpanded = true;

            if (e) {
                this._triggerExpanded(e);
            }
        },
        _onItemClick: function () {
            return false;
        },
        _onColorSelect: function (e, ui) {
            this._setButtonColorIndicator(ui.color);
            this.options.defaultColor = ui.color;
            this._trigger("colorSelected", e, { value: ui.color, item: this.element });
            this.collapse();
        },
        setColor: function (color) {
            /*Sets the color of the split button*/
            this._options.itemsList.igColorPicker("selectColor", color);
            this._setButtonColorIndicator(color);
            this.options.defaultColor = color;
            this.collapse();
        },
        _onDefaultButtonClick: function (e) {
            e.stopPropagation();
            this._trigger("colorSelected", e,
                { value: this.options.defaultColor, item: this.element });
        },
        _setButtonColorIndicator: function (color) {
            this._options.defaultButton.children(":first").css("border-bottom-color", color);
            if (!this.options.hasDefaultIcon) {
                this._options.defaultButton.children(":first").css("background-color", color);
                this._options.defaultButton.children(":first").css("background-image", "none");
            }
        },
        _preventCollapsing: function () {
            return false;
        },
         _setOption: function( key, value ) {
            var options = this.options,
                self = this;

            if (options[ key ] === value) {
                return;
            }

            $.Widget.prototype._setOption.apply(this, arguments);

            switch (key) {
                case "defaultColor":
                    self.setColor(value);
                    break;
            }
        },
        destroy: function () {
            /* Destroys the widget. */
            $.ui.igSplitButton.prototype.destroy.apply(this, arguments);
        }
    });

    $.extend($.ui.igColorPickerSplitButton, { version: "16.1.20161.2145" });
}(jQuery));
