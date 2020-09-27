/**
 * 根据事务hash获取区块信息
 * @param {Object} hash
 */
function userTransferLog(hash) {
	if ( hash== "") {
		$("#content").html("No information was found！");
		return;
	}
	//数据请求部分
	$.get("/rpc/transaction/"+hash,
		function(result) {
			if (result.code == "200") {
				setHtml(result.data,'tpl','content');
			}
	});
}
let hash = GetQueryString("hash");
if(hash != undefined &&
	hash != null &&
	hash != "undefined" &&
	hash != "null" &&
	hash != "") {
	$("#soso").val(hash);
	$("#sosowap").val(hash);
	userTransferLog(hash);
}
