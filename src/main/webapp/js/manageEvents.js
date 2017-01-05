$(document).ready(function() {
	isAuthUser();

	// getAllEventsCreatedByUser(currentUserId);
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
            $('#account').text(currentUser.firstName + ' ' + currentUser.lastName);
		}
	}).done(function(response) {
		var currentUserId = response.user.userId;
		getAllEventsCreatedByUser(currentUserId);
	});
}

function getAllEventsCreatedByUser(userId) {
	$.getJSON('rest/events/eventsByCreatorId?creatorId=' + userId, function(response) {
		console.log('data returned from server');
		console.log(response);

		var eventsData = response.event;

		var i, len = eventsData.length;

		if (len === 0) {
			$('#manage-events-table').text('You have not created any events!');
		}
		else {
			for (i = 0; i < len; i++) {
				var	id = eventsData[i].eventId,
					title = eventsData[i].title,
					date = eventsData[i].date.split(' ')[0],
					status = eventsData[i].status;

				var statusLabelClass;

				switch(status) {
    				case 'NEW':
    					statusLabelClass = 'label-warning';
        				break;
    				case 'NOT_APPROVED':
        				statusLabelClass = 'label-danger';
        				break;
    				default:
        				statusLabelClass = 'label-default';
				}

				if (status == 'NOT_APPROVED') {
					status = 'NOT APPROVED';
				}

				status = toTitleCase(status);

				var eventHtml = '<tr id="event' + id + '">' +
                    '<td class="managerTable__title">' +
                        '<a href="#">' + title + '</a>' +
                        '<span class="label ' + statusLabelClass + '">' + status + '</span>' +
                    '</td>' +
                    '<td class="managerTable__date">' + date + '</td>' +
                    '<td class="managerTable__CTA">' +
                    '</td>' +
                '</tr>';

                $('#manage-events-table').append(eventHtml);

                if (status == 'New') {
                	$('#event' + id + ' td.managerTable__CTA').append(' <a href="#" class="btn btn-success btn-xs">Publish</a>');
                }
                if (status == 'New' || status == 'Pending' || status == 'Not Approved') {
                	$('#event' + id + ' td.managerTable__CTA').append(' <a href="editevent.html" class="btn btn-primary btn-xs">Edit</a>');
                }

                $('#event' + id + ' td.managerTable__CTA').append(' <a href="#" class="btn btn-primary btn-xs">Add Lecture</a> <a href="#" class="btn btn-danger btn-xs">Delete</a>');
			}
		}
	});
}

function toTitleCase(str)
{
    return str.replace(/\w\S*/g, function(txt) {
    	return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
    });
}