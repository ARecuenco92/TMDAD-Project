$(document).ready(function() {
	resumePaties();
	setupTimeline();
});

function setupTimeline() {
	$.get('timeline', function(data) {
		var template = $('#twitterBlock').html();
		Mustache.parse(template); 
		var rendered = Mustache.render(template, data);
		$('#timelineTwitter').append(rendered);
	});	
	
	$.get('timelineFacebook', function(data) {
		var template = $('#facebookBlock').html();
		Mustache.parse(template); 
		var rendered = Mustache.render(template, data);
		$('#timelineFacebook').append(rendered);
	});	
	
	$('#twitter').click(function() {
		$('#timelineTwitter').css("display", "block");
		$('#timelineFacebook').css("display", "none");
	});
	
	$('#facebook').click(function() {
		$('#timelineTwitter').css("display", "none");
		$('#timelineFacebook').css("display", "block");
	});
	
}

function resumePaties(){
	$.get('resumeParties', function(data) {
		var template = $('#resumeBlock').html();
		Mustache.parse(template); 
		var rendered = Mustache.render(template, data);
		$('#resumeParties').append(rendered);
	});	
}