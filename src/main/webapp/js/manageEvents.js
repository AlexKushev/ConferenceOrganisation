$(document).ready(function() {
	isAuthUser();
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

				var eventHtml = '<tr id="' + id + '">' +
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
                	$('#' + id + ' td.managerTable__CTA').append(' <button class="btn btn-success btn-xs publish">Publish</button>');
                }
                if (status == 'New' || status == 'Pending' || status == 'Not Approved') {
                	$('#' + id + ' td.managerTable__CTA').append(' <button class="btn btn-primary btn-xs edit">Edit</button>');
                }

                $('#' + id + ' td.managerTable__CTA').append(' <button class="btn btn-primary btn-xs add-lecture">Add Lecture</button> <button class="btn btn-danger btn-xs delete">Delete</button>');
			}
		}
	}).done(function() {
		$('.publish').on('click', function(e) {
			var target = e.currentTarget;
			var parent = $(target).parent();
			var grandParent = $(parent).parent();
			var eventId = $(grandParent).attr('id');
			publishEvent(eventId);
		});
		
		$('.delete').on('click', function(e) {
			var target = e.currentTarget;
			console.log(target);
			var parent = $(target).parent();
			var grandParent = $(parent).parent();
			var eventId = $(grandParent).attr('id');
			deleteEvent(eventId);
		});

		$('.edit').on('click', function(e) {
			var target = e.currentTarget;
			var parent = $(target).parent();
			var grandParent = $(parent).parent();
			var eventId = $(grandParent).attr('id');
			sessionStorage.setItem('editConferenceId', eventId);
			window.location.href = 'editevent.html';
		});

		$('.add-lecture').on('click', function(e) {
			var target = e.currentTarget;
			var parent = $(target).parent();
			var grandParent = $(parent).parent();
			var eventId = $(grandParent).attr('id');
			sessionStorage.setItem('conferenceId', eventId);
			window.location.href = 'addlecture.html';
		});
	});
}

function publishEvent(eventId) {
	$.ajax({
		type: 'POST',
		url: 'rest/events/review?eventId=' + eventId
	}).done(function() {
		alert('Successfully send event for review!');
		window.location.reload();
	}).fail(function() {
		alert('Failed to send event for review');
	});
}

function deleteEvent(eventId) {
	$.ajax({
		type: 'POST',
		url: 'rest/events/delete?eventId=' + eventId
	}).done(function() {
		alert("Seccessfully deleted!")
		window.location.reload();
	}).fail(function() {
		alert("Failed to delete")
	})
}

function toTitleCase(str)
{
    return str.replace(/\w\S*/g, function(txt) {
    	return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
    });
}