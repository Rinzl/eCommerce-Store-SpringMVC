$( document ).ready(function () {
    $.getJSON("/admin/dashboardInfo",function (data, status) {
        if (status === 'success') {
            // Distributed Series
            new Chartist.Bar('.sales5', {
                labels: Object.keys(data['salePerCategory']),
                series: Object.values(data['salePerCategory'])
            }, {
                distributeSeries: true,
                plugins: [
                    Chartist.plugins.tooltip()
                ],
                axisY: {
                    labelInterpolationFnc: function(value) {
                        return (value / 1);
                    },
                    scaleMinSpace: 55,
                    onlyInteger: true,
                },
                axisX: {
                    showGrid: false
                },
                chartPadding: {
                    top: 15,
                    right: 15,
                    bottom: 5,
                    left: 0
                }
            }).on('draw', function(data) {
                if (data.type === 'bar') {
                    data.element.attr({
                        style: 'stroke-width: 25px'
                    });
                }
            });

            var billStatusChart = c3.generate({
                bindto: '.status',
                data: {
                    columns: [
                        ['Chờ xác nhận', data['billStatus'][0]],
                        ['Đơn hủy', data['billStatus'][-1]],
                        ['Hoàn thành', data['billStatus'][1]],
                    ],

                    type: 'donut'
                },
                donut: {
                    label: {
                        show: false
                    },
                    title: "Trạng thái",
                    width: 35,

                },

                legend: {
                    hide: true
                    //or hide: 'data1'
                    //or hide: ['data1', 'data2']
                },
                color: {
                    pattern: ['#137eff','#5ac146','#8b5edd']
                }
            });
            // ==============================================================
            // Earnings
            // ==============================================================
            let seriesArrayThisYear = [];
            let seriesArrayLastYear = [];
            let thisYear = data['monthlySaleThisYear'];
            let lastYear = data['monthlySaleLastYear'];

            for(var i =1; i<=12;i++) {
                seriesArrayThisYear.push(thisYear[i]);
                seriesArrayLastYear.push(lastYear[i]);
            }
            new Chartist.Bar('.chart1', {
                labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                series: [
                    seriesArrayThisYear,
                    seriesArrayLastYear
                ]
            }, {
                stackBars: true,
                axisY: {
                    labelInterpolationFnc: function(value) {
                        return (value / 1);
                    },
                    scaleMinSpace: 55
                },
                axisX: {
                    showGrid: false
                },
                plugins: [
                    Chartist.plugins.tooltip()
                ],
                seriesBarDistance: 1,
                chartPadding: {
                    top: 15,
                    right: 15,
                    bottom: 5,
                    left: 0
                }
            }).on('draw', function(data) {
                if (data.type === 'bar') {
                    data.element.attr({
                        style: 'stroke-width: 25px'
                    });
                }
               });
        }
    });
});