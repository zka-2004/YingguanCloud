import request from '@/utils/request'

const BASE_URl = '/admin/menu'

export default {
  // 请求获取菜单列表方法
  findList() {
    return request({
      url: `${BASE_URl}/findList`,
      method: 'get'
    })
  },
  // 请求添加菜单方法
  addMenu(sysMenu) {
    return request({
      url: `${BASE_URl}/add`,
      method: 'post',
      data: sysMenu
    })
  },
  // 请求回显菜单方法
  getMenuById(id) {
    return request({
      url: `${BASE_URl}/getMenuById/${id}`,
      method: 'get'
    })
  },
  // 修改菜单
  updateUserById(sysMenu) {
    return request({
      url: `${BASE_URl}/update`,
      method: 'post',
      data: sysMenu
    })
  },
  // 删除菜单
  removeById(id) {
    return request({
      url: `${BASE_URl}/removeMenu/${id}`,
      method: 'delete'
    })
  },
  // 批量删除菜单
  batchUserByIds(arr) {
    return request({
      url: `${BASE_URl}/batchUserById`,
      method: 'delete',
      data: arr
    })
  },
  // 修改菜单状态
  updateStatus(id, status) {
    return request({
      url: `${BASE_URl}/updateStatus/${id}/${status}`,
      method: 'get'
    })
  },
  // 查看某个角色的菜单权限列表
  toAssign(id) {
    return request({
      url: `${BASE_URl}/toAssign/${id}`,
      method: 'get'
    })
  },
  // 给某个角色分配菜单权限列表
  doAssign(AssignMenuVo) {
    return request({
      url: `${BASE_URl}/doAssign`,
      method: 'post',
      data: AssignMenuVo
    })
  }
}
