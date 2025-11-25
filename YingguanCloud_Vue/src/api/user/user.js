import request from '@/utils/request'

const BASE_URl = '/admin/user'

export default {
  // 请求获取用户列表方法
  list(pageindex, pageSize, sysUserQuery) {
    return request({
      url: `${BASE_URl}/page/${pageindex}/${pageSize}`,
      method: 'get',
      params: sysUserQuery
    })
  },
  // 请求添加用户方法
  addUser(sysUser) {
    return request({
      url: `${BASE_URl}/add`,
      method: 'post',
      data: sysUser
    })
  },
  // 请求回显用户方法
  getUserById(id) {
    return request({
      url: `${BASE_URl}/getUserById/${id}`,
      method: 'get',
    })
  },
  // 修改用户
  updateUserById(sysUser) {
    return request({
      url: `${BASE_URl}/updateUser`,
      method: 'post',
      data: sysUser
    })
  },
  // 删除用户
  removeById(id) {
    return request({
      url: `${BASE_URl}/remove/${id}`,
      method: 'delete',
    })
  },
  // 批量删除用户
  batchUserByIds(arr) {
    return request({
      url: `${BASE_URl}/batchUserById`,
      method: 'delete',
      data: arr
    })
  },
  // 修改用户状态
  updateStatus(id, status) {
    return request({
      url: `${BASE_URl}/updateStatus/${id}/${status}`,
      method: 'get'
    })
  }
}
