/**
 * Created by lenovo on 2017-06-13.
 */
var pageTableBar = Vue.extend({
    props: {
        bind: '',
    },
    data: function () {
        return {}
    },
    template: [
        '<el-row>',
        '<slot></slot>',
        '</el-row>'
    ].join(''),
    methods:{
        reloadTarget:function(param){
            this.$refs[this.bind].gridReload();
        }
    }
});

Vue.component("czy-page-grid-bar", pageTableBar)