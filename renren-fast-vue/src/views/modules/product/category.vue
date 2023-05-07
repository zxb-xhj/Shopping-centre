<template>
  <div>

    <el-switch v-model="draggable" active-text="开启拖拽" inactive-text="关闭拖拽"></el-switch>
    <el-button v-if="draggable" @click="batchSave">批量保存</el-button>
    <el-button type="danger" plain @click="batchDelete()">批量删除</el-button>
    <el-container style="height: 600px;margin-top: 10px;">
      <el-aside style="width: 100%;">
        <el-tree :data="menus" :props="defaultProps" @node-click="handleNodeClick" :expand-on-click-node="false"
          show-checkbox node-key="catId" :default-expanded-keys="expandedkeys" draggable :allow-drop="allowDrop"
          @node-drop="handleDrop" ref="menuTree">
          <span class="custom-tree-node" slot-scope="{ node, data }">
            <span>{{ node.label }}</span>
            <span>
              <el-button v-if="data.catLevel < 3" type="text" size="mini" @click="() => append(data)">
                Append
              </el-button>
              <el-button type="text" size="mini" @click="() => enit(data)">
                edit
              </el-button>
              <el-button v-if="data.children == null" type="text" size="mini" @click="() => remove(node, data)">
                Delete
              </el-button>
            </span>
          </span></el-tree>
        <el-dialog :title="title" :visible.sync="dialogVisible" width="30%">
          <el-form :model="category">
            <el-form-item label="菜单名称">
              <el-input v-model="category.name" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="菜单图标">
              <el-input v-model="category.icon" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="计量单位">
              <el-input v-model="category.productUnit" autocomplete="off"></el-input>
            </el-form-item>
          </el-form>
          <span slot="footer" class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="showCategory">确 定</el-button>
          </span>
        </el-dialog>
      </el-aside>
    </el-container>
  </div>
</template>

<script>
//这里可以导入其他文件（比如：组件，工具 js，第三方插件 js，json文件，图片文件等等）
//例如：import 《组件名称》 from '《组件路径》';

export default {
  //import 引入的组件需要注入到对象中才能使用
  components: {},
  props: {},
  data() {
    return {
      draggable: false,
      pCid: [],
      menus: [],
      maxlevel: 0,
      title: "",
      updateNodes: [],
      defaultProps: {
        children: "children",
        label: "name",
      },
      expandedkeys: [],
      dialogVisible: false,
      category: {
        name: "",
        parentCid: 0,
        catLevel: 0,
        showStatus: 1,
        sort: 0,
        catId: null,
        icon: null,
        productUnit: null,
        productCount: 0,
      },
      type: "",
    };
  },
  methods: {
    handleNodeClick(data) {
      console.log(data);
    },
    showCategory() {
      if (this.type == "addCategory") {
        this.addCategory();
      } else if (this.type == "enitCategory") {
        this.enitCategory();
      }
    },
    //菜单顺序修改
    batchSave() {
      this.$http({
        url: this.$http.adornUrl("/product/category/update/sort"),
        method: "post",
        data: this.$http.adornData(this.updateNodes, false)
      }).then(({ data }) => {
        this.$message({
          message: "菜单顺序等修改成功",
          type: "success"
        });
        //刷新出新的菜单
        this.getCategory();
        //设置需要默认展开的菜单
        this.expandedKey = this.pCid;
        this.updateNodes = [];
        this.maxLevel = 0;
        // this.pCid = 0;
      });
    },
    //修改菜单
    enitCategory() {
      var { catId, name, icon, productUnit } = this.category;
      this.$http({
        url: this.$http.adornUrl('/product/category/update'),
        method: 'post',
        data: this.$http.adornData({ catId, name, icon, productUnit }, false)
      }).then(({ data }) => {
        this.$message({
          showClose: true,
          message: "修改菜单成功！",
          type: "success",
        });
      });
      this.dialogVisible = false;
      this.getCategory();
      this.expandedkeys = [this.category.parentCid];
    },
    //批量删除
    batchDelete() {
      let catIds = [];
      let names = [];
      let checkedNodes = this.$refs.menuTree.getCheckedNodes();
      checkedNodes.forEach(checkedNode => {
        catIds.push(checkedNode.catId);
        names.push(checkedNode.name);
      });
      this.$confirm("是否删除【" + names + "】菜单?", '提示', {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl('/product/category/delete'),
          method: 'post',
          data: this.$http.adornData(catIds, false)
        }).then(({ data }) => {
          this.$message({
            showClose: true,
            message: "批量删除菜单成功！",
            type: "success",
          });
          this.getCategory();
          this.expandedkeys = [this.category.parentCid];
        });
      }).catch(() => { })
    },
    //获取所有菜单数据
    getCategory() {
      this.$http({
        url: this.$http.adornUrl("/product/category/list/tree"),
        method: "get",
      }).then(({ data }) => {
        this.menus = data.data;
        // console.log(data.data);
      });
    },
    // 添加菜单分类
    append(data) {
      this.dialogVisible = true;
      this.title = "添加菜单分类";
      this.$http({
        url: this.$http.adornUrl('/product/category/info/' + data.catId),
        method: 'get'
      }).then(({ data }) => {
        this.category.name = null
        this.category.parentCid = data.category.catId;
        this.category.catId = null;
        this.category.productUnit = null;
        this.category.catLevel = data.category.catLevel * 1 + 1;
        this.category.productCount = 0;
        this.category.showStatus = 1,
          this.category.icon = null,
          this.type = "addCategory";
        // this.dialogVisible = false;
      })
    },
    // 修改菜单分类
    enit(data) {
      this.dialogVisible = true;
      this.title = "修改菜单分类";
      this.$http({
        url: this.$http.adornUrl('/product/category/info/' + data.catId),
        method: 'get',
      }).then(({ data }) => {
        this.category.catId = data.category.catId;
        this.category.name = data.category.name;
        this.category.icon = data.category.icon;
        this.category.productUnit = data.category.productUnit;
        this.type = "enitCategory";
      })
    },
    // 删除菜单
    remove(node, data) {
      let id = [data.catId];
      this.$confirm("是否删除【" + data.name + "】菜单?", '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.$http({
            url: this.$http.adornUrl("/product/category/delete"),
            method: "post",
            data: this.$http.adornData(id, false),
          }).then(({ data }) => {
            this.$message({
              showClose: true,
              message: "删除成功！",
              type: "success",
            });
            this.getCategory();
            this.expandedkeys = [node.parent.data.catId];
          });
        })
        .catch(() => { });
    },
    // 添加菜单分类
    addCategory() {
      console.log(this.category);
      this.$http({
        url: this.$http.adornUrl("/product/category/save"),
        method: "post",
        data: this.$http.adornData(this.category, false),
      }).then(({ data }) => {
        this.$message({
          showClose: true,
          message: "添加菜单分类成功！",
          type: "success",
        });
        this.dialogVisible = false;
        this.getCategory();
        this.expandedkeys = [this.category.parentCid];
      });
    },
    // 拖拽功能
    handleDrop(draggingNode, dropNode, dropType, ev) {
      console.log("handleDrop: ", draggingNode, dropNode, dropType);
      // 1、当前节点最新的父节点id
      let pCid = 0;
      let siblings = null;
      if (dropType == "before" || dropType == "after") {
        pCid =
          dropNode.parent.data.catId == undefined
            ? 0
            : dropNode.parent.data.catId;
        siblings = dropNode.parent.childNodes;
      } else {
        pCid = dropNode.data.catId;
        siblings = dropNode.childNodes;
      }
      this.pCid.push(pCid);

      // 2、当前拖拽节点的最新顺序，
      for (let i = 0; i < siblings.length; i++) {
        if (siblings[i].data.catId == draggingNode.data.catId) {
          // 如果遍历的是当前正在拖拽的节点
          let catLevel = draggingNode.level;
          if (siblings[i].level != draggingNode.level) {
            // 当前节点的层级发生变化
            catLevel = siblings[i].level;
            // 修改他子节点的层级
            this.updateChildNodeLevel(siblings[i]);
          }
          this.updateNodes.push({
            catId: siblings[i].data.catId,
            sort: i,
            parentCid: pCid,
            catLevel: catLevel
          });
        } else {
          this.updateNodes.push({ catId: siblings[i].data.catId, sort: i });
        }
      }

      // 3、当前拖拽节点的最新层级
      console.log("updateNodes", this.updateNodes);
    },
    updateChildNodeLevel(node) {
      if (node.childNodes.length > 0) {
        for (let i = 0; i < node.childNodes.length; i++) {
          var cNode = node.childNodes[i].data;
          this.updateNodes.push({
            catId: cNode.catId,
            catLevel: node.childNodes[i].level
          });
          this.updateChildNodeLevel(node.childNodes[i]);
        }
      }
    },
    // 被拖动的当前节点总层数
    allowDrop(draggingNode, dropNode, type) {
      this.maxlevel = draggingNode.data.catLevel;
      this.countNodeLevel(draggingNode.data);
      let catlevel = this.maxlevel - draggingNode.data.catLevel + 1;
      if (type == "inner") {
        return catlevel + dropNode.level <= 3;
      } else {
        return catlevel + dropNode.parent.level <= 3;
      }
    },
    // 找到所有子节点，求出最大深度
    countNodeLevel(node) {
      if (node.children != null && node.children.length > 0) {
        for (let i = 0; i < node.children.length; i++) {
          if (node.children[i].catLevel > this.maxlevel) {
            this.maxlevel = node.children[i].catLevel;
          }
          this.countNodeLevel(node.children[i]);
        }
      }
      console.log(node);
    },
  },
  // 计算属性 类似于 data 概念
  computed: {},
  // 监控 data 中的数据变化
  watch: {},
  // 生命周期 - 创建完成（可以访问当前 this 实例）
   () {
    this.getCategory();
  },
  // 生命周期 - 挂载完成（可以访问 DOM 元素）
  mounted() { },
  beforeCreate() { }, // 生命周期 - 创建之前
  beforeMount() { }, // 生命周期 - 挂载之前
  beforeUpdate() { }, // 生命周期 - 更新之前
  updated() { }, // 生命周期 - 更新之后
  beforeDestroy() { }, // 生命周期 - 销毁之前
  destroyed() { }, // 生命周期 - 销毁完成
  activated() { }, // 如果页面有 keep-alive 缓存功能，这个函数会触发
};
</script>
<style scoped></style>
