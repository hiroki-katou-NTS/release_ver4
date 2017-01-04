﻿/*!@license
 * Infragistics.Web.ClientUI Grid Tooltips 16.1.20161.2145
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
 *	infragistics.ui.editors.js
 *	infragistics.ui.shared.js
 *	infragistics.dataSource.js
 *	infragistics.util.js
 */

/*global jQuery */
if (typeof jQuery !== "function") {
	throw new Error("jQuery is undefined");
}
(function ($) {

	/*
		igGridRowSelectors widget.
		The widget is pluggable to the element where the grid is instantiated and the actual igGrid object doesn't know about this.
		The Row Selectors widget works as a grid feature and injects an additional column on the left side of the initial table
		The column cells can be clicked to expand children grids derriving from the expanded row.
	*/
	"use strict";
	$.widget("ui.igGridRowSelectors", {
		options: {
			/* type="bool" determines whether the row selectors column should contain row numbering */
			enableRowNumbering: true,
			/* type="bool" determines whether the row selectors column should contain checkboxes */
			enableCheckBoxes: false,
			/* type="number" the seed to be added to the default numbering */
			rowNumberingSeed: 0,
			/* type="string|number|null"
				string The row selector column width can be set in pixels (px) and percentage (%)
				number The row selector width can be set as a number
				null type="object" will let the feature decide the best width depending on enabled contents
			*/
			rowSelectorColumnWidth: null,
			/* type="bool" determines whether the selection feature is required for the row selectors. If set to "false"
			the widget will not check for Selection availability. If set to "true" an exception will be thrown if Selection is
			not available. */
			requireSelection: true,
			/* type="bool" determines whether checkboxes will be shown only if row selectors are on focus/selected.
			*/
			showCheckBoxesOnFocus: false,
			/* type="bool" Enables/disables feature inheritance for the child layouts. NOTE: It only applies for igHierarchicalGrid. */
			inherit: false,
			/* type="bool" Enables/disables showing an overlay after clicking on the header checkbox, which allows selecting all records from all pages. */
			enableSelectAllForPaging: true,
			/* type="string" Custom template for "select all" overlay with paging
				Element with attribute data-rs-select-all is used for sellecting all the records
				Element with attribute data-rs-close-all is used for closing the overlay
				The default template is "<div class='ui-widget-header ui-priority-secondary' tabindex='0'><div>You have selected ${checked} records. <a href='#' tabindex='0' data-rs-select-all>Select all ${totalRecordsCount} records</a><div style='float:right;'><span data-rs-close-all class='ui-icon-close ui-icon ui-button' tabindex='0'></div></span></div></div>"
				There is also ${allCheckedRecords} parameter which is not used in the default template, but it represents the checked records from all pages.
			*/
			selectAllForPagingTemplate: null,
			/* type="string" Custom template for "deselect all" overlay with paging
				Element with attribute data-rs-deselect-all is used for sellecting all the records
				Element with attribute data-rs-close-all is used for closing the overlay
				The default template is "<div class='ui-widget-header ui-priority-secondary' tabindex='0'><div>You have deselected ${unchecked} records. <a href='#' tabindex='0' data-rs-deselect-all>Deselect all ${totalRecordsCount} records</a><div style='float:right;'><span data-rs-close-all class='ui-icon-close ui-icon ui-button' tabindex='0'></div></span></div></div>"
				There is also ${allCheckedRecords} parameter which is not used in the default template, but it represents the checked records from all pages.
			*/
			deselectAllForPagingTemplate: null
		},
		css: {
			/* Classes applied to the row selectors grid cells. */
			rowSelector: "ui-iggrid-rowselector-class",
			/* Classes applied to the row selectors grid cells when they are selected */
			rowSelectorSelected: "ui-iggrid-selectedcell ui-state-active",
			/* Classes applied to the row selectors grid cells when they are activated */
			rowSelectorActivated: "ui-iggrid-activecell ui-state-focus",
			/* Classes applied to the row selectors header cells. */
			headerRowSelector: "ui-iggrid-rowselector-header",
			/* Classes applied to the row selectors footer cells. */
			footerRowSelector: "ui-iggrid-rowselector-footer",
			/* Classes applied to the checkbox container */
			checkBox: "ui-state-default ui-corner-all ui-igcheckbox-normal",
			/* Classes defining the unchecked state of the checkbox */
			checkBoxOff: "ui-icon ui-icon-check ui-igcheckbox-normal-off",
			/* Classes defining the checked state of the checkbox */
			checkBoxOn: "ui-icon ui-icon-check ui-igcheckbox-normal-on",
			/* Class defining the hover state style of the node */
			nodeHovered: "ui-state-hover"
		},
		events: {
			/* Event fired after a row selector is clicked.
			Function takes arguments evt and ui.
			Use ui.row to get reference to the row the clicked row selector resides in.
			Use ui.fixedRow to get reference to the fixed row the clicked row selector resides in(if there are fixed columns).
			Use ui.rowIndex to get the index of the row the clicked row selector resides in.
			Use ui.rowKey to get the key of the row the clicked row selector resides in.
			Use ui.rowSelector to get reference to the row selector cell.
			Use ui.owner to get reference to igRowSelectors.
			Use ui.grid to get reference to the igGrid the igRowSelectors are initialized for.
			*/
			rowSelectorClicked: "rowSelectorClicked",
			/* cancel="true" Event fired when a row selector checkbox is changing.
			Function takes arguments evt and ui.
			Use ui.row to get reference to the row the clicked row selector resides in.
			Use ui.rowIndex to get the index of the row the clicked row selector resides in.
			Use ui.rowKey to get the key of the row the clicked row selector resides in.
			Use ui.rowSelector to get reference to the row selector cell.
			Use ui.owner to get reference to igRowSelectors.
			Use ui.grid to get reference to the igGrid the igRowSelectors are initialized for.
			Use ui.currentState to get the current state of the checkbox ("on","off").
			Use ui.newState to get the new state of the checkbox ("on","off").
			Use ui.isHeader to check if the header check box is the one being clicked. In this case no row related args are passed.
			*/
			checkBoxStateChanging: "checkBoxStateChanging",
			/* Event fired after a row selector checkbox had changed state.
			Function takes arguments evt and ui.
			Use ui.row to get reference to the row the clicked row selector resides in.
			Use ui.rowIndex to get the index of the row the clicked row selector resides in.
			Use ui.rowKey to get the key of the row the clicked row selector resides in.
			Use ui.rowSelector to get reference to the row selector cell.
			Use ui.owner to get reference to igRowSelectors.
			Use ui.grid to get reference to the igGrid the igRowSelectors are initialized for.
			Use ui.state to get the state of the checkbox ("on","off").
			Use ui.isHeader to check if the header check box is the one being clicked. In this case no row related args are passed.
			*/
			checkBoxStateChanged: "checkBoxStateChanged"
		},
		_createWidget: function () {
			$.Widget.prototype._createWidget.apply(this, arguments);
			if (this.options.rowSelectorColumnWidth === null) {
				this.options.rowSelectorColumnWidth = 55;
				if (this.options.enableCheckBoxes === true && this.options.enableRowNumbering === true) {
					this.options.rowSelectorColumnWidth += 15;
				}
			}
		},
		_create: function () {
			/* the the index of the last generated row selector */
			this._cIdx = 0;
			/* whether grid rendering functions have been replaced with rs ones */
			this._functionsRedirected = false;
			/* stores the hovered table row */
			this._hovTR = null;
			/* stores the original renderRecord function */
			this._gridRenderRecord = null;
			/* stores the original renderRecordInArray function */
			this._gridRenderRecordInArray = null;
			/* stores the checkboxes visibility status */
			this._checkBoxesShown = false;
			/* stores a template for getting number span when templating is enabled */
			this._nTmpl = "{{html ig_rs_idx}}";
			this._sTmpl = "{{if ${ig_rs_sel} === true}}" + this.css.rowSelectorSelected + "{{/if}}";
			/* stores fixed v flag */
			this._v = false;
			/* multiple selection flag */
			this._ms = false;
		},
		destroy: function () {
			this._unregisterEvents();
			this._unregisterCellEvents();
			this._unregisterSelectAllEvents();
			if (this._gridRenderRecordHandler !== undefined) {
				this.grid._renderColgroup = this._flatRenderColgroup;
				this.grid._renderRecord = this._gridRenderRecordHandler;
				this.grid._renderRecordInArray = this._gridRenderRecordInArrayHandler;
			}
			if (this.options.enableCheckBoxes === true) {
				this._unregisterCheckBoxEvents();
			}
			this._cleanInterface(false);
			if (this.grid._selection) {
				this.grid._selection.removeSubscriber(this._subId, this.grid.id());
			}
			$.Widget.prototype.destroy.call(this);
			return this;
		},
		_shouldRenderHeaderCheckBoxes: function() {
			return this.options.enableCheckBoxes === true && this._ms;
		},
		_renderHeaderRowSelectors: function (owner) {
			var rows, i, cell, header, $thDataSkip;
			if (owner.id() !== this.grid.id()) {
				return;
			}
			rows = this.grid.headersTable().children("thead").children();
			/* creates the header cells for row selectors on each row */
			if (rows.length > 0) {
				/* getting the expand header cell */
				cell = rows.eq(0).find("th.ui-iggrid-expandheadercell").first();
				if (cell.length === 0) {
					this._index = 0;
				} else {
					this._index = cell.index() + 1;
				}
				/* add support for multi column headers */
				if (this.grid._isMultiColumnGrid) {
					$thDataSkip = $("<th></th>").prependTo(this.grid.headersTable().find("thead tr:nth-child(1)"))
						.addClass(this.css.headerRowSelector)
						.addClass(this.grid.css.headerClass)
						.attr("data-role", "rs")
						.attr("data-skip", "true");
					$thDataSkip.attr("rowspan", this.grid._maxLevel + 1);
					if (this._shouldRenderHeaderCheckBoxes()) {
						/* M.H. 13 Aug 2012 Fix for bug #118563 */
						$(this._getCheckBox(true)).appendTo($thDataSkip);
					}
				} else {
					for (i = 0; i < rows.length; i++) {
						/* the row selector cell is already present */
						if (rows.eq(i).find("th[data-role='rs']").length > 0) {
							continue;
						}
						/* adding the header cell */
						/* S.S. April 9, 2012 Bug #103459 IE7 doesn't render empty cells, we need to add dummy data. */
						header = $("<th>" + ($.ig.util.isIE7 ? "&nbsp;" : "") + "</th>")
							.addClass(this.css.headerRowSelector)
							.addClass(i === 0 ? this.grid.css.headerClass : "")
							.attr("data-role", "rs")
							.attr("data-skip", "true")
							.insertBefore(rows.eq(i).children().eq(this._index));
						if (this._shouldRenderHeaderCheckBoxes() && i === 0) {
							$(this._getCheckBox(true)).appendTo(header);
						}
					}
				}
			}
		},
		_renderFooterRowSelectors: function (owner) {
			var rows, i;
			if (owner.id() !== this.grid.id()) {
				return;
			}
			rows = this.grid.footersTable().children("tfoot").children();
			/* creates the header cells for row selectors on each row */
			if (rows.length > 0) {
				for (i = 0; i < rows.length; i++) {
					// the row selector cell is already present */
					if (rows.eq(i).find("td[data-role='rs']").length > 0) {
						continue;
					}
					/* adding the header cell */
					/* S.S. April 9, 2012 Bug #103459 IE7 doesn't render empty cells, we need to add dummy data. */
					$("<td>" + ($.ig.util.isIE7 ? "&nbsp;" : "") + "</td>")
						.addClass(this.css.footerRowSelector)
						.attr("data-role", "rs")
						.attr("data-skip", "true")
						.insertBefore(rows.eq(i).children().eq(this._index));
				}
			}
		},
		_headerRendered: function (event, ui) {
			this._ms = !this._skipRefresh && this._getSelectionInstance().options.multipleSelection === true;
			this._renderHeaderRowSelectors(ui.owner);
			this._getColumnFixingInstance();
		},
		_footerRendered: function (event, ui) {
			this._renderFooterRowSelectors(ui.owner);
		},
		_getColumnFixingInstance: function () {
			if (!this._columnFixing && this.grid.element.data("igGridColumnFixing")) {
				this._columnFixing = this.grid.element.data("igGridColumnFixing");
			}
			return this._columnFixing;
		},
		_getSelectionInstance: function () {
			if (!this._selection && this.grid.element.data("igGridSelection")) {
				this._selection = this.grid.element.data("igGridSelection");
			}
			return this._selection;
		},
		_recordsRendering: function (event, ui) {
			if (this.grid.id() !== ui.owner.id()) {
				return;
			}
			/* S.S. August 21, 2012, Bug #113279 Adding the continuous virtualization marker
			detection so that proper numbering can be applied in the non-template case */
			this._cIdx = ui.vrtWnd.start && ui.vrtWnd.end ? ui.vrtWnd.start : 0;
			/* M.H. 30 Jan 2015 Fix for bug #187995: When a column is fixed with ColumnFixing feature
			and continuous rowVirtualization is enabled and if data	is not bound during initialization,
			calling dataBind will render the first non-fixed column as rowSelectors */
			this._redirectFunctions();
			this._bindToSelectionCollection();
		},
		_redirectFunctions: function () {
			if (this._functionsRedirected === false) {
				/* render record */
				this._renderRecordHandler = $.proxy(this._rsRenderRecord, this);
				this._renderRecordInArrayHandler = $.proxy(this._rsRenderRecordInArray, this);
				this._gridRenderRecordHandler = $.proxy(this.grid._renderRecord, this.grid);
				this._gridRenderRecordInArrayHandler = $.proxy(this.grid._renderRecordInArray, this.grid);
				/* register for row rendering */
				this._gridRenderRecord = this._gridRenderRecordHandler;
				this._gridRenderRecordInArray = this._gridRenderRecordInArrayHandler;
				this.grid._renderRecord = this._renderRecordHandler;
				this.grid._renderRecordInArray = this._renderRecordInArrayHandler;
			}
			this._functionsRedirected = true;
		},
		_recordsRendered: function (event, ui) {
			if (this.grid.id() !== ui.owner.id()) {
				return;
			}
			if (this.options.enableCheckBoxes === true) {
				this._registerCheckBoxEvents();
				this._updateHeader();
			}
		},
		_gridRendered: function (event, ui) {
			if (ui === undefined) {
				return;
			}
			if (this.grid.id() !== ui.owner.id()) {
				return;
			}
			/* ensuring the internal property is set even when showHeader is false */
			this._ms = !this._skipRefresh && this._getSelectionInstance().options.multipleSelection === true;
			this._unregisterCellEvents();
			this._registerCellEvents();
			if (this.options.enableCheckBoxes === true) {
				this._registerCheckBoxEvents();
			}
		},
		_hidingFinished: function () {
			this._unregisterCellEvents();
			this._registerCellEvents();
		},
		_rsRenderColgroup: function (frc, rs, table, isHeader, isFooter, autofitLastColumn, md) {
			var fdLeft, fixed, id = table.id;
			frc.apply(this, [ table, isHeader, isFooter, autofitLastColumn, md ]);
			if (rs.grid.hasFixedColumns()) {
				fdLeft = this.fixingDirection() === "left";
				fixed = !!(md && md.fixed); // render colgroups for fixed container if fixed is true
				/* render data-skip row-selector column if: fixing direction is left and render fixed table OR
				fixing direction is right and render unfixed table */
				id = ((fdLeft && fixed) || (!fdLeft && !fixed)) ? id : id + "_fixed";
				table = document.getElementById(id); // get table for which it should be rendered data-skip column
			}
			rs._rsRenderColgroupHelper(table);
		},
		_rsRenderColgroupHelper: function (table) {
			var cgrp;
			cgrp = $(table).find("colgroup");
			if (cgrp.find("col[data-role='rs']").length === 0) {
				$("<col></col>")
					.prependTo(cgrp)
					.css("width", this.options.rowSelectorColumnWidth)
					.attr("data-skip", "true")
					.attr("data-role", "rs");
			}
		},
		_rsRenderRecord: function (data, rowIndex, isFixed) {
			var markup = this._gridRenderRecord(data, rowIndex, isFixed), pre, app, idx,
				fCols = this.grid.hasFixedColumns(), fdLeft = this.grid.fixingDirection() === "left", rs;
			if (fCols && (!((isFixed && fdLeft) || (!isFixed && !fdLeft)))) {
				return markup;
			}
			/* M.H. 1 Mar 2016 Fix for bug 215077: RowSelectors in the fixed area are not checked after data binding */
			rs = this._getRowSelectorCellMarkup(false,
					isFixed && !this.grid._fixedColumns.length ?
								this._rowHasSelection(data) :
								markup.indexOf("ui-iggrid-selectedcell") !== -1,
					data);
			idx = markup.indexOf("ui-iggrid-expandcolumn");
			if (idx >= 0) {
				app = markup.substr(idx);
				idx = idx + app.indexOf("</td>") + 4;
				pre = markup.substring(0, idx + 1);
				app = markup.substring(idx + 1);
			} else {
				idx = markup.indexOf(">") + 1;
				pre = markup.substring(0, idx);
				app = markup.substring(idx);
			}
			markup = pre + rs + app;
			return markup;
		},
		_rsRenderRecordInArray: function (darr, tbody, data, rowIndex) {
			var i, j, sel = false;
			this._gridRenderRecordInArray(darr, tbody, data, rowIndex);
			for (i = darr.length - 1; i >= 0; i--) {
				if (darr[ i ].indexOf && darr[ i ].indexOf("<tr") !== -1) {
					for (j = i; j < darr.length; j++) {
						if (darr[ j ].indexOf && darr[ j ].indexOf("ui-iggrid-selectedcell") !== -1) {
							sel = true;
							break;
						}
					}
					for (j = i; j < darr.length; j++) {
						if (darr[ j ].indexOf && darr[ j ].indexOf("<td") !== -1) {
							if (darr[ j ].indexOf("ui-iggrid-expandcolumn") !== -1) {
								darr[ j ] += this._getRowSelectorCellMarkup(false, sel);
								sel = false;
								break;
							}
							darr[ j ] = this._getRowSelectorCellMarkup(false, sel) + darr[ j ];
							sel = false;
							break;
						}
					}
					break;
				}
			}
		},
		_renderExtraHeaderCells: function (row, colgroup, prepend) {
			this._renderExtraCells(row, colgroup, prepend, true);
		},
		_renderExtraFooterCells: function (row, colgroup, prepend, cssClass) {
			this._renderExtraCells(row, colgroup, prepend, false, cssClass);
		},
		_renderExtraCells: function (row, colgroup, prepend, header, cssClass) {
			var rHeader, rCol, index, cell;
			/* the row selector cell is already present */
			/* S.S. October 20, 2011 Bug #93127 Footer cells were changed to TD-s in an earlier stage and this selector should
			work for both TH-s and TD-s so it can properly skip adding additional cells in both THEAD and TFOOT. */
			if (row.find("[data-role='rs']").length > 0) {
				return;
			}
			if (header === true) {
				/* S.S. April 9, 2012 Bug #103459 IE7 doesn't render empty cells, we need to add dummy data. */
				rHeader = $("<th>" + ($.ig.util.isIE7 ? "&nbsp;" : "") + "</th>")
					.addClass(this.css.headerRowSelector)
					.attr("data-role", "rs")
					.attr("data-skip", "true");
			} else {
				rHeader = $("<td>" + ($.ig.util.isIE7 ? "&nbsp;" : "") + "</td>")
					.addClass(this.css.footerRowSelector)
					.addClass(cssClass)
					.attr("data-role", "rs")
					.attr("data-skip", "true");
			}
			cell = row.find("th.ui-iggrid-expandheadercell,td.ui-iggrid-expandheadercellgb");
			if (cell.length === 0) {
				cell = row.children().first();
				index = 0;
			} else {
				index = cell.last().index() + 1;
			}
			if (index === 0) {
				rHeader.prependTo(row);
			} else {
				rHeader.insertBefore(row.children().eq(index));
			}
			if (colgroup) {
				rCol = $("<col></col>")
					.attr("data-skip", "true")
					.attr("data-role", "rs")
					.css("width", this.options.rowSelectorColumnWidth);
				rCol.insertBefore(colgroup.children().eq(index));
			}
		},
		_registerCellEvents: function () {
			// select all body th-s
			this._allRowSelectorCells()
				.bind({
					"click": this._rsClickHandler
				});
			/* bind to tbody for hover styles */
			this.grid.element.children("tbody")
				.bind({
					"mousemove": this._rrHoverHandler,
					"mouseleave": this._rrLeaveHandler
				});
		},
		_unregisterCellEvents: function () {
			this._allRowSelectorCells()
				.unbind({
					"click": this._rsClickHandler
				});
			this.grid.element.children("tbody")
				.unbind({
					"mousemove": this._rrHoverHandler,
					"mouseleave": this._rrLeaveHandler
				});
		},
		_registerCheckBoxEvents: function () {
			this._allCheckboxes()
				.unbind({
					"click": this._checkboxClickHandler,
					"keydown": this._checkboxClickHandler,
					"mouseover": this._checkboxMouseOverHandler,
					"mouseout": this._checkboxMouseOutHandler
				})
				.bind({
					"click": this._checkboxClickHandler,
					"keydown": this._checkboxClickHandler,
					"mouseover": this._checkboxMouseOverHandler,
					"mouseout": this._checkboxMouseOutHandler
				});
			this._headerCheckbox()
				.unbind({
					"click": this._hCheckboxClickHandler,
					"keydown": this._hCheckboxClickHandler,
					"mouseover": this._checkboxMouseOverHandler,
					"mouseout": this._checkboxMouseOutHandler
				})
				.bind({
					"click": this._hCheckboxClickHandler,
					"keydown": this._hCheckboxClickHandler,
					"mouseover": this._checkboxMouseOverHandler,
					"mouseout": this._checkboxMouseOutHandler
				});
		},
		_unregisterCheckBoxEvents: function () {
			this._allCheckboxes()
				.unbind({
					"click": this._checkboxClickHandler,
					"keydown": this._checkboxClickHandler,
					"mouseover": this._checkboxMouseOverHandler,
					"mouseout": this._checkboxMouseOutHandler
				});
			this._headerCheckbox()
				.unbind({
					"click": this._hCheckboxClickHandler,
					"keydown": this._hCheckboxClickHandler,
					"mouseover": this._checkboxMouseOverHandler,
					"mouseout": this._checkboxMouseOutHandler
				});
		},
		_checkboxMouseOver: function (event) {
			$(event.target).closest("span[data-role='checkbox']").addClass(this.css.nodeHovered);
		},
		_checkboxMouseOut: function (event) {
			$(event.target).closest("span[data-role='checkbox']").removeClass(this.css.nodeHovered);
		},
		_checkBoxClicked: function (event) {
			if (event.type === "keydown" &&
				event.keyCode !== $.ui.keyCode.ENTER &&
				event.keyCode !== $.ui.keyCode.SPACE) {
				return;
			}
			var trg = $(event.target).closest("span[name='chk']"), rCell = trg.parent(),
				rRow = rCell.parent(), args, rIdx = this._getVisibleRowIndex(rRow),
				rKey = rRow.attr("data-id"), noCancel, state = trg.attr("data-chk");
			if (rKey === "" || rKey === null || rKey === undefined) {
				rKey = rIdx;
			}
			args = {
				row: rRow,
				rowIndex: rIdx,
				rowKey: rKey,
				rowSelector: rCell,
				owner: this,
				grid: this.grid,
				currentState: state,
				newState: state === "off" ? "on" : "off",
				isHeader: false
			};
			noCancel = this._triggerCheckingEvent(event, args);
			if (noCancel === true) {
				this._handleCheck(trg);
				/* this._updateHeader(); */
				/* modifying args to trigger checked event */
				delete args.currentState;
				args.state = args.newState;
				delete args.newState;
				this._triggerCheckedEvent(event, args);
			}
			event.preventDefault();
			event.stopPropagation();
		},
		_headerCheckBoxClicked: function (event) {
			var trg = $(event.target).closest("span[name='hchk']"), args,
				noCancel, state = trg.attr("data-chk");
			if (event.type === "keydown" &&
				event.keyCode !== $.ui.keyCode.ENTER &&
				event.keyCode !== $.ui.keyCode.SPACE) {
				return;
			}
			args = {
				owner: this,
				grid: this.grid,
				currentState: state,
				newState: state === "off" ? "on" : "off",
				isHeader: true
			};
			noCancel = this._triggerCheckingEvent(event, args);
			if (noCancel === true) {
				this._handleHeaderCheck(trg);
				/* modifying args to trigger checked event */
				delete args.currentState;
				args.state = args.newState;
				delete args.newState;
				this._triggerCheckedEvent(event, args);
			}
			event.stopPropagation();
			event.preventDefault();
		},
		_handleCheck: function (checkbox) {
			var row, rowId, upd, sel, offset;
			if (!checkbox) {
				return;
			}
			upd = this.grid.element.data("igGridUpdating");
			sel = this._getSelectionInstance();
			if (sel && sel._suspend) {
				if (upd) {
					if (upd.findInvalid()) {
						return;
					}
					upd._endEdit(null, true);
				} else {
					return;
				}
			}
			offset = this._v ? this.grid._startRowIndex : 0;
			row = checkbox.parent().parent();
			if (this.grid.hasFixedColumns() && sel) {
				/* M.H. 26 Feb 2016 Fix for bug 214893: Row selection is not applied corectly
				when row is selected from row selector`s checkbox and virtualization is enabled */
				row = row.add(sel._getRowByIndex(row.index(), !this.grid._isFixedElement(row)));
			}
			rowId = this.grid._fixPKValue(row.attr("data-id"));
			if (rowId === null || rowId === undefined) {
				rowId = row.closest("tbody").children("tr:not([data-container])").index(row) + offset;
			}
			if (this.grid._selection.settings.owner !== this.grid) {
				this.grid._selection.changeOwner(this.grid);
			}
			if (checkbox.attr("data-chk") === "off") {
				if (sel.options.activation) {
					this.grid._selection.activate(rowId, row);
				}
				this.grid._selection.select(rowId, true, { element: row, checkbox: checkbox });
			} else {
				this.grid._selection.deselect(rowId, { element: row, checkbox: checkbox });
			}
		},
		_handleHeaderCheck: function (checkbox) {
			var sel, go = this.grid.options, v = go.virtualization || go.rowVirtualization,
				templateData, toCheck = checkbox.attr("data-chk") === "off",
				dataViewLength, allCheckedRecs;
			/* hgrid check if owner is the same */
			sel = this._getSelectionInstance();
			if (this.grid._selection.settings.owner !== this.grid) {
				this.grid._selection.changeOwner(this.grid);
			}
			if (toCheck) {
				/* select all */
				if (v) {
					this._selectAllVirtualRows(sel);
				} else {
					this._selectAllRows(sel);
				}
				this._alterCheckbox(checkbox, true);
			} else {
				/* unselect all on current page */
				if (v) {
					this._deselectAllVirtualRows(sel);
				} else {
					this._deselectAllRows(sel);
				}
				this._alterCheckbox(checkbox, false);
			}
			if (this.options.enableSelectAllForPaging && this.grid.element.data("igGridPaging")) {
				/* unchecked and checked records should be the amount of the current data view */
				dataViewLength = this._getDataView().length;
				allCheckedRecs = this._getSelectionInstance().selectedRows().length;
				templateData = [{
					checked: dataViewLength,
					unchecked: dataViewLength,
					allCheckedRecords: allCheckedRecs,
					totalRecordsCount: this._getDSLocalRecords().length
				}];
				this._renderOverlay(templateData, toCheck);
			}
		},
		_getDSLocalRecords: function () {
			var ds = this.grid.dataSource,
				trecCount = ds.totalLocalRecordsCount();
			/* get datasource local records */
			if (ds._filteredData && ds._filteredData.length >= trecCount) {
				return ds._filteredData;
			}
			return this._getAllData();
		},
		_renderOverlay: function (templateData, toCheck) {
			var hTable = this.grid.headersTable(),
				 overlay;
			if (toCheck) {
				overlay = this._getSelectAllOverlay(templateData);
			} else {
				overlay = this._getDeselectAllOverlay(templateData);
			}
			/* position the overlay */
			if (overlay) {
				overlay.css({
					"z-index": "1000",
					"width": "inherit",
					"position": "absolute",
					/* parent of hTable is direct child to the container
					use first thead height for cases of MCH or fixedHeaders: false */
					"top": (this.grid.options.height || this.grid.options.width ?
							hTable.parent().position().top :
							hTable.position().top) +
						hTable.find("thead:first").outerHeight()
				});
				overlay.children("div:first").css("padding-left", this.options.rowSelectorColumnWidth);
				overlay.appendTo(this.grid.container());
				this._registerSelectAllEvents(overlay);
			}
		},
		_getSelectAllOverlay: function (templateData) {
			var ti = this.grid.options.tabIndex,
				overlay, selectAllTemplate;
			/* if all have been selected don't show the overlay */
			if (templateData[ 0 ].allCheckedRecords >= templateData[ 0 ].totalRecordsCount) {
				return null;
			}
			if (this.options.selectAllForPagingTemplate) {
				overlay = this._jsr ?
					$($.render([ this.grid.id() + "_selectAllForPagingTemplate" ](templateData))) :
					$($.ig.tmpl(this.options.selectAllForPagingTemplate, templateData));
			} else {
				selectAllTemplate = $.ig.GridRowSelectors.locale.selectedRecordsText
						.replace("${checked}", templateData[ 0 ].checked) +
					" <a href='#' data-rs-select-all tabindex='" + ti + "'>" +
					$.ig.GridRowSelectors.locale.selectAllText
						.replace("${totalRecordsCount}", templateData[ 0 ].totalRecordsCount) +
					"</a>";
				overlay = this._getDefaultOverlay(selectAllTemplate, true);
			}
			return overlay;
		},
		_getDeselectAllOverlay: function (templateData) {
			var ti = this.grid.options.tabIndex,
				overlay, deselectAllTemplate;
			/* if all have been deselected don't show the overlay */
			if (templateData[ 0 ].allCheckedRecords === 0) {
				return null;
			}
			if (this.options.deselectAllForPagingTemplate) {
				overlay = this._jsr ?
					$($.render([ this.grid.id() + "_deselectAllForPagingTemplate" ](templateData))) :
					$($.ig.tmpl(this.options.deselectAllForPagingTemplate, templateData));
			} else {
				deselectAllTemplate = $.ig.GridRowSelectors.locale.deselectedRecordsText
						.replace("${unchecked}",  templateData[ 0 ].unchecked) +
					" <a href='#' data-rs-deselect-all tabindex='" + ti + "'>" +
					$.ig.GridRowSelectors.locale.deselectAllText
						.replace("${totalRecordsCount}", templateData[ 0 ].totalRecordsCount) +
					"</a>";
				overlay = this._getDefaultOverlay(deselectAllTemplate, false);
			}
			return overlay;
		},
		_getDefaultOverlay: function (template, toCheck) {
			var ti = this.grid.options.tabIndex;
			return $("<div>").attr({
				"class": "ui-widget-header ui-priority-secondary",
				"tabIndex": ti,
				"id": this.grid.id() + (toCheck ? "_" : "_de") + "select_all_overlay"
			})
			.append($("<div>")
				.html(template)
				.append($("<div>")
					.css("float", "right")
					.append($("<span>")
						.addClass("ui-icon-close")
						.addClass("ui-icon")
						.addClass("ui-button")
						.attr("data-rs-close-all", "")
						.attr("tabindex", ti)
					)
				)
			);
		},
		_registerSelectAllEvents: function (overlay) {
			var self = this,
				overlayId = overlay.attr("id");
			/* close the overlay always */
			this.grid.container().bind("mouseup.containerselectall", function (event) {
				/* mouseup on the counter should close the overlay and
				focusing out also if the overlay is not the focused element */
				if (event.type === "mouseup" && $(event.target).closest("#" + overlayId).length === 0) {
					$(this).unbind(".containerselectall");
					if ($(overlay)) {
						overlay.remove();
					}
				}
			});
			overlay.bind("blur.containerselectall", function (event) {
				$(this).unbind(".containerselectall");
				if ($(overlay) && $(event.target).closest("#" + overlayId).length === 0) {
					overlay.remove();
				}
			});
			/* find the element to select all and bind to it */
			overlay.find("[data-rs-select-all]").bind("mouseup", function (event) {
				self._selectAllFromOverlay();
				event.stopPropagation();
				overlay.remove();
			});
			overlay.find("[data-rs-deselect-all]").bind("mouseup", function (event) {
				self._deselectAllFromOverlay();
				event.stopPropagation();
				overlay.remove();
			});
			overlay.find("[data-rs-select-all]").bind("keypress", function (event) {
				if (event.keyCode === $.ui.keyCode.ENTER || event.keyCode === $.ui.keyCode.SPACE) {
					self._selectAllFromOverlay();
					overlay.remove();
				}
			});
			overlay.find("[data-rs-deselect-all]").bind("keypress", function (event) {
				if (event.keyCode === $.ui.keyCode.ENTER || event.keyCode === $.ui.keyCode.SPACE) {
					self._deselectAllFromOverlay();
					overlay.remove();
				}
			});
			/* find the element for closing the overlay and bind to it */
			overlay.find("[data-rs-close-all]").bind("mouseup", function (event) {
				event.stopPropagation();
				overlay.remove();
			});
			overlay.find("[data-rs-close-all]").bind("keypress", function (event) {
				if (event.keyCode === $.ui.keyCode.ENTER || event.keyCode === $.ui.keyCode.SPACE) {
					overlay.remove();
				}
			});
		},
		_selectAllFromOverlay: function() {
			this._changeCheckStateForAllRecords(this._getSelectionInstance(), true);
		},
		_deselectAllFromOverlay: function() {
			this._changeCheckStateForAllRecords(this._getSelectionInstance(), false);
		},
		_unregisterSelectAllEvents: function() {
			if (this.grid.element.data("igGridPaging") && this.options.enableSelectAllForPaging) {
				//unregister select all overlay events
				this.grid.container().unbind(".containerselectall");
				$(this.grid.id() + "_select_all_overlay").remove();
				$(this.grid.id() + "_deselect_all_overlay").remove();
			}
		},
		_updateHeader: function () {
			var dvl, sl, checkboxes, check = true, i;
			if (this.grid.element.data("igGridPaging")) {
				checkboxes = this._allCheckboxes();
				if (checkboxes.length === 0) {
					check = false;
				} else {
					for (i = 0; i < checkboxes.length; i++) {
						if (checkboxes.eq(i).attr("data-chk") === "off") {
							check = false;
							break;
						}
					}
				}
			} else {
				dvl = this._getDataView().length;
				sl = this.grid._selection ? this.grid._selection.selectionLength() : 0;
				check = sl === dvl && this._isFirstRowSelected();
			}
			this._alterCheckbox(this._headerCheckbox(), check);
		},
		_alterCheckbox: function (checkbox, check) {
			var inner = checkbox.children().first();
			if (checkbox.length > 0 && inner.length > 0) {
				if (check === true) {
					checkbox.attr("data-chk", "on");
					inner.removeClass(this.css.checkBoxOff).addClass(this.css.checkBoxOn);
				} else {
					checkbox.attr("data-chk", "off");
					inner.removeClass(this.css.checkBoxOn).addClass(this.css.checkBoxOff);
				}
			}
		},
		_getRowSelectorCellMarkup: function (template, selected) {
			var markup = "";
			markup += "<th role=\"rowheader\" tabindex=\"" +
				this.grid.options.tabIndex + "\" class=\"" +
				this.css.rowSelector;
			if (selected) {
				markup += " " + this.css.rowSelectorSelected;
			}
			if (template) {
				markup += " " + this._sTmpl;
			}
			markup += "\"><span class=\"ui-icon ui-icon-triangle-1-e\" style=\"margin-left: -5px\"></span>";
			if (this.options.enableRowNumbering) {
				markup += template === true ? this._nTmpl : this._getCurrentNumber();
			}
			if (this.options.enableCheckBoxes) {
				markup += this._getCheckBox(false, selected);
			}
			markup += "</th>";
			return markup;
		},
		_getCheckBox: function (header, checked) {
			var markup = "";
			markup += "<span name=\"" + (header === true ? "hchk" : "chk") + "\" ";
			markup += "data-chk=\"" + (checked ? "on" : "off") + "\" ";
			markup += "data-role=\"checkbox\" class=\"" + this.css.checkBox + " \"";
			markup += "tabindex=\"" + this.grid.options.tabIndex + " \"";
			if (this.options.showCheckBoxesOnFocus === true && this._checkBoxesShown === false) {
				markup += " style=\"visibility: hidden;\"";
			}
			markup += "><span class=\"" + (checked ? this.css.checkBoxOn : this.css.checkBoxOff) + "\">";
			markup += "</span></span>";
			return markup;
		},
		_getCurrentNumber: function (id) {
			var seed;
			if (id !== undefined && id !== null) {
				seed = id;
			} else {
				seed = ++this._cIdx;
			}
			return "<span class=\"ui-iggrid-rowselector-row-number\">" +
				(seed + this.options.rowNumberingSeed + this._getStartingIndexForPage()) +
				"</span>";
		},
		_getStartingIndexForPage: function () {
			return (this.grid.dataSource.pageIndex() * this.grid.dataSource.pageSize());
		},
		_selectAllRows: function (selection) {
			var checkboxes = this._allCheckboxes(), i, range = [], row, rowId;
			if (checkboxes.length === 0) {
				return;
			}
			for (i = 0; i < checkboxes.length; i++) {
				row = checkboxes.eq(i).parent().parent();
				if (this.grid.hasFixedColumns()) {
					if (this.grid.fixedBodyContainer().attr("data-fixing-direction") === "left") {
						row = row.add(this.grid.element.find("tbody > tr").eq(row.index()));
					} else {
						row = row.add(this.grid.fixedBodyContainer().find("tbody > tr").eq(row.index()));
					}
				}
				rowId = selection._pkProp ? this.grid._fixPKValue(row.attr("data-id")) : i;
				range.push({ id: rowId, element: row, checkbox: checkboxes.eq(i) });
			}
			this._suspendHeader = true;
			this.grid._selection.rangeSelect(range, true, null, false, false);
			this._suspendHeader = false;
		},
		_selectAllVirtualRows: function (selection) {
			var dv = this._getDataView(), fr, lr, rangeStats;
			if (dv.length === 0) {
				return;
			}
			if (selection._pkProp) {
				fr = dv[ 0 ][ selection._pkProp ];
				lr = dv[ dv.length - 1 ][ selection._pkProp ];
			} else {
				fr = 0;
				lr = dv.length - 1;
			}
			rangeStats = selection._getRecordRange(fr, lr);
			this._suspendHeader = true;
			this.grid._selection.rangeSelect(rangeStats.range, true, null, rangeStats, false);
			/* D.K. 19 Sep 2015 Fix bug 206196 - When there is virtualization clicking
			on the header checkbox to select all rows, focus is applied to the last
			row and the scrollbar is not to the correct position */
			if (this.grid.options.virtualizationMode === "continuous") {
				this.grid._correctScrollPosition(this.grid._getTotalRowsCount());
			}
			this._suspendHeader = false;
		},
		_changeCheckStateForAllRecords: function (selection, toCheck) {
			var dv = this._getDSLocalRecords(), fr, lr, rangeStats;
			if (dv.length === 0) {
				return;
			}
			if (selection._pkProp) {
				fr = dv[ 0 ][ selection._pkProp ];
				lr = dv[ dv.length - 1 ][ selection._pkProp ];
			} else {
				fr = 0;
				lr = dv.length - 1;
			}
			rangeStats = selection._getRecordRange(fr, lr, dv);
			this._suspendHeader = true;
			if (toCheck) {
				this.grid._selection.rangeSelect(rangeStats.range, true, null, rangeStats, false);
			} else {
				this.grid._selection.rangeDeselect(rangeStats.range, rangeStats, false);
			}
			this._suspendHeader = false;
		},
		_deselectAllRows: function (selection) {
			var checkboxes = this._allCheckboxes(), i, row, rowId, range = [];
			if (checkboxes.length === 0) {
				return;
			}
			if (this.grid.element.data("igGridPaging")) {
				for (i = 0; i < checkboxes.length; i++) {
					row = checkboxes.eq(i).parent().parent();
					if (this.grid.hasFixedColumns()) {
						row = row.add(this.grid.scrollContainer().find("tbody > tr").eq(i));
					}
					rowId = selection._pkProp ? this.grid._fixPKValue(row.attr("data-id")) : i;
					range.push({ id: rowId, element: row, checkbox: checkboxes.eq(i) });
				}
				this._suspendHeader = true;
				this.grid._selection.rangeDeselect(range, false, false);
				this._suspendHeader = false;
			} else {
				this.grid._selection.deselectAll(false);
			}
		},
		_deselectAllVirtualRows: function (selection) {
			var dv = this._getDataView(), rangeStats, fr, lr;
			if (dv.length === 0) {
				return;
			}
			if (this.grid.element.data("igGridPaging")) {
				if (selection._pkProp) {
					fr = dv[ 0 ][ selection._pkProp ];
					lr = dv[ dv.length - 1 ][ selection._pkProp ];
				} else {
					fr = 0;
					lr = dv.length - 1;
				}
				rangeStats = selection._getRecordRange(fr, lr);
				this._suspendHeader = true;
				this.grid._selection.rangeDeselect(rangeStats.range, false, false);
				this._suspendHeader = false;
			} else {
				this.grid._selection.deselectAll(false);
			}
		},
		_isFirstRowSelected: function () {
			var row = this.grid.element.find("tbody > tr:not([data-grouprow='true']):first"),
				rowId = row.attr("data-id");
			rowId = rowId !== null && rowId !== undefined ?
				this.grid._fixPKValue(rowId) : this._getStartingIndexForPage();
			return this.grid._selection.isSelected(rowId, this.grid);
		},
		_rowHasSelection: function (data) {
			var sel = this._getSelectionInstance(), rowId = data[ this.grid.options.primaryKey || "ig_pk" ];
			if (!sel || rowId === null || rowId === undefined) {
				return false;
			}
			if (sel.options.mode === "cell") {
				return this.grid._selection.atLeastOneSelected(rowId, sel);
			}
			return this.grid._selection.isSelected(rowId, this.grid);
		},
		_rsRenderVirtualRecords: function (event, ui) {
			var i = 0, rs, rows, rowsLen;
			/* every time records are rerendered we need to clean rendering flags */
			this._cIdx = 0;
			if (this.grid.hasFixedColumns() &&
					this._getColumnFixingInstance().options.fixingDirection === "left") {
				rows = ui.fixedRows;
			} else {
				rows = ui.rows;
			}
			rowsLen = rows.length;
			/* generate rs dom and prepend to rows */
			for (i; i < rowsLen; i++) {
				rs = $(this._getRowSelectorCellMarkup());
				rs.prependTo(rows.eq(i));
			}
			/* register events */
			this._unregisterCellEvents();
			this._registerCellEvents();
			this._bindToSelectionCollection();
			if (this.options.enableCheckBoxes === true) {
				this._registerCheckBoxEvents();
			}
		},
		_rrn: function () {
			var rs = this._allRowSelectorCells(), cb = this._allCheckboxes(), cbx, i = 0, self = this,
				sri = this.grid._startRowIndex || 0, row, rowId;
			if (this.grid.options.virtualization === true &&
				this.grid.options.virtualizationMode === "continuous") {
				this._unregisterCellEvents();
				this._registerCellEvents();
				/* D.K. 16 Sep 2015 Fix bug 203995 - RowSelector’s checkbox gets unchecked when another
				checkbox is checked when virtualization and column fixing are enabled. */
				if (this.options.enableCheckBoxes === true) {
					this._unregisterCheckBoxEvents();
					this._registerCheckBoxEvents();
				}
			}
			if (this.grid.options.virtualizationMode === "fixed") {
				for (i = 0; i < rs.length; i++) {
					rs.eq(i).removeClass(this.css.nodeHovered);
					if (this.options.enableRowNumbering === true) {
						rs.eq(i)
							.children("span.ui-iggrid-rowselector-row-number")
							.text(sri + i + this.options.rowNumberingSeed + 1);
					}
				}
			}
			if (!this._skipRefresh) {
				rs.removeClass(this.css.rowSelectorSelected);
				cb.map(function () {
					self._alterCheckbox($(this), false);
				});
				for (i = 0; i < rs.length; i++) {
					row = rs.eq(i).closest("tr");
					rowId = this.grid._fixPKValue(row.attr("data-id"));
					if (rowId === null || rowId === undefined) {
						rowId = i + sri;
					}
					if (this.grid._selection.selection[ rowId ] !== undefined) {
						rs.eq(i).addClass(this.css.rowSelectorSelected);
						cbx = cb.eq(i);
						if (cbx.length === 1) {
							this._alterCheckbox(cbx, true);
						}
					}
				}
			}
		},
		_bindToSelectionCollection: function () {
			if (this.grid._selection && !this._subId) {
				this._subId = this.grid._selection.addSubscriber(this, this.grid.id());
			}
		},
		_select: function (info) {
			var res;
			info.element = info.element || this.grid._selection.elementFromIdentifier(info.id);
			res = this._rowSelectorFromSelection(info);
			if (!res) {
				return;
			}
			res.rowSelector.addClass(this.css.rowSelectorSelected);
			if (this.options.enableCheckBoxes) {
				this._alterCheckbox(res.checkbox, true);
				if (this._ms && !this._suspendHeader) {
					this._updateHeader();
				}
			}
		},
		_deselect: function (info) {
			var res, shouldDeselect;
			info.element = info.element || this.grid._selection.elementFromIdentifier(info.id);
			res = this._rowSelectorFromSelection(info);
			if (!res || !res.rowSelector.length) {
				return;
			}
			shouldDeselect = this.grid._selection instanceof $.ig.SelectedRowsCollection ||
				!this.grid._selection.atLeastOneSelected(
				this.grid._fixPKValue(info.element.parent().attr("data-id"))
				);
			if (shouldDeselect) {
				res.rowSelector.removeClass(this.css.rowSelectorSelected);
				if (this.options.enableCheckBoxes) {
					this._alterCheckbox(res.checkbox, false);
					if (this._ms && !this._suspendHeader) {
						this._updateHeader();
					}
				}
			}
		},
		_activate: function (element) {
			// should apply activated classes to the element
			if (element.is("th")) {
				element.addClass(this.css.rowSelectorActivated);
			}
		},
		_deactivate: function () {
			if (this.grid.hasFixedColumns()) {
				this.grid.fixedBodyContainer()
					.find("tbody")
					.find("th.ui-iggrid-activecell")
					.removeClass(this.css.rowSelectorActivated);
			}
			this.grid.element
				.find("tbody")
				.find("th.ui-iggrid-activecell")
				.removeClass(this.css.rowSelectorActivated);
		},
		_clearSelection: function () {
			var rsCells, self = this;
			if (this.grid.hasFixedColumns() && this.grid.fixingDirection() === "left") {
				rsCells = this.grid.fixedBodyContainer()
					.find("tbody")
					.find("th.ui-iggrid-selectedcell")
					.removeClass(this.css.rowSelectorSelected);
			} else {
				rsCells = this.grid.element
					.children("tbody")
					.find("th.ui-iggrid-selectedcell")
					.removeClass(this.css.rowSelectorSelected);
			}
			if (this.options.enableCheckBoxes) {
				$.each(rsCells, function () {
					self._alterCheckbox($(this).children("span:last"), false);
				});
				this._alterCheckbox(this._headerCheckbox(), false);
			}
		},
		_cellClick: function (event) {
			var args, target = $(event.target), fRow, rCell = target.closest("th"),
				rRow = rCell.parent(), rIdx = this._getVisibleRowIndex(rRow),
				rKey = rRow.attr("data-id"), fdCell, gridContainer,
				sel = this._getSelectionInstance();
			/* Updating draws in the first cell. If it is the Row Selector
			cell unwanted events are going to be thrown. */
			if (this.options.showCheckBoxesOnFocus === true && this._checkBoxesShown === false) {
				this._animateCheckboxes(true);
			}
			if (!sel) {
				return;
			}
			if (target.is("span") && target.attr("unselectable")) {
				return;
			}
			if (rKey === "" || rKey === null || rKey === undefined) {
				rKey = rIdx;
			}
			/* M.H. 22 Aug 2013 Fix for bug #149786: The row propery of the
			rowSelectorClicked's args object contains only the fixed row. */
			if (this.grid.hasFixedColumns()) {
				if (this.grid._isFixedElement(rCell)) {
					if (this.grid.options.virtualization || this.grid.options.rowVirtualization) {
						gridContainer = this.grid._vdisplaycontainer();
					} else {
						gridContainer = this.grid.scrollContainer();
					}
					fRow = rRow;
					rRow = gridContainer.find("tbody > tr").eq(fRow.index());
				} else {
					fRow = this.grid.fixedBodyContainer().find("tbody > tr").eq(rRow.index());
				}
			}
			fdCell = this._getFirstDataCell(rRow, fRow);
			if (event.shiftKey && this._ms) {
				/* shift selection will have different event args and event handling
				will be done once for the whole collection. Therefore we need a seperate
				function to handle it without convoluting the other scenarios */
				sel._shiftSelectChange(fdCell);
			} else {
				sel._singleSelectChange(fdCell, event.ctrlKey || event.metaKey);
			}
			args = {
				row: rRow,
				fixedRow: fRow,
				rowIndex: rIdx,
				rowKey: rKey,
				rowSelector: rCell,
				owner: this,
				grid: this.grid
			};
			this._triggerClickEvent(event, args);
		},
		_getFirstDataCell: function (row, frow) {
			if (frow && frow.length > 0) {
				frow.children("td").not("[data-skip='true'],[data-parent='true']").first();
			}
			return row.children("td").not("[data-skip='true'],[data-parent='true']").first();
		},
		_mouseHoverRow: function (event) {
			var par, tag, tr = event.target;
			while (tr) {
				par = tr.parentNode;
				tag = tr.nodeName;
				if (tag === "TR" && par.nodeName === "TBODY") {
					break;
				}
				tr = (tag === "TABLE") ? null : par;
			}
			if (this._hovTR !== tr) {
				this._mouseHoverRowHelper(this._hovTR, tr);
				if (this.grid.hasFixedColumns()) {
					this._mouseHoverFixedRow($(this._hovTR), $(tr));
				}
				this._hovTR = tr;
			}
		},
		_mouseHoverRowHelper: function (hovTr, tr) {
			if (hovTr && $(hovTr).attr("data-container") !== "true") {
				$("th." + this.css.rowSelector, hovTr).removeClass(this.css.nodeHovered);
			}
			if (tr && $(tr).attr("data-container") !== "true") {
				$("th." + this.css.rowSelector, tr).addClass(this.css.nodeHovered);
			}
		},
		_mouseHoverFixedRow: function ($hovTR, $tr) {
			var $fHTR, $fTR, ind;
			ind = $hovTR.index();
			$fHTR = this.grid.fixedBodyContainer().find("tr:nth-child(" + (ind + 1) + ")");
			ind = $tr.index();
			$fTR = this.grid.fixedBodyContainer().find("tr:nth-child(" + (ind + 1) + ")");
			this._mouseHoverRowHelper($fHTR, $fTR);
		},
		_mouseLeaveRow: function () {
			var ind, $fHTR;
			if (this._hovTR) {
				$("th", this._hovTR).removeClass(this.css.nodeHovered);
				if (this.grid.hasFixedColumns()) {
					ind = $(this._hovTR).index();
					$fHTR = this.grid.fixedBodyContainer().find("tr:nth-child(" + (ind + 1) + ")");
					$fHTR.find("th").removeClass(this.css.nodeHovered);
				}
				this._hovTR = null;
			}
		},
		_triggerClickEvent: function (event, args) {
			this._trigger(this.events.rowSelectorClicked, event, args);
		},
		_triggerCheckingEvent: function (event, args) {
			return this._trigger(this.events.checkBoxStateChanging, event, args);
		},
		_triggerCheckedEvent: function (event, args) {
			this._trigger(this.events.checkBoxStateChanged, event, args);
		},
		_getVisibleRowIndex: function (row) {
			return row.closest("tbody")
				.children("tr:not([data-container='true'],[data-grouprow='true'])")
				.index(row);
		},
		_getVisibleHRowIndex: function (row) {
			return row.closest("tbody")
				.children("tr:not([data-container='true'])")
				.index(row);
		},
		_animateCheckboxes: function (trans) {
			var h = this._headerCheckbox(),
				c = this._allCheckboxes(),
				all = h.add(c);
			if (trans === true) {
				all.css("visibility", "visible").css("opacity", 0).animate({ opacity: 1 }, 1000);
			} else {
				all.css("opacity", 1000)
					.animate({ opacity: 0 }, 1000, function () {
						$(this).css("visibility", "hidden");
					});
			}
			this._checkBoxesShown = trans;
		},
		_rowAdded: function () {
			this._unregisterCellEvents();
			this._registerCellEvents();
			if (this.options.enableCheckBoxes === true) {
				this._registerCheckBoxEvents();
			}
		},
		_resetIndexing: function () {
			this._cIdx = 0;
		},
		_groupedColumnsChanging: function () {
			// internal event of GroupBy
			// handle index reset events
			this._resetIndexing();
		},
		_cleanInterface: function (isRebind) {
			var header, footer, cols, rsCells, w;
			/* the function cleans all or some of the row selectors interface depending on context */
			if (isRebind === true) {
				this._alterCheckbox(this._headerCheckbox(), false);
				return;
			}
			cols = this.grid.element.children("colgroup").children("[data-role='rs']");
			cols = cols.add(this.grid.fixedBodyContainer().find("colgroup").children("[data-role='rs']"));
			if (this.grid.options.fixedHeaders === true) {
				cols = cols.add(this.grid.headersTable()
					.children("colgroup")
					.children("[data-role='rs']"));
				cols = cols.add(this.grid.fixedHeadersTable()
					.children("colgroup")
					.children("[data-role='rs']"));
			}
			if (this.grid.options.fixedFooters === true) {
				cols = cols.add(this.grid.footersTable()
					.children("colgroup")
					.children("[data-role='rs']"));
				cols = cols.add(this.grid.fixedFootersTable()
					.children("colgroup")
					.children("[data-role='rs']"));
			}
			footer = this._footerRowSelectorCells();
			header = this._headerRowSelectorCells();
			rsCells = this._allRowSelectorCells();
			if (!this.grid.options.width && this._functionsRedirected) {
				w = this.grid.container().css("width");
				if (w && !w.endsWith("%")) {
					this.grid.container().css("width", parseInt(w, 10) - this.options.rowSelectorColumnWidth);
				}
			}
			cols.remove();
			footer.remove();
			header.remove();
			rsCells.remove();
		},
		_allRowSelectorCells: function () {
			var rowSelectors, grid = this.grid;
			if (grid.hasFixedColumns() &&
					this._getColumnFixingInstance().options.fixingDirection === "left") {
				if (grid.options.virtualization || grid.options.rowVirtualization) {
					rowSelectors = grid.fixedBodyContainer()
						.find("tbody > tr > th." + this.css.rowSelector);
				} else {
					rowSelectors = grid.fixedBodyContainer()
						.find("tbody > tr > th." + this.css.rowSelector);
				}
			} else {
				rowSelectors = grid.element
					.children("tbody")
					.children("tr")
					.children("th." + this.css.rowSelector);
			}
			return rowSelectors;
		},
		_headerRowSelectorCells: function () {
			var headerCells = this.grid.headersTable()
				.children("thead").find("th[data-role='rs']");
			if (headerCells.length === 0 && this.grid.hasFixedColumns()) {
				headerCells = this.grid.fixedHeadersTable()
					.children("thead").find("th[data-role='rs']");
			}
			return headerCells;
		},
		_footerRowSelectorCells: function () {
			var footerCells = this.grid.footersTable()
				.children("tfoot").find("td[data-role='rs']");
			if (footerCells.length === 0 && this.grid.hasFixedColumns()) {
				footerCells = this.grid.fixedFootersTable()
					.children("tfoot").find("td[data-role='rs']");
			}
			return footerCells;
		},
		_allCheckboxes: function () {
			var checkboxes = this.grid.element.children("tbody")
				.children("tr").children("th").find("span[name='chk']");
			if (checkboxes.length === 0 && this.grid.hasFixedColumns() &&
				this._getColumnFixingInstance().options.fixingDirection === "left") {
				checkboxes = this.grid.fixedBodyContainer()
					.find("tbody > tr > th span[name='chk']");
			}
			return checkboxes;
		},
		_headerCheckbox: function () {
			var checkbox = this.grid.headersTable()
				.children("thead").find("tr > th span[name='hchk']");
			if (checkbox.length === 0 && this.grid.hasFixedColumns() &&
				this._getColumnFixingInstance().options.fixingDirection === "left") {
				checkbox = this.grid.fixedHeadersTable()
					.children("thead").find("tr > th span[name='hchk']");
			}
			return checkbox;
		},
		_rowSelectorFromSelection: function (info) {
			var element = info.element, rs, checkbox = info.checkbox, fixingDir;
			if (element.is("th")) {
				return;
			}
			if (checkbox) {
				/* available when called from checkbox selection */
				rs = checkbox.parent();
			} else {
				/* need to select the RS cell and checkbox */
				if (element.is("tr") || element.length === 2) {
					/* if we have row selection both rows will be present in the element jquery object */
					rs = element.children("th:first");
				} else if (this.grid.hasFixedColumns()) {
					fixingDir = this.grid.fixedBodyContainer().attr("data-fixing-direction");
					if (fixingDir === "left" && !this.grid._isFixedElement(element)) {
						// need to get the rs from the other table
						rs = this.grid.fixedBodyContainer()
							.find("tbody > tr")
							.eq(element.closest("tr").index())
							.children("th:first");
					} else if (fixingDir === "right" && this.grid._isFixedElement(element)) {
						rs = this.grid.element
							.find("tbody > tr")
							.eq(element.closest("tr").index())
							.children("th:first");
					}
				}
				if (!rs) {
					rs = element.closest("tr").children("th:first");
				}
				checkbox = this.options.enableCheckBoxes ? rs.children("span:last") : null;
			}
			return { rowSelector: rs, checkbox: checkbox };
		},
		_checkForSelection: function () {
			var i, isSelection = false;
			for (i = 0; i < this.grid.options.features.length; i++) {
				if (this.grid.options.features[ i ].name === "Selection") {
					isSelection = true;
					if (this.options.enableCheckBoxes === true) {
						this.grid.options.features[ i ].mode = "row";
					}
					break;
				}
			}
			if (isSelection === false) {
				if (this.options.requireSelection === true) {
					throw new Error($.ig.GridRowSelectors.locale.selectionNotLoaded);
				}
				this._skipRefresh = true;
			}
		},
		_checkForColumnVirtualization: function () {
			return (this.grid.options.virtualizationMode !== "continuous") &&
				((this.grid.options.virtualization === true && this.grid.options.width) ||
				this.grid.options.columnVirtualization === true);
		},
		_checkForRequireSelectionWithCheckboxes: function() {
			return this.options.requireSelection === false &&
				this.options.enableCheckBoxes === true;
		},
		_createHandlers: function () {
			this._headerRenderedHandler = $.proxy(this._headerRendered, this);
			this._footerRenderedHandler = $.proxy(this._footerRendered, this);
			this._recordsRenderingHandler = $.proxy(this._recordsRendering, this);
			this._recordsRenderedHandler = $.proxy(this._recordsRendered, this);
			this._gridRenderedHandler = $.proxy(this._gridRendered, this);
			this._virtualDomBuiltHandler = $.proxy(this._rsRenderVirtualRecords, this);
			this._virtualRowsHandler = $.proxy(this._rrn, this);
			this._renderExtraHeaderCellHandler = $.proxy(this._renderExtraHeaderCells, this);
			this._renderExtraFooterCellHandler = $.proxy(this._renderExtraFooterCells, this);
			this._rsClickHandler = $.proxy(this._cellClick, this);
			this._rrHoverHandler = $.proxy(this._mouseHoverRow, this);
			this._rrLeaveHandler = $.proxy(this._mouseLeaveRow, this);
			this._newRowAddedHandler = $.proxy(this._rowAdded, this);
			if (this.options.enableCheckBoxes === true) {
				this._checkboxMouseOverHandler = $.proxy(this._checkboxMouseOver, this);
				this._checkboxMouseOutHandler = $.proxy(this._checkboxMouseOut, this);
				this._checkboxClickHandler = $.proxy(this._checkBoxClicked, this);
				this._hCheckboxClickHandler = $.proxy(this._headerCheckBoxClicked, this);
			}
		},
		_registerEvents: function () {
			// these handle rendering of non-virtual grids
			this.grid.element.bind("iggridheaderrendered", this._headerRenderedHandler);
			this.grid.element.bind("iggridfooterrendered", this._footerRenderedHandler);
			this.grid.element.bind("iggridrowsrendering ", this._recordsRenderingHandler);
			this.grid.element.bind("iggridrowsrendered", this._recordsRenderedHandler);
			this.grid.element.bind("iggriddatarendered", this._gridRenderedHandler);
			/* these handle rendering of virtual grid */
			this.grid.element.bind("iggridvirtualdombuilt", this._virtualDomBuiltHandler);
			this.grid.element.bind("iggridvirtualrecordsrender", this._virtualRowsHandler);
			/* this handles new row updating row selector generation */
			this.grid.element.bind("iggridappendrowsondemandrowsrequested", this._newRowAddedHandler);
		},
		_unregisterEvents: function () {
			this.grid.element.unbind("iggridheaderrendered", this._headerRenderedHandler);
			this.grid.element.unbind("iggridfooterrendered", this._footerRenderedHandler);
			this.grid.element.unbind("iggridrowsrendering ", this._recordsRenderingHandler);
			this.grid.element.unbind("iggridrowsrendered", this._recordsRenderedHandler);
			this.grid.element.unbind("iggriddatarendered", this._gridRenderedHandler);
			this.grid.element.unbind("iggridvirtualdombuilt", this._virtualDomBuiltHandler);
			this.grid.element.unbind("iggridvirtualrecordsrender", this._virtualRowsHandler);
			this.grid.element.unbind("iggridappendrowsondemandrowsrequested", this._newRowAddedHandler);
		},
		_injectGrid: function (gridInstance, isRebind) {
			if (isRebind === true) {
				this._cleanInterface(true);
				return;
			}
			var self = this;
			this.grid = gridInstance;
			/* this._bindToSelectionCollection(isRebind); */
			this._v = this.grid.options.virtualization === true ||
				this.grid.options.rowVirtualization === true;
			this._flatRenderColgroup = this.grid._renderColgroup;
			this._createHandlers();
			/* adds cells rendering logic for other features */
			this.grid._headerInitCallbacks.push(
				{ type: "RowSelectors", func: this._renderExtraHeaderCellHandler }
			);
			this.grid._footerInitCallbacks.push(
				{ type: "RowSelectors", func: this._renderExtraFooterCellHandler }
			);
			this._registerEvents();
			/* register for colgroup creation */
			this.grid._renderColgroup = function () {
				if (self._flatRenderColgroup !== undefined) {
					self._rsRenderColgroup.apply(
						self.grid, $.merge([ self._flatRenderColgroup, self ], arguments)
					);
				}
			};
			this._checkForSelection();
			if (this._checkForColumnVirtualization()) {
				throw new Error($.ig.GridRowSelectors.locale.columnVirtualizationEnabled);
			}
			if (this._checkForRequireSelectionWithCheckboxes()) {
				throw new Error($.ig.GridRowSelectors.locale.requireSelectionWithCheckboxes);
			}
			if (String(this.grid.options.templatingEngine).toLowerCase() === "jsrender") {
				this._jsr = true;
				if (this.options.selectAllForPagingTemplate &&
					typeof this.options.selectAllForPagingTemplate === "string") {
					$.templates(this.grid.id() + "_selectAllForPagingTemplate",
						this.options.selectAllForPagingTemplate);
				}
				if (this.options.deselectAllForPagingTemplate &&
					typeof this.options.deselectAllForPagingTemplate === "string") {
					$.templates(this.grid.id() + "_deselectAllForPagingTemplate",
						this.options.deselectAllForPagingTemplate);
				}
			}
		},
		_getDataView: function () {
			return this.grid.dataSource.dataView();
		},
		_getAllData: function () {
			return this.grid.dataSource.data();
		}
	});
	$.extend($.ui.igGridRowSelectors, { version: "16.1.20161.2145" });
}(jQuery));
