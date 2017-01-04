$(document).ready(function() {
	isAuthUser();

	var addConferenceButton = $('#add-conference-button');

	addConferenceButton.on('click', function() {
		addNewConference();
	});


});

function isAuthUser() {
	$.getJSON('rest/user/current', function(response) {
		if (!response) {
			window.location.replace('index.html');
		}
	});
}

function addNewConference() {
	var title = $('#eventTopic').val(),
		description = $('#eventDescription').val(),
		date = $('#eventDate').val(),
		time = $('#eventTime').val(),
		price = $('#eventPrice').val(),
		seats = $('#eventSeats').val();

	console.log(date);
	console.log(time);

	// $.ajax({
	// 	type: 'POST',
	// 	url: 'rest/user/createEvent',
	// 	contentType: 'application/json'
	// });
}