<%@ page import = "java.io.*,java.util.*,java.sql.*"%>
<%@ page import = "javax.servlet.http.*,javax.servlet.*" %>
<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix = "c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/sql" prefix = "sql"%>
<!DOCTYPE html>
<html>
    <!-- head -->
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="../../Resources/css/bootstrap.min.css" rel="stylesheet">
        
        <link href="../../Resources/css/font-awesome.min.css" rel="stylesheet">
        <link href="../../Resources/css/prettyPhoto.css" rel="stylesheet">
        <link href="../../Resources/css/animate.css" rel="stylesheet">
        <link href="../../Resources/css/main.css" rel="stylesheet">
        <link href="../../Resources/css/responsive.css" rel="stylesheet">
    
        <style>body{padding-top: 60px;}</style>
	
        <link href="../../Resources/bootstrap3/css/bootstrap.css" rel="stylesheet" />
 
        <link href="../../Resources/css/login-register.css" rel="stylesheet" />
        <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css">
	
        <link rel="stylesheet" href="../../Resources/Remodal/remodal.css">
        <link rel="stylesheet" href="../../Resources/Remodal/remodal-default-theme.css">
        <link href="../../Resources/css/formStyle.css" rel="stylesheet">
        <link href="../../Resources/css/responsiveTabs.css" rel="stylesheet">
    
        <style>
	    .remodal-bg.with-red-theme.remodal-is-opening,
	    .remodal-bg.with-red-theme.remodal-is-opened {
	      filter: none;
	    }
	
	    .remodal-overlay.with-red-theme {
	      background-color: #f44336;
	    }
	
	    .remodal.with-red-theme {
	      background: #fff;
	    }
        </style>
	
        <script src="../../Resources/jquery/jquery-1.10.2.js" type="text/javascript"></script>
        <script src="../../Resources/bootstrap3/js/bootstrap.js" type="text/javascript"></script>
    
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <script src="../../Resources/js/sweetalert.min.js"></script>
        <link rel="stylesheet" type="text/css" href="../../Resources/css/sweetalert.css">
        <title>My Account</title>
    </head>
    
    <!-- body -->
    <body>
        <!--header-->
        <header id="header">
            
        <!--header_top-->
	<div class="header_top">
            <div class="container">
		<div class="row">
                    <div class="col-sm-6">
			<div class="contactinfo">
			    <ul class="nav nav-pills">
				<li><a href=""><i class="fa fa-phone"></i>  (+632) 8298816</a></li>
				<li><a href=""><i class="fa fa-envelope"></i>  tristanrosales0@gmail.com</a></li>
			    </ul>
			</div>
                    </div>
                    
                    <div class="col-sm-6">
			<div class="social-icons pull-right">
                            <ul class="nav navbar-nav">
				<li><a href="#"><i class="fa fa-facebook"></i></a></li>
				<li><a href="#"><i class="fa fa-twitter"></i></a></li>
				<li><a href="#"><i class="fa fa-linkedin"></i></a></li>
				<li><a href="#"><i class="fa fa-dribbble"></i></a></li>
				<li><a href="#"><i class="fa fa-google-plus"></i></a></li>
			    </ul>
			</div>
                    </div>
		</div>
            </div>
	</div>
		
        <!--header-middle-->
	<div class="header-middle">
            <div class="container">
		<div class="row">
                    <div class="col-sm-4">
			<div class="logo pull-left">
                            <a href="adminHomePage.jsp"><img src="../../Resources/images/home/logo.jpg"/></a>
			</div>
                    </div>
                    
                    <div class="col-sm-8">
			<div class="shop-menu pull-right">
                            <ul class="nav navbar-nav">            
                                <%
                                    String user_emailAddress = null;
                                    Cookie[] cookies = request.getCookies();
                                    if(cookies !=null){
                                        for(Cookie cookie : cookies){
                                            if(cookie.getName().equals("user_emailAddress")){
                                                user_emailAddress = cookie.getValue();
                                            }
                                        }
                                    }

                                    if(user_emailAddress == null){
                                        response.sendRedirect(request.getContextPath() + "/index.jsp");
                                    }
                                %>
                                
                                <li><a href="#"><i class="fa fa-user"></i> Welcome, <%= (String)request.getSession().getAttribute("COOKIE_USER_FIRSTNAME") %>! <b>(<%= (String)request.getSession().getAttribute("COOKIE_USER_TYPE") %>)</b></a></li>
                                <li>
                                    <a id="btnLogoutAdmin" href="${pageContext.request.contextPath}/LogoutUser"><i class="fa fa-lock"></i> Logout</a>
                                    <!-- btnLogoutAdmin javascript -->
                                    <script type="text/javascript">
                                        $('#btnLogoutAdmin').click(function (event){ 
                                            event.preventDefault(); 
                                             
                                            swal(
                                                {
                                                    title: "Are you sure you want to logout?",
                                                    text: "You will not be able to undo this action!",
                                                    type: "warning",
                                                    showCancelButton: true,
                                                    confirmButtonColor: "#199ac1",
                                                    confirmButtonText: "Yes, I'm Sure!",
                                                    cancelButtonText: "No, not sure yet!",
                                                    closeOnConfirm: false
                                                },
                                                function(){
                                                    $.ajax({
                                                        url: $(this).attr('href'),
                                                        success: function(){
                                                            location.replace("${pageContext.request.contextPath}/LogoutUser");
                                                        },
                                                        error: function(jqXHR, textStatus, errorThrown){
                                                            alert('Error: ' + textStatus + ' - ' + errorThrown);
                                                        }
                                                    });
                                                });
                                        });
                                    </script>                    
                                </li>
                            </ul>
			</div>
                    </div>
		</div>
            </div>
	</div>
	
        <!--header-bottom-->
	<div class="header-bottom">
            <div class="container">
		<div class="row">
                    <div class="col-sm-9">
			<div class="navbar-header">
                            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
				<span class="sr-only">Toggle navigation</span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
				<span class="icon-bar"></span>
                            </button>
			</div>
                    </div>
		</div>
            </div>
	</div>
        
        </header>
               
        <!--tabs-->
        <div class="main">
            <input id="tab1" type="radio" name="tabs" checked>
            <label for="tab1">Profile</label>
	
            <input id="tab2" type="radio" name="tabs">
            <label for="tab2">Change Password</label>
            
            <!-- content -->
            <div class="content">
                <!--Profile-->
		<div id="content1">
                    <!-- frmUpdateAdminInfo -->
                    <form id="frmUpdateAdminInfo" method="post" action="${pageContext.request.contextPath}/UpdateAdminInformation">
                        <div class="table-responsive cart_info">
                            <table class="table table-condensed" align="center">      
                                <tr>
                                    <td><font style="font-size: 16px;"><b>Firstname:</b></font></td>
                                    <td><font style="font-size: 16px;"><b><%= (String)request.getSession().getAttribute("COOKIE_USER_FIRSTNAME") %></b></font></td>
                                    <td><input type="text" name="txtFirstname" id="txtADMIN_firstname" required="required" placeholder="Enter your firstname" maxlength="15" oncopy="return false" onpaste="return false"/></td>
                                </tr>
                                        
                                <tr>
                                    <td><font style="font-size: 16px;"><b>Lastname:</b></font></td>
                                    <td><font style="font-size: 16px;"><b><%= (String)request.getSession().getAttribute("COOKIE_USER_LASTNAME") %></b></font></td>
                                    <td><input type="text" name="txtLastname" id="txtADMIN_lastname" required="required" placeholder="Enter your lastname" maxlength="20" oncopy="return false" onpaste="return false"></td>
                                </tr>

                                <tr>
                                    <td><font style="font-size: 16px;"><b>Email address:</b></font></td>
                                    <td><font style="font-size: 16px;"><b><%= (String)request.getSession().getAttribute("COOKIE_USER_EMAILADDRESS") %></b></font></td>
                                </tr>
                                            
                                <tr>
                                    <td><font style="font-size: 16px;"><b>Name of favorite pet:</b></font></td>
                                    <td><font style="font-size: 16px;"><b><%= (String)request.getSession().getAttribute("COOKIE_USER_NAMEOFFAVORITEPET") %></b></font></td>
                                    <td><input type="text" name="txtNameOfFavoritePet" id="txtADMIN_nameOfFavoritePet" required="required" placeholder="Enter name of favorite pet" maxlength="15" oncopy="return false" onpaste="return false"></td>
                                </tr>
                                        
                                <tr style="text-align: center;">
                                    <td colspan="5">
                                        <a href="${pageContext.request.contextPath}/pages/ADMIN/adminHomePage.jsp">
                                            <button type="button" class="button">Back</button>
                                        </a>
                                            
                                        <button type="submit" class="button">Save</button>
                                    </td>
                                </tr>
                            </table>
                        </div>         
                    </form>  
                        
                    <!-- frmUpdateAdminInfo javascript -->
                    <script type="text/javascript">
                        $('#txtADMIN_firstname').keypress(function (e) {
                            var regex = new RegExp("^[a-zA-Z\s]+$");
                            var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
                            if (regex.test(str)) {
                                return true;
                            }else{
                                e.preventDefault();
                                swal("Oops! We're sorry!", "Please enter letters only!", "error");
                                return false;
                            }
                        });

                        $('#txtADMIN_lastname').keypress(function (e) {
                            var regex = new RegExp("^[a-zA-Z\s]+$");
                            var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
                            if (regex.test(str)) {
                                return true;
                            }else{
                                e.preventDefault();
                                swal("Oops! We're sorry!", "Please enter letters only!", "error");
                                return false;
                            }
                        });
                        
                        $('#txtADMIN_nameOfFavoritePet').keypress(function (e) {
                            var regex = new RegExp("^[a-zA-Z\s]+$");
                            var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
                            if (regex.test(str)) {
                                return true;
                            }else{
                                e.preventDefault();
                                swal("Oops! We're sorry!", "Please enter letters only!", "error");
                                return false;
                            }
                        });
                        
                        submitInfoAboutUpdatingAdmin();
                        
                        function submitInfoAboutUpdatingAdmin(){
                            $('#frmUpdateAdminInfo').submit(function(event) {
                                event.preventDefault();
                                var formEl = $(this);

                                $.ajax({
                                    type: 'POST',
                                    dataType : 'html',
                                    url: formEl.prop('action'),
                                    data: formEl.serialize(),
                                    //if received a response from the server
                                    success: function( data, textStatus, jqXHR) {
                                        showSuccessfullyUpdatedAdminInfoMessage();
                                    },
                                    error: function(jqXHR, textStatus, errorThrown){
                                        alert('Error: ' + textStatus + ' - ' + errorThrown);
                                    }
                                });
                            }); 
                        }
                        
                        function showSuccessfullyUpdatedAdminInfoMessage() {
                            swal(
                                {
                                    title: "Successful!",
                                    text: "You've successfully updated your information!",
                                    type: "success",
                                    showCancelButton: false,
                                    confirmButtonColor: "#199ac1",
                                    confirmButtonText: "Continue",
                                    closeOnConfirm: false 
                                },
                            function(){
                                location.replace("${pageContext.request.contextPath}/pages/ADMIN/adminMyAccount.jsp?admin_emailAddress=<%=user_emailAddress %>");
                            });
                        }
                    </script>
                </div>
	
                <!--Change Password-->
                <div id="content2">
                    <!-- frmChangeAdminPassword -->
                    <form id="frmChangeAdminPassword" method="post" action="${pageContext.request.contextPath}/ChangeAdminPassword?admin_emailAddress=<%=user_emailAddress %>">
                        <div class="table-responsive cart_info">
                            <table class="table table-condensed" align="center">
                                <tr>
                                    <td><font style="font-size: 16px;"><b>Old Password:</b></font></td>
                                    <td><input type="password" name="txtADMIN_oldPassword" id="txtADMIN_oldPassword" placeholder="Old Password" required="required" maxlength="16" oncopy="return false" onpaste="return false"></td>
                                </tr>
                                        
                                <tr>
                                    <td><font style="font-size: 16px;"><b>New Password:</b></font></td>
                                    <td><input type="password" name="txtADMIN_newPassword" id="txtADMIN_newPassword" placeholder="New Password" required="required" maxlength="16" oncopy="return false" onpaste="return false"></td>
                                </tr>
                                        
                                <tr style="text-align: center;">
                                    <td colspan="2">
                                        <a href="${pageContext.request.contextPath}/pages/ADMIN/adminHomePage.jsp">
                                            <button type="button" class="button">Back</button>
                                        </a>
                                        &nbsp;&nbsp;&nbsp;  
                                        <button type="submit" class="button">Save</button>
                                        &nbsp;&nbsp;&nbsp;
                                        <button type="reset" class="button">Reset</button>
                                    </td>
                                </tr>
                            </table>
                        </div>         
                    </form>  
                        
                    <!-- frmChangeAdminPassword javascript -->
                    <script type="text/javascript">
                        $('#txtADMIN_oldPassword').keypress(function (e) {
                            var regex = new RegExp("^[a-zA-Z0-9]+$");
                            var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
                            if (regex.test(str)) {
                                return true;
                            }else{
                                e.preventDefault();
                                swal("Oops! We're sorry!", "Please enter letters and numbers only!", "error");
                                return false;
                            }
                        });
                        
                        $('#txtADMIN_newPassword').keypress(function (e) {
                            var regex = new RegExp("^[a-zA-Z0-9]+$");
                            var str = String.fromCharCode(!e.charCode ? e.which : e.charCode);
                            if (regex.test(str)) {
                                return true;
                            }else{
                                e.preventDefault();
                                swal("Oops! We're sorry!", "Please enter letters and numbers only!", "error");
                                return false;
                            }
                        });
                        
                        showAreYouSureYouWantToChangePasswordForAdminMessage();
                        
                        function showAreYouSureYouWantToChangePasswordForAdminMessage(){
                            $('#frmChangeAdminPassword').submit(function(event) {
                                event.preventDefault();
                                var formEl = $(this);
                                
                                swal(
                                {
                                    title: "Are you sure?",
                                    text: "You will not be able to recover your old password!",
                                    type: "warning",
                                    showCancelButton: true,
                                    confirmButtonColor: "#199ac1",
                                    confirmButtonText: "Yes, I'm Sure!",
                                    cancelButtonText: "No, not sure yet!",
                                    closeOnConfirm: false 
                                },
                                function(){
                                    $.ajax({
                                        type: 'POST',
                                        dataType : 'JSON',
                                        url: formEl.prop('action'),
                                        data: formEl.serialize(),
                                        //if received a response from the server
                                        success: function( data, textStatus, jqXHR) {
                                            if(data.isChangingPasswordForAdminSuccess){
                                                showSuccessUpdatePasswordForAdminMessage();
                                            }else{
                                                showErrorUpdatePasswordForAdminMessage();
                                            }
                                        },
                                        error: function(jqXHR, textStatus, errorThrown){
                                            alert('Error: ' + textStatus + ' - ' + errorThrown);
                                        }
                                    });  
                                });
                                
                                 
                            });
                        }
                        
                        function showSuccessUpdatePasswordForAdminMessage() {
                            swal(
                                {
                                    title: "Successful!",
                                    text: "You've successfully changed your password!",
                                    type: "success",
                                    showCancelButton: false,
                                    confirmButtonColor: "#199ac1",
                                    confirmButtonText: "Continue",
                                    closeOnConfirm: false 
                                },
                            function(){
                                location.replace("${pageContext.request.contextPath}/pages/ADMIN/adminMyAccount.jsp?admin_emailAddress=<%=user_emailAddress %>");
                            });
                        }
                        
                        function showErrorUpdatePasswordForAdminMessage() {
                            swal(
                                {
                                    title: "Invalid!",
                                    text: "Old password doesn't match in the database! Please try again!",
                                    type: "error",
                                    showCancelButton: false,
                                    confirmButtonColor: "#199ac1",
                                    confirmButtonText: "Close",
                                    closeOnConfirm: true 
                                });
                        }
                    </script>
		</div>
            </div>
        </div>
    
        <!--footer-->
        <footer id="footer">
            <div class="footer-top">
                <div class="container">
                    <div class="row">
                        <div class="col-sm-2">
                            <div class="companyinfo">
                                <h2 style="color: orange;"><span style="color: #199ac1;">SM</span> Cinema</h2>
                                <h5><b>"Happy to Serve."</b></h5>
                            </div>
                        </div>
                        
                        <div class="col-sm-3">
                            <div class="address">
                                <img src="../../Resources/images/home/map.png" alt="" />
                                <p>Dr. Santos Ave. corner Carlos P. Garcia Avenue Extension, San Dionisio, Parañaque City, Philippines</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="footer-widget">
                <div class="container">
                    <div class="row">
                        <div class="col-sm-2">
                            <div class="single-widget">
                                <h2>Contact Us</h2>
                                <ul class="nav nav-pills nav-stacked">
                                    <li><a href="#">tristanrosales0@gmail.com</a></li>
                                </ul>
                            </div>
                        </div>

                        <div class="col-sm-2">
                            <div class="single-widget">
                                <h2>Policies</h2>
                                <ul class="nav nav-pills nav-stacked">
                                    <li><a href="termsAndConditions.jsp" target="_blank">Terms and Conditions</a></li>
                                </ul>
                            </div>
                        </div>	
                    </div>
                </div>
            </div>

            <div class="footer-bottom">
                <div class="container">
                    <div class="row">
                        <p class="pull-left">Copyright © 2017 SM Cinema. All rights reserved.</p>
                        <p class="pull-right">Designed by <span><a target="_blank" href="http://www.themeum.com">Themeum</a><br>Developed by: Tristan Rosales</span></p>
                    </div>
                </div>
            </div>		
        </footer>

        <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="../../Resources/Remodal/remodal.min.js"><\/script>');</script>
        <script src="../../Resources/Remodal/remodal.js"></script>

        <!-- Events -->
        <script>
          $(document).on('opening', '.remodal', function () {
            console.log('opening');
          });

          $(document).on('opened', '.remodal', function () {
            console.log('opened');
          });

          $(document).on('closing', '.remodal', function (e) {
            console.log('closing' + (e.reason ? ', reason: ' + e.reason : ''));
          });

          $(document).on('closed', '.remodal', function (e) {
            console.log('closed' + (e.reason ? ', reason: ' + e.reason : ''));
          });

          $(document).on('confirmation', '.remodal', function () {
            console.log('confirmation');
          });

          $(document).on('cancellation', '.remodal', function () {
            console.log('cancellation');
          });

        //  Usage:
        //  $(function() {
        //
        //    // In this case the initialization function returns the already created instance
        //    var inst = $('[data-remodal-id=modal]').remodal();
        //
        //    inst.open();
        //    inst.close();
        //    inst.getState();
        //    inst.destroy();
        //  });

          //  The second way to initialize:
          $('[data-remodal-id=modal2]').remodal({
            modifier: 'with-red-theme'
          });
        </script>

        <script src="../../Resources/js/jquery.js"></script>
        <script src="../../Resources/js/bootstrap.min.js"></script>
        <script src="../../Resources/js/jquery.scrollUp.min.js"></script>
        <script src="../../Resources/js/jquery.prettyPhoto.js"></script>
        <script src="../../Resources/js/main.js"></script>
    </body>
</html>
