$(function() {

	$.ajax(
	{
		url:"/WisdomCore/ExplorerInfo",
			success:function(result){
			setHtml(result.data, 'tpl1', 'block_data_browser');},
			error:function(result){console.log("error");}
	}
	);
});

