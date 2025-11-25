import request from '@/utils/request'

const BASE_URL = '/admin/loginLog'

export default {
  // 请求获取登录日志方法
  page(page, limit, loginLogQuery) {
    return request({
      url: `${BASE_URL}/page/${page}/${limit}`,
      method: 'get',
      params: loginLogQuery
    })
  },
  // 请求获取登录日志方法
  list() {
    return request({
      url: `${BASE_URL}/list`,
      method: 'get'
    })
  },
  removeById(id) {
    return request({
      url: `${BASE_URL}/remove/${id}`,
      method: 'delete'
    })
  },
}
