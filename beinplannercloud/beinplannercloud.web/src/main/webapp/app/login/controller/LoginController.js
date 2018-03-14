ptBossLoginApp.controller('LoginController', function($scope,$translate,$http) {
	
	
	$scope.version;
	$scope.logosRight=new Array();
	$scope.logosLeft=new Array();
	
	
	var user=new Object();
	user.userName=localStorage.getItem('username');
 
	if(user.userName==null){
		user.userName="";
	}
	
	user.password="";
	
	
	   toastr.options = {
            "debug": false,
            "newestOnTop": false,
            "positionClass": "toast-top-center",
            "closeButton": true,
            "toastClass": "animated fadeInDown",
        };
	
	
	var days = ["sunday","monday","tuesday","wednesday","thursday","friday","saturday"];
	var monthNames = ["january", "february", "march", "april", "may", "june","july", "august", "september", "october", "november", "december"];
	
	var d=new Date();
	$scope.time=d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();
	$scope.date=$translate.instant(days[d.getDay()])+", "+$translate.instant(monthNames[d.getMonth()+1])+" "+(d.getMonth()+1)+","+d.getFullYear();
	//Friday, February 27, 2015
	   
	$scope.user=user;
	
	$scope.userName_PlaceHolder="enterUserName";
	$scope.password_PlaceHolder="enterPassword";
	
	$scope.init=function(){
		readMyXMLForBrand("https://s3-us-west-2.amazonaws.com/beinplanner/marketBein.json");
		//findGlobals();
	};
	
    	
    	$scope.loginKey=function(keyEvent){
    		if (keyEvent.which === 13){
    			$scope.login();
    		}
    	}
    	
   
	$scope.login=function(){
		
		var frmDatum={"username":$scope.user.userName,"password":$scope.user.password};
		
		$.ajax({
			  type:'POST',
			  url: "/login",
			  contentType: "application/json; charset=utf-8",				    
			  data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res.resultMessage=="LOGIN_SUCCESS"){
					//$(location).attr("href","./bein/#dashboard?"+$scope.version);
					//localStorage.setItem('username', user.userName);

					
					
				}else{
					$scope.resultMessage=res.resultMessage;
					toastr.error($translate.instant(res.resultMessage));
				}
			}).fail  (function(jqXHR, textStatus, errorThrown) 
					{ 
				  //if(jqXHR.status == 404 || textStatus == 'error')
					  //alert(textStatus);
					  
					  //$(location).attr("href","/beincloud/lock.html");
			});
	};
	
	
	$scope.reset=function(){
		
		if($scope.user.userName==""){
			toastr.error($translate.instant("noUserEntered"));
			return;
		}
		
		$.ajax({
			  type:'POST',
			  url: "./pt/ptusers/resetPassword/"+$scope.user.userName,
			  contentType: "application/json; charset=utf-8",				    
			 // data: JSON.stringify(frmDatum),
			  dataType: 'json', 
			  cache:false
			}).done(function(res) {
				if(res.resultStatu==1){
					toastr.success($translate.instant(res.resultMessage));
				}else{
					 toastr.error($translate.instant(res.resultMessage));  
				  }
			});
		
	}
	
	
	function readMyXMLForBrand(xmlName){
		$.ajax({
		type:'POST', 
		crossDomain: true,
		jsonpCallback: 'jsonCallback',
	    url: xmlName, // name of file you want to parse
	    dataType: 'jsonp',
	    success: function(json) {
	    	$(json.market).each(function(i,data){
	  		  var xmlPageId=data.pageId;
	  		  if(xmlPageId==1){
	  			   var version=data.title;
	  			   $scope.version=version;
	  			   $scope.$apply();
	  			   /*
	               if(version!=null){
	            	   sessionStorage.removeItem('updateObj');
	            	   var frmDatum = {"version":version}; 
	            	   $.ajax({
	     				  type:'POST',
	     				  url: "./pt/update/controlUpdate",
	     				  contentType: "application/json; charset=utf-8",
	     				  data: JSON.stringify(frmDatum),
		     			  dataType: 'json', 
	     				  cache:false
	     				}).done(function(res) {
	     					if(res.updVer!=version){
	     						updateInformation(json,version);
	     					}
	     					getBrands(json);
	     				});
	            	}
	            	*/
	  			}
	  		});
	     },
	     error: function(e) {
	        console.log(e.message);
	     }
	  });
 	}
	
	function getBrands(json){
		
		$(json.logosLeft).each(function(i,data){
			var logo=new Object();
			logo.src=data.src;
			logo.url=data.url;
			logo.title=data.title;
			$scope.logosLeft.push(logo);
			
		});
		
		$(json.logosRight).each(function(i,data){
			var logo=new Object();
			logo.src=data.src;
			logo.url=data.url;
			logo.title=data.title;
			
			
			$scope.logosRight.push(logo);
			
		});
		$scope.$apply();
	}
	
	
	
});