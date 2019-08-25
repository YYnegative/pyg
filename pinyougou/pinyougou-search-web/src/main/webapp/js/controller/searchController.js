var app = new Vue({
    el: "#app",
    data: {
        //搜索条件
        searchMap:{"keywords":""},
        //返回结果
        resultMap:{}
    },
    methods:{
        //根据关键字搜索
        search:function () {
            axios.post("itemSearch/search.do", this.searchMap).then(function (response) {
                app.resultMap = response.data;
            });
        }
    },
    created(){
        this.search();
    }
});