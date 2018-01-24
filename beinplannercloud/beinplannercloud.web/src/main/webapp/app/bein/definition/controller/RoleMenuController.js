ptBossApp.controller('RoleMenuController', function($scope,$http,$translate,parameterService,$location,homerService,commonService) {
	
	$scope.USER_TYPE_SUPER_MANAGER=5;
	$scope.USER_TYPE_MANAGER = 4;
	$scope.USER_TYPE_SCHEDULAR_STAFF  = 3;
	$scope.USER_TYPE_STAFF = 2;
	
	   $scope.menus;
	   $scope.topmenus;
	   $scope.dashboardPages=[{"name":$translate.instant("finance"),"value":800}
	    					,{"name":$translate.instant("classes"),"value":801}
	    					,{"name":$translate.instant("special"),"value":802}];
	   
	   $scope.dashmenu;
	   
	   $scope.userType=$scope.USER_TYPE_SUPER_MANAGER;
	   
	   
	   toastr.options = {
            "debug": false,
            "newestOnTop": false,
            "positionClass": "toast-top-center",
            "closeButton": true,
            "toastClass": "animated fadeInDown",
        };
	
	
	   $scope.init = function(){
		   
		    commonService.pageName=$translate.instant("definition_rolmenu");
			commonService.pageComment=$translate.instant("rolMenuDefinitionComment");
			commonService.normalHeaderVisible=true;
			commonService.setNormalHeader();
		   
			$http({
				  method:'POST',
				  url: "/bein/menu/findMenuLeft/"+$scope.USER_TYPE_SUPER_MANAGER,
				  contentType: "application/json; charset=utf-8",				    
				}).then(function successCallback(response) {
					$scope.menus=response.data.resultObj;
				}, function errorCallback(response) {
					$location.path("/login");
				});
			
			$http({
				  method:'POST',
				  url: "/bein/menu/findTopMenu/"+$scope.USER_TYPE_SUPER_MANAGER,
				}).then(function successCallback(response) {
					$scope.topmenus=response.data.resultObj;
				}, function errorCallback(response) {
					$location.path("/login");
				});
		   
			 
			   $http({
				   method:'POST',
					  url: "/bein/menu/findDashboardMenu/"+$scope.USER_TYPE_SUPER_MANAGER,
					}).then(function successCallback(response) {
						$scope.dashmenu=response.data.resultObj;
					}, function errorCallback(response) {
						$location.path("/login");
					});
	   }
	   
	   $scope.changeUserType=function(userType){
		   $scope.userType=userType;
		   $scope.findMenus(userType);
	   }
	   
	   
	   
	   $scope.findMenus=function(userType){
		      $http({
		    	   method:'POST',
				  url: "/bein/menu/findMenuLeft/"+userType,
				}).then(function successCallback(response) {
					$scope.menus=response.data.resultObj;
				}, function errorCallback(response) {
					$location.path("/login");
				});
		       
		      $http({
		    	   method:'POST',
					  url: "/bein/menu/findTopMenu/"+userType
					}).then(function successCallback(response) {
						$scope.topmenus=response.data.resultObj;
					}, function errorCallback(response) {
						$location.path("/login");
					});
		       
		       
		      $http({
		    	  method:'POST',
					  url: "/bein/menu/findDashboardMenu/"+userType,
					}).then(function successCallback(response) {
						$scope.dashmenu=response.data.resultObj;
					}, function errorCallback(response) {
						$location.path("/login");
					});
	   }
	   
	   $scope.changeMenuAuth=function(menuId,authority,menuName){
		   
		   if(authority==1){
			   removeMenuAuth(menuId,menuName);
		   }else{
			   addMenuAuth(menuId,menuName);
		   }
		   
	   }
	   
	   function getRoleName(userType){
		   if(userType==$scope.USER_TYPE_SUPER_MANAGER){
			   return "SMANAGER";
		   }else if(userType==$scope.USER_TYPE_MANAGER){
			   return "MANAGER";
		   }else if(userType==$scope.USER_TYPE_SCHEDULAR_STAFF){
			   return "SSTAFF";
		   }else if(userType==$scope.USER_TYPE_STAFF){
			   return "STAFF";
		   }
		}
	   
	   
	   
	   $scope.changeDashboard=function(menuId){
		   var frmDatum = {'roleId':$scope.userType,
			        'menuId':menuId,
			        'roleName':getRoleName($scope.userType),
			        }; 
		   $http({
				  type:'POST',
				  url: "../pt/definition/menu/changeDashboard/"+$scope.userType,
				  contentType: "application/json; charset=utf-8",				    
				  data: JSON.stringify(frmDatum),
				  dataType: 'json', 
				  cache:false
				}).then(function successCallback(response) {
					$scope.findMenus($scope.userType);
				});
	   };
	   
	   var addMenuAuth=function(menuId,menuName){
		   
		   var frmDatum = {'roleId':$scope.userType,
			        'menuId':menuId,
			        'roleName':getRoleName($scope.userType),
			        'menuName':menuName
			        }; 
		   
		        $.ajax({
				  type:'POST',
				  url: "../pt/definition/menu/addRolMenu",
				  contentType: "application/json; charset=utf-8",				    
				  data: JSON.stringify(frmDatum),
				  dataType: 'json', 
				  cache:false
				}).then(function successCallback(response) {
					$scope.findMenus($scope.userType);
				});
	   }
	   
	   var removeMenuAuth=function(menuId,menuName){
		   
		   var frmDatum = {'roleId':$scope.userType,
			        'menuId':menuId,
			        'roleName':getRoleName($scope.userType),
			        'menuName':menuName
			        }; 
		   
		   $.ajax({
				  type:'POST',
				  url: "../pt/definition/menu/removeRolMenu",
				  contentType: "application/json; charset=utf-8",				    
				  data: JSON.stringify(frmDatum),
				  dataType: 'json', 
				  cache:false
				}).then(function successCallback(response) {
					$scope.findMenus($scope.userType);
				});
	   }
});
