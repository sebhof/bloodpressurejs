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

    var app = angular.module('bloodpressurejs.bloodpressureController', []);

    app.controller('BPCtrl', ['$scope', '$http', '$location', '$filter', function ($scope, $http, $location, $filter) {

            $scope.data = [];

            var toMoment = moment();
            var fromMoment = moment().subtract(30, 'days');

            $scope.to = toMoment.toDate();
            $scope.from = fromMoment.toDate();

            $http.get('application/bloodpressure/' + fromMoment.format('YYYY-MM-DD') + '/' + toMoment.format('YYYY-MM-DD')).success(function (data) {
                $scope.data = data;
                $scope.render();
            }).error(function (data) {
                console.log("ERROR: " + data);
            });

            $scope.highchartsNG = {
                options: {
                    chart: {
                        type: 'line'
                    }
                },
                yAxis: {
                    plotLines: [],
                    title: {
                        text: undefined
                    },
                    floor: 40
                },
                xAxis: {
                    categories: [],
                    type: 'datetime',
                    labels: {
                        formatter: function () {
                            return $filter('date')(this.value, 'dd.MM.yyyy');
                            ;
                        },
                        rotation: -45,
                        align: 'right'
                    },
                },
                series: [],
                title: {
                    text: undefined
                },
                loading: false
            };

            $scope.render = function () {
                var systoleData = [];
                var diastoleData = [];
                var rateData = [];
                for (i = $scope.data.items.length-1; i >= 0 ; i--) {
                    $scope.highchartsNG.xAxis.categories.push($scope.data.items[i].date);
                    systoleData.push($scope.data.items[i].systole);
                    diastoleData.push($scope.data.items[i].diastole);
                    rateData.push($scope.data.items[i].rate);
                }
                $scope.highchartsNG.series.push({name: 'Systolisch (mmHG)', color: 'blue', data: systoleData});
                $scope.highchartsNG.series.push({name: 'Diastolisch (mmHG)', color: 'red', data: diastoleData});
                $scope.highchartsNG.series.push({name: 'Puls/Min.', color: 'yellow', data: rateData});
            };

            $scope.getWHOImageSrc = function (item) {
                if (item === undefined) {
                    return "";
                } else if (item.whoState === 'OPTIMAL' || item.whoState === 'NORMAL' || item.whoState === 'HIGH_NORMAL') {
                    return 'images/who_green.png';
                } else if (item.whoState === 'STAGE_1') {
                    return 'images/who_yellow.png';
                } else if (item.whoState === 'STAGE_2' || item.whoState === 'STAGE_3') {
                    return 'images/who_red.png';
                } 
            };

        }]);

    app.controller('ImportCtrl', ['$scope', '$http', '$location', '$filter', '$modal', function ($scope, $http, $location, $filter, $modal) {

            $scope.db = {};
            $scope.db.rows = [];
            $scope.db.columns = [];

            $scope.radioModel = 'Datum';

            $scope.selectionModel = {
                bloodPressure: {
                    dateColumn: null,
                    timeColumn: null,
                    systoleColumn: null,
                    diastoleColumn: null,
                    rateColumn: null,
                    startRow: null,
                    endRow: null
                }
            };

            $scope.previewButtonEnabled = function () {
                return $scope.selectionModel.bloodPressure.dateColumn !== null
                        && $scope.selectionModel.bloodPressure.timeColumn !== null
                        && $scope.selectionModel.bloodPressure.systoleColumn !== null
                        && $scope.selectionModel.bloodPressure.diastoleColumn !== null
                        && $scope.selectionModel.bloodPressure.rateColumn !== null
                        && $scope.selectionModel.bloodPressure.startRow !== null
                        && $scope.selectionModel.bloodPressure.endRow !== null;
            };

            $scope.$watch('my_image_model', function () {

                console.log($scope.my_image_model);

                var reader = new FileReader();
                var name = $scope.my_image_model.name;
                reader.onload = function (e) {
                    var data = e.target.result;

                    var workbook = XLSX.read(data, {type: 'binary'});

                    var sheet_name_list = workbook.SheetNames;
                    sheet_name_list.forEach(function (y) {
                        var worksheet = workbook.Sheets[y];
                        for (z in worksheet) {
                            if (z[0] === '!')
                                continue;

                            var cell = worksheet[z];
                            var cell_address = XLSX.utils.decode_cell(z);
                            var row = cell_address['r'];
                            var column = cell_address['c'];
                            var value = cell.w;

                            if (!$scope.db.rows[row]) {
                                $scope.db.rows[row] = [];
                            }
                            $scope.db.rows[row][column] = value;

                            if (!$scope.db.columns[column]) {
                                $scope.db.columns[column] = {
                                    data: column,
                                    readOnly: true
                                };
                            }

                            console.log("Identifier: " + z + "; row:" + row + "; column:" + column + "; value: " + value);

                        }
                        ;
                        $scope.$apply();
                    });

                };
                reader.readAsBinaryString($scope.my_image_model);
            });

            $scope.afterSelectionEnd = function (r1, c1, r2, c2) {
                console.log("r1:" + r1 + ", c1:" + c1 + ", r2:" + r2 + ", c2:" + c2);
                // only in same column...
                if (c1 === c2) {
                    $scope.selectionModel.bloodPressure.startRow = r1;
                    $scope.selectionModel.bloodPressure.endRow = r2;
                    if ($scope.radioModel === 'Datum') {
                        $scope.selectionModel.bloodPressure.dateColumn = c1;
                    } else if ($scope.radioModel === 'Zeit') {
                        $scope.selectionModel.bloodPressure.timeColumn = c1;
                    } else if ($scope.radioModel === 'Systole') {
                        $scope.selectionModel.bloodPressure.systoleColumn = c1;
                    } else if ($scope.radioModel === 'Diastole') {
                        $scope.selectionModel.bloodPressure.diastoleColumn = c1;
                    } else if ($scope.radioModel === 'Puls') {
                        $scope.selectionModel.bloodPressure.rateColumn = c1;
                    }
                } else {
                    //report error in selection
                }
                $scope.$apply();
            };

            $scope.prepareData = function () {
                var importData = [];

                $scope.convertDate = function (date, time) {
                    var dateParts = date.split(".");
                    var timeParts = time.split(":");
                    var d = new Date(dateParts[2], dateParts[1] - 1, dateParts[0], timeParts[0], timeParts[1], 0, 0);
                    return d;
                };

                for (i = $scope.selectionModel.bloodPressure.startRow; i <= $scope.selectionModel.bloodPressure.endRow; i++) {
                    var row = $scope.db.rows[i];
                    var data = {
                        date: $scope.convertDate(row[$scope.selectionModel.bloodPressure.dateColumn], row[$scope.selectionModel.bloodPressure.timeColumn]),
                        systole: row[$scope.selectionModel.bloodPressure.systoleColumn],
                        diastole: row[$scope.selectionModel.bloodPressure.diastoleColumn],
                        rate: row[$scope.selectionModel.bloodPressure.rateColumn]
                    };
                    importData.push(data);
                }
                ;
                return importData;
            };

            $scope.previewBloodpressure = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'import_preview_modal.html',
                    controller: 'ModalInstanceCtrl',
                    backdrop: 'static',
                    resolve: {
                        items: function () {
                            return $scope.prepareData();
                        }
                    }
                });
            };

        }]);

    app.controller('ModalInstanceCtrl', function ($http, $scope, $modalInstance, items) {

        $scope.items = items;

        $scope.ok = function () {
            $http.post('application/bloodpressure', $scope.items)
                    .success(function (data) {
                        console.log("OK: " + data);
                    }).error(function (data) {
                console.log("ERROR: " + data);
            });
            $modalInstance.close();
        };

        $scope.cancel = function () {
            $modalInstance.dismiss('cancel');
        };
    });

})();