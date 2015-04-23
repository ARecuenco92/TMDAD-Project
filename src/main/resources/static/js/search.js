function search(event){
	event.preventDefault();
	var criteria = $("#key-words");
	if(criteria.val().trim() === ""){
		criteria.parent("div").addClass("has-error");
	}
	else{
		criteria.parent("div").removeClass("has-error");
		var sarchFilter = $("#search-form").serializeArray();
		$.post('search', sarchFilter, function(data) {
			
		});
	}
}

$(document).ready(function() {
	$("#search-btn").on("click", search);
});