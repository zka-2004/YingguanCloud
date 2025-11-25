<template>
  <div>
    <!-- sand-div 搜索框-->
    <div class="search-div">

      <el-form label-width="70px" size="small">
        <el-row>
          <el-col :span="8">
            <el-form-item label="关 键 字">
              <el-input style="width: 60%" v-model="searchObj.username" clearable placeholder="登录用户"></el-input>
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
    <!-- 数据列表-表格 -->
    <el-table align="center"
    v-loading="listLoading"
      :data="list" stripe border
      style="width: 100%;margin-top: 10px;"
      >
      <!-- 复选框 -->
      <!-- <el-table-column width="40" type="selection" /> -->

      <el-table-column label="序号" width="70" align="center">
        <template slot-scope="scope">
          {{ (page - 1) * limit + scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="username" label="登录账号" align="center" />
      <el-table-column prop="ipaddr" label="登录ip地址" align="center" />
      <el-table-column prop="status" label="登录状态" align="center"/>
      <el-table-column prop="msg" label="提示信息" align="center" />
      <el-table-column prop="createTime" label="登录时间" align="center" />
      <el-table-column label="操作" width="200" align="center">
        <template slot-scope="scope">
          <el-button type="danger" icon="el-icon-delete" size="mini" @click="removeDataById(scope.row.id)" slot="reference" title="删除" />
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页组件 -->
    <el-pagination :current-page="page" :total="total" :page-size="limit" style="padding: 30px 0; text-align: center;"
      layout="total, prev, pager, next, jumper" @current-change="fetchUserData" />
  </div>
</template>

<script>
import api from '@/api/loginLog/loginLog.js';

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
      //登录日志对象
      sysLoginLog: {},
      //查询时间对象
      createTimes: [],
      //定义批量复选删除对象数组
      selectObjects: [],
      //定义批量删除ID数组
      IdArr: [],
    }
  },
  methods: {
    //分页获取登录日志列表
    fetchUserData(page = 1) {
      //改变当前页码
      this.page = page;
      this.searchObj.createTimeBegin=this.createTimes[0];
      this.searchObj.createTimeEnd=this.createTimes[1];
      //1.发送请求
      api.page(this.page, this.limit,this.searchObj).then((res) => {
        // console.log(res);
        if (res.code === 200) {
          this.listLoading = false
          this.list = res.data.records
          this.total = res.data.total
        }
      })
    },
    //重置按钮
    resetData() {
      //清空数据
      this.searchObj = {};
      this.createTimes = [];
      //刷新数据
      this.fetchUserData()
    },
    //删除登录日志方法
    removeDataById(id) {
      //1.给出提示是否确定删除
      this.$confirm('确定删除该日志吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        //2.确定发送请求执行删除
        api.removeById(id).then((res) => {
          if (res.code===200) {
            this.$message.success('删除成功！');
          } else {
            this.$message.error('删除失败，请重试～');
          }
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
  },
  created() {
    this.fetchUserData()
  },
}
</script>

<style lang="scss" scoped></style>
