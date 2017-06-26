/*!@license
* Infragistics.Web.ClientUI Pivot Data Selector localization resources 16.2.20162.2040
*
* Copyright (c) 2011-2016 Infragistics Inc.
*
* http://www.infragistics.com/
*
*/

/*global define, jQuery */
(function (factory) {
	if (typeof define === "function" && define.amd) {
		define( ["jquery"], factory );
	} else {
		factory(jQuery);
	}
}
(function ($) {
    $.ig = $.ig || {};

    if (!$.ig.PivotDataSelector) {
        $.ig.PivotDataSelector = {};

        $.extend($.ig.PivotDataSelector, {
            locale: {
                invalidBaseElement: " is not supported as base element. Use DIV instead.",
                catalog: "Catalog",
                cube: "Cube",
                measureGroup: "Measure Group",
                measureGroupAll: "(All)",
                rows: "Rows",
                columns: "Columns",
                measures: "Measures",
                filters: "Filters",
                deferUpdate: "Defer Update",
                updateLayout: "Update Layout",
                selectAll: "Select All"
            }
        });
    }
}));// REMOVE_FROM_COMBINED_FILES