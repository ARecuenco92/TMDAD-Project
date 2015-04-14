$(document).ready(function() {
	timelineSearch();
});

function timelineSearch() {
	$.get('timeline', function(data) {
		var template = $('#timeline').html();
		Mustache.parse(template); 
		var rendered = Mustache.render(template, data);
		$('#timelineBlock').append(rendered);
	});	
}