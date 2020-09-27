/**
 * 根据事务hash获取区块信息
 * @param {Object} hash
 */
function getBalance(coinaddress) {
	//var hash = "undefined" ? "" : hash;
	if (coinaddress == "") {
		//alert();
		$("#con-box-yu").html("No information was found！");
		return;
	}
	$("#accountAddress").html(coinaddress);

	//数据请求部分
	$.post("/getAddressBalance/", {
			address: coinaddress
		},
		function(result) {
			if (result.code == "2000") {
				setHtml(result, 'tpl', 'con-box-yu');
			}

		});
}

function getTransferLogList(coinaddress, pageIndex,startIndex2) {
	var pageIndex=pageIndex||1;
	if (coinaddress == "") {
		//alert();
		$("#block-content").html("No information was found！");
		return;
	}

	$("#accountAddress").html(coinaddress);
	if(pageIndex >0){
		pageIndex = pageIndex -1;
	}
	//数据请求部分
	$.get("/v2-web/get_transfer_list", {
			from_address: coinaddress,
			per_page: startIndex2,
			page: pageIndex
		},

		function(result) {
			if (result.code == "2000") {
				for (var i = 0; i < result.data.content.length; i++) {
					result.data.content[i].hash = result.data.content[i].hash;
					let hash = result.data.content[i].hash.substring(0, 5) + "***" + result.data.content[i].hash.substring(result.data.content[
						i].hash.length - 5, result.data.content[
						i].hash.length);
					result.data.content[i].txn_hash = hash;
					result.data.content[i].number = ((pageIndex) * startIndex2) + i + 1;
					result.data.content[i].created_at = getTime(result.data.content[i].created_at);
					result.data.content[i].amount = toNonExponential(result.data.content[i].amount);
				}
				setHtml(result.data.content, 'tpl2', 'block-content');
				//分页处理
				$('#totalCount').html(result.data.totalElements);
				$('#curr_page').html(pageIndex + 1);
				$('#totalPage').html(result.data.totalPages);
			}
		});
}

//将科学计数法转换为小数
function toNonExponential(num) {
	var m = num.toExponential().match(/\d(?:\.(\d*))?e([+-]\d+)/);
	return num.toFixed(Math.max(0, (m[1] || '').length - m[2]));
}

//getBalance("1FyA6jTrC2MSKoLUS8BxZdqgZWkxFH7G1n");
//getTransferLogList("1FyA6jTrC2MSKoLUS8BxZdqgZWkxFH7G1n");
var GetQueryString_address = GetQueryString("coinaddress");
var pageIndex = GetQueryString("pageIndex");
var startIndex2 = GetQueryString("select");
if(startIndex2 == null){
	startIndex2 = 10;
}
if (GetQueryString_address != undefined &&
	GetQueryString_address != null &&
	GetQueryString_address != "undefined" &&
	GetQueryString_address != "null" &&
	GetQueryString_address != "" && pageIndex != undefined &&
	pageIndex != null &&
	pageIndex != "undefined" &&
	pageIndex != "null" &&
	pageIndex != "") {
	$("#soso").val(GetQueryString_address);
	$("#sosowap").val(GetQueryString_address);
	getBalance(GetQueryString_address);
	getTransferLogList(GetQueryString_address,pageIndex,startIndex2);
}else{
	$("#soso").val(GetQueryString_address);
	//alert($("#soso").val());
	$("#sosowap").val(GetQueryString_address);

	//alert($("#sosowap").val());
	getBalance(GetQueryString_address);
	getTransferLogList(GetQueryString_address,0,startIndex2);
}

function changePageSize(page){
	let startIndex = document.getElementById("select").value;
	var GetQueryString_address = GetQueryString("coinaddress");
	let total = $('#totalPage').html();
	if(page == undefined ||
		page == null ||
		page == "undefined" ||
		page == "null" ||
		page == "") {
		location.href = "particulars.html?pageIndex=1&coinaddress=" + GetQueryString_address + "&select=" + startIndex;
	}else{
		if(parseInt(total)<parseInt(page)){
			alert("超过最大页数");
			return;
		}else {
			location.href = "particulars.html?pageIndex=" + page + "&coinaddress=" + GetQueryString_address + "&select=" + startIndex;
		}
	}
}

$(function() {
	var coinaddress=GetQueryString_address;//$("#soso").val();
	//首页
	$("#first_page").click(function() {
		//getTransferLogList(10, 1);
		var curr_page = parseInt($('#curr_page').html());
		let startIndex = document.getElementById("select").value;
		if(curr_page > 1) {
			location.href = "particulars.html?pageIndex=1&coinaddress=" + coinaddress + "&select=" + startIndex;
		}
	});

	//最后一页
	$('#last_page').click(function() {
		var totalPage = $('#totalPage').html();
		var curr_page = parseInt($('#curr_page').html());
		let startIndex = document.getElementById("select").value;
		if(curr_page < totalPage) {
			location.href = "particulars.html?pageIndex=" + totalPage + "&coinaddress=" + coinaddress + "&select=" + startIndex;
		}
		//getTransferLogList(10, totalPage);
	});
	//上一頁
	$("#pre_page").click(function() {
		var curr_page = parseInt($('#curr_page').html());
		var totalPage = parseInt($('#totalPage').html());
		if (curr_page > 1) {
			pageIndex = curr_page - 1;
		}else{
			pageIndex=curr_page;
		}
		let startIndex = document.getElementById("select").value;
		if(curr_page > 1) {
			location.href = "particulars.html?pageIndex=" + pageIndex + "&coinaddress=" + coinaddress + "&select=" + startIndex;
		}
	});
	//下一頁
	$("#next_page").click(function() {
		var curr_page = parseInt($('#curr_page').html());

		var totalPage = parseInt($('#totalPage').html());
		if (curr_page < totalPage) {
			pageIndex = curr_page+ 1;
		}else{
			pageIndex=curr_page;
		}
		let startIndex = document.getElementById("select").value;
		if(curr_page < totalPage) {
			location.href = "particulars.html?pageIndex=" + pageIndex + "&coinaddress=" + coinaddress + "&select=" + startIndex;
		}
	});
})

$(document).ready(function(){
	var test = GetQueryString("select");
	if (test == null){
		$("#select").val("10");
	}else {
		$("#select").val(test);
	}
});

function jumpSize(){
	let page = document.getElementById("page").value;
	if(isNaN(page)|| !(/(^[1-9]\d*$)/.test(page))){
		alert("请输入搜索内容!");
	}
	changePageSize(page);
}

function replaceAll(str)
{
	if(str!=null)
		str = str.replace(/-/g,"/")
	return str;
}


function getTime(UTCDateString) {
	//获取当前时区
	var offset = new Date().getTimezoneOffset()/60;
	if(!UTCDateString){
		return '-';
	}
	//UTCDateString = renderTime(UTCDateString);
	UTCDateString = UTCDateString.replace("-","/");
	UTCDateString = UTCDateString.replace("T"," ");
	UTCDateString = replaceAll(UTCDateString);
	UTCDateString = UTCDateString.substring(0,19);

	function formatFunc(str) {    //格式化显示
		return str > 9 ? str : '0' + str
	}
	var date2 = new Date(UTCDateString);//这步是关键
	var year = date2.getFullYear();
	var mon = formatFunc(date2.getMonth() + 1);
	var day = formatFunc(date2.getDate());
	var hour = date2.getHours();
	hour = formatFunc(hour);
	var min = formatFunc(date2.getMinutes());
	var sec = formatFunc(date2.getSeconds());
	var dateStr = year+'/'+mon+'/'+day+' '+hour+':'+min+':'+sec;
	//var dateStr = "2020/9/2 23:01:01";
	var dateStr1 = eosFormatTime2(dateStr,offset);
	var year1 = dateStr1.getFullYear();
	var mon1 = formatFunc(dateStr1.getMonth() + 1);
	var day1 = formatFunc(dateStr1.getDate());
	var hour1 = dateStr1.getHours();
	hour1 = formatFunc(hour1);
	var min1 = formatFunc(dateStr1.getMinutes());
	var sec1 = formatFunc(dateStr1.getSeconds());
	var dateStr2 = year1+'-'+mon1+'-'+day1+' '+hour1+':'+min1+':'+sec1;
	return dateStr2;
}

function eosFormatTime2(oldTimes1,offset) {
	var x = oldTimes1; // 取得时间"2017-07-08 13:00:00"
	var time = new Date(x);
	var timeNum = offset;//小时数
	time.setHours(time.getHours() - timeNum);
	return time;
}