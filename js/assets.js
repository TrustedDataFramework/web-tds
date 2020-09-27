// var pageIndex = 1;
// var totalPage = 0;

function getTransferLogList(pageSize, pageIndex) {
	var pageSize = pageSize || 10;
	var pageIndex = pageIndex || 0;
	var pageSize = GetQueryString("select");
	if (pageSize == null) {
		pageSize = 10;
	}
	if(pageIndex > 0){
		pageIndex = pageIndex -1;
	}
	//数据请求部分
	$.get("/v2-web/get_coin_list", {
			per_page: pageSize,
			page: pageIndex
		},

		function(result) {
			if (result.code == "2000") {
				for (let i = 0; i < result.data.content.length; i++) {
					result.data.content[i].number = ((pageIndex) * pageSize) + i + 1;
					if (result.data.content[i].image_url == null || result.data.content[i].image_url == "") {
						result.data.content[i].image_url = "/img/coin_Logo.png";
					}
				}
				setHtml(result.data.content, 'tpl2', 'block-content');
				//分页处理
				$('#totalCount').html(result.data.totalElements);
				$('#curr_page').html(pageIndex + 1);
				$('#totalPage').html(result.data.totalPages);

			}
		});

}


var pageIndex = GetQueryString("pageIndex");

var startIndex2 = GetQueryString("select");
if(startIndex2 == null){
	startIndex2 = 10;
}

if (pageIndex != undefined &&
	pageIndex != null &&
	pageIndex != "undefined" &&
	pageIndex != "null" &&
	pageIndex != "") {

	if (pageIndex < 1) {
		pageIndex = 1;
	}
	getTransferLogList(startIndex2, pageIndex);

} else {
	getTransferLogList(startIndex2, 0);
}

function changePageSize(page){
	let startIndex = document.getElementById("select").value;
	let total = $('#totalPage').html();
	if(page == undefined ||
		page == null ||
		page == "undefined" ||
		page == "null" ||
		page == ""){
		location.href = "assets.html?pageIndex=1&select=" + startIndex;
	}else {
		if(parseInt(total)<parseInt(page)){
			alert("超过最大页数");
			return;
		}else {
			location.href = "assets.html?pageIndex=" + page + "&select=" + startIndex;
		}
	}
}

$(function() {
	//首页
	$("#first_page").click(function() {
		//getTransferLogList(10, 1);
		var curr_page = parseInt($('#curr_page').html());
		let startIndex = document.getElementById("select").value;
		if(curr_page > 1) {
			location.href = "assets.html?pageIndex=1&select=" + startIndex;
		}
	});

	//最后一页
	$('#last_page').click(function() {
		var curr_page = parseInt($('#curr_page').html());
		var totalPage = $('#totalPage').html();
		let startIndex = document.getElementById("select").value;
		if(curr_page < totalPage) {
			location.href = "assets.html?pageIndex=" + totalPage + "&select=" + startIndex;
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
			location.href = "assets.html?pageIndex=" + pageIndex + "&select=" + startIndex;
		}
	});
	//下一頁
	$("#next_page").click(function() {
		var curr_page = parseInt($('#curr_page').html());
		var totalPage = parseInt($('#totalPage').html());
		if (curr_page < totalPage) {
			pageIndex = curr_page + 1;
		}else{
			pageIndex=curr_page;
		}
		let startIndex = document.getElementById("select").value;
		if(curr_page < totalPage){
			location.href = "assets.html?pageIndex=" + pageIndex + "&select=" + startIndex;
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

function soso_coin() {
	let sosoval = document.getElementById("soso_coin").value;
	if (sosoval == "") {
		alert("请输入搜索内容!");
		return;
	}
	location.href = "assetsList.html?coinaddress="+ sosoval;
}

function jumpSize(){
	let page = document.getElementById("page").value;
	if(isNaN(page)|| !(/(^[1-9]\d*$)/.test(page))){
		alert("请输入正确的数字!");
	}
	changePageSize(page);
}
