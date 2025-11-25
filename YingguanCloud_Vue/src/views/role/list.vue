<template>
  <div>
    <div class="search-div">
      <!-- 搜索栏 -->
      <el-form label-width="70px" size="small">
        <el-row>
          <el-col :span="24">
            <el-form-item label="角色名称">
              <el-input style="width: auto;" v-model="searchObj.roleName" placeholder="角色称名" clearable></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="display:flex">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="fetchData()">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetData">重置</el-button>
        </el-row>
      </el-form>

    </div>
    <!-- 添加工具条 -->
    <div class="tools-div">
      <el-button type="success" icon="el-icon-plus" size="mini" @click="add">添 加</el-button>
      <el-button type="danger" class="btn-add" size="mini" @click="batchRemove()">批量删除</el-button>
      <!-- 添加角色弹框 -->
      <el-dialog title="添加/修改" :visible.sync="dialogVisible" width="40%">
        <el-form
          ref="dataForm"
          :model="sysRole"
          :rules="rules"
          label-width="150px"
          size="small"
          style="padding-right: 40px;"
        >
          <el-form-item label="角色名称" prop="roleName">
            <el-input v-model="sysRole.roleName" />
          </el-form-item>
          <el-form-item label="角色编码" prop="roleCode">
            <el-input v-model="sysRole.roleCode" />
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
          <el-button @click="dialogVisible = false" size="small" icon="el-icon-refresh-right">取 消</el-button>
          <el-button type="primary" icon="el-icon-check" @click="saveOrUpdate()" size="small">确 定</el-button>
        </span>
      </el-dialog>
    </div>
    <!-- 数据列表 -->
    <el-table v-loading="listLoading" :data="list" stripe border
      style="width: 100%;margin-top: 10px;text-align: center;" @selection-change="handleSelectionChange">
      <!-- 复选框 -->
      <el-table-column width="40" type="selection" />

      <el-table-column label="序号" width="70" align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="roleName" label="角色名称" align="center" />
      <el-table-column prop="roleCode" label="角色编码" align="center" />
      <el-table-column prop="createTime" label="创建时间" width="160" align="center" />
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" size="mini" @click="edit(scope.row.id)" title="修改" />
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeDataById(scope.row.id)"
            slot="reference" title="删除" />
          <el-button type="warning" icon="el-icon-baseball" size="mini" @click="showAssignAuth(scope.row)" title="分配权限"/>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <el-pagination :current-page="page" :total="total" :page-size="limit" style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper" @current-change="fetchData" />
  </div>
</template>

<script>
import api from '@/api/role/role.js'

export default {
  data() {
    return {
      // 数据源
      list: [],
      // 定义加载显示变量
      listLoading: true,
      // 当前页码
      page: 1,
      // 每页显示条数
      limit: 4,
      // 查询变量
      searchObj: {},
      // 总记录数
      total: 0,
      //是否显示添加弹框变量
      dialogVisible: false,
      //角色对象
      sysRole: {},
      //定义存放复选对象数组
      SelectObjects: [],
      //定义存放批量删除数组
      idArr: [],
      // 表单验证规则
      rules: {
        roleName: [
          { required: true, message: '请输入角色名称', trigger: 'blur' }
        ],
        roleCode: [
          { required: true, message: '请输入角色编码', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    //分配权限按钮事件
    showAssignAuth(row){
      this.$router.push(`/system/assignAuth?id=${row.id}&roleName=${row.roleName}`)
    },
    //复选框对象数组
    handleSelectionChange(value) {
      this.SelectObjects = value
    },
    //批量删除角色方法
    batchRemove() {
      if (this.SelectObjects.length != 0) {
        this.$confirm('确定删除该角色吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          this.idArr = []
          for (var i = 0; i < this.SelectObjects.length; i++) {
            var id = this.SelectObjects[i].id
            this.idArr.push(id);
          }
          api.batchRemoveByIds(this.idArr).then((res) => {
            if (res.code===200) {
              this.$message.success('批量删除成功！');
              this.fetchData()
            } else {
              this.$message.error('删除失败，请重试～');
            }
            this.SelectObjects = [];
          })
        })
      } else {
        this.$message.error("没有选择任何数据，请先选择任意一条数据");
      }
    },
    //添加按钮事件
    add() {
      this.dialogVisible = true
      this.sysRole = {}
    },
    //添加或修改功能方法
    saveOrUpdate() {
      this.$refs.dataForm.validate((valid) => {//
        if (valid) {
          if (this.sysRole.id != null) {
            this.updateRole();
          } else {
            this.addRole();
          }
        } else {
          this.$message.warning('请完善表单信息后再提交');
          return false;
        }
      });
    },
    //添加的具体功能
    addRole() {
      api.addRole(this.sysRole).then((res) => {
        this.dialogVisible = false
        this.fetchData()
        this.$message.success("添加角色成功！");
      })
    },
    //根据ID回显角色
    edit(id) {
      api.getRoleById(id).then((res) => {
        this.sysRole = res.data
        this.dialogVisible = true
      })
    },
    //修改角色
    updateRole() {
      api.updateRole(this.sysRole).then((res) => {
        this.dialogVisible = false
        this.$message.success("修改角色成功！")
        this.fetchData()
      })
    },
    //删除角色
    removeDataById(id) {
      this.$confirm('确定删除该角色吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        api.removeById(id).then((res) => {
          console.log(res);
          this.$message.success("删除成功！")
        })
        this.fetchData();
      })
    },
    // 重置功能
    resetData() {
      this.searchObj = {}
      this.fetchData()
    },
    // 分页加条件获取角色列表
    fetchData(page = 1) {
      this.page = page
      api.list(this.page, this.limit, this.searchObj).then(res => {
        if (res != null) {
          this.listLoading = false
        }
        this.list = res.data.records
        this.total = res.data.total
      })
    }
  },
  created() {
    this.fetchData()
  }
}
</script>

<style lang="scss" scoped></style>
