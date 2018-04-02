ptBossLoginApp.controller('AppPlanController', function($scope,$translate,$http,$location) {
	
	
	$scope.currency="TRY";
	$scope.planBasic="";
	$scope.planPremium="";
	$scope.planPremium="";
	$scope.planPrestige="";
	
	$scope.m100000="100000";
	$scope.m50="50";
	$scope.m150="150";
	$scope.m450="450";
	
	
	$scope.descriptionBasic="BASIC PLAN";
	$scope.descriptionStandard="STANDART PLAN";
	$scope.descriptionPremium="PREMIUM PLAN";
	$scope.descriptionPrestige="PRESTIGE PLAN";
	$scope.lang="";
	
	$scope.init=function(){
		
		 var userLang = navigator.language || navigator.userLanguage; 
	     
		 
	   
	     if((userLang).substring(0,2)=="tr"){
	    	 $scope.currency="USD";
	    	 $scope.planBasic="plan_Cb0ChWxeskAV8w";
	    	 $scope.planStandard="plan_Cb0FxOZfmFoQFZ";
	    	 $scope.planPremium="plan_CbbW2NaZiQNOBo";
	    	 $scope.planPrestige="plan_Cbbg8wNHgyB5CH";
	    	 $scope.lang="tr_TR";
	    	 $translate.use("tr");
	     }else{
	    	 $scope.currency="USD";
	    	 $scope.planBasic="plan_Cb0ChWxeskAV8w";
	    	 $scope.planStandard="plan_Cb0FxOZfmFoQFZ";
	    	 $scope.planPremium="plan_CbbW2NaZiQNOBo";
	    	 $scope.planPrestige="plan_Cbbg8wNHgyB5CH";
	    	 $scope.lang="en_EN";
	    	 $translate.use("en");
	     }
	     
	     
	};
	
});