ptBossApp.factory("commonService",function($rootScope){
	var sharedService={};
	sharedService.loaderValue="true";
	sharedService.search;
	sharedService.searchBoxPH;
	
	sharedService.ptGlobal=null;
	sharedService.user=null;
	
	sharedService.setPtGlobal=function(global){
		
		sharedService.ptGlobal=new Object();
		sharedService.ptGlobal.ptTz=global.ptTz;
		sharedService.ptGlobal.ptCurrency=global.ptCurrency;
		sharedService.ptGlobal.ptStaticIp=global.ptStaticIp;
		sharedService.ptGlobal.ptLang=(global.ptLang).substring(0,2);
		sharedService.ptGlobal.ptDateFormat=global.ptScrDateFormat;
		
	}
	
	sharedService.getPtGlobal=function(){
		if(sharedService.ptGlobal==null){
			return null;
		}else{
			return sharedService.ptGlobal;
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