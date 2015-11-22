/* 
 * Copyright (C) 2015 shofmann
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


(function () {

    var app = angular.module('bloodpressurejs', ['ngRoute', 'highcharts-ng', 'ngHandsontable', 'ui.bootstrap', 'bloodpressurejs.bloodpressureController', 'bloodpressurejs.weightController']);

    app.value('version', '1.0.0');

    app.config(function ($routeProvider) {
        $routeProvider
                .when('/', {
                    templateUrl: 'html/bloodpressure-report.html',
                    controller: 'BPCtrl'
                })
                .when('/import-bloodpressure', {
                    templateUrl: 'html/import-bloodpressure.html',
                    controller: 'ImportBPCtrl'
                })
                .when('/import-weight', {
                    templateUrl: 'html/import-weight.html',
                    controller: 'ImportWeightCtrl'
                })
                .otherwise({
                    redirectTo: '/'
                });
    });

    app.directive('file', function () {
        return {
            require: "ngModel",
            restrict: 'A',
            link: function ($scope, el, attrs, ngModel) {
                el.bind('change', function (event) {
                    var files = event.target.files;
                    var file = files[0];

                    ngModel.$setViewValue(file);
                    $scope.$apply();
                });
            }
        };
    });

})();