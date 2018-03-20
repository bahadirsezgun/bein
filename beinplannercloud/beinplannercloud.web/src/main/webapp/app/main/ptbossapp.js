var ptBossApp;
define([],function(){
	ptBossApp=angular.module('PTBossApp',['ngRoute','pascalprecht.translate', 'ngSanitize','ui.mask','ngDraggable','ngPrint','ngIdle'])
	  .config(['KeepaliveProvider', 'IdleProvider', function(KeepaliveProvider, IdleProvider) {
		  IdleProvider.idle(5);
		  IdleProvider.timeout(1100);
		  KeepaliveProvider.interval(10);
		}]);;
	return ptBossApp;
});



