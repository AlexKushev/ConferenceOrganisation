$(document).ready(function() {
	var changeEmailButton = $('#change-email-button'),
		changePasswordButton = $('#change-password-button');

	changeEmailButton.on('click', function() {
		changeEmail();
	});

	changePasswordButton.on('click', function() {
		changePassword();
	});

	getUserTickets();
});

function getUserTickets() {
	$.getJSON('rest/user/current', function(response) {
		if (response) {
			var currentUser = response.user;
			var id = currentUser.userId;

			$.getJSON('rest/tickets/ticketsByUserId?ownerId=' + id, function(res) {
				var ticketsData = res.ticket;

				var i, len = ticketsData.length;

				if (len === 0) {
					$('#tickets-table').text('No tickets!');
				}
				else {
					for (i = 0; i < len; i++) {
						var eventId = ticketsData[i].eventId;

						$.getJSON('rest/events/eventByEventId?eventId=' + eventId, function(resp) {
							var eventData = resp.event;

							var conferenceId = eventData.eventId,
								title = eventData.title,
								date = eventData.date.split(' ')[0];

							var ticketHtml = '<tr id="' + conferenceId + '">' +
                    			'<td class="managerTable__title">' +
                        			'<a href="#">' + title + '</a>' +
                    			'</td>' +
                    			'<td class="managerTable__date">' + date + '</td>' +
                    			'<td class="managerTable__CTA">' +
                        			'<button class="btn btn-primary btn-xs view-event">View Event</button>' +
                    			'</td>' +
                			'</tr>';

                			$('#tickets-table').append(ticketHtml);

                			$('.view-event').on('click', function(e) {
                				var target = e.currentTarget;
								var parent = $(target).parent();
								var grandParent = $(parent).parent();
								var eventId = $(grandParent).attr('id');
								sessionStorage.setItem('detailsConferenceId', eventId);
								window.location.href = 'event.html';
                			});
						});
					}
				}
			});
		}
	});
}

function changePassword() {
	$.getJSON('rest/user/current', function(response) {
		if (response) {
			var currentUser = response.user;

			var id = currentUser.userId,
				oldPassword = $('#oldPassword').val(),
				newPassword = $('#newPassword').val(),
				repeatNewPassword = $('#newPasswordRe').val();

			var userData = {
				user: {
					userId: id,
					newPassword: newPassword,
					oldPassword: oldPassword
				}
			};

			if (!validateData(newPassword, repeatNewPassword)) {
				toastr.error('Invalid new password!');
				return;
			}

			$.ajax({
				type: 'POST',
				url: 'rest/user/editPassword',
				contentType: 'application/json',
				data: JSON.stringify(userData)
			}).done(function() {
				toastr.success('Successfully changed password!');
			}).fail(function() {
				toastr.error('Failed to change password! Old password is invalid!');
			}).always(function() {
				// $('#change-password-form').submit();
			});
		}
	});
}

function changeEmail() {
	$.getJSON('rest/user/current', function(response) {
		if (response) {
			var currentUser = response.user;

			var id = currentUser.userId,
				newEmail = $('#newEmail').val(),
				password = $('#password').val();

			var userData = {
				user: {
					userId: id,
					email: newEmail,
					password: password
				}
			};

			if (!validateEmail(email)) {
				toastr.error('Invalid e-mail address!');
				return;
			}

			$.ajax({
				type: 'POST',
				url: 'rest/user/editEmail',
				contentType: 'application/json',
				data: JSON.stringify(userData)
			}).done(function() {
				toastr.success('Successfully changed e-mail!');
			}).fail(function() {
				toastr.error('Failed to change e-mail! Wrong password!');
			}).always(function() {
				// $('#change-email-form').submit();
			});
		}
	});
}

function validateLength(str, min, max) {
    return str.length >= min && str.length <= max;
}

function validateIfEmpty(str) {
    if (str.trim() === null || str.trim() === '' || str.trim() === ' ') {
        return true;
    }

    return false;
}

function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(email);
}

function validateData(newPassword, repeatNewPassword) {
	if (validateIfEmpty(newPassword) || !validateLength(newPassword, 5, 20) || validateIfEmpty(repeatNewPassword) || !validateLength(repeatNewPassword, 2, 20) || newPassword !== repeatNewPassword) {
        return false;
    }

    return true;	
}
