import request from '@/utils/request'

// 查询进出账明细列表
export function listDetail(query) {
  return request({
    url: '/oa/detail/list',
    method: 'get',
    params: query
  })
}

// 查询进出账明细详细
export function getDetail(detailId) {
  return request({
    url: '/oa/detail/' + detailId,
    method: 'get'
  })
}

// 新增进出账明细
export function addDetail(data) {
  return request({
    url: '/oa/detail',
    method: 'post',
    data: data
  })
}

// 修改进出账明细
export function updateDetail(data) {
  return request({
    url: '/oa/detail',
    method: 'put',
    data: data
  })
}

// 删除进出账明细
export function delDetail(detailId) {
  return request({
    url: '/oa/detail/' + detailId,
    method: 'delete'
  })
}
