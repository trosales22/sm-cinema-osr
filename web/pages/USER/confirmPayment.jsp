<%@ page language="java" contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <!-- head -->
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="../../Resources/css/responsive.css" rel="stylesheet">
        <link href="../../Resources/bootstrap3/css/bootstrap.css" rel="stylesheet" />
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
        <link rel="stylesheet" href="http://netdna.bootstrapcdn.com/font-awesome/4.0.3/css/font-awesome.css">
        <link href="../../Resources/css/formStyle.css" rel="stylesheet">
        <script src="../../Resources/js/sweetalert.min.js"></script>
        <link rel="stylesheet" type="text/css" href="../../Resources/css/sweetalert.css">
        
        <title>Confirmation | SM Cinema Online Seat Reservation</title>
    </head>
    
    <!-- body -->
    <body>
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
                                
        <center>
            <h1>Confirmation:</h1>
            <h3>Hello, <%= (String)request.getSession().getAttribute("COOKIE_USER_FIRSTNAME") %> 
                <%= (String)request.getSession().getAttribute("COOKIE_USER_LASTNAME") %>! 
                Below are your reservation and purchase details.<br>
                Please confirm it by clicking the "Confirm" button below.</h3>
            
            <!-- frmReserveTicketViaPayPal -->
            <form id="frmReserveTicketViaPayPal" method="POST" action="${pageContext.request.contextPath}/ReserveTicketViaPaypal">
                <div style="float: left; width: 50%;">
                    <!-- movieScreeningDetails -->
                    <div class="table-responsive cart_info">
                        <table class="table table-condensed" border="1" style="text-align: center; width: 90%;">
                            <thead>
                                <tr style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">
                                    <td colspan="2">Movie Screening Details</td>
                                </tr>
                            </thead>

                            <tbody style="text-align: left;">
                                <tr>
                                    <td style="color: #199ac1; font-size: 16px;"><p>Movie</p></td>
                                    <input type="hidden" name="movieName" value="<%= (String)request.getSession().getAttribute("MOVIE_NAME") %>" />
                                    <td style="font-size: 16px;"><%= (String)request.getSession().getAttribute("MOVIE_NAME") %></td>
                                </tr>

                                <tr>
                                    <td style="color: #199ac1; font-size: 16px;"><p>Branch</p></td>
                                    <input type="hidden" name="movieBranchName" value="<%= (String)request.getSession().getAttribute("MOVIE_BRANCHNAME") %>" />
                                    <td style="font-size: 16px;"><%= (String)request.getSession().getAttribute("MOVIE_BRANCHNAME") %></td>
                                </tr>

                                <tr>
                                    <td style="color: #199ac1; font-size: 16px;"><p>Auditorium</p></td>
                                    <input type="hidden" name="movieAuditoriumName" value="<%= (String)request.getSession().getAttribute("selected_movieAuditorium") %>" />
                                    <td style="font-size: 16px;"><%= (String)request.getSession().getAttribute("selected_movieAuditorium") %></td>
                                </tr>

                                <tr>
                                    <td style="color: #199ac1; font-size: 16px;"><p>Seat (s)</p></td>
                                    <input type="hidden" name="movieSelectedSeats" value="<%= (String)request.getSession().getAttribute("selectedSeats_improvedName") %>" />
                                    <td style="font-size: 16px;"><%= (String)request.getSession().getAttribute("selectedSeats_improvedName") %></td>
                                </tr>

                                <tr>
                                    <td style="color: #199ac1; font-size: 16px;"><p>Type</p></td>
                                    <input type="hidden" name="movieTypeOfSeating" value="<%= (String)request.getSession().getAttribute("MOVIE_TYPEOFSEATING") %>" />
                                    <td style="font-size: 16px;"><%= (String)request.getSession().getAttribute("MOVIE_TYPEOFSEATING") %></td>
                                </tr>

                                <tr>
                                    <td style="color: #199ac1; font-size: 16px;"><p>Screening Date</p></td>
                                    <input type="hidden" name="movieScreeningDate" value="<%= (String)request.getSession().getAttribute("selected_movieAvailableDay") %>" />
                                    <td style="font-size: 16px;"><%= (String)request.getSession().getAttribute("selected_movieAvailableDay") %></td>
                                </tr>

                                <tr>
                                    <td style="color: #199ac1; font-size: 16px;"><p>Start Time</p></td>
                                    <input type="hidden" name="movieStartTime" value="<%= (String)request.getSession().getAttribute("selected_movieSchedule") %>" />
                                    <td style="font-size: 16px;"><%= (String)request.getSession().getAttribute("selected_movieSchedule") %></td>
                                </tr>

                                <tr>
                                    <td style="color: #199ac1; font-size: 16px;"><p>Ticket Price</p></td>
                                    <input type="hidden" name="moviePrice" value="<%= (String)request.getSession().getAttribute("MOVIE_PRICE") %>" />
                                    <td style="font-size: 16px;">&#8369;<%= (String)request.getSession().getAttribute("MOVIE_PRICE") %></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                                
                <div style="float: right; width: 50%;">
                    <!-- purchaseDetails -->
                    <div class="table-responsive cart_info">
                        <table class="table table-condensed" border="1" style="text-align: center; width: 95%;">
                            <thead>
                                <tr style="background-color: #199ac1; color: white; font-size: 16px; text-align: center;">
                                    <td colspan="4">Purchase Details</td>
                                </tr>
                            </thead>

                            <tbody style="text-align: left;">
                                <tr>
                                    <td style="color: #199ac1; font-size: 16px;"><p>Seat Quantity</p></td>
                                    <input type="hidden" name="movieSeatQuantity" value="<%= (String)request.getSession().getAttribute("selected_seatQuantity") %>" />
                                    <td colspan="3" style="font-size: 16px;"><%= (String)request.getSession().getAttribute("selected_seatQuantity") %></td>
                                </tr>

                                <tr>
                                    <td style="font-size: 16px;" colspan="4"><p></p></td>
                                </tr>

                                <tr style="text-align: center;">
                                    <td></td>
                                    <td style="color: #199ac1; font-size: 16px;"><p>Qty</p></td>
                                    <td style="color: #199ac1; font-size: 16px;"><p>Price</p></td>
                                    <td style="color: #199ac1; font-size: 16px;"><p>Total</p></td>
                                </tr>

                                <tr>
                                    <td style="color: #199ac1; font-size: 16px;"><p>Ticket</p></td>
                                    <td style="font-size: 16px; text-align: center;"><p id="txtQuantity"><%= (String)request.getSession().getAttribute("selected_seatQuantity") %></p></td>
                                    <td style="font-size: 16px;"><p id="txtPrice">&#8369;<%= (String)request.getSession().getAttribute("MOVIE_PRICE") %></p></td>
                                    <td style="font-size: 16px;"><p id="txtTotalTicketPrice"></p></td>
                                </tr>

                                <tr>
                                    <td style="color: #199ac1; font-size: 16px;"><p>Convenience Fee</p></td>
                                    <td style="font-size: 16px; text-align: center;"><p><%= (String)request.getSession().getAttribute("selected_seatQuantity") %></p></td>
                                    <td style="font-size: 16px;"><p>&#8369;20.00</p></td>
                                    <td style="font-size: 16px;"><p id="txtTotalConvenienceFee"></p></td>
                                </tr>

                                <tr>
                                    <td style="color: #199ac1; font-size: 16px;"><p>Grand Total</p></td>
                                    <td colspan="2" style="font-size: 16px;"><p></p></td>
                                    <td style="font-size: 16px;"><p id="txtGrandTotal_total"></p></td>
                                </tr>
                                
                                <tr style="text-align: center;">
                                    <td colspan="4"><button type="submit" class="button">Confirm</button></td>
                                </tr>
                            </tbody>

                            <script type="text/javascript">
                                $(document).ready(function() {
                                    var moviePrice,movieQuantity,totalTicketPrice,totalConvenienceFee,
                                    grandTotal_total;
                                    moviePrice = <%= (String)request.getSession().getAttribute("MOVIE_PRICE") %>;
                                    movieQuantity = <%= (String)request.getSession().getAttribute("selected_seatQuantity") %>;

                                    totalTicketPrice = (moviePrice * movieQuantity);
                                    $('#txtTotalTicketPrice').append("&#8369;" + totalTicketPrice + ".00");

                                    totalConvenienceFee = (movieQuantity * 20);
                                    $('#txtTotalConvenienceFee').append("&#8369;" + totalConvenienceFee + ".00");

                                    grandTotal_total = (totalTicketPrice + totalConvenienceFee);
                                    $('#txtGrandTotal_total').append("&#8369;" + grandTotal_total + ".00");
                                });
                            </script>
                        </table>
                    </div>
                </div>     
            </form>
                            
            <!-- frmReserveTicketViaPayPal javascript -->
            <script type="text/javascript">
                $('#frmReserveTicketViaPayPal').submit(function(event) {
                    event.preventDefault();
                    var formEl = $(this);
                    
                    $.ajax({
                        type: 'POST',
                        dataType : 'JSON',
                        url: formEl.prop('action'),
                        data: formEl.serialize(),
                        success: function( data, textStatus, jqXHR) {
                            if(data.status == "failedBecauseOfRecordOfCancellation"){
                                showFailedBecauseOfRecordOfCancellation();
                            }else if(data.status == "successToReserved"){
                                successfullyReserveTicketViaOnSitePaymentMessage();
                            }
                        },
                        error: function(jqXHR, textStatus, errorThrown){
                            alert('Error: ' + textStatus + ' - ' + errorThrown);
                        }
                    });
                });

                function showFailedBecauseOfRecordOfCancellation() {
                    swal(
                        {
                            title: "Invalid!",
                            text: "You had a previous record that you cancel a reservation. \n\
                                It means that you are not be able to reserve a ticket again. Thankyou!",
                            type: "error",
                            showCancelButton: false,
                            confirmButtonColor: "#199ac1",
                            confirmButtonText: "Close",
                            closeOnConfirm: true
                        }
                    );
                }

                function successfullyReserveTicketViaOnSitePaymentMessage() {
                    swal(
                        {
                            title: "Successful!",
                            text: "You have successfully reserved your ticket.\n\
                            Let's go print it now! Go to My Account->Reservations History->View",
                            type: "success",
                            showCancelButton: false,
                            confirmButtonColor: "#199ac1",
                            confirmButtonText: "Continue",
                            closeOnConfirm: false 
                        },
                    function(){
                        location.replace("${pageContext.request.contextPath}/pages/USER/myAccount.jsp?user_emailAddress=<%=user_emailAddress %>");
                    });
                }
            </script>           
        </center>
    </body>
</html>
