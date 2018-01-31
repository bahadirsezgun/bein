ptBossApp.controller('StaffPlanController', function($scope,$http,$translate,parameterService,$location,homerService,commonService,globals) {

	$scope.staffs;
	$scope.year;
	$scope.month="0";
	$scope.firmId;
	$scope.staffId="0";
	$scope.showQuery=true;
	
	$scope.monthly=true;
	
	$scope.staffPlans;
	
	$scope.startDate=new Date();
	$scope.endDate=new Date();
	
	$scope.user;
	$scope.staffId="0";
	
	$scope.initSPC=function(){
		commonService.pageName=$translate.instant("staffPlanTitle");
		commonService.pageComment=$translate.instant("staffPlanTitleComment");
		commonService.normalHeaderVisible=true;
		commonService.setNormalHeader();
		
		var date=new Date();
		var year=date.getFullYear();
		
		for(var i=-10;i<10;i++){
			$scope.years.push(year+i);
		}
		
		$scope.year=year;
		$scope.month=""+(date.getMonth()+1);
		$('#inQueryCheck').iCheck({
	        checkboxClass: 'icheckbox_square-green',
	        radioClass: 'iradio_square-green'
	    });
		
		commonService.getUser().then(function(user){
			$scope.user=user;
			findInstructors().then(function(staffs){
				$scope.staffs=staffs;
				$scope.staffId="0";
			});
		});
		
		commonService.getPtGlobal().then(function(global){
			$scope.dateFormat=global.ptScrDateFormat;
			$scope.dateTimeFormat=global.ptDateTimeFormat;
			$scope.ptCurrency=global.ptCurrency;
		});
	}
	
	
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
	
	
	
	
	$scope.showPlanDetail=function(staffPlan){
		
	}
	
	
	$scope.query=function(typeOfSchedule){
		var queryType=1; //Monthly
		if(!$scope.monthly){
			 queryType=2; // Daily
		}
		
		
		var queryObj=new Object();
		queryObj.month=$scope.month;
		queryObj.year=$scope.year;
		queryObj.staffId=$scope.staffId;
		queryObj.typeOfSchedule=typeOfSchedule;
		queryObj.queryType=queryType;
		
		$http({
	  		  method:'POST',
	  		  url: "/bein/staff/getSchStaffPlan",
	  		  data:angular.toJson(queryObj),
	  		}).then(function successCallback(response) {
	  			$scope.staffPlans=response.data;
	  			$scope.showQuery=false;
	  		}, function errorCallback(response) {
				$location.path("/login");
			});
		
	}
	
	/*
	$scope.queryPersonal=function(){
		var frmDatum={"month":$scope.month,"year":$scope.year,"staffId":$scope.staffId,"typeOfSchedule":globals.PROGRAM_PERSONAL}
		$.ajax({
	  		  type:'POST',
	  		  url: "../pt/scheduleStaff/getSchStaffPlan",
	  		  data:JSON.stringify(frmDatum),
	  		  contentType: "application/json; charset=utf-8",				    
	  		  dataType: 'json', 
	  		  cache:false
	  		}).done(function(res) {
	  			$scope.staffPlans=res;
	  			$scope.showQuery=false;
	  			$scope.$apply();
	  		});
		
		
	}
	
	$scope.queryClass=function(){
		var frmDatum={"month":$scope.month,"year":$scope.year,"staffId":$scope.staffId,"typeOfSchedule":globals.PROGRAM_CLASS}
		$.ajax({
	  		  type:'POST',
	  		  url: "../pt/scheduleStaff/getSchStaffPlan",
	  		  data:JSON.stringify(frmDatum),
	  		  contentType: "application/json; charset=utf-8",				    
	  		  dataType: 'json', 
	  		  cache:false
	  		}).done(function(res) {
	  			$scope.staffPlans=res;
	  			$scope.showQuery=false;
	  			$scope.$apply();
	  		});
		
		
	}
	
	
	
	$scope.queryPersonalDate=function(){
		var frmDatum={"startDateStr":$scope.startDate,"endDateStr":$scope.endDate,"staffId":$scope.staffId,"typeOfSchedule":globals.PROGRAM_PERSONAL}
		$.ajax({
	  		  type:'POST',
	  		  url: "../pt/scheduleStaff/getSchStaffPlanByDate",
	  		  data:JSON.stringify(frmDatum),
	  		  contentType: "application/json; charset=utf-8",				    
	  		  dataType: 'json', 
	  		  cache:false
	  		}).done(function(res) {
	  			$scope.staffPlans=res;
	  			$scope.showQuery=false;
	  			$scope.$apply();
	  		});
		
		
	}
	
	$scope.queryClassDate=function(){
		var frmDatum={"startDateStr":$scope.startDate,"endDateStr":$scope.endDate,"staffId":$scope.staffId,"typeOfSchedule":globals.PROGRAM_CLASS}
		$.ajax({
	  		  type:'POST',
	  		  url: "../pt/scheduleStaff/getSchStaffPlanByDate",
	  		  data:JSON.stringify(frmDatum),
	  		  contentType: "application/json; charset=utf-8",				    
	  		  dataType: 'json', 
	  		  cache:false
	  		}).done(function(res) {
	  			$scope.staffPlans=res;
	  			$scope.showQuery=false;
	  			$scope.$apply();
	  		});
		
		
	}
	
	*/
	
	$('#inQueryCheck').on('ifChanged', function(event) {
		if(event.target.checked){
			$scope.monthly=true;
		}else{
			$scope.monthly=false;
		}
		$scope.$apply();
    });
	
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