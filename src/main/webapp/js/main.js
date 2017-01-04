 // $(function() {
 //     $("body").load("../accountform.html");
 // });

 $(document).ready(function() {

    $('#logged-in-header').hide();
    $('.profileMenu__btn--logout').hide();
    $('#account').attr("data-toggle", "modal");
    $('#account').attr("data-target", "#login-modal");
    $('#account').text('Account');

    authUser();

    var registerButton = $('#register-button'),
        loginButton = $('#login-button');

    registerButton.on('click', function() {
        register();
    });

    loginButton.on('click', function() {
        login();
    });

     $(window).scroll(function() {
         if ($(this).scrollTop() > 350) {
             $('.scrollTop').show();
         } else {
             $('.scrollTop').hide();
         }
     });

     $('.scrollTop').click(function() {
         $("html, body").animate({
             scrollTop: 0
         }, 600);
         return false;
     });

 });

 function login() {
    var userEmail = $('#login_email').val(),
        userPassword = $('#login_password').val();


    var userData = {
        user: {
            email: userEmail,
            password: userPassword
        }
    };

    $.ajax({
        type: 'POST',
        url: 'rest/user/login',
        contentType: 'application/json',
        data: JSON.stringify(userData)
    }).done(function() {
        // destroy modal login 
        $('#login-modal').modal('toggle');

        // set current user
        authUser();

        alert("Successfully logged in!");

    }).fail(function() {
        alert("Wrong e-mail or password!");
    }).always(function() {
            
    });
 }

 function register() {
    var userEmail = $('#register_email').val(),
        userPassword = $('#register_password').val(),
        userFirstName = $('#register_firstName').val(),
        userLastName = $('#register_lastName').val();

    var userData = {
        user: {
            email: userEmail,
            password: userPassword,
            firstName: userFirstName,
            lastName: userLastName
        }
    };

    if (!validateUserData(userEmail, userPassword, userFirstName, userLastName)) {
        alert('Invalid data!');
        return;
    }

    $.ajax({
        type: 'POST',
        url: 'rest/user/register',
        contentType: 'application/json',
        data: JSON.stringify(userData)
    }).done(function() {
        alert('Successfully registered!');
    }).fail(function() {
        alert('Invalid data or user with this e-mail already exists!');
    }).always(function() {
        // submit
    });
 }

 function validateUserData(userEmail, userPassword, userFirstName, userLastName) {
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

    if (!validateEmail(userEmail) || validateIfEmpty(userPassword) || !validateLength(userPassword, 5, 20) || validateIfEmpty(userFirstName) || !validateLength(userFirstName, 2, 20) || validateIfEmpty(userLastName) || !validateLength(userLastName, 2, 20)) {
        return false;
    }

    return true;
 }

 function authUser() {
    $.getJSON('rest/user/current', function(response) {
        if (!response) {
            $('.profileMenu__btn--logout').hide();
            return;
        }
        else {
            var currentUser = response.user;

            console.log(currentUser);

            $('#filter-line-tabs').append('<a href="addevent.html">Add New</a>');
            $('#not-logged-in-header').hide();
            $('#logged-in-header').show();
            $('.profileMenu__btn--logout').show();
            $('#account').removeAttr("data-toggle");
            $('#account').removeAttr("data-target");
            $('#account').text(currentUser.firstName + ' ' + currentUser.lastName);
        }       
    }).done(function() {
        console.log("done");
    }).fail(function() {
        console.log("failed");
    });
 }

 // Mobile Menu Toggle
 $(document).on('click', '.mobileMenu__btn', function() {
     var menu = $('.mobileMenu__items');
     menu.toggleClass('active');
     $(this).toggleClass('active');
 });

 /* #####################################################################
    #
    #   Project       : Modal Login with jQuery Effects
    #   Author        : Rodrigo Amarante (rodrigockamarante)
    #   Version       : 1.0
    #   Created       : 07/29/2015
    #   Last Change   : 08/04/2015
    #
    ##################################################################### */

 $(function() {

     var $formLogin = $('#login-form');
     var $formLost = $('#lost-form');
     var $formRegister = $('#register-form');
     var $divForms = $('#div-forms');
     var $modalAnimateTime = 300;
     var $msgAnimateTime = 150;
     var $msgShowTime = 2000;

     $("form").submit(function() {
         switch (this.id) {
             case "login-form":
                 var $lg_email = $('#login_email').val();
                 var $lg_password = $('#login_password').val();
                 if ($lg_email == "ERROR") {
                     msgChange($('#div-login-msg'), $('#icon-login-msg'), $('#text-login-msg'), "error", "glyphicon-remove", "Login error");
                 } else {
                     msgChange($('#div-login-msg'), $('#icon-login-msg'), $('#text-login-msg'), "success", "glyphicon-ok", "Login OK");
                 }
                 return false;
             case "lost-form":
                 var $ls_email = $('#lost_email').val();
                 if ($ls_email == "ERROR") {
                     msgChange($('#div-lost-msg'), $('#icon-lost-msg'), $('#text-lost-msg'), "error", "glyphicon-remove", "Send error");
                 } else {
                     msgChange($('#div-lost-msg'), $('#icon-lost-msg'), $('#text-lost-msg'), "success", "glyphicon-ok", "Send OK");
                 }
                 return false;
             case "register-form":
                 var $rg_email = $('#register_email').val();
                 var $rg_password = $('#register_password').val();
                 if ($rg_email == "ERROR") {
                     msgChange($('#div-register-msg'), $('#icon-register-msg'), $('#text-register-msg'), "error", "glyphicon-remove", "Register error");
                 } else {
                     msgChange($('#div-register-msg'), $('#icon-register-msg'), $('#text-register-msg'), "success", "glyphicon-ok", "Register OK");
                 }
                 return false;
             default:
                 return false;
         }
         return false;
     });

     $('#login_register_btn').click(function() { modalAnimate($formLogin, $formRegister); });
     $('#register_login_btn').click(function() { modalAnimate($formRegister, $formLogin); });
     $('#login_lost_btn').click(function() { modalAnimate($formLogin, $formLost); });
     $('#lost_login_btn').click(function() { modalAnimate($formLost, $formLogin); });
     $('#lost_register_btn').click(function() { modalAnimate($formLost, $formRegister); });
     $('#register_lost_btn').click(function() { modalAnimate($formRegister, $formLost); });

     function modalAnimate($oldForm, $newForm) {
         var $oldH = $oldForm.height();
         var $newH = $newForm.height();
         $divForms.css("height", $oldH);
         $oldForm.fadeToggle($modalAnimateTime, function() {
             $divForms.animate({ height: $newH }, $modalAnimateTime, function() {
                 $newForm.fadeToggle($modalAnimateTime);
             });
         });
     }

     function msgFade($msgId, $msgText) {
         $msgId.fadeOut($msgAnimateTime, function() {
             $(this).text($msgText).fadeIn($msgAnimateTime);
         });
     }

     function msgChange($divTag, $iconTag, $textTag, $divClass, $iconClass, $msgText) {
         var $msgOld = $divTag.text();
         msgFade($textTag, $msgText);
         $divTag.addClass($divClass);
         $iconTag.removeClass("glyphicon-chevron-right");
         $iconTag.addClass($iconClass + " " + $divClass);
         setTimeout(function() {
             msgFade($textTag, $msgOld);
             $divTag.removeClass($divClass);
             $iconTag.addClass("glyphicon-chevron-right");
             $iconTag.removeClass($iconClass + " " + $divClass);
         }, $msgShowTime);
     }
 });
