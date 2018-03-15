ptBossApp.controller('PersonelProgController', function($rootScope,$scope,$translate,parameterService,$location,homerService,commonService,globals,$http) {

		$scope.programPersonal=new Object();
		$scope.programPersonal.progStatus="1";
		$scope.programPersonal.progUserId="0";
		$scope.programPersonal.type="pp";
		$scope.programPersonal.restDuration="0";
		$scope.programPersonal.restType="0";
		
		
	    $scope.programPersonals;
		$scope.noProgram=true;
		$scope.willProgramCreate=false;
		$scope.programInstructors;
		
		
	    toastr.options = {
            "debug": false,
            "newestOnTop": false,
            "positionClass": "toast-top-center",
            "closeButton": true,
            "toastClass": "animated fadeInDown",
        };
	
	    $scope.init = function(){
	    		$("[data-toggle=popover]").popover();
	    		commonService.pageName=$translate.instant("definition_personelprog");
			commonService.pageComment=$translate.instant("personelProgDefinitionComment");
			commonService.normalHeaderVisible=true;
			commonService.setNormalHeader();
			findInstructors();
			findPrograms();
			
		};
	    
	    
	    
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
	    
	    function findPrograms(){
	    	  $http({
				  method: 'POST',
				  url: "/bein/program/findPersonalPrograms"
				}).then(function successCallback(response) {
					$scope.programPersonals=response.data.resultObj;
					if($scope.programPersonals.length!=0){
						$scope.noProgram=false;
					}else{
						$scope.noProgram=true;
					}
					
				}, function errorCallback(response) {
				    // called asynchronously if an error occurs
				    // or server returns response with an error status.
				});
	   
		}
		
		$scope.addNewProgram =function(){
			$scope.programPersonal=new Object();
			$scope.programPersonal.progStatus="1";
			$scope.programPersonal.progUserId="0";
			$scope.programPersonal.restFlag="0";
			$scope.programPersonal.type="pp";
			$scope.programPersonal.progDescription="";
			$scope.programPersonal.progComment="";
			$scope.programPersonal.restDuration="0";
			$scope.programPersonal.restType="0";
			$scope.willProgramCreate=true;
			
		};
		
		
		$scope.showProgram =function(progId){
			$http({
				  method: 'POST',
				  url: "/bein/program/findPersonalProgramById/"+progId
				}).then(function successCallback(response) {
					$scope.programPersonal=response.data.resultObj;
					$scope.programPersonal.progStatus=""+$scope.programPersonal.progStatus;
					$scope.programPersonal.progUserId=""+$scope.programPersonal.progUserId;
					$scope.programPersonal.restFlag=""+$scope.programPersonal.restFlag;
					$scope.programPersonal.restDuration=""+$scope.programPersonal.restDuration;
					$scope.programPersonal.restType=""+$scope.programPersonal.restType;
					
					
					$scope.programPersonal.type="pp";
					$scope.willProgramCreate=true;
				}, function errorCallback(response) {
				    // called asynchronously if an error occurs
				    // or server returns response with an error status.
				});
		};
		
		
		
		$scope.createProgram =function(){
			$http({
				  method: 'POST',
				  url: "/bein/program/createPersonalProgram",
				  data:angular.toJson($scope.programPersonal)
				}).then(function successCallback(response) {
					$scope.programPersonal=response.data.resultObj;
					$scope.programPersonal.progStatus=""+$scope.programPersonal.progStatus;
					$scope.programPersonal.progUserId=""+$scope.programPersonal.progUserId;
					$scope.programPersonal.restFlag=""+$scope.programPersonal.restFlag;
					$scope.programPersonal.restDuration=""+$scope.programPersonal.restDuration;
					$scope.programPersonal.restType=""+$scope.programPersonal.restType;
					
					$scope.programPersonal.type="pp";
					$scope.willProgramCreate=true;
					
					if(response.data.resultStatu==1){
						toastr.success($translate.instant('success'));
					}else{
						toastr.error($translate.instant('noProcessDone'));
					}
					
					findPrograms();
				}, function errorCallback(response) {
				    // called asynchronously if an error occurs
				    // or server returns response with an error status.
				});
			
		}
			
		
		
		$scope.deleteProgram =function(progId){
			swal({
	            title: $translate.instant("areYouSureToDelete"),
	            text: $translate.instant("deleteProgramComment"),
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: $translate.instant("yesDelete"),
	            cancelButtonText: $translate.instant("noDelete"),
	            closeOnConfirm: false,
	            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	
	            	 
	            	 $http({
	   				  method: 'POST',
	   				  url: "/bein/program/deletePersonalProgram/"+progId,
	   				}).then(function successCallback(response) {
	   					if(response.data.resultStatu=="1"){
	     					swal($translate.instant("deleted"), $translate.instant("deletedSuccessMessage"), "success");
	     					$scope.willProgramCreate=false;
	     					findPrograms();
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