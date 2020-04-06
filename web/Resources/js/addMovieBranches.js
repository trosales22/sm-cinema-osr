var $ = function(id) {
    return document.getElementById(id);
};

var mySelectedMovieBranches = [];
                              
function addMovieBranches()
{    
     var movieBranches = $('txtMovieBranches').value;
     var selected_movieBranches = $('selected_movieBranches');    

     selected_movieBranches.innerHTML = "";
     mySelectedMovieBranches.push(movieBranches);
     selected_movieBranches.innerHTML += mySelectedMovieBranches.join(","); 
        
};

(function () {
    $("addMovieBranchesBtn").onclick = addMovieBranches;
    
})();