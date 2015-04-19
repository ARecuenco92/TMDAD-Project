$(document).ready(function() {
	resumeParties();
	setupTimeline();
	setupDial();
});

function setupTimeline() {
	$.get('timeline', function(data) {
		var template = $('#twitterBlock').html();
		Mustache.parse(template); 
		var rendered = Mustache.render(template, data);
		$('#timelineTwitter').html(rendered);
	});	
	
	$.get('timelineFacebook', function(data) {
		var template = $('#facebookBlock').html();
		Mustache.parse(template); 
		var rendered = Mustache.render(template, data);
		$('#timelineFacebook').html(rendered);
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

function resumeParties(){
	$.get('resumeParties', function(data) {
		var template = $('#resumeBlock').html();
		Mustache.parse(template); 
		var rendered = Mustache.render(template, data);
		$('#resumeParties').append(rendered);
	});	
}

function setupDial(){
	$('.knob').knob({
		value : 0,
		'readOnly' : true,
		'width' : 120,
		'height' : 120,
		'dynamicDraw' : true,
		'thickness' : 0.2,
		'tickColorizeValues' : true,
		'fgColor' : "#D3D4D2",
		'bgColor' : "#F8F9F7",
		'skin' : 'tron'
	});
	myDelay(0);
}

function myDelay(value) {
    $('.knob').val(value).trigger("change");
    if($('.knob').data('max') > value){
    	setTimeout(myDelay, 50, value+1);
    }
    else{
    	setTimeout(myDelay, 50, 0);
    }   
}