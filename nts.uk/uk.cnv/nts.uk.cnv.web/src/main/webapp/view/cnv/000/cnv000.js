var server = "http://" + location.host;
var ajaxOption = {
	type: "POST",
	contentType: "application/json",
	dataType: "json",

	build: function (path, data) {
		this.dataType = "json";
		return $.extend({url:server + path, data: JSON.stringify(data)}, this);
	},
	buildWithDataType: function (path, dataType, data) {
		this.dataType = dataType;
		return $.extend({url:server + path, data: JSON.stringify(data)}, this);
	}
};
$(function () {
	$("#export").click(function () {
		$('#result').val('');
		$.ajax(ajaxOption.build("/nemunoki.oruta.web/webapi/ddl/exporttofile", {
			path: $('#folderpath_export').val(),
			type: $('[name="ddl_type_export"]:checked').val(),
			withComment:
				$('#withComment').prop('checked') ? true : false,
			oneFile:
				($('[name="oneOrMultiple"]:checked').val() === "true") ? true : false
		})).done(function (res) {
			console.log(res);
			if (typeof res === "undefined") {
				$('#result').val('処理が完了しました。');
			}
			else {
				$('#result').val('処理が完了しました。' + res.message);
			}
		}).fail(function(rej){
			console.log(rej);
			if (typeof rej.responseStatus === "undefined" || typeof rej.stackTrace === "undefined") {
				$('#result').val('処理は異常終了しました。');
			}
			else {
				$('#result').val('処理は異常終了しました。' + rej.responseStatus + rej.stackTrace);
			}
		});
	});
	$("#import_erp").click(function () {
		$('#result').val('');
		$.ajax(ajaxOption.build("/nts.uk.cnv.web/webapi/cnv/tableinfo/importfromfile", {
			path: $('#folderpath_import_erp').val(),
			type: $('[name="ddl_type_import_erp"]:checked').val()
		})).done(function (res) {
			console.log(res);
			if (typeof res === "undefined") {
				$('#result').val('処理が完了しました。');
			}
			else {
				$('#result').val('処理が完了しました。' + res.message);
			}
		}).fail(function(rej){
			console.log(rej);
			if (typeof rej.responseStatus === "undefined" || typeof rej.stackTrace === "undefined") {
				$('#result').val('処理は異常終了しました。');
			}
			else {
				$('#result').val('処理は異常終了しました。' + rej.responseStatus + rej.stackTrace);
			}
		});
	});
});
