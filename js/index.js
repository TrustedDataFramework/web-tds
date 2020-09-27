$(function() {
	$.get("/rpc/stat",
		function(result) {
			if(result.code == "200"){
				setHtml(result.data, 'tpl1', 'block_data_browser');
				let height = result.data.height;
				$.get("/rpc/block/"+height,
					function(result1) {
						if(result1.code == "200"){
							setHtml(result1.data, 'tpl', 'block_data_test');
						}
					}
				);
			}
		}
	);
 });
