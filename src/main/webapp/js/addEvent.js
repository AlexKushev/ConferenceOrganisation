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
		location = $('#eventAddress').val(),
		hallName = $('#eventHall').val(),
		city = $('#eventCity').val(),
		seats = $('#eventSeats').val();

	console.log(date);
	console.log(time);
	console.log(hallName);
	console.log(city);

	var conferenceData = {
		event: {
			title: title,
			description: description,
			date: date + ' ' + time,
			price: price,
			hall: {
				name: hallName,
				location: location,
				city: city,
				capacity: seats
			}
		}
	};

	console.log(JSON.stringify(conferenceData));

	if (!validateConferenceData(conferenceData)) {
        alert('Invalid data!');
        return;
    }

	$.ajax({
		type: 'POST',
		url: 'rest/user/createEvent',
		contentType: 'application/json',
		data: JSON.stringify(conferenceData)
	}).done(function() {
		console.log(JSON.stringify(conferenceData));
		alert('Successfully added new conference!')
	}).fail(function() {
		alert('Invalid data!');
	});
}

function validateConferenceData(conferenceData) {
	var event = conferenceData.event;

	var date = $('#eventDate').val(),
		time = $('#eventTime').val();

    function validateLength(str, min, max) {
        return str.length >= min && str.length <= max;
    }

    function validateIfEmpty(str) {
        if (str.trim() === null || str.trim() === '' || str.trim() === ' ') {
            return true;
        }

        return false;
    }

    if (validateIfEmpty(event.title) || !validateLength(event.title, 4, 40) || validateIfEmpty(event.description) || !validateLength(event.description, 5, 10000) || validateIfEmpty(date) || validateIfEmpty(time) || event.price <= 0 || validateIfEmpty(event.hall.name) || !validateLength(event.hall.name, 2, 30) || validateIfEmpty(event.hall.location) || !validateLength(event.hall.location, 5, 50) || validateIfEmpty(event.hall.city) || !validateLength(event.hall.city, 2, 20) || event.hall.capacity <= 0) {
        return false;
    }

    return true;
 }