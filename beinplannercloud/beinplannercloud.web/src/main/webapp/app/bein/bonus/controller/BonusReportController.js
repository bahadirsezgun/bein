ptBossApp.controller('BonusReportController', function($scope,$translate,homerService,commonService,globals,$http ) {

	$scope.staffs=new Array();
	$scope.staffId="0";
	$scope.ptCurrency;
	$scope.year;
	$scope.filter=true;
	
	$scope.userBonusPersonalObjs;
	$scope.userBonusClassObjs;
	
	
	$scope.months=new Array({value:"0",name:$translate.instant("pleaseSelect")}
		    ,{value:"1",name:$translate.instant("january")}
		   ,{value:"2",name:$translate.instant("february")}
		   ,{value:"3",name:$translate.instant("march")}
		   ,{value:"4",name:$translate.instant("april")}
		   ,{value:"5",name:$translate.instant("may")}
		   ,{value:"6",name:$translate.instant("june")}
		   ,{value:"7",name:$translate.instant("july")}
		   ,{value:"8",name:$translate.instant("august")}
		   ,{value:"9",name:$translate.instant("september")}
		   ,{value:"10",name:$translate.instant("october")}
		   ,{value:"11",name:$translate.instant("november")}
		   ,{value:"12",name:$translate.instant("december")}
		   );
		
		
	$scope.years=new Array();
	$scope.userType=3;	
		
	$scope.init=function(){
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
		
		var date=new Date();
		var year=date.getFullYear();
		for(var i=-10;i<10;i++){
			$scope.years.push(year+i);
		}
		$scope.year=year;
		
		commonService.getUser().then(function(user){
			$scope.userType=user.userType;
			
			commonService.getPtGlobal().then(function(global){
				$scope.dateFormat=global.ptScrDateFormat;
				$scope.dateTimeFormat=global.ptScrDateFormat+" HH:mm";
				$scope.ptCurrency=global.ptCurrency;
				
				if($scope.userType==globals.USER_TYPE_ADMIN){
					findInstructors().then(function(response){
						
						$scope.staffs=response;
						$scope.staffId="0";
					});
				}
				
			});
		});
		
	}
	
	
	
	$scope.queryBonusPayment=function(){
		$(".splash").css("display",'');
		if($scope.staffId=="0" && $scope.userType==globals.USER_TYPE_ADMIN){
			$(".splash").css("display",'none');
			toastr.error($translate.instant('chooseInstructor'));
		  return;
		}
	
		  var searchObj=new Object();
		  searchObj.schStaffId=$scope.staffId;
		  searchObj.year=$scope.year;
		  
		    
		 $http({
			  method:'POST',
			  url: "/bein/bonus/calculate/findStaffAllBonus",
			  data:angular.toJson(searchObj)
			}).then(function(response) {
				$scope.userBonusPersonalObjs=response.data.userPBonusObj;
				$scope.userBonusClassObjs=response.data.userCBonusObj;
				
				$scope.filter=false;
		        $(".splash").css("display",'none');
			});
	};
	
	
	 function findInstructors(){
		   return  $http({
			  method: 'POST',
			  url: "/bein/staff/findAllSchedulerStaff"
			}).then(function successCallback(response) {
				return response.data.resultObj;
			}, function errorCallback(response) {
				$location.path("/login");
			});
	  }
	
});