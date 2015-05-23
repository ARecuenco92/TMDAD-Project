var politicalParty;

$(document).ready(function() {
	if(window.location.hash) {
		politicalParty = window.location.hash.substring(1);
		displayPoliticalParty();
	}
	$(".party").click(changePoliticalParty);
});

function changePoliticalParty(event){
	event.preventDefault();
	$("#navbar").collapse('hide');
	var nextParty = $(this).attr('id');
	if(nextParty != politicalParty){
		$("[id='"+politicalParty+"']").parent().removeClass('active');
		politicalParty = nextParty;
		document.location.hash = politicalParty;
		displayPoliticalParty();
	}
}

function displayPoliticalParty(){
	$("[id='"+politicalParty+"']").parent().addClass('active');
	setupTimeline();
	setupDial();
}

function setupTimeline() {
	$('#timelineTwitter').html('<div class="text-center panel-knob"><input class="knob" data-max="50" data-displayInput="false"/></div>');
	$('#timelineFacebook').html('<div class="text-center panel-knob"><input class="knob" data-max="50" data-displayInput="false"/></div>');
	
	$.get('twitter/timeline/'+politicalParty, function(data) {
		var template = $('#twitterBlock').html();
		Mustache.parse(template); 
		var rendered = Mustache.render(template, data);
		$('#timelineTwitter').html(rendered);
	});	

	$.get('facebook/timeline/'+politicalParty, function(data) {
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

function setupDial(){
	$('.knob').knob({
		value : 0,
		'readOnly' : true,
		'width' : 80,
		'height' : 80,
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