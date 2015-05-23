var politicalParty;

$(document).ready(function() {
	if(window.location.hash) {
		var politicalParty = window.location.hash.substring(1);
	}
});