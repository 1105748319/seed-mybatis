Date.prototype.format = function (fmt) {
    var o = {
        "M+": this.getMonth() + 1,                 //月份
        "d+": this.getDate(),                    //日
        "h+": this.getHours(),                   //小时
        "m+": this.getMinutes(),                 //分
        "s+": this.getSeconds(),                 //秒
        "q+": Math.floor((this.getMonth() + 3) / 3), //季度
        "S": this.getMilliseconds()             //毫秒
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
    }
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        }
    }
    return fmt;
};
var report = new Vue({
    el: '#report',
    data: function () {
        return {
            collect: {
                fi: '',
                createdBy: '',
                productType: '',
                group: '1',
                orderStatus: ''
            },
            typeOptions: [
                {value: '', label: 'ALL'},
                {value: 'BAGGAGE', label: 'BAGGAGE'},
                {value: 'SEAT', label: 'SEAT'},
                {value: 'BAGGAGE(OTHERS)', label: 'BAGGAGE(OTHERS)'},
                {value: 'OTHERS', label: 'OTHERS'}
            ],
            groupOptions: [
                {value: '1', label: 'Group by Staff ID'},
                {value: '2', label: 'Group by Products'}
            ],
            statusOptions: [
                {value: '', label: 'All'},
                {value: 'N', label: 'N'},
                {value: 'Y', label: 'Y'},
                {value: 'Refund', label: 'Refund'},
                {value: 'C', label: 'C'}
            ],
            collectData: [],

            person: {
                orderNum: '',
                fi: '',
                name: '',
                licenseNo: '',
                type: '',
                createdBy: ''
            },
            personOptions: [
                {value: '', label: 'All'},
                {value: 'N', label: 'N'},
                {value: 'Y', label: 'Y'},
                {value: 'Refund', label: 'Refund'},
                {value: 'C', label: 'C'}
            ],
            personData: [],

            flight: {
                orderNum: '',
                fiDate: '',
                status: ''
            },
            flightOptions: [
                {value: '', label: 'All'},
                {value: 'N', label: 'N'},
                {value: 'Y', label: 'Y'},
                {value: 'Refund', label: 'Refund'},
                {value: 'C', label: 'C'}
            ],
            flightInfo: [],
            flightSubtotal: [],
            flightTotal: [],
            rowData: {},//列表选中项
            showCreatedBy: true,
            activeName: 'person',
            isCki: false

        }
    },


    methods: {
        //查寻订单明细
        altM: function () {
            var _this = this;
            shortcut.remove("enter");
            _this.$refs.person.setCurrentRow(_this.$refs.person.data[0]);
            _this.rowData = _this.$refs.person.data[0];
            shortcut.add("down", _this.MDown, {});//改变输入框状态person
            shortcut.add("enter", _this.queryDetail, {});

        },
        MDown: function () {
            var _this = this;
            for (var i = 0; i < _this.$refs.person.data.length; i++) {
                if (_this.rowData === _this.$refs.person.data[i]) {
                    if (i < _this.$refs.person.data.length - 1) {
                        _this.$refs.person.setCurrentRow(_this.$refs.person.data[i + 1]);
                        _this.rowData = _this.$refs.person.data[i + 1];
                        return;
                    } else {
                        _this.$refs.person.setCurrentRow(_this.$refs.person.data[0]);
                        _this.rowData = _this.$refs.person.data[0];
                        i = 0;
                        return;
                    }

                }
            }

        },
        winClose: function () {
            window.location.href = "about:blank";
            window.close();
        },
        queryFlight: function () {
            var params = {};
            params.orderNum = this.flight.orderNum;
            params.fiDate = this.flight.fiDate;
            params.status = this.flight.status;
            $.post("/charge/report/queryListFlight", params, function (data) {
                report.flightInfo = data.data.flightInfo;
                report.flightSubtotal = data.data.flightSubtotal;
                report.flightTotal = data.data.flightTotal;
            });
        },
        printFlight: function () {
            var orderNum = this.flight.orderNum;
            var fiDate = this.flight.fiDate;
            var status = this.flight.status;

            window.open("/charge/report/printFlight?orderNum=" + orderNum + "&fiDate=" + fiDate + "&status=" + status,
                "", "modal=yes,width=900,height=600,resizable=no,scrollbars=no");
        },
        exportFlight: function () {
            var orderNum = this.flight.orderNum;
            var fiDate = this.flight.fiDate;
            var status = this.flight.status;

            window.location.href = "/charge/report/exportListFlight?orderNum=" +
                orderNum + "&fiDate=" + fiDate + "&status=" + status;
        },
        formatDate: function (row, column) {
            return new Date(row.createdDt).format("yyyy-MM-dd hh:mm:ss");
        },
        queryListPerson: function () {
            var params = {};
            params.orderNum = this.person.orderNum;
            params.fi = this.person.fi;
            params.name = this.person.name;
            params.licenseNo = this.person.licenseNo;
            params.type = this.person.type;
            params.createdBy = this.person.createdBy;

            $.post("/charge/report/queryListPerson", params, function (data) {

                report.personData = data.data.personData;
                if (data.data.showPage == '3') {
                    report.activeName = 'person';
                }
                report.rowData = {};    //查询之后选中行的状态消失了，将临时数据清空
            });
        },
        queryDetail: function () {
            shortcut.remove("enter");
            if (this.rowData.id == undefined || this.rowData.id == null || this.rowData.id == '') {
                this.$message.warning("please choose one!");
                return;
            }
            var name = $("#name").val();
            var orderNum = this.rowData.orderNum;
            if (this.isCki) {
                $("#report").load("http://" + window.location.host + "/cki/orderConfirm/orderConfirmation?name=" + name, function () {
                    pane.chargeNum = orderNum;
                    pane.selectOrder();
                });
            } else {
                $(".content-wrapper").load("/charge/orderDetail/orderConfirmation", function () {
                    pane.chargeNum = orderNum;
                    pane.selectOrder();
                });
            }

        },
        printPerson: function () {
            var orderNum = this.person.orderNum;
            var fi = this.person.fi;
            var name = this.person.name;
            var licenseNo = this.person.licenseNo;
            var type = this.person.type;
            var createdBy = this.person.createdBy;

            window.open("/charge/report/printPerson?orderNum=" + orderNum + "&fi=" + fi + "&name=" + name
                + "&licenseNo=" + licenseNo + "&type=" + type + "&createdBy=" + createdBy,
                "", "modal=yes,width=900,height=600,resizable=no,scrollbars=no");
        },
        exportPerson: function () {
            var orderNum = this.person.orderNum;
            var fi = this.person.fi;
            var name = this.person.name;
            var licenseNo = this.person.licenseNo;
            var type = this.person.type;
            var createdBy = this.person.createdBy;

            window.location.href = "/charge/report/exportListPerson?orderNum=" + orderNum +
                "&fi=" + fi + "&name=" + name + "&licenseNo=" + licenseNo +
                "&type=" + type + "&createdBy=" + createdBy;

        },
        deleteOne: function () {
            if (this.rowData.id == undefined) {
                this.$message.warning("please choose one.");
                return;
            }
            var id = this.rowData.id;
            this.$confirm('Are you sure to delete this？', 'delete confirm', {
                confirmButtonText: 'Confirm',
                cancelButtonText: 'Cancel',
                type: 'warning'
            }).then(function () {
                $.post("/charge/report/deleteOnePerson", {id: id}, function (data) {
                    if (data.code == '200') {
                        report.$message.success("delete success.");
                        report.rowData = {}; //删除之后将保存临时选中行的数据清空
                        report.queryListPerson();
                    } else {
                        report.$message.error("delete error!");
                    }
                });
            }).catch(function () {//空function可去除没有捕捉到取消事件的报错

            });


        },
        rowClick: function (row, event, column) {
            this.rowData = row;
        },
        formatPayment: function (row, column) {
            var type = row.paymentType;
            if (type == '1') {
                return 'CASH';
            } else if (type == '2') {
                return 'POS';
            } else if (type == '3') {
                return 'POS(G)';
            }
        },
        formatStatus: function (row, column) {
            var status = row.type;
            if (status == 'Y') {
                return 'Y';
            } else if (status == 'N') {
                return 'N';
            } else if (status == 'Refund') {
                return 'Refund';
            } else if (status == 'C') {
                return 'C';
            }
        },
        queryListCollect: function () {
            var params = {};
            params.fi = this.collect.fi;
            params.createdBy = this.collect.createdBy;
            params.productType = this.collect.productType;
            params.group = this.collect.group;
            params.orderStatus = this.collect.orderStatus;
            if (params.group == '2') {
                this.showCreatedBy = false;
            } else {
                this.showCreatedBy = true;
            }

            $.post("/charge/report/queryListCollect", params, function (data) {
                report.collectData = data.data.collectData;
            });
        },
        printListCollect: function () {
            var fi = this.collect.fi;
            var createdBy = this.collect.createdBy;
            var productType = this.collect.productType;
            var group = this.collect.group;
            var orderStatus = this.collect.orderStatus;
            window.open("/charge/report/printCollect?fi=" + fi + "&createdBy=" + createdBy
                + "&productType=" + productType + "&group=" + group + "&orderStatus=" + orderStatus,
                "", "modal=yes,width=800,height=500,resizable=no,scrollbars=no");
        },
        exportListCollect: function () {
            var fi = this.collect.fi;
            var createdBy = this.collect.createdBy;
            var productType = this.collect.productType;
            var group = this.collect.group;
            var orderStatus = this.collect.orderStatus;

            window.location.href = "/charge/report/exportListCollect?fi=" + fi + "&createdBy=" +
                createdBy + "&productType=" + productType + "&group=" + group + "&orderStatus=" + orderStatus;
        },
        formatCollectMoney: function (row, column) {
            var money = new Number(row.groupMoney);
            return this.moneyFormat(money);
        },
        formatPersonMoney: function (row, column) {
            var money = new Number(row.totalMoney);
            return this.moneyFormat(money);
        },
        flightFormatMoney: function (money) {
            var mon = new Number(money);
            return this.moneyFormat(mon);
        },
        moneyFormat: function (money) {
            var tempMoney = money.toFixed(2);
            var tempArr = tempMoney.split('.');
            var temp = tempArr[0];
            var endNum = '.' + tempArr[1];
            var result = '';
            while (temp.length > 3) {
                result = ',' + temp.slice(-3) + result;
                temp = temp.slice(0, temp.length - 3);
            }
            if (temp) {
                result = temp + result;
            }
            result += endNum;

            return result;
        },
        getSummaries: function (param) {//收费统计
            var columns = param.columns;
            var data = param.data;
            var sums = [];
            var num = 0;
            var money = 0;
            for (var i = 0; i < data.length; i++) {
                num += data[i].productNum;
                money += data[i].groupMoney;
            }

            money = this.moneyFormat(money);
            this.totalNum = num;

            var showCreatedBy = this.showCreatedBy;
            columns.forEach(function (column, index) {
                var a = 2, b = 3, c = 4;
                //不展示创建人
                if (!showCreatedBy) {
                    a = 1;
                    b = 2;
                    c = 3;
                }

                if (index == a) {
                    sums[a] = 'total';
                    return;
                }
                if (index == b) {

                    sums[b] = num;
                    return;
                }
                if (index == c) {
                    sums[c] = money;
                    return;
                }
            });

            return sums;
        }
    },
    created: function () {

        var _this = this;
        shortcut.remove("alt+r");
        shortcut.remove("alt+c");
        shortcut.remove("alt+y");
        shortcut.remove("alt+d");

        shortcut.remove("alt+M");
        shortcut.remove("alt+e");
        shortcut.remove("alt+p");
        shortcut.remove("alt+q");
        shortcut.remove("Esc");


        if ($("#showPageNum").val() == '3') {
            _this.activeName = 'person';
            _this.isCki = true;
        }
        _this.queryListPerson();


        shortcut.add("alt+m", _this.altM, {});
        shortcut.add("alt+e", _this.exportPerson, {});
        shortcut.add("alt+p", _this.printPerson, {});
        shortcut.add("alt+q", _this.queryListPerson, {});
        shortcut.add("Esc", _this.winClose, {});

    }
});


function flightFormatMoney(money) {
    var mon = new Number(money);
    return report.moneyFormat(mon);
}


