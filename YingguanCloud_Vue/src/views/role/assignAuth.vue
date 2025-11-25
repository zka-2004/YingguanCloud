<template>
  <div class="app-container">
    <div style="padding: 20px 20px 0 20px;">
      授权角色：{{ $route.query.roleName }}
    </div>
    <el-tree
      style="margin: 20px 0"
      ref="tree"
      :data="sysMenuList"
      node-key="id"
      show-checkbox
      default-expand-all
      :props="defaultProps"
    />
    <div style="padding: 20px 20px;">
      <el-button :loading="loading" type="primary" icon="el-icon-check" size="mini" @click="save">保存</el-button>
      <el-button @click="$router.push('/system/role')" size="mini" icon="el-icon-refresh-right">返回</el-button>
    </div>
  </div>
</template>

<script>
import api from '@/api/menu/menu.js'

export default {
  data(){
    return{
      // 提交请求加载
      loading:false,
      //权限菜单数据源
      sysMenuList:[],
      //
      defaultProps: {
        children: 'children',
        label: 'name'
      },
    }
  },
  methods: {
    //获取数据
    fetchMenu(){
      //1.发送请求
      api.toAssign(this.$route.query.id).then((res)=>{
        console.log(res);
        if(res.code===200){
          this.sysMenuList=res.data
          this.$refs.tree.setCheckedKeys(this.getCheckedIds(this.sysMenuList));
        }
      })
    },
    /*
      得到所有选中的id列表
    */
    getCheckedIds(auths, initArr = []) {
      return auths.reduce((pre, item) => {
        if (item.select && item.children.length === 0) {
          pre.push(item.id)
        } else if (item.children) {
          this.getCheckedIds(item.children, initArr)
        }
        return pre
      }, initArr)
    },
    //保存按钮事件
    save() {
        //获取到当前子节点及父节点 getCheckedNodes 若节点可被选择，则返回目前被选中的节点所组成的数组
        const allCheckedNodes = this.$refs.tree.getCheckedNodes(false, true);
        let MenuIds = allCheckedNodes.map(node => node.id);
        // console.log(MenuIds)
        let assignMenuVo = {
          roleId: this.$route.query.id,
          menuIdList: MenuIds
        }
        //打开提交请求加载
        this.loading = true
        api.doAssign(assignMenuVo).then(res => {
          this.loading = false
          if(res.code===200){
            this.$message.success('分配权限成功')
            this.$router.push('/system/role');
          }
        })
    }
  },

  created() {
    this.fetchMenu();
  },
}
</script>

<style lang="scss" scoped>

</style>
