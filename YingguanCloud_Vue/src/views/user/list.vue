<template>
  <div>
    <!-- 查询表单 -->
    <div class="search-div">
      <el-form label-width="70px" size="small">
        <el-row>
          <el-col :span="8">
            <el-form-item label="关 键 字">
              <el-input style="width: 60%" v-model="searchObj.keyword" clearable placeholder="用户名/姓名/手机号码"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="操作时间">
              <el-date-picker
              v-model="createTimes"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始时间"
                end-placeholder="结束时间"
                value-format="yyyy-MM-dd HH:mm:ss"
                style="margin-right: 10px;width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row style="display:flex">
          <el-button type="primary" icon="el-icon-search" size="mini" @click="fetchUserData()">搜索</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="resetData">重置</el-button>
        </el-row>
      </el-form>
    </div>
    <!-- 添加工具条 -->
    <div class="tools-div">
      <el-button type="success" icon="el-icon-plus" size="mini" @click="add">添 加</el-button>
      <el-button type="danger" class="btn-add" size="mini" @click="batchRemove()">批量删除</el-button>
      <!-- 添加用户对话框 -->
      <el-dialog
      title="添加/修改"
      :visible.sync="dialogVisible"
      width="40%"
      v-loading="loading" element-loading-text="头像上传中......"
      >
        <el-form
        ref="dataForm"
        :model="sysUser"
        label-width="100px" size="small"
        :rules="rules"
         style="padding-right: 40px;">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="sysUser.username" />
          </el-form-item>
          <el-form-item label="头像图片" prop="headUrl" >
            <img :src="sysUser.headUrl" width="100px" height="100px" v-if="isHeadUrl" />
          </el-form-item>
          <el-form-item  prop="image">
            <el-upload
              class="upload-demo"
              name="uploadImage"
              action="http://localhost:8080/admin/system/upload/uploadImage"
              :limit="1"
              :on-success="handleImageSuccess"
              :before-upload="handleBeforeUpload"
              :show-file-list="false"
            >
              <el-button size="small" type="primary">点击上传</el-button>
            </el-upload>
          </el-form-item>

          <el-form-item label="密码" prop="password" v-if="!sysUser.id">
            <el-input v-model="sysUser.password" />
          </el-form-item>
          <el-form-item label="姓名" prop="name">
            <el-input v-model="sysUser.name" />
          </el-form-item>
          <el-form-item label="手机" prop="phone">
            <el-input v-model="sysUser.phone" />
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
      <el-table-column prop="username" label="用户名" align="center" />
      <el-table-column prop="name" label="姓名" align="center"/>
      <el-table-column prop="phone" label="手机" align="center" />
      <el-table-column label="状态" width="80" align="center">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status === 1"
          @change="switchStatus(scope.row)">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" align="center" />
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <!-- {{scope.row}} -->
          <el-button type="primary" icon="el-icon-edit" size="mini" @click="edit(scope.row.id)" title="修改" />
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeDataById(scope.row.id)"
            slot="reference" title="删除" />
          <el-button type="warning" icon="el-icon-baseball" size="mini" @click="showAssignRole(scope.row)"title="分配角色"/>

          <!-- 分配角色对话框 -->
          <el-dialog title="分配角色" :visible.sync="dialogRoleVisible">
            <el-form label-width="80px">
              <el-form-item label="用户名">
                <el-input disabled :value="sysUser.username"></el-input>
              </el-form-item>
              <el-form-item label="角色列表">
                <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全      选</el-checkbox>
                <div style="margin: 15px 0;"></div>
                <el-checkbox-group v-model="userRoleIds" @change="handleCheckedChange">
                  <el-checkbox v-for=" role in allRoles" :key="role.id" :label="role.id">{{role.roleName}} </el-checkbox>
                </el-checkbox-group>
              </el-form-item>
            </el-form>
            <div slot="footer">
              <el-button type="primary" @click="assignRole" size="small">保存</el-button>
              <el-button @click="dialogRoleVisible = false" size="small">取消</el-button>
            </div>
          </el-dialog>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <el-pagination :current-page="page" :total="total" :page-size="limit" style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper" @current-change="fetchUserData" />
  </div>
</template>

<script>
import api from '@/api/user/user.js'
import roleApi from '@/api/role/role.js'

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
      //查询对象
      searchObj: {},
      //总记录数
      total: 0,
      //用户对象
      sysUser: {},
      //查询时间对象
      createTimes: [],
      //控制添加/修改对话框
      dialogVisible: false,
      //定义批量复选删除对象数组
      selectObjects: [],
      //定义批量删除ID数组
      IdArr: [],
      //控制分配角色对话框变量
      dialogRoleVisible:false,
      // 所有角色列表
      allRoles:[],
      userRoleIds: [], // 用户的角色ID的列表
      isIndeterminate: false, // 是否是不确定的
      checkAll: false ,// 是否全选
      //文件上传加载控制变量
      loading:false,
      //
      isHeadUrl:false,
      // 数据校验
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入姓名', trigger: 'blur' },
          { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
        ],
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
          { min: 11, max: 11, message: '请输入正确的手机号', trigger: 'blur' }
        ]
      },
    }
  },
  methods: {
    //分页获取用户列表
    fetchUserData(page = 1) {
      //改变当前页码
      this.page = page;
      this.searchObj.createTimeBegin=this.createTimes[0];
      this.searchObj.createTimeEnd=this.createTimes[1];
      //1.发送请求
      api.list(this.page, this.limit, this.searchObj).then((res) => {
        console.log(res);
        if (res.code === 200) {
          this.listLoading = false
          this.list = res.data.records
          this.total = res.data.total
        }
      })
    },
    //文件开始上传时加载
    handleBeforeUpload() {
      this.loading = true;
    },
    // 头像上传成功回调（核心修改：同步Vuex状态）
    handleImageSuccess(res, file) {
      this.loading = false;
      if (res.code === 200 && res.data) { // 确保接口返回成功且有图片地址
        this.isHeadUrl = true;
        this.sysUser.headUrl = res.data; // 赋值给当前表单
        this.$message.success("头像上传成功!");
      } else {
        this.$message.error("头像上传失败!");
      }
    },
    //重置按钮
    resetData() {
      //清空数据
      this.searchObj = {};
      this.createTimes = [];
      //刷新数据
      this.fetchUserData()
    },
    //修改状态事件
    switchStatus(row) {
      // alert(1)
      //1.修改状态值
      row.status=row.status === 1 ? 0 : 1;
      //2.传入修改状态值执行修改
      api.updateStatus(row.id, row.status).then((res)=>{
        // console.log(res);
        if(res.code===200){
          if(row.status===1){
            this.$message.success("该用户已启用！")
          }else{
            this.$message.warning("该用户已禁用！");
          }
        }

      })
    },
    //添加按钮事件
    add() {
      this.isHeadUrl=false;
      // alert(1)
      //1.打开对话框
      this.dialogVisible = true
      //清空上一次旧数据
      this.sysUser = {}
    },
    //添加用户或修改用户方法
    saveOrUpdate() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          if (this.sysUser.id != null) {
            //修改
            this.updateUser();
          } else {
            //添加
            this.addUser();
          }
        } else {
          this.$message.warning('请完善表单信息后再提交');
          return false;
        }
      })
    },
    //修改按钮事件及根据ID回显
    edit(id) {
      this.isHeadUrl=true;
      //1.发送回显请求
      api.getUserById(id).then((res) => {
        console.log(res);
        if (res.code === 200){
          //2.渲染数据
          this.sysUser = res.data
          //3.打开对话框
          this.dialogVisible = true
        } else {

        }

      })

    },
    //添加的具体方法
    addUser() {
      // alert("添加")
      //发送请求
      api.addUser(this.sysUser).then((res) => {
        // console.log(res);
        if (res.code === 200) {
          //添加成功关闭弹框
          this.dialogVisible = false;
          //反馈
          this.$message.success("添加用户成功！")
          //刷新数据
          this.fetchUserData();
        }

      })
    },
    //修改的具体方法
    updateUser() {
      // alert("修改")
      //发送请求
      api.updateUserById(this.sysUser).then((res) => {
        // console.log(res);
        if (res.code === 200) {
          // 关键：更新Vuex中的头像状态，触发左上角头像刷新
          // 加时间戳防止浏览器缓存旧图
          this.$store.commit('user/SET_AVATAR', this.sysUser.headUrl + '?t=' + Date.now());
          //添加成功关闭弹框
          this.dialogVisible = false;
          //反馈
          this.$message.success("修改用户成功！")
          //刷新数据
          this.fetchUserData();
        }
      })
    },
    //删除用户方法
    removeDataById(id) {
      //1.给出提示是否确定删除
      this.$confirm('确定删除该角色吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        //2.确定发送请求执行删除
        api.removeById(id).then((res) => {
          //3.给出反馈
          this.$message.success("删除成功！")
          //4.刷新列表
          this.fetchUserData()
        })
      })
    },
    //获取复选对象数组
    handleSelectionChange(value) {
      this.selectObjects = value
      // console.log(this.selectObjects);
    },
    //批量删除方法
    batchRemove() {
      if (this.selectObjects.length != 0) {
        //1.给出提示是否确定删除
        this.$confirm('确定删除该角色吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(() => {
          //转换复选对象数组提取id
          for(let i=0;i<this.selectObjects.length;i++){
            let id=this.selectObjects[i].id;
            this.IdArr.push(id);
          }
          //3.确定发送请求执行批量删除
          api.batchUserByIds(this.IdArr).then((res) => {
            if (res.code===200) {
              this.$message.success('批量删除成功！');
              // 刷新数据
              this.fetchUserData();
            } else {
              this.$message.error('删除失败，请重试～');
            }
            // 最后清空已选数据
            this.selectObjects=[];
            this.IdArr=[];
          })
        })
      }else{
        this.$message.error("没有选择任何数据，请先选择任意一条数据");
      }
    },
    //分配角色按钮事件
    showAssignRole(row){
      this.dialogRoleVisible=true;
      this.sysUser=row;
      //1.获取已分配角色
      roleApi.getUserRoleByIds(this.sysUser.id).then((res)=>{
        console.log(res);
        if(res.code===200){
          //2.将获取角色渲染
          this.allRoles=res.data.allRoles;
          console.log(this.allRoles);
          this.userRoleIds=res.data.userRoleIds;
          console.log(this.userRoleIds);
          this.checkAll = this.userRoleIds.length===this.allRoles.length
          this.isIndeterminate = this.userRoleIds.length>0 && this.userRoleIds.length<this.allRoles.length
        }

      })
    },

    /*
    全选勾选状态发生改变的监听
    */
    handleCheckAllChange (value) {// value 当前勾选状态true/false
      // 如果当前全选, userRoleIds就是所有角色id的数组, 否则是空数组
      this.userRoleIds = value ? this.allRoles.map(item => item.id) : []
      // 如果当前不是全选也不全不选时, 指定为false
      this.isIndeterminate = false
    },

     /*
    角色列表选中项发生改变的监听
    */
    handleCheckedChange (value) {
      const {userRoleIds, allRoles} = this
      this.checkAll = userRoleIds.length === allRoles.length && allRoles.length>0
      this.isIndeterminate = userRoleIds.length>0 && userRoleIds.length<allRoles.length
    },

    //分配角色
    assignRole () {
      let assignRoleVo = {
        userId: this.sysUser.id,
        roleIdList: this.userRoleIds
      }
      roleApi.assignRole(assignRoleVo).then(response => {
        this.$message.success(response.message || '分配角色成功')
        this.dialogRoleVisible = false
        this.fetchUserData()
      })
    },
  },
  created() {
    this.fetchUserData()
  },
}
</script>

<style lang="scss" scoped></style>
