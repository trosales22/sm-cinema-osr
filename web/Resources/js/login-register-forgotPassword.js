/*
 *
 * login-register modal
 * Autor: Creative Tim
 * Web-autor: creative.tim
 * Web script: http://creative-tim.com
 * 
 */
function showRegisterForm(){
    $('.loginBox').fadeOut('fast',function(){
        $('.registerBox').fadeIn('fast');
        $('.login-footer').fadeOut('fast',function(){
            $('.register-footer').fadeIn('fast');
        });
        $('.forgotPassword-footer').fadeOut('fast',function(){
            $('.register-footer').fadeIn('fast');
        });
        $('.modal-title').html('Register with');
    }); 
    
     $('.forgotPasswordBox').fadeOut('fast',function(){
        $('.registerBox').fadeIn('fast');
        $('.login-footer').fadeOut('fast',function(){
            $('.register-footer').fadeIn('fast');
        });
        $('.forgotPassword-footer').fadeOut('fast',function(){
            $('.register-footer').fadeIn('fast');
        });
        $('.modal-title').html('Register with');
    }); 
    $('.error').removeClass('alert alert-danger').html('');     
}

function showLoginForm(){
    $('#loginModal .registerBox').fadeOut('fast',function(){
        $('.loginBox').fadeIn('fast');
        $('.register-footer').fadeOut('fast',function(){
            $('.login-footer').fadeIn('fast');    
        });
        $('.forgotPassword-footer').fadeOut('fast',function(){
            $('.login-footer').fadeIn('fast');    
        });
        $('.modal-title').html('Login with');
    }); 
    
    $('#loginModal .forgotPasswordBox').fadeOut('fast',function(){
        $('.loginBox').fadeIn('fast');
        $('.register-footer').fadeOut('fast',function(){
            $('.login-footer').fadeIn('fast');    
        });
        $('.forgotPassword-footer').fadeOut('fast',function(){
            $('.login-footer').fadeIn('fast');    
        });
        $('.modal-title').html('Login with');
    });
     $('.error').removeClass('alert alert-danger').html(''); 
}

function showForgotPasswordForm(){
    $('.loginBox').fadeOut('fast',function(){
        $('.forgotPasswordBox').fadeIn('fast');
        $('.login-footer').fadeOut('fast',function(){
            $('.forgotPassword-footer').fadeIn('fast');
        });
        $('.register-footer').fadeOut('fast',function(){
            $('.forgotPassword-footer').fadeIn('fast');
        });
        $('.modal-title').html('Forgot password?');
    }); 
    $('.error').removeClass('alert alert-danger').html('');  
}

function openLoginModal(){
    showLoginForm();
    setTimeout(function(){
        $('#loginModal').modal('show');    
    }, 230);
}

function openRegisterModal(){
    showRegisterForm();
    setTimeout(function(){
        $('#loginModal').modal('show');    
    }, 230); 
}

function openForgotPasswordModal(){
    showForgotPasswordForm();
    setTimeout(function(){
        $('#loginModal').modal('show');    
    }, 230);  
}

function loginAjax(){
    $.ajax({  
        url:'${pageContext.request.contextPath}/LoginUser',  
        type:'POST',  
        dataType: 'JSON',  
        success: function(data) {  
            if(data){
                alert(data);
            }
            
            shakeLoginModal();  
        }
    });  
}

function registerAjax(){
    //Remove this comments when moving to server
    $.post( "AddUser", function( data ) {
        if(data == 1){
            window.location.href("#register_success");               
        } else {
            $('#registerModal .modal-dialog').addClass('shake');
            $('.error').addClass('alert alert-danger').html(data);
            setTimeout( function(){ 
                $('#registerModal .modal-dialog').removeClass('shake'); 
            }, 1000 ); 
        }
    });
}

function forgotPasswordAjax(){
    //Remove this comments when moving to server
    $.post( "", function( data ) {
        if(data == 1){
            //location.href("#register_success");               
        } else {
            $('#forgotPasswordModal .modal-dialog').addClass('shake');
            $('.error').addClass('alert alert-danger').html(data);
            setTimeout( function(){ 
                $('#forgotPasswordModal .modal-dialog').removeClass('shake'); 
            }, 1000 ); 
        }
    });
}

function shakeLoginModal(){
    $('#loginModal .modal-dialog').addClass('shake');
    $('.error').addClass('alert alert-danger').html("Invalid email/password combination");
    $('input[type="password"]').val('');
    setTimeout( function(){ 
        $('#loginModal .modal-dialog').removeClass('shake'); 
    }, 1000 ); 
}
   