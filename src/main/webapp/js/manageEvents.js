$(document).ready(function() {
	isAuthUser();
});

function isAuthUser() {
	$.getJSON('rest/user/current', function(response) {
		if (!response) {
			window.location.replace('index.html');
		}
	}).done(function(response) {
		var currentUserId = response.user.userId;
		getAllEventsCreatedByUser(currentUserId);
	});
}

function getAllEventsCreatedByUser(userId) {
	$.getJSON('rest/events/eventsByCreatorId?creatorId=' + userId, function(response) {
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
        			case 'PUBLISHED':
        				statusLabelClass = 'label-success';
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

                if (status == 'New' || status == 'Not Approved') {
                	$('#' + id + ' td.managerTable__CTA').append(' <button class="btn btn-success btn-xs publish">Publish</button> <button class="btn btn-primary btn-xs edit">Edit</button>');
                }
                if (status == 'Pending') {
                	$('#' + id + ' td.managerTable__CTA').append(' <button class="btn btn-primary btn-xs edit">Edit</button>');
                }

                $('#' + id + ' td.managerTable__CTA').append(' <button class="btn btn-primary btn-xs add-lecture">Add Lecture</button>');

                if (status == 'Published') {
                	$('#' + id + ' td.managerTable__CTA').append(' <button disabled class="btn btn-danger btn-xs">Delete</button>');
                }
                else {
                	$('#' + id + ' td.managerTable__CTA').append(' <button class="btn btn-danger btn-xs delete">Delete</button>');
                }
			}
		}
	}).done(function() {
		loadMore();

		$('.publish').on('click', function(e) {
			var target = e.currentTarget;
			var parent = $(target).parent();
			var grandParent = $(parent).parent();
			var eventId = $(grandParent).attr('id');
			publishEvent(eventId);
		});
		
		$('.delete').on('click', function(e) {
			var target = e.currentTarget;
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
		alert('Failed to send event for review!');
	});
}

function deleteEvent(eventId) {
	$.ajax({
		type: 'POST',
		url: 'rest/events/delete?eventId=' + eventId
	}).done(function() {
		alert("Seccessfully deleted event!")
		window.location.reload();
	}).fail(function() {
		alert("Failed to delete event!")
	})
}

function loadMore() {
    var loadMoreButton = $('#load-more-button');

    var eventsSize = $("#manage-events-table tr").size();
    var itemsToShow = 8;

    $('#manage-events-table tr:lt(' + itemsToShow + ')').show();

    if (itemsToShow < eventsSize) {
        loadMoreButton.addClass('active');
    } else {
        loadMoreButton.removeClass('active');
    }

    loadMoreButton.click(function() {
        itemsToShow = (itemsToShow + 10 <= eventsSize) ? itemsToShow + 10 : eventsSize;
        $('#manage-events-table tr:lt(' + itemsToShow + ')').show();

        if (itemsToShow == eventsSize) {
            loadMoreButton.removeClass('active');
        }
    });
}

function toTitleCase(str)
{
    return str.replace(/\w\S*/g, function(txt) {
    	return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
    });
}