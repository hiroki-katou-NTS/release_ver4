﻿
/*!@license
 * Infragistics.Web.ClientUI Tree Grid 16.1.20161.2145
 *
 * Copyright (c) 2011-2016 Infragistics Inc.
 *
 * http://www.infragistics.com/
 *
 * Depends on:
 *	jquery-1.9.1.js
 *	jquery.ui.core.js
 *	jquery.ui.widget.js
 *	infragistics.dataSource.js
 *	infragistics.ui.shared.js
 *	infragistics.ui.treegrid.js
 *	infragistics.util.js
 *	infragistics.ui.grid.framework.js
 *	infragistics.ui.grid.hiding.js
 */

/*global jQuery */
if (typeof jQuery !== "function") {
	throw new Error("jQuery is undefined");
}
(function ($) {
	/*
		igTreeGridHiding widget. The widget is pluggable to the element where the treegrid is instantiated and the actual igTreeGrid object doesn't know about this
		the filtering widget just attaches its functionality to the treegrid
		igTreeGridHiding is extending igGrid Hiding
	*/
	$.widget("ui.igTreeGridHiding", $.ui.igGridHiding, {
		css: {},
		options: {
		},
		_create: function () {
			this.element.data(
				$.ui.igGridHiding.prototype.widgetName,
				this.element.data($.ui.igTreeGridHiding.prototype.widgetName)
			);
			$.ui.igGridHiding.prototype._create.apply(this, arguments);
		},
		destroy: function () {
			$.ui.igGridHiding.prototype.destroy.apply(this, arguments);
			this.element.removeData($.ui.igGridHiding.prototype.widgetName);
		},
		_injectGrid: function () {
			$.ui.igGridHiding.prototype._injectGrid.apply(this, arguments);
		}
	});
	$.extend($.ui.igTreeGridHiding, { version: "16.1.20161.2145" });
}(jQuery));



