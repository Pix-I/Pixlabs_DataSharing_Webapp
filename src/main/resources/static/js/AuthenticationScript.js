/**
 * Created by pix-i on 17/01/2017.
 */


function validate() {
    if (document.loginForm.username.value == "" && document.loginForm.password.value == "") {
        //alert(/*[[#{message.username} + #{message.password}]]*/);
        document.loginForm.username.focus();
        return false;
    }
    if (document.loginForm.username.value == "") {
        //   alert(/*[[#{message.username}]]*/);
        document.loginForm.username.focus();
        return false;
    }
    if (document.loginForm.password.value == "") {
        //    alert(/*[[#{message.password}]]*/);
        document.loginForm.password.focus();
        return false;
    }
    return true;
}

$(document).ready(function () {
    $('#passwordRecoveryForm').submit(function (event) {
        recoverPassword();
    });

    //Sign up
    $('#signupForm').submit(function (event) {
       console.log("Form submitted.");
       validateSU(event);
   });

   //Recovery
   $('#recoveryForm').submit(function (event) {
       console.log("Recovering account.");
       recovery(event);
   });

   //Login
   $('#loginForm').submit(function (event) {
       console.log("Logging in");
       login(event);
   });
});



function recovery(event) {
    event.preventDefault();
    var formData = $('#recoveryForm').serialize();
    $.post("/auth/recovery",formData,function (data) {
        console.log("Recovering via email");
        if(data.message == "success"){
            document.getElementById("recoveryConfirmation").style.visibility = "visible";
            document.getElementById("recoveryError").style.visibility = "hidden";
        } else {
            document.getElementById("recoveryConfirmation").style.visibility = "hidden";
            document.getElementById("recoveryError").style.visibility = "visible";
        }
    })
        .fail(function (data) {
            document.getElementById("recoveryError").innerHTML = "Something didn't go well, try again later.";
            document.getElementById("recoveryConfirmation").style.visibility = "hidden";
            document.getElementById("recoveryError").style.visibility = "visible";
        })
    ;

}


function login(event) {
    event.preventDefault();
    var registerErrors = document.getElementsByClassName("auth-error");
    for (i = 0; i < registerErrors.length; i++) {
        registerErrors[i].style.visibility = "hidden";
    }
    if(validate()) {
        var formData = $('#loginForm').serialize();
        $.post("/auth/login", formData, function (data) {
            if (data.message == "success") {
                //Update view?
                window.location.href = "http://localhost:8080/";
                 } else if (data.error == "invalid") {
                document.getElementById("loginError").style.visibility = 'visible';
            } else {

            }
        });
    }
}


function updateInfo(event) {
    event.preventDefault();
    console.log("Updating info");
    var formData = $('#updateInfoForm').serialize();
    $.post("/updateInfo",formData,function (data) {
        if(data.message == "success"){
            window.location.href = "http://localhost:8080/preferences";

        } else {
            console.log("error!");
        }
    })
        .fail(function () {
            console.log("fail!");
        });
}



function validateSU(event) {
    event.preventDefault();
    var registerErrors = document.getElementsByClassName("auth-error");
    for(i =0;i<registerErrors.length;i++){
        registerErrors[i].style.visibility = "hidden";
    }


    if($("#passwordSignUp").val() != $("#confirmPassword").val()){
        document.getElementById("confirmPasswordError").style.visibility = "visible";
        document.signupForm.password.focus();
        return;
    }

    if($("#passwordSignUp").val().length<6){
        document.getElementById("passwordSignUpError").style.visibility = "visible";
        document.signupForm.password.focus();
        return;
    }
    console.log("Password should be ok");
    var formData = $("#signupForm").serialize();
    console.log("Form serialised");
    document.getElementById("creatingAccountSpan").style.visibility = "visible";
    $.post("/auth/register",formData,function (data) {
        document.getElementById("creatingAccountSpan").style.visibility = "hidden";
        if(data.message == "success"){
           console.log("Success");
            document.getElementById("overlay-auth").style.visibility = "hidden";
            document.getElementById("overlay-regsuccess").style.visibility = "visible";
        }
        else if(data.error == "UsernameTaken"){
            document.signupForm.username.focus();
            document.getElementById("usernameSignUpError").style.visibility = "visible";

        } else if(data.error == "EmailTaken"){
            document.signupForm.email.focus();
            document.getElementById("emailRegistrationError").style.visibility = "visible";
        }
        else {
            document.signupForm.email.focus();
            document.getElementById("regFailed").style.visibility = "visible";
        }
    }).fail(function (data) {
        document.getElementById("regFailed").style.visibility = "visible";

    });

}


function confirmRegistration(event) {
    $.post("/auth/tokenConfirm", {token: $("emailConfirmation").val()}, function (data) {
        console.log(data);
    });
}





function openNav() {
    if(document.getElementById("myNav").style.width != "100%")
        document.getElementById("myNav").style.width = "100%";
    else
        document.getElementById("myNav").style.width = "0%";

}

function closeNav() {
    document.getElementById("myNav").style.width = "0%";
}

