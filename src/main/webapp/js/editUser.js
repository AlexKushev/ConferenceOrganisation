$(document).ready(function() {
	var changeEmailButton = $('#change-email-button'),
		changePasswordButton = $('#change-password-button');

	changeEmailButton.on('click', function() {
		changeEmail();
	});
});

function changeEmail() {
	$.getJSON('rest/user/current', function(response) {
		console.log('here');
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

			if (!validateEmail(newEmail)) {
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
				toastr.error('Failed to change e-mail!');
			}).always(function() {
				// $('#change-email-form').submit();
			});
		}
	});
}

function validateEmail(email) {
        var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
        return re.test(email);
}
