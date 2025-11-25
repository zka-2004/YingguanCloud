import request from '@/utils/request'

const BASE_URl = '/admin/Role'

export default {
  // 请求获取角色列表方法
  list(pageindex, pageSize, sysRoleQuery) {
    return request({
      url: `${BASE_URl}/page/${pageindex}/${pageSize}`,
      method: 'get',
      params: sysRoleQuery
    })
  },
  // 请求添加角色方法
  addRole(sysRole) {
    return request({
      url: `${BASE_URl}/add`,
      method: 'post',
      data: sysRole
    })
  },
  // 请求回显角色方法
  getRoleById(id) {
    return request({
      url: `${BASE_URl}/getRoleById/${id}`,
      method: 'get',
    })
  },
  // 修改角色
  updateRole(sysRole) {
    return request({
      url: `${BASE_URl}/updateRole`,
      method: 'post',
      data: sysRole
    })
  },
  // 删除角色
  removeById(id) {
    return request({
      url: `${BASE_URl}/remove/${id}`,
      method: 'delete',
    })
  },
  // 批量删除角色
  batchRemoveByIds(arr) {
    return request({
      url: `${BASE_URl}/batchRemove`,
      method: 'delete',
      data: arr
    })
  },
  // 根据用户id查询用户已分配的角色
  getUserRoleByIds(id) {
    return request({
      url: `${BASE_URl}/toAssign/${id}`,
      method: 'get'
    })
  },
  // 给用户分配角色
  assignRole(assignRoleVo) {
    return request({
      url: `${BASE_URl}/doAssign`,
      method: 'post',
      data: assignRoleVo
    })
  }
}
