﻿/*!@license
 * Infragistics.Web.ClientUI Grid Responsive 16.1.20161.2145
 *
 * Copyright (c) 2011-2016 Infragistics Inc.
 *
 * http://www.infragistics.com/
 *
 * Depends on:
 *	jquery-1.9.1.js
 *	jquery.ui.core.js
 *	jquery.ui.widget.js
 *	infragistics.ui.grid.framework.js
 *	infragistics.ui.tree.js
 *	infragistics.ui.shared.js
 *	infragistics.dataSource.js
 *	infragistics.util.js
 */

/*global jQuery, Class, window */
/*jshint -W106 */
if (typeof jQuery !== "function") {
	throw new Error("jQuery is undefined");
}
(function ($) {

	"use strict";
	$.widget("ui.igGridResponsive", {
		options: {
			/* type="array" A list of column settings that specifies how columns will react based on the environment the grid is run on. */
			columnSettings: [
				{
					/* type="string" Column key. This is a required property in every column setting if columnIndex is not set. */
					columnKey: null,
					/* type="number" Column index. Can be used in place of column key. The preferred way of populating a column setting is to always use the column keys as identifiers. */
					columnIndex: null,
					/* type="string" A list of predefined classes to decide element's visibility on. */
					classes: "",
					/* type="object" A configuration object to use for the responsive functionality. Uses the keys defined in the widget's responsiveModes object. The classes property is not used if this one is set. */
					configuration: null
				}
			],
			/* type="bool" If this option is set to true an igResponsiveContainer widget will be attached to the igGrid control which will notify the feature when changes in the width of the container occur. */
			reactOnContainerWidthChanges: true,
			/* type="bool" If this option is set to true the widget will ensure the grid's width is always set to 100%. */
			forceResponsiveGridWidth: true,
			/* type="number" The amount of pixels the window needs to resize with for the grid to respond. */
			responsiveSensitivity: 20,
			/* type="object" The recognized types of environments and their configuration. */
			responsiveModes: null,
			/* type="bool" Enable or disable the responsive vertical rendering for the grid. */
			enableVerticalRendering: true,
			/* type="string|number|null" The window's width under which the grid will render its contents vertically.
				string The width in a (px) string
				number The width as a number
				null type="object" The grid will determine when to render this mode automatically.
			*/
			windowWidthToRenderVertically: null,
			/* type="string|number" The width of the properties column when vertical rendering is enabled
				string The width in a (%) string
				number The width as a number in percents
			*/
			propertiesColumnWidth: "50%",
			/* type="string|number" The width of the values column when vertical rendering is enabled
				string The width in a (%) string
				number The width as a number in percents
			*/
			valuesColumnWidth: "50%",
			/* type="object" When windowWidthToRenderVertically is null, determine minimal widths columns can take before
				forcing vertical rendering for the grid
			*/
			allowedColumnWidthPerType: {
				/* type="number" minimal width in pixels string columns can take before forcing vertical rendering */
				string: 120,
				/* type="number" minimal width in pixels number columns can take before forcing vertical rendering */
				number: 50,
				/* type="number" minimal width in pixels bool columns can take before forcing vertical rendering */
				bool: 50,
				/* type="number" minimal width in pixels date columns can take before forcing vertical rendering */
				date: 80,
				/* type="number" minimal width in pixels object columns can take before forcing vertical rendering */
				object: 150
			},
			/* type="object" Specifies a template to render a record with in a list-view style layout per mode. */
			singleColumnTemplate: null,
			/* type="bool" Enables/disables feature inheritance for the child layouts. NOTE: It only applies for igHierarchicalGrid. */
			inherit: false
		},
		events: {
			/* cancel="true" Event fired before a hiding operation is executed on a collection of columns.
			The handler function takes arguments evt and ui.
			Use ui.owner to get the reference to the igGridResponsive widget.
			Use ui.owner.grid to get the reference to the igGrid widget.
			Use ui.columnIndex to get the hidden column index. Has a value only if the column's key is a number.
			Use ui.columnKey to get the hidden column key. Has a value only if the column's key is a string.
			*/
			responsiveColumnHiding: "responsiveColumnHiding",
			/* Event fired after a hiding operation is executed on the collection of columns.
			The handler function takes arguments evt and ui.
			Use ui.owner to get the reference to the igGridResponsive widget.
			Use ui.owner.grid to get the reference to the igGrid widget.
			Use ui.columnIndex to get the hidden column index. Has a value only if the column's key is a number.
			Use ui.columnKey to get the hidden column key. Has a value only if the column's key is a string.
			*/
			responsiveColumnHidden: "responsiveColumnHidden",
			/* cancel="true" Event fired before a showing operation is executed on a collection of columns.
			The handler function takes arguments evt and ui.
			Use ui.owner to get the reference to the igGridResponsive widget.
			Use ui.owner.grid to get the reference to the igGrid widget.
			Use ui.columnIndex to get the shown column index. Has a value only if the column's key is a number.
			Use ui.columnKey to get the shown column key. Has a value only if the column's key is a string.
			*/
			responsiveColumnShowing: "responsiveColumnShowing",
			/* Event fired after a showing operation is executed on the collection of columns.
			The handler function takes arguments evt and ui.
			Use ui.owner to get the reference to the igGridResponsive widget.
			Use ui.owner.grid to get the reference to the igGrid widget.
			Use ui.columnIndex to get the shown column index. Has a value only if the column's key is a number.
			Use ui.columnKey to get the shown column key. Has a value only if the column's key is a string.
			*/
			responsiveColumnShown: "responsiveColumnShown",
			/* Event which is fired when the widget detects an environment change.
			The handler function takes arguments evt and ui.
			Use ui.owner to get the reference to the igGridResponsive widget.
			Use ui.owner.grid to get the reference to the igGrid widget.
			Use ui.previousMode to get the previously assumed mode.
			Use ui.mode to get the newly assumed mode.
			*/
			responsiveModeChanged: "responsiveModeChanged"
		},
		css: {
			/* Classes applied to the grid table when vertical rendering is enabled */
			verticalContainerCssClass: "ui-iggrid-responsive-vertical"
		},
		_createWidget: function () {
			/* !Strip dummy objects from options, because they are defined for documentation purposes only! */
			this.options.columnSettings = [];
			$.Widget.prototype._createWidget.apply(this, arguments);
		},
		_create: function () {
			/* stores the identifier sent by the responsive container for the callback */
			this._callBackId = null;
			/* stores a reference to the responsive container for easy use */
			this._responsive = null;
			/* stores a flag on whether the responsiveContainer was created exclusively for this widget */
			this._exclusiveContainer = false;
			/* stores the currently recognized environment */
			this._mode = "";
			/* stores the environment modes */
			this._modes = this.options.responsiveModes || {
				desktop: "infragistics",
				tablet: "infragistics",
				phone: "infragistics"
			};
			this._modes = this._initializeModeRecognizers(this._modes);
			/* stores if we check for visibility by class or by configuration */
			this._hiddenByClass = this._flagClassConfiguration();
			/* stores the default templates per column and the default row template */
			this._defaultColumnTemplates = null;
			/* stores the default formatters per column */
			this._defaultColumnFormatters = null;
			/* stores a footers % displacement due to borders */
			this._footersDisplacement = 0.1;
			/* stores a reference to the original renderRecord */
			this._originalRenderRecord = null;
			/* stores a reference to the modified renderRecord */
			this._newRenderRecord = null;
			/* stores a parsed width to kick virtual rendering under */
			this._vrw = typeof this.options.windowWidthToRenderVertically === "string" ?
				parseInt(this.options.windowWidthToRenderVertically, 10) :
				this.options.windowWidthToRenderVertically;
			/* stores if grid is rendered / should be rendered vertically */
			this._vr = null;
		},
		_setOption: function (key, value) {
			switch (key) {
				case "propertiesColumnWidth":
				case "valuesColumnWidth":
					throw new Error($.ig.Grid.locale.optionChangeNotSupported.replace("{optionName}", key));
				case "responsiveModes":
					this._modes = value;
					this._modes = this._initializeModeRecognizers(this._modes);
					break;
				case "reactOnContainerWidthChanges":
					if (value === true) {
						this._activateContainer();
					} else {
						this._deactivateContainer();
					}
					break;
				case "enableVerticalRendering":
					this.options.enableVerticalRendering = value;
					if (this._shouldRenderVertically()) {
						if (!this._vr) {
							this._enableNoHeaderLayout(true, false);
						}
					} else {
						if (this._vr) {
							this._disableNoHeaderLayout();
						}
					}
					break;
				case "windowWidthToRenderVertically":
					this._vrw = typeof value === "string" ? parseInt(value, 10) : value;
					break;
			}
			$.Widget.prototype._setOption.apply(this, arguments);
			this._hiddenByClass = this._flagClassConfiguration();
		},
		destroy: function () {
			/* Destroys the responsive widget. */
			this._deactivateContainer();
			if (this._initialGridRenderedHandler) {
				this.grid.element.unbind("iggridrendered", this._initialGridRenderedHandler);
				this.grid.element.unbind("iggridheaderrendering", this._gridHeaderRenderingHandler);
			}
			this.grid.element.unbind("iggrid_heightchanged", this._gridContainerHeightHandler);
			$("#" + this.grid.element[ 0 ].id + "_responsive_test_container").remove();
			this.grid._renderRecord = this._originalRenderRecord;
			this.grid._renderColgroup = this._originalRenderColgroup;
			this.grid._renderCell = this._originalRenderCell;
			this.grid.renderNewRow = this._originalRenderNewRow;
			$.Widget.prototype.destroy.call(this);
			return this;
		},
		getCurrentResponsiveMode: function () {
			/* Returns the currently active responsive mode.
			*/
			return this._mode;
		},
		_activateContainer: function () {
			// Checks for the presence of the responsive container and
			// initializes it if needed. Adds the resizing callback.
			if (!this._responsive || typeof this._responsive.addCallback !== "function") {
				this._responsive = this.grid.element.closest(".ui-widget")
					.igResponsiveContainer().data("igResponsiveContainer");
				this._exclusiveContainer = true;
			}
			this._callBackId = this._responsive.addCallback(this._containerResized,
				this, this.options.responsiveSensitivity, "x");
		},
		_deactivateContainer: function () {
			// Deactivates further callbacks and destroys the responsiveContainer
			// widget if it was created exclusively for the needs of the feature.
			if (typeof this._callBackId === "number") {
				this._responsive.removeCallback(this._callBackId);
				this._callBackId = null;
			}
			if (this._exclusiveContainer === true) {
				this._responsive.destroy();
				delete this._responsive;
				this._exclusiveContainer = false;
			}
		},
		_renderTestElement: function () {
			// Renders an element with absolute position outside the browser's viewport
			// that will be used to check if a set of classes make it hidden or not.
			$("<div></div>")
				.attr("id", this.grid.element[ 0 ].id + "_responsive_test_container")
				.css("position", "fixed")
				.css("height", "0px")
				.css("top", "-100px")
				.text("&nbsp;")
				.appendTo(this.grid.container());
		},
		_initializeModeRecognizers: function () {
			// Changes the values of the provided mode definitions to objects of the appropriate classes.
			var self = this, transformed = {}, nval;
			$.each(this._modes, function (key, value) {
				if (typeof value === "string") {
					/* strings should be translated to classes in the ig namespace */
					nval = value.substring(0, 1).toUpperCase() + value.substring(1);
					nval = new $.ig[ nval + "Mode" ]({
						/* the recognition is key based */
						key: key,
						/* we add the feature's visiblity tester if it's needed */
						visibilityTester: $.proxy(self._checkVisibilityByClass, self)
					});
				} else if (typeof value === "object" && !value.isActive) {
					/* if an object with dimensions is provided we'll use the default ResponsiveMode recognizer */
					nval = new $.ig.ResponsiveMode({
						minWidth: value.minWidth || -1,
						maxWidth: value.maxWidth || Number.MAX_VALUE,
						minHeight: value.minHeight || -1,
						maxHeight: value.maxHeight || Number.MAX_VALUE
					});
				} else {
					nval = value;
				}
				transformed[ key ] = nval;
			});
			return transformed;
		},
		_flagClassConfiguration: function () {
			// Checks for conflicts in the visibility options and decides which ones to use
			var byClass = false;
			$.each(this.options.columnSettings, function () {
				if (this.classes) {
					byClass = true;
					return false;
				}
			});
			return byClass;
		},
		_getDefaultColumnTemplates: function () {
			var cache = {}, col, i;
			for (i = 0; i < this.grid.options.columns.length; i++) {
				col = this.grid.options.columns[ i ];
				if (col.template) {
					if (col.key) {
						cache[ col.key ] = col.template;
					} else {
						cache[ i ] = col.template;
					}
				}
			}
			return cache;
		},
		_getDefaultColumnFormatters: function () {
			var cache = {}, col, i;
			for (i = 0; i < this.grid.options.columns.length; i++) {
				col = this.grid.options.columns[ i ];
				if (col.formatter) {
					if (col.key) {
						cache[ col.key ] = col.formatter;
					} else {
						cache[ i ] = col.formatter;
					}
				}
			}
			return cache;
		},
		_checkVisibilityByClass: function (classes) {
			/* Using the created test container this function applies the provided
			classes and checks if the container is visible with them. */
			var testContainer = $("#" + this.grid.element[ 0 ].id + "_responsive_test_container");
			/* remove all classes to ensure initial state */
			testContainer.removeClass();
			/* add classes */
			testContainer.addClass(classes);
			return testContainer.is(":visible");
		},
		_gridReady: function () {
			return !(this.grid._loadingIndicator && this.grid._loadingIndicator._indicator.is(":visible"));
		},
		_shouldRenderVertically: function () {
			if (!this.options.enableVerticalRendering) {
				return false;
			}
			if (this._vrw === null || this._vrw === undefined) {
				return this._shouldRenderVerticallyByDefault();
			}
			if ($(window).width() < this._vrw) {
				return true;
			}
			if ($(window).width() >= this._vrw) {
				return false;
			}
			return false;
		},
		_shouldRenderVerticallyByDefault: function () {
			var i, gw = this.grid.element.width(), gp = 100, cw, cols = [], col, tcocww = 0, p, aw;
			for (i = 0; i < this.grid._visibleColumns().length; i++) {
				col = this.grid._visibleColumns()[ i ];
				if (col.width) {
					cw = -1;
					if (typeof col.width === "string") {
						if (col.width.endsWith("%")) {
							cw = parseInt(col.width, 10);
							gp -= cw;
						} else {
							gw -= parseInt(col.width, 10);
						}
					} else {
						gw -= col.width;
					}
					if (cw !== -1) {
						cols.push({ key: col.key || i, width: cw, type: col.dataType });
					}
					continue;
				}
				tcocww++;
				cols.push({ key: col.key || i, width: -1, type: col.dataType });
			}
			for (i = 0; i < cols.length; i++) {
				p = cols[ i ].width === -1 ? gp / tcocww : cols[ i ].width;
				aw = (p / 100) * gw;
				if (aw <= this.options.allowedColumnWidthPerType[ cols[ i ].type ]) {
					return true;
				}
			}
			return false;
		},
		_isSingleColumn: function () {
			if (this.options.singleColumnTemplate && this.options.singleColumnTemplate[ this._mode ]) {
				return true;
			}
		},
		_updateGridSync: function (initial, full) {
			// Ensures updateGrid will only be called when no internal rendering operations are in place
			var self = this;
			if (!this._gridReady()) {
				setTimeout(function () {
					self._updateGridSync(initial, full);
				}, 50);
				return;
			}
			return self._updateGrid(initial, full);
		},
		_updateGrid: function (initial, full) {
			// Updates the grid's settings based on the current mode and rerenders the grid if needed.
			var result = [], applyTemplatesFormatters = false, colsToShow, colsToHide, self = this, i;
			this._vr = this._vr === null ? this._shouldRenderVertically() : this._vr;
			/* reset the visible columns cache */
			delete this.grid._visibleColumnsArray;
			if (this._vr) {
				this.grid.element.addClass(this.css.verticalContainerCssClass);
			}
			/* We update the columns by class if no configuration is set */
			if (this._hiddenByClass === true) {
				result = this._updateColumnsByClass();
			}
			if (full === true) {
				if (this._hiddenByClass === false) {
					result = this._updateColumns();
				}
				applyTemplatesFormatters = this._updateTemplatesFormatters();
			}
			colsToShow = result[ 0 ] || [];
			colsToHide = result[ 1 ] || [];
			if (colsToShow.length === 0 && colsToHide.length === 0) {
				/* this is a special case in which we won't hide/show anything but we still have new templates
				this means we need to rerender the grid with those */
				if (applyTemplatesFormatters === true && initial === false) {
					return true;
				}
				return;
			}
			if (initial === false) {
				this.grid._loadingIndicator.show();
				setTimeout(function () {
					self.grid._setHiddenColumns(colsToShow, false, initial);
					for (i = 0; i < colsToShow.length; i++) {
						self._trigger(self.events.responsiveColumnShown, null,
							self._getArgsByColumn(colsToShow[ i ]));
					}
					self.grid._setHiddenColumns(colsToHide, true, initial);
					for (i = 0; i < colsToHide.length; i++) {
						self._trigger(self.events.responsiveColumnHidden, null,
							self._getArgsByColumn(colsToHide[ i ]));
					}
					if (initial === false) {
						self.grid._loadingIndicator.hide();
					}
					/* finally update grid */
					self._setMaxWidthOnGrid();
				}, 0);
			} else {
				/* grid is not yet rendered so we can just set the column properties and let
				the default rendering do the rest */
				$.each(colsToShow, function () {
					this.hidden = false;
				});
				$.each(colsToHide, function () {
					this.hidden = true;
				});
			}
		},
		_updateColumnsByClass: function () {
			// Updates the grid's column visiblity based on the classes set in the options
			var i, cs = this.options.columnSettings, col, colsToHide = [],
				colsToShow = [], noCancel, hidden, visible;
			for (i = 0; i < cs.length; i++) {
				if (cs[ i ].classes) {
					col = this._getGridColumnBySetting(cs[ i ]);
					if (!col) {
						// wrongly set columnSettings may result in not being able to find the corresponsing grid column
						// in this case we'll just skip this iteration
						continue;
					}
					hidden = col.hidden || false;
					visible = this._checkVisibilityByClass(cs[ i ].classes);
					if (hidden === true && visible === true) {
						noCancel = this._trigger(this.events.responsiveColumnShowing,
							null, this._getArgsByColumnSetting(cs[ i ]));
						if (noCancel) {
							colsToShow.push(col);
						}
					}
					if (hidden === false && visible === false) {
						noCancel = this._trigger(this.events.responsiveColumnHiding,
							null, this._getArgsByColumnSetting(cs[ i ]));
						if (noCancel) {
							colsToHide.push(col);
						}
					}
				}
			}
			return [ colsToShow, colsToHide ];
		},
		_updateColumns: function () {
			// Updates the grid's column visiblity based on the explicit configuration
			var i, cs = this.options.columnSettings, col, colsToHide = [],
				colsToShow = [], noCancel, m = this._mode, hidden, visible;
			for (i = 0; i < cs.length; i++) {
				if (cs[ i ].configuration) {
					col = this._getGridColumnBySetting(cs[ i ]);
					if (!col) {
						// wrongly set columnSettings may result in not being able to find the corresponsing grid column
						// in this case we'll just skip this iteration
						continue;
					}
					hidden = col.hidden || false;
					visible = !(cs[ i ].configuration[ m ] && cs[ i ].configuration[ m ].hidden);
					if (hidden === true && visible === true) {
						noCancel = this._trigger(this.events.responsiveColumnShowing,
							null, this._getArgsByColumnSetting(cs[ i ]));
						if (noCancel) {
							colsToShow.push(col);
						}
					}
					if (hidden === false && visible === false) {
						noCancel = this._trigger(this.events.responsiveColumnHiding,
							null, this._getArgsByColumnSetting(cs[ i ]));
						if (noCancel) {
							colsToHide.push(col);
						}
					}
				}
			}
			return [ colsToShow, colsToHide ];
		},
		_updateTemplatesFormatters: function () {
			// Updates the grid's templates based on the explicit configuration
			var i, cs = this.options.columnSettings, nt, nf, updated = false, col, m = this._mode,
				jsRndr = String(this.grid.options.templatingEngine).toLowerCase() === "jsrender";
			for (i = 0; i < cs.length; i++) {
				col = this._getGridColumnBySetting(cs[ i ]);
				if (!col) {
					// wrongly set columnSettings may result in not being able to find the corresponsing grid column
					// in this case we'll just skip this iteration
					continue;
				}
				if (cs[ i ].configuration && cs[ i ].configuration[ m ] &&
					cs[ i ].configuration[ m ].template) {
					// we've explicitly set a template for the current mode
					nt = cs[ i ].configuration[ m ].template;
				} else {
					// no template is set which means we'll try to go back to the default one
					nt = col.key ? this._defaultColumnTemplates[ col.key ] :
						this._defaultColumnTemplates[ $.inArray(col, this.grid.options.columns) ];
				}
				if (cs[ i ].configuration && cs[ i ].configuration[ m ] &&
					cs[ i ].configuration[ m ].formatter) {
					nf = cs[ i ].configuration[ m ].formatter;
					nf = $.type(nf) === "string" ? window[ nf ] : nf;
				} else {
					nf = col.key ? this._defaultColumnFormatters[ col.key ] :
						this._defaultColumnFormatters[ $.inArray(col, this.grid.options.columns) ];
				}
				if (col.template !== nt) {
					col.template = nt;
					updated = true;
				}
				if (col.formatter !== nf) {
					col.formatter = nf;
					updated = true;
				}
			}
			/* finally reset internal row template cache so the new templates can be applied */
			if (updated === true) {
				if (!this.grid._tmplWrappers) {
					this.grid._tmplWrappers = jsRndr ? $.render : {};
				}
				this.grid._setTemplateDefinition(jsRndr);
			}
			return updated;
		},
		_executeTemplate: function (data) {
			if (this._jsr) {
				return $.render[ this.grid.id() + "_responsiveSct_" + this._mode ](data)
					.replace("<td", "").replace("</td>", "");
			}
			return $.ig.tmpl(this.options.singleColumnTemplate[ this._mode ], data)
				.replace("<td", "").replace("</td>", "");
		},
		_renderRecord: function (data, index) {
			if (this._vr) {
				return this._renderRecordVerticalGrid(data, index);
			}
			if (this._scr) {
				return this._renderRecordSingleColumnGrid(data, index);
			}
			return this._originalRenderRecord(data, index);
		},
		_renderRecordVerticalGrid: function (data, index) {
			var i = 0, str = "", tstr, alt, vc = this.grid._visibleColumns(),
				key = this.grid.options.primaryKey;
			alt = index % 2 !== 0 && this.grid.options.alternateRowStyles;
			for (i = 0; i < vc.length; i++) {
				str += "<tr";
				if (alt) {
					str += " class=\"" + this.grid.css.recordAltClass + "\"";
				}
				str += " data-id=\"" + data[ key ] + "\"";
				str += " data-col-key=\"" + vc[ i ].key + "\"";
				str += "><td>";
				str += vc[ i ].headerText + "</td>";
				str += "<td aria-readonly=\"true\"";
				if (vc[ i ].template && vc[ i ].template.length) {
					tstr = this.grid._renderTemplatedCell(data, vc[ i ]);
					if (tstr.indexOf("<td") === 0) {
						str += tstr.substring(3);
					} else {
						str += ">" + tstr;
					}
				} else {
					str += ">" + this.grid._renderCell(data[ vc[ i ].key ], vc[ i ]);
				}
				str += "</td></tr>";
			}
			return str;
		},
		_renderRecordSingleColumnGrid: function (data, index) {
			var str = "<tr", pk = this.grid.options.primaryKey, formattedData, key, tmplRes;
			if (index % 2 !== 0 && this.options.alternateRowStyles) {
				str += " class=\"" + this.grid.css.recordAltClass + "\"";
			}
			if (pk !== null && pk !== undefined) {
				/*jscs:disable*/
				str += " data-id=\"" + this.grid._kval_from_key(pk, data) + "\"";
			} else if (data.ig_pk !== null && pk !== undefined) {
				str += " data-id=\"" + data.ig_pk + "\"";
				/*jscs:enable*/
			}
			str += "><td aria-readonly=\"true\"";
			/* format the cells */
			formattedData = $.extend(true, {}, data);
			for (key in formattedData) {
				if (formattedData.hasOwnProperty(key)) {
					formattedData[ key ] = this.grid._renderCell(data[ key ],
						this.grid.columnByKey(key) || {}, formattedData);
				}
			}
			tmplRes = this._executeTemplate(formattedData);
			if (tmplRes.indexOf("<td") === 0) {
				str += tmplRes.substring(3);
			} else {
				str += ">" + tmplRes;
			}
			/* close the td */
			str += "</td></tr>";
			return str;
		},
		_renderNewRow: function (rec) {
			var tbody, go, index, virt;
			if (this._vr) {
				tbody = this.element.children("tbody");
				go = this.grid.options;
				virt = go.virtualization === true || go.rowVirtualization === true;
				if (virt) {
					this._renderVirtualRecordsContinuous();
					this._startRowIndex = 0;
					this.virtualScrollTo(this._totalRowCount);
				} else {
					index = this.grid.dataSource.dataView().length - 1;
					tbody.append(this.grid._renderRecord(rec, index));
				}
			} else {
				this._originalRenderNewRow(rec);
			}
		},
		_renderCell: function (val, col, record, displayStyle, returnObject) {
			var type = col.dataType, format = col.format, o = this.grid.options, auto = o.autoFormat;
			if (record) {
				val = this.grid.dataSource.getCellValue(col.key, record);
			}
			val = this.grid._fixDate(val, col);
			if (col.formatter) {
				return col.formatter(val, record, this._mode);
			}
			if (!format && type === "bool" && o.renderCheckboxes) {
				format = "checkbox";
			}
			if (format === "checkbox" && type !== "bool") {
				format = null;
			}
			type = (type === "date" || type === "number") ? type : "";
			if (format || ((auto === true || auto === "dateandnumber") && type) || (auto && auto === type)) {
				return $.ig.formatter(val, type, format, true, o.enableUTCDates, displayStyle);
			}
			if (returnObject) {
				return val;
			}
			return (val || val === 0 || val === false) ? val.toString() : "&nbsp;";
		},
		_renderColgroup: function (table, isHeader, isFooter, autofitLastColumn) {
			var colgroup, fcw, scw;
			this._vr = this._vr === null ? this._shouldRenderVertically() : this._vr;
			this._scr = this._scr === undefined || this._scr === null ? this._isSingleColumn() : this._scr;
			if (!this._vr && !this._scr) {
				this._originalRenderColgroup(table, isHeader, isFooter, autofitLastColumn);
				return;
			}
			colgroup = $(table).find("colgroup");
			if (colgroup.length === 0) {
				colgroup = $("<colgroup></colgroup>").prependTo(table);
			}
			colgroup.empty();
			if (this._vr) {
				fcw = typeof this.options.propertiesColumnWidth === "string" ?
					parseInt(this.options.propertiesColumnWidth, 10) : this.options.propertiesColumnWidth;
				scw = typeof this.options.valuesColumnWidth === "string" ?
					parseInt(this.options.valuesColumnWidth, 10) : this.options.valuesColumnWidth;
				colgroup.append("<col width=\"" + fcw + "%\"></col><col width=\"" + scw + "%\"></col>");
			} else if (this._scr) {
				/* single column layout requires a single column */
				colgroup.append("<col width=\"100%\"></col>");
			}
		},
		_enableNoHeaderLayout: function (vr, scr) {
			this.grid.element.addClass(this.css.verticalContainerCssClass);
			if (!this.grid.options.showHeader ||
				!this.grid.options.fixedHeaders ||
				this.grid.options.height === null) {
				this.grid.headersTable().children("thead").css("display", "none");
			} else {
				this.grid.headersTable().css("position", "absolute");
				this.grid.headersTable().css("top", "-100px");
			}
			this._vr = vr;
			this._scr = scr;
			this._modifySortingStyles();
			this._disableUpdating();
			this.grid._rerenderColgroups();
			this.grid._renderData();
		},
		_disableNoHeaderLayout: function () {
			this.grid.element.removeClass(this.css.verticalContainerCssClass);
			if (!this.grid.options.showHeader ||
				!this.grid.options.fixedHeaders ||
				this.grid.options.height === null) {
				this.grid.headersTable().children("thead").css("display", "");
			} else {
				this.grid.headersTable().css("position", "");
				this.grid.headersTable().css("top", "");
			}
			this._scr = false;
			this._vr = false;
			this._modifySortingStyles(true);
			this._enableUpdating();
			this.grid._rerenderColgroups();
			this.grid._renderData();
		},
		_disableUpdating: function () {
			if (this.grid.element.data("igGridUpdating")) {
				this._em = this.grid.element.igGridUpdating("option", "editMode");
				this._dm = this.grid.element.igGridUpdating("option", "enableDeleteRow");
				this.grid.element.igGridUpdating("option", "editMode", "none");
				this.grid.element.igGridUpdating("option", "enableDeleteRow", false);
			}
		},
		_enableUpdating: function () {
			if (this.grid.element.data("igGridUpdating")) {
				if (this._em) {
					this.grid.element.igGridUpdating("option", "editMode", this._em);
				}
				if (this._dm) {
					this.grid.element.igGridUpdating("option", "enableDeleteRow", true);
				}
			}
		},
		_getGridColumnBySetting: function (cs) {
			// Gets the column in the grid options corresponding to the provided columnSetting
			// returns undefined if the column cannot be found
			var col;
			if (cs.columnKey && typeof cs.columnKey === "string") {
				col = this.grid.columnByKey(cs.columnKey);
			} else if (cs.columnIndex !== null && cs.columnIndex !== undefined &&
				typeof cs.columnIndex === "number" && cs.columnIndex >= 0 &&
				cs.columnIndex < this.grid.options.columns.length) {
				col = this.grid.options.columns[ cs.columnIndex ];
			}
			return col;
		},
		_getArgsByColumnSetting: function (cs) {
			// Gets the event args per column setting
			return {
				owner: this,
				columnIndex: cs.columnIndex || null,
				columnKey: cs.columnKey || null
			};
		},
		_getArgsByColumn: function (col) {
			// Gets the event args per column
			return {
				owner: this,
				columnIndex: $.inArray(col, this.grid.options.columns),
				columnKey: col.key || null
			};
		},
		_getCurrentMode: function () {
			// Gets the current mode as a string.
			// returns undefined if no mode is detected
			var env;
			$.each(this._modes, function (key) {
				if (this.isActive() === true) {
					env = key;
					return false;
				}
			});
			return env;
		},
		_containerResized: function (nw, nh) {
			// The resposniveContainer callback.
			// Detects mode changes and notifies about them, calls for update if needed.
			var mode = this._getCurrentMode(), prevMode, shouldUpdate = false, shouldRerender = false;
			if (nw <= 0 && nh <= 0) {
				// don't react as the grid is probably hidden
				return;
			}
			this._setMaxWidthOnGrid();
			if (mode && mode !== this._mode) {
				prevMode = this._mode;
				this._mode = mode;
				this._trigger(this.events.responsiveModeChanged, null,
					{ owner: this, previousMode: prevMode, mode: mode });
				shouldUpdate = true;
			}
			if (shouldUpdate || this._hiddenByClass) {
				shouldRerender = this._updateGridSync(false, shouldUpdate);
			}
			if (this._isSingleColumn()) {
				if (!this._scr) {
					this._enableNoHeaderLayout(false, true);
				} else if (shouldUpdate) {
					this.grid._renderData();
				}
			} else {
				if (this._scr) {
					this._disableNoHeaderLayout();
					this._scr = false;
				} else if (shouldRerender) {
					this.grid._renderData();
				}
			}
			if (this._shouldRenderVertically()) {
				if (!this._vr) {
					this._enableNoHeaderLayout(true, false);
				}
			} else {
				if (this._vr) {
					this._disableNoHeaderLayout();
					this._vr = false;
				} else if (shouldRerender) {
					this.grid._renderData();
				}
			}
		},
		_gridHeaderRendering: function (evt, ui) {
			var i;
			if (ui.owner.id() !== this.grid.id()) {
				return;
			}
			this._renderTestElement();
			this._mode = this._getCurrentMode();
			this._defaultColumnTemplates = this._getDefaultColumnTemplates();
			this._defaultColumnFormatters = this._getDefaultColumnFormatters();
			if ($.isArray(this.grid._initialHiddenColumns)) {
				for (i = 0; i < this.grid._initialHiddenColumns.length; i++) {
					this.grid._initialHiddenColumns[ i ].hidden = true;
				}
			}
			this._updateGrid(true, true);
			this.grid._captureInitiallyHiddenColumns();
		},
		_initialGridRendered: function () {
			if (this.options.reactOnContainerWidthChanges) {
				this._activateContainer();
			}
			if (this._vr || this._scr) {
				if (!this.grid.options.showHeader ||
					!this.grid.options.fixedHeaders ||
					this.grid.options.height === null) {
					this.grid.headersTable().children("thead").css("display", "none");
				} else {
					this.grid.headersTable().css("position", "absolute");
					this.grid.headersTable().css("top", "-100px");
				}
				this._disableUpdating();
				this._modifySortingStyles();
			}
			this._alreadyRendered = true;
			this._setMaxWidthOnGrid();
		},
		_dataRendered: function () {
			this._setMaxWidthOnGrid();
		},
		_modifySortingStyles: function (restore) {
			var sorting = this.grid.element.data("igGridSorting");
			if (sorting) {
				if (restore) {
					sorting.options.applySortedColumnCss = this._srs;
				} else {
					this._srs = sorting.options.applySortedColumnCss;
					sorting.options.applySortedColumnCss = false;
				}
			}
		},
		_heightChanged: function () {
			var newHeight = $("#" + this.grid.element[ 0 ].id + "_scroll").height();
			if (this._height !== newHeight) {
				this._height = newHeight;
				this.grid._adjustLastColumnWidth(false);
				this._setMaxWidthOnGrid();
			}
		},
		_setMaxWidthOnGrid: function () {
			var sbw = this.grid._hasVerticalScrollbar === true ? this.grid._scrollbarWidth() : 0, npw;
			/* forces grid to acknowledge the whole content is visible and
			no horizontal scrollbar should appear */
			if (this.options.forceResponsiveGridWidth) {
				this.grid._gridContentWidth = 0;
				this.grid.element.css("width", "100%");
				if (!this.grid._allColumnWidthsInPercentage) {
					npw = 100 - (sbw / this.grid.container().width()) * 100;
					if (this.grid.options.fixedHeaders === true) {
						this.grid.headersTable().css("width", npw + "%");
					}
					if (this.grid.options.fixedFooters === true) {
						this.grid.footersTable().css("width", (npw + this._footersDisplacement) + "%");
					}
				}
			}
		},
		_createHandlers: function () {
			this._gridHeaderRenderingHandler = $.proxy(this._gridHeaderRendering, this);
			this._initialGridRenderedHandler = $.proxy(this._initialGridRendered, this);
			this._gridContainerHeightHandler = $.proxy(this._heightChanged, this);
		},
		_injectGrid: function (gridInstance, isRebind) {
			var key, sct;
			if (isRebind === true) {
				return;
			}
			this.grid = gridInstance;
			/* M.K. 1/13/2014 187174: Errors should be thrown when the grid is initialized with unsupported configurations */
			this._checkGridNotSupportedFeatures();
			this._createHandlers();
			if (this.options.forceResponsiveGridWidth === true) {
				this.grid.options.width = typeof this.grid.options.width === "string" &&
					this.grid.options.width.endsWith("%") ?
					this.grid.options.width : "100%";
				this.grid.options.autoFitLastColumn = false;
			}
			this.grid.element.bind("iggridheaderrendering", this._gridHeaderRenderingHandler);
			this.grid.element.bind("iggridrendered", this._initialGridRenderedHandler);
			/* If the grid has height in % we need to react when grid changes heights */
			if (this.grid.options.height !== null &&
				this.grid.options.height.indexOf &&
				this.grid.options.height.indexOf("%") !== -1) {
				this.grid.element.bind("iggrid_heightchanged", this._gridContainerHeightHandler);
			}
			/* vertical rendering implementation */
			this._originalRenderRecord = $.proxy(this.grid._renderRecord, this.grid);
			this._newRenderRecord = $.proxy(this._renderRecord, this);
			this._originalRenderColgroup = $.proxy(this.grid._renderColgroup, this.grid);
			this._newRenderColgroup = $.proxy(this._renderColgroup, this);
			this._originalRenderCell = this.grid._renderCell;
			this._newRenderCell = $.proxy(this._renderCell, this);
			this._originalRenderNewRow = $.proxy(this.grid.renderNewRow, this.grid);
			this._newRenderNewRow = $.proxy(this._renderNewRow, this);
			this.grid._renderRecord = this._newRenderRecord;
			this.grid._renderColgroup = this._newRenderColgroup;
			this.grid._renderCell = this._newRenderCell;
			this.grid.renderNewRow = this._newRenderNewRow;
			if (String(this.grid.options.templatingEngine).toLowerCase() === "jsrender") {
				this._jsr = true;
				sct = this.options.singleColumnTemplate;
				if (sct && typeof sct === "object") {
					for (key in sct) {
						if (sct.hasOwnProperty(key)) {
							$.templates(this.grid.id() + "_responsiveSct_" + key, sct[ key ]);
						}
					}
				}
			}
		},
		_checkGridNotSupportedFeatures: function () {
			/* Throw an exception for unsupported integration scenarios */
			var gridOptions = this.grid.options;
			if ((gridOptions.virtualization === true ||
				gridOptions.rowVirtualization === true ||
				gridOptions.columnVirtualization === true) &&
				gridOptions.virtualizationMode === "fixed") {
				/* igGridResponsive is not supported with fixed virtualization */
				throw new Error($.ig.igGridResponsive.locale.fixedVirualizationNotSupported);
			}
		}
	});
	$.extend($.ui.igGridResponsive, { version: "16.1.20161.2145" });

	$.ig = $.ig || {};

	$.ig.ResponsiveMode = $.ig.ResponsiveMode || Class.extend({
		settings: {
			minWidth: -1,
			maxWidth: Number.MAX_VALUE,
			minHeight: -1,
			maxHeight: Number.MAX_VALUE
		},
		init: function (options) {
			if (options) {
				this.settings = $.extend(true, {}, $.ig.ResponsiveMode.prototype.settings, options);
			}
			return this;
		},
		isActive: function () {
			return window.innerWidth >= this.settings.minWidth &&
				window.innerWidth <= this.settings.maxWidth &&
				window.innerHeight >= this.settings.minHeight &&
				window.innerHeight <= this.settings.maxHeight;
		}
	});

	$.ig.InfragisticsMode = $.ig.InfragisticsMode || $.ig.ResponsiveMode.extend({
		settings: {
			key: "",
			visibilityTester: null
		},
		init: function (options) {
			this._hc = "ui-hidden-" + options.key;
			this._vc = "ui-visible-" + options.key;
			this._super(options);
			return this;
		},
		isActive: function () {
			if (typeof this.settings.visibilityTester === "function") {
				return this.settings.visibilityTester(this._hc) === false &&
					this.settings.visibilityTester(this._vc) === true;
			}
			return this._super();
		}
	});

	$.ig.BootstrapMode = $.ig.BootstrapMode || $.ig.ResponsiveMode.extend({
		settings: {
			key: "",
			visibilityTester: null
		},
		init: function (options) {
			this._hc = "hidden-" + options.key;
			this._vc = "visible-" + options.key;
			this._super(options);
			return this;
		},
		isActive: function () {
			if (typeof this.settings.visibilityTester === "function") {
				return this.settings.visibilityTester(this._hc) === false &&
					this.settings.visibilityTester(this._vc) === true;
			}
			return this._super();
		}
	});
}(jQuery));
