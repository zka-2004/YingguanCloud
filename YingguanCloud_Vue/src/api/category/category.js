import request from '@/utils/request'

const BASE_URL = '/admin/category'

export default {
  // 请求获取影视列表方法
  page(page, limit, sysCategoryQuery) {
    return request({
      url: `${BASE_URL}/page/${page}/${limit}`,
      method: 'get',
      params: sysCategoryQuery
    })
  },
  // 请求获取影视列表方法
  list() {
    return request({
      url: `${BASE_URL}/list`,
      method: 'get'
    })
  },
  saveCategory(sysCategory) {
    return request({
      url: `${BASE_URL}/add`,
      method: 'post',
      data: sysCategory
    })
  },
  updateCategory(sysCategory) {
    return request({
      url: `${BASE_URL}/update`,
      method: 'post',
      data: sysCategory
    })
  },
  selectCategoryById(id) {
    return request({
      url: `${BASE_URL}/getById/${id}`,
      method: 'get'
    })
  },
  deleteCategory(id) {
    return request({
      url: `${BASE_URL}/remove/${id}`,
      method: 'delete'
    })
  }
}
