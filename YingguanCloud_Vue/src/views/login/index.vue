<template>
  <div class="login-container">
    <!-- 电影胶片主题背景（纯CSS实现） -->
    <div class="film-background">
      <div class="film-strip"></div>
      <div class="film-strip"></div>
      <div class="film-strip"></div>
      <div class="film-strip"></div>
      <div class="film-strip"></div>
    </div>

    <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on"
      label-position="left">
      <div class="title-container">
        <h3 class="title">爱观影平台</h3>
        <p class="subtitle">让每一部电影都成为你的故事</p>
      </div>

      <el-form-item prop="username">
        <span class="svg-container">
          <svg-icon icon-class="user" />
        </span>
        <el-input ref="username" v-model="loginForm.username" placeholder="用户名/邮箱" name="username" type="text"
          tabindex="1" auto-complete="on" />
      </el-form-item>

      <el-form-item prop="password">
        <span class="svg-container">
          <svg-icon icon-class="password" />
        </span>
        <el-input :key="passwordType" ref="password" v-model="loginForm.password" :type="passwordType" placeholder="密码"
          name="password" tabindex="2" auto-complete="on" @keyup.enter.native="handleLogin" />
        <span class="show-pwd" @click="showPwd">
          <svg-icon :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'" />
        </span>
      </el-form-item>

      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;"
        @click.native.prevent="handleLogin">登录爱观影</el-button>
      <div class="login-options">
        <el-link type="primary" :underline="false">注册新账号</el-link>
        <el-link type="primary" :underline="false">忘记密码？</el-link>
      </div>
    </el-form>
  </div>
</template>

<script>
import { validUsername } from '@/utils/validate'

export default {
  name: 'Login',
  data() {
    const validateUsername = (rule, value, callback) => {
      callback()
    }
    const validatePassword = (rule, value, callback) => {
      if (value.length < 6) {
        callback(new Error('密码至少6位'))
      } else {
        callback()
      }
    }
    return {
      loginForm: {
        username: 'admin',
        password: '123456'
      },
      loginRules: {
        username: [{ required: true, trigger: 'blur', validator: validateUsername }],
        password: [{ required: true, trigger: 'blur', validator: validatePassword }]
      },
      loading: false,
      passwordType: 'password',
      redirect: undefined
    }
  },
  watch: {
    $route: {
      handler: function (route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    handleLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true
          this.$store.dispatch('user/login', this.loginForm).then(() => {
            const redirectPath = this.redirect && this.redirect !== '/login' ? this.redirect : '/'
            this.$router.push({ path: redirectPath })
            this.loading = false
          }).catch(() => {
            this.loading = false
          })
        } else {
          console.log('error submit!!')
          return false
        }
      })
    }
  }
}
</script>

<style lang="scss">
/* 修复input 背景不协调 和光标变色 */
$bg: #1a2035;
$light_gray: #fff;
$cursor: #ff6b6b;

@supports (-webkit-mask: none) and (not (cater-color: $cursor)) {
  .login-container .el-input input {
    color: $cursor;
  }
}

.login-container {
  .el-input {
    display: inline-block;
    height: 47px;
    width: 85%;

    input {
      background: transparent;
      border: 0px;
      -webkit-appearance: none;
      border-radius: 0px;
      padding: 12px 5px 12px 15px;
      color: $light_gray;
      height: 47px;
      caret-color: $cursor;
      font-family: 'Microsoft YaHei', Arial, sans-serif;

      &:-webkit-autofill {
        box-shadow: 0 0 0px 1000px $bg inset !important;
        -webkit-text-fill-color: $cursor !important;
      }
    }
  }

  .el-form-item {
    border: 1px solid rgba(255, 255, 255, 0.1);
    background: rgba(0, 0, 0, 0.2);
    border-radius: 10px;
    color: #f5f5f5;
    margin-bottom: 20px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  }
}
</style>

<style lang="scss" scoped>
$bg: #1a2035;
$dark_gray: #e0e0e0;
$light_gray: #ff6b6b;
$secondary_color: #4a6fa5;

.login-container {
  min-height: 100%;
  width: 100%;
  background-color: $bg;
  overflow: hidden;
  position: relative;

  /* 电影胶片背景（纯CSS实现） */
  .film-background {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: -1;

    .film-strip {
      position: absolute;
      width: 100%;
      height: 20%;
      background: linear-gradient(rgba(0, 0, 0, 0.2), rgba(0, 0, 0, 0.1));
      border-top: 1px solid rgba(255, 255, 255, 0.05);
      border-bottom: 1px solid rgba(255, 255, 255, 0.05);

      &:nth-child(1) {
        top: 0;
      }

      &:nth-child(2) {
        top: 20%;
      }

      &:nth-child(3) {
        top: 40%;
      }

      &:nth-child(4) {
        top: 60%;
      }

      &:nth-child(5) {
        top: 80%;
      }

      &::before {
        content: '';
        position: absolute;
        top: 50%;
        left: 0;
        width: 100%;
        height: 2px;
        background: linear-gradient(to right, transparent, $light_gray, transparent);
        transform: translateY(-50%);
      }
    }
  }

  .login-form {
    position: relative;
    width: 520px;
    max-width: 100%;
    padding: 160px 35px 0;
    margin: 0 auto;
    overflow: hidden;
    background: rgba(25, 30, 55, 0.85);
    border-radius: 15px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
    backdrop-filter: blur(5px);
    border: 1px solid rgba(255, 255, 255, 0.1);

    &::before {
      content: '';
      position: absolute;
      top: -50%;
      left: -50%;
      width: 200%;
      height: 200%;
      background: radial-gradient(circle, transparent 20%, rgba(255, 255, 255, 0.05) 70%);
      z-index: -1;
    }
  }

  .tips {
    font-size: 14px;
    color: $dark_gray;
    margin-bottom: 20px;

    span {
      &:first-of-type {
        margin-right: 16px;
      }
    }
  }

  .title-container {
    position: relative;
    margin-bottom: 40px;

    .title {
      font-size: 28px;
      color: $light_gray;
      margin: 0px 0 15px 0;
      text-align: center;
      font-weight: 600;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
      letter-spacing: 1px;
    }

    .subtitle {
      font-size: 16px;
      color: $secondary_color;
      text-align: center;
      margin: 0;
      font-weight: 300;
      opacity: 0.8;
    }
  }

  .svg-container {
    padding: 6px 5px 6px 15px;
    color: $secondary_color;
    vertical-align: middle;
    width: 30px;
    display: inline-block;
    font-size: 18px;
  }

  .show-pwd {
    position: absolute;
    right: 10px;
    top: 7px;
    font-size: 16px;
    color: $secondary_color;
    cursor: pointer;
    user-select: none;
    transition: color 0.3s;

    &:hover {
      color: $light_gray;
    }
  }

  .login-options {
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
    color: $dark_gray;

    .el-link {
      font-size: 14px;
      text-decoration: none;
      transition: color 0.3s;

      &:hover {
        color: $light_gray;
      }
    }
  }
}
</style>
