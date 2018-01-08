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
		findGlobals();
		/*
		$rootScope.calendarTimes.morningTimes=calendarTimesService.getMorningTimes();
		$rootScope.calendarTimes.afternoonTimes=calendarTimesService.getAfternoonTimes();
		$rootScope.calendarTimes.nightTimes=calendarTimesService.getNightTimes();
		$rootScope.calendarTimes.allDayTimes=calendarTimesService.getNightTimes();
		*/
	}
	
	$scope.$on('$viewContentLoaded', function(event) {
		
	});
	
	function findGlobals(){
		if($rootScope.ptGlobal!=null){
		
		$scope.ptTz=commonService.ptGlobal.ptTz;
		$scope.ptCurrency=commonService.ptGlobal.ptCurrency;
		$scope.ptStaticIp=commonService.ptGlobal.ptStaticIp;
		$scope.ptLang=(commonService.ptGlobal.ptLang).substring(0,2);
		$scope.ptDateFormat=commonService.ptGlobal.ptScrDateFormat;
		
		$translate.use($scope.ptLang);
		$translate.refresh;
		commonService.changeLang($scope.ptLang);
		getDashboardMenu();
		}else{
    	$.ajax({
    		  type:'POST',
    		  url: "/bein/global/getGlobals",
    		  contentType: "application/json; charset=utf-8",				    
    		  dataType: 'json', 
    		  cache:false
    		}).done(function(result) {
    			if(result!=null){
    				var res=result.resultObj;
    				$scope.ptTz=res.ptTz;
    				$scope.ptCurrency=res.ptCurrency;
    				$scope.ptStaticIp=res.ptStaticIp;
    				$scope.ptLang=(res.ptLang).substring(0,2);
    				$scope.ptDateFormat=res.ptScrDateFormat;
    				//loggedInUser();
    				
    				
    				$translate.use($scope.ptLang);
    				$translate.refresh;
    				commonService.changeLang($scope.ptLang);
    				commonService.setPtGlobal(res);
    				getDashboardMenu();
    				$scope.$apply();
    				
    			}else{
    				$scope.ptLang = navigator.language || navigator.userLanguage; 
    				$scope.ptLang = ($scope.ptLang).substring(0,2);
    				if(ptLang=="tr"){
    					$translate.use($scope.ptLang);
    				}else{
    					$translate.use("en");
    				}
    				$translate.refresh;
    				commonService.changeLang($scope.ptLang);
    				getDashboardMenu();
    				$scope.$apply();
    			}
    		
    		}).fail  (function(jqXHR, textStatus, errorThrown) 
    				{ 
  			  if(jqXHR.status == 404 || textStatus == 'error')	
  				  $(location).attr("href","/lock.html");
  			  
  			  
	  			$scope.ptLang = navigator.language || navigator.userLanguage; 
				$scope.ptLang = ($scope.ptLang).substring(0,2);
				if(ptLang=="tr"){
					$translate.use($scope.ptLang);
				}else{
					$translate.use("en");
				}
				$translate.refresh;
				commonService.changeLang($scope.ptLang);
				$scope.$apply();
  			});
		}
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
  		}).fail  (function(jqXHR, textStatus, errorThrown){ 
  			
  			if(jqXHR.status == 404 || textStatus == 'error')	
				  $(location).attr("href","/lock.html");
		});
	}
	
    
    
    
    
});
