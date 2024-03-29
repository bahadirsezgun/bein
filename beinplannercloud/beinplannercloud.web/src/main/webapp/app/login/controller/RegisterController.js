ptBossLoginApp.controller('RegisterController', function($scope,$translate,$http,$location) {
	
	$scope.firmFinded=false;
	
	$scope.version;
	$scope.logosRight=new Array();
	$scope.logosLeft=new Array();
	
	
	
	
	$scope.defFirm=new Object();
	$scope.defFirm.firmName="";
	$scope.defFirm.firmAuthPerson="";
	$scope.defFirm.firmPhone="";
	$scope.defFirm.firmEmail="";
	$scope.defFirm.firmCityName="";
	$scope.defFirm.firmStateName="";
	$scope.defFirm.firmAddress="";
	$scope.defFirm.firmRestriction="0";
	
	$scope.maskGsm="(999) 999-9999";
	
	
		
	
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
	   
	
	
	$scope.init=function(){
		 var userLang = navigator.language || navigator.userLanguage; 
		 
		   
	     if((userLang).substring(0,2)=="tr"){
	    	 $translate.use("tr");
	     }else{
	    	 $translate.use("en"); 
	     }
		 
		 
	};
	
    $scope.dataAmount=0;
	
	
	
    function controlAttributes(){
    	if($scope.defFirm.firmName==""){
    		toastr.error($translate.instant('addNewFirmPH'));
    		return false;
    	}
    	
    	
    	if($scope.defFirm.firmAuthPerson==""){
    		toastr.error($translate.instant('enterFirmAuthPersonPH'));
    		return false;
    	}
    	
    	if($scope.defFirm.firmPhone==""){
    		toastr.error($translate.instant('enterFirmPhonePH'));
    		return false;
    	}
    	
    	if($scope.defFirm.firmEmail==""){
    		toastr.error($translate.instant('enterFirmEMailPH'));
    		return false;
    	}
    	
    	if($scope.defFirm.firmCityName==""){
    		toastr.error($translate.instant('enterCityNamePH'));
    		return false;
    	}
    	
    	if($scope.defFirm.firmStateName==""){
    		toastr.error($translate.instant('enterStateNamePH'));
    		return false;
    	}
    	
    	if($scope.defFirm.firmAddress==""){
    		toastr.error($translate.instant('enterFirmAddressPH'));
    		return false;
    	}
    	
    	if($scope.defFirm.firmRestriction=="0"){
    		toastr.error($translate.instant('enterFirmRestrictionPH'));
    		return false;
    	}
    	
    	return true;
    	
    }	
 
   
    $scope.find=function(){
    	$http({
			  method:'POST',
			  url: "/register/find",
			  data: angular.toJson($scope.defFirm),
			}).then(function successCallback(response) {
				if(response.data.resultStatu=="success"){
					$scope.defFirm=response.data.resultObj;
					$scope.firmFinded=true;
				}else{
					toastr.error($translate.instant('firmNotFound'));
				}
				
				
			});
    }
    
	$scope.register=function(){
		if(controlAttributes()){
		$http({
			  method:'POST',
			  url: "/register/create",
			  data: angular.toJson($scope.defFirm),
			}).then(function successCallback(res) {
				if(res.data.resultStatu=="success"){
					toastr.success($translate.instant(res.data.resultMessage));
					setTimeout(function(){
						$(location).attr("href","https://api.beinplanner.com");
					},2000);
					
				}else{
					$scope.resultMessage=res.data.resultMessage;
					
					toastr.error($translate.instant(res.data.resultMessage));
				}
			}, function errorCallback(response) {
				toastr.error("ERROR!!");
			});
		}
	};
	
	
	
	
	function readMyXMLForBrand(xmlName){
		$.ajax({
		type:'POST', 
		crossDomain: true,
		jsonpCallback: 'jsonCallback',
	    url: xmlName, // name of file you want to parse
	    dataType: 'jsonp',
	    success: function(json) {
	    	
	    	getBrands(json);
	    	
	    	
	     },
	     error: function(e) {
	        
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