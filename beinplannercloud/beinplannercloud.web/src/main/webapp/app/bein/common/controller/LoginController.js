ptBossApp.controller('LoginController', function($scope,commonService,$element,$location) {

	
	alert("SESSION EXPIRED ...");
	
	//$location.path("/login");
	
	$(location).attr('href', '/');
	
	
});