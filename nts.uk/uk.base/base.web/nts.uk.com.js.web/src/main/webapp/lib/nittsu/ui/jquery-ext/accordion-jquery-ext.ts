/// <reference path="../../reference.ts"/>

module nts.uk.ui.jqueryExtentions {
    module accordion {
        $.widget("ui.accordion", $.ui.accordion, {
            _create: function() {
                this["tabindex"] = parseInt(this.element.attr("tabindex"), 10);
                this["useTabindex"] = this["tabindex"] >= 0;
                this.element.removeAttr("tabindex");
                return this._super();
            },
            _refresh: function() {
                this._super();
                if (this.useTabindex) { 
                    if (!this.active.length) {
                        this.headers.eq(0).attr("tabindex", this.tabindex);
                    } else {
                        this.active.attr({
                            tabIndex: this.tabindex
                        })
                    }
                } else {
                    this.headers.eq(0).removeAttr("tabindex");
                }
            },
            _toggle: function(data) {
                this._super(data);
                var toShow = data.newPanel;
                if (this.useTabindex) {
                    toShow.prev().attr({ tabIndex: this.tabindex });
                } else {
                    toShow.prev().removeAttr("tabindex");
                }
            },
            _keydown: function(event) {
                if (event.altKey || event.ctrlKey || !this.useTabindex) {
                    return;
                }
    
                var keyCode = $.ui.keyCode,
                    length = this.headers.length,
                    currentIndex = this.headers.index(event.target),
                    toFocus = false;
    
                switch (event.keyCode) {
                    case keyCode.RIGHT:
                    case keyCode.DOWN:
                        toFocus = this.headers[(currentIndex + 1) % length];
                        break;
                    case keyCode.LEFT:
                    case keyCode.UP:
                        toFocus = this.headers[(currentIndex - 1 + length) % length];
                        break;
                    case keyCode.SPACE:
                    case keyCode.ENTER:
                        this._eventHandler(event);
                        break;
                    case keyCode.HOME:
                        toFocus = this.headers[0];
                        break;
                    case keyCode.END:
                        toFocus = this.headers[length - 1];
                        break;
                }
    
                if (toFocus) {
                    $(event.target).removeAttr("tabindex");
                    $(toFocus).attr("tabindex", this.tabindex);
                    $(toFocus).trigger("focus");
                    event.preventDefault();
                }
            }
        });
    }
}