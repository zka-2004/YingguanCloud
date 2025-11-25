<template>
    <div>
        <br>
        <!-- 查询表单 -->
        <el-form label-width="70px" size="small">
            <el-row>
                <el-col :span="24">
                    <el-form-item label="分类名称">
                        <el-input v-model="searchObj.name" style="width: 200px" clearable placeholder="分类名称" />
                        &nbsp;
                        <el-button type="primary" icon="el-icon-search" size="mini" @click="searchCategory()">搜
                            索</el-button>
                        <el-button icon="el-icon-refresh" size="mini" @click="resetData">重 置</el-button>
                        <el-button type="success" icon="el-icon-plus" size="mini" @click="openWindow()">添 加</el-button>
                    </el-form-item>
                </el-col>
            </el-row>
        </el-form>
        <!-- 添加菜单 -->
        <el-dialog title="添加/修改" :visible.sync="dialogAddVisible" width="40%">
            <el-form ref="dataForm" :model="sysCategory" label-width="150px" size="small" style="padding-right: 40px;">
                <el-form-item label="分类名称">
                    <el-input v-model="sysCategory.name" />
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button size="small" icon="el-icon-refresh-right" @click="dialogAddVisible = false">取 消</el-button>
                <el-button type="primary" icon="el-icon-check" size="small" @click="saveOrUpdate()">确 定</el-button>
            </span>
        </el-dialog>
        <!-- 表格 -->
        <el-table v-loading="listLoading" :data="list" stripe border style="width: 100%; margin-top: 10px;">

            <el-table-column label="序号" width="70" align="center">
                <template slot-scope="scope">
                    {{ (page - 1) * limit + scope.$index + 1 }}
                </template>
            </el-table-column>
            <el-table-column prop="name" label="分类名称" align="center" />
            <el-table-column prop="createTime" label="创建时间" width="400" align="center" />
            <el-table-column prop="updateTime" label="更新时间" width="400" align="center" />
            <el-table-column label="操作" width="200" align="center">
                <template slot-scope="scope">
                    <el-button type="primary" icon="el-icon-edit" size="mini" title="修改" @click="edit(scope.row.id)" />
                    <el-button
                        type="danger"
                        icon="el-icon-delete"
                        size="mini"
                        title="删除"
                        @click="removeDataById(scope.row.id)"
                    />
                </template>
            </el-table-column>
        </el-table>
        <div class="block">
            <el-pagination
                :page-size="limit"
                :page-sizes="[5, 10, 15, 20]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
                align="center"
                @current-change="fetchData"
                @size-change="handleSizeChange"
            />
        </div>
    </div>
</template>
<script>
import api from '@/api/category/category.js';
export default {
  data() {
    return {
      list: [],
      total: 0,
      page: 1,
      limit: 5,
      searchObj: {},
      sysCategory: {},
      listLoading: true,
      dialogAddVisible: false
    };
  },
  created() {
    this.fetchData();
  },
  methods: {
    fetchData(page = 1) {
      this.listLoading = true;
      this.page = page;
      api.page(this.page, this.limit, this.searchObj).then(response => {
        // console.log(response);
        this.listLoading = false;
        this.list = response.data.records;
        this.total = response.data.total;
      });
    },
    // 重置页码
    handleSizeChange(newLimit) {
      this.limit = newLimit;
      this.fetchData();
    },
    // 搜索
    searchCategory() {
      this.fetchData();
    },
    // 重置
    resetData() {
      this.searchObj = {};
      this.fetchData();
    },
    // 打开添加框
    openWindow() {
      this.dialogAddVisible = true;
      this.sysCategory = {};
    },
    saveOrUpdate() {
      if (this.sysCategory.id == null) {
        this.addCategory();
      } else {
        this.updateCategory();
        this.dialogAddVisible = false;
      }
    },
    // 添加
    addCategory() {
      api.saveCategory(this.sysCategory).then(response => {
        this.$message({
          type: 'success',
          message: '添加成功'
        });
        this.sysCategory = {};
        this.dialogAddVisible = false;
        this.fetchData(1);
      });
    },
    // 编辑
    updateCategory() {
      api.updateCategory(this.sysCategory).then(response => {
        this.$message({
          type: 'success',
          message: '修改成功!'
        });
        this.fetchData(1);
        this.dialogAddVisible = false;
      });
    },
    // 查询分类
    edit(id) {
      api.selectCategoryById(id).then(response => {
        console.log(response);
        this.dialogAddVisible = true;
        this.sysCategory = response.data;
      });
    },
    // 根据id删除一个分类
    removeDataById(id) {
      this.$confirm('您确定删除该分类吗?, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        return api.deleteCategory(id);
      }).then(() => {
        this.$message.success('删除成功!');
        this.fetchData(1);
      });
    }
  }
};

</script>

<style></style>
