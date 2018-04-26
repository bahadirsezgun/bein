ptBossApp.controller('DashboardController', function($rootScope,$scope,$translate,homerService,commonService,globals,calendarTimesService) {
	
	/***********************************
	GLOBALS
	***********************************/
	
	$scope.ptCurrency;
	$scope.ptLang;
	$scope.ptDateFormat;
	$scope.ptTz;
	$scope.user;
	
	$scope.dashboardPage=""
	
	$rootScope.calendarTimes=new Object();
	
	$rootScope.calendarTimes.morningTimes;
	$rootScope.calendarTimes.afternoonTimes;
	$rootScope.calendarTimes.nightTimes;
	$rootScope.calendarTimes.allDayTimes;
		
	$scope.init=function(){
		commonService.normalHeaderVisible=false;
		commonService.setNormalHeader();
		$('.animate-panel').animatePanel();
		$(".splash").css("display",'');
		findGlobals();
	
	}
	
	$scope.$on('$viewContentLoaded', function(event) {
		
	});
	
	function findGlobals(){
		
		commonService.getRestriction();
		
		commonService.getPtGlobal().then(function(){
			$scope.ptTz=commonService.ptGlobal.ptTz;
			$scope.ptCurrency=commonService.ptGlobal.ptCurrency;
			$scope.ptStaticIp=commonService.ptGlobal.ptStaticIp;
			$scope.ptLang=(commonService.ptGlobal.ptLang).substring(0,2);
			$scope.ptDateFormat=commonService.ptGlobal.ptScrDateFormat;
			$translate.use($scope.ptLang);
			$translate.refresh;
			commonService.changeLang($scope.ptLang);
			getDashboardMenu();
		});
	
	};
  
    function getDashboardMenu(){
		
		$.ajax({
  		  type:'POST',
  		  url: "/bein/menu/getDashboardMenu",
  		  contentType: "application/json; charset=utf-8",				    
  		  dataType: 'json', 
  		  cache:false
  		}).done(function(result) {
  			$scope.dashboardPage="/bein/dashboard/"+result.resultObj.menuLink;
  			$scope.$apply();
  			$(".splash").css("display",'none');
  		}).fail  (function(jqXHR, textStatus, errorThrown){ 
  			
  			if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/lock.html");
		});
	}
	
    
    
    
    
});
