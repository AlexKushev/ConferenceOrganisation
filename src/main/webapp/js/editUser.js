$(document).ready(function() {
	var changeEmailButton = $('#change-email-button'),
		changePasswordButton = $('#change-password-button');

	changeEmailButton.on('click', function() {
		changeEmail();
	});

	changePasswordButton.on('click', function() {
		changePassword();
	});
});

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
