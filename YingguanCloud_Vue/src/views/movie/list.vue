<template>
  <div class="movie-container">
    <!-- 查询表单 -->
    <div class="search-box">
      <el-form :model="searchObj" :inline="true" size="small" class="search-form">
        <el-form-item label="影视名称">
          <el-input v-model="searchObj.name" placeholder="影视名称" clearable
            @keyup.enter.native="fetchPageList"></el-input>
        </el-form-item>

        <el-form-item label="影视类型">
          <el-select v-model="searchObj.cid" placeholder="请选择类型" clearable>
            <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="fetchPageList">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetData">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 工具条 -->
    <div class="toolbar">
      <el-button type="success" icon="el-icon-plus" @click="add" size="medium">添加影视</el-button>
    </div>

    <!-- 添加/修改对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="550px" class="movie-dialog" v-loading="loading"
      element-loading-text="文件上传中...">
      <el-form ref="dataForm" :model="sysMovie" label-width="120px" :rules="rules" size="small">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="影视名称" prop="name">
              <el-input v-model="sysMovie.name" placeholder="请输入影视名称"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="所属栏目" prop="cid">
              <el-select v-model="sysMovie.cid" placeholder="请选择类型" style="width: 100%">
                <el-option v-for="item in categoryList" :key="item.id" :label="item.name" :value="item.id"></el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="关键字" prop="keyword">
              <el-input v-model="sysMovie.keyword" placeholder="请输入关键字"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="导演" prop="director">
              <el-input v-model="sysMovie.director" placeholder="请输入导演"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="演员" prop="actor">
              <el-input v-model="sysMovie.actor" placeholder="请输入演员"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="影视图片" prop="image">
              <div class="upload-container">
                <el-upload class="upload-demo" name="uploadImage"
                  action="http://localhost:8080/admin/system/upload/uploadImage" :limit="1"
                  :on-success="handleImageSuccess" :before-upload="handleBeforeUpload" :show-file-list="false">
                  <el-button size="small" type="primary" icon="el-icon-upload2">上传图片</el-button>
                  <div class="upload-tip">支持JPG/PNG格式，建议尺寸1200*630</div>
                </el-upload>
                <div v-if="sysMovie.image" class="image-preview">
                  <img :src="sysMovie.image" alt="影视图片" />
                </div>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="影视文件" prop="video">
              <div class="upload-container">
                <el-upload class="upload-demo" name="uploadVideo"
                  action="http://localhost:8080/admin/system/upload/uploadVideo" :limit="1"
                  :on-success="handleVideoSuccess" :before-upload="handleBeforeUpload" :show-file-list="false">
                  <el-button size="small" type="primary" icon="el-icon-upload2">上传视频</el-button>
                  <div class="upload-tip">支持MP4格式，建议分辨率720P</div>
                </el-upload>
                <div v-if="sysMovie.playId" class="video-preview">
                  <span class="video-name">{{ sysMovie.playId.split('/').pop() }}</span>
                </div>
              </div>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="影视描述" prop="description">
              <el-input v-model="sysMovie.description" type="textarea" :rows="4" placeholder="请输入影视描述"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false" size="small">取消</el-button>
        <el-button type="primary" @click="saveOrUpdate" size="small">确定</el-button>
      </span>
    </el-dialog>

    <!-- 表格 -->
    <el-table v-loading="listLoading" :data="list" stripe border style="width: 100%; margin-top: 20px"
      :header-cell-style="{ background: '#1a2035', color: '#e0e0e0' }">
      <el-table-column type="index" label="序号" width="70" align="center" />

      <el-table-column prop="name" label="影视名称" align="center" show-overflow-tooltip />

      <el-table-column prop="categoryName" label="影视类型" width="120" align="center" />

      <el-table-column prop="director" label="导演" width="120" align="center" />

      <el-table-column label="剧照" width="120" align="center">
        <template slot-scope="scope">
          <viewer v-if="scope.row.image">
            <img :src="scope.row.image" alt="剧照" style="width: 100px; height: 60px; object-fit: cover" />
          </viewer>
          <span v-else class="no-image">无</span>
        </template>
      </el-table-column>

      <el-table-column label="操作" width="300" align="center">
        <template slot-scope="scope">
          <el-button type="primary" icon="el-icon-edit" size="small" @click="edit(scope.row.id)"
            title="修改">修改</el-button>
          <el-button type="danger" icon="el-icon-delete" size="small" @click="removeDataById(scope.row.id)"
            title="删除">删除</el-button>
          <el-button type="warning" icon="el-icon-video-camera" size="small" @click="showPlayPage(scope.row)"
            title="播放视频">播放</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <el-pagination :current-page="page" :total="total" :page-size="limit" style="margin-top: 25px; text-align: center"
      layout="total, prev, pager, next, jumper" @current-change="fetchPageList" />
  </div>
</template>

<script>
import api from '@/api/movie/movie.js'
import categoryApi from '@/api/category/category.js'

export default {
  data() {
    return {
      // 查询对象
      searchObj: {},
      // 是否加载提示
      listLoading: false,
      // 文件上传提示
      loading: false,
      // 表格数据源
      list: [],
      // 当前页
      page: 1,
      // 每页显示数
      limit: 10,
      // 总记录数
      total: 0,
      // 是否显示添加/修改对话框
      dialogVisible: false,
      // 对话框显示数据对象
      sysMovie: {},
      // 分类数据数组
      categoryList: [],
      // 添加数据校验规则
      rules: {
        name: [
          { required: true, message: '请输入影视名称', trigger: 'blur' }
        ],
        cid: [
          { required: true, message: '请选择影视类型', trigger: 'blur' }
        ],
        keyword: [
          { required: true, message: '请输入影视关键字', trigger: 'blur' }
        ],
        director: [
          { required: true, message: '请输入导演名称', trigger: 'blur' }
        ]
      },
      // 对话框标题
      dialogTitle: '添加影视'
    }
  },
  methods: {
    // 获取数据源方法
    fetchPageList(page = 1) {
      this.listLoading = true;
      this.page = page;
      // 1.发送请求
      api.list(this.page, this.limit, this.searchObj).then((response) => {
        this.listLoading = false;
        if (response.code === 200) {
          this.list = response.data.records;
          this.total = response.data.total;
        }
      }).catch(error => {
        this.listLoading = false;
      })
    },

    // 重置按钮事件
    resetData() {
      this.searchObj = {};
      this.fetchPageList();
    },

    // 添加按钮事件
    add() {
      this.dialogVisible = true;
      this.sysMovie = {};
      this.dialogTitle = '添加影视';
      categoryApi.list().then(response => {
        this.categoryList = response.data;
      });
    },

    // 确定按钮事件
    saveOrUpdate() {
      this.$refs.dataForm.validate((valid) => {
        if (valid) {
          if (this.sysMovie.id) {
            // 修改
            this.updateMovie();
          } else {
            // 添加
            this.addMovie();
          }
        } else {
          this.$message.error('请填写完整的数据！');
          return false;
        }
      });
    },

    // 添加的具体方法
    addMovie() {
      api.addMovie(this.sysMovie).then((response) => {
        if (response.code === 200) {
          this.$message.success('添加成功！');
          this.dialogVisible = false;
          this.fetchPageList();
        }
      });
    },

    // 修改的具体方法
    updateMovie() {
      api.updateMovieByID(this.sysMovie).then((response) => {
        if (response.code === 200) {
          this.$message.success('修改成功！');
          this.dialogVisible = false;
          this.fetchPageList();
        }
      });
    },

    // 修改按钮事件
    edit(id) {
      this.dialogVisible = true;
      this.dialogTitle = '修改影视';
      // 1.发送回显请求
      api.getMovieById(id).then((res) => {
        this.sysMovie = res.data;
      });
    },

    // 文件开始上传，开始屏幕遮罩
    handleBeforeUpload() {
      this.loading = true;
    },

    // 图片上传成功的钩子函数
    handleImageSuccess(res, file) {
      this.loading = false;
      if (res.code === 200) {
        this.sysMovie.image = res.data;
        this.$message.success('图片上传成功！');
      } else {
        this.$message.error('图片上传失败！');
      }
    },

    // 视频上传成功的钩子函数
    handleVideoSuccess(res, file) {
      this.loading = false;
      if (res.code === 200) {
        this.sysMovie.playId = res.data;
        this.$message.success('视频上传成功！');
      } else {
        this.$message.error('视频上传失败！');
      }
    },

    // 删除按钮事件
    removeDataById(id) {
      this.$confirm('此操作将永久删除该影视, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        api.removeById(id).then((response) => {
          if (response.code === 200) {
            this.$message.success('删除成功！');
            this.fetchPageList();
          }
        });
      });
    },

    // 播放视频按钮事件
    showPlayPage(row) {
      this.$router.push('/system/assignVideo?id=' + row.id);
    },
  },
  created() {
    this.fetchPageList();
    categoryApi.list().then(response => {
      this.categoryList = response.data;
    });
  }
}
</script>

<style lang="scss" scoped>
.movie-container {
  padding: 20px;
  background: #1a2035;
  border-radius: 10px;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.3);
  min-height: 100vh;

  .search-box {
    background: rgba(25, 30, 55, 0.8);
    border-radius: 8px;
    padding: 15px;
    margin-bottom: 20px;
    border: 1px solid rgba(255, 255, 255, 0.1);

    .search-form {
      .el-form-item {
        margin-right: 15px;
        margin-bottom: 0;
      }
    }
  }

  .toolbar {
    margin-bottom: 20px;
    display: flex;
    justify-content: flex-end;
  }

  .movie-dialog {
    .el-dialog__header {
      background: linear-gradient(90deg, #1a2035, #2a3550);
      border-bottom: 1px solid rgba(255, 255, 255, 0.1);
      padding: 15px 20px;

      .el-dialog__title {
        color: #ff6b6b;
        font-weight: 600;
        font-size: 18px;
      }
    }

    .el-dialog__body {
      padding: 20px 30px;

      .el-form-item {
        margin-bottom: 20px;

        .el-input__inner {
          background: rgba(25, 30, 55, 0.7);
          border: 1px solid rgba(255, 255, 255, 0.1);
          color: #e0e0e0;
        }

        .el-select .el-input__inner {
          background: rgba(25, 30, 55, 0.7);
          border: 1px solid rgba(255, 255, 255, 0.1);
          color: #e0e0e0;
        }

        .el-textarea__inner {
          background: rgba(25, 30, 55, 0.7);
          border: 1px solid rgba(255, 255, 255, 0.1);
          color: #e0e0e0;
        }
      }

      .upload-container {
        position: relative;

        .upload-tip {
          font-size: 12px;
          color: #8c939d;
          margin-top: 5px;
        }

        .image-preview {
          margin-top: 10px;

          img {
            width: 100%;
            height: 80px;
            border-radius: 4px;
            object-fit: cover;
          }
        }

        .video-preview {
          margin-top: 10px;
          padding: 8px;
          background: rgba(255, 255, 255, 0.05);
          border-radius: 4px;
          font-size: 13px;
          color: #e0e0e0;

          .video-name {
            color: #ff6b6b;
            font-weight: 500;
          }
        }
      }
    }

    .el-dialog__footer {
      padding: 0 30px 20px;
      text-align: right;

      .el-button {
        padding: 8px 20px;
        margin-left: 10px;
      }

      .el-button--primary {
        background: linear-gradient(90deg, #ff6b6b, #ff8e8e);
        border: none;
        box-shadow: 0 2px 8px rgba(255, 107, 107, 0.4);
      }
    }
  }

  .el-table {
    background: rgba(25, 30, 55, 0.8);
    border: 1px solid rgba(255, 255, 255, 0.1);
    border-radius: 8px;

    .el-table__header-wrapper {
      th {
        background: rgba(25, 30, 55, 0.7) !important;
        color: #ff6b6b !important;
        font-weight: 600;
        border-bottom: 1px solid rgba(255, 255, 255, 0.1);
      }
    }

    .el-table__body {
      td {
        border-bottom: 1px solid rgba(255, 255, 255, 0.05);
      }
    }

    .no-image {
      color: #8c939d;
      font-style: italic;
    }
  }

  .el-pagination {

    .el-pagination__total,
    .el-pagination__jump {
      color: #e0e0e0;
    }

    .el-pagination__editor input {
      background: rgba(25, 30, 55, 0.7);
      border: 1px solid rgba(255, 255, 255, 0.1);
      color: #e0e0e0;
    }

    .el-pager li {
      background: rgba(25, 30, 55, 0.7);
      border: 1px solid rgba(255, 255, 255, 0.1);
      color: #e0e0e0;

      &.active {
        background: #ff6b6b;
        color: #fff;
      }
    }
  }
}
</style>
