// TODO: Fix user authentication!

$(document).ready(function() {
	isAuthUser();

	loadConferenceData();

	var editConferenceButton = $('#edit-conference-button');

	editConferenceButton.on('click', function() {
		editConference(conferenceI);
	})
});

function isAuthUser() {
	$.getJSON('rest/user/current', function(response) {
		if (!response) {
			window.location.replace('index.html');
		}
		else {
			var currentUser = response.user;
			
			$('.profileMenu__btn--logout').show();
            $('#account').removeAttr("data-toggle");
            $('#account').removeAttr("data-target");
            $('#account').text(currentUser.firstName);
		}
	});
}

function loadConferenceData() {
	var eventId = sessionStorage.getItem('editConferenceId');
	$.getJSON('rest/events/eventByEventId?eventId=' + eventId, function(response) {
		var eventData = response.event;

		console.log(eventData);

		var title = eventData.title,
			description = eventData.description,
			date = eventData.date.split(' ')[0],
			time = eventData.date.split(' ')[1],
			price = eventData.price,
			location = eventData.hall.location,
			hallName = eventData.hall.name,
			city = eventData.hall.city,
			availableSeats = eventData.hall.capacity;

		$('#eventTopic').val(title);
		$('#eventDescription').val(description);
		$('#eventDate').val(date);
		$('#eventTime').val(time);
		$('#eventPrice').val(price);
		$('#eventAddress').val(location);
		$('#eventHall').val(hallName);
		$('#eventCity').val(city);
		$('#eventSeats').val(availableSeats);

		$.getJSON('rest/lectures/getByEventId?eventId=' + eventId, function(res) {
        	var lecturesData = res.lecture;

        	var len = lecturesData.length;

        	if (len === 0) {
        		$('#edit-lectures-table').append('<div class="alert alert-info">Currently there are no lectures for this event!</div>');
        	}
        	else {
        		for (var i = 0; i < len; i++) {
        			var lectureTitle = lecturesData[i].title,
        				lecturerName = lecturesData[i].lecturerName;

        			var lectureHtml = '<tr>' +
                    	'<td class="managerTable__title">' +
                        	'<a href="editlecture.html">Building a Future Proof Artificial Intelligence</a>' +
                    	'</td>' +
                    	'<td class="managerTable__date">Ivan Ivanov</td>' +
                    	'<td class="managerTable__CTA">' +
                        	'<a href="editlecture.html" class="btn btn-primary btn-xs">Edit</a>' +
                        	'<a href="#" class="btn btn-danger btn-xs">Delete</a>' +
                    	'</td>' +
                	'</tr>';

                    $('#edit-lectures-table').append(lectureHtml);
        		}
        	}
		});
	});
}

function editConference() {

}