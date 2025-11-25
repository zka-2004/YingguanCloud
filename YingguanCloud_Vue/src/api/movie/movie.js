import request from '@/utils/request'

const BASE_URL = '/admin/movie'

export default {
  // 请求获取影视列表方法
  list(page, limit, sysMovieQuery) {
    return request({
      url: `${BASE_URL}/page/${page}/${limit}`,
      method: 'get',
      params: sysMovieQuery
    })
  },
  // 请求获取影视列表方法
  addMovie(sysMovie) {
    return request({
      url: `${BASE_URL}/addMovie`,
      method: 'post',
      data: sysMovie
    })
  },
  // 根据ID回显影视信息
  getMovieById(id) {
    return request({
      url: `${BASE_URL}/getMovieById/${id}`,
      method: 'get'
    })
  },
  updateMovieByID(sysMovie) {
    return request({
      url: `${BASE_URL}/updateMovie`,
      method: 'post',
      data: sysMovie
    })
  },
  // 删除影视信息
  removeById(id) {
    return request({
      url: `${BASE_URL}/deleteMovieById/${id}`,
      method: 'delete'
    })
  },
  // 根据id获取palyAuth播放凭证
  getPlayAuth(id) {
    return request({
      url: `${BASE_URL}/getPlayAuthById/${id}`,
      method: 'get'
    })
  },
}
