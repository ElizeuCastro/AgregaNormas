'use strict';

/**
 * @ngdoc overview
 * @name vaiApp
 * @description
 * # vaiApp
 *
 * Main module of the application.
 */
var app = angular
  .module('app', [
    'ngAnimate',
    'ngCookies',
    'ngResource',
    'ngRoute',
    'ngSanitize',
    'ngTouch'
  ]);

  app.config(function ($routeProvider, $locationProvider) {
    $routeProvider
      .when('/', {
        
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'

      }).when('/about', {
        
        templateUrl: 'views/about.html',
        controller: 'AboutCtrl'

      }).when('/esferas', {
        
        templateUrl: 'views/esferas.html'

      }).when('/normas/:tipoEsfera', {
        
        templateUrl: 'views/normas.html',
        controller: 'NormasCtrl'

      }).otherwise({
        
        redirectTo: '/'

      });
      
  });
