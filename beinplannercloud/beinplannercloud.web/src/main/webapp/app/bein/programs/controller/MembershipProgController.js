ptBossApp.controller('MembershipProgController', function($rootScope,$scope,$translate,$http,parameterService,$location,homerService,commonService,globals) {

	$scope.membershipRestriction=$rootScope.membershipRestriction;
	
	$scope.noProgram=true;
	$scope.willProgramCreate=false;
	$scope.programMemberships;
	$scope.programInstructors;
	
	
	
	
	
	$scope.programMembership=new Object();
	$scope.programMembership.progUserId="0";
	$scope.programMembership.progStatus="1";
	$scope.programMembership.progDurationType="2";
	$scope.programMembership.progPrice=0;
	$scope.programMembership.freezeDurationType="0";
	$scope.programMembership.progComment="";
	$scope.programMembership.progDescription="";
	
	$scope.programMembership.freezeDuration=0;
	$scope.programMembership.maxFreezeCount=0;
	$scope.programMembership.progRestriction="0";
	$scope.programMembership.progBeforeDuration=0;
	$scope.programMembership.progAfterDuration=0;
	$scope.programMembership.type="pm";
	
	$scope.monday="";
    $scope.tuesday="";
    $scope.wednesday="";
    $scope.thursday="";
    $scope.friday="";
    $scope.saturday="";
    $scope.sunday="";
    
    $scope.progRestrictionMonday="-1";
	$scope.progRestrictionTuesday="-1";
	$scope.progRestrictionWednesday="-1";
	$scope.progRestrictionThursday="-1";
	$scope.progRestrictionFriday="-1";
	$scope.progRestrictionSaturday="-1";
	$scope.progRestrictionSunday="-1";
	
	
	
    
    toastr.options = {
        "debug": false,
        "newestOnTop": false,
        "positionClass": "toast-top-center",
        "closeButton": true,
        "toastClass": "animated fadeInDown",
    };

    $scope.init = function(){
    	$('.clockpicker').clockpicker({autoclose: true, placement: 'left',align: 'top'});
    	$("[data-toggle=popover]").popover();
    	
    	
    	commonService.pageName=$translate.instant("definition_membershipprog");
		commonService.pageComment=$translate.instant("membershipProgDefinitionComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		if(commonService.ptGlobal!=null){
			$scope.dateFormat=commonService.ptGlobal.ptDateFormat;
			$scope.dateTimeFormat=commonService.ptGlobal.ptDateTimeFormat;
		}else{
			findGlobals();
		}
		findMembershipPrograms();
		findInstructors();
    };
    
   
    function findGlobals(){
    		$.ajax({
	  		  type:'POST',
	  		  url: "/bein/global/getGlobals",
	  		  contentType: "application/json; charset=utf-8",				    
	  		  dataType: 'json', 
	  		  cache:false
	  		}).done(function(result) {
	  			var res=result.resultObj;
	  			if(res!=null){
	  				$scope.ptTz=res.ptTz;
	  				$scope.ptCurrency=res.ptCurrency;
	  				$scope.ptStaticIp=res.ptStaticIp;
	  				$scope.ptLang=(res.ptLang).substring(0,2);
	  				$scope.ptDateFormat=res.ptScrDateFormat;
	  				
	  				$translate.use($scope.ptLang);
					$translate.refresh;
					commonService.setPtGlobal(res);
					$scope.$apply();
					$scope.dateFormat=commonService.ptGlobal.ptDateFormat;
					$scope.dateTimeFormat=commonService.ptGlobal.ptDateTimeFormat;
	  			}
	  			
	  		});
	}
    
    
    function findInstructors(){
		 
	    $http({
		  method: 'POST',
		  url: "/bein/staff/findAllSchedulerStaff"
		}).then(function successCallback(response) {
			$scope.programInstructors=response.data.resultObj;
		}, function errorCallback(response) {
		    // called asynchronously if an error occurs
		    // or server returns response with an error status.
		});
    }
    
    function findMembershipPrograms(){
		$http({
			  method: 'POST',
			  url: "/bein/program/findMembershipPrograms"
			}).then(function successCallback(response) {
				$scope.programMemberships=response.data.resultObj;
				if($scope.programMemberships.length!=0){
					$scope.noProgram=false;
				}else{
					$scope.noProgram=true;
				}
				
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
		
	}
   
	
	
	
	$scope.addNewMembershipProgram =function(){
		$scope.programMembership=new Object();
		
		$scope.programMembership.progUserId="0";
		$scope.programMembership.progStatus="1";
		$scope.programMembership.progDurationType="2";
		$scope.programMembership.progPrice=0;
		$scope.programMembership.freezeDurationType="0";
		$scope.programMembership.type="pm";
		
		$scope.programMembership.freezeDuration=0;
		$scope.programMembership.maxFreezeCount=0;
		$scope.programMembership.progRestriction="0";
		$scope.programMembership.progBeforeDuration=0;
		$scope.programMembership.progAfterDuration=0;
		$scope.programMembership.progComment="";
		$scope.programMembership.progDescription="";
		
		$scope.willProgramCreate=true;
		
	};
	
	
	$scope.showProgram =function(progId){
		
		
		$http({
			  method: 'POST',
			  url: "/bein/program/findMembershipProgramById/"+progId
			}).then(function successCallback(response) {
					
				     $scope.programMembership=response.data.resultObj;
					
					$scope.programMembership.progUserId=""+$scope.programMembership.progUserId;
					$scope.programMembership.progStatus=""+$scope.programMembership.progStatus;
					$scope.programMembership.progDurationType=""+$scope.programMembership.progDurationType;
					$scope.programMembership.freezeDurationType=""+$scope.programMembership.freezeDurationType;
					$scope.programMembership.progRestriction=""+$scope.programMembership.progRestriction;
					$scope.programMembership.type="pm";
					
					$scope.willProgramCreate=true;
					
					var programMembershipDetails=$scope.programMembership.programMembershipDetails;
					$.each(programMembershipDetails,function(i,data){
						
						if(data.progRestrictedDay==1){
							$scope.progRestrictionMonday=""+data.progRestrictedTime;
						}else if(data.progRestrictedDay==2){
							$scope.progRestrictionTuesday=""+data.progRestrictedTime;
						}else if(data.progRestrictedDay==3){
							$scope.progRestrictionWednesday=""+data.progRestrictedTime;
						}else if(data.progRestrictedDay==4){
							$scope.progRestrictionThursday=""+data.progRestrictedTime;
						}else if(data.progRestrictedDay==5){
							$scope.progRestrictionFriday=""+data.progRestrictedTime;
						}else if(data.progRestrictedDay==6){
							$scope.progRestrictionSaturday=""+data.progRestrictedTime;
						}else if(data.progRestrictedDay==7){
							$scope.progRestrictionSunday=""+data.progRestrictedTime;
						}
					});
						
					}, function errorCallback(response) {
					    // called asynchronously if an error occurs
					    // or server returns response with an error status.
					});
		 };
	
	
	
	$scope.createMembershipProgram =function(){
		var membershipDetails=generateMembershipDetailProgram($scope.progId);
		$scope.programMembership.programMembershipDetails=membershipDetails;
		$http({
			  method: 'POST',
			  url: "/bein/program/createMembershipProgram",
			  data:angular.toJson($scope.programMembership)
			}).then(function successCallback(response) {
				$scope.programMembership=response.data.resultObj;
				$scope.programMembership.type="pm";
				
				$scope.programMembership.progUserId=""+$scope.programMembership.progUserId;
				$scope.programMembership.progStatus=""+$scope.programMembership.progStatus;
				$scope.programMembership.progDurationType=""+$scope.programMembership.progDurationType;
				$scope.programMembership.freezeDurationType=""+$scope.programMembership.freezeDurationType;
				$scope.programMembership.progRestriction=""+$scope.programMembership.progRestriction;
				
				if(response.data.resultStatu==1){
					toastr.success($translate.instant('success'));
				}else{
					toastr.error($translate.instant('noProcessDone'));
				}
				findMembershipPrograms();
			}, function errorCallback(response) {
			    // called asynchronously if an error occurs
			    // or server returns response with an error status.
			});
	}
	
	
	function generateMembershipDetailProgram(progId){
		
		
		var days=new Array();
		if($scope.progRestrictionMonday!="-1"){
			var mondayObj=new Object();
			mondayObj.progId=progId;
			mondayObj.progRestrictedDay=1;
			mondayObj.progRestrictedTime=$scope.progRestrictionMonday;
			
			days.push(mondayObj);
		}
		if($scope.progRestrictionTuesday!="-1"){
			var tuesdayObj=new Object();
			tuesdayObj.progId=progId;
			tuesdayObj.progRestrictedDay=2;
			tuesdayObj.progRestrictedTime=$scope.progRestrictionTuesday;
			
			days.push(tuesdayObj);
		}
		if($scope.progRestrictionWednesday!="-1"){
			var wednesdayObj=new Object();
			wednesdayObj.progId=progId;
			wednesdayObj.progRestrictedDay=3;
			wednesdayObj.progRestrictedTime=$scope.progRestrictionWednesday;
			
			days.push(wednesdayObj);
		}
		if($scope.progRestrictionThursday!="-1"){
			var thursdayObj=new Object();
			thursdayObj.progId=progId;
			thursdayObj.progRestrictedDay=4;
			thursdayObj.progRestrictedTime=$scope.progRestrictionThursday;
			
			days.push(thursdayObj);
		}
		if($scope.progRestrictionFriday!="-1"){
			var fridayObj=new Object();
			fridayObj.progId=progId;
			fridayObj.progRestrictedDay=5;
			fridayObj.progRestrictedTime=$scope.progRestrictionFriday;
			
			days.push(fridayObj);
		}
		if($scope.progRestrictionSaturday!="-1"){
			var saturdayObj=new Object();
			saturdayObj.progId=progId;
			saturdayObj.progRestrictedDay=6;
			saturdayObj.progRestrictedTime=$scope.progRestrictionSaturday;
			
			days.push(saturdayObj);
		}
		if($scope.progRestrictionSunday!="-1"){
			var sundayObj=new Object();
			sundayObj.progId=progId;
			sundayObj.progRestrictedDay=7;
			sundayObj.progRestrictedTime=$scope.progRestrictionSunday;
			
			days.push(sundayObj);
		}
		
		return days;
		
	}
	
	
	
	$scope.deleteMembershipProgram =function(progId){
		   
		swal({
            title: $translate.instant("areYouSureToDelete"),
            text: $translate.instant("deleteMembershipComment"),
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: $translate.instant("yesDelete"),
            cancelButtonText: $translate.instant("noDelete"),
            closeOnConfirm: false,
            closeOnCancel: true },
        function (isConfirm) {
            if (isConfirm) {
            	var frmDatum = {"progId":progId,'type':'pm'}; 
     		  
            	
            	
            		$http({
	   				  method: 'POST',
	   				  url: "/bein/program/deleteMembershipProgram/"+progId,
	   				}).then(function successCallback(response) {
     				
	   					if(response.data.resultStatu=="1"){
	     					swal($translate.instant("deleted"), $translate.instant("deletedSuccessMessage"), "success");
	     					$scope.willProgramCreate=false;
	     					findMembershipPrograms();
	     				}else{
	     					swal($translate.instant("nodeleted"), $translate.instant("programUsedInSales"), "error");
	     				}
	   				}, function errorCallback(response) {
	   				    // called asynchronously if an error occurs
	   				    // or server returns response with an error status.
	   				});
            	
                
            } else {
                swal($translate.instant("deleteCanceled"), "");
            }
        });
	};
	
	
	
	
	
    
    

});