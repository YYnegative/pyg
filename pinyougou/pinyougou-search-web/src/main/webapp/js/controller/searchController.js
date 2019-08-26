var app = new Vue({
    el: "#app",
    data: {
        //搜索条件
        searchMap:{"keywords":"","category":"","brand":"","spec":{}, "price":""},
        //返回结果
        resultMap:{}
    },
    methods:{
        //删除过滤条件
        removeSearchItem: function(key){
            if ("category" == key || "brand" == key || "price" == key) {
                //参数1：要设置的对象，参数2：设置的属性，参数3：属性对应的值
                this.$set(this.searchMap, key, "");
            } else {
                //参数1：要设置的对象，参数2：设置的属性，参数3：属性对应的值
                this.$set(this.searchMap.spec, key, null);//为了页面及时显示

                delete this.searchMap.spec[key];

            }

            this.search();
        },
        //添加过滤条件
        addSearchItem: function(key, value){
            if ("category" == key || "brand" == key || "price" == key) {
                //参数1：要设置的对象，参数2：设置的属性，参数3：属性对应的值
                this.$set(this.searchMap, key, value);
            } else {
                //参数1：要设置的对象，参数2：设置的属性，参数3：属性对应的值
                this.$set(this.searchMap.spec, key, value);
            }

            this.search();
        },
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