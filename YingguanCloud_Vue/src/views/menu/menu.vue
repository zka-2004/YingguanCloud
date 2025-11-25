
<template>
  <div class="app-container">
    <!-- 添加工具条 -->
    <div class="tools-div">
      <el-button type="success" icon="el-icon-plus" size="mini" @click="add()">添 加</el-button>
    </div>
    <!-- 菜单数据列表 -->
    <el-table
      v-loading="listLoading"
      :data="sysMenuList"
      style="width: 100%;margin-bottom: 20px;margin-top: 10px;"
      row-key="id"
      border
      :default-expand-all="false"
      :tree-props="{children: 'children'}">

      <el-table-column prop="name" label="菜单名称" width="160" align="center" />
      <el-table-column label="图标" align="center">
        <template slot-scope="scope">
          <i :class="scope.row.icon"></i>
        </template>
      </el-table-column>
      <el-table-column prop="perms" label="权限标识" width="160" align="center"/>
      <el-table-column prop="path" label="路由地址" width="120" align="center"/>
      <el-table-column prop="component" label="组件路径" width="160" align="center"/>
      <el-table-column prop="sortValue" label="排序" width="60" align="center"/>
      <el-table-column label="状态" width="80" align="center">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status === 1" disabled="true">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" align="center"/>
      <el-table-column align="center" label="操作" width="180" fixed="right"  >
        <template slot-scope="scope">
          <el-button type="success" v-if="scope.row.type !== 2" icon="el-icon-plus" size="mini" @click="add(scope.row)" title="添加下级节点"/>
          <el-button type="primary" icon="el-icon-edit" size="mini" @click="edit(scope.row)" title="修改"/>
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeDataById(scope.row.id)" title="删除" :disabled="scope.row.children.length > 0"/>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加/修改菜单对话框 -->
     <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="40%" >
      <el-form
      ref="dataForm"
      :model="sysMenu"
      :rules="rules"
      label-width="150px"
      size="small"
      style="padding-right: 40px;">
        <el-form-item label="上级部门" v-if="sysMenu.id === '' ">
        <el-input v-model="sysMenu.parentName" disabled="true"/>
        </el-form-item>
        <el-form-item label="菜单类型" prop="type">
          <el-radio-group v-model="sysMenu.type" :disabled="typeDisabled">
            <el-radio :label="0" :disabled="type0Disabled">目录</el-radio>
            <el-radio :label="1" :disabled="type1Disabled">菜单</el-radio>
            <el-radio :label="2" :disabled="type2Disabled">按钮</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="菜单名称" prop="name">
          <el-input v-model="sysMenu.name"/>
        </el-form-item>
        <el-form-item label="图标" prop="icon" v-if="sysMenu.type !== 2">
          <el-select v-model="sysMenu.icon" clearable>
            <el-option v-for="item in iconList" :key="item.class" :label="item.class" :value="item.class">
            <span style="float: left;">
             <i :class="item.class"></i>  <!-- 如果动态显示图标，这里添加判断 -->
            </span>
              <span style="padding-left: 6px;">{{ item.class }}</span>
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="sysMenu.sortValue" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item prop="path">
              <span slot="label">
                <el-tooltip content="访问的路由地址，如：`sysUser`" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                路由地址
              </span>
          <el-input v-model="sysMenu.path" placeholder="请输入路由地址" />
        </el-form-item>
        <el-form-item prop="component" v-if="sysMenu.type !== 0">
              <span slot="label">
                <el-tooltip content="访问的组件路径，如：`system/user/index`，默认在`views`目录下" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                组件路径
              </span>
          <el-input v-model="sysMenu.component" placeholder="请输入组件路径" />
        </el-form-item>
        <el-form-item v-if="sysMenu.type === 2">
          <el-input v-model="sysMenu.perms" placeholder="请输入权限标识" maxlength="100" />
          <span slot="label">
                <el-tooltip content="控制器中定义的权限字符，如：@PreAuthorize(hasAuthority('bnt.sysRole.list'))" placement="top">
                <i class="el-icon-question"></i>
                </el-tooltip>
                权限字符
              </span>
        </el-form-item>
        <el-form-item label="状态" prop="type">
          <el-radio-group v-model="sysMenu.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small" icon="el-icon-refresh-right">取 消</el-button>
        <el-button type="primary" icon="el-icon-check" @click="saveOrUpdate()" size="small">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import api from '@/api/menu/menu.js'
// 定义菜单默认数据
const defaultForm = {
  id: '',
  parentId: '',
  name: '',
  type: 0,
  path: '',
  component: '',
  perms: '',
  icon: '',
  sortValue: 1,
  status: 1
}

export default {
  data() {
    return {
      // 定义加载显示变量
      listLoading: true,
      // 定义菜单列表数据变量
      sysMenuList: [],
      // 对话框显示/隐藏变量
      dialogVisible: false,
      // 菜单对象
      sysMenu:defaultForm,
      // 类型(0:目录,1:菜单,2:按钮) 禁用变量
      typeDisabled:false,
      //0:目录禁用变量 一级菜单
      type0Disabled:false,
      //1:菜单禁用变量 二级菜单
      type1Disabled:false,
      //2:按钮禁用变量 三级菜单
      type2Disabled:false,
      //数据验证规则
      rules: {
        name: [
          { required: true, message: '请输入菜单名称', trigger: 'blur' }
        ],
        path: [
          { required: true, message: '请输入路由地址', trigger: 'blur' }
        ],
        component: [
          { required: true, message: '请输入组件路径', trigger: 'blur' }
        ]
      },
      // 图标数据
      iconList: [
        {
          class: "el-icon-s-tools",
        },
        {
          class: "el-icon-s-custom",
        },
        {
          class: "el-icon-setting",
        },
        {
          class: "el-icon-user-solid",
        },
        {
          class: "el-icon-s-help",
        },
        {
          class: "el-icon-phone",
        },
        {
          class: "el-icon-s-unfold",
        },
        {
          class: "el-icon-s-operation",
        },
        {
          class: "el-icon-more-outline",
        },
        {
          class: "el-icon-s-check",
        },
        {
          class: "el-icon-tickets",
        },
        {
          class: "el-icon-s-goods",
        },
        {
          class: "el-icon-document-remove",
        },
        {
          class: "el-icon-warning",
        },
        {
          class: "el-icon-warning-outline",
        },
        {
          class: "el-icon-question",
        },
        {
          class: "el-icon-info",
        }
      ]
    }
  },
  methods: {
    //获取菜单数据
    getList() {
      api.findList().then((res) => {
        // console.log(res);
        if(res.code === 200){
          this.listLoading = false
          this.sysMenuList = res.data
        }
      })
    },
    //添加菜单按钮事件
    add(row){
      console.log();

      // alert('添加菜单')
      // 启用添加菜单类型
      this.typeDisabled = false ;
      this.dialogVisible = true;
      //清空旧数据
      this.sysMenu = Object.assign({},defaultForm);
      this.sysMenu.id= ''
      // 判断添加菜单类型
      if(row===undefined){
        this.dialogTitle = "添加一级菜单"
        console.log(this.sysMenu);

        this.type0Disabled = false;
        this.type1Disabled = true;
        this.type2Disabled = true;
      }else if(row.type === 0){
        this.dialogTitle = "添加二级菜单"
        // 设置父级菜单id
        // console.log(row);
        this.sysMenu.parentId = row.id
        // 设置父级菜单名称
        this.sysMenu.parentName = row.name
        this.sysMenu.type = 1
        this.type0Disabled = true;
        this.type1Disabled = false;
        this.type2Disabled = true;
      }else if(row.type === 1){
        this.dialogTitle = "添加三级菜单"
        this.sysMenu.parentId = row.id
        // 设置父级菜单名称
        this.sysMenu.parentName = row.name
        this.sysMenu.type = 2
        this.type0Disabled = true;
        this.type1Disabled = true;
        this.type2Disabled = false;
      }
    },
    //修改或添加菜单按钮事件
    saveOrUpdate() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          if (this.sysMenu.id != '') {
            console.log(this.sysMenu.id);
            //修改方法
            // alert("修改菜单")
            this.updateMenu();
          } else {
            //添加方法
            this.addMenu();
          }
        } else {
          this.$message.error("请填完整的菜单信息！")
          return false
        }
      })
    },
    //添加菜单具体方法
    addMenu(){
      // alert("添加菜单")
      // 1.发送请求
      api.addMenu(this.sysMenu).then((res) => {
        // console.log(res);
        // 2.处理响应数据
        if (res.code === 200) {
          //添加成功关闭弹框
          this.dialogVisible = false;
          //反馈
          this.$message.success("添加菜单成功！")
          //刷新数据
          this.getList();
        }
      })
    },
    //修改按钮事件
    edit(row){
      // alert("修改")
      // api.getMenuById(id).then((res) => {
      //   // console.log(res);
      //   this.sysMenu = res.data
      //   //打开修改弹框
      //   this.dialogVisible = true
      // })
      this.dialogTitle = '修改菜单'
      this.dialogVisible = true
      this.sysMenu = Object.assign({}, row)
      // 禁用修改菜单类型
      this.typeDisabled = true
    },
    //修改菜单具体方法
    updateMenu(){
      // alert("修改菜单")
      // 1.发送请求
      api.updateUserById(this.sysMenu).then((res) => {
        console.log(res);
        if (res.code === 200) {
          //修改成功关闭弹框
          this.dialogVisible = false;
          //反馈
          this.$message.success("修改菜单成功！")
          //刷新数据
          this.getList();
        }
      })

      // 2.处理响应数据
      // 3.给出反馈
      // 4.关闭弹框
      // 5.刷新数据
    },
    //删除按钮事件
    removeDataById(id){
      // alert("删除")
      this.$confirm('确定删除该菜单吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        //1.发送请求
        api.removeById(id).then((res) => {
          console.log(res);
          //2.处理响应数据
          if (res.code === 200) {
            //3.给出反馈
            this.$message.success("删除成功！")
            // 4.刷新数据
            this.getList();
          }
        })
      })
    }
  },
  created() {
    this.getList()
  },
}
</script>

<style lang="scss" scoped>

</style>
