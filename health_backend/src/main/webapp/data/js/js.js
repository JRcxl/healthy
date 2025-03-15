let f = $(function () {
    echarts_1();//本月到/(未)诊人数
    echarts_2();//年龄分布
    echarts_3();//性别
    echarts_4();//套餐占比
    echarts_5();//人员地址


    //本月到/(未)诊人数
    function echarts_1() {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart1'));
        axios.get("/report/getNumberReport.do").then((res) => {
            if (res.data.flag) {
                // console.log(JSON.stringify(res.data.data))
                //获取数据成功
                myChart.setOption(
                    {


                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'shadow'
                            }
                        },
                        grid: {
                            left: '0%',
                            top: '10px',
                            right: '0%',
                            bottom: '4%',
                            containLabel: true
                        },
                        xAxis: [{
                            type: 'category',
                            data: ['已到诊', '未到诊'],
                            axisLine: {
                                show: true,
                                lineStyle: {
                                    color: "rgba(255,255,255,.1)",
                                    width: 1,
                                    type: "solid"
                                },
                            },

                            axisTick: {
                                show: false,
                            },
                            axisLabel: {
                                interval: 0,
                                // rotate:50,
                                show: true,
                                splitNumber: 15,
                                textStyle: {
                                    color: "rgba(255,255,255,.6)",
                                    fontSize: '12',
                                },
                            },
                        }],
                        yAxis: [{
                            type: 'value',
                            axisLabel: {
                                //formatter: '{value} %'
                                show: true,
                                textStyle: {
                                    color: "rgba(255,255,255,.6)",
                                    fontSize: '12',
                                },
                            },
                            axisTick: {
                                show: false,
                            },
                            axisLine: {
                                show: true,
                                lineStyle: {
                                    color: "rgba(255,255,255,.1	)",
                                    width: 1,
                                    type: "solid"
                                },
                            },
                            splitLine: {
                                lineStyle: {
                                    color: "rgba(255,255,255,.1)",
                                }
                            }
                        }],
                        series: [
                            {
                                type: 'bar',
                                data: res.data.data,
                                barWidth: '35%', //柱子宽度
                                // barGap: 1, //柱子之间间距
                                itemStyle: {
                                    normal: {
                                        color: '#2f89cf',
                                        opacity: 1,
                                        barBorderRadius: 5,
                                    }
                                }
                            }

                        ]

















                        //
                        // tooltip: {
                        //     trigger: 'item',
                        //     formatter: "{a} <br/>{b} : {c} ({d}%)"//提示内容格式
                        // },
                        // series: [
                        //     {
                        //         name: 'Access From',
                        //         type: 'pie',
                        //         radius: '50%',
                        //         data:res.data.data,
                        //         emphasis: {
                        //             itemStyle: {
                        //                 shadowBlur: 10,
                        //                 shadowOffsetX: 0,
                        //                 shadowColor: 'rgba(0, 0, 0, 0.5)'
                        //             }
                        //         }
                        //     }
                        // ]


                    }
                );

            } else {
                //获取数据失败
                alert(res.data.message);
            }
        });


        // 使用刚指定的配置项和数据显示图表。
        window.addEventListener("resize", function () {
            myChart.resize();
        });
    }

    //年龄分布
    function echarts_2() {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart2'));


        axios.get("/report/getAge.do").then((res) => {
            if (res.data.flag) {
                //获取数据成功
                myChart.setOption(
                    {


                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {type: 'shadow'}
                        },
                        grid: {
                            left: '0%',
                            top: '10px',
                            right: '0%',
                            bottom: '4%',
                            containLabel: true
                        },
                        xAxis: [{
                            type: 'category',
                            data: res.data.data.name,
                            axisLine: {
                                show: true,
                                lineStyle: {
                                    color: "rgba(255,255,255,.1)",
                                    width: 1,
                                    type: "solid"
                                },
                            },

                            axisTick: {
                                show: false,
                            },
                            axisLabel: {
                                interval: 0,
                                // rotate:50,
                                show: true,
                                splitNumber: 15,
                                textStyle: {
                                    color: "rgba(255,255,255,.6)",
                                    fontSize: '12',
                                },
                            },
                        }],
                        yAxis: [{
                            type: 'value',
                            axisLabel: {
                                //formatter: '{value} %'
                                show: true,
                                textStyle: {
                                    color: "rgba(255,255,255,.6)",
                                    fontSize: '12',
                                },
                            },
                            axisTick: {
                                show: false,
                            },
                            axisLine: {
                                show: true,
                                lineStyle: {
                                    color: "rgba(255,255,255,.1	)",
                                    width: 1,
                                    type: "solid"
                                },
                            },
                            splitLine: {
                                lineStyle: {
                                    color: "rgba(255,255,255,.1)",
                                }
                            }
                        }],
                        series: [
                            {

                                type: 'bar',
                                data: res.data.data.value,
                                barWidth: '35%', //柱子宽度
                                // barGap: 1, //柱子之间间距
                                itemStyle: {
                                    normal: {
                                        color: '#27d08a',
                                        opacity: 1,
                                        barBorderRadius: 5,
                                    }
                                }
                            }

                        ]





                    }
                );

            } else {
                //获取数据失败
                alert(res.data.message);
            }
        });


        // 使用刚指定的配置项和数据显示图表。
        window.addEventListener("resize", function () {
            myChart.resize();
        });
    }

    //性别
    function echarts_3() {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart3'));


        //性别分布
        var labelFromatter = {
            normal: {
                label: {
                    position: 'center',
                    formatter: function (params) {
                        console.log(params)
                        if (params.name == "女性") {
                            return "女性" + ":" + (params.percent + '%')
                        } else {
                            return "男性" + ":" + (params.percent + '%')
                        }
                    },
                },
                labelLine: {
                    show: false
                }
            },
        };

        axios.get("/report/getSexReport.do").then((res) => {
            if (res.data.flag) {
                // console.log(JSON.stringify(res.data.data))
                //获取数据成功
                myChart.setOption(
                    {


                        color: ['#87cefa', '#FD6C88'],
                        tooltip: {
                            trigger: 'item',
                            formatter: "{b}({c})<br/>{d}%"
                        },

                        series: [
                            {
                                type: 'pie',
                                center: ['50%', '50%'],
                                radius: [55, 95],
                                x: '0%', // for funnel
                                itemStyle: labelFromatter,
                                data: [
                                    {name: '男性', value: res.data.data[1]},
                                    {name: '女性', value: res.data.data[0]},
                                ],
                            },
                        ],


                    }
                );

            } else {
                //获取数据失败
                alert(res.data.message);
            }
        });


        // 使用刚指定的配置项和数据显示图表。
        window.addEventListener("resize", function () {
            myChart.resize();
        });
    }


    //套餐占比
    function echarts_5() {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart5'));

        axios.get("/report/getAddress.do").then((res) => {
            if (res.data.flag) {
                //获取数据成功
                myChart.setOption(
                    {


                        tooltip: {
                            trigger: 'axis',
                            axisPointer: {
                                type: 'shadow'
                            }
                        },

                        grid: {
                            left: '0%',
                            top: '10px',
                            right: '0%',
                            bottom: '2%',
                            containLabel: true
                        },
                        xAxis: [{
                            type: 'category',
                            data: res.data.data.address,
                            axisLine: {
                                show: true,
                                lineStyle: {
                                    color: "rgba(255,255,255,.1)",
                                    width: 1,
                                    type: "solid"
                                },
                            },

                            axisTick: {
                                show: false,
                            },
                            axisLabel: {
                                interval: 0,
                                // rotate:50,
                                show: true,
                                splitNumber: 15,
                                textStyle: {
                                    color: "rgba(255,255,255,.6)",
                                    fontSize: '12',
                                },
                            },
                        }],
                        yAxis: [{
                            type: 'value',
                            axisLabel: {
                                //formatter: '{value} %'
                                show: true,
                                textStyle: {
                                    color: "rgba(255,255,255,.6)",
                                    fontSize: '12',
                                },
                            },
                            axisTick: {
                                show: false,
                            },
                            axisLine: {
                                show: true,
                                lineStyle: {
                                    color: "rgba(255,255,255,.1	)",
                                    width: 1,
                                    type: "solid"
                                },
                            },
                            splitLine: {
                                lineStyle: {
                                    color: "rgba(255,255,255,.1)",
                                }
                            }
                        }],
                        series: [{
                            type: 'bar',
                            data: res.data.data.value,
                            barWidth: '35%', //柱子宽度
                            // barGap: 1, //柱子之间间距
                            itemStyle: {
                                normal: {
                                    color: '#2f89cf',
                                    opacity: 1,
                                    barBorderRadius: 5,
                                }
                            }
                        }
                        ]


                    }
                );

            } else {
                //获取数据失败
                alert(res.data.message);
            }
        });


        // 使用刚指定的配置项和数据显示图表。
        window.addEventListener("resize", function () {
            myChart.resize();
        });
    }

    //人员地址
    function echarts_4() {
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('echart4'));

        axios.get("/report/getSetmealReports.do").then((res) => {
            if (res.data.flag) {
                // console.log(JSON.stringify(res.data.data))
                //获取数据成功
                myChart.setOption(
                    {


                        tooltip: {
                            trigger: 'item',
                            formatter: "{a} <br/>{b} : {c} ({d}%)"//提示内容格式
                        },
                        series: [
                            {
                                name: 'Access From',
                                type: 'pie',
                                radius: '50%',
                                data: res.data.data,
                                emphasis: {
                                    itemStyle: {
                                        shadowBlur: 10,
                                        shadowOffsetX: 0,
                                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                                    }
                                }
                            }
                        ]


                    }
                );

            } else {
                //获取数据失败
                alert(res.data.message);
            }
        });

        // 使用刚指定的配置项和数据显示图表。
        // myChart.setOption(option);
        window.addEventListener("resize", function () {
            myChart.resize();
        });
    }


   window.setInterval(echarts_1,5000);
   window.setInterval(echarts_2,5000);
    window.setInterval(echarts_3,5000);
   window.setInterval(echarts_4,5000);
    window.setInterval(echarts_5,5000);
})



		
		
		


		









