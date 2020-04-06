$(document).ready(function(){    
    var counter = 2;

    $("#addScheduleBtn").click(function () {

	if(counter>5){
            alert("Only 5 schedules allowed!");
            return false;
	}

	var newSchedule = $(document.createElement('div'))
	     .attr("id", 'Schedule' + counter);

	newSchedule.after().html('<label style="color: white;">Schedule #'+ counter + ' : </label>' +
	      '<input type="time" name="schedule" id="schedule' + counter + '" value="">');

	newSchedule.appendTo("#CinemaGroup");
        
	counter++;
     });

     $("#removeScheduleBtn").click(function () {
	if(counter==1){
          alert("No more schedule to remove!");
          return false;
       }

	counter--;

        $("#Schedule" + counter).remove();

     });

     $("#showAllScheduleBtn").click(function () {
	var msg = '';
        var time,hour,ms,suffix,schedules;
       
	for(i=1; i<counter; i++){
            time = $('#schedule' + i).val();
            hour = time.substr(0,2);
            ms = time.substr(2);
            
            suffix = hour >= 12 ? 'PM' : 'AM';
            
            schedules = (hour%12+12*(hour%12==0)) + ms + suffix + ","; 
            //msg += "\n Schedule #" + i + " : " + $('#schedule' + i).val();
            //msg += "\n Schedule #" + i + " : " + schedules;  
            msg += "" + schedules;
            
            $('#txtMovieSchedules').val(msg);
            
            if(schedules === undefined){
                $('#txtMovieSchedules').val('#txtMovieSchedules').val().replace('undefined','');
            }else{
                $('#txtMovieSchedules').val(msg);
            }
            
	}

        //alert(msg);  
     });
  });