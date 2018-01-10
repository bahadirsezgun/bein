ptBossApp.factory("commonService",function($rootScope,$http,$translate,$timeout){
	var sharedService={};
	sharedService.loaderValue="true";
	sharedService.search;
	sharedService.searchBoxPH;
	
	sharedService.ptGlobal=null;
	sharedService.user=null;
	
	
	sharedService.restriction=null;
	
	sharedService.setPtGlobal=function(global){
		
		sharedService.ptGlobal=new Object();
		sharedService.ptGlobal.ptTz=global.ptTz;
		sharedService.ptGlobal.ptCurrency=global.ptCurrency;
		sharedService.ptGlobal.ptStaticIp=global.ptStaticIp;
		sharedService.ptGlobal.ptLang=(global.ptLang).substring(0,2);
		sharedService.ptGlobal.ptDateFormat=global.ptScrDateFormat;
		sharedService.ptGlobal.ptScrDateFormat=global.ptScrDateFormat;
		
		
	}
	
	sharedService.getPtGlobal=function(){
		if(sharedService.ptGlobal==null){
			return $http({method:"POST", url:"/bein/global/getGlobals"}).then(function(response){
							var res=response.data.resultObj;
							var lang=(res.ptLang).substring(0,2);
							$translate.use(lang);
							$translate.refresh;
							
							sharedService.setPtGlobal(res);
							return response.data.resultObj;
	        			});
		}else{
			return $timeout(function() { return sharedService.ptGlobal },100);
		}
	}
	
	sharedService.setRestriction=function(restriction){
		sharedService.restriction=new Object();
		sharedService.personalRestriction=restriction.personalRestriction;
		sharedService.personalRestriction=restriction.personalRestriction;
		sharedService.personalRestriction=restriction.personalRestriction;
		
	}
	sharedService.getRestriction=function(){
		if(sharedService.restriction==null){
			return $http({method:"POST", url:"/bein/global/getRestrictions"}).then(function(response){
							var res=response.data.resultObj;
							sharedService.setRestriction(res);
							return sharedService.restriction;
	        			});
		}else{
			return $timeout(function() { return sharedService.restriction },100);
		}
	}
	
	sharedService.setUser=function(user){
		sharedService.user=user;
	}
	
	
		
	
	
	sharedService.modal=function(lval){
		this.loaderValue=lval;
		this.broadCastItem();
	};
	
	sharedService.broadCastItem = function(){
		$rootScope.$broadcast("handlebroadcast");
	};
	
	sharedService.searchItem = function(){
		$rootScope.$broadcast("search");
	};
	
	sharedService.searchBoxPHItem =function(){
		$rootScope.$broadcast("searchBoxPH");
	};
	
	sharedService.helpItem = function(){
		$rootScope.$broadcast("help");
	};

	sharedService.pageName;
	sharedService.pageComment;
	sharedService.normalHeaderVisible=false;
	
	sharedService.setNormalHeader = function(){
		$rootScope.$broadcast("setNormalHeader");
	};
	
	sharedService.changeLang = function(lang){
		$rootScope.lang=lang;
		$rootScope.$broadcast("changeLang");
	};
	
	return sharedService;
});