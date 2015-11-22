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

    var app = angular.module('bloodpressurejs.weightController', []);

    app.controller('ImportWeightCtrl', ['$scope', '$http', '$location', '$filter', '$modal', function ($scope, $http, $location, $filter, $modal) {

            $scope.db = {};
            $scope.db.rows = [];
            $scope.db.columns = [];

            $scope.radioModel = 'Datum';

            $scope.selectionModel = {
                weight: {
                    dateColumn: null,
                    timeColumn: null,
                    weightColumn: null,
                    muscleColumn: null,
                    bodyFatColumn: null,
                    waterColumn: null,
                    boneColumn: null,
                    bmiColumn: null,
                    startRow: null,
                    endRow: null
                }
            };

            $scope.previewButtonEnabled = function () {
                return $scope.selectionModel.weight.dateColumn !== null
                        && $scope.selectionModel.weight.timeColumn !== null
                        && $scope.selectionModel.weight.weightColumn !== null
                        && $scope.selectionModel.weight.muscleColumn !== null
                        && $scope.selectionModel.weight.bodyFatColumn !== null
                        && $scope.selectionModel.weight.waterColumn !== null
                        && $scope.selectionModel.weight.boneColumn !== null
                        && $scope.selectionModel.weight.bmiColumn !== null
                        && $scope.selectionModel.weight.startRow !== null
                        && $scope.selectionModel.weight.endRow !== null;
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
                    $scope.selectionModel.weight.startRow = r1;
                    $scope.selectionModel.weight.endRow = r2;
                    if ($scope.radioModel === 'Datum') {
                        $scope.selectionModel.weight.dateColumn = c1;
                    } else if ($scope.radioModel === 'Zeit') {
                        $scope.selectionModel.weight.timeColumn = c1;
                    } else if ($scope.radioModel === 'Gewicht') {
                        $scope.selectionModel.weight.weightColumn = c1;
                    } else if ($scope.radioModel === 'BMI') {
                        $scope.selectionModel.weight.bmiColumn = c1;
                    } else if ($scope.radioModel === 'Muskelanteil') {
                        $scope.selectionModel.weight.muscleColumn = c1;
                    } else if ($scope.radioModel === 'Körperfettanteil') {
                        $scope.selectionModel.weight.bodyFatColumn = c1;
                    } else if ($scope.radioModel === 'Wasseranteil') {
                        $scope.selectionModel.weight.waterColumn = c1;
                    } else if ($scope.radioModel === 'Knochenanteil') {
                        $scope.selectionModel.weight.boneColumn = c1;
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

                for (i = $scope.selectionModel.weight.startRow; i <= $scope.selectionModel.weight.endRow; i++) {
                    var row = $scope.db.rows[i];
                    var data = {
                        date: $scope.convertDate(row[$scope.selectionModel.weight.dateColumn], row[$scope.selectionModel.weight.timeColumn]),
                        weight: parseFloat(row[$scope.selectionModel.weight.weightColumn].replace(',','.')),
                        bmi: parseFloat(row[$scope.selectionModel.weight.bmiColumn].replace(',','.')),
                        muscle: parseFloat(row[$scope.selectionModel.weight.muscleColumn].replace(',','.')),
                        bodyFat: parseFloat(row[$scope.selectionModel.weight.bodyFatColumn].replace(',','.')),
                        water: parseFloat(row[$scope.selectionModel.weight.waterColumn].replace(',','.')),
                        bone: parseFloat(row[$scope.selectionModel.weight.boneColumn].replace(',','.'))
                    };
                    importData.push(data);
                }
                ;
                return importData;
            };

            $scope.previewWeight = function () {
                var modalInstance = $modal.open({
                    templateUrl: 'import_weight_preview_modal.html',
                    controller: 'ModalWeightImportCtrl',
                    backdrop: 'static',
                    resolve: {
                        items: function () {
                            return $scope.prepareData();
                        }
                    }
                });
            };

        }]);
    
    app.controller('ModalWeightImportCtrl', function ($http, $scope, $modalInstance, items) {

        $scope.items = items;

        $scope.ok = function () {
            $http.post('application/weight', $scope.items)
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
