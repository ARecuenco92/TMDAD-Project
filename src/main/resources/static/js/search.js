function search(event){
	event.preventDefault();
	var criteria = $("#key-words");
	if(criteria.val().trim() === ""){
		criteria.parent("div").addClass("has-error");
	}
	else{
		criteria.parent("div").removeClass("has-error");
		var filter = {};
		filter['politicalParties'] = $("[name='political-party']").val();
		filter['user'] = $("[name='user']").val();
		filter['keyWords'] = $("[name='key-words']").val();
		filter['sortBy'] = $("[name='sort-by']").val();
		$.ajax({
			url : 'search',
			data : JSON.stringify(filter),
			method: "POST",
			contentType : "application/json; charset=utf-8",
			success: function(data) {
				var template = $('#twitterMsn').html();
				Mustache.parse(template); 
				var rendered = Mustache.render(template, data);
				$('#results').html(rendered);
			}
		});
	}
}

$(document).ready(function() {
	$("#search-btn").on("click", search);
});