<template>
    <el-container style="height: 600px;">
        <el-aside>
            <el-tree :data="menus" :props="defaultProps" node-key="catId" ref="menuTree" @node-click="nodeclick">

            </el-tree>
        </el-aside>
    </el-container>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具 js，第三方插件 js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';

export default {
    //import 引入的组件需要注入到对象中才能使用
    components: {},
    props: {},
    data() {
        //这里存放数据
        return {
            menus: [],
            defaultProps: {
                children: "children",
                label: "name",
            },
            expandedkeys: [],
        };
    },
    //计算属性 类似于 data 概念
    computed: {},
    //监控 data 中的数据变化
    watch: {},
    //方法集合
    methods: {
        //获取所有菜单数据
        getCategory() {
            this.$http({
                url: this.$http.adornUrl("/product/category/list/tree"),
                method: "get",
            }).then(({ data }) => {
                this.menus = data.data;
            });
        },
        nodeclick(data, node, component) {
            console.log("子：", data, node, component);
            this.$emit("tree-node-click", data, node, component);
        },
    },
    //生命周期 - 创建完成（可以访问当前 this 实例）
    created() {
        this.getCategory();
    },
    //生命周期 - 挂载完成（可以访问 DOM 元素）
    mounted() {

    },
    beforeCreate() { }, //生命周期 - 创建之前
    beforeMount() { }, //生命周期 - 挂载之前
    beforeUpdate() { }, //生命周期 - 更新之前
    updated() { }, //生命周期 - 更新之后
    beforeDestroy() { }, //生命周期 - 销毁之前
    destroyed() { }, //生命周期 - 销毁完成
    activated() { }, //如果页面有 keep-alive 缓存功能，这个函数会触发
}
</script>
<style scoped></style>