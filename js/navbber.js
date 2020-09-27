$(document).ready(function(e) {
	$("#h-arrow,#h-assets").click(function(e) {
		if( $("#h-btnshow").hasClass("h-mobileMenuLanguage___3lWvP") ){
	// 执行隐藏
			$("#h-btnshow").hide().removeClass("h-mobileMenuLanguage___3lWvP");
			document.getElementById("h-arrow-down").style.display = "none";
			document.getElementById("h-arrow-right").style.display = "block";
		}else{
			// 显示
			$("#h-btnshow").show().addClass("h-mobileMenuLanguage___3lWvP");
			document.getElementById("h-arrow-down").style.display = "block";
			document.getElementById("h-arrow-right").style.display = "none";
		}
	 });
});

	$(function(){
		var u=window.location.pathname;
		if(u.indexOf('block')>-1)
		{
			$('#h_block_link').css('color','#FFFFFF')
		}else if(u.indexOf('nodes')>-1){
			$('#h_nodes_link').css('color','#FFFFFF')
		}else if(u.indexOf('deal')>-1){
			$('#h_deal_link').css('color','#FFFFFF')
		}else if(u.indexOf('rankList')>-1){
			$('#h_rankList_link').css('color','#FFFFFF')
		}else if(u.indexOf('pool')>-1){
			$('#h_pool_link').css('color','#FFFFFF')
		}else if(u.indexOf('assets')>-1){
			$('#h_assets_link').css('color','#FFFFFF')
		}else if(u.indexOf('prove')>-1){
			$('#h_prove_link').css('color','#FFFFFF')
		}else if(u.indexOf('home')>-1){
			$('#h_index_link').css('color','#FFFFFF')
		}else if(u.indexOf('pool')>-1){
			$('#h_pool_link').css('color','#FFFFFF')
		}else if(u.indexOf('particulars')>-1){
			$('#h_deal_link').css('color','#FFFFFF')
		}else if(u.indexOf('account')>-1){
			$('#h_deal_link').css('color','#FFFFFF')
		}else if(u.indexOf('contract')>-1){
			$('#h_contract_link').css('color','#FFFFFF')
		}else if(u.indexOf('operate')>-1){
			$('#h_contract_link').css('color','#FFFFFF')
		}
	})
