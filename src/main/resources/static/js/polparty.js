var politicalParty;

$(document).ready(function() {
	if(window.location.hash) {
		politicalParty = window.location.hash.substring(1);
		$("[id='"+politicalParty+"']").parent().addClass('active');
	}
	$(".party").click(displayPoliticalParty);
});

function displayPoliticalParty(event){
	event.preventDefault();
	var nextParty = $(this).attr('id');
	if(nextParty != politicalParty){
		$("[id='"+politicalParty+"']").parent().removeClass('active');
		$("[id='"+nextParty+"']").parent().addClass('active');
		document.location.hash = nextParty;
		politicalParty = nextParty;
	}
}